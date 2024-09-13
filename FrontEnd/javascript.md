>**$\textcolor{RubineRed}{Author: ACatSmiling}$**
>
>**$\color{RubineRed}{Since: 2024-09-13}$**

## 对象

### 对象的定义

方式一：

```javascript
let obj = Object()
```

方式二：

```javascript
let obj = {}
```

- 此方式也叫`对象字面量`。

### 对象的属性

方式一：

```javascript
let obj = Object()

// 添加属性
obj.name = "孙悟空"
obj.age = 18
obj.gender = "男"

// 修改属性
obj.name = "Tom sun"

// 删除属性
delete obj.name
```

方式二：

```javascript
{
    属性名: 属性值,
    [属性名]: 属性值
}
```

```javascript
let obj2 = {
    name: "孙悟空",
    age: 18,
    ["gender"]: "男",
    [mySymbol]: "特殊的属性",
    hello: {
        a: 1,
        b: true
    }
}
```

特殊说明一：`使用 [] 去操作属性时，可以使用变量。`

```javascript
// 此时的 str 是一个属性
let str = "address"
obj[str] = "花果山" // 等价于 obj["address"] = "花果山"

// 注意，此时 str 不是变量，而是一个叫 str 的属性
obj.str = "哈哈" // 使用 . 的形式添加属性时，不能使用变量
```

特殊说明二：**可以使用符号（symbol）作为属性名，来添加属性。**

```javascript
let mySymbol = Symbol()
let newSymbol = Symbol()
// 使用 symbol 作为属性名
obj[mySymbol] = "通过symbol添加的属性"
console.log(obj[mySymbol])
```

- 获取这种属性时，也必须使用 symbol。
- 使用 symbol 添加的属性，通常是不希望被外界访问的属性。

特殊说明三：使用`属性名 in 对象名`，可以检查对象中是否含有某个属性。

```javascript
console.log("name" in obj)
```

### 枚举属性

`枚举属性`：指将对象中的所有的属性全部获取。

语法：

```javascript
for(let 变量 in 对象名){
    语句...
}
```

```javascript
let obj = {
    name: '孙悟空',
    age: 18,
    gender: "男",
    address: "花果山",
    [Symbol()]: "测试的属性" // 使用符号添加的属性是不能枚举
}

for (let propName in obj) {
    console.log(propName, obj[propName])
}
```

- 并不是所有的属性都可以枚举，比如，使用符号添加的属性。

### 可变类型

1. `原始值都属于不可变类型`。一旦创建就无法修改，在内存中不会创建重复的原始值。
2. `对象属于可变类型`。对象创建完成后，可以任意的添加删除修改对象中的属性。
   - 当对两个对象进行相等或全等比较时，比较的是对象的内存地址。
   - 如果有两个变量同时指向一个对象，通过一个变量修改对象时，对另外一个变量也会产生影响。

```javascript
let obj = Object()
obj.name = "孙悟空"
obj.age = 18

let obj2 = Object()
let obj3 = Object()
console.log(obj2 == obj3) // false

let obj4 = obj
obj4.name = "猪八戒" // 当修改一个对象时，所有指向该对象的变量都会受到影响

console.log("obj", obj)
console.log("obj4", obj4)
console.log(obj === obj4) // true
```



## 本文参考

https://www.bilibili.com/video/BV1mG411h7aD

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
