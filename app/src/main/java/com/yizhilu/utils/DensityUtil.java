package com.yizhilu.utils;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DensityUtil {

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将px值转换为sp值，保证文字大小不变
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值，保证文字大小不变
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //后台返回的图片宽高是600*337
    public static double getHomeLiveListImageHeight(Context context) {
        return MathUtils.divide(getScreenWidth(context), 1.78, 2);
    }

    public static double getHomeListImageViewHeight(Context context) {
        double imageViewWidth = MathUtils.divide(getScreenWidth(context) - 80, 2, 2);
        return MathUtils.divide(imageViewWidth, 2.11, 2);
    }

    public static double getBannerHeight(Context context) {
        return MathUtils.divide(getScreenWidth(context), 2.14, 2);
    }

    public static double getCourseImageViewWidth(Context context) {
        return MathUtils.divide((getScreenWidth(context) - 60), 3, 2);
    }

    public static double getCourseImageViewHeight(double viewWidth) {
        return MathUtils.divide(viewWidth, 2.11, 2);
    }

}
