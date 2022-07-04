*date: 2021-08-26*

## WSL 使用

WSL 全称 Windows Subsystem for Linux，是 Windows 10 上适用于 Linux 的 Windows 子系统。WSL 是一项创建轻量级环境的功能，允许安装和运行受支持的 Linux 版本（例如 Ubuntu、OpenSuse、Debian 等），而无需设置虚拟机或不同的计算机。

### 开启 WSL 功能

打开设置，点击应用，点击程序和功能，点击启用或关闭 Windows 功能，开启`适用于 Linux 的 Windows 子系统功能`，然后重启电脑。

![image-20220210100937555](linux-virtualmachine/image-20220210100937555.png)

![image-20220210101018635](linux-virtualmachine/image-20220210101018635.png)

![image-20220210101113238](linux-virtualmachine/image-20220210101113238.png)

### 升级 WSL 2

前面步骤开启的 WSL 功能，是 WSL 的原始版本，实际使用时，建议使用 WSL 2。WSL 2 是对微软在 2017 年推出的 WSL 原始版本的重大升级。WSL 2 比 WSL 更快，拥有更多功能，并且使用了真正的 Linux 内核。

第一步：开启`虚拟化功能`，对于华硕主板，开启电脑时，按 F2 进入 BIOS 模式设置，设置完成后，通过任务管理器查看是否开启。

![image-20220331100500106](linux-virtualmachine/image-20220331100500106.png)

![image-20220331100120145](linux-virtualmachine/image-20220331100120145.png)

第二步：开启`Hyper-V`虚拟化功能，然后重启电脑。

![image-20220210152909053](linux-virtualmachine/image-20220210152909053.png)

第三步：管理员身份打开 Windos PowerShell，执行以下命令，启用虚拟机平台，然后重启电脑。

```powershell
# 启动WSL功能
PS C:\Windows\system32> dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart

# 安装WSL功能模块
PS C:\WINDOWS\system32> dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
```

第四步：下载 WSL 2 内核[更新包](https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi)，然后双击安装。

![image-20220331134129196](linux-virtualmachine/image-20220331134129196.png)

第五步：管理员身份打开 Windos PowerShell，执行以下命令，设置 WSL 2 为默认值。

```powershell
PS C:\WINDOWS\system32> wsl --set-default-version 2
```

- 在使用过程中，执行`wsl --set-version <distribution name> <versionNumber>`命令，可以自行切换 WSL 版本，以 Ubuntu 为例：

  ```powershell
  PS C:\Users\Xisun> wsl --set-version Ubuntu 2
  正在进行转换，这可能需要几分钟时间...
  有关与 WSL 2 的主要区别的信息，请访问 https://aka.ms/wsl2
  转换完成。
  PS C:\Users\Xisun> wsl --list -v
    NAME      STATE           VERSION
  * Ubuntu    Running         2
  ```

  - 说明：无论安装的 Ubuntu 是哪个发行版，比如 Ubuntu 18.04 或 20.04，此处都应替换为 Ubuntu。 

### 安装 Linux 发行版

打开微软商店 Microsoft Store，搜索需要安装的 Linux 发行版，然后安装。以 Ubuntu 为例，免费下载 Ubuntu 20.04 LTS 版本：

![image-20220210164901767](linux-virtualmachine/image-20220210164901767.png)

初次安装时，获取之后安装，再点击启动按钮，会弹出一个窗口，要求输入用户名和密码：

![image-20220331104223240](linux-virtualmachine/image-20220331104223240.png)

![image-20220331104324568](linux-virtualmachine/image-20220331104324568.png)

![image-20220331093445161](linux-virtualmachine/image-20220331093445161.png)

使用`win + x 键`选择打开 Windows PowerShell，验证 WSL 功能是否正常使用：

- 查看 WSL 版本：

  ```powershell
  # Ubuntu处于运行状态
  PS C:\Users\XiSun> wsl --list -v
    NAME      		STATE           VERSION
  * Ubuntu20.04LTS    Running         2
  
  # Ubuntu处于停止状态
  PS C:\Users\XiSun> wsl --list -v
    NAME      		STATE           VERSION
  * Ubuntu20.04LTS    Stopped         2
  ```

- 管理员身份：

  ```powershell
  Windows PowerShell
  版权所有 (C) Microsoft Corporation。保留所有权利。
  
  尝试新的跨平台 PowerShell https://aka.ms/pscore6
  
  # 进入WSL模式
  PS C:\Windows\system32> wsl
  xisun@WIN-K11OM3VD9KL:/mnt/c/Windows/system32$
  ```

