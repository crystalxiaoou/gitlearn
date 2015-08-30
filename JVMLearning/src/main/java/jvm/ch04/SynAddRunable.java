package jvm.ch04;

/**
 * Created by hombre on 2015/8/29.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/29 12:18
 */
public class SynAddRunable implements Runnable {
    private static final int FORLOOP_INIT_VALUE = 0;
    private static final int FORLOOP_END_VALUE = 1000;
    int a, b;
    public SynAddRunable(int a, int b){
        this.a = a;
        this.b = b;
    }

    public void run() {
        synchronized (Integer.valueOf(a)){
            synchronized (Integer.valueOf(b)){
                System.out.println(a + b);
            }
        }
    }

    public static void main(String[] args){
        for(int i = FORLOOP_INIT_VALUE; i < FORLOOP_END_VALUE; i++){
            new Thread(new SynAddRunable(1, 2)).start();
            new Thread(new SynAddRunable(2, 1)).start();
        }
    }
}
