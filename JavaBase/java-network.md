---
title: Java 网络编程
date: 2021-04-05 12:39:24
tags: java
---

## 网络编程概述

- Java 是 Internet 上的语言，它从语言级上提供了对网络应用程序的支持，程序员能够很容易开发常见的网络应用程序。
- Java 提供的网络类库，可以实现无痛的网络连接，联网的底层细节被隐藏在 Java 的本机安装系统里，由 JVM 进行控制。并且 Java 实现了一个跨平台的网络库， 程序员面对的是一个统一的网络编程环境。
- 计算机网络：把分布在不同地理区域的计算机与专门的外部设备用通信线路互连成一个规模大、功能强的网络系统，从而使众多的计算机可以方便地互相传递信息、共享硬件、软件、数据信息等资源。
- 网络编程的目的：直接或间接地通过网络协议与其它计算机实现数据交换，进行通讯。
- 网络编程中有两个主要的问题：
  - 如何准确地定位网络上一台或多台主机，以及定位主机上的特定的应用。
  - 找到主机后如何可靠高效地进行数据传输。

## 网络通信要素概述

- IP 和端口号
- 网络通信协议

- 如何实现网络中的主机互相通信：

  - **通信双方地址：**

    - IP
    - 端口号

  - **一定的规则 (即：网络通信协议。有两套参考模型)：**

    - OSI 参考模型：模型过于理想化，未能在因特网上进行广泛推广。

    - **TCP/IP 参考模型 (或叫 TCP/IP 协议)：事实上的国际标准。**

      <img src="java-network/image-20210405204926090.png" alt="image-20210405204926090" style="zoom:67%;" />

- 网络中数据传输过程：

  <img src="java-network/image-20210405205407834.png" alt="image-20210405205407834" style="zoom:67%;" />

## 通信要素 1：IP 和端口号

-  **IP 地址**：Java 中，一个 InetAddress 类的实例对象，就代表一个 IP 地址。

  - 唯一的标识 Internet 上的计算机 (通信实体)。
  - 本地回环地址 (hostAddress)：127.0.0.1，主机名 (hostName)：localhost。
  - IP 地址分类方式 1：**IPV4 和 IPV6。**
    - IPV4：4 个字节组成，4 个 0 ~ 255。大概 42 亿，30 亿都在北美，亚洲 4亿。2011 年初已经用尽。以点分十进制表示，如 192.168.0.1。
    - IPV6：128 位，16个字节，写成 8 个无符号整数，每个整数用四个十六进制位表示，数之间用冒号 : 分开，如：3ffe:3201:1401:1280:c8ff:fe4d:db39:1984。
  - IP 地址分类方式 2： **公网地址 (万维网使用) 和私有地址 (局域网使用)。**192.168. 开头的就是私有址址，范围为 192.168.0.0 ~ 192.168.255.255，专门为组织机构内部使用。
  - 特点：不易记忆。

- **端口号**：标识正在计算机上运行的进程 (程序)。

  - 不同的进程有不同的端口号。
  - 被规定为一个 16 位的整数 0 ~ 65535。
  - 端口分类：
    - **公认端口**：0 ~ 1023。被预先定义的服务通信占用，如：HTTP 占用端口 80，FTP 占用端口 21，Telnet 占用端口 23 等。
    - **注册端口**：1024 ~ 49151。分配给用户进程或应用程序，如：Tomcat 占用端口 8080，MySQL 占用端口 3306，Oracle 占用端口 1521 等。
    - **动态/私有端口**：49152 ~ 65535。

- **端口号与 IP 地址的组合得出一个网络套接字：Socket。**

