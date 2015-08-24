package javathreads.examples.ch08.example6;

import java.util.concurrent.BlockingQueue;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 10:59
 */
public class FibonacciConsumer implements Runnable {
    private Fibonacci fib = new Fibonacci();
    private Thread thr;
    private BlockingQueue<Integer> queue;

    public FibonacciConsumer(BlockingQueue<Integer> queue){
        this.queue = queue;
        thr = new Thread(this);
        thr.start();
    }

    public void run(){
        int request, result;
        try {
            while(true){
                request = queue.take().intValue();
                result = fib.calculateWithCache(request);
                System.out.println("Calculated result of " + result + " from " + request);
            }
        } catch (InterruptedException ex){
        }
    }
}


