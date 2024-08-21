*date: 2023-06-26*

## 基础配置

### 修改 root 密码

```sh
$ sudo passwd
```

### 固定 IP

操作系统：

```sh
$ lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description:    Ubuntu 23.04
Release:        23.04
Codename:       lunar
```

查看网卡信息：

```sh
$ ip link show
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN mode DEFAULT group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP mode DEFAULT group default qlen 1000
    link/ether 00:15:5d:02:87:02 brd ff:ff:ff:ff:ff:ff
3: docker0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state DOWN mode DEFAULT group default 
    link/ether 02:42:cb:88:61:ea brd ff:ff:ff:ff:ff:ff
4: br-04616b96f94b: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP mode DEFAULT group default 
    link/ether 02:42:95:2a:f5:3f brd ff:ff:ff:ff:ff:ff
```

- `ip link show`命令， 会输出所有的网络接口名称，需要找到想设置固定 IP 的网卡名称。一般情况下，它会被标记为 "eth0" 或 "enpXsX"（X 为数字）等。`ip a`命令是详细版。

打开并编辑 netplan 配置文件：

```sh
$ sudo vim /etc/netplan/00-installer-config.yaml
# This is the network config written by 'subiquity'
#network:
# ethernets:
#   eth0:
#     dhcp4: true
# version: 2

network:
  ethernets:
    eth0:
      dhcp4: no
      addresses: [192.168.2.100/24]
      gateway4: 192.168.2.1
      nameservers:
        addresses: [8.8.8.8,8.8.4.4]
  version: 2
```

生效配置：

```sh
$ sudo netplan apply
```

### 时区设置

```sh
# 查看时区
$ date -R
Thu, 03 Aug 2023 17:19:48 +0800

# 修改时区
$ sudo tzselect
[sudo] password for xx: 
Please identify a location so that time zone rules can be set correctly.
Please select a continent, ocean, "coord", or "TZ".
1) Africa                                                            7) Europe
2) Americas                                                          8) Indian Ocean
3) Antarctica                                                        9) Pacific Ocean
4) Asia                                                             10) coord - I want to use geographical coordinates.
5) Atlantic Ocean                                                   11) TZ - I want to specify the timezone using the Posix TZ format.
6) Australia
#? 4
Please select a country whose clocks agree with yours.
 1) Afghanistan              13) Cyprus                   25) Kazakhstan               37) Oman                     49) Taiwan
 2) Antarctica               14) East Timor               26) Korea (North)            38) Pakistan                 50) Tajikistan
 3) Armenia                  15) French S. Terr.          27) Korea (South)            39) Palestine                51) Thailand
 4) Azerbaijan               16) Georgia                  28) Kuwait                   40) Philippines              52) Turkmenistan
 5) Bahrain                  17) Hong Kong                29) Kyrgyzstan               41) Qatar                    53) United Arab Emirates
 6) Bangladesh               18) India                    30) Laos                     42) Russia                   54) Uzbekistan
 7) Bhutan                   19) Indonesia                31) Lebanon                  43) Réunion                  55) Vietnam
 8) Brunei                   20) Iran                     32) Macau                    44) Saudi Arabia             56) Yemen
 9) Cambodia                 21) Iraq                     33) Malaysia                 45) Seychelles
10) China                    22) Israel                   34) Mongolia                 46) Singapore
11) Christmas Island         23) Japan                    35) Myanmar (Burma)          47) Sri Lanka
12) Cocos (Keeling) Islands  24) Jordan                   36) Nepal                    48) Syria
#? 10
Please select one of the following timezones.
1) Beijing Time
2) Xinjiang Time, Vostok
#? 1

The following information has been given:

        China
        Beijing Time

Therefore TZ='Asia/Shanghai' will be used.
Selected time is now:   Thu Aug  3 17:20:09 CST 2023.
Universal Time is now:  Thu Aug  3 09:20:09 UTC 2023.
Is the above information OK?
1) Yes
2) No
#? 1
You can make this change permanent for yourself by appending the line
        TZ='Asia/Shanghai'; export TZ
to the file '.profile' in your home directory; then log out and log in again.

Here is that TZ value again, this time on standard output so that you
can use the /usr/bin/tzselect command in shell scripts:
Asia/Shanghai

# 复制
$ sudo cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime

# 设置 24 小时制
$ sudo vim /etc/default/locale
LC_TIME=en_DK.utf-8

# 重启
$ sudo reboot
```

### 设置 apt 镜像源

```shell
# 备份 sources.list
$ cd /etc/apt
$ sudo cp sources.list sources.list.bak

# 删除 sources.list 文件中原内容，替换成下面的阿里云镜像源（对应的是 ubuntu 23.04 (lunar)）
$ sudo vim sources.list
deb https://mirrors.aliyun.com/ubuntu/ lunar main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ lunar main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ lunar-security main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ lunar-security main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ lunar-updates main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ lunar-updates main restricted universe multiverse

# deb https://mirrors.aliyun.com/ubuntu/ lunar-proposed main restricted universe multiverse
# deb-src https://mirrors.aliyun.com/ubuntu/ lunar-proposed main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ lunar-backports main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ lunar-backports main restricted universe multiverse

# 更新配置
$ sudo apt update
$ sudo apt upgrade
```

