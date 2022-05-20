*date: 2020-12-15*

## find

用于在指定目录下查找文件。默认列出当前目录及子目录下所有的文件和文件夹。

### 语法

```sh
find   path   -option   [   -print ]   [ -exec   -ok   command ]   {} \;
```

**-print**： find 命令将匹配到的文件输出到标准输出。

**-exec**： find 命令对匹配的文件执行该参数所给出的 Shell 命令。

**-ok**： 和 -exec 的作用相同，只是更安全，在执行每个命令之前，都会给出提示，让用户来确定是否执行。

### 参数说明

```sh
-mount, -xdev
		只检查和指定目录在同一个文件系统下的文件，避免列出其它文件系统中的文件。

-amin n
		在过去n分钟内被读取过。

-anewer file
		比文件file更晚被读取过的文件。

-atime n
		在过去n天内被读取过的文件。
		
-cmin n
		在过去n分钟内被修改过。
		
-cnewer file
		比文件file更新的文件。
		
-ctime n
		在过去n天内被修改过的文件。
		
-empty
		空的文件。
		
-gid n
		gid是n。
		
-group name
		group名称是name。
		
-path p | -ipath p
		路径名称符合p的文件。ipath忽略大小写。
		
-name name | -iname name
		文件名称符合name的文件。iname忽略大小写。
		
-size n
		文件大小是n单位，b代表512位元组的区块，c表示字元数，k表示kilo bytes，w是二个位元组。
		
-type d|f
		文件类型是d|f的文件。
		
-pid n
		process id是n的文件。
```

> 文件类型：d --- 目录，f --- 一般文件，c --- 字型装置文件，b --- 区块装置文件，p --- 具名贮列，l --- 符号连结，s --- socket。

### 实例

- 查询当前路径下的所有目录|普通文件

```sh
$ find ./ -type d
$ find ./ -type f
```

- 查询权限为 777 的普通文件

```sh
$ find ./ -type f -perm 777
```

- 查询 .XML 文件，且权限不为 777

```sh
$ find ./ -type f -name "*.XML" ! -perm 777
```

- 查询 .XML 文件，并统计查询结果的条数

```sh
$ find ./ -name "*.XML" | wc -l
```

- 查询 .XML 文件，并复制查询结果到指定路径

```sh
$ find ./ -name "*.XML" | xargs -i cp {} ../111
$ find ./ -name "*.XML" -exec cp {} ../111 \;
```

> 此命令不同于 cp，`cp *.XML ../111` 命令复制的是当前路径下符合条件的所有文件，子路径的不会被复制。

- 查询 .XML 文件，并删除

```sh
$ find ./ -name "*.XML" | xargs -i rm {}
$ find ./ -name "*.XML" -exec rm {} \;
```

> 此命令不同于 rm，`rm *.XML` 命令删除的是当前路径下符合条件的所有文件，子路径的不会被删除。

- 查询 .XML 文件，并将查询结果以 "File: 文件名" 的形式打印出来

```sh
$ find ./ -name "*.XML" | xargs -i printf "File: %s\n" {}
$ find ./ -name "*.XML" -exec printf "File: %s\n" {} \;
```

- 将当前路径及子路径下所有 3 天前的 .XML 格式的文件复制一份到指定路径

```sh
$ find ./ -name "*.XML" -mtime +3 -exec cp {} ../111 \;
```

- 查询多个文件后缀类型的文件

```sh
$ find ./ -regextype posix-extended -regex ".*\.(java|xml|XML)"	 # 查找所有的.java、.xml和.XML文件
$ find ./ -name "*.java" -o -name "*.xml" -o -name "*.XML"		 # -o选项，适用于查询少量文件后缀类型
```

- 组合查询，可以多次拼接查询条件

```sh
$ find ./ -name "file1*" -a -name "*.xml"	# -a：与，查找以file1开头，且以.xml 结尾的文件
$ find ./ -name "file1*" -o -name "*.xml"	# -o：或，查找以file1开头，或以.xml 结尾的文件
$ find ./ -name "file1*" -not -name "*.xml"	# -not：非，查找以file1开头，且不以.xml 结尾的文件
$ find ./ -name "file1*" ! -name "file2*"	# !：同-not
```

- 查询当前目录下文件类型为 d 的文件，不包含子目录

```sh
$ find ./ -maxdepth 1 -type d
```

- 和正则表达式的结合使用

```sh
$ find ./ –name "[^abc]*"	# 在当前路径中搜索不以a、b、c开头的所有文件
$ find ./ -name "[A-Z0-9]*"	# 在当前路径中搜索以大写字母或数字开头的所有文件
```

> 正则表达式符号含义：
>
> \*		  代表任意字符 (可以没有字符)
>
> ?		  代表任意单个字符
>
> []		 代表括号内的任意字符，如 [abc] 可以匹配 a\b\c 某个字符
>
> [a-z]	可以匹配 a-z 的某个字母
>
> [A-Z]	可以匹配 A-Z 的某个字符
>
> [0-9]	可以匹配 0-9 的某个数字
>
> ^    	  用在 [] 内的前缀表示不匹配 [] 中的字符
>
> [^a-z]  表示不匹配a-z的某个字符

**参考：**

https://www.jianshu.com/p/b30a8aa4d1f1

https://www.oracle.com/cn/technical-resources/articles/linux-calish-find.html

https://www.cnblogs.com/qmfsun/p/3811142.html

https://www.cnblogs.com/ay-a/p/8017419.html

## cat

用于连接文件或标准输入并打印。这个命令常用来显示文件内容，或者将几个文件连接起来显示，或者从标准输入读取内容并显示，它常与重定向符号配合使用。 

### 语法

```sh
cat [-AbeEnstTuv] [--help] [--version] fileName
```

### 参数说明

```sh
-n | --number
		由1开始对所有输出的行数编号。

-b | --number-nonblank
		和-n相似，只不过对于空白行不编号。

-s | --squeeze-blank
		当遇到有连续两行以上的空白行，就代换为一行的空白行。

-v | --show-nonprinting
		使用^和M-符号，除了LFD和TAB之外。

-E | --show-ends
		在每行结束处显示$。
		
-T | --show-tabs
		将TAB字符显示为^I。
		
-A | --show-all
		等价于"-vET"。
		
-e
		等价于"-vE"。
		
-t
		等价于"-vT"。
```

### 实例

