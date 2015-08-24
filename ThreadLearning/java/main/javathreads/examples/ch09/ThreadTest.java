package javathreads.examples.ch09;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 13:23
 */
public class ThreadTest {
    public static void main(String[] args){
        int nThreads = Integer.parseInt(args[0]);
        long n = Long.parseLong(args[1]);
        Thread[] t = new Thread[nThreads];

        for(int i = 0; i < nThreads; i++){
            t[i] = new Thread(new Task(n, "Task " + i));
            t[i].setPriority((i % 10) + 1);
            t[i].start();
        }
        for(int i = 0; i < nThreads; i++){
            try {
                t[i].join();
            } catch (InterruptedException ie){
            }
        }
    }
}
