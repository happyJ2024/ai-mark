package cn.ar.aimark.server.constant;


import cn.asr.appframework.utility.file.FileUtils;
import cn.asr.appframework.utility.jwt.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * 应用定义常量
 *
 * @author yunjian.bian
 */
public class ApplicationConst {

    @Value("${appconfig.ocr_service_url}")
    public static String OCR_SERVICE_URL = "http://172.16.120.101:10115/yuanbao";

    public static String HEADER_Authorization_Key = "Authorization";
    public static String HEADER_ServerDomain_Key = "ServerDomain";
    public static String HEADER_ServerDomain_Value = "cn.ar";

    public static String UploadDataPathPrefix;

    /**
     * 初始化配置
     */
    public static void init() {
        JWTTokenUtils.JWT_TOKEN_KEY = "cn.ar.aimark.server";
        //JWTTokenUtils.JWT_ACCESS_TOKEN_EXPIRE_TIME = 60 * 1000;
        //JWTTokenUtils.JWT_REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000;

        String userDir = System.getProperty("user.dir");
        System.out.println("user.dir:" + userDir);

        UploadDataPathPrefix = userDir + "/uploadData/";
        String testFile = UploadDataPathPrefix + "test.txt";
        FileUtils.checkPath(testFile);
    }

}
