package cn.xisun.datastructure.list;

/**
 * @author Xisun Wang
 * @since 2024/1/17 12:34
 */
public interface CustomizeList<E> {

    /**
     * 获取线性表大小
     *
     * @return 线性表大小
     */
    int size();

    /**
     * 返回线性表是否为空
     *
     * @return 线性表是否为空
     */
    boolean isEmpty();

    /**
     * 获取元素
     *
     * @param index 指定位置
     * @return 获取的位置元素
     */
    E get(int index);


    /**
     * 添加元素
     *
     * @param data 添加的元素
     */
    void add(E data);


    /**
     * 插入元素
     *
     * @param index 指定位置
     * @param data  插入的元素
     */
    void insert(int index, E data);

    /**
     * 替换元素
     *
     * @param index 指定位置
     * @param data  替换的元素
     */
    void reset(int index, E data);

    /**
     * 移除元素
     *
     * @param index 指定位置
     * @return 移除的元素
     */
    E remove(int index);
}
