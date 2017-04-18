package com.huyingbao.demo.utils.imageloader;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.huyingbao.demo.R;
import com.huyingbao.demo.utils.imageloader.strategy.BaseImageLoaderStrategy;
import com.huyingbao.demo.utils.imageloader.strategy.GlideImageLoaderStrategy;

/**
 * 图片加载工具类
 * Created by Liu Junfeng on 2017/1/1.
 */
public class ImageLoaderUtils {

    private static BaseImageLoaderStrategy mStrategy;

    private ImageLoaderUtils() {
        mStrategy = new GlideImageLoaderStrategy();
    }

    public static void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

    public static void loadImage(Fragment fragment, ImageLoader img) {
        if (mStrategy == null) mStrategy = new GlideImageLoaderStrategy();
        mStrategy.loadImage(fragment, img);
    }

    public static void loadImage(Context context, ImageLoader img) {
        if (mStrategy == null) mStrategy = new GlideImageLoaderStrategy();
        mStrategy.loadImage(context, img);
    }

    /**
     * 无替换图片
     *
     * @param fragment
     * @param resource
     * @param imageView
     */
    public static void loadImage(Fragment fragment, Object resource, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        loadImage(fragment, builder.build());
    }

    /**
     * 无替换图片
     *
     * @param context
     * @param resource
     * @param imageView
     */
    public static void loadImage(Context context, Object resource, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        loadImage(context, builder.build());
    }

    /**
     * 有替换图片
     *
     * @param fragment
     * @param resource
     * @param placeHolder
     * @param imageView
     */
    public static void loadImage(Fragment fragment, Object resource, int placeHolder, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        builder.placeHolder = placeHolder;
        loadImage(fragment, builder.build());
    }

    /**
     * 有替换图片
     *
     * @param context
     * @param resource
     * @param placeHolder
     * @param imageView
     */
    public static void loadImage(Context context, Object resource, int placeHolder, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        builder.placeHolder = placeHolder;
        loadImage(context, builder.build());
    }

    /**
     * 自定义加载头像
     *
     * @param context
     * @param resource
     * @param imageView
     */
    public static void loadImageHead(Context context, Object resource, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        builder.placeHolder = R.mipmap.ic_launcher;
        loadImage(context, builder.build());
    }

    /**
     * 自定义加载图片 fitCenter
     *
     * @param context
     * @param resource
     * @param imageView
     */
    public static void loadImagePhoto(Context context, Object resource, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        builder.placeHolder = R.color.divider;
        loadImage(context, builder.build());
    }

    /**
     * 自定义加载图片 centerCrop 原比例缩放到没有空白
     *
     * @param context
     * @param resource
     * @param imageView
     */
    public static void loadImagePhotoCenterCrop(Context context, Object resource, ImageView imageView) {
        ImageLoader.Builder builder = new ImageLoader.Builder();
        builder.imgView = imageView;
        builder.resource = resource;
        builder.fitCenter = false;
        builder.placeHolder = R.color.divider;
        loadImage(context, builder.build());
    }
}
