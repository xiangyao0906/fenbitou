package com.fenbitou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.adapter.viewholder.MyLiveViewHolder;
import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

/**
 * Created by bishuang on 2017/8/29.
 */

public class MyIsLiveAdapter extends BaseAdapter<EntityCourse>{

    private Context context;
    private List<EntityCourse> myPreview;

    public MyIsLiveAdapter(Context context, List<EntityCourse> mList) {
        super(context, mList);
        this.context = context;
        this.myPreview = mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyLiveViewHolder holder;
        if(convertView==null){
            holder=new MyLiveViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_is_live, null);
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
            holder = (MyLiveViewHolder) convertView.getTag();
        }
        if(0==position){
            holder.view_one.setVisibility(View.VISIBLE);
            holder.view_two.setVisibility(View.GONE);
        }else{
            holder.view_one.setVisibility(View.GONE);
            holder.view_two.setVisibility(View.VISIBLE);
        }
        holder.time.setText(myPreview.get(position).getStartTime());
        holder.name.setText(myPreview.get(position).getName());
        holder.chapter.setText(myPreview.get(position).getOneLiveName());
        holder.content.setText(myPreview.get(position).getLiveName());
        GlideUtil.loadImage(context,Address.IMAGE_NET+myPreview.get(position).getMobileLogo(),holder.cours_image);
        holder.teacher.setText(myPreview.get(position).getTeacherName());
        return convertView;
    }
}
