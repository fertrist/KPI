package app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    public static Connection getConnection() {
        Properties properties = new Properties();
        Connection connection = null;
        try {
            //get properties
            FileInputStream in = new FileInputStream("resources/db.properties");
            properties.load(in);
            String drivers = properties.getProperty("jdbc.drivers");
            String url = properties.getProperty("jdbc.url");
            String user = properties.getProperty("jdbc.user");
            String password = properties.getProperty("jdbc.password");

            //register driver
            Class.forName(drivers);

            //get connection
            connection = DriverManager.getConnection(url, user, password);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
