# springboot-method-tracer
基于springboot的方法链路追踪工具
## 示例
可直接执行demo，里面包含了一个controller 与两个service，分别是同步与异步方法，执行后会输出当前方法的调用链路与耗时
![](https://i.loli.net/2021/09/12/wN6a2xXmWYetdL5.png)
格式化之后：
![](https://i.loli.net/2021/09/12/Bd2MlRIDmgQbNKw.png)
## 使用方法
1. 继承AbstractTracer
2. 自定义切点：在 tracePointCut 方法上添加注解:@Pointcut

## 实现原理
aop + 线程封装
