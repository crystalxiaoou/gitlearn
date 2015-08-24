package javathreads.examples.ch10.example2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 11:32
 */
public class Task implements Runnable{
    private long n;
    private String id;

    private long fib(long n){
        if(n == 0)
            return 0L;
        if(n == 1)
            return 1L;
        return fib(n - 1) + fib(n - 2);
    }

    public Task(long n, String id){
        this.n = n;
        this.id = id;
    }

    public void run(){
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
        long startTime = System.currentTimeMillis();
        d.setTime(startTime);
        System.out.println("Starting task " + id + " at " + df.format(d));
        fib(n);
        long endTime = System.currentTimeMillis();
        d.setTime(endTime);
        System.out.println("End task " + id + " at " + df.format(d) +
                          " after " + (endTime - startTime) + " milliseconds");
    }
}
