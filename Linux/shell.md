*date: 2020-12-22*

## 字符串截取

shell 截取字符串通常有两种方式：从指定位置开始截取和从指定字符 (子字符串) 开始截取。

### 从指定位置开始截取

两个参数：起始位置，截取长度。

####  从字符串左边开始计数

```sh
${string: start :length}
```

其中，string 是要截取的字符串，start 是起始位置 (从左边开始，从 0 开始计数)，length 是要截取的长度 (如果省略表示直到字符串的末尾)。

```sh
url="c.biancheng.net"
echo ${url: 2: 9}

biancheng
```

```sh
url="c.biancheng.net"
echo ${url: 2}

biancheng.net
```

#### 从字符串右边开始计数

```sh
${string: 0-start :length}
```

`0-` 是固定的写法，专门用来表示从字符串右边开始计数。

> 从左边开始计数时，起始数字是 0；从右边开始计数时，起始数字是 1。计数方向不同，起始数字也不同。
>
> 不管从哪边开始计数，截取方向都是从左到右。

```sh
url="c.biancheng.net"
echo ${url: 0-13: 9}

biancheng
```

```sh
url="c.biancheng.net"
echo ${url: 0-13}

biancheng.net
```

> 从右边数，b 是第 13 个字符。

### 从指定字符 (子字符串) 开始截取

这种截取方式无法指定字符串长度，只能从指定字符 (子字符串) 截取到字符串末尾。shell 可以截取指定字符 (子字符串) 右边的所有字符，也可以截取左边的所有字符。

#### 使用 # 截取指定字符 (子字符串)右边字符

```sh
${string#*chars}
```

其中，string 表示要截取的字符，chars 是指定的字符 (子字符串)，`*` 是通配符的一种，表示任意长度的字符串。`*chars` 连起来使用的意思是：忽略左边的所有字符，直到遇见 chars (chars 不会被截取)。

> 从左往右看。

```sh
url="http://c.biancheng.net/index.html"
echo ${url#*:}

//c.biancheng.net/index.html
```

以下写法也可以得到同样的结果：

```sh
echo ${url#*p:}
echo ${url#*ttp:}
```

如果不需要忽略 chars 左边的字符，那么也可以不写 `*`，例如：

```sh
url="http://c.biancheng.net/index.html"
echo ${url#http://}

c.biancheng.net/index.html
```

注意，以上写法遇到第一个匹配的字符 (子字符串) 就结束了。例如：

```sh
url="http://c.biancheng.net/index.html"
echo ${url#*/}

/c.biancheng.net/index.html
```

> url 字符串中有三个 `/`，输出结果表明，shell 遇到第一个 `/` 就匹配结束了。

如果希望直到最后一个指定字符 (子字符串) 再匹配结束，那么可以使用 `##`，具体格式为：

```sh
${string##*chars}
```

```sh
#!/bin/bash

url="http://c.biancheng.net/index.html"
echo ${url#*/}    # 结果为 /c.biancheng.net/index.html
echo ${url##*/}   # 结果为 index.html

str="---aa+++aa@@@"
echo ${str#*aa}   # 结果为 +++aa@@@
echo ${str##*aa}  # 结果为 @@@
```

#### 使用 % 截取指定字符 (子字符串)左边字符

```sh
${string%chars*}
```

注意 `*` 的位置，因为要截取 chars 左边的字符，而忽略 chars 右边的字符，所以 `*` 应该位于 chars 的右侧。其他方面 `%` 和 `#` 的用法相同。

> 从右往左看。

```sh
#!/bin/bash

url="http://c.biancheng.net/index.html"
echo ${url%/*}  # 结果为 http://c.biancheng.net
echo ${url%%/*}  # 结果为 http:

str="---aa+++aa@@@"
echo ${str%aa*}  # 结果为 ---aa+++
echo ${str%%aa*}  # 结果为 ---
```

### 汇总

