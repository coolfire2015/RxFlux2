package com.huyingbao.demo.utils.imageloader.strategy;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.huyingbao.demo.utils.imageloader.ImageLoader;

/**
 * Picasso
 * Created by Liu Junfeng on 2017/1/1.
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