>阿里云 Ubuntu 镜像源：https://developer.aliyun.com/mirror/ubuntu/

### 传输工具安装

```sh
$ apt install lrzsz
```

### jdk 安装

```sh
# 1. 下载
$ wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz

# 2. 解压
$ tar -zxvf jdk-17_linux-x64_bin.tar.gz

# 3. 移动
$ sudo mv jdk-17.0.10/ /usr/local

# 4. 配置环境变量
$ sudo vim /etc/profile

# 5. 生效配置
$ source /etc/profile

# 6. 检查
$ java -version
```

> 环境变量，追加在 /etc/profile 文件最后：
>
> ```sh
> export JAVA_HOME=/usr/local/jdk-17.0.10
> export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
> 
> export PATH=$JAVA_HOME/bin:$PATH
> ```

### maven 安装

```sh
# 1. 下载
$ wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz

# 2. 解压
$ tar xf apache-maven-3.9.6-bin.tar.gz

# 3. 移动
$ sudo mv apache-maven-3.9.6 /usr/local/

# 4. 配置环境变量
$ sudo vim /etc/profile

# 5. 生效配置
$ source /etc/profile

# 6. 配置本地仓库路径和镜像仓库
$ vim /usr/local/apache-maven-3.9.6/conf/settings.xml

# 7. 检查
$ mvn -version
```

>环境变量，追加在 /etc/profile 文件最后：
>
>```sh
>export MAVEN_HOME=/usr/local/apache-maven-3.9.6
>
>export PATH=$MAVEN_HOME/bin:$PATH
>```
>
>本地仓库路径和镜像仓库配置：
>
>```sh
><localRepository>/path/to/local/repo</localRepository>
>
><mirror>
>  <id>alimaven</id>
>  <name>aliyun maven</name>
>  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
>  <mirrorOf>central</mirrorOf>
></mirror>
>```

### python 环境



### nodejs 环境

首先，下载对应的官方安装包，https://nodejs.org/en/download/

<img src="./deploy/image-20240316232500762.png" alt="image-20240316232500762" style="zoom: 67%;" />

```sh
# 1. 上传到服务器，并解压
$ tar xf node-v20.11.1-linux-x64.tar.xz

# 2. 移动
$ sudo mv node-v20.11.1-linux-x64 /usr/local/

# 3. 配置环境变量
$ sudo vim /etc/profile
$ source /etc/profile

# 4. 验证
$ node -v
$ npm -v

# 5. 设置npm国内淘宝源
# npm中国镜像站地址: https://npmmirror.com/
$ npm config set registry https://registry.npmmirror.com

# 还原为初始源
# $ npm config set registry https://registry.npmjs.org/
```

>环境变量，追加在 /etc/profile 文件最后：
>
>```sh
>export NODE_HOME=/usr/local/node-v20.11.1-linux-x64
>
>export PATH=$NODE_HOME/bin:$PATH
>```

### docker 安装

1. 官网下载安装包。（以下示例脚本为下载最新版 docker 离线压缩包，也可以用浏览器打开官网地址直接下载再传到服务器。如果使用该脚本，下面的第 2、3 步可以不执行。）

   ```bash
   #!/bin/bash
   
   set -u
   # docker 官网下载地址
   url='https://download.docker.com/linux/static/stable/x86_64/'
   # 获取最新版的 docker 包名，地址：https://download.docker.com/linux/static/stable/x86_64/
   tarFile=$(curl -s ${url} | grep -E '.*docker-[0-9]*\.[0-9]*\.[0-9]*\.tgz' | tail -n 1 | awk -F '"' '{print $2}')
   # 拼接完整url并下载
   curl -o docker.tgz "${url}${tarFile}"
   
   # 解压，将压缩包内的文件挪到 /usr/local/bin
   tar xf docker.tgz
   mv docker/* /usr/local/bin/
   
   # 创建 docker 的数据目录
   mkdir -p /home/data/docker
   ```

   ```sh
   $ sudo bash docker.sh
   ```

2. 解压安装包，将安装包内的二进制程序解压到`/usr/local/bin`目录。

   ```sh
   $ tar xf docker.tgz
   $ mv docker/* /usr/local/bin/
   ```

3. 建立 docker 的数据存放目录`/home/data/docker`。

   ```sh
   $ mkdir -p /home/data/docker
   ```

4. 创建 docker 的 service 文件`/usr/lib/systemd/system/docker.service`。

   ```sh
   $ sudo vim /usr/lib/systemd/system/docker.service
   ```

   ```sh
   [Unit]
   Description=Docker Application Container Engine
   Documentation=https://docs.docker.com
   After=network-online.target firewalld.service
   Wants=network-online.target
   
   [Service]
   Type=notify
   # the default is not to use systemd for cgroups because the delegate issues still
   # exists and systemd currently does not support the cgroup feature set required
   # for containers run by docker
   ExecStart=/usr/local/bin/dockerd --data-root /home/data/docker
   
   ExecReload=/bin/kill -s HUP $MAINPID
   # Having non-zero Limit*s causes performance problems due to accounting overhead
   # in the kernel. We recommend using cgroups to do container-local accounting.
   LimitNOFILE=infinity
   LimitNPROC=infinity
   LimitCORE=infinity
   # Uncomment TasksMax if your systemd version supports it.
   # Only systemd 226 and above support this version.
   #TasksMax=infinity
   TimeoutStartSec=0
   # set delegate yes so that systemd does not reset the cgroups of docker containers
   Delegate=yes
   # kill only the docker process, not all processes in the cgroup
   KillMode=process
   # restart the docker process if it exits prematurely
   Restart=on-failure
   StartLimitBurst=3
   StartLimitInterval=60s
   
   [Install]
   WantedBy=multi-user.target
   ```

