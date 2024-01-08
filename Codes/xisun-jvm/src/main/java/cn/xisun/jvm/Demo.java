package cn.xisun.jvm;

/**
 * @author XiSun
 * @since 2024/1/4 13:29
 */
public class Demo {

    public void methodA() {
        int i = 0;
        int j = 1;

        methodB();
    }

    public void methodB() {
        int j = 2;
        int k = 3;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.methodA();
    }
}
