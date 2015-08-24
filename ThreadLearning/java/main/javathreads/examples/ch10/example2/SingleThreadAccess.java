package javathreads.examples.ch10.example2;

import java.util.concurrent.*;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 16:42
 */
public class SingleThreadAccess {
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 50000L;
    private ThreadPoolExecutor threadPoolExecutor;

    public SingleThreadAccess(){
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 异步执行
     * @param r : Runnable
     */
    public void invokeLater(Runnable r){
        threadPoolExecutor.execute(r);
    }

    /**
     * 同步执行
     * @param r : Runnable
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void invokeAndWait(Runnable r)
        throws  InterruptedException, ExecutionException {
        FutureTask task = new FutureTask(r, null);
        threadPoolExecutor.execute(task);
        task.get();
    }

    public void shutdown(){
        threadPoolExecutor.shutdown();
    }
}
