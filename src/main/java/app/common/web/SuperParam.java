package app.common.web;

import app.common.DateTimeTool;
import app.common.QuickJson;
import app.common.Texts;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by landy on 2017/11/23.
 */
public class SuperParam {
    public static final String SUPER_PARAM_KEY = ".SUPER_PARAM_KEY";
    private final JsonNode[] container;
    private HttpServletResponse response;
    private HttpServletRequest request;

    public HttpServletResponse getResponse() {
        return response;
    }


    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public SuperParam(JsonNode... obj) {
        Assert.notNull(obj);
        Assert.notEmpty(obj);
        container = obj;
    }

    public <T> T needParam(Map<String, Object> condition, String name, Class<T> type) {
        JsonNode rst = getJsonNode(name);
        Assert.notNull(rst, "param: " + name + " is empty");
        if (type == String.class) {
            Assert.hasText(rst.asText(), "param: " + name + " is empty");
        }
        T tmp = convertResult(type, rst);
        Assert.notNull(tmp, "param: " + name + " is empty");
        if (condition != null) {
            condition.put("name", tmp);
        }
        return tmp;
    }

    public <T> T needParam(String name, Class<T> type) {
        return needParam(null, name, type);
    }

    private <T> T convertResult(Class<T> type, JsonNode rst) {
        if (type == String.class) {
            return (T) rst.asText();
        } else if (type == Long.class) {
            return (T) Longs.tryParse(rst.asText());
        } else if (type == Double.class) {
            return (T) Doubles.tryParse(rst.asText());
        } else if (type == Float.class) {
            return (T) Floats.tryParse(rst.asText());
        } else if (type == Integer.class) {
            String string = rst.asText();
            if (Texts.hasText(string)) {
                if (string.toUpperCase().startsWith("0X")) {
                    Long tl = Longs.tryParse(string.substring(2), 16);
                    if (tl != null) {
                        return (T) new Integer(tl.intValue());
                    } else {
                        return null;
                    }
                } else {
                    return (T) Ints.tryParse(string);
                }
            } else {
                return null;
            }
        } else if (type == Date.class) {
            return (T) DateTimeTool.tryParse(rst.asText());
        } else if (type == Boolean.class) {
            String s = rst.asText("").trim();
            if (s.isEmpty() || s.equals("0") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("off")) {
                return (T) Boolean.FALSE;
            } else {
                return (T) Boolean.TRUE;
            }
        } else {
            throw new IllegalArgumentException("不支持的类型: " + type.getName());
        }
    }
    public <T> T convertResult(Class<T> type) {
        T obj = QuickJson.jsonToObject(container[0].toString(), type);
        return obj;
    }
    public <T> T getParam(Map<String, Object> condition, String name, Class<T> type) {
        JsonNode rst = getJsonNode(name);
        T out = null;
        if (rst != null) {
            out = convertResult(type, rst);
        }
        if (condition != null) {
            condition.put(name, out);
        }
        return out;
    }

    public <T> T getParam(String name, Class<T> type) {
        return getParam(null, name, type);
    }

    public List<SuperParam> getSuperParamList() {
        JsonNode rst = getJsonNodeArray();
        if (rst != null) {
            if (rst.isArray()) {
                List<SuperParam> out = new ArrayList<SuperParam>();
                for (JsonNode one : rst) {
                    out.add(new SuperParam(one));
                }
                return out;
            } else {
                throw new IllegalArgumentException("param not array");
            }
        }
        return Collections.emptyList();
    }

    public List<SuperParam> getSuperParamList(String name) {
        JsonNode rst = getJsonNode(name);
        if (rst != null) {
            if (rst.isArray()) {
                List<SuperParam> out = new ArrayList<SuperParam>();
                for (JsonNode one : rst) {
                    out.add(new SuperParam(one));
                }
                return out;
            } else {
                throw new IllegalArgumentException("param: " + name + " not array");
            }
        }
        return Collections.emptyList();
    }

    public SuperParam getSuperParam(String name) {
        JsonNode tmp = getJsonNode(name);
        if (tmp != null) {
            return new SuperParam(tmp);
        } else {
            return new SuperParam(QuickJson.newObject());
        }
    }

    public <T> List<T> getParamList(String name, Class<T> type) {
        JsonNode rst = getJsonNode(name);
        if (rst != null) {
            if (rst.isArray()) {
                List<T> out = new ArrayList<T>();
                for (JsonNode one : rst) {
                    out.add(convertResult(type, one));
                }
                return out;
            } else {
                throw new IllegalArgumentException("param: " + name + " not array");
            }
        }
        return Collections.emptyList();
    }


    public <T> T getParam(Map<String, Object> condition, String name, Class<T> type, T defVal) {
        JsonNode rst = getJsonNode(name);
        T out = null;
        if (rst == null) {
            out = defVal;
        } else if (type == String.class && !Texts.hasText(rst.asText())) {
            out = defVal;
        } else {
            T t = convertResult(type, rst);
            if (t == null) {
                out = defVal;
            } else {
                out = t;
            }
        }
        if (condition != null) {
            condition.put(name, out);
        }
        return out;
    }

    public <T> T getParam(String name, Class<T> type, T defVal) {
        return getParam(null, name, type, defVal);
    }

    private JsonNode getJsonNode(String name) {
        JsonNode rst = null;
        for (JsonNode one : container) {
            JsonNode path = one.path(name);
            if (!path.isNull() && !path.isMissingNode()) {
                rst = path;
                break;
            }
        }
        return rst;
    }

    private JsonNode getJsonNodeArray() {
        JsonNode rst = null;
        for (JsonNode path : container) {
            if (!path.isNull() && !path.isMissingNode()) {
                if (path.isArray()) {
                    rst = path;
                    break;
                }
            }
        }
        return rst;
    }

    @Override
    public String toString() {
        return Arrays.toString(container);
    }
}
