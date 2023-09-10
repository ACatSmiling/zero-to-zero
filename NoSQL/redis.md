*date: 2022-05-13*

## 概述

Redis 官网：https://redis.io/

GitHub：https://github.com/redis/redis

在线测试：https://try.redis.io/

Redis，Remote Dictionary Server，即远程字典服务。Redis 是完全开源的，使用 ANSIC 语言编写，遵守 BSD 协议，是一个高性能的 key-value 数据库，提供了丰富的数据结构，例如 String、Hash、List、Set、SortedSet 等等。Redis 数据是存在内存中的，同时 Redis 支持事务、持久化、LUA 脚本、发布/订阅、缓存淘汰、流技术等多种功能特性，提供了主从模式、Redis Sentinel 和 Redis Cluster 集群架构方案。

**主流功能与应用：**

<img src="redis/image-20230829174643247.png" alt="image-20230829174643247" style="zoom: 67%;" />

- 分布式缓存，比如，与 MySQL 数据库结合使用。

  <img src="redis/image-20230829174902752.png" alt="image-20230829174902752" style="zoom:50%;" />

  - Redis 是 key-value 数据库（NoSQL 一种），MySQL 是关系数据库。
  - Redis 数据操作主要在内存，而 MySQL 主要存储在磁盘。
  - Redis 在某些场景使用中要明显优于 MySQL，比如计数器、排行榜等方面。
  - Redis 通常用于一些特定场景，需要与 MySQL 一起配合使用。
  - Redis 与 MySQL 并不是相互替换和竞争关系，而是共用和配合使用。

- 内存存储和持久化（RDB + AOF）。

  - Redis 支持异步将内存中的数据写到硬盘上，同时不影响服务运行。

- 高可用架构搭配：单机、主从、哨兵、集群。

- 缓存穿透、击穿和雪崩。

- 分布式锁。

- 队列。

  - Redis 提供 List 和 Set 操作，这使得 Redis 能作为一个很好的消息队列平台来使用。我们常通过 Redis 的队列功能做购买限制。比如到节假日或者推广期间，进行一些活动。对用户购买行为进行限制，限制今天只能购买几次商品或者一段时间内只能购买一次，也比较适合适用。

- 排行榜、点赞等。

  - 在互联网应用中，有各种各样的排行榜，Redis 提供的 ZSet 数据类型能够快速实现这些复杂的排行榜。比如电商网站的月度销量排行榜、社交 APP 的礼物排行榜、小程序的投票排行榜等等。还有小说网站对小说进行排名，根据排名，将排名靠前的小说推荐给用户。

优点：

- 性能极高。Redis 读的速度是 110000 次/秒，写的速度是 81000 次/秒。
- 数据类型丰富。Redis 不仅支持简单的 key-value 类型的数据，同时还提供 List、Set、ZSet、Hash 等数据结果的存储。
- 支持数据的持久化。Redis 可以将内存中的数据写入磁盘中，重启的时候可以再次加载进行使用。
- 支持数据的备份。即 master-slave 模式的数据备份。

总结：

<img src="redis/image-20230830123308634.png" alt="image-20230830123308634" style="zoom:67%;" />

## Docker 安装

docker-compose.yaml：

```yaml
redis:
    image: redis:7.0.11
    container_name: redis
    ports:
      - 6379:6379
    volumes:
      - ./redis/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./redis/data:/data
    # 挂载redis.conf的话，需要指定启动命令中的配置文件路径
    command: redis-server /usr/local/etc/redis/redis.conf
    networks:
      - apps
    restart: on-failure:3
```

redis.conf：

```ini
port 6379
requirepass 123456
protected-mode no
daemonize no
appendonly yes
aof-use-rdb-preamble yes
```

- redis.conf 需要在本地目录预先创建。

> 各版本源码下载地址：https://download.redis.io/releases/

## Redis 版本迭代演化

几个里程碑式的重要版本：

<img src="redis/image-20230831103659742.png" alt="image-20230831103659742" style="zoom: 50%;" />

- 5.0 版本是直接升级到 6.0 版本的，对于这个激进的升级，Redis 之父 antirez 表现得很有信心和兴奋，所以第一时间发文来阐述 6.0 的一些重大功能 "Redis 6.0.0 GA is out!"。
- 2022 年 4 月 27 日，Redis 正式发布了 7.0 更新，其实早在 2022 年 1 月 31 日，Redis 已经预发布了 7.0rc-1，经过社区的考验后，确认没重大 Bug 才正式发布。

命名规则：

- 版本号第二位如果是奇数，则为非稳定版本，如 2.7、2.9、3.1。
- 版本号第二位如果是偶数，则为稳定版本，如 2.6、2.8、3.0、3.2。
- 当前奇数版本就是下一个稳定版本的开发版本，如 2.9 版本是 3.0 版本的开发版本。

## Redis 7.0 新特性

Redis 7.0rc-1 地址：https://github.com/redis/redis/releases?expanded=true&page=2&q=7.0.rc-1

Redis 7.0 大体和之前的 Redis 版本保持一致和稳定，主要是自身底层性能和资源利用率上的优化和提高，如果生产上系统稳定，不用着急升级到最新的 Redis 7.0 版本。

| 特性                               | 说明                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| 多 AOF 文件支持                    | Redis 7.0 版本中一个比较大的变化就是 AOF 文件由一个变成了多个，主要分为两种类型：基本文件（base files）、增量文件（incr files）。这些文件名称是复数形式，说明每一类文件不仅仅只有一个。在此之外还引入了一个清单文件（manifest）用于跟踪文件以及文件的创建和应用顺序（恢复）。 |
| config 命令增强                    | 对于 Config Set 和 Get 命令，支持在一次调用过程中传递多个配置参数。例如，现在可以在执行一次 Config Set 命令中更改多个参数：`config set maxmemory 10000001 maxmemory-clients 50% port 6399`。 |
| 限制客户端内存使用 Client-eviction | 一旦 Redis 连接较多，再加上每个连接的内存占用都比较大的时候， Redis 总连接内存占用可能会达到 maxmemory 的上限，可以增加允许限制所有客户端的总内存使用量配置项，redis.config 中对应的两种配置形式：指定内存大小（`maxmemory-clients 1g`）、基于 maxmemory 的百分比（`maxmemory-clients 10%`）。 |
| listpack 紧凑列表调整              | listpack 是用来替代 ziplist 的新数据结构，在 Redis 7.0 版本已经没有 ziplist 的配置了（Redis 6.0 版本仅部分数据类型作为过渡阶段在使用），listpack 已经替换了 ziplist 类似 hash-max-ziplist-entries 的配置。 |
| 访问安全性增强 ACLV2               | 在 redis.conf 配置文件中，protected-mode 默认为 yes，只有当你希望你的客户端在没有授权的情况下可以连接到 Redis Server 的时候，可以将 protected-mode 设置为 no。 |
| Redis Functions                    | Redis 函数，一种新的通过服务端脚本扩展 Redis 的方式，函数与数据本身一起存储。简言之，Redis 自己要去抢夺 Lua 脚本的饭碗。 |
| RDB 保存时间调整                   | 将持久化文件 RDB 的保存规则发生了改变，尤其是时间记录频度变化。 |
| 命令新增和变动                     | ZSet（有序集合）增加 ZMPOP、BZMPOP、ZINTERCARD 等命令，Set（集合）增加 SINTERCARD 命令，List（列表）增加 LMPOP、BLMPOP，从提供的键名列表中的第一个非空列表键中弹出一个或多个元素。 |
| 性能资源利用率、安全、等改进       | 自身底层部分优化改动，Redis 核心在许多方面进行了重构和改进。<br />主动碎片整理 V2：增强版主动碎片整理，配合 Jemalloc 版本更新，更快更智能，延时更低。<br />HyperLogLog 改进：在 Redis 5.0 中，HyperLogLog 算法得到改进，优化了计数统计时的内存使用效率，Redis 7.0 更加优秀更好的内存统计报告。 |

## Redis 数据类型

<img src="redis/image-20230831165327770.png" alt="image-20230831165327770" style="zoom:50%;" />

> Redis 的数据类型，指的是 Value 的类型，Key 的类型都是字符串。

### 字符串（String）

String 是 Redis **最基本的数据类型**，一个 key 对应一个 value，Redis 中一个字符串 value 最多可以是 512 MB。

String 类型是二进制安全的，意思是 Redis 的 String 可以包含任何数据，比如 jpg 图片或者序列化的对象。

### 列表（List）

<img src="redis/image-20230908163119790.png" alt="image-20230908163119790" style="zoom:50%;" />

Redis 列表是简单的字符串列表，**按照插入顺序排序**。也可以添加一个元素到列表的头部（左边）或者尾部（右边），它的底层实际是个**双端链表，对两端的操作性能很高，通过索引下标的操作中间的节点性能会较差**，最多可以包含 $$2^{32} - 1$$ 个元素（4294967295，即每个列表超过 40 亿个元素）。

