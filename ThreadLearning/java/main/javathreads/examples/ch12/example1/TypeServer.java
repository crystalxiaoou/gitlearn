package javathreads.examples.ch12.example1;

import javathreads.examples.ch12.TypeServerConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 9:47
 */
public class TypeServer extends TCPServer {
    public void run(Socket data){
        try {
            DataOutputStream dos = new
                    DataOutputStream(data.getOutputStream());
            dos.writeByte(TypeServerConstants.WELCOME);
            DataInputStream dis =
                    new DataInputStream(data.getInputStream());
            while(true){
                byte b = dis.readByte();
                if(b != TypeServerConstants.GET_STRING_REQUEST){
                    System.out.println("Client sent unknown request " + b);
                    continue;
                }
                dos.writeByte(TypeServerConstants.GET_STRING_RESPONSE);
                dos.writeUTF("Thisisateststring");
                dos.flush();
            }
        }catch (Exception e){
            System.out.println("Client terminating: " + e);
            return;
        }
    }

    public static void main(String[] args) throws IOException {
        TypeServer typeServer = new TypeServer();
        typeServer.startServer(Integer.parseInt(args[0]));

        System.out.println("Server ready and waiting...");
    }

}
