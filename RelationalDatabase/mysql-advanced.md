>**$\textcolor{RubineRed}{Author: ACatSmiling}$**
>
>**$\color{RubineRed}{Since: 2022-05-29}$**

## 索引的创建与设计原则

### 索引的声明与使用

#### 索引的分类

MySQL 的索引包括普通索引、唯一性索引、全文索引、单列索引、多列索引和空间索引等。

- 从`功能逻辑`上说，索引主要有 4 种：普通索引、唯一索引、主键索引、全文索引。
- 按照`物理实现方式`，索引可以分为 2 种：聚簇索引和非聚簇索引。
- 按照`作用字段个数`进行划分，分成单列索引和联合索引。

##### 普通索引

在创建普通索引时，不附加任何限制条件，只是用于提高查询效率。这类索引可以创建在`任何数据类型`中，其值是否唯一和非空，要由字段本身的完整性约束性条件决定。建立索引以后，可以通过索引进行查询。例如，在表 student 的字段 name 上建立一个普通索引，查询记录时就可以根据该索引进行查询。

##### 唯一性索引

使用`UNIQUE 参数`可以设置索引为唯一性索引，在创建唯一性索引时，限制该索引的值必须是唯一的，但允许有空值。在一张数据表里，`可以有多个唯一索引`。例如，在表 student 的字段 email 中创建唯一性索引，那么字段 email 的值就必须是唯一的。通过唯一性索引，可以快速确定某条记录。

##### 主键索引

主键索引就是一种特殊的唯一性索引，在唯一索引的基础上增加了不为空的约束，也就是 NOT NULL + UNIQUE，一张表里`最多只有一个主键索引`。

##### 单列索引

在表中的单个字段上创建索引，单列索引只根据该字段进行索引。单列索引可以是普通索引，也可以是唯一性索引，还可以是全文索引，只要保证该索引只对应一个字段即可。一张表中，`可以有多个单列索引`。

##### 多列（组合、联合）索引

在表中的多个字段组合上创建索引。多列索引指向创建时对应的多个字段，可以通过这几个字段进行查询，但是只有查询条件中使用了这些字段中的第一个字段时才会被使用。例如，在表 student 的字段 id、name 和 gender 上建立一个多列索引 idx_id_name_gender，只有在查询条件中使用了字段 id 时，该索引才会被使用。使用多列索引时，遵循`最左前缀集合`。

##### 全文索引

全文索引，也称全文检索，是目前搜索引擎使用的一种关键技术。它能够利用`分词技术`等多种算法智能分析出文本文字中关键词的频率和重要性，然后按照一定的算法规则智能的筛选出想要的搜索结果。全文索引非常适合大型数据集，对于小的数据集，它的用处比较小。

使用参数`FULLTEXT`可以设置索引为全文索引，在定义索引的列上支持值的全文查找，允许在这些索引列中插入重复值和空值。全文索引只能创建在`CHAR`、`VARCHAR`或`TEXT`类型及其系列类型的字段上，`查询数据量较大的字符串类型的字段时，使用全文索引可以提高查询速度`。例如，表 student 的字段 information 是 TEXT 类型，该字段包含了很多文字信息，在字段 information 上建立全文索引后，可以提高查询字段 information 的速度。

全文索引典型的有两种类型：自然语言的全文索引和布尔全文索引。

- 自然语言搜索引擎将计算每一个文档对象和查询的相关度。这里，相关度是基于匹配的关键词的个数，以及关键词在文档中出现的次数。`在整个索引中出现次数越少的词语，匹配时的相关度就越高。`相反，非常常见的单词将不会被搜索，如果一个词语在超过 50% 的记录中都出现了，那么自然语言的搜索将不会搜索这类词语。

MySQL 数据库从 3.23.23 版开始支持全文索引，但在 MySQL 5.6.4 之前只有 MyISAM 支持，5.6.4 版本以后 InnoDB 才支持，但是官方版本不支持中文分词，需要第三方分词插件。在 5.7.6 版本，MySQL 内置了`ngram 全文解析器`，用来支持亚洲语种的分词。测试或使用全文检索时，需要先查看一下 MySQL 版本、存储引擎和数据类型，是否支持全文索引。

随着大数据时代的到来，关系型数据库应对全文索引的需求已力不从心，逐渐被 Solr、ElasticSearch 等专门的搜索引擎所替代。

##### 补充：空间索引

使用参数`SPATIAL`可以设置索引为空间索引。空间索引只能建立在空间数据类型上，这样可以提高系统获取空间数据的效率。MySQL 中的空间数据类型包括`GEOMETRY`、`POINT`、`LINESTRING`和`POLYGON`等。目前只有 MyISAM 存储引擎支持空间检索，而且索引的字段不能为空值。

##### 小结

不同的存储引擎支持的索引类型也不一样。

- InnoDB：支持 B+Tree、Full-Text 等索引，不支持 Hash 索引；
- MyISAM：支持 B+Tree、Full-Text 等索引，不支持 Hash 索引；
- Memory：支持 B+Tree、Hash 等索引，不支持 Full-Text 索引； 
- NDB：支持 Hash 索引，不支持 B+Tree、Full-Text 等索引； 
- Archive：不支持 B+Tree、Hash、Full-Text 等索引。

#### 创建索引

MySQL 支持多种方法在单个或多个列上创建索引：

- 在创建表的定义语句`CREATE TABLE`中指定索引列。
- 使用`ALTER TABLE`语句在存在的表上创建索引。
- 使用`CREATE INDEX`语句在已存在的表上添加索引。

##### 创建表的时候创建索引

使用 CREATE TABLE 创建表时，除了可以定义列的数据类型外，还可以定义主键约束、外键约束或者唯一性约束，而不论创建哪种约束，在定义约束的同时，相当于在指定列上创建了一个索引。

`隐式的索引创建`：

```mysql
# 1.隐式的添加索引(在添加有主键约束、唯一性约束或者外键约束的字段会自动的创建索引)
CREATE TABLE dept(
    dept_id INT PRIMARY KEY AUTO_INCREMENT,# 创建主键索引
    dept_name VARCHAR(20)
)

CREATE TABLE emp(
    emp_id INT PRIMARY KEY AUTO_INCREMENT,# 主键索引
    emp_name VARCHAR(20) UNIQUE,# 唯一索引
    dept_id INT,
    CONSTRAINT emp_dept_id_fk FOREIGN KEY(dept_id) REFERENCES dept(dept_id)
) # 外键索引
```

`显式的索引创建`，基本语法格式如下，共有七种情况：

```mysql
CREATE TABLE table_name [col_name data_type]
[UNIQUE | FULLTEXT | SPATIAL] [INDEX | KEY] [index_name] (col_name [length]) [ASC | DESC]
```

- UNIQUE、FULLTEXT 和 SPATIAL 为可选参数，分别表示唯一索引、全文索引和空间索引。
- INDEX 与 KEY 为同义词，两者的作用相同，用来指定创建索引。
- index_name 指定索引的名称，为可选参数，如果不指定，那么 MySQL 默认 col_name 为索引名。
- col_name 为需要创建索引的字段列，该字段列必须从数据表中定义的多个列中选择。
- length 为可选参数，表示索引的长度，只有字符串类型的字段才能指定索引长度。
- ASC 或 DESC 指定升序或者降序的索引值存储。
- 特例：主键索引使用主键约束的方式来创建。

**创建普通索引：**

在 book 表中的 year_publication 字段上建立普通索引，SQL 语句如下：

```mysql
CREATE TABLE book(
    book_id INT,
    book_name VARCHAR(100),
    authors VARCHAR(100),
    info VARCHAR(100) ,
    comment VARCHAR(100),
    year_publication YEAR,
    # 声明索引
    INDEX idx_bname(year_publication)
)
```

**创建唯一索引：**

```mysql
CREATE TABLE test(
	id INT NOT NULL,
	name varchar(30) NOT NULL,
	UNIQUE INDEX uk_idx_id(id)
)
```

**主键索引：**

设定为主键后数据库会自动建立索引，InnoDB 为聚簇索引，语法：

```mysql
CREATE TABLE student (
    # 通过定义主键约束的方式定义主键索引
	id INT(10) UNSIGNED AUTO_INCREMENT ,
	student_no VARCHAR(200),
	student_name VARCHAR(200),
	PRIMARY KEY(id)
)
```

删除主键索引：

```mysql
ALTER TABLE student DROP PRIMARY KEY
```

> 修改主键索引：必须先删除掉（DROP）原索引，再新建（ADD）索引。

**创建单列索引：**

```mysql
CREATE TABLE test2(
	id INT NOT NULL,
	name CHAR(50) NULL,
	INDEX single_idx_name(name(20))
)
```

**创建组合索引：**

```mysql
CREATE TABLE test3(
    id INT(11) NOT NULL,
    name CHAR(30) NOT NULL,
    age INT(11) NOT NULL,
    info VARCHAR(255),
    INDEX multi_idx(id, name, age)
)
```

> 组合索引在查询时会遵守`最左索引原则`，先进行 id 条件的比较，然后再进行 name 比较，最后才是 age，因此注意把最常用的查询字段放在索引的最左边。

**创建全文索引：**

FULLTEXT 全文索引可以用于全文搜索，并且只为`CHAR`、`VARCHAR`和`TEXT`列创建索引。索引总是对整个列进行，不支持局部（前缀）索引。

```mysql
CREATE TABLE test4(
    id INT NOT NULL,
    name CHAR(30) NOT NULL,
    age INT NOT NULL,
    info VARCHAR(255),
    FULLTEXT INDEX futxt_idx_info(info)
) ENGINE=MyISAM
```

> 在 MySQL 5.7 及之后版本中可以不指定最后的 ENGINE，因为在此版本中 InnoDB 支持全文索引。

创建一个给 title 和 body 字段添加全文索引的表：

```mysql
CREATE TABLE articles (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR (200),
    body TEXT,
    FULLTEXT index (title, body)
) ENGINE=INNODB
```

```mysql
CREATE TABLE `papers` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `title` (`title`, `content`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8
```

全文索引用 `MATCH + AGAINST` 方式查询：

```mysql
SELECT * FROM papers WHERE MATCH(title, content) AGAINST (‘查询字符串’);
```

>注意：
>
>1. 使用全文索引前，搞清楚版本支持情况；
>2. 全文索引比 LIKE + % 快 N 倍，但是可能存在精度问题；
>3. 如果需要全文索引的是大量数据，建议先添加数据，再创建索引。

**创建空间索引：**

空间索引创建中，要求空间类型的字段必须为`非空` 。

```mysql
CREATE TABLE test5(
	geo GEOMETRY NOT NULL,
	SPATIAL INDEX spa_idx_geo(geo)
) ENGINE=MyISAM
```

##### 在已经存在的表上创建索引

在已经存在的表中创建索引可以使用`ALTER TABLE`语句或者`CREATE INDEX`语句。

**使用 ALTER TABLE 语句创建索引：**

```mysql
ALTER TABLE table_name ADD [UNIQUE | FULLTEXT | SPATIAL] [INDEX | KEY]
[index_name] (col_name[length],...) [ASC | DESC]
```

示例：

```mysql
# 唯一索引
ALTER TABLE book ADD UNIQUE INDEX uk_idex_bid(book_id)

# 单列索引
ALTER TABLE book ADD INDEX inx_cmt(comment(50))

# 组合索引
ALTER TABLE book ADD INDEX idx_author_info(authors(30), info(50))
```

声明主键索引：

```mysql
CREATE TABLE customer2(
	id INT(10) UNSIGNED,
    customer_no VARCHAR(200),
    customer_name VARCHAR(200)
)

ALTER TABLE customer2 ADD PRIMARY KEY customer2(id)
```

**使用 CREATE INDEX 创建索引：**

`CREATE INDEX`语句可以在已经存在的表上添加索引，在 MySQL 中，CREATE INDEX 被映射到一个 ALTER TABLE 语句上，基本语法结构为：

```mysql
CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
ON table_name (col_name[length],...) [ASC | DESC]
```

示例：

```mysql
# 普通索引
CREATE INDEX idx_cmt ON book(comment)

# 唯一索引
CREATE UNIQUE INDEX uk_idx_bid ON book(book_id)

# 组合索引
CREATE INDEX mul_bid_bname_info ON book(book_id, book_name, info)
```

#### 删除索引

MySQ L中删除索引使用`ALTER TABLE`或`DROP INDEX`语句，两者可实现相同的功能，DROP INDEX 语句在内部被映射到一个 ALTER TABLE 语句中。

##### 使用 ALTER TABLE 删除索引

语法：

```mysql
ALTER TABLE table_name DROP INDEX index_name
```

示例：

```mysql
# 查看索引是否存在
SHOW INDEX FROM book\G

# 删除索引
ALTER TABLE book DROP INDEX idx_bk_id
```

>添加 AUTO_INCREMENT 约束字段的唯一索引不能被删除。

##### 使用 DROP INDEX 语句删除索引

语法：

```mysql
DROP INDEX index_name ON table_name
```

示例：

```mysql
# 删除索引
DROP INDEX idx_aut_info ON book

# 查看索引是否删除
SHOW INDEX FROM book\G
```

> 删除表中的列时，如果要删除的列为索引的组成部分，则该列也会从索引中删除。如果组成索引的所有列都被删除，则整个索引将被删除。

### MySQL 8.0 索引新特性

#### 支持降序索引

`降序索引以降序存储键值。`虽然在语法上，从 MySQL 4 版本就已经支持降序索引，但实际上该 DESC 定义是被忽略的，直到 MySQL 8 版本才开始真正支持降序索引（仅限于 InnoDB 存储引擎）。

MySQL 在 8.0 版本之前创建的仍然是升序索引，使用时进行反向扫描，这大大降低了数据库的效率。在某些场景下，降序索引意义重大。例如，如果一个查询，需要对多个列进行排序，且顺序要求不一致，那么使用降序索引，将会避免数据库使用额外的文件排序操作，从而提高性能。

示例：

```mysql
mysql> CREATE TABLE ts1(a int, b int, index idx_a_b(a, b desc)); 
Query OK, 0 rows affected (0.04 sec)

mysql> SHOW CREATE TABLE ts1;
+-------+---------------------------------------------------------------------------------------------------------------------------------------------+
| Table | Create Table                                                                                                                                |
+-------+---------------------------------------------------------------------------------------------------------------------------------------------+
| ts1   | CREATE TABLE `ts1` (
  `a` int DEFAULT NULL,
  `b` int DEFAULT NULL,
  KEY `idx_a_b` (`a`,`b` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 |
+-------+---------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

从结果可以看出，索引已经是降序了。

向数据表 ts1 中插入 800 条随机数据，执行语句如下：

```mysql
mysql> DELIMITER //
mysql> CREATE PROCEDURE ts_insert()
    -> BEGIN
    -> DECLARE i INT DEFAULT 1;
    -> WHILE i < 800
    -> DO
    -> insert into ts1 select rand()*80000,rand()*80000;
    -> SET i = i + 1;
    -> END WHILE;
    -> commit;
    -> END //
Query OK, 0 rows affected (0.02 sec)

mysql> DELIMITER ;
mysql> CALL ts_insert();
Query OK, 0 rows affected (3.95 sec)
```

在 MySQL 8.0 版本中查看数据表 ts1 的执行计划：

```mysql
mysql> EXPLAIN SELECT * FROM ts1 ORDER BY a, b DESC LIMIT 5;
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
| id | select_type | table | partitions | type  | possible_keys | key     | key_len | ref  | rows | filtered | Extra       |
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | ts1   | NULL       | index | NULL          | idx_a_b | 10      | NULL |    5 |   100.00 | Using index |
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.01 sec)
```

从结果可以看出，执行计划中扫描数为 5，并且没有使用 Using filesort。

> 可以对比 MySQL 5.0 版本中相同查询的执行计划，执行计划中扫描数远大于 MySQL 8.0，并且使用了 Using filesort 排序。
>
> Using filesort 是 MySQL 中一种速度比较慢的外部排序，能避免是最好的。多数情况下，管理员可以通过优化索引来尽量避免出现 Using filesort，从而提高数据库执行速度。

`降序索引只对查询中特定的排序顺序有效，如果使用不当，反而查询效率更低。`例如，将上述查询的排序条件改为`ORDER BY a desc, b desc`，然后在 MySQL 8.0 版本中查看数据表 ts1 的执行计划：

```mysql
mysql> EXPLAIN SELECT * FROM ts1 ORDER BY a DESC,b DESC LIMIT 5;
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-----------------------------+
| id | select_type | table | partitions | type  | possible_keys | key     | key_len | ref  | rows | filtered | Extra                       |
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-----------------------------+
|  1 | SIMPLE      | ts1   | NULL       | index | NULL          | idx_a_b | 10      | NULL |  799 |   100.00 | Using index; Using filesort |
+----+-------------+-------+------------+-------+---------------+---------+---------+------+------+----------+-----------------------------+
1 row in set, 1 warning (0.00 sec)
```

> 此时再对比  MySQL 5.0 版本中相同查询的执行计划，会发现 MySQL 5.0 中执行计划会优于 MySQL 8.0。

#### 隐藏索引

在 MySQL 5.7 版本及之前，只能通过显式的方式删除索引。此时，如果发现删除索引后出现错误，又只能通过显式创建索引的方式将删除的索引创建回来。如果数据表中的数据量非常大，或者数据表本身比较大，这种操作就会消耗系统过多的资源，操作成本非常高。

从 MySQL 8.x 开始支持`隐藏索引 (invisible indexes)`，只需要将待删除的索引设置为隐藏索引，使查询优化器不再使用这个索引（即使使用 force index（强制使用索引），优化器也不会使用该索引），确认将索引设置为隐藏索引后系统不受任何响应，就可以彻底删除索引。这种通过先将索引设置为隐藏索引，再删除索引的方式就是`软删除`。

同时，如果想验证某个索引删除之后的查询性能影响，也可以暂时先隐藏该索引。

索引默认是可见的，在使用 CREATE TABLE，CREATE INDEX 或者 ALTER TABLE 等语句时，可以通过`VISIBLE`或者`INVISIBLE`关键词设置索引的可见性。

> 主键不能被设置为隐藏索引，当表中没有显式主键时，表中第一个唯一非空索引会成为隐式索引，也不能设置为隐藏索引。
>
> 当索引被隐藏时，它的内容仍然是和正常索引一样实时更新的。如果一个索引需要长期被隐藏，那么可以将其删除，因为索引的存在会影响插入、更新和删除的性能。

##### 创建表时直接创建

在 MySQL 中创建隐藏索引通过 SQL 语句`INVISIBLE`来实现，语法：

```mysql
 CREATE TABLE tablename(
    propname1 type1[CONSTRAINT1],
    propname2 type2[CONSTRAINT2],
    ......
    propnamen typen,
    INDEX [indexname](propname1 [(length)]) INVISIBLE
)
```

上述语句比普通索引多了一个关键字`INVISIBLE`，用来标记索引为不可见索引。

示例：

```mysql
CREATE TABLE book(
    book_id INT,
    book_name VARCHAR(100),
    authors VARCHAR (100),
    info VARCHAR (100),
    comment VARCHAR (100),
    year_publication YEAR,
    # 创建不可见的索引
    INDEX idx_cmt(comment) INVISIBLE
)
```

##### 在已经存在的表上创建

语法：

```mysql
CREATE INDEX indexname
ON tablename(propname[(length)]) INVISIBLE
```

示例：

```mysql
CREATE INDEX idx_year_pub ON book(year_publication) INVISIBLE
```

##### 通过 ALTER TABLE 语句创建

语法：

```mysql
ALTER TABLE tablename
ADD INDEX indexname (propname[(length)]) INVISIBLE
```

示例：

```mysql
ALTER TABLE book
ADD UNIQUE INDEX uk_idx_bname(book_name) INVISIBLE
```

##### 切换索引可见状态

已存在的索引可通过如下语句切换可见状态：

```mysql
# 切换成隐藏索引
ALTER TABLE tablename ALTER INDEX index_name INVISIBLE
# 切换成非隐藏索引
ALTER TABLE tablename ALTER INDEX index_name VISIBLE
```

示例：

```mysql
# 可见--->不可见
ALTER TABLE book ALTER INDEX idx_year_pub INVISIBLE
# 不可见--->可见
ALTER TABLE book ALTER INDEX idx_cmt VISIBLE
```

##### 使隐藏索引对查询优化器可见

在 MySQL 8.x 版本中，为索引提供了一种新的测试方式，可以通过查询优化器的一个开关 （`use_invisible_indexes`）来打开某个设置，使隐藏索引对查询优化器可见。如果 use_invisible_indexes 设置为 off（默认），优化器会忽略隐藏索引。如果设置为 on，即使隐藏索引不可见，优化器在生成执行计划时仍会考虑使用隐藏索引。

查看：

```mysql
mysql> SELECT @@optimizer_switch;
+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| @@optimizer_switch                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on,engine_condition_pushdown=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=on,block_nested_loop=on,batched_key_access=off,materialization=on,semijoin=on,loosescan=on,firstmatch=on,duplicateweedout=on,subquery_materialization_cost_based=on,use_index_extensions=on,condition_fanout_filter=on,derived_merge=on,use_invisible_indexes=off,skip_scan=on,hash_join=on,subquery_to_derived=off,prefer_ordering_index=on,hypergraph_optimizer=off,derived_condition_pushdown=on |
+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

修改：

```mysql
mysql> SELECT @@optimizer_switch;
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| @@optimizer_switch                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on,engine_condition_pushdown=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=on,block_nested_loop=on,batched_key_access=off,materialization=on,semijoin=on,loosescan=on,firstmatch=on,duplicateweedout=on,subquery_materialization_cost_based=on,use_index_extensions=on,condition_fanout_filter=on,derived_merge=on,use_invisible_indexes=on,skip_scan=on,hash_join=on,subquery_to_derived=off,prefer_ordering_index=on,hypergraph_optimizer=off,derived_condition_pushdown=on |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

### 索引的设计原则

#### 数据准备

创建数据库和表：

```mysql
mysql> CREATE DATABASE atguigudb1;
Query OK, 1 row affected (0.01 sec)

mysql> USE atguigudb1;
Database changed

mysql> CREATE TABLE `student_info` (
    -> `id` INT(11) NOT NULL AUTO_INCREMENT,
    -> `student_id` INT NOT NULL ,
    -> `name` VARCHAR(20) DEFAULT NULL,
    -> `course_id` INT NOT NULL ,
    -> `class_id` INT(11) DEFAULT NULL,
    -> `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 3 warnings (0.04 sec)

mysql> CREATE TABLE `course` (
    -> `id` INT(11) NOT NULL AUTO_INCREMENT,
    -> `course_id` INT NOT NULL ,
    -> `course_name` VARCHAR(40) DEFAULT NULL,
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 2 warnings (0.03 sec)
```

创建模拟数据必需的存储函数：

```mysql
# 函数1: 创建随机产生字符串函数
mysql> DELIMITER //
mysql> CREATE FUNCTION rand_string(n INT)
    -> RETURNS VARCHAR(255) # 函数会返回一个字符串
    -> BEGIN
    -> DECLARE chars_str VARCHAR(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ';
    -> DECLARE return_str VARCHAR(255) DEFAULT '';
    -> DECLARE i INT DEFAULT 0;
    -> WHILE i < n DO
    -> SET return_str = CONCAT(return_str, SUBSTRING(chars_str, FLOOR(1 + RAND() * 52), 1));
    -> SET i = i + 1;
    -> END WHILE;
    -> RETURN return_str;
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;

# 函数2: 创建随机数函数
mysql> DELIMITER //
mysql> CREATE FUNCTION rand_num(from_num INT, to_num INT) RETURNS INT(11)
    -> BEGIN
    -> DECLARE i INT DEFAULT 0;
    -> SET i = FLOOR(from_num + RAND() * (to_num - from_num + 1)) ;
    -> RETURN i;
    -> END //
Query OK, 0 rows affected, 1 warning (0.01 sec)

mysql> DELIMITER ;
```

>主从复制，主机会将写操作记录在 bin-log 日志中。从机读取 bin-log 日志，执行语句来同步数据。如果使用函数来操作数据，会导致从机和主机操作时间不一致。所以，默认情况下，MySQL 不开启创建函数设置。
>
>查看 MySQL 是否允许创建函数：
>
>```mysql
>mysql> SHOW VARIABLES LIKE 'log_bin_trust_function_creators';
>+---------------------------------+-------+
>| Variable_name                   | Value |
>+---------------------------------+-------+
>| log_bin_trust_function_creators | OFF   |
>+---------------------------------+-------+
>1 row in set (0.01 sec)
>```
>
>命令开启，允许创建函数设置：
>
>```mysql
>mysql> SET GLOBAL log_bin_trust_function_creators=1;
>Query OK, 0 rows affected (0.00 sec)
>```
>
>如果不进行如上设置，创建函数时会报错：
>
>```mysql
>ERROR 1418 (HY000): This function has none of DETERMINISTIC, NO SQL, or READS SQL DATA in its declaration and binary logging is enabled (you *might* want to use the less safe log_bin_trust_function_creators variable)
>```

创建插入模拟数据的存储过程：

```mysql
# 存储过程1: 创建插入课程表的存储过程
mysql> DELIMITER //
mysql> CREATE PROCEDURE insert_course(max_num INT)
    -> BEGIN
    -> DECLARE i INT DEFAULT 0;
    -> SET autocommit = 0; # 设置手动提交事务
    -> REPEAT # 循环
    -> SET i = i + 1; # 赋值
    -> INSERT INTO course(course_id, course_name) VALUES (rand_num(10000, 10100), rand_string(6));
    -> UNTIL i = max_num
    -> END REPEAT;
    -> COMMIT; # 提交事务
    -> END //
Query OK, 0 rows affected (0.02 sec)

mysql> DELIMITER ;

# 存储过程2: 创建插入学生信息表的存储过程
mysql> DELIMITER //
mysql> CREATE PROCEDURE insert_stu(max_num INT)
    -> BEGIN
    -> DECLARE i INT DEFAULT 0;
    -> SET autocommit = 0; # 设置手动提交事务
    -> REPEAT # 循环
    -> SET i = i + 1; # 赋值
    -> INSERT INTO student_info(course_id, class_id, student_id, name) VALUES (rand_num(10000, 10100), rand_num(10000, 10200), rand_num(1, 200000), rand_string(6));
    -> UNTIL i = max_num
    -> END REPEAT;
    -> COMMIT; # 提交事务
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

调用存储过程：

```mysql
mysql> CALL insert_course(100);
Query OK, 0 rows affected (0.02 sec)

mysql> CALL insert_stu(1000000);
Query OK, 0 rows affected (57.34 sec)
```

#### 适合创建索引的情景

##### 字段的数值有唯一性的限制

索引本身可以起到约束的作用，比如唯一索引、主键索引都是可以起到唯一性约束的，因此在数据表中，如果`某个字段是唯一性的`，就可以直接创建唯一性索引，或者主键索引。这样，可以更快速的通过该索引来确定某条记录。

>业务上具有唯一特性的字段，即使是组合字段，也必须建成唯一索引。（来源：Alibaba）
>
>说明：不要以为唯一索引影响了 INSERT 速度，实际上这个速度损耗可以忽略，但提高查找速度是明显的。

##### 频繁作为 WHERE 查询条件的字段

某个字段`在 SELECT 语句的 WHERE 条件中经常被使用到`，那么就需要给这个字段创建索引了。尤其是在数据量大的情况下，创建普通索引就可以大幅提升数据查询的效率。

##### 经常 GROUP BY 和 ORDER BY 的列

索引其实就是让数据按照某种顺序进行存储或检索。当我们使用 GROUP BY 对数据进行分组查询，或者使用 ORDER BY 对数据进行排序的时候，如果`对分组或者排序的字段建立索引`，本身索引的数据就已经排好序了，进行分组查询和排序操作性能不是很 nice 吗？另外，如果待排序的列有多个，那么可以在这些列上建立`组合索引`。

- 如果仅仅使用 GROUP BY 或者 ORDER BY，且后面只有一个字段，则单独建立索引；如果后面跟多个字段，则建立组合索引。
- 如果既有 GROUP BY 又有 ORDER BY，那就建立联合索引，且 GROUP BY 的字段写在前面，ORDER BY 的字段写在后面。MySQL 8.0 后的版本也可以考虑使用降序索引。

##### UPDATE、DELETE 的 WHERE 条件列

`对数据按照某个条件进行查询后再进行 UPDATE 或 DELETE 的操作，如果对 WHERE 字段创建了索引，就能大幅提升效率。`原理是因为需要先根据 WHERE 条件列检索出来这条记录，然后再对它进行更新或删除。如果进行更新的时候，更新的字段是非索引字段，提升的效率会更明显，这是因为非索引字段更新不需要对索引进行维护。

##### DISTINCT 字段需要创建索引

有时候需要对某个字段进行去重，使用`DISTINCT`，那么对这个字段创建索引，也会提升查询效率。这是因为索引会对数据按照某种顺序进行排序，所以在去重的时候会快很多。

##### 多表 JOIN 连接操作时，创建索引注意事项

首先，`连接表的数量尽量不要超过 3 张`，因为每增加一张表就相当于增加了一次嵌套的循环，数量级增长会非常快（n, n^2, n^3, …），严重影响查询的效率。

其次，`对 WHERE 条件创建索引`，因为 WHERE 才是对数据条件的过滤。如果在数据量非常大的情况下，没有 WHERE 条件过滤是非常可怕的。

最后，`对用于连接的字段创建索引`，并且该字段在多张表中的`类型必须一致`。比如 course_id 在 student_info 表和 course 表中都为 int(11) 类型，而不能一个为 int 另一个为 varchar 类型。否则在查询时，虽然也会进行隐式的类型转换，转换时会使用函数，但会导致`索引失效`。

##### 使用列的类型小的创建索引

此处说的`类型大小`，指的就是该类型表示的数据范围的大小。

在定义表结构的时候，要显式的指定列的类型，以整数类型为例，有 TINYINT、MEDIUMINT、INT、BIGINT 等，它们占用的存储空间依次递增，能表示的整数范围当然也是依次递增。如果想要对某个整数列建立索引的话，在表示的整数范围允许的情况下，尽量让索引列使用较小的类型，比如能使用 INT 就不要使用 BIGINT，能使用 MEDIUMINT 就不要使用 INT。原因如下：

- 数据类型越小，在查询时进行的比较操作就越快；
- 数据类型越小，索引占用的存储空间就越小，在一个数据页内就可以放下更多的记录，从而减少磁盘 I/O 带来的性能损耗，也就意味着可以把更多的数据页缓存在内存中，从而加快读写效率。

这个建议对于表的主键来说更加适应，因为不仅是聚簇索引中会存储主键值，其他所有的二级索引的节点处，都会存储一份记录的主键值，如果主键使用更小的数据类型，也就意味着节省更多的存储空间和更高效的 I/O。

##### 使用字符串前缀创建索引

假设字符串很长，那存储一个字符串就需要占用很大的存储空间。在需要为这个字符串列建立索引时，就意味着在对应的 B+Tree 中有这样的两个问题：

- B+Tree 索引中的记录需要把该列的完整字符串存储起来，更费时，而且字符串越长，在索引中占用的存储空间越大。
- 如果 B+Tree 索引中索引列存储的字符串很长，那在作字符串比较时会占用更多的时间。

此时，可以通过截取字段的前面一部分内容建立索引，即`前缀索引`。这样，在查找记录时，虽然不能精确的定位到记录的位置，但是能定位到相应字符串前缀所在的位置，然后根据前缀相同的记录的主键值，回表查询完整的字符串值。即节约空间，又减少了字符串的比较时间，还大体能解决排序的问题。

例如，TEXT 和 BLOG 类型的字段，进行全文检索会很浪费时间，如果只检索字段前面的若干字符，可以提高检索速度。

示例：

```mysql
# 创建一张商户表
CREATE TABLE shop(address varchar(120) NOT NULL);

# 因为地址字段比较长, 在地址字段上建立前缀索引
ALTER TABLE shop ADD INDEX(address(12));
```

问题是，前缀截取多少呢？截取的多了，达不到节省索引存储空间的目的，截取的少了，重复内容太多，字段的`散列度 (选择性)`会降低。 怎么计算不同的长度的选择性呢？

先看一下字段在全部数据中的选择度：

```mysql
SELECT COUNT(DISTINCT address) / COUNT(*) FROM shop
```

然后，通过不同长度去计算，与全表的选择性对比，公式：

```mysql
COUNT(DISTINCT LEFT(列名, 索引长度)) / COUNT(*)
```

例如：

```mysql
SELECT
COUNT(DISTINCT LEFT(address, 10)) / COUNT(*) AS sub10, -- 截取前10个字符的选择度 
COUNT(DISTINCT LEFT(address, 15)) / COUNT(*) AS sub11, -- 截取前15个字符的选择度 
COUNT(DISTINCT LEFT(address, 20)) / COUNT(*) AS sub12, -- 截取前20个字符的选择度 
COUNT(DISTINCT LEFT(address, 25)) / COUNT(*) AS sub13  -- 截取前25个字符的选择度
FROM shop
```

>拓展：Alibaba《Java开发手册》
>
>【 强制 】在 varchar 字段上建立索引时，必须指定索引长度，没必要对全字段建立索引，根据实际文本区分度决定索引长度。
>
>说明：索引的长度与区分度是一对矛盾体，一般对字符串类型数据，长度为 20 的索引，区分度会高达 90% 以上，可以使用`COUNT(DISTINCT LEFT(列名, 索引长度)) / COUNT(*)`计算的区分度来确定。

**引申：索引列前缀对排序的影响。**

如果使用了索引列前缀，比方说前边只把 address 列的前 12 个字符放到了二级索引中，下边的查询会有问题：

```mysql
SELECT * FROM shop ORDER BY address LIMIT 12
```

因为二级索引中不包含完整的 address 列信息，所以无法对前 12 个字符相同，后边的字符不同的记录进行排序，也就是`使用索引列前缀的方式无法支持使用索引排序`，只能使用文件排序。

##### 区分度高（散列性高）的列适合作为索引

`列的基数`指的是某一个列中不重复数据的个数，比方说某个列包含值 2，5，8，2，5，8，2，5，8，虽然有 9 条记录，但该列的基数是 3。也就是说，在记录行数一定的情况下，列的基数越大，该列中的值越分散；列的基数越小，该列中的值越集中。这个列基数指标非常重要，直接影响是否能有效的利用索引。最好为列的基数大的列建立索引，为基数太小的列建立索引，效果可能不好。

可以使用公式`SELECT COUNT(DISTINCT a) / COUNT(*) FROM t1`计算区分度，越接近 1 越好，一般超过`33%`就算是比较高效的索引。

> 拓展：**联合索引中，应把区分度高（散列性高）的列，放在前面。**

##### 使用最频繁的列放到联合索引的左侧

这样也可以较少的建立一些索引。同时，由于`最左前缀原则`，可以增加联合索引的使用率。

##### 在多个字段都要创建索引的情况下，联合索引优于单值索引

原因：

- 索引建立的多，维护的成本也高。
- 多个字段进行联合查询时，其实只使用到一个索引。
- 在建立联合索引的相关字段做查询时，联合索引都能生效，使用频率比较高，足够优化 SQL 执行的速度了。

#### 限制索引的数目

在实际工作中，也需要注意平衡，`索引的数目不是越多越好`。我们需要限制每张表上的索引数量，`建议单张表索引数量不超过 6 个`。原因：

- 每个索引都需要占用磁盘空间，索引越多，需要的磁盘空间就越大。


- 索引会影响 INSERT、DELETE、 UPDATE 等语句的性能，因为表中的数据更改的同时，索引也会进行调整和更新，会造成负担。


- 优化器在选择如何优化查询时，会根据统一信息，对每一个可以用到的索引来进行评估，以生成出一个最好的执行计划，如果同时有很多个索引都可以用于查询，会增加 MySQL 优化器生成执行计划的时间，降低查询性能。这是因为表中创建的索引过多，优化器在 possible_keys 中选择合适的 key 时需要的成本也会更多。比如下面查询中 possible_keys 有两个，实际使用的 key 只有一个，这个过程是优化器判断的。

  ```mysql
  mysql> EXPLAIN SELECT student_id, COUNT(*) AS num FROM student_info
      -> GROUP BY student_id
      -> ORDER BY create_time DESC
      -> LIMIT 100;
  +----+-------------+--------------+------------+-------+--------------------------+---------+---------+------+--------+----------+---------------------------------+
  | id | select_type | table        | partitions | type  | possible_keys            | key     | key_len | ref  | rows   | filtered | Extra                           |
  +----+-------------+--------------+------------+-------+--------------------------+---------+---------+------+--------+----------+---------------------------------+
  |  1 | SIMPLE      | student_info | NULL       | index | idx_sid,idx_cre_time_sid | idx_sid | 4       | NULL | 997449 |   100.00 | Using temporary; Using filesort |
  +----+-------------+--------------+------------+-------+--------------------------+---------+---------+------+--------+----------+---------------------------------+
  1 row in set, 1 warning (0.00 sec)
  ```

#### 不适合创建索引的情景

##### 在 WHERE 中使用不到的字段，不要设置索引

WHERE 条件（包括 GROUP BY 和 ORDER BY）里用不到的字段，不需要创建索引，索引的价值是快速定位，如果起不到定位的字段，通常是不需要创建索引的。

##### 数据量小的表最好不要使用索引

如果表记录太少，比如少于 1000 个，那么是不需要创建索引的。表记录太少，是否创建索引对查询效率的影响并不大，甚至说，查询花费的时间，可能比遍历索引的时间还要短，索引可能不会产生优化效果。

##### 有大量重复数据的列上不要建立索引

在条件表达式中经常用到的不同值较多的列上建立索引，但字段中如果有大量重复数据，也不用创建索引。比如性别字段，往往会有大量重复字段，如果建立索引，不但不会提高查询效率，反而会严重降低数据更新速度。

> 索引的价值是快速定位，如果想要定位的数据有很多，那么索引就失去了它的价值，比如通常情况下的性别字段。**当数据重复度大，比如高于 10% 的时候，也就不需要对这个字段使用索引。**

##### 避免对经常更新的表创建过多的索引

第一层含义：频繁更新的字段不一定需要创建索引。因为更新数据的时候，也需要更新索引，如果索引太多，在更新索引的时候也会造成负担，从而影响效率。

第二层含义：避免对经常更新的表创建过多的索引，并且索引中的列尽可能少。此时，虽然提高了查询速度，同时却会降低更新表的速度。

##### 不建议用无序的值作为索引

例如身份证、UUID（在索引比较时需要转为 ASCⅡ，并且插入时可能造成页分裂）、MD5、HASH、无序长字符串等。

##### 删除不再使用或者很少使用的索引

表中的数据被大量更新，或者数据的使用方式被改变后，原有的一些索引可能不再需要。数据库管理员应当定期找出这些索引，将它们删除，从而减少索引对更新操作的影响。

##### 不要定义冗余或重复的索引

有时候无意或者有意的对同一个列创建了多个索引，比如：index(a, b, c) 相当于 index(a)、index(a, b)、index(a, b, c)。

冗余索引：

```mysql
CREATE TABLE person_info(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL, 
    phone_number CHAR(11) NOT NULL,
    country varchar(100) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_name_birthday_phone_number (name(10), birthday, phone_number),
    KEY idx_name (name(10))
)
```

- 通过 idx_name_birthday_phone_number 索引就可以对 name 列进行快速搜索，再创建一 个专门针对 name 列的索引就算是一个 冗余索引，维护这个索引只会增加维护的成本，并不会对搜索有 什么好处。

重复索引：

```mysql
CREATE TABLE repeat_index_demo (
    col1 INT PRIMARY KEY,
    col2 INT,
    UNIQUE uk_idx_c1 (col1),
    INDEX idx_c1 (col1)
)
```

- col1 既是主键，又给它定义为一个唯一索引，还给它定义了一个普通索引，可是主键本身就会生成聚簇索引，所以定义的唯一索引和普通索引是重复的，这种情况要避免。

#### 小结

索引是一把双刃剑，可以提高查询效率，但也会降低插入和更新的速度，并占用磁盘空间。

使用索引的最终目的是为了使查询的速度变快，上面给出的原则是最基本的准则，但是不能拘泥于上面的准则，在日常的学习和工作中需要进行不断的实践，根据应用的实际情况进行分析和判断，从而选择最合适的索引方式。

## 性能分析工具的使用

`在数据库调优中，目标就是 响应时间更快，吞吐量更大。`利用宏观的监控工具和微观的日志分析可以帮助快速找到调优的思路和方式。

### 数据库服务器的优化步骤

当遇到数据库调优问题的时候，该如何思考呢？这里把思考的流程整理成下面这张图。

整个流程划分成了` 观察（Show status）`和`行动（Action）`两个部分。字母 S 的部分代表观察（会使用相应的分析工具），字母 A 代表的部分是行动（对应分析可以采取的行动）：

<img src="mysql-advanced/image-20230711090137783.png" alt="image-20230711090137783" style="zoom:60%;" />

数据库调优时，可以通过观察了解数据库整体的运行状态，通过性能分析工具，可以帮助了解执行慢的 SQL 有哪些，查看具体的 SQL 执行计划，甚至是 SQL 执行中的每一步的成本代价，这样才能定位问题所在，找到了问题，再采取相应的行动。

- 首先，在 S1 部分，我们需要观察服务器的状态是否存在周期性的波动。如果`存在周期性波动`，有可能是周期性节点的原因，比如双十一、促销活动等。这样的话，可以通过 A1 这一步骤解决，也就是加缓存，或者更改缓存失效策略。
- 如果缓存策略没有解决，或者不是周期性波动的原因，就需要进一步`分析查询延迟和卡顿的原因`。接下来进入 S2 这一步，需要开`启慢查询`，慢查询可以帮助定位执行慢的 SQL 语句。可以通过设置`long_query_time`参数定义慢查询的阈值，如果 SQL 执行时间超过了 long_query_time，则会认为是慢查询。当收集上来这些慢查询之后，就可以通过分析工具对慢查询日志进行分析。
- 在 S3 这一步骤中，我们知道了执行慢的 SQL，可以针对性的用`EXPLAIN`查看对应 SQL 语句的执行计划，或者用`SHOW PROFILE`查看 SQL 中每一个步骤的时间成本。然后，就可以了解 SQL 查询慢是因为执行时间长，还是等待时间长。
- 如果是 SQL 等待时间长，进入 A2 步骤。在这一步骤中，可以`调优服务器的参数`，比如适当增加数据库缓冲池等。如果是 SQL 执行时间长，就进入 A3 步骤，这一步中需要考虑是索引设计的问题，还是查询关联的数据表过多，亦或者是因为数据表的字段设计问题导致了这一问题，然后在对应的维度上进行相应的调整。
- 如果 A2 和 A3 都不能解决问题，我们需要考虑数据库自身的 SQL 查询性能是否已经达到了瓶颈，如果确认没有达到性能瓶颈，就需要重新检查，重复以上的步骤。如果已经`达到了性能瓶颈`，进入 A4 阶段，需要考虑`增加服务器`，采用`读写分离`的架构，或者考虑对数据库进行`分库分表`，比如垂直分库、垂直分表和水平分表等。

以上就是数据库调优的流程思路，如果发现执行 SQL 时存在不规则延迟或卡顿的时候，就可以采用分析工具帮忙定位有问题的 SQL，这三种分析工具可以理解是 SQL 调优的三个步骤：`慢查询`、`EXPLAIN`和`SHOW PROFILING`。

小结：

<img src="mysql-advanced/image-20230712090052115.png" alt="image-20230712090052115" style="zoom:50%;" />

- 可以看到数据库调优的步骤中越往金字塔尖走，其成本越高，效果越差，因此在数据库调优的过程中，要重点把握金字塔底部的 SQL 及索引调优，数据库表结构调优，系统配置参数调优等软件层面的调优。

### 查看系统性能参数

可以使用`SHOW STATUS`语句查询一些数据库服务器的性能参数和使用频率，语法：

```mysql
SHOW [GLOBAL][SESSION] STATUES LIKE '[参数]'
```

一些常用的性能参数如下：

- Connections：连接 MySQL 服务器的次数。
- Uptime：MySQL 服务器的上线时间。
- Slow_queries：慢查询的次数。
- Innodb_rows_read：SELECT 查询返回的行数。
- Innodb_rows_inserted：执行 INSERT 操作插入的行数。
- Innodb_rows_updated：执行 UPDATE 操作更新的行数。
- Innodb_rows_deleted：执行 DELETE 操作删除的行数。
- Com_select：查询操作的次数。
- Com_insert：插入操作的次数。对于批量插入的 INSERT 操作，只累加一次。
- Com_update：更新操作的次数。
- Com_delete：删除操作的次数。

### 统计 SQL 的查询成本：last_query_cost

一条 SQL 查询语句在执行前需要确定查询执行计划，如果存在多种执行计划的话，MySQL 会计算每个执行计划所需要的成本，从中选择`成本最小`的一个作为最终执行的执行计划。

如果想要查看某条 SQL 语句的查询成本，可以在执行完这条 SQL 语句之后，通过查看当前会话中的`last_query_cost`变量值来得到当前查询的成本。last_query_cost 也是`评价一个查询的执行效率`的一个常用指标，这个查询成本对应的是`SQL 语句所需要读取的页的数量`。

以 student_info 表为例：

1. 查询 id = 900001 的记录，可以直接在聚簇索引上进行查找。查看查询优化器的成本，实际上只需要检索一个页即可，`Value`表示 I/O 加载的数据页的页数。

   ```mysql
   mysql> SELECT * FROM student_info WHERE id = 900001;
   +--------+------------+--------+-----------+----------+---------------------+
   | id     | student_id | name   | course_id | class_id | create_time         |
   +--------+------------+--------+-----------+----------+---------------------+
   | 900001 |     103954 | BfYzAg |     10064 |    10138 | 2023-07-02 18:54:38 |
   +--------+------------+--------+-----------+----------+---------------------+
   1 row in set (0.00 sec)
   
   mysql> SHOW STATUS LIKE 'last_query_cost';
   +-----------------+----------+
   | Variable_name   | Value    |
   +-----------------+----------+
   | Last_query_cost | 1.000000 |
   +-----------------+----------+
   1 row in set (0.01 sec)
   ```

2. 扩大下查询范围，student_id > 199900 的学生记录，大概需要进行 102120 个页的查询。

   ```mysql
   mysql> SELECT * FROM student_info WHERE student_id > 199900;
   +--------+------------+--------+-----------+----------+---------------------+
   | id     | student_id | name   | course_id | class_id | create_time         |
   +--------+------------+--------+-----------+----------+---------------------+
   |    607 |     199908 | PYxrsM |     10000 |    10080 | 2023-07-02 18:53:47 |
   |   1788 |     199993 | XLQSTN |     10092 |    10070 | 2023-07-02 18:53:47 |
   |   3042 |     199909 | OTcESy |     10023 |    10148 | 2023-07-02 18:53:47 |
   | 999891 |     199922 | mlvtHf |     10087 |    10184 | 2023-07-02 18:54:44 |
   +--------+------------+--------+-----------+----------+---------------------+
   514 rows in set (0.27 sec)
   
   mysql> SHOW STATUS LIKE 'last_query_cost';
   +-----------------+---------------+
   | Variable_name   | Value         |
   +-----------------+---------------+
   | Last_query_cost | 102120.464739 |
   +-----------------+---------------+
   1 row in set (0.00 sec)
   ```

3. 再次扩大查询范围，student_id > 199000 的学生记录，大概需要进行 100512 个页的查询。

   ```mysql
   mysql> SELECT * FROM student_info WHERE student_id > 199000;
   +--------+------------+--------+-----------+----------+---------------------+
   | id     | student_id | name   | course_id | class_id | create_time         |
   +--------+------------+--------+-----------+----------+---------------------+
   |     18 |     199656 | CNfphR |     10058 |    10029 | 2023-07-02 18:53:47 |
   |     30 |     199615 | gEEhXr |     10015 |    10058 | 2023-07-02 18:53:47 |
   |     57 |     199193 | uaUups |     10021 |    10105 | 2023-07-02 18:53:47 |
   | 999891 |     199922 | mlvtHf |     10087 |    10184 | 2023-07-02 18:54:44 |
   +--------+------------+--------+-----------+----------+---------------------+
   5023 rows in set (0.19 sec)
   
   mysql> SHOW STATUS LIKE 'last_query_cost';
   +-----------------+---------------+
   | Variable_name   | Value         |
   +-----------------+---------------+
   | Last_query_cost | 100512.649000 |
   +-----------------+---------------+
   1 row in set (0.00 sec)
   ```

>SQL查询是一个动态的过程，从页加载的角度，我们可以得到以下两点结论：
>
>- `位置决定效率`：如果页就在数据库缓冲池中，那么效率是最高的，否则还需要从内存或者磁盘中进行读取，当然针对单个页的读取来说，如果页存在于内存中，会比在磁盘中读取效率高很多。即`数据库缓冲池 > 内存 > 磁盘`。
>- `批量决定效率`：如果我们从磁盘中单一页进行随机读，那么效率是很低的（差不多 10 ms），而采用顺序读取的方式，批量对页进行读取，平均一页的读取效率就会提升很多，甚至要快于单个页面在内存中的随机读取。即`顺序读取 > 随机读取`。
>
>所以说，遇到 I/O 并不用担心，方法找对了，效率还是很高的。我们首先要考虑数据存放的位置，如果是经常使用的数据就要尽量放到缓冲池中，其次我们可以充分利用磁盘的吞吐能力，一次性批量读取数据，这样单个页的读取效率也就得到了提升。
>
>注：缓冲池和查询缓存并不是一个东西。

### 定位执行慢的 SQL：慢查询日志

MySQL 的慢查询日志，用来记录在 MySQL 中`响应时间超过阈值`的语句，具体指运行时间超过`long_query_time`值的 SQL，会被记录到慢查询日志中。long_query_time 的默认值是 10，意思是运行 10 秒以上（不含 10 秒）的语句，认为是超出了最大的忍耐时间值。

它的主要作用是，帮助我们发现那些执行时间特别长的 SQL 查询，并且有针对性的进行优化，从而提高系统的整体效率。当我们的数据库服务器发生阻塞、运行变慢的时候，检查一下慢查询日志，找到那些慢查询，对解决问题很有帮助。

默认情况下，MySQL 数据库`没有开启慢查询日志`，需要手动来设置这个参数。**如果不是调优需要的话，一般不建议启动该参数。**因为开启慢查询日志，会或多或少来带一定的性能影响。

> 慢查询日志，支持将日志记录写入文件。

#### 开启慢查询日志

##### 开启 slow_query_log

查看慢查询日志是否开启，以及日志的位置：

```mysql
mysql> SHOW VARIABLES LIKE '%slow_query_log%';
+---------------------+--------------------------------------+
| Variable_name       | Value                                |
+---------------------+--------------------------------------+
| slow_query_log      | OFF                                  |
| slow_query_log_file | /var/lib/mysql/de5e82a9b92d-slow.log |
+---------------------+--------------------------------------+
2 rows in set (0.00 sec)
```

修改慢查询日志状态为开启，注意这里要加 `global`，因为它是全局系统变量，否则会报错：

```mysql
mysql> SET GLOBAL slow_query_log='ON';
Query OK, 0 rows affected (0.00 sec)
```

再次查看：

```mysql
mysql> SHOW VARIABLES LIKE '%slow_query_log%';
+---------------------+--------------------------------------+
| Variable_name       | Value                                |
+---------------------+--------------------------------------+
| slow_query_log      | ON                                   |
| slow_query_log_file | /var/lib/mysql/de5e82a9b92d-slow.log |
+---------------------+--------------------------------------+
2 rows in set (0.00 sec)
```

##### 修改 long_query_time 阈值

查看慢查询的时间阈值设置：

```mysql
mysql> SHOW VARIABLES LIKE '%long_query_time%';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)
```

按需重新设置，例如设置为 1 秒：

```mysql
# 测试发现设置global的方式对当前session的long_query_time失效，只对新连接的客户端有效，所以可以一并执行下列语句
mysql> SET GLOBAL long_query_time = 1;
Query OK, 0 rows affected (0.00 sec)

mysql> SET long_query_time = 1;
Query OK, 0 rows affected (0.00 sec)
```

##### 补充：配置文件中设置参数

如下的方式相较于前面的命令行方式，可以看作是永久设置。

修改`my.cnf`文件，在 [mysqld] 下增加或修改参数`slow_query_log`，`slow_query_log_file`和`long_query_time`后，然后重启 MySQL 服务器。

```cnf
[mysqld]
slow_query_log=ON # 开启慢查询日志的开关
slow_query_log_file=/var/lib/mysql/slow.log # 慢查询日志的目录和文件名信息
long_query_time=3 # 设置慢查询的阈值为3秒，超出此设定值的SQL即被记录到慢查询日志
log_output=FILE
```

如果不指定存储路径，慢查询日志将默认存储到 MySQL 数据库的数据文件夹下，如果不指定文件名，默认文件名为 hostname-slow.log。

#### 案例演示

第一步，建表：

```mysql
mysql> CREATE TABLE `student` (
    ->     `id` INT(11) NOT NULL AUTO_INCREMENT,
    ->     `stuno` INT NOT NULL ,
    ->     `name` VARCHAR(20) DEFAULT NULL,
    ->     `age` INT(3) DEFAULT NULL,
    ->     `classId` INT(11) DEFAULT NULL,
    ->     PRIMARY KEY (`id`)
    -> ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 4 warnings (0.03 sec)
```

第二步，开启允许创建函数：

```mysql
mysql> SHOW GLOBAL VARIABLES LIKE '%log_bin_trust_function_creators%';
+---------------------------------+-------+
| Variable_name                   | Value |
+---------------------------------+-------+
| log_bin_trust_function_creators | OFF   |
+---------------------------------+-------+
1 row in set (0.01 sec)

mysql> SET GLOBAL log_bin_trust_function_creators='ON';
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW GLOBAL VARIABLES LIKE '%log_bin_trust_function_creators%';
+---------------------------------+-------+
| Variable_name                   | Value |
+---------------------------------+-------+
| log_bin_trust_function_creators | ON    |
+---------------------------------+-------+
1 row in set (0.00 sec)
```

第三步，创建函数：

```mysql
# 随机产生字符串函数rand_string，同上章

# 随机数函数rand_num，同上章
```

第四步，创建存储过程：

```mysql
mysql> DELIMITER //
mysql> CREATE PROCEDURE insert_stu1(  START INT , max_num INT )
    -> BEGIN 
    -> DECLARE i INT DEFAULT 0; 
    -> SET autocommit = 0; # 设置手动提交事务
    -> REPEAT # 循环
    -> SET i = i + 1; # 赋值
    -> INSERT INTO student (stuno, NAME ,age ,classId ) VALUES
    -> ((START+i),rand_string(6),rand_num(10,100),rand_num(10,1000)); 
    -> UNTIL i = max_num 
    -> END REPEAT; 
    -> COMMIT; # 提交事务
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

第五步，调用存储过程：

```mysql
mysql> CALL insert_stu1(100001,4000000);
Query OK, 0 rows affected (4 min 8.07 sec)
```

第六步，验证是否成功：

```mysql
mysql> SELECT COUNT(*) FROM student;
+----------+
| COUNT(*) |
+----------+
|  4000000 |
+----------+
1 row in set (0.21 sec)
```

#### 慢查询演示

执行下面的查询操作，进行慢查询语句的测试：

```mysql
mysql> SELECT * FROM student WHERE stuno = 3455655;
+---------+---------+--------+------+---------+
| id      | stuno   | name   | age  | classId |
+---------+---------+--------+------+---------+
| 3355654 | 3455655 | WOcWyE |   60 |     968 |
+---------+---------+--------+------+---------+
1 row in set (1.21 sec)

mysql> SELECT * FROM student WHERE name = 'ZfCwDz';
+---------+---------+--------+------+---------+
| id      | stuno   | name   | age  | classId |
+---------+---------+--------+------+---------+
|  427220 |  527221 | zfcWDZ |   33 |     311 |
|  781361 |  881362 | ZFcwdZ |   80 |     898 |
|  812704 |  912705 | ZFcwdZ |   81 |     965 |
| 2138264 | 2238265 | zFCWdz |   31 |     727 |
| 2602748 | 2702749 | zFCWdz |   35 |     938 |
| 2763745 | 2863746 | zfcWDZ |   31 |     239 |
| 2978248 | 3078249 | zFCWdz |   30 |     708 |
+---------+---------+--------+------+---------+
7 rows in set (1.26 sec)
```

因为此时慢日志的时间阈值为 1 秒，上面的两条 SQL，都属于慢日志。查看下慢查询的记录：

```mysql
mysql> SHOW STATUS LIKE 'slow_queries';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Slow_queries  | 3     |
+---------------+-------+
1 row in set (0.00 sec)
```

>在 MySQL 中，除了上述`slow_queries`变量，控制慢查询日志的还有另外一个变量`min_examined_row_limit`。这个变量的意思是，查询扫描过的最少记录数。这个变量和查询执行时间，共同组成了判别一个查询是否慢查询的条件。**如果查询扫描过的记录数大于等于这个变量的值，并且查询执行时间超过 long_query_time 的值，那么这个查询就被记录到慢查询日志中。**反之，则不被记录到慢查询日志中。另外，min_examined_row_limit 默认是 0，我们也一般不会去修改它。
>
>```mysql
>mysql> SHOW VARIABLES LIKE 'min%';
>+------------------------+-------+
>| Variable_name          | Value |
>+------------------------+-------+
>| min_examined_row_limit | 0     |
>+------------------------+-------+
>1 row in set (0.00 sec)
>```
>
>当这个值为默认值 0 时，与 long_query_time=10 合在一起，表示只要查询的执行时间超过 10 秒钟，哪怕一个记录也没有扫描过，都要被记录到慢查询日志中。你也可以根据需要，通过修改 "my.ini" 文件，来修改查询时长，或者通过 SET 指令，用 SQL 语句修改 min_examined_row_limit 的值。

#### 慢查询日志分析工具

在生产环境中，如果要手工分析日志，查找、分析 SQL，显然是个体力活，MySQL 提供了日志分析工具`mysqldumpslow`。

>说明：
>
>1. 该工具并不是 MySQL 内置的，不要在 MySQL 下执行，可以直接在根目录或者其他位置执行。
>2. 该工具只有 Linux 下才是开箱可用的，实际上生产中 MySQL 数据库一般也是部署在 Linux 环境中的。如果是 Windows 环境，可以参考 https://www.cnblogs.com/-mrl/p/15770811.html。

通过 mysqldumpslow 可以查看慢查询日志帮助：

```bash
# 创建软连接
xisun@xisun-develop:~/mysql/mysql-8.0.33-linux-glibc2.17-x86_64-minimal/bin$ sudo ln -sf /home/xisun/mysql/mysql-8.0.33-linux-glibc2.17-x86_64-minimal/bin/mysqldumpslow /usr/bin/

# 分析慢日志
xisun@xisun-develop:~/apps$ sudo mysqldumpslow -s t -t 5 mysql/data/de5e82a9b92d-slow.log 

Reading mysql slow query log from mysql/data/de5e82a9b92d-slow.log
Count: 1  Time=248.07s (248s)  Lock=0.00s (0s)  Rows=0.0 (0), root[root]@[127.0.0.1]
  CALL insert_stu1(N,N)

Count: 1  Time=1.26s (1s)  Lock=0.00s (0s)  Rows=7.0 (7), root[root]@[127.0.0.1]
  SELECT * FROM student WHERE name = 'S'

Count: 1  Time=1.21s (1s)  Lock=0.00s (0s)  Rows=1.0 (1), root[root]@[127.0.0.1]
  SELECT * FROM student WHERE stuno = N

Died at /usr/bin/mysqldumpslow line 162, <> chunk 3.

# 添加-a参数，显示SQL中的真实数据，不将具体的数值被N代替，字符串被S代替
xisun@xisun-develop:~/apps$ sudo mysqldumpslow -s t -t 5 -a mysql/data/de5e82a9b92d-slow.log 

Reading mysql slow query log from mysql/data/de5e82a9b92d-slow.log
Count: 1  Time=248.07s (248s)  Lock=0.00s (0s)  Rows=0.0 (0), root[root]@[127.0.0.1]
  CALL insert_stu1(100001,4000000)

Count: 1  Time=1.26s (1s)  Lock=0.00s (0s)  Rows=7.0 (7), root[root]@[127.0.0.1]
  SELECT * FROM student WHERE name = 'ZfCwDz'

Count: 1  Time=1.21s (1s)  Lock=0.00s (0s)  Rows=1.0 (1), root[root]@[127.0.0.1]
  SELECT * FROM student WHERE stuno = 3455655

Died at /usr/bin/mysqldumpslow line 162, <> chunk 3.
```

> Docker 容器中可能没有 mysqldumpslow 命令，此时，可以下载 MySQL 源码，上传到宿主机。MySQL 慢日志路径为 /var/lib/mysql，此路径在启动 Docker 容器时，已经被挂载到宿主机，可以在宿主机上，使用下载的 MySQL 源码中的 mysqldumpslow 命令分析慢日志。
>
> 查看 mysqldumpslow 帮助：
>
> ```bash
> $ mysqldumpslow --help
> Usage: mysqldumpslow [ OPTS... ] [ LOGS... ]
> 
> Parse and summarize the MySQL slow query log. Options are
> 
> --verbose    verbose
> --debug      debug
> --help       write this text to standard output
> 
> -v           verbose
> -d           debug
> -s ORDER     what to sort by (al, at, ar, c, l, r, t), 'at' is default
>        al: average lock time
>        ar: average rows sent
>        at: average query time
>         c: count
>         l: lock time
>         r: rows sent
>         t: query time  
> -r           reverse the sort order (largest last instead of first)
> -t NUM       just show the top n queries
> -a           don't abstract all numbers to N and strings to 'S'
> -n NUM       abstract numbers with at least n digits within names
> -g PATTERN   grep: only consider stmts that include this string
> -h HOSTNAME  hostname of db server for *-slow.log filename (can be wildcard),
>       default is '*', i.e. match all
> -i NAME      name of server instance (if using mysql.server startup script)
> -l           don't subtract lock time from total time
> ```
>
> mysqldumpslow 命令的具体参数如下：
>
> - `-a`：不将数字抽象成 N，字符串抽象成 S。
>
> - `-s`：是表示按照何种方式排序：
>   - c：访问次数
>   - l：锁定时间
>   - r：返回记录
>   - t：查询时间
>   - al：平均锁定时间
>   - ar：平均返回记录数
>   - at：平均查询时间 （默认方式）
>   - ac：平均查询次数
>
> - `-t`：即为返回前面多少条的数据。
>
> - `-g`：后边搭配一个正则匹配模式，大小写不敏感的；
>
> mysqldumpslow 常用查询：
>
> ```bash
> # 得到返回记录集最多的10个SQL
> $ mysqldumpslow -s r -t 10 /var/lib/mysql/xisun-slow.log
> 
> #  得到访问次数最多的10个SQL
> $ mysqldumpslow -s c -t 10 /var/lib/mysql/xisun-slow.log
> 
> # 得到按照时间排序的前10条里面含有左连接的查询语句
> $ mysqldumpslow -s t -t 10 -g "left join" /var/lib/mysql/xisun-slow.log
> 
> # 另外建议在使用这些命令时结合|和more使用，否则有可能出现爆屏情况
> $ mysqldumpslow -s r -t 10 /var/lib/mysql/xisun-slow.log | more
> ```

#### 关闭慢查询日志

##### 临时关闭

```mysql
mysql> SET GLOBAL slow_query_log='OFF';
```

##### 永久关闭

修改 my.cnf 或 my.ini 文件，把 mysqld 组下的 slow_query_log 值设置为 OFF，修改保存后，再重启 MySQL 服务，即可生效。

#### 删除慢查询日志

慢查询日志都是使用`mysqladmin -uroot -p flush-logs slow` 命令来删除重建的。使用时一定要注意，一旦执行了这个命令，慢查询日志都只存在于新的日志文件中，如果需要旧的查询日志，就必须事先备份。或者也可以直接手动删除慢查询日志。

### 查看 SQL 执行成本：SHOW PROFILE

`// TODO`

## 索引优化与查询优化

数据库调优的维度：

- 索引失效、没有充分利用所以 —— **`索引建立`**。
- 关联查询太多 JOIN（设计缺陷或不得已的需求）—— **`SQL 优化`**。
- 服务器调优及各个参数设置（缓冲、 线程数）—— **`调整 my.cnf`**。
- 数据过多 —— **`分库分表`**。

关于数据库调优的知识点非常分散，不同 DBMS，不同的公司，不同的职位，不同的项目遇到的问题都不尽相同。

虽然 SQL 查询优化的技术很多，但是大体方向上完全可以分为`物理查询优化`和`逻辑查询优化`两大块。

- 物理查询优化：通过`索引`和`表连接方式`等技术来进行优化，这里重点需要掌握索引的使用。
- 逻辑查询优化：通过`SQL 等价变换`提升查询效率，直白一点来讲就是，换一种执行效率更高的查询写法。

### 数据准备

创建存储过程：

```mysql
# 创建往stu表中插入数据的存储过程
mysql> DELIMITER //
mysql> CREATE PROCEDURE insert_stu(  START INT , max_num INT )
    -> BEGIN 
    -> DECLARE i INT DEFAULT 0; 
    -> SET autocommit = 0; # 设置手动提交事务
    -> REPEAT # 循环
    -> SET i = i + 1; # 赋值
    -> INSERT INTO student (stuno, name ,age ,classId ) VALUES
    -> ((START+i),rand_string(6),rand_num(1,50),rand_num(1,1000)); 
    -> UNTIL i = max_num 
    -> END REPEAT; 
    -> COMMIT; # 提交事务
    -> END //
Query OK, 0 rows affected (0.02 sec)

mysql> DELIMITER ;

# 创建往class表中插入数据的存储过程
mysql> DELIMITER //
mysql> CREATE PROCEDURE `insert_class`( max_num INT )
    -> BEGIN 
    -> DECLARE i INT DEFAULT 0; 
    -> SET autocommit = 0;  
    -> REPEAT 
    -> SET i = i + 1; 
    -> INSERT INTO class ( classname,address,monitor ) VALUES
    -> (rand_string(8),rand_string(10),rand_num(1,100000)); 
    -> UNTIL i = max_num 
    -> END REPEAT; 
    -> COMMIT;
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

调用存储过程：

```mysql
# 往class表添加1万条数据
mysql> CALL insert_class(10000);
Query OK, 0 rows affected (6.75 sec)

# 往stu表添加80万条数据
mysql> CALL insert_stu(100000, 800000);
Query OK, 0 rows affected (4 min 30.96 sec)
```

查看数据是否添加成功：

```mysql
mysql> SELECT COUNT(*) FROM class;
+----------+
| COUNT(*) |
+----------+
|    10000 |
+----------+
1 row in set (0.00 sec)

mysql> SELECT COUNT(*) FROM student;
+----------+
| COUNT(*) |
+----------+
|   800000 |
+----------+
1 row in set (0.02 sec)
```

创建删除某个表的索引的存储过程，避免手动删除索引：

```mysql
mysql> DELIMITER //
mysql> CREATE  PROCEDURE `proc_drop_index`(dbname VARCHAR(200),tablename VARCHAR(200))
    -> BEGIN
    ->    DECLARE done INT DEFAULT 0;
    ->    DECLARE ct INT DEFAULT 0;
    ->    DECLARE _index VARCHAR(200) DEFAULT '';
    ->    DECLARE _cur CURSOR FOR  SELECT  index_name  FROM
    -> 		information_schema.STATISTICS  WHERE table_schema=dbname AND table_name=tablename AND
    -> 		seq_in_index=1 AND  index_name <>'PRIMARY'; # 每个游标必须使用不同的declare continue handler for not found set done=1来控制游标的结束
    ->    DECLARE  CONTINUE HANDLER FOR NOT FOUND set done=2; # 若没有数据返回，程序继续，并将变量done设为2
    ->     OPEN _cur;
    ->     FETCH _cur INTO _index;
    ->     WHILE _index<>'' DO
    ->        SET @str = CONCAT("drop index ", _index , " on " , tablename );
    ->        PREPARE sql_str FROM @str ;
    ->        EXECUTE sql_str;
    ->        DEALLOCATE PREPARE sql_str;
    ->        SET _index='';
    ->        FETCH _cur INTO _index;
    ->     END WHILE;
    ->  CLOSE _cur;
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

>调用方法：`CALL proc_drop_index("dbname", "tablename");`。

### 索引失效案例

MySQL 中提高性能的一个最有效的方式是对数据表设计合理的索引。索引提供了高校访问数据的方法，并且加快了查询的速度，因此索引对查询的速度有着至关重要的影响。

- 使用索引可以快速的定位表中的某条记录，从而提高数据库查询的速度，提高数据库的性能。
- 如果查询时没有使用索引，查询语句就会扫描表中的所有记录。在数据量大的情况下，这样查询的速度会很慢。

大多数情况下都（默认）采用 B+Tree 来构建索引。只是空间列类型的索引使用 R-Tree，并且 Memory 表还支持 Hash 索引。

其实，用不用索引，最终都是优化器说了算。优化器时基于`cost 开销 (CostBaseOptimizer)`，它不是基于规则（Rule-BasedOptimizer），也不是基于语义。因此，优化器总是选择开销最小的。另外，SQL 语句是否使用索引，跟数据库版本、数据量、数据选择度都有关系。

> cost 开销的单位不是时间。

#### 全值匹配

**`全值匹配时，可以充分的利用组合索引，会匹配符合字段最多的组合索引。`**

如下所示，系统中经常出现的 SQL 语句，当没有建立索引时，`possible_keys`和`key`都为 NULL：

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |    10.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND classId = 4;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |     1.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND classId = 4 AND NAME = 'abcd';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |     0.10 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

>`SQL_NO_CACHE`表示不使用查询缓存。

此时执行 SQL，数据查询速度会比较慢：

```mysql
mysql> SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND classId = 4 AND NAME = 'abcd';
Empty set, 1 warning (0.22 sec)
```

接下来建立索引：

```mysql
mysql> CREATE INDEX idx_age ON student(age);
Query OK, 0 rows affected (3.44 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> CREATE INDEX idx_age_classid ON student(age, classId);
Query OK, 0 rows affected (3.60 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> CREATE INDEX idx_age_classid_name ON student(age, classId, name);
Query OK, 0 rows affected (4.10 sec)
Records: 0  Duplicates: 0  Warnings: 0
```

>上面建立索引是与三条 SQL 的使用场景对应的，遵守了`全值匹配`的规则，就是说建立几个复合索引字段，最好的就是用上这几个字段，且按照顺序来用。

建立索引后执行，发现使用到了联合索引（组合索引字段最多的），且耗时明显减少：

```mysql
# 全值匹配，使用的是组合字段最多的索引idx_age_classid_name
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND classId = 4 AND NAME = 'abcd';
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys                                | key                  | key_len | ref               | rows | filtered | Extra |
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classid,idx_age_classid_name | idx_age_classid_name | 73      | const,const,const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
1 row in set, 2 warnings (0.01 sec)

mysql> SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND classId = 4 AND NAME = 'abcd';
Empty set, 1 warning (0.00 sec)
```

>注意：上面的索引可能不生效，在数据量较大的情况下，进行全值匹配`SELECT *`，优化器可能经过计算发现，我们使用索引查询所有的数据后，还需要对查找到的数据进行回表操作，性能还不如全表扫描。此处因为没有造这么多数据，所以不演示效果。

#### 最左匹配原则

在 MySQL 建立联合索引时会遵守**`最佳左前缀匹配原则`**，即最左优先，在检索数据时从组合索引的最左边开始匹配，如果第一个字段匹配不上，则整个组合索引都不会生效。

- 下面的 SQL 将使用索引 idx_age：

  ```mysql
  mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.name = 'abcd';
  +----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
  | id | select_type | table   | partitions | type | possible_keys                                | key     | key_len | ref   | rows  | filtered | Extra       |
  +----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
  |  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classid,idx_age_classid_name | idx_age | 5       | const | 30518 |    10.00 | Using where |
  +----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
  1 row in set, 2 warnings (0.00 sec)
  ```

- 下面的 SQL 不会使用索引，因为没有创建 classId 或者 name 的索引：

  ```mysql
  mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.classId = 4 AND student.name = 'abcd';
  +----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
  | id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
  +----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
  |  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |     1.00 | Using where |
  +----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
  1 row in set, 2 warnings (0.00 sec)
  ```

- 下面的 SQL 查询就是遵守这一原则的正确打开方式：

  ```mysql
  mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.classId = 4 AND student.name = 'abcd';
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  | id | select_type | table   | partitions | type | possible_keys                                | key                  | key_len | ref               | rows | filtered | Extra |
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  |  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classid,idx_age_classid_name | idx_age_classid_name | 73      | const,const,const |    1 |   100.00 | NULL  |
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  1 row in set, 2 warnings (0.01 sec)
  ```

- 下面的 SQL 也会使用索引，因为**优化器会执行优化，调整查询条件的顺序**，但开发中应保持良好的开发习惯，保持条件中字段的顺序匹配索引的组合顺序： 

  ```mysql
  mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.classId = 4 AND student.age = 30 AND student.name = 'abcd';
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  | id | select_type | table   | partitions | type | possible_keys                                | key                  | key_len | ref               | rows | filtered | Extra |
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  |  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classid,idx_age_classid_name | idx_age_classid_name | 73      | const,const,const |    1 |   100.00 | NULL  |
  +----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
  1 row in set, 2 warnings (0.00 sec)
  ```

- 如果删去索引 idx_age 和 idx_age_classid，只保留 idx_age_classid_name，执行如下 SQL，也会使用索引。

  ```mysql
  mysql> DROP INDEX idx_age ON student;
  Query OK, 0 rows affected (0.02 sec)
  Records: 0  Duplicates: 0  Warnings: 0
  
  mysql> DROP INDEX idx_age_classid ON student;
  Query OK, 0 rows affected (0.02 sec)
  Records: 0  Duplicates: 0  Warnings: 0
  
  # 使用了idx_age_classid_name索引，但是key_len是5，也就是说只使用了age部分的排序，因为age是int类型，4个字节加上null值列表一共5个字节。仔细想想，B+Tree是先按照age排序，再按照classid排序，最后按照name排序，因此不能跳过classId的排序直接就使用name的排序，所以只使用了age的索引
  mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.name = 'abcd';
  +----+-------------+---------+------------+------+----------------------+----------------------+---------+-------+-------+----------+-----------------------+
  | id | select_type | table   | partitions | type | possible_keys        | key                  | key_len | ref   | rows  | filtered | Extra                 |
  +----+-------------+---------+------------+------+----------------------+----------------------+---------+-------+-------+----------+-----------------------+
  |  1 | SIMPLE      | student | NULL       | ref  | idx_age_classid_name | idx_age_classid_name | 5       | const | 31982 |    10.00 | Using index condition |
  +----+-------------+---------+------------+------+----------------------+----------------------+---------+-------+-------+----------+-----------------------+
  1 row in set, 2 warnings (0.00 sec)
  
  ```

综上，MySQL 可以为多个字段创建索引，一个索引可以包括 16 个字段，`对于多列字段，过滤条件要使用索引必须按照索引建立时的顺序，依次满足，一旦跳过某个字段，索引后面的字段都无法使用，如果查询条件中没有使用这些字段中的第一个字段时，多列索引不会被使用。`

#### 主键插入顺序

对于一个使用 InnoDB 存储引擎的表来说，在没有显式的创建索引时，表中的数据实际上都是存储在聚簇索引的叶子节点上的。而记录又是存储在数据页中，数据页和记录又是按照记录主键值从小到大的顺序进行排序，所以如果我们插入的记录的主键是依次增大的话，那我们每插满一个数据页就换到下一个数据页继续插入，而如果我们插入的主键值忽大忽小的话，就比较麻烦。假设某个数据页存储的记录已经满了，它存储的主键值在 1 ~ 100 之间：

<img src="mysql-advanced/image-20231101225500158.png" alt="image-20231101225500158" style="zoom:67%;" />

如果此时再插入一条主键值为 9 的记录，那它插入的位置就如下图：

<img src="mysql-advanced/image-20231101225555374.png" alt="image-20231101225555374" style="zoom:67%;" />

可这个数据页已经满了，再插进来咋办呢？我们需要把`当前页面分裂成两个页面`，把本页中的一些`记录移动`到新创建的这个页中。页面分裂和记录移位意味着什么？意味着：`性能损耗`！所以如果想尽量避免这样无谓的性能损耗，最好让插入的记录的`主键值依次递增`，这样就不会发生这样的性能损耗了。 所以我们建议：`让主键具有 AUTO_INCREMENT，让存储引擎自己为表生成主键，而不是手动插入。`比如 person_info 表：

```mysql
CREATE TABLE person_info(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    phone_number CHAR(11) NOT NULL,
    country varchar(100) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_name_birthday_phone_number (name(10), birthday, phone_number)
);
```

自定义的主键列 id 拥有 AUTO_INCREMENT 属性，在插入记录时存储引擎会自动填入自增的主键值。这样的主键占用空间小，顺序写入，可以减少页分裂。

#### 计算导致索引失效

在 student 表的字段 stuno 上创建索引：

```mysql
mysql> CREATE INDEX idx_sno ON student(stuno);
Query OK, 0 rows affected (2.73 sec)
Records: 0  Duplicates: 0  Warnings: 0
```

**`当有计算条件时，索引失效。`**示例如下：

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE id, stuno, NAME FROM student WHERE stuno = 900001;
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key     | key_len | ref   | rows | filtered | Extra |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_sno       | idx_sno | 4       | const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------+
1 row in set, 2 warnings (0.00 sec)

# 有计算条件，索引失效
mysql> EXPLAIN SELECT SQL_NO_CACHE id, stuno, NAME FROM student WHERE stuno + 1 = 900001; 
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

可以看到如果对索引进行了表达式计算，索引就失效了。这是因为我们需要把索引字段的值都取出来，然后一次进行表达式的计算来进行条件判断，因此采用的就是`全表扫描`的方式，运行时间也会慢很多：

```mysql
mysql> SELECT SQL_NO_CACHE id, stuno, NAME FROM student WHERE stuno = 900001;
Empty set, 1 warning (0.00 sec)

mysql> SELECT SQL_NO_CACHE id, stuno, NAME FROM student WHERE stuno + 1 = 900001; 
+--------+--------+--------+
| id     | stuno  | NAME   |
+--------+--------+--------+
| 800000 | 900000 | skWeFP |
+--------+--------+--------+
1 row in set, 1 warning (0.18 sec)
```

#### 函数导致索引失效

在 student 表的字段 name 上创建索引：

```mysql
mysql> CREATE INDEX idx_name ON student(NAME); 
Query OK, 0 rows affected (3.58 sec)
Records: 0  Duplicates: 0  Warnings: 0
```

**`当有函数条件时，索引失效。`**示例如下

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.name LIKE 'abc%';
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_name      | idx_name | 63      | NULL |   40 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)

# 有函数，索引失效
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE LEFT(student.name, 3) = 'abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

查询效率也慢很多：

```mysql
mysql> SELECT SQL_NO_CACHE * FROM student WHERE student.name LIKE 'abc%';
+--------+--------+--------+------+---------+
| id     | stuno  | name   | age  | classId |
+--------+--------+--------+------+---------+
| 580020 | 680020 | ABcHeA |   14 |     850 |
| 519840 | 619840 | ABcHgL |    9 |     671 |
| 626755 | 726755 | ABcHhL |   11 |     915 |
| 311719 | 411719 | AbCHJl |    9 |     177 |
|  97823 | 197823 | aBCihn |   44 |     540 |
| 385679 | 485679 | AbCIJw |    7 |     364 |
| 160879 | 260879 | ABcIjX |   12 |     256 |
| 371703 | 471703 | aBCijy |   38 |     355 |
| 201505 | 301505 | ABcIkb |   25 |     386 |
| 371290 | 471290 | aBCiky |   39 |     446 |
| 463195 | 563195 | AbCILD |   33 |     608 |
| 472221 | 572221 | AbCJMI |    6 |     586 |
| 605967 | 705967 | AbCJNL |   16 |     430 |
| 289583 | 389583 | abcjNn |   50 |     184 |
| 621037 | 721037 | ABcJor |   42 |     166 |
| 164750 | 264750 | ABcJpw |   13 |     993 |
| 763434 | 863434 | ABcKqB |   33 |     683 |
| 477662 | 577662 | aBCkqY |   48 |     821 |
| 351310 | 451310 | abckQz |    3 |     740 |
| 469852 | 569852 | aBCksi |   37 |     219 |
| 300036 | 400036 | abckSJ |   41 |      64 |
| 277597 | 377597 | aBCktm |    6 |     833 |
| 120380 | 220380 | abckTN |    9 |     565 |
| 336922 | 436922 | AbCKTn |   35 |     649 |
| 625396 | 725396 | AbCKUq |   46 |     578 |
|  10062 | 110062 | ABcLuR |   50 |     431 |
| 340986 | 440986 | aBClus |   28 |     698 |
| 600606 | 700606 | ABcLuT |    9 |     208 |
| 275797 | 375797 | ABcLvT |   10 |     283 |
| 377409 | 477409 | AbCLVu |   12 |      19 |
| 440071 | 540071 | abclVU |   36 |     882 |
|  87638 | 187638 | ABcLvV |   18 |     969 |
| 175531 | 275531 | abclVV |   42 |     372 |
| 321836 | 421836 | ABcLvV |   19 |      90 |
|   2012 | 102012 | AbCLWB |   43 |     618 |
| 400060 | 500060 | abclWY |    4 |     431 |
| 528467 | 628467 | aBClxC |   22 |     528 |
| 622236 | 722236 | abclXc |   22 |      21 |
|  37117 | 137117 | AbCLXF |    9 |      28 |
|  89616 | 189616 | abclXf |   35 |     118 |
+--------+--------+--------+------+---------+
40 rows in set, 1 warning (0.00 sec)

mysql> SELECT SQL_NO_CACHE * FROM student WHERE LEFT(student.name, 3) = 'abc';
+--------+--------+--------+------+---------+
| id     | stuno  | name   | age  | classId |
+--------+--------+--------+------+---------+
|   2012 | 102012 | AbCLWB |   43 |     618 |
|  10062 | 110062 | ABcLuR |   50 |     431 |
|  37117 | 137117 | AbCLXF |    9 |      28 |
|  87638 | 187638 | ABcLvV |   18 |     969 |
|  89616 | 189616 | abclXf |   35 |     118 |
|  97823 | 197823 | aBCihn |   44 |     540 |
| 120380 | 220380 | abckTN |    9 |     565 |
| 160879 | 260879 | ABcIjX |   12 |     256 |
| 164750 | 264750 | ABcJpw |   13 |     993 |
| 175531 | 275531 | abclVV |   42 |     372 |
| 201505 | 301505 | ABcIkb |   25 |     386 |
| 275797 | 375797 | ABcLvT |   10 |     283 |
| 277597 | 377597 | aBCktm |    6 |     833 |
| 289583 | 389583 | abcjNn |   50 |     184 |
| 300036 | 400036 | abckSJ |   41 |      64 |
| 311719 | 411719 | AbCHJl |    9 |     177 |
| 321836 | 421836 | ABcLvV |   19 |      90 |
| 336922 | 436922 | AbCKTn |   35 |     649 |
| 340986 | 440986 | aBClus |   28 |     698 |
| 351310 | 451310 | abckQz |    3 |     740 |
| 371290 | 471290 | aBCiky |   39 |     446 |
| 371703 | 471703 | aBCijy |   38 |     355 |
| 377409 | 477409 | AbCLVu |   12 |      19 |
| 385679 | 485679 | AbCIJw |    7 |     364 |
| 400060 | 500060 | abclWY |    4 |     431 |
| 440071 | 540071 | abclVU |   36 |     882 |
| 463195 | 563195 | AbCILD |   33 |     608 |
| 469852 | 569852 | aBCksi |   37 |     219 |
| 472221 | 572221 | AbCJMI |    6 |     586 |
| 477662 | 577662 | aBCkqY |   48 |     821 |
| 519840 | 619840 | ABcHgL |    9 |     671 |
| 528467 | 628467 | aBClxC |   22 |     528 |
| 580020 | 680020 | ABcHeA |   14 |     850 |
| 600606 | 700606 | ABcLuT |    9 |     208 |
| 605967 | 705967 | AbCJNL |   16 |     430 |
| 621037 | 721037 | ABcJor |   42 |     166 |
| 622236 | 722236 | abclXc |   22 |      21 |
| 625396 | 725396 | AbCKUq |   46 |     578 |
| 626755 | 726755 | ABcHhL |   11 |     915 |
| 763434 | 863434 | ABcKqB |   33 |     683 |
+--------+--------+--------+------+---------+
40 rows in set, 1 warning (0.23 sec)
```

#### 类型转换（自动或手动）导致索引失效

**`当有类型转换 (自动或手动) 条件时，索引失效。`**示例如下：

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE name = '123';
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key      | key_len | ref   | rows | filtered | Extra |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_name      | idx_name | 63      | const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
1 row in set, 2 warnings (0.00 sec)

# 有类型转换，索引失效
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE name = 123;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 742203 |    10.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 5 warnings (0.00 sec)
```

> `name = 123`发生了类型转换，相当于使用了隐形函数，索引失效。
>
> **设计实体类属性时，一定要与数据库字段类型相对应。否则，就会出现类型转换的情况，进而导致索引失效。**

#### 范围条件右边的列索引失效

先删除 student 表上的所有索引，再重新创建索引：

```mysql
# 调用存储过程删除所有索引
mysql> CALL proc_drop_index('atguigudb1','student');
Query OK, 0 rows affected (0.06 sec)

# 验证是否删除成功
mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='student' AND seq_in_index=1 AND index_name <>'PRIMARY';
Empty set (0.00 sec)

# 重新创建组合索引
mysql> CREATE INDEX idx_age_classId_name ON student(age, classId, name);
Query OK, 0 rows affected (4.67 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 验证是否创建成功
mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='student' AND seq_in_index=1 AND index_name <>'PRIMARY';
+----------------------+
| INDEX_NAME           |
+----------------------+
| idx_age_classId_name |
+----------------------+
1 row in set (0.01 sec)
```

**`对于有范围条件的，如 <、<=、>、>= 和 between 等，范围右边的列不能使用索引。`**示例如下：

```mysql
#  key_len为10，只用到了组合索引的前两个字段
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.classId > 20 AND student.name = 'abc';
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys        | key                  | key_len | ref  | rows  | filtered | Extra                 |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_classId_name | idx_age_classId_name | 10      | NULL | 32002 |    10.00 | Using index condition |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)
```

即使是改变查询条件的顺序，依然不能使用全部索引。因为优化器会自动满足最左前缀原则 ，即优化器会`先根据组合索引进行排序`，组合索引的顺序决定了哪些列不能正常使用索引。

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.name ='abc' AND student.classId > 20;
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys        | key                  | key_len | ref  | rows  | filtered | Extra                 |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_classId_name | idx_age_classId_name | 10      | NULL | 32002 |    10.00 | Using index condition |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)
```

> 扩展：为什么范围查询会导致索引失效？
>
> **因为根据范围查找筛选后的数据，无法保证范围查找后面的字段是有序的。**例如：a_b_c 这个索引，根据 b 范围查找 > 2 的，在满足 b > 2 的情况下，如 b 为 "3, 4"，c 可能是 "5, 3"，因为 c 是无序的，那么 c 的索引也便失效了。

针对上述情况，可以建立如下索引（范围字段放在最后）进行改进：

```mysql
# 将classId放到组合索引最后
mysql> CREATE INDEX idx_age_name_classId ON student(age, name, classId);
Query OK, 0 rows affected (4.84 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 同一个查询，使用到了全部的组合索引字段
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age = 30 AND student.name ='abc' AND student.classId > 20;
+----+-------------+---------+------------+-------+-------------------------------------------+----------------------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys                             | key                  | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+-------------------------------------------+----------------------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_classId_name,idx_age_name_classId | idx_age_name_classId | 73      | NULL |    1 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+-------------------------------------------+----------------------+---------+------+------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)
```

在应用开发中范围查询，例如：金额查询，日期查询往往都是范围查询。此时，**应将查询条件放置 WHERE 语句最后，在创建的组合索引中，也需要把范围涉及到的字段写在最后。**

#### 不等于（!= 或者 <>）索引失效

**`对于有不等于 (!= 或者 <>) 判断的，索引失效。`**示例如下：

```mysql
mysql> CREATE INDEX idx_name ON student(NAME);
Query OK, 0 rows affected (3.80 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.name <> 'abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 742203 |    50.16 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.name != 'abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 742203 |    50.16 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

#### IS NULL 可以使用索引，IS NOT NULL 无法使用索引

示例如下：

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE name IS NULL;
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-----------------------+
| id | select_type | table   | partitions | type | possible_keys | key      | key_len | ref   | rows | filtered | Extra                 |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_name      | idx_name | 63      | const |    1 |   100.00 | Using index condition |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)

# IS NOT NULL，索引失效
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE name IS NOT NULL;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 742203 |    50.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

因此，最好在设计数据库的时候就将 `字段设置 NOT NULL 约束`。比如可以将 INT 类型的字段，默认设置为 0，将字符串类型的字段，默认设置为空字符串（""）。

> 扩展：同理，在查询中使用`NOT LIKE`也无法使用索引，会导致全表扫描。

#### LIKE 以通配符 % 开头索引失效

**`在使用 LIKE 关键字时，如果匹配字符串的第一个字符为 "%"，索引失效。`**只有 "%" 不在第一个位置，索引才会起作用。

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE NAME LIKE 'ab%';
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_name      | idx_name | 63      | NULL | 1201 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
1 row in set, 2 warnings (0.00 sec)

# %放在首位，索引失效
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE NAME LIKE '%ab%';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |    11.11 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

>拓展：Alibaba Java 开发手册。
>
>**【强制】页面搜索严禁左模糊或者全模糊，如果需要请走搜索引擎来解决。**

#### OR 前后存在非索引的列，索引失效

在 WHERE 子句中，如果在 OR 前的条件列进行了索引，而在 OR 后的条件列没有进行索引，那么索引会失效。也就是说，**`OR 前后的两个条件中的列都是索引时，查询中才使用索引。`**因为 OR 的含义就是两个只要满足一个即可，因此只有一个条件列进行了索引是没有意义的，只要有条件列没有进行索引，就会进行全表扫描，因此索引的条件列也会失效。（OR 前后一个使用索引，一个进行全表扫描，合起来还没有直接进行全表扫描更快。）

示例如下：

```mysql
#  创建索引，此时只有OR前面的字段有索引
mysql> CREATE INDEX idx_age ON student(age);
Query OK, 0 rows affected (3.28 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 未使用索引
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 10 OR classid = 100;
+----+-------------+---------+------------+------+---------------------------------------------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys                                     | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------------------------------------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_age_classId_name,idx_age_name_classId,idx_age | NULL | NULL    | NULL | 742203 |    11.88 | Using where |
+----+-------------+---------+------------+------+---------------------------------------------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

# 再为OR后面的字段创建一个索引
mysql> CREATE INDEX idx_cid ON student(classid);
Query OK, 0 rows affected (3.09 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 使用了索引
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 10 OR classid = 100;
+----+-------------+---------+------------+-------------+-----------------------------------------------------------+-----------------+---------+------+-------+----------+-------------------------------------------+
| id | select_type | table   | partitions | type        | possible_keys                                             | key             | key_len | ref  | rows  | filtered | Extra                                     |
+----+-------------+---------+------------+-------------+-----------------------------------------------------------+-----------------+---------+------+-------+----------+-------------------------------------------+
|  1 | SIMPLE      | student | NULL       | index_merge | idx_age_classId_name,idx_age_name_classId,idx_age,idx_cid | idx_age,idx_cid | 5,5     | NULL | 30807 |   100.00 | Using union(idx_age,idx_cid); Using where |
+----+-------------+---------+------------+-------------+-----------------------------------------------------------+-----------------+---------+------+-------+----------+-------------------------------------------+
1 row in set, 2 warnings (0.00 sec)
```

#### 数据库和表的字符集统一使用 utf8mb4/utf8mb3

统一使用 utf8mb4（5.5.3 版本以上支持）兼容性更好，统一字符集可以避免由于字符集转换产生的乱码。**`不同的字符集进行比较前，需要进行转换，会造成索引失效。`**

#### 总结

一般性建议：

- 对于单列索引，尽量选择针对当前 QUERY 过滤性更好的索引。
- 在选择组合索引的时候，当前 QUERY 中过滤性最好的字段在索引字段顺序中，位置越靠前越好。
- 在选择组合索引的时候，尽量选择能够包含当前 QUERY 中的 WHERE 子句中更多字段的索引。
- 在选择组合索引的时候，如果某个字段可能出现范围查询时，尽量把这个字段放在索引次序的最后面。

总之，书写 SQL 语句时，尽量避免造成索引失效的情况。

### 关联查询优化

#### 数据准备

创建 Type 表：

```mysql
mysql> CREATE TABLE IF NOT EXISTS `type` (
    -> `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    -> `card` INT(10) UNSIGNED NOT NULL,
    -> PRIMARY KEY (`id`)
    -> );
Query OK, 0 rows affected, 2 warnings (0.02 sec)
```

创建 Book 表：

```mysql
mysql> CREATE TABLE IF NOT EXISTS `book` (
    -> `bookid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    -> `card` INT(10) UNSIGNED NOT NULL,
    -> PRIMARY KEY (`bookid`)
    -> );
Query OK, 0 rows affected, 2 warnings (0.05 sec)
```

插入数据：

```mysql
# type表插入20条数据，以下SQL执行20次
mysql> INSERT INTO type(card) VALUES(FLOOR(1 + RAND() * 20));
Query OK, 1 row affected (0.01 sec)

# book表插入20条数据，以下SQL执行20次
mysql> INSERT INTO book(card) VALUES(FLOOR(1 + RAND() * 20));
Query OK, 1 row affected (0.00 sec)

mysql> SELECT COUNT(*) FROM type;
+----------+
| COUNT(*) |
+----------+
|       20 |
+----------+
1 row in set (0.01 sec)

mysql> SELECT COUNT(*) FROM book;
+----------+
| COUNT(*) |
+----------+
|       20 |
+----------+
1 row in set (0.01 sec)
```

#### 采用左外连接

多表查询分为外连接和内连接，而外连接又分为左外连接，右外连接和满外连接。其中外连接中，左外连接与右外连接可以通过交换表来相互改造，其原理也是类似的，而满外连接无非是二者的一个综合，因此外连接只介绍左外连接的优化即可。

首先，当没有使用索引时，进行 EXPLAIN 分析，可以看到是全表扫描：

```mysql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type LEFT JOIN book ON type.card = book.card;
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra                                      |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
|  1 | SIMPLE      | type  | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   20 |   100.00 | NULL                                       |
|  1 | SIMPLE      | book  | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   20 |   100.00 | Using where; Using join buffer (hash join) |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
2 rows in set, 2 warnings (0.00 sec)
```

- 在上面的查询 SQL 中，type 表是驱动表，book 表是被驱动表。在执行查询时，会先查找驱动表中符合条件的数据，再根据驱动表查询到的数据，在被驱动表中根据匹配条件查找对应的数据。因此被驱动表嵌套查询的次数是 20 * 20 = 400 次。实际上，由于总是需要在被驱动表中进行查询，优化器帮我们已经做了优化，上面的查询结果中可以看到，使用了`join buffer`，将数据缓存起来，提高检索的速度。

然后，为了提高外连接的性能，添加以下索引：

```mysql
# book表card字段添加索引
mysql> CREATE INDEX Y ON book(card);
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type LEFT JOIN book ON type.card = book.card;
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref                  | rows | filtered | Extra       |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
|  1 | SIMPLE      | type  | NULL       | ALL  | NULL          | NULL | NULL    | NULL                 |   20 |   100.00 | NULL        |
|  1 | SIMPLE      | book  | NULL       | ref  | Y             | Y    | 4       | atguigudb1.type.card |    1 |   100.00 | Using index |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
2 rows in set, 2 warnings (0.00 sec)
```

- 对于外层表来说，虽然其查询仍然是全表扫描，但是因为是左外连接，LEFT JOIN 左边的表的数据无论是否满足条件都会保留，因此全表扫描也是可以的。另外可以看到第二行的 type 变为了 ref，rows 也变成了 1，优化比较明显。这是由左连接特性决定的。**`LEFT JOIN 条件用于确定如何从右表搜索行，左边一定都有，所以右边是关键点，一定需要建立索引。`**

当然也可以给 type 表建立索引：

```mysql
# type表card字段添加索引
mysql> CREATE INDEX X ON type(card);
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 虽然type表card字段建立了索引，但是无法避免全表扫描
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type LEFT JOIN book ON type.card = book.card;
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
| id | select_type | table | partitions | type  | possible_keys | key  | key_len | ref                  | rows | filtered | Extra       |
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
|  1 | SIMPLE      | type  | NULL       | index | NULL          | X    | 4       | NULL                 |   20 |   100.00 | Using index |
|  1 | SIMPLE      | book  | NULL       | ref   | Y             | Y    | 4       | atguigudb1.type.card |    1 |   100.00 | Using index |
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
2 rows in set, 2 warnings (0.00 sec)
```

> 注意：**`外连接的关联条件中，两个关联字段的类型、字符集一定要保持一致，否则索引会失效。`**

删除索引 Y，继续查询：

```mysql
# 删除索引
mysql> DROP INDEX Y ON book;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type LEFT JOIN book ON type.card = book.card;
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+--------------------------------------------+
| id | select_type | table | partitions | type  | possible_keys | key  | key_len | ref  | rows | filtered | Extra                                      |
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+--------------------------------------------+
|  1 | SIMPLE      | type  | NULL       | index | NULL          | X    | 4       | NULL |   20 |   100.00 | Using index                                |
|  1 | SIMPLE      | book  | NULL       | ALL   | NULL          | NULL | NULL    | NULL |   20 |   100.00 | Using where; Using join buffer (hash join) |
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+--------------------------------------------+
2 rows in set, 2 warnings (0.00 sec)
```

- book 表使用了 join buffer，再次验证了左外连接左边的表是驱动表，右边的表是被驱动表，后面将与内连接在这一点进行对比。

>**左外链接左表是驱动表右表是被驱动表，右外链接和此相反，内链接则是按照数据量的大小，数据量少的是驱动表，多的是被驱动表。**

#### 采用内连接

删除现有的索引，换成 INNER JOIN（MySQL 会自动选择驱动表）：

```mysql
mysql> DROP INDEX X ON type;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> DROP INDEX Y ON book;
ERROR 1091 (42000): Can't DROP 'Y'; check that column/key exists

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type INNER JOIN book ON type.card = book.card;
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra                                      |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
|  1 | SIMPLE      | type  | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   20 |   100.00 | NULL                                       |
|  1 | SIMPLE      | book  | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   20 |    10.00 | Using where; Using join buffer (hash join) |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+--------------------------------------------+
2 rows in set, 2 warnings (0.00 sec)
```

为 book 表添加索引：

```mysql
mysql> ALTER TABLE book ADD INDEX Y (card);
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 此时，book表使用了索引，type表没有索引，type表为驱动表，book表为被驱动表
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type INNER JOIN book ON type.card = book.card;
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref                  | rows | filtered | Extra       |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
|  1 | SIMPLE      | type  | NULL       | ALL  | NULL          | NULL | NULL    | NULL                 |   20 |   100.00 | NULL        |
|  1 | SIMPLE      | book  | NULL       | ref  | Y             | Y    | 4       | atguigudb1.type.card |    1 |   100.00 | Using index |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
2 rows in set, 2 warnings (0.00 sec)
```

向 type 表中再增加 20 条数据，观察情况：

```mysql
mysql> INSERT INTO type(card) VALUES(FLOOR(1 + RAND() * 20));
Query OK, 1 row affected (0.02 sec)

mysql> SELECT COUNT(*) FROM type;
+----------+
| COUNT(*) |
+----------+
|       40 |
+----------+
1 row in set (0.00 sec)

mysql> SELECT COUNT(*) FROM book;
+----------+
| COUNT(*) |
+----------+
|       20 |
+----------+
1 row in set (0.00 sec)

# 此时，虽然book表数据少于type表，但是因为type表有索引，type表为驱动表，book表仍然为被驱动表
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type INNER JOIN book ON type.card = book.card;
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref                  | rows | filtered | Extra       |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
|  1 | SIMPLE      | type  | NULL       | ALL  | NULL          | NULL | NULL    | NULL                 |   40 |   100.00 | NULL        |
|  1 | SIMPLE      | book  | NULL       | ref  | Y             | Y    | 4       | atguigudb1.type.card |    1 |   100.00 | Using index |
+----+-------------+-------+------------+------+---------------+------+---------+----------------------+------+----------+-------------+
2 rows in set, 2 warnings (0.00 sec)
```

为 type 表增加索引：

```mysql
mysql> ALTER TABLE type ADD INDEX X (card);
Query OK, 0 rows affected (0.04 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 此时，type表和book表都有索引，因为book表的数据少于type表，所以book表为驱动表，type表为被驱动表
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM type INNER JOIN book ON type.card = book.card;
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
| id | select_type | table | partitions | type  | possible_keys | key  | key_len | ref                  | rows | filtered | Extra       |
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
|  1 | SIMPLE      | book  | NULL       | index | Y             | Y    | 4       | NULL                 |   20 |   100.00 | Using index |
|  1 | SIMPLE      | type  | NULL       | ref   | X             | X    | 4       | atguigudb1.book.card |    2 |   100.00 | Using index |
+----+-------------+-------+------------+-------+---------------+------+---------+----------------------+------+----------+-------------+
2 rows in set, 2 warnings (0.00 sec)
```

**对于内连接，查询优化器可以决定谁作为驱动表，谁作为被驱动表。`当都没有索引时，表数据量少的是驱动表，多的是被驱动表；当只有一方有索引时，没有索引的表是驱动表，有索引的表是被驱动表 (因为被驱动表查询次数更多)；当都有索引时，则是表数据量少的是驱动表，多的是被驱动表 (小表驱动大表)。`**

>上面的规律不是一成不变的，如果一个表有索引，但是数据量很小，一个表没有索引，但是数据量很大，情况会是怎样的呢？我们要明白`优化器的优化原理：对于内连接，MySQL 会选择扫描次数比较少的作为驱动表，因此实际生产中最好使用 EXPLAIN 测试验证。`

#### JOIN 语句原理

JOIN 方式连接多表，本质就是各个表之间数据的循环匹配。MySQL 5.5 版本之前，MySQL 只支持一种表间关联方式，就是`嵌套循环 (Nested Loop)`。如果关联表的数据量很大，则 JOIN 关联的执行时间会非常漫长。在 MySQL 5.5 以后的版本中，MySQL 通过引入 BNLJ 算法来优化嵌套执行。

##### 驱动表和被驱动表

`驱动表就是主表，被驱动表就是从表、非驱动表。`

对于内连接：

```mysql
SELECT * FROM A JOIN B ON ...
```

- A 不一定是驱动表，优化器会根据查询语句做优化，决定先查哪张表。**先查询的哪张表就是驱动表，反之就是被驱动表。**通过 EXPLAIN 关键字可以查看。

对于外连接：

```mysql
SELECT * FROM A LEFT JOIN B ON ...
# 或
SELECT * FROM B RIGHT JOIN A ON ...
```

- 通常，大家会认为 A 就是驱动表，B 就是被驱动表，但也未必。测试如下：

  ```mysql
  mysql> CREATE TABLE a(f1 INT,f2 INT,INDEX(f1)) ENGINE=INNODB;
  Query OK, 0 rows affected (0.05 sec)
  
  mysql> CREATE TABLE b(f1 INT,f2 INT) ENGINE=INNODB;
  Query OK, 0 rows affected (0.06 sec)
  
  mysql> INSERT INTO a values(1,1),(2,2),(3,3),(4,4),(5,5),(6,6);
  Query OK, 6 rows affected (0.01 sec)
  Records: 6  Duplicates: 0  Warnings: 0
  
  mysql> INSERT INTO b values(3,3),(4,4),(5,5),(6,6),(7,7),(8,8);
  Query OK, 6 rows affected (0.00 sec)
  Records: 6  Duplicates: 0  Warnings: 0
  
  # 测试
  mysql> EXPLAIN SELECT * FROM a LEFT JOIN b ON a.f1 = b.f1 WHERE a.f2 = b.f2;
  +----+-------------+-------+------------+------+---------------+------+---------+-----------------+------+----------+-------------+
  | id | select_type | table | partitions | type | possible_keys | key  | key_len | ref             | rows | filtered | Extra       |
  +----+-------------+-------+------------+------+---------------+------+---------+-----------------+------+----------+-------------+
  |  1 | SIMPLE      | b     | NULL       | ALL  | NULL          | NULL | NULL    | NULL            |    6 |   100.00 | Using where |
  |  1 | SIMPLE      | a     | NULL       | ref  | f1            | f1   | 5       | atguigudb1.b.f1 |    1 |    16.67 | Using where |
  +----+-------------+-------+------------+------+---------------+------+---------+-----------------+------+----------+-------------+
  2 rows in set, 1 warning (0.00 sec)
  ```

- 虽然 SQL 语句是 a LEFT JOIN b，但实际执行时，b 是驱动表，a 是被驱动表。这是因为`查询优化器会把外连接改造为内连接，然后根据其优化策略选择驱动表与被驱动表。`

##### Simple Nested-Loop Join（简单嵌套循环连接）

算法相当简单，从表 A 取出一条数据 1，然后遍历表 B，将匹配到的数据放到 result。以此类推，驱动表 A 中的每一条记录，都会与被动驱动表 B 的全部记录进行判断：

<img src="mysql-advanced/image-20231102153903260.png" alt="image-20231102153903260" style="zoom:80%;" />

可以看到这种方式效率是非常低的，假设以上述表 A 数据 100 条，表 B 数据 1000 条，则 A * B = 10 万次。开销统计如下：

| 开销统计         | SNLJ                      |
| ---------------- | ------------------------- |
| 外表扫描次数     | 1                         |
| 内表扫描次数     | A                         |
| 读取记录数       | A + B * A                 |
| JOIN 比较次数    | B * A                     |
| 回表读取记录次数 | 0（没有索引，不涉及回表） |

当然，MySQL 肯定不会这么粗暴的进行表的连接，所以就出现了后面的两种优化算法。另外，从读取记录数来看：A + B * A中，驱动表 A 对性能的影响权重更大，因此，优化器会选择小表驱动大表。

##### Index Nested-Loop Join（索引嵌套循环连接）

Index Nested-Loop Join，其优化的思路主要是为了`减少内层表数据的匹配次数`，所以`要求被驱动表上必须有索引`才行。**通过外层表匹配条件直接与内层索引进行匹配，避免和内层表的每条记录进行比较，这样极大地减少了对内层表的匹配次数。**

<img src="mysql-advanced/image-20231102155458108.png" alt="image-20231102155458108" style="zoom: 67%;" />

驱动表中的每条记录通过被驱动表的索引进行访问，因为索引查询的成本是比较固定的，故 MySQL 优化器都倾向于使用记录数少的表作为驱动表（外表）。

| 开销统计         | SNLJ      | INLJ                    |
| ---------------- | --------- | ----------------------- |
| 外表扫描次数     | 1         | 1                       |
| 内表扫描次数     | A         | 0                       |
| 读取记录数       | A + B * A | A + B(match)            |
| JOIN 比较次数    | B * A     | A * Index(Height)       |
| 回表读取记录次数 | 0         | B(match)（if possible） |

如果被驱动表加索引，效率是非常高的，如果索引不是主键索引，还需要进行一次回表查询。相比之下，如果被驱动表的索引是主键索引，效率会更高。

##### Block Nested-Loop Join（快嵌套循环连接）

如果存在索引，那么会使用 INDEX 的方式进行 JOIN，如果 JOIN 的列没有索引，则被驱动表要扫描的次数太多了。因为每次访问被驱动表，其表中的记录都会被加载到内存中，然后再从驱动表中取一条与其匹配，匹配结束后清除内存，然后再从驱动表中加载一条记录，然后把驱动表的记录再加载到内存匹配，这样周而复始，大大增加了 I/O 次数。为了减少被驱动表的 I/O 次数，就出现了`Block Nested-Loop Join`的方式。

<img src="mysql-advanced/image-20231102224359471.png" alt="image-20231102224359471" style="zoom:67%;" />

**BNLJ 不再是逐条获取驱动表的数据，而是一块一块的获取，并引入了 join buffer 缓冲区，将驱动表 JOIN 相关的部分数据列（每一批次的大小受 join buffer 的限制）缓存到 join buffer 中，然后全表扫描被驱动表，被驱动表的每一条记录一次性和 join buffer 中的所有驱动表记录进行匹配（内存中操作），将简单嵌套循环中的多次比较合并成一次，降低了被驱动表的访问频率。**

> 注意：**这里缓存的不只是关联表的列，SELECT 后面的列也会缓存起来，在一个有 N 个JOIN 关联的 SQL 中会分配 N - 1 个 join buffer。所以查询的时候尽量减少不必要的字段，这样可以让 join buffer 中存放更多的列。**

| 开销统计         | SNLJ      | INLJ                    | BNLJ                                              |
| ---------------- | --------- | ----------------------- | ------------------------------------------------- |
| 外表扫描次数     | 1         | 1                       | 1                                                 |
| 内表扫描次数     | A         | 0                       | A * used_column_size / join_buffer_size + 1       |
| 读取记录数       | A + B * A | A + B(match)            | A + B * (A * used_column_size / join_buffer_size) |
| JOIN 比较次数    | B * A     | A * Index(Height)       | B * A                                             |
| 回表读取记录次数 | 0         | B(match)（if possible） | 0                                                 |

参数设置：

- `block_nested_loop`：通过`SHOW VARIABLES LIKE '%optimizer_switch%'`查看 block_nested_loop 状态，默认是开启的。

  ```mysql
  mysql> SHOW VARIABLES LIKE '%optimizer_switch%';
  +------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
  | Variable_name    | Value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
  +------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
  | optimizer_switch | index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on,engine_condition_pushdown=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=on,block_nested_loop=on,batched_key_access=off,materialization=on,semijoin=on,loosescan=on,firstmatch=on,duplicateweedout=on,subquery_materialization_cost_based=on,use_index_extensions=on,condition_fanout_filter=on,derived_merge=on,use_invisible_indexes=off,skip_scan=on,hash_join=on,subquery_to_derived=off,prefer_ordering_index=on,hypergraph_optimizer=off,derived_condition_pushdown=on |
  +------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
  1 row in set (0.01 sec)
  ```

- `join_buffer_size`：驱动表能不能一次加载完，要看 join buffer 能不能存储所有的数据，`默认情况下 join_buffer_size = 256 KB`。join buffer size 的最大值在 32 位系统可以申请 4 GB，而在 64 位操做系统下可以申请大于 4 GB 的 join_buffer空间（64 位 Windows 除外，其大值会被截断为 4 GB，并发出警告）。

##### 总结

- 保证被驱动表的 JOIN 字段已经创建了索引（减少内层表的循环匹配次数）。
- 需要 JOIN 的字段，数据类型保持绝对一致。
- LEFT JOIN 时，选择小表作为驱动表， 大表作为被驱动表，减少外层循环的次数。
- INNER JOIN 时，MySQL 会自动将小结果集的表选为驱动表，选择相信 MySQL 优化策略。
- 能够直接多表关联的尽量直接关联，不用子查询。（减少查询的趟数）
- 不建议使用子查询，建议将子查询 SQL 拆开，并结合程序多次查询，或使用 JOIN 来代替子查询。
- 衍生表建不了索引。
- 默认效率比较：INLJ > BNLJ > SNLJ。
- 正确理解小表驱动大表：大小不是指表中的记录数，而是永远用小结果集驱动大结果集（其本质就是减少外层循环的数据数量）。 比如 A 表有 100 条记录，B 表有 1000 条记录，但是 WHERE 条件过滤后，B 表结果集只留下 50 个记录，A 表结果集有 80 条记录，此时就可能是 B 表驱动 A 表。其实上面的例子还是不够准确，因为结果集的大小也不能粗略的用结果集的行数表示，而是表行数 * 每行大小。其实要理解这一点，只需要结合 join buffer 就好了，因为表行数 * 每行大小越小，其占用内存越小，就可以在 join buffer 中尽量少的次数加载完了。

##### Hash Join

`从 MySQL 8.0.20 版本开始，将废弃 BNLJ，因为加入了 Hash Join，默认都会使用 Hash Join。`

Nested Loop 与 Hash Join 对比如下：

| 类型     | Nested Loop                                                  | Hash Join                                                    |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 使用条件 | 任何条件                                                     | 等值连接（=）                                                |
| 相关资源 | CPU、磁盘 I/O                                                | 内存、临时空间                                               |
| 特点     | 当有高选择性索引或进行限制性搜索时效率比较高，能够快速返回第一次的搜索结果 | 当缺乏索引或者索引条件模糊时，Hash Join 比 Nested Loop 有效。在数据仓库环境下，如果表的记录数多，效率高 |
| 缺点     | 当索引丢失或者查询条件限制不够时，效率很低；当表的记录数较多，效率低 | 为建立哈希表，需要大量内存。第一次的结果返回较慢             |

- Nested Loop：对于被连接的数据子集较小的情况，Nested Loop 是个较好的选择。
- Hash Join 是做`大数据集连接`时的常用方法，优化器使用两个表中较小（相对较小）的表利用 join key 在内存中建立`散列表`，然后扫描较大的表并探测散列表，找出与 Hash 表匹配的行。
  - 这种方式适用于较小的表完全可以放于内存中的情况，这样总成本就是访问两个表的成本之和。
  - 在表很大的情况下并不能完全放入内存，这时优化器会将它分割成若干不同的分区，不能放入内存的部分就把该分区写入磁盘的临时段，此时要求有较大的临时段，从而尽量提高 I/O 的性能。
  - 它能够很好的工作于没有索引的大表和并行查询的环境中，并提供最好的性能。大多数人都说它是 Join 的重型升降机。Hash Join 只能应用于等值连接（如 WHERE A.COL1 = B.COL2），这是由 Hash 的特点决定的。

### 子查询优化

MySQL 从 4.1 版本开始支持子查询，使用子查询可以进行 SELECT 语句的嵌套查询，即一个 SELECT 查询的结果作为另一个 SELECT 语句的条件，子查询可以一次性完成很多逻辑上需要多个步骤才能完成的操作。

子查询是 MySQL 的一项重要的功能，可以帮助我们通过一个 SQL 语句实现比较复杂的查询。但是，`子查询的执行效率不高，通常可以将其优化成一个连接查询。`

原因：

- 执行子查询时，MySQL 需要为内层查询语句的查询结果建立一个`临时表`，然后外层查询语句从临时表中查询记录。查询完毕后，再撤销这些临时表。这样会消耗过多的 CPU 和 IO 资源，产生大量的慢查询。
- 子查询的结果集存储的临时表，不论是内存临时表还是磁盘临时表都`不会存在索引`，所以查询性能会受到一定的影响。
- 对于返回结果集越大的子查询，其对查询性能的影响也就越大。

在 MySQL 中，可以使用连接（JOIN）查询来替代子查询。 连接查询`不需要建立临时表`，其`速度比子查询要快`，如果查询中`使用索引`的话，性能就会更好。

示例 1：查询学生表中是班长的学生信息。

- 使用子查询：

  ```mysql
  # 创建班级表中班长的索引
  mysql> CREATE INDEX idx_monitor ON class(monitor);
  Query OK, 0 rows affected (0.11 sec)
  Records: 0  Duplicates: 0  Warnings: 0
  
  # 询班长的信息
  mysql> EXPLAIN SELECT * FROM student stu1 WHERE stu1.stuno IN (SELECT monitor FROM class c WHERE monitor IS NOT NULL);
  +----+--------------+-------------+------------+--------+---------------------+---------------------+---------+-----------------------+--------+----------+--------------------------+
  | id | select_type  | table       | partitions | type   | possible_keys       | key                 | key_len | ref                   | rows   | filtered | Extra                    |
  +----+--------------+-------------+------------+--------+---------------------+---------------------+---------+-----------------------+--------+----------+--------------------------+
  |  1 | SIMPLE       | stu1        | NULL       | ALL    | NULL                | NULL                | NULL    | NULL                  | 742203 |   100.00 | NULL                     |
  |  1 | SIMPLE       | <subquery2> | NULL       | eq_ref | <auto_distinct_key> | <auto_distinct_key> | 5       | atguigudb1.stu1.stuno |      1 |   100.00 | NULL                     |
  |  2 | MATERIALIZED | c           | NULL       | index  | idx_monitor         | idx_monitor         | 5       | NULL                  |   9952 |   100.00 | Using where; Using index |
  +----+--------------+-------------+------------+--------+---------------------+---------------------+---------+-----------------------+--------+----------+--------------------------+
  3 rows in set, 1 warning (0.00 sec)
  ```

- 推荐，使用连接查询：

  ```mysql
  mysql> EXPLAIN SELECT stu1.* FROM student stu1 JOIN class c ON stu1.stuno = c.monitor WHERE c.monitor IS NOT NULL;
  +----+-------------+-------+------------+------+---------------+-------------+---------+-----------------------+--------+----------+-------------+
  | id | select_type | table | partitions | type | possible_keys | key         | key_len | ref                   | rows   | filtered | Extra       |
  +----+-------------+-------+------------+------+---------------+-------------+---------+-----------------------+--------+----------+-------------+
  |  1 | SIMPLE      | stu1  | NULL       | ALL  | NULL          | NULL        | NULL    | NULL                  | 742203 |   100.00 | Using where |
  |  1 | SIMPLE      | c     | NULL       | ref  | idx_monitor   | idx_monitor | 5       | atguigudb1.stu1.stuno |      1 |   100.00 | Using index |
  +----+-------------+-------+------------+------+---------------+-------------+---------+-----------------------+--------+----------+-------------+
  2 rows in set, 1 warning (0.00 sec)
  ```

示例 2：所有不为班长的同学。

- 使用子查询：

  ```mysql
  # 查询不为班长的学生信息
  mysql> EXPLAIN SELECT SQL_NO_CACHE a.* FROM student a WHERE a.stuno NOT IN (SELECT monitor FROM class b WHERE monitor IS NOT NULL);
  +----+-------------+-------+------------+-------+---------------+-------------+---------+------+--------+----------+--------------------------+
  | id | select_type | table | partitions | type  | possible_keys | key         | key_len | ref  | rows   | filtered | Extra                    |
  +----+-------------+-------+------------+-------+---------------+-------------+---------+------+--------+----------+--------------------------+
  |  1 | PRIMARY     | a     | NULL       | ALL   | NULL          | NULL        | NULL    | NULL | 742203 |   100.00 | Using where              |
  |  2 | SUBQUERY    | b     | NULL       | index | idx_monitor   | idx_monitor | 5       | NULL |   9952 |   100.00 | Using where; Using index |
  +----+-------------+-------+------------+-------+---------------+-------------+---------+------+--------+----------+--------------------------+
  2 rows in set, 2 warnings (0.00 sec)
  ```

- 推荐，使用连接查询：

  ```mysql
  mysql> EXPLAIN SELECT SQL_NO_CACHE a.* FROM student a LEFT OUTER JOIN class b ON a.stuno =b.monitor WHERE b.monitor IS NULL;
  +----+-------------+-------+------------+------+---------------+-------------+---------+--------------------+--------+----------+--------------------------+
  | id | select_type | table | partitions | type | possible_keys | key         | key_len | ref                | rows   | filtered | Extra                    |
  +----+-------------+-------+------------+------+---------------+-------------+---------+--------------------+--------+----------+--------------------------+
  |  1 | SIMPLE      | a     | NULL       | ALL  | NULL          | NULL        | NULL    | NULL               | 742203 |   100.00 | NULL                     |
  |  1 | SIMPLE      | b     | NULL       | ref  | idx_monitor   | idx_monitor | 5       | atguigudb1.a.stuno |      1 |   100.00 | Using where; Using index |
  +----+-------------+-------+------------+------+---------------+-------------+---------+--------------------+--------+----------+--------------------------+
  2 rows in set, 2 warnings (0.00 sec)
  ```

>尽量不要使用 NOT IN 或者 NOT EXISTS，用`LEFT JOIN xxx ON xx WHERE xx IS NULL`替代。

### ORDER BY 排序优化

#### 排序方式

问题：在 WHERE 条件字段上加索引，但是为什么在 ORDER BY 字段上还要加索引呢？

在 MySQL 中，支持两种排序方式，分别是`FileSort`和`Index 排序`。

- Index 排序中，索引可以保证数据的有序性，就不需要再进行排序，效率更更高。

- FileSort 排序则一般在`内存中`进行排序，占用 CPU 较多。如果待排序的结果较大，会产生临时文件 I/O 到磁盘进行排序的情况，效率低。

优化建议：

- SQL 中，可以在 WHERE 子句和 ORDER BY 子句中使用索引，目的是`在 WHERE 子句中避免全表扫描`，`在 ORDER BY 子句中避免使用 FileSort 排序`。当然，某些情况下全表扫描，或者 FileSort 排序不一定比索引慢。但总的来说，我们还是要避免，以提高查询效率。
- **尽量使用 Index 完成 ORDER BY 排序。如果 WHERE 和 ORDER BY 后面是相同的列就使用单索引列；如果不同就使用联合索引。**
- **无法使用 Index 时，需要对 FileSort 方式进行调优。**

####  优化实例

执先案例前，调用存储过程删除 student 和 class 表上的索引，只留主键：

```mysql
mysql> CALL proc_drop_index('atguigudb1','student');
Query OK, 0 rows affected (0.10 sec)

mysql> CALL proc_drop_index('atguigudb1','class');
Query OK, 0 rows affected (0.01 sec)

mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='student' AND seq_in_index=1 AND index_name <>'PRIMARY';
Empty set (0.00 sec)

mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='class' AND seq_in_index=1 AND index_name <>'
PRIMARY';
Empty set (0.00 sec)
```

##### 使用 LIMIT 参数

不加 LIMIT 参数：

```mysql
# 全表扫描，使用filesort排序
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student ORDER BY age, classid;  
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 2 warnings (0.00 sec)
```

加上 LIMIT 参数：

```mysql
# 全表扫描，使用filesort排序
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student ORDER BY age, classid LIMIT 10;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 2 warnings (0.00 sec)
```

##### 使用索引

创建索引，但是不加 LIMIT 限制，索引失效：

```mysql
mysql> CREATE  INDEX idx_age_classid_name ON student (age, classid, name);
Query OK, 0 rows affected (4.40 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 未加LIMIT参数，全表扫描，使用filesort排序
mysql> EXPLAIN  SELECT SQL_NO_CACHE * FROM student ORDER BY age, classid; 
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 2 warnings (0.00 sec)
```

- 虽然建立了索引，但是未加 LIMIT 参数得时候，优化器通过计算发现，需要回表的数据量特别大，使用索引的性能代价反而比不上不用索引的，因此并未使用索引。

现在，添加 LIMIT 参数：

```mysql
# 可以看到，使用了索引
mysql> EXPLAIN  SELECT SQL_NO_CACHE * FROM student ORDER BY age, classid LIMIT 10;
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows | filtered | Extra |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL |   10 |   100.00 | NULL  |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
1 row in set, 2 warnings (0.00 sec)
```

假如只查询组合索引中有的字段，观察结果：

```mysql
# 可以看到，此时也使用了索引，因为不涉及回表
mysql> EXPLAIN  SELECT SQL_NO_CACHE age, classid, name, id FROM student ORDER BY age, classid;  
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL | 742203 |   100.00 | Using index |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

##### ODRER BY 时顺序错误，索引失效

```mysql
# 创建索引age，classid，stuno
mysql> CREATE INDEX idx_age_classid_stuno ON student (age, classid, stuno);
Query OK, 0 rows affected (4.25 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 索引失效
mysql> EXPLAIN SELECT * FROM student ORDER BY classid LIMIT 10;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 1 warning (0.00 sec)

# 索引失效
mysql> EXPLAIN SELECT * FROM student ORDER BY classid, name LIMIT 10;  
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 1 warning (0.00 sec)

# 索引有效
mysql> EXPLAIN SELECT * FROM student ORDER BY age, classid, stuno LIMIT 10;
+----+-------------+---------+------------+-------+---------------+-----------------------+---------+------+------+----------+-------+
| id | select_type | table   | partitions | type  | possible_keys | key                   | key_len | ref  | rows | filtered | Extra |
+----+-------------+---------+------------+-------+---------------+-----------------------+---------+------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_stuno | 14      | NULL |   10 |   100.00 | NULL  |
+----+-------------+---------+------------+-------+---------------+-----------------------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)

# 索引有效
mysql> EXPLAIN SELECT * FROM student ORDER BY age, classid LIMIT 10;
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows | filtered | Extra |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL |   10 |   100.00 | NULL  |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)

# 索引有效
mysql> EXPLAIN SELECT * FROM student ORDER BY age LIMIT 10;
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows | filtered | Extra |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL |   10 |   100.00 | NULL  |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)
```

##### ODRER BY 时规则不一致，索引失效 

**`顺序错，不索引；方向反，不索引。`**

```mysql
# 索引失效
mysql> EXPLAIN SELECT * FROM student ORDER BY age DESC, classid ASC LIMIT 10;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 1 warning (0.00 sec)

# 索引失效
mysql> EXPLAIN SELECT * FROM student ORDER BY classid DESC, name DESC LIMIT 10;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 1 warning (0.00 sec)

# 索引失效
mysql> EXPLAIN SELECT * FROM student ORDER BY age ASC, classid DESC LIMIT 10; 
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+----------------+
1 row in set, 1 warning (0.00 sec)

# 索引有效（方向保持一致，要正都正，要反都反）
mysql> EXPLAIN SELECT * FROM student ORDER BY age DESC, classid DESC LIMIT 10;
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+---------------------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows | filtered | Extra               |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+---------------------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL |   10 |   100.00 | Backward index scan |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+---------------------+
1 row in set, 1 warning (0.00 sec)
```

##### 无过滤，不索引

```mysql
# 索引有效
mysql> EXPLAIN SELECT * FROM student WHERE age = 45 ORDER BY classid;
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys                              | key                  | key_len | ref   | rows  | filtered | Extra |
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age_classid_name,idx_age_classid_stuno | idx_age_classid_name | 5       | const | 29880 |   100.00 | NULL  |
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
1 row in set, 1 warning (0.00 sec)

# 索引有效
mysql> EXPLAIN SELECT * FROM student WHERE age = 45 ORDER BY classid, name;
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys                              | key                  | key_len | ref   | rows  | filtered | Extra |
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age_classid_name,idx_age_classid_stuno | idx_age_classid_name | 5       | const | 29880 |   100.00 | NULL  |
+----+-------------+---------+------------+------+--------------------------------------------+----------------------+---------+-------+-------+----------+-------+
1 row in set, 1 warning (0.00 sec)

# 索引失效
mysql> EXPLAIN SELECT * FROM student WHERE classid = 45 ORDER BY age;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra                       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |    10.00 | Using where; Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
1 row in set, 1 warning (0.00 sec)

# 索引有效
mysql> EXPLAIN SELECT * FROM student WHERE classid = 45 ORDER BY age LIMIT 10;
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------------+
| id | select_type | table   | partitions | type  | possible_keys | key                  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_classid_name | 73      | NULL |   10 |    10.00 | Using where |
+----+-------------+---------+------------+-------+---------------+----------------------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.01 sec)

# student表创建classid索引
mysql> CREATE INDEX idx_cid ON student(classid);
Query OK, 0 rows affected (3.46 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 索引有效
mysql> EXPLAIN SELECT * FROM student WHERE classid = 45 ORDER BY age;
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+----------------+
| id | select_type | table   | partitions | type | possible_keys | key     | key_len | ref   | rows | filtered | Extra          |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+----------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_cid       | idx_cid | 5       | const |  804 |   100.00 | Using filesort |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+----------------+
1 row in set, 1 warning (0.00 sec)
```

##### 总结

```mysql
对于索引：
INDEX a_b_c(a, b, c)

ORDER BY 能使用索引最左前缀，字段排序规则要保持一致
- ORDER BY a
- ORDER BY a, b
- ORDER BY a, b, c
- ORDER BY a DESC, b DESC, c DESC

如果 WHERE 使用索引的最左前缀定义为常量，则 ORDER BY 能使用索引 
- WHERE a = const ORDER BY b, c
- WHERE a = const AND b = const ORDER BY c
- WHERE a = const ORDER BY b, c
- WHERE a = const AND b > const ORDER BY b, c

不能使用索引进行排序
- ORDER BY a ASC, b DESC, c DESC /* 排序不一致 */ 
- WHERE g = const ORDER BY b, c /* 丢失a索引 */
- WHERE a = const ORDER BY c /* 丢失b索引 */
- WHERE a = const ORDER BY a, d /* d不是索引的一部分 */
- WHERE a IN (...) ORDER BY b, c /* 对于排序来说，多个相等条件也是范围查询 */
```

> 可以从 SQL 执行顺序方面来思考，WHERE 的执行顺序，排在 ORDER BY 之前。

#### 避免 FileSort 排序实例

下面，通过一个案例来实战 FileSort 和 Index 两种排序。`对 ORDER BY 子句，尽量使用 Index 方式排序，避免使用 FileSort 方式排序。`

场景：查询年龄为 30 岁的，且学生编号小于 101000 的学生，按用户名称排序。

执行前先清除 student 上的索引，只留主键：

```mysql
mysql> CALL proc_drop_index('atguigudb1','student');
Query OK, 0 rows affected (0.06 sec)

mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='student' AND seq_in_index=1 AND index_name <>'PRIMARY';
Empty set (0.01 sec)
```

测试以下的查询，此时显然使用的是 FileSort 进行排序：

```mysql
# type是ALL，即最坏的情况，Extra里还出现了Using filesort，也是最坏的情况，优化是必须的
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND stuno <101000 ORDER BY name;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra                       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |     3.33 | Using where; Using filesort |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-----------------------------+
1 row in set, 2 warnings (0.00 sec)
```

**方案一：为了去掉 FileSort 我们可以创建特定索引。**

```mysql
# 创建索引
mysql> CREATE INDEX idx_age_name ON student(age, name);
Query OK, 0 rows affected (4.06 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 可以看到已经使用了索引，虽然仅仅使用到了age这个字段
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND stuno < 101000 ORDER BY name;
+----+-------------+---------+------------+------+---------------+--------------+---------+-------+-------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key          | key_len | ref   | rows  | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+--------------+---------+-------+-------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age_name  | idx_age_name | 5       | const | 30062 |    33.33 | Using where |
+----+-------------+---------+------------+------+---------------+--------------+---------+-------+-------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```

**方案二：尽量让 WHERE 的过滤条件和排序使用上索引。**

```mysql
# 删除旧索引
mysql> DROP INDEX idx_age_name ON student;
Query OK, 0 rows affected (0.02 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 新建索引
mysql> CREATE INDEX idx_age_stuno_name ON student (age, stuno, name);
Query OK, 0 rows affected (4.05 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 测试结果，虽然使用了索引，但是用的FileSort方式排序
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE age = 30 AND stuno <101000 ORDER BY name;
+----+-------------+---------+------------+-------+--------------------+--------------------+---------+------+------+----------+---------------------------------------+
| id | select_type | table   | partitions | type  | possible_keys      | key                | key_len | ref  | rows | filtered | Extra                                 |
+----+-------------+---------+------------+-------+--------------------+--------------------+---------+------+------+----------+---------------------------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_stuno_name | idx_age_stuno_name | 9       | NULL |   17 |   100.00 | Using index condition; Using filesort |
+----+-------------+---------+------------+-------+--------------------+--------------------+---------+------+------+----------+---------------------------------------+
1 row in set, 2 warnings (0.00 sec)
```

原因：因为所有的排序都是在条件过滤之后才执行的，所以，如果条件过滤大部分数据的话，剩下几百几千条数据进行排序其实并不是很消耗性能，即使索引优化了排序，但实际提升性能很有限。相对的 stuno < 10100 这个条件，如果没有用到索引的话，要对几万条数据进行扫描，这是非常消耗性能的，所以索引放在这个字段上性价比最高，是最优选择。

结论：

- **两个索引同时存在，MySQL 自动选择最优的方案。（对于这个例子，MySQL 选择 idx_age_stuno_name）。但是，随着数据量的变化，选择的索引也会随之变化的 。**
- **当 "范围条件" 和 "GROUP BY 或者 ORDER BY" 的字段出现二选一时，优先观察条件字段的过滤数量，如果过滤的数据足够多，而需要排序的数据并不多时，优先把索引放在范围字段上。反之，亦然。**

#### FileSort 算法

排序的字段若不在索引列上，则 FileSort 会有两种算法：`双路排序`和`单路排序`。

##### 双路排序（慢）

MySQL 4.1 之前是使用`双路排序`，字面意思就是两次扫描磁盘，最终得到数据， 读取行指针和 ORDER BY 列，对它们进行排序，然后扫描已经排序好的列表，按照列表中的值重新从列表中读取对应的数据输出。

- 从磁盘取排序字段，在 buffer 进行排序，再从 磁盘取其他字段。

因为取一批数据，要对磁盘进行两次扫描，众所周知，I/O 是很耗时的，所以在 MySQL 4.1 之后，出现了第二种改进的算法，就是单路排序。

##### 单路排序（快）

从磁盘读取查询需要的所有列，按照 ORDER BY 列在 buffer 对它们进行排序，然后扫描排序后的列表进行输出，它的效率更快一些，避免了第二次读取数据。并且把随机 I/O 变成了顺序 I/O，但是它会使用更多的空间， 因为它把每一行都保存在内存中了。

单路排序总体是好过双路排序的，但是单路排序也存在问题：在 sort_buffer 中，单路排序比多路排序要`占用更多空间`。因为单路排序是把所有字段都取出，所以可能取出的数据的总大小超出了 sort_buffer 的容量，导致每次只能取 sort_buffer 容量大小的数据，再进行排序（创建 temp 文件，多路合并），排完再取 sort_buffer 容量大小，依次进行，从而导致`多次 I/O`。单路排序本来想省一次 I/O 操作，反而导致了大量的 I/O 操作，得不偿失。

优化策略：

- `提高 sort_buffer_size。`

  - 不管用哪种算法，提高这个参数都会提高效率，但是要根据系统的能力去提高，因为这个参数是针对每个进程（connection）的 1MB ~ 8MB 之间调整。MySQL 5.7，InnoDB 存储引擎默认值都是 1048576 字节，即 1 MB。

    ```mysql
    mysql> SHOW VARIABLES LIKE '%sort_buffer_size%';
    +-------------------------+---------+
    | Variable_name           | Value   |
    +-------------------------+---------+
    | innodb_sort_buffer_size | 1048576 |
    | myisam_sort_buffer_size | 8388608 |
    | sort_buffer_size        | 262144  |
    +-------------------------+---------+
    3 rows in set (0.00 sec)
    ```

- `提高 max_length_for_sort_data。`

  - 提高这个参数，会增加改进算法的概率。但是如果设的太高，数据总容量超出 sort_buffer_size 的概率就增大，明显症状是高的磁盘 I/O 活动和低的处理器使用率。如果需要返回的列的总长度大于 max_length_for_sort_data，使用双路算法，否则使用单路算法，可以在 1024 ~ 8192 字节之间调整。

    ```mysql
    mysql> SHOW VARIABLES LIKE'%max_length_for_sort_data%';
    +--------------------------+-------+
    | Variable_name            | Value |
    +--------------------------+-------+
    | max_length_for_sort_data | 4096  |
    +--------------------------+-------+
    1 row in set (0.00 sec)
    ```

- `ORDER BY 时避免使用 SELECT *，尽量只 Query 所需要的字段。`

  - 当 Query 的字段大小综合小于 max_length_for_sort_data，而且排序字段不是 TEXT|BLOG 类型时，会使用改进后的算法——单路排序，否则用老算法——多路排序。
  - 两种算法的数据都有可能超出 sort_buffer_size 的容量，超出之后，会创建 tmp 文件进行合并排序，导致多次 I/O，但是用单路排序算法的风险会更大一些，所以要提高 sort_buffer_size。

### GROUP BY 分组优化

- **GROUP BY 使用索引的原则几乎跟 ORDER BY 一致 ，GROUP BY 即使没有过滤条件用到索引，也可以直接使用索引。**
- **GROUP BY 先排序再分组，遵照索引建的最佳左前缀法则。**
- **当无法使用索引列，增大 max_length_for_sort_data 和 sort_buffer_size 参数的设置。**
- **WHERE 效率高于 HAVING，能写在 WHERE 限定的条件就不要写在 HAVING 中了。**
- **减少使用 ORDER BY，和业务沟通能不排序就不排序，或将排序放到程序端去做。ORDER BY、GROUP BY、DISTINCT 这些语句较为耗费 CPU，数据库的 CPU 资源是极其宝贵的。**
- **包含了 ORDER BY、GROUP BY、DISTINCT 这些查询的语句，WHERE 条件过滤出来的结果集请保持在 1000 行以内，否则 SQL 会很慢。**

### LIMIT 分页优化

一般分页查询时，通过创建覆盖索引能够比较好地提高性能。一个常见又非常头疼的问题就是`LIMIT 2000000, 10`，此时需要 MySQL 排序前 2000010 记录，仅仅返回 2000000-2000010 的记录，其他记录丢弃，查询排序的代价非常大。

```mysql
mysql> EXPLAIN SELECT * FROM student LIMIT 2000000, 10;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------+
1 row in set, 1 warning (0.00 sec)
```

**优化思路一**

- 在索引上完成排序分页操作，最后根据主键关联回原表查询所需要的其他列内容。

  ```mysql
  mysql> EXPLAIN SELECT * FROM student t, (SELECT id FROM student ORDER BY id LIMIT 2000000, 10) a WHERE t.id = a.id;
  +----+-------------+------------+------------+--------+---------------+---------+---------+------+--------+----------+-------------+
  | id | select_type | table      | partitions | type   | possible_keys | key     | key_len | ref  | rows   | filtered | Extra       |
  +----+-------------+------------+------------+--------+---------------+---------+---------+------+--------+----------+-------------+
  |  1 | PRIMARY     | <derived2> | NULL       | ALL    | NULL          | NULL    | NULL    | NULL | 742203 |   100.00 | NULL        |
  |  1 | PRIMARY     | t          | NULL       | eq_ref | PRIMARY       | PRIMARY | 4       | a.id |      1 |   100.00 | NULL        |
  |  2 | DERIVED     | student    | NULL       | index  | NULL          | PRIMARY | 4       | NULL | 742203 |   100.00 | Using index |
  +----+-------------+------------+------------+--------+---------------+---------+---------+------+--------+----------+-------------+
  3 rows in set, 1 warning (0.00 sec)
  ```

**优化思路二**

- 该方案适用于主键自增的表，可以把 LIMIT 查询转换成某个位置的查询。

  ```mysql
  mysql> EXPLAIN SELECT * FROM student WHERE id > 2000000 LIMIT 10;
  +----+-------------+---------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
  | id | select_type | table   | partitions | type  | possible_keys | key     | key_len | ref  | rows | filtered | Extra       |
  +----+-------------+---------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
  |  1 | SIMPLE      | student | NULL       | range | PRIMARY       | PRIMARY | 4       | NULL |    1 |   100.00 | Using where |
  +----+-------------+---------+------------+-------+---------------+---------+---------+------+------+----------+-------------+
  1 row in set, 1 warning (0.00 sec)
  
  mysql> SELECT * FROM student LIMIT 2000000, 10;
  Empty set (0.20 sec)
  
  mysql> SELECT * FROM student WHERE id > 2000000 LIMIT 10;
  Empty set (0.00 sec)
  ```

### 优先考虑覆盖索引

#### 什么是覆盖索引

理解方式一：索引是高效找到行的一个方法，但是一般数据库也能使用索引找到一个列的数据，因此它不必读取整个行。毕竟索引叶子节点存储了它们索引的数据；当能通过读取索引就可以得到想要的数据，那就不需要读取行了。一个索引包含了满足查询结果的数据就叫做`覆盖索引`。

理解方式二：**非聚簇复合索引的一种形式，它包括在查询里的 SELECT、JOIN 和 WHERE 子句用到的所有列（即建索引的字段正好是覆盖查询条件中所涉及的字段）。**

简单说就是， `覆盖索引的 "索引列 + 主键" 包含了 SELECT 到 FROM 之间查询的列`。

示例一：

```mysql
# 先删除索引
mysql> CALL proc_drop_index('atguigudb1','student');
Query OK, 0 rows affected (0.02 sec)

mysql> SELECT index_name FROM information_schema.STATISTICS WHERE table_schema='atguigudb1' AND table_name='student' AND seq_in_index=1 AND index_name <>'PRIMARY';
Empty set (0.00 sec)

# 新建索引，student表的age和name字段
mysql> CREATE INDEX idx_age_name ON student (age, name);
Query OK, 0 rows affected (4.20 sec)
Records: 0  Duplicates: 0  Warnings: 0

# 使用了不等于条件，索引失效
mysql> EXPLAIN SELECT * FROM student WHERE age <> 20;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_age_name  | NULL | NULL    | NULL | 742203 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

# 也使用了不等于条件，但使用了索引idx_age_name，因为当前查询的字段只有age和name，idx_age_name在此处是覆盖索引
mysql> EXPLAIN SELECT age,NAME FROM student WHERE age <> 20;
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
| id | select_type | table   | partitions | type  | possible_keys | key          | key_len | ref  | rows   | filtered | Extra                    |
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
|  1 | SIMPLE      | student | NULL       | index | idx_age_name  | idx_age_name | 68      | NULL | 742203 |   100.00 | Using where; Using index |
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
1 row in set, 1 warning (0.00 sec)
```

>**注意：前面提到如果使用上 <> 就不会使用上索引，这并不是绝对的，比如上面这条 SQL。需要明确的一点是，关于索引失效以及索引优化，都是根据`效率`来决定的。对于二级索引来说：查询时间 = 二级索引计算时间 + 回表查询时间，由于此处使用的是覆盖索引，回表查询时间 = 0，索引优化器考虑到这一点，就使用上二级索引了。**

示例二：

```mysql
mysql> EXPLAIN SELECT * FROM student WHERE NAME LIKE '%abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 742203 |    11.11 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

# 与示例一的<>同理，此处，LIKE的%在开头，但仍然使用了索引idx_age_name，idx_age_name在此处也是覆盖索引
mysql> EXPLAIN SELECT id, age FROM student WHERE NAME LIKE '%abc';
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
| id | select_type | table   | partitions | type  | possible_keys | key          | key_len | ref  | rows   | filtered | Extra                    |
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
|  1 | SIMPLE      | student | NULL       | index | NULL          | idx_age_name | 68      | NULL | 742203 |    11.11 | Using where; Using index |
+----+-------------+---------+------------+-------+---------------+--------------+---------+------+--------+----------+--------------------------+
1 row in set, 1 warning (0.00 sec)
```

#### 覆盖索引的利弊

**好处：**

1. **可以避免 Innodb 表进行索引的二次查询（回表）。**
   - Innodb 是以聚集索引的顺序来存储的，对于 Innodb 来说，二级索引在叶子节点中所保存的是行的主键信息，如果是用二级索引查询数据，在查找到相应的键值后，还需通过主键进行二次查询才能获取真实所需要的其他字段的数据。
   - 在覆盖索引中，二级索引的键值中可以获取所要的数据，避免了对主键的二次查询，减少了 I/O 操作，提升了查询效率。

2. **可以把随机 I/O 变成顺序 I/O 加快查询效率。**
   - 由于覆盖索引是按键值的顺序存储的，对于 I/O 密集型的范围查找来说，对比随机从磁盘读取每一行的数据 I/O 要少的多，因此利用覆盖索引在访问时也可以把磁盘的随机读取的 I/O 转变成索引查找的顺序 I/O。
   - 由于覆盖索引可以减少树的搜索次数，显著提升查询性能，所以使用覆盖索引是一个常用的性能优化手段。


**弊端：**

- 索引字段的维护总是有代价的。因此，在建立冗余索引来支持覆盖索引时就需要权衡考虑了。这是业务 DBA，或者称为业务数据架构师的工作。

### 如何给字符串添加索引

假设有一张教师表，表定义如下：

```mysql
CREATE TABLE teacher(
    ID bigint unsigned PRIMARY KEY,
    email varchar(64),
    ...
)ENGINE=innodb;
```

讲师要使用邮箱登录，所以业务代码中一定会出现类似于这样的语句：

```mysql
SELECT col1, col2 FROM teacher WHERE email = 'xxx';
```

如果 email 这个字段上没有索引，那么这个语句就只能做`全表扫描` 。

#### 前缀索引

MySQL是支持`前缀索引`的。默认地，如果你创建索引的语句不指定前缀长度，那么索引就会包含整个字符串。

```mysql
ALTER TABLE teacher ADD INDEX index1(email); 
# 或
ALTER TABLE teacher ADD INDEX index2(email(6)); 
```

这两种不同的定义在数据结构和存储上有什么区别呢？下图就是这两个索引的示意图：

<img src="mysql-advanced/image-20231104155631873.png" alt="image-20231104155631873" style="zoom:80%;" />

<img src="mysql-advanced/image-20231104155706381.png" alt="image-20231104155706381" style="zoom:40%;" />

如果使用的是 index1 （即 email 整个字符串的索引结构），执行顺序是这样的：

- 从 index1 索引树找到满足索引值是 "zhangssxyz@xxx.com" 的这条记录，取得 ID2 的值；

- 到主键上查到主键值是 ID2 的行，判断 email 的值是正确的，将这行记录加入结果集；
- 取 index1 索引树上刚刚查到的位置的下一条记录，发现已经不满足email = "zhangssxyz@xxx.com" 的条件了，循环结束。
- 这个过程中，只需要回主键索引取一次数据，所以系统认为只扫描了一行。

如果使用的是 index2（即 email(6) 索引结构），执行顺序是这样的：

- 从 index2 索引树找到满足索引值是 "zhangs " 的记录，找到的第一个是 ID1；
- 到主键上查到主键值是 ID1 的行，判断出 email 的值不是 "zhangssxyz@xxx.com"，这行记录丢弃；
- 取 index2 上刚刚查到的位置的下一条记录，发现仍然是 "zhangs"，取出 ID2，再到 ID 索引上取整行然后判断，这次值对了，将这行记录加入结果集；
- 重复上一步，直到在 idxe2 上取到的值不是 "zhangs" 时，循环结束。
- 也就是说`使用前缀索引，定义好长度，就可以做到既节省空间，又不用额外增加太多的查询成本。`前面已经讲过区分度，区分度越高越好。因为区分度越高，意味着重复的键值越少。

#### 前缀索引对覆盖索引的影响

前面我们说了使用`前缀索引可能会增加扫描行数`，这会影响到性能。其实，前缀索引的影响不止如此，我们再看一下另外一个场景：

如果使用 index1（即 email 整个字符串的索引结构）的话，可以利用覆盖索引，从 index1 查到结果后直接就返回了，不需要回到 ID 索引再去查一次。而如果使用 index2（即 email(6) 索引结构）的话，就不得不回到 ID 索引再去判断 email 字段的值。

即使你将 index2 的定义修改为 email(18) 的前缀索引，这时候虽然 index2 已经包含了所有的信息，但 InnoDB 还是要回到 id 索引再查一下，因为系统并不确定前缀索引的定义是否截断了完整信息。

结论：`使用前缀索引就用不上覆盖索引对查询性能的优化了`，这也是你在选择是否使用前缀索引时需要考虑的一个因素。

#### 拓展内容

对于类似于邮箱这样的字段来说，使用前缀索引的效果可能还不错。但是，遇到前缀的区分度不够好的情况时，我们要怎么办呢?

比如，我们国家的身份证号，一共 18 位，其中前 6 位是地址码，所以同一个县的人的身份证号前 6 位一般会是相同的。

假设你维护的数据库是一个市的公民信息系统，这时候如果对身份证号做长度为 6 的前缀索引的话，这个索引的区分度就非常低了。按照我们前面说的方法，可能你需要创建长度为 12 以上的前缀索引，才能够满足区分度要求。

但是，索引选取的越长，占用的磁盘空间就越大，相同的数据页能放下的索引值就越少，搜索的效率也就会越低。
那么，如果我们能够确定业务需求里面只有按照身份证进行等值查询的需求，还有没有别的处理方法呢？要求这种方法，既可以占用更小的空间，也能达到相同的查询效率。有!

**第一种方式是使用`倒序存储`。**如果你存储身份证号的时候把它倒过来存，每次查询的时候：

```mysql
mysql> SELECT field list FROM teacher WHERE id_card = reverse(input_id_card_string);
```

由于身份证号的最后 6 位没有地址码这样的重复逻辑，所以最后这 6 位很可能就提供了足够的区分度。当然，实践中你还要使用 COUNT(DISTINCT) 方法去做验证区分度。

**第二种方式是`使用 hash 字段`。**你可以在表上再创建一个整数字段，来保存身份证的校验码，同时在这个字段上创建索引。

```mysql
mysql> ALTER TABLE teacher ADD id_card_crc int unsignedadd INDEX(id_card_crc);
```

然后每次插入新记录的时候，都同时用 crc32() 这个函数得到校验码填到这个新字段，由于校验码可能存在冲突，也就是说两个不同的身份证号通过 crc32() 函数得到的结果可能是相同的，所以你的查询语句 WHERE 部分要判断 id_card 的值是否精确相同。

```mysql
mysql> SELECT field list FROM teacher WHERE id_card_rc = crc32(input_id_card_string) AND id_card = input id_card_string;
```

这样，索引的长度变成了 4 个字节，比原来小了很多。

>从查询效率上看，**使用 hash 字段方式的查询性能相对更稳定一些**，因为 crc32 算出来的值虽然有冲突的概率，但是概率非常小，可以认为每次查询的平均扫描行数接近 1。而倒序存储方式毕竟还是用的前缀索引的方式，也就是说还是会增加扫描行数。

### 索引条件下推（索引下推）

Index Condition Pushdown (ICP) 是 MySQL 5.6 中新特性，是一种在存储引擎层使用索引过滤数据的一种优化方式。`ICP 可以减少存储引擎访问基表的次数，以及 MySQL 服务器访问存储引擎的次数。`

**索引中包含某个字段，但是实际查询没有使用到这个字段的索引（失效了，比如该字段的条件为 "%a%"），此时可以使用这个字段在索引中进行条件过滤，从而可以减少回表的记录条数，这种情况就叫做`索引条件下推/索引下推`。**

#### 使用前后对比

**在不使用 ICP 索引扫描的过程：**

- Storage 层：只将满足 index key 条件的索引记录对应的整行记录取出，返回给 Server 层。
- Server 层：对返回的数据，使用后面的 WHERE 条件过滤，直至返回最后一行。

**使用 ICP 扫描的过程：**

- Storage层：首先将 index key 条件满足的索引记录区间确定，然后在索引上使用 index filter 进行过滤。将满足的 index filter 条件的索引记录才去回表取出整行记录返回 Server 层。不满足 index filter 条件的索引记录丢弃，不回表、也不会返回 Server 层。
- Server 层：对返回的数据，使用 table filter 条件做最后的过滤。

**使用前后的成本差别：**

- 使用 ICP 前，存储层多返回了需要被 index filter 过滤掉的整行记录。
- 使用 ICP 后，直接就去掉了不满足 index filter 条件的记录，省去了这些记录回表和传递到 Server 层的成本。
- ICP 的加速效果取决于在存储引擎内通过 ICP 筛选掉的数据的比例。

#### ICP 的开启、关闭

默认情况下`启用`索引条件下推。可以通过设置系统变量`optimizer_switch `控制`index_condition_pushdown`：

```mysql
mysql> SHOW VARIABLES LIKE '%optimizer_switch%';
+------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Variable_name    | Value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
+------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| optimizer_switch | index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on,engine_condition_pushdown=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=on,block_nested_loop=on,batched_key_access=off,materialization=on,semijoin=on,loosescan=on,firstmatch=on,duplicateweedout=on,subquery_materialization_cost_based=on,use_index_extensions=on,condition_fanout_filter=on,derived_merge=on,use_invisible_indexes=off,skip_scan=on,hash_join=on,subquery_to_derived=off,prefer_ordering_index=on,hypergraph_optimizer=off,derived_condition_pushdown=on |
+------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)

# 关闭索引下推
mysql> SET optimizer_switch='index_condition_pushdown=off';
Query OK, 0 rows affected (0.00 sec)

# 打开索引下推
mysql> SET optimizer_switch='index_condition_pushdown=on';
Query OK, 0 rows affected (0.00 sec)
```

>当使用索引条件下推时，`EXPLAIN`语句输出的结果中`Extra`列内容如果显示为`Using index condition`，即为索引条件下推。

#### ICP 使用案例

建表：

```mysql
mysql> CREATE TABLE people (
    -> `id` int NOT NULL AUTO_INCREMENT,
    -> `zipcode` varchar(20) COLLATE utf8_bin DEFAULT NULL,
    -> `firstname` varchar(20) COLLATE utf8_bin DEFAULT NULL,
    -> `lastname` varchar(20) COLLATE utf8_bin DEFAULT NULL,
    -> `address` varchar(50) COLLATE utf8_bin DEFAULT NULL,
    -> PRIMARY KEY (`id`),
    -> KEY `zip_last_first` (`zipcode`, `lastname`, `firstname`)
    -> ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
Query OK, 0 rows affected, 6 warnings (0.04 sec)
```

插入数据：

```mysql
INSERT INTO people VALUES
(1, '000001', 'san', 'zhang', 'beijing'),
(2, '000002', 'si', 'li', 'nanjing'),
(3, '000003', 'wu', 'wang', 'shanghai'),
(4, '000001', 'liu', 'zhao', 'tianjin');
```

为该表定义联合索引 zip_last_first(zipcode, lastname, firstname) 。如果我们知道了一个人的邮编，但是不确定这个人的姓氏，可以进行如下检索：

```mysql
mysql> SELECT * FROM people WHERE zipcode= '000001' AND lastname LIKE '%zhang%' AND address LIKE '%beijing%';
+----+---------+-----------+----------+---------+
| id | zipcode | firstname | lastname | address |
+----+---------+-----------+----------+---------+
|  1 | 000001  | san       | zhang    | beijing |
+----+---------+-----------+----------+---------+
1 row in set (0.00 sec)

# 查询计划
mysql> EXPLAIN SELECT * FROM people WHERE zipcode= '000001' AND lastname LIKE '%zhang%' AND address LIKE '%beijing%';
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+------------------------------------+
| id | select_type | table  | partitions | type | possible_keys  | key            | key_len | ref   | rows | filtered | Extra                              |
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+------------------------------------+
|  1 | SIMPLE      | people | NULL       | ref  | zip_last_first | zip_last_first | 63      | const |    2 |    25.00 | Using index condition; Using where |
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+------------------------------------+
1 row in set, 1 warning (0.00 sec)
```

- 从查询计划可以看出，Extra 中显示了 Using index condition，这表示使用了索引下推。即：先使用索引的 zipcode 字段进行匹配，然后索引下推使用 lastname 字段进行过滤，最后再进行回表。
- 另外，Using where 表示条件中包含需要过滤的非索引列的数据，即 "address LIKE '%beijing%'" 这个条件并不是索引列，需要在服务端过滤掉。

> 为了便于 MySQL 客户端查询，将相关汉字替换为了拼音。

这个 PEOPLE 表中存在两个索引，分别是：

- 主键索引（简图）：

  <img src="mysql-advanced/image-20231104201231770.png" alt="image-20231104201231770" style="zoom:67%;" />

- 二级索引 zip_last_first：

  <img src="mysql-advanced/image-20231104201305284.png" alt="image-20231104201305284" style="zoom:67%;" />

下面关闭 IPC，再次查看执行计划：

```mysql
mysql> SET optimizer_switch='index_condition_pushdown=off';
Query OK, 0 rows affected (0.00 sec)

# 关闭IPC后，不再使用索引下推
mysql> EXPLAIN SELECT * FROM people WHERE zipcode= '000001' AND lastname LIKE '%zhang%' AND address LIKE '%beijing%';
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys  | key            | key_len | ref   | rows | filtered | Extra       |
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+-------------+
|  1 | SIMPLE      | people | NULL       | ref  | zip_last_first | zip_last_first | 63      | const |    2 |    25.00 | Using where |
+----+-------------+--------+------------+------+----------------+----------------+---------+-------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```

#### 开启和关闭 ICP 的性能对比

创建存储过程，主要目的是插入很多 000001 的数据，这样查询的时候，为了在存储引擎层坐果率，减少 I/O，也为了减少缓冲池（缓存数据页，没有 I/O）的作用。

```mysql
mysql> DELIMITER //
mysql> CREATE PROCEDURE inser_people(max_num INT)
    -> BEGIN
    -> DECLARE i INT DEFAULT 0;
    -> SET autocommit = 0;
    -> REPEAT
    -> SET i = i + 1;
    -> INSERT INTO people(zipcode, firstname, lastname, address) VALUES('000001', 'liu', 'zhao', 'tianjin');
    -> UNTIL i = max_num
    -> END REPEAT;
    -> COMMIT;
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

调用存储过程：

```mysql
mysql> CALL inser_people(1000000);
Query OK, 0 rows affected (27.68 sec)
```

首先，打开 profiling：

```mysql
mysql> SET profiling=1;
Query OK, 0 rows affected, 1 warning (0.00 sec)
```

执行 SQL 语句，此时索引下推打开：

```mysql
mysql>  SET optimizer_switch='index_condition_pushdown=on';
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT * FROM people WHERE zipcode = '000001' AND lastname LIKE '%zhang%';
+----+---------+-----------+----------+---------+
| id | zipcode | firstname | lastname | address |
+----+---------+-----------+----------+---------+
|  1 | 000001  | san       | zhang    | beijing |
+----+---------+-----------+----------+---------+
1 row in set (0.25 sec)
```

再次执行 SQL 语句，此时索引下推关闭：

```mysql
mysql> SET optimizer_switch='index_condition_pushdown=off';
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT * FROM people WHERE zipcode = '000001' AND lastname LIKE '%zhang%';
+----+---------+-----------+----------+---------+
| id | zipcode | firstname | lastname | address |
+----+---------+-----------+----------+---------+
|  1 | 000001  | san       | zhang    | beijing |
+----+---------+-----------+----------+---------+
1 row in set (2.52 sec)
```

查看当前会话所产生的所有 profiles：

```mysql
mysql> SHOW profiles\G;
*************************** 1. row ***************************
Query_ID: 1
Duration: 0.24520475
   Query: SELECT * FROM people WHERE zipcode = '000001' AND lastname LIKE '%zhang%'
*************************** 2. row ***************************
Query_ID: 2
Duration: 2.52617775
   Query: SELECT * FROM people WHERE zipcode = '000001' AND lastname LIKE '%zhang%'
3 rows in set, 1 warning (0.00 sec)

ERROR: 
No query specified
```

- 多次测试效率对比来看，使用 ICP 优化的查询效率会好一些。这里建议多存储一些数据，效果更明显。

#### ICP 的使用条件

- 只能用于二级索引（Secondary Index）。
- EXPLAIN 显示的执行计划中 type 值（join 类型）为 range 、ref、eq_ref 或者 ref_or_null 。
- 并非全部 WHERE 条件都可以用 ICP 筛选，如果 WHERE 条件的字段不在索引列中，还是要读取整表的记录到 Server 端做 WHERE 过滤。(IPC 是 Storage 层行为)
- ICP 可以用于 MyISAM 和 InnnoDB 存储引擎。
- MySQL 5.6 版本的不支持分区表的 ICP 功能，5.7 版本的开始支持。
- 当 SQL 使用覆盖索引时，不支持 ICP 优化方法。

### 普通索引 vs 唯一索引

在不同的业务场景下，应该选择普通索引，还是唯一索引？

假设你在维护一个居民系统，每个人都有一个唯一的身份证号，而且业务代码已经保证了不会写入两个重复的身份证号。如果居民系统需要按照身份证号查姓名：

```mysql
SELECT name FROM CUser WHERE id_card = 'xxxxxxxyyyyyyzzzzz';
```

所以，你一定会考虑在 id_card 字段上建索引。

由于身份证号字段比较大，不建议把身份证号当做主键。现在有两个选择，要么给 id_card 字段创建唯一素引，要么创建一个普通索引。如果业务代码已经保证了不会写入重复的身份证号，那么这两个选择逻辑上都是正确的。

你知道的，InnoDB 的数据是按数据页为单位来读写的。也就是说，当需要读一条记录的时候，并不是将这个记录本身从磁盘读出来，而是以页为单位，将其整体读入内存。在 InnoDB中，每个数据页的大小默认是 16 KB。

从性能的角度考虑，你选择唯一索引还是普通索引呢？选择的依据是什么呢？

假设，我们有一个主键列为 id 的表，表中有字段 k，并且在 k 上有索引，假设字段 k 上的值都不重复。 这个表的建表语句是：

```mysql
CREATE TABLE test(
    id int PRIMARY KEY,
    k int NOT null,
    name varchar(16),
    index (k)
)ENGINE=InnoDB;
```

表中 R1 ~ R5 的 (id, k) 值分别为 (100, 1)、(200, 2)、(300, 3)、(500, 5) 和 (600, 6)。

#### 查询过程

假设，执行查询的语句是 "SELECT id FROM test WHERE k = 5"。

- 对于普通索引来说，查找到满足条件的第一个记录 (5, 500) 后，需要查找下一个记录，直到碰到第一个不满足 k = 5 条件的记录。
- 对于唯一索引来说，由于索引定义了唯一性，查找到第一个满足条件的记录后，就会停止继续检索。

那么，这个不同带来的性能差距会有多少呢？答案是， 微乎其微。

你知道的，InnoDB 的数据是按数据页为单位来读写的。也就是说，当需要读一条记录的时候，并不是将这个记录本身从磁盘读出来，而是以页为单位，将其整体读入内存。在 InnoDB 中，每个数据页的大小默认是 16KB。

因为引擎是按页读写的，所以说，当找到 k = 5 的记录的时候，它所在的数据页就都在内存里了。那么，对于普通索引来说，要多做的那一次 "查找和判断下一条记录" 的操作，就只需要一次指针寻找和一次计算。

当然，如果 k = 5 这个记录刚好是这个数据页的最后一个记录，那么要取下一个记录，必须读取下一个数据页，这个操作会稍微复杂一些。

但是，我们之前计算过，对于整型字段，一个数据页可以放近千个 key，因此出现这种情况的概率会很低。所以我们计算平均性能差异时，仍可以认为这个操作成本对于现在的 CPU 来说可以忽略不计。

#### 更新过程

为了说明普通索引和唯一索引对更新语句性能的影响这个问题，介绍一下`change buffer`。

当需要更新一个数据页时，如果数据页在内存中就直接更新，而如果这个数据页还没有在内存中的话， 在不影响数据一致性的前提下，InooDB 会将这些更新操作缓存在 change buffer 中，这样就不需要从磁盘中读入这个数据页了。在下次查询需要访问这个数据页的时候，将数据页读入内存，然后执行 change buffer 中与这个页有关的操作。通过这种方式就能保证这个数据逻辑的正确性。

将 change buffer 中的操作应用到原数据页，得到最新结果的过程称为`merge`。除了`访问这个数据页`会触发 merge 外，系统有`后台线程`会定期 merge。在`数据库正常关闭 (shutdown)`的过程中，也会执行 merge 操作。

如果能够将更新操作先记录在 change buffer，`减少读磁盘`，语句的执行速度会得到明显的提升。而且， 数据读入内存是需要占用 buffer pool 的，所以这种方式还能够`避免占用内存`，提高内存利用率。

那么，什么条件下可以使用 change buffer 呢？

对干唯一索引来说，所有的更新操作都要先判断这个操作是否违反唯一性约束。比如，要插入 (4, 400) 这个记录，就要先判断现在表中是否已经存在 k = 4 的记录，而这必须要将数据页读入内存才能判断。如果都已经读入到内存了，那直接更新内存会更快，就没必要使用 change buffer 了。

因此，`唯一索引的更新就不能使用 change buffer，实际上也只有普通索引可以使用。`

change buffer 用的是 buffer pool 里的内存，因此不能无限增大。change buffer 的大小，可以通过参数`innodb_change_buffer_maxsize`来动态设置。这个参数设置为 50 的时候，表示 changebuffer 的大小最多只能占用 buffer pool 的 50%。

```mysql
mysql> SHOW VARIABLES LIKE '%change_buffer%';
+-------------------------------+-------+
| Variable_name                 | Value |
+-------------------------------+-------+
| innodb_change_buffer_max_size | 25    |
| innodb_change_buffering       | all   |
+-------------------------------+-------+
2 rows in set (0.01 sec)
```

那么，如果要在这张表中插入一个新记录 (4, 400) 的话，InnoDB 的处理流程是怎样的？

第一种情况是，这个记录要更新的目标页在内存中。这时：

- 对干唯一索引来说，找到 3 和 5 之间的位置，判断为没有冲突，插入这个值，语句执行结束。
- 对于普通索引来说，找到 3 和 5 之间的位置，插入这个值，语句执行结束。
- 这样看来，普通索引和唯一索引对更新语句性能影响的差别，只是一个判断，只会耗费微小的 CPU 时间。

第二种情况是，这个记录要更新的目标页不在内存中。这时：

- 对于唯一索引来说，需要将数据页读入内存，判断有没有冲突，插入这个值，语句执行结束。
- 对于普通索引来说，则是将更新记录在 change buffer，语句执行就结束了。

将数据从磁盘读入内存涉及随机 I/O 的访问，是数据库里面成本最高的操作之一。change buffer 因为减少了随机磁盘访问，所以对更新性能的提升是会很明显的。

>案例：
>
>某个业务的库内存命中率突然从 99% 降低到了 75%，整个系统处于阻塞状态，更新语句全部堵住。而探究其原因后，发现这个业务有大量插入数据的操作，而他在前一天把其中的某个普通索引改成了唯一索引。

#### change buffer 的使用场景

change buffer 只限于用在普通索引的场景下，而不适用于唯一索引。那么，现在有一个问题就是：普通索引的所有场景，使用 change buffer 都可以起到加速作用吗？

因为 merge 的时候是真正进行数据更新的时刻，而 change buffer 的主要目的就是将记录的变更动作缓存下来，所以`在一个数据页做 merge 之前，change buffer 记录的变更越多 (也就是这个页面上要更新的次数越多)，收益就越大。`

因此，对于`写多读少`的业务来说，页面在写完以后马上被访问到的概率比较小，此时 change buffer 的使用效果最好。这种业务模型常见的就是账单类、日志类的系统。

反过来，假设一个业务的更新模式是写入之后马上会做查询，那么即使满足了条件，将更新先记录在 change buffer，之后由于马上要访问这个数据页，会立即触发 merge 过程，这样随机访问 I/O 的次数不会减少，反而增加了 change buffer 的维护代价。所以，对于这种业务模式来说，changebuffer 反而起到了副作用。

- 普通索引和唯一索引应该怎么选择？其实，这两类索引在查询能力上是没差别的，主要考虑的是对`更新性能`的影响。所以，建议你`尽量选择普通索引`。
- 在实际使用中会发现，普通索引和 change buffer 的配合使用，对于`数据量大`的表的更新优化还是很明显的。
- 如果所有的更新后面，都马上`伴随着对这个记录的查询，那么你应该关闭change buffer`。而在其他情况下，change buffer 都能提升更新性能。
- 由于唯一索引用不上 change buffer 的优化机制，因此如果`业务可以接受`，从性能角度出发建议优先考虑`非唯一索引`。但是如果 "业务可能无法确保" 的情况下，怎么处理呢？
  - 首先，`业务正确性优先`。我们的前提是 "业务代码已经保证不会写入重复数据" 的情况下，讨论性能问题。如果业务不能保证，或者业务就是要求数据库来做约束，那么没得选，必须创建唯一索引。 这种情况下，本节的意义在于，如果碰上了大量插入数据慢、内存命中率低的时候，给你多提供一个排查思路。
  - 然后，在一些 "`归档库`" 的场景，你是可以考虑使用唯一索引的。比如，线上数据只需要保留半年， 然后历史数据保存在归档库。这时候，归档数据已经是确保没有唯一键冲突了。要提高归档效率， 可以考虑把表里面的唯一索引改成普通索引。

### 其它查询优化策略

#### EXISTS 和 IN 的区分

**问题：**

不太理解哪种情况下应该使用 EXISTS，哪种情况应该用 IN。选择的标准是看能否使用表的索引吗？

回答:

索引是个前提，其实选择与否还是要看表的大小。你可以将选择的标准理解为`小表驱动大表`，在这种方式下效率是最高的。

比如下面这样：

```mysql
SELECT * FROM A WHERE cc IN (SELECT cc FROM B)

SELECT * FROM A WHERE EXISTS (SELECT cc FROM B WHERE B.cc = A.cc)
```

- 当 A 小于 B 时，用 EXISTS。因为 EXISTS 的实现，相当于外表循环，实现的逻辑类似于：

  ```mysql
  for i in A
      for j in B
          if j.cc == i.cc then ...
  ```

- 当 B 小于 A 时用 IN，因为实现的逻辑类似于：

  ```mysql
  for i in B
      for j in A
          if j.cc == i.cc then ...
  ```

结论：**哪个表小就用哪个表来驱动，A 表小就用 EXISTS ，B 表小就用 IN。**

#### COUNT(*) 与 COUNT(具体字段) 效率对比

问：在 MySQL 中统计数据表的行数，可以使用三种方式`SELECT COUNT(*)`、`SELECT COUNT(1)`和`SELECT COUNT(具体字段)`，使用这三者之间的查询效率是怎样的？

答：

前提：如果你要统计的是某个字段的非空数据行数，则另当别论，毕竟比较执行效率的前提是结果一样才可以。

**环节 1：**COUNT(\*) 和 COUNT(1) 都是对所有结果进行 COUNT，`COUNT(*) 和 COUNT(1) 本质上并没有区别`（二者执行时间可能略有差别，不过你还是可以把它俩的执行效率看成是相等的）。如果有 WHERE 子句，则是对所有符合筛选条件的数据行进行统计；如果没有 WHERE 子句，则是对数据表的数据行数进行统计。

**环节 2：**COUNT(\*) 和 COUNT(1) 的使用，如果是 MyISAM 存储引擎，统计数据表的行数只需要`O(1)`的复杂度，这是因为每张 MyISAM 的数据表都有一个 meta 信息存储了 row_count 值，而一致性则由表级锁来保证。如果是 InnoDB 存储引擎，因为 InnoDB 支持事务，采用行级锁和 MVCC 机制，所以无法像 MyISAM 一样，维护一个 row_count 变量，因此需要采用扫描全表，是`O(n)`的复杂度，进行循环 + 计数的方式来完成统计。

**环节 3：**`在 InnoDB 引擎中，如果采用 COUNT(具体字段) 来统计数据行数，要尽量采用二级索引。`因为主键采用的索引是聚簇索引，聚簇索引包含的信息多，资源消耗明显会大于二级索引。对于 COUNT(*) 和COUNT(1) 来说，它们不需要查找具体的行，只是统计行数，系统会自动采用占用空间更小的二级索引来进行统计，如果有多个二级索引，会使用 keylen 小的二级索引进行扫描，当没有二级索引的时候，才会采用主键索引来进行统计。

#### 关于 SELECT(*)

在表查询中，建议明确字段，不要使用 \* 作为查询的字段列表，`推荐使用 SELECT <字段列表> 查询`。原因：

1. MySQL 在解析的过程中，会通过`查询数据字典`将 "*" 按序转换成所有列名，这会大大的耗费资源和时间。
2. 无法使用`覆盖索引`。

#### LIMIT 1 对优化的影响

针对的是会扫描全表的 SQL 语句，如果你可以确定结果集只有一条，那么加上`LIMIT 1`的时候，当找到一条结果的时候就不会继续扫描了，这样会加快查询速度。

如果数据表已经对字段建立了唯一索引，那么可以通过索引进行查询，不会全表扫描的话，就不需要加上`LIMIT 1`了。

#### 多使用 COMMIT

只要有可能，在程序中尽量多使用 COMMIT，这样程序的性能得到提高，需求也会因为 COMMIT 所释放的资源而减少。

COMMIT 所释放的资源:

- 回滚段上用于恢复数据的信息。
- 被程序语句获得的锁。
- redo/undo log buffer 中的空间。
- 管理上述 3 种资源中的内部花费。

### 淘宝数据库，主键如何设计的

聊一个实际问题：淘宝的数据库，主键是如何设计的？

某些错的离谱的答案还在网上年复一年的流传着，甚至还成为了所谓的 MySQL 军规。其中，一个最明显的错误就是关于 MySQL 的主键设计。

大部分人的回答如此自信：用 8 字节的 BIGINT 做主键，而不要用 INT。错!

这样的回答，只站在了数据库这一层，而没有`从业务的角度`思考主键。主键就是一个自增 ID 吗？站在 2022 年的新年档口，用自增做主键，架构设计上可能连及格都拿不到。

#### 自增 ID 的问题

自增 ID 做主键，简单易懂，几乎所有数据库都支持自增类型，只是实现上各自有所不同而已。自增 ID 除了简单，其他都是缺点，总体来看存在以下几方面的问题：

1. **可靠性不高**

存在自增 ID 回溯的问题，这个问题直到最新版本的 MySQL 8.0 才修复。

2. **安全性不高**

对外暴露的接口可以非常容易猜测对应的信息。比如：/user/1/ 这样的接口，可以非常容易猜测用户 ID 的值为多少，总用户数量有多少，也可以非常容易地通过接口进行数据的爬取。

3. **性能差**

自增 ID 的性能较差，需要在数据库服务器端生成。

4. **交互多**

业务还需要额外执行一次类似 last_insert_id() 的函数才能知道刚才插入的自增值，这需要多一次的网络交互。在海量并发的系统中，多 1 条 SQL，就多一次性能上的开销。

5. **局部唯一性**

最重要的一点，自增 ID 是局部唯一，只在当前数据库实例中唯一，而不是全局唯一，无法保证在任意服务器间都是唯一的。对于目前分布式系统来说，这简直就是噩梦。

#### 业务字段做主键

为了能够唯一地标识一个会员的信息，需要为`会员信息表`设置一个主键。那么，怎么为这个表设置主键，才能达到我们理想的目标呢？这里我们考虑业务字段做主键。 表数据如下：

<img src="mysql-advanced/image-20231105082644708.png" alt="image-20231105082644708" style="zoom:80%;" />

在这表里，哪个字段比较合适呢？

##### 选择卡号（cardno）

会员卡号（cardno）看起来比较合适，因为会员卡号不能为空，而且有唯一性，可以用来标识一条会员记录。

```mysql
mysql> CREATE TABLE demo.membermaster
-> (
-> cardno CHAR(8) PRIMARY KEY, -- 会员卡号为主键 -> membername TEXT,
-> memberphone TEXT,
-> memberpid TEXT,
-> memberaddress TEXT,
-> sex TEXT,
-> birthday DATETIME
-> );
Query OK, 0 rows affected (0.06 sec)
```

不同的会员卡号对应不同的会员，字段 cardno 唯一地标识某一个会员。如果都是这样，会员卡号与会员一一对应，系统是可以正常运行的。

但实际情况是， `会员卡号可能存在重复使用的情况`。比如，张三因为工作变动搬离了原来的地址，不再到商家的门店消费了（退还了会员卡），于是张三就不再是这个商家门店的会员了。但是，商家不想让这个会员卡空着，就把卡号是 10000001 的会员卡发给了王五。

从系统设计的角度看，这个变化只是修改了会员信息表中的卡号是 10000001 这个会员信息，并不会影响到数据一致性。也就是说，修改会员卡号是 10000001 的会员信息，系统的各个模块，都会获取到修改后的会员信息，不会出现有的模块获取到修改之前的会员信息，有的模块获取到修改后的会员信息， 而导致系统内部数据不一致的情况。因此，从信息系统层面上看是没问题的。

但是从使用系统的业务层面来看，就有很大的问题了，会对商家造成影响。

比如，我们有一个销售流水表（trans），记录了所有的销售流水明细。2020 年 12 月 01 日，张三在门店购买了一本书，消费了 89 元。那么，系统中就有了张三买书的流水记录，如下所示：

<img src="mysql-advanced/image-20231105091726752.png" alt="image-20231105091726752" style="zoom:80%;" />

接着，我们查询一下 2020 年 12 月 01 日的会员销售记录：

```mysql
mysql> SELECT b.membername,c.goodsname,a.quantity,a.salesvalue,a.transdate
-> FROM demo.trans AS a
-> JOIN demo.membermaster AS b
-> JOIN demo.goodsmaster AS c
-> ON (a.cardno = b.cardno AND a.itemnumber=c.itemnumber); 
+------------+-----------+----------+------------+---------------------+ 
| membername | goodsname | quantity | salesvalue |           transdate | 
+------------+-----------+----------+------------+---------------------+ 
| 张三        |        书 |    1.000 |      89.00 | 2020-12-01 00:00:00 | 
+------------+-----------+----------+------------+---------------------+ 
1 row in set (0.00 sec)
```

如果会员卡 10000001 又发给了王五，我们会更改会员信息表。导致查询时：

```mysql
mysql> SELECT b.membername,c.goodsname,a.quantity,a.salesvalue,a.transdate
-> FROM demo.trans AS a
-> JOIN demo.membermaster AS b
-> JOIN demo.goodsmaster AS c
-> ON (a.cardno = b.cardno AND a.itemnumber=c.itemnumber); 
+------------+-----------+----------+------------+---------------------+ 
| membername | goodsname | quantity | salesvalue | transdate           | 
+------------+-----------+----------+------------+---------------------+ 
| 王五        |        书 |    1.000 |      89.00 | 2020-12-01 00:00:00 | 
+------------+-----------+----------+------------+---------------------+ 
1 row in set (0.01 sec)
```

这次得到的结果是：王五在 2020 年 12 月 01 日，买了一本书，消费 89 元。显然是错误的！结论：`千万不能把会员卡号当做主键`。

##### 选择会员电话或身份证号

会员电话可以做主键吗？不行的。在实际操作中，`手机号也存在被运营商收回，重新发给别人用的情况。`

那身份证号行不行呢？好像可以。因为身份证决不会重复，身份证号与一个人存在一一对 应的关系。可问题是，`身份证号属于个人隐私`，顾客不一定愿意给你。要是强制要求会员必须登记身份证号，会把很多客人赶跑的。其实，客户电话也有这个问题，这也是我们在设计会员信息表的时候，允许身份证号和电话都为空的原因。

所以，建议`尽量不要用跟业务有关的字段做主键`。毕竟，作为项目设计的技术人员，我们谁也无法预测在项目的整个生命周期中，哪个业务字段会因为项目的业务需求而有重复，或者重用之类的情况出现。

> 经验：刚开始使用 MySQL 时，很多人都很容易犯的错误是喜欢用业务字段做主键，想当然地认为了解业务需求，但实际情况往往出乎意料，而更改主键设置的成本非常高。

#### 淘宝的主键设计

在淘宝的电商业务中，订单服务是一个核心业务。请问， 订单表的主键淘宝是如何设计的呢？是自增ID吗？

打开淘宝，看一下订单信息：

<img src="mysql-advanced/image-20231105093015352.png" alt="image-20231105093015352" style="zoom:80%;" />

从上图可以发现，订单号不是自增 ID！我们详细看下上述 4 个订单号：

```tex
1550672064762308113
1481195847180308113
1431156171142308113
1431146631521308113
```

订单号是 19 位的长度，且订单的最后 5 位都是一样的，都是 08113。且订单号的前面 14 位部分是单调递增的。

大胆猜测，淘宝的订单 ID 设计应该是：`订单ID = 时间 + 去重字段 + 用户 ID 后 6 位尾号`。

这样的设计能做到全局唯一，且对分布式系统查询及其友好。

#### 推荐的主键设计

`非核心业务`：对应表的主键自增 ID，如告警、日志、监控等信息。

`核心业务`：**主键设计至少应该是全局唯一且是单调递增**。全局唯一保证在各系统之间都是唯一的，单调递增是希望插入时不影响数据库性能。

这里推荐最简单的一种主键设计：`UUID`。

##### UUID

**UUID 的特点：**全局唯一，占用 36 字节，数据无序，插入性能差。

**认识 UUID：**

- 为什么 UUID 是全局唯一的？
- 为什么 UUID 占用 36 个字节？
- 为什么 UUID 是无序的？

MySQL 数据库的 UUID 组成如下所示：

```tex
UUID = 时间 + UUID 版本（16 字节）- 时钟序列（4 字节） - MAC 地址（12 字节）
```

以 UUID 值 e0ea12d4-6473-11eb-943c-00155dbaa39d 举例：

<img src="mysql-advanced/image-20231105094017944.png" alt="image-20231105094017944" style="zoom: 67%;" />

1. 为什么 UUID 是全局唯一的？
   - 在 UUID 中时间部分占用 60 位，存储的类似 TIMESTAMP 的时间戳，但表示的是从 1582-10-15 00:00:00.00 到现在的 100 ns 的计数。可以看到 UUID 存储的时间精度比 TIMESTAMPE 更高，时间维度发生重复的概率降低到 1/100 ns。
   - 时钟序列是为了避免时钟被回拨导致产生时间重复的可能性。MAC 地址用于全局唯一。


2. 为什么 UUID 占用 36 个字节？
   - UUID 根据字符串进行存储，设计时还带有无用 "-" 字符串，因此总共需要 36 个字节。


3. 为什么 UUID 是随机无序的呢？
   - 因为 UUID 的设计中，将时间低位放在最前面，而这部分的数据是一直在变化的，并且是无序。


##### 改造UUID

若将时间高低位互换，则时间就是单调递增的了，也就变得单调递增了。MySQL 8.0 可以更换时间低位和时间高位的存储方式，这样无序的 UUID 就是有序的 UUID 了。

MySQL 8.0 还解决了 UUID 存在的空间占用的问题，除去了 UUID 字符串中无意义的 "-" 字符串，并且将字符串用二进制类型保存，这样存储空间降低为了 16 字节。

可以通过 MySQL 8.0 提供的`uuid_to_bin`函数实现上述功能，同样的，MySQL 也提供了`bin_to_uuid`函数进行转化：

```mysql
mysql> SET @uuid = UUID();
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT @uuid, uuid_to_bin(@uuid), uuid_to_bin(@uuid, TRUE);
+--------------------------------------+----------------------------------------+----------------------------------------------------+
| @uuid                                | uuid_to_bin(@uuid)                     | uuid_to_bin(@uuid, TRUE)                           |
+--------------------------------------+----------------------------------------+----------------------------------------------------+
| 394569c7-7b7d-11ee-bd87-0242ac160002 | 0x394569C77B7D11EEBD870242AC160002     | 0x11EE7B7D394569C7BD870242AC160002                 |
+--------------------------------------+----------------------------------------+----------------------------------------------------+
1 row in set (0.00 sec)
```

**通过函数`uuid_to_bin(@uuid, TRUE)`，可以将无序的 UUID 转化为有序的 UUID，具有`全局唯一 + 单调递增`的特点，这不就是我们想要的主键！**

##### 有序 UUID 性能测试

16 字节的有序 UUID，相比之前 8 字节的自增ID，性能和存储空间对比究竟如何呢？

我们来做一个测试，插入 1 亿条数据，每条数据占用 500 字节，含有 3 个二级索引，最终的结果如下所示：

<img src="mysql-advanced/image-20231105095226429.png" alt="image-20231105095226429" style="zoom:67%;" />

从上图可以看到插入 1 亿条数据有序 UUID 是最快的，而且在实际业务使用中有序 UUID 在`业务端就可以生成`，还可以进一步减少 SQL 的交互次数。

另外，虽然有序 UUID 相比自增 ID 多了 8 个字节，但实际只增大了 3G 的存储空间，还可以接受。

>在当今的互联网环境中，非常不推荐自增 ID 作为主键的数据库设计，更推荐类似有序 UUID 的全局唯一的实现。
>
>另外在真实的业务系统中，主键还可以加入业务和系统属性，如用户的尾号，机房的信息等。这样的主键设计就更为考验架构师的水平了。

##### 非 MySQL 8.0 版本

手动赋值字段做主键！

比如，设计各个分店的会员表的主键，因为如果每台机器各自产生的数据需要合并，就可能会出现主键重复的问题。

可以在总部 MySQL 数据库中，有一个管理信息表，在这个表中添加一个字段，专门用来记录当前会员编号的最大值。

门店在添加会员的时候，先到总部 MySQL 数据库中获取这个最大值，在这个基础上加 1，然后用这个值作为新会员的 id 的同时，更新总部 MySQL 数据库管理信息表中的当前会员编号的最大值。

这样一来，各个门店添加会员的时候，都对同一个总部 MySQL 数据库中的数据表字段进行操作，就解决了各门店添加会员时会员编号冲突的问题。

## 数据库的设计规范

### 为什么需要数据库设计

我们在设计数据表的时候，要考虑很多问题。比如：

- 用户都需要什么数据？需要在数据表中保存哪些数据？
- 如何保证数据表中数据的`正确性`？当插入、删除、更新的时候该进行怎样的`约束检查`？
- 如何降低数据表的`数据冗余度`，保证数据表不会因为用户量的增长而迅速扩张？
- 如何让负责数据库维护的人员`更方便`的使用数据库？
- 使用数据库的应用场景也各不相同，可以说针对不同的情况，设计出来的数据表可能千差万别。

现实情况中，面临的场景：

- 数据冗余、信息重复，存储空间浪费。
- 数据更新、插入、删除的异常。
- 无法正确表示信息。
- 丢失有效信息。
- 程序性能差。

良好的数据库设计，则有以下优点：

- 节省数据的存储空间。
- 能够保证数据的完整性。
- 方便进行数据库应用系统的开发。

总之，开始设置数据库的时候，我们就需要重视数据表的设计。为了建立`冗余较小`、`结构合理`的数据库，设计数据库时`必须遵循一定的规则`。

### 范式

#### 范式简介

**在关系型数据库中，关于数据表设计的基本原则、规则就称为`范式`。**可以理解为，一张数据表的设计结构需要满足的某种设计标准的`级别`。要想设计一个结构合理的关系型数据库，必须满足一定的范式。

范式的英文名称是 Normal Form，简称 NF。它是英国人 E.F.Codd 在上个世纪 70 年代提出关系数据库模型后总结出来的。范式是关系数据库理论的基础，也是我们在设计数据库结构过程中所要遵循的`规则`和`指导方法`。

#### 范式都包括哪些

目前关系型数据库有六种常见范式，按照范式级别，从低到高分别是：`第一范式 (1NF)、第二范式 (2NF)、第三范式 (3NF)、巴斯-科德范式 (BCNF)、第四范式 (4NF) 和第五范式 (5NF，又称完美范式)。`

数据库的范式设计越高阶，冗余度就越低，同时高阶的范式一定符合低阶范式的要求，满足最低要求的范式是第一范式（1NF）。在第一范式的基础上，进一步满足更多规范要求的称为第二范式（2NF），其余范式以此类推。

<img src="mysql-advanced/image-20231105101706772.png" alt="image-20231105101706772" style="zoom:80%;" />

`一般来说，在关系型数据库设计中，最高也就遵循到 BCNF，普遍的是 3NF。`但范式不是绝对的，有时候为了提高某些查询性能，我们还需要破坏范式规则，也就是`反规范化`。

#### 键和相关属性的概念

范式的定义会使用到主键和候选键，数据库中的键（Key）由一个或者多个属性组成。数据表中常用的几种键和属性的定义：

- `超键`：能`唯一标识`元组的属性集叫做超键。（属性集，可以理解为一个或多个字段的组合）
- `候选键`：如果超键不包括多余的属性，那么这个超键就是候选键。用户可以从候选键中选择一个作为主键。
- `外键`∶如果数据表 R1 中的某属性集不是 R1 的主键，而是另一个数据表 R2 的主键，那么这个属性集就是数据表 R1 的外键。
- `主属性`：包含在任一候选键中的属性称为主属性。
- `非主属性`：与主属性相对，指的是不包含在任何一个候选键中的属性。

通常，我们也将候选键称之为`码`，把主键也称为`主码`。因为键可能是由多个属性组成的，针对单个属性，我们还可以用主属性和非主属性来进行区分。

举例来说，假设有两个表：

- 球员表（player）：球员编号 | 姓名 | 身份证号 | 年龄 | 球队编号


- 球队表（team）：球队编号 | 主教练 | 球队所在地

那么：

- 超键：对于球员表来说，超键就是包括球员编号或者身份证号（球员编号或者身份证号是能唯一标识元组的）的任意组合，比如（球员编号）、（球员编号，姓名）、（身份证号，年龄）等。
- 候选键：就是最小的超键，对于球员表来说，候选键就是（球员编号）或者（身份证号）。
- 主键：我们自己选定，也就是从候选键中选择一个，比如（球员编号）。
- 外键：球员表中的球队编号。
- 主属性、非主属性：在球员表中，主属性是（球员编号）（身份证号），其他的属性（姓名）（年龄）（球队编号）都是非主属性。

#### 第一范式

**第一范式主要是确保数据表中每个字段的值必须具有`原子性`，也就是说数据表中每个字段的值为`不可再次拆分的最小数据单位`。**

我们在设计某个字段的时候，对于字段 x 来说，不能把字段 x 拆分成字段 x_1 和字段 x_2。事实上，任何的 DBMS 都会满足第一范式的要求，不会将字段进行拆分。

**示例一：**

- 假设一家公司要存储员工的姓名和联系方式。它创建一个如下表：

  <img src="mysql-advanced/image-20231105111915247.png" alt="image-20231105111915247" style="zoom:80%;" />

- 该表不符合 1NF ，因为规则说 "表的每个属性必须具有原子（单个）值"，lisi 和 zhaoliu 员工的 emp_mobile 值违反了该规则。为了使表符合 1NF ，我们应该有如下表数据：

  <img src="mysql-advanced/image-20231105112027619.png" alt="image-20231105112027619" style="zoom:80%;" />

**示例二：**

- user 表的设计不符合第一范式：

  <img src="mysql-advanced/image-20231105112149639.png" alt="image-20231105112149639" style="zoom: 67%;" />

- 其中，user_info 字段为用户信息，可以进一步拆分成更小粒度的字段，不符合数据库设计对第一范式的要求。将 user_info 拆分后如下：

  <img src="mysql-advanced/image-20231105112426133.png" alt="image-20231105112426133" style="zoom: 67%;" />

**示例三：**

- 属性的原子性是主观的 。例如，Employees 关系中雇员姓名应当使用 1 个（fullname），2 个（firstname 和 lastname），还是3个（firstname、middlename 和 lastname）属性表示呢？答案取决于应用程序。如果应用程序需要分别处理雇员的姓名部分（如：用于搜索目的），则有必要把它们分开。否则，不需要。

  <img src="mysql-advanced/image-20231105112551568.png" alt="image-20231105112551568" style="zoom: 67%;" />

  <img src="mysql-advanced/image-20231105112627064.png" alt="image-20231105112627064" style="zoom: 67%;" />

#### 第二范式

**第二范式要求，在满足第一范式的基础上，还要满足数据表里的每一条数据记录，都是`可唯一标识`的（也就是一定有主键）。而且所有非主键字段，都必须`完全依赖主键`，不能只依赖主键的一部分。**如果知道主键的所有属性的值，就可以检索到任何元组（行）的任何属性的任何值。（要求中的主键，其实可以拓展替换为候选键）

另外第二范式只能完全函数依赖，不能部分函数依赖。

**示例一：**

- 成绩表 （学号，课程号，成绩）关系中，（学号，课程号）可以决定成绩，但是学号不能决定成绩，课程号也不能决定成绩，所以 "（学号，课程号）---> 成绩" 就是`完全依赖关系`。

**示例二：**

- `比赛表 player_game`，里面包含球员编号、姓名、年龄、比赛编号、比赛时间和比赛场地等属性，这里候选键和主键都为（球员编号，比赛编号），我们可以通过候选键（或主键）来决定如下的关系：

  ```tex
  (球员编号, 比赛编号) → (姓名, 年龄, 比赛时间, 比赛场地，得分)
  ```

- 但是这个数据表不满足第二范式，因为数据表中的字段之间还存在着如下的对应关系：

  ```tex
  # 姓名和年龄部分依赖球员编号
  (球员编号) → (姓名，年龄)
  
  # 比赛时间, 比赛场地部分依赖(球员编号, 比赛编号)
  (比赛编号) → (比赛时间, 比赛场地)
  ```

对于非主属性来说，并非完全依赖候选键。这样会产生怎样的问题呢？（为什么要满足 2NF）

- `数据冗余`： 如果一个球员可以参加 m 场比赛，那么球员的姓名和年龄就重复了 m - 1 次。一个比赛也可能会有 n 个球员参加，比赛的时间和地点就重复了 n - 1 次。

- `插入异常`： 如果我们想要添加一场新的比赛，但是这时还没有确定参加的球员都有谁，那么就没法插入。

- `删除异常`： 如果我要删除某个球员编号，如果没有单独保存比赛表的话，就会同时把比赛信息删除掉。

- `更新异常`： 如果我们调整了某场比赛的时间，那么数据表中所有这场比赛的时间都需要进行调整，否则就会出现一场比赛时间不同的情况。

为了避免出现上述的情况，我们可以把球员比赛表设计为下面的三张表：

| 表名                        | 属性（字段）                       |
| --------------------------- | ---------------------------------- |
| 球员 player 表              | 球员编号、姓名和年龄等属性         |
| 比赛 game 表                | 比赛编号、比赛时间和比赛场地等属性 |
| 球员比赛关系 player_game 表 | 球员编号、比赛编号和得分等属性     |

这样的话，每张数据表都符合第二范式，也就避免了异常情况的发生。

> **1NF 告诉我们`字段属性`需要是原子性的，而 2NF 告诉我们`一张表就是一个独立的对象`，一张表只表达一个意思。**

**示例三：**

- 定义一个名为 Orders 的关系，表示订单和订单行的信息：

  <img src="mysql-advanced/image-20231105135908487.png" alt="image-20231105135908487" style="zoom: 67%;" />

  - 违反了第二范式，因为有非主键属性仅依赖于候选键（或主键）的一部分。例如，可以仅通过 orderid 找到订单的 orderdate，以及 customerid 和 companyname，而没有必要再去使用 productid。

- 修改为 Orders 表和 OrderDetails 表如下，此时符合第二范式。

  <img src="mysql-advanced/image-20231105142331128.png" alt="image-20231105142331128" style="zoom:60%;" />

> 小结：`第二范式要求实体的属性完全依赖主关键字。`如果存在不完全依赖，那么这个属性和主关键字的这一部分应该分离出来形成一个新的实体，新实体与元实体之间是一对多的关系。

#### 第三范式

**第三范式是在第二范式的基础上，确保数据表中的`每一个非主键字段都和主键字段直接相关`，也就是说，要求数据表中的所有非主键字段不能依赖于其他非主键字段。**即，不能存在非主属性 A 依赖于非主属性 B，非主属性 B 依赖于主键 C 的情况，即存在 "A ---> B ---> C" 的决定关系。通俗地讲，该规则的意思是所有非主键属性之间不能有传递依赖关系，必须相互独立。（这里的主键可以拓展为候选键）

**示例一：**

- **部门信息表：**每个部门有部门编号（dept_id）、部门名称、部门简介等信息。
- **员工信息表：**每个员工有员工编号、姓名、部门编号。列出部门编号后就不能再将部门名称、部门简介等与部门有关的信息再加入员工信息表中。
- 如果不存在部门信息表，则根据第三范式（3NF）也应该构建它，否则就会有大量的数据冗余。

**示例二：**

- 如下表，商品类别名称依赖于商品类别编号，不符合第三范式：

  <img src="mysql-advanced/image-20231105143003915.png" alt="image-20231105143003915" style="zoom:67%;" />

- 将其修改为商品表和商品类别表，商品表 goods 通过商品类别 id 字段（category_id）与商品类别表 goods_category 进行关联。

  <img src="mysql-advanced/image-20231105143223182.png" alt="image-20231105143223182" style="zoom:67%;" />

**示例三：**

- 球员 player 表 ：球员编号、姓名、球队名称和球队主教练。现在，我们把属性之间的依赖关系画出来，如下图所示：

  <img src="mysql-advanced/image-20231105192956621.png" alt="image-20231105192956621" style="zoom:67%;" />

- 可以看到，球员编号决定了球队名称，同时球队名称决定了球队主教练，非主属性球队主教练就会传递依赖于球员编号，因此不符合 3NF 的要求。

- 如果要达到 3NF 的要求，需要把数据表拆成下面这样：

  | 表名   | 属性（字段）             |
  | ------ | ------------------------ |
  | 球队表 | 球员编号、姓名和球队名称 |
  | 球员表 | 球队名称、球队主教练     |

**示例四：**

- 修改第二范式中的示例三。

- 此时的 Orders 关系包含 orderid、orderdate、customerid 和 companyname 属性，主键定义为 orderid。customerid 和 companyname 均依赖于主键 orderid。例如，你需要通过 orderid 主键来查找代表订单中客户的 customerid，同样，你需要通过 orderid 主键查找订单中客户的公司名称（companyname）。然而， customerid 和 companyname 也是互相依靠的。为满足第三范式，可以改写如下：

  <img src="mysql-advanced/image-20231105193501255.png" alt="image-20231105193501255" style="zoom:67%;" />

>符合3NF后的数据模型通俗地讲，2NF 和 3NF 通常以这句话概括：`"每个非键属性依赖于键，依赖于整个键，并且除了键别无他物"。`

#### 小结

关于数据表的设计，有三个范式要遵循：

- 第一范式（1NF），`确保每列保持原子性`。数据库的每一列都是不可分割的原子数据项，不可再分的最小数据单元，而不能是集合、数组、记录等非原子数据项。
- 第二范式（2NF），`确保每列都和主键完全依赖`。尤其在复合主键的情况向下，非主键部分不应该依赖于部分主键。
- 第三范式（3NF），`确保每列都和主键直接相关`，而不是间接相关。

**范式的优点：**数据的标准化有助于消除数据库中的数据冗余，`第三范式通常被认为在性能、拓展性和数据完整性方面达到了最好的平衡。`

**范式的缺点：**范式的使用，可能降低查询的效率。因为范式等级越高，设计出来的数据表就越多、越精细，数据的冗余度就越低，进行数据查询的时候就可能需要关联多张表，这不但代价昂贵，也可能使一些索引策略无效。

范式只是提出了设计的标准，实际上设计数据表时，未必一定要符合这些标准。开发中，我们会出现为了性能和读取效率违反范式化的原则，通过增加少量的冗余或重复的数据来提高数据库的读性能，减少关联查询，join 表的次数，实现空间换取时间的目的。因此，在实际的设计过程中要理论结合实际，灵活运用。

>范式本身没有优劣之分，只有适用场景不同。没有完美的设计，只有合适的设计，我们在数据表的设计中，还需要根据需求将范式和反范式混合使用。

### 反范式化

#### 概述

有的时候，不能简单按照规范要求设计数据表，因为有的数据看似冗余，其实对业务来说十分重要。这个时候，我们就要`遵循业务优先的原则`，首先满足业务需求，再尽量减少冗余。

如果数据库中的数据量比较大，系统的 UV 和 PV 访问频次比较高，则完全按照 MySQL 的三大范式设计数据表，读数据时会产生大量的关联查询，在一定程度上会影响数据库的读性能。如果我们想对查询效率进行优化，反范式化也是一种优化思路。此时，可以通过在数据表中增加冗余字段来提高数据库的读性能。

> **规范化 VS 性能：**
>
> 1. 为满足某种商业目标，数据库性能比规范化数据库更重要。
> 2. 在数据规范化的同时，要综合考虑数据库的性能。
> 3. 通过在给定的表中添加额外的字段，以大量减少需要从中搜索信息所需的时间。
> 4. 通过在给定的表中插入计算列，以方便查询。

#### 应用举例

**示例一：**

- 员工的信息存储在 employees 表中，部门信息存储在 departments 表中，通过 employees 表中的 department_id 字段与 departments 表建立关联关系。如果要查询一个员工所在部门的名称，可以使用下面的 SQL。如果经常需要进行这个操作，连接查询就会浪费很多时间。可以在 employees 表中增加一个冗余字段 department_name，这样就不用每次都进行连接操作了。

  ```mysql
  SELECT employee_id, department_name FROM employees e JOIN departments d ON e.department_id = d.department_id;
  ```

**示例二：**

- 反范式化的 goods商品信息表 设计如下：

  <img src="mysql-advanced/image-20231105195225812.png" alt="image-20231105195225812" style="zoom:67%;" />

**示例三：**

- 假设有 2 个表，分别是商品流水表（trans ）和商品信息表（goodsinfo）。商品流水表里有 400 万条流水记录，商品信息表里有 2000 条商品记录。

  - 商品流水表：

    <img src="mysql-advanced/image-20231105195442786.png" alt="image-20231105195442786" style="zoom:67%;" />

  - 商品信息表：

    <img src="mysql-advanced/image-20231105195506227.png" alt="image-20231105195506227" style="zoom:55%;" />

- 上面这两个表，都是符合第三范式要求的。但是，在项目的实施过程中，对流水的查询频率很高，而且为了获取商品名称，基本都会用到与商品信息表的连接查询。为了减少连接，可以直接把商品名称字段加到流水表里面。这样一来，就可以直接从流水表中获取商品名称字段了。虽然增加了冗余字段，但是避免了关联查询，提升了查询的效率。新的商品流水表如下所示：

  <img src="mysql-advanced/image-20231105200705246.png" alt="image-20231105200705246" style="zoom:70%;" />

**示例四：**

- 课程评论表 class_comment，对应的字段名称及含义如下：

  <img src="mysql-advanced/image-20231105200825357.png" alt="image-20231105200825357" style="zoom:60%;" />

- 学生表 student，对应的字段名称及含义如下：

  <img src="mysql-advanced/image-20231105200859601.png" alt="image-20231105200859601" style="zoom: 50%;" />

- 在实际应用中，我们在显示课程评论的时候，通常会显示这个学生的昵称，而不是学生 ID，因此，当我们想要查询某个课程的前 1000 条评论时，需要关联 class_comment 和 student这两张表来进行查询。

**实验数据：模拟两张百万量级的数据表**

为了更好地进行 SQL 优化实验，我们需要给学生表和课程评论表随机模拟出百万量级的数据。我们可以通过存储过程来实现模拟数据。

- 创建表：

  ```mysql
  mysql> set names utf8;
  Query OK, 0 rows affected, 1 warning (0.00 sec)
  
  mysql> CREATE DATABASE atguigudb2;
  Query OK, 1 row affected (0.01 sec)
  
  mysql> USE atguigudb2;
  Database changed
  
  # 学生表
  mysql> CREATE TABLE student(
      -> stu_id INT PRIMARY KEY AUTO_INCREMENT,
      -> stu_name VARCHAR(25),
      -> create_time DATETIME
      -> );
  Query OK, 0 rows affected (0.03 sec)
  
  # 课程评论表
  mysql> CREATE TABLE class_comment(
      -> comment_id INT PRIMARY KEY AUTO_INCREMENT,
      -> class_id INT,
      -> comment_text VARCHAR(35),
      -> comment_time DATETIME,
      -> stu_id INT
      -> );
  Query OK, 0 rows affected (0.03 sec)
  ```

- 创建存储过程：

  ```mysql
  # 创建向学生表中添加数据的存储过程
  mysql> DELIMITER //
  mysql> CREATE PROCEDURE batch_insert_student(IN START INT(10),IN max_num INT (10))
      -> BEGIN
      -> DECLARE i INT DEFAULT 0;
      -> DECLARE date_start DATETIME DEFAULT ('2017-01-01 00:00:00');
      -> DECLARE date_temp DATETIME;
      -> SET date_temp = date_start;
      -> SET autocommit=0;
      -> REPEAT
      -> SET i=i+1;
      -> SET date_temp = DATE_ADD(date_temp,INTERVAL RAND( )*60 SECOND);
      -> INSERT INTO student(stu_id, stu_name,create_time)
      -> VALUES ((START+i), CONCAT ('stu_',i), date_temp) ;
      -> UNTIL i = max_num
      -> END REPEAT;
      -> COMMIT;
      -> END //
  Query OK, 0 rows affected, 2 warnings (0.02 sec)
  
  mysql> DELIMITER ;
  
  # 创建向课程评论表中添加数据的存储过程
  mysql> DELIMITER //
  mysql> CREATE PROCEDURE batch_insert_class_comments(IN START INT(10),IN max_num INT (10))
      -> BEGIN
      -> DECLARE i INT DEFAULT 0;
      -> DECLARE date_start DATETIME DEFAULT ('2018-01-01 00:00:00');
      -> DECLARE date_temp DATETIME;
      -> DECLARE comment_text VARCHAR(25);
      -> DECLARE stu_id INT;
      -> SET date_temp = date_start;
      -> SET autocommit=0;
      -> REPEAT
      -> SET i=i+1;
      -> SET date_temp = DATE_ADD(date_temp,INTERVAL RAND( )*60 SECOND);
      -> SET comment_text=SUBSTR(MD5(RAND()),1,20);
      -> SET stu_id=FLOOR(RAND()*1000000);
      -> INSERT INTO `class_comment`(`comment_id`,`class_id`,`comment_text`,`comment_time`,`stu_id`)
      -> VALUES ((START+i),10001,comment_text,date_temp,stu_id);
      -> UNTIL i = max_num
      -> END REPEAT;
      -> COMMIT;
      -> END //
  Query OK, 0 rows affected, 2 warnings (0.01 sec)
  
  mysql> DELIMITER ;
  ```

- 调用存储过程：

  ```mysql
  # 调用存储过程，学生id从10001开始，添加1000000条数据
  mysql> CALL batch_insert_student(10000, 1000000);
  Query OK, 0 rows affected (31.58 sec)
  
  # 添加数据的过程的调用，一个1000000条数据
  mysql> CALL batch_insert_class_comments(10000, 1000000);
  Query OK, 0 rows affected (34.81 sec)
  
  mysql> SELECT COUNT(*) FROM student;
  +----------+
  | COUNT(*) |
  +----------+
  |  1000000 |
  +----------+
  1 row in set (0.03 sec)
  
  mysql> SELECT COUNT(*) FROM class_comment;
  +----------+
  | COUNT(*) |
  +----------+
  |  1000000 |
  +----------+
  1 row in set (0.03 sec)
  ```

- 测试：

  ```mysql
  mysql> SELECT p.comment_text, p.comment_time, stu.stu_name FROM class_comment AS p LEFT JOIN student AS stu ON p.stu_id = stu.stu_id WHERE p.class_id = 10001 ORDER BY p.comment_id DESC LIMIT 10000;
  +----------------------+---------------------+------------+
  | comment_text         | comment_time        | stu_name   |
  +----------------------+---------------------+------------+
  | ea4d03873f7f3a26e1ba | 2018-12-14 03:26:26 | stu_209036 |
  | 1fe6697da6c77d637497 | 2018-12-14 03:26:22 | stu_740513 |
  | 5fafc1b2b297f23159cd | 2018-12-14 03:26:21 | stu_168595 |
  | badca0f8d453eb0b67bf | 2018-12-10 15:29:16 | stu_288793 |
  | 219f577d16b8005ca971 | 2018-12-10 15:28:48 | stu_259916 |
  | dcb3e24911b5bcaa47a8 | 2018-12-10 15:28:11 | stu_815722 |
  +----------------------+---------------------+------------+
  10000 rows in set (0.04 sec)
  ```

  - 运行时长为 0.04 秒，对于网站的响应来说，这已经很慢了，用户体验会非常差。如果我们想要提升查询的效率，可以允许适当的数据冗余，也就是在商品评论表中增加用户昵称字段，在 class_comment 数据表的基础上增加 stu_name 字段，就得到了 class_comment2 数据表。

- 反范式优化实验对比：

  ```mysql
  # 进行反范式化设计
  
  # 表的复制
  mysql> CREATE TABLE class_comment1 AS SELECT * FROM class_comment;
  Query OK, 1000000 rows affected (5.70 sec)
  Records: 1000000  Duplicates: 0  Warnings: 0
  
  # 添加主键，保证class_comment1与class_comment的结构相同
  mysql> ALTER TABLE class_comment1 ADD PRIMARY KEY(comment_id);
  Query OK, 0 rows affected (8.74 sec)
  Records: 0  Duplicates: 0  Warnings: 0
  
  mysql> SHOW INDEX FROM class_comment1;
  +----------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | Table          | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
  +----------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | class_comment1 |          0 | PRIMARY  |            1 | comment_id  | A         |      996244 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
  +----------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  1 row in set (0.00 sec)
  
  # 向课程评论表中增加stu_name字段
  mysql> ALTER TABLE class_comment1 ADD stu_name VARCHAR(25);
  Query OK, 0 rows affected (0.02 sec)
  Records: 0  Duplicates: 0  Warnings: 0
  
  # 给新添加的字段赋值
  mysql> UPDATE class_comment1 c SET stu_name = (SELECT stu_name FROM student s WHERE c.stu_id = s.stu_id);
  Query OK, 989941 rows affected (32.28 sec)
  Rows matched: 1000000  Changed: 989941  Warnings: 0
  ```

- 如果我们想要查询课程 ID 为 10001 的前 10000 条评论，需要写成下面这样：

  ```mysql
  mysql> SELECT comment_text, comment_time, stu_name FROM class_comment1 WHERE class_id = 10001 ORDER BY class_id DESC LIMIT 10000;
  +----------------------+---------------------+------------+
  | comment_text         | comment_time        | stu_name   |
  +----------------------+---------------------+------------+
  | 98ac57e47f969ccb3d5e | 2018-01-01 00:00:58 | stu_657565 |
  | 9417961cb63119c96cbc | 2018-01-01 00:01:10 | stu_236175 |
  | cb5b9be56b3ce6de9f0f | 2018-01-01 00:01:30 | stu_659727 |
  | 339b5f86158833d800c0 | 2018-01-04 11:59:26 | stu_411945 |
  | 2e069c3facbd62cf2051 | 2018-01-04 11:59:37 | stu_759542 |
  | 979fd006b1ea975b5c4d | 2018-01-04 12:00:28 | stu_160871 |
  +----------------------+---------------------+------------+
  10000 rows in set (0.00 sec)
  ```

  - 优化之后只需要扫描一次聚集索引即可，运行时间为 0.00 秒，查询时间比之前少很多。 你能看到，在数据量大的情况下，查询效率会有显著的提升。

#### 反范式的新问题

- `存储空间变大`了。
- 一个表中字段做了修改，另一个表中冗余的字段也需要做同步修改，否则`数据不一致`。
- 若采用存储过程来支持数据的更新、删除等额外操作，如果更新频繁，会非常`消耗系统资源`。
- 在`数据量小`的情况下，反范式不能体现性能的优势，可能还会让数据库的设计更加`复杂`。

#### 反范式的适用场景

当`冗余信息有价值`或者能`大幅度提高查询效率`的时候，我们才会采取反范式的优化。

1. 增加冗余字段的建议。
   - 这个冗余字段不需要经常进行修改。
   - 这个冗余字段查询的时候不可或缺（因为经常要用，所以才增加该冗余字段）。


2. 历史快照、历史数据的需要。
   - 在现实生活中，我们经常需要一些冗余信息，比如订单中的收货人信息，包括姓名、电话和地址等。每次发生的订单收货信息都属于历史快照，需要进行保存，但用户可以随时修改自己的信息，这时保存这些冗余信息是非常有必要的。


反范式优化也常用在数据仓库的设计中，因为数据仓库通常存储历史数据，对增删改的实时性要求不强，对历史数据的分析需求强。这时适当允许数据的冗余度，更方便进行数据分析。

简单总结下数据仓库和数据库在使用上的区别：

- 数据库设计的目的在于捕获数据，而数据仓库设计的目的在于分析数据；
- 数据库对数据的增删改实时性要求强，需要存储在线的用户数据，而数据仓库存储的一般是历史数据；
- 数据库设计需要尽量避免冗余，但为了提高查询效率也允许一定的冗余度，而数据仓库在设计上更偏向采用反范式设计。

> 注意：反范式反的是第二范式或第三范式，第一范式是一定要遵守的。

### 巴斯范式

人们在 3NF 的基础上进行了改进，提出了`巴斯范式 (BCNF)`，也叫做`巴斯-科德范式 (Boyce-Codd NormalForm)`。BCNF 被认为没有新的设计规范加入，`只是对第三范式中设计规范要求更强`，使得数据库冗余度更小。所以，称为是修正的第三范式，或扩充的第三范式，BCNF 不被称为第四范式。

若一个关系达到了第三范式，并且它只有一个候选键，或者它的每个候选键都是单属性，则该关系自然达到 BCNF。

> 一般来说，一个数据库设计符合 3NF 或 BCNF 就可以了。

#### 案例

我们分析如下表的范式情况：

<img src="mysql-advanced/image-20231105231508896.png" alt="image-20231105231508896" style="zoom:67%;" />

在这个表中，一个仓库只有一个管理员，同时一个管理员也只管理一个仓库。先来梳理下这些属性之间的依赖关系。

- 仓库名决定了管理员，管理员也决定了仓库名，同时（仓库名，物品名）的属性集合可以决定数量这个属性。这样，我们就可以找到数据表的候选键。


- 候选键：是（管理员，物品名）和（仓库名，物品名），然后从候选键中选择一个作为主键，比如（仓库名，物品名）。
- 主属性：包含在任一候选键中的属性，也就是仓库名，管理员和物品名。

- 非主属性 ：数量这个属性。

#### 是否符合三范式

如何判断一张表的范式呢？需要根据范式的等级，从低到高来进行判断：

- 首先，数据表每个属性都是原子性的，符合 1NF 的要求；


- 其次，数据表中非主属性 "数量" 都与候选键全部依赖，（仓库名，物品名）决定数量，（管理员，物品名）决定数量。因此，数据表符合 2NF 的要求；


- 最后，数据表中的非主属性，不传递依赖于候选键。因此符合 3NF 的要求。

#### 存在的问题

既然数据表已经符合了 3NF 的要求，是不是就不存在问题了呢？来看下面的情况：

- 增加一个仓库，但是还没有存放任何物品。根据数据表实体完整性的要求，主键不能有空值，因此会出现插入异常 ；
- 如果仓库更换了管理员，就可能会修改数据表中的多条记录；
- 如果仓库里的商品都卖空了，那么此时仓库名称和相应的管理员名称也会随之被删除。

可以能看到，即便数据表符合 3NF 的要求，同样可能存在插入，更新和删除数据的异常情况。

#### 问题解决

首先需要确认造成异常的原因：主属性仓库名对于候选键（管理员，物品名）是部分依赖的关系，这样就有可能导致上面的异常情况。因此引入 BCNF，它在 3NF 的基础上消除了主属性对候选键的部分依赖或者传递依赖关系。

如果在关系 R 中，U 为主键，A 属性是主键的一个属性，若存在 A ---> Y，Y 为主属性（也就是非候选码中的属性），则该关系不属于 BCNF。

根据 BCNF 的要求，需要把仓库管理关系 warehouse_keeper 表拆分成下面这样：

- 仓库表 ：（仓库名，管理员）
- 库存表 ：（仓库名，物品名，数量）

这样就不存在主属性对于候选键的部分依赖或传递依赖，上面数据表的设计就符合 BCNF。

> 再举例如下：
>
> 有一个学生导师表，其中包含字段：学生 ID，专业，导师，专业 GPA，这其中学生 ID 和专业是联合主键。
>
> <img src="mysql-advanced/image-20231106082639953.png" alt="image-20231106082639953" style="zoom: 67%;" />
>
> 这个表的设计满足三范式，但是这里存在另一个依赖关系，"专业" 依赖于 "导师"，也就是说每个导师只做一个专业方面的导师，只要知道了是哪个导师，自然就知道是哪个专业的了。
>
> 所以这个表的部分主键 Major 依赖于非主键属性 Advisor，那么我们可以进行以下的调整，拆分成 2 个表：
>
> 学生导师表：
>
> <img src="mysql-advanced/image-20231106090057819.png" alt="image-20231106090057819" style="zoom: 67%;" />
>
> 导师表：
>
> <img src="mysql-advanced/image-20231106090132192.png" alt="image-20231106090132192" style="zoom:67%;" />
>
> 

### 第四范式

`多值依赖`的概念：

- 多值依赖即属性之间的一对多关系，记为 K →→ A。
- 函数依赖事实上是单值依赖，所以不能表达属性值之间的一对多关系。
- 平凡的多值依赖∶全集 U = K + A，一个 K 可以对应于多个 A，即 K →→ A。此时整个表就是一组一对多关系。
- 非平凡的多值依赖：全集 U = K + A + B，一个 K 可以对应于多个 A，也可以对应于多个 B，A 与 B 互相独立，即 K →→ A，K →→ B。整个表有多组一对多关系，且有："一" 部分是相同的属性集合，"多" 部分是互相独立的属性集合。

**示例一：**

职工表（职工编号，职工孩子姓名，职工选修课程）。

在这个表中，同一个职工可能会有多个职工孩子姓名。同样，同一个职工也可能会有多个职工选修课程，即这里存在着多值事实，不符合第四范式。

如果要符合第四范式，只需要将上表分为两个表，使它们只有一个多值事实，例如： 职工表一（职工编号，职工孩子姓名），职工表二（职工编号，职工选修课程），两个表都只有一个多值事实，所以符合第四范式。

**示例二：**

比如建立课程、教师、教材的模型。我们规定，每门课程有对应的一组教师，每门课程也有对应的一组教材，一门课程使用的教材和教师没有关系。我们建立的关系表如下：课程 ID，教师 ID，教材 ID，这三列作为联合主键。

为了表述方便，我们用教师 Name 代替 ID，这样更容易看懂：

<img src="mysql-advanced/image-20231106090818655.png" alt="image-20231106090818655" style="zoom:67%;" />

这个表除了主键，就没有其他字段了，所以肯定满足 BCNF，但是却存在多值依赖导致的异常。

假如下学期想采用一本新的英版高数教材，但是还没确定具体哪个老师来教，那么就无法在这个表中维护 Course 高数和 Book 英版高数教材的的关系。

解决办法是把这个多值依赖的表拆解成 2 个表，分别建立关系。这是拆分后的表：

<img src="mysql-advanced/image-20231106104448526.png" alt="image-20231106104448526" style="zoom:67%;" />

<img src="mysql-advanced/image-20231106104509220.png" alt="image-20231106104509220" style="zoom:67%;" />

### 第五范式、域键范式

除了第四范式外，还有更高级的`第五范式 (又称完美范式) 和域键范式 (DKNF)`。

在满足第四范式（4NF）的基础上，消除不是由候选键所蕴含的连接依赖。如果关系模式 R 中的每一个连接依赖均由 R 的候选键所隐含，则称此关系模式符合第五范式。

函数依赖是多值依赖的一种特殊的情况，而多值依赖实际上是连接依赖的一种特殊情况。但连接依赖不像函数依赖和多值依赖可以由语义直接导出 ，而是在关系连接运算时才反映出来。存在连接依赖的关系模式仍可能遇到数据冗余及插入、修改、删除异常等问题。

第五范式处理的是无损连接问题，这个范式基本`没有实际意义`，因为无损连接很少出现，而且难以察觉。而域键范式试图定义一个终极范式 ，该范式考虑所有的依赖和约束类型，但是实用价值也是最小的，只存在理论研究中。

### 实战案例

**需求：商超进货系统中的进货单表进行剖析。**

进货单表：

<img src="mysql-advanced/image-20231106124225094.png" alt="image-20231106124225094" style="zoom:77%;" />

这个表中的字段很多，表里的数据量也很惊人。大量重复导致表变得庞大，效率极低。如何改造？

>在实际工作场景中，这种由于数据表结构设计不合理，而导致的数据重复的现象并不少见。往往是系统虽然能够运行，承载能力却很差，稍微有点流量，就会出现内存不足、CUP 使用率飙升的情况，甚至会导致整个项目失败。

#### 迭代 1 次：考虑 1NF

**第一范式要求：所有的字段都是基本数据字段，不可进一步拆分。这里需要确认，所有的列中，每个字段只包含一种数据。**

这张表里把 "property" 这一字段，拆分成 "specification (规格)" 和 "unit (单位)"，这 2 个字段如下：

<img src="mysql-advanced/image-20231106184832839.png" alt="image-20231106184832839" style="zoom:77%;" />

#### 迭代 2 次：考虑 2NF

**第二范式要求：在满足第一范式的基础上，还要满足数据表里的每一条数据记录，都是可唯一标识的。而且所有字段，都必须完全依赖主键，不能只依赖主键的一部分。**

第 1 步，就是要确定这个表的主键。通过观察发现，字段 "listnumber (单号)" + "barcode (条码)" 可以唯一标识每一条记录，可以作为主键。

第 2 步，确定好了主键以后，判断哪些字段完全依赖主键，哪些字段只依赖于主键的一部分。把只依赖于主键一部分的字段拆分出去，形成新的数据表。

首先，进货单明细表里面的 "goodsname (名称)"，"specification (规格)"，"unit (单位)" 这些信息是商品的属性，只依赖于 "barcode (条码)"，不完全依赖主键，可以拆分出去。把这 3 个字段加上它们所依赖的字段 "barcode (条码)"，拆分形成一个新的数据表 "商品信息表"。这样一来，原来的数据表就被拆分成了两个表。

- 商品信息表：

  <img src="mysql-advanced/image-20231106190656366.png" alt="image-20231106190656366" style="zoom: 60%;" />

- 进货单表：

  <img src="mysql-advanced/image-20231106190744501.png" alt="image-20231106190744501" style="zoom: 65%;" />

此外，字段 "supplierid (供应商编号)"，"suppliername (供应商名称)"，"stock (仓库)" 只依赖于 "listnumber (单号)"，不完全依赖于主键，所以，可以把 "supplierid"，"suppliername"，"stock" 这 3 个字段拆出去，再加上它们依赖的字段 "listnumber (单号)"，就形成了一个新的表 "进货单头表"。剩下的字段，会组成新的表，我们叫它 "进货单明细表"。原来的数据表就拆分成了 3 个表。

- 进货单头表：

  <img src="mysql-advanced/image-20231106191110728.png" alt="image-20231106191110728" style="zoom: 60%;" />

- 进货单明细表：

  <img src="mysql-advanced/image-20231106191217636.png" alt="image-20231106191217636" style="zoom:60%;" />

- 商品信息表：

  <img src="mysql-advanced/image-20231106190656366.png" alt="image-20231106190656366" style="zoom: 60%;" />

现在来分析一下拆分后的 3 个表，保证这 3 个表都满足第二范式的要求。

第 3 步，在 "商品信息表" 中，字段 "barcode" 是有可能存在重复的，比如，用户门店可能有散装称重商品和自产商品，会存在条码共用的情况。所以，所有的字段都不能唯一标识表里的记录。这个时候必须给这个表加上一个主键，比如说是自增字段 "itemnumber"。

现在就可以把进货单明细表里面的字段 "barcode" 都替换成字段 "itemnumber"，这就得到了新的表。

- 进货单明细表：

  <img src="mysql-advanced/image-20231106191535891.png" alt="image-20231106191535891" style="zoom:55%;" />

- 商品信息表：

  <img src="mysql-advanced/image-20231106191617188.png" alt="image-20231106191617188" style="zoom: 50%;" />

拆分后的 3 个数据表，就全部满足了第二范式的要求。

#### 迭代 3 次：考虑 3NF

进货单头表还有数据冗余的可能。因为 "supplername" 依赖 "supplierid"，那么，这个时候，就可以按照第三范式的原则进行拆分了。进一步拆分一下进货单头表，把它拆解成供货商表和进货单头表。

- 供货商表：

  <img src="mysql-advanced/image-20231106194914167.png" alt="image-20231106194914167" style="zoom: 67%;" />

- 进货单头表：

  <img src="mysql-advanced/image-20231106194949527.png" alt="image-20231106194949527" style="zoom:67%;" />

这 2 个表都满足第三范式的要求了。

#### 反范式化：业务优先的原则

在进货单明细表中，"quantity * importprice = importvalue"，"importprice"、"quantity" 和 "importvalue"，可以通过任意两个计算出第三个来，这就存在冗余字段。如果严格按照第三范式的要求，应该进行进一步优化。优化的办法是删除其中一个字段，只保留另外 2 个，这样就没有冗余数据了。

可是，真的可以这样做吗？要回答这个问题，就要先了解下实际工作中的业务优先原则。

所谓的业务优先原则，就是指一切以业务需求为主，技术服务于业务。**完全按照理论的设计，不一定就是最优，还要根据实际情况来决定。**这里就来分析一下不同选择的利与弊。

对于 "quantity * importprice =importvalue"，看起来 "importvalue" 似乎是冗余字段，但并不会导致数据不一致，可是，如果把这个字段取消，是会影响业务的。

因为有的时候，供货商会经常进行一些促销活动，按金额促销，那他们拿来的进货单只有金额，没有价格。而 "importprice" 反而是通过 "importvalue / quantity" 计算出来的，经过四舍五入，会产生较大的误差。这样日积月累，最终会导致查询结果出现较大偏差，影响系统的可靠性。

举例：进货金额（importvalue）是 25.5 元，数量（quantity）是 34，那么进货价格（importprice）就等于 25.5 / 34 = 0.74 元，但是如果用这个计算出来的进货价格（importprice）来计算进货金额，那么，进货金额（importvalue）就等于 0.74 x 34 = 25.16元，其中相差了 25.5 - 25.16 = 0.34 元。

所以，本着业务优先的原则，在不影响系统可靠性的前提下，可适当增加数据冗余，保留 "importvalue"，"importprice" 和 "quantity"。

因此，最终我们可以把进货单表拆分成下面的 4 个表：

- 进货单明细表：

  <img src="mysql-advanced/image-20231106201017268.png" alt="image-20231106201017268" style="zoom:67%;" />

- 商品信息表：

  <img src="mysql-advanced/image-20231106201037491.png" alt="image-20231106201037491" style="zoom:55%;" />

- 供货商表：

  <img src="mysql-advanced/image-20231106201059846.png" alt="image-20231106201059846" style="zoom: 67%;" />

- 进货单头表：

  <img src="mysql-advanced/image-20231106201118266.png" alt="image-20231106201118266" style="zoom:67%;" />

这样一来，我们就避免了冗余，而且还能够满足业务的需求，这样的数据表设计，才是合格的设计。

### ER 模型

数据库设计是牵一发而动全身的。那有没有什么办法提前看到数据库的全貌呢?比如需要哪些数据表、数据表中应该有哪些字段，数据表与数据表之间有什么关系、通过什么字段进行连接，等等。这样才能进行整体的梳理和设计。

其实，ER 模型就是一个这样的工具。ER 模型也叫作实体关系模型，是用来描述现实生活中客观存在的事物、事物的属性，以及事物之间关系的一种数据模型。在开发基于数据库的信息系统的设计阶段，通常使用ER模型来描述信息需求和信息特性，帮助我们理清业务逻辑，从而设计出优秀的数据库。

#### ER 模型包括哪些要素

ER 模型中有三个要素，分别是`实体`、`属性`和`关系`。

- `实体`，可以看做是数据对象，往往对应于现实生活中的真实存在的个体。在 ER 模型中，用`矩形`来表示。实体分为两类，分别是强实体和弱实体。强实体是指不依赖于其他实体的实体；弱实体是指对另一个实体有很强的依赖关系的实体。
- `属性`， 则是指实体的特性。比如超市的地址、联系电话、员工数等。在 ER 模型中用`椭圆形`来表示。
- `关系`， 则是指实体之间的联系。比如超市把商品卖给顾客，就是一种超市与顾客之间的联系。在 ER 模型中用`菱形`来表示。

注意：实体和属性不容易区分。这里提供一个原则：要从系统整体的角度出发去看，可以独立存在的是实体，不可再分的是属性。也就是说，属性不能包含其他属性。

#### 关系类型

在 ER 模型的 3 个要素中，关系又可以分为 3 种类型，分别是一对一、一对多、多对多。

- `一对一`：指实体之间的关系是一一对应的，比如个人与身份证信息之间的关系就是一对一的关系。一个人只能有一个身份证信息，一个身份证信息也只属于一个人。
- `一对多`∶指一边的实体通过关系，可以对应多个另外一边的实体。相反，另外一边的实体通过这个关系，则只能对应唯一的一边的实体。比如说，新建一个班级表，而每个班级都有多个学生，每个学生则对应一个班级，班级对学生就是一对多的关系。
- `多对多`：指关系两边的实体都可以通过关系对应多个对方的实体。比如在进货模块中，供货商与超市之间的关系就是多对多的关系，一个供货商可以给多个超市供货，一个超市也可以从多个供货商那里采购商品。再比如一个选课表，有许多科目，每个科目有很多学生选，而每个学生又可以选择多个科目，这就是多对多的关系。

#### 建模分析

ER 模型看起来比较麻烦，但是对我们把控项目整体非常重要。如果你只是开发一个小应用，或许简单设计几个表够用了，一旦要设计有一定规模的应用，在项目的初始阶段，建立完整的 ER 模型就非常关键了。开发应用项目的实质，其实就是`建模`。

此处设计的案例是电商业务，由于电商业务太过庞大且复杂，所以做了业务简化，比如针对 SKU（StockKeepingUnit，库存量单位）和 SPU（Standard Product Unit，标准化产品单元）的含义上，直接使用了 SKU，并没有提及 SPU 的概念。本次电商业务设计总共有 8 个实体，如下所示：

- 地址实体
- 用户实体
- 购物车实体
- 评论实体
- 商品实体
- 商品分类实体
- 订单实体
- 订单详情实体

其中，用户和商品分类是强实体，因为它们不需要依赖其他任何实体。而其他同于弱实体，因为它们虽然都可以独立存在，但是它们都依赖用户这个实体，因此都是弱实体。知道了这些要素就可以给电商业务创建 ER 模型了，如图：

<img src="mysql-advanced/image-20231106201758591.png" alt="image-20231106201758591" style="zoom:80%;" />

在这个图中，地址和用户之间的添加关系，是一对多的关系，而商品和商品详情示一对一的关系，商品和订单是多对多的关系。 这个 ER 模型，包括了 8 个实体之间的 8 种关系。

1. 用户可以在电商平台添加多个地址。
2. 用户只能拥有一个购物车。
3. 用户可以生成多个订单。
4. 用户可以发表多条评论。
5. 一件商品可以有多条评论。
6. 每一个商品分类包含多种商品。
7. 一个订单可以包含多个商品，一个商品可以在多个订单里。
8. 订单中又包含多个订单详情，因为一个订单中可能包含不同种类的商品。

#### ER 模型的细化

有了这个 ER 模型就可以从整体上理解电商的业务了。刚刚的 ER 模型展示了电商业务的框架，但是只包括了订单，地址，用户，购物车，评论，商品，商品分类和订单详情这八个实体，以及它们之间的关系，还不能对应到具体的表，以及表与表之间的关联。需要把属性加上，用椭圆来表示，这样得到的 ER 模型就更加完整了。

因此，我们需要进一步去设计一下这个 ER 模型的各个局部，也就是细化下电商的具体业务流程，然后把它们综合到一起，形成一个完整的 ER 模型。这样可以理清数据库的设计思路。

接下来再分析一下各个实体都有哪些属性，如下所示：

1. 地址实体：包括用户编号、省、市、地区、收件人、联系电话、是否是默认地址。
2. 用户实体：包括用户编号、用户名称、昵称、用户密码、手机号、邮箱、头像、用户级别。
3. 购物车实体：包括购物车编号、用户编号、商品编号、商品数量、图片文件 url。
4. 订单实体：包括订单编号、收货人、收件人电话、总金额、用户编号、付款方式、送货地址、下单时间。
5. 订单详情实体：包括订单详情编号、订单编号、商品名称、商品编号、商品数量。
6. 商品实体：包括商品编号、价格、商品名称、分类编号、是否销售，规格、颜色。
7. 评论实体：包括评论 id、评论内容、评论时间、用户编号、商品编号。
8. 商品分类实体：包括类别编号、类别名称、父类别编号。

这样细分之后就可以重新设计电商业务了，ER 模型如图：

![image-20231106202151621](mysql-advanced/image-20231106202151621.png)

#### ER 模型图转换成数据表

通过绘制 ER 模型已经理清了业务逻辑，现在就要进行非常重要的一步了：把绘制好的 ER 模型，转换成具体的数据表。下面介绍下转换的原则：

1. 一个实体通常转换成一个数据表；
2. 一个多对多的关系，通常也转换成一个 数据表；
3. 一个 1 对 1，或者 1 对多的关系，往往通过表的外键来表达，而不是设计一个新的数据表；
4. 属性转换成表的字段。

下面结合前面的 ER 模型，具体讲解一下怎么运用这些转换的原则，把 ER 模型转换成具体的数据表，从而把抽象出来的数据模型，落实到具体的数据库设计当中。

**1、一个实体通常转换成一个数据表**

先来看一下强实体转换成数据表：

- 用户实体转换成用户表（user_info）的代码如下所示：

  ```mysql
  CREATE TABLE `user_info`(
    `id` bigint(20)NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_name` varchar(200)DEFAULT NULL COMMENT '用户名称',
    `nick_name` varchar (200)DEFAULT NULL COMMENT '用户昵称',
    `passwd` varchar (200)DEFAULT NULL COMMENT '用户密码',
    `phone_num` varchar (200) DEFAULT NULL COMMENT '手机号',
    `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
    `head_img` varchar ( 200)DEFAULT NULL COMMENT'头像',
    `user_level` varchar(200) DEFAULT NULL COMMENT '用户级别',
    PRIMARY KEY (id)
  ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';
  ```

- 商品分类实体转换成商品分类表（base_category），由于商品分类可以有一级分类和二级分类，比如一级分类有家居、手机等等分类，二级分类可以根据手机的一级分类分为手机配件，运营商等，这里我们把商品分类实体规划为两张表，分别是一级分类表和二级分类表，之所以这么规划是因为一级分类和二级分类都是有限的，存储为两张表业务结构更加清晰。

  ```mysql
  # 一级分类表
    CREATE TABLE`base_category1`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` varchar (10) NOT-NULL COMMENT '分类名称',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='一级分类表';
  
  # 二级分类表
  CREATE TABLE `base_category2`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` varchar (208) NOT NULL COMMENT '二级分类名称',
    `category1_id` bigint(20) DEFAULT NULL COMMENT '一级分类编号',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='二级分类表';
  ```

- 那么如果规划为—张表呢，表结构如下所示：

  ```mysql
  CREATE TABLE `base_category`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` varchar (200)NOT NULL COMMENT '分类名不',
    `category_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类编号',
    PRIMARY KEY ( id  ) USING BTREE
  )ENGINE=InnoDB AUTO_INCRENENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT= '分类表';
  ```

  - 如果这样分类的话，那么查询一级分类时候，就需要判断父分类编号是否为空，但是如果插入二级分类的时候也是空，就容易造成业务数据混乱。而且查询二级分类的时候 IS NOT NULL 条件是无法使用到索引的。同时，这样的设计也不符合第二范式（因为父分类编号并不依赖分类编号 ID，因为父分类编号可以有很多数据为 NULL)，所以需要进行表的拆分。因此无论是业务需求还是数据库表的规范来看都应该拆分为两张表。

下面再把弱实体转换成数据表：

- 地址实体转换成地址表（user_address），如下所示：

  ```mysql
  CREATE TABLE `user_address`(
  `id` bigint(20)NOT NULL AUTO_INCREMENT COMMENT '编号',
  'province' varchar (500)DEFAULT NULL COMMENT'省',
  `city` varchar (500) DEFAULT NULL COMMENT '市',
  `user_address` varchar (500) DEFAULT NULL COMMENT '具体地址',
  `user_id bipint(20)` DEFAULT NULL COMMENT '用户id',
  `consignee` varchar( 40) DEFAULT NULL COMMENT '收件人',
  `phone_num ` varchar(40) DEFAULT NULL COMMENT ‘联系方式',
  `is_default` varchar( 1) DEFAULT NULL COMMENT '是否是默认',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户地址表';
  ```

- 订单实体转换成订单表（order_info)，如下所示，实际业务中订单的信息会非常多，我们这里做了简化。

  ```mysql
  CREATE TABLE `order_info`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `consignee` varchar (100) DEFAULT NULL COMMENT '收货人',
    `consignee_tel` varchar(20) DEFAULT NULL COMMENT'收件人电话',
    `total_amount` decimal( 10,2)DEFAULT NULL COMMENT '总金额',
    `user_id` bigint(20) DEFAULT NULL COMMENT'用户id',
    `payment_way` varchar(20)DEFAULT NULL COMMENT'付款方式',
    `delivery_address` varchar( 1000) DEFAULT NULL COMMENT'送货地址',
    `create_time` datetime DEFAULT NULL COMMENT'下单时间',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCRENENT=1 DEFAULT CHARSET=utf8 ROW_FORNAT=DYNAMIC COMMENT= '订单表';
  ```

- 订单详情实体转换成订单详情表（order_detail)，如下所示。（用于体现多对多关系的，见下节）：

  ```mysql
  # 订单详情表
  CREATE TABLE `order_detail`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单详情编号',
    `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
    `sku_id` bigint(20)DEFAULT NULL COMMENT 'sku_id',
    `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
    `sku_num` varchar(200) DEFAULT NULL COMMENT '购买个数',
    `create_time` datetime DEFAULT NULL COMMENT'操作时间',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单明细表';
  ```

- 购物车实体转换成购物车表（cart_info），如下所示：

  ```mysql
  CREATE TABLE `cart_info`(
    `cart_id` bigint(20)NOT NULL AUTO_INCREMENT COMMENT'编号',
    `user_id` varchar(200) DEFAULT NULL COMMENT'用户id',
    `sku_id` bigint(20)DEFAULT NULL COMMENT 'skuid' ,
    `sku_num` int( 11)DEFAULT NULL COMMENT '数量',
    `img_url` varchar ( 500) DEFAULT NULL COMMENT '图片文件',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCRENENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='购物车表';
  ```

- 评论实体转换成评论表（members），如下所示：

  ```mysql
  CREATE TABLE `sku_comments`(
    `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT'评论编号',
    `user_id` bigin)t (20) DEFAULT NULL COMMENT'用户编号',
    `sku_id` decimal( 10,0) DEFAULT NULI COMMENT '商品编号',
    `comment` varchar(2000)DEFAULT NULL COMMENT '评论内容',
    `create_time` datetime DEFAULT NULL COMMENT '评论时间',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCRENENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMNENT='商品评论表';
  ```

- 商品实体转换成商品表（members），如下所示：

  ```mysql
  CREATE TABLE `sku_info`(
    `sku_id` bigint(20) NOT NULL AUTO_INCREMENT COPMENT'商品编号(itemID)',
     `price` decimal(10,0) DEFAULT NULL COMMENT'价格',
    `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
    `sku_desc` varchar(2000) DEFAULT NULL COMMENT'商品规格描述',
    `category3_id` bigint(20) DEFAULT NULL COMMENT'三级分类id(冗余)',
    `color` varchar (2000) DEFAULT NULL COMMENT '颜色',
    `is_sale` tinyint(3) NOT NULL DEFAULT '0' CONMMENT'是否销售(1:是0:否)',
    PRIMARY KEY (`id`) USING BTREE
  )ENGINE=InnoDB AUTO_INCRENENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT= '商品表';
  ```

**2、一个多对多的关系转换成一个数据表**

这个 ER 模型中的多对多的关系有 1 个，即商品和订单之间的关系，同品类的商品可以出现在不同的订单中，不同的订单也可以包含同一类型的商品，所以它们之间的关系是多对多。针对这种情况需要设计一个独立的表来表示，这种表一般称为中间表。

我们可以设计一个独立的订单详情表，来代表商品和订单之间的包含关系。这个表关联到 2 个实体，分别是订单、商品。所以，表中必须要包括这 2 个实体转换成的表的主键。除此之外，我们还要包括该关系自有的属性：商品数量，商品下单价格以及商品名称。

```mysql
# 订单详情表
CREATE TABLE `order_detail`(
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单详情编号',
`order_id` bigint(20)DEFAULT NULL COMMENT '订单编号',
`sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id ',
`sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
`sku_num` varchar(200)DEFAULT NULL COMMENT '购买个数',
`create_time` datetime DEFAULT NULL COMMENT '操作时间',
PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB AUTO_INCRENENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单明细表';
```

> 公司的订单相关表主要有：order、order_item、sku、spu…，其中 order_detail 相当于 order_item。

**3、通过外键来表达1对多的关系**

在上面的表的设计中，我们可以用外键来表达一对多的关系。比如在商品评论表 sku_comments中，我们分别把 user_id、sku_id 定义成外键，以使用下面的语句设置外键。

```mysql
CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES user_info (id)

CONSTRAINT fk_comment_sku FOREIGN KEY (sku_id) REFERENCES sku_info (sku_id)
```

外键约束主要是在数据库层面上保证数据的一致性，但是因为插入和更新数据需要检查外键，理论上性能会有所下降，对性能是负面的影响。

`实际的项目，不建议使用外键。`一方面是降低开发的复杂度（有外键的话主从表类的操作必须先操作主表），另外是有外键在处理数据的时候非常麻烦。在电商平台，由于并发业务量比较大，所以一般不设置外键，以免影响数据库性能。

在应用层面做数据的一致性检查，本来就是一个正常的功能需求。如学生选课的场景，课程肯定不是输入的，而是通过下拉或查找等方式从系统中进行选取，就能够保证是合法的课程 ID，因此就不需要靠数据库的外键来检查了。

**4、把属性转换成表的字段**

在刚刚的设计中，我们也完成了把属性都转换成了表的字段，比如把商品属性转换成了商品信息表中的字段。

```mysql
CREATE TABLE `sku_info`(
  `sku_id` bigint(20) NOT NULL AUTO_INCREMENT COPMENT'商品编号(itemID)',
   `price` decimal(10,0) DEFAULT NULL COMMENT'价格',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
  `sku_desc` varchar(2000) DEFAULT NULL COMMENT'商品规格描述',
  `category3_id` bigint(20) DEFAULT NULL COMMENT'三级分类id(冗余)',
  `color` varchar (2000) DEFAULT NULL COMMENT '颜色',
  `is_sale` tinyint(3) NOT NULL DEFAULT '0' CONMMENT'是否销售(1:是0:否)',
  PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB AUTO_INCRENENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT= '商品表';
```

到这里，我们通过创建电商项目业务流程的 ER 模型，再把 ER 模型转换成具体的数据表的过程，完成利用 ER 模型设计电商项目数据库的工作。

其实，任何一个基于数据库的应用项目，都可以通过这种先建立 ER 模型，再转换成数据表的方式，完成数据库的设计工作。创建 ER 模型不是目的，目的是把业务逻辑梳理清楚，设计出优秀的数据库。不是为了建模而建模，要利用创建 ER 模型的过程来整理思路，这样创建 ER 模型才有意义。

<img src="mysql-advanced/image-20231106205251437.png" alt="image-20231106205251437" style="zoom:80%;" />

### 数据表的设计原则

综合以上内容，总结出数据表设计的一般原则：`三少一多`。

1. 数据表的个数越少越好。
   - RDBMS 的核心在于对实体和联系的定义，也就是 E-R 图（Entity Relationship Diagram），数据表越少，证明实体和联系设计得越简洁，既方便理解又方便操作。
2. 数据表中的字段个数越少越好。
   - 字段个数越多，数据冗余的可能性越大。设置字段个数少的前提是各个字段相互独立，而不是某个字段的取值可以由其他字段计算出来。当然字段个数少是相对的，通常会在数据冗余和检索效率中进行平衡。
3. 数据表中联合主键的字段个数越少越好。
   - 设置主键是为了确定唯一性，当一个字段无法确定唯一性的时候，就需要采用联合主键的方式（也就是用多个字段来定义一个主健）。联合主键中的字段越多，占用的索列空间越大，不仅会加大理解难度，还会增加运行时间和索引空间，因此联合主键的字段个数越少越好。
4. 使用主键和外键越多越好。
   - 数据库的设计实际上就是定义各种表，以及各种字段之间的关系。这些关系越多，证明这些实体之间的冗余度越低，利用度越高。这样做的好处在于不仅保证了数据表之间的独立性，还能提升相互之间的关联使用率。
   - 这里的外键指业务上实现外键，也就是逻辑外键。不一定使用外键约束实现。

"三少一多" 原则的核心就是简单可复用。简单指的是用更少的表、更少的字段、更少的联合主键字段来完成数据表的设计。可复用则是通过主键、外键的使用来增强数据表之间的复用率。因为一个主键可以理解是一张表的代表。键设计得越多，证明它们之间的利用率越高。

>注意：这个原则并不是绝对的，有时候我们需要牺牲数据的冗余度来换取数据处理的效率。

### 数据库对象编写建议

前面讲了数据库的设计规范，下面给出的这些规范适用于大多数公司，按照下面的规范来使用数据库，这样数据库可以发挥出更高的性能。

#### 关于库

1. 【强制】库的名称必须控制`在 32 个字符以内`，只能使用英文字母、数字和下划线，建议以英文字母开头。
2. 【强制】库名中英文一律`小写`，不同单词采用`下划线分割`，须见名知意。
3. 【强制】库的名称格式：`业务系统名称_子系统名`。
4. 【强制】库名`禁止使用关键字`，如 type，order 等。
5. 【强制】创建数据库时必须显式指定字符集，并且字符集只能是`utf8 或者 utf8mb4`。创建数据库 SQL 举例：CREATE DATABASE crm_fund DEFAULT CHARACTER SET 'utf8'。
6. 【建议】对于程序连接数据库账号，遵循`权限最小原则`。使用数据库账号只能在一个 DB 下使用，不准跨库。程序使用的账号原则上不准有 drop 权限。
7. 【建议】临时库以`tmp_`为前缀，并以日期为后缀；备份库以`bak_`为前缀，并以日期为后缀。

#### 关于表、列

1. 【强制】表和列的名称必须控制在 32 个字符以内，表名只能使用英文字母、数字和下划线，建议以英文字母开头。

2. 【强制】 表名、列名一律小写，不同单词采用下划线分割，须见名知意。

3. 【强制】表名要求有模块名强相关，同一模块的表名尽量使用统一前缀。比如：crm_fund_item。

4. 【强制】创建表时必须显式指定字符集为 utf8 或 utf8mb4。

5. 【强制】表名、列名禁止使用关键字，如 type，order 等。

6. 【强制】创建表时必须显式指定表存储引擎类型。如无特殊需求，一律为 InnoDB。

7. 【强制】建表`必须有 comment`。

8. 【强制】字段命名应尽可能使用表达实际含义的英文单词或缩写。如：公司 ID，不要使用 corporation_id，而用 corp_id 即可。

9. 【强制】布尔值类型的字段命名为`is_`描述。如 member 表上表示是否为 enabled 的会员的字段命名为 is_enabled。

10. 【强制】`禁止在数据库中存储图片、文件等大的二进制数据。`通常文件很大，短时间内造成数据量快速增长，数据库进行数据库读取时，通常会进行大量的随机 I/O 操作，文件很大时，I/O 操作很耗时。通常存储于文件服务器（如 FastDFS），数据库只存储文件地址信息。

11. 【建议】建表时关于主键：表必须有主键。

    - 强制要求主键为 id，类型为 int 或 bigint，且为 auto_increment，建议使用 unsigned 无符号型。
    - 标识表里每一行主体的字段不要设为主键，建议设为其他字段如 user_id，order_id 等，并建立 unique key 索引。因为如果设为主键且主键值为随机插入，则会导致 InnoDB 内部页分裂和大量随机 I/O，性能下降。

12. 【建议】核心表（如用户表）必须有行数据的创建时间字段（create_time）和最后更新时间字段（update_time），便于查问题。

13. 【建议】表中所有字段尽量都是 NOT NULL 属性，业务可以根据需要定义 DEFAULT 值。 因为使用 NULL 值会存在每一行都会占用额外存储空间、数据迁移容易出错、聚合函数计算结果偏差、业务代码容易出现空指针等问题。

14. 【建议】所有存储相同数据的列名和列类型必须一致（一般作为关联列，如果查询时关联列类型不一致会自动进行数据类型隐式转换，会造成列上的索引失效，导致查询效率降低）。

15. 【建议】中间表（或临时表）用于保留中间结果集，名称以`tmp_`开头。备份表用于备份或抓取源表快照，名称以`bak_`开头。中间表和备份表定期清理。

16. 【示范】一个较为规范的建表语句：

    ```mysql
    CREATE TABLE user_info (
      `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
      `user_id` bigint(11) NOT NULL COMMENT '用户id',
      `username` varchar(45) NOT NULL COMMENT '真实姓名',
      `email` varchar(30) NOT NULL COMMENT '用户邮箱',
      `nickname` varchar(45) NOT NULL COMMENT '昵称',
      `birthday` date NOT NULL COMMENT '生日',
      `sex` tinyint(4) DEFAULT '0' COMMENT '性别',
      `short_introduce` varchar(150) DEFAULT NULL COMMENT '一句话介绍自己，最多50个汉字',
      `user_resume` varchar(300) NOT NULL COMMENT '用户提交的简历存放地址',
      `user_register_ip` int NOT NULL COMMENT '用户注册时的源ip',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      `user_review_status` tinyint NOT NULL COMMENT '用户资料审核状态，1为通过，2为审核中，3为未通过，4为还未提交审核',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uniq_user_id` (`user_id`),
      KEY `idx_username`(`username`),
      KEY `idx_create_time_status`(`create_time`,`user_review_status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网站用户基本信息';
    ```

17. 【建议】创建表时，可以使用可视化工具。这样可以确保表、字段相关的约定都能设置上。实际上，我们通常很少自己写 DDL 语句，可以使用一些可视化工具来创建和操作数据库和数据表。可视化工具除了方便，还能直接帮我们将数据库的结构定义转化成 SQL 语言，方便数据库和数据表结构的导出和导入。

#### 关于索引

1. 【强制】InnoDB 表必须主键为 id int/bigint auto_increment，且主键值禁止被更新。
2. 【强制】InnoDB 和 MyISAM 存储引擎表，索引类型必须为 BTREE（此处是官方写法，代表的其实就是 B+Tree）。
3. 【建议】主键的名称以`pk_`开头，唯一键以`uni_`或`uk_`开头，普通索引以`idx_`开头，一律使用小写格式，以字段的名称或缩写作为后缀。
4. 【建议】多单词组成的 columnname，取前几个单词首字母，加末单词组成 column_name。如 sample 表 member_id 上的索引：idx_sample_mid。
5. 【建议】单个表上的索引个数不能超过 6 个。
6. 【建议】在建立索引时，多考虑建立联合索引，并把区分度最高的字段放在最前面。
7. 【建议】在多表 JOIN 的 SQL 里，保证被驱动表的连接列上有索引，这样 JOIN 执行效率最高。
8. 【建议】建表或加索引时，保证表里互相不存在冗余索引。 比如：如果表里已经存在 key(a, b)， 则 key(a) 为冗余索引，需要删除。

#### SQL 编写

1. 【强制】程序端 SELECT 语句必须指定具体字段名称，禁止写成 *。
2. 【建议】程序端 INSERT 语句指定具体字段名称，不要写成 INSERT INTO t1 VALUES(…)。
3. 【建议】除静态表或小表（100 行以内），DML 语句必须有 WHERE 条件，且使用索引查找。
4. 【建议】INSERT INTO…VALUES(XX), (XX), (XX)…，这里 XX 的值不要超过 5000 个。值过多虽然上线很快，但会引起主从同步延迟。
5. 【建议】SELECT 语句不要使用 UNION，推荐使用 UNION ALL，并且 UNION 子句个数限制在 5 个以内。
6. 【建议】线上环境，多表 JOIN 不要超过 5 个表。
7. 【建议】减少使用 ORDER BY，和业务沟通能不排序就不排序，或将排序放到程序端去做。`ORDER BY、GROUP BY、DISTINCT 这些语句较为耗费 CPU，数据库的 CPU 资源是极其宝贵的。`
8. 【建议】包含了 ORDER BY、GROUP BY、DISTINCT 这些查询的语句，WHERE 条件过滤出来的结果集请保持在 1000 行以内，否则 SQL 会很慢。
9. 【建议】对单表的多次 ALTER 操作必须合并为一次。对于超过 100 W 行的大表进行 ALTER TABLE，必须经过 DBA 审核，并在业务低峰期执行，多个 ALTER 需整合在一起。 因为 ALTER TABLE 会产生表锁，期间阻塞对于该表的所有写入，对于业务可能会产生极大影响。
10. 【建议】批量操作数据时，需要控制事务处理间隔时间，进行必要的 sleep。
11. 【建议】事务里包含 SQL 不超过 5 个。因为过长的事务会导致锁数据较久，MySQL 内部缓存、连接消耗过多等问题。
12. 【建议】事务里更新语句尽量基于主键或 UNIQUE KEY，如 UPDATE… WHERE id = XX，否则会产生间隙锁，内部扩大锁定范围，导致系统性能下降，产生死锁。

## 数据库其他调优策略

### 数据库调优的措施

#### 调优的目标

- 尽可能`节省系统资源`，以便系统可以提供更大负荷的服务（吞吐量更大）。
- 合理的结构设计和参数调整，以提高用户`操作响应的速度`（响应速度更快）。
- 减少系统的瓶颈，提高 MySQL 数据库整体的性能。

#### 如何定位调优问题

不过随着用户量的不断增加，以及应用程序复杂度的提升，我们很难用 "更快" 去定义数据库调优的目标，因为用户在不同时间段访问服务器遇到的瓶颈不同，比如双十一促销的时候会带来大规模的并发访问。还有用户在进行不同业务操作的时候，数据库的事务处理和 SQL 查询都会有所不同。因此还需要更加精细的定位，去确定调优的目标。

如何确定呢？一般情况下，有如下几种方式：

- **用户的反馈（主要）**
  - 用户是服务的对象，因此他们的反馈是最直接的。
  - 虽然他们不会直接提出技术建议，但是有些问题往往是用户第一时间发现的。要重视用户的反馈，找到和数据相关的问题。
- **日志分析（主要）**
  - 可以通过查看数据库日志和操作系统日志等方式找出异常情况，通过它们来定位遇到的问题。
  - 服务器资源使用监控，通过监控服务器的 CPU、内存、I/O 等使用情况，可以实时了解服务器的性能使用，与历史情况进行对比。
- **数据库内部状况监控**
  - 在数据库的监控中，活动会话（Active Session）监控是一个重要的指标。通过它可以清楚地了解数据库当前是否处于非常繁忙的状态，是否存在 SQL 堆积等。
- **其它**
  - 除了活动会话监控以外也可以对事务、锁等待等进行监控，这些都可以帮助我们对数据库的运行状态有更全面的认识。

#### 调优的维度和步骤

`需要调优的对象是整个数据库管理系统，它不仅包括 SQL 查询，还包括数据库的部署配置、架构等。`从这个角度来说，思考的维度就不仅仅局限在 SQL 优化上了。通过如下的步骤进行梳理：

##### 第 1 步：选择适合的 DBMS

如果对事务性处理以及安全性要求高的话，可以选择商业的数据库产品。这些数据库在事务处理和查询性能上都比较强，比如采用 SQL Server、Oracle，那么单表存储上亿条数据是没有问题的。如果数据表设计得好，即使不采用分库分表的方式，查询效率也不差。

除此以外也可以采用开源的 MySQL 进行存储，它有很多存储引擎可以选择，如果进行事务处理的话可以选择 InnoDB，非事务处理可以选择 MyISAM。

NoSQL 阵营包括键值型数据库、文档型数据库、搜索引擎，列式存储和图形数据库。这些数据库的优缺点和使用场景各有不同，比如列式存储数据库可以大幅度降低系统的 I/O，适合于分布式文件系统，但如果数据需要频繁地增删改，那么列式存储就不太适用了。

DBMS 的选择关系到了后面的整个设计过程，所以第一步就是要选择适合的 DBMS。如果已经确定好了 DBMS，那么这步可以跳过。

##### 第 2 步：优化表设计

选择了 DBMS 之后就需要进行表设计了。而数据表的设计方式也直接影响了后续的 SQL 查询语句。RDBMS 中，每个对象都可以定义为一张表，表与表之间的关系代表了对象之间的关系。如果用的是 MySQL，还可以根据不同表的使用需求，选择不同的存储引擎。除此以外，还有一些优化的原则可以参考：

- 表结构要尽量遵循三范式的原则。这样可以让数据结构更加清晰规范，减少冗余字段，同时也减少了在更新，插入和删除数据时等异常情况的发生。
- 如果查询应用比较多，尤其是需要进行多表联查的时候，可以采用反范式进行优化。反范式采用空间换时间的方式，通过增加冗余字段提高查询的效率。
- 表字段的数据类型选择，关系到了查询效率的高低以及存储空间的大小。一般来说，如果字段可以采用数值类型就不要采用字符类型。字符长度要尽可能设计得短一些。针对字符类型来说，当确定字符长度固定时，就可以采用 CHAR 类型。当长度不固定时，通常采用 VARCHAR 类型。

数据表的结构设计很基础，也很关键。好的表结构可以在业务发展和用户量增加的情况下依然发挥作用，不好的表结构设计会让数据表变得非常臃肿，查询效率也会降低。

##### 第 3 步：优化逻辑查询

当建立好数据表之后，就可以对数据表进行增删改查的操作了。这时首先需要考虑的是逻辑查询优化。

`SQL 查询优化，可以分为逻辑查询优化和物理查询优化。`逻辑查询优化就是通过改变 SQL 语句的内容，让 SQL 执行效率更高效，采用的方式是对 SQL 语句进行等价变换，对查询进行重写。

SQL 的查询重写包括了子查询优化、等价谓词重写、视图重写、条件简化、连接消除和嵌套连接消除等。

比如 EXISTS 子查询和 IN 子查询，会根据小表驱动大表的原则选择适合的子查询。在 WHERE 子句中会尽量避免对字段进行函数运算，它们会让字段的索引失效。示例如下：

- 查询评论内容开头为 abc 的内容都有哪些，如果在 WHERE 子句中使用了函数，语句就会写成下面这样：

  ```mysql
  SELECT comment_id, comment_text, comment_time FROM product_comment WHERE SUBSTRING(comnment_text, 1, 3) = 'abc';
  ```

- 采用查询重写的方式进行等价替换：

  ```mysql
  SELECT comment_id, comment_text, comment_time FROM product_comment WHERE comment_text LIKE 'abc%';
  ```

##### 第 4 步：优化物理查询

物理查询优化是在确定了逻辑查询优化之后，采用物理优化技术（比如索引等），通过计算代价模型对各种可能的访问路径进行估算，从而找到执行方式中代价最小的作为执行计划。在这个部分中需要掌握的重点是对索引的创建和使用。

但索引不是万能的，要根据实际情况来创建索引。那么都有哪些情况需要考虑呢？这在前面几章中已经进行了细致的剖析。

SQL 查询时需要对不同的数据表进行查询，因此在物理查询优化阶段也需要确定这些查询所采用的路径，具体的情况包括：

- 单表扫描：对于单表扫描来说，可以全表扫描所有的数据，也可以局部扫描。
- 两张表的连接：常用的连接方式包括了嵌套循环连接、 HASH 连接和合并连接。
- 多张表的连接：多张数据表进行连接的时候，顺序很重要，因为不同的连接路径查询的效率不同，搜索空间也会不同。在进行多表连接的时候，搜索空间可能会达到很高的数据量级，巨大的搜索空间显然会占用更多的资源，因此需要通过调整连接顺序，将搜索空间调整在一个可接受的范围内。

##### 第 5 步：使用 Redis 或 Memcached 作为缓存

除了可以对 SQL 本身进行优化以外，还可以请外援提升查询的效率。

因为数据都是存放到数据库中，需要从数据库层中取出数据放到内存中进行业务逻辑的操作，当用户量增大的时候，如果频繁地进行数据查询，会消耗数据库的很多资源。如果将常用的数据直接放到内存中，就会大幅提升查询的效率

键值存储数据库可以帮我们解决这个问题。

常用的键值存储数据库有 Redis 和 Memcached，它们都可以将数据存放到内存中。

从可靠性来说， Redis 支持持久化，可以让我们的数据保存在硬盘上，不过这样一来性能消耗也会比较大。而 Memcached 仅仅是内存存储，不支持持久化。

通常对于查询响应要求高的场景（响应时间短，吞吐量大)，可以考虑内存数据库，毕竟术业有专攻。传统的 RDBMS，都是将数据存储在硬盘上，而内存数据库则存放在内存中，查询起来要快得多。不过使用不同的工具，也增加了开发人员的使用成本。

##### 第 6 步：库级优化

库级优化是站在数据库的维度上进行的优化策略，比如控制一个库中的数据表数量。另外，单一的数据库总会遇到各种限制，不如取长补短，利用 "外援" 的方式。通过`主从架构`优化读写策略，通过对数据库进行垂直或者水平切分，突破单一数据库或数据表的访问限制，提升查询的性能。

###### 读写分离

如果读和写的业务量都很大，并且它们都在同一个数据库服务器中进行操作，那么数据库的性能就会出现瓶颈，这时为了提升系统的性能，优化用户体验，可以采用`读写分离`的方式降低主数据库的负载，比如用主数据库（master）完成写操作，用从数据库（slave）完成读操作。

<img src="mysql-advanced/image-20231107091127166.png" alt="image-20231107091127166" style="zoom:67%;" />

<img src="mysql-advanced/image-20231107091152976.png" alt="image-20231107091152976" style="zoom:67%;" />

###### 数据分片

对数据库分库分表。当数据量级达到千万级以上时，有时候要把一个数据库切成多份，放到不同的数据库服务器上，减少对单一数据库服务器的访问压力。如果你使用的是 MySQL，就可以使用 MySQL 自带的分区表功能，当然你也可以考虑自己做`垂直拆分 (分库)`、`水平拆分 (分表)`、`垂直 + 水平拆分 (分库分表)`。

<img src="mysql-advanced/image-20231107091350062.png" alt="image-20231107091350062" style="zoom:67%;" />

<img src="mysql-advanced/image-20231107091417143.png" alt="image-20231107091417143" style="zoom:80%;" />

>- 垂直分表：比如按照热数据、冷数据进行分表。
>- 水平分表：比如按照日期范围进行划分。
>
>但需要注意的是，分拆在提升数据库性能的同时，也会增加维护和使用成本

### 优化 MySQL 服务器

优化 MySQL 服务器主要从两个方面来优化，一方面是对`硬件`进行优化。另一方面是对`MySQL 服务的参数`进行优化。这部分的内容需要较全面的知识，一般只有**专业的数据库管理员**才能进行这一类的优化。对于可以定制参数的操作系统，也可以针对 MySQL 进行操作系统优化。

#### 优化服务器硬件

服务器的硬件性能直接决定着 MySQL 数据库的性能。硬件的性能瓶颈直接决定 MySQL 数据库的运行速度和效率。针对性能瓶颈提高硬件配置，可以提高 MySQL 数据库查询、更新的速度。

1. 配置`较大的内存`。足够大的内存是提高 MySQL 数据库性能的方法之一。内存的速度比磁盘 I/O 快得多，可以通过增加系统的缓冲区容量使数据在内存中停留的时间更长，以读少磁盘 I/O。
2. 配置`高速磁盘系统`，以减少读盘的等待时间，提高响应速度。磁盘的 I/O 能力，也就是它的寻道能力，目前的 SCSI 高速旋转的是 7200 转/分钟，这样的速度，一旦访问的用户量上去，磁盘的压力就会过大，如果是每天的网站 pv（page view）在 150 w，这样的一般的配置就无法满足这样的需求了。现在 SSD 盛行，在 SSD 上随机访问和顺序访问性能几乎差不多，使用 SSD 可以减少随机 I/O 带来的性能损耗。
3. `合理分布磁盘 I/O`，把磁盘 I/O 分散在多个设备上，以减少资源竞争，提高并行操作能力。
4. 配置`多处理器`，MySQL 是多线程的数据库，多处理器可同时执行多个线程。

#### 优化 MySQL 服务的参数

通过优化 MySQL 服务的参数可以提高资源利用率，从而达到提高 MySQL 服务器性能的目的。

MySQL 服务的配置参数都在`my.cnf`或者`my.ini`文件的`[mysqld]`组中，配置完参数以后，需要重新启动 MySQL 服务才会生效。

下面对几个对性能影响比较大的参数进行详细介绍：

- `innodb_buffer_pool_size`：这个参数是 MySQL 数据库最重要的参数之一，表示 InnoDB 类型的`表和索引的最大缓存`。它不仅仅缓存索引数据，还会缓存表的数据。这个值越大，查询的速度就会越快。但是这个值太大会影响操作系统的性能。

- `key_buffer_size`：表示`索引缓冲区的大小`。`索引缓冲区是所有的线程共享`。增加索引缓冲区可以得到更好处理的索引（对所有读和多重写）。当然，这个值不是越大越好，它的大小取决于内存的大小。如果这个值太大，就会导致操作系统频繁换页，也会降低系统性能。对于内存在 4 GB 左右的服务器该参数可设置为 256 MB 或 384 MB。

- `table_cache`：表示`同时打开的表的个数`。这个值越大，能够同时打开的表的个数越多。物理内存越大，设置就越大。默认为 2402，调到 512 ~ 1024 最佳。这个值不是越大越好，因为同时打开的表太多会影响操作系统的性能。

- `query_cache_size`：表示查询缓冲区的大小。可以通过在 MySQL 控制台观察，如果 qcache_lowmem_prunes 的值非常大，则表明经常出现缓冲不够的情况，就要增加 query_cache_size 的值；如果 qcache_hits 的值非常大，则表明查询缓冲使用非常频繁，如果该值较小反而会影响效率，那么可以考虑不用查询缓存。

- `qcache_free_blocks`：如果该值非常大，则表明缓冲区中碎片很多。MySQL 8.0 之后失效。该参数需要和 query_cache_type 配合使用。

- `query_cache_type`：值是 0 时，所有的查询都不使用查询缓存区。但是 query_cache_type = 0 并不会导致 MySQL 释放 query_cache_size 所配置的缓存区内存。

  - 当 query_cache_type = 1 时，所有的查询都将使用查询缓存区，除非在查询语句中指定 SQL_NO_CACHE，如 SELECT SQL_NO_CACHE FROM tbl_name。
  - 当 query_cache_type = 2 时，只有在查询语句中使用 SQL_CACHE 关键字，查询才会使用查询缓存区。使用查询缓存区可以提高查询的速度，这种方式只适用于修改操作少且经常执行相同的查询操作的情况。

- `sort_buffer_size`：表示每个`需要进行排序的线程分配的缓冲区的大小`。增加这个参数的值可以提高 ORDER BY 或 GROUP BY 操作的速度。默认数值是 2097144 字节（约 2 MB）。对于内存在 4 GB 左右的服务器，推荐设置为 6 ~ 8 MB，如果有 100 个连接，那么实际分配的总共排序缓冲区大小为 100 × 6 = 600 MB。

- `join_buffer_size = 8M`：表示`联合查询操作所能使用的缓冲区大小`，和sort_buffer_size一样，该参数对应的分配内存也是每个连接独享。

- `read_buffer_size`：表示`每个线程连续扫描时为扫描的每个表分配的缓冲区的大小 (字节)`。当线程从表中连续读取记录时需要到这个缓冲区。SET SESSION read_buffer_size=n 可以临时设置该参数的值。默认为 64 KB，可以设置为 4 MB。

- `innodb_flush_log_at_trx_commit`：表示`何时将缓冲区的数据写入日志文件`，并且将日志文件写入磁盘中。该参数对于 InnoDB 引擎非常重要。该参数有 3 个值，分别为 0、1 和 2。该参数的默认值为 1。

  - 值为 0 时，表示`每秒 1 次`的频率，将数据写入日志文件并将日志文件写入磁盘。每个事务的 commit 并不会触发前面的任何操作。该模式速度最快，但不太安全，mysqld 进程的崩溃会导致上一秒钟所有事务数据的丢失。
  - 值为 1 时，表示`每次提交事务时`，将数据写入日志文件并将日志文件写入磁盘进行同步。该模式是最安全的，但也是最慢的一种方式。因为每次事务提交或事务外的指令都需要把日志写入（flush）硬盘。
  - 值为 2 时，表示`每次提交事务时`，将数据写入日志文件，每隔 1 秒将日志文件写入磁盘。该模式速度较快，也比 0 安全，只有在操作系统崩溃或者系统断电的情况下，上一秒钟所有事务数据才可能丢失。

- `innodb_log_buffer_size`：这是 InnoDB 存储引擎的`事务日志使用的缓冲区`。为了提高性能，也是先将信息写入 Innodb Log Buffer中，当满足 innodb_flush_log_trx_commit 参数所设置的相应条件（或者日志缓冲区写满）之后，才会将日志写到文件（或者同步到磁盘）中。

- `max_connections`：表示`允许连接到 MySQL 数据库的最大数量`，默认值是 151。如果状态变量 connection_errors_max_connections 不为零，并且一直增长，则说明不断有连接请求因数据库连接数已达到允许最大值而失败，这时可以考虑增大 max_connections 的值，在 Linux 平台下，性能好的服务器，支持 500 ~ 1000 个连接不是难事，需要根据服务器性能进行评估设定。这个连接数不是越大越好，因为这些连接会浪费内存的资源，过多的连接可能会导致 MySQL 服务器僵死。

- `back_log`：用于控制 MySQL 监听 TCP 端口时，设置的`积压请求栈大小`。如果 MySQL 的连接数达到 max_connections 时，新来的请求将会被存在堆栈中，以等待某一连接释放资源，该堆栈的数量即 back_log，如果等待连接的数量超过 back_log，将不被授予连接资源，将会报错。5.6.6 版本之前默认值为 50， 之后的版本默认为 50 +  (max_connections / 5)，对于 Linux 系统推荐设置为小于 512 的整数，最大不超过 900。

  - 如果需要数据库在较短的时间内处理大量连接请求， 可以考虑适当增大 back_log 的值。

- `thread_cache_size`：`线程池缓存线程数量的大小`，当客户端断开连接后将当前线程缓存起来，当在接到新的连接请求时快速响应无需创建新的线程。这尤其对那些使用短连接的应用程序来说可以极大的提高创建连接的效率。那么为了提高性能可以增大该参数的值。默认为 60，可以设置为 120。

  - 可以通过如下几个 MySQL 状态值，来适当调整线程池的大小：

    ```mysql
    show global status like 'Thread%';
    /*
    +-------------------+-------+
    | Variable_name | Value |
    +-------------------+-------+
    | Threads_cached | 2 |
    | Threads_connected | 1 |
    | Threads_created | 3 |
    | Threads_running | 2 |
    +-------------------+-------+
    */
    ```

  - 当 Threads_cached 越来越少，但 Threads_connected 始终不降，且 Threads_created 持续升高，可适当增加 thread_cache_size 的大小。

- `wait_timeout `：指定`一个请求的最大连接时间 `，对于 4 GB 左右内存的服务器，可以设置为 5 ~ 10。

- `interactive_timeout `：表示服务器在关闭连接前等待行动的秒数。

这里给出一份 my.cnf 的参考配置：

```cnf
[mysqld]
port = 3306 
serverid = 1 
socket = /tmp/mysql.sock 
# 避免MySQL的外部锁定，减少出错几率增强稳定性
skip-locking 
# 禁止MySQL对外部连接进行DNS解析，使用这一选项可以消除MySQL进行DNS解析的时间。但需要注意，如果开启该选项，则所有远程主机连接授权都要使用IP地址方式，否则MySQL将无法正常处理连接请求back_log = 384
skip-name-resolve 
key_buffer_size = 256M 
max_allowed_packet = 4M 
thread_stack = 256K
table_cache = 128K 
sort_buffer_size = 6M 
read_buffer_size = 4M
read_rnd_buffer_size=16M 
join_buffer_size = 8M 
myisam_sort_buffer_size =64M t
able_cache = 512 thread_cache_size = 64 query_cache_size = 64M
tmp_table_size = 256M 
max_connections = 768 
max_connect_errors = 10000000
wait_timeout = 10 
# 该参数取值为服务器逻辑CPU数量*2，在本例中，服务器有2颗物理CPU，而每颗物理CPU又支持H.T超线程，所以实际取值为4*2=8 
thread_concurrency = 8 
# 开启该选项可以彻底关闭MySQL的TCP/IP连接方式，如果WEB服务器是以远程连接的方式访问MySQL数据库服务器则不要开启该选项！否则将无法正常连接
skipnetworking 
table_cache=1024
# 默认为2M 
innodb_additional_mem_pool_size=4M 
innodb_flush_log_at_trx_commit=1
# 默认为1M
innodb_log_buffer_size=2M  
# 你的服务器CPU有几个就设置为几，建议用默认一般为8 
innodb_thread_concurrency=8 
# 默认为16M，调到64~256最挂
tmp_table_size=64M 
```

>很多情况还需要具体情况具体分析！

#### 示例

下面是一个电商平台，类似京东或天猫这样的平台。商家购买服务，入住平台，开通之后，商家可以在系统中上架各种商品，客户通过手机 App、微信小程序等渠道购买商品，商家接到订单以后安排快递送货。

刚刚上线的时候，系统运行状态良好。但是，随着入住的商家不断增多，使用系统的用户量越来越多，每天的订单数据达到了 5 万条以上。这个时候，系统开始出现问题，CPU 使用率不断飙升。终于，双十一或者 618 活动高峰的时候，CPU 使用率达到 99%，这实际上就意味着，系统的计算资源已经耗尽，再也无法处理任何新的订单了。换句话说，系统已经崩溃了。

这个时候，我们想到了对系统参数进行调整，因为参数的值决定了资源配置的方式和投放的程度。为了解决这个问题，一共调整 3 个系统参数，分别是：

- `InnoDB_flush_log_at_trx_commit`
- `InnoDB_buffer_pool_size`
- `InnoDB_buffer_pool_instances`

下面就说一说调整这三个参数的原因是什么：

1. 调整系统参数 InnoDB_flush_log_at_trx_commit。
   - 这个参数适用于 InnoDB 存储引擎，电商平台系统中的表用的存储引擎都是 InnoDB。默认的值是 1，意思是每次提交事务的时候，都把数据写入日志，并把日志写入磁盘。这样做的好处是数据安全性最佳，不足之处在于每次提交事务，都要进行磁盘写入的操作。在大并发的场景下，过于频繁的磁盘读写会导致 CPU 资源浪费，系统效率变低。
   - 这个参数的值还有 2 个可能的选项，分别是 0 和 2。现在把这个参数的值改成 2，这样就不用每次提交事务的时候都启动磁盘读写了，在大并发的场景下，可以改善系统效率，降低 CPU 使用率。即便出现故障，损失的数据也比绞小。
2. 调整系统参数 InnoDB_buffer_pool_size。
   - 这个参数的意思是，InnoDB 存储引擎使用缓存来存储索引和数据。这个值越大，可以加载到缓存区的索引和数据量就越多，需要的磁盘读写就越少。
   - 因为 MySQL 服务器是数据库专属服务器，只用来运行 MySQL 数据库服务，没有其他应用了，而我们的计算机是 64 位机器，内存也有 128 GB。于是把这个参数的值调整为 64 GB。这样一来，磁盘读写次数可以大幅降低，就可以充分利用内存，释放出一些 CPU 的资源。
3. 调整系统参数 InnoDB_buffer_pool_instances。
   - 这个参数可以将 InnoDB 的缓存区分成几个部分，这样可以提高系统的并行处理能力，因为可以允许多个进程同时处理不同部分的缓存区。
   - 把 InnoDB_buffer_pool_instances 的值修改为 64，意思就是把 InnoDB 的缓存区分成 64 个分区，这样就可以同时有多个进程进行数据操作，CPU 的效率就高多了。修改好了系统参数的值，要重启 MySQL 数据库服务器。

总结，遇到 CPU 资源不足的问题，可以从下面 2 个思路去解决：

- 疏通拥堵路段，消除瓶颈，让等待的时间更短。
- 开拓新的通道，增加并行处理能力。

### 优化数据库结构

一个好的`数据库设计方案`对于数据库的性能常常会起到`事半功倍`的效果。合理的数据库结构不仅可以使数据库占用更小的磁盘空间，而且能够使查询速度更快。数据库结构的设计需要考虑`数据冗余`、`查询和更新的速度`、`字段的数据类型`是否合理等多方面的内容。

#### 拆分表：冷热数据分离

拆分表的思路是，把 1 个包含很多字段的表，拆分成 2 个或者多个相对较小的表，这样做的原因是，这些表中某些字段的操作频率很高（`热数据`），经常要进行查询或者更新操作，而另外一些字段的使用频率却很低（`冷数据`），`冷热数据分离`，可以减小表的宽度。如果放在一个表里面，每次查询都要读取大记录，会消耗较多的资源。

MySQL 限制每个表最多存储`4096`列，并且每一行数据的大小不能超过`65535`字节。表越宽，把表装载进内存缓冲池时所占用的内存也就越大，也会消耗更多的 I/O。

冷热数据分离的目的是：① 减少磁盘 I/O，保证热数据的内存缓存命中率。② 更有效的利用缓存，避免读入无用的冷数据。

假设会员 members 表存储会员登录认证信息。该表中有很多字段，如 id、姓名、密码、地址、电话、个人描述字段。其中地址、电话、个人描述等字段并不常用，可以将这些不常用的字段分解出另一个表。将这个表取名叫 members_detail，表中有 member_id、address、telephone、description 等字段。这样就把会员表分成了两个表，分别为 members 表和 members_detail 表。

创建这两个表的 SQL 语句如下：

```mysql
CREATE TABLE members (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) DEFAULT NULL,
  password varchar(50) DEFAULT NULL,
  last_login_time datetime DEFAULT NULL,
  last_login_ip varchar(100) DEFAULT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE members_detail (
  member_id int(11) NOT NULL DEFAULT 0,
  address varchar(255) DEFAULT NULL,
  telephone varchar(255) DEFAULT NULL,
  description text
);
```

如果需要查询会员的基本信息或详细信息，那么可以用会员的 id 来查询。如果需要将会员的基本信息和详细信息同时显示，那么可以将 members 表和 members_detail 表进行联合查询，查询语句如下：

```mysql
SELECT * FROM members LEFT JOIN members_detail on members.id = members_detail.member_id;
```

通过这种分解，可以提高表的查询效率。对于字段很多且有些字段使用不频繁的表，可以通过这种分解的方式来优化数据库的性能。

#### 增加中间表

对于需要经常联合查询的表，可以建立中间表以提高查询效率。**通过建立中间表，把需要经常联合查询的数据插入中间表中，然后将原来的联合查询改为对中间表的查询，以此来提高查询效率。**

首先，分析经常联合查询表中的字段。然后，使用这些字段建立一个中间表，并将原来联合查询的表的数据插入中间表中。最后，使用中间表来进行查询。

假设学生信息表和班级表的 SQL 语句如下：

```mysql
CREATE TABLE `class` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `className` VARCHAR(30) DEFAULT NULL,
  `address` VARCHAR(40) DEFAULT NULL,
  `monitor` INT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `student` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `stuno` INT NOT NULL ,
  `name` VARCHAR(20) DEFAULT NULL,
  `age` INT(3) DEFAULT NULL,
  `classId` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

现在有一个模块，需要经常查询带有学生名称（name）、学生所在班级名称（className）、学生班级班长（monitor）的学生信息。根据这种情况，可以创建一个 temp_student 表。temp_student 表中存储学生名称（stu_name）、学生所在班级名称（className）和学生班级班长（monitor）信息。创建表的语句如下：

```mysql
CREATE TABLE `temp_student` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `stu_name` INT NOT NULL ,
  `className` VARCHAR(20) DEFAULT NULL,
  `monitor` INT(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

接下来，从学生信息表和班级表中查询相关信息存储到临时表中：

```mysql
INSERT INTO temp_student(stu_name,className,monitor)
SELECT s.name, c.className, c.monitor FROM student AS s, class AS c WHERE s.classId = c.id
```

以后，可以直接从 temp_student 表中查询学生名称、班级名称和班级班长，而不用每次都进行联合查询，这样可以提高数据库的查询速度。

>如果用户信息修改了，是不是会导致 temp_student 中的数据不一致的问题呢？如何同步数据呢？
>
>方式 1：清空数据 ---> 重新添加数据。
>
>方式 2：使用视图。

#### 增加冗余字段

设计数据库表时应尽量遵循范式理论的规约，尽可能减少冗余字段，让数据库设计看起来精致、优雅。但是，合理地加入冗余字段可以提高查询速度。

表的规范化程度越高，表与表之间的关系就越多，需要连接查询的情况也就越多。尤其在数据量大，而且需要频繁进行连接的时候，为了提升效率，我们也可以考虑增加冗余字段来减少连接。

> 参考前面章节的反范式化。

#### 优化数据类型

改进表的设计时，可以考虑优化字段的数据类型。这个问题在大家刚从事开发时基本不算是问题。但是，随着你的经验越来越丰富，参与的项目越来越大，数据量也越来越多的时候，你就不能只从系统稳定性的角度来思考问题了，还要考虑到系统整体的稳定性和效率。此时，优先选择符合存储需要的最小的数据类型。

列的`字段越大`，建立索引时所需要的`空间也就越大`，这样一页中所能存储的索引节点的`数量就越少`，在遍历时所需要的`I/O次数也就越多`，`索引的性能也就越差`。

具体来说:

- **情况 1：对整数类型数据进行优化。**
  - 遇到整数类型的字段可以用`INT `型 。这样做的理由是，INT 型数据有足够大的取值范围，不用担心数据超出取值范围的问题。刚开始做项目的时候，首先要保证系统的稳定性，这样设计字段类型是可以的。但在数据量很大的时候，数据类型的定义，在很大程度上会影响到系统整体的执行效率。
  - 对于`非负型`的数据（如自增 ID、整型 IP）来说，要优先使用无符号整型`UNSIGNED`来存储。因为无符号相对于有符号，同样的字节数，存储的数值范围更大。如 tinyint 有符号为 -128 ~ 127，无符号为 0 ~ 255，多出一倍的存储空间。
- **情况 2：既可以使用文本类型也可以使用整数类型的字段，要选择使用整数类型。**
  - 跟文本类型数据相比，大整数往往占用`更少的存储空间` ，因此，在存取和比对的时候，可以占用更少的内存空间。所以，在二者皆可用的情况下，尽量使用整数类型，这样可以提高查询的效率。如：将 IP 地址转换成整型数据。
- **情况 3：避免使用 TEXT、BLOB 数据类型。**
  - MySQL`内存临时表`不支持TEXT、BLOB这样的大数据类型，如果查询中包含这样的数据，在排序等操作时，就不能使用内存临时表，必须使`用磁盘临时表`进行。并且对于这种数据，MySQL 还是要进行`二次查询`，会使 SQL 性能变得很差，但是不是说一定不能使用这样的数据类型。
  - 如果一定要使用，建议把 BLOB 或是 TEXT 列`分离到单独的扩展表`中，查询时一定不要使用 SELECT \*，而只需要取出必要的列，不需要 TEXT 列的数据时，不要对该列进行查询。
- **情况 4：避免使用 ENUM 类型。**
  - 修改 ENUM 值需要使用 ALTER 语句。
  - ENUN 类型的 ORDER BY 操作效率低，需要额外操作。
  - 使用 TINYINT 来代替 ENUM 类型。
- **情况 5：使用 TIMESTAMP 存储时间。**
  - TIMESTAMP 存储的时间范围 1970-01-0100:00:01 ~ 2038-01-19-03:14:07。
  - TIMESTAMP 使用 4 字节，DATETIME 使用 8 字节，同时 TIMESTAMP 具有自动赋值以及自动更新的特性。
- **情况 6：用 DECIMAL 代替 FLOAT 和 DOUBLE 存储精确浮点数。**
  - 非精准浮点：FLOAT 和 DOUBLE。
  - 精准浮点: DECIMAL。
  - DECIMAL 类型为精准浮点数，在计算时不会丢失精度，尤其是财务相关的金融类数据。占用空间由定义的宽度决定，每 4 个字节可以存储 3 位数字，并且小数点要占用一个字节。可用于存储比 BIGINT 更大的整型数据。

总之，遇到数据量大的项目时，一定要在充分了解业务需求的前提下，合理优化数据类型，这样才能充分发挥资源的效率，使系统达到最优。

#### 优化插入记录的速度

插入记录时，影响插入速度的主要是索引、唯一性校验、一次插入记录条数等。根据这些情况可以分别进行优化。这里我们分为 MyISAM 存储引擎和 InnoDB 存储引擎来讲。

##### MyISAM 存储引擎

###### 禁用索引

对于非空表，插入记录时，MySQL 会根据表的索引对插入的记录建立索引。如果插入大量数据，建立索引就会降低插入记录的速度。为了解决这种情况，可以在`插入记录之前禁用索引，数据插入完毕后再开启索引。`

禁用索引的语句如下：

```mysql
ALTER TABLE table_name DISABLE KEYS;
```

重新开启索引的语句如下：

```mysql
ALTER TABLE table_name ENABLE KEYS;
```

>若对空表批量导入数据，则不需要进行此操作，因为 MyISAM 引擎的表，是在导入数据之后才建立索引的。

###### 禁用唯一性检查

插入数据时，MySQL 会对插入的记录进行唯一性校验，这种唯一性校验会降低插入记录的速度。为了降低这种情况对查询速度的影响，可以`在插入记录之前禁用唯一性检合，等到记录插入完毕后再开启。`

禁用唯一性检查的语句如下：

```mysql
SET UNIQUE_GHECKS=0;
```

开启唯一性检查的语句如下：

```mysql
SET UNIQUE_GHECKS=1;
```

###### 使用批量插入

插入多条记录时，可以使用一条 INSERT 语句插入一条记录，也可以使用一条 INSERT 语句插入多条记录。

插入一条记录的 INSERT 语句情形如下：

```mysql
INSERT INTO student VALUES(1, 'zhangsan', 18, 1);
INSERT INTO student VALUES(2, 'lisi', 17, 1);
INSERT INTO student VALUES(3, 'wangwu', 17, 1);
INSERT INTO student VALUES(4, 'zhaoliu', 19, 1);
```

使用一条 INSERT 语句，插入多条记录的情形如下：

```mysql
INSERT INTO student VALUES
(1, 'zhangsan', 18, 1),
(2, 'lisi', 17, 1),
(3, 'wangwu', 17, 1),
(4, 'zhaoliu', 19, 1);
```

批量插入的速度要比逐条插入快。

###### 使用 LOAD DATA INFILE 批量导入

当需要批量导入数据时，如果能用`LOAD DATA INFILE`语句，就尽量使用。因为 LOAD DATA INFILE 语句导入数据的速度，比 INSERT 语句快。

##### InnoDB 存储引擎

###### 禁用唯一性检查

插入数据之前，执行`set unique_checks=0`来禁止对唯一索引的检查，数据导入完成之后再运行`set unique_checks=1`。这个和 MyISAM 引擎的使用方法一样。

###### 禁用外键检查

`插入数据之前，执行禁止对外键的检查，数据插入完成之后再恢复对外键的检查。`

禁用外键检查的语句如下：

```mysql
SET foreign_key_checks=0;
```

恢复对外键的检查语句如下：

```mysql
SET foreign_key_checks=1;
```

###### 禁止自动提交

`插入数据之前禁止事务的自动提交，数据导入完成之后，执行恢复自动提交操作。`

禁止自动提交的语句如下:

```mysql
SET autocommit=0;
```

恢复自动提交的语句如下：

```mysql
SET autocommit=1;
```

#### 使用非空约束

在设计字段的时候，如果业务允许，建议`尽量使用非空约束`。这样做的好处是：

- 进行比较和计算时，省去要对 NULL 值的字段判断是否为空的开销，提高存储效率。
- 非空字段也容易创建索引。因为索引 NULL 列需要额外的空间来保存，所以要占用更多的空间。使用非空约束，就可以节省存储空间（每个字段 1 个 bit）。

#### 分析表、检查表与优化表

MySQL提供了分析表、检查表和优化表的语句。`分析表`主要是分析关键字的分布，`检查表`主要是检查表是否存在错误，`优化表`主要是消除删除或更新造成的空间浪费。

##### 分析表

MySQL 中提供了`ANALYZE TABLE`语句分析表，ANALYZE TABLE 语句的基本语法如下：

```mysql
ANALYZE [LOCAL | NO_WRITE_TO_BINLOG] TABLE tbl_name[,tbl_name]…
```

默认的，MySQL 服务会将 ANALYZE TABLE 语句写到 binlog 中，以便在主从架构中，从服务能够同步数据。可以`添加参数 LOCAL 或者 NO_WRITE_TO_BINLOG 取消将语句写到 binlog 中`。

使用` ANALYZE TABLE`分析表的过程中，数据库系统会自动对表加一个`只读锁` 。在分析期间，只能读取表中的记录，不能更新和插入记录。ANALYZE TABLE 语句能够分析 InnoDB 和 MyISAM 类型的表，但是不能作用于视图。

ANALYZE TABLE 分析后的统计结果，会反应到`Cardinality`的值，该值`统计了表中某一键所在的列不重复的值的个数`。该值越接近表中的总行数，则在表连接查询或者索引查询时，就越优先被优化器选择使用。也就是索引列的 Cardinality 的值，与表中数据的总条数差距越大，即使查询的时候使用了该索引作为查询条件，存储引擎实际查询的时候使用的概率就越小。下面通过例子来验证下，Cardinality 可以通过`SHOW INDEX FROM tablename`查看。

下面举例说明，使用下面的语句创建一张 user 表。

- 创建表并添加 1000 条记录：

  ```mysql
  # 建表
  mysql> CREATE TABLE `user1` (
      ->   `id` INT NOT NULL AUTO_INCREMENT,
      ->   `name` VARCHAR(255) DEFAULT NULL,
      ->   `age` INT DEFAULT NULL,
      ->   `sex` VARCHAR(255) DEFAULT NULL,
      ->   PRIMARY KEY (`id`),
      ->   KEY `idx_name` (`name`) USING BTREE
      -> ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;
  Query OK, 0 rows affected, 1 warning (0.04 sec)
  
  mysql> SET GLOBAL log_bin_trust_function_creators = 1;
  Query OK, 0 rows affected (0.00 sec)
  
  # 创建函数
  mysql> DELIMITER //
  mysql> CREATE FUNCTION  rand_num (from_num INT ,to_num INT) RETURNS INT(11)
      -> BEGIN   
      -> DECLARE i INT DEFAULT 0;  
      -> SET i = FLOOR(from_num +RAND()*(to_num - from_num+1))   ;
      -> RETURN i;  
      -> END //
  Query OK, 0 rows affected, 1 warning (0.02 sec)
  
  mysql> DELIMITER ;
  
  # 创建存储过程
  mysql> DELIMITER //
  mysql> CREATE PROCEDURE  insert_user( max_num INT )
      -> BEGIN  
      -> DECLARE i INT DEFAULT 0;   
      ->  SET autocommit = 0;    
      ->  REPEAT  
      ->  SET i = i + 1;  
      ->  INSERT INTO `user1` ( NAME,age,sex ) 
      ->  VALUES ("atguigu",rand_num(1,20),"male");  
      ->  UNTIL i = max_num  
      ->  END REPEAT;  
      ->  COMMIT; 
      -> END //
  Query OK, 0 rows affected (0.01 sec)
  
  mysql> DELIMITER ;
  
  # 向表中添加1000条记录
  mysql> CALL insert_user(1000);
  Query OK, 0 rows affected (0.06 sec)
  ```

- 查看表中的索引：

  <img src="mysql-advanced/image-20231107183245079.png" alt="image-20231107183245079" style="zoom:80%;" />

- 执行下列语句：

  ```mysql
  # 修改其中一条数据的name为atguigu03，此时，数据库中应该有两个不同的name值
  mysql> UPDATE user1 SET NAME = 'atguigu03' WHERE id = 2;
  Query OK, 1 row affected (0.00 sec)
  Rows matched: 1  Changed: 1  Warnings: 0
  
  # 但此时Cardinality显示name仍然只有一个，因为没有更新
  mysql> SHOW INDEX FROM user1;
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | Table | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | user1 |          0 | PRIMARY  |            1 | id          | A         |        1000 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
  | user1 |          1 | idx_name |            1 | name        | A         |           1 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  2 rows in set (0.00 sec)
  ```

- 分析表之后，再次查看：

  ```mysql
  # 分析表
  mysql> ANALYZE TABLE user1;
  +------------------+---------+----------+----------+
  | Table            | Op      | Msg_type | Msg_text |
  +------------------+---------+----------+----------+
  | atguigudb2.user1 | analyze | status   | OK       |
  +------------------+---------+----------+----------+
  1 row in set (0.01 sec)
  
  # Cardinality中name的值变为2，如果name越接近主键，说明其区分度越高
  mysql> SHOW INDEX FROM user1;
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | Table | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  | user1 |          0 | PRIMARY  |            1 | id          | A         |        1000 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
  | user1 |          1 | idx_name |            1 | name        | A         |           2 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
  +-------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
  2 rows in set (0.00 sec)
  ```

>假如说我们把 id = 3的数据的 name 修改成 atguigudb04，此时直接查看索引，发现 Cardinality 还是2，但是执行 ANALYZE 后，再查看会发现 Cardinality 已经变成 3，说明 ANALYZE 启动了刷新数据的作用。

##### 检查表

MySQL 中可以使用`CHECK TABLE`语句来检查表。CHECK TABLE 语句能够检查 InnoDB 和 MyISAM 类型的表是否存在错误。CHECK TABLE 语句在执行过程中也会给表加上`只读锁` 。

对于 MyISAM 类型的表，CHECK TABLE 语句还会更新关键字统计数据。而且，CHECK TABLE 也可以检查视图是否有错误，比如在视图定义中被引用的表已不存在。该语句的基本语法如下：

```mysql
CHECK TABLE tbl_name [, tbl_name] ... [option] ...
option = {QUICK | FAST | MEDIUM | EXTENDED |
```

其中，tbl_name 是表名；option 参数有 5 个取值，分别是 QUICK、FAST、MEDIUM、EXTENDED 和 CHANGED。各个选项的意义分别是：

- `QUICK`：不扫描行，不检查错误的连接。
- `FAST`：只检查没有被正确关闭的表。
- `CHANGED`：只检查上次检查后被更改的表和没有被正确关闭的表。
- `MEDIUM`：扫描行，以验证被删除的连接是有效的。也可以计算各行的关键字校验和，并使用计算出的校验和验证这一点。
- `EXTENDED`：对每行的所有关键字进行一个全面的关键字查找。这可以确保表是 100% 一致的，但是花的时间较长。

option 只对 MyISAM 类型的表有效，对 InnoDB 类型的表无效。比如：

```mysql
mysql> CHECK TABLE student;
+--------------------+-------+----------+----------+
| Table              | Op    | Msg_type | Msg_text |
+--------------------+-------+----------+----------+
| atguigudb2.student | check | status   | OK       |
+--------------------+-------+----------+----------+
1 row in set (0.40 sec)
```

该语句对于检查的表可能会产生多行信息。最后一行有一个状态的`Msg_type`值，Msg_text 通常为 OK。如果得到的不是 OK，通常要对其进行修复；是 OK 说明表已经是最新的了。表已经是最新的，意味着存储引擎对这张表不必进行检查。

##### 优化表

###### 方式 1：使用 OPTIMIZE TABLE 命令

MySQL 中使用`OPTIMIZE TABLE`语句来优化表。但是，OPTILMIZE TABLE 语句只能优化表中的`VARCHAR`、`BLOB `或`TEXT`类型的字段。一个表使用了这些字段的数据类型，若已经`删除`了表的一大部分数据，或者已经对含有可变长度行的表（含有 VARCHAR、BLOB 或 TEXT 列的表）进行了很多`更新 `，则应使用 OPTIMIZE TABLE 来重新利用未使用的空间，并整理数据文件的`碎片`。

OPTIMIZE TABLE 语句对 InnoDB 和 MyISAM 类型的表都有效，该语句在执行过程中也会给表加上`只读锁`。

OPTIMIZE TABLE 语句的基本语法如下：

```mysql
OPTIMIZE [LOCAL | NO_WRITE_TO_BINLOG] TABLE tbl_name [, tbl_name] ...
```

- LOCAL | NO_WRITE_TO_BINLOG 关键字的意义和分析表相同，都是指定不写入二进制日志。

示例：

```mysql
mysql> CREATE TABLE t1(id INT, name VARCHAR(15)) ENGINE=MyISAM;
Query OK, 0 rows affected (0.01 sec)

# 优化MyISAM表
mysql> OPTIMIZE TABLE t1;
+---------------+----------+----------+-----------------------------+
| Table         | Op       | Msg_type | Msg_text                    |
+---------------+----------+----------+-----------------------------+
| atguigudb2.t1 | optimize | status   | Table is already up to date |
+---------------+----------+----------+-----------------------------+
1 row in set (0.00 sec)

mysql> CREATE TABLE t2(id INT, name VARCHAR(15)) ENGINE=InnoDB;
Query OK, 0 rows affected (0.03 sec)

# 优化InnoDB表
mysql> OPTIMIZE TABLE t2;
+---------------+----------+----------+-------------------------------------------------------------------+
| Table         | Op       | Msg_type | Msg_text                                                          |
+---------------+----------+----------+-------------------------------------------------------------------+
| atguigudb2.t2 | optimize | note     | Table does not support optimize, doing recreate + analyze instead |
| atguigudb2.t2 | optimize | status   | OK                                                                |
+---------------+----------+----------+-------------------------------------------------------------------+
2 rows in set (0.05 sec)
```

> Msg_text 显示的 "Table does not support optimize, doing recreate + analyze instead" 是正常的，可以查看官网 https://dev.mysql.com/doc/refman/8.0/en/optimize-table.html 验证。

优化过程：**在 MyISAM 中，是先分析这张表，然后会整理相关的 MySQL datafile，之后回收未使用的空间；在 InnoDB 中，回收空间是简单通过 Alter table 进行整理空间。在优化期间，MySQL 会创建一个临时表，优化完成之后会删除原始表，然后会将临时表 rename 成为原始表。**

> 说明：在多数的设置中，根本不需要运行 OPTIMIZE TABLE。即使对可变长度的行进行了大量的更新，也不需要经常运行，每周一次或每月一次即可，并且只需要对特定的表运行。

示例：

```tex
1. 新建一张表，使用存储过程往里面放入100W数据，或者更多一些，争取能够以兆的单位显示
2. 查看服务器上数据文件的大小，文件目录是/var/1ib/mysq1/所在的数据库
3. 删除二分之一的数据，然后再查看当前数据文件的大小，会发现此时大小是不变的
4. 使用OPTIMIZE tablename命令优化表
5. 再查看当前数据文件的大小，会发现此时大小已经变化了，做了空间的回收
```

优化前：

<img src="mysql-advanced/image-20231107190317730.png" alt="image-20231107190317730" style="zoom:67%;" />

优化后：

<img src="mysql-advanced/image-20231107190342411.png" alt="image-20231107190342411" style="zoom:67%;" />

###### 方式 2：使用 mysqlcheck 命令

```bash
# mysqlcheck是Linux中的rompt，-o是代表optimize，优化特定表
$ mysqlcheck -o database_name table_name -h127.0.0.1 -uroot -p123456
# 或优化所有表
$ mysqlcheck -o --all-databases -h127.0.0.1 -uroot -p123456
```

> 使用 Docker 创建的 MySQL 容器，需要加上 -h 参数，指定主机。

示例：

```mysql
xisun@xisun-develop:~/mysql/mysql-8.0.33-linux-glibc2.17-x86_64-minimal/bin$ ./mysqlcheck -o atguigudb2 -h127.0.0.1 -uroot -p123456 -P3306
mysqlcheck: [Warning] Using a password on the command line interface can be insecure.
atguigudb2.class_comment
note     : Table does not support optimize, doing recreate + analyze instead
status   : OK
atguigudb2.class_comment1
note     : Table does not support optimize, doing recreate + analyze instead
status   : OK
atguigudb2.student
note     : Table does not support optimize, doing recreate + analyze instead
status   : OK
atguigudb2.t1                                      Table is already up to date
atguigudb2.t2
note     : Table does not support optimize, doing recreate + analyze instead
status   : OK
atguigudb2.user1
note     : Table does not support optimize, doing recreate + analyze instead
status   : OK
```

#### 小结

上述这些方法都是有利有弊的。比如：

- 修改数据类型，节省存储空间的同时，你要考虑到数据不能超过取值范围。
- 增加冗余字段的时候，不要忘了确保数据一致性。
- 把大表拆分，也意味着你的查询会增加新的连接，从而增加额外的开销和运维的成本。

因此，一定要结合实际的业务需求进行权衡。

### 大表优化

当 MySQL 单表记录数过大时，数据库的 CRUD 性能会明显下降，一些常见的优化措施如下。

#### 限定查询的范围

`禁止不带任何限制数据范围条件的查询语句。`比如：当用户在查询订单历史的时候，可以控制在一个月的范围内。

#### 读写分离

经典的数据库拆分方案，主库负责写，从库负责读。

- 一主一从模式：

  <img src="mysql-advanced/image-20231107123115352.png" alt="image-20231107123115352" style="zoom: 67%;" />

- 双主双从模式：

  <img src="mysql-advanced/image-20231107123134706.png" alt="image-20231107123134706" style="zoom:67%;" />

#### 垂直拆分

当数据量级达到`千万级`以上时，有时候我们需要把一个数据库切成多份，放到不同的数据库服务器上，减少对单一数据库服务器的访问压力。

<img src="mysql-advanced/image-20231107123238030.png" alt="image-20231107123238030" style="zoom:60%;" />

**垂直拆分的优点：** 可以使得列数据变小，在查询时减少读取的 Block 数，减少 I/O 次数。此外，垂直分区可以简化表的结构，易于维护。

**垂直拆分的缺点：** 主键会出现冗余，需要管理冗余列，并会引起 JOIN 操作。此外，垂直拆分会让事务变得更加复杂。

#### 水平拆分

特点：

- 尽量控制单表数据量的大小，建议控制在`1000 万以内`。1000 万并不是 MySQL 数据库的限制，但是过大会造成修改表结构、备份、恢复都会有很大的问题。此时可以用`历史数据归挡`（应用于日志数据），`水平分表`（应用于业务数据）等手段来控制数据量大小。

- 这里主要考虑业务数据的水平分表策略。将大的数据表按照`某个属性维度`分拆成不同的小表，每张小表保持相同的表结构。比如可以按照年份来划分，把不同年份的数据放到不同的数据表中。2017 年、2018 年和 2019 年的数据就可以分别放到三张数据表中。

- 水平分表仅是解决了单一表数据过大的问题，但由于表的数据还是在同一台机器上，其实对于提升 MySQL 并发能力没有什么意义，所以`水平拆分最好分库`，从而达到分布式的目的。

  <img src="mysql-advanced/image-20231107124344079.png" alt="image-20231107124344079" style="zoom:80%;" />

水平拆分能够支持非常大的数据量存储，应用端改造也少，但`分片事务难以解决，跨节点 JOIN 性能较差，逻辑复杂。`《Java 工程师修炼之道》的作者推荐尽量不要对数据进行分片，因为拆分会带来逻辑、部署、运维的各种复杂度，一般的数据表在优化得当的情况下，支撑千万以下的数据量是没有太大问题的。如果实在要分片，尽量选择客户端分片架构，这样可以减少一次和中间件的网络 I/O。

下面补充一下数据库分片的两种常见方案：

- **客户端代理：分片逻辑在应用端，封装在 JAR 包中，通过修改或者封装 JDBC 层来实现。**比如当当网的 Sharding-JDBC、阿里的 TDDL，是两种比较常用的实现。

- **中间件代理：在应用和数据中间加了一个代理层，分片逻辑统一维护在中间件服务中。**比如 Mycat、360 的 Atlas、网易的 DDB 等，都是这种架构的实现。

### 其他调优策略

#### 服务器语句超时处理

在 MySQL 8.0 中可以设置`服务器语句超时的限制`，单位可以达到`毫秒级别` 。当中断的执行语句超过设置的毫秒数后，服务器将终止查询影响不大的事务或连接，然后将错误报给客户端。

设置服务器语句超时的限制，可以通过设置系统变量`MAX_EXECUTION_TIME`来实现。默认情况下，MAX_EXECUTION_TIME 的值为 0，代表没有时间限制。示例：

```mysql
SET GLOBAL MAX_EXECUTION_TIME=2000;
SET SESSION MAX_EXECUTION_TIME=2000; # 指定该会话中SELECT语句的超时时间
```

#### 创建全局通用表空间

MySQL 8.0 使用`CREATE TABLE SPACE`语句来创建一个全局通用表空间。全局表空间可以被所有的数据库的表共享，而且相比于独享表空间，使用手动创建共享表空间，可以节约元数据方面的内存。可以在创建表的时候，指定属于哪个表空间，也可以对已有表进行表空间修改等。

下面创建名为 atguigu1 的共享表空间，SQL 语句如下：

```mysql
mysql> CREATE TABLESPACE atguigu1 ADD datafile 'atguigu1.ibd' file_block_size=16k;
Query OK, 0 rows affected (0.02 sec)
```

指定表空间，SQL 语句如下：

```mysql
mysql> CREATE TABLE test(id int, name varchar(18)) Engine=InnoDB DEFAULT charset utf8mb4 TABLESPACE atguigu1;
Query OK, 0 rows affected (0.02 sec)
```

也可以通过 ALTER TABLE 语句指定表空间，SQL 语句如下：

```mysql
mysql> ALTER TABLE test TABLESPACE atguigu1;
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0
```

如何删除创建的共享表空间？因为是共享表空间，所以不能直接通过 drop table tbname 删除，这样操作并不能回收空间。当确定共享表空间的数据都没用，并且依赖该表空间的表均已经删除时，可以通过`DROP TABLESPACE`删除共享表空间来释放空间，如果依赖该共享表空间的表存在，就会删除失败。如下所示：

```mysql
mysql> DROP TABLESPACE atguigu1;
ERROR 3120 (HY000): Tablespace `atguigu1` is not empty.
```

所以，应该首先删除依赖该表空间的数据表，SQL 语句如下：

```mysql
mysql> DROP TABLE test;
Query OK, 0 rows affected (0.02 sec)
```

最后即可删除表空间，SQL 语句如下：

```mysql
mysql> DROP TABLESPACE atguigu1;
Query OK, 0 rows affected (0.02 sec)
```

####  MySQL 8.0 新特性：隐藏索引对调优的帮助

不可见索引的特性对于性能调试非常有用。在 MySQL 8.0 中，索引可以被 "隐藏" 和 "显示"。`当一个索引被隐藏时，它不会被查询优化器所使用。`也就是说，管理员可以隐藏一个索引，然后观察对数据库的影响。如果数据库性能有所下降，就说明这个索引是有用的，于是将其 "恢复显示" 即可；如果数据库性能看不出变化，就说明这个索引是多余的，可以删掉了。

需要注意的是当索引被隐藏时，它的内容仍然是和正常索引一样`实时更新`的。如果一个索引需要长期被隐藏，那么可以将其删除，因为索引的存在会影响插入、更新和删除的性能。

**数据表中的主键不能被设置为`invisible`。**

## 事务基础知识

### 数据库事务概述

事务是数据库区别于文件系统的重要特性之一，当有了事务就会让数据库始终保持`一致性`，同时还能通过事务的机制`恢复到某个时间点`，这样可以保证已提交到数据库的修改不会因为系统崩溃而丢失。

#### 存储引擎支持情况

`SHOW ENGINES `命令，可以查看当前 MySQL 支持的存储引擎都有哪些，以及这些存储引擎是否支持事务。

```mysql
mysql> SHOW ENGINES;
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| Engine             | Support | Comment                                                        | Transactions | XA   | Savepoints |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| ndbcluster         | NO      | Clustered, fault-tolerant tables                               | NULL         | NULL | NULL       |
| FEDERATED          | NO      | Federated MySQL storage engine                                 | NULL         | NULL | NULL       |
| MEMORY             | YES     | Hash based, stored in memory, useful for temporary tables      | NO           | NO   | NO         |
| InnoDB             | DEFAULT | Supports transactions, row-level locking, and foreign keys     | YES          | YES  | YES        |
| PERFORMANCE_SCHEMA | YES     | Performance Schema                                             | NO           | NO   | NO         |
| MyISAM             | YES     | MyISAM storage engine                                          | NO           | NO   | NO         |
| ndbinfo            | NO      | MySQL Cluster system information storage engine                | NULL         | NULL | NULL       |
| MRG_MYISAM         | YES     | Collection of identical MyISAM tables                          | NO           | NO   | NO         |
| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
| CSV                | YES     | CSV storage engine                                             | NO           | NO   | NO         |
| ARCHIVE            | YES     | Archive storage engine                                         | NO           | NO   | NO         |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
11 rows in set (0.00 sec)
```

可以看出，`在 MySQL 中，只有 InnoDB 是支持事务的。`

#### 基本概念

**`事务`：一组逻辑操作单元（一组 SQL），使数据从一种状态变换到另一种状态。**

**`事务处理的原则`**：保证所有事务都作为`一个工作单元`来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事务都被提交（`commit`），那么这些修改就`永久`地保存下来；要么数据库管理系统将`放弃`所作的所有`修改`，整个事务回滚（`rollback`）到最初状态。

示例：

```mysql
# AA用户给BB用户转账100
UPDATE accounts SET money = money - 50 WHERE NAME = 'AA';

# 服务器宕机
UPDATE accounts SET money = money + 50 WHERE NAME = 'BB';
```

#### 事务的 ACID 特性

##### 原子性（Atomicity）

**`原子性`：是指事务是一个不可分割的工作单位，要么全部提交，要么全部失败回滚。**即，要么转账成功，要么转账失败，是不存在中间的状态。如果无法保证原子性会怎么样？那就会出现数据不一致的情形，A 账户减去 100 元，而 B 账户增加 100 元操作失败，系统将无故丢失 100 元。

##### 一致性（Consistency）

**`一致性`：是指事务执行前后，数据从一个合法性状态变换到另外一个合法性状态。这种状态是`语义上的`而不是语法上的，跟具体的业务有关。**

那什么是合法的数据状态呢？满足预定的约束的状态就叫做合法的状态。通俗一点，这状态是由你自己来定义的（比如满足现实世界中的约束）。满足这个状态，数据就是一致的，不满足这个状态，数据就是不一致的。如果事务中的某个操作失败了，系统就会自动撤销当前正在执行的事务，返回到事务操作之前的状态。

举例 1：A 账户有 200 元，转账 300 元出去，此时 A 账户余额为 -100 元。你自然就发现了此时数据是不一致的，为什么呢？因为你定义了一个状态，余额这列必须 >= 0。

举例 2：A 账户 200 元，转账 50 元给 B 账户，A 账户的钱扣了，但是 B 账户因为各种意外，余额并没有增加。你也知道此时数据是不一致的，为什么呢？因为你定义了一个状态，要求 A + B 的总余额必须不变。

举例 3：在数据表中将姓名字段设置为唯一性约束，这时当事务进行提交或者事务发生回滚的时候，如果数据表中的姓名不唯一，就破坏了事务的一致性要求。

> 国内很多网站上对一致性的阐述有误，具体可以参考 wikipedia 对 Consistency 的阐述。

##### 隔离型（Isolation）

**`隔离性`：是指一个事务的执行不能被其他事务干扰 ，即一个事务内部的操作及使用的数据对并发的其他事务是隔离的，并发执行的各个事务之间不能互相干扰。**

如果无法保证隔离性会怎么样？假设 A 账户有 200 元，B 账户 0 元。A 账户往 B 账户转账两次，每次金额为 50 元，分别在两个事务中执行。如果无法保证隔离性，会出现下面的情形：

```mysql
UPDATE accounts SET money = money - 50 WHERE NAME = 'AA';
UPDATE accounts SET money = money + 50 WHERE NAME = 'BB';
```

根据图解，发现出现了线程安全的问题，从而导致转账前后总金额不一致的情况：

<img src="mysql-advanced/image-20231109185600898.png" alt="image-20231109185600898" style="zoom: 80%;" />

>可以联系 JUC 中的临界区的概念，为了避免各个线程都执行临界区的代码，必须加 Synchronized。

##### 持久性（Durability）

**`持久性`：是指一个事务一旦被提交，它对数据库中数据的改变就是`永久性`的 ，接下来的其他操作和数据库故障不应该对其有任何影响。**

持久性是通过`事务日志`来保证的，事务日志包括了`重做日志`和`回滚日志`。当我们通过事务对数据进行修改的时候，首先会将数据库的变化信息记录到重做日志中，然后再对数据库中对应的行进行修改。这样做的好处是，即使数据库系统崩溃，数据库重启后也能找到没有更新到数据库系统中的重做日志，重新执行，从而使事务具有持久性。

##### 总结

ACID 是事务的四大特性，在这四个特性中，`原子性是基础，隔离性是手段，一致性是约束条件，而持久性是目的。`

数据库事务，其实就是数据库设计者为了方便起见，把需要保证原子性、隔离性、一致性和持久性的一个或多个数据库操作称为一个事务。一句话，事务就是 ACID。

#### 事物的状态

我们现在知道，事务是一个抽象的概念，它其实对应着一个或多个数据库操作，MySQL 根据这些操作所执行的不同阶段把事务大致划分成几个状态。

一个基本的状态转换图如下所示：

<img src="mysql-advanced/image-20231109210049244.png" alt="image-20231109210049244" style="zoom:67%;" />

图中可见，只有当事务处于`提交的`或者`中止的`状态时，一个事务的生命周期才算是结束了。**对于已经提交的事务来说，该事务对数据库所做的修改将永久生效，对于处于中止状态的事务，该事务对数据库所做的所有修改被回滚到没执行该事务之前的状态。**

##### 活动的（active）

事务对应的数据库操作正在执行过程中时，就说该事务处在`活动的`状态。比如转账的事务（两条 DML）在执行。

##### 部分提交的（partially committed）

当事务中的最后一个操作执行完成，但由于操作都在内存中执行，所造成的影响并`没有刷新到磁盘`时，我们就说该事务处在`部分提交的`状态。比如转账的事务执行完成，但是还没有进行提交。

##### 失败的（failed）

当事务处在`活动的`或者`部分提交的`状态时，可能遇到了**某些错误**（数据库自身的错误、操作系统错误或者直接断电等）而无法继续执行，或者**人为的停止**当前事务的执行，就说该事务处在`失败的`状态。 比如正在转账时，银行突然断电了，事务就会被停止。

##### 中止的（aborted）

如果事务执行了一部分而变为`失败的`状态，那么就需要把已经修改的事务中的操作还原到事务执行前的状态。换句话说，就是要撤销失败事务对当前数据库造成的影响。把这个撤销的过程称之为`回滚`。当`回滚`操作执行完毕时，也就是数据库恢复到了执行事务之前的状态，就说该事务处在了`中止的`状态。比如当事务执行失败后，需要进行回滚，回滚完毕后的状态就是中止态。

##### 提交的（committed）

当一个处在`部分提交的`状态的事务将修改过的数据都`同步到磁盘`上之后，就可以说该事务处在了`提交的`状态。

### 如何使用事务

使用事务有两种方式，分别为`显式事务`和`隐式事务`。

#### 显式事务

**事务的完成过程：**

- 步骤 1：开启事务。
- 步骤 2：一系列的 DML 操作。
- …
- 步骤 3：事务结束，表现为两种状态：提交的状态（COMMIT）、中止的状态（ROLLBACK）。

**步骤 1：`START TRANSACTION`或者`BEGIN`命令，显式开启一个事务。**

```mysql
BEGIN;

# 或者
START TRANSACTION;
```

- `START TRANSACTION`语句相较于`BEGIN`特别之处在于，后边能跟随几个`修饰符`：

  - `READ ONLY`：标识当前事务是一个`只读事务`，也就是属于该事务的数据库操作只能读取数据，而不能修改数据。
  - `READ WRITE`：标识当前事务是一个`读写事务`，也就是属于该事务的数据库操作既可以读取数据，也可以修改数据。
  - `WITH CONSISTENT SNAPSHOT`：启动`一致性读`。

- 示例：

  ```mysql
  START TRANSACTION READ ONLY; # 开启一个只读事务
  
  START TRANSACTION READ ONLY, WITH CONSISTENT SNAPSHOT; # 开启只读事务和一致性读
  
  START TRANSACTION READ WRITE,WITH CONSISTENT SNAPSHOT; # 开启读写事务和一致性读
  ```

>注意：
>
>- READ ONLY 和 READ WRITE 是用来设置所谓的事务访问模式的，就是以只读还是读写的方式来访问数据库中的数据。
>- 一个事务的访问模式不能同时既设置为只读的，又设置为读写的，所以不能同时把 READ ONLY 和 READ WRITE 放到 START TRANSACTION 语句后边。
>
>- **如果不显式指定事务的访问模式，那么该事务的访问模式就是`读写模式`。**

**步骤 2：一系列事务中的操作（主要是 DML，不含 DDL）。**

**步骤 3：提交事务或中止事务（即回滚事务）。**

```mysql
# 提交事务。当提交事务后，对数据库的修改是永久性的
COMMIT;

# 回滚事务。即撤销正在进行的所有没有提交的修改
ROLLBACK;

# 将事务回滚到某个保存点
ROLLBACK TO [SAVEPOINT]
```

其中关于`SAVEPOINT`相关操作有：

```mysql
# 在事务中创建保存点，方便后续针对保存点进行回滚。一个事务中可么存在多个保存点
SAVEPOINT 保存点名称;

#删除某个保存点
RELEASE SAVEPOINT保存点名称;
```

#### 隐式事务

MySQL 中有一个系统变量`autocommit`：

```mysql
mysql> SHOW VARIABLES LIKE 'autocommit';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| autocommit    | ON    |
+---------------+-------+
1 row in set (0.01 sec)
```

默认情况下，如果不显式的使用`START TRANSACTION`或者`BEGIN`语句开启一个事务，那么每一条语句都算是一个独立的事务，这种特性称之为事务的`自动提交`。下边这两条语句就相当于放到两个独立的事务中去执行：

```mysql
# 假设此时autocommit是默认值on
UPDATE account SET balance = balance - 10 WHERE id = 1; # 此时这条DML操作是一个独立的事务

UPDATE account SET balance = balance + 10 WHERE id = 2; # 此时这条DML操作是一个独立的事务
```

当然，如果想关闭这种自动提交的功能，可以使用下边两种方法之一：

- 显式的的使用`START TRANSACTION`或者`BEGIN`语句开启一个事务，这样在本次事务提交或者回滚前会暂时关闭掉自动提交的功能。

- 把系统变量`autocommit `的值设置为`OFF `。

  ```mysql
  SET autocommit = OFF;
  
  # 或
  SET autocommit = 0;
  ```

**关闭自动提交之后，写入的多条语句就算是属于同一个事务了。**直到我们显式的写出`COMMIT`语句来把这个事务提交掉，或者显式的写出`ROLLBACK`语句来把这个事务回滚掉。

> 补充：Oracle 默认不自动提交，需要手写 COMMIT 命令，而 MySQL 默认自动提交。

#### 隐式提交数据的情况

- **使用数据定义语言**（Data definition language，缩写为 DDL）

  - 数据库对象，指的就是`数据库`、`表`、`视图`、`存储过程`等结构。当使用`CREATE `、`ALTER`、`DROP`等语句去修改数据库对象时，会隐式的提交前边语句所属于的事务。即：

    ```mysql
    BEGIN;
    
    SELECT ... # 事务中的一条语句
    UPDATE ...# 事务中的一条语句
    ...  # 事务中的其它语句
    
    CREATE TABLE ...# 此语句会隐式的提交前边语句所属于的事务
    ```

- **隐式使用或修改 MySQL 数据库中的表**

  - 当使用`ALTER USER`、`CREATE USER`、`DROP USER` 、`GRANT`、`RENAME USER`、`REVOKE`、`SET PASSWORD`等语句时，会隐式的提交前边语句所属于的事务。

- **事务控制或使用关于锁定的语句**

  - 当在一个事务还没提交或者回滚时，就又使用`START TRANSACTION`或者`BEGIN`语句开启了另一个事务时，会隐式的提交前边语句所属于的事务。即：

    ```mysql
    BEGIN;
    
    SELECT ... # 事务中的一条语句
    UPDATE ... # 事务中的一条语句
    ...       # 事务中的其它语句
    
    BEGIN;   # 此语句会隐式的提交前面语句所属于的事务
    ```

  - 当前的 autocommit 系统变量的值为 OFF ，我们手动把它调为`ON`时，会隐式的提交前边语句所属于的事务。

  - 使用`LOCK TABLES`、`UNLOCK TABLES`等关于锁定的语句，会隐式的提交前边语句所属于的事务。

- **使用加载数据的语句**

  - 使用`LOAD DATA`语句来批量往数据库中导入数据时，会隐式的提交前边语句所属于的事务。

- **使用 MySQL 复制的一些语句**

  - 使用`START SLAVE`、`STOP SLAVE`、`RESET SLAVE`、`CHANGE MASTER TO`等语句时，会隐式的提交前边语句所属于的事务。

- **使用其它的一些语句**

  - 使用`ANALYZE TABLE`、`CACHE INDEX`、`CHECK TABLE`、`FLUSH`、`LOAD INDEX INTO CACHE `、`OPTIMIZE TABLE`、`REPAIR TABLE`、`RESET `等语句时，会隐式的提交前边语句所属于的事务。

#### 示例：提交与回滚

我们看下在 MySQL 的默认状态下，下面这个事务最后的处理结果是什么。

先创建 user3 表：

```mysql
mysql> USE atguigudb2;
Database changed

mysql> CREATE TABLE user3(NAME VARCHAR(15) PRIMARY KEY);
Query OK, 0 rows affected (0.03 sec)
```

**情况一：**

```mysql
mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('zhangsan'); # 此时不会自动提交数据
Query OK, 1 row affected (0.00 sec)

mysql> COMMIT;
Query OK, 0 rows affected (0.01 sec)

mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('lisi'); # 此时不会自动提交数据
Query OK, 1 row affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('lisi'); # 受主键的影响，添加失败
ERROR 1062 (23000): Duplicate entry 'lisi' for key 'user3.PRIMARY'
mysql> ROLLBACK;
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT * FROM user3;
+----------+
| NAME     |
+----------+
| zhangsan |
+----------+
1 row in set (0.00 sec)
```

**情况二：**

```mysql
mysql> TRUNCATE TABLE user3; # DDL操作会自动提交数据，不受autocommit变量的影响
Query OK, 0 rows affected (0.04 sec)

mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('zhangsan'); # 此时不会自动提交数据
Query OK, 1 row affected (0.00 sec)

mysql> COMMIT;
Query OK, 0 rows affected (0.01 sec)

mysql> INSERT INTO user3 VALUES('lisi'); # 默认情况下（即autocommit为true），DML操作也会自动提交数据
Query OK, 1 row affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('lisi'); # 事务的失败的状态
ERROR 1062 (23000): Duplicate entry 'lisi' for key 'user3.PRIMARY'
mysql> ROLLBACK;
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT * FROM user3;
+----------+
| NAME     |
+----------+
| lisi     |
| zhangsan |
+----------+
2 rows in set (0.00 sec)
```

**情况三：**

```mysql
mysql> TRUNCATE TABLE user3; # DDL操作会自动提交数据，不受autocommit变量的影响
Query OK, 0 rows affected (0.04 sec)

mysql> SELECT @@completion_type;
+-------------------+
| @@completion_type |
+-------------------+
| NO_CHAIN          |
+-------------------+
1 row in set (0.00 sec)

mysql> SET @@completion_type = 1;
Query OK, 0 rows affected (0.00 sec)

mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('zhangsan');
Query OK, 1 row affected (0.00 sec)

mysql> COMMIT;
Query OK, 0 rows affected (0.01 sec)

mysql> SELECT * FROM user3;
+----------+
| NAME     |
+----------+
| zhangsan |
+----------+
1 row in set (0.00 sec)

mysql> INSERT INTO user3 VALUES('lisi');
Query OK, 1 row affected (0.00 sec)

mysql> INSERT INTO user3 VALUES('lisi');
ERROR 1062 (23000): Duplicate entry 'lisi' for key 'user3.PRIMARY'
mysql> ROLLBACK;
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT * FROM user3;
+----------+
| NAME     |
+----------+
| zhangsan |
+----------+
1 row in set (0.00 sec)
```

- 可以看到，相同的 SQL 代码，情况三只是在事务开始之前设置了`SET @@completion_type = 1`，但结果就和情况一的不一样，只有一个 "张三"。这是为什么呢？

这里讲解下 MySQL 中`completion_type`参数的作用，实际上这个参数有 3 种可能：

- `completion = 0`，这是`默认情况`。当执行 COMNIT 的时候会提交事务，在执行下一个事务时，还需要使用`START TRANSACTION`或者`BEGIN`来开启事务。
- `completion = 1`，这种情况下，当提交事务后，相当于执行了`COMMIT AND CHAIN`，也就是开启一个`链式事务`，即提交事务之后会开启一个相同隔离级别的事务。
- `completion = 2`，这种情况下，`CONMMIT = COMMIT AND RELEASE`，也就是提交后，会自动与服务器断开连接。

当我们设置 autocommit = 0 时，不论是否采用 START TRANSACTION 或者 BEGIN 的方式来开启事务，都需要用 COMMIT 进行提交，让事务生效，使用 ROLLBACK 对事务进行回滚。

当我们设置 autocommit = 1 时，每条 SQL 语句都会自动进行提交。不过这时，如果你采用 START TRANSACTION 或者 BEGIN 的方式来显式地开启事务，那么这个事务只有在 COMMIT 时才会生效，在 ROLLBACK 时才会回滚。

#### 示例：测试不支持事务的 Engine

创建测试的表：

```mysql
mysql> CREATE TABLE test1(i INT) ENGINE = INNODB;
Query OK, 0 rows affected (0.03 sec)

mysql> CREATE TABLE test2(i INT) ENGINE = MYISAM;
Query OK, 0 rows affected (0.01 sec)
```

针对于 InnoDB 表，ROLLBACK 会生效：

```mysql
mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO test1 VALUES (1);
Query OK, 1 row affected (0.00 sec)

mysql> ROLLBACK;
Query OK, 0 rows affected (0.00 sec)

# 执行完，发现表为空，说明回滚成功
mysql> SELECT * FROM test1;
Empty set (0.00 sec)
```

针对于 MyISAM表，不支持事务，BEGIN、ROLLBACK 都会失效：

```mysql
mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO test2 VALUES (1);
Query OK, 1 row affected (0.00 sec)

mysql> ROLLBACK;
Query OK, 0 rows affected, 1 warning (0.00 sec)

# 执行完，发现表中有上面插入的记录，说明MyISAM不支持事务
mysql> SELECT * FROM test2;
+------+
| i    |
+------+
|    1 |
+------+
1 row in set (0.00 sec)
```

#### 示例：SAVEPOINT

创建测试表，并简单测试：

```mysql
mysql> CREATE TABLE user4(name VARCHAR(15), balance DECIMAL(10, 2));
Query OK, 0 rows affected (0.03 sec)

mysql> BEGIN;
Query OK, 0 rows affected (0.01 sec)

mysql> INSERT INTO user4(name, balance) VALUES('zhangsan', 1000);
Query OK, 1 row affected (0.01 sec)

mysql> COMMIT;
Query OK, 0 rows affected (0.01 sec)

# 执行完，发现表中有上面插入的记录，说明默认创建的表是InnoDB的
mysql> SELECT * FROM user4;
+----------+---------+
| NAME     | balance |
+----------+---------+
| zhangsan | 1000.00 |
+----------+---------+
1 row in set (0.00 sec)
```

测试`SAVEPOINT`：

```mysql
# 开启事务
mysql> BEGIN;
Query OK, 0 rows affected (0.00 sec)

mysql> UPDATE user4 SET balance = balance - 100 WHERE name = 'zhangsan';
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> UPDATE user4 SET balance = balance - 100 WHERE name = 'zhangsan';
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

# 设置保存点（类似于虚拟机的快照）
mysql> SAVEPOINT s1;
Query OK, 0 rows affected (0.00 sec)

mysql> UPDATE user4 SET balance = balance + 1 WHERE name = 'zhangsan';
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

# 回滚到保存点
mysql> ROLLBACK TO s1;
Query OK, 0 rows affected (0.00 sec)

# 执行完，发现balance=800，说明回滚到保存点s1成功
mysql> SELECT * FROM user4;
+----------+---------+
| NAME     | balance |
+----------+---------+
| zhangsan |  800.00 |
+----------+---------+
1 row in set (0.00 sec)

# 由于还没有commit，所以可以对此次事务彻底回滚
mysql> ROLLBACK;
Query OK, 0 rows affected (0.00 sec)

# 执行完，发现balance=1000，说明回滚成功
mysql> SELECT * FROM user4;
+----------+---------+
| NAME     | balance |
+----------+---------+
| zhangsan | 1000.00 |
+----------+---------+
1 row in set (0.00 sec)
```

### 事务隔离级别

MySQL 是一个`客户端／服务器`架构的软件，对于同一个服务器来说，可以有若干个客户端与之连接，每个客户端与服务器连接上之后，就可以称为一个会话（`Session`）。每个客户端都可以在自己的会话中向服务器发出请求语句，一个请求语句可能是某个事务的一部分，也就是对于服务器来说可能同时处理多个事务。事务有`隔离性`的特性，理论上在某个事务`对某个数据进行访问`时，其他事务应该进行`排队` ，当该事务提交之后，其他事务才可以继续访问这个数据。但是这样对`性能影响太大` ，我们既想保持事务的隔离性，又想让服务器在处理访问同一数据的多个事务时`性能尽量高些`，那就看二者如何权衡取舍了。

#### 数据准备

```mysql
mysql> CREATE TABLE student (
    ->   studentno INT,
    ->   name VARCHAR(20),
    ->   class varchar(20),
    ->   PRIMARY KEY (studentno)
    -> ) Engine=InnoDB CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.03 sec)

mysql> INSERT INTO student VALUES(1, 'xiaogu', '1 ban');
Query OK, 1 row affected (0.01 sec)

mysql> SELECT * FROM student;
+-----------+--------+-------+
| studentno | name   | class |
+-----------+--------+-------+
|         1 | xiaogu | 1 ban |
+-----------+--------+-------+
1 row in set (0.00 sec)
```

#### 数据并发问题

针对事务的隔离性和并发性怎么做取舍呢？先看一下访问相同数据的事务在`不保证串行执行`（也就是执行完一个再执行另一个）的情况下可能会出现哪些问题。

##### 脏写（Dirty Write）

对于两个事务 Session A、Session B，如果事务 Session A`修改了`另一个`未提交`事务 Session B`修改过`的数据，那就意味着发生了**`脏写`**。

<img src="mysql-advanced/image-20231110095441305.png" alt="image-20231110095441305" style="zoom: 55%;" />

Session A 和 Session B 各开启了一个事务，Session B 中的事务先将 studentno 列为 1 的记录的 name 列更新为 "李四"，然后 Session A 中的事务接着又把这条 studentno 列为 1 的记录的 name 列更新为 "张三"。如果之后 Session B 中的事务进行了回滚，那么 Session A 中的更新也将不复存在，这科现象被称之为`脏写`。

这时，Session A 中的事务就没有效果了，明明把数据更新了，最后也提交事务了，最后看到的数据什么变化也没有。这里大家对事务的隔离级别比较了解的话，会发现默认隔离级别下，上面 Session A 中的更新语句会处于等待状态，这里只是跟大家说明一下会出现这样现象。

##### 脏读（Dirty Read）

对于两个事务 Session A、Session B，Session A`读取`了已经被 Session B`更新`但还`没有被提交`的字段。之后，如果 Session B`回滚`，Session A`读取`的内容就是`临时且无效`的。

<img src="mysql-advanced/image-20231110095911836.png" alt="image-20231110095911836" style="zoom:60%;" />

Session A 和 Session B 各开启了一个事务，Session B 中的事务先将 studentno 列为 1 的记录的 name 列更新为 "张三"，然后 Session A 中的事务再去查询这条 studentno 为 1 的记录，如果读到列 name 的值为 "张三"，而 Session B 中的事务稍后进行了回滚，那么 Session A 中的事务相当于读到了一个不存在的数据，这种现象被称之为`脏读`。

##### 不可重复读（Non-Repeatable Read）

对于两个事务 Session A、Session B，Session A`读取`了一个字段，然后 Session B`更新`了该字段。之后，如果 Session A`再次读取`同一个字段，` 值就不同`了，那就意味着发生了**`不可重复读`**。

<img src="mysql-advanced/image-20231110100549950.png" alt="image-20231110100549950" style="zoom:60%;" />

在 Session B 中提交了几个隐式事务（注意是隐式事务，意味着语句结束事务就提交了），这些事务都修改了 studentno 列为 1 的记录的列 name 的值，每次事务提交之后，如果 Session A 中的事务都可以查看到最新的值，这种现象被称之为`不可重复读`。

##### 幻读（Phantom）

对于两个事务 Session A、Session B，Session A 从一个表中`读取`了一个字段，然后 Session B 在该表中`插入`了一些新的行。之后，如果 Session A`再次读取` 同一个表，就会多出几行，那就意味着发生了**`幻读`**。

<img src="mysql-advanced/image-20231110101322891.png" alt="image-20231110101322891" style="zoom:60%;" />

Session A 中的事务先根据 studentno > 0 这个条件查询表 student，得到了 name 列值为 "张三" 的记录；之后，Session B 中提交了一个隐式事务，该事务向表 student 中插入了一条新记录；之后，Session A 中的事务再根据相同的条件 studentno > 0 查询表 student，得到的结果集中包含 Session B 中的事务新插入的那条记录，这种现象被称之为`幻读` 。我们把新插入的那些记录称之为`幻影记录`。

>注意：
>
>- 如果 Session B 中删除了一些符合 studentno > 0 的记录而不是插入新记录，那 Session A 之后再根据 studentno > 0 的条件读取的记录变少了，这种现象`不属于幻读`，幻读强调的是`一个事务按照某个相同条件多次读取记录时，后续读取读到了之前没有读到的记录`。
>- 对于先前已经读到的记录，之后又读取不到这种情况，这相当于对每一条记录都发生了`不可重复读`的现象。幻读只是重点强调了读取到了之前读取没有获取到的记录。

#### SQL 中的四种隔离级别

上面介绍了几种并发事务执行过程中可能遇到的一些问题，这些问题有轻重缓急之分，我们给这些问题按照严重性来排一下序：**`脏写 > 脏读 > 不可重复读 > 幻读`**。

我们愿意舍弃一部分隔离性来换取一部分性能在这里就体现在：**设立一些隔离级别，隔离级别越低，并发问题发生的就越多。** `SQL 标准`中设立了 4 个`隔离级别`：

- **`READ-UNCOMMITTED`**：**读未提交**，在该隔离级别，所有事务都可以看到其他未提交事务的执行结果。**可以避免脏写，但不能避免脏读、不可重复读、幻读。**
- **`READ-COMMITTED`**：**读已提交**，它满足了隔离的简单定义：一个事务只能看见已经提交事务所做的改变。这是大多数数据库系统的默认隔离级别（但不是 MySQL 默认的）。**可以避免脏写和脏读，但不能避免可重复读和幻读。**
- **`REPEATABLE-READ`**：**可重复读**，事务 A 在读到一条数据之后，此时事务 B 对该数据进行了修改并提交，那么事务 A 再读该数据，读到的还是原来的内容。**可以避免脏写、脏读和不可重复读，但不能避免幻读。**这是 MySQL 的默认隔离级别。
- **`SERIALIZABLE `**：**可串行化**，确保事务可以从一个表中读取相同的行。在这个事务持续期间，禁止其他事务对该表执行插入、更新和删除操作。**可以避免脏写、脏读、不可重复读和幻读，但性能十分低下。**

`SQL 标准`中规定，针对不同的隔离级别，并发事务可以发生不同严重程度的问题，具体情况如下：

<img src="mysql-advanced/image-20231110103236834.png" alt="image-20231110103236834" style="zoom:67%;" />

- YES 表示没有解决。

>`脏写`怎么没涉及到？**因为脏写这个问题太严重了，不论是哪种隔离级别，都不允许脏写的情况发生。**

不同的隔离级别有不同的现象，并有不同的锁和并发机制，隔离级别越高，数据库的并发性能就越差，4 种事务隔离级别与并发性能的关系如下：

<img src="mysql-advanced/image-20231110103452779.png" alt="image-20231110103452779" style="zoom: 55%;" />

#### MySQL 支持的四种隔离级别

不同的数据库厂商对 SQL 标准中规定的四种隔离级别支持不一样。比如，`Oracle 就只支持 READ-COMNITTED (默认隔离级别) 和 SERIALIZABLE 隔离级别`。MySQL 虽然支持 4 种隔离级别，但与 SQL 标准中所规定的各级隔离级别允许发生的问题却有些出入，MySQL 在 REPEATABLE READ 隔离级别下，是可以禁止幻读问题的发生的，禁止幻读的原因在后面 "多版本并发控制" 章节讲解。

MySQL 的默认隔离级别为`REPEATABLE-READ`，可以手动修改一下事务的隔离级别：

```mysql
# MySQL 5.7.20版本之后，引入transaction_isolation来替换tx_isolation
mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+-----------------+
| Variable_name         | Value           |
+-----------------------+-----------------+
| transaction_isolation | REPEATABLE-READ |
+-----------------------+-----------------+
1 row in set (0.01 sec)

# 或者不同MySQL版本中均可使用
mysql> SELECT @@transaction_isolation;
+-------------------------+
| @@transaction_isolation |
+-------------------------+
| REPEATABLE-READ         |
+-------------------------+
1 row in set (0.01 sec)
```

- MySQL 5.7.20 的版本之前，使用`SHOW VARIABLES LIKE 'tx_isolation';`命令查看隔离级别。

#### 如何设置事务的隔离级别

**1. 通过下面的语句修改事务的隔离级别。**

```mysql
SET [GLOBAL|SESSION] TRANSACTION ISOLATION LEVEL <隔离级别>;

# 其中，隔离级别格式：
> READ-UNCOMMITTED
> READ-COMMITTED
> REPEATABLE-READ
> SERIALIZABLE
```

或者：

```mysql
SET [GLOBAL|SESSION] TRANSACTION_ISOLATION = '<隔离级别>'

#其中，隔离级别格式：
> READ-UNCOMMITTED
> READ-COMMITTED
> REPEATABLE-READ
> SERIALIZABLE
```

**2. 关于设置时使用 GLOBAL 或 SESSION 的影响。**

使用`GLOBAL`关键字（在全局范围影响）：

```mysql
SET GLOBAL TRANSACTION ISOLATION LEVEL SERIALIZABLE;
# 或
SET GLOBAL TRANSACTION_ISOLATION = 'SERIALIZABLE';
```

- 当前已经存在的会话无效。
- 只对执行完该语句之后产生的会话起作用。

使用`SESSION`关键字（在会话范围影响）：

```mysql
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
# 或
SET SESSION TRANSACTION_ISOLATION = 'SERIALIZABLE';
```

- 对当前会话的所有后续的事务有效。
- 如果在事务之间执行，则对后续的事务有效。
- 该语句可以在已经开启的事务中间执行，但不会影响当前正在执行的事务。

如果在服务器启动时想改变事务的默认隔离级别，可以修改启动参数`TRANSACTION_ISOLATION`的值。比如，在启动服务器时指定了 "TRANSACTION_ISOLATION = 'SERIALIZABLE'"，那么事务的默认隔离级别就从原来的 REPEATABLE-READ 变成了 SERIALIZABLE。

>数据库规定了多种事务隔离级别，不同隔离级别对应不同的干扰程度，隔离级别越高，数据一致性就越好，但并发性越弱。

**3. 演示 GLOBAL。**

当前会话设置 "TRANSACTION_ISOLATION = 'SERIALIZABLE'"，当前会话隔离级别未变：

```mysql
mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+-----------------+
| Variable_name         | Value           |
+-----------------------+-----------------+
| transaction_isolation | REPEATABLE-READ |
+-----------------------+-----------------+
1 row in set (0.00 sec)

mysql> SET GLOBAL TRANSACTION_ISOLATION = 'SERIALIZABLE';
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+-----------------+
| Variable_name         | Value           |
+-----------------------+-----------------+
| transaction_isolation | REPEATABLE-READ |
+-----------------------+-----------------+
1 row in set (0.00 sec)
```

新会话隔离级别发生改变：

```mysql
mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+--------------+
| Variable_name         | Value        |
+-----------------------+--------------+
| transaction_isolation | SERIALIZABLE |
+-----------------------+--------------+
1 row in set (0.00 sec)
```

把当前会话退出重新登陆后，隔离级别发生改变。

> MySQL 服务重启后，隔离级别又重新回到默认，因为设置的都是在内存级别的。

**4. 演示 SESSION。**

当前会话设置 "TRANSACTION_ISOLATION = 'SERIALIZABLE'"，当前会话隔离级别就发生了改变，其他会话也发生了改变。

```mysql
mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+-----------------+
| Variable_name         | Value           |
+-----------------------+-----------------+
| transaction_isolation | REPEATABLE-READ |
+-----------------------+-----------------+
1 row in set (0.01 sec)

mysql> SET SESSION TRANSACTION_ISOLATION = 'SERIALIZABLE';
Query OK, 0 rows affected (0.01 sec)

mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+--------------+
| Variable_name         | Value        |
+-----------------------+--------------+
| transaction_isolation | SERIALIZABLE |
+-----------------------+--------------+
1 row in set (0.01 sec)
```

#### 不同隔离级别举例

创建数据表，并初始化数据：

```mysql
mysql> CREATE TABLE account(
    -> id INT PRIMARY KEY AUTO_INCREMENT,
    -> name VARCHAR(15),
    -> balance DECIMAL(15)
    -> );
Query OK, 0 rows affected (0.03 sec)

mysql> INSERT INTO account VALUES(1, 'zhangsan', '100'), (2, 'lisi', '0');
Query OK, 2 rows affected (0.01 sec)
Records: 2  Duplicates: 0  Warnings: 0
```

打开两个 Session，模拟两个事务，并将两个 Session 中的隔离级别都设置成 READ-UNCOMMITTED：

```mysql
mysql> SET SESSION TRANSACTION_ISOLATION = 'READ-UNCOMMITTED';
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW VARIABLES LIKE 'transaction_isolation';
+-----------------------+------------------+
| Variable_name         | Value            |
+-----------------------+------------------+
| transaction_isolation | READ-UNCOMMITTED |
+-----------------------+------------------+
1 row in set (0.00 sec)
```

##### 演示 1：读未提交之脏读

案例一：

- 顺序 1：

  ```mysql
  # 事务1中更新数据，但是还没提交
  mysql> BEGIN;
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> UPDATE account SET balance = balance + 100 WHERE id = 1;
  Query OK, 1 row affected (0.00 sec)
  Rows matched: 1  Changed: 1  Warnings: 0
  ```

- 顺序 2：

  ```mysql
  # 事务2中读取到了事务1还没提交的数据，出现了脏读
  mysql> SELECT * FROM account;
  +----+----------+---------+
  | id | name     | balance |
  +----+----------+---------+
  |  1 | zhangsan |     200 |
  |  2 | lisi     |       0 |
  +----+----------+---------+
  2 rows in set (0.00 sec)
  ```

- 顺序 3：

  ```mysql
  # 事务1回滚
  mysql> ROLLBACK;
  Query OK, 0 rows affected (0.01 sec)
  ```

- 顺序  4：

  ```mysql
  # 事务2读取的数据也变为之前的值
  mysql> SELECT * FROM account;
  +----+----------+---------+
  | id | name     | balance |
  +----+----------+---------+
  |  1 | zhangsan |     100 |
  |  2 | lisi     |       0 |
  +----+----------+---------+
  2 rows in set (0.00 sec)
  ```

案例二：

<img src="mysql-advanced/image-20231110131612475.png" alt="image-20231110131612475" style="zoom:80%;" />

##### 演示 2：读已提交之不可重复读

环境准备：

```mysql
mysql> TRUNCATE TABLE account;
Query OK, 0 rows affected (0.02 sec)

mysql> INSERT INTO account VALUES(1, '张三', '100'), (2, '李四', '0');
Query OK, 2 rows affected (0.01 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> SELECT * FROM account;
+----+--------+---------+
| id | name   | balance |
+----+--------+---------+
|  1 | 张三   |     100 |
|  2 | 李四   |       0 |
+----+--------+---------+
2 rows in set (0.01 sec)
```

将两个 Session 的隔离级别设置为 READ-COMMITTED：

```mysql
mysql> SET SESSION transaction_isolation = 'READ-COMMITTED';
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT @@transaction_isolation;
+-------------------------+
| @@transaction_isolation |
+-------------------------+
| READ-COMMITTED          |
+-------------------------+
1 row in set (0.00 sec)
```

演示图解：

<img src="mysql-advanced/image-20231110131944123.png" alt="image-20231110131944123" style="zoom:80%;" />

##### 演示 3：可重复读

将两个 Session 的隔离级别设置为 REPEATABLE-READ：

```mysql
mysql> SET SESSION transaction_isolation = 'REPEATABLE-READ';
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT @@transaction_isolation;
+-------------------------+
| @@transaction_isolation |
+-------------------------+
| REPEATABLE-READ         |
+-------------------------+
1 row in set (0.00 sec)
```

演示图解：

<img src="mysql-advanced/image-20231110132350026.png" alt="image-20231110132350026" style="zoom:80%;" />

##### 演示 4：幻读

<img src="mysql-advanced/image-20231110133731697.png" alt="image-20231110133731697" style="zoom:80%;" />

这里要灵活的理解读取的意思。第一次 SELECT 是读取，第二次的 INSERT 其实也属于隐式的读取，只不过是在 MySQL 的机制中读取的，**插入数据也是要先读取一下有没有主键冲突才能决定是否执行插入。**

**幻读，并不是说两次读取获取的结果集不同，幻读侧重的方面是某一次的 SELECT 操作得到的结果所表征的数据状态，无法支撑后续的业务操作。更为具体一些：SELECT 某记录是否存在，发现不存在，准备插入此记录，但执行 INSERT 时发现此记录已存在，无法插入，此时就发生了幻读（如上图所示）。**

在 REPEATABLE-READ 隔离级别下，step1、step2 是会正常执行的，step3 则会报错主键冲突，对于事务 B 的业务来说是执行失败的，这里事务 B 就是发生了幻读，因为事务 B 在 step1 中读取的数据状态并不能支撑后续的业务操作，事务 B："见鬼了，我刚才读到的结果应该可以支持我这样操作才对啊，为什么现在不可以？"事务 B 不敢相信的又执行了 step4，发现和 step1 读取的结果是一样的（REPEATABLE-READ下的 MVCC 机制)。此时，幻读无疑已经发生，事务 B 无论读取多少次，都查不到 id = 3 的记录，但它的确无法插入这条他通过读取来认定不存在的记录（此数据已被事务 A 插入)，对于事务 B 来说，它幻读了。

其实 REPEATABLE-READ（MySQL 默认隔离级别）也是可以避免幻读的，通过对 SELECT 操作手动加行 X 锁（独占锁）（SELECT … FOR UPDATE，这也正是 SERIALIZABLE 隔离级别下会隐式为你做的事情），同时，即便当前记录不存在，比如 id = 3 是不存在的，当前事务也会获得一把记录锁（因为 InnoDB 的行锁锁定的是索引，故记录实体存在与否没关系，存在就加`行 X 锁`，不存在就加`间隙锁`），他事务则无法插入此索引的记录，故杜绝了幻读。

在`SERIALIZABLE 隔离级别`下，step1 执行时，会隐式的添加`行 (X) 锁 / gap (X) 锁`的，从而 step2 会被阻塞，step3 会正常执行，待事务 1 提交后，事务 2 才能继续执行（主键冲突执行失败），对于事务 1 来说业务是正确的，成功的阻塞扼杀了扰乱业务的事务 2，对于事务 1 来说他前期读取的结果是可以支撑其后续业务的。

所以 MySQL 的幻读并非什么读取两次返回结果集不同，而是事务在插入事先检测不存在的记录时，惊奇的发现这些数据已经存在了，之前的检测读获取到的数据如同鬼影一般。

##### 演示 5：隔离级别是 SERIALIZABLE 时的效果

<img src="mysql-advanced/image-20231110135453439.png" alt="image-20231110135453439" style="zoom:80%;" />

### 事务的常见分类

从事务理论的角度来看，可以把事务分为以下几种类型：

- 扁平事务（Flat Transactions）
- 带有保存点的扁平事务（Flat Transactions with Savepoints）
- 链事务（Chained Transactions）
- 嵌套事务（Nested Transactions）
- 分布式事务（Distributed Transactions）

下面简单介绍这几种类型：

- `扁平事务`：是事务类型中最简单的一种，但是在实际生产环境中，这可能是使用最频繁的事务。**在扁平事务中，所有操作都处于同一层次，其由 BEGIN WORK 开始，由 COMMIT WORK 或 ROLLBACK WORK 结束，其间的操作是原子的，要么都执行，要么都回滚。**因此，扁平事务是应用程序成为原子操作的基本组成模块。扁平事务虽然简单，但是在实际环境中使用最为频繁，也正因为其简单，使用频繁，故每个数据库系统都实现了对扁平事务的支持。**扁平事务的主要限制是不能提交或者回滚事务的某一部分，或分几个步骤提交。**扁平事务一般有三种不同的结果：

  - ① 事务成功完成。在平常应用中约占所有事务的 96%。
  - ② 应用程序要求停止事务。比如应用程序在捕获到异常时会回滚事务，约占事务的 3%。
  - ③ 外界因素强制终止事务。如连接超时或连接断开，约占所有事务的 1%。

- `带有保存点的扁平事务`：除了支持扁平事务支持的操作外，还允许在事务执行过程中回滚到同一事务中较早的一个状态。这是因为某些事务可能在执行过程中出现的错误并不会导致所有的操作都无效，放弃整个事务不合乎要求，开销太大。

  - `保存点 (Savepoint)`用来通知事务系统应该记住事务当前的状态，以便当之后发生错误时，事务能回到保存点当时的状态。对于扁平的事务来说，隐式的设置了一个保存点，然而在整个事务中，只有这一个保存点，因此，回滚只能会滚到事务开始时的状态。

- `链事务`：是指一个事务由多个子事务链式组成，它可以被视为保存点模式的一个变种。带有保存点的扁平事务，当发生系统崩溃时，所有的保存点都将消失，这意味着当进行恢复时，事务需要从开始处重新执行，而不能从最近的一个保存点继续执行。链事务的思想是：在提交一个事务时，释放不需要的数据对象，将必要的处理上下文隐式地传给下一个要开始的事务，前一个子事务的提交操作和下一个子事务的开始操作合并成一个原子操作，这意味着下一个事务将看到上一个事务的结果，就好像在一个事务中进行一样。这样，**在提交子事务时就可以释放不需要的数据对象，而不必等到整个事务完成后才释放**。其工作方式如下：

  <img src="mysql-advanced/image-20231110144102235.png" alt="image-20231110144102235" style="zoom: 67%;" />

  - 链事务与带有保存点的扁平事务的不同之处体现在：

    - 带有保存点的扁平事务能回滚到任意正确的保存点，而链事务中的回滚仅限于当前事务，即只能恢复到最近的一个保存点。

    - 对于锁的处理，两者也不相同，链事务在执行 COMMIT 后即释放了当前所持有的锁，而带有保存点的扁平事务不影响迄今为止所持有的锁。

- `嵌套事务`：是个层次结构框架，由一个顶层事务（Top-Level Transaction）控制着各个层次的事务，顶层事务之下嵌套的事务被称为子事务（Subtransaction），其控制着每一个局部的变换，子事务本身也可以是嵌套事务。因此，嵌套事务的层次结构可以看成是一棵树。

- `分布式事务`：通常是在一个分布式环境下运行的扁平事务，因此，需要根据数据所在位置访问网络中不同节点的数据库资源。例如，一个银行用户从招商银行的账户向工商银行的账户转账 1000 元，这里需要用到分布式事务，因为不能仅调用某一家银行的数据库就完成任务。

## MySQL 事务日志

事务有 4 种特性：`原子性`、`一致性`、`隔离性`和`持久性`。那么事务的 4 种特性到底是基于什么机制实现呢？

- 事务的**隔离性**由`锁机制`实现。
- 而事务的**原子性、一致性和持久性**由事务的`redo log`和`undo log`来保证。
  - redo log 称为`重做日志`，提供再写入操作，恢复提交事务修改的页操作，用来保证事务的**持久性**。
  - undo log 称为`回滚日志`，回滚行记录到某个特定版本，用来保证事务的**原子性、一致性**。

有的 DBA 或许会认为 undo log 是 redo log 的逆过程，其实不然。redo log 和 undo log 都可以视为是一种`恢复操作`。但是：

- redo log：是存储引擎层（InnoDB）生成的日志，记录的是`物理级别`上的页修改操作，比如页号 xxx、偏移量 yyy 写入了 zzz 数据。主要为了保证`数据的可靠性`。
- undo log：是存储引擎层（InnoDB）生成的日志，记录的是`逻辑操作`日志，比如对某一行数据进行了 INSERT 语句操作，那么 undo log 就记录一条与之相反的 DELETE 操作。主要用于事务的`回滚`（undo log 记录的是每个修改操作的`逆操作`）和`一致性非锁定读`（undo log 回滚行记录到某种特定的版本 ---> MVCC，即多版本并发控制）。

### redo log

InnoDB 存储引擎是`以页为单位`来管理存储空间的。在真正访问页面之前，需要把在`磁盘上的页`缓存到内存中的`Buffer Pool`之后才可以访问。所有的变更都必须先更新缓冲池中的数据，然后缓冲池中的脏页会以一定的频率被刷入磁盘（`checkpoint 机制`），通过缓冲池来优化 CPU 和磁盘之间的鸿沟，这样就可以保证整体的性能不会下降太快。

#### 为什么需要 redo log

一方面，缓冲池可以帮助我们消除 CPU 和磁盘之间的鸿沟，checkpoint 机制可以保证数据的最终落盘，然而由于 checkpoint 并不是每次变更的时候就触发的，而是 master 线程隔一段时间去处理的。所以最坏的情况就是事务提交后，刚写完缓冲池，数据库宕机了，那么这段数据就是丢失的，无法恢复。

另一方面，事务包含持久性的特性，就是说对于一个已经提交的事务，在事务提交后即使系统发生了崩溃，这个事务对数据库中所做的更改也不能丢失。

那么如何保证这个持久性呢？一个简单的做法：`在事务提交完成之前把该事务所修改的所有页面都刷新到磁盘`。但是这个简单粗暴的做法有些问题：

- **修改量与刷新磁盘工作量严重不成比例**
  - 有时候仅仅修改了某个页面中的一个字节，但是我们知道在 InnoDB 中是以页为单位来进行磁盘 I/O 的，也就是说在该事务提交时不得不将一个完整的页面从内存中刷新到慈盘，我们又知道一个页面默认是 16 KB 大小，只修改一个字节就要刷新 16 KB 的数据到磁盘上，显然是太小题大做了。
- **随机 I/O 刷新较慢**
  - 一个事务可能包含很多语句，即使是一条语句也可能修改许多页面，假如该事务修改的这些页面可能并不相邻，这就意味着在将某个事务修改的 Buffer Pool 中的页面刷新到磁盘时，需要进行很多的随机 I/O，随机 I/O 比顺序 I/O 要慢，尤其对于传统的机械硬盘来说。

另一个解决的思路：我们只是想让已经提交了的事务对数据库中数据所做的修改永久生效，即使后来系统崩溃，在重启后也能把这种修改恢复出来。所以，`其实没有必要在每次事务提交时就把该事务在内存中修改过的全部页面刷新到磁盘，只需要把修改了哪些东西记录一下就好。`比如，某个事务将 0 号系统表空间中第 10 号页面中偏移量为 100 处的值 1 改成 2，我们只需记录一下："将第 0 号表空间的第 10 号页面中偏移量为 100 处的值更新为 2"。

InnoDB 引擎的事务采用了`WAL 技术 (Write-Ahead Logging)`，这种技术的思想就是`先写日志，再写磁盘`，只有日志写入成功，才算事务提交成功，这里的日志就是 redo log。当发生宕机且数据未刷到磁盘的时候，可以通过 redo log 来恢复，保证 ACID 中的 D，这就是 redo log 的作用。

<img src="mysql-advanced/image-20231110193214624.png" alt="image-20231110193214624" style="zoom:67%;" />

#### redo log 的好处和特点

好处：

- redo log 降低了刷盘频率。
- redo log 占用的空间非常小。redo log 存储`表空间 ID`、`页号`、`偏移量`以及`需要更新的值`，所需的存储空间是很小的，刷盘快。

特点：

- redo log 是顺序写入磁盘的。
  - 在执行事务的过程中，每执行一条语句，就可能产生若干条 redo log，这些日志是按照产生的顺序写入磁盘的，也就是使用顺序 I/O，效率比随机 I/O 快。
- 事务执行过程中，redo log 不断记录。
  - redo log 跟 bin log 的区别，redo log 是`存储引擎层`产生的，而 bin log 是`数据库层`产生的。假设一个事务，对表做 10 万行的记录插入，在这个过程中，一直不断的往 redo log 顺序记录，而 bin log 不会记录，直到这个事务提交，才会一次写入到 bin log 文件中（`bin log 是记录主从复制的文件`）。

#### redo log 的组成

redo log 可以简单分为以下两个部分：

- `重做日志缓冲 (redo log buffer)` ，保存在内存中，是易失的。
- `重做日志文件 (redo log file)`，保存在硬盘中，是持久的。

##### redo log buffer

在服务器启动时，就向操作系统申请了一大片称之为`redo log buffer`的`连续内存空间`，翻译成中文就是重做日志缓冲区。这片内存空间被划分成若干个连续的`redo log block`。`一个 redo log block 占用 512 字节大小`。

<img src="mysql-advanced/image-20231110194328602.png" alt="image-20231110194328602" style="zoom:67%;" />

**参数设置：`innodb_log_buffer_size`。**

- redo log buffer 的大小，默认 16 MB，最大值是 4096 MB，最小值为 1 MB。

  ```mysql
  mysql> SHOW VARIABLES LIKE '%innodb_log_buffer_size%';
  +------------------------+----------+
  | Variable_name          | Value    |
  +------------------------+----------+
  | innodb_log_buffer_size | 16777216 |
  +------------------------+----------+
  1 row in set (0.00 sec)
  ```

##### redo log file

redo log file 如图所示，其中的`ib_logfile0`和`ib_logfile1`即为 redo log：

<img src="mysql-advanced/image-20231110194915124.png" alt="image-20231110194915124" style="zoom:67%;" />

#### redo log 的整体流程

以一个更新事务为例，redo log 的流转过程，如下图所示：

<img src="mysql-advanced/image-20231110195130060.png" alt="image-20231110195130060" style="zoom:67%;" />

- 第 1 步：先将原始数据从磁盘中读入内存中来，修改数据的内存拷贝。
- 第 2 步：生成一条重做日志，并写入 redo log buffer，记录的是数据被修改后的值。
- 第 3 步：当事务 COMMIT 时，将 redo log buffer 中的内容刷新到 redo log file，对 redo log file 采用追加写的方式。
- 第 4 步：定期将内存中修改的数据刷新到磁盘中。

>`Write-Ahead Log`：预先日志持久化，在持久化一个数据页之前，先将内存中相应的日志页持久化。

#### redo log 的刷盘策略

redo log 的写入并不是直接写入磁盘的，InnoDB 引擎会在写 redo log 的时候先写 redo log buffer，之后以`一定的频率`刷到真正的 redo log file 中。这里的一定频率怎么看待呢？这就是我们要说的`刷盘策略`。

<img src="mysql-advanced/image-20231110195627970.png" alt="image-20231110195627970" style="zoom:80%;" />

注意，redo log buffer 刷盘到 redo log file 的过程，并不是真正的刷到磁盘中去，只是刷入到`文件系统缓存`（`page cache`）中去（OS buffer，这是现代操作系统为了提高文件写入效率做的一个优化），真正的写入会交给`系统自己来决定`（比如 page cache 足够大了）。那么对于 InnoDB 来说就存在一个问题，如果交给系统来同步，同样如果系统宕机，那么数据也丢失了（虽然整个系统宕机的概率还是比较小的）。

针对这种情况，InnoDB 给出`innodb_flush_log_at_trx_commit `参数，该参数控制 COMMIT 提交事务时，如何将 redo log buffer 中的日志刷新到 redo log file 中。它支持三种策略：

- `设置为 0`：每次提交事务时，不会将 redo log buffer 中的日志写入 page cache，而是通过一个单独的线程，**每秒**写入 page cache 并调用系统的 fsync() 函数写入磁盘的 redo log file。这种方式不是实时写磁盘的， 而是每隔 1 秒写一次日志，如果系统崩溃，可能会丢失 1 秒的数据。

- `设置为 1`：**默认值**，每次提交事务时，都会将 redo log buffer 中的日志写入 page cache 中，并且会调用 fsync() 函数将日志写入 redo log file 中。这种方式虽然不会再崩溃时丢失数据，但是性能比较差。

  ```mysql
  mysql> SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';
  +--------------------------------+-------+
  | Variable_name                  | Value |
  +--------------------------------+-------+
  | innodb_flush_log_at_trx_commit | 1     |
  +--------------------------------+-------+
  1 row in set (0.00 sec)
  ```

- `设置为 2`：每次提交事务时，都只将 redo log buffer 中的日志写入 page cache 中，之后每隔 1 秒，通过 fsync() 函数将 page cache 中的数据写入 redo log file 中。

>write：`刷盘`，指的是 MySQL 从 buffer pool 中将内容写到系统的 page cache 中，并没有持久化到系统磁盘上。这个速度其实是很快的。
>
>fsync：`持久化到磁盘`，指的是从系统的 page cache 中，将数据持久化到系统磁盘上。这个速度可以认为比较慢，而且也是 IOPS 升高的真正原因。

另外，InnoDB 存储引擎有一个后台线程，`每隔 1 秒`，就会把 redo log buffer 中的内容写到文件系统缓存（page cache），然后调用刷盘操作。

<img src="mysql-advanced/image-20231110204145340.png" alt="image-20231110204145340" style="zoom:80%;" />

**也就是说，一个没有提交事务的 redo log 记录，也可能会刷盘。因为在事务执行过程 redo log 记录是会写入 redo log buffer 中，这些 redo log 记录会被后台线程刷盘。**

<img src="mysql-advanced/image-20231110204328011.png" alt="image-20231110204328011" style="zoom:80%;" />

除了后台线程每秒 1 次的轮询操作，还有一种情况，当 redo log buffer 占用的空间即将达到 innodb_log_buffer_size 的`一半`的时候，后台线程会主动刷盘。

#### 不同刷盘策略演示

##### 刷盘策略分析

**1. innodb_flush_log_at_trx_commit = 1**

<img src="mysql-advanced/image-20231110204709173.png" alt="image-20231110204709173" style="zoom:80%;" />

- innodb_flush_log_at_trx_commit = 1 时，`只要事务提交成功，都会主动同步刷盘，这个速度是很快的。`最终 redo log 记录就一定在硬盘里，不会有任何数据丢失。
- `如果事务执行期间 MySQL 挂了或宕机，这部分日志丢了，但是事务并没有提交，所以日志丢了也不会有损失。`可以保证 ACID 的 D，数据绝对不会丢失，但是这种效率是最差的。
- 建议使用默认值，虽然操作系统宕机的概率理论小于数据库宕机的概率，但是一般既然使用了事务，那么数据的安全相对来说更重要些。

**2. innodb_flush_log_at_trx_commit = 2**

<img src="mysql-advanced/image-20231110211552048.png" alt="image-20231110211552048" style="zoom:80%;" />

- innodb_flush_log_at_trx_commit = 2 时，只要事务提交成功，redo log buffer 中的内容就会写入文件系统缓存（page cache）。
- 如果只是 MySQL 挂了不会有任何数据丢失，但是操作系统宕机可能会有 1 秒数据的丢失，这种情况下无法满足 ACID 中的 D。
- 数值 2 是一种折中的做法，它的 I/O 效率理论是高于 1，低于 0 的。当进行调优时，为了降低 CPU 的使用率，可以从 1 降成 2，因为 O/S 出现故障的概率很小。

**3. innodb_flush_log_at_trx_commit = 0**

<img src="mysql-advanced/image-20231110212019323.png" alt="image-20231110212019323" style="zoom:80%;" />

- innodb_flush_log_at_trx_commit = 0 时，master thread 中每 1 秒进行一次重做日志的 fsync 操作，因此实例 crash，`最多丢失 1 秒钟内的事务`。（master thread 是负责将缓冲池中的数据异步刷新到磁盘，保证数据的一致性）
- 数值 0，是一种效率最高的做法，这种策略有丢失数据的风险，也无法保证 D。

> 一句话，**`0：延迟写，延迟刷`，`1：实时写，实时刷`，`2：实时写，延迟刷`**。

##### 示例

比较 innodb_flush_log_at_trx_commit 对事务的影响。准备数据：

```mysql
mysql> USE atguigudb2;
Database changed

mysql> CREATE TABLE test_load(
    -> a INT,
    -> b CHAR(80)
    -> )ENGINE=INNODB;
Query OK, 0 rows affected (0.04 sec)

mysql> DELIMITER //
mysql> CREATE PROCEDURE p_load(COUNT INT UNSIGNED)
    -> BEGIN
    -> DECLARE s INT UNSIGNED DEFAULT 1;
    -> DECLARE c CHAR(80)DEFAULT REPEAT('a',80);
    -> WHILE s<=COUNT DO
    -> INSERT INTO test_load SELECT NULL,c;
    -> COMMIT;
    -> SET s=s+1;
    -> END WHILE;
    -> END //
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
```

innodb_flush_log_at_trx_commit = 1 时：

```mysql
mysql> SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';
+--------------------------------+-------+
| Variable_name                  | Value |
+--------------------------------+-------+
| innodb_flush_log_at_trx_commit | 1     |
+--------------------------------+-------+
1 row in set (0.00 sec)

mysql> CALL p_load(30000);
Query OK, 0 rows affected (2 min 3.70 sec)
```

innodb_flush_log_at_trx_commit = 2 时：

```mysql
mysql> TRUNCATE TABLE test_load;
Query OK, 0 rows affected (0.04 sec)

mysql> SET GLOBAL innodb_flush_log_at_trx_commit = 2;
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';
+--------------------------------+-------+
| Variable_name                  | Value |
+--------------------------------+-------+
| innodb_flush_log_at_trx_commit | 2     |
+--------------------------------+-------+
1 row in set (0.00 sec)

mysql> CALL p_load(30000);
Query OK, 0 rows affected (1 min 4.00 sec)
```

innodb_flush_log_at_trx_commit = 0 时：

```mysql
mysql> TRUNCATE TABLE test_load;
Query OK, 0 rows affected (0.03 sec)

mysql> SET GLOBAL innodb_flush_log_at_trx_commit = 0;
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';
+--------------------------------+-------+
| Variable_name                  | Value |
+--------------------------------+-------+
| innodb_flush_log_at_trx_commit | 0     |
+--------------------------------+-------+
1 row in set (0.00 sec)

mysql> CALL p_load(30000);
Query OK, 0 rows affected (1 min 0.72 sec)
```

最终结果：

| innodb_flush_logat_trx_commit | 执行所用的时间 |
| ----------------------------- | -------------- |
| 0                             | 1 min 0.72 sec |
| 1                             | 2 min 3.70 sec |
| 2                             | 1 min 4.00 sec |

针对上述存储过程，为了提高事务的提交性能，应该在将 3 万行记录插入表后进行一次的 COMMIT 操作，而不是每插入一条记录后就进行一次 COMMIT 操作，这样做的好处是可以使事务方法在 ROLLBACK 时，回滚到事务最开始的确定状态。

>注意：虽然用户可以通过设置参数 innodb_flush_log_at_trx_commit 为 0 或 2 来提高事务提交的性能，但需清楚，这种设置方法丧失了事务的 ACID 特性。

#### 写入 redo log buffer 过程

##### Mini-Transaction

MySQL 把对底层页面中的一次原子访问的过程，称之为一个`Mini-Transaction`，简称 mtr。比如，向某个索引对应的 B+Tree 中，插入一条记录的过程就是一个 Mini-Transaction。`一个 mtr 可以包含一组 redo log`，在进行崩溃恢复时这一组 redo log 作为一个不可分割的整体。

一个事务可以包含若干条语句，`每一条语句其实是由若干个 mtr 组成，每一个 mtr 又可以包含若干条 redo log`，画个图表示它们的关系就是这样：

<img src="mysql-advanced/image-20231110213606186.png" alt="image-20231110213606186" style="zoom: 55%;" />

- 一个事务由多条 SQL 语句组成。
- 一条 SQL 语句包含多个 mtr，因为一条 SQL 可能改变多条记录。
- 一个 mtr 对应多条 redo log，因为 redo log 存放的是物理级别的修改，当插入语句且页分裂时，会出现大量比如 "A 页 xxx、偏移量 yy 写入了 zzz 数据"，"B 页面 aaa、偏移量 bb 写入了 ccc 数据" 这样的记录。

##### redo log 写入 redo log buffer

向 redo log buffer 中写入 redo log 的过程是顺序的，也就是先往前边的 block 中写，当该 block 的空闲空间用完之后，再往下一个 block 中写。当想往 redo log buffer 中写入 redo log 时，第一个遇到的问题就是应该写在哪个 block 的哪个偏移量处，所以 InnoDB 的设计者特意提供了一个称之为`buf_free`的全局变量，该变量指明后续写入的 redo log 应该写入到 redo log buffer 中的哪个位置，如图所示：

<img src="mysql-advanced/image-20231110214215918.png" alt="image-20231110214215918" style="zoom: 80%;" />

一个 mtr 执行过程中，可能产生若干条 redo log，`这些 redo log 是一个不可分割的组`，所以其实并不是每生成一条 redo log，就将其插入到 redo log buffer 中，而是每个 mtr 运行过程中产生的日志`先暂时存到一个地方`，当该 mtr 结束的时候，将过程中产生的一组 redo log 再全部复制到 redo log buffer 中。假设有两个名为 T1、T2 的事务，每个事务都包含 2 个 mtr，我们给这几个 mtr 命名一下：

- 事务 T1 的两个 mtr 分别称为 mtr_T1_1 和 mtr_T1_2。
- 事务 T2 的两个 mtr 分别称为 mtr_T2_1 和 mtr_T2_2。

每个 mtr 都会产生一组 redo log，用示意图来描述一下这些 mtr 产生的日志情况：

<img src="mysql-advanced/image-20231110214607022.png" alt="image-20231110214607022" style="zoom:80%;" />

不同的事务可能是`并发`执行的，所以 T1、T2 之间的 mtr 可能是`交替执行`的。每当一个 mtr 执行完成时，伴随该 mtr 生成的一组 redo log 就需要被复制到 redo log buffer 中，也就是说，`不同事务的 mtr 可能是交替写入 redo log buffer 的`，我们画个示意图（为了美观，把一个 mtr 中产生的所有的 redo log 当作一个整体来画）：

<img src="mysql-advanced/image-20231110215030086.png" alt="image-20231110215030086" style="zoom: 80%;" />

有的 mtr 产生的 redo log 量可能非常大，比如 mtr_t1_2 产生的 redo log 占用空间比较大，占用了 3 个 block 来存储。

##### redo log block 的结构图

一个 redo log block 是由`日志头`、`日志体`、`日志尾`组成。日志头占用 12 字节，日志尾占用 8 字节，所以一个 block 真正能存储的数据就是 512 - 12 - 8 = 492 字节。

<img src="mysql-advanced/image-20231110215410008.png" alt="image-20231110215410008" style="zoom: 67%;" />

**真正的 redo log 都是存储到占用 496 字节大小的 log block body 中，图中的 log block header 和 log block trailer 存储的是一些管理信息。**我们来看看这些所谓的管理信息都有什么。

<img src="mysql-advanced/image-20231110215536304.png" alt="image-20231110215536304" style="zoom:80%;" />

- `log block header`的属性分别如下：
  - `LOG_BLOCK_HDR_NO `：log buffer 是由 log block 组成，在内部 log buffer 就好似一个数组，因此 LOG_BLOCK_HDR_NO 用来标记这个数组中的位置。其是递增并且循环使用的，占用 4 个字节，但是由于第—位用来判新是否是 flush bit，所以最大的值为 2 GB。
  - `LOG_BLOCK_HDR_DATA_LEN`：表示 block 中已经使用了多少字节，初始值为`12`，因为 log block body 从第 12 个字节处开始。随着往 block 中写入的 redo log 越来越多，该属性值也跟着增长。如果 log block body 已经被全部写满，那么该属性的值被设置为`512`。
  - `LOG_BLOCK_FIRST_REC_GROUP `：一条 redo log 也可以称之为一条 redo 日志记录（redo log record)，一个 mtr 会生产多条 redo 日志记录，这些 redo 日志记录被称之为一个 redo 日志记录组（redo log record group）。LOG_BLOCK_FIRST_REC_GROUP 就代表该 block 中第一个 mtr 生成的 redo 日志记录组的偏移量（其实也就是这个 block 里，第一个 mtr 生成的第一条 redo 日志的偏移量）。如果该值的大小与 LOG_BLOCK_HDR_DATA_LEN 相同，则表示当前 log block 不包含新的日志。
  - `LOG_BLOCK_CHECKPOINT_NO`：占用 4 字节，表示该 log block 最后被写入时的`checkpoint`。
- `log block trailer`的属性如下：
  - `LOG_BLOCK_CHECKSUN`：表示 block 的校验值，用于正确性校验（其值和 LOG_BLOCK_HDR_NO 相同)，暂时不关心它。

#### redo log file 

##### 相关参数设置

- `innodb_log_group_home_dir `：指定 redo log 文件组所在的路径，默认值为`./`，表示在数据库的数据目录下。MySQL 的默认数据目录（`var/lib/mysql` ）下默认有两个名为`ib_logfile0 `和`ib_logfile1 `的文件，log buffer 中的日志，默认情况下就是刷新到这两个磁盘文件中。此 redo log 文件组位置可以修改。

  ```mysql
  mysql> SHOW VARIABLES LIKE 'innodb_log_group_home_dir';
  +---------------------------+-------+
  | Variable_name             | Value |
  +---------------------------+-------+
  | innodb_log_group_home_dir | ./    |
  +---------------------------+-------+
  1 row in set (0.00 sec)
  ```

- `innodb_log_files_in_group`：指明 redo log file 的个数，命名方式如 ib_logfile0，iblogfile1，…，iblogfilen。默认 2 个，最大 100 个。

  ```mysql
  mysql> SHOW VARIABLES LIKE 'innodb_log_files_in_group';
  +---------------------------+-------+
  | Variable_name             | Value |
  +---------------------------+-------+
  | innodb_log_files_in_group | 2     |
  +---------------------------+-------+
  1 row in set (0.00 sec)
  ```

- `innodb_flush_log_at_trx_commit`：控制 redo log 刷新到磁盘的策略，默认为`1`。

  ```mysql
  mysql> SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';
  +--------------------------------+-------+
  | Variable_name                  | Value |
  +--------------------------------+-------+
  | innodb_flush_log_at_trx_commit | 1     |
  +--------------------------------+-------+
  1 row in set (0.01 sec)
  ```

- `innodb_log_file_size`：单个 redo log file 设置大小，默认值为`48 MB `。最大值为 512 GB，注意最大值指的是整个 redo log 系列文件之和，即（innodb_log_files_in_group * innodb_log_file_size）不能大于最大值 512 GB。

  ```mysql
  mysql> SHOW VARIABLES LIKE 'innodb_log_file_size';
  +----------------------+----------+
  | Variable_name        | Value    |
  +----------------------+----------+
  | innodb_log_file_size | 50331648 |
  +----------------------+----------+
  1 row in set (0.00 sec
  ```

  - 根据业务修改其大小，以便容纳较大的事务。编辑 my.cnf 文件并重启数据库生效，如下所示：

    ```bash
    [root@centos7-mysql-1 mysql]#vim /etc/my.cnf
    innodb_log_file_size=200M
    ```

>在数据库实例更新比较频繁的情况下，可以适当加大 redo log 组数和大小。但也不推荐 redo log 设置过大，在 MySQL 崩溃恢复时，会重新执行 redo log 中的记录。

##### 日志文件组

从上边的描述中可以看到，磁盘上的 redo log file 不止一个，而是以一个`日志文件组`的形式出现的。这些文件以`ib_logfile[数字]`（`数字`可以是`0、1、2…`）的形式进行命名，每个 redo log file 大小都是一样的。

在将 redo log 写入日志文件组时，是从`ib_logfile0`开始写，如果`ib_logfile0`写满了，就接着`ib_logfile1`写。同理，`ib_logf1le1`写满了就去写`ib_logfile2`，依次类准。如果写到最后一个文件该咋办？那就`重新转到 ib_logfile0 继续写`，所以整个过程如下图所示：

<img src="mysql-advanced/image-20231111095945175.png" alt="image-20231111095945175" style="zoom: 55%;" />

总共的 redo log file 大小其实就是：`innodb_log_file_size * innodb_log_files_in_group`。

采用循环使用的方式向 redo log 文件组里写数据的话，会导致后写入的 redo log 覆盖掉前边写的 redo log，基于此，InnoDB 的设计者提出了 checkpoint 的概念。

##### checkpoint

在整个日志文件组中还有两个重要的属性，分别是`write pos`、`checkpoint`。

- write pos 是当前记录的位置，一边写一边后移。
- checkpoint 是当前要擦除的位置，也是往后推移。

**每次刷盘 redo log 记录到日志文件组中，write pos 位置就会后移更新。每次 MySQL 加载日志文件组恢复数据时，会清空加载过的 redo log 记录，并把 checkpoint 后移更新。**write pos 和 checkpoint 之间的还空着的部分，可以用来写入新的 redo log 记录。

<img src="mysql-advanced/image-20231111102851434.png" alt="image-20231111102851434" style="zoom:80%;" />

如果 write pos 追上 checkpoint ，表示日志文件组满了，这时候不能再写入新的 redo log 记录，MySQL 得停下来，清空一些记录，把 checkpoint 推进一下。

<img src="mysql-advanced/image-20231111102951774.png" alt="image-20231111102951774" style="zoom:80%;" />

#### 小结

InnoDB 的更新操作，采用的是 Write Ahead Log（预先日志持久化）策略，即先写日志，再写入磁盘：

<img src="mysql-advanced/image-20231111103139939.png" alt="image-20231111103139939" style="zoom:80%;" />

### undo log

redo log 是事务持久性的保证，undo log 是事务原子性的保证。在事务中更新数据的前置操作，其实是要先写入一个 undo log。

#### 如何理解 undo log

事务需要保证原子性，也就是事务中的操作要么全部完成，要么什么也不做。但有时候事务执行到一半会出现一些情况，比如：

- 情况一：事务执行过程中可能遇到各种错误，比如`服务器本身的错误`，`操作系统错误 `，甚至是突然`断电`导致的错误。
- 情况二：程序员可以在事务执行过程中，手动输入`ROLLBACK `语句结束当前事务的执行。

以上情况出现，需要把数据改回原先的样子，这个过程称之为`回滚` ，这样就可以造成一个假象：这个事务看起来什么都没做，所以符合原子性要求。

每当我们要对一条记录做`改动`时（这里的改动可以指`INSERT`、`DELETE`、`UPDATE `），都需要 "留一手"---> **把回滚时所需的东西记下来**。比如：

- `插入一条记录`时，至少要把这条记录的`主键值`记下来，之后回滚的时候，只需要把这个主键值对应的记录`删除`就好了（对于每个 INSERT，InnoDB 存储引擎会添加一个 DELETE）。
- `删除一条记录`时，至少要把这条记录中的`内容`都记下来，之后回滚的时候，再把由这些内容组成的记录`插入`到表中就好了（对于每个 DELETE，InnoDB 存储引擎会添加一个 INSERT）。

- `修改一条记录`时，至少要把修改这条记录前的`旧值`都记录下来，之后回滚的时候，再把这条记录`更新`为旧值就好了（对于每个 UPDATE，InnoDB 存储引擎会执行一个相反的 UPDATE，将修改前的行放回去）。

MySQL 把这些为了回滚而记录的内容，称之为`撤销日志`或者`回滚日志`，即  undo log。

>说明：
>
>- 由于查询操作（`SELECT`）并不会修改任何用户记录，所以在查询操作执行时，`不需要记录`相应的 undo log。
>- `undo log 会产生 redo log`，也就是 undo log 的产生会伴随着 redo log 的产生，这是因为 undo log 也需要持久性的保护。

#### undo log 的作用

##### 回滚数据

用户对 undo log 可能有误解的认为：undo log 用于将数据库物理地恢复到执行语句或事务之前的样子。但事实并非如此，undo log 是`逻辑日志`，只是将数据库逻辑地恢复到原来的样子。所有修改都被逻辑地取消了，但是**数据结构和页本身在回滚之后可能大不相同**，比如新增的页不会逻辑的进行删除。

这是因为在多用户并发系统中，可能会有数十、数百甚至数千个并发事务，数据库的主要任务就是`协调对数据记录的并发访问`。比如，一个事务在修改当前一个页中某几条记录，同时还有别的事务在对同一个页中另几条记录进行修改。因此，不能将一个页回滚到事务开始的样子，因为这样会影响其他事务正在进行的工作。

##### MVCC

undo log 的另一个作用是`MVCC`，即在 InnoDB 存储引擎中，`MVCC 的实现是通过 undo log 来完成的`。当用户读取一行记录时，若该记录已经被其他事务占用，当前事务可以通过 undo log 读取之前的行版本信息，以此实现`非锁定读取`。

#### undo log 的存储结构

##### 回滚段与 undo 页

InnoDB 对 undo log 的管理采用段的方式，也就是 `回滚段 (rollback segment)`。每个回滚段记录了`1024 `个` undo log segment` ，而在每个 undo log segment 中进行`undo 页`的申请。

- 在 InnoDB 1.1 版本之前（不包括 1.1 版本），只有一个 rollback segment ，因此，支持同时在线的事务限制为`1024`个，实际上这对绝大多数的应用来说都已经够用。
- 从 1.1 版本开始，InnoDB 支持`最大 128 个rollback segment `，故其支持同时在线的事务限制，提高到了`128 * 1024`个。

虽然 InnoDB 1.1 版本支持了 128 个 rollback segment，但是这些 rollback segment 都存储于共享表空间`ibdata`中。从 InnoDB 1.2 版本开始，可通过参数对 rollback segment 做进一步的设置。这些参数包括：

- `innodb_undo_directory`：设置 rollback segment 文件所在的路径。这意味着，rollback segment 可以存放在共享表空间以外的位置，即可以设置为独立表空间。该参数的默认值为 "./"，表示当前 InnoDB 存储引擎的目录。
- `innodb_undo_logs`：设置 rollback segment 的个数，默认值为`128`。在 InnoDB 1.2 版本中，该参数用来替换之前版本的参数 innodb_rollback_segments。
- `innodb_undo_tablespaces`：设置构成 rollback segment 文件的数目，`默认值为 2`，这样 rollback segment 可以较为平均地分布在多个文件中。设置该参数后，会在路径 innodb_undo_directory 看到 undo 为前缀的文件，该文件就代表 rollback segment 文件。

>undo log 相关参数一般很少改动。

##### undo 页的重用

当开启一个事务需要写 undo log 的时候，就得先去 undo log segment 中找到一个空闲的位置，当有空位的时候，就去申请 undo 页，在这个申请到的 undo 页中进行 undo log 的写入。我们知道 MySQL 默认一页的大小是 16 KB。

为每一个事务分配一个页，是非常浪费的（除非你的事务非常长)，假设你的应用的 TPS（每秒处理的事务数目）为 1000，那么 1 秒就需要 1000 个页，大概需要 16 MB 的存储，1 分钟大概需要 1 GB 的存储。如果照这样下去除非 MySQL 清理的非常勤快，否则随着时间的推移，磁盘空间会增长的非常快，而且很多空间都是浪费的。

于是，undo 页就被设计的可以`重用`了，当事务提交时，`不会立刻删除 undo 页`。因为重用，所以这个 undo 页可能混杂着其他事务的 undo log。undo log 在 commit 后，会被放到一个链表中，然后判断 undo 页的使用空间是否小于 3 /4，如果`小于 3/4`的话，则表示当前的 undo 页可以被重用，那么它就不会被回收，其他事务的 undo log 可以记录在当前 undo 页的后面。由于 undo log 是`离散的`，所以清理对应的磁盘空间时，效率不高。

> 因为每一个事务分配一个页，造成极大的浪费，所以要重用 ---> 因为重用，所以当前日志的 undo 页，可能会有其他事务的 undo log  ---> 所以当前事务提交后，不能立即删除 undo 页，而是 undo log 放到链表中，尝试重用 undo 页。

##### 回滚段与事务

1. 每个事务只会使用一个回滚段，一个回滚段在同一时刻可能会服务于多个事务。

2. 当一个事务开始的时候，会指定一个回滚段，在事务进行的过程中，当数据被修改时，原始的数据会被复制到回滚段。

3. 在回滚段中，事务会不断填充盘区，直到事务结束或所有的空间被用完。如果当前的盘区不够用，事务会在段中请求扩展下一个盘区，如果所有已分配的盘区都被用完，事务会覆盖最初的盘区，或者在回滚段允许的情况下扩展新的盘区来使用。

4. 回滚段存在于 undo 表空间中，在数据库中可以存在多个 undo 表空间，但同一时刻只能使用一个 undo 表空间。

   ```mysql
   mysql> SHOW VARIABLES LIKE 'innodb_undo_tablespaces';
   +-------------------------+-------+
   | Variable_name           | Value |
   +-------------------------+-------+
   | innodb_undo_tablespaces | 2     |
   +-------------------------+-------+
   1 row in set (0.00 sec)
   ```

5. 当事务提交时，InnoDB 存储引擎会做以下两件事情：

   - 将 undo log 放入列表中，以供之后的 purge 操作。
   - 判断 undo log 所在的页是否可以重用，若可以分配给下个事务使用。

##### 回滚段中的数据分类

- `未提交的回滚数据 (uncommitted undo information)`：该数据所关联的事务并未提交，用于实现读一致性，所以该数据不能被其他事务的数据覆盖。

- `已经提交但未过期的回滚数据(committed undo information)`：该数据关联的事务已经提交，但是仍受到 "undo retention" 参数的保持时间的影响。
- `事务已经提交并过期的数据(expired undo information)`：该数据关联的事务已经提交，而且数据保存时间已经超过 "undo retention" 参数指定的时间，属于已经过期的数据。当回滚段满了之后，会优先覆盖 "事务已经提交并过期的数据"。

事务提交后并不能马上删除 undo log 及 undo log 所在的页，这是因为可能还有其他事务需要通过 undo log 来得到行记录之前的版本。因此，事务提交时将 undo log 放入一个`链表`中，是否可以最终删除 undo log 及 undo log 所在页，由`purge`线程来判断。

#### undo log 的类型

在 InnoDB 存储引擎中，undo log 分为：

- `insert undo log`
  - insert undo log 是指在`INSERT`操作中产生的 undo log。因为 INSERT 操作的记录，只对事务本身可见，对其他事务不可见（这是事务隔离性的要求），故该 undo log 可以在事务提交后直接删除，不需要进行 purge 操作。
- `update undo log`
  - update undo log 记录的是对`UPDATE`和`DELETE`操作产生的 undo log，该 undo log 可能需要提供`MVCC`机制，因此不能在事务提交时就进行删除。提交时放入 undo log 链表，等待 purge 线程进行最后的删除。

#### undo log 的生命周期

##### 简要生成过程

以下是  undo + redo 事务的简化过程。

假设有 2 个数值，分别为 A = 1 和 B = 2，然后将 A 修改为 3，B 修改为 4：

```mysql
1. start transaction;
2．记录A = 1到undo log;
3. update A = 3;
4. 记录A = 3到redo log;
5．记录B = 2到undo log;
6. update B = 4;
7. 记录B = 4到redo log;
8．将redo log刷新到磁盘;
9. commit;
```

- 在 1 ~ 8 步骤的任意一步，系统宕机，事务未提交，该事务就不会对磁盘上的数据做任何影响。
- 如果在 8 ~ 9 之间宕机，恢复之后可以选择回滚，也可以选择继续完成事务提交，因为此时 redo log 已经持久化。
- 若在 9 之后系统宕机，内存映射中变更的数据还来不及刷回磁盘，那么系统恢复之后，可以根据 redo log 把数据刷回磁盘。

只有 Buffer Pool 的流程：

<img src="mysql-advanced/image-20231111181508120.png" alt="image-20231111181508120" style="zoom:80%;" />

有了 redo log 和 undo log 之后：

<img src="mysql-advanced/image-20231111181600020.png" alt="image-20231111181600020" style="zoom:80%;" />

在更新 Buffer Pool 中的数据之前，需要先将该数据事务开始之前的状态写入 undo log 中。假设更新到一半出错了，就可以通过 undo log 来回滚到事务开始前。

##### 详细生成过程

对于 InnoDB 引擎来说，每个行记录除了记录本身的数据之外，还有几个隐藏的列：

<img src="mysql-advanced/image-20231111183414685.png" alt="image-20231111183414685" style="zoom: 67%;" />

- `DB_ROW_ID`：如果没有为表显式的定义主键，并且表中也没有定义唯一索引，那么 InnoDB 会自动为表添加一个 row_id 的`隐藏列`作为主键。
- `DB_TRX_ID`：每个事务都会分配一个事务 ID，当对某条记录发生变更时，就会将这个事务的事务 ID 写入 trx_id 中。
- `DB_ROLL_PTR`：回滚指针，本质上就是指句 undo log 的指针。

**当执行 INSERT 时：**

```mysql
BEGIN;

INSERT INTO user (name) VALUES ("tom");
```

插入的数据都会生成一条`insert undo log`，并且数据的回滚指针会指向它。undo log 会记录 undo log 的序号、插入主键的列和值。那么在进行 rollback 的时候，通过主键直接把对应的数据删除即可。

<img src="mysql-advanced/image-20231111183619474.png" alt="image-20231111183619474" style="zoom: 80%;" />

**当执行 UPDATE 时：**

对于更新的操作会产生`update undo log`，并且会分更新主键的和不更新主键的。

假设现在执行：

```mysql
UPDATE user SET name = "Sun" WHERE id = 1;
```

<img src="mysql-advanced/image-20231111183825711.png" alt="image-20231111183825711" style="zoom:80%;" />

这时会把老的记录写入新的 undo log，让回滚指针指向新的 undo log，它的 undo no 是 1，并且新的 undo log 会指向老的 undo log，它的 undo no 是 0。

假设现在执行：

```mysql
UPDATE user SET id = 2 WHERE id = 1;
```

![image-20231111184027834](mysql-advanced/image-20231111184027834.png)

对于更新主键的操作，会先把原来的数据 deletemark 标识打开，这时并没有真正的删除数据，`真正的删除会交给清理线程去判断`，然后在后面插入一条新的数据，新的数据也会产生 undo log，并且 undo log 的序号会递增。

可以发现每次对数据的变更都会产生一个 undo log，当一条记录被变更多次时，那么就会产生多条 undo log，undo log 记录的是变更前的日志，并且每个 undo log 的序号是`递增`的，那么当要回滚的时候，按照序号`依次向前推`，就可以找到原始数据。

#### undo log 是如何回滚的

以上面的例子来说，假设执行 rollback，那么对应的流程应该是这样：

1. 通过 undo no = 3 的日志，把 id = 2 的数据删除。
2. 通过 undo no = 2 的日志，把 id = 1 的数据的 deletemark 还原成 0。
3. 通过 undo no = 1 的日志，把 id = 1 的数据的 name 还原成 Tom。
4. 通过 undo no = 0 的日志，把 id = 1 的数据删除。

#### undo log 的删除

对于`insert undo log`：

- 因为 INSERT 操作的记录，只对事务本身可见，对其他事务不可见。故该 undo log 可以在事务提交后直接删除，不需要进行 purge 操作。

对于`update undo log`：

- 该 undo log 可能需要提供 MVCC 机制，因此不能在事务提交时就进行删除。提交时放入 undo log 链表，等待 purge 线程进行最后的删除。

>purge 线程两个主要作用是：`清理 undo 页和清除 page 里面带有 Delete_Bit 标识的数据行`。在 InnoDB 中，事务中的 DELETE 操作，实际上并不是真正的删除掉数据行，而是一种`Delete Mark`操作，在记录上标识`Delete_Bit`，而不删除记录。这是一种 "假删除"，只是做了个标记，**真正的删除工作需要后台 purge 线程去完成**。

#### 小结

<img src="mysql-advanced/image-20231111215246241.png" alt="image-20231111215246241" style="zoom:80%;" />

- redo log 是物理日志，记录的是数据页的物理变化。
- undo log 是逻辑日志，对事务回滚时，只是将数据库逻辑地恢复到原来的样子。
- undo log 不是 redo log 的逆过程。

## 锁

`锁`是计算机协调多个进程或线程并发访问某一资源的机制。在程序开发中会存在多线程同步的问题，当多个线程并发访问某个数据的时候，尤其是针对一些敏感的数据（比如订单、金额等)，就需要保证这个数据在任何时刻最多只有一个线程在访问，保证数据的完整性和一致性。在开发过程中加锁是为了保证数据的一致性，这个思想在数据库领域中同样很重要。

在数据库中，除传统的计算资源（如 CPU、RAM、I/O 等）的争用以外，数据也是一种供许多用户共享的资源。**为保证数据的一致性，需要对并发操作进行控制，因此产生了锁。同时，锁机制也为实现 MySQL 的各个隔离级别提供了保证。**锁冲突也是影响数据库并发访问性能的一个重要因素。所以锁对数据库而言显得尤其重要，也更加复杂。

### MySQL 并发事务访问相同记录
MySQL 并发事务访问相同记录的情况,大致可以划分为三种：

1. 读-读
2. 写-写
3. 读-写或写-读

#### 读-读
`读-读`，即**并发事务相继读取相同的记录**。读取操作本身不会对记录有任何影响，并不会引起什么问题，所以`允许`这种情况的发生。

#### 写-写
`写-写`，即**并发事务相继对相同的记录做出改动**。

在这种情况下会发生`脏写`的问题，任何一种隔离级别都不允许这种问题的发生。所以在多个未提交事务相继对一条记录做改动时，需要让它们`排队执行`，这个排队的过程其实是通过锁来实现的。这个所谓的锁其实是一个内存中的结构，在事务执行前本来是没有锁的，也就是说一开始是没有锁结构和记录进行关联的，如图所示：

<img src="mysql-advanced/image-20240707102901731.png" alt="image-20240707102901731" style="zoom: 35%;" />

当一个事务想对这条记录做改动时，首先会看看内存中有没有与这条记录关联的锁结构，当没有的时候就会在内存中生成一个锁结构与之关联。比如，事务`T1`要对这条记录做改动，就需要生成一个锁结构与之关联：

<img src="mysql-advanced/image-20240707103135785.png" alt="image-20240707103135785" style="zoom:50%;" />

在锁结构里有很多信息，为了简化理解，只把两个比较重要的属性拿了出来:

- `trx 信息`：**代表这个锁结构是哪个事务生成的。**
- `is_waiting`：**代表当前事务是否在等待。**

当事务 T1 改动了这条记录后，就生成了一个锁结构与该记录关联，因为之前没有别的事务为这条记录加锁，所以 is_waiting 属性就是 false，把这个场景就称之为获取锁成功，或者加锁成功，然后就可以继续执行操作了。

在事务 T1 提交之前，另一个事务`T2`也想对该记录做改动，那么先看看有没有锁结构与这条记录关联，发现有一个锁结构与之关联后，然后也生成了一个锁结构与这条记录关联，不过锁结构的 is_waiting 属性值为 true，表示当前事务需要等待，把这个场景就称之为获取锁失败，或者加锁失败。如图所示：

<img src="mysql-advanced/image-20240707103608183.png" alt="image-20240707103608183" style="zoom: 67%;" />

在事务 T1 提交之后，就会把该事务生成的锁结构释放掉，然后看看还有没有别的事务在等待获取锁，发现了事务 T2 还在等待获取锁，所以把事务 T2 对应的锁结构的 is_waiting 属性设置为 false，然后把该事务对应的线程唤醒，让它继续执行，此时事务 T2 就算获取到锁了。效果图就是这样：

<img src="mysql-advanced/image-20240707103751379.png" alt="image-20240707103751379" style="zoom:50%;" />

小结几种说法:

- `不加锁`：意思就是不需要在内存中生成对应的锁结构，可以直接执行操作。
- `获取锁成功，或者加锁成功`：意思就是在内存中生成了对应的锁结构，而且锁结构的 is_waiting 属性为 false，也就是事务可以继续执行操作。
- `获取锁失败，或者加锁失败，或者没有获取到锁`：意思就是在内存中生成了对应的锁结构，不过锁结构的 is_waiting 属性为 true，也就是事务需要等待，不可以继续执行操作。

#### 读-写或写-读

`读-写`或者`写-读`，即一个事务进行读取操作，另一个进行改动操作，这种情况下可能发生`脏读`、`不可重复读`、`幻读`的问题。

> 各个数据库厂商对 SQL 标准的支持都可能不一样，比如 **MySQL 在`REPEATABLE READ`隔离级别上就已经解决了幻读的问题**。

那么，怎么解决脏读、不可重复读、幻读这些问题呢？ 其实有两种可选的解决方案：

**`方案一：读操作利用多版本并发控制 MVCC，写操作进行加锁。`**

**所谓的 MVCC，就是生成一个 ReadView，通过 ReadView 找到符合条件的记录版本（历史版本由 undo 日志构建）。**查询语句只能读到在生成 ReadView 之前已提交事务所做的更改，在生成 ReadView 之前未提交的事务或者之后才开启的事务所做的更改是看不到的。而写操作肯定针对的是最新版本的记录，读记录的历史版本和改动记录的最新版本本身并不冲突，也就是采用 MVCC 时，读-写操作并不冲突。

>普通的 SELECT 语句在 READ COMMITTED 和 REPEATABLE READ 隔离级别下会使用到 MVCC 读取记录。
>
>- 在`READ COMMITTED`隔离级别下，一个事务在执行过程中，**每次执行 SELECT 操作时都会生成一个 ReadView**，ReadView 的存在本身就保证了事务不可以读取到未提交的事务所做的更改，也就是避免了脏读现象。
>- 在`REPEATABLE READ`隔离级别下，一个事务在执行过程中，**只有第一次执行 SELECT 操作才会生成一个 ReadView**，之后的 SELECT 操作都复用这个 ReadView，这样也就避免了不可重复读 和幻读的问题。

**`方案二：读、写操作都采用加锁的方式。`**

如果我们的一些业务场景不允许读取记录的旧版本，而是每次都必须去读取记录的最新版本。比如，在银行存款的事务中，你需要先把账户的余额读出来，然后将其加上本次存款的数额，最后再写到数据库中。在将账户余额读取出来后，就不想让别的事务再访问该余额，直到本次存款事务执行完成，其他事务才可以访问账户的余额。这样在读取记录的时候就需要对其进行加锁操作，这样也就意味着读操作和写操作也像写-写操作那样排队执行。

- 脏读问题的产生，是因为当前事务读取了另一个未提交事务写的一条记录，如果另一个事务在写记录的时候就给这条记录加锁，那么当前事务就无法继续读取该记录了，所以也就不会有脏读问题的产生了。
- 不可重复读问题的产生，是因为当前事务先读取一条记录，另外一个事务对该记录做了改动之后并提交之后，当前事务再次读取时会获得不同的值，如果在当前事务读取记录时就给该记录加锁，那么另一个事务就无法修改该记录，自然也不会发生不可重复读了。
- 幻读问题的产生，是因为当前事务读取了一个范围的记录，然后另外的事务向该范围内插入了新记录，当前事务再次读取该范围的记录时发现了新插入的新记录。采用加锁的方式解决幻读问题就有一些麻烦，因为当前事务在第一次读取记录时幻影记录并不存在，所以读取的时候加锁就有点尴尬（因为并不知道给谁加锁）。

小结对比发现：

- **采用 MVCC 方式的话，读-写操作彼此并不冲突，性能更高。**
- **采用加锁方式的话，读-写操作彼此需要排队执行，影响性能。**

一般情况下当然愿意采用 MVCC 来解决读-写操作并发执行的问题，但是业务在某些特殊情况下，要求必须采用加锁的方式执行。下面就讲解下 MySQL 中不同类别的锁。

### 锁的分类

<img src="mysql-advanced/1720321735370.jpg" alt="1720321735370" style="zoom:80%;" />

#### 按数据操作的类型划分：读锁、写锁

对于数据库中并发事务的读-读情况并不会引起什么问题，对于写-写、读-写或写-读这些情况可能会引起一些问题，需要使用 MVCC 或者加锁的方式来解决它们。在使用加锁的方式解决问题时，由于既要允许读-读情况不受影响，又要使写-写、读-写或写-读情况中的操作相互阻塞，所以 MySQL 实现一个由两种类型的锁组成的锁系统来解决。这两种类型的锁，通常被称为`共享锁 (Shared Lock，S Lock) 和排他锁 (Exclusive Lock，X Lock)`，也叫`读锁 (Read Lock) 和写锁 (Write Lock)`。

- **读锁：也称为共享锁、英文用 S 表示。**针对同一份数据，多个事务的读操作可以同时进行而不会互相影响，相互不阻塞的。
- **写锁：也称为排他锁、英文用 X 表示。**当前写操作没有完成前，它会阻断其他写锁和读锁。这样就能确保在给定的时间里，只有一个事务能执行写入，并防止其他用户读取正在写入的同一资源。
- **举例（行级读写锁)︰** 如果一个事务 T1 已经获得了某个行 r 的读锁，那么此时另外的一个事务 T2 是可以再去获得这个行 r 的读锁的，因为读取操作并没有改变行 r 的数据。但是，如果某个事务 T3 想获得行 r 的写锁，则它必须等待事务 T1、T2 释放掉行 r 上的读锁才行。

> 注意：对于 InnoDB 引擎来说，读锁和写锁即可以加在表上，也可以加在行上。

`锁的兼容情况`（这里的兼容是指对同一张表或同一个记录的锁的兼容性情况）：

| 兼容情况 | X 锁   | S 锁   |
| -------- | ------ | ------ |
| X 锁     | 不兼容 | 不兼容 |
| S 锁     | 不兼容 | 兼容   |

##### 锁定读

在采用加锁方式解决脏读、不可重复读、幻读这些问题时，读取一条记录时需要获取该记录的 S 锁，其实是不严谨的，有时候需要在读取记录时就获取记录的 X 锁 ，来禁止别的事务读写该记录，为此 MySQL 提出了两种比较特殊的 SELECT 语句格式：

1. 对读取的记录加 S 锁∶

   ```mysql
   SELECT ... LOCK IN SHARE MODE
   # 或
   SELECT ... FOR SHARE # 8.0新增语法
   ```

   - 在普通的 SELECT 语句后边加 LOCK IN SHARE MODE，如果当前事务执行了该语句，那么它会为读取到的记录加 S 锁，这样允许别的事务继续获取这些记录的 S 锁（比方说别的事务也使用 SELECT … LOCK IN SHARE MODE 语句来读取这些记录），但是不能获取这些记录的 X 锁（比如使用 SELECT … FOR UPDATE 语句来读取这些记录，或者直接修改这些记录）。**如果别的事务想要获取这些记录的 X 锁，那么它们会阻塞，直到当前事务提交之后将这些记录上的 S 锁释放掉。**

2. 对读取的记录加 X 锁：

   ```mysql
   SELECT ... FOR UPDATE
   ```

   - 在普通的 SELECT 语句后边加 FOR UPDATE，如果当前事务执行了该语句，那么它会为读取到的记录加 X 锁，这样既不允许别的事务获取这些记录的 S 锁（比方说别的事务使用 SELECT … LOCK IN SHARE MODE 语句来读取这些记录），也不允许获取这些记录的 X 锁（比如使用 SELECT … FOR UPDATE 语句来读取这些记录，或者直接修改这些记录)。**如果别的事务想要获取这些记录的 S 锁或者 X 锁，那么它们会阻塞，直到当前事务提交之后将这些记录上的 X 锁释放掉。**

**案例演示：**

- S ---> S：事务 A 先获取 account 表的 S 锁，此时，事务 B 也可以正常获得 account 表的 S 锁，并读取记录。

  ![image-20240707112619440](mysql-advanced/image-20240707112619440.png)

- S ---> X：事务 A 先获取 account 表的 S 锁，此时，事务 B 无法获取 account 表的 X 锁，被阻塞，直到事务 A 提交，事务 B 才可以正常获取 account 表的 X 锁。

  ![image-20240707112713118](mysql-advanced/image-20240707112713118.png)

- X ---> S：事务 A 先获取 account 表的 X 锁，此时，事务 B 无法获取 account 表的 S 锁，被阻塞，直到事务 A 提交，事务 B 才可以正常获取 account 表的 S 锁。

  ![image-20240707113011011](mysql-advanced/image-20240707113011011.png)

- X ---> X：事务 A 先获取 account 表的 X 锁，此时，事务 B 无法获取 account 表的 X 锁，被阻塞，直到事务 A 提交，事务 B 才可以正常获取 account 表的 X 锁。

  ![image-20240707113033633](mysql-advanced/image-20240707113033633.png)

>在 MySQL 5.7 及之前的版本，SELECT … FOR UPDATE，如果获取不到锁，会一直等待，直到 innodb_lock_wait_timeout 超时。在 MySQL 8.0 版本中，在 SELECT … FOR UPDATE，SELECT … FOR SHARE 后添加`NOWAIT`、`SKIP LOCKED`语法，可以跳过锁等待，或者跳过锁定。
>
>如果查询的行已经加锁：
>
>- **那么 NOWAIT 会立即报错返回。**
>- **而 SKIP LOCKED 也会立即返回，只是返回的结果中不包含被锁定的行。**
>
>演示：
>
>```mysql
># 事务A，开启事务，先获取X锁
>mysql> begin ;
>mysql> select * from account for update;
>+-- --+--------+- ------ +
>| id  |    NAME|  balance|
>|   1 │     张三|    40.00|
>|   2 |     李四|    0.00 |
>|   3 |     王五|   100.00|
>+--- -+--------+- ------ +
>
># 事务B
>mysql> begin ;
>mysql> select * from account for update nowait;
># 报错返回
>ERROR 3572 (HYO00): Statement aborted because lock(s) could not be acquired immediately and NOMAIT is set.
>
>mysql> select * from account for update skip locked;
># 因为事务A获得X锁，所以查到的记录是空的
>Empty set (o.00 sec)
>
>mysql> commit;
>Query Ok,o rows affected (0.00 sec)
>```

##### 写操作

平常所用到的写操作无非是 DELETE、UPDATE、INSERT 这三种：

- `DELETE`：对一条记录做 DELETE 操作的过程，其实是先在 B+ 树中定位到这条记录的位置，然后获取这条记录的 X 锁，再执行 delete mark 操作。也可以把这个定位待删除记录在 B+ 树中位置的过程，看成是一个获取 X 锁的锁定读。
- `UPDATE`：在对一条记录做 UPDATE 操作时，分为三种情况。
  - 情况 1：**未修改该记录的键值（例如主键），并且被更新的列占用的存储空间在修改前后未发生变化。**则先在 B+ 树中定位到这条记录的位置，然后再获取一下记录的 X 锁，最后在原记录的位置进行修改操作。也可以把这个定位待修改记录在 B+ 树中位置的过程，看成是一个获取 X 锁的锁定读。
  - 情况 2：**未修改该记录的键值，并且至少有一个被更新的列占用的存储空间在修改前后发生变化。**则先在 B+ 树中定位到这条记录的位置，然后再获取一下记录的 X 锁，将该记录彻底删除掉（就是把记录彻底移入垃圾链表)，最后再插入一条新记录。这个定位待修改记录在 B+ 树中位置的过程，看成是一个获取 X 锁的锁定读，新插入的记录由 INSERT 操作提供的隐式锁进行保护。
  - 情况 3：**修改了该记录的键值。**则相当于在原记录上做 DELETE 操作之后再来一次 INSERT 操作，加锁操作就需要按照 DELETE 和 INSERT 的规则进行。（同情况 2）
- `INSERT`：一般情况下，新插入一条记录的操作并不加锁，通过一种称之为`隐式锁`的结构，来保护这条新插入的记录在本事务提交前不被别的事务访问。（因为插入之前就没有要锁的记录，所以也就不需要加 X 锁了）

#### 按数据操作的粒度划分：表级锁、页级锁、行锁

为了尽可能提高数据库的并发度，每次锁定的数据范围越小越好，理论上每次只锁定当前操作的数据的方案会得到最大的并发度，但是管理锁是很耗资源的事情（涉及获取、检查、释放锁等动作）。因此数据库系统需要在`高并发响应`和`系统性能`两方面进行平衡，这样就产生了`锁粒度 (Lock granularity)`的概念。

**对一条记录加锁影响的也只是这条记录而已，我们就说这个锁的粒度比较细；其实一个事务也可以在表级别进行加锁，自然就被称之为表级锁或者表锁，对一个表加锁影响整个表中的记录，我们就说这个锁的粒度比较粗。**锁的粒度主要分为`表级锁`、`页级锁`和`行锁`。

##### 表锁（Table Lock）

**`表锁`会锁定整张表，它是 MySQL 中最基本的锁策略，并不依赖于存储引擎（不管是 MySQL 的什么存储引擎，对于表锁的策略都是一样的），并且表锁是开销最小的策略（因为粒度比较大）。由于表级锁一次会将整个表锁定，所以可以很好的避免死锁问题。当然，锁的粒度大所带来最大的负面影响就是出现锁资源争用的概率也会最高，导致并发率大打折扣。**

###### 表级别的 S 锁和 X 锁

在对某个表执行 SELECT、INSERT、DELETE、UPDATE 语句时，InnoDB 存储引擎是不会为这个表添加表级别的 S 锁或者 X 锁的。在对某个表执行一些诸如 ALTER TABLE、DROP TABLE 这类的 DDL 语句时，其他事务对这个表并发执行诸如 SELECT、INSERT、DELETE、UPDATE 的语句会发生阻塞。同理，某个事务中对某个表执行 SELECT、INSERT、DELETE、UPDATE 语句时，在其他会话中对这个表执行 DDL 语句也会发生阻塞。这个过程其实是通过在 Server 层使用一种称之为`元数据锁 (Metadata Locks，简称 MDL)`的结构来实现的。

**一般情况下，不会使用 InnoDB 存储引擎提供的表级别的 S 锁和 X 锁（因为 InnoDB 支持更小粒度的行锁），只会在一些特殊情况下，比方说崩溃恢复过程中用到。**比如，在系统变量 autocommit=0，innodb_table_locks = 1 时，`手动`获取 InnoDB 存储引擎提供的表 t 的 S 锁或者 X 锁，可以这么写：

- `LOCK TABLES t READ`：InnoDB 存储引擎会对表 t 加表级别的 S 锁 。
- `LOCK TABLES t WRITE`：InnoDB 存储引擎会对表 t 加表级别的 X 锁 。

> 尽量避免在使用 InnoDB 存储引擎的表上使用 LOCK TABLES 这样的手动锁表语句，它们并不会提供什么额外的保护，只是会降低并发能力而已。InnoDB 的厉害之处还是实现了更细粒度的`行锁`，关于 InnoDB 表级别的 S 锁和 X 锁，大家了解一下就可以了。

**举例：下面我们讲解 MyISAM 引擎下的表锁。**

- 步骤 1：创建表并添加数据。

  ```mysql
  CREATE TABLE mylock(
  id INT NOT NULL PRIMARY KEY auto_increment,NAME VARCHAR(20)
  )ENGINE myisam; # 存储引擎也可以使用InnoDB，只是不建议
  
  # 插入一条数据
  INSERT INTO mylock(NAME) VALUES('a');
  
  # 查询表中所有的数据
  SELECT * FROM mylock;
  +----+------+
  | id | NAME |
  +----+------+
  |  1 | a    |
  +----+------+
  ```

- 步骤 2：查看表上加过的锁。

  ```mysql
  SHOW OPEN TABLES WHERE In_use > 0;
  # 或者
  SHOW OPEN TABLES; # 主要关注In_use字段的值>0
  
  SHOW OPEN TABLES; # 以下为部分输出，没有In_use>0的记录，表明当前数据库中没有被锁定的表。
  +--------------------+---------------------------+--------+-------------+
  | Database           | Table                     | In_use | Name_locked |
  +--------------------+---------------------------+--------+-------------+
  | atguigudb3         | user1                     |      0 |           0 |
  | mysql              | tablespace_files          |      0 |           0 |
  | mysql              | column_statistics         |      0 |           0 |
  | atguigudb3         | account                   |      0 |           0 |
  | mysql              | table_stats               |      0 |           0 |
  | mysql              | check_constraints         |      0 |           0 |
  | mysql              | view_table_usage          |      0 |           0 |
  | mysql              | tables_priv               |      0 |           0 |
  | mysql              | column_type_elements      |      0 |           0 |
  | mysql              | foreign_key_column_usage  |      0 |           0 |
  | mysql              | time_zone_name            |      0 |           0 |
  .........................................................................
  | information_schema | TABLES                    |      0 |           0 |
  | mysql              | time_zone_transition_type |      0 |           0 |
  | mysql              | tablespaces               |      0 |           0 |
  +--------------------+---------------------------+--------+-------------+
  61 rows in set (0.01 sec)
  ```

- 步骤 3：手动增加表锁命令。

  ```mysql
  LOCK TABLES t READ: # 存储引擎会对表t加表级别的共享锁。共享锁也叫读锁或S锁，Share的缩写
  LOCK TABLES t WRITE; # 存储引擎会对表t加表级别的排他锁。排它锁也叫独占锁、写锁或X锁，是eXclusive的缩写
  ```

  ```mysql
  # 示例
  mysql> LOCK TABLES mylock READ;
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> SHOW OPEN TABLES WHERE In_use > 0;
  +----------------+--------+--------+-------------+
  | Database       | Table  | In_use | Name_locked |
  +----------------+--------+--------+-------------+
  | platform_basic | mylock |      1 |           0 |
  +----------------+--------+--------+-------------+
  1 row in set (0.00 sec)
  ```

- 步骤 4：释放锁。

  ```mysql
  # 释放锁
  UNLOCK TABLES; # 释放当前加锁的表
  ```

- 步骤 5：加读锁。为 mylock 表加 READ 锁（读阻塞写），观察阻塞的情况，流程如下：

  ![image-20240707150709224](mysql-advanced/image-20240707150709224.png)

  ```mysql
  ########################SessonA中########################################
  mysql> begin;
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> lock tables mylock read; # 事务A为表加上读锁
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> select * from mylock; # 事务A可读
  +----+------+
  | id | NAME |
  +----+------+
  |  1 | a    |
  +----+------+
  1 row in set (0.00 sec)
  
  mysql> update mylock set name = 'a1' where id = 1; # 事务A不可写
  ERROR 1099 (HY000): Table 'mylock' was locked with a READ lock and can't be updated
  
  mysql> select * from account; # 事务A不可操作其他表
  ERROR 1100 (HY000): Table 'account' was not locked with LOCK TABLES
  
  ##################################sessionB###############################
  mysql> select * from mylock; # 事务B可以读
  +----+------+
  | id | NAME |
  +----+------+
  |  1 | a    |
  +----+------+
  1 row in set (0.00 sec)
  
  mysql> update mylock set name = 'a2' where id = 1; # 事务B不可写，需要等待
  # 阻塞等待事务A释放锁....
  
  ########################SessionA##########################
  mysql> unlock tables; # 事务A释放锁
  Query OK, 0 rows affected (0.00 sec)
  
  ########################SessionB#########################
  mysql> update mylock set name = 'a2' where id = 1; # 事务B获取到锁，进行写操作
  Query OK, 1 row affected (13.41 sec)
  Rows matched: 1  Changed: 1  Warnings: 0
  
  mysql> select * from mylock; # 事务B提交后，数据发生变更
  +----+------+
  | id | NAME |
  +----+------+
  |  1 | a2   |
  +----+------+
  1 row in set (0.00 sec)
  ```

- 步骤 6∶加写锁。为 mylock 表加 WRITE 锁，观察阻塞的情况，流程如下：

  ![image-20240707154231079](mysql-advanced/image-20240707154231079.png)

  ```mysql
  ########################SessionA########################################
  mysql> lock tables mylock write; # 事务A为表上加写锁
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> select * from mylock; # 事务A可以读
  +----+------+
  | id | NAME |
  +----+------+
  |  1 | a1   |
  +----+------+
  1 row in set (0.00 sec)
  
  mysql> update mylock set name = 'a2' where id = 1; # 事务A可以写
  Query OK, 1 row affected (0.01 sec)
  Rows matched: 1  Changed: 1  Warnings: 0
  
  mysql> select * from account; # 事务A无法操作其他表
  ERROR 1100 (HY000): Table 'account' was not locked with LOCK TABLES
  
  ############################SessionB##################################
  mysql> select * from mylock; # 事务B不可以读
  # 阻塞等待事务A释放锁....
  
  mysql> update mylock set name = 'a3' where id = 1; # 事务B不可以写
  # 阻塞等待事务A释放锁....
  
  mysql> select * from account; # 事务B可以操作其他表
  +----+--------+---------+
  | id | name   | balance |
  +----+--------+---------+
  |  1 | abc    |      40 |
  |  2 | 李四   |       0 |
  |  3 | 王五   |     100 |
  |  4 | 马六   |    1000 |
  |  5 | 张三   |    6666 |
  +----+--------+---------+
  5 rows in set (0.01 sec)
  ```

总结：**MyISAM 在执行查询语句（SELECT）前，会给涉及的所有表加读锁，在执行增删改操作前，会给涉及的表加写锁。InnoDB 存储引擎是不会为这个表添加表级别的读锁或者写锁的。**

MySQL 的表级锁有两种模式：（以 MyISAM 表进行操作的演示）

- **表共享读锁（Table Read Lock）**

- **表独占写锁（Table Write Lock）**

- 二者的关系：

  | 锁类型 | 自己可读 | 自己可写 | 自己可操作其他表 | 他人可读   | 他人可写   |
  | ------ | -------- | -------- | ---------------- | ---------- | ---------- |
  | 读锁   | 是       | 否       | 否               | 是         | 否，需等待 |
  | 写锁   | 是       | 是       | 否               | 否，需等待 | 否，需等待 |

###### 意向锁（intention lock)

InnoDB 支持`多粒度锁 (multiple granularity locking)`，它允许行级锁与表级锁共存，而`意向锁`就是其中的一种表锁。

- 意向锁的存在是为了协调行锁和表锁的关系，支持多粒度（表锁与行锁）的锁并存。
- 意向锁是一种不与行级锁冲突的表级锁，这一点非常重要。
- 意向锁表明 "某个事务正在某些行持有了锁或该事务准备去持有锁"。

**意向锁分为两种：**

- `意向共享锁 (intention shared lock, IS)`：事务有意向对表中的某些行加共享锁（S 锁）。

  ```mysql
  # 事务要获取某些行的S锁，必须先获得表的IS锁
  SELECT column FROM table ... LOCK IN SHARE MODE;
  ```

- `意向排他锁 (intention exclusive lock, IX)`：事务有意向对表中的某些行加排他锁（X 锁）。

  ```mysql
  # 事务要获取某些行的X锁，必须先获得表的IX锁
  SELECT column FROM table ... FOR UPDATE;
  ```

> 意向锁是由存储引擎自己维护的，用户无法手动操作意向锁，在为数据行加共享锁/排他锁之前，InooDB 会先获取该数据行所在数据表的对应意向锁。

**意向锁要解决的问题：**

现在有两个事务，分别是 T1 和 T2，其中 T2 试图在该表级别上应用共享或排它锁，如果没有意向锁存在，那么 T2 就需要去检查各个页或行是否存在锁；如果存在意向锁，那么此时就会受到由 T1 控制的表级别意向锁的阻塞。T2 在锁定该表前不必检查各个页或行锁，而只需检查表上的意向锁。**简单来说，意向锁就是给更大一级别的空间示意里面是否已经上过锁。**

在数据表的场景中，如果我们给某一行数据加上了排它锁，数据库会自动给更大一级的空间，比如数据页或数据表加上意向锁，告诉其他人这个数据页或数据表已经有人上过排它锁了，这样当其他人想要获取数据表排它锁的时候，只需要了解是否有人已经获取了这个数据表的意向排他锁即可。

- **如果事务想要获得数据表中某些记录的共享锁，就需要在数据表上添加意向共享锁。**
- **如果事务想要获得数据表中某些记录的排他锁，就需要在数据表上添加意向排他锁。**

这时，意向锁会告诉其他事务已经有人锁定了表中的某些记录。

举例：事务的隔离级别默认为 REPEATABLE-READ，如下所示。

```mysql
# 创建表teacher，插入6条数据
CREATE TABLE `teacher`(
  id int NOT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `teacher` VALUES
(1, 'zhangsan'),
(2 , 'lisi'),
(3, 'wangwu'),
(4, 'zhaoliu'),
(5, 'songhongkang'),
(6 , 'leifengyang');

# 查看数据
mysql> select * from teacher;
+----+--------------+
| id | name         |
+----+--------------+
|  1 | zhangsan     |
|  2 | lisi         |
|  3 | wangwu       |
|  4 | zhaoliu      |
|  5 | songhongkang |
|  6 | leifengyang  |
+----+--------------+
6 rows in set (0.00 sec)

# 假设事务A获取了某一行的排他锁，并未提交
begin;
SELECT * FROM teacher WHERE id = 6 FOR UPDATE;

# 事务B想要获取teacher表的表读锁，语句如下
begin ;
LOCK TABLES teacher READ;
# 阻塞...
```

因为共享锁与排他锁互斥，所以事务 B 在试图对 teacher 表加共享锁的时候，必须保证两个条件：

1. 当前没有其他事务持有 teacher 表的排他锁。
2. 当前没有其他事务持有 teacher 表中任意一行的排他锁。

为了检测是否满足第二个条件，事务 B 必须在确保 teacher 表不存在任何排他锁的前提下，去检测表中的每一行是否存在排他锁。很明显这是一个效率很差的做法，但是有了意向锁之后，情况就不一样了。

意向锁是怎么解决这个问题的呢？首先需要知道意向锁之间的兼容互斥性，如下所示：

| 兼容性           | 意向共享锁（IS） | 意向排他锁（IX） |
| ---------------- | ---------------- | ---------------- |
| 意向共享锁（IS） | 兼容             | 兼容             |
| 意向排他锁（IX） | 兼容             | 兼容             |

即**`意向锁之间是互相兼容`**的，虽然意向锁之间互相兼容，但是**`意向锁会与普通的表级的排他锁/共享锁互斥`**，如下所示：

| 兼容性          | 意向共享锁（IS） | 意向排他锁（IX） |
| --------------- | ---------------- | ---------------- |
| 表级共享锁（S） | 兼容             | 互斥             |
| 表级排他锁（X） | 互斥             | 互斥             |

> 注意：这里的排他锁/共享锁指的都是表锁，意向锁不会与行级的共享锁/排他锁互斥，**可以把意向锁看做是一种行级锁的标记**。

回到刚才 teacher 表的例子，事务 A 获取了某一行的排他锁，并未提交：

```mysql
BEGIN;
SELECT * FROM teacher WHERE id = 6 FOR UPDATE;
```

此时，teacher 表存在两把锁：teacher 表上的意向排他锁与 id 为 6 的数据行上的排他锁。然后，事务 B 想要获取 teacher 表的共享锁：

```mysql
BEGIN;
LOCK TABLES teacher READ;
```

**这个时候，事务 B 检测事务 A 持有 teacher 表的意向排他锁，就可以得知事务 A 必然持有该表中某些数据行的排他锁，那么事务 B 对 teacher 表的加锁请求就会被排斥（事务 B 阻塞），而无需去检测表中的每一行数据是否存在排他锁。**

> 总结来看，意向锁可以看作是一种标记，通过意向锁，可以在一个事务想要对表进行加锁请求时，快速的判断是否会被阻塞，提高效率。

**意向锁的并发性：**

**`意向锁不会与行级的共享锁/排他锁互斥`**！正因为如此，意向锁并不会影响到多个事务对不同数据行加排他锁时的并发性。（如果互斥，那么行级锁直接就退化成表锁了，就没有什么优势了）

我们扩展一下上面 teacher 表的例子，来概括一下意向锁的作用（一条数据从被锁定到被释放的过程中，可能存在多种不同锁，但是这里只着重表现意向锁）

1. 事务 A 先获取了某一行的排他锁，并未提交：

   ```mysql
   BEGIN;
   SELECT * FROM teacher WHERE id = 6 FOR UPDATE;
   ```

2. 此时，事务 A 获取了 teacher 表上的意向排他锁，事务 A 获取了 id 为 6 的数据行上的排他锁。之后，事务 B 想要获取 teacher 表的共享锁：

   ```mysql
   BEGIN;
   LOCK TABLES teacher READ;
   ```

3. 事务 B 检测到事务 A 持有 teacher 表的意向排他锁，则事务 B 对 teacher 表的加锁请求被排斥（事务 B 阻塞）。如果事务 C 想获取 teacher 表中某一行的排他锁：

   ```mysql
   BEGIN;
   SELECT * FROM teacher WHERE id = 5 FOR UPDATE;
   ```

4. 那么，事务 C 先申请 teacher 表的意向排他锁，事务 C 检测到事务 A 持有 teacher 表的意向排他锁，因为意向锁之间并不互斥，所以事务 C 获取到了 teacher 表的意向排他锁。因为 id 为 5 的数据行上不存在任何排他锁，最终，事务 C 成功获取到了该数据行上的排他锁。

从上面的案例可以得到如下结论：

- InnoDB 支持多粒度锁，特定场景下，行级锁可以与表级锁共存。
- 意向锁之间互不排斥，但除了 IS 与 S 兼容外，意向锁会与共享锁 / 排他锁互斥。
- **IX，IS 是表级锁，不会和行级的 X，S 锁发生冲突，只会和表级的 X，S 发生冲突。**
- 意向锁在保证并发性的前提下，实现了行锁和表锁共存，且满足事务隔离性的要求。

###### 自增锁（AUTO-INC 锁）

在使用 MySQL 过程中，我们可以为表的某个列添加`AUTO_INCREMENT`属性。举例：

```mysql
CREATE TABLE `teacher` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

由于这个表的 id 字段声明了 AUTO_INCREMENT，意味着在书写插入语句时不需要为其赋值，SQL 语句修改如下所示：

```mysql
INSERT INTO `teacher` (name) VALUES ('zhangsan'), ('lisi');
```

上边的插入语句并没有为 id 列显式赋值，所以系统会自动为它赋上递增的值，结果如下所示：

```mysql
SELECT * FROM teacher;
+----+----------+
| id | name |
+----+----------+
| 1 | zhangsan |
| 2 | lisi |
+----+----------+
```

现在看到的上面插入数据的方式，是一种简单的插入模式。**所有插入数据的方式总共分为三类，分别是 "Simple inserts"，"Bulk inserts" 和 "Mixed-mode inserts"。**

1. `Simple inserts (简单插入)`：**可以预先确定要插入的行数（当语句被初始处理时）的语句。**包括没有嵌套子查询的单行和多行 INSERT … VALUES() 和 REPLACE 语句。比如我们上面举的例子就属于该类插入，已经确定要插入的行数。

2. `Bulk inserts (批量插入)`：**事先不知道要插入的行数（和所需自动递增值的数量）的语句。**比如 INSERT … SELECT，REPLACE … SELECT 和 LOAD DATA 语句，但不包括纯 INSERT。InnoDB 在每处理一行，为 AUTO_INCREMENT 列分配一个新值。

3. `Mixed-mode inserts (混合模式插入)`：**其中一种是指那些是 Simple inserts 语句，但是指定部分新行的自动递增值。**例如 INSERT INTO teacher (id,name) VALUES (1,‘a’), (NULL,‘b’), (5,‘c’), (NULL,‘d’)，只是指定了部分 id 的值，其他部分 id 的值还是自增。另一种类型的混合模式插入是 INSERT … ON DUPLICATE KEY UPDATE。

对于上面数据插入的案例，MySQL 中采用了`自增锁`的方式来实现，**AUTO-INC 锁是当向使用含有 AUTO_INCREMENT 列的表中插入数据时需要获取的一种特殊的表级锁，在执行插入语句时就在表级别加一个 AUTO-INC 锁，然后为每条待插入记录的 AUTO_INCREMENT 修饰的列分配递增的值，在该语句执行结束后，再把 AUTO-INC 锁释放掉。**一个事务在持有 AUTO-INC 锁的过程中，其他事务的插入语句都要被阻塞，这可以保证一个语句中分配的递增值是连续的。也正因为此，其并发性显然并不高，当我们向一个有 AUTO_INCREMENT 关键字的主键插入值的时候，每条语句都要对这个表锁进行竞争，这样的并发潜力其实是很低下的，所以 InnoDB 通过`innodb_autoinc_lock_mode`的不同取值来提供不同的锁定机制，来显著提高 SQL 语句的可伸缩性和性能。

>innodb_autoinc_lock_mode 有三种取值，分别对应与不同锁定模式：
>
>- `innodb_autoinc_lock_mode = 0 ("传统"锁定模式)`：在此锁定模式下，所有类型的 INSERT 语句都会获得一个特殊的表级 AUTO-INC 锁，用于插入具有 AUTO_INCREMENT 列的表。这种模式其实就如我们上面的例子，即每当执行 INSERT 的时候，都会得到一个表级锁（AUTO-INC 锁），使得语句中生成的 auto_increment 为顺序，且在 binlog 中重放的时候，可以保证 master 与 slave 中数据的 auto_increment 是相同的。因为是表级锁，当在同一时间多个事务中执行 INSERT 的时候，对于 AUTO-INC 锁的争夺会`限制并发`能力。
>- `innodb_autoinc_lock_mode = 1("连续"锁定模式)`：在 MySQL 8.0 之前，连续锁定模式是默认的。在这个模式下，"Bulk inserts" 仍然使用 AUTO-INC 表级锁，并保持到语句结束。这适用于所有 INSERT … SELECT，REPLACE … SELECT 和 LOAD DATA 语句。同一时刻只有一个语句可以持有 AUTO-INC 锁。对于 "Simple inserts"（要插入的行数事先已知），则通过在 mutex（轻量锁）的控制下获得所需数量的自动递增值来避免表级 AUTO-INC 锁，它只在分配过程的持续时间内保持，而不是直到语句完成。"Simple inserts" 不使用表级 AUTO-INC 锁，除非 AUTO-INC 锁由另一个事务保持。如果另一个事务保持 AUTO-INC 锁，则 "Simple inserts" 等待 AUTO-INC 锁，如同它是一个 "Bulk inserts"。
>- `innodb_autoinc_lock_mode = 2("交错"锁定模式)`：从 MySQL 8.0 开始，交错锁定模式是`默认`的。在这种锁定模式下，所有 INSERT 语句都不会使用表级 AUTO-INC 锁，并且可以同时执行多个语句。这是最快和最可扩展的锁定模式，但是当使用基于语句的复制或恢复方案时，从二进制日志重播 SQL 语句时，这是不安全的。在此锁定模式下，自动递增值保证在所有并发执行的所有类型的 INSERT 语句中是唯一且单调递增的。但是，由于多个语句可以同时生成数字（即，跨语句交叉编号），为任何给定语句插入的行生成的值可能不是连续的。如果执行的语句是 "Simple inserts"，其中要插入的行数已提前知道，除了 "Mixed-mode inserts" 之外，为单个语句生成的数字不会有间隙。然而，当执行 "Bulk inserts" 时，在由任何给定语句分配的自动递增值中可能存在间隙。

###### 元数据锁（MDL 锁）

MySQL 5.5 引入了`Meta Data Lock`，简称 MDL 锁，属于表锁范畴。**MDL 的作用是，保证读写的正确性。**比如，如果一个查询正在遍历一个表中的数据，而执行期间另一个线程对这个表结构做变更，增加了一列，那么查询线程拿到的结果跟表结构对不上，肯定是不行的。

因此，**当对一个表做增删改查操作（DML 操作）的时候，加 MDL 读锁；当要对表做结构变更操作（DDL 操作）的时候，加 MDL 写锁。**

- **读锁之间不互斥，因此可以有多个线程同时对一张表增删改查。**
- **读锁与写锁之间、写锁之间是互斥的，用来保证变更表结构操作的安全性，解决了 DML 和 DDL 操作之间的一致性问题。**
- **MDL 不需要显式使用，在访问一个表的时候会被自动加上。**

举例：元数据锁的使用场景模拟。

- 事务 A：从表中查询数据。

  <img src="mysql-advanced/image-20240707171805808.png" alt="image-20240707171805808" style="zoom:80%;" />

- 事务 B：修改表结构，增加新列。

  <img src="mysql-advanced/image-20240707171834375.png" alt="image-20240707171834375" style="zoom:80%;" />

- 事务 C：查看当前 MySQL 的进程，可以得出 B 中的阻塞就是因为 A 为 teacher 加了 MDL 锁。

  ![image-20240707171948825](mysql-advanced/image-20240707171948825.png)

- 在事务 B 中结束修改，重新进行读操作。

  <img src="mysql-advanced/image-20240707172106997.png" alt="image-20240707172106997" style="zoom:80%;" />

- 事务 B 中之前的所有进行提交，重新开启事务尽心修改，同时 C 中也开启一个事务进行查询。可以看出，事务 B 被阻塞，这是因为事务 A 拿到了 teacher 表的元数据读锁，事务 B 想申请 teacher 表的元数据写锁，由于读写锁互斥，事务 B 需要等待事务 A 释放元数据锁才能执行。而事务 C 要在表 teacher 上新申请 MDL 读锁的请求也会被事务 B 阻塞。如前面所说，所有对表的增删改查操作都需要先申请 MDL 读锁，现在就都会被阻塞了，也就等于这个表现在完全不可读写了，并发性大大降低！！！这也就是元数据锁可能带来的问题。

  ![image-20240707172329486](mysql-advanced/image-20240707172329486.png)

##### InnoDB 中的行锁（Row Lock）

**`行锁`也称为记录锁，顾名思义，就是锁住某一行（某条记录 Row)。需要的注意的是，MySQL 服务器层并没有实现行锁机制，行级锁只在存储引擎层实现。**

- 优点：**锁定力度小，发生锁冲突概率低，可以实现的并发度高。**
- 缺点：**对于锁的开销比较大，加锁会比较慢，容易出现死锁情况。**

InnoDB 与 MyISAM 的最大不同有两点：一是支持事务（TRANSACTION）；二是采用了行级锁。

演示环境搭建：

```mysql
# 创建表
CREATE TABLE student (
  id INT,
  name VARCHAR(20),
  class varchar (10) ,PRIMARY KEY (id)
)Engine=InnoDB CHARSET=utf8;

# 插入几条记录
INSERT INTO student VALUES
(1, '张三', '一班'),
(3, '李四', '一班'),
(8, '王五', '二班'),
(15, '赵六', '二班'),
(20, '钱七', '三班');

# 查看
SELECT *FROM student;
+----+--------+--------+
| id | name   | class  |
+----+--------+--------+
|  1 | 张三   | 一班   |
|  3 | 李四   | 一班   |
|  8 | 王五   | 二班   |
| 15 | 赵六   | 二班   |
| 20 | 钱七   | 三班   |
+----+--------+--------+
```

student 表中的聚簇索引的简图如下所示：

<img src="mysql-advanced/image-20240707195851775.png" alt="image-20240707195851775" style="zoom: 60%;" />

这里把 B+ 树的索引结构做了一个超级简化，只把索引中的记录给拿了出来，下面看看都有哪些常用的行锁类型。

###### 记录锁（Record Locks）

`记录锁`也就是仅仅把一条记录锁上，官方的类型名称为`LOCK_REC_NOT_GAP`。比如把 id 值为 8 的那条记录加一个记录锁的示意图如图所示，仅仅是锁住了 id 值为 8 的记录，对周围的数据没有影响：

<img src="mysql-advanced/image-20240707200151734.png" alt="image-20240707200151734" style="zoom:60%;" />

**举例如下：**

<img src="mysql-advanced/image-20240707200245984.png" alt="image-20240707200245984" style="zoom: 80%;" />

**代码演示：**

```mysql
###############################SessionA###################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> update student set name = '张三1' where id = 1; # 为id=1的记录加X型的行锁
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

###############################SessionB###################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student where id = 2 lock in share mode;
Empty set (0.00 sec)

mysql> select * from student where id = 3 lock in share mode;
+----+--------+--------+
| id | name   | class  |
+----+--------+--------+
|  3 | 李四   | 一班   |
+----+--------+--------+
1 row in set (0.00 sec)

mysql> select * from student where id = 1 lock in share mode; 
# 阻塞...，因为sessonA中的事务对该记录了X锁
ERROR 1205 (HY000): Lock wait timeout exceeded; # 执行超时

mysql> update student set name = '李四1' where id = 3; # 为id=3的记录加X型的锁
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> update student set name = '张三2' where id = 1;
# 阻塞...
ERROR 1205 (HY000): Lock wait timeout exceeded; # 执行超时

###############################SessionA###################################
mysql> commit; # 提交
Query OK, 0 rows affected (0.01 sec)

###############################SessionB###################################
mysql> update student set name = '张三2' where id = 1; # 再次尝试获取X锁，执行成功
Query OK, 1 row affected (5.74 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from student;
+----+---------+--------+
| id | name    | class  |
+----+---------+--------+
|  1 | 张三2   | 一班   |
|  3 | 李四1   | 一班   |
|  8 | 王五    | 二班   |
| 15 | 赵六    | 二班   |
| 20 | 钱七    | 三班   |
+----+---------+--------+
5 rows in set (0.00 sec)
```

记录锁是有 S 锁和 X 锁之分的，称之为`S 型记录锁`和`X 型记录锁`。

- 当一个事务获取了一条记录的 S 型记录锁后，其他事务也可以继续获取该记录的 S 型记录锁，但不可以继续获取 X 型记录锁。
- 当一个事务获取了一条记录的 X 型记录锁后，其他事务既不可以继续获取该记录的 S 型记录锁，也不可以继续获取 X 型记录锁。

###### 间隙锁（Gap Locks）

MySQL 在 REPEATABLE-READ 隔离级别下是可以解决幻读问题的，解决方案有两种，可以使用 MVCC 方案解决，也可以采用 加锁 方案解决。但是在使用加锁方案解决时有个大问题，就是事务在第一次执行读取操作时，那些幻影记录尚不存在，我们无法给这些幻影记录加上记录锁。InnoDB 提出了一种称之为`Gap Locks`的锁，官方的类型名称为`LOCK_GAP`，我们可以简称为`gap 锁`。

比如，把 id 值为 5 的那条记录加一个 gap 锁的示意图如下：

<img src="mysql-advanced/image-20240707200920693.png" alt="image-20240707200920693" style="zoom:58%;" />

**图中 id 值为 5 的记录加了gap 锁，意味着不允许别的事务在 id 值为 5 的记录所在的间隙插入新记录，其实就是 id 列的值 (3, 8) 这个区间的新记录是不允许立即插入的。**比如，有另外一个事务再想插入一条 id 值为 4 的新记录，它定位到该条新记录在 id 为 5 的间隙锁的范围内，所以就会阻塞插入操作，直到拥有这个 gap 锁的事务提交了之后，id 列的值在区间 (3, 8) 中的新记录才可以被插入。

gap 锁仅仅是为了防止插入幻影记录而提出的。虽然有共享 gap 锁和独占 gap 锁这样的说法，但是它们起到的作用是相同的。而且如果对一条记录加了 gap 锁（不论是共享 gap 锁还是独占 gap 锁），并不会限制其他事务对这条记录加记录锁或者继续加 gap 锁。

**举例如下：**

| Session1                                               | Session2                                       |
| ------------------------------------------------------ | ---------------------------------------------- |
| select * from student where id = 5 lock in share mode; |                                                |
|                                                        | select * from student where id = 5 for update; |

这里 Session 2 并不会被堵住，因为表里并没有 id = 5 这个记录，因此 session 1 加的是间隙锁 (3, 8)，而 Session 2 也是在这个间隙加的间隙锁。它们有共同的目标，即保护这个间隙，不允许插入值。但，它们之间是不冲突的。

注意，给一条记录加了 gap 锁，只是不允许其他事务往这条记录前边的间隙插入新记录，那对于最后一条记录之后的间隙，也就是 student 表中 id 值为 20 的记录之后的间隙该咋办呢？也就是说给哪条记录加 gap 锁才能阻止其他事务插入 id 值在 (20, +∞) 这个区间的新记录呢？这时候我们在讲数据页时介绍的两条伪记录派上用场了：

- Infimum记录，表示该页面中最小的记录。
- Supremum记录，表示该页面中最大的记录。

为了实现阻止其他事务插入 id 值在 (20, +∞) 这个区间的新记录，可以给索引中的最后一条记录，也就是 id 值为 20 的那条记录所在页面的 Supremum 记录加上一个 gap 锁，如图所示：

<img src="mysql-advanced/image-20240707204132136.png" alt="image-20240707204132136" style="zoom:60%;" />

**代码演示：**

- 关于 X 和 S 锁互斥的知识回顾：

  ```mysql
  ###############################SessionA###################################
  mysql> begin;
  Query OK, 0 rows affected (0.00 sec)
  
  mysql> select * from student where id = 8 lock in share mode; # 为id=8的记录加S锁
  +----+--------+--------+
  | id | name   | class  |
  +----+--------+--------+
  |  8 | 王五   | 二班   |
  +----+--------+--------+
  1 row in set (0.00 sec)
  
  ###############################SessionB####################################
  mysql> select * from student where id = 8 for update; # A已经为id=8的加了S锁，B就不能加X锁了
  ^C^C -- query aborted
  ERROR 1317 (70100): Query execution was interrupted
  
  #################SessionA&SessionB########################################
  mysql> commit;
  Query OK, 0 rows affected (0.00 sec)
  ```

- 间隙锁：

  ```mysql
  #############################SessionA#####################################
  mysql> begin;
  Query OK, 0 rows affected (0.00 sec)
  
  # id=5的记录不存在，所以无法加上记录锁，对于不存在的记录，加的是间隙锁(3,8）
  mysql> select * from student where id = 5 lock in share mode; 
  Empty set (0.00 sec)
  
  ############################SessionB####################################
  mysql> begin;
  Query OK, 0 rows affected (0.00 sec)
  
  # 依旧加的是间隙锁，可以看出共享gap锁和独占gap锁作用相同，而且可以重复加
  mysql> select * from student where id = 5 for update;
  Empty set (0.00 sec)
  
  ##########################SessionC######################################
  mysql> insert into student(id,name,class) values(6,'tom','三班'); # 在间隙锁范围内，无法插入
  # 阻塞...
  ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction
  ```

- 可以这样实现加 id > 20的间隙锁：

  ```mysql
  mysql> select * from student where id = 25 lock in share mode;
  Empty set (0.00 sec)
  
  mysql> select * from student where id > 20 for update;
  Empty set (0.00 sec)
  ```

>**注意：如果记录存在，则使用`for update`或`lock in share mode`加的就是记录锁，如果记录不存在加的就是间隙锁。**

间隙锁的引入，可能会导致同样的语句锁住更大的范围，这其实是影响了并发度的。下面的例子会产生死锁：

| Session1                                                     | Session2                                             |
| ------------------------------------------------------------ | ---------------------------------------------------- |
| begin; select *from student where id = 5 for update;         | begin;select * from student where id = 5 for update; |
|                                                              | INSERT INTO student VALUES(5,'宋红康,‘二班’);阻塞    |
| INSERT INTO student VALUES(5,‘宋红康’,‘二班’);(ERROR 1213(40001):Deadlock found when trying to get lock; try restarting transaction) |                                                      |

- Session1 执行 select … for update 语句，由于 id = 5 这一行并不存在，因此会加上间隙锁 (3, 8)。
- Session2 执行 select … for update 语句，同样会加上间隙锁 (3, 8)，间隙锁之间不会冲突，因此这个语句可以执行成功。
- Session2 试图插入一行 (5,‘宋红康’,‘二班’)，被 Session1 的间隙锁挡住了，只好进入等待。
- Session1 试图插入一行 (5,‘宋红康’,‘二班’)，被 Session2 的间隙锁挡住了。至此，**两个 Session 陷入死锁**。

**代码演示：**

```mysql
#############################SessionA####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student where id = 5 lock in share mode; # 为id=5加间隙锁
Empty set (0.00 sec)

#############################SessionB####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student where id = 5 for update; # 为id=5加间隙锁
Empty set (0.00 sec)

mysql> insert into student(id,name,class) values(7,'Tom','一班'); # id=7在间隙区间
# 阻塞...

###########################SessionA######################################
# 出现了死锁，此报错也可能在B中出现
mysql> insert into student(id,name,class) values(6,'Jane','一班'); 
ERROR 1213 (40001): Deadlock found when trying to get lock; try restarting transaction

##########################SessionB##################################
# 出现死锁后，按照策略，让A回滚，从而B中的Insert执行成功
mysql> insert into student(id,name,class) values(7,'Tom','一班'); 
Query OK, 1 row affected (0.00 sec)
```

**分析：为什么会出现死锁呢？**

当 SessionA 中执行 INSERT，就会造成 SessionB 中 INSERT 在等 SessionA 中的间隙锁释放，SessionA 中的 INSERT 在等 SessionB 中的间隙锁的释放（因为只有释放后，这俩各自的 INSERT 才会继续执行）。从而 SessionA 和 SessionB 相互等待，就产生了死锁。

那为啥发生死锁后，SessionA 执行失败，SessionB 又成功执行了呢？这涉及 MySQL 的`处理死锁机制`：**当 MySQL 发起死锁检测，发现死锁后，主动回滚死锁链条中的某一个事务（将持有最少行级排他锁的事务进行回滚），让其他事务得以继续执行！**（详见后文死锁章节分析）

###### 临键锁（Next-Key Locks）

有时候既想锁住某条记录，又想阻止其他事务在该记录前边的间隙插入新记录，所以 InnoDB 就提出了一种称之为`Next-Key Locks`的锁，官方的类型名称为`LOCK_ORDINARY`，我们也可以简称为`next-key 锁`。**Next-Key Locks 是在存储引擎是 Innodb、事务级别在 REPEATABLE-READ 的情况下使用的数据库锁，Innodb 默认的锁就是 Next-Key locks。**

比如，把 id 值为 8 的那条记录加一个 next-key 锁的示意图如下：

<img src="mysql-advanced/image-20240707210441002.png" alt="image-20240707210441002" style="zoom:60%;" />

**next-key 锁的本质就是一个记录锁和一个 gap锁的合体，它既能保护该条记录，又能阻止别的事务将新记录插入被保护记录前边的间隙。**

```mysql
select * from student where id <=8 and id > 3 for update;
```

**代码演示：**

```mysql
#############################SessionA####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

# 为(8,15]加邻键锁(8-15是间隙锁，加上15是记录锁)
mysql> select * from student where id <= 15 and id > 8 for update; 
+----+--------+--------+
| id | name   | class  |
+----+--------+--------+
| 15 | 赵六   | 二班   |
+----+--------+--------+
1 row in set (0.00 sec)

#############################SessionB####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student where id = 15 lock in share mode; # 无法获取记录15的S锁
^C^C -- query aborted
ERROR 1317 (70100): Query execution was interrupted

mysql> select * from student where id = 15 for update; # 无法获取记录15的X锁
^C^C -- query aborted
ERROR 1317 (70100): Query execution was interrupted

mysql> insert into student(id,name,class) values(12,'Tim','一班'); # 无法在间隙内插入数据
^C^C -- query aborted
ERROR 1317 (70100): Query execution was interrupted
    
###############################SessionA&SessionB#######################
mysql> commit;
Query OK, 0 rows affected (0.00 sec)
```

###### 插入意向锁（Insert Intention Locks）

我们说一个事务在插入一条记录时，需要判断一下插入位置是不是被别的事务加了 gap 锁 （ next-key 锁也包含 gap 锁 ），如果有的话，插入操作需要等待，直到拥有 gap 锁的那个事务提交。但是，InnoDB 规定事务在等待的时候，也需要在内存中生成一个锁结构，表明有事务想在某个间隙中插入新记录，只是现在在等待。InnoDB 就把这种类型的锁命名为`Insert Intention Locks`，官方的类型名称为`LOCK_INSERT_INTENTION`，我们称为`插入意向锁`。插入意向锁是一种 gap 锁，不是意向锁，在 INSERT 操作时产生。

>**插入意向锁是在插入一条记录行前，由 INSERT 操作产生的一种间隙锁，事实上，插入意向锁并不会阻止别的事务继续获取该记录上任何类型的锁。**

插入意向锁是在插入一条记录行前，由 INSERT 操作产生的一种间隙锁。该锁用以表示插入意向，当多个事务在同一区间（gap）插入位置不同的多条数据时，事务之间不需要互相等待。假设存在两条值分别为 8 和 15 的记录，两个不同的事务分别试图插入值为 11 和 12 的两条记录，每个事务在获取插入行上独占的（排他）锁前，都会获取 (8, 15) 之间的间隙锁，但是因为数据行之间并不冲突，所以两个事务之间并不会产生冲突（阻塞等待)。总结来说，插入意向锁的特性可以分成两部分：

1. 插入意向锁是一种特殊的间隙锁 —— 间隙锁可以锁定开区间内的部分记录。

2. 插入意向锁之间互不排斥，所以即使多个事务在同一区间插入多条记录，只要记录本身（主键、唯一索引）不冲突，那么事务之间就不会出现冲突等待。

**注意，虽然插入意向锁中含有意向锁三个字，但是它并不属于意向锁而属于间隙锁，因为意向锁是表锁而插入意向锁是行锁。**

比如，把 id 值为 8 的那条记录加一个插入意向锁，示意图如下：

<img src="mysql-advanced/image-20240707214350428.png" alt="image-20240707214350428" style="zoom:60%;" />

比如，现在 T1 为 id 值为 8 的记录加了一个 gap 锁，然后 T2 和 T3 分别想向 student 表中插入 id 值分别为 4、5 的两条记录，所以现在为 id 值为 8 的记录加的锁的示意图就如下所示：

<img src="mysql-advanced/image-20240707214542389.png" alt="image-20240707214542389" style="zoom: 80%;" />

从图中可以看到，由于 T1 持有 gap 锁，所以 T2 和 T3 需要生成一个插入意向锁的锁结构并且处于等待状态。当 T1 提交后会把它获取到的锁都释放掉，这样 T2 和 T3 就能获取到对应的插入意向锁了（本质上就是把插入意向锁对应锁结构的 is_waiting 属性改为 false），T2 和 T3 之间也并不会相互阻塞，它们可以同时获取到 id 值为 8 的插入意向锁，然后执行插入操作。事实上，插入意向锁并不会阻止别的事务继续获取该记录上任何类型的锁。

**代码演示：**

```mysql
###############################SessionA####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student where id = 12 for update; # 加间隙锁
Empty set (0.00 sec)

###############################SessionB####################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> insert into student(id,name,class) values(12,'Tim','一班'); 
# 阻塞..同时会加插入意向锁

##############################SessionC###################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql>  insert into student(id,name,class) values(11,'Tim','一班');
# 阻塞..同时会加插入意向锁。可以看出插入意向锁是相互兼容的，毕竟id都不同嘛

##############################SessionA#################################
mysql> commit; # 提交
Query OK, 0 rows affected (0.00 sec)

#############################SessionB###############################
mysql> insert into student(id,name,class) values(12,'Tim','一班'); # 插入成功
Query OK, 1 row affected (45.43 sec)

##############################SessionC#################################
mysql>  insert into student(id,name,class) values(11,'Tim','一班'); # 插入成功
Query OK, 1 row affected (0.00 sec)
```

##### 页锁

`页锁`就是在`页的粒度`上进行锁定，锁定的数据资源比行锁要多，因为一个页中可以有多个行记录。当使用页锁的时候，会出现数据浪费的现象，但这样的浪费最多也就是一个页上的数据行。**页锁的开销介于表锁和行锁之间，会出现死锁。锁定粒度介于表锁和行锁之间，并发度一般。**

>死锁演示：
>
>```mysql
>事务A目前锁定了页A，想要锁定页B才可以执行完。
>事务B目前锁定了页B，想要锁定页A才可以执行完。
>### 产生死锁
>```

每个层级的锁数量是有限制的，因为锁会占用内存空间，锁空间的大小是有限的。当某个层级的锁数量超过了这个层级的阈值时，就会进行`锁升级`。锁升级就是用更大粒度的锁替代多个更小粒度的锁，比如 InnoDB 中行锁升级为表锁，这样做的好处是占用的锁空间降低了，但同时数据的并发度也下降了。

#### 按对待锁的态度划分：悲观锁、乐观锁

从对待锁的态度来看锁的话，可以将锁分成`乐观锁`和`悲观锁`，从名字中也可以看出这两种锁是两种`看待数据并发的思维方式`。需要注意的是，乐观锁和悲观锁并不是锁，而是`锁的设计思想`。

##### 悲观锁（Pessimistic Locking）

悲观锁是一种思想，顾名思义，就是很悲观，对数据被其他事务的修改持保守态度，会通过数据库自身的锁机制来实现，从而保证数据操作的排它性。

**悲观锁总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞，直到它拿到锁（共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程）。**比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁，当其他线程想要访问数据时，都需要阻塞挂起。Java 中 Synchronized 和 ReentrantLock 等独占锁，就是悲观锁思想的实现。

秒杀案例 1：商品秒杀过程中，库存数量的减少，避免出现超卖的情况。比如，商品表中有一个字段为 quantity 表示当前该商品的库存量。假设商品为华为 mate 40，id 为 1001，quantity 为 100 个。如果不使用锁的情况下，操作方法如下所示：

```mysql
# 第1步：查出商品库存
select quantity from items where id = 1001;

# 第2步：如果库存大于0，则根据商品信息生产订单
insert into orders(item_id) values(1001);
                    
# 第3步：修改商品的库存，num表示购买数量
update items set quantity = quantity - num where id = 1001;
```

这样写的话，在并发量小的公司没有大的问题，但是如果在高并发环境下，可能出现以下问题：

|      | 线程 A                    | 线程 B                    |
| ---- | ------------------------- | ------------------------- |
| 1    | step 1：查询还有100部手机 | step 1：查询还有100部手机 |
| 2    |                           | step 2：生成订单          |
| 3    | step 2：生成订单          |                           |
| 4    |                           | step 3：减库存1           |
| 5    | step 3：减库存2           |                           |

其中，线程 B 此时已经下单并且减完库存，这个时候线程 A 依然去执行 step 3，就可能会造成超卖。

我们使用悲观锁可以解决这个问题，商品信息从查询出来到修改，中间有一个生成订单的过程，使用悲观锁的原理就是，在查询 items 信息后就把当前的数据锁定，直到修改完毕后再解锁。那么整个过程中，因为数据被锁定了，就不会出现有第三者来对其进行修改了。而这样做的前提是需要将要执行的 SQL 语句放在同一个事务中，否则达不到锁定数据行的目的。修改如下：

```mysql
# 第1步：查出商品库存
select quantity from items where id = 1001 for update;

# 第2步：如果库存大于0，则根据商品信息生产订单
insert into orders(item_id) values(1001);

# 第3步：修改商品的库存，num表示购买数量
update items set quantity = quantity - num where id = 1001;
```

select … for update 是 MySQL 中的悲观锁。此时在 items 表中，id 为 1001 的那条数据就被锁定了，其他的要执行 select quantity from items where id = 1001 for update; 语句的事务，必须等本次事务提交之后才能执行，这样可以保证当前的数据不会被其它事务修改。

>注意，当执行 select quantity from items where id = 1001 for update; 语句之后，如果在其他事务中执行 select quantity from items where id = 1001; 语句，并不会受第一个事务的影响，仍然可以正常查询出数据。（存疑？）

另外，select … for update 语句执行过程中所有扫描的行都会被锁上，因此，在 MySQL 中用悲观锁必须确定使用了索引，而不是全表扫描，否则将会把整个表锁住（表锁）。

>**`InnoDB 行锁是通过给索引上的索引项加锁来实现的，只有通过索引条件检索数据，InnoDB 才使用行级锁，否则 InnoDB 将使用表锁。`**

悲观锁不适用的场景较多，它存在一些不足，因为悲观锁大多数情况下依靠数据库的锁机制来实现，以保证程序的并发访问性，同时这样对数据库性能开销影响也很大，特别是 `长事务` 而言，这样的`开销往往无法承受`，这时就需要乐观锁。

##### 乐观锁（Optimistic Locking）

**乐观锁认为对同一数据的并发操作不会总发生，属于小概率事件，不用每次都对数据上锁，但是在更新的时候会判断一下在此期间，别人有没有去更新这个数据，也就是不采用数据库自身的锁机制，而是通过程序来实现。**在程序上，可以采用`版本号机制`或者`CAS 机制`实现。**乐观锁适用于多读的应用类型，这样可以提高吞吐量。**在 Java 中 java.util.concurrent.atomic 包下的原子变量类就是使用了乐观锁的一种实现方式：CAS 实现的。

###### 乐观锁的版本号机制

在表中设计一个版本字段 version，第一次读的时候，会获取 version 字段的取值。然后对数据进行更新或删除操作时，会执行 UPDATE ... SET version = version + 1 WHERE version = version。此时，如果已经有事务对这条数据进行了更改，修改就不会成功。

这种方式类似我们熟悉的 SVN、CVS 版本管理系统，当修改了代码进行提交时，首先会检查当前版本号与服务器上的版本号是否一致，如果一致就可以直接提交，如果不一致就需要更新服务器上的最新代码，然后再进行提交。

###### 乐观锁的 CAS 机制

时间戳和版本号机制一样，也是在更新提交的时候，将`当前数据的时间戳和更新之前取得的时间戳`进行比较，如果两者一致则更新成功，否则就是版本冲突。

能看到乐观锁就是程序员自己控制数据并发操作的权限，基本是通过给数据行增加一个戳（版本号或者时间戳），从而证明当前拿到的数据是否最新。

秒杀案例 2：

```mysql
# 第1步：查出商品库存
select quantity from items where id = 1001;

# 第2步：如果库存大于0，则根据商品信息生产订单
insert into orders(item_id) values(1001);

# 第3步：修改商品的库存，num表示购买数量
update items set quantity = quantity - num, version = version + 1 where id = 1001 and version = #{version};
```

注意，如果数据表是`读写分离 (主写从读)`的表，当 matser 表中写入的数据没有及时同步到 slave 表中时，会造成更新一直失败的问题（因为查询是 slave 表，而更新是 master 表，如果 master 表的数据没有及时同步到 slave 表，可能会出现 master 表与 slave 表的 version 不一致，导致更新失败）。此时，**需要强制读取 master 表中的数据**（即将 select 语句放到事务中即可，这时候查询的就是 master 主库了）。

如果`对同一条数据进行频繁的修改`的话，那么就会出现这么一种场景，每次修改都只有一个事务能更新成功，在业务感知上面就有大量的失败操作。我们把代码修改如下：

```mysql
# 第1步：查出商品库存
select quantity from items where id = 1001;

# 第2步：如果库存大于0，则根据商品信息生产订单
insert into orders(item_id) values(1001);

# 第3步：修改商品的库存，num表示购买数量
update items set quantity = quantity - num where id = 1001 and quantity - num > 0;
```

这样就会使每次修改都能成功，而且不会出现超卖的现象。

###### 两种锁的适用场景

从这两种锁的设计思想中，总结一下乐观锁和悲观锁的适用场景：

- `乐观锁适合读操作多的场景`，相对来说写的操作比较少。它的优点在于程序实现，不存在死锁问题，不过适用场景也会相对乐观，因为它阻止不了除了程序以外的数据库操作。

- `悲观锁适合写操作多的场景`，因为写的操作具有排它性。采用悲观锁的方式，可以在数据库层面阻止其他事务对该数据的操作权限，防止读-写和写-写的冲突。

把乐观锁和悲观锁总结如下图所示：

<img src="mysql-advanced/image-20240707230658688.png" alt="image-20240707230658688" style="zoom: 60%;" />

#### 按加锁的方式划分：隐式锁、显式锁

##### 隐式锁

>回顾：
>
>- 一个事务在执行 INSERT 操作时，如果即将插入的间隙已经被其他事务加了 gap 锁，那么本次 INSERT 操作会阻塞，并且当前事务会在该间隙上加一个插入意向锁。【被动加锁】
>- 否则，一般情况下，新插入一条记录的操作并不加锁（后面会推翻这个结论，严格来说是加锁的，加的是隐式锁）。【主动加锁】

那如果一个事务首先插入了一条记录（此时并没有在内存生产与该记录关联的锁结构），然后另一个事务：

- 立即使用 SELECT … LOCK IN SHARE MODE 语句读取这条记录，也就是要获取这条记录的 S 锁，或者使用 SELECT … FOR UPDATE 语句读取这条记录，也就是要获取这条记录的 X 锁，怎么办？
  - 如果允许这种情况的发生，那么可能产生脏读问题。
- 立即修改这条记录，也就是要获取这条记录的 X 锁，怎么办?
  - 如果允许这种情况的发生，那么可能产生脏写问题。

这时候前边提过的`事务 id`又要起作用了。把聚簇索引和二级索引中的记录分开看一下：

- **情景一：**对于聚簇索引记录来说，有一个 trx_id 隐藏列，该隐藏列记录着最后改动该记录的事务 id。那么如果在当前事务中新插入一条聚簇索引记录后，该记录的 trx_id 隐藏列代表的的就是当前事务的事务id，如果其他事务此时想对该记录添加 S 锁或者 X 锁时，首先会看一下该记录的 trx_id 隐藏列代表的事务是否是当前的活跃事务，如果是的话，那么就帮助当前事务创建一个 X 锁 （也就是为当前事务创建一个锁结构，is_waiting 属性是 false），然后自己进入阻塞状态（也就是为自己也创建一个锁结构，is_waiting 属性是 true）。
- **情景二：**对于二级索引记录来说，本身并没有 trx_id 隐藏列，但是在二级索引页面的 Page Header 部分有一个 PAGE_MAX_TRX_ID 属性，该属性代表对该页面做改动的最大的事务 id，如果 PAGE_MAX_TRX_ID 属性值小于当前最小的活跃事务 id，那么说明对该页面做修改的事务都已经提交了，否则就需要在页面中定位到对应的二级索引记录，然后回表找到它对应的聚簇索引记录，然后再重复情景一的做法。
- 即：一个事务对新插入的记录可以不显式的加锁（生成一个锁结构），但是由于事务 id 的存在，相当于加了一个隐式锁。别的事务在对这条记录加 S 锁或者 X 锁时，由于隐式锁的存在，会先帮助当前事务生成一个锁结构，然后自己再生成一个锁结构后进入等待状态。隐式锁是一种延迟加锁的机制，从而来减少加锁的数量。

隐式锁在实际内存对象中并不含有这个锁信息。**只有当产生锁等待时，隐式锁转化为显式锁。**

InnoDB 的 INSERT 操作，对插入的记录不加锁，但是此时如果另一个线程进行当前读，类似以下的用例，整个过程会发生什么呢？

```mysql
############################SessionA#########################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> insert into student values(12,'关羽','三班'); # 此时相当于会加个隐式锁
Query OK, 1 row affected (0.00 sec)

###########################SessionC###############################
mysql> SELECT * FROM performance_schema.data_lock_waits\G; # 隐式锁是查不到的
Empty set (0.00 sec)

##########################SessionB################################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from student lock in share mode; # 可以侧面得出结论，隐式锁是存在的
# 阻塞... # 阻塞也会导致事务A中的隐式锁转为显示锁

##########################SessionC###################################
mysql> SELECT * FROM performance_schema.data_lock_waits\G; # 查到由隐式锁转的显示锁
*************************** 1. row ***************************
                          ENGINE: INNODB
       REQUESTING_ENGINE_LOCK_ID: 140078105288944:47:4:10:140078009627240
REQUESTING_ENGINE_TRANSACTION_ID: 421553081999600
            REQUESTING_THREAD_ID: 49
             REQUESTING_EVENT_ID: 25
REQUESTING_OBJECT_INSTANCE_BEGIN: 140078009627240
         BLOCKING_ENGINE_LOCK_ID: 140078105288088:47:4:10:140078009620736
  BLOCKING_ENGINE_TRANSACTION_ID: 17430
              BLOCKING_THREAD_ID: 49
               BLOCKING_EVENT_ID: 25
  BLOCKING_OBJECT_INSTANCE_BEGIN: 140078009620736
1 row in set (0.00 sec)
```

**隐式锁的逻辑过程如下：**

1. InnoDB 的每条记录中都一个隐含的 trx_id 字段，这个字段存在于聚簇索引的 B+ 树中。
2. 在操作一条记录前，首先根据记录中的 trx_id 检查该事务是否是活动的事务（未提交或回滚）。如果是活动的事务，首先将隐式锁转换为显式锁（就是为该事务添加一个锁）。
3. 检查是否有锁冲突，如果有冲突，创建锁，并设置为 waiting 状态。如果没有冲突不加锁，跳到第五步。
4. 等待加锁成功，被唤醒，或者超时。
5. 写数据，并将自己的 事务 id 写入trx_id 字段。

##### 显式锁

通过特定的语句进行加锁，一般称之为显示加锁，例如：

- 显示加共享锁：

  ```mysql
  select ... lock in share mode;
  ```

- 显示加排他锁：

  ```mysql
  select ... for update;
  ```

#### 其它锁之：全局锁

`全局锁`就是**对整个数据库实例加锁**。当你需要让整个库处于`只读状态`的时候，可以使用这个命令，之后其他线程的以下语句会被阻塞：数据更新语句（(数据的增删改）、数据定义语句（包括建表、修改表结构等）和更新类事务的提交语句。全局锁的典型使用场景是：做`全库逻辑备份`。

全局锁的命令：

```mysql
Flush tables with read lock;
```

####  其它锁之：死锁

##### 基本概念

`死锁`是**指两个或多个事务在同一资源上相互占用，并请求锁定对方占用的资源，从而导致恶性循环。**死锁举例如下：

举例一：

|      | 事务 1                                                       | 事务 2                                      |
| ---- | ------------------------------------------------------------ | ------------------------------------------- |
| 1    | start transaction;<br />update account set money = 10 where id = 1; | start transaction;                          |
| 2    |                                                              | update account set money = 10 where id = 2: |
| 3    | update account set money = 20 where id = 2;                  |                                             |
| 4    |                                                              | update account set money = 20 where id = 1; |

这时候，事务 1 在等待事务 2 释放 id = 2 的行锁，而事务 2 在等待事务 1 释放 id = 1 的行锁。 事务 1 和事务 2 在互相等待对方的资源释放，就是进入了死锁状态。当出现死锁以后，有两种策略 ：

- 一种策略是，**直接进入等待，直到超时。**这个超时时间可以通过参数`innodb_lock_wait_timeout`来设置。
- 另一种策略是，**发起死锁检测，发现死锁后，主动回滚死锁链条中的某一个事务（将持有最少行级排他锁的事务进行回滚），让其他事务得以继续执行。**将参数`innodb_deadlock_detect`设置为 on，表示开启这个逻辑。

在 InnoDB 中，innodb_lock_wait_timeout 的默认值是 50 秒，意味着如果采用第一个策略，当出现死锁以后，第一个被锁住的线程要过 50 秒才会超时退出，然后其他线程才有可能继续执行。对于在线服务来说，这个等待时间往往是无法接受的。

但是，我们又不可能直接把这个时间设置成一个很小的值，比如 1 秒。这样当出现死锁的时候，确实很快就可以解开，但如果不是死锁，而是简单的锁等待呢？所以，超时时间设置太短的话，会出现很多误伤。

**举例二：**

用户 A 给用户 B 转账 100，与此同时，用户 B 也给用户 A 转账 100。这个过程，可能导致死锁。

```mysql
# 事务1
update account set balance = balance - 108 where name = 'A'; # 操作1
update account set balance = balance + 100 where name = 'B'; # 操作3

# 事务2
update account set balance = balance - 100 where name = 'B'; # 操作2
update account set balance = balance + 100 where name = 'A'; # 操作4
```

**产生死锁的必要条件：**

1. 两个或者两个以上事务。
2. 每个事务都已经持有锁并且申请新的锁。
3. 锁资源同时只能被同一个事务持有或者不兼容。
4. 事务之间因为持有锁和申请锁导致彼此循环等待。

>死锁的关键在于：两个或以上的 Session 加锁的顺序不一致。

**代码演示：**

```mysql
#####################################SessionA########################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> update account set balance = balance - 10 where id = 1; # 为id=1的加X锁
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

###################################SessionB########################
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> update account set balance = balance - 10 where id = 3; # 为id=3的加X锁
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

######################################SessionA###########################
mysql> update account set balance = balance + 10 where id = 3; # 想要获取id为3的X锁
# 阻塞...

#####################################SessionB####################
mysql> update account set balance = balance + 10 where id = 1; # 想要获取id为1的X锁
ERROR 1213 (40001): Deadlock found when trying to get lock; # 出现死锁

#################################SessionA############################
mysql> update account set balance = balance + 10 where id = 3; # 阻塞解开，继续执行
Query OK, 1 row affected (18.39 sec) # 具体死锁为啥会被解开，下面会讲哦
Rows matched: 1  Changed: 1  Warnings: 0
```

##### 如何处理死锁

**方式一：等待，直到超时（默认 innodb_lock_wait_timeout=50s）。**

即当两个事务互相等待时，当一个事务等待时间超过设置的阈值时，就将其回滚，另外事务继续进行。这种方法简单有效，在 InnoDB 中，参数 innodb_lock_wait_timeout 用来设置超时时间。

缺点：对于在线服务来说，这个等待时间往往是无法接受的。那将此值修改短一些，比如 1 秒，0.1 秒是否合适？不合适，容易误伤到普通的锁等待。

**方式二：使用死锁检测进行死锁处理。**

方式一检测死锁太过被动，InnoDB 还提供了`wait-for graph`算法来主动进行死锁检测，每当加锁请求无法立即满足需要并进入等待时，wait-for graph 算法都会被触发。

这是一种较为`主动的死锁检测机制`，要求数据库`保存锁的信息链表`和`事务等待链表`两部分信息。

<img src="mysql-advanced/image-20240707233744440.png" alt="image-20240707233744440" style="zoom:67%;" />

基于这两个信息，可以绘制 wait-for graph 算法等待图：

<img src="mysql-advanced/image-20240707233843016.png" alt="image-20240707233843016" style="zoom:67%;" />

> 死锁检测的原理是构建一个以`事务为顶点、锁为边的有向图`，判断有向图是否存在`环`，存在即有死锁。

一旦检测到回路、有死锁，这时候 InnoDB 存储引擎会选择回滚 undo 量最小的事务，让其他事务继续执行（innodb_deadlock_detect=on 表示开启这个逻辑）。

缺点：每个新的被阻塞的线程，都要判断是不是由于自己的加入导致了死锁，这个操作时间复杂度是 O(n)。如果 100 个并发线程同时更新同一行，意味着要检测 100 * 100 = 1 万次，1 万个线程就会有 1 千万次检测。

**如何解决？**

- 方式 1：关闭死锁检测，但意味着可能会出现大量的超时，会导致业务有损。
- 方式 2：控制并发访问的数量。比如在中间件中实现对于相同行的更新，在进入引擎之前排队，这样在 InnoDB 内部就不会有大量的死锁检测工作。

>进一步的思路：可以考虑通过`将一行改成逻辑上的多行`来减少锁冲突。比如，连锁超市账户总额的记录，可以考虑放到多条记录上，账户总额等于这多个记录的值的总和。

##### 如何避免死锁

- 合理设计索引，使业务 SQL 尽可能通过索引定位更少的行，减少锁竞争。
- 调整业务逻辑 SQL 执行顺序，避免 update/delete 等长时间持有锁的 SQL 在事务前面。
- 避免大事务，尽量将大事务拆成多个小事务来处理，小事务缩短锁定资源的时间，发生锁冲突的几率也更小。
- 在并发比较高的系统中，不要显式加锁，特别是是在事务里显式加锁。如 select … for update 语句，如果是在事务里运行了 start transaction 或设置了 autocommit 等于 0，那么就会锁定所查找到的记录。
- 降低隔离级别。如果业务允许，将隔离级别调低也是较好的选择，比如将隔离级别从 RR 调整为 RC，可以避免掉很多因为 gap 锁造成的死锁。

### 锁的内存结构

前边说对一条记录加锁的本质就是在内存中创建一个`锁结构`与之关联，那么是不是一个事务对多条记录加锁，就要创建多个锁结构呢？比如：

```mysql
# 事务T1
SELECT * FROM user LOCK IN SHARE MODE;
```

理论上创建多个锁结构没问题，但是如果一个事务要获取 10000 条记录的锁，生成 10000 个锁结构也太崩溃了！所以决定在对不同记录加锁时，如果符合下边这些条件的记录会放到一个锁结构中：

- 在同一个事务中进行加锁操作。
- 被加锁的记录在同一个页面中。
- 加锁的类型是一样的。
- 等待状态是一样的。

InnoDB 存储引擎中的锁结构如下：

<img src="mysql-advanced/image-20240708210424850.png" alt="image-20240708210424850" style="zoom: 50%;" />

结构解析：

1. `锁所在的事务信息`

   - 不论是表锁还是行锁，都是在事务执行过程中生成的，哪个事务生成了这个锁结构，这里就记录这个事务的信息。
   - 此锁所在的事务信息在内存结构中只是一个指针，通过指针可以找到内存中关于该事务的更多信息，比方说事务id等。

2. `索引信息`

   - 对于行锁来说，需要记录一下加锁的记录是属于哪个索引的。这里也是一个指针。

3. `表锁/行锁信息`：表锁结构和行锁结构在这个位置的内容是不同的。

   - 表锁：记载着是对哪个表加的锁，还有其他的一些信息。
   - 行锁：记载了三个重要的信息。
     - Space ID：记录所在表空间。
     - Page Number：记录所在页号。
     - n_bits：对于行锁来说，一条记录就对应着一个比特位，一个页面中包含很多记录，用不同的比特位来区分到底是哪一条记录加了锁。为此在行锁结构的末尾放置了一堆比特位，这个 n_bits 属性代表使用了多少比特位。n_bits 的值一般都比页面中记录条数多一些，主要是为了之后在页面中插入了新记录后也不至于重新分配锁结构。

4. `type_mode`：这是一个 32 位的数，被分成了`lock_mode`、`lock_type`和`rec_lock_type`三个部分，如图所示。

   <img src="mysql-advanced/image-20240708231033582.png" alt="image-20240708231033582" style="zoom:67%;" />

   - `锁的模式 lock_mode`，占用低 4 位，可选的值如下：
     - LOCK_IS（十进制的 0）：表示共享意向锁，也就是 IS 锁。
     - LOCK_IX（十进制的 1）：表示独占意向锁，也就是 IX 锁。
     - LOCK_S（十进制的 2）：表示共享锁，也就是 S 锁。
     - LOCK_X（十进制的 3）：表示独占锁，也就是 X 锁。
     - LOCK_AUTO_INC（十进制的 4）：表示 AUTO-INC 锁。
     - 在 InnoDB 存储引擎中，LOCK_IS，LOCK_IX，LOCK_AUTO_INC 都算是表级锁的模式，LOCK_S 和 LOCK_X 既可以算是表级锁的模式，也可以是行级锁的模式。
   - `锁的类型 lock_type`，占用第 5～8 位，不过现阶段只有第 5 位和第 6 位被使用：
     - LOCK_TABLE（十进制的 16）：也就是当第 5 个比特位置为 1 时，表示表级锁。
     - LOCK_REC（十进制的 32）：也就是当第 6 个比特位置为 1 时，表示行级锁。
   - `行锁的具体类型 rec_lock_type`，使用其余的位来表示。只有在 lock_type 的值为 LOCK_REC 时，也就是只有在该锁为行级锁时，才会被细分为更多的类型：
     - LOCK_ORDINARY（十进制的 0）：表示 next-key 锁 。
     - LOCK_GAP（十进制的 512）：也就是当第 10 个比特位置为 1 时，表示 gap 锁。
     - LOCK_REC_NOT_GAP（十进制的 1024）：也就是当第 11 个比特位置为 1 时，表示正经记录锁。
     - LOCK_INSERT_INTENTION（十进制的 2048）：也就是当第 12 个比特位置为 1 时，表示插入意向锁。
     - 其他的类型：还有一些不常用的类型我们就不多说了。
   - `is_waiting 属性`，基于内存空间的节省，所以把 is_waiting 属性放到了 type_mode 这个 32 位的数字中：
     - LOCK_WAIT（十进制的 256）：当第 9 个比特位置为 1 时，表示 is_waiting 为 true，也就是当前事务尚未获取到锁，处在等待状态；当这个比特位为 0 时，表示 is_waiting 为 false，也就是当前事务获取锁成功。

5. `其他信息`：为了更好的管理系统运行过程中生成的各种锁结构而设计了各种哈希表和链表。

6. `一堆比特位`：如果是行锁结构的话，在该结构末尾还放置了一堆比特位，比特位的数量是由上边提到的 n_bits 属性表示的。InnoDB 数据页中的每条记录在记录头信息中都包含一个 heap_no 属性，伪记录 Infimum 的 heap_no 值为 0，Supremum 的 heap_no 值为 1，之后每插入一条记录，heap_no 值就增 1。锁结构最后的一堆比特位就对应着一个页面中的记录，一个比特位映射一个 heap_no，即一个比特位映射到页内的一条记录。

### 锁监控

关于 MySQL 锁的监控，我们一般可以通过检查`InnoDB_row_lock`等状态变量来分析系统上的行锁的争夺情况：

```mysql
mysql> show status like 'innodb_row_lock%';
+-------------------------------+--------+
| Variable_name                 | Value  |
+-------------------------------+--------+
| Innodb_row_lock_current_waits | 0      |
| Innodb_row_lock_time          | 129831 |
| Innodb_row_lock_time_avg      | 18547  |
| Innodb_row_lock_time_max      | 51095  |
| Innodb_row_lock_waits         | 7      |
+-------------------------------+--------+
5 rows in set (0.00 sec)
```

对各个状态量的说明如下：

- `Innodb_row_lock_current_waits`：当前正在等待锁定的数量。
- `Innodb_row_lock_time`：从系统启动到现在锁定总时间长度。（**等待总时长**）
- `Innodb_row_lock_time_avg`：每次等待所花平均时间。（**等待平均时长**）
- `Innodb_row_lock_time_max`：从系统启动到现在等待最常的一次所花的时间。
- `Innodb_row_lock_waits`：系统启动后到现在总共等待的次数。（**等待总次数**）
- 对于这 5 个状态变量，比较重要的 3 个是 Innodb_row_lock_time，Innodb_row_lock_time_avg 和 Innodb_row_lock_waits。

**其他监控方法：**

MySQL 把事务和锁的信息记录在了`information_schema`库中，涉及到的三张表分别是`INNODB_TRX`、`INNODB_LOCKS`和`INNODB_LOCK_WAITS`。

MySQL 5.7 及之前，可以通过 information_schema.INNODB_LOCKS 查看事务的锁情况，但只能看到阻塞事务的锁；如果事务并未被阻塞，则在该表中看不到该事务的锁情况。

MySQL 8.0 删除了 information_schema.INNODB_LOCKS，添加了`performance_schema.data_locks`，可以通过 performance_schema.data_locks 查看事务的锁情况，和 MySQL 5.7 及之前不同，**performance_schema.data_locks 不但可以看到阻塞该事务的锁，还可以看到该事务所持有的锁。**

同时，information_schema.INNODB_LOCK_WAITS 也被`performance_schema.data_lock_waits`所代替。

### 附录

`// TODO`

## 多版本并发控制

### 什么是 MVCC

**`MVCC`**：**Multiversion Concurrency Control，多版本并发控制。**顾名思义，**MVCC 是通过数据行的多个版本管理来实现数据库的并发控制**。这项技术使得在 InnoDB 的事务隔离级别下执行一致性读操作有了保证。换言之，就是为了查询一些正在被另一个事务更新的行，并且可以看到它们被更新之前的值，这样在做查询的时候就不用等待另一个事务释放锁。

MVCC 没有正式的标准，在不同的 DBMS 中 MVCC 的实现方式可能是不同的，也不是普遍使用的（可以参考相关的 DBMS 文档）。这里讲解 InnoDB 中 MVCC 的实现机制（MySQL 其它的存储引擎并不支持它）。

### 快照读与当前读

MVCC 在 MySQL InnoDB 中的实现主要是为了提高数据库并发性能，用更好的方式去处理读-写冲突，做到即使有读写冲突时，也能做到不加锁，非阻塞并发读，而这个读指的就是快照读，而非当前读。当前读实际上是一种加锁的操作，是悲观锁的实现，而 MVCC 本质是采用乐观锁思想的一种方式。

#### 快照读

**`快照读`**：**又叫一致性读，读取的是快照数据。**不加锁的简单的 SELECT 都属于快照读，即不加锁的非阻塞读。比如：

```mysql
SELECT * FROM player WHERE ...;
```

之所以出现快照读的情况，是**基于提高并发性能**的考虑，快照读的实现是基于 MVCC，它在很多情况下，避免了加锁操作，降低了开销。

既然是基于多版本，那么**快照读可能读到的并不一定是数据的最新版本**，而有可能是之前的历史版本。

快照读的**前提是隔离级别不是串行级别**，串行级别下的快照读会退化成当前读。

#### 当前读

**`当前读`**：**读取的是记录的最新版本**（最新数据，而不是历史版本的数据），读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。加锁的 SELECT，或者对数据进行增删改操作，都会进行当前读。比如：

```mysql
SELECT * FROM student LOCK IN SHARE MODE; # 共享锁

SELECT * FROM student FOR UPDATE; # 排他锁

INSERT INTO student values ...; # 排他锁

DELETE FROM student WHERE ...; # 排他锁

UPDATE student SET ...; # 排他锁
```

> 注意：**InnoDB 增删改操作默认加 X 锁，读操作默认不加锁。**

### 知识点回顾

#### 再谈隔离级别

事务有 4 个隔离级别，可能存在三种并发问题：（准确来说是四种，还有一种是脏写）

<img src="mysql-advanced/image-20240720235523060.png" alt="image-20240720235523060" style="zoom: 33%;" />

在 MySQL 中，默认的隔离级别是可重复读，可以解决脏读和不可重复读的问题，如果仅从定义的角度来看，它并不能解决幻读问题。如果想要解决幻读问题，就需要采用串行化的方式，也就是将隔离级别提升到最高，但这样一来就会大幅降低数据库的事务并发能力。

**MVCC 可以不采用锁机制，而是通过乐观锁的方式来解决不可重复读和幻读问题！它可以在大多数情况下替代行级锁，降低系统的开销。**

<img src="mysql-advanced/image-20240720235653609.png" alt="image-20240720235653609" style="zoom:33%;" />

> MySQL 中，是遵循上图的处理方式，可重复读和串行化两种隔离级别，都可以解决幻读的问题。
>
> - 如果隔离级别是可重复读，采用的是 MVCC 的方式，这是 MySQL 默认的隔离级别。
> - 如果隔离级别是串行化，采用的是加锁的方式。
> - **如果采用加锁的方式，使用的是间隙锁解决幻读问题。**

#### 隐藏字段、undo log 版本链

回顾一下 undo log 的版本链，对于使用 InnoDB 存储引擎的表来说，它的聚簇索引记录中都包含两个必要的隐藏列。

1. `trx_id`：每次一个事务对某条聚簇索引记录进行改动时，都会把该事务的事务 id 赋值给 trx_id 隐藏列。
2. `roll_pointer`：每次对某条聚簇索引记录进行改动时，都会把旧的版本写入到 undo log 中，然后这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息。

举例：student 表数据如下。

```mysql
mysql> SELECT * FROM student;
+----+--------+--------+
| id | name   | class  |
+----+--------+--------+
|  1 | 张三   | 一班    |
+----+--------+--------+
1 row in set (0.07 sec)
```

假设插入该记录的事务 id 为 8，那么此刻该条记录的示意图如下所示：

<img src="mysql-advanced/image-20240721000255201.png" alt="image-20240721000255201" style="zoom: 50%;" />

>**insert undo 只在事务回滚时起作用，当事务提交后，该类型的 undo log 就没用了，它占用的 Undo Log Segment 也会被系统回收（也就是该 undo log 占用的 Undo 页面链表要么被重用，要么被释放)。**

假设之后两个事务 id 分别为 10、20 的事务对这条记录进行 UPDATE 操作，操作流程如下：

| 发生时间顺序 | 事务 10                                        | 事务 20                                        |
| ------------ | ---------------------------------------------- | ---------------------------------------------- |
| 1            | BEGIN;                                         |                                                |
| 2            |                                                | BEGIN;                                         |
| 3            | UPDATE student SET name = "李四" WHERE id = 1; |                                                |
| 4            | UPDATE student SET name = "王五" WHERE id = 1; |                                                |
| 5            | COMMIT;                                        |                                                |
| 6            |                                                | UPDATE student SET name = "钱七" WHERE id = 1; |
| 7            |                                                | UPDATE student SET name = "宋八" WHERE id = 1; |
| 8            |                                                | COMMIT;                                        |

>有人可能会想，能不能在两个事务中交叉更新同一条记录呢？
>
>答案是不能！因为这种情况，就是一个事务修改了另一个未提交事务修改过的数据，属于脏写。
>
>InnoDB 使用锁来保证不会有脏写情况的发生，也就是在第一个事务更新了某条记录后，就会给这条记录加锁，另一个事务再次更新时，就需要等待第一个事务提交了，把锁释放之后才可以继续更新。
>

每次对记录进行改动，都会记录一条 undo log，每条 undo log 也都有一个 roll_pointer 属性（INSERT 操作对应的 undo log 没有该属性，因为 INSERT 记录没有更早的版本，它自己是起始的版本)，可以将这些 undo log 都连起来，串成一个链表：

<img src="mysql-advanced/image-20240721001216698.png" alt="image-20240721001216698" style="zoom: 50%;" />

对该记录每次更新后，都会将旧值放到一条 undo log 中，就算是该记录的一个旧版本，随着更新次数的增多，所有的版本都会被 roll_pointer 属性连接成一个链表，把这个链表称之为`版本链`，版本链的头节点就是当前记录最新的值。

另外，每个版本中还包含生成该版本时对应的事务 id。

### MVCC 实现原理之 ReadView

**`MVCC 的实现依赖于：隐藏字段、undo log 版本链、ReadView。`**

#### 什么是 ReadView

在 MVCC 机制中，多个事务对同一个行记录进行更新会产生多个历史快照，这些历史快照保存在 undo log 里。如果一个事务想要查询这个行记录，需要读取哪个版本的行记录呢？这时就需要用到 ReadView 了，它解决了行的可见性问题。

**`ReadView`**：**就是事务在使用 MVCC 机制进行快照读操作时产生的读视图。**当事务启动时，会生成数据库系统当前的一个快照，InnoDB 为每个事务构造了一个数组，用来记录并维护系统当前`活跃事务的 ID`（"活跃" 指的就是，启动了但还没提交)

>ReadView 和事务是一对一的关系。

#### 设计思路

使用 READ UNCONNMITTED 隔离级别的事务，由于可以读到未提交事务修改过的记录，所以直接读取的记录就是最新版本了。此时，不需要使用 MVCC，也就不需要 ReadView。

使用 SERIALIZABLE 隔离级别的事务，InnoDB 规定使用加锁的方式来访问记录。此时，不需要使用 MVCC，也就不需要 ReadView。

**使用 READ COMMITTED 和 REPEATABLE READ 隔离级别的事务，都必须保证读到已经提交了的事务修改过的记录。假如另一个事务已经修改了记录但是尚未提交，是不能直接读取最新版本的记录的，核心问题就是需要判断一下版本链中的哪个版本是当前事务可见的，这是 ReadView 要解决的主要问题。**

ReadView 中主要包含 4 个比较重要的内容，分别如下：

1. `creator_trx_id`：创建这个 ReadView 的事务 ID。
2. `trx_ids`：表示在生成 ReadView 时，当前系统中活跃的读写事务的事务 id 列表。
3. `up_limit_id`：活跃的事务中最小的事务 ID。
4. `low_limit_id`：表示生成 ReadView 时，系统中应该分配给下一个事务的 id 值。low_limit_id 是当前系统最大的事务 id 值，这里要注意是系统中的事务 id，需要区别于正在活跃的事务 id。

>注意：**low_limit_id 并不是 trx_ids 中的最大值，实际上，low_limit_id 不存在于 trx_ids 中。**事务 id 是递增分配的，比如，现在有 id 为 1，2，3 这三个事务，之后 id 为 3 的事务提交了。那么一个新的读事务在生成 ReadView 时，trx_ids 就包括 1 和 2，up_limit_id 的值就是 1，low_limit_id 的值就是 4。

**举例：**

trx_ids 为 trx2、trx3、trx5 和 trx8 的集合，系统的最大事务 id（low_limit_id）为 trx8 + 1（如果在此之前没有其他的新增事务），活跃的最小事务 id（up_limit_id）为 trx2。

<img src="mysql-advanced/image-20240721085100350.png" alt="image-20240721085100350" style="zoom:50%;" />

#### ReadView 的规则

有了这个 ReadView，这样在访问某条记录时，只需要按照下边的步骤判断该记录在 undo log 版本链中的某个版本是否可见：

- 如果被访问版本的 trx_id 属性值`等于 ReadView 中的 creator_trx_id 值`，意味着当前事务在访问它自己修改过的记录，所以`该版本可以被当前事务访问`。
- 如果被访问版本的 trx_id 属性值`小于 ReadView 中的 up_limit_id 值`，表明生成该版本的事务在当前事务生成 ReadView 前已经提交，所以`该版本可以被当前事务访问`。
- 如果被访问版本的 trx_id 属性值`大于或等于 ReadView 中的 low_limit_id 值`，表明生成该版本的事务在当前事务生成 ReadView 后才开启，所以`该版本不可以被当前事务访问`。（否则会出现脏读）
- 如果被访问版本的 trx_id 属性值`在 ReadView 的 up_limit_id 和 low_limit_id 之间`，那就需要判断一下 trx_id 属性值是不是在 trx_ids 列表中。
  - 如果在，说明创建 ReadView 时生成该版本的事务还是活跃的，`该版本不可以被当前事务访问`。
  - 如果不在，说明创建 ReadView 时生成该版本的事务已经被提交，`该版本可以被当前事务访问`。

> 此处被访问版本，是指 undo log 版本链中的版本。 

#### MVCC 整体操作流程

了解了这些概念之后，来看下当查询一条记录的时候，系统如何通过 MVCC 找到它：

1. 首先，获取事务自己的版本号，也就是事务 id；
2. 获取（生成）ReadView；
3. 查询得到的数据，然后与 ReadView 中的事务版本号进行比较；
4. 如果不符合 ReadView 规则（当前版本不能被访问），就需要从 undo log 中获取历史快照；
5. 最后返回符合规则的数据。

**如果某个版本的数据对当前事务不可见的话，那就`顺着 undo log 版本链`找到下一个版本的数据，继续按照上边的步骤判断可见性，依此类推，直到版本链中的最后一个版本。如果最后一个版本也不可见的话，那么就意味着该条记录对该事务完全不可见，查询结果就不包含该记录。**

>InnoDB中，MVCC 是通过`undo log 版本链 + ReadView`进行数据读取：undo log 版本链保存了历史快照，而 ReadView 规则帮我们判断当前版本的数据是否可见。

在隔离级别为`读已提交`（READ COMMITTED）时，**一个事务中的每一次 SELECT 查询都会重新获取一次 ReadView**。示例：

| 事务                                 | 说明               |
| ------------------------------------ | ------------------ |
| BEGIN;                               |                    |
| SELECT * FROM student WHERE id  > 2; | 获取一次 Read View |
| …                                    |                    |
| SELECT * FROM student WHERE id  > 2; | 获取一次 Read View |
| COMMIT;                              |                    |

>注意，此时同样的查询语句都会重新获取一次 ReadView，这时如果 ReadView 不同，就可能产生不可重复读或者幻读的情况，这样符合Read Committed的规则特点。

当隔离级别为`可重复读`（REPEATABLE READ）的时候，就避免了不可重复读，这是因为**一个事务只在第一次 SELECT 的时候会获取一次 ReadView，而后面所有的 SELECT 都会复用这个 ReadView**。示例：

<img src="mysql-advanced/image-20240721101450225.png" alt="image-20240721101450225" style="zoom: 60%;" />

### 举例说明

假设现在 student 表中只有一条由事务 id 为 8 的事务插入的一条记录：

```mysql
mysql> SELECT * FROM student;
+----+--------+--------+
| id | name   | class  |
+----+--------+--------+
|  1 | 张三   | 一班    |
+----+--------+--------+
1 row in set (0.07 sec)
```

MVCC 只能在`READ COMMITTED`和`REPEATABLE READ`两个隔离级别下工作。接下来看一下 READ COMMITTED 和 REPEATABLE READ 所谓的生成 ReadView 的时机不同，到底不同在哪里。

>关于不同隔离级别下 ReadView 的事务 id，可以概括如下：
>
>- 对于 RC 隔离级别：
>  - 在一个事务中，每次查询会创建 id 为 0 的 ReadView。
>  - 一旦有修改操作，会切换到以当前事务 id 为 creator_trx_id 的新 ReadView。
>- 对于 RR 隔离级别：
>  - 在一个事务中，只有第一次的查询会创建一个 Read View。
>  - 这个 ReadView 的 creator_trx_id 就是当前的事务 id。
>
>**RR 要求整个事务的查询都要一致，所以只有第一次查询才会生成一个 ReadView。而 RC 可以在同一事务内读取不同版本的数据，所以每次修改和查询都会生成新的 ReadView。**

#### READ COMMITTED 隔离级别下

**`READ COMMITTED：每次读取数据前都生成一个 ReadView。`**

现在有两个事务 id 分别为 10、20 的事务在执行：

```mysql
# Transaction 10
BEGIN;
UPDATE student SET name = "李四" WHERE id = 1;
UPDATE student SET name = "王五" WHERE id = 1;

# Transaction 20
BEGIN;
# 更新了一些别的表的记录 (为了分配事务 id)
...
```

> 说明：**事务执行过程中，只有在第一次真正修改记录时（比如使用 INSERT、DELETE、UPDATE 语句），才会被分配一个单独的事务 id，这个事务 id 是递增的。**所以我们才在事务 20 中更新一些别的表的记录，目的是让它分配事务 id。

此刻，表 student 中 id 为 1 的记录得到的 undo log 版本链如下所示：

<img src="mysql-advanced/image-20240721102342138.png" alt="image-20240721102342138" style="zoom:67%;" />

假设现在有一个使用  READ COMMITTED 隔离级别的事务开始执行：

```mysql
# 使用 READ COMMITTED 隔离级别的事务

BEGIN;
# SELECT1 操作，此时，Transaction 10 和 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值为'张三'
```

这个 SELECT1 的执行过程如下：

1. 步骤一：在执行 SELECT 语句时会先生成一个 ReadView，ReadView 的 trx_ids 列表的内容就是 [10, 20]，up_limit_id 为 10，low_limit_id 为 21，creator_trx_id 为 0。
2. 步骤二：从 undo log 版本链中挑选可见的记录，从图中看出，最新版本的列 name 的内容是 '王五'，该版本的 trx_id 值为 10，在 trx_ids 列表内（说明 ReadView 生成时，trx_id 为 10 的事务还是活跃的），所以不符合可见性要求，根据 roll_pointer 跳到下一个版本。
3. 步骤三：下一个版本的列 name 的内容是 '李四'，该版本的 trx_id 值也为 10，也在 trx_ids 列表内，所以也不符合要求，继续跳到下一个版本。
4. 步骤四：下一个版本的列 name 的内容是 '张三'，该版本的 trx_id 值为 8，小于 ReadView 中的 up_limit_id 值 10，所以这个版本是符合要求的，最后，返回给用户的版本就是这条列 name 为 '张三' 的记录。

之后，把 事务 id 为 10 的事务提交一下：

```mysql
# Transaction 10
BEGIN;

UPDATE student SET name = "李四" WHERE id = 1;
UPDATE student SET name = "王五" WHERE id = 1;

COMMIT;
```

然后再到事务 id 为 20 的事务中，更新一下表 student 中 id 为 1 的记录：

```mysql
# Transaction 20
BEGIN;

# 更新了一些别的表的记录
...
UPDATE student SET name = "钱七" WHERE id = 1;
UPDATE student SET name = "宋八" WHERE id = 1;
```

此刻，表 student 中 id 为 1 的记录的版本链就长这样：

<img src="mysql-advanced/image-20240721103731212.png" alt="image-20240721103731212" style="zoom:67%;" />

然后，再到刚才使用 READ COMMITTED 隔离级别的事务中继续查找这个 id 为 1 的记录，如下：

```mysql
# 使用 READ COMMITTED 隔离级别的事务
BEGIN;

# SELECT1 操作，此时，Transaction 10 和 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值为'张三'

# SELECT2 操作，此时，Transaction 10 提交，Transaction 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值为'王五'
```

这个 SELECT2 的执行过程如下:

1. 步骤一：在执行 SELECT 语句时会又会单独生成一个 ReadView，该 ReadView 的 trx_ids 列表的内容就是 [20]，up_limit_id 为 20，low_limit_id 为 21，creator_trx_id 为 0。
2. 步骤二：从 undo log 版本链中挑选可见的记录，从图中看出，最新版本的列 name 的内容是 '宋八'，该版本的 trx_id 值为20，在 trx_ids 列表内，所以不符合可见性要求，根据 roll_pointer 跳到下一个版本。
3. 步骤三：下一个版本的列 name 的内容是 '钱七'，该版本的 trx_id 值为 20，也在 trx_ids 列表内，所以也不符合要求，继续跳到下一个版本。
4. 步骤四：下一个版本的列 name 的内容是 '王五'，该版本的 trx_id 值为 10，小于 ReadView 中的 up_limit_id 值 20，所以这个版本是符合要求的，最后，返回给用户的版本就是这条列 name 为 '王五' 的记录。

以此类推，如果之后事务 id 为 20 的记录也提交了，再次在使用 READ COMMITED 隔离级别的事务中，查询表 student 中 id 值为 1 的记录时，得到的结果就是 '宋八' 了，具体流程我们就不分析了。

> **强调：** 使用 READ COMMITTED 隔离级别的事务，在每次查询开始时，都会生成一个独立的 ReadView。

#### REPEATABLE READ 隔离级别下

**`REPEATABLE READ：只会在第一次执行查询语句时生成一个 ReadView，之后的查询就不会重复生成了，而是复用这个 ReadView。`**

比如，系统里有两个事务 id 分别为 10、20 的事务在执行：

```mysql
# Transaction 10
BEGIN;
UPDATE student SET name = "李四" WHERE id = 1;
UPDATE student SET name = "王五" WHERE id = 1;

# Transaction 20
BEGIN;
# 更新了一些别的表的记录
...
```

此刻，表 student 中 id 为 1 的记录得到的版本链表如下所示：

<img src="mysql-advanced/image-20240721110043183.png" alt="image-20240721110043183" style="zoom:67%;" />

假设现在有一个使用 REPEATABLE READ 隔离级别的事务开始执行：

```mysql
# 使用 REPEATABLE READ 隔离级别的事务
BEGIN;

# SELECT1 操作，此时，Transaction 10 和 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值为'张三'
```

这个 SELECT1 的执行过程如下：

1. 步骤一：在执行 SELECT 语句时会先生成一个 ReadView，ReadView 的 trx_ids 列表的内容就是 [10, 20]，up_limit_id 为 10，low_limit_id 为 21，creator_trx_id 为 0。
2. 步骤二：然后从 undo log 版本链中挑选可见的记录，从图中看出，最新版本的列 name 的内容是 '王五'，该版本的 trx_id 值为 10，在 trx_ids 列表内，所以不符合可见性要求，根据 roll_pointer 跳到下一个版本。
3. 步骤三：下一个版本的列 name 的内容是 '李四'，该版本的 trx_id 值也为 10，也在 trx_ids 列表内，所以也不符合要求，继续跳到下一个版本。
4. 步骤四：下一个版本的列 name 的内容是 '张三'，该版本的 trx_id 值为 8，小于 ReadView 中的 up_limit_id 值10，所以这个版本是符合要求的，最后，返回给用户的版本就是这条列 name 为 '张三 ' 的记录。

之后，我们把事务 id 为 10 的事务提交一下，就像这样：

```mysql
# Transaction 10
BEGIN;

UPDATE student SET name = "李四" WHERE id = 1;
UPDATE student SET name = "王五" WHERE id = 1;

COMMIT;
```

然后，再到事务 id 为 20 的事务中更新一下表 student 中 id 为 1 的记录：

```mysql
# Transaction 20
BEGIN;

# 更新了一些别的表的记录
...
UPDATE student SET name = "钱七" WHERE id = 1;
UPDATE student SET name = "宋八" WHERE id = 1;
```

此刻，表 student 中 id 为 1 的记录的版本链长这样：

<img src="mysql-advanced/image-20240721110539834.png" alt="image-20240721110539834" style="zoom:67%;" />

然后，再到刚才使用 REPEATABLE READ 隔离级别的事务中继续查找这个id 为 1 的记录，如下：

```mysql
# 使用 REPEATABLE READ 隔离级别的事务
BEGIN;

# SELECT1 操作，此时，Transaction 10 和 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值为'张三'

# SELECT2 操作，此时，Transaction 10 提交，Transaction 20 未提交
SELECT * FROM student WHERE id = 1; # 得到的列 name 的值仍为'张三'
```

SELECT2 的执行过程如下:

1. 步骤一：因为当前事务的隔离级别为 REPEATABLE READ，而之前在执行 SELECT1 时已经生成过 ReadView 了，所以此时直接复用之前的 ReadView，之前的 ReadView 的 trx_ids 列表的内容就是 [10, 20]，up_limit_id 为 10，low_limit_id 为 21，creator_trx_id 为 0。
2. 步骤二：然后从 undo log 版本链中挑选可见的记录，从图中可以看出，最新版本的列 name 的内容是 '宋八'，该版本的 trx_id 值为 20，在 trx_ids 列表内，所以不符合可见性要求，根据 roll_pointer 跳到下一个版本。
3. 步骤三：下一个版本的列 name 的内容是 '钱七'，该版本的 trx_id 值为 20，也在 trx_ids 列表内，所以也不符合要求，继续跳到下一个版本。
4. 步骤四：下一个版本的列 name 的内容是 '王五'，该版本的 trx_id 值为 10，而 trx_ids 列表中是包含值为 10 的事务 id 的，所以该版本也不符合要求。同理，下一个列 name 的内容是 '李四' 的版本也不符合要求，继续跳到下一个版本。
5. 步骤五：下一个版本的列 name 的内容是 '张三'，该版本的 trx_id 值为 8，小于 ReadView 中的 up_limit_id 值 10，所以这个版本是符合要求的，最后，返回给用户的版本就是这条列 name 为 '张三' 的记录。

两次 SELECT 查询得到的结果是重复的，记录的列 name 值都是 '张三'，这就是可重复读的含义。如果我们之后再把事务 id 为 20 的记录提交了，然后再到刚才使用 REPEATABLE READ 隔离级别的事务中，继续查找这个 id 为 1 的记录，得到的结果还是 '张三'，具体执行过程大家可以自己分析一下。

#### 如何解决幻读

接下来说明 InnoDB 是如何解决幻读的。

假设现在表 student 中只有一条数据，数据内容中，主键 id = 1，隐藏的 trx_id = 10，它的 undo log 如下图所示：

<img src="mysql-advanced/image-20240721120852189.png" alt="image-20240721120852189" style="zoom:33%;" />

假设现在有事务 A 和事务 B 并发执行，事务 A 的事务 id 为 20，事务 B 的事务 id 为 30。

步骤一：事务 A 开始第一次查询数据，查询的 SQL 语句如下。

```mysql
SELECT * FROM student WHERE id >= 1;
```

在开始查询之前，MySQL 会为事务 A 产生一个 ReadView，此时 ReadView 的内容如下：trx_ids = [20, 30]，up_limit_id = 20，low_limit_id = 31，creator_trx_id = 20。

由于此时表 student 中只有一条数据，且符合 WHERE id >= 1 条件，因此会查询出来。然后根据 ReadView机制，发现该行数据的 trx_id = 10，小于事务 A 的 ReadView 里 up_limit_id，这表示这条数据是事务 A 开启之前，其他事务就已经提交了的数据，因此事务 A 可以读取到。

结论：事务 A 的第一次查询，能读取到一条数据，id = 1。

步骤二：接着事务 B，往表 student 中新插入两条数据，并提交事务。

```mysql
INSERT INTO student(id, name) VALUES(2, '李四');
INSERT INTO student(id, name) VALUES(3, '王五');
```

此时，表 student 中就有三条数据了，对应的 undo log 如下图所示：

<img src="mysql-advanced/image-20240721121549931.png" alt="image-20240721121549931" style="zoom:50%;" />

步骤三：接着事务 A 开启第二次查询，根据可重复读隔离级别的规则，此时事务 A 并不会再重新生成 ReadView。此时表 student 中的 3 条数据都满足 WHERE id >= 1 的条件，因此会先查出来。然后根据 ReadView 机制，判断每条数据是不是都可以被事务 A 看到。

1. 首先 id = 1 的这条数据，前面已经说过了，可以被事务 A 看到。
2. 然后是 id = 2 的数据，它的 trx_id = 30，此时事务 A 发现，这个值处于 up_limit_id 和 low_limit_id 之间，因此还需要再判断 30 是否处于 trx_ids 数组内。由于事务 A 的 trx_ids = [20, 30]，因此在数组内，这表示 id = 2 的这条数据是与事务 A 在同一时刻启动的其他事务提交的，所以这条数据不能让事务 A 看到。
3. 同理，id = 3 的这条数据，trx_id 也为 30，因此也不能被事务 A 看见。

如下图所示：

<img src="mysql-advanced/image-20240721122343548.png" alt="image-20240721122343548" style="zoom:50%;" />

结论：最终事务 A 的第二次查询，只能查询出 id = 1 的这条数据，这和事务 A 的第一次查询的结果是一样的，因此没有出现幻读现象，所以说在 MySQL 的可重复读隔离级别下，不存在幻读问题。

### 总结

这里介绍了 MVCC 在 READ COMMITTD、REPEATABLE READ 这两种隔离级别的事务，在执行快照读操作时访问记录的版本链的过程。这样使不同事务的读-写、写-读操作并发执行，从而提升系统性能

核心点在于 ReadView 的原理，READ COMMITTD、REPEATABLE READ 这两个隔离级别的一个很大不同就是生成 ReadView 的时机不同：

- READ COMMITTD 在每一次进行普通 SELECT 操作前，都会生成一个ReadView。
- REPEATABLE READ 只在第一次进行普通 SELECT 操作前生成一个 ReadView，之后的查询操作都重复使用这个 ReadView。

>说明：之前说执行 DELETE 语句或者更新主键的 UPDATE 语句，并不会立即把对应的记录完全从页面中删除，而是执行一个所谓的`delete mark`操作（标记 0 -> 1），相当于只是对记录打上了一个删除标志位，这主要就是为 MVCC 服务的。另外后面回滚也可能用到这个 delete mark。

通过 MVCC 可以解决：

- `读写之间阻塞的问题`：通过 MVCC 可以让读写互相不阻塞，即读不阻塞写，写不阻塞读，这样就可以提升事务并发处理能力。
- `降低了死锁的概率`：这是因为 MVCC 采用了乐观锁的方式，读取数据时并不需要加锁，对于写操作，也只锁定必要的行。
- `解决快照读的问题`：当查询数据库在某个时间点的快照时，只能看到这个时间点之前事务提交更新的结果，而不能看到这个时间点之后事务提交的更新结果。

## 其他数据库日志

`// TODO`

## 主从复制

`// TODO`

## 数据库备份与恢复

`// TODO`

## 本文参考

https://www.bilibili.com/video/BV1iq4y1u7vj

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。