package javathreads.examples.ch08.example4;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 9:52
 */
public class CharacterTracker extends JPanel {
    private CharCounter counter;
    private AbstractTableModel model;

    private static String[] colName = {
            "Character", "Num Correct", "Num Incorrect"
    };
    private static int[] charAt = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private class CharacterTracerModel extends AbstractTableModel {

        private static final int COLUMN_COUNT = 3;
        private static final int CHARACTER_CASE = 0;
        private static final int CORRECT_NUM_CASE = 1;
        private static final int INCORRECT_NUM_CASE = 2;

        public int getRowCount() {
            return charAt.length;
        }

        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public String getColumnName(int col){
            return colName[col];
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex){
                case CHARACTER_CASE:
                    return Character.toString((char) charAt[rowIndex]);
                case CORRECT_NUM_CASE:
                    return Integer.toString(counter.getCorrectNum(charAt[rowIndex]));
                case INCORRECT_NUM_CASE:
                    return Integer.toString(counter.getIncorrectNum(charAt[rowIndex]));
                default:
                    throw new IllegalArgumentException("Too many cols");
            }
        }
        public Class getColumnClass(int c){
            return String.class;
        }
    }

    public CharacterTracker(CharCounter charCounter){
        counter = charCounter;
        model = new CharacterTracerModel();
        counter.addModel(model);
        setLayout(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(new JTable(model));
        add(jScrollPane, BorderLayout.CENTER);
    }
}