5. 配置 docker 的 daemon.json。

   ```sh
   $ sudo vim /etc/docker/daemon.json
   ```

   ```sh
   {
       "registry-mirrors": [
           "https://dockerproxy.com",
           "https://hub-mirror.c.163.com",
           "https://mirror.baidubce.com",
           "https://ccr.ccs.tencentyun.com"
       ]
   }
   ```

   - /etc 目录下可能没有 docker 目录和 daemon.json，需要新建。
   - 检查 daemon.json 文件的格式：`cat /etc/docker/daemon.json | python -m json.tool`。

6. 生效配置。

   ```sh
   $ systemctl daemon-reload
   $ systemctl start docker
   $ systemctl enable docker
   ```

7. 验证。

   ```sh
   $ systemctl status docker
   $ docker info
   ```

8. 普通用户如需使用二进制方式安装的 docker，可参考以下步骤，假设普通用户的用户名为 admin。

   ```sh
   # 1. 创建 docker 用户组
   $ sudo groupadd docker
   # 2. 将 admin 用户加到 docker 用户组
   $ sudo usermod admin -a -G docker
   # 3. 重启 docker
   $ systemctl restart docker
   # 4. admin 用户执行 docker 命令进行测试
   ```

9. 如果安装过程中出现错误，使用命令查看详细的错误日志。

   ```sh
   $ systemctl status docker.service
   $ journalctl -xeu docker | less
   ```

> 补充，自用安装脚本，需联网。
>
> ```bash
> #!/bin/bash
> # description: 国内联网情况下通过 docker 二进制包安装 docker 和 docker-compose
> 
> set -u
> dataDir='/home/apps/docker'
> 
> downloadDocker(){
>     # docker 官网下载地址
>     local url='https://download.docker.com/linux/static/stable/x86_64/'
>     # 获取最新版的 docker 包名
>     local tarFile=$(curl -s ${url} | grep -E '.*docker-[0-9]*\.[0-9]*\.[0-9]*\.tgz' | tail -n 1 | awk -F '"' '{print $2}')
>     # 拼接完整 url 并下载
>     curl -s -o docker.tgz "${url}${tarFile}"
> 
>     # 解压，将压缩包内的文件挪到 /usr/local/bin
>     tar xf docker.tgz
>     mv docker/* /usr/local/bin/
> 
>     # 创建 docker 的数据目录
>     mkdir -p ${dataDir}
>     echo -e "\n\t>>> docker data dir: ${dataDir} <<<\n"
> 
>     # （可选）创建 docker 用户组
>     groupadd docker
> }
> 
> downloadDockerCompose() {
>     # 获取最新版 tag
>     local tag=$(curl -s "https://api.github.com/repos/docker/compose/tags" | grep '"name":' | head -n 1 | awk -F '"' '{print $4}')
>     # 通过 ghproxy 的代理拼接下载 url
>     local url="https://ghproxy.com/https://github.com/docker/compose/releases/download/${tag}/docker-compose-linux-x86_64"
> 
>     curl -s -o docker-compose ${url}
>     chmod +x docker-compose
>     mv docker-compose /usr/local/bin
> }
> 
> createSystemConfig() {
>     cat > docker.service <<EOF
> [Unit]
> Description=Docker Application Container Engine
> Documentation=https://docs.docker.com
> After=network-online.target firewalld.service
> Wants=network-online.target
> 
> [Service]
> Type=notify
> ExecStart=/usr/local/bin/dockerd --data-root ${dataDir}
> 
> ExecReload=/bin/kill -s HUP $MAINPID
> LimitNOFILE=infinity
> LimitNPROC=infinity
> LimitCORE=infinity
> 
> #TasksMax=infinity
> TimeoutStartSec=0
> 
> Delegate=yes
> 
> KillMode=process
> 
> Restart=on-failure
> StartLimitBurst=3
> StartLimitInterval=60s
> 
> [Install]
> WantedBy=multi-user.target
> EOF
>     mv docker.service /etc/systemd/system/
> 
>     systemctl daemon-reload
>     systemctl start docker
>     systemctl enable docker
> }
> 
> # 生成 docker 的配置文件
> createDockerConfig() {
>     if [ ! -d "/etc/docker" ]; then
>         echo -e "\n\t>>> /etc/docker is not exist. <<<\n"
>         return
>     fi
> 
>     if [ -f "/etc/docker/daemon.json" ]; then
>         echo -e "\n\t>>> /etc/docker/daemon.json is exist. <<<\n"
>         return
>     fi
> 
>     cat > /etc/docker/daemon.json <<EOF
> {
>     "log-driver": "json-file",
>     "log-level": "warn",
>     "log-opts": {
>         "max-size": "100m"
>     }
> }
> EOF
>     systemctl daemon-reload
>     systemctl restart docker
> }
> 
> main() {
>     # 检测当前用户是否为 root 用户
>     if [ $(whoami) != 'root' ]; then
>         echo -e "\n\t>>> Please use \033[31mroot\033[0m privilege to run this script. <<<\n"
>         exit 1
>     fi
> 
>     # 检测 docker 是否已安装
>     docker --version > /dev/null 2>&1
>     if [ $? -eq 0 ]; then
>         echo -e "\n\t\033[31m>>> Docker is already installed. <<<\033[0m"
>         echo -e "\t\033[31m>>> If you want to reinstall, please uninstall it first. <<<\033[0m\n"
>         exit 1
>     fi
> 
>     downloadDocker
>     downloadDockerCompose
>     createSystemConfig
>     createDockerConfig
> }
> 
> main
> ```