- 把 textfile1 的文档内容加上行号后输入 textfile2 这个文档里

```sh
$ cat -n textfile1 > textfile2
```

- 清空 /etc/test.txt 文档内容

```sh
$ cat /dev/null > /etc/test.txt
```

>**/dev/null**：在类 Unix 系统中，/dev/null 称空设备，是一个特殊的设备文件，它丢弃一切写入其中的数据 (但报告写入操作成功)，读取它则会立即得到一个 EOF。
>
>而使用 `cat $filename > /dev/null` 则不会得到任何信息，因为我们将本来该通过标准输出显示的文件信息重定向到了 /dev/null 中。
>
>使用 `cat $filename 1 > /dev/null` 也会得到同样的效果，因为默认重定向的 1 就是标准输出。 如果你对 shell 脚本或者重定向比较熟悉的话，应该会联想到 2 ，也即标准错误输出。
>
>如果我们不想看到错误输出呢？我们可以禁止标准错误 `cat $badname 2 > /dev/null`。

- 合并多个文件内容

```sh
$ cat b1.sql b2.sql b3.sql > b_all.sql
$ cat *.sql > merge.sql
```

## head

https://blog.csdn.net/zmx19951103/article/details/78575265

## tail

https://blog.csdn.net/luo200618/article/details/52510638

## sed

https://www.cnblogs.com/qmfsun/p/6626361.html

## jq

https://www.jianshu.com/p/dde911234761

https://blog.csdn.net/whatday/article/details/105781861

https://blog.csdn.net/qq_26502245/article/details/100191694

https://blog.csdn.net/u011641885/article/details/45559031

## basename

用于去掉文件名的目录和后缀。

### 语法

```sh
	basename NAME [SUFFIX]
or:	basename OPTION... NAME...
```

去掉 NAME 中的目录部分和后缀 SUFFIX，如果输出结果没有，则输出 SUFFIX。

### 参数说明

```sh
-a | --multiple
			support multiple arguments and treat each as a NAME
			
-s | --suffix=SUFFIX
			remove a trailing SUFFIX; implies -a
			
-z | --zero
			end each output line with NUL, not newline(默认情况下，每条输出行以换行符结尾)
			
--help
			display this help and exit
			
--version
			output version information and exit
```

### 实例

- 去除目录

```sh
$basename /usr/bin/sort
sort
```

```sh
$ basename /usr/include/stdio.h    
stdio.h
```

- 去除目录和后缀

```sh
$ basename /usr/include/stdio.h .h
stdio
```

```sh
$ basename -s .h /usr/include/stdio.h
stdio
```

```sh
$ basename /usr/include/stdio.h stdio.h 
stdio.h
```

- 去除多个目录

```sh
$ basename -a any1/str1 any2/str2 any3/str3
str1
str2
str3
```

## dirname

用于去除文件名中的非目录部分，删除最后一个 "\\" 后面的路径，显示父目录。

### 语法

```sh
dirname [OPTION] NAME...
```

如果 NAME 中不包含 /，则输出 .，即当前目录。

### 参数说明

```sh
-z | --zero
		end each output line with NUL, not newline
		
--help
		display this help and exit
		
--version
		output version information and exit
```

### 实例

```sh
$ dirname /usr/bin/
/usr

$ dirname /usr/bin
/usr

$ dirname /etc/
/

$ dirname /etc/httpd/conf/httpd.conf
/etc/httpd/conf
```

```sh
$ dirname dir1/str dir2/str
dir1
dir2
```

```sh
$ dirname stdio.h
.
```

## xargs

xargs 是给命令传递参数的一个过滤器，也是组合多个命令的一个工具。

xargs 可以将管道或标准输入 (stdin) 数据转换成命令行参数，也能够从文件的输出中读取数据。

xargs 也可以将单行或多行文本输入转换为其他格式，例如多行变单行，单行变多行。

xargs 默认的命令是 echo，这意味着通过管道传递给 xargs 的输入将会包含换行和空白，不过通过 xargs 的处理，换行和空白将被空格取代。

xargs 是一个强有力的命令，它能够捕获一个命令的输出，然后传递给另外一个命令。

之所以能用到这个命令，关键是由于很多命令不支持管道来传递参数，而日常工作中有有这个必要，所以就有了 xargs 命令，例如：

```sh
find /sbin -perm +700 | ls -l       #这个命令是错误的
find /sbin -perm +700 | xargs ls -l   #这样才是正确的
```

>xargs 一般是和管道一起使用。

### 语法

```sh
some command | xargs -item command
```

### 参数说明

```sh
-a file
	从文件中读入作为sdtin
		
-e flag
	注意有的时候可能会是-E，flag必须是一个以空格分隔的标志，当xargs分析到含有flag这个标志的时候就停止。
		
-p
	当每次执行一个argument的时候询问一次用户。
	
-n num
	后面加次数，表示命令在执行的时候一次用的argument的个数，默认是用所有的。

-t
	表示先打印命令，然后再执行。

-i
	或者是-I，这得看linux支持了，将xargs的每项名称，一般是一行一行赋值给{}，可以用{}代替。

-r
	no-run-if-empty，当xargs的输入为空的时候则停止xargs，不用再去执行了。

-s
	num命令行的最大字符数，指的是xargs后面那个命令的最大命令行字符数。

-L num
	从标准输入一次读取num行送给command命令。

-l
	同 -L。

-d delim
	分隔符，默认的xargs分隔符是回车，argument的分隔符是空格，这里修改的是xargs的分隔符。

-x
	exit的意思，主要是配合-s使用。

-P
	修改最大的进程数，默认是1，为0时候为as many as it can ，这个例子我没有想到，应该平时都用不到的吧。
```

### 实例

- 读取输入数据重新格式化后输出

定义一个测试文件，内有多行文本数据：

```sh
$ cat test.txt
a b c d e f g
h i j k l m n
o p q
r s t
u v w x y z
```

单行输出：

```sh
$ cat test.txt | xargs
a b c d e f g h i j k l m n o p q r s t u v w x y z
```

-n 选项自定义多行输出：

```sh
$ cat test.txt | xargs -n3
a b c
d e f
g h i
j k l
m n o
p q r
s t u
v w x
y z
```

-d 选项自定义一个定界符：

```sh
$ echo "nameXnameXnameXname" | xargs -dX
name name name name
```

结合 -n 选项使用：

