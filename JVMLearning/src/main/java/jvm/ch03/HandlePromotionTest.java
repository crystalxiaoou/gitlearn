package jvm.ch03;

/**
 * Created by hombre on 2015/8/29.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/29 10:10
 *
 * VM 参数: -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure
 */
public class HandlePromotionTest {
    public static final int _1MB = 1024 * 1024;

    public static void testHandlePromotion(){
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5,
                allocation6, allocation7;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation1 = null;
        allocation4 = new byte[2 * _1MB];
        allocation5 = new byte[2 * _1MB];
        allocation6 = new byte[2 * _1MB];
        allocation4 = null;
        allocation5 = null;
        allocation6 = null;
        allocation7 = new byte[2 * _1MB];
    }

    public static void main(String[] args){
        testHandlePromotion();
    }
}
