package app;

import org.junit.Assert;
import org.junit.Test;

public class TestClass {

    Allocator allocator;

    @Test
    public void allocateSomeBlocks(){
        allocator = new Allocator();
        allocator.report();
        int tests = 5;
        long [] addresses = new long[tests+1];
        for(int i = 0; i <= tests; i++){
            addresses[i] = allocator.allocateMemory((short) (100 + i));
        }
        allocator.report();
    }

    @Test
    public void testRegularScenario(){
        allocator = new Allocator();
        allocator.report();
        long address = allocator.allocateMemory((short) 100);
        Assert.assertNotEquals("Memory should have been allocated!", address, -1);
        allocator.report();
        allocator.showContent(address);
        allocator.writeDataToBlock(address, new byte []{100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110});
        allocator.showContent(address);
        address = allocator.reallocateMemory(address + 3, 150);
        allocator.report();
        allocator.showContent(address);
        address = allocator.reallocateMemory(address, 10);
        allocator.report();
        allocator.showContent(address);
    }

    @Test
    public void testMultiDomainScenario(){
        Allocator allocator = new Allocator();
        allocator.report();
        long address = allocator.allocateMemory(Allocator.DOMAIN_SIZE);
        Assert.assertEquals(-1, address);
        allocator.report();
        address = allocator.allocateMemory((short) (Allocator.DOMAIN_SIZE-Allocator.LIMIT*3));
        allocator.report();
        allocator.writeDataToBlock(address, new byte[]{100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110});
        allocator.showContent(address);
        address = allocator.reallocateMemory(address, 100);
        allocator.report();
        allocator.showContent(address);
    }
}
