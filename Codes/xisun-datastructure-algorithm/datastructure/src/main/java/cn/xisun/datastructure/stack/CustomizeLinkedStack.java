package cn.xisun.datastructure.stack;

/**
 * @author Xisun Wang
 * @since 2024/1/25 12:23
 */
public class CustomizeLinkedStack<E> {

    private Node<E> top;

    private static class Node<E> {
        E value;

        Node<E> next;

        public Node(E value) {
            this.value = value;
        }
    }

    /**
     * 检查栈是否为空
     *
     * @return true: 栈空  false: 未空
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * 进栈（头插法）
     *
     * @param value 进栈的数据元素
     */
    public void push(E value) {
        Node<E> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
    }

    /**
     * 出栈
     *
     * @return 出栈的数据元素
     */
    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈为空，无法弹出元素");
        }

        E value = top.value;
        top = top.next;
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

        return top.value;
    }
}
