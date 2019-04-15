package com.fenbitou.utils;

import android.content.Context;
import android.widget.ImageView;

import com.fenbitou.entity.EntityPublic;
import com.youth.banner.loader.ImageLoader;

/**
 * Title: ${NAME}$
 * Description: 优化图片的加载性能
 *
 * @author xiangyao
 * date 2018/8/2
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {


        GlideUtil.loadImage(context,Address.IMAGE_NET+path,imageView);

    }

    @Override
    public ImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        ImageView simpleDraweeView = new ImageView(context);
        return simpleDraweeView;
    }
}
