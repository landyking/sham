package app.controller.api;

import app.common.JdbcTool;
import app.common.QuickJson;
import app.common.web.SuperParam;
import app.service.AbstractTemplate;
import app.service.DBType;
import app.service.DataSourceManager;
import app.service.TemplateManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by landy on 2018/10/5.
 */
@Component
public class CodeGenController extends ApiController {
    @Resource
    private TemplateManager templateManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        String dataSource = superParam.needParam("dataSource", String.class);
        final String catalog = superParam.getParam("catalog", String.class);
        final String schema = superParam.getParam("schema", String.class);
        final String table = superParam.needParam("table", String.class);
        final String template = superParam.needParam("template", String.class);
        final Integer prefixLength = superParam.needParam("prefixLength", Integer.class);
        AbstractTemplate tpl = templateManager.getTemplate(template);
        String txt = tpl.gen(dataSource, catalog, schema, table,prefixLength);
        ObjectNode rst = QuickJson.newObject();
        rst.put("code", 0);
        rst.put("data", txt);
        rst.put("fileName", tpl.genFileName(dataSource, catalog, schema, table,prefixLength));
        return jsonResult(rst);
    }
}