### 哈希表（Hash）

Redis Hash 是一个 String 类型的 field（字段）和 value（值）的映射表，Hash 特别适合用于存储对象。Redis 中每个 Hash 可以存储 $$2^{32} - 1$$ 个键值对。

### 集合（Set）

Redis Set 是 String 类型的**无序集合**。集合成员是唯一的，这就意味着集合中不能出现重复的数据，集合对象的编码可以是 intset 或者 hashtable。

- Redis Set 是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。

- Redis 中每个 Set 可以存储 $$2^{32} - 1$$ 个键值对。

### 有序集合（ZSet）

Redis ZSet，也叫 Sorted Set，即**有序集合**。Redis ZSet 和 Set 一样，也是 String 类型元素的集合，且不允许重复。不同的是每个元素都会关联一个 double 类型的分数（score），Redis 正是通过分数来为集合中的成员进行从小到大的排序。

- Redis ZSet 的成员是唯一的，但分数（score）却可以重复。

- Redis ZSet 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。 
- Redis 中每个 ZSet 可以存储 $$2^{32} - 1$$ 个键值对。

### 地理空间（GEO）

Redis GEO 主要用于存储地理位置信息，并对存储的信息进行操作，包括：

- 添加地理位置的坐标。

- 获取地理位置的坐标。

- 计算两个位置之间的距离。

- 根据用户给定的经纬度坐标，来获取指定范围内的地理位置集合。

### 基数统计（HyperLogLog）

HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是：在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定且是很小的。

在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 $$2^{64}$$ 个不同元素的基数，这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

### 位图（Bitmap）

<img src="redis/image-20230831171238312.png" alt="image-20230831171238312" style="zoom: 67%;" />

- 由 0 和 1 状态表现的二进制位的 bit 数组。

### 位域（Bitfield）

通过 Bitfield 命令可以一次性操作多个比特位域（指的是连续的多个比特位），它会执行一系列操作并返回一个响应数组，这个数组中的元素对应参数列表中的相应操作的执行结果。说白了就是通过 Bitfield 命令，我们可以一次性对多个比特位域进行操作。

### 流（Stream）

Redis Stream 是 Redis 5.0 版本新增加的数据结构。

Redis Stream 主要用于消息队列（MQ，Message Queue），Redis 本身是有一个 Redis 发布订阅来实现消息队列的功能，但它有个缺点就是消息无法持久化，如果出现网络断开、Redis 宕机等，消息就会被丢弃。简单来说发布订阅可以分发消息，但无法记录历史消息。而 Redis Stream 提供了消息的持久化和主备复制功能，可以让任何客户端访问任何时刻的数据，并且能记住每一个客户端的访问位置，还能保证消息不丢失。 

## Redis 命令

官网：https://redis.io/commands/

中文：http://redis.cn/commands.html

参考：http://doc.redisfans.com/

### Client 命令

- 连接客户端：`redis-cli [-h hostname] [-p port] [-a password]`。

  ```bash
  $ redis-cli -h 127.0.0.1 -p 6379 -a "mypass"
  127.0.0.1:6379>
  ```

>在 redis-cli 后面加上 --raw，可以避免中文乱码。

### Server 命令

- 验证密码：`AUTH <password>`。密码匹配时返回 OK，否则返回一个错误。

  ```bash
  127.0.0.1:6379> AUTH password
  (error) ERR Client sent AUTH, but no password is set
  127.0.0.1:6379> CONFIG SET requirepass "mypass"
  OK
  127.0.0.1:6379> AUTH mypass
  Ok
  ```

- 服务器统计信息：`INFO [section]`。返回关于 Redis 服务器的各种信息和统计数值。通过给定可选的参数 section，可以让命令只返回某一部分的信息。

  ```bash
  127.0.0.1:6379> INFO
  # Server
  redis_version:7.0.11								# Redis服务器版本
  redis_git_sha1:00000000
  redis_git_dirty:0
  redis_build_id:c87ff843cceeb98e
  redis_mode:standalone
  os:Linux 3.10.0-1160.90.1.el7.x86_64 x86_64			# Redis服务器的宿主操作系统
  arch_bits:64										# 架构(32或64位)
  monotonic_clock:POSIX clock_gettime
  multiplexing_api:epoll
  atomicvar_api:c11-builtin
  gcc_version:10.2.1
  process_id:1										# 服务器进程的PID
  process_supervised:no
  run_id:8de30f0d5af0e8f0f7761f0cd0636c03885baa7d		# Redis服务器的随机标识符(用于Sentinel和集群)
  tcp_port:6379										# TCP/IP监听端口
  server_time_usec:1693962718865941
  uptime_in_seconds:5689318							# 自Redis服务器启动以来, 经过的秒数
  uptime_in_days:65									# 自Redis服务器启动以来, 经过的天数
  hz:10
  configured_hz:10
  lru_clock:16241118
  executable:/data/redis-server
  config_file:/usr/local/etc/redis/redis.conf
  io_threads_active:0
  
  # Clients
  connected_clients:3									# 已连接客户端的数量(不包括通过从属服务器连接的客户端)
  cluster_connections:0
  maxclients:10000
  client_recent_max_input_buffer:8
  client_recent_max_output_buffer:0
  blocked_clients:0									# 正在等待阻塞命令(BLPOP、BRPOP、BRPOPLPUSH)的客户端的数量
  tracking_clients:0
  clients_in_timeout_table:0
  
  # Memory
  used_memory:1292232									# 由Redis分配器分配的内存总量, 以字节(byte)为单位
  used_memory_human:1.23M								# 以人类可读的格式返回Redis分配的内存总量
  used_memory_rss:10547200							# 从操作系统的角度, 返回Redis已分配的内存总量(俗称常驻集大小), 这个值和top、ps等命令的输出一致
  used_memory_rss_human:10.06M
  used_memory_peak:1402552							# Redis的内存消耗峰值, 以字节(byte)为单位
  used_memory_peak_human:1.34M						# 以人类可读的格式返回Redis的内存消耗峰值
  used_memory_peak_perc:92.13%
  used_memory_overhead:868304
  used_memory_startup:862368
  used_memory_dataset:423928
  used_memory_dataset_perc:98.62%
  allocator_allocated:1509328
  allocator_active:1904640
  allocator_resident:4808704
  total_system_memory:8369803264
  total_system_memory_human:7.79G
  used_memory_lua:31744								# Lua引擎所使用的内存大小, 以字节(byte)为单位
  used_memory_vm_eval:31744
  used_memory_lua_human:31.00K
  used_memory_scripts_eval:0
  number_of_cached_scripts:0
  number_of_functions:0
  number_of_libraries:0
  used_memory_vm_functions:32768
  used_memory_vm_total:64512
  used_memory_vm_total_human:63.00K
  used_memory_functions:184
  used_memory_scripts:184
  used_memory_scripts_human:184B
  maxmemory:0
  maxmemory_human:0B
  maxmemory_policy:noeviction
  allocator_frag_ratio:1.26
  allocator_frag_bytes:395312
  allocator_rss_ratio:2.52
  allocator_rss_bytes:2904064
  rss_overhead_ratio:2.19
  rss_overhead_bytes:5738496
  mem_fragmentation_ratio:8.31
  mem_fragmentation_bytes:9277264
  mem_not_counted_for_evict:8
  mem_replication_backlog:0
  mem_total_replication_buffers:0
  mem_clients_slaves:0
  mem_clients_normal:5400
  mem_cluster_links:0
  mem_aof_buffer:8
  mem_allocator:jemalloc-5.2.1
  active_defrag_running:0
  lazyfree_pending_objects:0
  lazyfreed_objects:0
  
  # Persistence
  loading:0
  async_loading:0
  current_cow_peak:0
  current_cow_size:0
  current_cow_size_age:0
  current_fork_perc:0.00
  current_save_keys_processed:0
  current_save_keys_total:0
  rdb_changes_since_last_save:0
  rdb_bgsave_in_progress:0
  rdb_last_save_time:1693910292
  rdb_last_bgsave_status:ok
  rdb_last_bgsave_time_sec:0
  rdb_current_bgsave_time_sec:-1
  rdb_saves:19
  rdb_last_cow_size:4923392
  rdb_last_load_keys_expired:0
  rdb_last_load_keys_loaded:0
  aof_enabled:1
  aof_rewrite_in_progress:0
  aof_rewrite_scheduled:0
  aof_last_rewrite_time_sec:-1
  aof_current_rewrite_time_sec:-1
  aof_last_bgrewrite_status:ok
  aof_rewrites:0
  aof_rewrites_consecutive_failures:0
  aof_last_write_status:ok
  aof_last_cow_size:0
  module_fork_in_progress:0
  module_fork_last_cow_size:0
  aof_current_size:56673
  aof_base_size:89
  aof_pending_rewrite:0
  aof_buffer_length:0
  aof_pending_bio_fsync:0
  aof_delayed_fsync:0
  
  # Stats
  total_connections_received:53
  total_commands_processed:4992
  instantaneous_ops_per_sec:0
  total_net_input_bytes:196447
  total_net_output_bytes:662900
  total_net_repl_input_bytes:0
  total_net_repl_output_bytes:0
  instantaneous_input_kbps:0.00
  instantaneous_output_kbps:0.00
  instantaneous_input_repl_kbps:0.00
  instantaneous_output_repl_kbps:0.00
  rejected_connections:0
  sync_full:0
  sync_partial_ok:0
  sync_partial_err:0
  expired_keys:0
  expired_stale_perc:0.00
  expired_time_cap_reached_count:0
  expire_cycle_cpu_milliseconds:241079
  evicted_keys:0
  evicted_clients:0
  total_eviction_exceeded_time:0
  current_eviction_exceeded_time:0
  keyspace_hits:4550
  keyspace_misses:304
  pubsub_channels:0
  pubsub_patterns:0
  pubsubshard_channels:0
  latest_fork_usec:2431
  total_forks:19
  migrate_cached_sockets:0
  slave_expires_tracked_keys:0
  active_defrag_hits:0
  active_defrag_misses:0
  active_defrag_key_hits:0
  active_defrag_key_misses:0
  total_active_defrag_time:0
  current_active_defrag_time:0
  tracking_total_keys:0
  tracking_total_items:0
  tracking_total_prefixes:0
  unexpected_error_replies:0
  total_error_replies:24
  dump_payload_sanitizations:0
  total_reads_processed:5068
  total_writes_processed:5017
  io_threaded_reads_processed:0
  io_threaded_writes_processed:0
  reply_buffer_shrinks:77
  reply_buffer_expands:24
  
  # Replication
  role:master
  connected_slaves:0
  master_failover_state:no-failover
  master_replid:942eb2629cc1a39b0c740df9929c526bcc1d57f7
  master_replid2:0000000000000000000000000000000000000000
  master_repl_offset:0
  second_repl_offset:-1
  repl_backlog_active:0
  repl_backlog_size:1048576
  repl_backlog_first_byte_offset:0
  repl_backlog_histlen:0
  
  # CPU
  used_cpu_sys:11484.273541
  used_cpu_user:11837.988764
  used_cpu_sys_children:0.238952
  used_cpu_user_children:0.046146
  used_cpu_sys_main_thread:11484.226705
  used_cpu_user_main_thread:11837.940036
  
  # Modules
  
  # Errorstats
  errorstat_ERR:count=6
  errorstat_NOAUTH:count=16
  errorstat_WRONGPASS:count=2
  
  # Cluster
  cluster_enabled:0
  
  # Keyspace
  db0:keys=3,expires=0,avg_ttl=0
  db4:keys=4,expires=0,avg_ttl=0
  ```

