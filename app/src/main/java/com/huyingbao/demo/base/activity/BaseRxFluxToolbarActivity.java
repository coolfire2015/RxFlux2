package com.huyingbao.demo.base.activity;

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
    TextView mTvTopTitle;
    @BindView(R.id.tlb_top)
    protected Toolbar mTlbTop;
    @BindView(R.id.abl_top)
    protected AppBarLayout mAblTop;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_base;
    }

    /**
     * 获取fragment事务,先隐藏已经存在的fragment
     *
     * @return
     */
    protected FragmentTransaction getFragmentTransaction() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fl_content);
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
        //设置toobar
        setSupportActionBar(mTlbTop);
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
     * @param title    actionbar 的title
     * @param backAble 是否显示可返回图标
     */
    public void initActionBar(String title, boolean backAble) {
        //设置标题
        mTvTopTitle.setText(title == null ? getTitle() : title);
        setToolbar(backAble);
    }

    /**
     * 默认可返回的
     *
     * @param title
     */
    public void initActionBar(String title) {
        this.initActionBar(title, true);
    }

    /**
     * 默认可返回的,使用manifest中默认的title
     */
    public void initActionBar() {
        this.initActionBar(null, true);
    }
}
