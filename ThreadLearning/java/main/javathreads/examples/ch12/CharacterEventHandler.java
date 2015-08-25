package javathreads.examples.ch12;

import java.util.ArrayList;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 13:25
 */
public class CharacterEventHandler {
    private ArrayList<CharacterListener> listeners = new ArrayList<CharacterListener>();

    public void addCharacterListener(CharacterListener cl){
        listeners.add(cl);
    }

    public void removeCharacterListener(CharacterListener cl){
        listeners.remove(cl);
    }

    public void fireNewCharacter(CharacterSource source, int c){
        CharacterEvent ce = new CharacterEvent(source, c);
        CharacterListener[] cl = (CharacterListener[]) listeners.toArray(new CharacterListener[0]);

        for(int i = 0; i < cl.length; i++){
            cl[i].newCharacter(ce);
        }
    }
}
