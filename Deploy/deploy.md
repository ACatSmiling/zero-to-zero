*date: 2023-06-26*

## 常用命令

### 固定 IP

操作系统：

```bash
$ lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description:    Ubuntu 23.04
Release:        23.04
Codename:       lunar
```

查看网卡信息：

```bash
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

```bash
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

```bash
$ sudo netplan apply
```

### 时区设置

```bash
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

# 设置24小时制
$ sudo vim /etc/default/locale
LC_TIME=en_DK.utf-8

# 重启
$ sudo reboot
```

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



## Docker

官网：https://www.docker.com/

Ubuntu 安装：https://docs.docker.com/engine/install/ubuntu/

**卸载冲突包：**

```bash
$ for pkg in docker.io docker-doc docker-compose podman-docker containerd runc; do sudo apt-get remove $pkg; done
```

> 卸载冲突包后，保存在`/var/lib/docker/`路径的镜像、容器、数据卷、网络等，不会自动删除，如果想彻底删除，参考：https://docs.docker.com/engine/install/ubuntu/#uninstall-docker-engine。

**设置 Docker 仓库：**

```bash
# 更新apt软件包索引和安装包, 以允许apt使用HTTPS上的存储库
$ sudo apt-get update
$ sudo apt-get install ca-certificates curl gnupg

# 添加Docker的官方GPG密钥
$ sudo install -m 0755 -d /etc/apt/keyrings
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
$ sudo chmod a+r /etc/apt/keyrings/docker.gpg

# 设置仓库
$ echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

**安装 Docker 引擎：**

```bash
$ sudo apt-get update
$ sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

> 安装 Dokcer 引擎的时候，也安装了 Docker Compose 插件。

**设置镜像源：**

```bash
# 设置镜像源，新建一个daemon.json文件，然后添加如下内容
$ sudo vim /etc/docker/daemon.json
$ sudo cat /etc/docker/daemon.json
{
    "registry-mirrors": ["http://hub-mirror.c.163.com"]
}
```

**验证是否安装成功：**

```bash
$ sudo docker run hello-world
```

**添加当前用户到 docker 组：**

```bash
# 当前用户无权限
$ docker ps
permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/containers/json": dial unix /var/run/docker.sock: connect: permission denied

# 添加当前用户到docker组
$ sudo gpasswd -a ${USER} docker
[sudo] password for xisun: 
Adding user xisun to group docker

# 退出当前用户, 比如切换为root, 再切换为xisun
$ sudo su -
$ su xisun 

# 有权限
$ docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES

```

## Docker Compose

官网：https://www.docker.com/

Ubuntu 安装：https://docs.docker.com/compose/install/linux/

Github：https://github.com/docker/compose

releases：https://github.com/docker/compose/releases

**Docker 引擎安装好之后，可以直接使用以下命令安装 Docker Compose：**

```bash
$ sudo apt-get update
$ sudo apt-get install docker-compose-plugin
```

**验证 Docker Compose 是否安装成功：**

```bash
$ docker compose version
```

**更新 Docker Compose：**

```bash
$ sudo apt-get update
$ sudo apt-get install docker-compose-plugin
```

> 手动安装：
>
> ```bash
> # 下载, 如需下载其他版本, 替换v2.19.0为最新的版本号即可
> $ sudo curl -L "https://github.com/docker/compose/releases/download/v2.19.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
> 
> # 赋予二进制文件可执行权限
> $ sudo chmod +x /usr/local/bin/docker-compose
> ```

**docker-compose.yaml：**

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
    volumes:
      - ./mysql/data:/var/lib/mysql
      - ./mysql/conf/mysql.conf.d/mysqld.cnf:/etc/mysql/mysql.conf.d/mysqld.cnf
      - /etc/localtime:/etc/localtime:ro
    ports:
      - 3306:3306
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_INNODB_BUFFER_SIZE=1G
      - MYSQL_SERVER_ID=101
    networks:
      - apps
    restart: on-failure:3
  
  rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq
    hostname: rabbitmqhosta
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

  nginx:
    image: nginx:1.23.4-perl
    container_name: nginx
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
        
  redis:
    image: redis:7.0.11
    container_name: redis
    ports:
      - 6379:6379
    volumes:
      - ./redis/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - ./redis/data:/data
    command: redis-server /usr/local/etc/redis/redis.conf
    networks:
      - apps
    restart: on-failure:3
  
  minio:
    container_name: minio
    image: minio/minio:RELEASE.2023-07-21T21-12-44Z
    command: server /data --console-address ":9001"
    environment:
      MINIO_ACCESS_KEY: "admin"
      MINIO_SECRET_KEY: "@admin2023"
      TZ: Asia/Shanghai
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - ./minio/data:/data
    networks:
      - apps
    restart: on-failure:3