- InetAddress 类：

  - Internet 上的主机有两种方式表示地址：

    - 域名 (hostName)，如：www.atguigu.com。
    - IP 地址 (hostAddress)，如：202.108.35.210。

  - InetAddress 类主要表示 IP 地址，两个子类：Inet4Address、Inet6Address。

  - InetAddress 类对象含有一个 Internet 主机地址的域名和 IP 地址，如：www.atguigu.com 和 202.108.35.210。

  - 域名容易记忆，当在连接网络时输入一个主机的域名后，域名服务器 (DNS) 负责将域名转化成 IP 地址，这样才能和主机建立连接。这就是**域名解析**。

    - 域名解析时，先找本机 hosts 文件，确定是否有输入的域名地址，如果没有，再通过 DNS 服务器，找到对应的主机。

      <img src="java-network/image-20210406115221751.png" alt="image-20210406115221751" style="zoom:67%;" />

  - InetAddress 类没有提供公共的构造器，而是提供了如下几个静态方法来获取 InetAddress 实例：

    - `public static InetAddress getLocalHost()`
    - `public static InetAddress getByName(String host)`

  - InetAddress 类提供了如下几个常用的方法：

    - `public String getHostAddress()`：返回 IP 地址字符串，以文本表现形式。
    - `public String getHostName()`：获取此 IP 地址的主机名。
    - `public boolean isReachable(int timeout)`：测试是否可以达到该地址。

  - 实例：

    ```java
    /**
     * 一、网络编程中有两个主要的问题：
     * 1.如何准确地定位网络上一台或多台主机；定位主机上的特定的应用
     * 2.找到主机后如何可靠高效地进行数据传输
     *
     * 二、网络编程中的两个要素：
     * 1.对应问题一：IP和端口号
     * 2.对应问题二：提供网络通信协议：TCP/IP参考模型（应用层、传输层、网络层、物理+数据链路层）
     *
     *
     * 三、通信要素一：IP和端口号
     *
     * 1. IP: 唯一的标识 Internet 上的计算机（通信实体）
     * 2. 在Java中使用InetAddress类代表IP
     * 3. IP分类：IPv4 和 IPv6 ; 万维网 和 局域网
     * 4. 域名:   www.baidu.com   www.mi.com  www.sina.com  www.jd.com  www.vip.com
     * 5. 本地回路地址：127.0.0.1 对应着：localhost
     *
     * 6. 如何实例化InetAddress:两个方法：getByName(String host) 、 getLocalHost()
     *        两个常用方法：getHostName() / getHostAddress()
     *
     * 7. 端口号：正在计算机上运行的进程。
     * 要求：不同的进程有不同的端口号
     * 范围：被规定为一个 16 位的整数 0~65535。
     *
     * 8. 端口号与IP地址的组合得出一个网络套接字：Socket
     */
    public class InetAddressTest {
        public static void main(String[] args) {
            try {
                InetAddress inet1 = InetAddress.getByName("192.168.10.14");
                System.out.println(inet1);// /192.168.10.14
    
                InetAddress inet2 = InetAddress.getByName("www.atguigu.com");
                System.out.println(inet2);// www.atguigu.com/58.215.145.131
    
                InetAddress inet3 = InetAddress.getByName("127.0.0.1");
                System.out.println(inet3);// /127.0.0.1
    
                // 获取本地ip
                InetAddress inet4 = InetAddress.getLocalHost();
                System.out.println(inet4);
    
                // getHostAddress()
                System.out.println(inet2.getHostAddress());// 58.215.145.131
                // getHostName()
                System.out.println(inet2.getHostName());// www.atguigu.com
                // isReachable(int timeout)
                try {
                    System.out.println(inet2.isReachable(10));// true
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    ```

## 通信要素 2：网络通信协议

- **网络通信协议：计算机网络中实现通信必须有一些约定，即通信协议，对速率、传输代码、代码结构、传输控制步骤、出错控制等制定标准。**

- 问题：网络协议太复杂。计算机网络通信涉及内容很多，比如指定源地址和目标地址、加密解密、压缩解压缩、差错控制、流量控制、路由控制等，如何实现如此复杂的网络协议呢？

- 通信协议分层的思想：在制定协议时，把复杂成份分解成一些简单的成份，再将它们复合起来。最常用的复合方式是层次方式，即**同层间可以通信、上一层可以调用下一层，而与再下一层不发生关系。**各层互不影响，利于系统的开发和扩展。

- TCP/IP 协议簇：

  - **TCP/IP 协议簇以其两个主要协议传输控制协议 (TCP) 和网络互联协议 (IP) 而得名**，实际上是一组协议，包括多个具有不同功能且互为关联的协议。
  - 传输层协议中两个非常重要的协议：
    - **传输控制协议 TCP** (Transmission Control Protocol)。
    - **用户数据报协议 UDP** (User Datagram Protocol)。
  - 网络层的主要协议： 网络互联协议 IP (Internet Protocol)，其支持网间互连的数据通信。
  - TCP/IP 协议模型从更实用的角度出发，形成了高效的四层体系结构，即**物理链路层、IP 层、传输层和应用层**。

