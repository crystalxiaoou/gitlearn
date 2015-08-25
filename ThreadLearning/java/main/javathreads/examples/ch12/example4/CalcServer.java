package javathreads.examples.ch12.example4;

import javathreads.examples.ch08.example6.Fibonacci;
import javathreads.examples.ch12.TCPNIOServer;
import sun.org.mozilla.javascript.internal.ast.CatchClause;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 15:59
 */
public class CalcServer extends TCPNIOServer {
    private static final int CAPACITY = 8;
    private static final long KEEP_ALIVE_TIME = 50000L;
    static ThreadPoolExecutor poolExecutor;

    class FibClass implements Runnable {
        private static final int CAPACITY = 8;
        long n;
        SocketChannel clientChannel;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
        FibClass(long n, SocketChannel sc){
            this.n = n;
            clientChannel = sc;
        }

        private long fib(long n){
            if(n == 0){
                return 0L;
            }
            if(n == 1){
                return 1L;
            }
            return fib(n - 1) + fib(n - 2);
        }

        public void run() {
            try {
                long answer = fib(n);
                buffer.putLong(answer);
                buffer.flip();
                clientChannel.write(buffer);
                if(buffer.remaining() > 0){
                    Selector s = Selector.open();
                    clientChannel.register(s, SelectionKey.OP_WRITE);
                    while(buffer.remaining() > 0){
                        s.select();
                        clientChannel.write(buffer);
                    }
                    s.close();
                }
            } catch (ClosedChannelException e) {
               System.out.println(e.getStackTrace());
            } catch (IOException ioe) {
                System.out.println("Client error " + ioe);
            }
        }
    }
    @Override
    protected void registeredClient(SocketChannel sc) throws IOException {

    }

    @Override
    protected void handleClient(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
        sc.read(buffer);
        buffer.flip();
        long n = buffer.getLong();
        FibClass fc = new FibClass(n, sc);
        poolExecutor.execute(fc);
    }

    public static void main(String[] args) throws Exception {
        CalcServer cs = new CalcServer();
        cs.port = Integer.parseInt(args[0]);
        int tpSize = Integer.parseInt(args[1]);
        poolExecutor = new ThreadPoolExecutor(
                tpSize, tpSize, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        new Thread(cs).start();
        System.out.println("Calc Server waiting for requests...");
    }
 }