```sh
$ echo "nameXnameXnameXname" | xargs -dX -n2
name name
name name
```

- 读取 stdin，将格式化后的参数传递给命令

假设一个命令为 sk.sh 和一个保存参数的文件 arg.txt：

sk.sh 命令内容：

```sh
#!/bin/bash

# 打印出所有参数。
echo $*
```

arg.txt 文件内容：

```sh
$ cat arg.txt
aaa
bbb
ccc
```

xargs 的一个选项 -I，使用 -I 指定一个替换字符串 {}，这个字符串在 xargs 扩展时会被替换掉，当 -I 与 xargs 结合使用，每一个参数命令都会被执行一次：

```sh
$ cat arg.txt | xargs -I {} ./sk.sh -p {} -l
-p aaa -l
-p bbb -l
-p ccc -l
```

复制所有图片文件到 /data/images 目录下：

```sh
ls *.jpg | xargs -n1 -I {} cp {} /data/images
```

- 结合 find 使用

用 rm 删除太多的文件时候，可能得到一个错误信息：**/bin/rm Argument list too long.**， 用 xargs 去避免这个问题：

```sh
$ find . -type f -name "*.log" -print0 | xargs -0 rm -f
```

>xargs -0 将 \0 作为定界符。

统计一个源代码目录中所有 php 文件的行数：

```sh
$ find . -type f -name "*.php" -print0 | xargs -0 wc -l
```

查找所有的 jpg 文件，并且压缩它们：

```sh
$ find . -type f -name "*.jpg" -print | xargs tar -zcvf images.tar.gz
```

- xargs 其他应用

假如你有一个文件包含了很多你希望下载的 URL，你能够使用 xargs 下载所有链接：

```sh
$ cat url-list.txt | xargs wget -c
```

参考：？？？

https://www.cnblogs.com/wangqiguo/p/6464234.html





## crontab

linux 内置的 cron 进程能实现定时任务需求。

crontab 命令是 cron table 的简写，它是 cron 的配置文件，也可以叫它作业列表。我们可以在以下文件夹内找到相关配置文件：

- **/var/spool/cron/**：该目录下存放的是每个用户包括 root 的 crontab 任务，每个任务以创建者的名字命名。
- **/etc/crontab**：该文件负责调度各种管理和维护任务。
- **/etc/cron.d/**：该目录用来存放任何要执行的crontab文件或脚本。
- 另外，还可以把脚本放在 **/etc/cron.hourly、/etc/cron.daily、/etc/cron.weekly、/etc/cron.monthly** 目录中，让它**每小时/天/星期/月**执行一次。

### 语法

```sh
crontab [-u user] [ -e | -l | -r ]
```

### 参数说明

```sh
-u user
	用来设定某个用户的crontab服务，省略此参数表示操作当前用户的crontab。

file
	file是命令文件的名字，表示将file做为crontab的任务列表文件并载入crontab。如果在命令行中没有指定这个文件，crontab命令将接受标准输入(键盘)上键入的命令，并将它们载入crontab。

-e
	编辑某个用户的crontab文件内容。如果不指定用户，则表示编辑当前用户的crontab文件。

-l
	显示某个用户的crontab文件内容。如果不指定用户，则表示显示当前用户的crontab文件内容。

-r
	从/var/spool/cron目录中删除某个用户的crontab文件。如果不指定用户，则默认删除当前用户的crontab文件。

-i
	在删除用户的crontab文件时给确认提示。
```

### 实例

设置定时任务时，输入 `crontab -e` 命令，进入当前用户的工作表编辑，是常见的 vim 界面。每行是一条命令。

**crontab 的命令格式：**

![Thumbnail](linux-command/45b1947d455a43e71d169dba7ca042fea49b6968c1697f3433a1a92e12c5786c.png)

**crontab 的命令构成：**

`时间 + 命令`，其时间有**分、时、日、月、周**五种，时间的操作符有：

- <span style="color:red;">*****</span>：取值范围内的所有数字
- <span style="color:red;">**/**</span>：每过多少个数字，间隔频率，例如：用在小时段的"*/2"表示每隔两小时
- <span style="color:red;">**-**</span>：从X到Z，例如："2-6"表示"2,3,4,5,6"
- <span style="color:red;">**,**</span>：散列数字，例如："1,2,5,7"

**crontab 的命令实例：**

- 每 2 小时执行一次：`0 */2 * * * command`

> 上述命令的含义：能被2整除的整点的0分，执行命令，即 0、2、4、6、…、20、22、24。

**crontab 的日志查看：**

```sh
$ tail -f /var/log/cron.log
$ tail -f /var/spool/mail/[username]
```

**注意事项：**

1. **环境变量问题**

有时我们创建了一个 crontab，但是这个任务却无法自动执行，而手动执行这个任务却没有问题，这种情况一般是由于在 crontab 文件中没有配置环境变量引起的。

在 crontab 文件中定义多个调度任务时，需要特别注环境变量的设置，因为我们手动执行某个任务时，是在当前 shell 环境下进行的，程序当然能找到环境变量，而系统自动执行任务调度时，是不会加载任何环境变量的，因此，就需要在 crontab 文件中指定任务运行所需的所有环境变量，这样，系统执行任务调度时就没有问题了。

不要假定 cron 知道所需要的特殊环境，它其实并不知道。所以你要保证在 shell 脚本中提供所有必要的路径和环境变量，除了一些自动设置的全局变量。所以注意如下 3 点：

- 脚本中涉及文件路径时写全局路径；

- 脚本执行要用到 java 或其他环境变量时，通过 source 命令引入环境变量，如：

```sh
$ cat start_cbp.sh
!/bin/sh
source /etc/profile
export RUN_CONF=/home/d139/conf/platform/cbp/cbp_jboss.conf
/usr/local/jboss-4.0.5/bin/run.sh -c mev &
```

- 或者通过在 crontab 中直接引入环境变量解决问题。如：

```sh
0 * * * * . /etc/profile;/bin/sh /var/www/java/audit_no_count/bin/restart_audit.sh
```

2. **及时清理系统用户的邮件日志**

每条任务调度执行完毕，系统都会将任务输出信息通过电子邮件的形式发送给当前系统用户，这样日积月累，日志信息会非常大，可能会影响系统的正常运行，因此，将每条任务进行重定向处理非常重要。 例如，可以在 crontab 文件中设置如下形式，忽略日志输出：

