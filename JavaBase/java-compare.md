---
title: Java 中的比较器
date: 2021-03-17 15:54:07
tags: java
---

在 java 中经常会涉及到对象数组等的排序问题，那么就涉及到对象之间的比较问题。

java 实现对象排序的方式有两种：

- 自然排序：`java.lang.Comparable`
- 定制排序：`java.util.Comparator`

## `java.lang.Comparable` --- 自然排序

- Comparable 接口强行对实现它的每个类的对象进行整体排序，这种排序被称为类的自然排序。

- 实现 Comparable 接口的类必须实现 `compareTo(Object obj)`，两个对象通过 `compareTo(Object obj)` 的返回值来比较大小。

  - **重写 `compareTo(Object obj)` 的规则：如果当前对象 this 大于形参对象 obj，则返回正整数，如果当前对象 this 小于形参对象 obj，则返回负整数，如果当前对象 this 等于形参对象 obj，则返回零。**

- 实现 Comparable 接口的对象列表或数组，可以通过 `Collections.sort()` (针对集合)或 `Arrays.sort()` (针对数组)进行自动排序。实现此接口的对象可以用作有序映射中的键或有序集合中的元素，无需指定比较器。

  ```java
  public class Test {
      public static void main(String[] args) {
          // 集合排序
          List<String> list = new ArrayList<>();
          list.add("AA");
          list.add("VV");
          list.add("BB");
          list.add("AC");
          list.add("CC");
          list.add("EE");
          list.add("DE");
          for (String str : list) {
              System.out.print(str + " ");// AA VV BB AC CC EE DE
          }
          System.out.println();
          Collections.sort(list);
          for (String str : list) {
              System.out.print(str + " ");// AA AC BB CC DE EE VV
          }
          System.out.println();
  
          // 数组排序
          String[] strings = {"AA", "VV", "BB", "AC", "CC", "EE", "DE"};
          System.out.println(Arrays.toString(strings));// [AA, VV, BB, AC, CC, EE, DE]
          Arrays.sort(strings);
          System.out.println(Arrays.toString(strings));// [AA, AC, BB, CC, DE, EE, VV]
      }
  }
  ```

- 对于类 C 的每一个 e1 和 e2 来说，当且仅当 `e1.compareTo(e2) == 0` 与 `e1.equals(e2)` 具有相同的 boolean 值时，类 C 的自然排序才叫做与 equals 一致。建议 (虽然不是必需的) 最好使自然排序与 equals 一致。

- Comparable 的典型实现：(默认都是从小到大排列的)

  - String 类：按照字符串中字符的 Unicode 值进行比较。
  - Character 类：按照字符的 Unicode 值来进行比较。
  - 数值类型对应的包装类以及 BigInteger 类、BigDecimal 类：按照它们对应的数值大小进行比较。
  - Boolean 类：true 对应的包装类实例大于 false 对应的包装类实例。
  - Date 类、Time 类等：后面的日期时间比前面的日期时间大。

- 对于自定义类来说，如果需要排序，我们可以让自定义类实现 Comparable 接口，并重写 `compareTo(Object obj)`，在 `compareTo(Object obj)` 中，指明如何排序。

  ```java
  public class Test {
      public static void main(String[] args) {
          Goods[] arr = new Goods[5];
          arr[0] = new Goods("lenovo", 34);
          arr[1] = new Goods("dell", 43);
          arr[2] = new Goods("xiaomi", 12);
          arr[3] = new Goods("huawei", 65);
          arr[4] = new Goods("microsoft", 43);
  
          System.out.println("排序前：" + Arrays.toString(arr));
          Arrays.sort(arr);
          System.out.println("排序后：" + Arrays.toString(arr));
      }
  }
  
  class Goods implements Comparable {
      private String name;
      private double price;
  
      public Goods() {
      }
  
      public Goods(String name, double price) {
          this.name = name;
          this.price = price;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public double getPrice() {
          return price;
      }
  
      public void setPrice(double price) {
          this.price = price;
      }
  
      @Override
      public String toString() {
          return "Goods{" +
                  "name='" + name + '\'' +
                  ", price=" + price +
                  '}';
      }
  
      // 先按照价格从低到高进行排序，再按照名称从高到低进行排序
      @Override
      public int compareTo(Object o) {
          if (o instanceof Goods) {
              Goods goods = (Goods) o;
              if (this.price > goods.price) {
                  return 1;
              } else if (this.price < goods.price) {
                  return -1;
              } else {
                  return -this.name.compareTo(goods.name);
              }
              // return Double.compare(this.getPrice(), goods.getPrice());
          }
          throw new RuntimeException("传入的数据类型有误");
      }
  }
  ```

