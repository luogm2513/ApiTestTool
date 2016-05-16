package com.utils;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {

    /**
     * json字符串转换成标准格式
     * 
     * @param uglyJSONString
     * @return
     */
    public static String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public static String getUglyJson(String prettyJsonString) {
        if (StringUtils.isBlank(prettyJsonString)) {
            return "";
        }
        JsonObject jsonObject = JsonUtils.fromJson(prettyJsonString, JsonObject.class);
        return jsonObject.toString();
    }

    /**
     * 对象转换成json格式
     * 
     * @param obj
     * @return
     */
    public static String tojson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * 
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * 从json对象中获取字符串对象
     * 
     * @param jo
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getStrFromJO(JsonObject jo, String name, String defaultValue) {
        String value = defaultValue;
        if (jo.has(name)) {
            value = jo.get(name).getAsString();
            if (StringUtils.isBlank(value) || "null".equals(value)) {
                value = defaultValue;
            }
        }
        return value;
    }
}
