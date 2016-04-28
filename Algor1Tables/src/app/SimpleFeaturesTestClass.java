package app;

import junit.framework.Assert;
import org.testng.annotations.Test;

public class SimpleFeaturesTestClass {
    @Test(enabled = true)
    public void testEquality(){
        //equals only if mod and value are equal
        Record r1 = new Record(100);
        Record r2 = new Record(100);

        Assert.assertTrue("Shouldn't be equal", !r1.equals(r2));

        r1.getKey().setMod(1000);
        r2.getKey().setMod(1000);

        Assert.assertTrue("Should be equal", r1.equals(r2));
    }

    @Test(enabled = true)
    public void testSimilarity(){
        Key k1 = new Key(100);
        Key k2 = new Key(200);

        SimilarityUtil.SIMILARITY_FACTOR = 0.70;
        Assert.assertTrue("Should be similar.", k1.isSimilar(k2));

        SimilarityUtil.SIMILARITY_FACTOR = 0.85;
        Assert.assertTrue("Shouldn't be similar.", !k1.isSimilar(k2));

        String str = k2.getStr();
        System.out.println("Was: " + str);
        str = str.substring(0, str.length()/2) + "zz" +
                str.substring(str.length()/2, str.length());
        k2.setStr(str);
        System.out.println("Now :" + k2.getStr());
        //the same factor
        SimilarityUtil.SIMILARITY_FACTOR = 0.70;
        Assert.assertTrue("Shouldn't be similar.", !k1.isSimilar(k2));
    }
}
