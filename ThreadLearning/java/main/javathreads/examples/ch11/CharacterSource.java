package javathreads.examples.ch11;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 13:21
 */
public interface CharacterSource {
    void addCharacterListener(CharacterListener cl);
    void revmoceCharacterListener(CharacterListener cl);
    void nextCharacter();
}
