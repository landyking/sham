package app.controller.api;

import app.common.JdbcTool;
import app.common.QuickJson;
import app.common.Texts;
import app.common.web.SuperParam;
import app.service.DBType;
import app.service.DataSourceManager;
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
public class ListTableController extends ApiController {
    @Resource
    private DataSourceManager dataSourceManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        String dataSource = superParam.getParam("dataSource", String.class);
        if (!Texts.hasText(dataSource)) {
            return jsonResultSuccess();
        }
        final String catalog = superParam.getParam("catalog", String.class);
        final String schema = superParam.getParam("schema", String.class);
        final String type = superParam.getParam("type", String.class);
        final String table = superParam.getParam("table", String.class);
        NamedParameterJdbcTemplate jdbc = dataSourceManager.getNamedParameterJdbcTemplate(dataSource);
        final DBType dataBaseType = dataSourceManager.getDataBaseType(dataSource);
        List<JdbcTool.TableStruct> list = jdbc.getJdbcOperations().execute(new ConnectionCallback<List<JdbcTool.TableStruct>>() {
            @Override
            public List<JdbcTool.TableStruct> doInConnection(Connection connection) throws SQLException, DataAccessException {
                return JdbcTool.listTable(dataBaseType, connection, catalog, schema, type, table);
            }
        });
        ArrayNode data = QuickJson.newArray();
        for (JdbcTool.TableStruct one : list) {
            ObjectNode o = data.addObject();
            o.put("catalog", one.catalog);
            o.put("schema", one.schema);
            o.put("name", one.name);
            o.put("type", one.type);
            o.put("remarks", one.remarks);
        }
        return jsonResultSuccess("data", data);
    }
}
