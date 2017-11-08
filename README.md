## [简介](http://www.jianshu.com/p/e11712b9b3f7)
[RxFlux2](https://github.com/coolfire2015/RxFlux2) 是在 [RxFlux](https://github.com/skimarxall/RxFlux) 基础上，使用 [RxJava2](https://github.com/ReactiveX/RxJava) 和 [Dagger2](https://github.com/google/dagger) 实现 [Flux模式](https://facebook.github.io/flux/docs/overview.html) 的轻量级框架。
## Why
#### 减少不同层级之间的耦合：
每一层级负责接收数据、发出数据，不关心谁来响应数据变化。RxFlux2封装的Dispatcher来通知谁（View，Store）来响应数据变化。
1. View 只负责用户交互并调用 ActionCreator 中的方法创建RxAction，不关心 RxAction 如何执行（调用接口，操作数据库）。
2. ActionCreator 负责创建 RxAction，操作 RxAction。
操作正确时，发送封装了result data 的 RxAction，不关心哪个 Store 来接收。
操作失败时，发送 RxError，不关心哪个 View 来接收。
3. Store 负责接收 RxAction，处理 RxAction 携带的 result data，发送 RxStoreChange，不关心哪个 View 来接收。
 
####  减少业务模块之间的耦合（View 层级）：
1. Activity 和 Fragment 解耦，Fragment 与 Activity 之间互不调用。
2. Fragment 之间完全解耦，不同 Fragment 之间互不调用。
3. Activity 主要作为 Fragment 容器，负责响应 Fragment 发送的RxAction（经过Store转为RxStoreChange），控制 Fragment 之间的跳转。
4. 不在前端显示的 Activity 和 Fragment 对应的 Store 不会响应 RxAction。
5. 不在前端显示的 Activity 和 Fragment 不会响应RxStoreChange。

## RxFlux2 与 RxFlux 的区别
1. 使用 RxJava2 替换 RxJava1，修改 RxBus 实现方式。

2. 修改 RxAction，向创建的 RxAction 实例中添加 key-value 时，当 value 为空时，当前 key-value 不添加到 RxAction 实例中。

3. 接口 RxViewDispatch 中删除 **_onRxViewRegistered()_** 方法和  **_onRxViewUnRegistered()_** 方法。

4. views 中 activity 由 RxFlux 类根据 activity 生命周期来负责注册 store、注册 view、解除 view 注册、解除 store 注册。

5. 其他 views (fragment、dialogfragment，service)由自己负责注册 store、注册 view、解除 view 注册、解除 store 注册，activity 不再负责其中的 fragments的注册解除操作。views 中的相关操作已被集成到 base 类中。

## RxFlux2 架构图
使用 RxFlux2 最好了解 RxJava2 和 Flux，记住 Flux 的架构图能更好的理解 RxFlux2。

![Flux 架构图](http://upload-images.jianshu.io/upload_images/2118953-b5fe1f790ccd11ea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![RxFlux2 架构图](http://upload-images.jianshu.io/upload_images/2118953-672c855cd9867240.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# RxFlux2 Dagger2及Store生命周期说明
1. RxFlux2 使用 Dagger2 将 store、actioncreator及其他需要的类注入到用到的地方，主要在views中用到。
2. views 包括activitys、fragments、dialogfragments、services。若是需要接收store发出的RxStoreChange，需要实现 RxViewDispatch 接口。在 base 类中已实现该接口，只需要继承 base 类即可。 
3. app 中有一个 AppStore，用来接收全局性的 RxAction，发送全局性的 RxStoreChange。AppStore 跟随 Application 生命周期。
4. 一个 activity 对应一个 ActivityStore（名字随activity），该 activity 及其管理的 fragments 共享这个 ActivityStore。ActivityStore 跟随 activity 生命周期。
5. 某些特殊的 fragment 对应独立的 FragmentStore，该FragmentStore 跟随 fragment 生命周期。
![RxFlux2 框架图](http://upload-images.jianshu.io/upload_images/2118953-374c3faab9bcba21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)