```

**构建并启动容器命令：**

```bash
$ docker compose up -d
```

- -d 是后台运行。

**停止容器命令：**

```bash
$ docker compose stop
```

**启动容器命令：**

```bash
$ docker compose start
```

**重启容器服务：**

```bash
$ docker compose restart
```

**停止并删除容器命令：**

```bash
$ docker compose down
```

- 此命令将会停止`up`命令所启动的容器（同时会删除容器），并移除网络。

**指定 compose 文件启动：**

```bash
$ docker compose -f docker-compose-redis.yaml up -d
```

- 默认使用 docker-compose.yaml 文件。

**检查配置：**

```bash
$ docker compose config
```

- 验证 compose 文件格式是否正确，若正确则显示配置，若格式错误显示错误原因。

> 更多用法和命令参考：https://yeasy.gitbook.io/docker_practice/compose

>指定 compose 文件启动时，可能会出现 network 错误：
>
>```bash
>$ docker compose -f docker-compose.yaml up
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
>```bash
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
>```bash
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
>```bash
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
>```bash
>$ docker network connect 4c3e1c2beb1f nginx
>$ docker network connect 4c3e1c2beb1f redis
>$ docker network connect 4c3e1c2beb1f mysql
>$ docker network connect 4c3e1c2beb1f minio
>$ docker network connect 4c3e1c2beb1f rabbitmq
>```
>
>然后，重新指定该 compose 文件，可以正常启动服务：
>
>```bash
>$ docker compose -f docker-compose.yaml up -d
>[+] Running 5/5
> ✔ Container minio     Started                                                                                                       0.9s 
> ✔ Container mysql     Started                                                                                                       0.9s 
> ✔ Container redis     Started                                                                                                       0.9s 
> ✔ Container rabbitmq  Started                                                                                                       0.8s 
> ✔ Container nginx     Started                                                                                                       0.9s
>```

## MySQL

docker-compose.yaml：

```yaml
mysql:
    image: mysql:8.0.33
    container_name: mysql
    volumes:
      - ./mysql/data:/var/lib/mysql
      - ./mysql/conf/mysql.conf.d/mysqld.cnf:/etc/mysql/mysql.conf.d/mysqld.cnf
      - /etc/localtime:/etc/localtime:ro
    ports:
      - 3306:3306
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_INNODB_BUFFER_SIZE=1G
      - MYSQL_SERVER_ID=101
    networks:
      - apps
    restart: on-failure:3
```

mysqld.cnf：

```ini
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

- mysqld.cnf 需要在本地目录预先创建。

## Rabbitmq

docker-compose.yaml：

```yaml
rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq
    hostname: rabbitmqhosta
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

## Nginx

docker-compose.yaml：

```yaml
nginx:
    image: nginx:1.23.4-perl
    container_name: nginx
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

nginx.conf：

```ini
user  nginx;
worker_processes  auto;

error_log  /var/log/nginx/error.log notice;
pid        /var/run/nginx.pid;


events {
    worker_connections  65536;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format main '{"@timestamp": "$time_iso8601", '
                '"connection": "$connection", '
                '"remote_addr": "$remote_addr", '
                '"remote_user": "$remote_user", '
                '"request_method": "$request_method", '
                '"request_uri": "$request_uri", '
                '"request_length": "$request_length", '
                '"server_protocol": "$server_protocol", '
                '"status": "$status", '
                '"body_bytes_sent": "$body_bytes_sent", '
                '"http_referer": "$http_referer", '
                '"http_user_agent": "$http_user_agent", '
                '"http_x_forwarded_for": "$http_x_forwarded_for", '
                '"upstream_addr": "$upstream_addr", '
                '"request_time": "$request_time"}';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    # 始终发送静态的gzip压缩数据
    gzip_static always;
    # 若客户端浏览器不支持gzip压缩数据，则解压后再发送
    gunzip on;
    gunzip_buffers 16 8k;
    # 当被代理的服务器符合条件时，对响应数据启用gzip压缩
    gzip_proxied expired no-cache no-store private auth;

    gzip on;
    # 响应数据超过1KB时启用gzip压缩
    gzip_min_length 1k;
    gzip_comp_level 3;
    gzip_types text/plain application/x-javascript
                          text/css application/xml text/javascript
                          application/x-httpd-php image/jpeg
                          image/gif image/png;
    gzip_vary on;

    include /etc/nginx/conf.d/*-http.conf;
}
```

apps-http.conf：

```ini
server {
    listen 80;
    server_name localhost;
    charset utf-8;

    location / {
        root /apps/html;
        index  index.html index.htm;
    }

    location /test/ {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header REMOTE-HOST $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://192.168.2.100:7795/;
    }

    location /ws {
        proxy_read_timeout 60s;
        proxy_set_header Host $host;
        proxy_set_header X-Real_IP $remote_addr;
        proxy_set_header X-Forwarded-for $remote_addr;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'Upgrade';
        proxy_pass http://192.168.2.100:7795/;
    }
}
server {
    listen 8081;
    location / {
        root /apps/picture;
        index  index.html index.htm;
    }
}
```

命令：

```bash
# 查看配置
$ docker exec -it nginx nginx -t

# 重新加载配置
$ docker exec -it nginx nginx -s reload
```

## Redis

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

## MinIO

docker-compose.yaml：

```yaml
minio:
    container_name: minio
    image: minio/minio:RELEASE.2023-07-21T21-12-44Z
    command: server /data --console-address ":9001"
    environment:
      MINIO_ACCESS_KEY: "admin"
      MINIO_SECRET_KEY: "@admin2023"
      TZ: Asia/Shanghai
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - ./minio/data:/data
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



