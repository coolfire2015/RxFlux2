package com.huyingbao.demo.utils.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;

/**
 * 图片加载参数设置
 * Created by Liu Junfeng on 2017/1/1.
 *
 * @param <ResourceType>
 */
public class ImageLoader<ResourceType> {
    private ResourceType resource;
    private ImageView imgView;
    private int placeHolder;
    private int errorHolder;
    private float sizeMultiplier;
    private boolean fitCenter;
    private boolean netImage;
    private Target target;

    private ImageLoader(Builder<ResourceType> builder) {
        this.resource = builder.resource;
        this.imgView = builder.imgView;
        this.placeHolder = builder.placeHolder;
        this.errorHolder = builder.errorHolder;
        this.sizeMultiplier = builder.sizeMultiplier;
        this.fitCenter = builder.fitCenter;
        this.netImage = builder.netImage;
        this.target = builder.target;
    }

    public ResourceType getResource() {
        return resource;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public int getErrorHolder() {
        return errorHolder;
    }

    public float getSizeMultiplier() {
        return sizeMultiplier;
    }

    public boolean isFitCenter() {
        return fitCenter;
    }

    public boolean isNetImage() {
        return netImage;
    }

    public Target getTarget() {
        return target;
    }

    public static class Builder<ResourceType> {
        public ResourceType resource;
        public ImageView imgView;
        public int placeHolder;
        public int errorHolder;
        public float sizeMultiplier;
        public boolean fitCenter;
        public boolean netImage;
        public Target target;

        public Builder() {
            this.fitCenter = true;
            this.netImage = true;
            this.sizeMultiplier = 0;
            this.placeHolder = 0;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }
    }
}
