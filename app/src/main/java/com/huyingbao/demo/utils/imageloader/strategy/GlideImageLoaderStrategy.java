package com.huyingbao.demo.utils.imageloader.strategy;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huyingbao.demo.utils.imageloader.ImageLoader;


public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context context, ImageLoader img) {
        load(img, Glide.with(context).load(img.getResource()));
    }

    @Override
    public void loadImage(Activity activity, ImageLoader img) {
        load(img, Glide.with(activity).load(img.getResource()));
    }

    @Override
    public void loadImage(FragmentActivity fragmentActivity, ImageLoader img) {
        load(img, Glide.with(fragmentActivity).load(img.getResource()));
    }

    @Override
    public void loadImage(Fragment fragment, ImageLoader img) {
        load(img, Glide.with(fragment).load(img.getResource()));
    }

    private void load(ImageLoader img, DrawableTypeRequest<Object> drawableTypeRequest) {
        if (img.isFitCenter()) {//图像居中,缩放到都能看到
            drawableTypeRequest.fitCenter();
        } else {//图像居中,缩放到没有空白
            drawableTypeRequest.centerCrop();
        }
        if (img.getSizeMultiplier() != 0) {
            drawableTypeRequest.thumbnail(img.getSizeMultiplier());
        }
        if (img.getPlaceHolder() != 0) {
            drawableTypeRequest.placeholder(img.getPlaceHolder());
        }
        if (img.getErrorHolder() != 0) {
            drawableTypeRequest.error(img.getErrorHolder());
        }
        if (img.isNetImage()) {//网络图片全部缓存
            drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        } else {//本地图片,不需要缓存
            drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        if (img.getTarget() != null) {
            drawableTypeRequest.into(img.getTarget());
        } else {
            drawableTypeRequest.into(img.getImgView());
        }
    }
}
