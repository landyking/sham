package app.controller.api;

import app.common.QuickJson;
import app.common.web.SuperParam;
import app.service.DataSourceManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by landy on 2018/10/5.
 */
@Component
public class ListDataSourceController extends ApiController {
    @Resource
    private DataSourceManager dataSourceManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        List<String> list = dataSourceManager.listDataSource();
        ArrayNode data = QuickJson.newArray();
        for (String one : list) {
            data.add(one);
        }
        return jsonResultSuccess("data", data);
    }
}
