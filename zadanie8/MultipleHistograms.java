//Julia Zezula grudzien 2022

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


class HistogramNotify implements Runnable {
    Consumer<Histogram.HistogramResult> histogramConsumer;
    int vectorID;
    HashMap<Integer, Integer> histogramData;

    public HistogramNotify(Consumer<Histogram.HistogramResult> histogramConsumer, int vectorID, HashMap<Integer, Integer> histogramData) {
        this.histogramConsumer = histogramConsumer;
        this.vectorID = vectorID;
        this.histogramData = histogramData;
    }

    public void run() {
       // System.out.println("HistogramNotify" + Thread.currentThread().getName());
        synchronized (histogramConsumer) {
            histogramConsumer.accept(new Histogram.HistogramResult(vectorID, histogramData));
        }
    }

}

class HistogramTask implements Runnable {
    Consumer<Histogram.HistogramResult> histogramConsumer;
    ExecutorService notifyService;
    int vectorID;
    Vector data;


    public HistogramTask(ExecutorService notifyService, Consumer<Histogram.HistogramResult> histogramConsumer, int vectorID, Vector data) {
        this.histogramConsumer = histogramConsumer;
        this.vectorID = vectorID;
        this.data = data;
        this.notifyService = notifyService;
    }

    public void run() {
        //System.out.println("HistogramQueue" + Thread.currentThread().getName());
        int vectorSize = data.getSize();
        HashMap<Integer, Integer> histogramData = new HashMap<Integer, Integer>();

        for (int i = 0; i < data.getSize(); i++) {
            pumpValue(histogramData, data.getValue(i));
        }
        notifyService.submit(new HistogramNotify(histogramConsumer, vectorID, histogramData));
    }

    protected void pumpValue(HashMap<Integer, Integer> histogramData, int a) {
        Integer key = Integer.valueOf(a);
        Integer value = histogramData.get(key);
        if (value != null) {
            histogramData.put(key, Integer.valueOf(value + 1));
        } else {
            histogramData.put(key, Integer.valueOf(1));
        }
    }

}


public class MultipleHistograms implements Histogram {
    Consumer<Histogram.HistogramResult> histogramConsumer = null;

    Map last = null;
    ExecutorService queueService = Executors.newFixedThreadPool(10);
    ExecutorService notifyService = Executors.newSingleThreadExecutor();
    int bins = 0;


    //metoda ustala sposob generowania histogramow
    //bins = max liczba koszykow
    //histogramConsumer = obiekt do ktorego przekazujemt wyliczone histogramy
    public void setup(int bins, Consumer<Histogram.HistogramResult> histogramConsumer) {
        this.bins = bins;
        this.histogramConsumer = histogramConsumer;

    }

    //Metoda przekazuje wektor liczb całkowitych do przetworzenia na histogram
    //asynchroniczne uruchomienie sie obliczen
    public void addVector(int vectorID, Vector vector) {
        queueService.submit(new HistogramTask(notifyService, histogramConsumer, vectorID, vector));
    }

}