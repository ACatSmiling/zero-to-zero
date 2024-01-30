package cn.xisun.datastructure.list;

/**
 * @author Xisun Wang
 * @since 2024/1/24 12:14
 */
public class CircularLinkedList<E> {
    Node<E> head = null;

    Node<E> tail = null;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * 添加节点到循环链表
     *
     * @param data 添加的数据元素
     */
    public void addNode(E data) {
        Node<E> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        tail.next = head;
    }

    /**
     * 显示循环链表中的节点
     */
    public void display() {
        Node<E> current = head;
        if (head == null) {
            System.out.println("List is empty");
        } else {
            do {
                System.out.print(" " + current.data);
                current = current.next;
            } while (current != head);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CircularLinkedList<Integer> circularLinkedList = new CircularLinkedList<>();

        // 添加节点
        circularLinkedList.addNode(1);
        circularLinkedList.addNode(2);
        circularLinkedList.addNode(3);
        circularLinkedList.addNode(4);

        // 显示节点
        circularLinkedList.display();// 输出:  1 2 3 4
    }
}
