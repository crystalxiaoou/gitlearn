package jvm.ch04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hombre on 2015/8/29.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/29 11:47
 */
public class MonitoringTest {

    private static final int SLEEP_TIME = 50;
    private static final int FILL_OBJECT_NUMS = 1000;

    /**
     * 内存占位符，一个OOMObject大约占64KB
     */
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for(int i = 0; i < num; i++){
            // 稍作延时，令监控曲线的变化更加明显
            Thread.sleep(SLEEP_TIME);
            list.add(new OOMObject());
        }

    }

    /**
     * 线程死循环演示
     */
    public static void createBusyThread(){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while(true) ; // 第38行
            }
        }, "testBusyThread");
        thread.start();
    }

    /**
     * 线程锁等待演示
     * @param lock
     */
    public static void createLockThread(final Object lock){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock){
                    try {
                        lock.wait();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }, "testLockThread");
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        Object lock = new Object();
        createLockThread(lock);
    }
}