- **TCP 协议：**

  - 使用 TCP 协议前，**须先建立 TCP 连接**，形成传输数据通道。

  - 传输前，采用 **"三次握手"** 方式，点对点通信，**是可靠的**。

    <img src="java-network/image-20210406150220705.png" alt="image-20210406150220705" style="zoom:67%;" />

  - TCP 协议进行通信的两个应用进程：客户端、服务端。

  - 在连接中**可进行大数据量的传输**。

  - 传输完毕，采用 **"四次挥手"** 方式，**释放已建立的连接，效率相对低**。

    <img src="java-network/image-20210406150310805.png" alt="image-20210406150310805" style="zoom:67%;" />

- **UDP 协议：**

  - 将数据、源、目的封装成数据包，**不需要建立连接**。
  - 每个数据报的大小限制在 64 K 内。
  - 发送不管对方是否准备好，接收方收到也不确认，故**是不可靠的**。
  - 可以广播发送。
  - 发送数据结束时**无需释放资源，开销小，速度相对快**。

## TCP 网络编程

- Socket 类实现了基于 TCP 协议的网络编程。

- Socket 类：

  - 利用套接字 (Socket) 开发网络应用程序早已被广泛的采用，以至于成为事实上的标准。
  - **网络上具有唯一标识的 IP 地址和端口号组合在一起，才能构成唯一能识别的标识符套接字。**
  - 通信的两端都要有 Socket，是两台机器间通信的端点。
  - 网络通信其实就是 Socket 间的通信。
  - Socket 允许程序把网络连接当成一个流，数据在两个 Socket 间通过 IO 传输。
  - 一般主动发起通信的应用程序属客户端，等待通信请求的为服务端。
  - Socket 类：
    - **流套接字 (stream socket)：使用 TCP 提供可依赖的字节流服务。**
    - **数据报套接字 (datagram socket)：使用 UDP 提供 "尽力而为" 的数据报服务。**
  - Socket 类的常用构造器 ：
    - `public Socket(InetAddress address,int port)`：创建一个流套接字并将其连接到指定 IP 地址的指定端口号。
    - `public Socket(String host,int port)`：创建一个流套接字并将其连接到指定主机上的指定端口号。
  - Socket 类的常用方法：
    - `public InputStream getInputStream()`：返回此套接字的输入流。可以用于接收网络消息。
    - `public OutputStream getOutputStream()`：返回此套接字的输出流。可以用于发送网络消息。
    - `public InetAddress getInetAddress()`：返回此套接字连接到的远程 IP 地址；如果尚未连接套接字，则返回 null。
    - `public InetAddress getLocalAddress()`：返回此套接字绑定的本地地址，即本端的 IP 地址。
    - `public int getPort()`：返回此套接字连接到的远程端口号；如果尚未连接套接字，则返回 0。
    - `public int getLocalPort()`：返回此套接字绑定的本地端口，即本端的端口号。如果尚未绑定套接字，则返回 -1。
    - `public void close()`：关闭此套接字。套接字被关闭后，便不可在以后的网络连接中使用 (即无法重新连接或重新绑定)。需要创建新的套接字对象。关闭此套接字也将会关闭该套接字的 InputStream 和 OutputStream。
    - `public void shutdownInput()`：关闭此套接字的输入流。如果在套接字上调用 `shutdownInput()` 后再从套接字输入流读取内容，则流将返回 EOF (文件结束符)，即不能再从此套接字的输入流中接收任何数据。
    - `public void shutdownOutput()`：关闭此套接字的输出流。对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。 如果在套接字上调用 `shutdownOutput()` 后再写入套接字输出流，则该流将抛出 IOException，即不能再通过此套接字的输出流发送任何数据。
  
- Java 语言的基于套接字编程分为服务端编程和客户端编程，其通信模型如图所示：

  <img src="java-network/image-20210406213311857.png" alt="image-20210406213311857" style="zoom:67%;" />
  
- **服务端 Scoket 的工作过程包含以下四个基本的步骤：**

  - 调用 `ServerSocket(int port)`：创建一个服务器端套接字，并绑定到指定端口上。用于监听客户端的请求。
  - 调用 `accept()`：监听连接请求，如果客户端请求连接，则接受连接，并返回通信套接字对象。
  - 调用该 Socket 类对象的 `getOutputStream()` 和 `getInputStream()`：获取输出流和输入流，开始网络数据的发送和接收。
  - 关闭 ServerSocket 和 Socket 对象：客户端访问结束，关闭通信套接字。
  
