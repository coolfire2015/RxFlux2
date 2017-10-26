package com.huyingbao.demo.base.activity;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.huyingbao.demo.R;

import butterknife.BindView;


/**
 * 带有toolbar的Activity父类
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxToolbarActivity extends BaseRxFluxActivity {

    @BindView(R.id.tv_top_title)
    protected TextView mTvTopTitle;

    @BindView(R.id.tlb_top)
    protected Toolbar mToolbarTop;

    @BindView(R.id.abl_top)
    protected AppBarLayout mAppBarLayoutTop;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_base;
    }

    @Override
    public void setTitle(CharSequence title) {
        // 设置标题
        mTvTopTitle.setText(title == null ? getTitle() : title);
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
        //取代原本的actionbar
        setSupportActionBar(mToolbarTop);
        //设置actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        //显示右侧返回图标
        if (backAble) {
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_delete);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //不显示home图标
        actionBar.setDisplayShowHomeEnabled(false);
        //不显示标题
        actionBar.setDisplayShowTitleEnabled(false);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        //App Logo
        mToolbarTop.setLogo(R.mipmap.ic_launcher);
        //主标题,默认为app_label的名字
        mToolbarTop.setTitle(null);
        //设置颜色
        mToolbarTop.setTitleTextColor(Color.YELLOW);
        //副标题
        mToolbarTop.setSubtitle(null);
        //设置颜色
        mToolbarTop.setSubtitleTextColor(Color.parseColor("#80FF0000"));
        //侧边栏的按钮
        mToolbarTop.setNavigationIcon(R.drawable.ic_action_refresh);
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
