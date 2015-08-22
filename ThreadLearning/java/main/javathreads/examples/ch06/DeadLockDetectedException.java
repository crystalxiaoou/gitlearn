package javathreads.examples.ch06;

/**
 * Created by hombre on 2015/8/22.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/22 16:45
 */
public class DeadLockDetectedException extends RuntimeException{
    public DeadLockDetectedException(String s){
        super(s);
    }
}

