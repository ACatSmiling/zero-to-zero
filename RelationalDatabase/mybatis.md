*date: 2022-06-22*

## 简介

`MyBatis`最初是 Apache 的一个开源项目`iBatis`，2010 年 6 月这个项目由 Apache Software Foundation 迁移到了 Google Code。随着开发团队转投 Google Code 旗下，iBatis3.x 正式更名为 MyBatis，代码于 2013 年 11 月迁移到 Github。

iBatis 一词来源于 internet 和 abatis 的组合，它是一个`基于 Java 的持久层框架`。 iBatis 提供的持久层框架包括 SQL Maps 和 Data Access Objects（DAO）。

MyBatis 的特性：

- MyBatis 是支持`定制化 SQL`、`存储过程`以及`高级映射`的优秀的持久层框架；
- MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集；
- MyBatis 可以使用简单的`XML`或`注解`用于配置和原始映射，将接口和 Java 的 POJO（Plain Old Java Objects，普通的 Java 对象）映射成数据库中的记录；
- MyBatis 是一个`半自动的 ORM (Object Relation Mapping) 框架`。

GitHub 地址：https://github.com/mybatis/mybatis-3

![image-20220710144154585](mybatis/image-20220710144154585.png)

点击 Download Latest，进入下载界面：

![image-20220710154133790](mybatis/image-20220710154133790.png)

点击下载后，解压，可以得到 MyBatis 的官方文档（jar 包通过 Maven 管理，不需要使用）：

![image-20220710154313877](mybatis/image-20220710154313877.png)

不同持久化层技术的对比：

- JDBC：
  - SQL 夹杂在 Java 代码中，耦合度高，导致硬编码内伤；
  - 维护不易且实际开发需求中 SQL 有变化，频繁修改的情况多见代码冗长，开发效率低。
- Hibernate 和 JPA
  - 操作简便，开发效率高，程序中的长难复杂 SQL 需要绕过框架
    内部自动生产的 SQL，不容易做特殊优化
    基于全映射的全自动框架，大量字段的 POJO 进行部分映射时比较困难。
    反射操作太多，导致数据库性能下降
- MyBatis
  - 轻量级，性能出色
    SQL 和 Java 编码分开，功能边界清晰。Java代码专注业务、SQL语句专注数据
    开发效率稍逊于HIbernate，但是完全能够接受



