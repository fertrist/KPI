package app.dao;

import app.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao extends GenericDao<User> {

    public UserDao(Connection connection) {
        super(User.class, connection);
    }

    @Override
    public int update(int id, User replacement) {
        PreparedStatement stat = null;
        try {
            try {
                Map<String, String> updates = new HashMap<String, String>();
                if (replacement.getName() != null) {
                    updates.put("name", replacement.getName());
                }
                if (replacement.getSurname() != null) {
                    updates.put("surname", replacement.getSurname());
                }
                if (replacement.getFamilies() != null) {
                    updates.put("families", replacement.getFamilies());
                }
                if (replacement.getEmail() != null) {
                    updates.put("email", replacement.getEmail());
                }
                StringBuilder s = new StringBuilder("UPDATE User U set ");
                int size = updates.size(), count = 0;
                for (Map.Entry<String, String> entry : updates.entrySet()) {
                    count++;
                    String params = String.format("%s='%s'", entry.getKey(), entry.getValue());
                    s.append(params);
                    if (count < size) {
                        s.append(",");
                    }
                }
                s.append(" WHERE U.id=").append(id);
                stat = conn.prepareStatement(s.toString());
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

    @Override
    public int delete(int id) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("DELETE FROM " + table + " WHERE id=?;");
                stat.setInt(1, id);
                return stat.executeUpdate();
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("User can't be removed.");
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int create(User user) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("INSERT INTO User(name, surname, families, email) "
                    + "VALUES(?,?,?,?);");
                stat.setString(1, user.getName());
                stat.setString(2, user.getSurname());
                stat.setString(3, user.getFamilies());
                stat.setString(4, user.getEmail());
                return stat.executeUpdate();
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("User hasn't been created.");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public User read(int id) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + " WHERE id=?;");
                stat.setInt(1, id);
                ResultSet rs = stat.executeQuery();
                User object = null;
                if (rs.next()) {
                    object = new User();
                    object.setId(rs.getInt("id"));
                    object.setName(rs.getString("name"));
                    object.setSurname(rs.getString("surname"));
                    object.setFamilies(rs.getString("families"));
                    object.setEmail(rs.getString("email"));
                }
                return object;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> readAll() {
        PreparedStatement stat = null;
        List<User> all = new ArrayList<User>();
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + ";");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setFamilies(rs.getString("families"));
                    user.setEmail(rs.getString("email"));
                    all.add(user);
                }
                return all;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return all;
        }
    }

    public int readId(User user) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("SELECT id FROM " + table + " WHERE email=? "
                    + "AND name=? AND surname=? AND families=?;");
                stat.setString(1, user.getEmail());
                stat.setString(2, user.getName());
                stat.setString(3, user.getSurname());
                stat.setString(4, user.getFamilies());
                ResultSet rs = stat.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<User> readUsersByFamily(int familyId) {
        PreparedStatement stat = null;
        try{
            try{
                //select length(U.families)-length(replace(U.families,'1','')) from User U where U.id=1;

                stat = conn.prepareStatement("SELECT * FROM User U WHERE " +
                    "LENGTH(U.families)-LENGTH(REPLACE(U.families, ?, ''))=1;");
                stat.setString(1, "" + familyId);
                ResultSet rs = stat.executeQuery();
                List<User> users = new ArrayList<User>();
                while(rs.next()) {
                    users.add(new User(rs.getString("name"), rs.getString("surname"),
                        rs.getString("families"), rs.getString("email")));
                }
                return users;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
