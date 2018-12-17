package com.huyingbao.rxflux2.base.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.huyingbao.simple.R;


/**
 * 带有toolbar的Activity父类
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxToolbarActivity extends BaseRxFluxActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_base;
    }

    @Override
    public void setTitle(CharSequence title) {
    }

    /**
     * 获取fragment事务,先隐藏已经存在的fragment
     *
     * @return
     */
    protected FragmentTransaction getFragmentTransaction(@IdRes int viewId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(viewId);
        if (fragment != null)
            fragmentTransaction.addToBackStack(fragment.getClass().getName()).hide(fragment);
        return fragmentTransaction;
    }

    /**
     * 设置toolbar
     *
     * @param backAble 是否有回退按钮
     */
    private void setToolbar(boolean backAble) {
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
    }

    /**
     * @param title    actionbar 的title
     * @param backAble 是否显示可返回图标
     */
    public void initActionBar(String title, boolean backAble) {
        //设置标题
        setTitle(title);
        // 设置toolbar
        setToolbar(backAble);
    }

    /**
     * 默认有返回按钮
     *
     * @param title toolbar的title
     */
    public void initActionBar(String title) {
        this.initActionBar(title, true);
    }

    /**
     * 默认有返回按钮,默认使用manifest中label作为的title
     */
    public void initActionBar() {
        this.initActionBar(null, true);
    }
}
