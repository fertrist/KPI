package app.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    public static final String propertiesFile = "resources/database.properties";

    public static Connection getConnection(String properties) {
        Connection conn = null;
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(properties);
            props.load(in);
            in.close();
            Class.forName(props.getProperty("jdbc.driver"));
            String url = props.getProperty("database.url");
            String user = props.getProperty("database.user");
            String password = props.getProperty("database.password");
            System.out.println(String.format("Getting connection to %s "
                + "for user: %s; password: %s %n", url, user, password));
            conn = DriverManager.getConnection(url, user, password);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
