package jvm.ch07;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 9:27
 */
public class Test {
    /*static {
        i = 0;
        System.out.println(i);
    }
    static int i = 1;

    public static void main(String[] args){
        System.out.println(i);
    }*/

   /* static class Parent {
        public static int A = 1;
        static {
            A = 2;
        }
    }

    static class Sub extends Parent {
        public static int B = A;
    }

    public static void main(String[] args){
        System.out.println(Sub.B);
    }*/

    static class DeadLoopClass {
        static {
            if(true){
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while(true){
                }
            }
        }
    }

    public static void main(String[] args){
        Runnable script = new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread() + "start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
}
