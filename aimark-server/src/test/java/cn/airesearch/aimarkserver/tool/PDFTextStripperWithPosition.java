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
    // 坐标信息集合
    private List<PDFTextObject> pagelist = new ArrayList<>();

    // 有参构造方法
    public PDFTextStripperWithPosition(PDDocument doc) throws IOException {
        super();
        super.setSortByPosition(true);
        this.document = doc;
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
            // text得到pdf这一行中的汉字
             String text = textPositions.get(i).getUnicode();

             if(text.equals("")||text.equals(" "))continue;

            PDFTextObject obj = new PDFTextObject();
            obj.text = text;
            // X坐标
            obj.x = textPositions.get(i).getX();
            // Y坐标
         obj.y = textPositions.get(i).getPageHeight() - textPositions.get(i).getY()+textPositions.get(i).getHeight();

            System.out.println(obj.toString());
            pagelist.add(obj);
        }
    }
}