- 当前用户：

  ```powershell
  Windows PowerShell
  版权所有 (C) Microsoft Corporation。保留所有权利。
  
  尝试新的跨平台 PowerShell https://aka.ms/pscore6
  
  # 进入WSL模式
  PS C:\Users\XiSun> wsl
  xisun@WIN-K11OM3VD9KL:/mnt/c/Users/XiSun$
  ```

- 退出 WSL：

  ```powershell
  xisun@WIN-K11OM3VD9KL:/mnt/c/Users/XiSun$ exit
  logout
  PS C:\Users\XiSun>
  ```

- Windows PowerShell 页面不太友好，建议安装使用`Windows Terminal`：

  ![image-20220216210743600](linux-virtualmachine/image-20220216210743600.png)

如果需要卸载安装的 Linux 发行版，可以按照以下步骤执行：

![image-20220210160926017](linux-virtualmachine/image-20220210160926017.png)

### 异常情况

WSL 无法启动，提示`参考的对象类型不支持尝试的操作`，此时以管理员身份打开 Windows PowerShell，然后执行以下命令，再重启电脑即可恢复：

```powershell
Windows PowerShell
版权所有 (C) Microsoft Corporation。保留所有权利。

尝试新的跨平台 PowerShell https://aka.ms/pscore6

加载个人及系统配置文件用了 897 毫秒。
PS C:\WINDOWS\system32> wsl
参考的对象类型不支持尝试的操作。
PS C:\WINDOWS\system32> netsh winsock reset

成功地重置 Winsock 目录。
你必须重新启动计算机才能完成重置。

PS C:\WINDOWS\system32>
```

- 此方式不能彻底解决问题，可能会再次出现。

### WSL 与 Windows 互传文件

Windows 下，打开文件资源管理器，然后在目录窗口输入`\\wsl$`，回车就可以看到所有的子系统：

![image-20220520170435644](linux-virtualmachine/image-20220520170435644.png)

WSL 下，`/mnt`目录下可以访问 Windows 系统的各个盘：

```powershell
root@WIN-K11OM3VD9KL:/mnt/c/Users# cd /mnt/
root@WIN-K11OM3VD9KL:/mnt# pwd
/mnt
root@WIN-K11OM3VD9KL:/mnt# ls
c  e  f  g  wsl
```

### 迁移

WSL 默认安装在 C 盘，可通过以下方式迁移到其他磁盘，假设迁移到 E:\wsl。

首先，查看已安装的 WSL 系统，注意，不是在 WSL 系统下操作，使用普通的终端即可：

```powershell
Windows PowerShell
版权所有 (C) Microsoft Corporation。保留所有权利。

尝试新的跨平台 PowerShell https://aka.ms/pscore6

PS C:\Users\Administrator> wsl --list
适用于 Linux 的 Windows 子系统分发版:
Ubuntu20.04LTS (默认)
```

其次，将指定的 WSL 子系统备份：

```powershell
# 备份路径需要事先创建
PS C:\Users\Administrator> mkdir E:\backup


    目录: E:\


Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
d-----         2022/5/20     17:27                backup


# 名称保持一致
PS C:\Users\Administrator> wsl --export Ubuntu20.04LTS E:\backup\ubuntu.tar
```

![image-20220520173107956](linux-virtualmachine/image-20220520173107956.png)

然后，从 C 盘卸载该 WSL 子系统，并确认卸载成功：

```powershell
PS C:\Users\Administrator> wsl --unregister Ubuntu20.04LTS
正在注销...
PS C:\Users\Administrator> wsl --list
适用于 Linux 的 Windows 子系统没有已安装的分发版。
可以通过访问 Microsoft Store 来安装分发版:
https://aka.ms/wslstore
```

最后，将备份解压到指定的路径下，并确认重新安装成功：

```powershell
# 解压路径需要事先创建
PS C:\Users\Administrator> mkdir E:\programs\WSL


    目录: E:\programs


Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
d-----         2022/5/20     21:15                WSL


PS C:\Users\Administrator> wsl --import Ubuntu20.04LTS E:\programs\WSL\ E:\backup\ubuntu.tar
PS C:\Users\Administrator> wsl --list
适用于 Linux 的 Windows 子系统分发版:
Ubuntu20.04LTS (默认)
```

> 迁移之后，WSL 默认的用户是 root。

### 参考

https://docs.microsoft.com/en-us/windows/wsl/install

