package app.common;

import app.service.DBType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.beetl.sql.core.JavaType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by landy on 2018/10/5.
 */
public class JdbcTool {

    public static String convertJavaType(Integer dataType, Integer columnSize, Integer decimalDigits) {
        String type = JavaType.getType(dataType, columnSize, decimalDigits);
            /*if(config.isPreferBigDecimal()&&type.equals("Double")){
                type = "BigDecimal";
            }*/
        if (type.equals("Timestamp")) {
            type = "Date";
        }
        return type;
    }

    public static class TableStruct {
        public String catalog;
        public String schema;
        public String name;
        public transient String javaName;
        public transient String javaLowerName;
        public String remarks;
        public String type;
        public final transient List<ColumnStruct> columns = Lists.newLinkedList();

        public String getCatalog() {
            return catalog;
        }

        public String getSchema() {
            return schema;
        }

        public String getName() {
            return name;
        }

        public String getJavaName() {
            return javaName;
        }

        public String getJavaLowerName() {
            return javaLowerName;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getType() {
            return type;
        }

        public List<ColumnStruct> getColumns() {
            return columns;
        }
    }

    public static class ColumnStruct {
        public String name;
        public transient String javaName;
        public transient String javaMethodName;
        public Integer dataType;
        public String typeName;
        public transient String javaTypeName;
        public Integer columnSize;
        public Integer decimalDigits;
        public String remarks;
        public String columnDef;
        public String isNullable;
        public String isAutoIncrement;
        public Boolean primaryKey;

        public String getName() {
            return name;
        }

        public String getJavaName() {
            return javaName;
        }

        public String getJavaMethodName() {
            return javaMethodName;
        }

        public Integer getDataType() {
            return dataType;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getJavaTypeName() {
            return javaTypeName;
        }

        public Integer getColumnSize() {
            return columnSize;
        }

        public Integer getDecimalDigits() {
            return decimalDigits;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getColumnDef() {
            return columnDef;
        }

        public String getIsNullable() {
            return isNullable;
        }

        public String getIsAutoIncrement() {
            return isAutoIncrement;
        }

        public Boolean getPrimaryKey() {
            return primaryKey;
        }
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
       /* if (dataBaseType == DBType.Oracle) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("(select owner from user_tab_privs group by owner) union (select user from dual)");
                while (rs.next()) {
                    String one = rs.getString(1);
                    rst.add(one);
                }
            }
        } else */
        {
            ResultSet catalogs = connection.getMetaData().getSchemas();
            while (catalogs.next()) {
                String one = catalogs.getString("TABLE_SCHEM");
                rst.add(one);
            }
        }
        return rst;
    }

    public static String toUpper(String s, boolean upper) {
        if (upper) {
            return s.toUpperCase();
        } else {
            return s;
        }
    }

    public static List<TableStruct> listTable(DBType dataBaseType, Connection connection, String cat, String schema, String type, String table) throws SQLException {
        List<TableStruct> tableStructs = listTable0(connection, cat, schema, type, table, false);
        if (tableStructs.isEmpty()) {
            return listTable0(connection, cat, schema, type, table, true);
        }
        return tableStructs;
    }

    private static List<TableStruct> listTable0(Connection connection, String cat, String schema, String type, String table, boolean forceUpper) throws SQLException {
        if (Texts.hasText(cat)) {
            cat = toUpper(cat.trim(), forceUpper);
        } else {
            cat = null;
        }
        if (Texts.hasText(schema)) {
            schema = toUpper(schema.trim(), forceUpper);
        } else {
            schema = null;
        }
        String[] types = null;
        if (Texts.hasText(type)) {
            types = new String[]{toUpper(type.trim(), forceUpper)};
        } else {
            types = null;
        }
        if (Texts.hasText(table)) {
            table = toUpper(table.trim(), forceUpper);
        } else {
            table = "%";
        }
        List<TableStruct> rst = Lists.newLinkedList();
        ResultSet tables = connection.getMetaData().getTables(cat, schema, table, types);
        int max = 1000;
        int count = 0;
        while (tables.next() && count++ < max) {
            String table_cat = tables.getString("TABLE_CAT");
            String table_schem = tables.getString("TABLE_SCHEM");
            String table_name = tables.getString("TABLE_NAME");
            String table_type = tables.getString("TABLE_TYPE");
            String remarks = tables.getString("REMARKS");
            TableStruct ts = new TableStruct();
            ts.catalog = table_cat;
            ts.schema = table_schem;
            ts.name = table_name;
            ts.type = table_type;
            ts.remarks = remarks;
            rst.add(ts);
        }
        return rst;
    }

    public static List<ColumnStruct> listTableColumn(DBType dataBaseType, Connection connection, String cat, String schema, String table) throws SQLException {
        List<ColumnStruct> columnStructs = listTableColumn0(connection, cat, schema, table, false);
        if (columnStructs.isEmpty()) {
            return listTableColumn0(connection, cat, schema, table, true);
        }
        return columnStructs;
    }

    private static List<ColumnStruct> listTableColumn0(Connection connection, String cat, String schema, String table, boolean foreUpper) throws SQLException {
        if (Texts.hasText(cat)) {
            cat = toUpper(cat.trim(), foreUpper);
        } else {
            cat = null;
        }
        if (Texts.hasText(schema)) {
            schema = toUpper(schema.trim(), foreUpper);
        } else {
            schema = null;
        }
        if (Texts.hasText(table)) {
            table = toUpper(table.trim(), foreUpper);
        } else {
            throw new RuntimeException("未指定表");
        }
        List<ColumnStruct> rst = Lists.newLinkedList();
        Set<String> pkColumns = Sets.newHashSet();
        ResultSet pks = connection.getMetaData().getPrimaryKeys(cat, schema, table);
        while (pks.next()) {
            String name = pks.getString("COLUMN_NAME");
            pkColumns.add(name);
        }
        ResultSet columns = connection.getMetaData().getColumns(cat, schema, table, "%");
        int max = 1000;
        int count = 0;
        while (columns.next() && count++ <= max) {

            String column_name = columns.getString("COLUMN_NAME");
            int data_type = columns.getInt("DATA_TYPE");
            String type_name = columns.getString("TYPE_NAME");
            int column_size = columns.getInt("COLUMN_SIZE");
            int decimal_digits = columns.getInt("DECIMAL_DIGITS");
            String remarks = columns.getString("REMARKS");
            String column_def = columns.getString("COLUMN_DEF");
            String is_nullable = columns.getString("IS_NULLABLE");
            String is_autoincrement = columns.getString("IS_AUTOINCREMENT");

            ColumnStruct cs = new ColumnStruct();
            cs.name = column_name;
            cs.dataType = data_type;
            cs.typeName = type_name;
            cs.columnSize = column_size;
            cs.decimalDigits = decimal_digits;
            cs.remarks = remarks;
            cs.columnDef = column_def;
            cs.isNullable = is_nullable;
            cs.isAutoIncrement = is_autoincrement;
            cs.primaryKey = pkColumns.contains(column_name);
            rst.add(cs);
        }
        return rst;
    }
}
