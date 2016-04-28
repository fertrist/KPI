package app;

import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ComplicatedActionsTestClass {
    Table table;
    int total = 10;

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method){
        System.out.println("Test: " + method.getName());
        table = new Table();
        DirectAccessActionsTestClass.createTestData(table, total);
    }

    @Test(enabled = true)
    public void testSorting(){
        //add more records with smaller values
        System.out.println("More records: \n");
        DirectAccessActionsTestClass.createTestData(table, 5);
        table.sort();
        System.out.println("Sorted table: \n");
        System.out.println(table);

        //check that records are sorted in ascending order
        Record previous = table.select(0);
        for(Record record : table.getRecords()){
            if(record != null) Assert.assertTrue(record.compareTo(previous) >= 0);
            previous = record;
        }
    }

    @Test(enabled = true)
    public void testLinearSelection(){
        int n = 13;
        SimilarityUtil.CODE_SIMILARITY_RANGE = 0;
        //add more values
        System.out.println("More records...");
        DirectAccessActionsTestClass.createTestData(table, 7);

        Record record = table.select(n);
        System.out.println("Record chosen: " + record);
        Key key = record.getKey().clone();
        System.out.println("Key is: " + key);

        //change records order
        table.sort();
        System.out.println("Records order was changed : " );
        System.out.println(table);

        Assert.assertTrue("Should be equal", table.select(key).contains(record));
        System.out.println("Compare record: " + record + " with list : \n" + table.select(key));
    }

    @Test(enabled = true)
    public void testBinarySearch(){
        int n = 7;
        Key key = table.select(n).getKey().clone();
        System.out.println("Key is: " + key);

        List<Record> resultList = table.selectBinary(key);
        Assert.assertTrue("Should contain value", resultList.contains(table.select(n)));
        System.out.println("Compare list: " + resultList + " and record: " + table.select(n));
    }


}
