package javathreads.examples.ch15.example2;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 14:45
 */
public class SinTable implements Runnable {

    private static final int THREAD_SIZE = 12;
    private static final int ARRAY_LENGTH = 360 * 100;
    private static final int CIRCLE_RADIUS = 360;
    private static final float HALF_CIRCLE_RADIUS = 180.0f;

    private class SinTableRange{
        public int start, end;
    }

    private float lookupValues[];
    private Thread lookupThreads[];
    private int startLoop, endLoop, curLoop, numThreads;


    public SinTable(){
        lookupThreads = new Thread[THREAD_SIZE];
        lookupValues = new float[ARRAY_LENGTH];
        startLoop = curLoop = 0;
        endLoop = ARRAY_LENGTH;
        numThreads = THREAD_SIZE;
    }

    private synchronized SinTableRange loopGetRange(){
        if(curLoop >= endLoop)
            return null;
        SinTableRange ret = new SinTableRange();
        ret.start = curLoop;
        curLoop += (endLoop - startLoop) / numThreads + 1;
        ret.end = (curLoop < endLoop) ? curLoop : endLoop;
        return ret;
    }

    private void loopDoRange(int start, int end){
        for(int i = start; i < end; i += 1){
            float sinValue = (float) Math.sin((i % CIRCLE_RADIUS) * Math.PI / HALF_CIRCLE_RADIUS);
            lookupValues[i] = sinValue * (float)i / HALF_CIRCLE_RADIUS;
        }
    }

    public void run(){
        SinTableRange str;
        while((str = loopGetRange()) != null){
            loopDoRange(str.start, str.end);
        }
    }

    public float[] getValues(){
        for(int i = 0; i < numThreads; i++){
            lookupThreads[i] = new Thread(this);
            lookupThreads[i].start();
        }
        for(int i = 0; i < numThreads; i++){
            try {
                lookupThreads[i].join();
            } catch (InterruptedException iex){}
        }
        return lookupValues;
    }

    public static void main(String[] args){
        System.out.println("Starting Example 2 (Threaded Example)");

        SinTable st = new SinTable();
        float results[] = st.getValues();
        System.out.println("Results: " + results[0] + ", " +
            results[1] + ", " + results[2] + ", " + "...");
        System.out.println("Done");
    }
}
