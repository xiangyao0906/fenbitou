package com.fenbitou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.adapter.viewholder.CommentViewHolder;
import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.ValidateUtil;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 * 课程评论 适配器
 */

public class CourseCommentAdapter extends BaseAdapter<EntityPublic>{

    public CourseCommentAdapter(Context context, List<EntityPublic> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CommentViewHolder holder;
        if(convertView == null){
            holder = new CommentViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_course_comment, null);
            holder.userHead = (ImageView) convertView.findViewById(R.id.discuss_head);
            holder.userName = (TextView) convertView.findViewById(R.id.discuss_user_name);
            holder.content = (TextView) convertView.findViewById(R.id.discuss_content);
            holder.time = (TextView) convertView.findViewById(R.id.discuss_time);
            convertView.setTag(holder);
        }else{
            holder = (CommentViewHolder) convertView.getTag();
        }
        EntityPublic entity = getList().get(position);
        String nickname = entity.getNickname();
        String email =entity.getEmail();
        String mobile = entity.getMobile();
        if(!TextUtils.isEmpty(nickname)){
            holder.userName.setText(nickname);
        }else if(!TextUtils.isEmpty(email)){
            holder.userName.setText(email);
        }else{
            holder.userName.setText(ValidateUtil.hideStar(mobile));
        }

        holder.content.setText(entity.getContent());

        String timeString = entity.getCreateTime();
        String month = timeString.split(":")[0];
        String time = timeString.split(":")[1].split(":")[0];
        holder.time.setText(month+":"+time);
        GlideUtil.loadCircleHeadImage(getContext(),Address.IMAGE_NET+ entity.getAvatar(),holder.userHead);
        return convertView;
    }

}
