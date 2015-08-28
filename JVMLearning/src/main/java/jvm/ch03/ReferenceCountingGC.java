package jvm.ch03;

/**
 * Created by hombre on 2015/8/28.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/28 14:44
 *
 * testGC()方法执行后，objA 和 objB 会不会被GC呢
 */
public class ReferenceCountingGC {
    public Object instance = null;

    private static final int _1MB = 1024 * 1024;

    /**
     * 这个成员属性的唯一意义就是占点内存，以便在GC日志中看清楚是否被回收过
     */
    private byte[] bigSize = new byte[2 * _1MB];

    public static void testGC(){
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        // 假设在这行发生GC, objA 和 objB 是否能被回收?
        System.gc();
    }

    public static void main(String[] args){
        ReferenceCountingGC referenceCountingGC = new ReferenceCountingGC();
        referenceCountingGC.testGC();
    }
}