```sh
0 */3 * * * /usr/local/apache2/apachectl restart > /dev/null 2>&1
```

">/dev/null 2>&1" 表示先将标准输出重定向到 `/dev/null`，然后将标准错误重定向到标准输出，由于标准输出已经重定向到了 `/dev/null`，因此标准错误也会重定向到 `/dev/null`，这样日志输出问题就解决了。

3. **其他注意事项**

新创建的 cron job，不会马上执行，至少要过 2 分钟才执行。如果重启 cron 则马上执行。

当 crontab 失效时，可以尝试 `/etc/init.d/crond restart` 解决问题。或者查看日志看某个 job 有没有执行/报错 `tail -f /var/log/cron`。

**千万别乱运行 `crontab -r`**。它从 crontab 目录 (/var/spool/cron) 中删除用户的 crontab 文件，删除了该文件，则用户的所有 crontab 都没了。

在 crontab 中 % 是有特殊含义的，表示换行的意思。如果要用的话必须进行转义 %，如经常用的 date ‘+%Y%m%d’ 在 crontab 里是不会执行的，应该换成 date ‘+%Y%m%d’。

**参考：**

https://linuxtools-rst.readthedocs.io/zh_CN/latest/tool/crontab.html

https://segmentfault.com/a/1190000021815907

## mv

用于为文件或目录改名、或将文件或目录移入其它位置。

### 语法

```sh
	mv [OPTION]... [-T] SOURCE DEST
or: mv [OPTION]... SOURCE... DIRECTORY
or: mv [OPTION]... -t DIRECTORY SOURCE...
```

### 参数说明

```sh
-b
	当目标文件或目录sh存在时，在执行覆盖前，会为其创建一个备份。

-i
	如果指定移动的源目录或文件与目标的目录或文件同名，则会先询问是否覆盖旧文件，输入y表示直接覆盖，输入n表示取消该操作。

-f
	如果指定移动的源目录或文件与目标的目录或文件同名，不会询问，直接覆盖旧文件。

-n
	不要覆盖任何已存在的文件或目录。

-u
	当源文件比目标文件新或者目标文件不存在时，才执行移动操作。
```

### 实例

- 将源文件名 source_file 改为目标文件名 dest_file

```sh
$ mv source_file(文件) dest_file(文件)
```

- 将文件 source_file 移动到目标目录 dest_directory 中

```sh
$ mv source_file(文件) dest_directory(目录)
```

- 将源目录 source_directory 移动到 目标目录 dest_directory中

```sh
$ mv source_directory(目录) dest_directory(目录)
```

> 若目录名 dest_directory 已存在，则 source_directory 移动到目录名 dest_directory 中；
>
> 若目录名 dest_directory 不存在，则 source_directory 改名为目录名 dest_directory。

## rename

用于实现文件或批量文件重命名。在不同的 linux 版本，命令的语法格式可能不同。

### 语法

```sh
rename [ -h|-m|-V ] [ -v ] [ -n ] [ -f ] [ -e|-E *perlexpr*]*|*perlexpr* [ *files* ]
```

> linux 版本：
>
> Linux version 4.4.0-116-generic (buildd@lgw01-amd64-021) (gcc version 5.4.0 20160609 (Ubuntu 5.4.0-6ubuntu1~16.04.9) ) #140-Ubuntu SMP Mon Feb 12 21:23:04 UTC 2018

或者

```sh
rename [options] expression replacement file...
```

> linux 版本：
>
> Linux version 3.10.0-1062.el7.x86_64 (mockbuild@kbuilder.bsys.centos.org) (gcc version 4.8.5 20150623 (Red Hat 4.8.5-36) (GCC) ) #1 SMP Wed Aug 7 18:08:02 UTC 2019

### 参数说明

```sh
-v, -verbose
        Verbose: print names of files successfully renamed.
-n, -nono
        No action: print names of files to be renamed, but don't rename.

-f, -force
        Over write: allow existing files to be over-written.

-h, -help
        Help: print SYNOPSIS and OPTIONS.

-m, -man
        Manual: print manual page.

-V, -version
        Version: show version number.

-e      Expression: code to act on files name.

        May be repeated to build up code (like "perl -e"). If no -e, the
        first argument is used as code.

-E      Statement: code to act on files name, as -e but terminated by
        ';'.
```
或者

```sh
-v, --verbose  explain what is being done
-s, --symlink  act on symlink target

-h, --help     display this help and exit
-V, --version  output version information and exit
```

### 实例

- 替换文件名中特定字段

```sh
$ rename -v "s/20/patent-application/" *.tar.gz
```

```sh
# lin @ lin in ~/share/storage_server_3/Download/test [14:52:08] 
$ ll
total 76G
-rw-rw-r-- 1 lin lin 4.3G Dec  4 16:54 2005.tar.gz
-rw-rw-r-- 1 lin lin 4.3G Dec  5 21:50 2006.tar.gz
-rw-rw-r-- 1 lin lin 4.4G Dec  5 21:52 2007.tar.gz
-rw-rw-r-- 1 lin lin 4.7G Dec  5 21:53 2008.tar.gz
-rw-rw-r-- 1 lin lin 5.0G Dec  7 22:10 2009.tar.gz

# lin @ lin in ~/share/storage_server_3/Download/test [14:52:08] 
$ rename -v "s/20/patent-application/" *.tar.gz
2005.tar.gz renamed as patent-application05.tar.gz
2006.tar.gz renamed as patent-application06.tar.gz
2007.tar.gz renamed as patent-application07.tar.gz
2008.tar.gz renamed as patent-application08.tar.gz
2009.tar.gz renamed as patent-application09.tar.gz

# lin @ lin in ~/share/storage_server_3/Download/test [14:53:55] 
$ ll
total 76G
-rw-rw-r-- 1 lin lin 4.3G Dec  4 16:54 patent-application05.tar.gz
-rw-rw-r-- 1 lin lin 4.3G Dec  5 21:50 patent-application06.tar.gz
-rw-rw-r-- 1 lin lin 4.4G Dec  5 21:52 patent-application07.tar.gz
-rw-rw-r-- 1 lin lin 4.7G Dec  5 21:53 patent-application08.tar.gz
-rw-rw-r-- 1 lin lin 5.0G Dec  7 22:10 patent-application09.tar.gz

```

或者

