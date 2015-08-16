package javathreads.examples.ch05.example2;

import javathreads.examples.ch04.CharacterEventHandler;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

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
    private static char[] chars;
    private static String charArray = "abcdefghijklmnopqrstuvwxyz0123456789";
    static {
        chars = charArray.toCharArray();
    }

    private Random random;
    private CharacterEventHandler handler;
    private boolean done = true;
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();

    public RandomCharacterGenerator(){
        random = new Random();
        handler = new CharacterEventHandler();
    }

    public int getPauseTime(){
        return (int)(Math.max(1000, 5000 * random.nextDouble()));
    }

    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    public void revmoceCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    public void nextCharacter() {
        handler.fireNewCharacter(this,
                                 (int) chars[random.nextInt(chars.length)]);
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
