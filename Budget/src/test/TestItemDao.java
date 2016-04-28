package test;

import app.dao.FamilyDao;
import app.dao.ItemDao;
import app.dao.UserDao;
import app.entity.Family;
import app.entity.Item;
import app.entity.User;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestItemDao extends TestDao{

    private FamilyDao familyDao;
    private UserDao userDao;
    private ItemDao itemDao;
    private Item item;

    @BeforeMethod(alwaysRun = true)
    public void createTable() {
        familyDao = new FamilyDao(conn);
        Assert.assertEquals(familyDao.createTable(), 0);
        userDao = new UserDao(conn);
        Assert.assertEquals(userDao.createTable(), 0);
        itemDao = new ItemDao(conn);
        Assert.assertEquals(itemDao.createTable(), 0);

        //init tables
        Family jakesons = new Family("Jakeson", "jakesonPassword", "jakesons@gmail.com", "Mom age", "60");
        Assert.assertEquals(familyDao.create(jakesons), 1);
        User jakeson = new User("John", "Jakeson", Integer.toString(1), "jonh.jakeson@gmail.com");
        Assert.assertEquals(userDao.create(jakeson), 1);
        item = new Item("Pencil", 2.00, 1, 1);
    }

    @Test
    public void testCreateRead() {
        Assert.assertEquals(itemDao.create(item), 1);
        Item read = itemDao.read(1);
        Assert.assertEquals(read.getName(), item.getName());
        Assert.assertEquals(read.getFamily(), item.getFamily());
        Assert.assertEquals(read.getUser(), item.getUser());
        Assert.assertEquals(read.getPrice(), item.getPrice());
    }

    /**
     * Test insertion with fields(FK) which depend on another tables.
     */
    @Test
    public void testCreateNonExistentFamilyOrUser() {
        //normal insertion
        item.setName(item.getName() + 1);
        Assert.assertEquals(itemDao.create(item), 1);
        //repeat the same
        item.setName(item.getName() + 2);
        Assert.assertEquals(itemDao.create(item), 1);

        //set non-existent family
        item.setFamily(2);
        Assert.assertEquals(itemDao.create(item), 0, "Shouldn't be created.");

        //set non-existent user
        item.setUser(2);
        Assert.assertEquals(itemDao.create(item), 0, "Shouldn't be created.");

        //add second family and try again
        Family robertsons = new Family("Robertson", "robertsonPassword", "robertson@gmail.com",
            "Dad age", "70");
        Assert.assertEquals(familyDao.create(robertsons), 2);
        Assert.assertEquals(itemDao.create(item), 0, "Shouldn't be created.");

        //add second user and try again
        User robertson = new User("Robert", "Robertson", Integer.toString(2),
            "robert.robertson@gmail.com");
        Assert.assertEquals(userDao.create(robertson), 1);
        Assert.assertEquals(itemDao.create(item), 1, "Should be created.");
    }

    /**
     * Check update functionality.
     */
    @Test
    public void testUpdate() {
        //insert an item
        Assert.assertEquals(itemDao.create(item), 1);
        Item read = itemDao.read(1);
        Assert.assertEquals(read.getName(), item.getName());
        Assert.assertEquals(read.getFamily(), item.getFamily());
        Assert.assertEquals(read.getUser(), item.getUser());
        Assert.assertEquals(read.getPrice(), item.getPrice());
        System.out.println(read.getDate());

        //change some fields and update an item
        item.setPrice(3.5);
        item.setName("Pen");
        Assert.assertEquals(itemDao.update(1, item), 1);
        //check all fields
        read = itemDao.read(1);
        Assert.assertEquals(read.getName(), item.getName());
        Assert.assertEquals(read.getFamily(), item.getFamily());
        Assert.assertEquals(read.getUser(), item.getUser());
        Assert.assertEquals(read.getPrice(), item.getPrice());
        System.out.println(read.getDate());
    }

    /**
     * Try to update foreign key fields, which contain values which match to non-existent records
     * in other tables.
     */
    @Test
    public void testUpdateNonExistentForeignKeys() {
        item.setName(item.getName() + 1);
        Assert.assertEquals(itemDao.create(item), 1);

        //set non-existent family
        Item replacement = new Item();
        replacement.setFamily(2);
        Assert.assertEquals(itemDao.update(1, replacement), 0, "Shouldn't be updated.");

        //set non-existent user
        replacement.setUser(2);
        Assert.assertEquals(itemDao.update(1, replacement), 0, "Shouldn't be updated.");

        //add second family and try again
        Family robertsons = new Family("Robertson", "robertsonPassword", "robertson@gmail.com",
            "Dad age", "70");
        Assert.assertEquals(familyDao.create(robertsons), 2);
        replacement.setFamily(2);
        replacement.setUser(1);
        Assert.assertEquals(itemDao.update(1, replacement), 1, "Should be updated.");
        Item read = itemDao.read(1);
        Assert.assertEquals(read.getFamily(), replacement.getFamily());
        Assert.assertEquals(read.getUser(), replacement.getUser());

        //add second user and try again
        User robertson = new User("Robert", "Robertson", Integer.toString(2),
            "robert.robertson@gmail.com");
        Assert.assertEquals(userDao.create(robertson), 1);
        replacement.setFamily(2);
        replacement.setUser(2);
        Assert.assertEquals(itemDao.update(1, replacement), 1, "Should be updated.");
        read = itemDao.read(1);
        Assert.assertEquals(read.getFamily(), replacement.getFamily());
        Assert.assertEquals(read.getUser(), replacement.getUser());
    }

    @Test
    public void testDelete() {
        //insert 10 users
        String name = item.getName();
        for (int i = 1; i <= 10; i++) {
            item.setName(name + i);
            Assert.assertEquals(itemDao.create(item), 1);
        }
        List<Item> items = itemDao.readAll();
        System.out.println("initial state : \n" + items);
        //remove all not odd users
        for (Item i : items) {
            if (i.getId() % 2 == 1) {
                Assert.assertEquals(itemDao.delete(i.getId()), 1);
            }
        }
        //check the state
        items = itemDao.readAll();
        System.out.println("After removal : \n" + items);
        Assert.assertEquals(items.size(), 5);
        for (Item i : items) {
            Assert.assertTrue(i.getId() % 2 == 0, "Should be only odd ids.");
        }
    }

    /**
     * Check that all inserted items are present in response.
     */
    @Test
    public void readAll() {
        //insert 10 users
        List<Item> inputItems = new ArrayList<Item>();
        String name = item.getName();
        for (int i = 1; i <= 10; i++) {
            item.setName(name + i);
            Assert.assertEquals(itemDao.create(item), 1);
            inputItems.add(item);
        }
        //check results
        List<Item> outputItems = itemDao.readAll();
        System.out.println("Output list \n: " + outputItems);
        Assert.assertTrue(outputItems.containsAll(inputItems));
    }

    /**
     * Submit many items with different users. Reading items for specific user expecting
     * correct number of items which belong to mentioned user.
     */
    @Test(enabled = true)
    public void testGetUsersPurchases() {
        Timestamp start = new Timestamp(System.currentTimeMillis()-1000);
        //insert few users
        int n = 5;
        for(int i = 1; i <= n; i++){
            Assert.assertEquals(userDao.create(new User("User" + i, "Userson" + i, "" + 1,
                "user" + i + "@gmail.com")), 1);
        }
        int[] users = new int[n+1];
        //create 100 items, randomly set their users and families
        Random random = new Random();
        for(int i = 1; i <= 100; i++) {
            int id = random.nextInt(n + 1);
            if (id < 1 || id > n) {
                id = 2;
            }
            for (int j = 1; j <= n; j++) {
                if (id == j) {
                    users[id]++;
                }
            }
            Assert.assertEquals(itemDao.create(new Item("Item" + i, 0.30 + i, id, 1)), 1);
        }
        Timestamp end = new Timestamp(System.currentTimeMillis()+1000);
        //check results
        for(int i = 1; i <= n; i++) {
            List<Item> items = itemDao.readItemsUserDates(i, start, end);
            Assert.assertEquals(items.size(), users[i]);
            for(Item item : items) {
                Assert.assertEquals(item.getFamily(), 1);
                Assert.assertEquals(item.getUser(), i);
            }
        }
    }

    /**
     * Submit many items with different families and users. Read items for specific
     * family and check if expected items were retrieved (Should also belong to expected family).
     */
    @Test(enabled = true)
    public void testGetFamilyPurchases() {
        Timestamp start = new Timestamp(System.currentTimeMillis()-1000);
        //insert few families
        int n = 5;
        for(int i = 2; i <= n; i++) {
            String userson = "userson" + i;
            Assert.assertEquals(familyDao.create(new Family(userson, userson + "password",
                userson + "@gmail.com", userson + "Mom", "60" + i)), i);
        }
        //insert an appropriate users from each family
        for(int i = 2; i <= n; i++){
            String user = "user" + i;
            Assert.assertEquals(userDao.create(new User(user, "Userson" + i, "" + i,
                user + "@gmail.com")), 1);
        }
        int[] families = new int[n+1];
        //create 100 items, randomly set their users and families
        Random random = new Random();
        for(int i = 1; i <= 100; i++) {
            int id = random.nextInt(n + 1);
            if (id < 1 || id > n) {
                id = 2;
            }
            for (int j = 1; j <= n; j++) {
                if (id == j) {
                    families[id]++;
                }
            }
            Assert.assertEquals(itemDao.create(new Item("Item" + i, 0.30 + i, id, id)), 1);
        }
        Timestamp end = new Timestamp(System.currentTimeMillis()+1000);
        //check results
        for(int i = 1; i <= n; i++) {
            List<Item> items = itemDao.readItemsFamilyDates(i, start, end);
            Assert.assertEquals(items.size(), families[i]);
            for(Item item : items) {
                Assert.assertEquals(item.getFamily(), i);
                Assert.assertEquals(item.getUser(), i);
            }
        }
    }

    @Test
    public void testDateRanges() {

    }

    @AfterMethod(alwaysRun = true)
    public void dropTable() {
        Assert.assertEquals(itemDao.dropTable(), 0);
        Assert.assertEquals(userDao.dropTable(), 0);
        Assert.assertEquals(familyDao.dropTable(), 0);
    }

}
