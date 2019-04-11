package com.fenbitou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;

import java.util.List;

import static com.mob.MobSDK.getContext;

/**
 * @author 杨财宾 修改人: 时间:2015-9-8 下午4:30:57 类说明:显示订单的课程
 */
public class OrderCourseAdapter extends BaseAdapter {
    private Context context; // 上下文对象
    private List<EntityCourse> detailList; // 订单课程的集合

    public OrderCourseAdapter(Context context, List<EntityCourse> detailList) {
        super();
        this.context = context;
        this.detailList = detailList;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_course, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
            holder.money_text = (TextView) convertView.findViewById(R.id.money_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleText.setText(detailList.get(position).getShopName());
        holder.money_text.setText(detailList.get(position).getPrice());
        Glide.with(getContext()).load(Address.IMAGE_NET + detailList.get(position).getShopImg()).into(holder.image);
//		GlideUtil.loadImage(getContext(), Address.IMAGE_NET+detailList.get(position).getShopImg(),holder.image);
        return convertView;
    }

    class ViewHolder {
        private ImageView image; // 课程的图片
        private TextView titleText, money_text; // 课程名，价格
    }
}
