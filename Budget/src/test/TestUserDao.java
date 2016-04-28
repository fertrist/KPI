package test;

import app.dao.FamilyDao;
import app.dao.UserDao;
import app.entity.Family;
import app.entity.User;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUserDao extends TestDao {
    private FamilyDao familyDao;
    private UserDao userDao;
    private User jakeson;

    @BeforeMethod(alwaysRun = true)
    public void createTable() {
        familyDao = new FamilyDao(conn);
        Assert.assertEquals(familyDao.createTable(), 0);
        userDao = new UserDao(conn);
        Assert.assertEquals(userDao.createTable(), 0);
        Family jakesons = new Family("Jakeson", "jakesonPassword", "jakesons@gmail.com",
            "Mom age", "60");
        Assert.assertEquals(familyDao.create(jakesons), 1);
        jakeson = new User("John", "Jakeson", Integer.toString(1), "jonh.jakeson@gmail.com");
    }

    @AfterMethod(alwaysRun = true)
    public void dropTable() {
        Assert.assertEquals(userDao.dropTable(), 0);
        Assert.assertEquals(familyDao.dropTable(), 0);
    }

    /**
     * Init table, insert a value, drop table.
     */
    @Test
    public void testInitDropBase() {
        Assert.assertEquals(userDao.create(jakeson), 1);
    }

    /**
     * Attempt to create users with similar emails should fail.
     */
    @Test
    public void testNonUniqueEmail() {
        Assert.assertEquals(userDao.create(jakeson), 1);
        jakeson.addFamily(2);
        Assert.assertEquals(userDao.create(jakeson), 0);
    }

    /**
     * Insert and then read the user.
     */
    @Test
    public void testCreateRead() {
        int id = userDao.create(jakeson);
        System.out.println("New user's id is " + id);
        Assert.assertEquals(id, 1, "Invalid Id");
        User user = userDao.read(id);
        Assert.assertEquals(user.getName(), jakeson.getName());
        Assert.assertEquals(user.getSurname(), jakeson.getSurname());
        Assert.assertEquals(user.getFamilies(), jakeson.getFamilies());
        Assert.assertEquals(user.getEmail(), jakeson.getEmail());
    }

    /**
     * Check that all inserted users are present in read-all response.
     */
    @Test
    public void readAll() {
        //insert 10 users
        List<User> inputUsers = new ArrayList<User>();
        String email = jakeson.getEmail();
        for (int i = 1; i <= 10; i++) {
            jakeson.setEmail(email + i);
            Assert.assertEquals(userDao.create(jakeson), 1);
            inputUsers.add(jakeson);
        }
        //check results
        List<User> outputUsers = userDao.readAll();
        System.out.println("Output list \n: " + outputUsers);
        Assert.assertTrue(outputUsers.containsAll(inputUsers));
    }

    /**
     * Create a user, change brief value. Read it and check it was changed.
     */
    @Test
    public void testUpdate() {
        Assert.assertEquals(userDao.create(jakeson), 1);
        int id = userDao.readId(jakeson);
        jakeson.setName("Robert");
        jakeson.setSurname("Robertson");
        Assert.assertEquals(userDao.update(id, jakeson), 1);
        User user = userDao.read(id);
        Assert.assertEquals(user.getEmail(), jakeson.getEmail());
        Assert.assertEquals(user.getName(), jakeson.getName());
        Assert.assertEquals(user.getSurname(), jakeson.getSurname());
        Assert.assertEquals(user.getFamilies(), jakeson.getFamilies());
    }

    /**
     * Check that entity and dao work as expected.
     */
    @Test
    public void testUpdateFamilies() {
        Assert.assertEquals(userDao.create(jakeson), 1);
        int id = userDao.readId(jakeson);
        jakeson.addFamily(2, 3, 4, 5);
        Assert.assertEquals(userDao.update(id, jakeson), 1);
        User user = userDao.read(id);
        System.out.println("Local user: " + jakeson);
        System.out.println("In DB table: " + user);
        Assert.assertEquals(user.getEmail(), jakeson.getEmail());
        Assert.assertEquals(user.getName(), jakeson.getName());
        Assert.assertEquals(user.getSurname(), jakeson.getSurname());
        Assert.assertEquals(user.getFamilies(), jakeson.getFamilies());
        Assert.assertEquals(user.getFamilies(), "1,2,3,4,5");
    }

    /**
     * Attempt to assign existing email to another user should fail.
     */
    @Test
    public void testUpdateNonUnique() {
        Assert.assertEquals(userDao.create(jakeson), 1);
        User user = new User("Robert", "Robertson", "2", "robert@gmail.com");
        Assert.assertEquals(userDao.create(user), 1);
        int id = userDao.readId(user);
        user.setEmail(jakeson.getEmail());
        Assert.assertEquals(userDao.update(id, user), 0);
    }

    /**
     * Insert 10 users, remove all not odd. Check the state.
     */
    @Test
    public void testDelete() {
        //insert 10 users
        String email = jakeson.getEmail();
        for (int i = 1; i <= 10; i++) {
            jakeson.setEmail(email + i);
            Assert.assertEquals(userDao.create(jakeson), 1);
        }
        List<User> users = userDao.readAll();
        System.out.println("initial state : \n" + users);
        //remove all not odd users
        for (User u : users) {
            if (u.getId() % 2 == 1) {
                Assert.assertEquals(userDao.delete(u.getId()), 1);
            }
        }
        //check the state
        users = userDao.readAll();
        System.out.println("After removal : \n" + users);
        Assert.assertEquals(users.size(), 5);
        for (User u : users) {
            Assert.assertTrue(u.getId() % 2 == 0, "Should be only odd ids.");
        }
    }

    /**
     * Inserts amount of users with different families, sometimes combined.
     * Retrieves users by specific family, checks the result.
     */
    @Test
    public void testSelectUsersByFamily() {
        int [] families = new int[4];

        //add families
        for (int i = 2; i <= 3; i++) {
             Assert.assertEquals(familyDao.create(
                 new Family("Family" + i, "jakesonPassword" + i,
                     "family" + i + "@gmail.com", "Mom" + i + " age", "60" + i)
             ), i);
        }
        //create 100 users with random families, count them. Create some families with few
        // families in them, count them also.
        for(int i = 1; i <= 100; i++) {
            Random random = new Random();
            int family = random.nextInt(4);
            if (family == 0) family = 1;
            if (family == 1) {
                families[1]++;
            } else if (family == 2) {
                families[2]++;
            } else {
                families[3]++;
            }
            User user = new User("user" + i, "userson" + i, "", "user" + i + "@gmail.com");
            user.addFamily(family);
            if (i % 2 == 0 && family != 2) {
                user.addFamily(2);
                families[2]++;
            }
            Assert.assertEquals(userDao.create(user), 1);
        }
        List<User> users;
        for(int i = 1; i <= 3; i++) {
            users = userDao.readUsersByFamily(i);
            Assert.assertEquals(users.size(), families[i]);
            containsFamily(users, i);
        }
    }

    private void containsFamily(List<User> users, int family) {
        for(User u : users){
            Assert.assertTrue(u.getFamilies().contains("" + family));
        }
    }

}
