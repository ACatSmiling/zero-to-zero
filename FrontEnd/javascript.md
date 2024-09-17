>**$\textcolor{RubineRed}{Author: ACatSmiling}$**
>
>**$\color{RubineRed}{Since: 2024-09-13}$**

## 数据类型

Javascript 中`原始值`一共有七种：

1. 数值：Number。

2. 大整数：BigInt。

3. 字符串：String。

4. 布尔值：Boolean。

5. 空值：Null。

6. 未定义：Undefined。

7. 符号：Symbol。

   ```javascript
   // 调用 Symbol() 创建了一个符号
   let a = Symbol()
   console.log(typeof a) // "symbol"
   ```

**原始值在 Javascript 中是不可变类型，一旦创建就不能修改。**

## 运算符

运算符优先级：https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Operator_Precedence

> `??=`：空赋值，只有当变量的值为 null 或 undefined 时才会对变量进行赋值。
>
> ```javascript
> let a = null
> a ??= 101
> console.log(a) // 101
> ```

## 流程控制

`使用 {} 来创建代码块`，代码块可以用来对代码进行分组。

- 同一个代码中的代码，就是同一组代码，一个代码块中的代码要么都执行要么都不执行。
- 在代码块中声明的变量无法在代码块的外部访问。

let 和 var：

- **使用 let 声明的变量，具有块作用域。**
- **使用 var 声明的变量，不具有块作用域。**

## 对象

`对象`：对象是 Javascript 中的一种复合数据类型，原始值只能用来表示一些简单的数据，不能表示复杂数据。

### 对象的定义

方式一：

```javascript
let obj = Object()
```

- **使用 typeof 判断对象时，返回 object。**

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

### 修改对象和修改变量

`修改对象`：修改对象时，如果有其他变量指向该对象，则所有指向该对象的变量都会受到影响。

`修改变量`：修改变量时，只会影响当前的变量。

在使用变量存储对象时，很容易因为改变变量指向的对象，提高代码的复杂度。所以，`通常情况下，声明存储对象的变量时会使用 const。`注意：const 只是禁止变量被重新赋值，对对象的修改没有任何影响。

```javascript
const obj = {
    name: "孙悟空",
}

const obj2 = obj

obj2 = {} // obj2 再次赋值，会报错
```

### 对象的方法

`方法（method）`：当一个对象的属性指向一个函数，那么我们就称这个函数是该对象的方法，调用函数就称为调用对象的方法。

```javascript
let obj = {}

obj.name = "孙悟空"
obj.age = 18

// 函数也可以成为一个对象的属性
obj.sayHello = function () {
    alert("hello")
}

console.log(obj)

// 调用对象的方法
obj.sayHello()
```

## 函数

`函数（Function）`：函数也是一个对象，它具有其他对象所有的功能，函数中可以存储代码，且可以在需要时调用这些代码。

### 函数的定义方式

#### 函数声明

语法：

```javascript
function 函数名() {
    语句...
}
```

- **使用 typeof 判断函数时，返回 function。**

#### 函数表达式

语法：

```javascript
const 变量 = function() {
    语句...
}
```

- 匿名函数定义方式。
- 如果函数中的语句只有一条，可以省略 {}。

#### 箭头函数

```javascript
const 变量 = () => {
    语句...
}
```

- 匿名函数定义方式。



## 本文参考

https://www.bilibili.com/video/BV1mG411h7aD

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
