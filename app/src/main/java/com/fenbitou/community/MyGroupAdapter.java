package com.fenbitou.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

//我的小组适配器
public class MyGroupAdapter extends BaseAdapter {
	private List<EntityPublic> list;
	private Context context;
	private HolderView holderView = null;
	private String type;

	public MyGroupAdapter(List<EntityPublic> list, Context context, String type) {
		super();
		this.list = list;
		this.context = context;
		this.type = type;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_my_group_list, parent, false);
			holderView.my_group_avatar = (ImageView) convertView.findViewById(R.id.my_group_avatar);
			holderView.my_group_name = (TextView) convertView.findViewById(R.id.my_group_name);
			holderView.my_group_time = (TextView) convertView.findViewById(R.id.my_group_time);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		if (type != null && type.equals("Create")) {
			holderView.my_group_name.setText(list.get(position).getName());
			holderView.my_group_time.setText(list.get(position).getCreateTime());

		} else {
			holderView.my_group_name.setText(list.get(position).getGroupName());
			holderView.my_group_time.setText(list.get(position).getAddTime());
		}
		GlideUtil.loadCircleImage(context,Address.IMAGE + list.get(position).getImageUrl(),holderView.my_group_avatar);
		return convertView;
	}

	class HolderView {
		private ImageView my_group_avatar;
		private TextView my_group_name, my_group_time;
	}

}
