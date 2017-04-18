package com.huyingbao.demo.core.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.huyingbao.demo.core.actions.ActionCreator;
import com.huyingbao.demo.core.actions.BaseActions;
import com.huyingbao.demo.core.api.HttpApi;
import com.huyingbao.demo.core.base.activity.BaseRxFluxActivity;
import com.huyingbao.demo.core.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.demo.core.inject.component.FragmentComponent;
import com.huyingbao.demo.core.inject.qualifier.ContextLife;
import com.huyingbao.demo.stores.base.BaseHttpStore;
import com.huyingbao.demo.stores.base.BaseStore;
import com.huyingbao.demo.utils.LocalStorageUtils;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 继承自rxlifecycle
 * Created by Liu Junfeng on 2017/1/1.
 */
public abstract class BaseFragment extends RxFragment {
    @Inject
    @ContextLife("Activity")
    public Context mContext;

    @Inject
    protected ActionCreator mActionCreator;
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;

    @Inject
    protected HttpApi mHttpApi;
    @Inject
    protected BaseStore mBaseStore;
    @Inject
    protected BaseHttpStore mBaseHttpStore;

    protected FragmentComponent mFragmentComponent;
    protected View mRootView;
    protected Bundle mBundle;
    protected String mAction;
    protected String mTitle;
    private Unbinder mUnbinder;
    private boolean isVisibleToUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //依赖注入
        inject(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //父类中获取argument
        mBundle = getArguments();
        //获取action
        if (mBundle != null && mBundle.containsKey(BaseActions.BASE_ACTION))
            mAction = mBundle.getString(BaseActions.BASE_ACTION);
        //设置布局
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    /**
     * 隐藏状态改变事件
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //从隐藏转为非隐藏的时候调用
        if (!hidden) initActionBar();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    /**
     * 注册RxStore
     * 因为fragment不能像activity通过RxFlux根据生命周期在启动的时候,
     * 调用getRxStoreListToRegister,注册RxStore,只能手动注册
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //绑定view
        mUnbinder = ButterKnife.bind(this, view);
        //绑定view之后运行
        super.onViewCreated(view, savedInstanceState);
        //注册全局store
        if (mBaseStore == null || mBaseHttpStore == null) this.initInjector();
        mBaseStore.register();
        mBaseHttpStore.register();
        //view创建之后的操作
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 显示短暂的Toast
     *
     * @param text
     */
    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示actionbar
     *
     * @param title    页面标题
     * @param backAble true:显示返回按钮,false:不显示返回按钮
     */
    protected void initActionBar(String title, boolean backAble) {
        mTitle = title;
        if (mContext instanceof BaseRxFluxToolbarActivity)
            ((BaseRxFluxToolbarActivity) mContext).initActionBar(title, backAble);
    }

    /**
     * 显示actionbar,显示返回按钮
     *
     * @param title 页面标题
     */
    protected void initActionBar(String title) {
        mTitle = title;
        if (mContext instanceof BaseRxFluxToolbarActivity)
            ((BaseRxFluxToolbarActivity) mContext).initActionBar(title);
    }

    /**
     * 显示actionbar,显示返回按钮,显示页面标题
     * mTitle:有值显示,无值显示manifest中activity label
     */
    protected void initActionBar() {
        if (mContext instanceof BaseRxFluxToolbarActivity)
            ((BaseRxFluxToolbarActivity) mContext).initActionBar(mTitle);
    }

    /**
     * fragment中快速创建action的方法
     *
     * @param actionId
     * @param data
     * @return
     */
    protected RxAction creatAction(@NonNull String actionId, @NonNull Object... data) {
        return mActionCreator.newRxAction(actionId, data);
    }

    /**
     * 启动activity
     *
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    /**
     * 依赖注入
     *
     * @param context
     */
    protected void inject(Context context) {
        //初始化注入器
        mFragmentComponent = ((BaseRxFluxActivity) context).getActivityComponent().getFragmentComponent();
        //注入Injector
        initInjector();
    }

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * 设置view
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 创建之后的操作
     *
     * @param savedInstanceState
     */
    public abstract void afterCreate(Bundle savedInstanceState);
}
