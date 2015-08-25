package javathreads.examples.ch12.example1;

import javathreads.examples.ch12.CharacterEventHandler;
import javathreads.examples.ch12.CharacterListener;
import javathreads.examples.ch12.CharacterSource;
import javathreads.examples.ch12.TypeServerConstants;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 13:48
 */
public class RandomCharacterGenerator extends Thread implements CharacterSource {
    private static final int MIN_TIME = 2000;
    private static final int MAX_TIME = 5500;
    private char[] chars;
    private int curChar;
    private Random random = new Random();
    private CharacterEventHandler handler;
    private boolean done = true;
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();
    private Socket sock;
    private DataInputStream reader;
    private DataOutputStream writer;

    public RandomCharacterGenerator(String host, int port) throws IOException {
        handler = new CharacterEventHandler();
        sock = new Socket(host, port);
        reader = new DataInputStream(sock.getInputStream());
        reader.read(); // 欢迎
        writer = new DataOutputStream(sock.getOutputStream());
        getString();
    }

    private synchronized  void getString() throws IOException {
        byte b = TypeServerConstants.GET_STRING_REQUEST;
        writer.writeByte(b);
        writer.flush();
        b = (byte) reader.readByte();
        if(b != TypeServerConstants.GET_STRING_RESPONSE){
            throw new IllegalStateException("Bad rev state " + b);
        }
        String s= reader.readUTF();
        chars = s.toCharArray();
        curChar = 0;
    }

    public int getPauseTime(int minTime, int maxTime){
        return (int)(minTime + (maxTime - minTime) * random.nextDouble());
    }

    public int getPauseTime(){
        return getPauseTime(MIN_TIME, MAX_TIME);
    }

    public void addCharacterListener(CharacterListener cl){
        handler.addCharacterListener(cl);
    }

    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }


    public void nextCharacter(){
        handler.fireNewCharacter(this, (int)chars[curChar++]);
        if(curChar == chars.length){
            try {
                getString();
            } catch (IOException iOException){
                // 跳出对话框，警告用户有错误
               JOptionPane.showMessageDialog(null, "客户端请求新字符串错误", "有错误",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * 使用等待-通知机制实现RandomCharacterGenerator这个字符源在stop按扭按下时，线程并不直接退出，而是
     * wait（）, 当start按扭按下时，notify(), 通知线程，这样线程就又开始产生新字符了
     *
     * 这里的run()方法上加上synchronzied,是为了使用wait()和wait(long）
     */
    public  void run(){
        try {
            lock.lock();
            while (true) {
                try {
                    if (done) {
                        cv.await();
                    } else {
                        nextCharacter();
                        cv.await(getPauseTime(), TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * 这里不使用volatile这个关键字来修饰done这个成员变量，而是使用一个synchronized方法
     * 这个方法在设置为false时，通知线程开始产生一个新字符
     * @param b
     */
    public  void setDone(boolean b){
        try {
            lock.lock();
            done = b;
            if(!done) {
                cv.signal();
            }
        } finally {
            lock.unlock();
        }
    }


}