- **客户端 Socket 的工作过程包含以下四个基本的步骤：**

  - 创建 Socket：根据指定服务端的 IP 地址或端口号构造 Socket 类对象。若服务器端响应，则建立客户端到服务器的通信线路。若连接失败，会出现异常。
  - 打开连接到 Socket 的输入/输出流：使用 `getInputStream()` 获得输入流，使用 `getOutputStream()` 获得输出流，进行数据传输。
  - 按照一定的协议对 Socket 进行读/写操作：通过输入流读取服务器放入通信线路的信息 (但不能读取自己放入通信线路的信息)，通过输出流将信息写入通信线路。
  - 关闭 Socket：断开客户端到服务器的连接，释放通信线路。
  
- 服务器建立 ServerSocket 对象：

  - ServerSocket 对象负责等待客户端请求建立套接字连接，类似邮局某个窗口中的业务员。也就是说，**服务器必须事先建立一个等待客户请求建立套接字**
    **连接的 ServerSocket 对象。**
  
  - 所谓 "接收" 客户的套接字请求，就是 `accept()` 会返回一个 Socket 对象。
  
    ```java
    ServerSocket ss = new ServerSocket(9999);
    Socket s = ss.accept();
    InputStream in = s.getInputStream();
    byte[] buf = new byte[1024];
    int num = in.read(buf);
    String str = new String(buf,0,num);
    System.out.println(s.getInetAddress().toString()+”:”+str);
    s.close();
    ss.close();
    ```
  
- 客户端创建 Socket 对象：

  - 客户端程序可以使用 Socket 类创建对象，**创建的同时会自动向服务器方发起连接。**Socket 的构造器是：
  
    - `Socket(String host,int port)throws UnknownHostException,IOException`：向服务器 (域名为 host，端口号为 port) 发起 TCP 连接，若成功，则创建 Socket 对象，否则抛出异常。
    - `Socket(InetAddress address,int port)throws IOException`：根据 InetAddress 对象所表示的 IP 地址以及端口号 port 发起连接。
  
  - 客户端建立 socketAtClient 对象的过程就是向服务器发出套接字连接请求。
  
    ```java
    Socket s = new Socket("192.168.40.165",9999);
    OutputStream out = s.getOutputStream();
    out.write(" hello".getBytes());
    s.close();
    ```
  
- 流程示意图：

  <img src="java-network/image-20210407094416022.png" alt="image-20210407094416022" style="zoom:80%;" />
  
- 实例 1：客户端发送信息给服务端，服务端将数据显示在控制台上。

  ```java
  /**
   * 实现TCP的网络编程
   * 实例1：客户端发送信息给服务端，服务端将数据显示在控制台上
   */
  public class TCPTest {
      /*
      客户端
       */
      @Test
      public void client() {
          Socket socket = null;
          OutputStream os = null;
          try {
              // 1.创建Socket对象，指明服务器端的ip和端口号
              InetAddress inet = InetAddress.getByName("127.0.0.1");// 本机
              socket = new Socket(inet, 8879);
              // 2.获取一个输出流，用于输出数据
              os = socket.getOutputStream();
              // 3.写出数据的操作
              os.write("你好，我是客户端".getBytes());
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              // 4.关闭资源
              if (os != null) {
                  try {
                      os.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              if (socket != null) {
                  try {
                      socket.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }
      }
  
      /*
      服务端
       */
      @Test
      public void server() {
          ServerSocket ss = null;
          Socket socket = null;
          InputStream is = null;
          ByteArrayOutputStream baos = null;
          try {
              // 1.创建服务器端的ServerSocket，指明自己的端口号
              ss = new ServerSocket(8879);
              // 2.调用accept()表示接收来自于客户端的socket
              socket = ss.accept();
              // 3.获取输入流
              is = socket.getInputStream();
              // 4.读取输入流中的数据
              // 不建议这样写，可能会有乱码(字节流读取中文)
              /*byte[] buffer = new byte[1024];
              int len;
              while ((len = is.read(buffer)) != -1) {
                  String str = new String(buffer, 0, len);
                  System.out.print(str);
              }*/
              baos = new ByteArrayOutputStream();
              byte[] buffer = new byte[5];
              int len;
              while ((len = is.read(buffer)) != -1) {
                  // 将输入流中的数据都读到ByteArrayOutputStream中，读完之后再转换
                  baos.write(buffer, 0, len);
              }
              System.out.println("收到了来自于：" + socket.getInetAddress().getHostAddress() + "的数据");// 客户端信息
              System.out.println(baos.toString());// 客户端发送的数据
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              // 5.关闭资源
              if (baos != null) {
                  try {
                      baos.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              if (is != null) {
                  try {
                      is.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              if (socket != null) {
                  try {
                      socket.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              if (ss != null) {
                  try {
                      ss.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }
      }
  }
  ```