>补充：docker 官方安装。
>
>官网：https://www.docker.com/
>
>Ubuntu 安装：https://docs.docker.com/engine/install/ubuntu/
>
>1. 卸载冲突包。
>
>  ```sh
>  $ for pkg in docker.io docker-doc docker-compose podman-docker containerd runc; do sudo apt-get remove $pkg; done
>  ```
>
>  - 卸载冲突包后，保存在`/var/lib/docker/`路径的镜像、容器、数据卷、网络等，不会自动删除，如果想彻底删除，参考：https://docs.docker.com/engine/install/ubuntu/#uninstall-docker-engine。
>
>2. 设置 docker 仓库。
>
>  ```sh
>  # 更新 apt 软件包索引和安装包, 以允许 apt 使用 HTTPS 上的存储库
>  $ sudo apt-get update
>  $ sudo apt-get install ca-certificates curl gnupg
>
>  # 添加 docker 的官方 GPG 密钥
>  $ sudo install -m 0755 -d /etc/apt/keyrings
>  $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
>  $ sudo chmod a+r /etc/apt/keyrings/docker.gpg
>
>  # 设置仓库
>  $ echo \
>    "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
>    "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
>    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
>  ```
>
>3. 安装 docker 引擎。
>
>  ```sh
>  $ sudo apt-get update
>  $ sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
>  ```
>
>  - 安装 dokcer 引擎的时候，也安装了 docker-compose 插件。
>
>4. 设置镜像源。
>
>  ```sh
>  # 设置镜像源，新建一个 daemon.json 文件，然后添加如下内容
>  $ sudo vim /etc/docker/daemon.json
>  $ sudo cat /etc/docker/daemon.json
>  {
>      "registry-mirrors": [
>          "https://dockerproxy.com",
>          "https://hub-mirror.c.163.com",
>          "https://mirror.baidubce.com",
>          "https://ccr.ccs.tencentyun.com"
>      ]
>  }
>  ```
>
>5. 验证是否安装成功。
>
>  ```sh
>  $ sudo docker run hello-world
>  ```
>
>6. 添加当前用户到 docker 组。
>
>  ```sh
>  # 当前用户无权限
>  $ docker ps
>  permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/containers/json": dial unix /var/run/docker.sock: connect: permission denied
>
>  # 添加当前用户到 docker 组
>  $ sudo gpasswd -a ${USER} docker
>  [sudo] password for xisun: 
>  Adding user xisun to group docker
>
>  # 退出当前用户, 比如切换为 root, 再切换为 xisun
>  $ sudo su -
>  $ su xisun 
>
>  # 有权限
>  $ docker ps
>  CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
>  ```

>官网：https://www.docker.com/
>
>Ubuntu 安装：https://docs.docker.com/compose/install/linux/
>
>Github：https://github.com/docker/compose
>
>releases：https://github.com/docker/compose/releases
>
>1. docker 引擎安装好之后，可以直接使用以下命令安装 docker-compose。
>
>   ```sh
>   $ sudo apt-get update
>   $ sudo apt-get install docker-compose-plugin
>   ```
>
>2. 验证 docker-compose 是否安装成功。
>
>   ```sh
>   $ docker compose version
>   ```
>
>3. 更新 docker-compose。
>
>   ```sh
>   $ sudo apt-get update
>   $ sudo apt-get install docker-compose-plugin
>   ```

### docker-compose 安装

1. 官网下载二进制程序包（以下示例脚本为通过代理站从 GitHub 下载最新版 docker-compose 的安装包，也可以用浏览器直接从浏览器下载再传到服务器。如果使用该脚本，下面的第 2、3 步可以不执行。）

   ```bash
   #!/bin/bash
   
   set -eu
   tag=$(curl -s "https://api.github.com/repos/docker/compose/tags" | grep '"name":' | head -n 1 | awk -F '"' '{print $4}')
   
   url="https://github.com/docker/compose/releases/download/${tag}/docker-compose-$(uname -s)-$(uname -m)"
   
   curl -o docker-compose ${url}
   
   chmod +x docker-compose
   
   # 将 docker-compose 挪到 PATH 环境变量的路径
   mv docker-compose /usr/local/bin/
   
   # 验证并查看版本
   docker-compose --version
   ```

   ```sh
   $ sudo bash docker-compose.sh
   ```

