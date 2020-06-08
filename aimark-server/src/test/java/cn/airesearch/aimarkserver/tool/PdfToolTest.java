package cn.airesearch.aimarkserver.tool;

import cn.airesearch.aimarkserver.exception.PdfOperationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author ZhangXi
 */
@Slf4j
class PdfToolTest {

    @Test
    void testSavePdfToImages() throws PdfOperationException {
//        String pdfPath = PdfToolTest.class.getResource("/pdf_2.pdf").toString().substring(6);
        String pdfPath = "/home/byj/Project/园区报关中心OCR测试数据/test/full-waybill-invoice-two.pdf";
        String imgDir = "/home/byj/Project/园区报关中心OCR测试数据/test/image/full-waybill-invoice-two";
        String prefix = "pdfsss";
        log.info("PDF PATH : {}", pdfPath);
        PdfTool.savePdfToImages(new File(pdfPath), imgDir);
        log.info("pdf转换成功");
    }

    @Test
    void readPDFObject() throws PdfOperationException, IOException {
//        String pdfPath = PdfToolTest.class.getResource("/pdf_2.pdf").toString().substring(6);
        String pdfPath = "/home/byj/Project/园区报关中心OCR测试数据/test/full-waybill-invoice-two.pdf";
        String signedPdfPath = "/home/byj/Project/园区报关中心OCR测试数据/test/full-waybill-invoice-two_signed.pdf";

        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(pdfPath));

            // 获取页码
            int pages = doc.getNumberOfPages();

//            // 读文本内容
//            PDFTextStripper stripper = new PDFTextStripper();
//            // 设置按顺序输出
//            stripper.setSortByPosition(true);
//            stripper.setStartPage(1);
//            stripper.setEndPage(1);
//            String content = stripper.getText(doc);
//            System.out.println(content);

            PDImageXObject pdImage = PDImageXObject.createFromFile("/home/byj/Downloads/sign.png", doc);
            PDFTextStripperWithPosition pdf = new PDFTextStripperWithPosition(doc);
            PDPageContentStream contentStream = null;
            List<PDFTextObject> list = pdf.getCoordinate(1);
            for (PDFTextObject fs : list) {
                System.out.println(fs.toString());
                PDPage page = doc.getPage(fs.pageNumber-1);
                contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);
                contentStream.drawImage(pdImage, fs.x, fs.y);
                contentStream.close();
            }
            doc.save(signedPdfPath);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
        }

    }


    @Test
    void testSavePdfToImages_Batch() throws PdfOperationException {

        File dir = new File("/home/byj/Downloads/进相");

        File[] files = dir.listFiles();
        for (File f :
                files) {
            if (f.isDirectory()) continue;
            ;

            if (f.getAbsolutePath().endsWith(".pdf") == false) continue;

            String pdfPath = f.getAbsolutePath();
            log.info("PDF PATH : {}", pdfPath);
            String imgDir = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf('.')) + File.separator;
            File imgDirFile = new File(imgDir);
            if (imgDirFile.exists()) imgDirFile.delete();
            imgDirFile.mkdirs();

            PdfTool.savePdfToImages(new File(pdfPath), imgDir);

        }


    }


}
