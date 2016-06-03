package com.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtils {
    public static ObjectMapper objectMapper;

    /**
     * 把JavaBean转换为json字符串
     * 
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class) (2)转换为List,如List
     * <Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     * 
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T readValue(String json, Class<T> valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json数组转List
     * 
     * @param json
     * @param valueTypeRef
     * @return
     */
    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取json中的属性值
     * 
     * @param json
     * @param name
     * @return String
     */
    public static String getStrFromJson(String json, String name) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            Map<String, Object> map = objectMapper.readValue(json, Map.class);
            for (Entry<String, Object> extry : map.entrySet()) {
                if (extry.getKey().equals(name)) {
                    return extry.getValue().toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象在标准/普通格式之间转换
     * 
     * @param Object 需要转换的对象
     * @param state 是否标准化
     * @return
     */
    public static String toJSONFormatter(Object obj, boolean state) {
        if (obj == null) {
            return "";
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, state);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
