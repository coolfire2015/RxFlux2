package com.huyingbao.rxflux2

import android.app.Activity
import android.app.Application
import android.os.Bundle

import com.huyingbao.rxflux2.dispatcher.Dispatcher
import com.huyingbao.rxflux2.dispatcher.RxBus
import com.huyingbao.rxflux2.dispatcher.RxViewDispatch
import com.huyingbao.rxflux2.store.RxStore
import com.huyingbao.rxflux2.util.DisposableManager
import java.util.Stack

/**
 * Created by marcel on 09/09/15.
 * Main class, the init method of this class must be called onCreate of the Application and must
 * be called just once. This class will automatically track the lifecycle of the application and
 * unregister all the remaining subscriptions for each activity.
 * 主类,必须在application创建的时候调用该类的实例方法,并仅调用一次.
 * 这个类会自动跟踪应用程序的生命周期,并且注销每个activity剩余的订阅subscriptions
 */
class RxFlux
/**
 * 私有构造方法
 *
 * @param application
 */
private constructor(application: Application) : Application.ActivityLifecycleCallbacks {
    /**
     * @return the instance of the RxBus in case you want to reused for something else
     */
    val rxBus: RxBus
    /**
     * @return the instance of the dispatcher
     */
    val dispatcher: Dispatcher
    /**
     * @return the instance of the subscription manager in case you want to reuse for something else
     */
    val subscriptionManager: DisposableManager
    private var activityCounter: Int = 0
    private val activityStack: Stack<Activity>

    init {
        this.rxBus = RxBus.instance
        this.dispatcher = Dispatcher.getInstance(rxBus)
        this.subscriptionManager = DisposableManager.instance
        activityCounter = 0
        activityStack = Stack()
        application.registerActivityLifecycleCallbacks(this)
    }

    /**
     * activity创建成功之后调用,
     * 若activity是RxViewDispatch的子类,
     * 获取需要注册的RxStoreList,并进行注册,将其注册到dispatcher
     *
     * @param activity
     * @param bundle
     */
    override fun onActivityCreated(activity: Activity, bundle: Bundle) {
        activityCounter++
        activityStack.add(activity)
        if (activity is RxViewDispatch) {
            val rxStoreList = (activity as RxViewDispatch).rxStoreListToRegister
            if (rxStoreList != null)
                for (rxStore in rxStoreList)
                    rxStore.register()
        }
    }

    /**
     * 当activity start的时候,如果当前activity是RxViewDispatch,
     * 将该activity添加到dispatcher的订阅中,
     * 并调用onRxViewRegistered方法
     *
     * @param activity
     */
    override fun onActivityStarted(activity: Activity) {
        //activity中的fragment自己实现RxViewDispatch,启动的时候注册,不需要该接口
        //((RxViewDispatch) activity).onRxViewRegistered();
        if (activity is RxViewDispatch)
            dispatcher.subscribeRxView(activity as RxViewDispatch)
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    /**
     * 在activity stop时,如果当前activity是RxViewDispatch,
     * 从dispatcher中取消当前view的注册
     * 并调用onRxViewUnRegistered方法
     *
     * @param activity
     */
    override fun onActivityStopped(activity: Activity) {
        //activity中的fragment自己实现RxViewDispatch,启动的时候注册,不需要该接口
        //((RxViewDispatch) activity).onRxViewUnRegistered();
        if (activity is RxViewDispatch)
            dispatcher.unSubscribeRxView(activity as RxViewDispatch)
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    /**
     * 在activity 销毁的时候,
     *
     * @param activity
     */
    override fun onActivityDestroyed(activity: Activity) {
        activityCounter--
        activityStack.remove(activity)
        if (activity is RxViewDispatch) {
            val rxStoreList = (activity as RxViewDispatch).rxStoreListToUnRegister
            if (rxStoreList != null)
                for (rxStore in rxStoreList)
                    rxStore.unregister()
        }
        if (activityCounter == 0 || activityStack.size == 0)
            RxFlux.shutdown()
    }

    private fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    private fun getActivity(cls: Class<*>): Activity? {
        for (activity in activityStack)
            if (activity.javaClass == cls)
                return activity
        return null
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        while (!activityStack.empty())
            activityStack.pop().finish()
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(cls: Class<*>) {
        finishActivity(getActivity(cls))
    }

    companion object {
        private var instance: RxFlux? = null

        /**
         * 实例化
         *
         * @param application
         * @return
         */
        fun init(application: Application): RxFlux {
            return if (instance != null) instance else instance = RxFlux(application)
        }

        /**
         * 关闭
         */
        fun shutdown() {
            if (instance == null) return
            instance!!.subscriptionManager.clear()
            instance!!.dispatcher.unSubscribeAll()
        }
    }
}
