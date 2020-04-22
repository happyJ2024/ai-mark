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








}
