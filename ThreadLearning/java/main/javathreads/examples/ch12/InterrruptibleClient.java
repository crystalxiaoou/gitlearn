package javathreads.examples.ch12;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 16:48
 */
public class InterrruptibleClient extends InterruptibleReader {

    private static final int MAIN_SLEEP_TIME = 10000;

    public InterrruptibleClient(InputStream is) {
        super(is);
    }

    public void processData(byte[] b, int n){
        System.out.println("Got data" + new String(b, 0, n));
    }

    public static void main(String[] args) throws Exception {
        Socket s = new Socket(args[0], Integer.parseInt(args[1]));
        InputStream is = s.getInputStream();
        InterrruptibleClient c = new InterrruptibleClient(is);
        c.start();
        System.out.println("Main thread sleeping");
        Thread.sleep(MAIN_SLEEP_TIME);
        System.out.println("Main thread woke up");
        c.interrupt();
        System.out.println("Main thread called interrupt");
    }
}
