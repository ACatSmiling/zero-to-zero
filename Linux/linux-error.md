---
date: 2021-01-11
---

## No space left on device

有时候，在创建新文件，或者往磁盘写内容时，会提示`No space left on device`异常。

一般来说，Linux 空间占满有如两种情况：

### 空间占满

通过`df -h`命令，查看空间的使用情况：

```shell
$ df -h
Filesystem                     Size  Used Avail Use% Mounted on
udev                           3.9G     0  3.9G   0% /dev
tmpfs                          799M   82M  718M  11% /run
/dev/mapper/lin--vg-root       491G  195G  272G  42% /
tmpfs                          3.9G  8.0K  3.9G   1% /dev/shm
tmpfs                          5.0M     0  5.0M   0% /run/lock
tmpfs                          3.9G     0  3.9G   0% /sys/fs/cgroup
/dev/sda1                      472M   58M  390M  13% /boot
tmpfs                          799M     0  799M   0% /run/user/1000
192.168.1.152:/mnt/chenlei      10T  5.0T  4.6T  53% /home/lin/share/storage_server_1
192.168.1.236:/mnt              10T  9.0T  520G  95% /home/lin/share/storage_server_2
192.168.1.106:/mnt              40T   24T   15T  63% /home/lin/share/storage_server_3
192.168.1.102:/home/lin/share  491G  195G  272G  42% /tmp/share
```

可以看出，各分区仍有较大的空间能够使用。如果某个分区的使用率达到了 100%，那也就无法再创建新文件，也无法再写入内容，需要删除一些文件。

### inode 占满

通过`df -i`命令，查看 inode 的使用情况。

```shell
$ df -ih
Filesystem                    Inodes IUsed IFree IUse% Mounted on
udev                            993K   421  993K    1% /dev
tmpfs                           998K   749  998K    1% /run
/dev/mapper/lin--vg-root         32M  1.6M   30M    6% /
tmpfs                           998K     2  998K    1% /dev/shm
tmpfs                           998K     3  998K    1% /run/lock
tmpfs                           998K    16  998K    1% /sys/fs/cgroup
/dev/sda1                       122K   303  122K    1% /boot
tmpfs                           998K     4  998K    1% /run/user/1000
192.168.1.152:/mnt/chenlei      320M   42M  279M   13% /home/lin/share/storage_server_1
192.168.1.236:/mnt              320M   69M  252M   22% /home/lin/share/storage_server_2
192.168.1.106:/mnt              640M  641M    0M  100% /home/lin/share/storage_server_3
192.168.1.102:/home/lin/share    32M  1.6M   30M    6% /tmp/share
```

可以看出，每个分区都有一定大小的 inode 空间，但`/home/lin/share/storage_server_3`分区的 inode 空间使用率达到 100%。因此，再往此分区创建新文件或写入内容时，会提示`No space left on device`异常。

解决方法：将`/home/lin/share/storage_server_3`分区上一些不必要的文件删除。

>理解 inode，要从文件储存说起。
>
>文件储存在硬盘上，硬盘的最小存储单位叫做 "扇区"（Sector），每个扇区储存 512 字节（相当于 0.5 KB）。
>
>操作系统读取硬盘的时候，不会一个个扇区地读取，这样效率太低，而是一次性连续读取多个扇区，即一次性读取一个 "块"（Block）。这种由多个扇区组成的 "块"，是文件存取的最小单位。"块" 的大小，最常见的是 4 KB，即连续 8 个 Sector 组成一个 Block。
>
>文件数据都储存在 "块" 中，那么很显然，我们还必须找到一个地方储存文件的元信息，比如文件的创建者、文件的创建日期、文件的大小等等。这种储存文件元信息的区域就叫做 inode，中文译名为 "索引节点"。
>
>每一个文件都有对应的 inode，里面包含了与该文件有关的一些信息。
>
>某些时候，尽管一个分区的磁盘占用率未满，但是 inode 已经用完，可能是因为该分区的目录下存在大量小文件导致。尽管小文件占用的磁盘空间并不大，但是数量太多，也会导致 inode 用尽。本例中就是因为`/home/lin/share/storage_server_3`分区存在大量的小文件。

