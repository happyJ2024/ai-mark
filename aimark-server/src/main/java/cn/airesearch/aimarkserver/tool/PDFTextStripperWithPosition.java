package cn.airesearch.aimarkserver.tool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class PDFTextStripperWithPosition extends PDFTextStripper {


    private final PDDocument document;
    private float scale = 1;
    // 坐标信息集合
    private List<PDFTextObject> pagelist = new ArrayList<>();

    // 有参构造方法
    public PDFTextStripperWithPosition(PDDocument doc, float scale) throws IOException {
        super();
        super.setSortByPosition(true);
        this.document = doc;
        this.scale =scale;
    }

    // 获取坐标信息
    public List<PDFTextObject> getCoordinate(int pageNumber) throws IOException {
        try {

            pagelist.clear();
            super.setSortByPosition(true);
            super.setStartPage(pageNumber);
            super.setEndPage(pageNumber);
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            super.writeText(document, dummy);
            for (PDFTextObject obj : pagelist) {
                obj.pageNumber = pageNumber;
            }

            return pagelist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return pagelist;
    }

    // 获取坐标信息
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (int i = 0; i < textPositions.size(); i++) {
            TextPosition item = textPositions.get(i);
            // text得到pdf这一行中的汉字
            String text = item.getUnicode();

            if (pagelist.size() == 0) {
                if (text.equals("") || text.equals(" ")) {
                    continue;
                }
            }

            PDFTextObject obj = new PDFTextObject();
            obj.text = text;
            // X坐标
            obj.x1 = item.getX();
            obj.x2 = item.getX() + item.getWidth();
            // Y坐标
            float y = item.getY();
            float height = item.getHeight();
//            obj.y1 = item.getPageHeight() - item.getY() + item.getHeight();
//            obj.y2 = item.getPageHeight() - item.getY();

            obj.y1 = y - height;
            obj.y2 = y;

            obj.x1 = obj.x1 * this.scale;
            obj.x2 = obj.x2 * this.scale;
            obj.y1 = obj.y1 * this.scale;
            obj.y2 = obj.y2 * this.scale;
            pagelist.add(obj);
        }
    }
}

