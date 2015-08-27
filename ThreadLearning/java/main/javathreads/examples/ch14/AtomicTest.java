package javathreads.examples.ch14;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 11:12
 */
public class AtomicTest {
    private static final int EXECUTE_TIME_BEFORE_TEST = 10000;
    static int nLoops;
    static int nThreads;

    public static void main(String[] args){
        nLoops = EXECUTE_TIME_BEFORE_TEST;
        nThreads = 1;
        doTest(new AtomicRunnable());
        doTest(new SyncRunnable());

        nLoops = Integer.parseInt(args[0]);
        nThreads = Integer.parseInt(args[1]);

        System.out.println("Starting atomic test");
        cleanGC();
        Timestamp atomicTS = new Timestamp();
        doTest(new AtomicRunnable());
        atomicTS.stop();
        System.out.println("Atomic took " + atomicTS);

        System.out.println("Starting sync test");
        cleanGC();
        Timestamp syncTS = new Timestamp();
        doTest(new SyncRunnable());
        syncTS.stop();
        System.out.println("Local sync took " + syncTS);

        double d = ((double)(syncTS.elapsedTime() - atomicTS.elapsedTime())) / (nLoops * nThreads);

        System.out.println("Atomic operation saves " + d + " " + syncTS.units() + " per call");
    }

    private static void cleanGC() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    static class AtomicRunnable implements Runnable {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        public void run(){
            for(int i = 0; i < nLoops; i++){
                atomicInteger.incrementAndGet();
            }
        }
    }

    static class SyncRunnable implements Runnable {
        int testVar;

        synchronized void incrVar(){
            testVar++;
        }

        public void run(){
            for(int i = 0;i < nLoops; i++)
                incrVar();
        }
    }

    static void doTest(Runnable r){
        Thread threads[] = new Thread[nThreads];
        for(int i = 0; i < nThreads; i++){
            threads[i] = new Thread(r);
        }

        for(int i = 0; i < nThreads; i++){
            threads[i].start();
        }

        for(int i = 0; i < nThreads; i++){
            try {
                threads[i].join();
            } catch(InterruptedException ie){
            }
        }
    }
}