- 实例 2：客户端发送文件给服务端，服务端将文件保存在本地。

  ```java
  /**
   *
   * 实现TCP的网络编程
   * 实例2：客户端发送文件给服务端，服务端将文件保存在本地。
   */
  public class TCPTest {
      /*
     客户端
     这里涉及到的异常，应该使用try-catch-finally处理
      */
      @Test
      public void client() throws IOException {
          // 1.创建Socket对象，指明服务器端的ip和端口号
          Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9090);
          // 2.获取一个输出流，用于输出数据
          OutputStream os = socket.getOutputStream();
          // 3.创建输入流，可以使用BufferedInputStream包装
          FileInputStream fis = new FileInputStream(new File("beauty.jpg"));
          // 4.读写操作
          byte[] buffer = new byte[1024];
          int len;
          while ((len = fis.read(buffer)) != -1) {
              os.write(buffer, 0, len);
          }
          // 5.关闭资源
          fis.close();
          os.close();
          socket.close();
      }
  
      /*
      服务端
      这里涉及到的异常，应该使用try-catch-finally处理
       */
      @Test
      public void server() throws IOException {
          // 1.创建服务器端的ServerSocket，指明自己的端口号
          ServerSocket ss = new ServerSocket(9090);
          // 2.调用accept()表示接收来自于客户端的socket
          Socket socket = ss.accept();
          // 3.获取输入流
          InputStream is = socket.getInputStream();
          // 4.创建输出流，可以使用BufferedOutputStream包装
          FileOutputStream fos = new FileOutputStream(new File("beauty1.jpg"));
          // 5.读写操作
          byte[] buffer = new byte[1024];
          int len;
          while ((len = is.read(buffer)) != -1) {
              fos.write(buffer, 0, len);
          }
          // 6.关闭资源
          fos.close();
          is.close();
          socket.close();
          ss.close();
      }
  }
  ```

- 实例 3：从客户端发送文件给服务端，服务端保存到本地，然后返回 "发送成功" 给客户端，并关闭相应的连接。

  ```java
  /**
   * 实现TCP的网络编程
   * 实例3：从客户端发送文件给服务端，服务端保存到本地，然后返回"发送成功"给客户端，并关闭相应的连接。
   */
  public class TCPTest {
      /*
     客户端
     这里涉及到的异常，应该使用try-catch-finally处理
      */
      @Test
      public void client() throws IOException {
          // 1.创建Socket对象，指明服务器端的ip和端口号
          Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9090);
          // 2.获取一个输出流，用于输出数据
          OutputStream os = socket.getOutputStream();
          // 3.创建输入流，可以使用BufferedInputStream包装
          FileInputStream fis = new FileInputStream(new File("beauty.jpg"));
          // 4.读写操作
          byte[] buffer = new byte[1024];
          int len;
          while ((len = fis.read(buffer)) != -1) {
              os.write(buffer, 0, len);
          }
          // 关闭数据的输出，表示客服端数据传输已经完成，提醒服务端不必继续等待
          // 如果不执行此操作，服务器端会一直阻塞
          socket.shutdownOutput();
  
          // 5.接收来自于服务器端的数据，并显示到控制台上
          InputStream is = socket.getInputStream();
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] bufferr = new byte[20];
          int len1;
          while ((len1 = is.read(buffer)) != -1) {
              baos.write(buffer, 0, len1);
          }
          System.out.println(baos.toString());
  
          // 6.关闭资源
          baos.close();
          fis.close();
          os.close();
          socket.close();
      }
  
      /*
      服务端
      这里涉及到的异常，应该使用try-catch-finally处理
       */
      @Test
      public void server() throws IOException {
          // 1.创建服务器端的ServerSocket，指明自己的端口号
          ServerSocket ss = new ServerSocket(9090);
          // 2.调用accept()表示接收来自于客户端的socket
          Socket socket = ss.accept();
          // 3.获取输入流
          InputStream is = socket.getInputStream();
          // 4.创建输出流，可以使用BufferedOutputStream包装
          FileOutputStream fos = new FileOutputStream(new File("beauty1.jpg"));
          // 5.读写操作
          byte[] buffer = new byte[1024];
          int len;
          while ((len = is.read(buffer)) != -1) {// read()是一个阻塞式方法
              fos.write(buffer, 0, len);
          }
  
          System.out.println("图片传输完成");
  
          // 6.服务器端给予客户端反馈
          OutputStream os = socket.getOutputStream();
          os.write("你好，客户端，照片已收到！".getBytes());
  
          // 7.关闭资源
          os.close();
          fos.close();
          is.close();
          socket.close();
          ss.close();
      }
  }
  ```

