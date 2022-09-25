*date: 2022-05-29*

## 概述

### 为什么要使用数据库
`持久化 (persistence)`：把数据保存到可掉电式存储设备中以供之后使用。大多数情况下，特别是企业级应用，数据持久化意味着将内存中的数据保存到硬盘上加以 "固化"，而持久化的实现过程大多通过各种关系数据库来完成。

持久化的主要作用是将内存中的数据存储在关系型数据库中，当然也可以存储在磁盘文件、XML 数据文件中等。

![image-20220531222017937](mysql/image-20220531222017937.png)

### 数据库与数据库管理系统

#### 数据库的相关概念

`DB`：Database，数据库，即存储数据的 "仓库"，其本质是一个文件系统。它保存了一系列有组织的数据。

`DBMS`：Database Management System，数据库管理系统，是一种操纵和管理数据库的大型软件，用于建立、使用和维护数据库，对数据库进行统一管理和控制。用户通过数据库管理系统访问数据库中表内的数据。

`SQL`：Structured Query Language，结构化查询语言，专门用来与数据库通信的语言。

#### 数据库与数据库管理系统的关系

`数据库管理系统 (DBMS) 可以管理多个数据库`，一般开发人员会针对每一个应用创建一个数据库。为保存应用中实体的数据，一般会在数据库创建多个表，以保存程序中实体用户的数据。

数据库管理系统、数据库和表的关系如图所示：

![image-20220531223454362](mysql/image-20220531223454362.png)

![image-20220531223704029](mysql/image-20220531223704029.png)

#### 常见的数据库管理系统排名

目前互联网上常见的数据库管理软件有 Oracle、MySQL、SQL Server、DB2、PostgreSQL、Access、Sybase、Informix 这几种。以下是 2022 年 5 月 DB-Engines Ranking 对各数据库受欢迎程度进行调查后的统计结果（查看数据库最新排名：https://db-engines.com/en/ranking）：

![image-20220531224320886](mysql/image-20220531224320886.png)

对应的走势图（https://db-engines.com/en/ranking_trend）：

![image-20220531225320951](mysql/image-20220531225320951.png)

#### 常见的数据库介绍

`Oracle`

1979 年，Oracle 2 诞生，它是第一个商用的 RDBMS（关系型数据库管理系统）。随着 Oracle 软件的名气越来越大，公司也改名叫 Oracle 公司。

2007 年，总计 85 亿美金收购 BEA Systems。

2009 年，总计 74 亿美金收购 SUN。此前的 2008 年，SUN 以 10 亿美金收购 MySQL。意味着 Oracle 同时拥有了 MySQL 的管理权，至此 Oracle 在数据库领域中成为绝对的领导者。

2013 年，Oracle超越 IBM，成为继 Microsoft 后全球第二大软件公司。如今 Oracle 的年收入达到了 400 亿美金，足以证明商用（收费）数据库软件的价值。

`SQL Server`

SQL Server 是微软开发的大型商业数据库，诞生于 1989 年。C#、.net 等语言常使用，与 WinNT 完全集成，也可以很好地与 Microsoft BackOffice 产品集成。

`DB2`

IBM 公司的数据库产品，收费的。常应用在银行系统中。

`PostgreSQL`

PostgreSQL 的稳定性极强，最符合 SQL 标准，开放源码，具备商业级 DBMS 质量。PG 对数据量大的文本以及 SQL 处理较快。

`SyBase`

已经淡出历史舞台。提供了一个非常专业数据建模的工具 PowerDesigner。

`SQLite`

嵌入式的小型数据库，应用在手机端。 零配置，SQlite3 不用安装，不用配置，不用启动，关闭或者配置数据库实例。当系统崩溃后不用做任何恢复操作，在下次使用数据库的时候自动恢复。

`informix`

IBM 公司出品，取自 Information 和 Unix 的结合，它是第一个被移植到 Linux 上的商业数据库产品。仅运行于 unix/linux 平台，命令行操作。 性能较高，支持集群，适应于安全性要求极高的系统，尤其是银行，证券系统的应用。

### RDBMS 与非 RDBMS

从排名中我们能看出来，关系型数据库绝对是 DBMS 的主流，其中使用最多的 DBMS 分别是 Oracle、MySQL 和 SQL Server。这些都是关系型数据库（RDBMS）。

#### 关系型数据库（RDBMS）

这种类型的数据库是`最古老`的数据库类型，关系型数据库模型是把复杂的数据结构归结为简单的`二元关系 (即二维表格形式)`。

![image-20220531234452255](mysql/image-20220531234452255.png)

