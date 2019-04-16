package com.fenbitou.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenbitou.entity.HomeLiveItemEntity;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

public class HomeReconnedLiveAdapter extends BaseQuickAdapter<HomeLiveItemEntity, BaseViewHolder> {
    public HomeReconnedLiveAdapter( @Nullable List<HomeLiveItemEntity> data) {
        super(R.layout.home_reconned_live_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeLiveItemEntity item) {



      ImageView imageView=   helper.getView(R.id.courseImage);
        GlideUtil.loadRoundedImage(mContext,"http://t7.baidu.com/it/u=3334323,3539159968&fm=191&app=48&wm=1,13,90,45,0,7&wmo=10,10&n=0&g=0n&f=JPEG?sec=1853310920&t=ef8d84faa3ed73f5c7ce6f6cfe18811f",imageView,10);

    }
}