2. 重命名为 docker-compose，并移到`/usr/local/bin`。

3. 验证。

> 如果脚本执行异常，下载的 docker-compose 安装包不正确，可以手动安装。（因为下载地址是 GitHub，如果没有配置代理，下载速度会很慢，甚至失败。）
>
> ```sh
> # 1. 查看最新版本
> $ curl -s "https://api.github.com/repos/docker/compose/tags" | grep '"name":' | head -n 1 | awk -F '"' '{print $4}'
> 
> # 2. 下载，如需下载其他版本，替换 v2.24.7 为最新的版本号即可
> $ sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.7/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
> 
> # 3. 赋予二进制文件可执行权限
> $ sudo chmod +x /usr/local/bin/docker-compose
> ```

### docker 配置 clash 代理

国内镜像源偶尔会出现不能使用的情况，在已有代理的情况下，按如下方式配置：

```sh
# 1. 编辑 daemon.json 文件，添加代理，192.168.1.17:7890 是宿主机局域网中的代理服务器地址（国内镜像源的配置需要删除）
$ vim /etc/docker/daemon.json 
{
    "proxies": {
        "http-proxy": "http://192.168.1.17:7890",
        "https-proxy": "http://192.168.1.17:7890"
    }
}

# 2. 生效配置
$ sudo systemctl daemon-reload
$ sudo systemctl restart docker
```

代理配置完成后，需要设置防火墙的入站规则，否则可能出现无法拉取镜像，宿主机也无法 ping 通虚拟机的情况。

<img src="deploy/image-20240621234809771.png" alt="image-20240621234809771" style="zoom:67%;" />

![image-20240622001039962](deploy/image-20240622001039962.png)

### git 安装



## 常用命令

### 修改用户组

```bash
$ chown -R xisun:xisun apps/
```

### 设置文件为可执行文件

```bash
$ chmod +x start.sh
```

### 下载 JDK

```bash
$ wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
```

### java -jar 执行启动服务脚本

```sh
kill -9 $(netstat -nlp | grep :9527 | awk '{print $7}' | awk -F"/" '{ print $1 }')
echo "杀死(9527)端口进程成功 ！";
nohup /home/qirui/apps/jdk/jdk-17.0.8/bin/java -Duser.timezone=Asia/Shanghai -Dfile.encoding=utf8 -Dspring.profiles.active=dev -Xms1G -Xmx1G -jar platform-dev.jar --server.port=9527 >> platform.log &
```

### curl 模拟 POST 请求

```bash
$ curl -H "Content-Type: application/json" -X POST -d '{"tenant": "2000000015", "startTime": "2022-06-01 00:00:00", "endTime": "2023-07-29 23:59:59"}' http://127.0.0.1:39091/jz_report/extract_data/risk_analysis_object
```

>https://blog.csdn.net/u012340794/article/details/71440604

### 压缩和解压

```bash
$ unzip test.zip

# 解压
$ tar -zxvf test.tag.gz

# 压缩
$ tar -zcvf test test.tag.gz
```

### 历史命令

```bash
$ history

# 清空历史命令
$ history -c
```

### 操作系统

```bash
$ head -n 1 /etc/issue
$ cat /etc/redhat-release
$ cat /etc/centos-release
```

### CPU 核数

```bash
# 物理CPU个数
$ cat /proc/cpuinfo | grep "physical id" | sort | uniq | wc -l
# 每个物理CPU的核数
$ cat /proc/cpuinfo | grep "cores" | uniq
# 逻辑CPU总数
$ cat /proc/cpuinfo | grep "processor" | wc -l
```

### 内存和磁盘空间

```bash
# 内存
$ free -m

# 磁盘
$ df -h
$ df -h -t xfs
```

### 端口监控

```bash
# 所有端口
$ netstat -ntlp

# 指定端口
$ netstat -ntlp | grep 9527
```

### Centos 防火墙开启端口

```bash
# 查看防火墙状态
$ systemctl status firewalld
$ firewall-cmd --state

# 查询指定端口是否已开
$ firewall-cmd --query-port=9991/tcp

# 添加指定需要开放的端口
$ firewall-cmd --zone=public --add-port=9991/tcp --permanent

# 重启防火墙
$ systemctl restart firewalld.service
```

### crontab 定时任务

```bash
# 查看
$ crontab -l

# 编辑
$ crontab -e 
```

### 循环

```bash
for file in `ls /mnt/webapps/`; do echo $file; cat /mnt/webapps/$file/WEB-INF/classes/META-INF/meta.properties | grep env=; done
```

### MySQL 导出数据

```mysql
# 导出特定数据库的数据和结构
mysqldump -uroot -proot --databases db1 db2 > /tmp/user.sql
1
# 导出特定数据库特定表的数据和结构
mysqldump -uroot -proot --databases db1 --tables a1 a2  > /tmp/db1.sql
```

## Docker Compose

### 语法

**构建并启动容器命令：**

```sh
$ docker-compose up -d
```

- -d 是后台运行。

**停止容器命令：**

```sh
$ docker-compose stop
```

**启动容器命令：**

```sh
$ docker-compose start
```

