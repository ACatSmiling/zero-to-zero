package cn.xisun.jvm;

/**
 * @author XiSun
 * @since 2024/1/7 22:17
 */
public class OperandStackTest {
    public void testAddOperation() {
        byte i = 15;
        int j = 8;
        int k = i + j;
    }

    public int getSum() {
        int i = 10;
        int j = 20;
        int k = i + j;
        return k;
    }

    public void testGetSum() {
        // 获取上一个栈帧返回的结果，并保存在操作数栈中
        int i = getSum();
        int j = 10;
    }
}
