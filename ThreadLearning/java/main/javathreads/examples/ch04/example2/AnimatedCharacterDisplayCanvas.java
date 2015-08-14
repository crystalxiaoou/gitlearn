package javathreads.examples.ch04.example2;

import javathreads.examples.ch04.CharacterDisplayCanvas;
import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

import java.awt.*;

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
    private Object doneLock = new Object();

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
        synchronized(doneLock) {
            while (true) {
                try {
                    if (done) {
                        doneLock.wait();
                    } else {
                        repaint();
                        doneLock.wait(WAIT_TIME);
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }
        }
    }

    /**
     * 这里第一次启动线程，是使用setDone方法启动的
     * @param b
     */
    public  void setDone(boolean b){
        synchronized(doneLock) {
            done = b;
            if (timer == null) {
                timer = new Thread(this);
                timer.start();
            }
            if (!done) {
                doneLock.notify();
            }
        }
    }
}