**重启容器服务：**

```sh
$ docker-compose restart
```

**停止并删除容器命令：**

```sh
$ docker-compose down
```

- 此命令将会停止`up`命令所启动的容器（同时会删除容器），并移除网络。

**指定 compose 文件启动：**

```sh
$ docker-compose -f docker-compose-redis.yaml up -d
```

- 默认使用 docker-compose.yaml 文件。

**检查配置：**

```sh
$ docker-compose config
```

- 验证 compose 文件格式是否正确，若正确则显示配置，若格式错误显示错误原因。

> 更多用法和命令参考：https://yeasy.gitbook.io/docker_practice/compose

>指定 compose 文件启动时，可能会出现 network 错误：
>
>```sh
>$ docker-compose -f docker-compose.yaml up
>[+] Running 6/0
> ✔ Network apps        Created                                                                                                       0.1s 
> ✔ Container nginx     Created                                                                                                       0.0s 
> ✔ Container rabbitmq  Created                                                                                                       0.0s 
> ✔ Container mysql     Created                                                                                                       0.0s 
> ✔ Container redis     Created                                                                                                       0.0s 
> ✔ Container minio     Created                                                                                                       0.0s 
>Attaching to minio, mysql, nginx, rabbitmq, redis
>Error response from daemon: network f6ba421c0918bec0b093f7342f29f2e103989224a19395330874364782d7a99c not found
>```
>
>查看当前所有的 network：
>
>```sh
>$ docker network ls
>NETWORK ID     NAME      DRIVER    SCOPE
>4c3e1c2beb1f   apps      bridge    local
>ab842eba8483   bridge    bridge    local
>054499900bca   host      host      local
>c6aab822c5a4   none      null      local
>```
>
>查看指定 network 的详细信息：
>
>```sh
>$ docker network inspect 4c3e1c2beb1f
>[
>    {
>        "Name": "apps",
>        "Id": "4c3e1c2beb1fb14990cc2bff7e0646d0c2449e7c2f49c5a567063e83ee908e66", // apps这个network当前的id
>        "Created": "2023-10-12T19:56:42.603082507+08:00",
>        "Scope": "local",
>        "Driver": "bridge",
>        "EnableIPv6": false,
>        "IPAM": {
>            "Driver": "default",
>            "Options": null,
>            "Config": [
>                {
>                    "Subnet": "172.18.0.0/16",
>                    "Gateway": "172.18.0.1"
>                }
>            ]
>        },
>        "Internal": false,
>        "Attachable": false,
>        "Ingress": false,
>        "ConfigFrom": {
>            "Network": ""
>        },
>        "ConfigOnly": false,
>        "Containers": {},
>        "Options": {},
>        "Labels": {
>            "com.docker.compose.network": "apps",
>            "com.docker.compose.project": "apps",
>            "com.docker.compose.version": "2.20.2"
>        }
>    }
>]
>```
>
>查看容器绑定的 network：
>
>```sh
>$ docker inspect redis
>......
>"NetworkSettings": {
>            "Bridge": "",
>            "SandboxID": "53ee53516b1035e683f3457cc7d72ddc351d2fe4c8172ca2304f7d5dcdd45444",
>            "HairpinMode": false,
>            "LinkLocalIPv6Address": "",
>            "LinkLocalIPv6PrefixLen": 0,
>            "Ports": {},
>            "SandboxKey": "/var/run/docker/netns/53ee53516b10",
>            "SecondaryIPAddresses": null,
>            "SecondaryIPv6Addresses": null,
>            "EndpointID": "",
>            "Gateway": "",
>            "GlobalIPv6Address": "",
>            "GlobalIPv6PrefixLen": 0,
>            "IPAddress": "",
>            "IPPrefixLen": 0,
>            "IPv6Gateway": "",
>            "MacAddress": "",
>            "Networks": {
>                "apps": {
>                    "IPAMConfig": null,
>                    "Links": null,
>                    "Aliases": [
>                        "redis",
>                        "redis",
>                        "6565393956e3"
>                    ],
>                    "NetworkID": "f6ba421c0918bec0b093f7342f29f2e103989224a19395330874364782d7a99c", // 与apps这个network当前的id不对应
>                    "EndpointID": "",
>                    "Gateway": "",
>                    "IPAddress": "",
>                    "IPPrefixLen": 0,
>                    "IPv6Gateway": "",
>                    "GlobalIPv6Address": "",
>                    "GlobalIPv6PrefixLen": 0,
>                    "MacAddress": "",
>                    "DriverOpts": null
>                }
>            }
>        }
>......
>```
>
>可以看出，容器中绑定的 network，与当前该名称的 network id 不同，发生了变化，因此，需要重新绑定该 compose 文件中所有容器的 network：
>
>```sh
>$ docker network connect 4c3e1c2beb1f nginx
>$ docker network connect 4c3e1c2beb1f redis
>$ docker network connect 4c3e1c2beb1f mysql
>$ docker network connect 4c3e1c2beb1f minio
>$ docker network connect 4c3e1c2beb1f rabbitmq
>```
>
>然后，重新指定该 compose 文件，可以正常启动服务：
>
>```sh
>$ docker-compose -f docker-compose.yaml up -d
>[+] Running 5/5
> ✔ Container minio     Started                                                                                                       0.9s 
> ✔ Container mysql     Started                                                                                                       0.9s 
> ✔ Container redis     Started                                                                                                       0.9s 
> ✔ Container rabbitmq  Started                                                                                                       0.8s 
> ✔ Container nginx     Started                                                                                                       0.9s
>```

