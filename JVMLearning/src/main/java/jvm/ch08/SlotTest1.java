package jvm.ch08;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 10:40
 */
public class SlotTest1 {
    public static void main(String[] args){
        byte[] placeholder = new byte[64 * 1024 * 1024];
        System.gc();
    }
}
