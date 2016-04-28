package app;

import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class DirectAccessActionsTestClass {

    Table table;
    int total = 10;

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method){
        System.out.println("Test: " + method.getName());
        table = new Table();
        createTestData(table, total);
    }

    @Test(enabled = true)
    public void testSimpleInsertion(){
        //test insertion by ascending indexes
        Assert.assertEquals("Table should have " + total + " records", table.countRecords(), total);
    }

    @Test(enabled = true)
    public void testInsertionInMidth(){
        //tests insertion in the midth of filled table.
        //tests shifting works properly
        Record record = new Record(100);
        Record lastRecord = table.select(total-1);
        Assert.assertTrue(table.insert(record, total / 2));

        System.out.println("When value was inserted. ");
        System.out.println(table);

        System.out.println(total / 2 + " : " + table.select(total / 2));
        System.out.println(total + ": " + table.select(total));

        //checks if record was really inserted
        Assert.assertEquals("Should be the same value. ", 0, table.select(total/2).compareTo(record));
        //checks if last record was shifted correctly
        Assert.assertEquals("Should be the same value", 0, table.select(total).compareTo(lastRecord));
    }

    @Test(enabled = true)
    public void testRemoving(){
        //checks if table was filled
        Assert.assertEquals("Table should have " + total + " records", table.countRecords(), total);

        int instancesToRemove = 3;
        for(int i = 0; i < instancesToRemove; i++){
            table.delete(i);
            System.out.println(i + " removed");
        }

        System.out.println(instancesToRemove + " items removed: ");
        System.out.println(table);
        Assert.assertEquals("Table should have " + (total-instancesToRemove) + " records", table.countRecords(), total-instancesToRemove);
    }

    @Test(enabled = true)
    public void updating_test(){
        int n = 3;
        Record record = table.select(n);
        record.getFunctional().setValue(300);
        Assert.assertTrue("Shouldn't be equal", !record.equals(table.select(n)));
        table.update(record, n);
        //checks if record was updated
        Assert.assertTrue("Should be equal", record.equals(table.select(n)));
    }

    public static void createTestData(Table table, int n){
        Record record;
        int begin = 100;
        int end = begin + n * 10;
        for(int i = begin; i < end; i = i+10){
            record = new Record(i);
            table.insert(record);
        }
        System.out.println("Filled table: ");
        System.out.println(table);
    }

}
