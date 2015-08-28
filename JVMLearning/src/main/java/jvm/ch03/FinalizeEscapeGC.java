package jvm.ch03;

/**
 * Created by hombre on 2015/8/28.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/8/28 16:06
 *
 * 此代码演示了两点：
 * 1.对象可以在被GC时自我拯救
 * 2.这种自救的机会只有一次，因为一个对象的finalize()方法的最多只会被系统自动调用一次
 */
public class FinalizeEscapeGC {
    private static final int SLEEP_TIME = 500;
    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive(){
        System.out.println("yes, i am still alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        // 对象第一次成功拯救自己
        SAVE_HOOK = null;
        System.gc();
        // 因为Finalize()方法优先级很低，应该暂停0.5秒以等待它
        Thread.sleep(SLEEP_TIME);
        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no, i am dead :(");
        }

        // 下面这段代码与上面的完全相同，但是这次却自救失败了
        SAVE_HOOK = null;
        System.gc();
        // 因为Finalize()方法优先级很低，应该暂停0.5秒以等待它
        Thread.sleep(SLEEP_TIME);
        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no, i am dead :(");
        }
    }

}
