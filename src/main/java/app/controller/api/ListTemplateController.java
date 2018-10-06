package app.controller.api;

import app.common.QuickJson;
import app.common.web.SuperParam;
import app.service.TemplateManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by landy on 2018/10/5.
 */
@Component
public class ListTemplateController extends ApiController {
    @Resource
    private TemplateManager templateManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        ArrayNode data = QuickJson.newArray();
        for (String one : templateManager.listTemplateName()) {
            data.add(one);
        }
        return jsonResultSuccess("data", data);
    }
}
