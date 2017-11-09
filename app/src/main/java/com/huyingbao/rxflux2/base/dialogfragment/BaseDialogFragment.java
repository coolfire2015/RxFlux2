package com.huyingbao.rxflux2.base.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.base.activity.BaseRxFluxActivity;
import com.huyingbao.rxflux2.inject.component.FragmentComponent;
import com.huyingbao.rxflux2.inject.qualifier.ContextLife;
import com.huyingbao.rxflux2.store.AppStore;
import com.huyingbao.rxflux2.util.LocalStorageUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 继承自rxlifecycle
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseDialogFragment extends RxAppCompatDialogFragment {
    @Inject
    @ContextLife("Activity")
    public Context mContext;
    @Inject
    protected ActionCreator mActionCreator;
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;
    @Inject
    protected AppStore mAppStore;

    protected FragmentComponent mFragmentComponent;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 依赖注入
        inject(context);
        // 注册全局store
        mAppStore.register();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 设置布局
        View rootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        // 绑定view
        mUnbinder = ButterKnife.bind(this, rootView);
        // view创建之后的操作
        afterCreate(savedInstanceState);
        // 创建AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(rootView);
        return builder.create();
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
