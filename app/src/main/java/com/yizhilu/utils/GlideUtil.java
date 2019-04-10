package com.yizhilu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yizhilu.eduapp.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author admin
 * @date 2017/7/4
 * 加载图片工具类
 */

public class GlideUtil {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.defined_image) //设置占位图，在加载之前显示
                .error(R.drawable.defined_image) //在图像加载失败时显示
                .fallback(R.drawable.defined_image)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.defined_user_head) //设置占位图，在加载之前显示
                .error(R.drawable.defined_user_head) //在图像加载失败时显示
                .fallback(R.drawable.defined_user_head)
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆形头像
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleHeadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.defined_user_head) //设置占位图，在加载之前显示
                .error(R.drawable.defined_user_head) //在图像加载失败时显示
                .fallback(R.drawable.defined_user_head)
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundedImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.defined_image) //设置占位图，在加载之前显示
                .error(R.drawable.defined_image) //在图像加载失败时显示
                .fallback(R.drawable.defined_image)
                .bitmapTransform(new RoundedCornersTransformation(context, 80, 0, RoundedCornersTransformation.CornerType.ALL))
                .crossFade()
                .into(imageView);
    }


}
