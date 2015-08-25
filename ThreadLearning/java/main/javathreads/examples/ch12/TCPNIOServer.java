package javathreads.examples.ch12;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by hombre on 2015/8/25.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/25 14:53
 */
public abstract class TCPNIOServer implements Runnable {
    protected ServerSocketChannel channel = null;
    private boolean done = false;
    protected Selector selector;
    protected  int port = 8000;

    public void startServer() throws IOException {
        channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket server = channel.socket();
        server.bind(new InetSocketAddress(port));
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public synchronized void stopServer() throws IOException {
        done = true;
        channel.close();
    }

    protected synchronized boolean getDone(){
        return done;
    }

    public void run() {
        try {
            startServer();
        } catch (IOException ioe){
            System.out.println("Can't start server: " + ioe);
            return ;
        }
        while(!getDone()){
            try {
                selector.select();
            } catch (IOException ioe){
                System.err.println("Server error: " + ioe);
                return;
            }
            Iterator it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = (SelectionKey) it.next();
                if(key.isReadable() || key.isWritable()){
                    // key 代表一个socket客户端
                    try {
                        handleClient(key);
                    } catch (IOException ioe){
                        // 客户端断线
                        key.cancel();
                    }
                }else if(key.isAcceptable()){
                    try {
                        handleServer(key);
                    } catch (IOException ioe){
                        // 接受错误， 将其视为fatal
                        throw new IllegalStateException(ioe);
                    }
                }else {
                    System.out.println("unknown key state");
                }
                it.remove();
            }
        }
    }

    protected void handleServer(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = channel.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        registeredClient(sc);
    }

    protected abstract void registeredClient(SocketChannel sc) throws IOException;

    protected abstract void handleClient(SelectionKey key) throws IOException;
}
