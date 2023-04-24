
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class Tester {
    @Test
    public void test1() {
        Vector vectorTester = new VectorImpl();
        FastHistogram fh = new FastHistogram();
        fh.setup(3,4);
        fh.setVector(vectorTester);
        Vector vectorTester2 = new VectorImpl();
        ((VectorImpl)vectorTester2).nextData();
        fh.setVector(vectorTester2);


        while (!fh.isReady())
        {
            try {
               // System.out.println("Waiting");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Map<Integer, Integer> r  = fh.histogram();
      //  printHashMap("abc", r);
        assertEquals(Integer.valueOf(100), r.get(5));
        assertNull(r.get(23));



        while (!fh.isReady())
        {
            try {
             //   System.out.println("Waiting");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        r  = fh.histogram();


        assertEquals(Integer.valueOf(50), r.get(1));
        assertNull(r.get(5));
        assertEquals(Integer.valueOf(100), r.get(2));
      //  printHashMap("2nd", r);

    }

    public void printHashMap(String title, Map<Integer,Integer> r)
    {
        System.out.println("START of: " + title);
        for(Map.Entry<Integer, Integer> entry : r.entrySet()){
            Integer key = entry.getKey();
            //System.out.println(key);
            Integer value = entry.getValue();
            System.out.println(key + "=> " + value);
        }
        System.out.println("End of: " + title);
    }

}

class VectorImpl implements Vector{

    int data1 [] = {1,2,3,4,5,6,7,8,9,0,12,5};

    int data2 [] = {1,2,2,3,4,6,7,8,9,0,13};


    int data [] = data1;
    int repeat = 50;


    public void nextData()
    {
        data = data2;
    }

    public int getValue(int idx) {
        if (idx >= data.length * repeat)
        {
            throw new RuntimeException("Bum!");
        }
        return data [idx % data.length];
    }
    public int getSize() {
        return data.length * repeat;

    }
}
