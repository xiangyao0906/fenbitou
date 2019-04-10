package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;

import java.util.List;


public class HorizontalListViewAdapter extends RecyclerView.Adapter<HorizontalListViewAdapter.MyViewHolder> {

    private List<EntityPublic> hotGroupList;
    private Context context;
    private int number = 0;

    public HorizontalListViewAdapter(List<EntityPublic> hotGroupList, Context context) {
        this.hotGroupList = hotGroupList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (number == position) {
            holder.hot_group_name.setTextColor(context.getResources().getColor(R.color.Blue));
            holder.xian.setVisibility(View.VISIBLE);
        } else {
            holder.hot_group_name.setTextColor(context.getResources().getColor(R.color.tabText));
            holder.xian.setVisibility(View.GONE);
        }
        holder.hot_group_name.setText(hotGroupList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return hotGroupList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView hot_group_name;
        private ImageView xian;

        public MyViewHolder(View itemView) {
            super(itemView);
            hot_group_name = (TextView) itemView.findViewById(R.id.hot_group_name);
            xian = (ImageView) itemView.findViewById(R.id.xian);
        }
    }

    public void setPosition(int number) {
        this.number = number;
    }

}