## `java.util.Comparator` --- 定制排序

- **当元素的类型没有实现 `java.lang.Comparable` 接口而又不方便修改代码，或者实现了 `java.lang.Comparable` 接口的排序规则不适合当前的操作，那么可以考虑使用 Comparator 的对象来排序，强行对多个对象进行整体排序的比较。**

  ```java
  public class Test {
      public static void main(String[] args) {
          // 集合排序
          List<String> list = new ArrayList<>();
          list.add("AA");
          list.add("VV");
          list.add("BB");
          list.add("AC");
          list.add("CC");
          list.add("EE");
          list.add("DE");
          for (String str : list) {
              System.out.print(str + " ");// AA VV BB AC CC EE DE
          }
          System.out.println();
          // 不再以String本身默认的从小到大排序，而是从大到小排序
          Collections.sort(list, new Comparator<String>() {
              @Override
              public int compare(String o1, String o2) {
                  return o2.compareTo(o1);
              }
          });
          for (String str : list) {
              System.out.print(str + " ");// VV EE DE CC BB AC AA
          }
          System.out.println();
  
          // 数组排序
          String[] strings = {"AA", "VV", "BB", "AC", "CC", "EE", "DE"};
          System.out.println(Arrays.toString(strings));// [AA, VV, BB, AC, CC, EE, DE]
          // 不再以String本身默认的从小到大排序，而是从大到小排序
          Arrays.sort(strings, new Comparator<String>() {
              @Override
              public int compare(String o1, String o2) {
                  return -o1.compareTo(o2);
              }
          });
          System.out.println(Arrays.toString(strings));// [VV, EE, DE, CC, BB, AC, AA]
      }
  }
  ```

- **重写 `compare(Object o1,Object o2)`，比较 o1 和 o2 的大小： 如果方法返回正整数，则表示 o1 大于 o2；如果返回 0，表示相等；返回负整数，表示 o1 小于 o2。**

  ```java
  public class Test {
      public static void main(String[] args) {
          Goods[] arr = new Goods[5];
          arr[0] = new Goods("lenovo", 34);
          arr[1] = new Goods("dell", 43);
          arr[2] = new Goods("xiaomi", 12);
          arr[3] = new Goods("huawei", 65);
          arr[4] = new Goods("lenovo", 43);
  
          System.out.println("排序前：" + Arrays.toString(arr));
          // 不以Goods本身的自然排序方式排序，更改为：按产品名称从低到高进行排序，再按照价格从高到低进行排序
          Arrays.sort(arr, new Comparator<Goods>() {
              @Override
              public int compare(Goods o1, Goods o2) {
                  if (!o1.getName().equals(o2.getName())) {
                      return o1.getName().compareTo(o2.getName());
                  } else {
                      if (o1.getPrice() < o2.getPrice()) {
                          return 1;
                      } else if (o1.getPrice() > o2.getPrice()) {
                          return -1;
                      }
                  }
                  return 0;
              }
          });
          System.out.println("排序后：" + Arrays.toString(arr));
      }
  }
  
  class Goods implements Comparable {
      private String name;
      private double price;
  
      public Goods() {
      }
  
      public Goods(String name, double price) {
          this.name = name;
          this.price = price;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public double getPrice() {
          return price;
      }
  
      public void setPrice(double price) {
          this.price = price;
      }
  
      @Override
      public String toString() {
          return "Goods{" +
                  "name='" + name + '\'' +
                  ", price=" + price +
                  '}';
      }
  
      // 先按照价格从低到高进行排序，再按照名称从高到低进行排序
      @Override
      public int compareTo(Object o) {
          if (o instanceof Goods) {
              Goods goods = (Goods) o;
              if (this.price > goods.price) {
                  return 1;
              } else if (this.price < goods.price) {
                  return -1;
              } else {
                  return -this.name.compareTo(goods.name);
              }
              // return Double.compare(this.getPrice(), goods.getPrice());
          }
          throw new RuntimeException("传入的数据类型有误");
      }
  }
  ```

- 可以将 Comparator 传递给 `sort()`，比如：`Collections.sort()` 或 `Arrays.sort()`，从而允许在排序顺序上实现精确控制。

- 还可以使用 Comparator 来控制某些数据结构 (如有序 set 或有序映射) 的顺序，或者为那些没有自然顺序的对象 collection 提供排序。

## Comparable 和 Comparator 的对比

- Comparable 接口的方式一旦指定，能够保证 Comparable 接口实现类的对象在任何位置都可以比较大小。
- Comparator 接口属于临时性的比较，什么时候需要什么时候实现。

## 本文参考

https://www.gulixueyuan.com/goods/show/203?targetId=309&preview=0

声明：写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。