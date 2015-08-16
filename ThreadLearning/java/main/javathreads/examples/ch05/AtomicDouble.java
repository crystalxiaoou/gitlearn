package javathreads.examples.ch05;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by hombre on 2015/8/16.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/16 19:39
 */
public class AtomicDouble extends Number{
    private AtomicReference<Double> value;

    public AtomicDouble(double initVal){
        value = new AtomicReference<Double>(new Double(initVal));
    }
    public AtomicDouble(){
        this(0.0);
    }

    public double get(){
        return value.get().doubleValue();
    }

    public void set(double newVal){
        value.set(new Double(newVal));
    }

    public boolean compareAndSet(double expect, double update){
        Double origVal, newVal;
        newVal = new Double(update);
        while(true){
            origVal = value.get();
            if(Double.compare(origVal.doubleValue(), expect) == 0){
                if(value.compareAndSet(origVal, newVal)){
                    return true;
                }else {
                    return false;
                }
            }
        }
    }

    public boolean weakCompareAndSet(double expect, double update){
        return compareAndSet(expect, update);
    }

    public double getAndSet(double setVal){
        Double origVal, newVal;
        newVal = new Double(setVal);
        while(true){
            origVal = value.get();
            if(value.compareAndSet(origVal, newVal)){
                return origVal.doubleValue();
            }
        }
    }

    public double getAndAdd(double delta){
        Double origVal, newVal;
        while(true){
            origVal = value.get();
            newVal = new Double(origVal.doubleValue() + delta);
            if(value.compareAndSet(origVal, newVal)){
                return origVal.doubleValue();
            }
        }
    }

    public double addAndGet(double delta){
        Double origVal, newVal;
        while(true){
            origVal = value.get();
            newVal = new Double(origVal.doubleValue() + delta);
            if(value.compareAndSet(origVal, newVal)){
                return newVal.doubleValue();
            }
        }
    }

    public double getAndIncrement(){
        return getAndAdd((double) 1.0);
    }

    public double getAndDecrement(){
        return getAndAdd((double) -1.0);
    }

    public double incrementAndGet(){
        return addAndGet((double) 1.0);
    }

    public double decrementAndGet(){
        return addAndGet((double) -1.0);
    }

    public double getAndMultiply(double multiple){
        Double origVal, newVal;
        while(true){
            origVal = value.get();
            newVal = new Double(origVal.doubleValue() * multiple);
            if(value.compareAndSet(origVal, newVal)){
                return origVal.doubleValue();
            }
        }
    }

    public double multiplyAndGet(double multiple){
        Double origVal, newVal;
        while(true){
            origVal = value.get();
            newVal = new Double(origVal.doubleValue() * multiple);
            if(value.compareAndSet(origVal, newVal)){
                return newVal.doubleValue();
            }
        }
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }
}
