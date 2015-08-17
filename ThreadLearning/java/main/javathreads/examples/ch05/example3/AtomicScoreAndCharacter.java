package javathreads.examples.ch05.example3;

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
        public ScoreAndCharacter(int score, int char2type){
            this.score = score;
            this.char2type = char2type;
        }

        public int getScore(){
            return score;
        }

        public int getChar2type(){
            return char2type;
        }


    }
}
