package javathreads.examples.ch14;

import java.util.*;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 10:58
 */
public class CollectionTest {
    private static final int TEST_TIME_BEFORE_TEST_COLLECTION = 10000;
    static int nLoops;

    public static void main(String[] args){
        nLoops = TEST_TIME_BEFORE_TEST_COLLECTION;
        doTest(new Vector());
        doTest(new ModVector());
        doTest(new ArrayList());
        doTest(Collections.synchronizedList(new ArrayList()));

        nLoops = Integer.parseInt(args[0]);

        System.out.println("Starting synchronzied vector test");
        cleanGC();
        Timestamp syncTS = new Timestamp();
        doTest(new Vector());
        syncTS.stop();
        System.out.println("Synchronized vector took " + syncTS);

        System.out.println("Starting unsynchronized vector test");
        cleanGC();
        Timestamp unsyncTS = new Timestamp();
        doTest(new ModVector());
        unsyncTS.stop();
        System.out.println("Unsynchronized vector took " + unsyncTS);

        double d = ((double) (syncTS.elapsedTime() - unsyncTS.elapsedTime())) / nLoops;
        System.out.println("Unsynchronized operation saves " + d + " " + syncTS.units() + " per call");

        System.out.println("Starting synchronized array list test");
        cleanGC();
        syncTS = new Timestamp();
        doTest(Collections.synchronizedList(new ArrayList()));
        syncTS.stop();
        System.out.println("Synchronized array list took " + syncTS);

        System.out.println("Starting unsynchronized array list test");
        cleanGC();
        unsyncTS = new Timestamp();
        doTest(new ArrayList());
        unsyncTS.stop();
        System.out.println("Unsynchronized array list took " + unsyncTS);
        d = ((double)(syncTS.elapsedTime() - unsyncTS.elapsedTime())) / nLoops;
        System.out.println("Unsynchronized operations saves " + d + " " + syncTS.units() + " per call");
    }

    private static void cleanGC() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    private static void doTest(List list) {
        Integer n = new Integer(0);
        for(int i = 0; i < nLoops; i++){
            list.add(n);
        }
    }
}
