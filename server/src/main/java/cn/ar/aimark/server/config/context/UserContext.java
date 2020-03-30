package cn.ar.aimark.server.config.context;


import cn.asr.appframework.utility.context.ExecutionContext;

/**
 * 用户上下文
 *
 * @author yunjian.bian
 */
public class UserContext extends ExecutionContext {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String IP = "ip";

    public static String getUserId() {
        return get(USER_ID);
    }

    public static void setUserId(String userId) {
        put(USER_ID, userId);
    }

    public static String getUserName() {
        return get(USER_NAME);
    }

    public static void setUserName(String userName) {
        put(USER_NAME, userName);
    }


    public static String getAccessToken() {
        return get(ACCESS_TOKEN);
    }

    public static void setAccessToken(String token) {
        put(ACCESS_TOKEN, token);
    }

    public static String getIP() {
        return get(IP);
    }

    public static void setIP(String ip) {
        put(IP, ip);
    }

}
