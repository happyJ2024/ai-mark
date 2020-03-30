package cn.ar.aimark.server.constant;


import cn.asr.appframework.utility.file.FileUtils;
import cn.asr.appframework.utility.jwt.JWTTokenUtils;

/**
 * 应用定义常量
 *
 * @author yunjian.bian
 */
public class ApplicationConst {
    public static String HEADER_Authorization_Key = "Authorization";
    public static String HEADER_ServerDomain_Key = "ServerDomain";
    public static String HEADER_ServerDomain_Value = "cn.ar";

    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_PROD = "prod";

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