### MySQL

docker-compose.yaml：

```yaml
version: "3.4"

networks:
  apps:
    name: apps
    external: false

services:
    mysql:
        image: mysql:8.0.33
        container_name: mysql
        hostname: zeloud.mysql
        ports:
          - 3306:3306
        volumes:
          - ./mysql/data:/var/lib/mysql
          - ./mysql/conf/mysql.conf.d/mysqld.cnf:/etc/mysql/mysql.conf.d/mysqld.cnf
          - /etc/localtime:/etc/localtime:ro
        environment:
          - MYSQL_ROOT_PASSWORD=123456
          - MYSQL_INNODB_BUFFER_SIZE=1G
          - MYSQL_SERVER_ID=101
        networks:
          - apps
        restart: on-failure:3
```

./mysql/conf/mysql.conf.d/mysqld.cnf（需要在本地目录预先创建）：

```sh
[mysqld]
pid-file        = /var/run/mysqld/mysqld.pid
socket          = /var/run/mysqld/mysqld.sock
datadir         = /var/lib/mysql
#log-error      = /var/log/mysql/error.log
# By default we only accept connections from localhost
#bind-address   = 127.0.0.1
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0

max_allowed_packet = 32M
lower_case_table_names=1
max_connections=2000
# innodb缓冲池大小，此处设置为512MB. 512 * 1024 * 1024
innodb_buffer_pool_size=536870912

# binlog配置
#log-bin=mysql-bin
#binlog-format=ROW
#server-id=1
#binlog_ignore_db=information_schema,mysql,performance_schema,sys
#expire_logs_days=30

#sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
```

### Redis

docker-compose.yaml：

```yaml
version: "3.4"

networks:
  apps:
    name: apps
    external: false

services:
    redis:
        image: redis:7.0.11
        container_name: redis
        hostname: zeloud.redis
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

./redis/conf/redis.conf（需要在本地目录预先创建）：

```sh
port 6379
requirepass 123456
protected-mode no
daemonize no
appendonly yes
aof-use-rdb-preamble yes
```

### Rabbitmq

docker-compose.yaml：

```yaml
version: "3.4"

networks:
  apps:
    name: apps
    external: false

services:
    rabbitmq:
        image: rabbitmq:management
        container_name: rabbitmq
        hostname: zeloud.rabbitmq
        ports:
          - 5672:5672
          - 15672:15672
        volumes:
          - ./rabbitmq/data:/var/lib/rabbitmq
        environment:
          - "RABBITMQ_DEFAULT_USER=rbmq"
          - "RABBITMQ_DEFAULT_PASS=rbmq"
        networks:
          - apps
        restart: on-failure:3
```

### Nginx

docker-compose.yaml：

```yaml
version: "3.4"

networks:
  apps:
    name: apps
    external: false

services:
    nginx:
        image: nginx:1.23.4-perl
        container_name: nginx
        hostname: zeloud.nginx
        ports:
          - 80:80
          - 8081:8081
        volumes:
          - ./nginx/conf/nginx.conf:/etc/nginx/nginx.conf
          - ./nginx/conf/conf.d:/etc/nginx/conf.d
          - ./nginx/logs:/var/log/nginx
          - ./nginx/html:/apps/html
          - ./nginx/picture:/apps/picture
          - /etc/localtime:/etc/localtime:ro
        ulimits:
          nofile:
            soft: 65536
            hard: 65536
        networks:
          - apps
        restart: on-failure:3
```

./nginx/conf/nginx.conf（需要在本地目录预先创建）：

```sh
user  nginx;
worker_processes  auto;

