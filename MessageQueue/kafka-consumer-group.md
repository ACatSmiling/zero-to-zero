*date: 2020-11-04*

Kafka 的 consumer 比 producer 要复杂许多，producer 没有 group 的概念，也不需要关注 offset，而 consumer 不一样，它有组织 (consumer group)，有纪律 (offset)。这些对 consumer 的要求就会很高，这篇文章就主要介绍 consumer 是如何加入 consumer group 的。

在这之前，我们需要先了解一下什么是 GroupCoordinator。简单地说，**GroupCoordinator 是运行在服务器上的一个服务，Kafka 集群上的每一个 broker 节点启动的时候，都会启动一个 GroupCoordinator 服务，其功能是负责进行 consumer 的 group 成员与 offset 管理 (但每个 GroupCoordinator 只是管理一部分的 consumer group member 和 offset 信息)。**

consumer group 对应的 GroupCoordinator 节点的确定，会通过如下方式：

将 consumer group 的 `group.id` 进行 hash，把得到的值的绝对值，对 _consumer_offsets 的 partition 总数取余，然后得到其对应的 partition 值，该 partition 的 leader 所在的 broker 即为该 consumer group 所对应的 GroupCoordinator 节点，GroupCoordinator 会存储与该 consumer group 相关的所有的 Meta 信息。

> 1._consumer_offsets 这个 topic 是 Kafka 内部使用的一个 topic，专门用来存储 group 消费的情况，默认情况下有 50 个 partition，每个 partition 默认有三个副本。
>
> 2.partition 计算方式：`abs(GroupId.hashCode()) % NumPartitions`，其中，NumPartitions 是 _consumer_offsets 的 partition 数，默认是 50 个。
>
> 3.比如，现在通过计算 `abs(GroupId.hashCode()) % NumPartitions` 的值为 35，然后就找第 35 个 partition 的 leader 在哪个 broker 上 (假设在 192.168.1.12)，那么 GroupCoordinator 节点就在这个 broker 上。

**同时，这个 consumer group 所提交的消费 offset 信息也会发送给这个 partition 的 leader 所对应的 broker 节点，因此，这个节点不仅是 GroupCoordinator，而且还保存分区分配方案和组内消费者 offset 信息。**