- 打印字符串：`ECHO <message>`。返回字符串本身。

  ```bash
  127.0.0.1:6379> ECHO "Hello World"
  "Hello World"
  ```

- 查看服务是否运行：`PING`。如果连接正常就返回一个 PONG，否则返回一个连接错误。

  ```bash
  # 客户端和服务器连接正常
  127.0.0.1:6379> PING
  PONG
  
  # 客户端和服务器连接不正常(网络不正常或服务器未能正常运行)
  127.0.0.1:6379> PING
  Could not connect to Redis at 127.0.0.1:6379: Connection refused
  ```

- 返回当前服务器时间：`TIME`。返回一个包含两个字符串的列表，第一个字符串是当前时间（以 UNIX 时间戳格式表示），第二个字符串是当前这一秒钟已经逝去的微秒数。

  ```bash
  127.0.0.1:6379> TIME
  1) "1693965189"
  2) "195945"
  ```

- 切换到指定数据库：`SELECT <index>`。总是返回 OK。

  ```bash
  # 默认使用0号数据库
  127.0.0.1:6379> SET db_number 0
  OK
  
  # 使用 1 号数据库
  127.0.0.1:6379> SELECT 1
  OK
  
  # 已经切换到 1 号数据库，注意 Redis 现在的命令提示符多了个 [1]
  127.0.0.1:6379[1]> GET db_number
  (nil)
  
  127.0.0.1:6379[1]> SET db_number 1
  OK
  
  127.0.0.1:6379[1]> GET db_number
  "1"
  
  # 再切换到3号数据库
  127.0.0.1:6379[1]> SELECT 3
  OK
  
  # 提示符从[1]改变成了[3]
  127.0.0.1:6379[3]>
  ```

- 清空当前数据库：`FLUSHDB`。总是返回 OK。

  ```bash
  # 清空前的key数量
  127.0.0.1:6379> DBSIZE
  (integer) 4
  
  127.0.0.1:6379> FLUSHDB
  OK
  
  # 清空后的key数量
  127.0.0.1:6379> DBSIZE
  (integer) 0
  ```

  - Redis 默认是 16 个库，通过 0 ~ 15 标识库名，默认使用 0 号库，可以通过 redis.conf 修改配置。

    <img src="redis/image-20230908115310519.png" alt="image-20230908115310519" style="zoom: 80%;" />

- 清空全部数据库：`FLUSHALL`。总是返回 OK。

  ```bash
  # 0号数据库的key数量
  127.0.0.1:6379> DBSIZE
  (integer) 9
  
  # 切换到1号数据库
  127.0.0.1:6379[1]> SELECT 1
  OK
  
  # 1号数据库的key数量
  127.0.0.1:6379[1]> DBSIZE
  (integer) 6
  
  # 清空所有数据库的所有key
  127.0.0.1:6379[1]> FLUSHALL
  OK
  
  # 不但1号数据库被清空了
  127.0.0.1:6379[1]> DBSIZE
  (integer) 0
  
  # 0号数据库(以及其他所有数据库)也一样
  127.0.0.1:6379> SELECT 0
  OK
  
  127.0.0.1:6379> DBSIZE
  (integer) 0
  ```

- 实时打印出 Redis 服务器接收到的命令：`MONITOR`。调试用。

  ```bash
  127.0.0.1:6379> MONITOR
  OK
  1693965563.441319 [0 127.0.0.1:46990] "auth" "(redacted)"
  1693965567.613074 [0 127.0.0.1:46990] "dbsize"
  1693965630.705873 [0 127.0.0.1:46992] "keys" "*"
  ```

- 关闭当前连接：`QUIT`。总是返回 OK。

  ```bash
  127.0.0.1:6379> QUIT
  OK
  ```

### Key 命令

- 查看当前库 key 的数量：`DBSIZE`。返回当前数据库的 key 的数量。

  ```bash
  127.0.0.1:6379> dbsize
  (integer) 1
  ```

- 查找所有符合给定模式（pattern）的 key：`KEYS <pattern>`。返回符合给定模式的 key 列表。

  ```bash
  # 获取所有的key
  127.0.0.1:6379> KEYS *
  1) "inspection_task-1115"
  
  # inspection开头的key
  127.0.0.1:6379> KEYS inspection*
  1) "inspection_task-1115"
  
  # bbcc开头的key
  127.0.0.1:6379> KEYS bbcc*
  (empty array)
  ```

- 判断某个 key 是否存在：`EXISTS <key_name>`。若 key 存在返回 1，否则返回 0。

  ```bash
  127.0.0.1:6379> EXISTS runoob-new-key
  (integer) 0
  ```

- 返回 key 所储存的值的类型：`TYPE <key_name>`。返回 key 的数据类型。

  ```bash
  # key不存在
  127.0.0.1:6379> TYPE aabb
  none
  
  # 字符串
  127.0.0.1:6379> SET weather "sunny"
  OK
  127.0.0.1:6379> TYPE weather
  string
  
  # 列表
  127.0.0.1:6379> LPUSH book_list "programming in scala"
  (integer) 1
  127.0.0.1:6379> TYPE book_list
  list
  
  # 集合
  127.0.0.1:6379> SADD pat "dog"
  (integer) 1
  127.0.0.1:6379> TYPE pat
  set
  ```

- 在 key 存在时删除 key：`DEL <key_name>`。返回被删除 key 的数量。

  ```bash
  127.0.0.1:6379> DEL key
  (integer) 1
  ```

- 以秒为单位，为给定 key 设置过期时间：`EXPIRE <key_name> <time_in_seconds>`。设置成功返回 1，当 key 不存在或者不能为 key 设置过期时间时返回 0。

  ```bash
  127.0.0.1:6379> EXPIRE runooobkey 60
  (integer) 1
  ```

