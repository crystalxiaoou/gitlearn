package jvm.ch03;

/**
 * Created by hombre on 2015/8/28.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/28 21:23
 *
 * VM 参数: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 */
public class PretenureSizeThreshold {
    private static final int _1MB = 1024 * 1024;

    public static void testPretenureSizeThreshold(){
        byte[] allocation;
        allocation = new byte[4 * _1MB]; // 直接分配在老年代
    }

    public static void main(String[] args){
        testPretenureSizeThreshold();
    }
}
