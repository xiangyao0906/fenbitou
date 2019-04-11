package com.fenbitou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;

/**
 * Created by xiangyao on 2017/8/8.
 */

public class SystemAcmViewHolder extends RecyclerView.ViewHolder {


    public TextView messageContent, messageTime;

    public SystemAcmViewHolder(View view) {
        super(view);

        messageContent = (TextView) view.findViewById(R.id.message_content);
        messageTime = (TextView) view.findViewById(R.id.message_time);

    }
}
