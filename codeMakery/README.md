[TOC]
# 第二部分
## StringProperty
在JavaFX中,对一个模型类的所有属性使用 Properties是很常见的. 一个 Property 允许我们, 打个比方, 当 lastName 或其他属性被改变时自动收到通知, 这有助于我们保持视图与数据的同步

## FXCollections
我们处理JavaFX的view classes需要在人员列表发生任何改变时都被通知. 这是很重要的,不然视图就会和数据不同步.为了达到这个目的,JavaFX引入了一些新的集合类.

## initialize()
initialize() 方法在fxml文件完成载入时被自动调用. 那时, 所有的FXML属性都应已被初始化.

# 第三部分
## 监听
在JavaFX中有一个接口称为ChangeListener，带有一个方法changed()。该方法有三个参数：observable, oldValue和newValue。

## TableView删除所有元素后继续删除的ArrayIndexOutOfBoundsException
不能删除掉索引为-1人员项目。索引-1由getSelectedIndex()返回，它意味着你没有选择项目。

## Dialogs
不推荐使用该包，JavaFX中已经继承对话框
对话框使用：https://code.makery.ch/blog/javafx-dialogs-official/

## Lambda语法： 类名::方法名
注意是方法名哦，后面没有括号“()”哒。为啥不要括号，因为这样的是式子并不代表一定会调用这个方法。这种式子一般是用作Lambda表达式，Lambda有所谓懒加载嘛，不要括号就是说，看情况调用方法。
这种[方法引用]或者说[双冒号运算]对应的参数类型是Function<T,R> T表示传入类型，R表示返回类型。
```
例如

表达式:

person -> person.getAge();

可以替换成

Person::getAge

表达式

() -> new HashMap<>();

可以替换成

HashMap::new
```