package cn.xisun.datastructure.list;

/**
 * @author Xisun Wang
 * @since 2024/1/17 12:17
 */
public class CustomizeSequentialList<E> implements CustomizeList<E> {

    private E[] list;

    private int size;

    public CustomizeSequentialList(int capacity) {
        list = (E[]) new Object[capacity];
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

        return list[index];
    }

    @Override
    public void add(E data) {
        // 当数组满时，自动扩容为原来的两倍
        if (size == list.length) {
            resize(2 * list.length);
        }
        list[size++] = data;
    }

    @Override
    public void insert(int index, E data) {
        // 注意此处判断条件，index可以等于size，此时是在数组最后一个数据的后一位插入新数据，前面的数据元素不需要移动
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // 当数组满时，自动扩容为原来的两倍
        if (size == list.length) {
            resize(2 * list.length);
        }

        // 从index位置开始，所有数据元素后移一位
        for (int i = size; i > index; i--) {
            list[i] = list[i - 1];
        }
        list[index] = data;
        size++;
    }

    @Override
    public void reset(int index, E data) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        list[index] = data;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // 从index位置开始，所有数据元素前移一位
        E removedElement = list[index];
        for (int i = index + 1; i < size; i++) {
            list[i - 1] = list[i];
        }
        size--;

        // 当元素数量少于数组长度的1/4时，自动缩小为原来的一半
        if (size > 0 && size == list.length / 4) {
            resize(list.length / 2);
        }

        return removedElement;
    }

    private void resize(int newCapacity) {
        E[] newList = (E[]) new Object[newCapacity];
        if (size >= 0) System.arraycopy(list, 0, newList, 0, size);
        list = newList;
    }
}
