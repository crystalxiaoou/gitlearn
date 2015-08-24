package javathreads.examples.ch10.example1;

import com.sun.javafx.image.IntPixelGetter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 15:18
 */
public class ThreadPoolTest {

    private static final long KEEP_ALIVE_TIME = 50000L;

    public static void main(String[] args){
        int nTasks = Integer.parseInt(args[0]);
        long n = Long.parseLong(args[1]);
        int tpSize = Integer.parseInt(args[2]);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                tpSize, tpSize, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        Task[] tasks = new Task[nTasks];
        for(int i = 0; i < nTasks; i++){
            tasks[i] = new Task(n, "Task " + i);
            threadPoolExecutor.execute(tasks[i]);
        }
        threadPoolExecutor.shutdown();
    }
}
