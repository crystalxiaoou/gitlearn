package javathreads.examples.ch05.example3;

import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 14:16
 */
public class ScoreLabel extends JLabel implements CharacterListener {
    private AtomicScoreAndCharacter scoreAChar = new AtomicScoreAndCharacter();
    private AtomicReference<CharacterSource> generator = null, typist = null;
    private Lock scoreLock = new ReentrantLock();

    public ScoreLabel(CharacterSource generator, CharacterSource typist){
        this.generator = new AtomicReference<CharacterSource>(generator);
        this.typist = new AtomicReference<CharacterSource>(typist);

        if(generator != null) {
            generator.addCharacterListener(this);
        }
        if(typist != null) {
            typist.addCharacterListener(this);
        }
    }

    public ScoreLabel(){
        this(null, null);
    }

    public void resetGenerator(CharacterSource newGenerator){
        CharacterSource oldGenerator;

        if (newGenerator != null) {
            newGenerator.addCharacterListener(this);
        }
        oldGenerator = generator.getAndSet(newGenerator);
        if (oldGenerator != null) {
            oldGenerator.revmoceCharacterListener(this);
        }
    }

    public void resetTypist(CharacterSource newTypist){
        CharacterSource oldTypist;

        if (newTypist != null) {
            newTypist.addCharacterListener(this);
        }
        oldTypist = typist.getAndSet(newTypist);
        if (oldTypist != null) {
            oldTypist.revmoceCharacterListener(this);
        }
    }

    public void resetScore(){
        scoreAChar.set(0, -1);
        setScore();
    }

    private void setScore() {
        //  This method will be explained later in chapter 7
        if(SwingUtilities.isEventDispatchThread()){
            setText(Integer.toString(scoreAChar.getScore()));
        }else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        setText(Integer.toString(scoreAChar.getScore()));
                    }
                });
            } catch (InterruptedException ie){ }
              catch (InvocationTargetException ite){}
        }
    }

    public void newCharacter(CharacterEvent ce) {
        // Previous character not typed correctly -1 point penalty
        if(ce.source == generator.get()){
            scoreAChar.setCharacterUpdateScore(ce.character);
            setScore();
        }

        // if Character is extraneous -1 point penalty
        // if Character does not match -1 point penalty
        else if(ce.source == typist.get()){
            scoreAChar.processCharacter(ce.character);
            setScore();
        }
    }
}
