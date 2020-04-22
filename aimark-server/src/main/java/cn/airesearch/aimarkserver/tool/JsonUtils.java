package cn.airesearch.aimarkserver.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author ZhangXi
 */
public final class JsonUtils {


    public static String toJsonString(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static String toJsonStringPrettyFormat(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static <T> T jsonToObject(String json, Class<T> tClass) {
        return JSONObject.parseObject(json, tClass);
    }


}
