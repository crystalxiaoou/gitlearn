package javathreads.examples.ch05.example3;

import javathreads.examples.ch04.CharacterDisplayCanvas;
import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 14:00
 */
public class AnimatedCharacterDisplayCanvas extends CharacterDisplayCanvas implements CharacterListener, Runnable {
    private static final int WAIT_TIME = 100;
    private AtomicBoolean done = new AtomicBoolean(true);
    private AtomicInteger curX = new AtomicInteger(0);
    private AtomicInteger tempChar = new AtomicInteger(0);
    private Thread timer = null;

    public  AnimatedCharacterDisplayCanvas(){
        startAnimationThread();
    }

    private void startAnimationThread() {
        if(timer == null) {
            timer = new Thread(this);
            timer.start();
        }
    }

    public AnimatedCharacterDisplayCanvas(CharacterSource cs){
        super(cs);
        startAnimationThread();
    }

    public void newCharacter(CharacterEvent ce){
        curX.set(0);
        tempChar.set(ce.character);
        repaint();
    }

    protected  void paintComponent(Graphics gc){
        char[] localTmpChar = new char[1];
        localTmpChar[0] = (char) tempChar.get();
        int localCurX = curX.get();
        Dimension d = getSize();
        int charWidth = fm.charWidth(localTmpChar[0]);
        gc.clearRect(0, 0, d.width, d.height);
        if(localTmpChar[0] == 0) {
            return;
        }
        gc.drawChars(localTmpChar, 0, 1, localCurX, fontHeight);
        curX.getAndIncrement();
    }

    /**
     * 这里的run()方法上加上synchronzied,是为了使用wait()和wait(long）
     */
    public  void run() {
        while (true) {
            try {
                Thread.sleep(WAIT_TIME);
                if(!done.get()){
                    repaint();
                }
            } catch (InterruptedException ie) {
                return;
            }
        }
    }

    /**
     * 这里第一次启动线程，是使用setDone方法启动的
     * @param b
     */
    public  void setDone(boolean b){
        done.set(b);
    }
}