https://www.sitepoint.com/wsl2/

https://os.51cto.com/article/649463.html

## VMware 使用

官网：https://www.vmware.com/cn.html

登录：

![image-20220527181648724](linux-virtualmachine/image-20220527181648724.png)

下载：

![image-20220527200802283](linux-virtualmachine/image-20220527200802283.png)

![image-20220527201957037](linux-virtualmachine/image-20220527201957037.png)

![image-20220527202213176](linux-virtualmachine/image-20220527202213176.png)

![image-20220527202354067](linux-virtualmachine/image-20220527202354067.png)



### VMware 安装

安装 VMware 时，如果主机启用了 Hyper-V 功能，需要额外开启 WHP 功能：

![image-20220527204618948](linux-virtualmachine/image-20220527204618948.png)



Vmware 创建新的虚拟机前，首先配置硬件清单，安装步骤参考如下：

![image-20220527213047172](linux-virtualmachine/image-20220527213047172.png)

![image-20220527213200350](linux-virtualmachine/image-20220527213200350.png)

![image-20220527213531273](linux-virtualmachine/image-20220527213531273.png)

![image-20220527213704902](linux-virtualmachine/image-20220527213704902.png)

![image-20220527214621147](linux-virtualmachine/image-20220527214621147.png)

![image-20220527215057305](linux-virtualmachine/image-20220527215057305.png)

![image-20220527221037967](linux-virtualmachine/image-20220527221037967.png)

![image-20220527221237035](linux-virtualmachine/image-20220527221237035.png)

![image-20220527221410314](linux-virtualmachine/image-20220527221410314.png)

![image-20220527221504691](linux-virtualmachine/image-20220527221504691.png)

![image-20220527221527818](linux-virtualmachine/image-20220527221527818.png)

![image-20220527221610522](linux-virtualmachine/image-20220527221610522.png)

![image-20220527221801846](linux-virtualmachine/image-20220527221801846.png)

![image-20220527222004016](linux-virtualmachine/image-20220527222004016.png)

![image-20220527222033199](linux-virtualmachine/image-20220527222033199.png)

删除不需要的硬件，编辑虚拟机设置，然后移除 USB 控制器、声卡、打印机，这样可以使虚拟器启动的快一点：

![image-20220527223527295](linux-virtualmachine/image-20220527223527295.png)

![image-20220527223728294](linux-virtualmachine/image-20220527223728294.png)

> 如果需要，可以手动添加硬件，比如，一个网口不够，再添加一个。

### CentOS 安装

准备工作，检查 BIOS 虚拟化支持，打开任务管理器，进入性能，查看虚拟化是否启用：

![image-20220527222407813](linux-virtualmachine/image-20220527222407813.png)

> 若虚拟化未启用，重启电脑，F2 进入 BIOS 模式（不同主板快捷键不同），进入高级模式页面，Advanced ---> CPU Configuration，开启虚拟化支持。

CentOS 镜像：https://developer.aliyun.com/mirror/centos?spm=a2c6h.13651102.0.0.3e221b11se5c1r

下载地址：https://mirrors.aliyun.com/centos/

下载版本：dvd 标准安装版，选择 7.9.2009版本。

![image-20220527223200924](linux-virtualmachine/image-20220527223200924.png)

Vmware 配置 CentOS 软件，即，向虚拟机插入系统盘：

![image-20220527225352279](linux-virtualmachine/image-20220527225352279.png)

开启虚拟机，安装系统盘并配置：

![image-20220527225558168](linux-virtualmachine/image-20220527225558168.png)

![image-20220527225653989](linux-virtualmachine/image-20220527225653989.png)

- 语言环境：

  ![image-20210828143835803](linux-virtualmachine/image-20210828143835803.png)

- 日期和时间：

  ![image-20210828144044232](linux-virtualmachine/image-20210828144044232.png)

- 软件选择：

  ![image-20210828144401900](linux-virtualmachine/image-20210828144401900.png)

- 安装位置，自定义磁盘分区，配置 boot，swap 和根目录：

  ![image-20210828230742891](linux-virtualmachine/image-20210828230742891.png)

  ![image-20210828145303732](linux-virtualmachine/image-20210828145303732.png)

  ![image-20210828145447647](linux-virtualmachine/image-20210828145447647.png)

  ![image-20210828145847360](linux-virtualmachine/image-20210828145847360.png)

  ![image-20210828230512829](linux-virtualmachine/image-20210828230512829.png)

  ![image-20210828231006455](linux-virtualmachine/image-20210828231006455.png)

