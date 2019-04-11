package com.fenbitou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.adapter.viewholder.MyToReviewViewHolder;
import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2017/8/29.
 */

public class MyToReviewAdapter extends BaseAdapter<EntityCourse>{

    private Context context;
    private List<EntityCourse> myTo;

    public MyToReviewAdapter(Context context, List<EntityCourse> mList) {
        super(context, mList);
        this.context = context;
        this.myTo = mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyToReviewViewHolder holder=null;
        if(convertView==null){
            holder=new MyToReviewViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_to_review, null);
            holder.time=(TextView) convertView.findViewById(R.id.time);
            holder.name=(TextView) convertView.findViewById(R.id.name);
            holder.chapter=(TextView) convertView.findViewById(R.id.chapter);
            holder.content=(TextView) convertView.findViewById(R.id.content);
            holder.cours_image=(ImageView) convertView.findViewById(R.id.cours_image);
            holder.teacher=(TextView) convertView.findViewById(R.id.teacher);
            holder.view_one=(View) convertView.findViewById(R.id.view_one);
            holder.view_two=(View) convertView.findViewById(R.id.view_two);
            convertView.setTag(holder);
        } else {
            holder = (MyToReviewViewHolder) convertView.getTag();
        }
        if(0==position){
            holder.view_one.setVisibility(View.VISIBLE);
            holder.view_two.setVisibility(View.GONE);
        }else{
            holder.view_one.setVisibility(View.GONE);
            holder.view_two.setVisibility(View.VISIBLE);
        }
        holder.time.setText(myTo.get(position).getStartTime());
        holder.name.setText(myTo.get(position).getName());
        holder.chapter.setText(myTo.get(position).getOneLiveName());
        holder.content.setText(myTo.get(position).getLiveName());
        GlideUtil.loadImage(context,Address.IMAGE_NET+myTo.get(position).getMobileLogo(),holder.cours_image);
        holder.teacher.setText(myTo.get(position).getTeacherName());
        return convertView;
    }
}
