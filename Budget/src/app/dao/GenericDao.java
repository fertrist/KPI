package app.dao;

import app.helper.DBHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public abstract class GenericDao<Type> {
    protected Connection conn;
    protected String table;

    public GenericDao(Class<Type> cls, Connection connection) {
        this.conn = connection;
        String[] packages = cls.getName().split("\\.");
        table = packages[packages.length - 1];
    }

    public abstract int update(int id, Type replacement);

    public abstract int delete(int id);

    public abstract int create(Type object);

    public abstract Type read(int id);

    /**
     * Reads whe whole table.
     *
     * @return whole table
     */
    public abstract List<Type> readAll();

    /**
     * Creates a table. For each child it's different.
     *
     * @return number of affected rows
     */
    public int createTable() {
        PreparedStatement stat = null;
        BufferedReader script = null;
        try {
            try {
                Properties props = new Properties();
                FileInputStream dbProps = new FileInputStream(DBHelper.propertiesFile);
                props.load(dbProps);
                dbProps.close();
                script = new BufferedReader(new FileReader(props.getProperty
                    (table + ".script")));
                StringBuilder query = new StringBuilder();
                String s;
                while ((s = script.readLine()) != null) {
                    query.append(s);
                }
                stat = conn.prepareStatement(query.toString());
                return stat.executeUpdate();
            } finally {
                if (stat != null) {
                    stat.close();
                }
                if (script != null) {
                    script.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SQLException sqlEx;
            while ((sqlEx = e.getNextException()) != null) {
                sqlEx.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Drops table. Each child has its own table.
     *
     * @return number of affected rows.
     */
    public int dropTable() {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("DROP TABLE " + table + ";");
                return stat.executeUpdate();
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
