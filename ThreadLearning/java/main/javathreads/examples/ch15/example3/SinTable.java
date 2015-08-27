package javathreads.examples.ch15.example3;

import javathreads.examples.ch15.LoopHandler;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 15:18
 */
public class SinTable extends LoopHandler {

    private static final int START = 0;
    private static final int END = 360 * 100;
    private static final int THREADS = 12;
    private static final int LOOKUPVALUES_LENGTH = 360 * 100;
    private static final float HALF_CIRCLE_RADIUS = 180.0f;
    private static final int CIRCLE_RADIUS = 360;

    private float lookupValues[];

    public SinTable() {
        super(START, END, THREADS);
        lookupValues = new float[LOOKUPVALUES_LENGTH];
    }

    public void loopDoRange(int start, int end){
        for(int i = start; i < end; i++){
            float sinValue = (float)Math.sin((i % CIRCLE_RADIUS) * Math.PI / HALF_CIRCLE_RADIUS);
            lookupValues[i] = sinValue * (float) i / HALF_CIRCLE_RADIUS;
        }
    }

    public float[] getValues(){
        loopProcess();
        return lookupValues;
    }

    public static void main(String[] args){
        System.out.println("Starting Example 3 (Loop Handler Example)");
        SinTable st = new SinTable();
        float[] results = st.getValues();

        System.out.println("Results: " + results[0] + ", " +
            results[1] + ", " + results[2] + ", " + "...");
        System.out.println("Done");
    }
}