- 以秒为单位，返回 key 的剩余过期时间：`TTL <key_name>`。当 key 不存在时，返回 -2，当 key 存在但没有设置剩余生存时间时，返回 -1，否则，以秒为单位，返回 key 的剩余生存时间。

  ```bash
  # 不存在的key
  127.0.0.1:6379> FLUSHDB
  OK
  127.0.0.1:6379> TTL key
  (integer) -2
  
  # key存在, 但没有设置剩余生存时间
  127.0.0.1:6379> SET key value
  OK
  127.0.0.1:6379> TTL key
  (integer) -1
  
  # 有剩余生存时间的key
  127.0.0.1:6379> EXPIRE key 10086
  (integer) 1
  127.0.0.1:6379> TTL key
  (integer) 10084
  ```

- 将当前数据库的 key 移动到给定的数据库 db 当中：`MOVE <key_name> <db_name>`。移动成功返回 1，失败则返回 0。

  ```bash
  # key存在于当前数据库
  127.0.0.1:6379> SELECT 0								# redis默认使用数据库0, 为了清晰起见, 这里再显式指定一次
  OK
  
  127.0.0.1:6379> SET song "secret base - Zone"
  OK
  
  127.0.0.1:6379> MOVE song 1								# 将song移动到数据库1
  (integer) 1
  
  127.0.0.1:6379> EXISTS song								# song已经被移走
  (integer) 0
  
  127.0.0.1:6379> SELECT 1								# 使用数据库1
  OK
  
  127.0.0.1:6379[1]> EXISTS song							# 证实 song被移到了数据库1
  (integer) 1
  
  
  # 当key不存在的时候
  127.0.0.1:6379[1]> EXISTS fake_key
  (integer) 0
  
  127.0.0.1:6379[1]> MOVE fake_key 0						# 试图从数据库1移动一个不存在的key到数据库0, 失败
  (integer) 0
  
  127.0.0.1:6379[1]> select 0								# 使用数据库0
  OK
  
  127.0.0.1:6379> EXISTS fake_key							# 证实fake_key不存在
  (integer) 0
  
  
  # 当源数据库和目标数据库有相同的key时
  127.0.0.1:6379> SELECT 0								# 使用数据库0
  OK
  127.0.0.1:6379> SET favorite_fruit "banana"
  OK
  
  127.0.0.1:6379> SELECT 1								# 使用数据库1
  OK
  127.0.0.1:6379[1]> SET favorite_fruit "apple"
  OK
  
  127.0.0.1:6379[1]> SELECT 0								# 使用数据库0, 并试图将favorite_fruit移动到数据库1
  OK
  
  127.0.0.1:6379> MOVE favorite_fruit 1					# 因为两个数据库有相同的key, MOVE失败
  (integer) 0
  
  127.0.0.1:6379> GET favorite_fruit						# 数据库0的favorite_fruit没变
  "banana"
  
  127.0.0.1:6379> SELECT 1
  OK
  
  127.0.0.1:6379[1]> GET favorite_fruit					# 数据库1的favorite_fruit也是
  "apple"
  ```

### Value 命令

帮助命令：`HELP @数据类型`。

```bash
127.0.0.1:6379> HELP @string

127.0.0.1:6379> HELP @list
```

#### String

- 设置指定 key 的值：`SET <key_name> <value>`。在设置操作成功完成后，返回 OK。

  ```bash
  127.0.0.1:6379> SET key "value"
  OK
  
  127.0.0.1:6379> GET key
  "value"
  ```

- 获取指定 key 的值：`GET <key_name>`。返回 key 的值，如果 key 不存在时，返回 nil。 如果 key 不是字符串类型，那么返回一个错误。

  ```bash
  # 对不存在的key或字符串类型key进行GET
  127.0.0.1:6379> GET db
  (nil)
  
  127.0.0.1:6379> SET db redis
  OK
  
  127.0.0.1:6379> GET db
  "redis"
  
  # 对不是字符串类型的key进行GET
  127.0.0.1:6379> DEL db
  (integer) 1
  
  127.0.0.1:6379> LPUSH db redis mongodb mysql
  (integer) 3
  
  127.0.0.1:6379> GET db
  (error) ERR Operation against a key holding the wrong kind of value
  ```

- 将值 value 关联到 key ，并将 key 的过期时间设为 seconds（以秒为单位）：`SETEX <key_name> <timeout> <value>`。在设置操作成功完成后，返回 OK。

  ```bash
  127.0.0.1:6379> SETEX mykey 60 redis
  OK
  127.0.0.1:6379> TTL mykey
  60
  127.0.0.1:6379> GET mykey
  "redis
  ```

- 同时设置一个或多个 key-value 对：` MSET <key_name1> <value1> [<key_name2> <value2>] .. [<key_nameN> <valueN>]`。总是返回 OK。

  ```bash
  127.0.0.1:6379> MSET key1 "Hello" key2 "World"
  OK
  127.0.0.1:6379> GET key1
  "Hello"
  127.0.0.1:6379> GET key2
  1) "World"
  ```

- 获取所有（一个或多个）给定 key 的值：`MGET <key_name1> [<key_name2>] .. [<key_nameN>]`。返回一个包含所有给定 key 的值的列表。

  ```bash
  127.0.0.1:6379> SET key1 "hello"
  OK
  127.0.0.1:6379> SET key2 "world"
  OK
  127.0.0.1:6379> MGET key1 key2 someOtherKey
  1) "Hello"
  2) "World"
  3) (nil)
  ```

- 将给定 key 的值设为 value，并返回 key 的旧值 old value：`GETSET <key_name> <value>`。返回给定 key 的旧值。当 key 没有旧值时，即 key 不存在时，返回 nil。当 key 存在，但不是字符串类型时，返回一个错误。

  ```bash
  127.0.0.1:6379> GETSET db mongodb	# 没有旧值, 返回nil
  (nil)
  
  127.0.0.1:6379> GET db
  "mongodb"
  
  127.0.0.1:6379> GETSET db redis		# 返回旧值mongodb
  "mongodb"
  
  127.0.0.1:6379> GET db
  "redis"
  ```

- 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始：`SETRANGE <key_name> OFFSET <value>`。返回被修改后的字符串长度。

  ```bash
  127.0.0.1:6379> SET key1 "Hello World"
  OK
  127.0.0.1:6379> SETRANGE key1 6 "Redis"
  (integer) 11
  127.0.0.1:6379> GET key1
  "Hello Redis"
  ```

- 返回 key 中字符串值的子字符：`GETRANGE <key_name> <start> <end>`。返回截取得到的子字符串。

  ```bash
  127.0.0.1:6379> SET mykey "This is my test key"
  OK
  127.0.0.1:6379> GETRANGE mykey 0 3
  "This"
  127.0.0.1:6379> GETRANGE mykey 0 -1
  "This is my test key"
  ```

- 返回 key 所储存的字符串值的长度：`STRLEN <key_name>`。返回字符串值的长度，当 key 不存在时，返回 0。

  ```bash
  # 获取字符串的长度
  127.0.0.1:6379> SET mykey "Hello World"
  OK
  
  127.0.0.1:6379> STRLEN mykey
  (integer) 11
  
  # 不存在的key长度为0
  127.0.0.1:6379> STRLEN nonexisting
  (integer) 0
  ```

- 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾：`APPEND <key_name> <new_value>`。返回追加指定值之后， key 中字符串的长度。如果 key 已经存在并且是一个字符串，APPEND 命令将 value 追加到 key 原来的值的末尾。如果 key 不存在，APPEND 就简单地将给定 key 设为 value，就像执行 SET key value 一样。

  ```bash
  # 对不存在的key执行APPEND
  127.0.0.1:6379> EXISTS myphone				# myphone不存在
  (integer) 0
  
  127.0.0.1:6379> APPEND myphone "nokia"		# 对不存在的key进行APPEND, 等同于SET myphone "nokia"
  (integer) 5
  
  # 对已存在的字符串进行APPEND
  127.0.0.1:6379> APPEND myphone " - 1110"	# 长度从5个字符增加到12个字符
  (integer) 12
  
  127.0.0.1:6379> GET myphone
  "nokia - 1110"
  ```

- 将 key 中储存的数字值增一：`INCR <key_name>`。返回执行 INCR 命令之后 key 的值。如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 INCR 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

  ```bash
  127.0.0.1:6379> SET page_view 20
  OK
  
  127.0.0.1:6379> INCR page_view
  (integer) 21
  
  127.0.0.1:6379> GET page_view    # 数字值在Redis中以字符串的形式保存
  "21"
  ```

