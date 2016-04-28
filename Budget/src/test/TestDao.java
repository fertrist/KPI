package test;

import app.helper.DBHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDao {

    Connection conn;

    @BeforeClass(alwaysRun = true)
    public void setConnection() {
        conn = DBHelper.getConnection(DBHelper.propertiesFile);
    }

    @AfterClass(alwaysRun = true)
    public void dropConnection() throws SQLException {
        conn.close();
    }
}
