package javathreads.examples.ch08.example4;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 9:41
 */
public class CharCounter {
    private static final int NOT_EXISTS_NUM = 0;
    public HashMap correctChars = new HashMap();
    public HashMap incorrectChars = new HashMap();
    private AbstractTableModel abstractTableModel;

    public void correctChar(int c){
        synchronized (correctChars){
            Integer key = new Integer(c);
            Integer num = (Integer) correctChars.get(key);
            if(num == null){
                correctChars.put(key, new Integer(1));
            }else {
                correctChars.put(key, new Integer(num.intValue() + 1));
            }
            if(abstractTableModel != null){
                abstractTableModel.fireTableDataChanged();
            }
        }
    }

    public int getCorrectNum(int c){
        synchronized (correctChars){
            Integer key = new Integer(c);
            Integer num = (Integer) correctChars.get(key);
            if(num == null) {
                return NOT_EXISTS_NUM;
            }
            return num.intValue();
        }
    }

    public void incorrectChar(int c){
        synchronized (incorrectChars){
            Integer key = new Integer(c);
            Integer num = (Integer) incorrectChars.get(key);
            if(num == null){
                incorrectChars.put(key, new Integer(-1));
            }else {
                incorrectChars.put(key, new Integer(num.intValue() - 1));
            }
            if(abstractTableModel != null){
                abstractTableModel.fireTableDataChanged();
            }
        }
    }

    public int getIncorrectNum(int c){
        synchronized (incorrectChars){
            Integer key = new Integer(c);
            Integer num = (Integer) incorrectChars.get(key);
            if(num == null){
                return NOT_EXISTS_NUM;
            }
            return num.intValue();
        }
    }

    public void addModel(AbstractTableModel abstractTableModel){
        this.abstractTableModel = abstractTableModel;
    }
}