error_log  /var/log/nginx/error.log notice;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    include /etc/nginx/conf.d/*.conf;
}
```

./nginx/conf/conf.d/default.conf（需要在本地目录预先创建）：

```sh
server {
    listen       80;
    listen  [::]:80;
    server_name  localhost;

    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /apps/html;
        index  index.html index.htm;
    }

    #error_page  404              /404.html;

    # redirect server error pages to the static page /50x.html
    #
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /apps/html;
    }

    # proxy the PHP scripts to Apache listening on 127.0.0.1:80
    #
    #location ~ \.php$ {
    #    proxy_pass   http://127.0.0.1;
    #}

    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    #
    #location ~ \.php$ {
    #    root           html;
    #    fastcgi_pass   127.0.0.1:9000;
    #    fastcgi_index  index.php;
    #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    #    include        fastcgi_params;
    #}

    # deny access to .htaccess files, if Apache's document root
    # concurs with nginx's one
    #
    #location ~ /\.ht {
    #    deny  all;
    #}
}
```

./nginx/html/index.html（需要在本地目录预先创建）：

```html
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```

./nginx/html/50x.html（需要在本地目录预先创建）：

```html
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>An error occurred.</h1>
<p>Sorry, the page you are looking for is currently unavailable.<br/>
Please try again later.</p>
<p>If you are the system administrator of this resource then you should check
the error log for details.</p>
<p><em>Faithfully yours, nginx.</em></p>
</body>
</html>
```

命令：

```sh
# 查看配置文件是否有误
$ docker exec -it nginx nginx -t

# 重新加载配置
$ docker exec -it nginx nginx -s reload
```

### MinIO

docker-compose.yaml：

```yaml
version: "3.4"

networks:
  apps:
    name: apps
    external: false
services:
    minio:
        image: minio/minio:RELEASE.2023-07-21T21-12-44Z
        container_name: minio
        hostname: zeloud.minio
        ports:
          - "9000:9000"
          - "9001:9001"
        volumes:
          - ./minio/data:/data
        command: server /data --console-address ":9001"
        environment:
          MINIO_ACCESS_KEY: "admin"
          MINIO_SECRET_KEY: "@admin2023"
          TZ: Asia/Shanghai
        networks:
          - apps
        restart: on-failure:3
```

## 云效流水线部署

变量：

- APP_NAME：service-headquarters
- APP_NAME_VAR：service_headquarters
- PROJECT_NAME：zgzh
- BRANCH：master

### 流水线源

<img src="deploy/image-20230802151135147.png" alt="image-20230802151135147" style="zoom:80%;" />



https://codeup.aliyun.com/sshy/qunan/service.git

### 构建

整体流程：

<img src="deploy/image-20230802143926088.png" alt="image-20230802143926088" style="zoom:80%;" />





#### 执行命令

<img src="deploy/image-20230802144100487.png" alt="image-20230802144100487" style="zoom:80%;" />

执行命令：

```sh
sed -i 's/APPNAME/'$APP_NAME'/g' pom.xml

git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/$PROJECT_NAME/public/parent.git parent

git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/$PROJECT_NAME/public/core.git core

git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/$PROJECT_NAME/service/$APP_NAME.git $APP_NAME


git submodule init
git submodule update
```

```bash
sed -i 's/APPNAME/'$APP_NAME'/g' pom.xml

git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/zgzh/public/parent.git parent
git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/zgzh/public/core.git core
git submodule add -b $BRANCH https://$USERNAME:$PASSWORD@codeup.aliyun.com/sshy/project/zgzh/service/$APP_NAME.git $APP_NAME

git submodule init
git submodule update
```

```bash
git clone $BRANCH https://$USERNAME:$PASSWORD@https://codeup.aliyun.com/sshy/qunan/service.git
```



#### Java 构建

<img src="deploy/image-20230802144155484.png" alt="image-20230802144155484" style="zoom:80%;" />

构建命令：

```bash
mvn clean dependency:list install -pl ${APP_NAME} -am -Dspring.profiles.active=prod

mkdir -p ./target
rm -rf ./${APP_NAME}/target/*sources.jar
ls -l ./${APP_NAME}/target/*.jar
cp ./${APP_NAME}/target/*.jar ./target/app.jar
```

#### 镜像构建并推送至阿里云镜像仓库个人版

<img src="deploy/image-20230802150902630.png" alt="image-20230802150902630" style="zoom:80%;" />

#### 构建物上传

<img src="deploy/image-20230802151013277.png" alt="image-20230802151013277" style="zoom:80%;" />

### 部署

#### 基础信息

<img src="deploy/image-20230802151745614.png" alt="image-20230802151745614" style="zoom:80%;" />

#### 部署配置

<img src="deploy/image-20230802151829396.png" alt="image-20230802151829396" style="zoom:80%;" />

部署脚本：

```bash
cd /home/apps
tar zxvf Artifacts_${PIPELINE_ID}.tgz

cat password.txt | docker login --username=litubai registry.cn-hangzhou.aliyuncs.com --password-stdin

sed -i 's#HOST_IP=.*#HOST_IP=192.168.3.211#g' .env
sed -i 's#hse_service_gateway=registry.*#hse_service_gateway=registry.cn-hangzhou.aliyuncs.com/lantukeji/lantu:'$PROJECT_NAME'-'$APP_NAME'-'$DATETIME'#g' .env

docker-compose -f docker-compose.yaml up $APP_NAME -d --force-recreate
```



#### 部署策略

<img src="deploy/image-20230802151846972.png" alt="image-20230802151846972" style="zoom:80%;" />





部署配置：

- 下载路径：/home/apps/Artifacts_${PIPELINE_ID}.tgz
- 执行用户：root

部署脚本：

```sh
# 
cd /home/apps
tar zxvf Artifacts_${PIPELINE_ID}.tgz
# 登录docker
echo $PASSWORD > password.txt
cat password.txt|docker login --username=$USERNAME registry.cn-shanghai.aliyuncs.com --password-stdin
# 修改IP
sed -i 's#HOST_IP=.*#HOST_IP=192.168.3.26#g' .env
# 修改镜像
sed -i 's#'$APP_NAME_VAR'=registry.*#'$APP_NAME_VAR'=registry.cn-shanghai.aliyuncs.com/alf_hse/'$PROJECT_NAME'-'$APP_NAME':'$DATETIME'#g' .env
docker-compose -f docker-compose.yml up $APP_NAME -d --force-recreate
```



