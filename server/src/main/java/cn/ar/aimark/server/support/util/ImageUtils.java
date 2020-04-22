package cn.ar.aimark.server.support.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * @author ZhangXi
 */
public class ImageUtils {

    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 将图片文件转为base64编码
     *
     * @param image
     * @param type
     * @return
     */
    public static String transImage2Base64Str(BufferedImage image, String type) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String base64str = null;
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            base64str = encoder.encodeToString(imageBytes);
        } catch (Exception e) {
            log.error("图片转base64字符串失败", e);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                log.error("{} 流关闭失败", ByteArrayOutputStream.class.getName(), e);
            }
        }
        return base64str;
    }

    private static java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
    private static java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

    /**
     *
     * 将base64编码转为图片
     *
     * @param base64String
     * @return
     */
    public static BufferedImage transBase64Str2Image(String base64String) {
        try {
            byte[] bytes1 = decoder.decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            return bi1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void zoomImage() {

    }

    public static String colorToHexValue(Color color) {
        return intToHexValue(color.getAlpha()) + intToHexValue(color.getRed()) + intToHexValue(color.getGreen()) + intToHexValue(color.getBlue());
    }

    public static String intToHexValue(int number) {
        StringBuilder result = new StringBuilder(Integer.toHexString(number & 0xff));
        while (result.length() < 2) {
            result.insert(0, "0");
        }
        return result.toString().toUpperCase();
    }

    public static Color fromStrToRGB(String str) {
        if (str.startsWith("#")) {
            str = str.substring(1);
        }

        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        String str3 = str.substring(4, 6);
        int red = Integer.parseInt(str1, 16);
        int green = Integer.parseInt(str2, 16);
        int blue = Integer.parseInt(str3, 16);
        Color color = new Color(red, green, blue);
        return color;
    }

    public static Color fromStrToARGB(String str) {
        if (str.startsWith("#")) {
            str = str.substring(1);
        }
        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        String str3 = str.substring(4, 6);
        String str4 = str.substring(6, 8);
        int red = Integer.parseInt(str1, 16);
        int green = Integer.parseInt(str2, 16);
        int blue = Integer.parseInt(str3, 16);
        int alpha = Integer.parseInt(str4, 16);
        Color color = new Color(red, green, blue, alpha);
        return color;
    }

    public static BufferedImage clipImage(String imgBase64, int x, int y, int width, int height, String imageExt) {

        try {
            // 根据图片类型获取该种类型的ImageReader
            ImageReader reader = ImageIO.getImageReadersBySuffix(imageExt).next();
            byte[] bytes1 = Base64.decodeBase64(imgBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            ImageInputStream iis = new MemoryCacheImageInputStream(bais);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage image = reader.read(0, param);
            return image;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static BufferedImage clipImage(BufferedImage originImage, int x, int y, int width, int height, String imageExt) {

        try {
            // 根据图片类型获取该种类型的ImageReader
            ImageReader reader = ImageIO.getImageReadersBySuffix(imageExt).next();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(originImage, imageExt, os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());

            ImageInputStream iis = new MemoryCacheImageInputStream(input);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage image = reader.read(0, param);
            return image;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
