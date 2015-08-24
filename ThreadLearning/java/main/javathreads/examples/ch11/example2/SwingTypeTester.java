package javathreads.examples.ch11.example2;

import javathreads.examples.ch11.CharacterDisplayCanvas;
import javathreads.examples.ch11.CharacterEventHandler;
import javathreads.examples.ch11.CharacterListener;
import javathreads.examples.ch11.CharacterSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by hombre on 2015/8/14.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/14 14:33
 */
public class SwingTypeTester extends JFrame implements CharacterSource {
    protected RandomCharacterGenerator producer;
    private AnimatedCharacterDisplayCanvas displayCanvas;
    private CharacterDisplayCanvas feedbackCanvas;
    private JButton quitButton;
    private JButton startButton;
    private JButton stopButton;
    private CharacterEventHandler handler;
    private ScoreLabel score;
    private SwingTypeTester parent;



    public SwingTypeTester(){
        initComponents();
    }

    private void initComponents() {
        parent = this;
        handler = new CharacterEventHandler();
        producer = new RandomCharacterGenerator();
        producer.setDone(true);
        producer.start();
        displayCanvas = new AnimatedCharacterDisplayCanvas(producer);
        feedbackCanvas = new CharacterDisplayCanvas(this);
        quitButton = new JButton();
        startButton = new JButton();
        stopButton = new JButton();
        score = new ScoreLabel(producer, this);

        Container pane = getContentPane();
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.PAGE_AXIS));
        p1.add(displayCanvas);
        p1.add(feedbackCanvas);

        JPanel p2 = new JPanel();
        score.setText("         ");
        score.setFont(new Font("MONOSPACED", Font.BOLD, 30));
        p2.add(score);
        startButton.setText("Start");
        p2.add(startButton);
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        p2.add(stopButton);
        quitButton.setText("Quit");
        p2.add(quitButton);
        p1.add(p2);
        pane.add(p1, BorderLayout.NORTH);
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        feedbackCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                char c = ke.getKeyChar();
                if(c != KeyEvent.CHAR_UNDEFINED){
                    newCharacter((int) c);
                }
            }
        });
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FeedbackFrame(parent).show();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                producer.setDone(true);
                displayCanvas.setDone(true);
                feedbackCanvas.setEnabled(false);
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
    }

    void setupDone(){
        displayCanvas.setDone(false);
        producer.setDone(false);
        score.resetScore();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        feedbackCanvas.setEnabled(true);
        feedbackCanvas.requestFocus();
    }

    void setupCancelled(){ }

    private void newCharacter(int c) {
        handler.fireNewCharacter(this, c);
    }

    private void quit() {
        System.exit(0);
    }

    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    public void revmoceCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    public void nextCharacter() {
        throw new IllegalStateException("We don't produce an demand");
    }

    public static void main(String[] args)  {
        new SwingTypeTester().show();
    }
}