关系型数据库以`行(row)`和`列(column)`的形式存储数据，以便于用户理解。这一系列的行和列被称为`表(table)`，一组表组成了一个`库(database)`。

表与表之间的数据记录有`关系 (relationship)`。现实世界中的各种实体以及实体之间的各种联系均用`关系模型`来表示。`关系型数据库，就是建立在关系模型基础上的数据库。`

SQL 就是关系型数据库的查询语言。

![image-20220531235057041](mysql/image-20220531235057041.png)

关系型数据库的优势：

- `复杂查询`：可以用 SQL 语句方便的在一个表以及多个表之间做非常复杂的数据查询。
- `事务支持`：使得对于安全性能很高的数据访问要求得以实现。

#### 非关系型数据库（非 RDBMS）

非关系型数据库，可看成传统关系型数据库的功能阉割版本，基于键值对存储数据，不需要经过 SQL 层的解析，性能非常高 。同时，通过减少不常用的功能，进一步提高性能。

目前基本上大部分主流的非关系型数据库都是免费的。

##### 非关系型数据库分类

相比于 SQL，`NoSQL`泛指非关系型数据库，包括了榜单上的`键值型数据库`、`文档型数据库`、`搜索引擎数据库`和`列存储数据库`等，除此以外还包括`图形数据库`。也只有用 NoSQL 一词才能将这些技术囊括进来。

###### 键值型数据库

键值型数据库通过`Key-Value`键值的方式来存储数据，其中 Key 和 Value 可以是简单的对象，也可以是复杂的对象。`Key 作为唯一的标识符`，`优点是查找速度快`，在这方面明显优于关系型数据库，`缺点是无法像关系型数据库一样使用条件过滤 (比如 WHERE)`，如果你不知道去哪里找数据，就要遍历所有的键，这就会消耗大量的计算。

键值型数据库典型的使用场景是作为`内存缓存`。`Redis`是最流行的键值型数据库。

![image-20220601104010861](mysql/image-20220601104010861.png)

###### 文档型数据库

此类数据库可存放并获取文档，可以是 XML、JSON 等格式。在数据库中文档作为处理信息的基本单位，一个文档就相当于一条记录。文档数据库所存放的文档，就相当于键值数据库所存放的 "值"。`MongoDB`是最流行的文档型数据库，此外，还有CouchDB等。

###### 搜索引擎数据库

虽然关系型数据库采用了索引提升检索效率，但是针对全文索引效率却较低。搜索引擎数据库是应用在搜索引擎领域的数据存储形式，由于搜索引擎会爬取大量的数据，并以特定的格式进行存储，这样在检索的时候才能保证性能最优。核心原理是`倒排索引`。

典型产品：`Solr`、`Elasticsearch`、`Splunk`等。

###### 列存储数据库

列存储数据库是相对于行存储的数据库，Oracle、MySQL、SQL Server 等数据库都是采用的行式存储（Row-based），而列存储数据库是将数据按照列存储到数据库中，这样做的好处是可以大量降低系统的 I/O，适合于分布式文件系统，不足在于功能相对有限。典型产品：`HBase`等。

![image-20220601104444503](mysql/image-20220601104444503.png)

###### 图形数据库

图形数据库，利用了图这种数据结构存储了实体（对象）之间的关系。图形数据库最典型的例子就是社交网络中人与人的关系，数据模型主要是以节点和边（关系）来实现，特点在于`能高效地解决复杂的关系问题`。

图形数据库顾名思义，就是一种存储图形关系的数据库。它利用了图这种数据结构存储了实体（对象）之间的关系。关系型数据用于存储明确关系的数据，但对于复杂关系的数据存储却有些力不从心。如社交网络中人物之间的关系，如果用关系型数据库则非常复杂，用图形数据库将非常简单。典型产品：`Neo4J`、`InfoGrid`等。

![image-20220601125434608](mysql/image-20220601125434608.png)

##### NoSQL 的演变

由于 SQL 一直称霸 DBMS，因此许多人在思考是否有一种数据库技术能远离 SQL，于是 NoSQL 诞生了，但是随着发展却发现越来越离不开 SQL。到目前为止 NoSQL 阵营中的 DBMS 都会有实现类似 SQL 的功能。下面是 NoSQL 这个名词在不同时期的诠释，从这些释义的变化中可以看出 NoSQL 功能的演变 ：

1970：NoSQL = We have no SQL

1980：NoSQL = Know SQL

2000：NoSQL = No SQL!

2005：NoSQL = Not only SQL

2013：NoSQL = No, SQL!

