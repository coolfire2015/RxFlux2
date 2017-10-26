package com.huyingbao.rxflux2.util.imageloader.strategy;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.huyingbao.rxflux2.util.imageloader.ImageLoader;


/**
 * Created by liujunfeng on 2017/1/1.
 */
public interface BaseImageLoaderStrategy {
    void loadImage(Context context, ImageLoader img);

    void loadImage(Activity activity, ImageLoader img);

    void loadImage(FragmentActivity fragmentActivity, ImageLoader img);

    void loadImage(Fragment fragment, ImageLoader img);
}
