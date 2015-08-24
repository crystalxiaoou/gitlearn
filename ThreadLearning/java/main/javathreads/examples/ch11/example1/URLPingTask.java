package javathreads.examples.ch11.example1;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by hombre on 2015/8/24.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/24 20:12
 */
public class URLPingTask implements Runnable {

    private static final int LOOP_TIME = 5000;
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 1000;
    private static final boolean IS_ALIVE = true;
    private static final boolean IS_NOT_ALIVE = false;

    public interface URLUpdate {
        void isAlive(boolean isAlive);
    }

    URL url;
    URLUpdate updater;

    public URLPingTask(URL url){
        this(url, null);
    }

    public URLPingTask(URL url, URLUpdate urlUpdate){
        this.url = url;
        updater = urlUpdate;
    }


    public void run() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            int code = httpURLConnection.getResponseCode();
            if(updater != null ){
                if(code == HttpURLConnection.HTTP_OK) {
                    updater.isAlive(IS_ALIVE);
                }else{
                    updater.isAlive(IS_NOT_ALIVE);
                }
            }
        } catch (Exception e){
            if(updater != null){
                updater.isAlive(IS_NOT_ALIVE);
            }
        }
    }

}
