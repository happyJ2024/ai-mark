package cn.airesearch.aimarkserver.tool;

import cn.airesearch.aimarkserver.exception.PdfOperationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author ZhangXi
 */
@Slf4j
class PdfToolTest {

    @Test
    void testSavePdfToImages() throws PdfOperationException {
//        String pdfPath = PdfToolTest.class.getResource("/pdf_2.pdf").toString().substring(6);
        String pdfPath = "E:\\files\\aimark\\big_size.pdf";
        String imgDir = "E:\\files\\aimark\\test\\";
        String prefix = "pdfsss";
        log.info("PDF PATH : {}", pdfPath);
        PdfTool.savePdfToImages(new File(pdfPath), imgDir);
        log.info("pdf转换成功");
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
            String imgDir = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf('.'))+ File.separator;
            File imgDirFile = new File(imgDir);
            if (imgDirFile.exists()) imgDirFile.delete();
            imgDirFile.mkdirs();

            PdfTool.savePdfToImages(new File(pdfPath), imgDir);

        }


    }


}
