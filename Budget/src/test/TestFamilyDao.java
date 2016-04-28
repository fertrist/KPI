package test;

import app.dao.FamilyDao;
import app.entity.Family;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestFamilyDao extends TestDao {
    private FamilyDao familyDao;
    private Family jakesons;

    @BeforeMethod(alwaysRun = true)
    public void setTable() {
        familyDao = new FamilyDao(conn);
        jakesons = new Family("Jakeson", "jakesonPassword", "jakesons@gmail.com",
            "Mom age", "60");
        Assert.assertEquals(familyDao.createTable(), 0);
    }

    @AfterMethod
    public void dropTable() {
        familyDao.dropTable();
    }

    @Test
    public void testInitDropDataBase() throws IOException, SQLException {
        System.out.println(familyDao.create(jakesons));
    }

    /**
     * Create and read single family check if returned Id is correct and then retrieve
     * family by its Id and check if all field match.
     */
    @Test
    public void testCreate() {
        int id = familyDao.create(jakesons);
        System.out.println("new Family Id is " + id);
        Assert.assertEquals(id, 1, "Invalid Id!");
        Family family1 = familyDao.read(id);
        Assert.assertEquals(family1.getfName(), jakesons.getfName());
        Assert.assertEquals(family1.getfPassword(), jakesons.getfPassword());
        Assert.assertEquals(family1.getEmail(), jakesons.getEmail());
        Assert.assertEquals(family1.getQuestion(), jakesons.getQuestion());
        Assert.assertEquals(family1.getAnswer(), jakesons.getAnswer());
    }

    /**
     * Test unique values
     */
    @Test
    public void testCreateNonUnique() {
        Assert.assertEquals(familyDao.create(jakesons), 1, "Should succeed.");
        jakesons.setEmail(jakesons.getEmail() + 1);
        Assert.assertEquals(familyDao.create(jakesons), 2, "Should succeed.");
        Assert.assertEquals(familyDao.create(jakesons), -1, "Attempt to create family " +
            "with non unique email should fail.");
    }

    @Test
    public void testUpdate() throws SQLException, IOException {
        jakesons.setEmail(jakesons.getEmail() + 1);
        Assert.assertEquals(familyDao.create(jakesons), 1, "Should succeed.");
        jakesons.setEmail(jakesons.getEmail() + 2);
        Assert.assertEquals(familyDao.create(jakesons), 2, "Should succeed.");
        jakesons.setEmail(jakesons.getEmail() + 3);
        Assert.assertEquals(familyDao.create(jakesons), 3, "Should succeed.");
        String newPassword = "newPassword";
        Family newFamily = new Family(null, newPassword, null, null, null);
        System.out.println(familyDao.update(2, newFamily));
        newFamily = familyDao.read(2);
        Assert.assertEquals(newFamily.getfName(), jakesons.getfName());
        Assert.assertEquals(newFamily.getfPassword(), newPassword);
        Assert.assertEquals(newFamily.getQuestion(), jakesons.getQuestion());
        Assert.assertEquals(newFamily.getAnswer(), jakesons.getAnswer());
    }

    @Test
    public void testUpdateExistingEmail() throws SQLException, IOException {
        jakesons.setEmail(jakesons.getEmail() + 1);
        Assert.assertEquals(familyDao.create(jakesons), 1, "Should succeed.");
        jakesons.setEmail(jakesons.getEmail() + 2);
        String existingEmail = jakesons.getEmail();
        Assert.assertEquals(familyDao.create(jakesons), 2, "Should succeed.");
        jakesons.setEmail(jakesons.getEmail() + 3);
        Assert.assertEquals(familyDao.create(jakesons), 3, "Should succeed.");
        Family newFamily = new Family();
        newFamily.setEmail(existingEmail);
        Assert.assertEquals(familyDao.update(3, newFamily), 0, "Update should fail.");
    }

    @Test(enabled = true)
    public void testReadAll() {
        List<Family> familyList = new ArrayList<Family>();
        Family currentFamily;
        jakesons.setEmail(jakesons.getEmail() + 1);
        int id = familyDao.create(jakesons);
        Assert.assertEquals(id, 1, "Should succeed.");
        currentFamily = new Family(jakesons);
        currentFamily.setId(id);
        familyList.add(currentFamily);

        jakesons.setEmail(jakesons.getEmail() + 2);
        id = familyDao.create(jakesons);
        Assert.assertEquals(id, 2, "Should succeed.");
        currentFamily = new Family(jakesons);
        currentFamily.setId(id);
        familyList.add(currentFamily);

        jakesons.setEmail(jakesons.getEmail() + 3);
        id = familyDao.create(jakesons);
        Assert.assertEquals(id, 3, "Should succeed.");
        currentFamily = new Family(jakesons);
        currentFamily.setId(id);
        familyList.add(currentFamily);

        List<Family> table = familyDao.readAll();
        System.out.println(table);
        Assert.assertEquals(table.size(), 3);
        Assert.assertTrue(table.containsAll(familyList));
    }

    @Test
    public void testDelete() {
        //create 3 families
        List<Family> familyList = new ArrayList<Family>();
        String email = jakesons.getEmail();
        for (int i = 1; i <= 20; i++) {
            Family family = new Family(jakesons);
            family.setEmail(email + i);
            Assert.assertEquals(familyDao.create(family), i, "Should succeed.");
            family.setId(i);
            familyList.add(family);
        }
        //remove 2nd
        for (int i = 1; i <= 20; i++) {
            if (i % 2 == 0) {
                int index = i;
                for (Family f : familyList) {
                    if (f.getId() == i) {
                        index = familyList.indexOf(f);
                    }
                }
                familyList.remove(index);
                Assert.assertEquals(familyDao.delete(i), 1, "Should succeed.");
                List<Family> table = familyDao.readAll();
                Assert.assertEquals(table.size(), familyList.size());
                Assert.assertTrue(table.containsAll(familyList));
            }
        }
    }
}
