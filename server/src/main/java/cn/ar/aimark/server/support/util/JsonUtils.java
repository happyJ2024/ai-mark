package cn.ar.aimark.server.support.util;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author ZhangXi
 */
public final class JsonUtils {


    public static String toJsonString(Object object) {
        return JSONObject.toJSONString(object);
    }


    public static <T> T jsonToObject(String json, Class<T> tClass) {
        return JSONObject.parseObject(json, tClass);
    }


}
