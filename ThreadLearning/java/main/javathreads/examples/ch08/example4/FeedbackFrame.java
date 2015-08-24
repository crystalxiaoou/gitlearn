package javathreads.examples.ch08.example4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by hombre on 2015/8/23.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/23 13:58
 */
public class FeedbackFrame extends JFrame implements Runnable {
    private static final int PREFERRED_SIZE = 200;
    private static final int SLEEP_TIME_TO_TEST = 5 * 1000;
    private SwingTypeTester stt;
    private Thread t;
    private JLabel label;
    private int state;

    static String[] stateMessages = {
            "Connecting to Server...",
            "Logging into Server...",
            "Waiting for data...",
            "Complete"
    };

    public FeedbackFrame(SwingTypeTester stt){
        this.stt = stt;
        setupFrame();
        t = new Thread(this);
        t.start();
        pack();
        show();
    }

    private void setupFrame() {
        label = new JLabel();
        label.setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        Container c = getContentPane();
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                error();
            }
        });
        c.add(label, BorderLayout.NORTH);
        c.add(stopButton, BorderLayout.SOUTH);
    }

    private void setText(final String s){
        try{
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    label.setText(s);
                }
            });
        } catch (InterruptedException ie){
            error();
        } catch (InvocationTargetException ite){
            error();
        }
    }


    private void error() {
        t.interrupt();
        if(SwingUtilities.isEventDispatchThread()){
            closeDown();
        }else{
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    closeDown();
                }
            });
        }
    }

    private void closeDown() {
        stt.setupCancelled();
        hide();
        dispose();
    }


    public void run() {
        // Simulate connecting to server
        for(int i = 0; i < stateMessages.length; i++){
            setText(stateMessages[i]);
            try{
                Thread.sleep(SLEEP_TIME_TO_TEST);
            } catch (InterruptedException ie){ }
            if(Thread.currentThread().isInterrupted()){
                return;
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                stt.setupDone();
                hide();
                dispose();
            }
        });
    }
}
