package javathreads.examples.ch04;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 13:19
 */
public class CharacterEvent {
    public CharacterSource source;
    public int character;

    public CharacterEvent(CharacterSource cs, int c){
        source = cs;
        character = c;
    }
}
