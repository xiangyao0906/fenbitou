package com.fenbitou.BLDowload;

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
 * Created by bishuang on 2017/8/29.
 * 课程下载列表的适配器
 */

public class DownloadListAdapter extends BaseExpandableListAdapter {
    private Context context;  //上下文对象
    private List<EntityPublic> datas;
    private LayoutInflater inflater;
    public DownloadListAdapter(Context context, List<EntityPublic> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getChildKpoints().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if(convertView == null){
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_download_child, null);
            childHolder.chile_image = convertView.findViewById(R.id.chile_image);
            childHolder.childName = convertView.findViewById(R.id.download_child_text);
            childHolder.download_playType = convertView.findViewById(R.id.download_playType); //播放类型
            convertView.setTag(childHolder);
        }else{
            childHolder = (ChildHolder) convertView.getTag();
        }
        EntityPublic courseEntity = datas.get(groupPosition).getChildKpoints().get(childPosition);
        childHolder.childName.setText(courseEntity.getName());
        if (courseEntity.getFileType().equals("AUDIO")) { //播放类型
            childHolder.download_playType.setBackgroundResource(R.drawable.sign_audio_no);
        }else {
            childHolder.download_playType.setBackgroundResource(R.drawable.sign_video_no);
        }
        int isfree = courseEntity.getIsfree();
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).getChildKpoints().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ParentHolder parentHolder = null;
        if(convertView == null){
            parentHolder = new ParentHolder();
            convertView = inflater.inflate(R.layout.item_download_group, null);
            parentHolder.parentName = convertView.findViewById(R.id.download_group_text);
            parentHolder.child_number = convertView.findViewById(R.id.child_number);
            parentHolder.headImage = convertView
                    .findViewById(R.id.download_group_image);
            convertView.setTag(parentHolder);
        }else{
            parentHolder = (ParentHolder) convertView.getTag();
        }
        parentHolder.parentName.setText(datas.get(groupPosition).getName());
        List<EntityPublic> childKpoints = datas.get(groupPosition).getChildKpoints();
        if(childKpoints!=null&&childKpoints.size()>0){
            parentHolder.child_number.setText("共"+childKpoints.size()+"节");
        }else{
            parentHolder.child_number.setText("共0节");
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ParentHolder{
        private TextView parentName,child_number;
        private ImageView headImage;
    }
    class ChildHolder{
        private TextView childName;
        private ImageView chile_image,download_playType;
    }
}