```sh
$ rename 20 patent-application-20 *.tar.gz
```

```sh
(base) [hadoop@client version-1.0]$ ll
total 79555796
-rw-rw-r-- 1 hadoop hadoop 4527645498 Dec  4 16:54 2005.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4550889304 Dec  5 21:50 2006.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4712276001 Dec  5 21:52 2007.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4986740725 Dec  5 21:53 2008.tar.gz
-rw-rw-r-- 1 hadoop hadoop 5311490484 Dec  7 22:10 2009.tar.gz
(base) [hadoop@client version-1.0]$ rename 20 patent-application-20 *.tar.gz
(base) [hadoop@client version-1.0]$ ll
total 79555796
-rw-rw-r-- 1 hadoop hadoop       1372 Dec 16 09:15 hash_calculate.txt
-rw-rw-r-- 1 hadoop hadoop 4527645498 Dec  4 16:54 patent-application-2005.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4550889304 Dec  5 21:50 patent-application-2006.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4712276001 Dec  5 21:52 patent-application-2007.tar.gz
-rw-rw-r-- 1 hadoop hadoop 4986740725 Dec  5 21:53 patent-application-2008.tar.gz
-rw-rw-r-- 1 hadoop hadoop 5311490484 Dec  7 22:10 patent-application-2009.tar.gz
```

**参考：**

http://einverne.github.io/post/2018/01/rename-files-batch.html

## unzip

用于解压缩 .zip 文件。

### 语法

```sh
	unzip [-cflptuvz][-agCjLMnoqsVX][-P <密码>][.zip文件][文件][-d <目录>][-x <文件>]
or: unzip [-Z]
```

### 参数说明

```sh
-c
	将解压缩的结果显示到屏幕上，并对字符做适当的转换。

-f
	更新现有的文件。

-l
	显示压缩文件内所包含的文件。

-p
	与-c参数类似，会将解压缩的结果显示到屏幕上，但不会执行任何的转换。

-t
	检查压缩文件是否正确。

-u
	与-f参数类似，但是除了更新现有的文件外，也会将压缩文件中的其他文件解压缩到目录中。

-v
	查看压缩文件目录信息，但是不解压该文件。

-z
	仅显示压缩文件的备注文字。

-a
	对文本文件进行必要的字符转换。

-b
	不要对文本文件进行字符转换。

-C
	压缩文件中的文件名称区分大小写。

-j
	不处理压缩文件中原有的目录路径。

-L
	将压缩文件中的全部文件名改为小写。

-M
	将输出结果送到more程序处理。

-n
	解压缩时不要覆盖原有的文件。

-o
	不必先询问用户，unzip执行后覆盖原有文件。


-q
	执行时不显示任何信息。

-s
	将文件名中的空白字符转换为底线字符。

-V
	保留VMS的文件版本信息。

-X
	解压缩时同时回存文件原来的UID/GID。

-P <密码>
	使用zip的密码选项。

[.zip文件]
	指定.zip压缩文件。

[文件]
	指定要处理.zip压缩文件中的哪些文件。

-d <目录>
	指定文件解压缩后所要存储的目录。

-x <文件>
	指定不要处理.zip压缩文件中的哪些文件。

-Z
    'unzip -Z'等于执行zipinfo指令。
```

### 实例

- 查看压缩文件目录信息，但不解压

```sh
$ unzip -v I20090212-SUPP.ZIP 
Archive:  I20090212-SUPP.ZIP
 Length   Method    Size  Cmpr    Date    Time   CRC-32   Name
--------  ------  ------- ---- ---------- ----- --------  ----
       0  Stored        0   0% 2009-01-31 04:56 00000000  project/pdds/ICEApplication/I20090212-SUPP/
       0  Stored        0   0% 2009-01-31 04:56 00000000  project/pdds/ICEApplication/I20090212-SUPP/DTDS/
   88199  Stored    88199   0% 2007-01-22 00:07 d5e3060f  project/pdds/ICEApplication/I20090212-SUPP/DTDS/DTDS.zip
       0  Stored        0   0% 2009-01-31 04:56 00000000  project/pdds/ICEApplication/I20090212-SUPP/UTIL0041/
   15664  Stored    15664   0% 2009-01-28 22:45 3dfa6c1c  project/pdds/ICEApplication/I20090212-SUPP/UTIL0041/US20090041797A1-20090212-SUPP.ZIP
       0  Stored        0   0% 2009-01-31 04:56 00000000  project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/
  901714  Stored   901714   0% 2009-01-28 22:45 75ce3ca6  project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/US20090044288A1-20090212-SUPP.ZIP
 1911858  Stored  1911858   0% 2009-01-28 22:45 cbc1d0bd  project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/US20090044297A1-20090212-SUPP.ZIP
--------          -------  ---                            -------
 2917435          2917435   0%                            8 files

```

- 解压 .zip 文件

```sh
$ unzip I20090212-SUPP.ZIP 
Archive:  I20090212-SUPP.ZIP
   creating: project/pdds/ICEApplication/I20090212-SUPP/
   creating: project/pdds/ICEApplication/I20090212-SUPP/DTDS/
 extracting: project/pdds/ICEApplication/I20090212-SUPP/DTDS/DTDS.zip  
   creating: project/pdds/ICEApplication/I20090212-SUPP/UTIL0041/
 extracting: project/pdds/ICEApplication/I20090212-SUPP/UTIL0041/US20090041797A1-20090212-SUPP.ZIP  
   creating: project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/
 extracting: project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/US20090044288A1-20090212-SUPP.ZIP  
 extracting: project/pdds/ICEApplication/I20090212-SUPP/UTIL0044/US20090044297A1-20090212-SUPP.ZIP 
```

- 解压 .zip 文件，但不显示信息

```sh
$ unzip -q I20090212-SUPP.ZIP
```

> 注意：如果压缩文件 .zip 是大于 2G 的，那 unzip 就无法使用，此时可以使用 7zip 解压。

**参考：**

https://www.bbsmax.com/A/lk5aMEAP51/

## zip

用于压缩文件，压缩后的文件后缀名为 .zip。

### 语法

```sh
zip [-AcdDfFghjJKlLmoqrSTuvVwXyz$] [-b <工作目录>] [-ll] [-n <字尾字符串>] [-t <日期时间>] [-<压缩效率>] [压缩文件] [文件...] [-i <范本样式>] [-x <范本样式>]
```

