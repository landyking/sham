package app.controller.api;

import app.common.QuickJson;
import app.common.web.BaseController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by landy on 2018/10/5.
 */
public abstract class ApiController extends BaseController{
    @Override
    public ModelAndView jsonResultSuccess() {
        ObjectNode rst = QuickJson.newObject();
        rst.put("code", 0);
        rst.put("msg", "ok");
        return jsonResult(rst);
    }

    @Override
    public ModelAndView jsonResultSuccess(String name, JsonNode node) {
        ObjectNode rst = QuickJson.newObject();
        rst.put("code", 0);
        rst.put("msg", "ok");
        rst.set(name, node);
        return jsonResult(rst);
    }

    @Override
    public ModelAndView jsonResultFailure(String message) {
        ObjectNode rst = QuickJson.newObject();
        rst.put("code", 1);
        rst.put("msg", message);
        return jsonResult(rst);
    }

}
