package cn.xisun.datastructure.list;

/**
 * @author Xisun Wang
 * @since 2024/1/17 17:27
 */
public class CustomizeStaticLinkedList<E> {
    private Node<E>[] nodes;

    private int maxSize;

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private static class Node<E> {
        E data;
        int cur;

        Node(E data, int cur) {
            this.data = data;
            this.cur = cur;
        }
    }

    public CustomizeStaticLinkedList(int capacity) {
        this.nodes = (Node<E>[]) new Node<?>[capacity];
        // 初始化一个空的静态链表，使得链表的所有元素都连接在一起，但是还没有存储任何实际的数据
        for (int i = 0; i < capacity; i++) {
            // 使用(i + 1) % capacity，来获取当前结点的下一个元素在数组中的位置，并确保cur值在数组的范围内
            nodes[i] = new Node<>(null, (i + 1) % capacity);
            System.out.println(nodes[i].cur);
        }
        System.out.println();
        this.head = nodes[0];
        this.head.cur = 1;// head的cur初始值设为1
        this.tail = nodes[capacity - 1];
        this.tail.cur = 0;// tail的cur初始值设为0
        this.maxSize = capacity - 2;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    /**
     * 获取元素
     * 注意：此处的index不是数据元素在数组中的位置，链表中的数据元素顺序是逻辑顺序，不一定和其存储的数组中数据元素的物理位置一致
     *
     * @param index 数据元素在链表中的位置
     * @return 获取的数据元素
     */
    public E get(int index) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 头结点
        int curr = tail.cur;
        // 从头结点依次找到index位置的前一个结点
        for (int i = 0; i < index; i++) {
            curr = nodes[curr].cur;
        }
        return nodes[curr].data;
    }

    /**
     * 添加元素
     *
     * @param data 添加的数据元素
     */
    public void add(E data) {
        if (size == maxSize) {
            throw new RuntimeException("List is full");
        }

        // 添加时默认从第一个位置开始
        if (tail.cur == 0) {
            tail.cur = 1;
        }

        int next = head.cur;
        nodes[next].data = data;
        head.cur = nodes[next].cur;
        size++;
    }

    /**
     * 插入元素
     * 注意：此处的index不是数据元素在数组中的位置，链表中的数据元素顺序是逻辑顺序，不一定和其存储的数组中数据元素的物理位置一致
     *
     * @param index 数据元素在链表中的位置，
     * @param data  插入的数据元素
     */
    public void insert(int index, E data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (size == maxSize) {
            throw new RuntimeException("List is full");
        }

        if (index == 0) {
            nodes[1].data = data;
            tail.cur = 1;
            head.cur = nodes[1].cur;
        } else {
            // 头结点
            Node<E> curr = nodes[tail.cur];
            // 从头结点依次找到index位置的前一个结点
            for (int i = 0; i < index; i++) {
                curr = nodes[curr.cur];
            }
            nodes[head.cur].cur = curr.cur;
            curr.cur = head.cur;

            nodes[head.cur].data = data;
            head.cur = nodes[head.cur].cur;
        }

        size++;
    }


    public void printList() {
        Node<E> currentNode = nodes[tail.cur];
        for (int i = 0; i < size; i++) {
            System.out.println("位置: " + i + ", 数据: " + currentNode.data);
            currentNode = nodes[currentNode.cur];
        }
    }

    public static void main(String[] args) {
        CustomizeStaticLinkedList<Integer> list = new CustomizeStaticLinkedList<>(5);
        list.insert(0, 2);
        list.insert(1, 3);
        list.printList();
        System.out.println(list.size);
        System.out.println(list.head.cur);
        System.out.println(list.tail.cur);
    }
}
