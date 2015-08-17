package javathreads.examples.ch05.example3;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by hombre on 2015/8/17.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/17 8:29
 */
public class AtomicScoreAndCharacter {
    public class ScoreAndCharacter {
        private int score, char2type;

        public ScoreAndCharacter(int score, int char2type) {
            this.score = score;
            this.char2type = char2type;
        }

        public int getScore() {
            return score;
        }

        public int getChar2type() {
            return char2type;
        }
    }

    private AtomicReference<ScoreAndCharacter> value;

    public AtomicScoreAndCharacter() {
        this(0, -1);
    }

    public AtomicScoreAndCharacter(int initScore, int initChar) {
        value = new AtomicReference<ScoreAndCharacter>(
                     new ScoreAndCharacter(initScore, initChar));
    }

    public int getScore(){
        return value.get().getScore();
    }

    public int getCharacter(){
        return value.get().getChar2type();
    }

    public void set(int newScore, int newChar){
        value.set(new ScoreAndCharacter(newScore, newChar));
    }

    public void setScore(int newScore){
        ScoreAndCharacter origVal, newVal;

        while(true){
            origVal = value.get();
            newVal = new ScoreAndCharacter(newScore, origVal.getChar2type());
            if(value.compareAndSet(origVal, newVal)) break;
        }
    }

    public void setCharacter(int newCharacter){
        ScoreAndCharacter origVal, newVal;
        while(true){
            origVal = value.get();
            newVal = new ScoreAndCharacter(origVal.getScore(), newCharacter);
            if(value.compareAndSet(origVal, newVal)) break;
        }
    }

    public void setCharacterUpdateScore(int newCharacter){
        ScoreAndCharacter origVal, newVal;
        int score;
        while(true){
            origVal = value.get();
            score = origVal.getScore();
            score = (origVal.getChar2type() == -1) ? score : score - 1;
            newVal = new ScoreAndCharacter(score, newCharacter);
            if(value.compareAndSet(origVal, newVal)) break;
        }
    }

    public boolean processCharacter(int typedChar){
        ScoreAndCharacter origVal, newVal;
        int origScore, origCharacter;
        boolean retValue;

        while(true){
            origVal = value.get();
            origScore = origVal.getScore();
            origCharacter = origVal.getChar2type();

            if (typedChar == origCharacter){
                origCharacter = -1;
                origScore++;
                retValue = true;
            }else{
                origScore--;
                retValue = false;
            }
            newVal = new ScoreAndCharacter(origScore, origCharacter);
            if(value.compareAndSet(origVal, newVal)) break;
        }
        return retValue;
    }
}