NoSQL 对 SQL 做出了很好的补充，比如实际开发中，有很多业务需求，其实并不需要完整的关系型数据库功能，非关系型数据库的功能就足够使用了。这种情况下，使用性能更高、成本更低的非关系型数据库当然是更明智的选择。比如：日志收集、排行榜、定时器等。

总而言之，虽然 NoSQL 的分类很多，但即便如此，在 DBMS 排名中，还是 SQL 阵营的比重更大，影响力前 5 的 DBMS 中有 4 个是关系型数据库，而排名前 20 的 DBMS 中也有 12 个是关系型数据库。所以说，掌握 SQL 是非常有必要的。

### 关系型数据库设计规则

- 关系型数据库的典型数据结构就是`数据表` ，这些数据表的组成都是结构化的（Structured）。

- 将数据放到表中，表再放到库中。

- 一个数据库中可以有多个表，每个表都有一个名字，用来标识自己。表名具有`唯一性`。

- 表具有一些特性，这些特性定义了数据在表中如何存储，类似 Java 和 Python 中类的设计。

####  表、记录、字段

`E-R (entity-relationship，实体-联系)`模型中有三个主要概念是：`实体集`、`属性`、`联系集`。

一个实体集（class）对应于数据库中的一个表（table），一个实体（instance）则对应于数据库表中的一行（row），也称为一条记录（record）。一个属性（attribute）对应于数据库表中的一列（column），也称为一个字段（field）。

![image-20220601131827935](mysql/image-20220601131827935.png)

`ORM 思想 (Object Relational Mapping)`体现：

- 数据库中的一个表 <---> Java 或 Python 中的一个类

- 表中的一条数据 <---> 类中的一个对象（或实体）

- 表中的一个列 <----> 类中的一个字段、属性（field）

#### 表的关联关系

- 表与表之间的数据记录有关系（relationship）。现实世界中的各种实体以及实体之间的各种联系均用关系模型来表示。

- 四种：一对一关联、一对多关联、多对多关联、自我引用。

#####  一对一关联（one-to-one）

在实际的开发中应用不多，因为一对一可以创建成一张表。

一对一关联两种建表原则：

- `外键唯一`：主表的主键和从表的外键（唯一），形成主外键关系，外键唯一。
- `外键是主键`：主表的主键和从表的主键，形成主外键关系。

举例，设计学生表 ：学号、姓名、手机号码、班级、系别、身份证号码、家庭住址、籍贯、紧急联系人、...

- 拆为两个表：两个表的记录是一一对应关系。

- 基础信息表（常用信息）：学号、姓名、手机号码、班级、系别

- 档案信息表（不常用信息）：学号、身份证号码、家庭住址、籍贯、紧急联系人、...

  ![image-20220601132459103](mysql/image-20220601132459103.png)

##### 一对多关联（one-to-many）

常见实例场景：客户表和订单表，分类表和商品表，部门表和员工表。

一对多关联建表原则：`在从表 (多方) 创建一个字段，字段作为外键指向主表 (一方) 的主键。`

![image-20220601133318896](mysql/image-20220601133318896.png)

举例：

- 员工表：编号、姓名、...、所属部门

- 部门表：编号、名称、简介

  ![image-20220601133524961](mysql/image-20220601133524961.png)

  ![image-20220601133540109](mysql/image-20220601133540109.png)

##### 多对多关联（many-to-many）

要表示多对多关联，必须创建第三个表，该表通常称为`联接表`，它将多对多关系划分为两个一对多关系。将这两个表的主键都插入到第三个表中。

![image-20220601133833826](mysql/image-20220601133833826.png)

多对多关联建表原则：`需要创建第三张表，中间表中至少两个字段，这两个字段分别作为外键指向各自一方的主键。`

举例 1：学生-课程

- 学生信息表 ：一行代表一个学生的信息（学号、姓名、手机号码、班级、系别...）。

- 课程信息表 ：一行代表一个课程的信息（课程编号、授课老师、简介...）。

- 选课信息表 ：一个学生可以选多门课，一门课可以被多个学生选择。

  ![image-20220601140500467](mysql/image-20220601140500467.png)

举例 2：产品-订单

- 订单表和产品表有一种多对多的关系，这种关系是通过与订单明细表建立两个一对多关系来定义的。一个订单可以有多个产品，每个产品可以出现在多个订单中。

- 产品表 ：产品表中的每条记录表示一个产品。

- 订单表 ：订单表中的每条记录表示一个订单。

- 订单明细表 ：每个产品可以与订单表中的多条记录对应，即出现在多个订单中。一个订单可以与产品表中的多条记录对应，即包含多个产品。

  ![image-20220601140146835](mysql/image-20220601140146835.png)