- 将 key 所储存的值加上给定的增量值（increment）：`INCRBY <key_name> <increment>`。返回加上指定的增量值之后， key 的值。如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 INCRBY 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

  ```bash
  # key存在且是数字值
  127.0.0.1:6379> SET rank 50
  OK
  
  127.0.0.1:6379> INCRBY rank 20
  (integer) 70
  
  127.0.0.1:6379> GET rank
  "70"
  
  # key不存在时
  127.0.0.1:6379> EXISTS counter
  (integer) 0
  
  127.0.0.1:6379> INCRBY counter 30
  (integer) 30
  
  127.0.0.1:6379> GET counter
  "30"
  
  # key不是数字值时
  127.0.0.1:6379> SET book "long long ago..."
  OK
  
  127.0.0.1:6379> INCRBY book 200
  (error) ERR value is not an integer or out of range
  ```

- 将 key 中储存的数字值减一：`DECR <key_name>`。返回执行 INCR 命令之后 key 的值。如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 DECR 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

  ```bash
  # 对存在的数字值key进行DECR
  redis> SET failure_times 10
  OK
  
  redis> DECR failure_times
  (integer) 9
  
  # 对不存在的key值进行DECR
  redis> EXISTS count
  (integer) 0
  
  redis> DECR count
  (integer) -1
  
  # 对存在但不是数值的key进行DECR
  redis> SET company YOUR_CODE_SUCKS.LLC
  OK
  
  redis> DECR company
  (error) ERR value is not an integer or out of range
  ```

- 将 key 所储存的值减去给定的减量值（decrement）：`DECRBY <key_name> <decrement>`。返回加上指定的增量值之后， key 的值。如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 DECRBY 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

  ```bash
  # 对已存在的key进行DECRBY
  redis> SET count 100
  OK
  
  redis> DECRBY count 20
  (integer) 80
  
  # 对不存在的key进行DECRBY
  redis> EXISTS pages
  (integer) 0
  
  redis> DECRBY pages 10
  (integer) -10
  ```

#### List

- 将一个或多个值插入到列表头部：`LPUSH <key_name1> <value1> [<value2>] .. [<valueN>]`。返回列表的长度。

  ```bash
  127.0.0.1:6379> LPUSH list1 "foo"
  (integer) 1
  127.0.0.1:6379> LPUSH list1 "bar"
  (integer) 2
  127.0.0.1:6379> LRANGE list1 0 -1
  1) "bar"
  2) "foo"
  ```

- 将一个或多个值添加到列表尾部：`RPUSH <key_name1> <value1> [<value2>] .. [<valueN>]`。返回列表的长度。

  ```bash
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "foo"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "bar"
  (integer) 3
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "hello"
  2) "foo"
  3) "bar"
  ```

- 将一个值插入到已存在的列表头部：`LPUSHX <key_name1> <value1> [<value2>] .. [<valueN>]`。返回列表的长度，列表不存在时操作无效。

  ```bash
  127.0.0.1:6379> LPUSH list1 "foo"
  (integer) 1
  127.0.0.1:6379> LPUSHX list1 "bar"
  (integer) 2
  127.0.0.1:6379> LPUSHX list2 "bar"		# 列表不存在时操作无效
  (integer) 0
  127.0.0.1:6379> LRANGE list1 0 -1
  1) "bar"
  2) "foo"
  ```

- 将一个值添加到已存在的列表头部：`RPUSHX <key_name1> <value1> [<value2>] .. [<valueN>]`。返回列表的长度，列表不存在时操作无效。

  ```bash
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "foo"
  (integer) 2
  127.0.0.1:6379> RPUSHX mylist2 "bar"	# 列表不存在时操作无效
  (integer) 0
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "hello"
  2) "foo"
  ```

- 移出并获取列表的第一个元素：`LPOP <key_name>`。返回列表的第一个元素。当列表 key 不存在时，返回 nil。

  ```bash
  127.0.0.1:6379> RPUSH list1 "foo"
  (integer) 1
  127.0.0.1:6379> RPUSH list1 "bar"
  (integer) 2
  127.0.0.1:6379> LPOP list1
  "foo"
  ```

- 移出并获取列表的最后一个元素：`RPOP <key_name>`。返回列表的最后一个元素。当列表 key 不存在时，返回 nil。

  ```bash
  127.0.0.1:6379> RPUSH mylist "one"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "two"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "three"
  (integer) 3
  re127.0.0.1:6379dis> RPOP mylist
  "three"
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "one"
  2) "two"
  ```

- 获取列表的长度：`LLEN <key_name>`。返回列表的长度。如果列表 key 不存在，则 key 被解释为一个空列表，返回 0。 如果 key 不是列表类型，返回一个错误。

  ```bash
  127.0.0.1:6379> RPUSH list1 "foo"
  (integer) 1
  127.0.0.1:6379> RPUSH list1 "bar"
  (integer) 2
  127.0.0.1:6379> LLEN list1
  (integer) 2
  ```

- 获取列表指定范围内的元素：`LRANGE <key_name> <start> <end>`。返回一个列表，包含指定区间内的元素。其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

  ```bash
  127.0.0.1:6379> RPUSH mylist "one"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "two"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "three"
  (integer) 3
  127.0.0.1:6379> LRANGE mylist 0 0
  1) "one"
  127.0.0.1:6379> LRANGE mylist -3 2
  1) "one"
  2) "two"
  3) "three"
  127.0.0.1:6379> LRANGE mylist -100 100
  1) "one"
  2) "two"
  3) "three"
  127.0.0.1:6379> LRANGE mylist 5 10
  (empty list or set)
  ```

- 通过索引获取列表中的元素：`LINDEX <key_name> <index>`。返回列表中下标为指定索引值的元素，如果指定索引值不在列表的区间范围内，返回 nil。

  ```bash
  127.0.0.1:6379> LPUSH mylist "World"
  (integer) 1
  
  127.0.0.1:6379> LPUSH mylist "Hello"
  (integer) 2
  
  127.0.0.1:6379> LINDEX mylist 0
  "Hello"
  
  127.0.0.1:6379> LINDEX mylist -1
  "World"
  
  127.0.0.1:6379> LINDEX mylist 3		# index不在mylist的区间范围内
  (nil)
  ```

- 通过索引设置列表元素的值：`LSET <key_name> <index> <value>`。操作成功返回 ok，否则返回错误信息。当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。

  ```bash
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "foo"
  (integer) 3
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 4
  127.0.0.1:6379> LSET mylist 0 "bar"
  OK
  127.0.0.1:6379> LRANGE mylist 0 -1
  1: "bar"
  2) "hello"
  3) "foo"
  4) "hello"
  ```

- 在列表的元素前或者后插入元素：`LINSERT <key_name> BEFORE|AFTER <pivot> <value>`。如果命令执行成功，返回插入操作完成之后，列表的长度。如果没有找到指定元素 ，返回 -1。如果 key 不存在或为空列表，返回 0。当指定元素不存在于列表中时，不执行任何操作。当列表不存在时，被视为空列表，不执行任何操作。

  ```bash
  127.0.0.1:6379> RPUSH mylist "Hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "World"
  (integer) 2
  127.0.0.1:6379> LINSERT mylist BEFORE "World" "There"
  (integer) 3
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "Hello"
  2) "There"
  3) "World"
  ```

- 移除列表元素：`LREM <key_name> <count> <value>`。

  ```bash
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "foo"
  (integer) 3
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 4
  127.0.0.1:6379> LREM mylist -2 "hello"
  (integer) 2
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "hello"
  2) "foo"
  ```

  - count > 0：从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count。
  - count < 0：从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
  - count = 0：移除表中所有与 value 相等的值。

- 对一个列表进行修剪（trim），即让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除：`LTRIM <key_name> <start> <stop>`。

  ```bash
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 1
  127.0.0.1:6379> RPUSH mylist "hello"
  (integer) 2
  127.0.0.1:6379> RPUSH mylist "foo"
  (integer) 3
  127.0.0.1:6379> RPUSH mylist "bar"
  (integer) 4
  127.0.0.1:6379> LTRIM mylist 1 -1
  OK
  127.0.0.1:6379> LRANGE mylist 0 -1
  1) "hello"
  2) "foo"
  3) "bar"
  ```

#### Hash

- 将哈希表 key 中的字段 field 的值设为 value：`HSET <key_name> <field> <value>`。如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1。如果哈希表中域字段已经存在，且旧值已被新值覆盖，返回 0。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  OK
  127.0.0.1:6379> HGET myhash field1
  "foo"
  
  127.0.0.1:6379> HSET website google "www.g.cn"			# 设置一个新域
  (integer) 1
  
  127.0.0.1:6379> HSET website google "www.google.com"	# 覆盖一个旧域
  (integer) 0
  ```

- 获取存储在哈希表中指定字段的值：`HGET <key_name> <field>`。返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil。

  ```bash
  127.0.0.1:6379> HSET site redis redis.com
  1
  127.0.0.1:6379> HGET site redis
  "redis.com"
  127.0.0.1:6379> HGET site mysql
  (nil)
  ```

- 同时将多个 field-value（域-值）对设置到哈希表 key 中：`HMSET <key_name> <field1> <value1> [<field2> <value2>] .. [<fieldN> <valueN>]`。如果命令执行成功，返回 OK。此命令会覆盖哈希表中已存在的字段。如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。

  ```bash
  redis 127.0.0.1:6379> HMSET myhash field1 "Hello" field2 "World"
  OK
  redis 127.0.0.1:6379> HGET myhash field1
  "Hello"
  redis 127.0.0.1:6379> HGET myhash field2
  "World"
  ```

- 获取所有给定字段的值：`HMGET <key_name> <field1> [<field2>] .. [<fieldN>]`。返回一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HSET myhash field2 "bar"
  (integer) 1
  127.0.0.1:6379> HMGET myhash field1 field2 nofield
  1) "foo"
  2) "bar"
  3) (nil)
  ```

