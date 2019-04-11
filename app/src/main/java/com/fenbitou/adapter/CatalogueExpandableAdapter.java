package com.fenbitou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

/**
 * @author bin 修改人: 时间:2015-10-23 上午9:54:06 类说明:子目录的适配器
 */
public class CatalogueExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<EntityPublic> courseKpoints; // 子目录的集合
	private EntityPublic entityCourse = null;  //当前选中的实体

	public CatalogueExpandableAdapter(Context context, List<EntityPublic> courseKpoints) {
		super();
		this.context = context;
		this.courseKpoints = courseKpoints;
	}

	public void setSelectEntity(EntityPublic entityCourse){
		this.entityCourse = entityCourse;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return courseKpoints.get(groupPosition).getChildKpoints()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewChild viewChild = null;
		if (convertView == null) {
			viewChild = new ViewChild();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_course_child, null);
			viewChild.chile_image = (ImageView) convertView.findViewById(R.id.chile_image);
			viewChild.child_name = (TextView) convertView
					.findViewById(R.id.child_name);
			viewChild.course_isfree = (TextView) convertView.findViewById(R.id.course_free);  //试听
			viewChild.child_fileType = (ImageView) convertView.findViewById(R.id.child_type); //文件类型
			convertView.setTag(viewChild);
		} else {
			viewChild = (ViewChild) convertView.getTag();
		}
		EntityPublic courseEntity = courseKpoints.get(groupPosition).getChildKpoints().get(childPosition);
		viewChild.child_name.setText(courseEntity.getName());
		int isfree = courseEntity.getIsfree();
		if(isfree == 1){  //试听
			viewChild.course_isfree.setVisibility(View.VISIBLE);
		}else{  //不是试听
			viewChild.course_isfree.setVisibility(View.GONE);
		}
		if (courseEntity.getFileType().equals("AUDIO")) {
			viewChild.child_fileType.setBackgroundResource(R.drawable.sign_audio_no);
		}else {
			viewChild.child_fileType.setBackgroundResource(R.drawable.sign_video_no);
		}
		if(entityCourse!=null){
			if(courseEntity == entityCourse){
				viewChild.chile_image.setBackgroundResource(R.drawable.section_selected);
				viewChild.child_name.setTextColor(context.getResources().getColor(R.color.color_main));
				if (courseEntity.getFileType().equals("AUDIO")) {
					viewChild.child_fileType.setBackgroundResource(R.drawable.sign_audio_yes);
				}else {
					viewChild.child_fileType.setBackgroundResource(R.drawable.sign_video_yes);
				}
			}else{
				viewChild.chile_image.setBackgroundResource(R.drawable.section);
				viewChild.child_name.setTextColor(context.getResources().getColor(R.color.color_67));
				if (courseEntity.getFileType().equals("AUDIO")) {
					viewChild.child_fileType.setBackgroundResource(R.drawable.sign_audio_no);
				}else {
					viewChild.child_fileType.setBackgroundResource(R.drawable.sign_video_no);
				}
			}
		}else {
			viewChild.chile_image.setBackgroundResource(R.drawable.section);
			viewChild.child_name.setTextColor(context.getResources().getColor(R.color.color_67));
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return courseKpoints.get(groupPosition).getChildKpoints().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return courseKpoints.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return courseKpoints.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewParent viewParent = null;
		if (convertView == null) {
			viewParent = new ViewParent();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_course_parent, null);
			viewParent.parent_headImage = (ImageView) convertView.findViewById(R.id.parent_headImage);
			viewParent.parent_name = (TextView) convertView
					.findViewById(R.id.parent_name);
			viewParent.child_number = (TextView) convertView.findViewById(R.id.child_number);
			convertView.setTag(viewParent);
		} else {
			viewParent = (ViewParent) convertView.getTag();
		}
		EntityPublic courseEntity = courseKpoints.get(groupPosition);
		viewParent.parent_name.setText(courseEntity.getName());
		List<EntityPublic> childKpoints = courseEntity.getChildKpoints();
		if(childKpoints!=null&&childKpoints.size()>0){
			viewParent.child_number.setText("共"+childKpoints.size()+"节");
		}else{
			viewParent.child_number.setText("共0节");
		}
		if(entityCourse!=null){
			if(courseEntity == entityCourse){
				viewParent.parent_headImage.setBackgroundResource(R.drawable.xian_select);
				viewParent.parent_name.setTextColor(context.getResources().getColor(R.color.color_main));
			}else{
				viewParent.parent_headImage.setBackgroundResource(R.drawable.xian);
				viewParent.parent_name.setTextColor(context.getResources().getColor(R.color.color_67));
			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewParent {
		private TextView parent_name,child_number;  //父目录的title
		private ImageView parent_headImage;
	}

	class ViewChild {
		private ImageView chile_image,child_fileType;  
		private TextView child_name,course_isfree;
	}
}
