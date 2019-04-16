package com.fenbitou.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenbitou.utils.GlideUtil;
import com.fenbitou.wantongzaixian.R;


/**
 * Created by admin on 2017/6/30.
 * 首页 gridView 适配器
 */
public class HomeCourseAdapter extends RecyclerView.Adapter<HomeCourseAdapter.HomeCourseViewHolder> {

    private Context context;

    public HomeCourseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HomeCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recond_course_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCourseViewHolder holder, int position) {

        GlideUtil.loadRoundedImage(context,"http://t7.baidu.com/it/u=3334323,3539159968&fm=191&app=48&wm=1,13,90,45,0,7&wmo=10,10&n=0&g=0n&f=JPEG?sec=1853310920&t=ef8d84faa3ed73f5c7ce6f6cfe18811f",holder.courseImage,10);

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class HomeCourseViewHolder extends RecyclerView.ViewHolder {
        public ImageView courseImage;

        public HomeCourseViewHolder(View itemView) {
            super(itemView);
            courseImage=itemView.findViewById(R.id.courseImage);
        }
    }


}
