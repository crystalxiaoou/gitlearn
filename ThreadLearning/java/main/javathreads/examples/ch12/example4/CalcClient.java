package javathreads.examples.ch12.example4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 16:20
 */
public class CalcClient extends Thread {
    long n;
    Socket socket;

    public CalcClient(long n, String host, int port) throws IOException {
        this.n = n;
        socket = new Socket(host, port);
    }

    public void run(){
        try {
            DataOutputStream dos =
                    new DataOutputStream(socket.getOutputStream());
            dos.writeLong(n);
            DataInputStream dis =
                    new DataInputStream(socket.getInputStream());
            System.out.println("Received answer " + dis.readLong());
        } catch (IOException ioe){
            System.out.println("Socket error " + ioe);
        }
    }

    public static void main(String[] args) throws Exception {
        int nThreads = Integer.parseInt(args[0]);
        long n = Long.parseLong(args[1]);
        int port = Integer.parseInt(args[3]);
        CalcClient[] clients = new CalcClient[nThreads];
        for(int i = 0; i < nThreads; i++){
            clients[i] = new CalcClient(n, args[2], port);
            clients[i].start();
        }

        for(int i = 0; i < nThreads; i++){
            clients[i].join();
        }
    }
}
