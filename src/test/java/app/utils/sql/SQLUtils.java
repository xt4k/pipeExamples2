package app.utils.sql;

import app.helper.BaseTestGui;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;

import static app.helper.BaseTestGui.sSqlDate;
import static app.helper.BaseTestGui.sSqlDateTimeShort;
import static app.pageobject.BasePageObject.reportInfo;
import static io.qameta.allure.Allure.step;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.lang.System.out;

public class SQLUtils extends BaseTestGui {
    private static String connectionString = getProperty("sql.db.connection.string");
  
    // here is xt4k methods started
    @Step("SQL:  Construct DB connection script")
    private static String sGetConnectionString() {// Setup the connection to the database:
        /*The name of the database and its location, A valid username/password for the connection:
         connect = DriverManager.getConnection("jdbc:mysql://localhost/" + databaseName + "?" + "user=" + username + "&password=" + password);*/
        String sConnectionString = String.format("%s://%s;DatabaseName=%s;user=%s;password=%s",
                getProperty("db.server.type"), getProperty("server.ip.addr"), getProperty("db.name"), getProperty("db.username"), getProperty("db.password"));//
        reportInfo("sConnectionString: " + sConnectionString);
        return sConnectionString;
    }

    @Step("SQL: executeUpdate SQL scripts '{0}' and return row count (atomic).")
    private static int iUpdateDB(String sScript) {
        int iResult = -1;
        try {
            Connection conConnect = DriverManager.getConnection(sGetConnectionString());
            //experimental setting.
            conConnect.setNetworkTimeout(Executors.newFixedThreadPool(1), 3000);

            Statement stStatement = conConnect.createStatement();
            Class.forName(getProperty("db.driver")).newInstance();
            reportInfo(String.format("`%s` run SQL script:`%s`.", getMethodName(), sScript));
            iResult = stStatement.executeUpdate(sScript);
            conConnect.close();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            reportInfo("When the driver has determined that the timeout value that was specified by the setQueryTimeout method has been exceeded" +
                    " and has at least attempted to cancel the currently running Statement. error:" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            reportInfo("DB access error occurs, this method is called on a closed Statement, the given SQL statement " +
                    "produces a ResultSet object, the method is called on a PreparedStatement or CallableStatement. error:" + e);
            e.printStackTrace();
        }/* finally {
            stStatement.close();
        }*/
        //System.out.println(String.format("SQL_Script: '%s' returned answer '%s'", sScript.toUpperCase(), bResult));
        reportInfo(String.format("`%s` run script :`%s` returned answer:`%s`.", getMethodName(), sScript.toUpperCase(), iResult));
        return iResult;
    }

    @Step("SQL: executeUpdate SQL scripts '{0}' and return 'false'/'true' (atomic).")
    public static boolean bUpdateDB(String sScript) {
        boolean bResult = false; //        System.out.println("connectionString: " + connectionString);        System.out.println("sScript: " + sScript);
        int iResult = -1;
        try {
            Connection conConnect = DriverManager.getConnection(sGetConnectionString());
            //experimental setting.
            conConnect.setNetworkTimeout(Executors.newFixedThreadPool(1), 3000);

            Statement stStatement = conConnect.createStatement();
            Class.forName(getProperty("db.driver")).newInstance();
            reportInfo(String.format("`%s` run SQL script:`%s`.", getMethodName(), sScript));
            iResult = stStatement.executeUpdate(sScript);

            if (iResult > 0) bResult = true;
            conConnect.close();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            reportInfo("When the driver has determined that the timeout value that was specified by the setQueryTimeout method has been exceeded" +
                    " and has at least attempted to cancel the currently running Statement. error:" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            reportInfo("DB access error occurs, this method is called on a closed Statement, the given SQL statement " +
                    "produces a ResultSet object, the method is called on a PreparedStatement or CallableStatement. error:" + e);
            e.printStackTrace();
        }/* finally {
            stStatement.close();
        }*/
        //System.out.println(String.format("SQL_Script: '%s' returned answer '%s'", sScript.toUpperCase(), bResult));
        reportInfo(String.format("SQL script :`%s` returned answer:`%s`, detected as:`%s`.", sScript.toUpperCase(), iResult, bResult));
        return bResult;
    }

    @Step("SQL: Convert ResultSet:`{0}` to List<HashMap<String, String>> form and return as list (atomic).")
    private static List<HashMap<String, String>> lGetListOfDataFromResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmdReSetMetaData = rs.getMetaData();
        int iColCount = rsmdReSetMetaData.getColumnCount();
        String[] aColName = new String[iColCount];
        List<HashMap<String, String>> lFromResultSet = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 1; i <= iColCount; i++) {
                aColName[i - 1] = rsmdReSetMetaData.getColumnLabel(i);
                map.put(aColName[i - 1], String.valueOf(rs.getObject(i)));
            }
            lFromResultSet.add(map);
        }
        reportInfo(String.format("`%s` convert:`%s` => List(HashMap):`%s`.", getMethodName(), rs, lFromResultSet));
        return lFromResultSet;
    }


    ////------ BASE SQL METHODS-------