### 参数说明

```sh
-A
	调整可执行的自动解压缩文件。
	
-c
	替每个被压缩的文件加上注释。

-d
	从压缩文件内删除指定的文件。

-D
	压缩文件内不建立目录名称。

-f
	更新现有的文件。

-F
	尝试修复已损坏的压缩文件。

-g
	将文件压缩后附加在既有的压缩文件之后，而非另行建立新的压缩文件。

-h
	在线帮助。

-j
	只保存文件名称及其内容，而不存放任何目录名称。

-J
	删除压缩文件前面不必要的数据。
	
-k
	使用MS-DOS兼容格式的文件名称。
	
-l
	压缩文件时，把LF字符置换成LF+CR字符。

-L
	显示版权信息。

-m
	将文件压缩并加入压缩文件后，删除原始文件，即把文件移到压缩文件中。

-o
	以压缩文件内拥有最新更改时间的文件为准，将压缩文件的更改时间设成和该文件相同。

-q
	不显示指令执行过程。

-r
	递归处理，将指定目录下的所有文件和子目录一并处理。

-S
	包含系统和隐藏文件。

-T
	检查备份文件内的每个文件是否正确无误。

-u
	与-f参数类似，但是除了更新现有的文件外，也会将压缩文件中的其他文件解压缩到目录中。

-v
	显示指令执行过程或显示版本信息。

-V
	保存VMS操作系统的文件属性。

-w
	在文件名称里假如版本编号，本参数仅在VMS操作系统下有效。

-X
	不保存额外的文件属性。

-y
	直接保存符号连接，而非该连接所指向的文件，本参数仅在UNIX之类的系统下有效。

-z
	替压缩文件加上注释。

-$
	保存第一个被压缩文件所在磁盘的卷册名称。
	
-b <工作目录>
	指定暂时存放文件的目录。
	
-ll
	压缩文件时，把LF+CR字符置换成LF字符。

-n <字尾字符串>
	不压缩具有特定字尾字符串的文件。

-t <日期时间>
	把压缩文件的日期设成指定的日期。

-<压缩效率>
	压缩效率是一个介于1-9的数值。

-i <范本样式>
	只压缩符合条件的文件。

-x <范本样式>
	压缩时排除符合条件的文件。
```

### 实例

- 将 /home/html/ 目录下所有文件和文件夹打包为当前目录下的 html.zip

```sh
$ zip -q -r html.zip /home/html
```

- 如果当前在 /home/html 目录下，可以执行以下命令

```sh
$ zip -q -r html.zip *
```

- 从压缩文件 cp.zip 中删除文件 a.c

```sh
zip -dv cp.zip a.c
```

## tar

用于打包、解包文件。

> tar 本身不具有压缩功能，可以通过参数调用其他压缩工具实现压缩功能。 

### 语法

```sh
tar [-ABcdgGhiklmMoOpPrRsStuUvwWxzZ] [-b <区块数目>] [-C <目的目录>] [-f <备份文件>] [-F <Script文件>] [-K <文件>] [-L <媒体容量>] [-N <日期时间>] [-T <范本文件>] [-V <卷册名称>] [-X <范本文件>] [-<设备编号><存储密度>] [--after-date=<日期时间>] [--atime-preserve] [--backuup=<备份方式>] [--checkpoint] [--concatenate] [--confirmation] [--delete] [--exclude=<范本样式>] [--force-local] [--group=<群组名称>] [--help] [--ignore-failed-read] [--new-volume-script=<Script文件>] [--newer-mtime] [--no-recursion] [--null] [--numeric-owner] [--owner=<用户名称>] [--posix] [--erve] [--preserve-order] [--preserve-permissions] [--record-size=<区块数目>] [--recursive-unlink] [--remove-files] [--rsh-command=<执行指令>] [--same-owner] [--suffix=<备份字尾字符串>] [--totals] [--use-compress-program=<执行指令>] [--version] [--volno-file=<编号文件>] [文件或目录...]
```

> 语法结构：`tar [必要参数] [可选参数] [文件]`

### 参数说明

