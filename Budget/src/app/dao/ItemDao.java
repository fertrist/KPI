package app.dao;

import app.entity.Item;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDao extends GenericDao<Item> {

    public ItemDao(Connection connection) {
        super(Item.class, connection);
    }

    @Override
    public int update(int id, Item replacement) {
        PreparedStatement stat = null;
        try {
            try {
                Map<String, Object> updates = new HashMap<String, Object>();
                if (replacement.getName() != null) {
                    updates.put("name", replacement.getName());
                }
                if (replacement.getFamily() != 0) {
                    updates.put("family", replacement.getFamily());
                }
                if (replacement.getUser() != 0) {
                    updates.put("user", replacement.getUser());
                }
                if (replacement.getPrice() != 0) {
                    updates.put("price", replacement.getPrice());
                }
                StringBuilder s = new StringBuilder("UPDATE Item I set ");
                int size = updates.size(), count = 0;
                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    count++;
                    String base;
                    if (entry.getValue() instanceof String) {
                        base = "%s='%s'";
                    } else {
                        base = "%s=%s";
                    }
                    String params = String.format(base, entry.getKey(), entry.getValue());
                    s.append(params);
                    if (count < size) {
                        s.append(",");
                    }
                }
                s.append(" WHERE I.id=").append(id);
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
    public int create(Item item) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("INSERT INTO Item(name, date, price, user, family) "
                    + "VALUES(?,?,?,?,?);");
                stat.setString(1, item.getName());
                if (item.getDate() == null){
                    stat.setTimestamp(2, null);
                } else {
                    stat.setTimestamp(2, new Timestamp(item.getDate().getTime()));
                }
                    //stat.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                stat.setDouble(3, item.getPrice());
                stat.setInt(4, item.getUser());
                stat.setInt(5, item.getFamily());
                return stat.executeUpdate();
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("Item hasn't been created.");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Item read(int id) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + " WHERE id=?;");
                stat.setInt(1, id);
                ResultSet rs = stat.executeQuery();
                Item object = null;
                if (rs.next()) {
                    object = new Item();
                    object.setId(rs.getInt("id"));
                    object.setName(rs.getString("name"));
                    object.setPrice(rs.getDouble("price"));
                    object.setUser(rs.getInt("user"));
                    object.setFamily(rs.getInt("family"));
                    object.setDate(rs.getTimestamp("date"));
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
    public List<Item> readAll() {
        PreparedStatement stat = null;
        List<Item> all = new ArrayList<Item>();
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + ";");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    item.setUser(rs.getInt("user"));
                    item.setFamily(rs.getInt("family"));
                    item.setDate(rs.getTimestamp("date"));
                    all.add(item);
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

    public List<Item> readItemsUserDates(int user, Timestamp start, Timestamp end) {
        PreparedStatement stat = null;
        List<Item> items = null;
        try{
            try {
                stat = conn.prepareStatement("SELECT * FROM Item I WHERE I.user=? AND I.date " +
                    "between ? AND ?;");
                stat.setInt(1, user);
                stat.setTimestamp(2, start);
                stat.setTimestamp(3, end);
                ResultSet rs = stat.executeQuery();
                items = new ArrayList<Item>();
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    item.setUser(rs.getInt("user"));
                    item.setFamily(rs.getInt("family"));
                    item.setDate(rs.getTimestamp("date"));
                    items.add(item);
                }
                return items;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return items;
        }
    }

    public List<Item> readItemsFamilyDates(int family, Timestamp start, Timestamp end) {
        PreparedStatement stat = null;
        List<Item> items = null;
        try{
            try {
                stat = conn.prepareStatement("SELECT * FROM Item I WHERE I.family=? AND I.date " +
                    "between ? AND ?;");
                stat.setInt(1, family);
                stat.setTimestamp(2, start);
                stat.setTimestamp(3, end);
                ResultSet rs = stat.executeQuery();
                items = new ArrayList<Item>();
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    item.setUser(rs.getInt("family"));
                    item.setFamily(rs.getInt("family"));
                    item.setDate(rs.getTimestamp("date"));
                    items.add(item);
                }
                return items;
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return items;
        }
    }
}
