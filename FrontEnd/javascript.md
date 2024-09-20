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
const 变量名 = function() {
    语句...
}
```

- 匿名函数定义方式。
- 如果函数中的语句只有一条，可以省略 {}。

#### 箭头函数

```javascript
const 变量名 = () => {
    语句...
}
```

- 匿名函数定义方式。

### 函数的参数

`形式参数`：

1. 在定义函数时，可以在函数中指定数量不等的形式参数（形参）。
2. 在函数中定义形参，就相当于在函数内部声明了对应的变量，但是没有赋值。

`实际参数`：

1. 在调用函数时，可以在函数的 () 中传递数量不等的实际参数（实参）。
2. 实参的个数：
   - **如果实参和形参数量相同，则对应的实参赋值给对应的形参。**
   - **如果实参多于形参，则多余的实参不会使用。**
   - **如果形参多于实参，则多余的形参为 undefined。**
3. 实参的类型：
   - **JavaScript 中不会检查参数的类型，可以传递任何类型的值作为参数。**

参数定义：

```javascript
// 函数声明
function 函数名([参数]){
    语句...
}

// 函数表达式
const 变量名 = function([参数]){
    语句...
}

// 箭头函数
const 变量名 = ([参数]) => {
    语句...
}
```

### 箭头函数的参数

```javascript
const fn = (a, b) => {
    console.log("a = ", a);
    console.log("b = ", b);
}
fn(1, 2)

// 当箭头函数中只有一个参数时，可以省略 ()
const fn2 = a => {
    console.log("a = ", a);
}
fn2(123)

// 定义参数时，可以为参数指定默认值，默认值会在没有对应实参时生效
const fn3 = (a = 10, b = 20, c = 30) => {
    console.log("a = ", a);
    console.log("b = ", b);
    console.log("c = ", c);
}
fn3(1, 2)
```

### 对象作为参数

对象可以作为参数传递，传递实参时，传递并不是变量本身，而是变量中存储的值。

```javascript
function fn(a) {
    console.log("a = ", a.name)

    // a = {} // 如果重新定义了 a，则修改变量时，只会影响当前的变量
    a.name = "猪八戒" // 修改对象时，如果有其他变量指向该对象则所有指向该对象的变量都会受到影响
    console.log("a = ", a.name)
}

// 对象可以作为参数传递
let obj = { name: "孙悟空" }

// 传递实参时，传递并不是变量本身，而是变量中存储的值
fn(obj) // 孙悟空 猪八戒

// 可以放开 fn 中对变量 a 的重新定义的注释，查看 obj 的变化
console.log("obj = ", obj.name)

let obj2 = { name: "沙和尚" }

// 函数每次调用，都会重新创建默认值
function fn2(a = { name: "沙和尚" }) {
    console.log("a = ", a.name)
    a.name = "唐僧"
    console.log("a = ", a.name)
}

fn2() // 沙和尚 唐僧
fn2() // 沙和尚 唐僧
```

### 函数作为参数

在 JavaScript 中，函数也是一个对象（一等函数），别的对象能做的事情，函数也可以。

```javascript
function fn(a) {
    console.log("a = ", a)
    if (typeof a === "function") {
        console.log("当前入参是一个函数")
        // 调用函数
        a()
    }
    // a()
}

// 对象
let obj = { name: "孙悟空" }
fn(obj)

// 函数声明作为参数
function fn2() {
    console.log("我是函数声明")
}
fn(fn2)

// 函数表达式作为参数（匿名函数）
fn(function () {
    console.log("我是函数表达式")
})

// 箭头函数参数
fn(() => console.log("我是箭头函数"))
```

### 函数的返回值

在函数中，可以通过`return 关键字`来指定函数的返回值。

- 返回值就是函数的执行结果，函数调用完毕，返回值便会作为结果返回。
- 任何值都可以作为返回值使用（包括对象和函数之类）。
  - 如果 return 后不跟任何值，则相当于返回 undefined。
  - 如果不写 return，那么函数的返回值依然是 undefined。

```javascript
function sum(a, b) {
    // console.log(a + b)
    // 计算完成后，将计算的结果返回而不是直接打印
    return a + b
}

function fn() {
    // return { name: "孙悟空" } // 返回对象
    // return () => alert(123) // 返回函数

    alert(123)
    return // 返回 undefined
    // return 之后的语句不会执行
    alert(456)
}

let result = fn()

// result = sum(123, 456)
// result = sum(10, result)

console.log("result = ", result)
```

### 箭头函数的返回值

箭头函数的返回值可以直接写在箭头后，**如果直接在箭头后设置对象字面量为返回值时，对象字面量必须使用 () 括起来。**

```javascript
const sum = (a, b) => a + b

