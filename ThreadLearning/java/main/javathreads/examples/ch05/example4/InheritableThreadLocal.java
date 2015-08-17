package javathreads.examples.ch05.example4;

/**
 * Created by hombre on 2015/8/17.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/17 9:16
 */
public abstract class InheritableThreadLocal extends ThreadLocal {
    protected abstract Object childValue(Object parentValue);
}