```sh
-A | --catenate
		新增文件到已存在的备份文件。
		
-B | --read-full-records
		读取数据时重设区块大小。
		
-c | --create
		建立新的备份文件。
		
-d | --diff | --compare
		对比备份文件内和文件系统上的文件的差异。
		
-g | --listed-incremental
		处理GNU格式的大量备份。
		
-G | --incremental
		处理旧的GNU格式的大量备份。
		
-h | --dereference
		不建立符号连接，直接复制该连接所指向的原始文件。
		
-i | --ignore-zeros
		忽略备份文件中的0 Byte区块，也就是EOF。
		
-k | --keep-old-files
		解开备份文件时，不覆盖已有的文件。
		
-l | --one-file-system
		复制的文件或目录存放的文件系统，必须与tar指令执行时所处的文件系统相同，否则不予复制。
		
-m | --modification-time
		还原文件时，不变更文件的更改时间。
		
-M | --multi-volume
		在建立，还原备份文件或列出其中的内容时，采用多卷册模式。
		
-o | --old-archive | --portability
		将资料写入备份文件时使用V7格式。
		
-O | --stdout
		把从备份文件里还原的文件输出到标准输出设备。
		
-p | --same-permissions
		用原来的文件权限还原文件。
		
-P | --absolute-names
		文件名使用绝对名称，不移除文件名称前的"/"号。
		
-r | --append
		新增文件到已存在的备份文件的结尾部分。
		
-R | --block-number
		列出每个信息在备份文件中的区块编号。
		
-s | --same-order
		还原文件的顺序和备份文件内的存放顺序相同。
		
-S | --sparse
		倘若一个文件内含大量的连续0字节，则将此文件存成稀疏文件。
		
-t | --list
		列出备份文件的内容。
		
-u | --update
		仅置换较备份文件内的文件更新的文件。
		
-U | --unlink-first
		解开压缩文件还原文件之前，先解除文件的连接。
		
-v | --verbose
		显示指令执行过程。
		
-w | --interactive
		遭遇问题时先询问用户。
		
-W | --verify
		写入备份文件后，确认文件正确无误。
		
-x | --extract | --get
		从备份文件中还原文件。
		
-z | --gzip | --ungzip
		通过gzip指令处理备份文件。
		
-Z | --compress | --uncompress
		通过compress指令处理备份文件。

-b <区块数目> | --blocking-factor=<区块数目>
		设置每笔记录的区块数目，每个区块大小为12Bytes。

-C <目的目录> | --directory=<目的目录>
		切换到指定的目录。

-f <备份文件> | --file=<备份文件>
		指定备份文件。多个命令时需要放在最后面。
		
-F <Script文件> | --info-script=<Script文件>
		每次更换磁带时，就执行指定的Script文件。

-K <文件> | --starting-file=<文件>
		从指定的文件开始还原。

-L <媒体容量> | -tape-length=<媒体容量>
		设置存放每体的容量，单位以1024Bytes计算。

-N <日期格式> | --newer=<日期时间>
		只将较指定日期更新的文件保存到备份文件里。

-T <范本文件> | --files-from=<范本文件>
		指定范本文件，其内含有一个或多个范本样式，让tar解开或建立符合设置条件的文件。

-V<卷册名称> | --label=<卷册名称>
		建立使用指定的卷册名称的备份文件。

-X <范本文件> | --exclude-from=<范本文件>
		指定范本文件，其内含有一个或多个范本样式，让tar排除符合设置条件的文件。

-<设备编号><存储密度>
		设置备份用的外围设备编号及存放数据的密度。
		
--after-date=<日期时间>
		此参数的效果和指定"-N"参数相同。

--atime-preserve
		不变更文件的存取时间。

--backup=<备份方式> | --backup
		移除文件前先进行备份。

--checkpoint
		读取备份文件时列出目录名称。

--concatenate
		此参数的效果和指定"-A"参数相同。

--confirmation
		此参数的效果和指定"-w"参数相同。

--delete
		从备份文件中删除指定的文件。

--exclude=<范本样式>
		排除符合范本样式的文件。

--group=<群组名称>
		把加入设备文件中的文件的所属群组设成指定的群组。

--help
		在线帮助。

--ignore-failed-read
		忽略数据读取错误，不中断程序的执行。

--new-volume-script=<Script文件>
		此参数的效果和指定"-F"参数相同。

--newer-mtime
		只保存更改过的文件。

--no-recursion
		不做递归处理，也就是指定目录下的所有文件及子目录不予处理。

--null
		从null设备读取文件名称。

--numeric-owner
		以用户识别码及群组识别码取代用户名称和群组名称。

--owner=<用户名称>
		把加入备份文件中的文件的拥有者设成指定的用户。

--posix
		将数据写入备份文件时使用POSIX格式。

--preserve
		此参数的效果和指定"-ps"参数相同。

--preserve-order
		此参数的效果和指定"-A"参数相同。

--preserve-permissions
		此参数的效果和指定"-p"参数相同。

--record-size=<区块数目>
		此参数的效果和指定"-b"参数相同。

--recursive-unlink
		解开压缩文件还原目录之前，先解除整个目录下所有文件的连接。

--remove-files
		文件加入备份文件后，就将其删除。

--rsh-command=<执行指令>
		设置要在远端主机上执行的指令，以取代rsh指令。

--same-owner
		尝试以相同的文件拥有者还原文件。

--suffix=<备份字尾字符串>
		移除文件前先行备份。

--totals
		备份文件建立后，列出文件大小。

--use-compress-program=<执行指令>
		通过指定的指令处理备份文件。

--version
		显示版本信息。

--volno-file=<编号文件>
		使用指定文件内的编号取代预设的卷册编号。
```

### 实例

- 打包，不压缩

```sh
$ tar -cvf test.tar test
test/
test/3
test/1
test/2
test/5
test/4
```

- 解包

```sh
$ tar -xvf test.tar 
test/
test/3
test/1
test/2
test/5
test/4
```

- 打包，并以 **gzip** 压缩

```sh
$ tar -zcvf test.tar.gz test
test/
test/3
test/1
test/2
test/5
test/4
```

> 在参数 f 之后的文件档名是自己取的，我们习惯上都用 .tar 来作为辨识。 如果加 z 参数，则以 .tar.gz 或 .tgz 来代表 gzip 压缩过的 tar 包。

- 解压 .tar.gz

```sh
$ tar -zxvf test.tar.gz 
test/
test/3
test/1
test/2
test/5
test/4
```

- 打包，以 **bzip2** 压缩

```sh
$ tar -zcvf test.tar.bz2 test
test/
test/3
test/1
test/2
test/5
test/4
```

- 解压 .tar.bz2

```sh
$ tar -zxvf test.tar.bz2 
test/
test/3
test/1
test/2
test/5
test/4
```

- 查看 .tar.gz 或 .tar.bz2 压缩包内的文件，但不解压

```sh
$ tar -ztvf test.tar.gz 
drwxrwxr-x lin/lin           0 2020-12-21 11:38 test/
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/3
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/1
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/2
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/5
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/4

$ tar -ztvf test.tar.bz2 
drwxrwxr-x lin/lin           0 2020-12-21 11:38 test/
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/3
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/1
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/2
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/5
-rw-rw-r-- lin/lin           0 2020-12-21 11:38 test/4
```

- 解压 .tar.gz 压缩包内的部分文件

```sh
$ tar -zxvf test.tar.gz test/2 test/3
test/3
test/2
```

- 解压 .tar.gz 到指定目录

```sh
$ tar -zxvf test.tar.gz -C ../Download 
test/
test/3
test/1
test/2
test/5
test/4
```

- 使用绝对路径打包压缩和解压

```sh
# 压缩
$ tar -zcPf /home/lin/share/storage_server_3/patent/grant-patent/patent_version-1.0/patent-grant-2019.tar.gz /home/lin/share/storage_server_3/patent/grant-patent/patent_version-1.0/2019

# 解压
$ tar -zxPf patent-grant-2019.tar.gz
```

> **tar 对文件打包时，一般不建议使用绝对路径。**
>
> 如果使用绝对路径，需要加 -P 参数。如果不添加，会发出警告：`tar: Removing leading '/' from member names`。
>
> 对于使用绝对路径打包压缩的文件，解压时 tar 会在当前目录下创建压缩时的绝对路径所对应的目录，在上面例子中，即为在当前目录下创建一个子目录 `home/lin/share/storage_server_3/patent/grant-patent/patent_version-1.0`。
>
> 如果在解压时使用 -P 参数，需要保证系统存在压缩时的绝对路径。

- 使用 **pigz** 并发压缩和解压

