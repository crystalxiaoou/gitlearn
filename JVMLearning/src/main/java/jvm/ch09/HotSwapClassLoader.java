package jvm.ch09;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 15:15
 */
public class HotSwapClassLoader extends ClassLoader {
    public HotSwapClassLoader(){
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classByte){
        return defineClass(null, classByte, 0, classByte.length);
    }
}