更多关于 GroupCoordinator 的解析，参考：[Kafka 源码解析之 GroupCoordinator 详解](https://matt33.com/2018/01/28/server-group-coordinator/)。

## KafkaConsumer 消费消息的主体流程

接下来，我们回顾下 KafkaConsumer 消费消息的主体流程：

```java
// 创建消费者
KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

// 订阅主题
kafkaConsumer.subscribe(Collections.singletonList("consumerCodeTopic"))

// 从服务器拉取数据
ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
```

### 创建 KafkaConsumer

创建 KafkaConsumer 的时候，会创建一个 ConsumerCoordinator 服务，由它来负责和 GroupCoordinator 通信：

```java
// no coordinator will be constructed for the default (null) group id
this.coordinator = groupId == null ? null :
	new ConsumerCoordinator(logContext,
                        this.client,
                        groupId,
                        this.groupInstanceId,
                        maxPollIntervalMs,
                        sessionTimeoutMs,
                        new Heartbeat(time, sessionTimeoutMs, heartbeatIntervalMs, maxPollIntervalMs, retryBackoffMs),
                        assignors,
                        this.metadata,
                        this.subscriptions,
                        metrics,
                        metricGrpPrefix,
                        this.time,
                        retryBackoffMs,
                        enableAutoCommit,
                        config.getInt(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG),
                        this.interceptors,
                        config.getBoolean(ConsumerConfig.LEAVE_GROUP_ON_CLOSE_CONFIG));
```

### 订阅 topic

KafkaConsumer 订阅 topic 的方式有好几种，这在[前面的文章](https://acatsmiling.github.io/2020/10/29/kafka-consumer/)有提到过。订阅的时候，会根据订阅的方式，设置其对应的订阅类型，默认存在四种订阅类型：

```java
private enum SubscriptionType {
    // 默认
    NONE,
    // subscribe方式订阅
    AUTO_TOPICS,
    // subscribe方式订阅
    AUTO_PATTERN,
    // assign方式订阅
    USER_ASSIGNED
}
```

比如，采用 `kafkaConsumer.subscribe(Collections.singletonList("consumerCodeTopic"))` 方式订阅 topic 时，会将订阅类型设置为 `SubscriptionType.AUTO_TOPICS`，其核心代码如下：

```java
/**
 * Subscribe to the given list of topics to get dynamically assigned partitions.
 * <b>Topic subscriptions are not incremental. This list will replace the current
 * assignment (if there is one).</b> It is not possible to combine topic subscription with group management
 * with manual partition assignment through {@link #assign(Collection)}.
 *
 * If the given list of topics is empty, it is treated the same as {@link #unsubscribe()}.
 *
 * <p>
 * This is a short-hand for {@link #subscribe(Collection, ConsumerRebalanceListener)}, which
 * uses a no-op listener. If you need the ability to seek to particular offsets, you should prefer
 * {@link #subscribe(Collection, ConsumerRebalanceListener)}, since group rebalances will cause partition offsets
 * to be reset. You should also provide your own listener if you are doing your own offset
 * management since the listener gives you an opportunity to commit offsets before a rebalance finishes.
 *
 * @param topics The list of topics to subscribe to
 * @throws IllegalArgumentException If topics is null or contains null or empty elements
 * @throws IllegalStateException If {@code subscribe()} is called previously with pattern, or assign is called
 *                               previously (without a subsequent call to {@link #unsubscribe()}), or if not
 *                               configured at-least one partition assignment strategy
 */
@Override
public void subscribe(Collection<String> topics) {
    subscribe(topics, new NoOpConsumerRebalanceListener());
}
```

```java
/**
 * Subscribe to the given list of topics to get dynamically
 * assigned partitions. <b>Topic subscriptions are not incremental. This list will replace the current
 * assignment (if there is one).</b> Note that it is not possible to combine topic subscription with group management
 * with manual partition assignment through {@link #assign(Collection)}.
 *
 * If the given list of topics is empty, it is treated the same as {@link #unsubscribe()}.
 *
 * <p>
 * As part of group management, the consumer will keep track of the list of consumers that belong to a particular
 * group and will trigger a rebalance operation if any one of the following events are triggered:
 * <ul>
 * <li>Number of partitions change for any of the subscribed topics
 * <li>A subscribed topic is created or deleted
 * <li>An existing member of the consumer group is shutdown or fails
 * <li>A new member is added to the consumer group
 * </ul>
 * <p>
 * When any of these events are triggered, the provided listener will be invoked first to indicate that
 * the consumer's assignment has been revoked, and then again when the new assignment has been received.
 * Note that rebalances will only occur during an active call to {@link #poll(Duration)}, so callbacks will
 * also only be invoked during that time.
 *
 * The provided listener will immediately override any listener set in a previous call to subscribe.
 * It is guaranteed, however, that the partitions revoked/assigned through this interface are from topics
 * subscribed in this call. See {@link ConsumerRebalanceListener} for more details.
 *
 * @param topics The list of topics to subscribe to
 * @param listener Non-null listener instance to get notifications on partition assignment/revocation for the
 *                 subscribed topics
 * @throws IllegalArgumentException If topics is null or contains null or empty elements, or if listener is null
 * @throws IllegalStateException If {@code subscribe()} is called previously with pattern, or assign is called
 *                               previously (without a subsequent call to {@link #unsubscribe()}), or if not
 *                               configured at-least one partition assignment strategy
 */
@Override
public void subscribe(Collection<String> topics, ConsumerRebalanceListener listener) {
    acquireAndEnsureOpen();
    try {
        maybeThrowInvalidGroupIdException();
        if (topics == null)
            throw new IllegalArgumentException("Topic collection to subscribe to cannot be null");
        if (topics.isEmpty()) {
            // treat subscribing to empty topic list as the same as unsubscribing
            this.unsubscribe();
        } else {
            for (String topic : topics) {
                if (topic == null || topic.trim().isEmpty())
                    throw new IllegalArgumentException("Topic collection to subscribe to cannot contain null or empty topic");
            }

            throwIfNoAssignorsConfigured();
            fetcher.clearBufferedDataForUnassignedTopics(topics);
            log.info("Subscribed to topic(s): {}", Utils.join(topics, ", "));
            if (this.subscriptions.subscribe(new HashSet<>(topics), listener))
                metadata.requestUpdateForNewTopics();
        }
    } finally {
        release();
    }
}
```

```java
public synchronized boolean subscribe(Set<String> topics, ConsumerRebalanceListener listener) {
    registerRebalanceListener(listener);
    setSubscriptionType(SubscriptionType.AUTO_TOPICS);
    return changeSubscription(topics);
}
```

```java
/**
 * This method sets the subscription type if it is not already set (i.e. when it is NONE),
 * or verifies that the subscription type is equal to the give type when it is set (i.e.
 * when it is not NONE)
 * @param type The given subscription type
 */
private void setSubscriptionType(SubscriptionType type) {
    if (this.subscriptionType == SubscriptionType.NONE)
        this.subscriptionType = type;
    else if (this.subscriptionType != type)
        throw new IllegalStateException(SUBSCRIPTION_EXCEPTION_MESSAGE);
}
```

### 从服务器拉取数据

订阅完成后，就可以从服务器拉取数据了，应该注意的是，KafkaConsumer 没有后台线程默默的拉取数据，它的所有行为都集中在 `poll ()` 方法中，**KafkaConsumer 是线程不安全的，同时只能允许一个线程运行。**

`kafkaConsumer.poll ()` 方法的核心代码如下：

```java
private ConsumerRecords<K, V> poll(final Timer timer, final boolean includeMetadataInTimeout) {
    // Step1:确认KafkaConsumer实例是单线程运行，以及没有被关闭
    acquireAndEnsureOpen();
    try {
        if (this.subscriptions.hasNoSubscriptionOrUserAssignment()) {
            throw new IllegalStateException("Consumer is not subscribed to any topics or assigned any partitions");
        }

        // poll for new data until the timeout expires
        do {
            client.maybeTriggerWakeup();

            if (includeMetadataInTimeout) {
                // Step2:更新metadata信息，获取GroupCoordinator的ip以及接口，并连接、 join-group、sync-group，期间group会进行rebalance。在此步骤，consumer会先加入group，然后获取需要消费的topic partition的offset信息
                if (!updateAssignmentMetadataIfNeeded(timer)) {
                    return ConsumerRecords.empty();
                }
            } else {
                while (!updateAssignmentMetadataIfNeeded(time.timer(Long.MAX_VALUE))) {
                    log.warn("Still waiting for metadata");
                }
            }

            // Step3:拉取数据
            final Map<TopicPartition, List<ConsumerRecord<K, V>>> records = pollForFetches(timer);
            if (!records.isEmpty()) {
                // before returning the fetched records, we can send off the next round of fetches
                // and avoid block waiting for their responses to enable pipelining while the user
                // is handling the fetched records.
                //
                // NOTE: since the consumed position has already been updated, we must not allow
                // wakeups or any other errors to be triggered prior to returning the fetched records.
                if (fetcher.sendFetches() > 0 || client.hasPendingRequests()) {
                    client.pollNoWakeup();
                }

                return this.interceptors.onConsume(new ConsumerRecords<>(records));
            }
        } while (timer.notExpired());

        return ConsumerRecords.empty();
    } finally {
        release();
    }
}
```

可以看出，在 Step 1 阶段， `poll ()` 方法会先进行判定，如果有多个线程同时使用一个 KafkaConsumer 则会抛出异常：

```java
/**
 * Acquire the light lock and ensure that the consumer hasn't been closed.
 * @throws IllegalStateException If the consumer has been closed
 */
private void acquireAndEnsureOpen() {
    acquire();
    if (this.closed) {
        release();
        throw new IllegalStateException("This consumer has already been closed.");
    }
}
```

```java
/**
 * Acquire the light lock protecting this consumer from multi-threaded access. Instead of blocking
 * when the lock is not available, however, we just throw an exception (since multi-threaded usage is not
 * supported).
 * @throws ConcurrentModificationException if another thread already has the lock
 */
private void acquire() {
    long threadId = Thread.currentThread().getId();
    if (threadId != currentThread.get() && !currentThread.compareAndSet(NO_CURRENT_THREAD, threadId))
        throw new ConcurrentModificationException("KafkaConsumer is not safe for multi-threaded access");
    refcount.incrementAndGet();
}
```

## KafkaConsumer 如何加入 consumer group

**一个 KafkaConsumer 实例消费数据的前提是能够加入一个 consumer group 成功，并获取其要订阅的 tp（topic-partition）列表，因此首先要做的就是和 GroupCoordinator 建立连接，加入组织。**

> consumer 加入 group 的过程，也就是 reblance 的过程。如果出现了频繁 reblance 的问题，可能和 `max.poll.interval.ms` 和 `max.poll.records` 两个参数有关。

因此，我们先把目光集中在 ConsumerCoordinator 上，这个过程主要发生在 Step 2 阶段：

```java
/**
 * Visible for testing
 */
boolean updateAssignmentMetadataIfNeeded(final Timer timer) {
    // 1.本篇文章的内容主要集中在coordinator.poll(timer)方法源码分析(主要功能是:consumer加入group)
    if (coordinator != null && !coordinator.poll(timer)) {
        return false;
    }

    // 2.updateFetchPositions(timer)方法留待下一篇文章分析(主要功能是:consumer获得partition的offset)
    return updateFetchPositions(timer);
}
```

关于对 ConsumerCoordinator 的处理都集中在 `coordinator.poll ()` 方法中。其主要逻辑如下：

```java
/**
 * Poll for coordinator events. This ensures that the coordinator is known and that the consumer
 * has joined the group (if it is using group management). This also handles periodic offset commits
 * if they are enabled.
 * (确保group的coordinator是已知的，并且这个consumer是已经加入到了group中，也用于offset周期性的commit)
 * <p>
 * Returns early if the timeout expires
 *
 * @param timer Timer bounding how long this method can block
 * @return true iff the operation succeeded
 */
public boolean poll(Timer timer) {
    maybeUpdateSubscriptionMetadata();

    invokeCompletedOffsetCommitCallbacks();

    // 如果是subscribe方式订阅的topic
    if (subscriptions.partitionsAutoAssigned()) {
        // Always update the heartbeat last poll time so that the heartbeat thread does not leave the
        // group proactively due to application inactivity even if (say) the coordinator cannot be found.
        // 1.检查心跳线程运行是否正常，如果心跳线程失败则抛出异常，反之则更新poll调用的时间
        pollHeartbeat(timer.currentTimeMs());
        // 2.如果coordinator未知，则初始化ConsumeCoordinator
        if (coordinatorUnknown() && !ensureCoordinatorReady(timer)) {
            return false;
        }

        // 判断是否需要重新加入group，如果订阅的partition变化或者分配的partition变化，都可能需要重新加入group
        if (rejoinNeededOrPending()) {
            // due to a race condition between the initial metadata fetch and the initial rebalance,
            // we need to ensure that the metadata is fresh before joining initially. This ensures
            // that we have matched the pattern against the cluster's topics at least once before joining.
            if (subscriptions.hasPatternSubscription()) {
                // For consumer group that uses pattern-based subscription, after a topic is created,
                // any consumer that discovers the topic after metadata refresh can trigger rebalance
                // across the entire consumer group. Multiple rebalances can be triggered after one topic
                // creation if consumers refresh metadata at vastly different times. We can significantly
                // reduce the number of rebalances caused by single topic creation by asking consumer to
                // refresh metadata before re-joining the group as long as the refresh backoff time has
                // passed.
                if (this.metadata.timeToAllowUpdate(timer.currentTimeMs()) == 0) {
                    this.metadata.requestUpdate();
                }

                if (!client.ensureFreshMetadata(timer)) {
                    return false;
                }

                maybeUpdateSubscriptionMetadata();
            }

            // 3.确保group是active的，重新加入group，分配订阅的partition
            if (!ensureActiveGroup(timer)) {
                return false;
            }
        }
    } else {
        // For manually assigned partitions, if there are no ready nodes, await metadata.
        // If connections to all nodes fail, wakeups triggered while attempting to send fetch
        // requests result in polls returning immediately, causing a tight loop of polls. Without
        // the wakeup, poll() with no channels would block for the timeout, delaying re-connection.
        // awaitMetadataUpdate() initiates new connections with configured backoff and avoids the busy loop.
        // When group management is used, metadata wait is already performed for this scenario as
        // coordinator is unknown, hence this check is not required.
        if (metadata.updateRequested() && !client.hasReadyNodes(timer.currentTimeMs())) {
            client.awaitMetadataUpdate(timer);
        }
    }

    // 4.如果设置的是自动commit,如果定时达到则自动commit
    maybeAutoCommitOffsetsAsync(timer.currentTimeMs());
    return true;
}
```

`coordinator.poll ()` 方法中，具体实现可以分为四个步骤：

1. `pollHeartbeat ()`：检测心跳线程运行是否正常，需要定时向 GroupCoordinator 发送心跳，如果超时未发送心跳，consumer 会离开 consumer group。
2. `ensureCoordinatorReady ()`：当通过 `subscribe ()` 方法订阅 topic 时，如果 coordinator 未知，则初始化 ConsumerCoordinator (在 `ensureCoordinatorReady ()` 中实现，该方法主要的作用是发送 FindCoordinatorRequest 请求，并建立连接)。
3. `ensureActiveGroup ()`：判断是否需要重新加入 group，如果订阅的 partition 变化或者分配的 partition 变化时，需要 rejoin，则通过 `ensureActiveGroup ()` 发送 join-group、sync-group 请求，加入 group 并获取其 assign 的 TopicPartition list。
4. `maybeAutoCommitOffsetsAsync ()`：如果设置的是自动 commit，并且达到了发送时限则自动 commit offset。

关于 rejoin，**下列几种情况会触发再均衡 (reblance) 操作**：

- 订阅的 topic 列表变化
- topic 被创建或删除

- 新的消费者加入消费者组 (第一次进行消费也属于这种情况)
- 消费者宕机下线 (长时间未发送心跳包)
- 消费者主动退出消费组，比如调用 `unsubscrible ()` 方法取消对主题的订阅
- 消费者组对应的 GroupCoorinator 节点发生了变化
- 消费者组内所订阅的任一主题或者主题的分区数量发生了变化

> 取消  topic 订阅，consumer 心跳线程超时以及在  Server 端给定的时间内未收到心跳请求，这三个都是触发的  LEAVE_GROUP 请求。

下面重点介绍下第二步中的 `ensureCoordinatorReady ()` 方法和第三步中的 `ensureActiveGroup ()` 方法。

### ensureCoordinatorReady

`ensureCoordinatorReady ()`这个方法主要作用：**选择一个连接数最少的 broker (还未响应请求最少的 broker)，发送 FindCoordinator 请求，找到 GroupCoordinator 后，建立对应的 TCP 连接。**

- 方法调用流程是 `ensureCoordinatorReady ()` → `lookupCoordinator ()` → `sendFindCoordinatorRequest ()`。
- 如果 client 收到 server response，那么就与 GroupCoordinator 建立连接。

```java
/**
 * Visible for testing.
 *
 * Ensure that the coordinator is ready to receive requests.
 *
 * @param timer Timer bounding how long this method can block
 * @return true If coordinator discovery and initial connection succeeded, false otherwise
 */
protected synchronized boolean ensureCoordinatorReady(final Timer timer) {
    if (!coordinatorUnknown())
        return true;

    do {
        // 找到GroupCoordinator，并建立连接
        final RequestFuture<Void> future = lookupCoordinator();
        client.poll(future, timer);

        if (!future.isDone()) {
            // ran out of time
            break;
        }

        if (future.failed()) {
            if (future.isRetriable()) {
                log.debug("Coordinator discovery failed, refreshing metadata");
                client.awaitMetadataUpdate(timer);
            } else
                throw future.exception();
        } else if (coordinator != null && client.isUnavailable(coordinator)) {
            // we found the coordinator, but the connection has failed, so mark
            // it dead and backoff before retrying discovery
            markCoordinatorUnknown();
            timer.sleep(retryBackoffMs);
        }
    } while (coordinatorUnknown() && timer.notExpired());

    return !coordinatorUnknown();
}
```

```java
protected synchronized RequestFuture<Void> lookupCoordinator() {
    if (findCoordinatorFuture == null) {
        // find a node to ask about the coordinator(找一个最少连接的broker，此处对应的应该就是文章开头处确定GroupCoordinator节点的发发)
        Node node = this.client.leastLoadedNode();
        if (node == null) {
            log.debug("No broker available to send FindCoordinator request");
            return RequestFuture.noBrokersAvailable();
        } else
            // 对找到的broker发送FindCoordinator请求，并对response进行处理
            findCoordinatorFuture = sendFindCoordinatorRequest(node);
    }
    return findCoordinatorFuture;
}
```

```java
/**
 * Discover the current coordinator for the group. Sends a GroupMetadata request to
 * one of the brokers. The returned future should be polled to get the result of the request.
 * @return A request future which indicates the completion of the metadata request
 */
private RequestFuture<Void> sendFindCoordinatorRequest(Node node) {
    // initiate the group metadata request
    log.debug("Sending FindCoordinator request to broker {}", node);
    FindCoordinatorRequest.Builder requestBuilder =
            new FindCoordinatorRequest.Builder(
                    new FindCoordinatorRequestData()
                        .setKeyType(CoordinatorType.GROUP.id())
                        .setKey(this.groupId));
    // 发送请求，并将response转换为RequestFuture
    // compose的作用是将FindCoordinatorResponseHandler类转换为RequestFuture
    // 实际上就是为返回的Future类重置onSuccess()和onFailure()方法
    return client.send(node, requestBuilder)
            .compose(new FindCoordinatorResponseHandler());
}
```

```java
// 根据response返回的ip以及端口信息，和该broke上开启的GroupCoordinator建立连接
private class FindCoordinatorResponseHandler extends RequestFutureAdapter<ClientResponse, Void> {

    @Override
    public void onSuccess(ClientResponse resp, RequestFuture<Void> future) {
        log.debug("Received FindCoordinator response {}", resp);
        clearFindCoordinatorFuture();

        FindCoordinatorResponse findCoordinatorResponse = (FindCoordinatorResponse) resp.responseBody();
        Errors error = findCoordinatorResponse.error();
        if (error == Errors.NONE) {
            // 如果正确获取broker上的GroupCoordinator，建立连接，并更新心跳时间
            synchronized (AbstractCoordinator.this) {
                // use MAX_VALUE - node.id as the coordinator id to allow separate connections
                // for the coordinator in the underlying network client layer
                int coordinatorConnectionId = Integer.MAX_VALUE - findCoordinatorResponse.data().nodeId();

                AbstractCoordinator.this.coordinator = new Node(
                        coordinatorConnectionId,
                        findCoordinatorResponse.data().host(),
                        findCoordinatorResponse.data().port());
                log.info("Discovered group coordinator {}", coordinator);
                // 初始化tcp连接
                client.tryConnect(coordinator);
                // 更新心跳时间
                heartbeat.resetSessionTimeout();
            }
            future.complete(null);
        } else if (error == Errors.GROUP_AUTHORIZATION_FAILED) {
            future.raise(new GroupAuthorizationException(groupId));
        } else {
            log.debug("Group coordinator lookup failed: {}", findCoordinatorResponse.data().errorMessage());
            future.raise(error);
        }
    }

    @Override
    public void onFailure(RuntimeException e, RequestFuture<Void> future) {
        clearFindCoordinatorFuture();
        super.onFailure(e, future);
    }
}
```

上面代码主要作用就是：往一个负载最小的 broker 节点发起 FindCoordinator 请求，Kafka 在走到这个请求后会根据 group_id 查找对应的 GroupCoordinator 节点 (文章开头处介绍的方法)，如果找到对应的 GroupCoordinator 则会返回其对应的 node_id，host 和 port 信息，并建立连接。

> 这里的 GroupCoordinator 节点的确定在文章开头提到过，是通过 `group.id` 和 partitionCount 来确定的。

### ensureActiveGroup

现在已经知道了 GroupCoordinator 节点，并建立了连接。`ensureActiveGroup ()` 这个方法的主要作用：**向 GroupCoordinator 发送 join-group、sync-group 请求，获取 assign 的 tp list。**

- 调用过程是 `ensureActiveGroup ()` → `ensureCoordinatorReady ()` → `startHeartbeatThreadIfNeeded ()` → `joinGroupIfNeeded ()`。
- `joinGroupIfNeeded ()` 方法中最重要的方法是 `initiateJoinGroup ()`，它的的调用流程是 `disableHeartbeatThread ()` → `sendJoinGroupRequest ()` → `JoinGroupResponseHandler::handle ()` → `onJoinLeader ()`，`onJoinFollower ()` → `sendSyncGroupRequest ()`。

```java
/**
 * Ensure the group is active (i.e., joined and synced)
 *
 * @param timer Timer bounding how long this method can block
 * @return true iff the group is active
 */
boolean ensureActiveGroup(final Timer timer) {
    // always ensure that the coordinator is ready because we may have been disconnected
    // when sending heartbeats and does not necessarily require us to rejoin the group.
    // 1.确保GroupCoordinator已经连接
    if (!ensureCoordinatorReady(timer)) {
        return false;
    }

    // 2.启动心跳线程，但是并不一定发送心跳，满足条件后才会发送心跳
    startHeartbeatThreadIfNeeded();
    // 3.发送joinGroup请求，并对返回的信息进行处理，核心步骤
    return joinGroupIfNeeded(timer);
}
```

心跳线程就是在这里启动的，但是并不一定马上发送心跳包，会在满足条件之后才会开始发送。后面最主要的逻辑就集中在 `joinGroupIfNeeded ()` 方法，它的核心代码如下：

```java
/**
 * Joins the group without starting the heartbeat thread.
 *
 * Visible for testing.
 *
 * @param timer Timer bounding how long this method can block
 * @return true iff the operation succeeded
 */
boolean joinGroupIfNeeded(final Timer timer) {
    while (rejoinNeededOrPending()) {
        if (!ensureCoordinatorReady(timer)) {
            return false;
        }

        // call onJoinPrepare if needed. We set a flag to make sure that we do not call it a second
        // time if the client is woken up before a pending rebalance completes. This must be called
        // on each iteration of the loop because an event requiring a rebalance (such as a metadata
        // refresh which changes the matched subscription set) can occur while another rebalance is
        // still in progress.
        // 触发onJoinPrepare，包括offset commit和rebalance listener
        if (needsJoinPrepare) {
            // 如果是自动提交，则要开始提交offset以及在join group之前回调reblance listener接口
            onJoinPrepare(generation.generationId, generation.memberId);
            needsJoinPrepare = false;
        }

        // 初始化joinGroup请求，并发送joinGroup请求，核心步骤
        final RequestFuture<ByteBuffer> future = initiateJoinGroup();
        client.poll(future, timer);
        if (!future.isDone()) {
            // we ran out of time
            return false;
        }

        // join succeed，这一步时，时间上sync-group已经成功了
        if (future.succeeded()) {
            // Duplicate the buffer in case `onJoinComplete` does not complete and needs to be retried.
            ByteBuffer memberAssignment = future.value().duplicate();
            // 发送完成，consumer加入group成功，触发onJoinComplete()方法
            onJoinComplete(generation.generationId, generation.memberId, generation.protocol, memberAssignment);

            // We reset the join group future only after the completion callback returns. This ensures
            // that if the callback is woken up, we will retry it on the next joinGroupIfNeeded.
            // 重置joinFuture为空
            resetJoinGroupFuture();
            needsJoinPrepare = true;
        } else {
            resetJoinGroupFuture();
            final RuntimeException exception = future.exception();
            if (exception instanceof UnknownMemberIdException ||
                    exception instanceof RebalanceInProgressException ||
                    exception instanceof IllegalGenerationException ||
                    exception instanceof MemberIdRequiredException)
                continue;
            else if (!future.isRetriable())
                throw exception;

            timer.sleep(retryBackoffMs);
        }
    }
    return true;
}
```

`initiateJoinGroup ()` 方法的核心代码如下：

```java
private synchronized RequestFuture<ByteBuffer> initiateJoinGroup() {
    // we store the join future in case we are woken up by the user after beginning the
    // rebalance in the call to poll below. This ensures that we do not mistakenly attempt
    // to rejoin before the pending rebalance has completed.
    if (joinFuture == null) {
        // fence off the heartbeat thread explicitly so that it cannot interfere with the join group.
        // Note that this must come after the call to onJoinPrepare since we must be able to continue
        // sending heartbeats if that callback takes some time.
        // Step1:rebalance期间，心跳线程停止运行
        disableHeartbeatThread();

        // 设置当前状态为rebalance
        state = MemberState.REBALANCING;
        // Step2:发送joinGroup请求，核心步骤
        joinFuture = sendJoinGroupRequest();
        
        // Step3:为joinGroup请求添加监听器，监听joinGroup请求的结果并做相应的处理
        joinFuture.addListener(new RequestFutureListener<ByteBuffer>() {
            @Override
            public void onSuccess(ByteBuffer value) {
                // handle join completion in the callback so that the callback will be invoked
                // even if the consumer is woken up before finishing the rebalance
                synchronized (AbstractCoordinator.this) {
                    log.info("Successfully joined group with generation {}", generation.generationId);
                    // 如果joinGroup成功，设置状态为stable
                    state = MemberState.STABLE;
                    rejoinNeeded = false;

                    if (heartbeatThread != null)
                        // Step4:允许心跳线程继续运行
                        heartbeatThread.enable();
                }
            }

            @Override
            public void onFailure(RuntimeException e) {
                // we handle failures below after the request finishes. if the join completes
                // after having been woken up, the exception is ignored and we will rejoin
                synchronized (AbstractCoordinator.this) {
                    // 如果joinGroup失败，设置状态为unjoined
                    state = MemberState.UNJOINED;
                }
            }
        });
    }
    return joinFuture;
}
```

可以看到在 joinGroup 之前会让心跳线程暂时停下来，此时会将 ConsumerCoordinator 的状态设置为 rebalance 状态，当 joinGroup 成功之后会将状态设置为 stable 状态，同时让之前停下来的心跳线程继续运行。

`sendJoinGroupRequest ()` 方法的核心代码如下：

```java
/**
 * Join the group and return the assignment for the next generation. This function handles both
 * JoinGroup and SyncGroup, delegating to {@link #performAssignment(String, String, List)} if
 * elected leader by the coordinator.
 *
 * NOTE: This is visible only for testing
 *
 * @return A request future which wraps the assignment returned from the group leader
 */
// 发送joinGroup请求
RequestFuture<ByteBuffer> sendJoinGroupRequest() {
    if (coordinatorUnknown())
        return RequestFuture.coordinatorNotAvailable();

    // send a join group request to the coordinator
    log.info("(Re-)joining group");
    JoinGroupRequest.Builder requestBuilder = new JoinGroupRequest.Builder(
            new JoinGroupRequestData()
                    .setGroupId(groupId)
                    .setSessionTimeoutMs(this.sessionTimeoutMs)
                    .setMemberId(this.generation.memberId)
                    .setGroupInstanceId(this.groupInstanceId.orElse(null))
                    .setProtocolType(protocolType())
                    .setProtocols(metadata())
                    .setRebalanceTimeoutMs(this.rebalanceTimeoutMs)
    );

    log.debug("Sending JoinGroup ({}) to coordinator {}", requestBuilder, this.coordinator);

    // Note that we override the request timeout using the rebalance timeout since that is the
    // maximum time that it may block on the coordinator. We add an extra 5 seconds for small delays.

    int joinGroupTimeoutMs = Math.max(rebalanceTimeoutMs, rebalanceTimeoutMs + 5000);
    return client.send(coordinator, requestBuilder, joinGroupTimeoutMs)
            .compose(new JoinGroupResponseHandler());// Step5:处理joinGroup请求后的response
}
```

在发送 joinGroup 请求之后，会收到来自服务器的响应，然后针对这个响应再做一些重要的事情：

```java
// 处理发送joinGroup请求后的response的handler(同步group信息)
private class JoinGroupResponseHandler extends CoordinatorResponseHandler<JoinGroupResponse, ByteBuffer> {
    @Override
    public void handle(JoinGroupResponse joinResponse, RequestFuture<ByteBuffer> future) {
        Errors error = joinResponse.error();
        if (error == Errors.NONE) {
            log.debug("Received successful JoinGroup response: {}", joinResponse);
            sensors.joinLatency.record(response.requestLatencyMs());

            synchronized (AbstractCoordinator.this) {
                if (state != MemberState.REBALANCING) {
                    // if the consumer was woken up before a rebalance completes, we may have already left
                    // the group. In this case, we do not want to continue with the sync group.
                    future.raise(new UnjoinedGroupException());
                } else {
                    AbstractCoordinator.this.generation = new Generation(joinResponse.data().generationId(),
                            joinResponse.data().memberId(), joinResponse.data().protocolName());
                    // Step6:joinGroup成功，下面需要进行sync-group，获取分配的tp列表
                    if (joinResponse.isLeader()) {
                        // 当前consumer是leader
                        onJoinLeader(joinResponse).chain(future);
                    } else {
                        // 当前consumer是follower
                        onJoinFollower().chain(future);
                    }
                }
            }
        } else if (error == Errors.COORDINATOR_LOAD_IN_PROGRESS) {
            log.debug("Attempt to join group rejected since coordinator {} is loading the group.", coordinator());
            // backoff and retry
            future.raise(error);
        } else if (error == Errors.UNKNOWN_MEMBER_ID) {
            // reset the member id and retry immediately
            resetGeneration();
            log.debug("Attempt to join group failed due to unknown member id.");
            future.raise(Errors.UNKNOWN_MEMBER_ID);
        } else if (error == Errors.COORDINATOR_NOT_AVAILABLE
                || error == Errors.NOT_COORDINATOR) {
            // re-discover the coordinator and retry with backoff
            markCoordinatorUnknown();
            log.debug("Attempt to join group failed due to obsolete coordinator information: {}", error.message());
            future.raise(error);
        } else if (error == Errors.FENCED_INSTANCE_ID) {
            log.error("Received fatal exception: group.instance.id gets fenced");
            future.raise(error);
        } else if (error == Errors.INCONSISTENT_GROUP_PROTOCOL
                || error == Errors.INVALID_SESSION_TIMEOUT
                || error == Errors.INVALID_GROUP_ID
                || error == Errors.GROUP_AUTHORIZATION_FAILED
                || error == Errors.GROUP_MAX_SIZE_REACHED) {
            // log the error and re-throw the exception
            log.error("Attempt to join group failed due to fatal error: {}", error.message());
            if (error == Errors.GROUP_MAX_SIZE_REACHED) {
                future.raise(new GroupMaxSizeReachedException(groupId));
            } else if (error == Errors.GROUP_AUTHORIZATION_FAILED) {
                future.raise(new GroupAuthorizationException(groupId));
            } else {
                future.raise(error);
            }
        } else if (error == Errors.UNSUPPORTED_VERSION) {
            log.error("Attempt to join group failed due to unsupported version error. Please unset field group.instance.id and retry" +
                    "to see if the problem resolves");
            future.raise(error);
        } else if (error == Errors.MEMBER_ID_REQUIRED) {
            // Broker requires a concrete member id to be allowed to join the group. Update member id
            // and send another join group request in next cycle.
            synchronized (AbstractCoordinator.this) {
                AbstractCoordinator.this.generation = new Generation(OffsetCommitRequest.DEFAULT_GENERATION_ID,
                        joinResponse.data().memberId(), null);
                AbstractCoordinator.this.rejoinNeeded = true;
                AbstractCoordinator.this.state = MemberState.UNJOINED;
            }
            future.raise(Errors.MEMBER_ID_REQUIRED);
        } else {
            // unexpected error, throw the exception
            log.error("Attempt to join group failed due to unexpected error: {}", error.message());
            future.raise(new KafkaException("Unexpected error in join group response: " + error.message()));
        }
    }
}
```

上面代码的主要过程如下：

1. 如果 group 是新的 `group.id`，那么此时 group 初始化的状态为 **Empty**；
2. 当 GroupCoordinator 接收到 consumer 的 join-group 请求后，由于此时这个 group 的 member 列表还是空 (group 是新建的，每个 consumer 实例被称为这个 group 的一个 member)，**第一个加入的 member 将被选为 leader**，也就是说，对于一个新的 consumer group 而言，当第一个 consumer 实例加入后将会被选为 leader。如果后面 leader 挂了，会从其他 member 里面随机选择一个 member 成为新的 leader；
3. 如果 GroupCoordinator 接收到 leader 发送 join-group 请求，将会触发 rebalance，group 的状态变为 **PreparingRebalance**；
4. 此时，GroupCoordinator 将会等待一定的时间，如果在一定时间内，接收到 join-group 请求的 consumer 将被认为是依然存活的，此时 group 会变为 **AwaitSync** 状态，并且 GroupCoordinator 会向这个 group 的所有 member 返回其 response；
5. consumer 在接收到 GroupCoordinator 的 response 后，如果这个 consumer 是 group 的 leader，那么这个 consumer 将会负责为整个 group assign partition 订阅安排（默认是按 range 的策略，目前也可选 roundrobin），然后 leader 将分配后的信息以 `sendSyncGroupRequest ()` 请求的方式发给 GroupCoordinator，而作为 follower 的 consumer 实例会发送一个空列表；
6. GroupCoordinator 在接收到 leader 发来的请求后，会将 assign 的结果返回给所有已经发送 sync-group 请求的 consumer 实例，并且 group 的状态将会转变为 **Stable**，如果后续再收到 sync-group 请求，由于 group 的状态已经是 Stable，将会直接返回其分配结果。

sync-group 发送请求核心代码如下：

```java
// 当consumer为follower时，从GroupCoordinator拉取分配结果
private RequestFuture<ByteBuffer> onJoinFollower() {
    // send follower's sync group with an empty assignment
    SyncGroupRequest.Builder requestBuilder =
            new SyncGroupRequest.Builder(
                    new SyncGroupRequestData()
                            .setGroupId(groupId)
                            .setMemberId(generation.memberId)
                            .setGroupInstanceId(this.groupInstanceId.orElse(null))
                            .setGenerationId(generation.generationId)
                            .setAssignments(Collections.emptyList())
            );
    log.debug("Sending follower SyncGroup to coordinator {}: {}", this.coordinator, requestBuilder);
    // 发送sync-group请求
    return sendSyncGroupRequest(requestBuilder);
}

// 当consumer客户端为leader时，对group下的所有实例进行分配，将assign的结果发送到GroupCoordinator
private RequestFuture<ByteBuffer> onJoinLeader(JoinGroupResponse joinResponse) {
    try {
        // perform the leader synchronization and send back the assignment for the group(assign 操作)
        Map<String, ByteBuffer> groupAssignment = performAssignment(joinResponse.data().leader(), joinResponse.data().protocolName(),
                joinResponse.data().members());

        List<SyncGroupRequestData.SyncGroupRequestAssignment> groupAssignmentList = new ArrayList<>();
        for (Map.Entry<String, ByteBuffer> assignment : groupAssignment.entrySet()) {
            groupAssignmentList.add(new SyncGroupRequestData.SyncGroupRequestAssignment()
                    .setMemberId(assignment.getKey())
                    .setAssignment(Utils.toArray(assignment.getValue()))
            );
        }

        SyncGroupRequest.Builder requestBuilder =
                new SyncGroupRequest.Builder(
                        new SyncGroupRequestData()
                                .setGroupId(groupId)
                                .setMemberId(generation.memberId)
                                .setGroupInstanceId(this.groupInstanceId.orElse(null))
                                .setGenerationId(generation.generationId)
                                .setAssignments(groupAssignmentList)
                );
        log.debug("Sending leader SyncGroup to coordinator {}: {}", this.coordinator, requestBuilder);
        // 发送sync-group请求
        return sendSyncGroupRequest(requestBuilder);
    } catch (RuntimeException e) {
        return RequestFuture.failure(e);
    }
}

// 发送SyncGroup请求，获取对partition分配的安排
private RequestFuture<ByteBuffer> sendSyncGroupRequest(SyncGroupRequest.Builder requestBuilder) {
    if (coordinatorUnknown())
        return RequestFuture.coordinatorNotAvailable();
    return client.send(coordinator, requestBuilder)
            .compose(new SyncGroupResponseHandler());
}

private class SyncGroupResponseHandler extends CoordinatorResponseHandler<SyncGroupResponse, ByteBuffer> {
    @Override
    public void handle(SyncGroupResponse syncResponse,
                       RequestFuture<ByteBuffer> future) {
        Errors error = syncResponse.error();
        if (error == Errors.NONE) {
            // sync-group成功
            sensors.syncLatency.record(response.requestLatencyMs());
            future.complete(ByteBuffer.wrap(syncResponse.data.assignment()));
        } else {
            // join的标志位设置为true
            requestRejoin();

            if (error == Errors.GROUP_AUTHORIZATION_FAILED) {
                future.raise(new GroupAuthorizationException(groupId));
            } else if (error == Errors.REBALANCE_IN_PROGRESS) {
                // group正在进行rebalance，任务失败
                log.debug("SyncGroup failed because the group began another rebalance");
                future.raise(error);
            } else if (error == Errors.FENCED_INSTANCE_ID) {
                log.error("Received fatal exception: group.instance.id gets fenced");
                future.raise(error);
            } else if (error == Errors.UNKNOWN_MEMBER_ID
                    || error == Errors.ILLEGAL_GENERATION) {
                log.debug("SyncGroup failed: {}", error.message());
                resetGeneration();
                future.raise(error);
            } else if (error == Errors.COORDINATOR_NOT_AVAILABLE
                    || error == Errors.NOT_COORDINATOR) {
                log.debug("SyncGroup failed: {}", error.message());
                markCoordinatorUnknown();
                future.raise(error);
            } else {
                future.raise(new KafkaException("Unexpected error from SyncGroup: " + error.message()));
            }
        }
    }
}
```

这个阶段主要是将分区分配方案同步给各个消费者，这个同步仍然是通过 GroupCoordinator 来转发的。

> 分区策略并非由 leader 消费者来决定，而是各个消费者投票决定的，谁的票多就采用什么分区策略。这里的分区策略是通过 `partition.assignment.strategy` 参数设置的，可以设置多个。如果选举出了消费者不支持的策略，那么就会抛出异常 `IllegalArgumentException: Member does not support protocol`。

经过上面的步骤，一个 consumer 实例就已经加入 group 成功了，加入 group 成功后，将会触发 ConsumerCoordinator 的 `onJoinComplete ()` 方法，其作用就是：更新订阅的 tp 列表、更新其对应的 metadata 及触发注册的 listener。

```java
// 加入group成功
@Override
protected void onJoinComplete(int generation,
                              String memberId,
                              String assignmentStrategy,
                              ByteBuffer assignmentBuffer) {
    // only the leader is responsible for monitoring for metadata changes (i.e. partition changes)
    if (!isLeader)
        assignmentSnapshot = null;

    PartitionAssignor assignor = lookupAssignor(assignmentStrategy);
    if (assignor == null)
        throw new IllegalStateException("Coordinator selected invalid assignment protocol: " + assignmentStrategy);

    Assignment assignment = ConsumerProtocol.deserializeAssignment(assignmentBuffer);
    if (!subscriptions.assignFromSubscribed(assignment.partitions())) {
        handleAssignmentMismatch(assignment);
        return;
    }

    Set<TopicPartition> assignedPartitions = subscriptions.assignedPartitions();

    // The leader may have assigned partitions which match our subscription pattern, but which
    // were not explicitly requested, so we update the joined subscription here.
    maybeUpdateJoinedSubscription(assignedPartitions);

    // give the assignor a chance to update internal state based on the received assignment
    assignor.onAssignment(assignment, generation);

    // reschedule the auto commit starting from now
    if (autoCommitEnabled)
        this.nextAutoCommitTimer.updateAndReset(autoCommitIntervalMs);

    // execute the user's callback after rebalance
    ConsumerRebalanceListener listener = subscriptions.rebalanceListener();
    log.info("Setting newly assigned partitions: {}", Utils.join(assignedPartitions, ", "));
    try {
        listener.onPartitionsAssigned(assignedPartitions);
    } catch (WakeupException | InterruptException e) {
        throw e;
    } catch (Exception e) {
        log.error("User provided listener {} failed on partition assignment", listener.getClass().getName(), e);
    }
}
```

至此，一个 consumer 实例算是真正上意义上加入 group 成功。

然后 consumer 就进入正常工作状态，同时 consumer 也通过向 GroupCoordinator 发送心跳来维持它们与消费者组的从属关系，以及它们对分区的所有权关系。只要以正常的间隔发送心跳，就被认为是活跃的，但是如果 GroupCoordinator 没有响应，那么就会发送 LeaveGroup 请求退出消费者组。

## 本文参考

http://generalthink.github.io/2019/05/15/how-to-join-kafka-consumer-group/

https://matt33.com/2017/10/22/consumer-join-group/

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。