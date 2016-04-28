package app.dao;

import app.entity.Family;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FamilyDao extends GenericDao<Family> {

    public FamilyDao(Connection connection) {
        super(Family.class, connection);
    }

    //user should be able to update familyName, password, email, secret and answer
    @Override
    public int update(int id, Family replacement) {
        PreparedStatement stat = null;
        try {
            try {
                String field = null, value = null;
                if (replacement.getAnswer() != null) {
                    field = "answer";
                    value = replacement.getAnswer();
                } else if (replacement.getfName() != null) {
                    field = "fName";
                    value = replacement.getfName();
                } else if (replacement.getfPassword() != null) {
                    field = "fPassword";
                    value = replacement.getfPassword();
                } else if (replacement.getEmail() != null) {
                    field = "email";
                    value = replacement.getEmail();
                } else if (replacement.getQuestion() != null) {
                    field = "question";
                    value = replacement.getQuestion();
                }
                stat = conn.prepareStatement(String.format(
                    "UPDATE Family F set %s=? WHERE F.id=?", field));
                stat.setString(1, value);
                stat.setInt(2, id);
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
            System.out.println("Family can't be removed.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @return id which is assigned to new Family. Part of project logic. Familu members should
     * know their family Id and password as well as email.
     */
    @Override
    public int create(Family family) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("INSERT INTO Family(fName, fPassword, " +
                    "email, question, answer) VALUES(?,?,?,?,?);");
                stat.setString(1, family.getfName());
                stat.setString(2, family.getfPassword());
                stat.setString(3, family.getEmail());
                stat.setString(4, family.getQuestion());
                stat.setString(5, family.getAnswer());
                if (stat.executeUpdate() == 1) {
                    return readId(family);
                } else {
                    System.out.println("Family was not created.");
                    return -1;
                }
            } finally {
                if (stat != null) {
                    stat.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("Family was not created.");
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Family read(int id) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + " WHERE id=?;");
                stat.setInt(1, id);
                ResultSet rs = stat.executeQuery();
                Family object = null;
                if (rs.next()) {
                    object = new Family();
                    object.setId(rs.getInt("id"));
                    object.setfName(rs.getString("fName"));
                    object.setfPassword(rs.getString("fPassword"));
                    object.setQuestion(rs.getString("question"));
                    object.setAnswer(rs.getString("answer"));
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

    private int readId(Family family) {
        PreparedStatement stat = null;
        try {
            try {
                stat = conn.prepareStatement("SELECT id FROM " + table + " WHERE email=?;");
                stat.setString(1, family.getEmail());
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

    /**
     * Reads whe whole table.
     *
     * @return whole table
     */
    public List<Family> readAll() {
        PreparedStatement stat = null;
        List<Family> all = new ArrayList<Family>();
        try {
            try {
                stat = conn.prepareStatement("SELECT * FROM " + table + ";");
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    Family family = new Family();
                    family.setId(rs.getInt("id"));
                    family.setfName(rs.getString("fName"));
                    family.setfPassword(rs.getString("fPassword"));
                    family.setEmail(rs.getString("email"));
                    family.setQuestion(rs.getString("question"));
                    family.setAnswer(rs.getString("answer"));
                    all.add(family);
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
}
