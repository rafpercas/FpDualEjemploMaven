package edu.fpdual.jdbc.ejemplobd.connector;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class MySQLConnector {

    @Setter
    @Getter
    Properties prop = new Properties();

    public MySQLConnector() {
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the connection object for a MySQL DDBB
     *
     * @return a {@link Connection}
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getMySQLConnection() throws ClassNotFoundException, SQLException {
        try {

            //Indicates which driver is going to be used.
            Class.forName(prop.getProperty(MySQLConstants.DRIVER));
            //Creates the connection based on the obtained URL.
            return DriverManager.getConnection(getURL());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Obtains the URL to connect to a MySQL DDBB.
     *
     * @return an URL
     */
    private String getURL() {
        //jdbc:mysql://localhost:3306/world?user=sa&password=12345678&useSSL=false;
        return new StringBuilder().append(prop.getProperty(MySQLConstants.URL_PREFIX))
                .append(prop.getProperty(MySQLConstants.URL_HOST)).append(":")
                .append(prop.getProperty(MySQLConstants.URL_PORT)).append("/")
                .append(prop.getProperty(MySQLConstants.URL_SCHEMA)).append("?user=")
                .append(prop.getProperty(MySQLConstants.USER)).append("&password=")
                .append(prop.getProperty(MySQLConstants.PASSWD)).append("&useSSL=")
                .append(prop.getProperty(MySQLConstants.URL_SSL))
                .append(("&useJDBCCompliantTimezoneShift="))
                .append(prop.getProperty(MySQLConstants.USE_JDBC_COMPLIANT_TIMEZONE_SHIFT)).append(("&useLegacyDatetimeCode="))
                .append(prop.getProperty(MySQLConstants.USE_LEGACY_DATE_TIME_CODE)).append(("&serverTimezone="))
                .append(prop.getProperty(MySQLConstants.SERVER_TIMEZONE)).toString();
    }


    private static void ejemploStatement() throws SQLException, ClassNotFoundException {
        MySQLConnector connector = new MySQLConnector();
        try(Connection connection = connector.getMySQLConnection();
            Statement stm = connection.createStatement()) {

            ResultSet result = stm.executeQuery("SELECT Id, Name, District FROM world.city where CountryCode = 'ESP'");
            int counter = 0;
            result.beforeFirst();
            while (result.next()) {
                int id = result.getInt("Id");
                String name = result.getString("Name");
                String district = result.getString("District");
                System.out.println(id + " " + name + " " + district);
                counter++;
            }
            System.out.println("Total de elementos: " + counter);
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySQLConnector connector = new MySQLConnector();
        Connection connection = connector.getMySQLConnection();


        Statement stm = connection.createStatement();
        stm.executeUpdate("UPDATE city SET District = 'Kabol' where id = 1");
        stm.close();
        connection.close();

        System.out.println(connection.getCatalog());
    }

}
