package javathreads.examples.ch12.example3;

import javathreads.examples.ch12.TCPNIOServer;
import javathreads.examples.ch12.TypeServerConstants;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 15:10
 */
public class TypeServer extends TCPNIOServer {
    static String testString = "Thisisateststring";
    static class ClientInfo {
        private static final int CAPACITY = 512;
        ByteBuffer inBuf = ByteBuffer.allocateDirect(CAPACITY);
        ByteBuffer outBuf = ByteBuffer.allocateDirect(CAPACITY);
        boolean outputPending = false;
        SocketChannel channel;
    }
    Map allClients = new HashMap();
    Charset encoder = Charset.forName("UTF-8");
    
    
    @Override
    protected void registeredClient(SocketChannel sc) throws IOException {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.channel = sc;
        clientInfo.outBuf.clear();
        clientInfo.outBuf.put(TypeServerConstants.WELCOME);
        clientInfo.outBuf.flip();
        allClients.put(sc, clientInfo);
        send(sc, clientInfo);
    }

    private void send(SocketChannel sc, ClientInfo clientInfo) throws IOException {
        int len = clientInfo.outBuf.remaining();
        int nBytes = sc.write(clientInfo.outBuf);
        if(nBytes != len){
            // 客户端还没有准备好接收数据
            clientInfo.outputPending = true;
            clientInfo.channel.register(selector,
                    SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }else {
            clientInfo.outBuf.clear();
            if(clientInfo.outputPending){
                clientInfo.outputPending = false;
                clientInfo.channel.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    @Override
    protected void handleClient(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ClientInfo clientInfo = (ClientInfo) allClients.get(sc);
        if(clientInfo == null){
            throw new IllegalStateException("Unknown client");
        }
        if(key.isWritable()){
            send(sc, clientInfo);
        }
        if(key.isReadable()){
            recv(sc, clientInfo);
        }
    }

    private void recv(SocketChannel sc, ClientInfo clientInfo) throws IOException {
        clientInfo.channel.read(clientInfo.inBuf);
        ByteBuffer tmpBuf = clientInfo.inBuf.duplicate();
        tmpBuf.flip();
        int bytesProcessed = 0;
        boolean doneLoop = false;
        while(!doneLoop){
            byte b;
            try {
                b = tmpBuf.get();
            } catch (BufferUnderflowException bufferUnderflowException){
                // 处理缓冲区中的所有数据
                clientInfo.inBuf.clear();
                break;
            }
            switch(b){
                case TypeServerConstants.WELCOME:
                    bytesProcessed++;
                    break;
                case TypeServerConstants.GET_STRING_REQUEST:
                    bytesProcessed++;
                    if(clientInfo.outputPending){
                        // 客户端堵塞，我们无法对byte缓冲区加入数据
                        // 因为它的状态不对。我们也可以设定新的缓冲区并改变送出的method, 以
                        // 符合多个缓冲区，但是我们就直接假设客户端已死机
                        break;
                    }
                    clientInfo.outBuf.put(TypeServerConstants.GET_STRING_RESPONSE);
                    ByteBuffer strBuf = encoder.encode(testString);
                    clientInfo.outBuf.putShort((short)strBuf.remaining());
                    clientInfo.outBuf.put(strBuf);
                    clientInfo.outBuf.flip();
                    send(sc, clientInfo);
                    break;
                case TypeServerConstants.GET_STRING_RESPONSE:
                    int startPos = tmpBuf.position();
                    try {
                        int nBytes = tmpBuf.getInt();
                        byte[] buf = new byte[nBytes];
                        tmpBuf.get(buf);
                        bytesProcessed += buf.length + 5;
                        String s = new String(buf);
                        // 将字符串送到GUI中
                        break;
                    } catch (BufferUnderflowException bufferUnderflowException){
                        // 处理所有可用的数据
                        clientInfo.inBuf.position(clientInfo.inBuf.position() + bytesProcessed);
                        doneLoop = true;
                    }
                    break;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        TypeServer typeServer = new TypeServer();
        typeServer.port = Integer.parseInt(args[0]);
        Thread t = new Thread(typeServer);
        t.start();
        System.out.println("Type server ready... Type CTRL-D to exit");
        while(System.in.read() > 0) ;
        typeServer.stopServer();
        t.join();
    }


}
