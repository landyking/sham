package app.common;

import app.service.DBType;
import com.google.common.collect.Lists;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by landy on 2018/10/5.
 */
public class JdbcTool {
    public static class TableStruct {
        public String catalog;
        public String schema;
        public String name;
        public String remarks;
        public String type;
    }

    public static List<String> listCatalog(DBType dataBaseType, Connection connection) throws SQLException {
        ResultSet catalogs = connection.getMetaData().getCatalogs();
        LinkedList<String> rst = Lists.newLinkedList();
        while (catalogs.next()) {
            String one = catalogs.getString("TABLE_CAT");
            rst.add(one);
        }
        return rst;
    }

    public static List<String> listTableType(DBType dataBaseType, Connection connection) throws SQLException {
        ResultSet catalogs = connection.getMetaData().getTableTypes();
        LinkedList<String> rst = Lists.newLinkedList();
        while (catalogs.next()) {
            String one = catalogs.getString("TABLE_TYPE");
            rst.add(one);
        }
        return rst;
    }

    public static List<String> listSchema(DBType dataBaseType, Connection connection) throws SQLException {
        LinkedList<String> rst = Lists.newLinkedList();
        if (dataBaseType == DBType.Oracle) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("(select owner from user_tab_privs group by owner) union (select user from dual)");
                while (rs.next()) {
                    String one = rs.getString(1);
                    rst.add(one);
                }
            }
        } else {
            ResultSet catalogs = connection.getMetaData().getSchemas();
            while (catalogs.next()) {
                String one = catalogs.getString("TABLE_SCHEM");
                rst.add(one);
            }
        }
        return rst;
    }

    public static List<TableStruct> listTable(DBType dataBaseType, Connection connection, String cat, String schema, String type, String table) throws SQLException {
        if (Texts.hasText(cat)) {
            cat = cat.trim().toUpperCase();
        } else {
            cat = null;
        }
        if (Texts.hasText(schema)) {
            schema = schema.trim().toUpperCase();
        } else {
            schema = null;
        }
        String[] types = null;
        if (Texts.hasText(type)) {
            types = new String[]{type.trim().toUpperCase()};
        } else {
            types = null;
        }
        if (Texts.hasText(table)) {
            table = table.trim().toUpperCase();
        } else {
            table = "%";
        }
        List<TableStruct> rst = Lists.newLinkedList();
        ResultSet tables = connection.getMetaData().getTables(cat, schema, table, types);
        int max = 1000;
        int count = 0;
        while (tables.next() && count++ <= max) {
            String table_cat = tables.getString("TABLE_CAT");
            String table_schem = tables.getString("TABLE_SCHEM");
            String table_name = tables.getString("TABLE_NAME");
            String table_type = tables.getString("TABLE_TYPE");
            String remarks = tables.getString("REMARKS");
            TableStruct ts = new TableStruct();
            ts.catalog=table_cat;
            ts.schema=table_schem;
            ts.name = table_name;
            ts.type = table_type;
            ts.remarks = remarks;
            rst.add(ts);
        }
        return rst;
    }
}
