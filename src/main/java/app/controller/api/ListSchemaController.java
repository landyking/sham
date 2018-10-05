package app.controller.api;

import app.common.JdbcTool;
import app.common.QuickJson;
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by landy on 2018/10/5.
 */
@Component
public class ListSchemaController extends ApiController {
    @Resource
    private DataSourceManager dataSourceManager;

    @Override
    protected ModelAndView doWork(SuperParam superParam) throws Exception {
        String dataSource = superParam.needParam("dataSource", String.class);
        NamedParameterJdbcTemplate jdbc = dataSourceManager.getNamedParameterJdbcTemplate(dataSource);
        final DBType dataBaseType = dataSourceManager.getDataBaseType(dataSource);
        List<String> rst = jdbc.getJdbcOperations().execute(new ConnectionCallback<List<String>>() {
            @Override
            public List<String> doInConnection(Connection connection) throws SQLException, DataAccessException {
                return JdbcTool.listSchema(dataBaseType, connection);
            }
        });
        ArrayNode data = QuickJson.newArray();
        for (String one : rst) {
            data.add(one);
        }
        return jsonResultSuccess("data", data);
    }
}
