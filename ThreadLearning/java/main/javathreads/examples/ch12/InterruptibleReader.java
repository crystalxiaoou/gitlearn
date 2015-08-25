package javathreads.examples.ch12;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 16:42
 */
public abstract class InterruptibleReader extends Thread {
    private static final int BUF_LEN = 512;
    private Object lock = new Object();
    private InputStream is;
    private boolean done;
    private int buflen;

    protected void processData(byte[] b, int n){ }

    class ReaderClass extends Thread {
        public void run(){
            byte[] b = new byte[buflen];
            while(!done){
                try {
                    int n = is.read(b, 0, buflen);
                    processData(b, n);
                } catch (IOException ioe){
                    done = true;
                }
            }
            synchronized (lock){
                lock.notify();
            }
        }
    }

    public InterruptibleReader(InputStream is){
        this(is, BUF_LEN);
    }
    public InterruptibleReader(InputStream is, int len){
        this.is = is;
        buflen = len;
    }

    public void run(){
        ReaderClass rc = new ReaderClass();
        synchronized (lock){
            rc.start();
            while(!done){
                try {
                    lock.wait();
                } catch (InterruptedException ie){
                    done = true;
                    rc.interrupt();
                    try {
                        is.close();
                    } catch (IOException ioe){ }
                }
            }
        }
    }
}
