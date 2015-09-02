package jvm.ch07;

/**
 * Created by hombre on 2015/8/31.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/31 9:12
 */
public class FieldResolution {
    interface Interface0 {
        int A = 0;
    }

    interface Interface1 extends Interface0 {
        int A = 1;
    }

    interface Interface2 {
        int A = 2;
    }

    static class Parent implements Interface1{
        public static final int A = 3;
    }

    static class Sub extends Parent implements Interface2 {
        public static final int A = 4;
    }

    public static void main(String[] args){
        System.out.println(Parent.A);
    }
}
