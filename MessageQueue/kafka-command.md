*date: 2020-12-30*

- 查看 Kafka topic 列表命令，返回 topic 名字列表


```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-topics.sh --zookeeper hadoopdatanode1:2181 --list
```

- 创建 Kafka topic 命令


```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-topics.sh --zookeeper hadoopdatanode1:2181,hadoopdatanode2:2181,hadoopdatanode3:2181 --create --partitions 6 --replication-factor 2 --topic patent-grant
```

-  查看 Kafka 指定 topic 的详情命令，返回该 topic 的 parition 数量、replica 因子以及每个 partition 的 leader、replica 信息

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-topics.sh --zookeeper hadoopdatanode1:2181 --describe --topic patent-grant
```

- 查看 Kafka 指定 topic 各 partition 的 offset 信息命令，--time 参数为 -1 时，表示各分区最大的 offset，为 -2 时，表示各分区最小的 offset

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list hadoopdatanode1:9092 --time -1 --topic patent-grant
```

- 删除 Kafka topic 命令

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-topics.sh --zookeeper hadoopdatanode1:2181 --delete -topic patent-grant
```

> 只有 topic 不再被使用时，才能被删除。

- 修改 kafka topic 的数据保存时间：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-configs.sh --bootstrap-server hadoopdatanode1:9092 --alter --entity-type topics --entity-name extractor-patent --add-config retention.ms=2592000000
```

>kafka 中默认消息的保留时间是 7 天，若想更改，需在配置文件 server.properties 里更改选项：log.retention.hours=168。
>
>如果需要对某一个主题的消息存留的时间进行变更，但不影响其他主题，并且 kafka 集群不用重启，则使用上面的命令修改，该命令设置的是 30 天。

- 查看 kafka topic 配置信息：


```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-configs.sh --bootstrap-server hadoopdatanode1:9092 --describe --entity-type topics --entity-name extractor-patent
```

如果使用的是默认配置，显示：

```sh
Dynamic configs for topic extractor-patent are:
```

如果更改了配置，显示：

```sh
Dynamic configs for topic extractor-patent are:
  retention.ms=2592000000 sensitive=false synonyms={DYNAMIC_TOPIC_CONFIG:retention.ms=2592000000}
```

- 查看 kafka consumer group 命令，返回 consumer group 名字列表 (新版信息保存在 broker 中，老版信息保存在 zookeeper 中，二者命令不同)

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-consumer-groups.sh --bootstrap-server hadoopdatanode1:9092 --list
```

> 老版命令：`~/kafka_2.12-2.6.0/bin/kafka-consumer-groups.sh --zookeeper hadoopdatanode1:2181 --list`

- 查看 Kafka 指定 consumer group 的详情命令，返回 consumer group 对应的 topic 信息、当前消费的 offset、总 offset、剩余待消费 offset 等信息

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-consumer-groups.sh --bootstrap-server hadoopdatanode1:9092 --describe --group log-consumer
```

- 重置 Kafka 指定 consumer group 消费的 topic 的 offset 命令

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-consumer-groups.sh --bootstrap-server hadoopdatanode1:9092 --reset-offsets -to-offset 0 --execute --topic patent-app --group log-consumer
```

- 删除 Kafka 指定 consumer group 命令

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-consumer-groups.sh --bootstrap-server hadoopdatanode1:9092 --delete --group log-consumer
```

- 消费 Kafka 指定 topic 的内容命令

kafka-console-consumer.sh 脚本是一个简易的消费者控制台。该 shell 脚本的功能通过调用 kafka.tools 包下的 ConsoleConsumer 类，并将提供的命令行参数全部传给该类实现。

参数说明：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-console-consumer.sh
This tool helps to read data from Kafka topics and outputs it to standard output.
Option                                   Description                            
------                                   -----------                            
--bootstrap-server <String: server to    REQUIRED: The server(s) to connect to. 
  connect to>                                                                   
--consumer-property <String:             A mechanism to pass user-defined       
  consumer_prop>                           properties in the form key=value to  
                                           the consumer.                        
--consumer.config <String: config file>  Consumer config properties file. Note  
                                           that [consumer-property] takes       
                                           precedence over this config.         
--enable-systest-events                  Log lifecycle events of the consumer   
                                           in addition to logging consumed      
                                           messages. (This is specific for      
                                           system tests.)                       
--formatter <String: class>              The name of a class to use for         
                                           formatting kafka messages for        
                                           display. (default: kafka.tools.      
                                           DefaultMessageFormatter)             
--from-beginning                         If the consumer does not already have  
                                           an established offset to consume     
                                           from, start with the earliest        
                                           message present in the log rather    
                                           than the latest message.             
--group <String: consumer group id>      The consumer group id of the consumer. 
--help                                   Print usage information.               
--isolation-level <String>               Set to read_committed in order to      
                                           filter out transactional messages    
                                           which are not committed. Set to      
                                           read_uncommitted to read all         
                                           messages. (default: read_uncommitted)
--key-deserializer <String:                                                     
  deserializer for key>                                                         
--max-messages <Integer: num_messages>   The maximum number of messages to      
                                           consume before exiting. If not set,  
                                           consumption is continual.            
--offset <String: consume offset>        The offset id to consume from (a non-  
                                           negative number), or 'earliest'      
                                           which means from beginning, or       
                                           'latest' which means from end        
                                           (default: latest)                    
--partition <Integer: partition>         The partition to consume from.         
                                           Consumption starts from the end of   
                                           the partition unless '--offset' is   
                                           specified.                           
--property <String: prop>                The properties to initialize the       
                                           message formatter. Default           
                                           properties include:                  
                                         	print.timestamp=true|false            
                                         	print.key=true|false                  
                                         	print.value=true|false                
                                         	key.separator=<key.separator>         
                                         	line.separator=<line.separator>       
                                         	key.deserializer=<key.deserializer>   
                                         	value.deserializer=<value.            
                                           deserializer>                        
                                         Users can also pass in customized      
                                           properties for their formatter; more 
                                           specifically, users can pass in      
                                           properties keyed with 'key.          
                                           deserializer.' and 'value.           
                                           deserializer.' prefixes to configure 
                                           their deserializers.                 
--skip-message-on-error                  If there is an error when processing a 
                                           message, skip it instead of halt.    
--timeout-ms <Integer: timeout_ms>       If specified, exit if no message is    
                                           available for consumption for the    
                                           specified interval.                  
--topic <String: topic>                  The topic id to consume on.            
--value-deserializer <String:                                                   
  deserializer for values>                                                      
--version                                Display Kafka version.                 
--whitelist <String: whitelist>          Regular expression specifying          
                                           whitelist of topics to include for   
                                           consumption.
```

> 参数说明参考：https://blog.csdn.net/qq_29116427/article/details/80206125

从头开始消费：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-console-consumer.sh --bootstrap-server hadoopdatanode1:9092 --from-beginning --topic log-collect
```

从头开始消费前 10 条消息，并显示 key：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-console-consumer.sh --bootstrap-server hadoopdatanode1:9092 --from-beginning --max-messages 10 --property print.key=true --topic log-collect
```

从指定分区、指定 offset 开始消费：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-console-consumer.sh --bootstrap-server hadoopdatanode1:9092 --partition 0 --offset 219000 --topic log-collect
```

从尾开始消费，必须指定分区：

```sh
$ ~/kafka_2.12-2.6.0/bin/kafka-console-consumer.sh --bootstrap-server hadoopdatanode1:9092 --partition 0 --offset latest --topic log-collect
```

