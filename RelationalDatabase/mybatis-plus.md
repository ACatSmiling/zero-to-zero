*date: 2022-07-04*

## 简介

官网：https://baomidou.com/

Github：https://github.com/baomidou/mybatis-plus

`MyBatis-Plus`（简称 MP）是`一个 MyBatis 的增强工具`，在 MyBatis 的基础上`只做增强不做改变`，为简化开发、提高效率而生。

<img src="mybatis-plus/image-20220727094854512.png" alt="image-20220727094854512" style="zoom:50%;" />

MyBatis-Plus 提供了通用的 mapper 和 service，可以在不编写任何 SQL 语句的情况下，快速实现对单表的 CRUD、批量、逻辑删除、分页等操作。

### 特性

`无侵入`：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑。

`损耗小`：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作。

`强大的 CRUD 操作`：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求。

`支持 Lambda 形式调用`：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错。

`支持主键自动生成`：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 Sequence），可自由配置，完美解决主键问题。

`支持 ActiveRecord 模式`：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作。

`支持自定义全局通用操作`：支持全局通用方法注入（ Write once, use anywhere ）。

`内置代码生成器`：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用。

`内置分页插件`：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询。

`分页插件支持多种数据库`：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库。

`内置性能分析插件`：可输出 SQL 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询。

`内置全局拦截插件`：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作。

### 支持数据库

任何能使用 MyBatis 进行 CRUD，并且支持标准 SQL 的数据库，都可以使用 MyBatisPlus，具体支持情况如下：

- MySQL，Oracle，DB2，H2，HSQL，SQLite，PostgreSQL，SQLServer，Phoenix，Gauss ，ClickHouse，Sybase，OceanBase，Firebird，Cubrid，Goldilocks，csiidb。
- 达梦数据库，虚谷数据库，人大金仓数据库，南大通用（华库）数据库，南大通用数据库，神通数据库，瀚高数据库。

### 框架结构

<img src="mybatis-plus/image-20220727100032206.png" alt="image-20220727100032206" style="zoom:50%;" />

## quick start

### 环境说明

JDK：

```bash
C:\Users\XiSun>java -version
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_222-b10)      
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.222-b10, mixed mode)
```

Maven：

```bash
C:\Users\XiSun>mvn --version
Apache Maven 3.8.5 (3599d3414f046de2324203b78ddcf9b5e4388aa0)
Maven home: D:\Programs\Maven\apache-maven-3.8.5
Java version: 1.8.0_222, vendor: AdoptOpenJDK, runtime: D:\Programs\AdoptOpenJDK\jdk-8.0.222.10-hotspot\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

MySQL：

```bash
root@xisun-develop:/home/xisun# docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED       STATUS      PORTS                                                  NAMES
4905d5364838   mysql:8.0.29   "docker-entrypoint.s…"   3 weeks ago   Up 2 days   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   mysql_8.0.29
root@xisun-develop:/home/xisun# docker images
REPOSITORY              TAG          IMAGE ID       CREATED         SIZE
mysql                   8.0.29       0ef9083d9892   4 weeks ago     524MB
```

Spring Boot：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.2</version>
</parent>
```

> Spring Boot 2.7.2 版本，默认使用的 MySQL 连接驱动为 8.0.29。

MyBatis Plus：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.2</version>
</dependency>
```

### 生成数据

```sql
CREATE TABLE IF NOT EXISTS mybatisplus.user (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id，主键',
	`name` VARCHAR(10) NOT NULL COMMENT '姓名',
	`sex` VARCHAR(10) NOT NULL COMMENT '性别',
	`age` INT(10) NOT NULL COMMENT '年龄',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

INSERT INTO mybatisplus.`user` (name, sex, age) VALUES ("张三", "男", 27);

INSERT INTO mybatisplus.`user` (name, sex, age) VALUES ("李四", "男", 28);

INSERT INTO mybatisplus.`user` (name, sex, age) VALUES ("王二", "男", 26);

INSERT INTO mybatisplus.`user` (name, sex, age) VALUES ("刘七", "女", 25);

INSERT INTO mybatisplus.`user` (name, sex, age) VALUES ("郑八", "男", 29);
```

### 创建 Spring Boot 项目

![image-20220727103730428](mybatis-plus/image-20220727103730428.png)

### 引入依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.xisun.mybatisplus.springboot</groupId>
    <artifactId>xisun-mybatisplus-springboot</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.2</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <jupiter.version>5.8.2</jupiter.version>
        <lombok.version>1.18.24</lombok.version>
        <logback.version>1.2.11</logback.version>
        <hutool.version>5.8.3</hutool.version>
        <fastjson.version>2.0.7</fastjson.version>
    </properties>

    <repositories>
        <repository>
            <id>aliyun</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>aliyun-plugin</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${jupiter.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fastjson.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- MySQL 驱动依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- MyBatis Plus Starter依赖 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.2</version>
        </dependency>
    </dependencies>
</project>
```

说明：spring-boot-starter-parent 中，有定义 mysql-connector-java 的版本。

<img src="mybatis-plus/image-20220727154234040.png" alt="image-20220727154234040" style="zoom:50%;" />

> 如果默认的 MySQL 连接驱动，不匹配实际使用的 MySQL 数据库，在引入驱动依赖时，需要指定相应的驱动版本。同时，注意 driver-class-name 不同：
>
> - ≤ MySQL 5：`com.mysql.jdbc.Driver`
> - ≥ MySQL 6：`com.mysql.cj.jdbc.Driver`

### 配置 application.yml



## 本文参考

https://www.bilibili.com/video/BV12R4y157Be?spm_id_from=333.337.search-card.all.click

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
