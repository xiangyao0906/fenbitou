package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yizhilu.adapter.viewholder.MyCollectionViewHolder;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;

import java.util.List;


/**
 * @Title: MyCollectionAdapter
 * @Package com.yizhilu.adapter
 * @Description:
 * @author xiangyao
 * @date  2017/8/10 13:59
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionViewHolder> {

    private Context context;
    private List<EntityCourse> datas;
    private boolean flag;  //判断选中按钮时候显示

    public MyCollectionAdapter(Context context, List<EntityCourse> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyCollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCollectionViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_my_course,null));
    }


    @Override
    public void onBindViewHolder(MyCollectionViewHolder holder, int position) {
        StringBuffer buffer = new StringBuffer();
        holder.course_title.setText(datas.get(position).getName());
        holder.course_teacher.setText("讲师 : " + buffer.toString());
        holder.course_lessionnum.setText("课时 : " + datas.get(position).getAddTime());

        ViewGroup.LayoutParams params = holder.course_image.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(context);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.course_image.setLayoutParams(params);

        GlideUtil.loadImage(context, Address.IMAGE_NET + datas.get(position).getMobileLogo(), holder.course_image);
        if (flag) {
            holder.imagecheck.setVisibility(View.VISIBLE);
        } else {
            holder.imagecheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public List<EntityCourse> getDatas() {
        return datas;
    }

    public void setDatas(List<EntityCourse> datas) {
        this.datas = datas;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
