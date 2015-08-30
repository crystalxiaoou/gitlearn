package jvm.ch05;

/**
 * Created by hombre on 2015/8/30.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/30 21:18
 *
 * 被动使用类字段演示一:
 * 通过子类引用父类的静态字段, 不会导致子类初始化
 */
public class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }
    public static int value = 123;
}
