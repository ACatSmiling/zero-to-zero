---
title: hadoop-hdfs
date: 2021-01-03 21:59:31
tags: hadoop
---

## HDFS 常用 shell 命令

操作 HDFS 的 shell命令有三种：

1. hadoop fs：适用于任何不同的文件系统，比如本地文件系统和 HDFS 文件系统。
2. hadoop dfs：只适用于 HDFS 文件系统。
3. hdfs dfs：只适用于 HDFS 文件系统。

>官方不推荐使用第二种命令 hadoop dfs，有些 Hadoop 版本中已将这种命令弃用。

### 语法

```sh
hadoop fs [genericOptions] [commandOptions]
```

### 参数说明

| HDFS 常用命令             | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| **hadoop fs -ls**         | **显示指定文件的详细信息**                                   |
| hadoop fs -cat            | 将指定文件的内容输出到标准输出                               |
| hadoop fs touchz          | 创建一个指定的空文件                                         |
| **hadoop fs -mkdir [-p]** | **创建指定的一个或多个文件夹，-p 选项用于递归创建**          |
| **hadoop fs -cp**         | **将文件从源路径复制到目标路径**                             |
| hadoop fs -mv             | 将文件从源路径移动到目标路径                                 |
| **hadoop fs -rm**         | **删除指定的文件，只删除非空目录和文件**                     |
| hadoop fs -rm -r          | 删除指定的文件夹及其下的所有文件，-r 表示递归删除子目录      |
| hadoop fs -chown          | 改变指定文件的所有者，该命令仅适用于超级用户                 |
| hadoop fs -chmod          | 将指定的文件权限更改为可执行文件，该命令仅适用于超级用户和文件所有者 |
| **hadoop fs -get**        | **复制指定的文件到本地文件系统指定的文件或文件夹**           |
| **hadoop fs -put**        | **从本地文件系统中复制指定的单个或多个源文件到指定的目标文件系统** |
| hadoop fs -moveFromLocal  | 与 -put 命令功能相同，但是文件上传结束后会删除源文件         |
| hadoop fs -copyFromLocal  | 与 -put 命令功能相同，将本地源文件复制到路径指定的文件或文件夹中 |
| hadoop fs -copyToLocal    | 与 -get命令功能相同，将目标文件复制到本地文件或文件夹中      |

hadoop网站：

https://xiaoxiaogua.github.io/2019/03/24/YARN-Scheduler/

https://blog.csdn.net/qq_26442553/article/details/117284107

https://blog.csdn.net/shudaqi2010/article/details/114528809

https://www.cnblogs.com/yinzhengjie/p/13383344.html

https://bbs.huaweicloud.com/blogs/218022

https://cloud.tencent.com/developer/article/1195056

kafka网站：

https://stackoverflow.com/questions/34188574/is-the-group-option-deprecated-from-kafka-console-consumer-tool-if-so-how-ca

https://blog.csdn.net/qq_29116427/article/details/80206125

flink网站：

https://www.jianshu.com/p/27fa3d590a62

https://zhuanlan.zhihu.com/p/50845911

https://blog.csdn.net/chentangdan2377/article/details/101000408

https://blog.csdn.net/L13763338360/article/details/110873662

https://blog.51cto.com/u_15080019/2653853

https://blog.csdn.net/weixin_33648811/article/details/112103174

https://cloud.tencent.com/developer/article/1500184

https://www.jianshu.com/p/aa00be723f23

https://www.cnblogs.com/gentlescholar/p/15044085.html

https://dragonlsl.blog.csdn.net/article/details/105823127

https://www.sohu.com/a/363674737_120342237

redis网站：

https://www.cnblogs.com/wei-zw/p/9163687.html

https://www.liaoxuefeng.com/wiki/1252599548343744/1282386499207201?luicode=10000011&lfid=1076031658384301&featurecode=newtitl&u=https%3A%2F%2Fwww.liaoxuefeng.com%2Fwiki%2F1252599548343744%2F1282386499207201

https://cloud.tencent.com/developer/article/1683498

线程池网站：

https://blog.csdn.net/u010235716/article/details/90059966

https://www.cnblogs.com/meijsuger/p/11492388.html

https://www.cnblogs.com/warehouse/p/10732965.html

SpringBoot：

https://ashiamd.github.io/docsify-notes/#/study/SpringBoot/README
