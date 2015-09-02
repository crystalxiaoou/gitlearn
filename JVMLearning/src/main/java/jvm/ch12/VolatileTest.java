package jvm.ch12;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 21:08
 * volatile变量自增运算测试
 */
public class VolatileTest {
    private static final int FOR_LOOP_EVERY_THREAD = 100;
    public static volatile int race = 0;

    public static void increase(){
        race++;
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREADS_COUNT];
        for(int i = 0; i < THREADS_COUNT; i++){
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for(int i = 0; i < FOR_LOOP_EVERY_THREAD; i++){
                        increase();
                    }
                }
            });
            threads[i].start();
            threads[i].join();
        }


        System.out.println(race);
    }
}
