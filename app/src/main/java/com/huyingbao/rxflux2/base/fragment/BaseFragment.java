package com.huyingbao.rxflux2.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxActivity;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxToolbarActivity;
import com.huyingbao.rxflux2.inject.component.FragmentComponent;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.LocalStorageUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 继承自rxlifecycle
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseFragment extends RxFragment {
    @Inject
    @ContextLife("Activity")
    protected Context mContext;
    @Inject
    protected ActionCreator mActionCreator;
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;
    @Inject
    protected AppStore mAppStore;

    protected FragmentComponent mFragmentComponent;
    private Unbinder mUnbinder;

    private boolean isVisibleToUser;
    private String mTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //依赖注入
        inject(context);
        // 注册全局store
        mAppStore.register();
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // fragment中创建菜单
        setHasOptionsMenu(true);
        // 设置布局
        View rootView = inflater.inflate(getLayoutId(), container, false);
        // 绑定view
        mUnbinder = ButterKnife.bind(this, rootView);
        // view创建之后的操作
        afterCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * viewpager中fragment左右滑动,隐藏显示回调方法
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    /**
     * 隐藏状态改变回调方法
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //从隐藏转为非隐藏的时候调用
        if (!hidden)
            initActionBar();
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
     * 依赖注入
     *
     * @param context
     */
    protected void inject(Context context) {
        // 初始化注入器
        mFragmentComponent = ((BaseRxFluxActivity) context).getActivityComponent().getFragmentComponent();
        // 注入Injector
        initInjector();
    }


    /**
     * 显示短暂的Toast
     *
     * @param text
     */
    protected void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
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
