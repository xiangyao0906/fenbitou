package com.yizhilu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.adapter.viewholder.CourseViewHolder;
import com.yizhilu.base.BaseAdapter;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;

import java.util.List;


/**
 * Created by admin on 2017/6/30.
 * 首页 gridView 适配器
 */
public class HomeGridAdapter extends BaseAdapter<EntityPublic> {

    private Context context;

    public HomeGridAdapter(Context context, List<EntityPublic> mList) {
        super(context, mList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseViewHolder holder;
        if (convertView == null) {
            holder = new CourseViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_home_grid, null);
            holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);
            holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
            holder.coursePlayNum = (TextView) convertView.findViewById(R.id.coursePlayNum);
            holder.coursePrice = (TextView) convertView.findViewById(R.id.coursePrice);
            holder.courseFree = (ImageView) convertView.findViewById(R.id.courseFree);
            convertView.setTag(holder);
        } else {
            holder = (CourseViewHolder) convertView.getTag();
        }
        EntityPublic entity = getList().get(position);

        ViewGroup.LayoutParams params = holder.courseImage.getLayoutParams();
        params.height = (int) DensityUtil.getHomeListImageViewHeight(context);
        holder.courseImage.setLayoutParams(params);

        GlideUtil.loadImage(getContext(), Address.IMAGE_NET + entity.getMobileLogo(), holder.courseImage);
        holder.courseName.setText(entity.getCourseName());
        holder.coursePlayNum.setText("播放量：" + entity.getPlaycount());
        int isPay = entity.getIsPay();
        double price = entity.getCurrentPrice();
        if (isPay == 0 || price <= 0) {
            holder.courseFree.setVisibility(View.VISIBLE);
            holder.coursePrice.setText("免费");
        } else {
            holder.courseFree.setVisibility(View.GONE);
            holder.coursePrice.setText("￥" + price);
        }

        return convertView;
    }

}
