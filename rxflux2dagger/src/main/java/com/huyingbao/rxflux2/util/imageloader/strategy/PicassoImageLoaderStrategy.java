package com.huyingbao.rxflux2.util.imageloader.strategy;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.huyingbao.rxflux2.util.imageloader.ImageLoader;

/**
 * Picasso
 * Created by liujunfeng on 2017/1/1.
 */
public class PicassoImageLoaderStrategy implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context ctx, ImageLoader img) {
    }

    @Override
    public void loadImage(Activity activity, ImageLoader img) {

    }

    @Override
    public void loadImage(FragmentActivity fragmentActivity, ImageLoader img) {

    }

    @Override
    public void loadImage(Fragment fragment, ImageLoader img) {

    }
}
