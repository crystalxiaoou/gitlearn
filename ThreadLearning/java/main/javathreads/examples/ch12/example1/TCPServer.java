package javathreads.examples.ch12.example1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 9:30
 */
public class TCPServer implements Cloneable, Runnable {
    Thread runner = null;
    ServerSocket serverSocket = null;
    Socket data = null;

    private boolean done = false;

    public synchronized void startServer(int port) throws IOException {
        if(runner == null){
            serverSocket = new ServerSocket(port);
            runner = new Thread(this);
            runner.start();
        }
    }
    public synchronized  void stopServer(){
        done = true;
        runner.interrupt();
    }

    protected synchronized boolean getDone(){
        return done;
    }


    public void run() {
        if(serverSocket != null){
            while(!getDone()){
                try {
                    Socket dataSocket = serverSocket.accept();
                    TCPServer newSocket = (TCPServer) clone();

                    newSocket.serverSocket = null;
                    newSocket.data = dataSocket;
                    newSocket.runner = new Thread(newSocket);
                    newSocket.runner.start();
                } catch (Exception e){ }
            }
        } else {
            run(data);
        }
    }

    public void run(Socket data) {
    }
}