安装 pigz：

```sh
$ sudo apt install pigz
```

打包：

```sh
$ tar --use-compress-program=pigz -cvpf package.tgz ./package
```

解包：

```sh
$ tar --use-compress-program=pigz -xvpf package.tgz -C ./package
```

>pigz 是支持并行的 gzip，默认用当前逻辑 cpu 个数来并发压缩，无法检测个数的话，则并发 8 个线程。

另一种方式：

```sh
# 语法
$ tar -cvpf - $Dir | pigz -9 -p 6 $target-name

# 实例
$ tar -cvpf - /usr/bin | pigz -9 -p 6 bin.tgz
```

>-9：代表压缩率
>-p ：代表 cpu 数量

## time

用于检测特定指令执行时所需消耗的时间及系统资源 (内存和 I/O) 等资讯。

### 语法

```sh
time [options] COMMAND [arguments]
```

### 参数说明

```sh
-o | --output=FILE
			设定结果输出档。这个选项会将time的输出写入所指定的档案中。如果档案已经存在，系统将覆写其内容。
		
-a | --append
			配合-o使用，会将结果写到档案的末端，而不会覆盖掉原来的内容。
			
-f FORMAT | --format=FORMAT
			以FORMAT字串设定显示方式。当这个选项没有被设定的时候，会用系统预设的格式。不过你可以用环境变数time来设定这个格式，如此一来就不必每次登入系统都要设定一次。
```

### 实例

- date 命令的运行时间

```sh
$ time date
Tue Dec 22 12:01:50 CST 2020
date  0.00s user 0.01s system 8% cpu 0.092 total
```

- 查找文件并复制的运行时间

```sh
$ time find /home/lin/share/storage_server_3/patent/application/unzip_version-1.0/2019 -iname "*.xml" | xargs -P 6 -i cp {} /home/lin/share/storage_server_3/patent/application-patent/patent_version-1.0/2019 
find  -iname "*.xml"  24.00s user 114.39s system 1% cpu 2:08:02.95 total
xargs -P 6 -i cp {}   4.35s user 28.35s system 0% cpu 2:08:02.99 total
```

**参考：**

http://c.biancheng.net/linux/time.html

## mkdir

### 语法

### 参数说明

### 实例

- 创建多级目录

```sh
$ mkdir -p Project/a/src
```

- 创建多层次、多维度的目录树

```sh
$ mkdir -p Project/{a,b,c,d}/src
```

## sh -c

sh -c 命令，可以让 bash 将一个字串作为完整的命令来执行。

比如，向 test.asc 文件中随便写入点内容，可以：

```sh
$ echo "信息" > test.asc
```

或者

```sh
$ echo "信息" >> test.asc
```

下面，如果将 test.asc 权限设置为只有 root 用户才有权限进行写操作：

```sh
$ sudo chown root.root test.asc
```

然后，我们使用 sudo 并配合 echo 命令再次向修改权限之后的 test.asc 文件中写入信息：

```sh
$ sudo echo "又一行信息" >> test.asc
-bash: test.asc: Permission denied
```

这时，可以看到 bash 拒绝这么做，说是权限不够。这是因为重定向符号 > 和 >> 也是 bash 的命令。我们使用 sudo 只是让 echo 命令具有了 root 权限，但是没有让 > 和 >> 命令也具有 root 权限，所以 bash 会认为这两个命令都没有向 test.asc 文件写入信息的权限。解决这一问题的途径有两种。

第一种是利用 sh -c 命令，它可以让 bash 将一个字串作为完整的命令来执行，这样就可以将 sudo 的影响范围扩展到整条命令。具体用法如下：

```sh
$ sudo sh -c 'echo "又一行信息" >> test.asc'
```

另一种方法是利用管道和 tee 命令，该命令可以从标准输入中读入信息并将其写入标准输出或文件中，具体用法如下：

```sh
$ echo "第三条信息" | sudo tee -a test.asc
```

注意，tee 命令的 -a 选项的作用等同于 >> 命令，如果去除该选项，那么 tee 命令的作用就等同于 > 命令。 

## 1>/dev/null 2>&1

https://blog.csdn.net/ithomer/article/details/9288353

## top



https://www.jianshu.com/p/e9e0ce23a152

PID：进程的ID

   USER：进程所有者

​    PR：进程的优先级别，越小越优先被执行

​    NI：进程Nice值，代表这个进程的优先值

​    VIRT：进程占用的虚拟内存

​    RES：进程占用的物理内存

​    SHR：进程使用的共享内存

   S：进程的状态。S表示休眠，R表示正在运行，Z表示僵死状态

​    %CPU：进程占用CPU的使用

​    %MEM：进程使用的物理内存和总内存的百分

​    TIME+：该进程启动后占用的总的CPU时间，即占用CPU使用时间的累加值

​    COMMAND：启动该进程的命令名称

## free

free   用KB为单位展示数据

free -m   用MB为单位展示数据

free -h   用GB为单位展示数据

total : 总计屋里内存的大小

used : 已使用内存的大小

free : 可用内存的大小

shared : 多个进程共享的内存总额

buff/cache : 磁盘缓存大小

available : 可用内存大小 ， 从应用程序的角度来说：available = free + buff/cache .

## ps

## md5sum



## sha1sum

用来为给定的文件或文件夹计算单个哈希，以校验文件或文件夹的完整性。

给文件：

```sh
$ sha1sum patent-grant-2005.tar.gz 
77b6416501d34b904bd25f9aa32ca60d3e14659a  patent-grant-2005.tar.gz
```



https://www.itranslater.com/qa/details/2326085750774825984





## parallel 

https://linux.cn/article-9718-1.html

https://www.myfreax.com/gnu-parallel/

https://www.hi-linux.com/posts/32794.html

https://www.jianshu.com/p/c5a2369fa613

https://www.aqee.net/post/use-multiple-cpu-cores-with-your-linux-commands.html

https://blog.csdn.net/orangefly0214/article/details/103701600

## gzip

```shell
$ gunzip test.txt.gz > test.txt
```

- http://c.biancheng.net/view/785.html

## wc 

- https://cloud.tencent.com/developer/article/1830887

- 查看文件行数：

  ```shell
  wc -l filename 就是查看文件里有多少行
  wc -w filename 看文件里有多少个word。
  wc -L filename 文件里最长的那一行是多少个字。
  ```


## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
