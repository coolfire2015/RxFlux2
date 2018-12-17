package com.huyingbao.rxflux2.base.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huyingbao.simple.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 带上拉加载,下拉刷新的Fragment,实现RxFlux接口,可以接受store传递过来的数据
 * Created by liujunfeng on 2017/1/1.
 */
public abstract class BaseRxFluxListFragment<T> extends BaseRxFluxFragment {
    @BindView(R.id.rv_content)
    protected RecyclerView mRvContent;

    protected List<T> mDataList = new ArrayList();
    protected LinearLayoutManager mLinearLayoutManager;
    protected BaseQuickAdapter mAdapter;

    protected int mFirstIndex = 0;//初始索引,用于第一次获取数据和刷新获取数据
    protected int mNextIndex;//加载更多数据索引

    protected boolean isRefresh;
    protected boolean isLoadingMore = false;//是否需要加载更多,true:需要加载更多,false:加载完成

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initAdapter();
        initRecyclerView();
        refresh();
    }

    /**
     * 实例化RecyclerView,并设置adapter
     */
    protected void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//硬件加速
    }

    /**
     * 首次加载数据或者刷新时调用
     */
    protected void refresh() {
        isRefresh = true;
        mNextIndex = mFirstIndex;
        getDataList(mFirstIndex);
    }

    /**
     * 处理并显示数据
     *
     * @param list
     */
    protected void showDataList(List<T> list) {
        //返回的没有数据,或者返回的为空
        if (list == null || list.size() == 0) {
            isLoadingMore = false;//停止加载数据
            if (isRefresh) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        //返回数据不为空,可以继续加载
        isLoadingMore = true;
        if (isRefresh) {//刷新先把原来数据清空
            isRefresh = false;
            mDataList.clear();
        }
        //添加数据
        mDataList.addAll(list);
        mAdapter.notifyDataSetChanged();
        //更新加载索引
        updateLoadingIndex();
    }

    /**
     * 更新加载索引
     */
    protected void updateLoadingIndex() {
        //mNextIndex = mNextIndex + mLimit;
        mNextIndex = mNextIndex + 1;
    }

    /**
     * 实例化adapter
     */
    protected abstract void initAdapter();

    /**
     * 获取数据
     */
    protected abstract void getDataList(int index);
}