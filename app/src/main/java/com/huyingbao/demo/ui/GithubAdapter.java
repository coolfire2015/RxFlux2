package com.huyingbao.demo.ui;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huyingbao.demo.R;
import com.huyingbao.demo.model.GitHubRepo;

import java.util.List;

/**
 * Created by liujunfeng on 2017/8/15.
 */

public class GithubAdapter extends BaseQuickAdapter<GitHubRepo, BaseViewHolder> {
    public GithubAdapter(@Nullable List<GitHubRepo> data) {
        super(R.layout.item_github_repository, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GitHubRepo item) {
        helper.setText(R.id.tv_repository_name, item.getName())
                .setText(R.id.tv_repository_description, item.getDescription())
                .setText(R.id.tv_repository_id, "GithubId" + item.getId());
    }
}