举例 3：用户-角色

- 一个用户，可能存在多个角色。

  ![image-20220601140424296](mysql/image-20220601140424296.png)

##### 自我引用（Self reference）

![image-20220601140611310](mysql/image-20220601140611310.png)

### MySQL 介绍

![image-20220531232307913](mysql/image-20220531232307913.png)

- MySQL 是一个`开放源代码的关系型数据库管理系统` ，由瑞典 MySQL AB（创始人Michael Widenius）公司 1995 年开发，迅速成为开源数据库的 No.1。
- 2008 被 Sun 收购（10 亿美金），2009 年 Sun 被 Oracle 收购。`MariaDB`应运而生。（MySQL 的创造者担心 MySQL 有闭源的风险，因此创建了 MySQL 的分支项目 MariaDB）
- MySQL 6.x 版本之后分为`社区版`和`商业版`。
- MySQL 是一种关联数据库管理系统，将数据保存在不同的表中，而不是将所有数据放在一个大仓库内，这样就增加了速度并提高了灵活性。
- MySQL 是开源的，所以你不需要支付额外的费用。
- MySQL 是可以定制的，采用了`GPL (GNU General Public License) 协议`，你可以修改源码来开发自己的 MySQL 系统。
- MySQL 支持大型的数据库。可以处理拥有上千万条记录的大型数据库。
- MySQL 支持大型数据库，支持 5000 万条记录的数据仓库，32 位系统表文件最大可支持 4 GB ，64 位系统支持最大的表文件为`8 TB`。
- MySQL 使用`标准的SQL数据语言`形式。
- MySQL 可以允许运行于多个系统上，并且支持多种语言。这些编程语言包括 C、C++、Python、Java、Perl、PHP 和 Ruby 等。

#### MySQL 发展史重大事件

MySQL 的历史就是整个互联网的发展史。互联网业务从社交领域、电商领域到金融领域的发展，推动着应用对数据库的需求提升，对传统的数据库服务能力提出了挑战。高并发、高性能、高可用、轻资源、易维护、易扩展的需求，促进了 MySQL 的长足发展。

![image-20220531232824970](mysql/image-20220531232824970.png)

#### 关于 MySQL 8.0

`MySQL 从 5.7 版本直接跳跃发布了 8.0 版本`，可见这是一个令人兴奋的里程碑版本。MySQL 8版本在功能上做了显著的改进与增强，开发者对MySQL的源代码进行了重构，最突出的一点是多MySQL Optimizer优化器进行了改进。不仅在速度上得到了改善，还为用户带来了更好的性能和更棒的体验。

#### Why choose MySQL

![image-20220531233419499](mysql/image-20220531233419499.png)

为什么如此多的厂商要选用 MySQL？大概总结的原因主要有以下几点：

1. 开放源代码，使用成本低。
2. 性能卓越，服务稳定。
3. 软件体积小，使用简单，并且易于维护。
4. 历史悠久，社区用户非常活跃，遇到问题可以寻求帮助。
5. 许多互联网公司在用，经过了时间的验证。

#### Oracle VS MySQL

Oracle 更适合大型跨国企业的使用，因为他们对费用不敏感，但是对性能要求以及安全性有更高的要求。

MySQL 由于其`体积小`、`速度快`、`总体拥有成本低`，`可处理上千万条记录`的大型数据库，尤其是`开放源码`这一特点，使得很多互联网公司、中小型网站选择了 MySQL 作为网站数据库（Facebook，Twitter，YouTube，阿里巴巴/蚂蚁金服，去哪儿，美团外卖，腾讯）。

### SQL 语言

1946 年，世界上第一台电脑诞生，如今，借由这台电脑发展起来的互联网已经自成江湖。在这几十年里，无数的技术、产业在这片江湖里沉浮，有的方兴未艾，有的已经几幕兴衰。但在这片浩荡的波动里，有一门技术从未消失，甚至老当益壮，那就是 SQL。

45 年前，也就是 1974 年，IBM 研究员发布了一篇揭开数据库技术的论文《SEQUEL：一门结构化的英语查询语言》，直到今天这门结构化的查询语言并没有太大的变化，相比于其他语言， SQL 的半衰期可以说是非常长了。

不论是前端工程师，还是后端算法工程师，都一定会和数据打交道，都需要了解如何又快又准确地提取自己想要的数据。更别提数据分析师了，他们的工作就是和数据打交道，整理不同的报告，以便指导业务决策。

`SQL (Structured Query Language，结构化查询语言)`是使用关系模型的数据库应用语言，与数据直接打交道 ，由 IBM 上世纪70年代开发出来。后由美国国家标准局（ANSI）开始着手制定SQL标准，先后有 SQL-86 ， SQL-89 ， SQL-92 ， SQL-99 等标准。

