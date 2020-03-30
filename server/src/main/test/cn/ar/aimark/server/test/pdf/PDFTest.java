package cn.ar.aimark.server.test.pdf;

import cn.ar.aimark.server.support.pdf.PdfToImage;
import org.apache.pdfbox.pdmodel.font.FontMapper;
import org.apache.pdfbox.pdmodel.font.FontMapperImpl;
import org.apache.pdfbox.pdmodel.font.FontMappers;
import org.junit.Test;


public class PDFTest {

    @Test
    public void testpdfToMultipleImage() {
        FontMapper fm=  FontMappers.instance() ;
        if(fm instanceof FontMapperImpl){
            FontMapperImpl fml=(FontMapperImpl)fm;
            fml.addSubstitute("SimSun,Bold","SimSun");
            fml.addSubstitute("YouYuan,Bold","YouYuan");
        }

        String pdfPath = "/home/byj/Project/园区报关中心OCR2020327/2020327/RBAC_I_20012170-C.pdf";
        String imgDir = "/home/byj/Project/园区报关中心OCR2020327/2020327/image/RBAC_I_20012170-C";
        String imgNamePrefix = "";
        PdfToImage.pdfToMultipleImage(pdfPath, imgDir, imgNamePrefix);

    }
}
