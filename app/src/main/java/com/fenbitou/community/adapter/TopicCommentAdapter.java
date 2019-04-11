package com.fenbitou.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.community.interfaceUtils.OnSingleItemClicListener;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

//话题评论适配器
public class TopicCommentAdapter extends BaseAdapter {

    private Context context;
    private List<EntityPublic> commentDtoList;
    private HolderView holderView = null;
    private boolean isload = true, isSize = true;
    private List<Boolean> booleans;
    private int size;
    private OnSingleItemClicListener onSingleItemClicListener;

    public void setOnSingleItemClicListener(OnSingleItemClicListener onSingleItemClicListener) {
        this.onSingleItemClicListener = onSingleItemClicListener;
    }

    public TopicCommentAdapter(Context context, List<EntityPublic> commentDtoList) {
        super();
        this.context = context;
        this.commentDtoList = commentDtoList;
        booleans = new ArrayList<Boolean>();
    }

    @Override
    public int getCount() {
        if (isSize) {
            size = commentDtoList.size();
            isSize = false;
        }
        if (size != commentDtoList.size()) {
            isload = true;
            isSize = true;
        }
        if (isload) {
            for (int i = 0; i < commentDtoList.size(); i++) {
                booleans.add(false);
            }
            isload = false;
        }
        return commentDtoList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentDtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holderView = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_comment_list, parent, false);
            holderView.topic_comment_avatar = (ImageView) convertView.findViewById(R.id.topic_comment_avatar);
            holderView.topic_comment_nickname = (TextView) convertView.findViewById(R.id.topic_comment_nickname);
            holderView.topic_comment_content = (TextView) convertView.findViewById(R.id.topic_comment_content);
            holderView.topic_comment_addTime = (TextView) convertView.findViewById(R.id.topic_comment_addTime);
            holderView.topic_comment_praise = (TextView) convertView.findViewById(R.id.topic_comment_praise);
            holderView.loft_num = (TextView) convertView.findViewById(R.id.loft_num);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.topic_comment_nickname.setText(commentDtoList.get(position).getNickname());
        holderView.topic_comment_content.setText(StringUtil.replaceHtml(commentDtoList.get(position).getCommentContent()));
        holderView.topic_comment_addTime.setText(commentDtoList.get(position).getAddTime());
        holderView.topic_comment_praise.setText(String.valueOf(commentDtoList.get(position).getPraiseNumber()));
        holderView.topic_comment_praise.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onSingleItemClicListener.OnItemClickListener(position);
            }
        });
        holderView.loft_num.setText(position + 1 + "楼");
        GlideUtil.loadCircleImage(context, Address.IMAGE + commentDtoList.get(position).getAvatar(), holderView.topic_comment_avatar);
        return convertView;
    }



    class HolderView {
        private ImageView topic_comment_avatar;
        private TextView topic_comment_nickname, topic_comment_content, topic_comment_addTime, topic_comment_praise,
                loft_num;
    }


    public void setCommentDtoList(List<EntityPublic> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }
}
