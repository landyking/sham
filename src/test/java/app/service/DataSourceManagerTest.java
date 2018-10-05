package app.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by landy on 2018/10/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext_*.xml")
public class DataSourceManagerTest {
    @Resource
    DataSourceManager dataSourceManager;

    @Test
    public void test1() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                String schemaTerm = connection.getMetaData().getSchemaTerm();
                System.out.println("oracle: " + schemaTerm);
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                String schemaTerm = connection.getMetaData().getSchemaTerm();
                System.out.println("mysql: " + schemaTerm);
                return null;
            }
        });

    }

    @Test
    public void test2() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                String schemaTerm = connection.getMetaData().getCatalogTerm();
                System.out.println("oracle: " + schemaTerm);
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                String schemaTerm = connection.getMetaData().getCatalogTerm();
                System.out.println("mysql: " + schemaTerm);
                return null;
            }
        });

    }

    @Test
    public void test3() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getCatalogs();
                System.out.println("oracle ###################");
                while (rs.next()) {
                    String string = rs.getString("TABLE_CAT");
                    System.out.println(string);
                }
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getCatalogs();
                System.out.println("mysql ###################");
                while (rs.next()) {
                    String string = rs.getString("TABLE_CAT");
                    System.out.println(string);
                }
                return null;
            }
        });

    }

    @Test
    public void test4() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getSchemas();
                System.out.println("oracle ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_SCHEM");
                    System.out.println(schem);
                }
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getSchemas();
                System.out.println("mysql ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_SCHEM");
                    System.out.println(schem);
                }
                return null;
            }
        });

    }

    @Test
    public void test5() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getSchemas();
                System.out.println("oracle ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_SCHEM");
                    String catalog = rs.getString("TABLE_CATALOG");
                    System.out.println(String.format("cata: %s, schem: %s", catalog, schem));
                }
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getSchemas();
                System.out.println("mysql ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_SCHEM");
                    String catalog = rs.getString("TABLE_CATALOG");
                    System.out.println(String.format("cata: %s, schem: %s", catalog, schem));
                }
                return null;
            }
        });

    }

    @Test
    public void test6() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getTableTypes();
                System.out.println("oracle ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_TYPE");
                    System.out.println(schem);
                }
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getTableTypes();
                System.out.println("mysql ###################");
                while (rs.next()) {
                    String schem = rs.getString("TABLE_TYPE");
                    System.out.println(schem);
                }
                return null;
            }
        });

    }

    @Test
    public void test7() throws Exception {
        NamedParameterJdbcTemplate oracle = dataSourceManager.getNamedParameterJdbcTemplate("oracle");
        oracle.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getTables(null, null, "%", null);
                System.out.println("oracle ###################");
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(JdbcUtils.lookupColumnName(metaData, i)+"\t");
                }
                System.out.println();
                while (rs.next()) {
                    showTableRow(rs);
                }
                return null;
            }
        });
        NamedParameterJdbcTemplate mysql = dataSourceManager.getNamedParameterJdbcTemplate("mysql");
        mysql.getJdbcOperations().execute(new ConnectionCallback<Void>() {
            @Override
            public Void doInConnection(Connection connection) throws SQLException, DataAccessException {
                ResultSet rs = connection.getMetaData().getTables(null, null, "%", null);
                System.out.println("mysql ###################");
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(JdbcUtils.lookupColumnName(metaData, i)+"\t");
                }
                System.out.println();
                while (rs.next()) {
                    showTableRow(rs);
                }
                return null;
            }
        });

    }

    private void showTableRow(ResultSet rs) throws SQLException {
        String table_cat = rs.getString("TABLE_CAT");
        String table_schem = rs.getString("TABLE_SCHEM");
        String table_name = rs.getString("TABLE_NAME");
        String table_type = rs.getString("TABLE_TYPE");
        String remarks = rs.getString("REMARKS");
//        String type_cat = rs.getString("TYPE_CAT");
//        String type_schem = rs.getString("TYPE_SCHEM");
//        String type_name = rs.getString("TYPE_NAME");
//        String self_referencing_col_name = rs.getString("SELF_REFERENCING_COL_NAME");
//        String ref_generation = rs.getString("REF_GENERATION");
        System.out.println(String.format("tbCat: %s, tbSchem: %s, tbName: %s, tbType: %s, remarks: %s",
                table_cat, table_schem, table_name, table_type, remarks));
    }
}