package com.huyingbao.demo.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.huyingbao.demo.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class ViewUtils {

    /**
     * 在屏幕上添加一个转动的小菊花（传说中的Loading），默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param activity                    需要添加菊花的Activity
     * @param customIndeterminateDrawable 自定义的菊花图片，可以为null，此时为系统默认菊花
     * @return {ProgressBar}    菊花对象
     */
    public static ProgressBar createProgressBar(Activity activity, Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null)
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }

    /**
     * 初始化emptyview
     *
     * @param emptyView
     * @param icEmpty
     */
    public static void initEmptyView(View emptyView, int icEmpty) {
//        ImageView ivEmpty = ButterKnife.findById(emptyView, R.id.iv_empty);
//        ivEmpty.setImageResource(icEmpty);
    }

    /**
     * 初始化emptyview
     *
     * @param emptyView
     * @param icEmpty
     * @param infoEmpty
     */
    public static void initEmptyView(View emptyView, int icEmpty, String infoEmpty) {
//        TextView tvEmpty = ButterKnife.findById(emptyView, R.id.tv_empty);
//        ImageView ivEmpty = ButterKnife.findById(emptyView, R.id.iv_empty);
//        tvEmpty.setVisibility(View.VISIBLE);
//        tvEmpty.setText(infoEmpty);
//        ivEmpty.setImageResource(icEmpty);
    }

    /**
     * 获取屏幕宽高
     *
     * @param activity
     * @return
     */
    @NonNull
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = AppUtils.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = AppUtils.getApplication().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 根据分辨率获取图片宽高
     *
     * @param activity
     * @param width
     * @param height
     * @return
     */
    public static int[] getImageSize(Activity activity, int width, int height) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        int req = dm.widthPixels / 3 * 2;
        if (height > req || width > req) {
            float widthRatio = (float) width / (float) req;
            float heightRatio = (float) height / (float) req;
            float ratio = widthRatio > heightRatio ? widthRatio : heightRatio;
            width = (int) (width / ratio);
            height = (int) (height / ratio);
        }
        int[] size = new int[2];
        size[0] = width == 0 ? 300 : width;
        size[1] = height == 0 ? 300 : height;
        return size;
    }

    /**
     * 获取recyclerview 分割线
     *
     * @param mContext
     * @return
     */
    public static RecyclerView.ItemDecoration getItemDecoration(Context mContext) {
        int margin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_10);
        return new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.divider))
                .showLastDivider()
                .margin(margin, margin)
                .sizeResId(R.dimen.dp_04)
                .build();
    }

    /**
     * 获取recyclerview 分割线
     *
     * @param mContext
     * @return
     */
    public static RecyclerView.ItemDecoration getItemDecorationNoMargin(Context mContext) {
        return new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.divider))
                .showLastDivider()
                .sizeResId(R.dimen.dp_04)
                .build();
    }

    /**
     * 获取recyclerview 分割线
     *
     * @param mContext
     * @return
     */
    public static RecyclerView.ItemDecoration getItemDecorationNoMarginNoLast(Context mContext) {
        return new HorizontalDividerItemDecoration.Builder(mContext)
                .color(mContext.getResources().getColor(R.color.divider))
                .sizeResId(R.dimen.dp_04)
                .build();
    }

    /**
     * 获取recyclerview 分割线
     *
     * @param mContext
     * @return
     */
    public static RecyclerView.ItemDecoration getItemDecoration(Context mContext, FlexibleDividerDecoration.VisibilityProvider visibilityProvider) {
        return new HorizontalDividerItemDecoration.Builder(mContext)
                .visibilityProvider(visibilityProvider)
                .color(mContext.getResources().getColor(R.color.divider))
                .sizeResId(R.dimen.dp_04)
                .build();
    }

    /**
     * 获取recyclerview 点分割线
     *
     * @param mContext
     * @return
     */
    public static RecyclerView.ItemDecoration getItemDotDecoration(Context mContext, int margin, FlexibleDividerDecoration.VisibilityProvider visibilityProvider) {
        Paint paint = new Paint();
        paint.setStrokeWidth(mContext.getResources().getDimension(R.dimen.dp_2));
        paint.setColor(mContext.getResources().getColor(R.color.cardview_dark_background));
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{mContext.getResources().getDimension(R.dimen.dp_2), mContext.getResources().getDimension(R.dimen.dp_4)}, 0));
        return new HorizontalDividerItemDecoration.Builder(mContext)
                .visibilityProvider(visibilityProvider)
                .showLastDivider()
                .margin(margin)
                .paint(paint)
                .build();
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static void setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) return;
        //根据DP与PX转换计算值
        int leftPx = isDp ? dip2px(left) : left;
        int rightPx = isDp ? dip2px(right) : right;
        int topPx = isDp ? dip2px(top) : top;
        int bottomPx = isDp ? dip2px(bottom) : bottom;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //通过MarginLayoutParams设置
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            //设置margin
            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(leftPx, topPx, rightPx, bottomPx);
            //或者view.requestLayout();
            view.setLayoutParams(layoutParams);
            return;
        }
        //不是MarginLayoutParams
        if (!(view.getParent() instanceof ViewGroup)) return;
        ViewGroup rootView = (ViewGroup) view.getParent();
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(layoutParams);
        linearLayoutParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(linearLayoutParams);

        LinearLayout linearLayout = new LinearLayout(view.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        rootView.removeView(view);
        linearLayout.addView(view);
        rootView.addView(linearLayout);
    }

    /**
     * 设置全屏
     *
     * @param view
     */
    public static void setFullScreen(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * 设置view显示动画
     *
     * @param view
     * @param show
     */
    public static void setViewAnimate(View view, final boolean show) {
        int shortAnimTime = AppUtils.getApplication().getResources().getInteger(android.R.integer.config_shortAnimTime);
        view.setVisibility(show ? View.GONE : View.VISIBLE);
        view.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }
}