SQL 有两个重要的标准，分别是`SQL-92`和`SQL-99`，它们分别代表了 92 年和 99 年颁布的 SQL 标准，我们今天使用的 SQL 语言依然遵循这些标准。

不同的数据库生产厂商都支持 SQL 语句，但都有特有内容：

![image-20220912195710669](mysql/image-20220912195710669.png)

#### SQL 语言的分类

SQL 语言在功能上主要分为如下三大类：

- `DDL (Data Definition Languages、数据定义语言)`：这些语句定义了不同的数据库、表、视图、索引等数据库对象，还可以用来创建、删除、修改数据库和数据表的结构。
  - 主要的语句关键字包括`CREATE` 、`DROP`、`ALTER`等。
- `DML (Data Manipulation Language、数据操作语言)`：用于添加、删除、更新和查询数据库记录，并检查数据完整性。
  - 主要的语句关键字包括`INSERT`、`DELETE`、`UPDATE`、`SELECT`等。
  - `SELECT 是 SQL 语言的基础，最为重要。`
- `DCL (Data Control Language、数据控制语言)`：用于定义数据库、表、字段、用户的访问权限和安全级别。
  - 主要的语句关键字包括`GRANT`、`REVOKE`、`COMMIT`、`ROLLBACK`、`SAVEPOINT`等。

>因为查询语句使用的非常的频繁，所以很多人把查询语句单拎出来一类：DQL（数据查询语言）。
>
>还有单独将 COMMIT 、 ROLLBACK 取出来的，称为 TCL （Transaction Control Language，事务控制语言）。

#### SQL 语言的规则与规范

##### 基本规则

- SQL 可以写在一行或者多行。为了提高可读性，各子句分行写，必要时使用缩进。
- 每条命令以`;`或`\g`或`\G`结束。
- 关键字不能被缩写也不能分行。
- 关于标点符号：
  - 必须保证所有的 ()、单引号、双引号是成对结束的。
  - 必须使用英文状态下的半角输入方式。
  - 字符串型和日期时间类型的数据可以使用单引号（''）表示。
  - 列的别名，尽量使用双引号（""），而且不建议省略 as。

##### SQL大小写规范

- MySQL 在 Windows 环境下是大小写不敏感的。
- MySQL 在 Linux 环境下是大小写敏感的。
- 数据库名、表名、表的别名、变量名是严格区分大小写的。
  - 关键字、函数名、列名（字段名）、列的别名（字段的别名）是忽略大小写的。
- 推荐采用统一的书写规范：
  - `数据库名、表名、表别名、字段名、字段别名等都小写。`
  - `SQL 关键字、函数名、绑定变量等都大写。`

- 以上规范，建议遵守。

##### 注释

可以使用如下格式的注释结构：

- 单行注释：`#注释文字 (MySQL 特有的方式)`
- 单行注释：`-- 注释文字 (--后面必须包含一个空格。)`
- 多行注释：`/* 注释文字 */`

##### 命名规则

- 数据库、表名不能超过 30 个字符，变量名限制为 29 个字符。
- 只能包含 A–Z，a–z，0–9 和 _ 共 63 个字符。
- 数据库名、表名、字段名等对象名中间不要包含空格。
- 同一个 MySQL 软件中，数据库不能同名；同一个库中，表不能重名；同一个表中，字段不能重名。
- 字段不能和保留字、数据库系统或常用方法冲突。如果必须使用，需在 SQL 语句中使用`` (着重号)`引起来。
- 保持字段名和类型的一致性，在命名字段并为其指定数据类型的时候一定要保证一致性。假如数据类型在一个表里是整数，那在另一个表里可就别变成字符型了

##### 导入指令

命令行登录 MySQL 客户端，使用`source`指令导入 sql 文件：

```sql
mysql> source /tmp/insert.sql
```

> /tmp/insert.sql 是 sql 文件在服务器上的路径。

##  Docker 安装 MySQL

登录 https://hub.docker.com/，查看 MySQL 镜像：

![image-20220530234501437](mysql/image-20220530234501437.png)

选择以下版本，拉取镜像：

![image-20220530235224960](mysql/image-20220530235224960.png)

```bash
$ docker pull mysql:8.0.29
```

查看拉取的镜像：

```bash
$ docker images
```

启动容器：

```bash
$ docker run -d --name mysql_8.0.29 -p 3306:3306 -v /home/xisun/MyDatas/mysql/:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mysql:8.0.29
```

## MySQL 图形化管理工具

### DBeaver

官网：https://dbeaver.io/download/，下载方式如下：

![image-20220531115809915](mysql/image-20220531115809915.png)

Github：https://github.com/dbeaver/dbeaver/releases，下载方式如下：

![image-20220531115703986](mysql/image-20220531115703986.png)

安装程序下载后，点击安装即可。安装成功后，因为 DBeaver 是基于 Maven 构建的，数据库驱动也就是链接数据库的 JDBC 驱动是通过 Maven 仓库下载的，因此，第一步要做的是配置 Maven 镜像，否则在后续下载数据库驱动的时候会非常的慢。

点击 "窗口" ---> "首选项" ---> "连接" ---> "驱动" ---> "Maven"，设置阿里云镜像`http://maven.aliyun.com/nexus/content/groups/public/`：

