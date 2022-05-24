*date: 2021-04-13*

## 基本设置

### 字体设置

![image-20220401094240564](idea/image-20220401094240565.png)

### 编码设置

![image-20220407105335024](idea/image-20220407105335024.png)

### 注释信息

![image-20220402095038092](idea/image-20220402095038092.png)

### 导出配置

![image-20220419133704108](idea/image-20220419133704108.png)

![image-20220520171328283](idea/image-20220520171328283.png)

![image-20220520171403203](idea/image-20220520171403203.png)

> 导出配置后，原先设置的配置会失效，建议不要使用此操作。

### Java Compiler

![image-20220401102004494](idea/image-20220401102004494.png)

### Maven

![image-20220401100252388](idea/image-20220401100252388.png)

![image-20220401103319190](idea/image-20220401103319190.png)

### Structure

![image-20220426090755703](idea/image-20220426090755703.png)

### 全局设置

说明，通过以下标记，可以判断配置的是全局，还是只针对当前项目：

![image-20220426091129298](idea/image-20220426091129298.png)

Java Compiler：

![image-20220426083854620](idea/image-20220426083854620.png)

![image-20220426090006956](idea/image-20220426090006956.png)

Maven：

![image-20220426083854620](idea/image-20220426083854620.png)

![image-20220426090224210](idea/image-20220426090224210.png)

![image-20220426090420870](idea/image-20220426090420870.png)

Structure：

![image-20220426084055394](idea/image-20220426084055394.png)

![image-20220426084014679](idea/image-20220426084014679.png)

![image-20220426084156637](idea/image-20220426084156637.png)

## 插件管理

参考：https://wcqblog.com/article/detail/262534926061142016

### Lombok

![image-20220401101309290](idea/image-20220401101309290.png)

### Grep Console

![image-20220419134005987](idea/image-20220419134005987.png)

- 可以配置日志输出，调试。

### Maven Helper

![image-20220419134118139](idea/image-20220419134118139.png)

- 帮助优化 Maven 依赖，以及排除不必要的依赖。

### Alibaba Java Coding Guidelines

![image-20220419134320462](idea/image-20220419134320462.png)

- 阿里 Java 开发规范。

### Rainbow Brackets

![image-20220419134635205](idea/image-20220419134635205.png)

- 此插件会将括号变成彩色，同一组括号颜色一样，相邻两个括号颜色不一样。

### Tabnine

![image-20220419143804631](idea/image-20220419143804631.png)

- 此插件提供代码补全功能。

## 快捷方式

`Ctrl + H`：查看类的继承层级关系

`Ctrl + Alt + U`：查看类图

`Ctrl + Alt + B`：查找接口的实现类

`Ctrl + Alt + ⬅`：回到上一步

`Ctrl + Alt + →`：回到下一步

`Ctrl + Alt + S`：打开 settings

`Ctrl + Alt + T`：对一段代码添加包围语句，如 try/catch

`Ctrl + Y`：删除当前行

`Ctrl + D`：复制当前行

`Shift + F6`：重命名

`Ctrl + F`：查找

`Ctrl + R`：替换

## 引入本地 Jar 包

通过添加 Libraries 的方式引入：

1. 首先在根目录下创建一个 libs 的目录：

   ![image-20220520150547738](idea/image-20220520150547738.png)

2. 打开 File -> Project Structure。

3. 单击 Libraries ---> "+" ---> "Java" ---> 选择我们导入的项目主目录，点击OK：

   ![image-20220520150720796](idea/image-20220520150720796.png)

   ![image-20220520151055312](idea/image-20220520151055312.png)

4. 注意：在弹出的方框中点击 Cancel，取消将其添加到 Module 中。

   ![image-20220520151239532](idea/image-20220520151239532.png)

5. libs 目录创建成功，删除目录中添加进来的多余内容，重新添加需要的 jar 包：

   ![image-20220520152349834](idea/image-20220520152349834.png)

6. 重新添加需要的 jar 包：

   ![image-20220520152656539](idea/image-20220520152656539.png)

7. 引入 jar 包：Modules -> 项目 -> "Dependencies"，点击 "+" ---> "Library"，将刚才创建成功的 Library 目录加入：

   ![image-20220520153443474](idea/image-20220520153443474.png)

   ![image-20220520153559163](idea/image-20220520153559163.png)

8. jar 包导入成功！

9. 如果要将引入的 jar 包打包到 war 中，参考在 pom 文件中添加以下配置：

   ```xml
   <!-- 引用本地jar包 -->
   <dependency>
       <groupId>com.aspose</groupId>
       <artifactId>aspose-words</artifactId>
       <version>16.8.0</version>
       <scope>system</scope>
       <systemPath>${pom.basedir}/libs/aspose-words-16.8.0-jdk16.jar</systemPath>
   </dependency>
   <dependency>
       <groupId>com.aspose</groupId>
       <artifactId>aspose-cells</artifactId>
       <version>8.5.2</version>
       <scope>system</scope>
       <systemPath>${pom.basedir}/libs/aspose-cells-8.5.2.jar</systemPath>
   </dependency>
   <dependency>
       <groupId>com.aspose</groupId>
       <artifactId>aspose-slides</artifactId>
       <version>15.9.0</version>
       <scope>system</scope>
       <systemPath>${pom.basedir}/libs/aspose.slides-15.9.0.jar</systemPath>
   </dependency>
   ```

   ```xml
   <!-- 将本地jar包打到war中 -->
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-dependency-plugin</artifactId>
       <executions>
           <execution>
               <id>copy-dependencies</id>
               <phase>compile</phase>
               <goals>
                   <goal>copy-dependencies</goal>
               </goals>
               <configuration>
                   <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/lib</outputDirectory>
                   <includeScope>system</includeScope>
               </configuration>
           </execution>
       </executions>
   </plugin>
   ```

## 本文参考

https://www.cnblogs.com/hunttown/p/13488486.html

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
