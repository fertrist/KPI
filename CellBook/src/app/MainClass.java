package app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainClass {
    public static void main(String [] args) {
        Connection connection = Connector.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from branch;");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));
            }
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    PanelFrame panelFrame = new PanelFrame();
                    panelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    panelFrame.setVisible(true);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
