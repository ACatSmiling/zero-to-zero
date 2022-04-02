---
title: Java 关闭 IO 流
date: 2020-12-14 21:54:17
tags: java
---

在操作 java 流对象后要将流关闭，但实际编写代码时，可能会出现一些误区，导致不能正确关闭流。

## 在 try 中关流，而没在 finally 中关流

错误：

```java
try {
	OutputStream out = new FileOutputStream("");
	// ...操作流代码
	out.close();
} catch (Exception e) {
	e.printStackTrace();
}
```

修正：

```java

OutputStream out = null;
try {
	out = new FileOutputStream("");
	// ...操作流代码
} catch (Exception e) {
	e.printStackTrace();
} finally {
	try {
		if (out != null) {
			out.close();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

## 在一个 try 中关闭多个流

错误：

```java
OutputStream out = null;
OutputStream out2 = null;
try {
	out = new FileOutputStream("");
	out2 = new FileOutputStream("");
	// ...操作流代码
} catch (Exception e) {
	e.printStackTrace();
} finally {
	try {
		if (out != null) {
			out.close();// 如果此处出现异常，则out2流没有被关闭
		}
		if (out2 != null) {
			out2.close();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

修正：

```java
OutputStream out = null;
OutputStream out2 = null;
try {
	out = new FileOutputStream("");
	out2 = new FileOutputStream("");
	// ...操作流代码
} catch (Exception e) {
	e.printStackTrace();
} finally {
	try {
		if (out != null) {
			out.close();// 如果此处出现异常，则out2流也会被关闭
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	try {
		if (out2 != null) {
			out2.close();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

## 在循环中创建流，在循环外关闭，导致关闭的是最后一个流

错误：

```java
OutputStream out = null;
try {
	for (int i = 0; i < 10; i++) {
		out = new FileOutputStream("");
		// ...操作流代码
	}
} catch (Exception e) {
	e.printStackTrace();
} finally {
	try {
		if (out != null) {
			out.close();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

修正：

```java
for (int i = 0; i < 10; i++) {
	OutputStream out = null;
	try {
		out = new FileOutputStream("");
		// ...操作流代码
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

## 在 java 7 中，关闭流的方式得到很大的简化

```java
try (OutputStream out = new FileOutputStream("")){
	// ...操作流代码
} catch (Exception e) {
	e.printStackTrace();
}
```

只要实现的自动关闭接口 (Closeable) 的类都可以在 try 结构体上定义，java 会自动帮我们关闭，即使在发生异常的情况下也会。

可以在 try 结构体上定义多个，用分号隔开即可，如：

```java
try (OutputStream out = new FileOutputStream("");
     OutputStream out2 = new FileOutputStream("")){
	// ...操作流代码
} catch (Exception e) {
	throw e;
}
```

> Android SDK 20 版本对应 java 7，低于 20 版本无法使用 `try-catch-resources` 自动关流。

## 内存流的关闭

内存流可以不用关闭。

ByteArrayOutputStream 和 ByteArrayInputStream 其实是伪装成流的字节数组 (把它们当成字节数据来看就好了)，他们不会锁定任何文件句柄和端口，如果不再被使用，字节数组会被垃圾回收掉，所以不需要关闭。

## 装饰流的关闭

装饰流是指通过装饰模式实现的 java 流，又称为包装流，装饰流只是为原生流附加额外的功能或效果，java 中的缓冲流、桥接流也是属于装饰流。

例如：

```java
InputStream is = new FileInputStream("C:\\Users\\tang\\Desktop\\test.txt");
InputStreamReader isr = new InputStreamReader(is);
BufferedReader br = new BufferedReader(isr);
String string = br.readLine();
System.out.println(string);

// 只需要关闭最后的br即可
try {
    br.close();
} catch (Exception e) {
    e.printStackTrace();
}
```

装饰流关闭时会调用原生流关闭。

BufferedReader.java 源码如下：

```java
public void close() throws IOException {
    synchronized (lock) {
        if (in == null)
            return;
        try {
            // 这里的in就是原生流
            in.close();
        } finally {
            in = null;
            cb = null;
        }
    }
}
```

InputStreamReader.java 源码如下：

```java
public void close() throws IOException {
    // 这里的sd就是原生流的解码器(StreamDecoder)，解码器的close会调用原生流的close
    sd.close();
}
```

如上所示，有这样层层关闭的机制，我们就只需要关闭最外层的流就行了。

## 关闭流的顺序问题

两个不相干的流的关闭顺序没有任何影响，如：

```java
// 这里的out1和out2谁先关谁后关都一样，没有任何影响
out1 = new FileOutputStream("");
out2 = new FileOutputStream("");
```

如果两个流有依赖关系，那么可以像上面说的，只关闭最外层的即可。

如果不嫌麻烦，非得一个个关闭，那么需要先关闭最里层，从里往外一层层进行关闭。

为什么不能从外层往里层逐步关闭？原因上面讲装饰流已经讲的很清楚了，关闭外层时，内层的流其实已经同时关闭了，你再去关内层的流，就会报错。

至于网上说的先声明先关闭，就是这个道理，先声明的是内层，最先申明的是最内层，最后声明的是最外层。

## 一定要关闭流的原因

一个流绑定了一个文件句柄 (或网络端口)，如果流不关闭，该文件 (或端口) 将始终处于被锁定 (不能读取、写入、删除和重命名) 状态，占用大量系统资源却没有释放。

## 本文参考

https://blog.csdn.net/u012643122/article/details/38540721

声明：写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。