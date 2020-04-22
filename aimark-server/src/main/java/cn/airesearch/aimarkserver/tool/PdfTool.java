package cn.airesearch.aimarkserver.tool;

import cn.airesearch.aimarkserver.exception.PdfOperationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.FontMapper;
import org.apache.pdfbox.pdmodel.font.FontMapperImpl;
import org.apache.pdfbox.pdmodel.font.FontMappers;
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

    /**
     * pdf文件转换帮助类
     * <p>
     * 出现的问题：
     * 一些简单的pdf文件转换没有什么问题，但是一些比较复杂的，例如用了少见的字体样式就会出现问题。
     * 出现的问题，例如中文变成方块□，0里有点等等
     * 问题解决：
     * 注意查看日志，如果打印出类似这样的日志（例：Using fallback AdobeSongStd-Light for CID-keyed font STSong-Light），就说明系统没有安装STSong-Light字体，pdfbox使用AdobeSongStd-Light字体来替代了。如果出现方块，就说明没有这种字体，并且替代字体也没有，日志也有相应的其他提示
     * （2）windows安装字体
     * <p>
     *        缺少什么字体去搜索引擎搜索下载对应字体，然后再windows里直接安装ttf/otf等格式结尾的文件即可，或者在系统目录下C:\Windows\Fonts，只需把字体文件拖进来便会提示安装
     * <p>
     * （3）linux下安装字体
     * <p>
     * 通常linux缺少的常用字体，在windows目录下C:\Windows\Fonts都能找到对应的字体文件，拷贝到linux上即可，没有的字体去搜索下载都能找到
     * <p>
     * #cd /usr/share/fonts/   // 进入系统自带的字体目录
     * #mkdir myfonts  // myfonts 是你自己随便取得文件夹名字
     * #将字体文件拷贝到这个文件夹下，在cd /usr/share/fonts/目录下执行以下命令
     * #mkfontscale
     * #mkfontdir
     * #fc-cache -fv           //更新字体缓存
     * #source /etc/profile    // 执行以下命令让字体生效
     * #fc-list    // 查看系统中所有得字体，可用于测试是否安装字体成功
     */

    /**
     * 经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
     */
    private static final int DEFAULT_DPI = 200;

    /**
     * 默认转换的图片格式为jpg
     */
    private static final String DEFAULT_IMG_FORMAT = "jpg";

    static {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        FontMapper fm = FontMappers.instance();
        if (fm instanceof FontMapperImpl) {
            FontMapperImpl fml = (FontMapperImpl) fm;
            fml.addSubstitute("SimSun,Bold", "SimSun");
            fml.addSubstitute("YouYuan,Bold", "YouYuan");
        }
    }

    public static List<String> savePdfToImages(File file, String imgDir) throws PdfOperationException {
        if (null == file || !file.exists()) {
            // fixme 抛出异常或许更好
            log.warn("PDF文件：{}不存在", null == file ? "=NULL" : file.getAbsolutePath());
            throw new PdfOperationException("PDF文件不存在");
        }
        // 生成pdf图像
        try {
            PDDocument document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            List<String> names = new ArrayList<>();
            for (int i = 0, length = document.getNumberOfPages(); i < length; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
                String name = (i + 1) + "." + DEFAULT_IMG_FORMAT;
                String imgPath = imgDir + name;
                ImageIOUtil.writeImage(bufferedImage, imgPath, DEFAULT_DPI);
                names.add(name);
            }
            document.close();
            return names;
        } catch (IOException e) {
            log.error("PDF文件：{}加载失败", file.getAbsolutePath(), e);
            throw new PdfOperationException("PDF文件加载失败!");
        }
    }


    public static void generatePDF(String destFile, List<SplitPDFPage> splitPDFPageList) throws IOException {
        PDDocument outputDocument = new PDDocument();

        for (SplitPDFPage p : splitPDFPageList
        ) {
            PDDocument loadDocument = PDDocument.load(new File(p.getPdfPath()), MemoryUsageSetting.setupTempFileOnly());

            for (Integer pageNumber : p.getPages()) {
                int actualPageNumber = pageNumber - 1;
                outputDocument.addPage(loadDocument.getPage(actualPageNumber));
            }
//            loadDocument.close();
        }

        outputDocument.save(destFile);
        outputDocument.close();


    }
}
