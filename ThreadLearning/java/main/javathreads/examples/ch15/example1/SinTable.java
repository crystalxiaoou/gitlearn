package javathreads.examples.ch15.example1;

/**
 * Created by hombre on 2015/8/27.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/27 14:36
 */
public class SinTable {
    private static final int LOOP_UP_VALUE_LENGTH = 360 * 100;
    private static final int CIRCLE_RIDUS = 360;
    private static final float HALF_CIRCLE_RIDUS = 180.0f;
    private float lookupValues[] = null;

    public synchronized float[] getValues(){
        if(lookupValues == null){
            lookupValues = new float[LOOP_UP_VALUE_LENGTH];
            for(int i = 0; i < LOOP_UP_VALUE_LENGTH; i++){
                float sinValue = (float) Math.sin((i % CIRCLE_RIDUS) * Math.PI / HALF_CIRCLE_RIDUS);
                lookupValues[i] = sinValue * (float) i / HALF_CIRCLE_RIDUS;
            }
        }
        return lookupValues;
    }

    public static void main(String[] args){
        System.out.println("Starting Example 1 (Control Example)");

        SinTable st = new SinTable();
        float[] results = st.getValues();
        System.out.println("Results: " + results[0] + ", " +
            results[1] + ", " + results[2] + ", " + "...");
        System.out.println("Done");
    }
}
