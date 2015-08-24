package javathreads.examples.ch11.example1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 20:18
 */
public class URLMonitorPanel extends JPanel implements URLPingTask.URLUpdate {
    private static final int STATUS_WIDTH = 20;
    private static final int STATUS_HEIGHT = 20;
    private static final long DELAY = 0L;
    private static final long PERIOD = 5L;
    private static final int EXIT_STATUS = 0;
    private static final int TIMEOUT_TIME = 120;

    ScheduledThreadPoolExecutor executor;
    ScheduledFuture future;


    URL url;
    URLPingTask task;
    JPanel status;
    JButton startButton, stopButton;

    static Future<Integer> futureTaskResult;
    static volatile boolean done = false;

    private void checkLicense(){
        if(done) return;
        try {
            Integer I = futureTaskResult.get(0L, TimeUnit.SECONDS);
            // 若取得结果，则知道许可已经到期
            JOptionPane.showMessageDialog(null, "Evaluation time period has expired", "Expired",
                    JOptionPane.INFORMATION_MESSAGE);
            done = true;
        } catch (TimeoutException te){
            // task还没有运行，继续
        } catch (InterruptedException ie){
            // task被外部中断
        } catch (ExecutionException ee){
            // task 抛出IOException, 可用下列方法取得
            IOException ioe = (IOException) ee.getCause();
            // 在异常后进行清理动作
        }
    }

    public URLMonitorPanel(String url, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) throws MalformedURLException {
        setLayout(new BorderLayout());
        executor = scheduledThreadPoolExecutor;
        this.url = new URL(url);
        add(new JLabel(url), BorderLayout.CENTER);
        JPanel temp = new JPanel();
        status = new JPanel();
        status.setSize(STATUS_WIDTH, STATUS_HEIGHT);
        temp.add(status);
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeTask();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        stopButton = new JButton("Stop");
        stopButton.setEnabled(true);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                future.cancel(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
        temp.add(startButton);
        temp.add(stopButton);
        add(temp, BorderLayout.EAST);
        makeTask();
    }

    private void makeTask() {
        task = new URLPingTask(url, this);
        future = executor.scheduleAtFixedRate(task, DELAY, PERIOD, TimeUnit.SECONDS);
    }

    public void isAlive(final boolean isAlive) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    checkLicense();
                    if (done) {
                        future.cancel(true);
                        startButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        return;
                    }
                    status.setBackground(isAlive ? Color.GREEN : Color.RED);
                    status.repaint();
                }
            });
        }catch (Exception e){}
    }

    public static void main(String[] args) throws MalformedURLException {
        JFrame jFrame = new JFrame("URL Monitor");
        Container c = jFrame.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        ScheduledThreadPoolExecutor se = new ScheduledThreadPoolExecutor(
                (args.length + 1) /2);
        TimeoutTask tt = new TimeoutTask();
        futureTaskResult = se.schedule(tt, TIMEOUT_TIME, TimeUnit.SECONDS);
        for(int i = 0; i < args.length; i++){
            c.add(new URLMonitorPanel(args[i], se));
        }

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(EXIT_STATUS);
            }
        });
        jFrame.pack();
        jFrame.show();
    }
}
