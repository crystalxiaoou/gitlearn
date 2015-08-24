package javathreads.examples.ch08.example4;

import javathreads.examples.ch08.CharacterEvent;
import javathreads.examples.ch08.CharacterListener;
import javathreads.examples.ch08.CharacterSource;

import javax.swing.*;
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
    private volatile int score = 0;
    private int char2type = -1;
    private CharacterSource generator = null, typist = null;
    private Lock scoreLock = new ReentrantLock();
    private CharCounter counter;

    public ScoreLabel(CharacterSource generator, CharacterSource typist, CharCounter charCounter){
        this.generator = generator;
        this.typist = typist;
        counter = charCounter;
        if(generator != null) {
            generator.addCharacterListener(this);
        }
        if(typist != null) {
            typist.addCharacterListener(this);
        }
    }

    public ScoreLabel(){
        this(null, null, null);
    }

    public void resetGenerator(CharacterSource newGenerator){
        try{
            scoreLock.lock();
            if(generator != null) {
                generator.revmoceCharacterListener(this);
            }
            generator = newGenerator;
            if(generator != null) {
                generator.addCharacterListener(this);
            }
        } finally {
            scoreLock.unlock();
        }
    }

    public void resetTypist(CharacterSource newTypist){
        try{
            scoreLock.lock();
            if(typist != null) {
                typist.revmoceCharacterListener(this);
            }
            typist = newTypist;
            if(typist != null) {
                typist.addCharacterListener(this);
            }
        } finally {
            scoreLock.unlock();
        }
    }

    public void resetScore(){
        try {
            scoreLock.lock();
            score = 0;
            char2type = -1;
            setScore();
        } finally {
            scoreLock.unlock();
        }
    }

    private void setScore() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setText(Integer.toString(score));
            }
        });
    }

    public void newCharacter(CharacterEvent ce) {
        try {
            scoreLock.lock();
            // Previous character not typed correctly -1 point penalty
            if(ce.source == generator){
                if(char2type != -1){
                    score--;
                    setScore();
                    counter.incorrectChar(char2type);
                }
                char2type = ce.character;
            }

            // if character is extraneous, -1 point penalty
            // if character does not match, -1 point penalty
            else {
                if(char2type != ce.character){
                    score--;
                    counter.incorrectChar(ce.character);
                }else {
                    score++;
                    counter.correctChar(ce.character);
                    char2type = -1;
                }
                setScore();
            }
        } finally {
            scoreLock.unlock();
        }
    }
}
