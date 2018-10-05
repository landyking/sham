package app.common.web;

import app.common.QuickJson;
import app.common.Texts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by landy on 2017/11/23.
 */
public abstract class BaseController extends AbstractController {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());


    protected String getRequestSourceIp() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr;
    }


    public boolean isRequestHeaderDebug() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return StringUtils.hasText(request.getHeader("_debug"));
    }

    protected SuperParam getSuperParam(HttpServletRequest request, HttpServletResponse response) {
        try {
            SuperParam superParam = (SuperParam) request.getAttribute(SuperParam.SUPER_PARAM_KEY);
            if (superParam != null) {
                return superParam;
            }
            String contentType = request.getContentType();
            if (StringUtils.hasText(contentType)) {
                if (contentType.toUpperCase().contains("JSON")) {
                    String content = CharStreams.toString(new InputStreamReader(request.getInputStream(), Charsets.UTF_8));
                    JsonNode root = null;
                    if (Texts.hasText(content)) {
                        LOGGER.debug("json request: {}", content);
                        root = QuickJson.JSON.readTree(content);
                    }
                    if (root == null) {
                        root = QuickJson.newObject();
                    }
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    JsonNode obj = QuickJson.convertParameterMapToJsonObject(parameterMap);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("request map: {}", parameterMap);
                    }
                    superParam = new SuperParam(obj, root);
                }
            }
            if (superParam == null) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("request map: {}", QuickJson.JSON.writeValueAsString(parameterMap));
                }
                superParam = new SuperParam(QuickJson.convertParameterMapToJsonObject(parameterMap));
            }
            request.setAttribute(SuperParam.SUPER_PARAM_KEY, superParam);
            superParam.setRequest(request);
            superParam.setResponse(response);

            return superParam;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void logInfo(String msg, Object... var2) {
        LOGGER.info(msg, var2);
    }

    protected void logWarn(String var1, Object... var2) {
        LOGGER.warn(var1, var2);
    }

    protected void logWarn(String var1) {
        LOGGER.warn(var1);
    }

    protected void logError(String msg, Throwable throwable) {
        LOGGER.error(msg, throwable);
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return doWork(getSuperParam(request, response));
        } catch (IllegalArgumentException e) {
            logWarn("操作失败: " + e.getMessage());
            return jsonResultFailure("操作失败：" + e.getMessage());
        } catch (Exception e) {
            logError("服务器异常", e);
            return jsonResultFailure("服务器异常：" + e.getMessage());
        }
    }

    protected abstract ModelAndView doWork(SuperParam superParam) throws Exception;

    public abstract ModelAndView jsonResultSuccess();

    public abstract ModelAndView jsonResultSuccess(String name, JsonNode node);

    public abstract ModelAndView jsonResultFailure(String message);

    public ModelAndView jsonResult(Object obj) {
        MappingJackson2JsonView view = new MyMappingJackson2JsonView();
        view.setObjectMapper(QuickJson.JSON);
        view.getObjectMapper().registerModule(QuickJson.JSON_MODULE);
        ModelAndView rst = new ModelAndView(view);
        if (obj instanceof ObjectNode) {
            ObjectNode node = ((ObjectNode) obj);
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> one = fields.next();
                rst.addObject(one.getKey(), one.getValue());
            }
        } else if (obj instanceof ArrayNode || obj instanceof Collection) {
            rst.addObject("list", obj);
            view.setExtractValueFromSingleKeyModel(true);
            view.setModelKey("list");
        } else {
            if (obj instanceof Map) {
                Map tmp = (Map) obj;
                for (Object one : tmp.keySet()) {
                    rst.addObject(one.toString(), tmp.get(one));
                }
            } else {
                rst.addObject(obj);
            }
        }
        return rst;
    }

    protected String getBaseUrl() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String url = "";
        url = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();
        return url;
    }
}
