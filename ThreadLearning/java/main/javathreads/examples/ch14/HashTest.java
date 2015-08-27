package javathreads.examples.ch14;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 11:24
 */
public class HashTest implements Runnable {
    private static final int EXECUTE_TIME_BEFORE_TEST = 10000;
    static int nLoops;
    static int nThreads;

    public static void main(String[] args){
        nLoops = EXECUTE_TIME_BEFORE_TEST;
        doTest(new Hashtable());
        doTest(new HashMap());
        doTest(new ConcurrentHashMap());

        nLoops = Integer.parseInt(args[0]);

        System.out.println("Starting standard hashmap test");
        cleanGC();
        Timestamp hashTS = new Timestamp();
        doTest(new HashMap());
        hashTS.stop();
        System.out.println("Standard hashmap (1 thread) took " + hashTS);

        System.out.println("Starting standard hashtable test");
        cleanGC();
        Timestamp hashtableTS = new Timestamp();
        doTest(new Hashtable());
        hashtableTS.stop();
        System.out.println("Standard hashtable (1 thread) took " + hashtableTS);

        System.out.println("Starting concurrent hashmap test");
        cleanGC();
        Timestamp concTS = new Timestamp();
        doTest(new ConcurrentHashMap());
        concTS.stop();
        System.out.println("Concurrent hashmap (1 thread) took " + concTS);

        nThreads = Integer.parseInt(args[1]);

        System.out.println("Starting standard hashmap test");
        cleanGC();
        Timestamp hashNTS = new Timestamp();
        doTest(new HashMap());
        hashNTS.stop();
        System.out.println("Standard hashmap (" +  nThreads +  " thread) took " + hashNTS);

        System.out.println("Starting standard hashtable test");
        cleanGC();
        Timestamp hashtableNTS = new Timestamp();
        doTest(new Hashtable());
        hashtableNTS.stop();
        System.out.println("Standard hashtable (" +  nThreads +  " thread) took " + hashtableNTS);

        System.out.println("Starting concurrent hashmap test");
        cleanGC();
        Timestamp concNTS = new Timestamp();
        doTest(new ConcurrentHashMap());
        concNTS.stop();
        System.out.println("Concurrent hashmap (" + nThreads + "thread) took " + concNTS);

        double d =((double)(hashtableTS.elapsedTime() - hashTS.elapsedTime())) / nLoops;

        System.out.println("Hashmap saves " + d + " " + hashTS.units() + " per call vs. hashtable");

        d = ((double)(hashTS.elapsedTime() - concTS.elapsedTime())) / nLoops;
        System.out.println("Unsynchronized operation (1 thread) save " + d + " " + concTS.units() + " per call");

        d = ((double)(hashNTS.elapsedTime() - concNTS.elapsedTime())) / (nLoops * nThreads);

        System.out.println("Unsynchronized operation (" + nThreads + " threads saves " + d + " " + concNTS.units() +
        " per call");
    }

    private static void cleanGC(){
        System.gc();
        System.runFinalization();
        System.gc();
    }

    private static void doTest(Map m){
        targetMap = m;
        Thread[] t = new Thread[nThreads];
        Runnable r = new HashTest();
        for(int i = 0; i < nThreads; i++){
            t[i] = new Thread(r);
        }

        for(int i = 0; i < nThreads; i++){
            t[i].start();
        }

        for(int i = 0; i < nThreads; i++){
            try {
                t[i].join();
            } catch (InterruptedException ie){
            }
        }
    }

    static Map targetMap;
    public void run() {
        for(int i = 0; i < nLoops; i++){
            Integer I = new Integer(i);
            targetMap.put(I, I);
            targetMap.get(I);
        }
    }
}
