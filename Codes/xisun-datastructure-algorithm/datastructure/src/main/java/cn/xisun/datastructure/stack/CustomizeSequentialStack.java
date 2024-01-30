package cn.xisun.datastructure.stack;

/**
 * @author Xisun Wang
 * @since 2024/1/25 11:56
 */
public class CustomizeSequentialStack<E> {
    private final E[] stack;

    private final int maxSize;

    private int top;

    public CustomizeSequentialStack(int capacity) {
        this.stack = (E[]) new Object[capacity];
        this.maxSize = capacity - 1;
        this.top = -1;// 栈顶初始化为-1，表示栈为空
    }

    /**
     * 检查栈是否已满
     *
     * @return true: 栈满  false: 未满
     */
    public boolean isFull() {
        return top == maxSize;
    }

    /**
     * 检查栈是否为空
     *
     * @return true: 栈空  false: 未空
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * 向栈顶添加一个元素
     *
     * @param value 进栈的数据元素
     */
    public void push(E value) {
        if (isFull()) {
            throw new RuntimeException("栈已满，无法添加元素");
        }

        stack[top] = value;
        top++;
    }

    /**
     * 移除并返回栈顶元素
     *
     * @return 出栈的数据元素
     */
    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈为空，无法弹出元素");
        }

        E value = stack[top];
        top--;
        return value;
    }

    /**
     * 返回但不移除栈顶元素
     *
     * @return 栈顶的数据元素
     */
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("栈为空，无法获取栈顶元素");
        }

        return stack[top];
    }
}
