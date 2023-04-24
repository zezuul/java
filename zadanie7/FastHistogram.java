//Julia Zezula grudzien 2022

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.min;

class HistogramQueue implements Runnable {
    Vector data;
    FastHistogram f;
    ExecutorService executorService;
    HashMap<Integer, Integer> histogramData = null;

    public HistogramQueue(FastHistogram f) {
        this.f = f;
    }

    protected void storeResult(Map<Integer, Integer> res) {
        synchronized (histogramData) {
            for (Integer key : res.keySet()) {
                // System.out.println(Thread.currentThread().getName() + ": " + key + " - " + res.get(key));
                Integer val = histogramData.get(key);
                if (val == null) {
                    histogramData.put(key, res.get(key));
                } else {
                    histogramData.put(key, Integer.valueOf(val + res.get(key)));
                }
            }
        }
    }

    public void run() {
        synchronized (f.input) {
            data = f.input.poll();
        }

        int vectorSize = data.getSize();
        int batchSize = Integer.min(vectorSize / f.threads, 100);
        HistogramFragmentGenerator t;

        histogramData = new HashMap<Integer, Integer>();
        executorService = Executors.newFixedThreadPool(f.threads);
        for (int i = 0; i < vectorSize; i += batchSize) {
            t = new HistogramFragmentGenerator(data, i, min(i + batchSize, vectorSize), this);
            executorService.submit(t);
        }
        executorService.shutdown();

        while (!executorService.isTerminated()) {
            try {
                //System.out.println("And Waiting");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        f.completeHistogram(histogramData);

    }

}

class HistogramFragmentGenerator implements Runnable {

    Vector data;
    int start, end;
    HistogramQueue histoQ;
    HashMap<Integer, Integer> resultFragment = null;

    public HistogramFragmentGenerator(Vector data, int start, int end, HistogramQueue f) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.histoQ = f;

    }

    public void run() {
        // System.out.println(Thread.currentThread().getName() + ": " + start + " to " + end);
        resultFragment = new HashMap<Integer, Integer>();
        for (int i = start; i < end; i++) {
            pumpValue(data.getValue(i));
        }
        histoQ.storeResult(resultFragment);
    }

    protected void pumpValue(int a) {
        Integer key = Integer.valueOf(a);
        Integer value = resultFragment.get(key);
        if (value != null) {
            resultFragment.put(key, Integer.valueOf(value + 1));
        } else {
            resultFragment.put(key, Integer.valueOf(1));
        }
    }

}


public class FastHistogram implements Histogram {

    ArrayDeque<Map> results = new ArrayDeque<Map>();
    ArrayDeque<Vector> input = new ArrayDeque<Vector>();

    Map last = null;
    ExecutorService queueService = Executors.newSingleThreadExecutor();
    int threads = 0;

    public void setup(int threads, int bins) {
        this.threads = threads;

    }

    protected void completeHistogram(Map histogramData) {
        synchronized (results) {
            results.push(histogramData);
        }
    }

    public void setVector(Vector vector) {
        input.push(vector);
        queueService.submit(new HistogramQueue(this));
    }

    public boolean isReady() {
        return !results.isEmpty();
    }

    public Map<Integer, Integer> histogram() {
        synchronized (results) {
            Map x = results.poll();
            if (x != null)
            {
                last = x;
            }
            return last;
        }
    }
}
