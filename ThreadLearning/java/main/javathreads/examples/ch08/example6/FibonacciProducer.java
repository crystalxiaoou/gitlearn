package javathreads.examples.ch08.example6;

import java.util.concurrent.BlockingQueue;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 10:51
 */
public class FibonacciProducer implements Runnable {
    private static final int PRODUCER_SLEEP_TIME = 1000;
    private Thread thr;
    private BlockingQueue<Integer> queue;

    public FibonacciProducer(BlockingQueue<Integer> q){
        queue = q;
        thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            for(int x = 0; ;x++){
                Thread.sleep(PRODUCER_SLEEP_TIME);
                queue.put(new Integer(x));
                System.out.println("Produced request " + x);
            }
        } catch (InterruptedException ex){
        }
    }


}