| 格式                       | 说明                                                         |
| -------------------------- | ------------------------------------------------------------ |
| ${string: start :length}   | 从 string 字符串的左边第 start 个字符开始，向右截取 length 个字符。 |
| ${string: start}           | 从 string 字符串的左边第 start 个字符开始截取，直到最后。    |
| ${string: 0-start :length} | 从 string 字符串的右边第 start 个字符开始，向右截取 length 个字符。 |
| ${string: 0-start}         | 从 string 字符串的右边第 start 个字符开始截取，直到最后。    |
| ${string#*chars}           | 从 string 字符串第一次出现 *chars 的位置开始，截取 *chars 右边的所有字符。 |
| ${string##*chars}          | 从 string 字符串最后一次出现 *chars 的位置开始，截取 *chars 右边的所有字符。 |
| ${string%*chars}           | 从 string 字符串第一次出现 *chars 的位置开始，截取 *chars 左边的所有字符。 |
| ${string%%*chars}          | 从 string 字符串最后一次出现 *chars 的位置开始，截取 *chars 左边的所有字符。 |

**参考：**

http://c.biancheng.net/view/1120.html

## 字符串拼接

```sh
#!/bin/bash

name="Shell"
url="http://c.biancheng.net/shell/"

str1=$name$url  # 中间不能有空格
str2="$name $url"  # 如果被双引号包围，那么中间可以有空格
str3=$name": "$url  # 中间可以出现别的字符串
str4="$name: $url"  # 这样写也可以
str5="${name}Script: ${url}index.html"  # 这个时候需要给变量名加上大括号

echo $str1
echo $str2
echo $str3
echo $str4
echo $str5

Shellhttp://c.biancheng.net/shell/
Shell http://c.biancheng.net/shell/
Shell: http://c.biancheng.net/shell/
Shell: http://c.biancheng.net/shell/
ShellScript: http://c.biancheng.net/shell/index.html
```

对于第 7 行代码，$name 和 $url 之间之所以不能出现空格，是因为当字符串不被任何一种引号包围时，遇到空格就认为字符串结束了，空格后边的内容会作为其他变量或者命令解析。

对于第 10 行代码，加 { } 是为了帮助解释器识别变量的边界。

## 字符串分割

### 以空格为分隔符

比如有一个变量 “123 456 789”，要求以空格为分隔符把这个变量分隔，并把分隔后的字段分别赋值给变量，即 a=123；b=456；c=789。
共有3中方法：
方法一：先定义一个数组，然后把分隔出来的字段赋值给数组中的每一个元素
方法二：通过 eval+ 赋值的方式
方法三：通过多次 awk 把每个字段赋值

```sh
#!/bin/bash

a="123 456 789"

# 方法一：通过数组的方式
declare -a arr
index=0
for i in $(echo $a | awk '{print $1,$3}')
do
    arr[$index]=$i
    let "index+=1"
done
echo ${arr[0]} # 结果为 123
echo ${arr[1]} # 结果为 789

# 方法二：通过eval+赋值的方式
b=""
c=""
eval $(echo $a | awk '{ printf("b=%s;c=%s",$2,$1)}')
echo $b # 结果为 456
echo $c # 结果为 123

# 方法三：通过多次awk赋值的方式
m=""
n=""
m=`echo $a | awk '{print $1}'`
n=`echo $a | awk '{print $2}'`
echo $m # 结果为 123
echo $n # 结果为 456
```

### 指定分隔符

```sh
#!/bin/bash

string="hello,shell,haha"

# 方法一  
array=(${string//,/ })  
for var in ${array[@]}
do
   echo $var
done

# 方法二
IFS=","
OLD_IFS="$IFS"
IFS="$OLD_IFS"
array2=($string)
for var2 in ${array2[@]}
do
   echo $var2
done

hello
shell
haha
hello
shell
haha
```

## 变量赋值

反引号：

```sh
var=`command`
```

`$()`：

```sh
var=$(command)
```

例如：

```sh
$ A=`date`
$ echo $A
Fri Dec 25 20:02:30 CST 2020 # 变量A存放了date命令的执行结果

$ B=$(date)
$ echo $B
Fri Dec 25 20:03:12 CST 2020 # 变量B存放了date命令的执行结果
```

>注意：= 号前后不要有空格。

**参考：**

https://book.51cto.com/art/201411/457601.htm

## 判断文件夹是否存在

```sh
#!/bin/bash

if [ ! -d testgrid  ];then
  mkdir testgrid
else
  echo dir exist
fi
```

外部传参：

```sh
#!/bin/bash

# 判断传入的参数的个数是不是一个
if [ ! $# -eq 1  ];then
  echo param error!
  exit 1
fi

# 判断目录是不是已经存在，如果不存在则创建，存在则输出"dir exist" 
dirname=$1
echo "the dir name is $dirname"
if [ ! -d $dirname  ];then
  mkdir $dirname
else
  echo dir exist
fi
```

## 循环

类 C 语言：

```sh
# !/bin/sh

for ((i=1; i<=100; i ++))
do
	echo $i
done
```

in 使用：

```sh
# !/bin/sh

for i in {1..100}
do
	echo $i
done
```

seq 使用：

```sh
# !/bin/sh

for i in `seq 1 100`
do
	echo $i
done
```

## 发送微信消息

https://blog.csdn.net/whatday/article/details/105781861

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。