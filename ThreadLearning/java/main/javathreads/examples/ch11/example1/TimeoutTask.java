package javathreads.examples.ch11.example1;

import java.util.concurrent.Callable;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 21:27
 */
public class TimeoutTask implements Callable {

    public Integer call() throws Exception {
        return new Integer(0);
    }

}