- 获取在哈希表中指定 key 的所有字段和值：`HGETALL <key_name>`。以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。在返回值里，紧跟每个字段名之后是字段的值，所以返回值的长度是哈希表大小的两倍。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "Hello"
  (integer) 1
  127.0.0.1:6379> HSET myhash field2 "World"
  (integer) 1
  127.0.0.1:6379> HGETALL myhash
  1) "field1"
  2) "Hello"
  3) "field2"
  4) "World"
  ```

- 查看哈希表 key 中，指定的字段是否存在：`HEXISTS <key_name> <field>`。如果哈希表含有给定字段，返回 1。如果哈希表不含有给定字段，或 key 不存在，返回 0。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HEXISTS myhash field1
  (integer) 1
  127.0.0.1:6379> HEXISTS myhash field2
  (integer) 0
  ```

- 删除一个或多个哈希表字段，不存在的字段将被忽略：`HDEL <key_name> <field1> [<field2>] .. [<fieldN>]`。返回被成功删除字段的数量，不包括被忽略的字段。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HDEL myhash field1
  (integer) 1
  127.0.0.1:6379> HDEL myhash field2
  (integer) 0
  ```

- 获取哈希表中字段的数量：`HLEN <key_name>`。返回哈希表中字段的数量，当 key 不存在时，返回 0。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HSET myhash field2 "bar"
  (integer) 1
  127.0.0.1:6379> HLEN myhash
  (integer) 2
  ```

- 获取哈希表中的所有字段：`HKEYS <key_name>`。返回包含哈希表中所有域（field）列表。当 key 不存在时，返回一个空列表。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HSET myhash field2 "bar"
  (integer) 1
  127.0.0.1:6379> HKEYS myhash
  1) "field1"
  2) "field2"
  ```

- 获取哈希表中所有值：`HVALS <key_name>`。返回一个包含哈希表中所有值的列表。当 key 不存在时，返回一个空表。

  ```bash
  127.0.0.1:6379> HSET myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HSET myhash field2 "bar"
  (integer) 1
  127.0.0.1:6379> HVALS myhash
  1) "foo"
  2) "bar"
  
  # 空哈希表/不存在的key
  127.0.0.1:6379> EXISTS not_exists
  (integer) 0
  
  127.0.0.1:6379> HVALS not_exists
  (empty list or set)
  ```

- 为哈希表 key 中的指定字段的整数值加上增量 increment：`HINCRBY <key_name> <field> <increment>`。返回执行 HINCRBY 命令之后，哈希表中字段的值。

  ```bash
  127.0.0.1:6379> HSET myhash field 5
  (integer) 1
  127.0.0.1:6379> HINCRBY myhash field 1
  (integer) 6
  127.0.0.1:6379> HINCRBY myhash field -1
  (integer) 5
  127.0.0.1:6379> HINCRBY myhash field -10
  (integer) -5
  ```

- 为哈希表 key 中的指定字段的浮点数值加上增量 increment：`HINCRBYFLOAT <key_name> <field> <increment>`。返回执行 HINCRBYFLOAT 命令之后，哈希表中字段的值。

  ```bash
  127.0.0.1:6379> HSET mykey field 10.50
  (integer) 1
  127.0.0.1:6379> HINCRBYFLOAT mykey field 0.1
  "10.6"
  127.0.0.1:6379> HINCRBYFLOAT mykey field -5
  "5.6"
  127.0.0.1:6379> HSET mykey field 5.0e3
  (integer) 0
  127.0.0.1:6379> HINCRBYFLOAT mykey field 2.0e2
  "5200"
  ```

- 只有在字段 field 不存在时，设置哈希表字段的值：`HSETNX <key_name> <field> <value>`。设置成功，返回 1。如果给定字段已经存在且没有操作被执行，返回 0。

  ```bash
  127.0.0.1:6379> HSETNX myhash field1 "foo"
  (integer) 1
  127.0.0.1:6379> HSETNX myhash field1 "bar"
  (integer) 0
  127.0.0.1:6379> HGET myhash field1
  "foo"
  
  127.0.0.1:6379> HSETNX nosql key-value-store redis
  (integer) 1
  
  127.0.0.1:6379> HSETNX nosql key-value-store redis		# 操作无效, key-value-store已存在
  (integer) 0
  ```

#### Set

- 向集合添加一个或多个成员，已经存在于集合的成员元素将被忽略：`SADD <key_name> <member1> [<member2>] .. [<memberN>]`。返回被添加到集合中的新元素的数量，不包括被忽略的元素。

  ```bash
  127.0.0.1:6379> SADD myset "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset "foo"
  (integer) 1
  127.0.0.1:6379> SADD myset "hello"
  (integer) 0
  127.0.0.1:6379> SMEMBERS myset
  1) "hello"
  2) "foo"
  ```

- 返回集合中的所有成员：`SMEMBERS <key_name>`。返回集合中的所有成员，不存在的集合 key 被视为空集合。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "world"
  (integer) 1
  127.0.0.1:6379> SMEMBERS myset1
  1) "World"
  2) "Hello"
  ```

- 判断 member 元素是否是集合 key 的成员：`SISMEMBER <key_name> <member>`。如果成员元素是集合的成员，返回 1。如果成员元素不是集合的成员，或 key 不存在，返回 0。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SISMEMBER myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SISMEMBER myset1 "world"
  (integer) 0
  ```

- 移除集合中一个或多个成员，不存在的成员元素会被忽略：`SREM <key_name> <member1> [<member2>] .. [<memberN>]`。返回被成功移除的元素的数量，不包括被忽略的元素。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "world"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "bar"
  (integer) 1
  127.0.0.1:6379> SREM myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SREM myset1 "foo"
  (integer) 0
  127.0.0.1:6379> SMEMBERS myset1
  1) "bar"
  2) "world"
  ```

- 获取集合的成员数：`SCARD <key_name>`。返回集合的数量。当集合 key 不存在时，返回 0。

  ```bash
  127.0.0.1:6379> SADD myset "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset "foo"
  (integer) 1
  127.0.0.1:6379> SADD myset "hello"
  (integer) 0
  127.0.0.1:6379> SCARD myset
  (integer) 2
  ```

- 返回集合中一个或多个随机数：`SRANDMEMBER <key_name> [<count>]`。只提供集合 key 参数时，返回一个元素；如果集合为空，返回 nil。如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "world"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "bar"
  (integer) 1
  127.0.0.1:6379> SRANDMEMBER myset1
  "bar"
  127.0.0.1:6379> SRANDMEMBER myset1 2
  1) "Hello"
  2) "world"
  ```

  - 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。
  - 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。

- 移除并返回集合中的一个随机元素：`SPOP <key_name> [<count>]`。参考 SRANDMEMBER 命令，前者不移除元素，而 SPOP 会移除元素。

  ```bash
  127.0.0.1:6379> SADD myset "one"
  (integer) 1
  127.0.0.1:6379> SADD myset "two"
  (integer) 1
  127.0.0.1:6379> SADD myset "three"
  (integer) 1
  127.0.0.1:6379> SPOP myset
  "one"
  127.0.0.1:6379> SMEMBERS myset
  1) "three"
  2) "two"
  127.0.0.1:6379> SADD myset "four"
  (integer) 1
  127.0.0.1:6379> SADD myset "five"
  (integer) 1
  127.0.0.1:6379> SPOP myset 3
  1) "five"
  2) "four"
  3) "two"
  127.0.0.1:6379> SMEMBERS myset
  1) "three"
  ```

- 将 member 元素从 source 集合移动到 destination 集合：`SMOVE <source> <destination> <member>`。如果成员元素被成功移除，返回 1。如果成员元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 0。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "world"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "bar"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "foo"
  (integer) 1
  127.0.0.1:6379> SMOVE myset1 myset2 "bar"
  (integer) 1
  127.0.0.1:6379> SMEMBERS myset1
  1) "World"
  2) "Hello"
  127.0.0.1:6379> SMEMBERS myset2
  1) "foo"
  2) "bar"
  ```

  - SMOVE 是原子性操作。
  - 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0。否则，member 元素从 source 集合中被移除，并添加到 destination 集合中去。
  - 当 destination 集合已经包含 member 元素时，SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
  - 当 source 或 destination 不是集合类型时，返回一个错误。

