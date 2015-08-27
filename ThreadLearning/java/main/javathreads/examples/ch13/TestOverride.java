package javathreads.examples.ch13;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 9:49
 */
public class TestOverride implements Runnable {
    static class OverrideThreadGroup extends ThreadGroup {
        public OverrideThreadGroup(){
            super("Administrator Alert Group");
        }

        public void uncaughtException(Thread t, Throwable e){
            alertAdminstratror(e);
        }
    }

    public static void alertAdminstratror(Throwable e) {
        System.out.println("Adminstrator alert!");
        e.printStackTrace();
    }

    public static void main(String[] args){
        ThreadGroup tg = new OverrideThreadGroup();
        Thread t = new Thread(tg, new TestOverride());
        t.start();
    }
    public void run() {
        ArrayList al = new ArrayList();
        while(true){
            al.add(new byte[1024]);
        }
    }
}
