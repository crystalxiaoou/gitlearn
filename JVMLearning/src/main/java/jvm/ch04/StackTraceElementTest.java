package jvm.ch04;

import java.util.Map;

/**
 * Created by hombre on 2015/8/29.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/29 11:29
 */
public class StackTraceElementTest {
    public static void main(String[] args) {
        for (Map.Entry<Thread, StackTraceElement[]> stackTrace :
                Thread.getAllStackTraces().entrySet()) {
            Thread thread = (Thread) stackTrace.getKey();
            StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();
            if (thread.equals(Thread.currentThread())) {
                continue;
            }
            System.out.println("线程： " + thread.getName());
            for (StackTraceElement element : stack) {
                System.out.println("\t" + element);
            }
        }
    }
}