- 返回第一个集合与其他集合之间的差异：`SDIFF <first_key_name> <other_key_name1> [<other_key_name2>] .. [<other_key_nameN>]`。返回包含差集成员的列表。

  ```bash
  127.0.0.1:6379> SADD key1 "a"
  (integer) 1
  127.0.0.1:6379> SADD key1 "b"
  (integer) 1
  127.0.0.1:6379> SADD key1 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "d"
  (integer) 1
  127.0.0.1:6379> SADD key2 "e"
  (integer) 1
  127.0.0.1:6379> SDIFF key1 key2
  1) "a"
  2) "b"
  ```

- 返回给定所有集合的差集并存储在 destination 中，如果指定的集合 key 已存在，则会被覆盖：`SDIFFSTORE <destination> <key_name1> [<key_name2>] .. [<key_nameN>]`。返回结果集中的元素数量。

  ```bash
  127.0.0.1:6379> SADD myset "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset "foo"
  (integer) 1
  127.0.0.1:6379> SADD myset "bar"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "world"
  (integer) 1
  127.0.0.1:6379> SDIFFSTORE destset myset myset2
  (integer) 2
  127.0.0.1:6379> SMEMBERS destset
  1) "foo"
  2) "bar"
  ```

- 返回给定所有集合的交集：`SINTER <key_name> <key_name1> [<key_name2>] .. [<key_nameN>]`。返回交集成员的列表。不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集（根据集合运算定律）。

  ```bash
  127.0.0.1:6379> SADD myset "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset "foo"
  (integer) 1
  127.0.0.1:6379> SADD myset "bar"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "world"
  (integer) 1
  127.0.0.1:6379> SINTER myset myset2
  1) "hello"
  ```

- 返回给定所有集合的交集并存储在 destination 中：`SINTERSTORE <destination> <key_name> <key_name1> [<key_name2>] .. [<key_nameN>]`。返回存储交集的集合的元素数量。如果指定的集合已经存在，则将其覆盖。

  ```bash
  127.0.0.1:6379> SADD myset1 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "foo"
  (integer) 1
  127.0.0.1:6379> SADD myset1 "bar"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "hello"
  (integer) 1
  127.0.0.1:6379> SADD myset2 "world"
  (integer) 1
  127.0.0.1:6379> SINTERSTORE myset myset1 myset2
  (integer) 1
  127.0.0.1:6379> SMEMBERS myset
  1) "hello"
  ```

- 返回所有给定集合的并集：`SUNION <key_name> <key_name1> [<key_name2>] .. [<key_nameN>] `。返回并集成员的列表。

  ```bash
  127.0.0.1:6379> SADD key1 "a"
  (integer) 1
  127.0.0.1:6379> SADD key1 "b"
  (integer) 1
  127.0.0.1:6379> SADD key1 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "d"
  (integer) 1
  127.0.0.1:6379> SADD key2 "e"
  (integer) 1
  127.0.0.1:6379> SUNION key1 key2
  1) "a"
  2) "c"
  3) "b"
  4) "e"
  5) "d"
  ```

- 所有给定集合的并集存储在 destination 集合中：`SUNIONSTORE <destination> <key_name> <key_name1> [<key_name2>] .. [<key_nameN>]`。返回结果集中的元素数量。如果 destination 已经存在，则将其覆盖。

  ```bash
  127.0.0.1:6379> SADD key1 "a"
  (integer) 1
  127.0.0.1:6379> SADD key1 "b"
  (integer) 1
  127.0.0.1:6379> SADD key1 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "c"
  (integer) 1
  127.0.0.1:6379> SADD key2 "d"
  (integer) 1
  127.0.0.1:6379> SADD key2 "e"
  (integer) 1
  127.0.0.1:6379> SUNIONSTORE key key1 key2
  (integer) 5
  127.0.0.1:6379> SMEMBERS key
  1) "c"
  2) "b"
  3) "e"
  4) "d"
  5) "a"
  ```

#### ZSet

- 向有序集合添加一个或多个成员，或者更新已存在成员的分数：`ZADD <key_name> <score1> <value1> [<score2> <value2>] .. [<scoreN> <valueN>]`。返回被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。

  ```bash
  127.0.0.1:6379> ZADD myzset 1 "one"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 1 "uno"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 2 "two" 3 "three"
  (integer) 2
  127.0.0.1:6379> ZRANGE myzset 0 -1 WITHSCORES
  1) "one"
  2) "1"
  3) "uno"
  4) "1"
  5) "two"
  6) "2"
  7) "three"
  8) "3"
  ```

  - 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
  - 分数值可以是整数值或双精度浮点数。
  - 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
  - 当 key 存在但不是有序集类型时，返回一个错误。

- 获取有序集合的成员数：`ZCARD <key_name>`。当 key 存在且是有序集类型时，返回有序集的基数。当 key 不存在时，返回 0。

  ```bash
  127.0.0.1:6379> ZADD myzset 1 "one"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 2 "two"
  (integer) 1
  127.0.0.1:6379> ZCARD myzset
  (integer) 2
  ```