- 禁用 kdump 设置，如果是正式开发阶段，应该启用 kdump 设置：

  ![image-20210828231358785](linux-virtualmachine/image-20210828231358785.png)

- 网络和主机名：

  ![image-20210828232134005](linux-virtualmachine/image-20210828232134005.png)

- 安全策略：

  ![image-20210828232312406](linux-virtualmachine/image-20210828232312406.png)

- 等上面配置完成之后，开始安装系统盘：

  ![image-20210828232519213](linux-virtualmachine/image-20210828232519213.png)

  ![image-20210828232922462](linux-virtualmachine/image-20210828232922462.png)

  ![image-20210828234105781](linux-virtualmachine/image-20210828234105781.png)

  ![image-20210828234207374](linux-virtualmachine/image-20210828234207374.png)


### 配置 IP 地址和主机名称

**VMware：**

![image-20210829220432882](linux-virtualmachine/image-20210829220432882.png)

![image-20210829220525207](linux-virtualmachine/image-20210829220525207.png)

![image-20210829220827200](linux-virtualmachine/image-20210829220827200.png)

![image-20210829221133759](linux-virtualmachine/image-20210829221133759.png)

**Window 10（即本机）：**

![image-20210829221518857](linux-virtualmachine/image-20210829221518857.png)

![image-20210829221544041](linux-virtualmachine/image-20210829221544041.png)

![image-20210829221730077](linux-virtualmachine/image-20210829221730077.png)

> 如果没有出现 VMnet 8 选项，回到 VMware 的虚拟网络编辑器，点击 "更改设置" ---> "还原默认设置"，即可。

![image-20210829221917341](linux-virtualmachine/image-20210829221917341.png)

![image-20210829222140699](linux-virtualmachine/image-20210829222140699.png)

**虚拟机：**

