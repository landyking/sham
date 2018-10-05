package app.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.util.Map;

/**
 * Created by landy on 2017/11/23.
 */
public class QuickJson {
    public static final Module JSON_MODULE = initJsonModule();
    public static final ObjectMapper JSON = initObjectMapper();

    private static Module initJsonModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(Long.class, ToStringSerializer.instance);
        return module;
    }

    private static ObjectMapper initObjectMapper() {
        ObjectMapper rst = new ObjectMapper();
        rst.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        rst.registerModule(JSON_MODULE);
//        DeserializationConfig with = rst.getDeserializationConfig().with(new CustomJsonNodeFactory());
//        rst.setConfig(with);
        return rst;
    }

    public static ObjectNode newObject() {
        return JSON.createObjectNode();
    }

    public static ArrayNode newArray() {
        return JSON.createArrayNode();
    }


    public static JsonNode convertParameterMapToJsonObject(Map<String, String[]> parameterMap) {
        ObjectNode rst = newObject();
        for (Map.Entry<String, String[]> one : parameterMap.entrySet()) {
            String name = one.getKey();
            String[] value = one.getValue();
            if (value != null && value.length > 0) {
                if (value.length == 1) {
                    if (!"null".equals(value[0])) {
                        rst.put(name, value[0]);
                    }

                } else {
                    ArrayNode arr = rst.putArray(name);
                    for (String tmp : value) {
                        arr.add(tmp);
                    }
                }
            }
        }
        return rst;
    }

    public static void fillArray(ArrayNode pics, String text) {
        if (!Texts.hasText(text)) {
            return;
        }
        Iterable<String> picsTmp = Splitter.on(",").omitEmptyStrings().split(text);
        for (String s : picsTmp) {
            pics.add(s);
        }
    }
    public static void fillArray(ArrayNode pics, String text , String prefix) {
        if (!Texts.hasText(text)) {
            return;
        }
        Iterable<String> picsTmp = Splitter.on(",").omitEmptyStrings().split(text);
        for (String s : picsTmp) {
            if (prefix != null) {
                pics.add(prefix + s);
            } else {
                pics.add(s);
            }

        }
    }

    public static Tuple<Boolean, String> validJsonFormat(String styleOption) {
        try {
            JsonNode jsonNode = JSON.readTree(styleOption);
            return Tuple.newOne(true, "success");
        } catch (Exception e) {
            return Tuple.newOne(false, e.getMessage());
        }
    }

    public static String toJsonString(Object dep) {
        if (dep == null) {
            return "";
        }
        String json = null;
        try {
            json = JSON.writeValueAsString(dep);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
        return json;
    }
    public static <T>T  jsonToObject(String jsonStr, Class<T> type) {
        T obj = null;
        try {
            obj = JSON.readValue(jsonStr, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("类型转换错误");
        }

        return obj;
    }
}
