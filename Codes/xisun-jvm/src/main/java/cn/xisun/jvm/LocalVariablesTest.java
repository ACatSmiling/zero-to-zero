package cn.xisun.jvm;

/**
 * @author XiSun
 * @since 2024/1/6 22:42
 */
public class LocalVariablesTest {

    public void test1() {
        int i = 0;
        int j = 0;
        int k = 0;
        String s = test2();
        System.out.println(s);
    }

    public String test2() {
        int l = 0;
        int m = 0;
        int n = 0;
        return "test2";
    }

    public void test3() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        int c = a + 1;
    }

    public void test4() {
        int num;
        // System.out.println(num);// 编译不通过：Variable 'num' might not have been initialized
    }

    public static void main(String[] args) {
        LocalVariablesTest test = new LocalVariablesTest();
        int num = 10;
        test.test1();
    }
}
