package jvm.ch05;

/**
 * Created by hombre on 2015/8/30.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/30 21:21
 *
 */
public class NotInitialization {
    public static void main(String[] args){
       // System.out.println(SubClass.value);  非主动使用类字段演示
        /**
         * 被动使用类字段演示二
         * 通过数组定义来引用类，不会触发此类的初始化
         */
       // SuperClass[] superClasses = new SuperClass[10];
        /**
         * 被动使用类字段演示三:
         * 常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类
         * 因此不会触发定义常量的类的初始化
         */
        System.out.println(ConstClass.HELLOWORLD);
    }
}
