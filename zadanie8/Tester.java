
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class Tester {
    public static int cnt = 0;
    @Test
    public void test1() {
        VectorImpl vectorTester = new VectorImpl();
        MultipleHistograms fh = new MultipleHistograms();
        Consumer<Histogram.HistogramResult> histogramConsumer = a ->  Tester.printRT(a);
                //printRT(a);

        Consumer<Integer> display = a -> System.out.println(a);
        display.accept(10);

        fh.setup(1, histogramConsumer);
        fh.addVector(1, new VectorImpl());
       vectorTester.nextData();
        fh.addVector(2, vectorTester);
        fh.addVector(3, new VectorImpl());
        fh.addVector(4, new VectorImpl());
        fh.addVector(5, new VectorImpl());
        fh.addVector(6, new VectorImpl());
        fh.addVector(7, new VectorImpl());
        fh.addVector(8, new VectorImpl());

        fh.addVector(9, new VectorImpl());
        fh.addVector(10, new VectorImpl());
        fh.addVector(11, new VectorImpl());
        while(cnt < 11) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void printRT(Histogram.HistogramResult a)
    {
        System.out.println("START of: " + a.vectorID() + Thread.currentThread().getName());

        printHashMap("VectorID: " + a.vectorID(), a.histogram());
        cnt++;
    }
    public static void printHashMap(String title, Map<Integer,Integer> r)
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
