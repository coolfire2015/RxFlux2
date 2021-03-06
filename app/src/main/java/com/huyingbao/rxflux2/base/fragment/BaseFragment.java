package com.huyingbao.rxflux2.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huyingbao.rxflux2.base.activity.BaseRxFluxActivity;
import com.huyingbao.rxflux2.inject.component.FragmentComponent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 继承自rxlifecycle
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseFragment extends RxFragment {
    //非静态，除了针对整个App的Component可以静态，其他一般都不能是静态的。
    protected FragmentComponent mFragmentComponent;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //依赖注入
        inject(context);
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
