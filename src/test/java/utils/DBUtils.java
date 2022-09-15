package utils;

import org.apache.commons.collections4.map.HashedMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    public static List<Map<String, String>> getDataFromDB(String query) {

        String dbUrl = ConfigReader.getPropertyValue("dbUrl");
        String userName = ConfigReader.getPropertyValue("dbUserName");
        String password = ConfigReader.getPropertyValue("dbPassword");
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        List<Map<String, String>> tableData = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(dbUrl, userName, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {

                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    String columnValue = resultSet.getString(columnName);
                    row.put(columnName, columnValue);
                }
                tableData.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            closeConnection(connection,statement,resultSet);
        }
        return tableData;

    }

    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