////------ STARTED-------
    @Step("SQL: Execute SQL_Script: {0} and return result table in List<HashMap<String, String>> form.")
    private List<HashMap<String, String>> lExecuteScriptAndReturnTable(String sScript) {
        Connection connect = null;
        Statement statement = null;
        try {// Load the MySQL driver.// String string = "jdbc:sqlserver://13.68.201.246;DatabaseName=DFDS_Magnolia;user=sa;password=SUPER";
            Class.forName(getProperty("db.driver"));
            connect = DriverManager.getConnection(sGetConnectionString());    // Create the statement to be used to get the results.
            statement = connect.createStatement();  // Execute the query and get the result set, which contains all the results returned from the database.
            ResultSet rs = statement.executeQuery(sScript);
            List<HashMap<String, String>> lConvertedRs = lGetListOfDataFromResultSet(rs);
            reportInfo(String.format("`%s` returns:`%s`.", getMethodName(), lConvertedRs));
            return lConvertedRs;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // We have to closeTotalHoursScreen the connection and release the resources used.
            // Closing the statement results in closing the resultSet as well.
            try {
                assert statement != null;
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //________________________________
    @Step("SQL: Transform DB answer (ResultSet) '{0}' into HashMap (fieldName,fieldValue) (atomic).")
    private HashMap<String, String> hConvertResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();//            System.out.print("hConvertResultSet ");
        int iColNum = rsmd.getColumnCount();//            System.out.println("iColNum: " + iColNum);
        HashMap<String, String> hmSql = new HashMap<>();
        reportInfo(String.format("`%s` ResultSet:`%s`.", getMethodName(), rs));
        while (rs.next()) {        //System.out.print("FieldName/Value: ");
            for (int i = 1; i <= iColNum; i++) {        //if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);         //System.out.print(String.format("%s/%s", rsmd.getColumnName(i), columnValue));
                hmSql.put("fieldName", rsmd.getColumnName(i));
                hmSql.put("fieldValue", columnValue);
                reportInfo(String.format("`%s` rs:[`Field/Value`:`%s/%s`].", getMethodName(), rsmd.getColumnName(i), columnValue));
            }
        }
        String sLogMsg = String.format("`%s` get ResultSet:`%s`=> ResultSetMetaData:`%s` => hashMap:`%s`.", getMethodName(), rs, rsmd, hmSql);
        reportInfo(sLogMsg);
        return hmSql;
    }

    //+++++++
    @Step("SQL: Transform DB answer (ResultSet) '{0}' into HashMap (fieldName,fieldValue) (atomic).")
    private HashMap<String, String> hConvertResultSet2(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        HashMap<String, String> hmSql = new HashMap<>();
        int iColNum = rsmd.getColumnCount();//
        reportInfo(String.format("`%s` ResultSet:`%s`.", getMethodName(), rs));
        while (rs.next()) {
            for (int i = 1; i <= iColNum; ++i) {
                hmSql.putIfAbsent(rsmd.getColumnName(i), rs.getString(i));
            }
            reportInfo(String.format("`%s` hmSql:`%s`.", getMethodName(), hmSql));
        }
        String sLogMsg = String.format("`%s` get ResultSet:`%s`=> ResultSetMetaData:`%s` => hashMap:`%s`.", getMethodName(), rs, rsmd, hmSql);
        reportInfo(sLogMsg);
        return hmSql;
    }

    //___________________________________
    @Step("SQL: Execute query sScript {0} on DB.(atomic)")
    private boolean bQueryDB(String sScript) {
        ResultSet rs;//        System.out.println("connectionString: " + connectionString); System.out.println("sScript: " + sScript);
        try (Connection connection = DriverManager.getConnection(sGetConnectionString());
             Statement statement = connection.createStatement()) {
            Class.forName(getProperty("db.driver")).newInstance();
            reportInfo(String.format("Query SQL_Script: '%s'", sScript));// System.out.println("SQL_Script: " + sScript.toUpperCase());
            rs = statement.executeQuery(sScript);            //System.out.println("RS: "+rs.toString());
            hConvertResultSet(rs);           // TableModel tableModel = resultSetToTableModel(rs);
            rs.close();
            connection.close();
            return true;
        } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Step("SQL: Execute select {0} on DB.(atomic)")
    private HashMap<String, String> hmQueryDB(String sScript) {
        HashMap<String, String> hmResult = null;
        ResultSet rs;//        System.out.println("connectionString: " + connectionString); System.out.println("sScript: " + sScript);
        try (Connection connection = DriverManager.getConnection(sGetConnectionString());
             Statement statement = connection.createStatement()) {
            Class.forName(getProperty("db.driver")).newInstance();
            reportInfo(String.format("Query SQL_Script: '%s'", sScript));// System.out.println("SQL_Script: " + sScript.toUpperCase());
            rs = statement.executeQuery(sScript);
            String sLogMsg = String.format("`%s` => ResultSet:`%s`", getMethodName(), rs);
            reportInfo(sLogMsg);
            hmResult = hConvertResultSet(rs);           // TableModel tableModel = resultSetToTableModel(rs);
            rs.close();
            connection.close();
        } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hmResult;
    }

    @Step("SQL: Execute select {0} on DB.(atomic)")
    private HashMap<String, String> hmQueryDB2(String sScript) {
        HashMap<String, String> hmResult = null;
        ResultSet rs;//        System.out.println("connectionString: " + connectionString); System.out.println("sScript: " + sScript);
        try (Connection connection = DriverManager.getConnection(sGetConnectionString());
             Statement statement = connection.createStatement()) {
            Class.forName(getProperty("db.driver")).newInstance();
            reportInfo(String.format("Query SQL_Script: '%s'", sScript));// System.out.println("SQL_Script: " + sScript.toUpperCase());
            rs = statement.executeQuery(sScript);
            String sLogMsg = String.format("`%s` => ResultSet:`%s`", getMethodName(), rs);
            reportInfo(sLogMsg);
            hmResult = hConvertResultSet2(rs);           // TableModel tableModel = resultSetToTableModel(rs);
            rs.close();
            connection.close();
        } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hmResult;
    }
////------ COMPLETED-------
////------ BASE SQL METHODS-------

    @Step("SQL: `delete` script:`{0}` (return 'true-false') (atomic).")
    private boolean bDeleteInDB(String sScript) {
        boolean bResult = false; //        System.out.println("connectionString: " + connectionString);        System.out.println("sScript: " + sScript);
        int iResult = -1;
        try {
            Connection conConnect = DriverManager.getConnection(sGetConnectionString());
            //experimental setting.
            conConnect.setNetworkTimeout(Executors.newFixedThreadPool(1), 1000);

            Statement stStatement = conConnect.createStatement();
            Class.forName(getProperty("db.driver")).newInstance();
            //reportInfo("SQL(update_DB_with_script): " + sScript);
            iResult = stStatement.executeUpdate(sScript);

            if (iResult >= 0) bResult = true;
            conConnect.close();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            reportInfo("When the driver has determined that the timeout value that was specified by the setQueryTimeout method has been exceeded" +
                    " and has at least attempted to cancel the currently running Statement. error:" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            reportInfo("DB access error occurs, this method is called on a closed Statement, the given SQL statement " +
                    "produces a ResultSet object, the method is called on a PreparedStatement or CallableStatement. error:" + e);
            e.printStackTrace();
        }        //System.out.println(String.format("SQL_Script: '%s' returned answer '%s'", sScript.toUpperCase(), bResult));
        reportInfo(String.format("SQL script:`%s` returned answer:`%s`, detected as:`%s`.", sScript.toUpperCase(), iResult, bResult));
        return bResult;
    }

    @Step("SQL: executeUpdate SQL scripts '{0}' and return 'false'/'true' (atomic).")
    private boolean bUpdateDB2(String sScript) {
        boolean bResult = false; //        System.out.println("connectionString: " + connectionString);        System.out.println("sScript: " + sScript);
        try {
            Connection conConnect = DriverManager.getConnection(sGetConnectionString());
            //experimental setting.
            conConnect.setNetworkTimeout(Executors.newFixedThreadPool(1), 1000);
            Statement stStatement = conConnect.createStatement();
            Class.forName(getProperty("db.driver")).newInstance();
            //reportInfo("SQL(update_DB_with_script): " + sScript);
            bResult = stStatement.execute(sScript);
            conConnect.close();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLTimeoutException e) {
            reportInfo("When the driver has determined that the timeout value that was specified by the setQueryTimeout method has been exceeded" +
                    " and has at least attempted to cancel the currently running Statement. error:" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            reportInfo("DB access error occurs, this method is called on a closed Statement, the given SQL statement " +
                    "produces a ResultSet object, the method is called on a PreparedStatement or CallableStatement. error:" + e);
            e.printStackTrace();
        }        //System.out.println(String.format("SQL_Script: '%s' returned answer '%s'", sScript.toUpperCase(), bResult));
        reportInfo(String.format("SQL DB script:'%s' returned:'%s'.", sScript.toUpperCase(), bResult));
        return bResult;
    }

    @Step("SQL: Execute SQL script:`{0}` and return String answer.")
    private String sExecuteScriptAndReturnAnswer(String sScript) {
        Connection connect = null;
        Statement statement = null;
        try {// Load the MySQL driver.
            Class.forName(getProperty("db.driver"));// String sstring = "jdbc:sqlserver://13.68.201.246;DatabaseName=DFDS_Magnolia;user=sa;password=SUPER";
            connect = DriverManager.getConnection(sGetConnectionString());      // Create the statement to be used to get the results.
            statement = connect.createStatement();
            // Execute the query and get the result set, which contains all the results returned from the database.
            ResultSet resultSet = statement.executeQuery(sScript);
            Map<String, String> sqlPair;
            sqlPair = hConvertResultSet(resultSet);//   System.out.println(); System.out.println("sqlPair: " + sqlPair);
            String sSqlPairValue = sqlPair.get("fieldValue");
            reportInfo(String.format("`%s method`, Script:`%s`, returns ResultSet:`%s`, with value:`%s`.", getMethodName(), sScript, resultSet, sSqlPairValue));
            return sSqlPairValue;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {            // We have to closeTotalHoursScreen the connection and release the resources used.
            // Closing the statement results in closing the resultSet as well.
            try {
                assert statement != null;
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}


