package javathreads.examples.ch05.example4;

/**
 * Created by hombre on 2015/8/17.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/17 9:18
 */
public class CalculatorTest extends Calculator implements Runnable{
    @Override
    protected Object doLocalCalculate(Object param) {
        System.out.println("Doing calculation of " + param + " in thread " + Thread.currentThread());
        return param;
    }

    public void run() {
        for(int i = 0; i < 30; i++){
            Integer p = new Integer(i % 5);
            calculate(p);
        }
    }

    public static void main(String[] args){
        int nThreads = Integer.parseInt(args[0]);
        for(int i = 0; i < nThreads; i++){
            Thread t = new Thread(new CalculatorTest());
            t.start();
        }
    }
}
