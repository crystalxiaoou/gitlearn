package javathreads.examples.ch12.example1;

import javathreads.examples.ch12.CharacterDisplayCanvas;
import javathreads.examples.ch12.CharacterEventHandler;
import javathreads.examples.ch12.CharacterListener;
import javathreads.examples.ch12.CharacterSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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

    public SwingTypeTester(String host, int port) throws IOException {
        initComponents(host, port);
    }

    private void initComponents(String host, int port) throws IOException {
        handler = new CharacterEventHandler();
        producer = new RandomCharacterGenerator(host, port);
        producer.setDone(true);
        producer.start();
        displayCanvas = new AnimatedCharacterDisplayCanvas(producer);
        feedbackCanvas = new CharacterDisplayCanvas(this);
        quitButton = new JButton();
        startButton = new JButton();
        stopButton = new JButton();
        score = new ScoreLabel(producer, this);

        Container pane = getContentPane();
        pane.add(displayCanvas, BorderLayout.NORTH);
        pane.add(feedbackCanvas, BorderLayout.CENTER);

        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        score.setText("     ");
        score.setFont(new Font("MONOSPACED", Font.BOLD, 30));
        p1.add(score, BorderLayout.CENTER);

        JPanel p2 = new JPanel();
        startButton.setLabel("Start");
        stopButton.setLabel("Stop");
        stopButton.setEnabled(false);
        quitButton.setLabel("Quit");
        p2.add(startButton);
        p2.add(stopButton);
        p2.add(quitButton);

        p1.add(p2, BorderLayout.EAST);
        pane.add(p1, BorderLayout.SOUTH);

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
                displayCanvas.setDone(false);
                producer.setDone(false);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                feedbackCanvas.setEnabled(true);
                feedbackCanvas.requestFocus();
                score.resetScore();
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
        pack();
    }

    private void newCharacter(int c) {
        handler.fireNewCharacter(this, c);
    }

    private void quit() {
        System.exit(0);
    }

    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }


    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    public void nextCharacter() {
        throw new IllegalStateException("We don't produce an demand");
    }

    public static void main(String[] args)  {
        String host = "localhost";
        int port = 8003;
        if(args.length >= 1){
            host = args[0];
        }
        if(args.length == 2){
            port = Integer.parseInt(args[1]);
        }
        try {
            new SwingTypeTester(host, port).show();
        } catch (IOException ioe){
            System.out.println("Can't contact server " + host + " on port " + port + ": " + ioe);
        }
    }
}