## UDP 网络编程

- DatagramSocket 类和 DatagramPacket 类实现了基于 UDP 协议的网络编程。

- UDP 数据报通过数据报套接字 DatagramSocket 发送和接收，系统不保证 UDP 数据报一定能够安全送到目的地，也不能确定什么时候可以抵达。

- DatagramPacket 对象封装了 UDP 数据报，在数据报中包含了发送端的 IP 地址和端口号以及接收端的 IP 地址和端口号。

- UDP 协议中每个数据报都给出了完整的地址信息，因此无须建立发送方和接收方的连接。如同发快递包裹一样。

- DatagramSocket 类的常用方法：

  - `public DatagramSocket(int port)`：创建数据报套接字并将其绑定到本地主机上的指定端口。套接字将被绑定到通配符地址，IP 地址由内核来选择。
  - `public DatagramSocket(int port,InetAddress laddr)`：创建数据报套接字，将其绑定到指定的本地地址。本地端口必须在 0 到 65535 之间 (包括两者)。如果 IP 地址为 0.0.0.0，套接字将被绑定到通配符地址，IP 地址由内核选择。
  - `public void close()`：关闭此数据报套接字。
  - `public void send(DatagramPacket p)`：从此套接字发送数据报包。DatagramPacket 包含的信息指示：将要发送的数据、数据长度、远程主机的 IP 地址和远程主机的端口号。
  - `public void receive(DatagramPacket p)`：从此套接字接收数据报包。当此方法返回时，DatagramPacket 的缓冲区填充了接收的数据。数据报包也包含发送方的 IP 地址和发送方机器上的端口号。此方法在接收到数据报前一直阻塞。数据报包对象的 length 字段包含所接收信息的长度。如果信息比包的长度长，该信息将被截短。
  - `public InetAddress getLocalAddress()`：获取套接字绑定的本地地址。
  - `public int getLocalPort()`：返回此套接字绑定的本地主机上的端口号。
  - `public InetAddress getInetAddress()`：返回此套接字连接的地址。如果套接字未连接，则返回 null。
  - `public int getPort()`：返回此套接字的端口。如果套接字未连接，则返回 -1。

- DatagramPacket 类的常用方法：

  - `public DatagramPacket(byte[] buf,int length)`：构造 DatagramPacket，用来接收长度为 length 的数据包。 length 参数必须小于等于  `buf.length()`。
  - `public DatagramPacket(byte[] buf,int length,InetAddress address,int port)`：构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。length 参数必须小于等于 `buf.length()`。
  - `public InetAddress getAddress()`：返回某台机器的 IP 地址，此数据报将要发往该机器或者是从该机器接收到的。
  - `public int getPort()`：返回某台远程主机的端口号，此数据报将要发往该主机或者是从该主机接收到的。
  - `public byte[] getData()`：返回数据缓冲区。接收到的或将要发送的数据从缓冲区中的偏移量 offset 处开始，持续 length 长度。
  - `public int getLength()`：返回将要发送或接收到的数据的长度。

- UDP 网络通信流程：

  - DatagramSocket 与 DatagramPacket。
  - 建立发送端，接收端， 发送端与接收端是两个独立的运行程序。
  - 建立数据包。
  - 调用 Socket 的发送、接收方法。
  - 关闭 Socket。

