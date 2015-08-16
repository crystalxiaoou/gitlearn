package javathreads.examples.ch05.example1;

import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch04.CharacterListener;
import javathreads.examples.ch04.CharacterSource;

import javax.swing.*;
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
    private AtomicInteger score = new AtomicInteger(0);
    private AtomicInteger char2type = new AtomicInteger(-1);

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
        score.set(0);
        char2type.set(-1);
        setScore();
    }

    private void setScore() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setText(Integer.toString(score.get()));
            }
        });
    }

    public void newCharacter(CharacterEvent ce) {
       int oldChar2type;
       // 前一个字母没有正确输入: 扣一分
        if(ce.source == generator.get()){
            oldChar2type = char2type.getAndSet(ce.character);
            if(oldChar2type != -1){
                score.decrementAndGet();
                setScore();
            }
        }
        // 如果字母是无关的: 扣一分
        // 如果字母不相符: 扣一分
        else if(ce.source == typist.get()){
            while (true){
                oldChar2type = char2type.get();
                if(oldChar2type != ce.character){
                    score.decrementAndGet();
                    break;
                }else if(char2type.compareAndSet(oldChar2type, -1)){
                    score.incrementAndGet();
                    break;
                }
            }
            setScore();
        }
    }
}
