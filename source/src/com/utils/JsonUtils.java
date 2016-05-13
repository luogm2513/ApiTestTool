package com.utils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
        String uglyJSONString = "";
        try {
            JSONObject jsonObject = new JSONObject(prettyJsonString);
            uglyJSONString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uglyJSONString;
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
     * 从json对象中获取字符串对象
     * 
     * @param jo
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getStrFromJO(JSONObject jo, String name, String defaultValue) {
        String value = defaultValue;
        if (jo.has(name)) {
            try {
                value = jo.getString(name);
                if (StringUtils.isBlank(value) || "null".equals(value)) {
                    value = defaultValue;
                }
            } catch (JSONException e) {
            }
        }
        return value;
    }
}
