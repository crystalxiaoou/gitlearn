package javathreads.examples.ch08.example6;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 11:04
 */
public class FibonacciTest  {

    private static final int BLOCKING_QUEUE_SIZE = 10;

    public static void main(String[] args){
        ArrayBlockingQueue<Integer> queue;
        queue = new ArrayBlockingQueue<Integer>(BLOCKING_QUEUE_SIZE);
        new FibonacciProducer(queue);

        int nThreads = Integer.parseInt(args[0]);
        for(int i = 0; i < nThreads; i++){
            new FibonacciConsumer(queue);
        }
    }
}


