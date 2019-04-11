package com.fenbitou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by admin on 2017/6/30.
 * 标签 适配器
 */

public class TagFlowAdapter extends TagAdapter<EntityPublic>{

    private Context context;
    private List<EntityPublic> list;

    public TagFlowAdapter(List<EntityPublic> list,Context context) {
        super(list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, EntityPublic courseEntity) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tag_text,parent,false);
        if (!TextUtils.isEmpty(courseEntity.getSubjectName())){
            tv.setText(list.get(position).getSubjectName());
        }else {
            tv.setText(list.get(position).getName());
        }

        return tv;
    }

}
