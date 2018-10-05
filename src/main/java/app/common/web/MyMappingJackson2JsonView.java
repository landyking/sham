package app.common.web;

import app.common.QuickJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

/**
 * @author: landy
 * @date: 2018-01-28 22:28
 */
public class MyMappingJackson2JsonView extends MappingJackson2JsonView {
    public static boolean openDebug = false;

    @Override
    protected Object filterModel(Map<String, Object> model) {
        Object o = super.filterModel(model);
        if (openDebug) {
            try {
                logger.info("json output: " + QuickJson.JSON.writeValueAsString(o));
            } catch (JsonProcessingException e) {
                logger.warn("convert object to json error!", e);
            }
        }
        return o;
    }
}
