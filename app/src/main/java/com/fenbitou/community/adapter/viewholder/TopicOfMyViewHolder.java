package com.fenbitou.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;
import com.fenbitou.widget.NineGridTestLayout;

/**
 *
 * @author xiangyao
 * @date 2017/11/07
 */

public class TopicOfMyViewHolder extends RecyclerView.ViewHolder {

    public TextView my_topic_title, checking, my_topic_content, my_topic_comment, my_topic_praise, my_topic_browse, new_topic, top, essence, fiery;
    public NineGridTestLayout layout;

    public TopicOfMyViewHolder(View itemView) {
        super(itemView);

        my_topic_title = (TextView) itemView.findViewById(R.id.my_topic_title);
        checking = (TextView) itemView.findViewById(R.id.checking);
        my_topic_content = (TextView) itemView.findViewById(R.id.my_topic_content);
        my_topic_comment = (TextView) itemView.findViewById(R.id.my_topic_comment);
        my_topic_praise = (TextView) itemView.findViewById(R.id.my_topic_praise);
        my_topic_browse = (TextView) itemView.findViewById(R.id.my_topic_browse);
        new_topic = (TextView) itemView.findViewById(R.id.new_topic);
        top = (TextView) itemView.findViewById(R.id.top);
        essence = (TextView) itemView.findViewById(R.id.essence);
        fiery = (TextView) itemView.findViewById(R.id.fiery);
        layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);

    }


}