![image-20220531123024201](mysql/image-20220531123024201.png)

![image-20220531123127630](mysql/image-20220531123127630.png)

配置完 Maven 仓库后，连接数据库：

![image-20220531124034671](mysql/image-20220531124034671.png)

![image-20220531123753835](mysql/image-20220531123753835.png)

![image-20220531124007864](mysql/image-20220531124007864.png)

更多关于 DBeaver 的使用方法，参考：https://juejin.cn/post/7065474476607012878

>DBeaver 是用Java 开发的，使用前需要安装 JDK 环境。

## MySQL 的基本使用

MySQL 语法规范：

- `不区分大小写`；
- 每句话用`;`或`\g`结尾；
- 各子句一般分行写；
- 关键字不能缩写，也不能分行；
- 用缩进提高语句的可读性。

进入容器，并与 MySQL 客户端交互：

```bash
[root@hadoop100 ~]# docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED       STATUS       PORTS                                                  NAMES
9ca17e09b0c1   mysql:8.0.29   "docker-entrypoint.s…"   4 hours ago   Up 4 hours   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   mysql
[root@hadoop100 ~]# docker exec -it 9ca17e09b0c1 /bin/bash
root@9ca17e09b0c1:/# ls
bin  boot  dev	docker-entrypoint-initdb.d  entrypoint.sh  etc	home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
# 进入MySQL客户端，-u用户名，-p密码
root@9ca17e09b0c1:/# mysql -uroot -proot
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 15
Server version: 8.0.29 MySQL Community Server - GPL

Copyright (c) 2000, 2022, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```

- 查看全部的数据库：`SHOW databases;`

  ```bash
  mysql> SHOW databases;
  +--------------------+
  | Database           |
  +--------------------+
  | information_schema |
  | mybatis            |
  | mybatisplus        |
  | mybatisplus_1      |
  | mysql              |
  | performance_schema |
  | sys                |
  +--------------------+
  7 rows in set (0.00 sec)
  ```

  - `information_schema`、`performance_schema`、`sys`和`mysql`，都是 MySQL 系统自带的数据库。

  - `information_schema`库主要保存 MySQL 数据库服务器的系统信息，比如数据库的名称、数据表的名称、字段名称、存取权限、数据文件所在的文件夹和系统使用的文件夹，等等。

  - `performance_schema`库可以用来监控 MySQL 的各类性能指标。

  - `sys`库主要作用是以一种更容易被理解的方式，展示 MySQL 数据库服务器的各类性能指标，帮助系统管理员和开发人员监控 MySQL 的技术性能。

  - `mysql`库保存了 MySQL 数据库服务器运行时需要的系统信息，比如数据文件夹、当前使用的字符集、约束检查信息，等等。

- 创建数据库：`CREATE database 数据库名;`

  ```bash
  mysql> CREATE database test;
  Query OK, 1 row affected (0.02 sec)
  
  mysql> SHOW databases;
  +--------------------+
  | Database           |
  +--------------------+
  | information_schema |
  | mybatis            |
  | mybatisplus        |
  | mybatisplus_1      |
  | mysql              |
  | performance_schema |
  | sys                |
  | test               |
  +--------------------+
  8 rows in set (0.00 sec)
  ```

- 使用数据库：`USE 数据库名;`

  ```bash
  mysql> USE test;
  Database changed
  ```

  - 如果没有使用 USE 语句，对数据库的操作也没有加 "数据名" 的限定，那么会报 "ERROR 1046(3D000): No database selected"（没有选择数据库）。

  - 使用完 USE 语句之后，如果接下来的 SQL 都是针对一个数据库操作的，那就不用重复 USE 了，如果要针对另一个数据库操作，那么要重新 USE。

- 查看当前数据库中有哪些数据表：`SHOW tables;`

  ```bash
  mysql> SHOW tables;
  Empty set (0.00 sec)
  ```

