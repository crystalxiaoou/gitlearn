package javathreads.examples.ch12;

import com.sun.corba.se.spi.activation.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 10:44
 */
public class TCPThrottledServer implements Runnable {
    ServerSocket server = null;
    Thread[] serverThreads;
    volatile boolean done = false;

    public synchronized void startServer(int port, int nThreads) throws IOException {
        server = new ServerSocket(port);
        serverThreads = new Thread[nThreads];
        for(int i = 0; i < nThreads; i++){
            serverThreads[i] = new Thread(this);
            serverThreads[i].start();
        }
    }

    public synchronized  void setDone(){
        done = true;
    }

    public void run() {
        while(!done){
            try {
                Socket data = server.accept();
                run(data);
            } catch (IOException ioe) {
                System.out.println("Accept error " + ioe);
            }
        }
    }

    public void run(Socket data) {
    }
}
