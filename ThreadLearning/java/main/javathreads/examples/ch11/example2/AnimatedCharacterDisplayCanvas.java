package javathreads.examples.ch11.example2;

import javathreads.examples.ch04.CharacterEvent;
import javathreads.examples.ch11.CharacterSource;
import javathreads.examples.ch11.CharacterDisplayCanvas;
import javathreads.examples.ch11.CharacterListener;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 14:00
 */
public class AnimatedCharacterDisplayCanvas extends CharacterDisplayCanvas implements CharacterListener, ActionListener {
    private static final int DELAY = 100;
    private int curX;
    private Timer timer;

    public AnimatedCharacterDisplayCanvas(CharacterSource cs){
        super(cs);
        timer = new Timer(DELAY, this);
    }

    public synchronized void newCharacter(CharacterEvent ce){
        curX = 0;
        tmpChar[0] = (char) ce.character;
        repaint();
    }

    public synchronized  void paintComponent(Graphics gc){
        if(tmpChar[0] == 0)
            return;
        Dimension d = getSize();
        int charWidth = fm.charWidth(tmpChar[0]);
        gc.clearRect(0, 0, d.width, d.height);
        gc.drawChars(tmpChar, 0, 1, curX++, fontHeight);
        if(curX > d.width - charWidth){
            curX = 0;
        }
    }


    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void setDone(boolean b){
        if(!b){
            timer.start();
        }else{
            timer.stop();
        }
    }
}