- 查看指定数据库中有哪些数据表：`SHOW tables FROM 数据库名;`

  ```bash
  mysql> SHOW tables FROM mybatisplus;
  +-----------------------+
  | Tables_in_mybatisplus |
  +-----------------------+
  | user                  |
  +-----------------------+
  1 row in set (0.01 sec)
  ```

- 建表：`CREATE table 表名(表结构);`

  ```bash
  mysql> CREATE table student (
      ->   id int,
      ->   name varchar(20)
      -> );
  Query OK, 0 rows affected (0.04 sec)
  mysql> SHOW tables;
  +----------------+
  | Tables_in_test |
  +----------------+
  | student        |
  +----------------+
  1 row in set (0.00 sec)
  ```

  - 如果是最后一个字段，后面就不用加逗号，因为逗号的作用是分割每个字段。

- 查看表结构：`DESC[RIBE] 表名;`，或者`SHOW FULL COLUMNS FROM 表名;`

  ```bash
  mysql> DESC student;
  +-------+-------------+------+-----+---------+-------+
  | Field | Type        | Null | Key | Default | Extra |
  +-------+-------------+------+-----+---------+-------+
  | id    | int         | YES  |     | NULL    |       |
  | name  | varchar(20) | YES  |     | NULL    |       |
  +-------+-------------+------+-----+---------+-------+
  2 rows in set (0.01 sec)
  
  mysql> SHOW FULL COLUMNS FROM student;
  +-------+-------------+--------------------+------+-----+---------+-------+---------------------------------+---------+
  | Field | Type        | Collation          | Null | Key | Default | Extra | Privileges                      | Comment |
  +-------+-------------+--------------------+------+-----+---------+-------+---------------------------------+---------+
  | id    | int         | NULL               | YES  |     | NULL    |       | select,insert,update,references |         |
  | name  | varchar(20) | utf8mb4_0900_ai_ci | YES  |     | NULL    |       | select,insert,update,references |         |
  +-------+-------------+--------------------+------+-----+---------+-------+---------------------------------+---------+
  2 rows in set (0.00 sec)
  ```

- 查看表中的所有记录：`SELECT * FROM 表名;`

  ```bash
  mysql> SELECT * FROM student;
  Empty set (0.10 sec)
  ```

- 向表中添加一条记录：`INSERT INTO 表名 (表字段列表) VALUES (值列表);`

  ```bash
  mysql> INSERT INTO student VALUES (1, 'Tom');
  Query OK, 1 row affected (0.00 sec)
  
  mysql> INSERT INTO student VALUES (1, 'Jerry');
  Query OK, 1 row affected (0.01 sec)
  ```

- 修改表中的记录：`UPDATE 表名 SET 表字段1 = 值1, 表字段2 = 值2 ... WHERE 条件;`

  ```sql
  mysql> UPDATE student SET id = 2 WHERE name = 'Jerry';
  Query OK, 1 row affected (0.04 sec)
  Rows matched: 1  Changed: 1  Warnings: 0
  ```
  
- 删除表中的记录：`DELETE FROM  表名 WHERE 条件;`

  ```sql
  mysql> DELETE FROM student WHERE id = 3;
  Query OK, 1 row affected (0.01 sec)
  ```
  
- 查看表的创建信息：`SHOW CREATE TABLE 表名;`

  ```sql
  mysql> SHOW CREATE TABLE student;
  +---------+--------------------------------------------------------------------------------------------------------------------------------------------------------+
  | Table   | Create Table                                                                                                                                           |
  +---------+--------------------------------------------------------------------------------------------------------------------------------------------------------+
  | student | CREATE TABLE `student` (
    `id` int DEFAULT NULL,
    `name` varchar(20) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci |
  +---------+--------------------------------------------------------------------------------------------------------------------------------------------------------+
  1 row in set (0.00 sec)-
  ```

