package javathreads.examples.ch06;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hombre on 2015/8/22.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/22 18:19
 */
public class TestLockException {
    private Lock testLock = new ReentrantLock();
    private synchronized void testLock(){
        try{
            testLock.lock();
            TestException testException = new TestException();
            testException.test();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            testLock.unlock();
        }
    }

    public static void main(String[] args){
        new TestLockException().testLock();
    }
}

class TestException{
    public void test() throws Exception{
        throw new Exception();
    }
}