- 实例：

  ```java
  public class UDPTest {
      /*
      发送端
      注意：发送端发送数据，是不管接收端能不能收到，为了保证接收端能收到数据，应该先启动接收端。
       */
      @Test
      public void sender() {
          DatagramSocket socket = null;
          try {
              socket = new DatagramSocket();
  
              String str = "我是UDP方式发送的数据";
              byte[] data = str.getBytes();
              InetAddress inet = InetAddress.getLocalHost();
              // 封装数据报，发送到本机的9090端口
              DatagramPacket packet = new DatagramPacket(data, 0, data.length, inet, 9090);
  
              socket.send(packet);
          } catch (IOException exception) {
              exception.printStackTrace();
          } finally {
              if (socket != null) {
                  socket.close();
              }
          }
      }
  
      /*
      接收端
      注意：在接收端，要指定监听的端口。
       */
      @Test
      public void receiver() {
          DatagramSocket socket = null;
          try {
              socket = new DatagramSocket(9090);
  
              byte[] buffer = new byte[100];
              DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
  
              socket.receive(packet);
              System.out.println(new String(packet.getData(), 0, packet.getLength()));
          } catch (IOException exception) {
              exception.printStackTrace();
          } finally {
              if (socket != null) {
                  socket.close();
              }
          }
      }
  }
  ```

## URL 网络编程

- **URL (Uniform Resource Locator)：统一资源定位符，它表示 Internet 上某一资源的地址。**

- 它是一种具体的 URI，即 URL 可以用来标识一个资源，而且还指明了如何 locate 这个资源。

- 通过 URL 我们可以访问 Internet 上的各种网络资源，比如最常见的 www，ftp 站点。浏览器通过解析给定的 URL 可以在网络上查找相应的文件或其他资源。

- URL 的基本结构由 5 部分组成：**<传输协议>://< 主机名>:< 端口号>/< 文件名>#片段名?参数列表**。

  - 例如：http://192.168.1.100:8080/helloworld/index.jsp#a?username=shkstart&password=123
  - #片段名：即锚点，例如看小说，直接定位到章节
  - 参数列表格式：参数名=参数值&参数名=参数值....

- 为了表示 URL，`java.net` 中实现了类 URL。我们可以通过下面的构造器来初始化一个 URL 对象：

  - **`public URL (String spec)`**：通过一个表示 URL 地址的字符串可以构造一个 URL 对象。例如：`URL url = new URL("http://www. atguigu.com/");`。
  - **`public URL(URL context, String spec)`**：通过基 URL 和相对 URL 构造一个 URL 对象。例如：`URL downloadUrl = new URL(url, "download.html");`。
  - `public URL(String protocol, String host, String file)`：例如：`new URL("http","www.atguigu.com", “download. html");`。
  - `public URL(String protocol, String host, int port, String file)`：例如：`URL gamelan = new URL("http", "www.atguigu.com", 80, “download.html");`。

- URL 类的构造器都声明抛出非运行时异常，必须要对这一异常进行处理，通常使用 try - catch 语句进行捕获。

- 一个 URL 对象生成后，其属性是不能被改变的，但可以通过它给定的方法来获取这些属性：

  - `public String getProtocol()`：获取该 URL 的协议名

  - `public String getHost()`：获取该 URL 的主机名。

  - `public String getPort()`：获取该 URL 的端口号。

  - `public String getPath()`：获取该 URL 的文件路径。

  - `public String getFile()`：获取该 URL 的文件名。

  - `public String getQuery()`：获取该 URL 的查询名。

  - 实例：

    ```java
    /**
     * URL网络编程
     * 1.URL：统一资源定位符，对应着互联网的某一资源地址
     * 2.格式：
     *  http://localhost:8080/examples/beauty.jpg?username=Tom
     *  协议    主机名     端口号  资源地址           参数列表
     */
    public class URLTest {
        public static void main(String[] args) {
            try {
                URL url = new URL("http://localhost:8080/examples/beauty.jpg?username=Tom");
    
                // public String getProtocol(): 获取该URL的协议名
                System.out.println(url.getProtocol());// http
                // public String getHost(): 获取该URL的主机名
                System.out.println(url.getHost());// localhost
                // public String getPort(): 获取该URL的端口号
                System.out.println(url.getPort());// 8080
                // public String getPath(): 获取该URL的文件路径
                System.out.println(url.getPath());// /examples/beauty.jpg
                // public String getFile(): 获取该URL的文件名
                System.out.println(url.getFile());// /examples/beauty.jpg?username=Tom
                // public String getQuery(): 获取该URL的查询名
                System.out.println(url.getQuery());// username=Tom
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    ```