- 查看数据库的创建信息：`SHOW CREATE DATABASES 表名;`

  ```sql
  mysql> SHOW CREATE DATABASE test;
  +----------+--------------------------------------------------------------------------------------------------------------------------------+
  | Database | Create Database                                                                                                                |
  +----------+--------------------------------------------------------------------------------------------------------------------------------+
  | test     | CREATE DATABASE `test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */ |
  +----------+--------------------------------------------------------------------------------------------------------------------------------+
  1 row in set (0.05 sec)
  ```

- 删除表：`DROP TABLE 表名;`

  ```sql
  mysql> DROP TABLE student;
  Query OK, 0 rows affected (0.32 sec)
  ```
  
- 删除数据库：`DROP DATABASE 表名;`

  ```sql
  mysql> DROP DATABASE test;
  Query OK, 0 rows affected (0.13 sec)
  ```

## MySQL 的编码

查看编码命令：

```sql
mysql> SHOW VARIABLES LIKE 'character_%';
+--------------------------+--------------------------------+
| Variable_name            | Value                          |
+--------------------------+--------------------------------+
| character_set_client     | latin1                         |
| character_set_connection | latin1                         |
| character_set_database   | utf8mb4                        |
| character_set_filesystem | binary                         |
| character_set_results    | latin1                         |
| character_set_server     | utf8mb4                        |
| character_set_system     | utf8mb3                        |
| character_sets_dir       | /usr/share/mysql-8.0/charsets/ |
+--------------------------+--------------------------------+
8 rows in set (0.00 sec)

mysql> SHOW VARIABLES LIKE 'collation_%';
+----------------------+--------------------+
| Variable_name        | Value              |
+----------------------+--------------------+
| collation_connection | latin1_swedish_ci  |
| collation_database   | utf8mb4_0900_ai_ci |
| collation_server     | utf8mb4_0900_ai_ci |
+----------------------+--------------------+
3 rows in set (0.00 sec)
```

在 MySQL 8.0 版本之前，默认字符集为`latin1`，utf8 字符集指向的是 utf8mb3。网站开发人员在数据库设计的时候往往会将编码修改为 utf8 字符集，如果遗忘修改默认的编码，就会出现乱码的问题。从 MySQL 8.0 开始，数据库的默认字符集为`utf8mb4`，从而避免了上述的乱码问题。

## 基本的 SELECT 语句

### 常数

```sql
mysql> SELECT 1;
+---+
| 1 |
+---+
| 1 |
+---+
1 row in set (0.00 sec)

mysql> SELECT 9/2;
+--------+
| 9/2    |
+--------+
| 4.5000 |
+--------+
1 row in set (0.00 sec)

mysql> SELECT 4*3;
+-----+
| 4*3 |
+-----+
|  12 |
+-----+
1 row in set (0.00 sec)
```

### 常规语法

```sql
SELECT [标识选择哪些列] FROM [标识从哪个表中选择] ;
```

查询所有列的所有数据：

```sql
mysql> SELECT * FROM employee;
+----+----------+--------+-----+--------+
| id | emp_name | sex    | age | dep_id |
+----+----------+--------+-----+--------+
|  1 | Tom      | male   |  27 |      1 |
|  2 | Jerry    | male   |  28 |      2 |
|  3 | Lucy     | female |  29 |      2 |
+----+----------+--------+-----+--------+
3 rows in set (0.00 sec)
```

- 一般情况下，除非需要使用表中所有的字段数据，最好不要使用通配符`*`。使用通配符虽然可以节省输入查询语句的时间，但是获取不需要的列数据，通常会降低查询和所使用的应用程序的效率。
- 通配符的优势是，当不知道所需要的列的名称时，可以通过它获取它们。
- `在生产环境下，不推荐直接使用 SELECT * 进行查询。`

查询特定列的所有数据：

```sql
mysql> SELECT emp_name, sex FROM employee;
+----------+--------+
| emp_name | sex    |
+----------+--------+
| Tom      | male   |
| Jerry    | male   |
| Lucy     | female |
+----------+--------+
3 rows in set (0.00 sec)
```

### 别名

语法：`在表名或列名，与其别名之间加入关键字 AS，如果别名中包含空格或特殊的字符或区分大小写，则别名使用双引号。`

```sql
mysql> SELECT emp_name AS name, sex, age FROM employee;
+-------+--------+-----+
| name  | sex    | age |
+-------+--------+-----+
| Tom   | male   |  27 |
| Jerry | male   |  28 |
| Lucy  | female |  29 |
+-------+--------+-----+
3 rows in set (0.00 sec)

mysql> SELECT e.emp_name AS name, e.sex, e.age FROM employee e;
+-------+--------+-----+
| name  | sex    | age |
+-------+--------+-----+
| Tom   | male   |  27 |
| Jerry | male   |  28 |
| Lucy  | female |  29 |
+-------+--------+-----+
3 rows in set (0.01 sec)
```

> AS 可以省略。

### 去除重复行

默认情况下，查询会返回全部行，包括重复行。`在 SELECT 语句中使用关键字 DISTINCT 去除重复行。`

```sql
mysql> SELECT DISTINCT dep_id FROM employee;
+--------+
| dep_id |
+--------+
|      1 |
|      2 |
+--------+
2 rows in set (0.00 sec)
```