- 切换 root 用户：

  ```bash
  [xisun@centos7 ~]$ su root
  密码：
  [root@centos7 xisun]# 

- 设置虚拟机 IP 地址：

  ```bash
  [root@centos7 xisun]# vim /etc/sysconfig/network-scripts/ifcfg-ens33
  ```

  - ifcfg-ens33 文件原内容：

    ```bash
    TYPE="Ethernet"
    PROXY_METHOD="none"
    BROWSER_ONLY="no"
    BOOTPROTO="dhcp"		# 动态获取IP地址，服务器每次开机时，IP地址可能发生改变
    DEFROUTE="yes"
    IPV4_FAILURE_FATAL="no"
    IPV6INIT="yes"
    IPV6_AUTOCONF="yes"
    IPV6_DEFROUTE="yes"
    IPV6_FAILURE_FATAL="no"
    IPV6_ADDR_GEN_MODE="stable-privacy"
    NAME="ens33"
    UUID="eb503f88-96af-455d-b8f9-dbda02ca79d4"
    DEVICE="ens33"
    ONBOOT="yes"
    ```

  - ifcfg-ens33 文件新内容：

    ```bash
    TYPE="Ethernet"
    PROXY_METHOD="none"
    BROWSER_ONLY="no"
    BOOTPROTO="static"			# 修改为静态IP地址
    DEFROUTE="yes"
    IPV4_FAILURE_FATAL="no"
    IPV6INIT="yes"
    IPV6_AUTOCONF="yes"
    IPV6_DEFROUTE="yes"
    IPV6_FAILURE_FATAL="no"
    IPV6_ADDR_GEN_MODE="stable-privacy"
    NAME="ens33"
    UUID="eb503f88-96af-455d-b8f9-dbda02ca79d4"
    DEVICE="ens33"
    ONBOOT="yes"
    
    # 新增
    IPADDR=192.168.10.99		# 静态IP地址，按需自定义
    GATEWAY=192.168.10.2		# 网关
    DNS1=192.168.10.2			# 域名解析器
    ```

- 修改虚拟机的主机名称：

  ```bash
  [root@centos7 xisun]# vim /etc/hostname
  [root@centos7 xisun]# cat /etc/hostname
  centos7												# 主机名称按需求自定义
  ```

- 修改虚拟机主机的名称映射：

  ```bash
  [root@centos7 xisun]# vim /etc/hosts
  ```

  - 原文件内容：

    ```bash
    127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
    ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
    ```

  - 新内容：

    ```bash
    127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
    ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
    
    # 按需求添加主机名称映射
    192.168.10.99 centos7
    192.168.10.100 hadoop100
    192.168.10.101 hadoop101
    192.168.10.102 hadoop102
    192.168.10.103 hadoop103
    192.168.10.104 hadoop104
    192.168.10.105 hadoop105
    192.168.10.106 hadoop106
    192.168.10.107 hadoop107
    192.168.10.108 hadoop108
    ```

  - 修改 Windows 10 主机的名称映射：`C:\Windows\System32\drivers\etc\hosts`

    ![image-20210829232429026](linux-virtualmachine/image-20210829232429026.png)

    - 如果操作系统是 Window7，可以直接修改 hosts 文件；如果操作系统是 Window10，需要先将 hosts 文件拷贝出来，修改保存以后，再覆盖原文件即可。

    - hosts 文件新增如下主机映射：

      ```bash
      192.168.10.99 centos7
      192.168.10.100 hadoop100
      192.168.10.101 hadoop101
      192.168.10.102 hadoop102
      192.168.10.103 hadoop103
      192.168.10.104 hadoop104
      192.168.10.105 hadoop105
      192.168.10.106 hadoop106
      192.168.10.107 hadoop107
      192.168.10.108 hadoop108
      ```

- 重启虚拟机：

  ```bash
  [root@centos7 xisun]# reboot
  ```

- 重启之后，以 root 用户重新登陆。

  - 验证虚拟机 IP 地址：

    ```bash
    [root@centos7 ~]# ifconfig
    ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
            inet 192.168.10.99  netmask 255.255.255.0  broadcast 192.168.10.255
            inet6 fe80::ac1e:7fe1:a566:2670  prefixlen 64  scopeid 0x20<link>
            ether 00:0c:29:1c:d5:13  txqueuelen 1000  (Ethernet)
            RX packets 2033  bytes 2797234 (2.6 MiB)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 924  bytes 61834 (60.3 KiB)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
    
    lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
            inet 127.0.0.1  netmask 255.0.0.0
            inet6 ::1  prefixlen 128  scopeid 0x10<host>
            loop  txqueuelen 1000  (Local Loopback)
            RX packets 48  bytes 4080 (3.9 KiB)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 48  bytes 4080 (3.9 KiB)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
    
    virbr0: flags=4099<UP,BROADCAST,MULTICAST>  mtu 1500
            inet 192.168.122.1  netmask 255.255.255.0  broadcast 192.168.122.255
            ether 52:54:00:97:ed:a7  txqueuelen 1000  (Ethernet)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
    
    ```

  - 验证是否能连通外网：

    ```bash
    [root@centos7 ~]# ping www.baidu.com 
    PING www.a.shifen.com (14.215.177.39) 56(84) bytes of data.
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=1 ttl=128 time=38.4 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=2 ttl=128 time=38.6 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=3 ttl=128 time=38.4 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=4 ttl=128 time=39.3 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=5 ttl=128 time=38.3 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=6 ttl=128 time=38.6 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=7 ttl=128 time=38.3 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=8 ttl=128 time=38.4 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=9 ttl=128 time=38.9 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=10 ttl=128 time=38.3 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=11 ttl=128 time=38.7 ms
    64 bytes from 14.215.177.39 (14.215.177.39): icmp_seq=12 ttl=128 time=38.3 ms
    ^C
    --- www.a.shifen.com ping statistics ---
    12 packets transmitted, 12 received, 0% packet loss, time 11035ms
    rtt min/avg/max/mdev = 38.357/38.606/39.328/0.332 ms
    ```

  - 查看主机地址：

    ```bash
    [root@centos7 ~]# hostname
    centos7
    ```

### 安装 JDK

安装的 centos7 模板机，可能有自带的 JDK，某些情况，需要删除：

```bash
[xisun@centos7 ~]$ su root
密码：
[root@centos7 ~]# rpm -qa | grep -i java
javapackages-tools-3.4.1-11.el7.noarch
tzdata-java-2019c-1.el7.noarch
java-1.8.0-openjdk-headless-1.8.0.242.b08-1.el7.x86_64
java-1.8.0-openjdk-1.8.0.242.b08-1.el7.x86_64
java-1.7.0-openjdk-headless-1.7.0.251-2.6.21.1.el7.x86_64
python-javapackages-3.4.1-11.el7.noarch
java-1.7.0-openjdk-1.7.0.251-2.6.21.1.el7.x86_64
[root@centos7 ~]# rpm -qa | grep -i java | xargs -n1 rpm -e --nodeps
[root@centos7 ~]# rpm -qa | grep -i java
```

重新安装 OpenJDK，官网：http://openjdk.java.net/install/

![image-20220529122300127](linux-virtualmachine/image-20220529122300127.png)

```bash
[root@centos7 ~]# yum install -y java-1.8.0-openjdk-devel
```

> `yum install -y java-1.8.0-openjdk`命令安装的是 JRE，`yum install -y java-1.8.0-openjdk-devel`命令安装的是 JDK。

验证安装成功：

```bash
[root@hadoop100 ~]# java -version
openjdk version "1.8.0_332"
OpenJDK Runtime Environment (build 1.8.0_332-b09)
OpenJDK 64-Bit Server VM (build 25.332-b09, mixed mode)
```

寻找 JDK 安装路径：

```bash
[root@hadoop100 ~]# ls -lrt /usr/bin/java
lrwxrwxrwx. 1 root root 22 5月  29 12:22 /usr/bin/java -> /etc/alternatives/java
[root@hadoop100 ~]# ls -lrt /etc/alternatives/java
lrwxrwxrwx. 1 root root 73 5月  29 12:22 /etc/alternatives/java -> /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64/jre/bin/java
```

配置全局环境变量：

```bash
[root@centos7 ~]# vi /etc/profile
```

在 profile 文件最后添加：

```bash
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
PATH=$PATH:$JAVA_HOME/bin
export JAVA_HOME
export CLASSPATH
export PATH
```

>export 是把这三个变量导出为全局变量。

生效配置信息，并验证是否安装成功：

```bash
[root@hadoop100 ~]# source /etc/profile
```

全局环境变量配置时，所有用户的 shell 都有权使用这些环境变量，可能会给系统带来安全性问题。

因此，修改`.bashrc`文件这种方法更为安全，它可以把使用这些环境变量的权限控制到用户级别，如果需要给某个用户权限使用这些环境变量，只需要修改其个人用户主目录下的 .bashrc 文件就可以了。用文本编辑器打开用户目录下的 .bashrc 文件，在 .bashrc 文件末尾加入：

```bash
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
PATH=$PATH:$JAVA_HOME/bin

set JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64
export JAVA_HOME
set CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export CLASSPATH
set PATH=$PATH:$JAVA_HOME/bin
export PATH
```

然后，生效配置信息：

```bash
root@hadoop100 ~]# source .bashrc
```

### 克隆虚拟机

新配置的 centos7 虚拟机，可以作为一个纯净的虚拟机，在此基础上，克隆出新的虚拟机，在新虚拟机上安装软件，而纯净的虚拟机留作备用。

克隆虚拟机之前，需要正确的关闭虚拟机：

![image-20210830151900185](linux-virtualmachine/image-20210830151900185.png)

**克隆：**

![image-20210830152407112](linux-virtualmachine/image-20210830152407112.png)

![image-20220528133348075](linux-virtualmachine/image-20220528133348075.png)

![image-20210830153137306](linux-virtualmachine/image-20210830153137306.png)

![image-20210830153347287](linux-virtualmachine/image-20210830153347287.png)

![image-20210830153523350](linux-virtualmachine/image-20210830153523350.png)

![image-20210830154101255](linux-virtualmachine/image-20210830154101255.png)

**常规操作：**

- 移除，此操作只会在 VMware 列表中移除虚拟机，但不会删除磁盘上的虚拟机：

  ![image-20210830154158479](linux-virtualmachine/image-20210830154158479.png)

- 添加，通过打开操作，可以添加磁盘上的虚拟机到 VMware 列表中：

  ![image-20210830154443435](linux-virtualmachine/image-20210830154443435.png)

- 删除，此操作会将磁盘上的虚拟机删除：

  ![image-20210830154727908](linux-virtualmachine/image-20210830154727908.png)

### 修改克隆机的 IP 地址和主机名称

克隆机的信息，和被克隆机相同，需要修改 IP 地址，以及主机名称。

开启克隆机，以 root 用户登录，按以下步骤修改 IP 地址：

```bash
[root@centos7 ~]# vim /etc/sysconfig/network-scripts/ifcfg-ens32
```

- 修改 ifcfg-ens32 文件中的 IPADDR：

  ```bash
  TYPE="Ethernet"
  PROXY_METHOD="none"
  BROWSER_ONLY="no"
  BOOTPROTO="static"
  DEFROUTE="yes"
  IPV4_FAILURE_FATAL="no"
  IPV6INIT="yes"
  IPV6_AUTOCONF="yes"
  IPV6_DEFROUTE="yes"
  IPV6_FAILURE_FATAL="no"
  IPV6_ADDR_GEN_MODE="stable-privacy"
  NAME="ens33"
  UUID="eb503f88-96af-455d-b8f9-dbda02ca79d4"
  DEVICE="ens33"
  ONBOOT="yes"
  
  IPADDR=192.168.10.100		# 只需要将IP地址按需修改即可
  GATEWAY=192.168.10.2
  DNS1=192.168.10.2
  ```

- 修改主机名称：

  ```bash
  [root@centos7 ~]# vim /etc/hostname
  [root@centos7 ~]# cat /etc/hostname
  hadoop100
  ```

- 重启：

  ```bash
  [root@centos7 ~]# reboot
  ```

- 查看新的 IP 地址和主机名称：

  ```bash
  [root@hadoop100 ~]# ifconfig
  ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
          inet 192.168.10.100  netmask 255.255.255.0  broadcast 192.168.10.255
          inet6 fe80::ac1e:7fe1:a566:2670  prefixlen 64  scopeid 0x20<link>
          ether 00:0c:29:1d:f8:56  txqueuelen 1000  (Ethernet)
          RX packets 604  bytes 804811 (785.9 KiB)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 279  bytes 21418 (20.9 KiB)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
          inet 127.0.0.1  netmask 255.0.0.0
          inet6 ::1  prefixlen 128  scopeid 0x10<host>
          loop  txqueuelen 1000  (Local Loopback)
          RX packets 48  bytes 4080 (3.9 KiB)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 48  bytes 4080 (3.9 KiB)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  virbr0: flags=4099<UP,BROADCAST,MULTICAST>  mtu 1500
          inet 192.168.122.1  netmask 255.255.255.0  broadcast 192.168.122.255
          ether 52:54:00:97:ed:a7  txqueuelen 1000  (Ethernet)
          RX packets 0  bytes 0 (0.0 B)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 0  bytes 0 (0.0 B)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  [root@hadoop100 ~]# hostname
  hadoop100
  ```

### 参考

https://www.bilibili.com/video/BV1Qp4y1n7EN

### 补充：Ubuntu 安装

桌面版下载：https://ubuntu.com/download/desktop

![image-20220630162643336](linux-virtualmachine/image-20220630162643336.png)

> 上面的是桌面版。

非桌面版下载：https://ubuntu.com/download/server

![image-20220630163552967](linux-virtualmachine/image-20220630163552967.png)

![image-20220630163701129](linux-virtualmachine/image-20220630163701129.png)

> 可以使用清华源镜像地址下载：https://mirrors.tuna.tsinghua.edu.cn/ubuntu-cdimage/ubuntu/releases/20.04.4/release/ubuntu-20.04.4-live-server-arm64.iso
>
> 桌面版与非桌面版的区别：非桌面版的软件需要自己安装，桌面版自带了很多安装好的软件。

其他步骤参考 CentOS 安装，需要修改的地方如下：

<img src="linux-virtualmachine/image-20220630163047226.png" alt="image-20220630163047226" style="zoom: 50%;" />

<img src="linux-virtualmachine/image-20220630165011882.png" alt="image-20220630165011882" style="zoom:50%;" />

<img src="linux-virtualmachine/image-20220630170138476.png" alt="image-20220630170138476" style="zoom:50%;" />

<img src="linux-virtualmachine/image-20220630170422620.png" alt="image-20220630170422620" style="zoom:50%;" />

Vmware 配置 Ubuntu 软件，即，向虚拟机插入系统盘：

![image-20220630173458690](linux-virtualmachine/image-20220630173458690.png)

> 如果选择非桌面版镜像文件，安装系统的时候出现了`Operating System not found`异常，暂不明确异常原因，因此，后续选择桌面版镜像文件。

开启虚拟机，安装系统盘并配置，当选择桌面版镜像文件时，推荐最小化安装：

<img src="linux-virtualmachine/image-20220630175311944.png" alt="image-20220630175311944" style="zoom:50%;" />

<img src="linux-virtualmachine/image-20220630175609327.png" alt="image-20220630175609327" style="zoom: 67%;" />

<img src="linux-virtualmachine/image-20220630175941141.png" alt="image-20220630175941141" style="zoom:67%;" />

<img src="linux-virtualmachine/image-20220630180022743.png" alt="image-20220630180022743" style="zoom:67%;" />

<img src="linux-virtualmachine/image-20220630181828463.png" alt="image-20220630181828463" style="zoom:67%;" />

系统安装完成后，可以使用安装过程中创建的用户名和密码登录，然后修改下 root 密码：

```bash
xisun@xisun-virtual-machine:~/Desktop$ sudo passwd root
New password: 
BAD PASSWORD: The password is shorter than 8 characters
Retype new password: 
passwd: password updated successfully
```

安装 vim：

```bash
$ apt remove vim-common
$ apt install vim
```

> Ubuntu 默认安装装的是 vim tiny 版本，使用时会出现上下左右方向键变为 ABCD 的情况，因此，将其移除，安装 vim full 版本。

安装 openssh-server：

```bash
$ apt install openssh-server
```

> 不安装 openssh-server，Xshell 无法通过 SSH 连接虚拟机。

查看 22 端口：

```bash
$ netstat -ntlp | grep 22
```

修改虚拟机 hostname：

```bash
$ vim /etc/hostname
$ hostname
```

配置虚拟机静态 IP 地址，Ubuntu 从 17.10 开始，放弃在 /etc/network/interfaces 里面配置 IP，改为在`/etc/netplan/XX-installer-config.yaml`的 yaml 文件中配置 IP 地址。（以下命令需要使用 root 权限执行）

> 关于 VMware 和 Windows 主机的地址修改，参考 Centos，下面的内容只涉及虚拟机的静态 IP 配置。

查看网络配置信息：

```bash
# 方式一
xisun@xisun-virtual-machine:~$ ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.10.128  netmask 255.255.255.0  broadcast 192.168.10.255
        inet6 fe80::ec53:34d9:1134:ce0b  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:14:62:e0  txqueuelen 1000  (Ethernet)
        RX packets 216049  bytes 316602255 (316.6 MB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 16993  bytes 2432653 (2.4 MB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 717  bytes 111403 (111.4 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 717  bytes 111403 (111.4 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

# 方式二
xisun@xisun-virtual-machine:~$ ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    link/ether 00:0c:29:14:62:e0 brd ff:ff:ff:ff:ff:ff
    altname enp2s1
    inet 192.168.10.128/24 brd 192.168.10.255 scope global dynamic noprefixroute ens33
       valid_lft 1534sec preferred_lft 1534sec
    inet6 fe80::ec53:34d9:1134:ce0b/64 scope link noprefixroute 
       valid_lft forever preferred_lft forever
```

修改配置文件：

```bash
$ vim /etc/netplan/01-network-manager-all.yaml
```

```yaml
network:
  version: 2
  renderer: NetworkManager
  # 根据自身需要，添加以下配置
  ethernets:
    ens33:	# 配置的网卡的名称
      addresses: [192.168.10.99/24]	# 配置的静态ip地址和掩码
      dhcp4: false	# 关闭dhcp4(动态IP)
      gateway4: 192.168.10.2	# 网关地址
      nameservers:
        addresses: [192.168.10.2]	# DNS服务器地址，多个DNS服务器地址需要用英文逗号分隔开，可不配置
```

使配置生效：

```bash
$ netplan apply
```

查看修改后的 IP 地址：

```bash
xisun@xisun-virtual-machine:~$ ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.10.99  netmask 255.255.255.0  broadcast 192.168.10.255
        inet6 fe80::20c:29ff:fe14:62e0  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:14:62:e0  txqueuelen 1000  (Ethernet)
        RX packets 224034  bytes 325714476 (325.7 MB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 19050  bytes 2666184 (2.6 MB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 776  bytes 119231 (119.2 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 776  bytes 119231 (119.2 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

xisun@xisun-virtual-machine:~$ ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    link/ether 00:0c:29:14:62:e0 brd ff:ff:ff:ff:ff:ff
    altname enp2s1
    inet 192.168.10.99/24 brd 192.168.10.255 scope global noprefixroute ens33
       valid_lft forever preferred_lft forever
    inet6 fe80::20c:29ff:fe14:62e0/64 scope link 
       valid_lft forever preferred_lft forever
```

## Xshell 远程连接虚拟机

Xshell 安装过程略。

远程连接配置：

![image-20210829231009322](linux-virtualmachine/image-20210829231009322.png)

![image-20210829231122803](linux-virtualmachine/image-20210829231122803.png)

连接成功：

![image-20210829231402711](linux-virtualmachine/image-20210829231402711.png)

数据传输：安装`Xftp`工具，或者使用`rz`和`sz`命令。

## Linux 系统基本工具包

```bash
$ apt install net-tools
```

```bash
$ apt install lrzsz
```

`// TODO`

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
