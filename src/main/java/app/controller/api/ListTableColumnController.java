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
public class ListTableColumnController extends ApiController {
    @Resource
    private DataSourceManager dataSourceManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        String dataSource = superParam.needParam("dataSource", String.class);
        final String catalog = superParam.getParam("catalog", String.class);
        final String schema = superParam.getParam("schema", String.class);
        final String table = superParam.needParam("table", String.class);
        NamedParameterJdbcTemplate jdbc = dataSourceManager.getNamedParameterJdbcTemplate(dataSource);
        final DBType dataBaseType = dataSourceManager.getDataBaseType(dataSource);
        List<JdbcTool.ColumnStruct> list = jdbc.getJdbcOperations().execute(new ConnectionCallback<List<JdbcTool.ColumnStruct>>() {
            @Override
            public List<JdbcTool.ColumnStruct> doInConnection(Connection connection) throws SQLException, DataAccessException {
                return JdbcTool.listTableColumn(dataBaseType, connection, catalog, schema, table);
            }
        });

        ObjectNode rst = QuickJson.newObject();
        rst.put("code", 0);
        rst.put("count", list.size());
        ArrayNode data = rst.putArray("data");
        for (JdbcTool.ColumnStruct one : list) {
            ObjectNode o = data.addObject();
            o.put("name", one.name);
            o.put("columnDef", one.columnDef);
            o.put("columnSize", one.columnSize);
            o.put("dataType", one.dataType);
            o.put("decimalDigits", one.decimalDigits);
            o.put("isAutoIncrement", one.isAutoIncrement);
            o.put("isNullable", one.isNullable);
            o.put("typeName", one.typeName);
            o.put("remarks", one.remarks);
        }
        return jsonResult(rst);
    }
}
