package javathreads.examples.ch05.example4;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by hombre on 2015/8/17.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/17 9:11
 */
public abstract class Calculator {
    private static ThreadLocal<HashMap> results = new ThreadLocal<HashMap>(){
        @Override
        protected HashMap initialValue() {
            return new HashMap();
        }
    };

    public Object calculate(Object param){
        HashMap hm = results.get();
        Object o = hm.get(param);
        if(o != null)
            return o;
        o = doLocalCalculate(param);
        hm.put(param, o);
        return o;
    }

    protected abstract Object doLocalCalculate(Object param);
}
