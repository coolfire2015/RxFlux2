package com.huyingbao.rxflux2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.huyingbao.rxflux2.dispatcher.Dispatcher;
import com.huyingbao.rxflux2.dispatcher.RxViewDispatch;
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.util.DisposableManager;

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
    private static RxFlux sInstance;
    private final Dispatcher mDispatcher;
    private final DisposableManager mDisposableManager;
    private int mActivityCounter;
    private Stack<Activity> mActivityStack;

    /**
     * 私有构造方法
     *
     * @param application
     */
    private RxFlux(Application application) {
        this.mDispatcher = Dispatcher.getInstance();
        this.mDisposableManager = DisposableManager.getInstance();
        mActivityCounter = 0;
        mActivityStack = new Stack<>();
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 实例化
     *
     * @param application
     * @return
     */
    public static RxFlux init(Application application) {
        if (sInstance != null) return sInstance;
        return sInstance = new RxFlux(application);
    }

    /**
     * 关闭
     */
    public static void shutdown() {
        if (sInstance == null) return;
        sInstance.mDisposableManager.clear();
        sInstance.mDispatcher.unsubscribeAll();
    }

    /**
     * @return the sInstance of the mDispatcher
     */
    public Dispatcher getDispatcher() {
        return mDispatcher;
    }

    /**
     * @return the sInstance of the subscription manager in case you want to reuse for something else
     */
    public DisposableManager getDisposableManager() {
        return mDisposableManager;
    }

    /**
     * activity创建成功之后调用,
     * 若activity是RxViewDispatch的子类,
     * 获取需要注册的RxStoreList,并进行注册,将其注册到dispatcher
     *
     * @param activity
     * @param bundle
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mActivityCounter++;
        mActivityStack.add(activity);
        if (activity instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) activity).getRxStoreListToRegister();
            if (rxStoreList != null)
                for (RxStore rxStore : rxStoreList)
                    rxStore.register();
        }
    }

    /**
     * 当activity start的时候,如果当前activity实现RxViewDispatch接口,
     * 将该activity添加到dispatcher的订阅中.
     *
     * @param activity
     */
    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof RxViewDispatch)
            mDispatcher.subscribeRxView((RxViewDispatch) activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    /**
     * 在activity stop时,如果当前activity实现RxViewDispatch,
     * 从dispatcher中取消当前view的注册
     *
     * @param activity
     */
    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof RxViewDispatch)
            mDispatcher.unsubscribeRxView((RxViewDispatch) activity);
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
        mActivityCounter--;
        mActivityStack.remove(activity);
        if (activity instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) activity).getRxStoreListToUnRegister();
            if (rxStoreList != null)
                for (RxStore rxStore : rxStoreList)
                    rxStore.unregister();
        }
        if (mActivityCounter == 0 || mActivityStack.size() == 0)
            RxFlux.shutdown();
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    private Activity getActivity(Class<?> cls) {
        for (Activity activity : mActivityStack)
            if (activity.getClass().equals(cls))
                return activity;
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!mActivityStack.empty())
            mActivityStack.pop().finish();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Class<?> cls) {
        finishActivity(getActivity(cls));
    }
}
