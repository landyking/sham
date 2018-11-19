package app.service;

import app.common.JdbcTool;
import app.common.Texts;
import app.common.beetl.BeetlTool;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.beetl.core.Template;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.TailBean;
import org.beetl.sql.core.kit.StringKit;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by landy on 2018/10/6.
 */
public abstract class AbstractTemplate {
    @Resource
    private DataSourceManager dataSourceManager;

    public String gen(String dataSource, final String catalog, final String schema, final String table, Integer prefixLength) throws IOException {
        final DBType dataBaseType = dataSourceManager.getDataBaseType(dataSource);
        NamedParameterJdbcTemplate jdbc = dataSourceManager.getNamedParameterJdbcTemplate(dataSource);
        JdbcTool.TableStruct model = jdbc.getJdbcOperations().execute(new ConnectionCallback<JdbcTool.TableStruct>() {
            @Override
            public JdbcTool.TableStruct doInConnection(Connection connection) throws SQLException, DataAccessException {
                List<JdbcTool.TableStruct> tables = JdbcTool.listTable(dataBaseType, connection, catalog, schema, null, table);
                Assert.isTrue(tables.size() > 0, "表"+table+"不存在");
                Assert.isTrue(tables.size() < 2, "存在多张表"+table);
                JdbcTool.TableStruct rst = tables.get(0);
                List<JdbcTool.ColumnStruct> columns = JdbcTool.listTableColumn(dataBaseType, connection, catalog, schema, table);
                rst.columns.addAll(columns);
                return rst;
            }
        });
        return gen(model, prefixLength);
    }

    protected abstract String gen(JdbcTool.TableStruct model, Integer prefixLength) throws IOException;

    protected abstract String name();

    protected String renderBeetl(JdbcTool.TableStruct model, org.springframework.core.io.Resource resource, Integer prefixLength) throws IOException {
        Template tp = BeetlTool.getTemplate().getTemplate(Resources.toString(resource.getURL(), Charsets.UTF_8));
        String cutTableName = model.name.substring(prefixLength);
        model.javaName = StringKit.toUpperCaseFirstOne(StringKit.deCodeUnderlined(cutTableName.toLowerCase()));
        model.javaLowerName = (StringKit.deCodeUnderlined(cutTableName.toLowerCase()));
        for (JdbcTool.ColumnStruct col : model.columns) {
            col.javaName = StringKit.deCodeUnderlined(col.name.toLowerCase());
            col.javaMethodName = StringKit.toUpperCaseFirstOne(col.javaName);
            col.javaTypeName = JdbcTool.convertJavaType(col.dataType,col.columnSize,col.decimalDigits);
            if (!Texts.hasText(col.remarks)) {
                col.remarks = col.javaName;
            }
        }
        tp.binding("m", model);
        tp.binding("ext", TailBean.class.getName());
        tp.binding("imports", "");
        tp.binding("implSerializable", false);
        return tp.render();
    }

    public String genFileName(String dataSource, String catalog, String schema, String table, Integer prefixLength) {
        table = table.substring(prefixLength);
        return genFileName(dataSource, catalog, schema, table);
    }

    public abstract String genFileName(String dataSource, String catalog, String schema, String table);
}