const fn = () => ({ name: "孙悟空" }) // 返回值为对象字面量，需要使用 () 括起来

let result = sum(123, 456)
console.log(result)

result = fn()
console.log(result)
```

###  函数的作用域

`作用域（scope）`：作用域指的是一个变量的可见区域。作用域有两种：

- `全局作用域`：全局作用域在网页运行时创建，在网页关闭时销毁。
  - 所有直接编写到 script 标签中的代码都位于全局作用域中。
  - 全局作用域中的变量是全局变量，可以在任意位置访问。
- `局部作用域`：块作用域是一种局部作用域。
  - 块作用域在代码块执行时创建，代码块执行完毕时销毁。
  - 在块作用域中声明的变量是局部变量，只能在块内部访问，外部无法访问。

```javascript
let a = "变量a"

{
    let b = "变量b"

    {
        {
            console.log(b) // 变量b
        }
    }
}

{
    console.log(b) // Uncaught ReferenceError: b is not defined
}
```

`函数的作用域`：

- 函数作用域也是一种局部作用域。
- **函数作用域在函数调用时产生，调用结束后销毁。**
- **函数每次调用都会产生一个全新的函数作用域。**
- 在函数中定义的变量是局部变量，只能在函数内部访问，外部无法访问。

```javascript
function fn() {
    let a = "fn中的变量a"
    console.log(a)
}

fn() // 调用函数，正常输出 "fn中的变量a"

console.log(a) // Uncaught ReferenceError: a is not defined
```

### 作用域链

`作用域链`：当我们使用一个变量时，JavaScript 解释器会优先在当前作用域中寻找变量，如果找到了，则直接使用；如果没找到，则去上一层作用域中寻找，找到了则使用；如果没找到，则继续去上一层寻找，以此类推；如果一直到全局作用域都没找到，则报错 "xxx is not defined"。

### window 对象

`window 对象`：在浏览器中，浏览器提供了一个 window 对象，可以直接访问。

- window 对象代表的是浏览器窗口，通过该对象可以对浏览器窗口进行各种操作。
- window 对象还负责存储 JavaScript 中的内置对象和浏览器的宿主对象。
- window 对象的属性，可以通过 window 对象访问，也可以直接访问。
- 向 window 对象中添加的属性会自动成为全局变量。
- 函数也可以认为是 window 对象的方法。
  - 使用 function 声明的函数，都会作为 window 的方法保存。
- var 用来声明变量，作用和 let 相同，但是 var 不具有块作用域。
  - 在全局中使用 var 声明的变量，都会作为 window 对象的属性保存。
  - 使用 let 声明的变量不会存储在 window 对象中，而存在一个特殊的地方（无法访问）。
  - var 虽然没有块作用域，但有函数作用域。
- 在局部作用域中，如果没有使用 var 或 let 声明变量，则变量会自动成为 window 对象的属性，也就是全局变量。

```javascript
window.a = 1
console.log(a) // 等同于 console.log(window.a)

var b = 20 // 等同于 window.b = 20
console.log(window.b) // 20

function fn() {
    console.log("我是fn")
}
window.fn() // 等同于 fn()，输出 "我是fn"

function fn2() {
    // var d = 10 // var 虽然没有块作用域，但有函数作用域
    d = 10 // 此变量会成为 window 对象的属性
}
fn2()
console.log(d) // 10
```

### 提升

`变量的提升`：使用 var 声明的变量，它会在所有代码执行前被声明，所以我们可以在变量赋值前就访问变量。

- let 声明的变量实际也会提升，但是在赋值之前解释器禁止对该变量的访问。

`函数的提升`：使用函数声明创建的函数，会在其他代码执行前被创建，所以我们可以在函数声明前调用函数。

```javascript
console.log(b) // 可以访问，但输出 undefined
var b = 10

/ fn() // 可以正常调用
function fn() {
    console.log("我是fn函数")
}

// fn2() // 无法调用，Uncaught TypeError: fn2 is not a function，函数的提升只适用函数声明创建的函数
var fn2 = function () {
}

console.log(a) // 无法访问，Uncaught ReferenceError: Cannot access 'a' before initialization
let a = 10
```

>变量和函数的提升同样适用于函数作用域。

### 立即执行函数



## 本文参考

https://www.bilibili.com/video/BV1mG411h7aD

## 声明

写作本文初衷是个人学习记录，鉴于本人学识有限，如有侵权或不当之处，请联系 [wdshfut@163.com](mailto:wdshfut@163.com)。
