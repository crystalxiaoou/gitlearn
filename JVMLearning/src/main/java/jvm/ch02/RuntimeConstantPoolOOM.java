package jvm.ch02;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hombre on 2015/8/28.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/28 13:43
 *
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args){
        // 使用List保持着常量池引用，避免Full GC 回收常量池行为
        List<String> list = new ArrayList<String>();
        // 10MB的PermSize在integer范围内足够产生OOM了
        int i = 0;
        while(true){
            list.add(String.valueOf(i++).intern());
        }
    }
}

