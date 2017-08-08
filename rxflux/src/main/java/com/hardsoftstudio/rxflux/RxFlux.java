package com.hardsoftstudio.rxflux;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.dispatcher.RxBus;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

import java.util.List;
import java.util.Stack;

/**
 * Created by marcel on 09/09/15.
 * Main class, the init method of this class must be called onCreate of the Application and must
 * be called just once. This class will automatically track the lifecycle of the application and
 * unregister all the remaining subscriptions for each activity.
 * 主类,必须在application创建的时候调用该类的实例方法,并仅调用一次.
 * 这个类会自动跟踪应用程序的生命周期,并且注销每个activity剩余的订阅subscriptions
 */
public class RxFlux implements Application.ActivityLifecycleCallbacks {
    private static RxFlux instance;
    private final RxBus rxBus;
    private final Dispatcher dispatcher;
    private final SubscriptionManager subscriptionManager;
    private int activityCounter;
    private Stack<Activity> activityStack;

    /**
     * 私有构造方法
     *
     * @param application
     */
    private RxFlux(Application application) {
        this.rxBus = RxBus.getInstance();
        this.dispatcher = Dispatcher.getInstance(rxBus);
        this.subscriptionManager = SubscriptionManager.getInstance();
        activityCounter = 0;
        activityStack = new Stack<>();
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 实例化
     *
     * @param application
     * @return
     */
    public static RxFlux init(Application application) {
        if (instance != null) return instance;
        return instance = new RxFlux(application);
    }

    /**
     * 关闭
     */
    public static void shutdown() {
        if (instance == null) return;
        instance.subscriptionManager.clear();
        instance.dispatcher.unsubscribeAll();
    }

    /**
     * @return the instance of the RxBus in case you want to reused for something else
     */
    public RxBus getRxBus() {
        return rxBus;
    }

    /**
     * @return the instance of the dispatcher
     */
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * @return the instance of the subscription manager in case you want to reuse for something else
     */
    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    /**
     * activity创建成功之后调用,
     * 若acitivity是RxViewDispatch的子类,
     * 获取需要注册的RxStoreList,并进行注册,将其注册到dispatcher
     *
     * @param activity
     * @param bundle
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityCounter++;
        activityStack.add(activity);
        if (activity instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) activity).getRxStoreListToRegister();
            if (rxStoreList != null)
                for (RxStore rxStore : rxStoreList)
                    rxStore.register();
        }
    }

    /**
     * 当activity start的时候,如果当前activity是RxViewDispatch,
     * 将该activity添加到dispatcher的订阅中,
     * 并调用onRxViewRegistered方法
     *
     * @param activity
     */
    @Override
    public void onActivityStarted(Activity activity) {
        //activity中的fragment自己实现RxViewDispatch,启动的时候注册,不需要该接口
        //((RxViewDispatch) activity).onRxViewRegistered();
        if (activity instanceof RxViewDispatch)
            dispatcher.subscribeRxView((RxViewDispatch) activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    /**
     * 在activity stop时,如果当前activity是RxViewDispatch,
     * 从dispatcher中取消当前view的注册
     * 并调用onRxViewUnRegistered方法
     *
     * @param activity
     */
    @Override
    public void onActivityStopped(Activity activity) {
        //activity中的fragment自己实现RxViewDispatch,启动的时候注册,不需要该接口
        //((RxViewDispatch) activity).onRxViewUnRegistered();
        if (activity instanceof RxViewDispatch)
            dispatcher.unsubscribeRxView((RxViewDispatch) activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    /**
     * 在activity 销毁的时候,
     *
     * @param activity
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        activityCounter--;
        activityStack.remove(activity);
        if (activity instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) activity).getRxStoreListToUnRegister();
            if (rxStoreList != null)
                for (RxStore rxStore : rxStoreList)
                    rxStore.unregister();
        }
        if (activityCounter == 0 || activityStack.size() == 0)
            RxFlux.shutdown();
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    private Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack)
            if (activity.getClass().equals(cls))
                return activity;
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.empty())
            activityStack.pop().finish();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Class<?> cls) {
        finishActivity(getActivity(cls));
    }
}
