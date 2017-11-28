package com.huyingbao.simple.main.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huyingbao.simple.R;
import com.huyingbao.simple.main.model.Product;

import java.util.List;

/**
 * Created by liujunfeng on 2017/8/15.
 */

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public ProductAdapter(@Nullable List<Product> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_description, item.getCreatedAt())
                .setText(R.id.tv_product_id, "ProductId:" + item.getProductId());
    }
}
