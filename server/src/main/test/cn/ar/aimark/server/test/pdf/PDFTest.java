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


        String pdfPath = "/home/byj/Pictures/DGF_(S)ROBERT BOSCH GMBH(C)BOSCH AUTOMOTIVE PRODUCTS_1QK0062.pdf";
        String imgDir = "/home/byj/Pictures/DGF_(S)ROBERT BOSCH GMBH(C)BOSCH AUTOMOTIVE PRODUCTS_1QK0062";
        String imgNamePrefix = "";
        PdfToImage.pdfToMultipleImage(pdfPath, imgDir, imgNamePrefix);

    }
}
