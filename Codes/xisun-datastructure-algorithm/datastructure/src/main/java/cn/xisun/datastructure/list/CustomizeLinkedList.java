package cn.xisun.datastructure.list;

/**
 * @author Xisun Wang
 * @since 2024/1/17 12:18
 */
public class CustomizeLinkedList<E> implements CustomizeList<E> {

    private Node<E> head;

    private int size;

    private static class Node<E> {
        E data;// 数据域

        Node<E> next;// 后继结点的指针域

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    public CustomizeLinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.data;
    }

    @Override
    public void add(E data) {
        if (head == null) {
            head = new Node<>(data, null);
        } else {
            Node<E> node = head;
            while (node.next != null) {
                node = node.next;
            }
            node.next = new Node<>(data, null);
        }
        size++;
    }

    @Override
    public void insert(int index, E data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            head = new Node<>(data, head);
        } else {
            Node<E> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            prev.next = new Node<>(data, prev.next);
        }
        size++;
    }

    @Override
    public void reset(int index, E data) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        cur.data = data;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> prev = null;
        Node<E> cur = head;
        if (index == 0) {
            head = cur.next;
            size--;
            return cur.data;
        }
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        prev.next = cur.next;
        size--;
        return cur.data;
    }

    /**
     * 头插法创建链表：新结点始终在第一的位置
     *
     * @param arr 链表初始化时的数据元素
     */
    public void createListByHeadInsertionMethod(E[] arr) {
        // 创建头结点
        head = new Node<>(null, null);

        // 对于每个要插入的元素
        for (int i = 0; i < arr.length; i++) {
            // 创建新结点
            Node<E> newNode = new Node<>(arr[i], null);
            // 将新节点插入到头结点之后
            newNode.next = head.next;
            head.next = newNode;
            size++;
        }
    }

    /**
     * 尾插法创建链表：新结点始终在最后的位置
     *
     * @param arr 链表初始化时的数据元素
     */
    public void createListByTailInsertionMethod(E[] arr) {
        // 创建头结点
        head = new Node<>(null, null);
        // 指定当前结点
        Node<E> cur = head;

        // 对于每个要插入的元素
        for (int i = 0; i < arr.length; i++) {
            // 创建新节点
            Node<E> newNode = new Node<>(arr[i], null);
            // 将新结点插入到当前结点之后
            // 移动当前结点为新结点
            cur.next = newNode;
            cur = newNode;
            size++;
        }
    }

    /**
     * 删除整个链表
     */
    public void delete() {
        Node<E> cur = head;
        while (cur != null) {
            cur.next = null;
            cur = cur.next;
            size--;
        }
    }

    /**
     * 打印整个链表
     */
    public void printList() {
        if (head != null) {
            System.out.print(head.data + " ");
        }
        Node<E> cur = head.next;
        while (cur != null) {
            System.out.print(cur.data + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        /*Integer[] arr = {1, 2, 3, 4, 5};
        CustomizeLinkedList<Integer> integerListNode = new CustomizeLinkedList<>();
        integerListNode.createListByHeadInsertionMethod(arr);
        integerListNode.printList();// 输出: 5 4 3 2 1

        Integer[] arr2 = {6, 7, 8, 9, 10};
        CustomizeLinkedList<Integer> integerListNode3 = new CustomizeLinkedList<>();
        integerListNode3.createListByTailInsertionMethod(arr2);
        integerListNode3.printList();// 输出: 6 7 8 9 10

        String[] arr3 = {"a", "b", "c", "d", "e"};
        CustomizeLinkedList<String> stringListNode = new CustomizeLinkedList<>();
        stringListNode.createListByHeadInsertionMethod(arr3);
        stringListNode.printList();// 输出: e d c b a

        String[] arr4 = {"f", "g", "h", "i", "j"};
        CustomizeLinkedList<String> stringListNode2 = new CustomizeLinkedList<>();
        stringListNode2.createListByTailInsertionMethod(arr4);
        stringListNode2.printList();// 输出: f g h i j

        System.out.println(stringListNode2.size);
        stringListNode2.delete();
        System.out.println(stringListNode2.size);*/

        CustomizeLinkedList<String> stringListNode = new CustomizeLinkedList<>();
        stringListNode.insert(0, "a");
        stringListNode.insert(1, "b");
        stringListNode.insert(2, "c");
        stringListNode.printList();// 输出: a b c
    }
}
