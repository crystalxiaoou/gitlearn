package javathreads.examples.ch10.example2;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 16:47
 */
public class SingleThreadTest {
    public static void main(String[] args){
        int nTasks = Integer.parseInt(args[0]);
        int fib = Integer.parseInt(args[1]);
        SingleThreadAccess singleThreadAccess = new SingleThreadAccess();
        for(int i = 0; i < nTasks; i++){
            singleThreadAccess.invokeLater(new Task(fib, "Task " + i));
        }
        singleThreadAccess.shutdown();
    }
}