- 返回有序集合中指定成员的索引，其中有序集成员按分数值递增（从小到大）顺序排列：`ZRANK <key_name> <member>`。如果成员是有序集 key 的成员，返回 member 的排名。如果成员不是有序集 key 的成员，返回 nil。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES			# 显示所有成员及其score值
  1) "peter"
  2) "3500"
  3) "tom"
  4) "4000"
  5) "jack"
  6) "5000"
  
  127.0.0.1:6379> ZRANK salary tom						# 显示tom的薪水排名, 第二
  (integer) 1
  ```

- 移除有序集合中的一个或多个成员，不存在的成员将被忽略：`ZREM <key_name> <member> [<member2>] .. [<memberN>]`。返回被成功移除的成员的数量，不包括被忽略的成员。

  ```bash
  # 测试数据
  127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
  1) "bing.com"
  2) "8"
  3) "baidu.com"
  4) "9"
  5) "google.com"
  6) "10"
  
  # 移除单个元素
  127.0.0.1:6379> ZREM page_rank google.com
  (integer) 1
  
  127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
  1) "bing.com"
  2) "8"
  3) "baidu.com"
  4) "9"
  
  # 移除多个元素
  127.0.0.1:6379> ZREM page_rank baidu.com bing.com
  (integer) 2
  
  127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
  (empty list or set)
  
  # 移除不存在元素
  127.0.0.1:6379> ZREM page_rank non-exists-element
  (integer) 0
  ```

- 计算在有序集合中指定区间分数的成员数：`ZCOUNT <key_name> <min> <max>`。返回分数值在 min 和 max 之间的成员的数量。

  ```bash
  127.0.0.1:6379> ZADD myzset 1 "hello"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 1 "foo"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 2 "world" 3 "bar"
  (integer) 2
  127.0.0.1:6379> ZCOUNT myzset 1 3
  (integer) 4
  ```

- 在有序集合中计算指定字典区间内成员数量：`ZLEXCOUNT <key_name> <min> <max>`。返回指定区间内的成员数量。

  ```bash
  127.0.0.1:6379> ZADD myzset 0 a 0 b 0 c 0 d 0 e
  (integer) 5
  127.0.0.1:6379> ZADD myzset 0 f 0 g
  (integer) 2
  127.0.0.1:6379> ZLEXCOUNT myzset - +
  (integer) 7
  127.0.0.1:6379> ZLEXCOUNT myzset [b [f
  (integer) 5
  ```

- 通过索引区间返回有序集合指定区间内的成员：`ZRANGE <key_name> <start> <stop> [WITHSCORES]`。返回指定区间内，带有分数值（可选）的有序集成员的列表。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES				# 显示整个有序集成员
  1) "jack"
  2) "3500"
  3) "tom"
  4) "5000"
  5) "boss"
  6) "10086"
  
  127.0.0.1:6379> ZRANGE salary 1 2 WITHSCORES				# 显示有序集下标区间1至2的成员
  1) "tom"
  2) "5000"
  3) "boss"
  4) "10086"
  
  127.0.0.1:6379> ZRANGE salary 0 200000 WITHSCORES			# 测试end下标超出最大下标时的情况
  1) "jack"
  2) "3500"
  3) "tom"
  4) "5000"
  5) "boss"
  6) "10086"
  
  127.0.0.1:6379> ZRANGE salary 200000 3000000 WITHSCORES		# 测试当给定区间不存在于有序集时的情况
  (empty list or set)
  ```

  - 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。也可以使用负数下标，以 -1 表示最后一个成员，-2 表示倒数第二个成员，以此类推。

- 返回有序集中指定区间内的成员，通过索引，分数从高到低：`ZREVRANGE <key_name> <start> <stop> [WITHSCORES]`。返回指定区间内，带有分数值（可选）的有序集成员的列表。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES			# 递增排列
  1) "peter"
  2) "3500"
  3) "tom"
  4) "4000"
  5) "jack"
  6) "5000"
  
  127.0.0.1:6379> ZREVRANGE salary 0 -1 WITHSCORES		# 递减排列
  1) "jack"
  2) "5000"
  3) "tom"
  4) "4000"
  5) "peter"
  6) "3500"
  ```

- 通过分数返回有序集合指定区间内的成员：`ZRANGEBYSCORE <key_name> <min> <max> [WITHSCORES] [LIMIT offset count]`。返回指定区间内，带有分数值（可选）的有序集成员的列表。

  ```bash
  127.0.0.1:6379> ZADD salary 2500 jack							# 测试数据
  (integer) 0
  redis 127.0.0.1:6379> ZADD salary 5000 tom
  (integer) 0
  redis 127.0.0.1:6379> ZADD salary 12000 peter
  (integer) 0
  
  127.0.0.1:6379> ZRANGEBYSCORE salary -inf +inf					# 显示整个有序集
  1) "jack"
  2) "tom"
  3) "peter"
  
  127.0.0.1:6379> ZRANGEBYSCORE salary -inf +inf WITHSCORES		# 显示整个有序集及成员的score值
  1) "jack"
  2) "2500"
  3) "tom"
  4) "5000"
  5) "peter"
  6) "12000"
  
  127.0.0.1:6379> ZRANGEBYSCORE salary -inf 5000 WITHSCORES		# 显示工资<=5000的所有成员
  1) "jack"
  2) "2500"
  3) "tom"
  4) "5000"
  
  127.0.0.1:6379> ZRANGEBYSCORE salary (5000 400000				# 显示工资大于5000小于等于400000的成员
  1) "peter"
  ```

  - 默认情况下，区间的取值使用闭区间（小于等于或大于等于），也可以通过给参数前增加`(`符号来使用可选的开区间（小于或大于）。

- 返回有序集中指定分数区间内的成员，分数从高到低排序：`ZREVRANGEBYSCORE <key_name> <max> <min> [WITHSCORES] [LIMIT offset count]`。返回指定区间内，带有分数值（可选）的有序集成员的列表。

  ```bash
  127.0.0.1:6379> ZADD salary 10086 jack
  (integer) 1
  127.0.0.1:6379> ZADD salary 5000 tom
  (integer) 1
  127.0.0.1:6379> ZADD salary 7500 peter
  (integer) 1
  127.0.0.1:6379> ZADD salary 3500 joe
  (integer) 1
  
  127.0.0.1:6379> ZREVRANGEBYSCORE salary +inf -inf			# 逆序排列所有成员
  1) "jack"
  2) "peter"
  3) "tom"
  4) "joe"
  
  127.0.0.1:6379> ZREVRANGEBYSCORE salary 10000 2000			# 逆序排列薪水介于10000和2000之间的成员
  1) "peter"
  2) "tom"
  3) "joe"
  ```

- 返回有序集合中指定成员的排名，有序集成员按分数值递减（从大到小）排序：`ZREVRANK <key_name> <member>`。如果成员是有序集 key 的成员，返回成员的排名。如果成员不是有序集 key 的成员，返回 nil。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES				# 测试数据
  1) "jack"
  2) "2000"
  3) "peter"
  4) "3500"
  5) "tom"
  6) "5000"
  
  127.0.0.1:6379> ZREVRANK salary peter						# peter的工资排第二
  (integer) 1
  
  127.0.0.1:6379> ZREVRANK salary tom							# tom的工资最高
  (integer) 0
  ```

- 返回有序集中，成员的分数值：`ZSCORE <key_name> <member>`。返回成员的分数值，以字符串形式表示。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES				# 测试数据
  1) "tom"
  2) "2000"
  3) "peter"
  4) "3500"
  5) "jack"
  6) "5000"
  
  127.0.0.1:6379> ZSCORE salary peter							# 注意返回值是字符串
  "3500"
  ```

- 有序集合中对指定成员的分数加上增量 increment：`ZINCRBY <key_name> <increment> <member>`。当 key 不存在，或分数不是 key 的成员时，ZINCRBY key increment member 等同于 ZADD key increment member。

  ```bash
  127.0.0.1:6379> ZADD myzset 1 "one"
  (integer) 1
  127.0.0.1:6379> ZADD myzset 2 "two"
  (integer) 1
  127.0.0.1:6379> ZINCRBY myzset 2 "one"
  "3"
  127.0.0.1:6379> ZRANGE myzset 0 -1 WITHSCORES
  1) "two"
  2) "2"
  3) "one"
  4) "3"
  ```

- 移除有序集合中给定的排名区间的所有成员：`ZREMRANGEBYRANK <key_name> <start> <stop>`。

  ```bash
  127.0.0.1:6379> ZADD salary 2000 jack
  (integer) 1
  127.0.0.1:6379> ZADD salary 5000 tom
  (integer) 1
  127.0.0.1:6379> ZADD salary 3500 peter
  (integer) 1
  
  127.0.0.1:6379> ZREMRANGEBYRANK salary 0 1			# 移除下标0至1区间内的成员
  (integer) 2
  
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES		# 有序集只剩下一个成员
  1) "tom"
  2) "5000"
  ```

- 移除有序集合中给定的分数区间的所有成员：`ZREMRANGEBYSCORE <key_name> <min> <max>`。返回被移除成员的数量。

  ```bash
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES          # 显示有序集内所有成员及其score值
  1) "tom"
  2) "2000"
  3) "peter"
  4) "3500"
  5) "jack"
  6) "5000"
  
  127.0.0.1:6379> ZREMRANGEBYSCORE salary 1500 3500      # 移除所有薪水在1500到3500内的员工
  (integer) 2
  
  127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES          # 剩下的有序集成员
  1) "jack"
  2) "5000"
  ```

## Redis 持久化

<img src="redis/image-20230910205909519.png" alt="image-20230910205909519" style="zoom:80%;" />

Persistence refers to the writing of data to durable storage, such as a solid-state disk (SSD). Redis provides a range of persistence options. These include:

- **RDB** (Redis Database): RDB persistence performs point-in-time snapshots of your dataset at specified intervals.
- **AOF** (Append Only File): AOF persistence logs every write operation received by the server. These operations can then be replayed again at server startup, reconstructing the original dataset. Commands are logged using the same format as the Redis protocol itself.
- **No persistence**: You can disable persistence completely. This is sometimes used when caching.
- **RDB + AOF**: You can also combine both AOF and RDB in the same instance.

### RDB

RDB，即 Redis 数据库，RDB 持久性以指定的时间间隔执行数据集的时间点快照。

- 在指定的时间间隔内，将内存中的数据集快照写入磁盘，也就是 Snapshot 内存快照，Redis 服务恢复时再将磁盘快照文件直接读回到内存里。
- Redis 的数据都在内存中，保存备份时它执行的是`全量快照`，也就是说，把内存中的所有数据都记录到磁盘中。
- RDB 保存的是 dump.rdb 文件。

#### 优势

官网：

- RDB is a very compact single-file point-in-time representation of your Redis data. RDB files are perfect for backups. For instance you may want to archive your RDB files every hour for the latest 24 hours, and to save an RDB snapshot every day for 30 days. This allows you to easily restore different versions of the data set in case of disasters.
- RDB is very good for disaster recovery, being a single compact file that can be transferred to far data centers, or onto Amazon S3 (possibly encrypted).
- RDB maximizes Redis performances since the only work the Redis parent process needs to do in order to persist is forking a child that will do all the rest. The parent process will never perform disk I/O or alike.
- RDB allows faster restarts with big datasets compared to AOF.
- On replicas, RDB supports [partial resynchronizations after restarts and failovers](https://redis.io/topics/replication#partial-resynchronizations-after-restarts-and-failovers).

简译：



#### 劣势

官网：

- RDB is NOT good if you need to minimize the chance of data loss in case Redis stops working (for example after a power outage). You can configure different *save points* where an RDB is produced (for instance after at least five minutes and 100 writes against the data set, you can have multiple save points). However you'll usually create an RDB snapshot every five minutes or more, so in case of Redis stopping working without a correct shutdown for any reason you should be prepared to lose the latest minutes of data.
- RDB needs to fork() often in order to persist on disk using a child process. fork() can be time consuming if the dataset is big, and may result in Redis stopping serving clients for some milliseconds or even for one second if the dataset is very big and the CPU performance is not great. AOF also needs to fork() but less frequently and you can tune how often you want to rewrite your logs without any trade-off on durability.

简译：

### AOF

