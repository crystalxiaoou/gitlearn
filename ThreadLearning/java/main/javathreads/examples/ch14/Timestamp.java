package javathreads.examples.ch14;

import java.util.concurrent.TimeUnit;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 10:52
 */
public class Timestamp {
    private long startTime;
    private long stopTime;
    private boolean stopped = false;
    private TimeUnit ts;

    public Timestamp(){
        this(TimeUnit.NANOSECONDS);
    }

    public Timestamp(TimeUnit ts){
        this.ts = ts;
        start();
    }

    private void start() {
        startTime = System.nanoTime();
        stopped = false;
    }

    public void stop(){
        stopTime = System.nanoTime();
        stopped = true;
    }

    public long elapsedTime(){
        if(!stopped)
            throw new IllegalStateException("Timestamp not stopped");
        return ts.convert(stopTime - startTime, TimeUnit.NANOSECONDS);
    }

    public String toString(){
        try {
            return elapsedTime() + " " + ts;
        } catch (IllegalStateException ise){
            return "Timestamp (not stopped)";
        }
    }

    public String units(){
        return ts.toString();
    }

}
