package jvm.ch07;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 9:47
 *
 * 类加载器与instanceof 关键字演示
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws Exception{
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if(is == null){
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException ioe){
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myLoader.loadClass("jvm.ch07.ClassLoaderTest");
        System.out.println(obj.getClass());
        System.out.println(obj instanceof jvm.ch07.ClassLoaderTest);
    }
}