- URL 的方法 `openStream()`：能从网络上读取数据。

- 若希望输出数据，例如向服务器端的 CGI (公共网关接口 Common Gateway Interface 的简称，是用户浏览器和服务器端的应用程序进行连接的接口) 程序发送一些数据，则必须先与 URL 建立连接，然后才能对其进行读写，此时需要使用 URLConnection 类。

- URLConnection：表示到 URL 所引用的远程对象的连接。当与一个 URL 建立连接时，首先要在一个 URL 对象上通过方法 `openConnection()` 生成对应的 URLConnection 对象。如果连接过程失败，将产生 IOException。比如：

  ```java
  URL netchinaren = new URL ("http://www.atguigu.com/index.shtml");
  URLConnectonn u = netchinaren.openConnection();
  ```

- 通过 URLConnection 对象获取的输入流和输出流，即可以与现有的 CGI 程序进行交互。

  - `public Object getContent() throws IOException`

  - `public int getContentLength()`

  - `public String getContentType()`

  - `public long getDate()`

  - `public long getLastModified()`

  - `public InputStream getInputStream()throws IOException`

  - `public OutputSteram getOutputStream()throws IOException`

  - 实例：

    ```java
    public class URLTest {
        public static void main(String[] args) {
            HttpURLConnection urlConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                URL url = new URL("http://localhost:8080/examples/beauty.jpg");
    
                urlConnection = (HttpURLConnection) url.openConnection();
    
                urlConnection.connect();
    
                is = urlConnection.getInputStream();
                fos = new FileOutputStream("day10\\beauty3.jpg");
    
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
    
                System.out.println("下载完成");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
    ```

- URI 、URL 和URN的区别：

  <img src="java-network/image-20210407115543616.png" alt="image-20210407115543616" style="zoom:80%;" />

  - URI，是 uniform resource identifier，统一资源标识符，用来唯一的标识一个资源。而 URL 是 uniform resource locator，统一资源定位符，它是一种具体的 URI，即 URL 可以用来标识一个资源，而且还指明了如何 locate 这个资源。而 URN，是 uniform resource name，统一资源命名，是通过名字来标识资源，比如 mailto:java-net@java.sun.com。也就是说，URI 是以一种抽象的，高层次概念定义统一资源标识，而 URL 和 URN 则是具体的资源标识的方式。URL 和 URN 本身也都是一种 URI。
  - 在 Java 的 URI 中，一个 URI 实例可以代表绝对的，也可以是相对的，只要它符合 URI 的语法规则。而 URL 类则不仅符合语义，还包含了定位该资源的信息，因此它不能是相对的。

## 总结

- 位于网络中的计算机具有唯一的 IP 地址，这样不同的主机可以互相区分。
- 客户端－服务器是一种最常见的网络应用程序模型。服务器是一个为其客户端提供某种特定服务的硬件或软件。客户机是一个用户应用程序，用于访问某台服务器提供的服务。端口号是对一个服务的访问场所，它用于区分同一物理计算机上的多个服务。套接字用于连接客户端和服务器，客户端和服务器之间的每个通信会话使用一个不同的套接字。TCP 协议用于实现面向连接的会话。
- Java 中有关网络方面的功能都定义在 `java.net` 程序包中。Java 用 InetAddress 对象表示  IP 地址，该对象里有两个字段：主机名 (String) 和 IP 地址 (int)。
- 类 Socket 和 ServerSocket 实现了基于 TCP 协议的客户端－服务器程序。Socket 是客户端和服务器之间的一个连接，连接创建的细节被隐藏了。这个连接提供了一个安全的数据传输通道，这是因为 TCP 协议可以解决数据在传送过程中的丢失、损坏、重复、乱序以及网络拥挤等问题，它保证数据可靠的传送。
- 类 URL 和 URLConnection 提供了最高级网络应用。URL 的网络资源的位置来统一标识 Internet 上各种网络资源。通过 URL 对象可以创建当前应用程序和 URL 表示的网络资源之间的连接，这样当前程序就可以读取网络资源数据，或者把自己的数据传送到网络上去。

## 本文参考

https://www.gulixueyuan.com/goods/show/203?targetId=309&preview=0

声明：写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。