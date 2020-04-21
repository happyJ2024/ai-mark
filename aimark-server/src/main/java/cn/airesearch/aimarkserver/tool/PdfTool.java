package cn.airesearch.aimarkserver.tool;

import cn.airesearch.aimarkserver.exception.PdfOperationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangXi
 */
@Slf4j
public final class PdfTool {
    
    private static final int DEFAULT_DPI = 105;
    private static final String DEFAULT_IMG_FORMAT = "jpg";



    public static List<String> savePdfToImages(File file, String imgDir, String imgNamePrefix) throws PdfOperationException {
        if(null == file || !file.exists()) {
            // fixme 抛出异常或许更好
            log.warn("PDF文件：{}不存在", null == file ? "=NULL" : file.getAbsolutePath());
            throw new PdfOperationException("PDF文件不存在");
        }
        // 生成pdf图像
        try {
            PDDocument document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            List<String> names = new ArrayList<>();
            for (int i=0, length = document.getNumberOfPages(); i<length; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
                String name = imgNamePrefix + "_" + (i+1) + "." + DEFAULT_IMG_FORMAT;
                String imgPath = imgDir + name;
                ImageIOUtil.writeImage(bufferedImage, imgPath, DEFAULT_DPI);
                names.add(name);
            }
            return names;
        } catch (IOException e) {
            log.error("PDF文件：{}加载失败", file.getAbsolutePath(), e);
            throw new PdfOperationException("PDF文件加载失败!");
        }
    }


}
