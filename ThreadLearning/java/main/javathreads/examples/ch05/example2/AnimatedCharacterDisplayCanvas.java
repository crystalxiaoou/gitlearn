package javathreads.examples.ch05.example2;

import javathreads.examples.ch04.CharacterDisplayCanvas;
import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 14:00
 */
public class AnimatedCharacterDisplayCanvas extends CharacterDisplayCanvas implements CharacterListener, Runnable {
    private static final int WAIT_TIME = 100;
    private boolean done = true;
    private int curX = 0;
    private Thread timer = null;
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();


    public  AnimatedCharacterDisplayCanvas(){
    }

    public AnimatedCharacterDisplayCanvas(CharacterSource cs){
        super(cs);
    }

    public synchronized  void newCharacter(CharacterEvent ce){
        curX = 0;
        tmpChar[0] = (char) ce.character;
        repaint();
    }

    protected synchronized void paintComponent(Graphics gc){
        Dimension d = getSize();
        gc.clearRect(0, 0, d.width, d.height);
        if(tmpChar[0] == 0)
            return;
        int charWidth = fm.charWidth(tmpChar[0]);
        gc.drawChars(tmpChar, 0, 1, curX++, fontHeight);
    }

    /**
     * 这里的run()方法上加上synchronzied,是为了使用wait()和wait(long）
     */
    public  void run() {
        try{
            lock.lock();
            while (true) {
                try {
                    if (done) {
                        cv.await();
                    } else {
                        repaint();
                        cv.await(WAIT_TIME, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 这里第一次启动线程，是使用setDone方法启动的
     * @param b
     */
    public  void setDone(boolean b){
        try {
            lock.lock();
            done = b;
            if (timer == null) {
                timer = new Thread(this);
                timer.start();
            }
            if (!done) {
                cv.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
