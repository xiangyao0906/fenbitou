package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yizhilu.adapter.viewholder.SystemAcmViewHolder;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/8.
 */

public class SystemAcmAdapter extends RecyclerView.Adapter<SystemAcmViewHolder> {
    private List<EntityPublic> letters;  //公告的集合
    private Context context;

    public SystemAcmAdapter(List<EntityPublic> letters, Context context) {
        this.letters = letters;
        this.context = context;
    }

    @Override
    public SystemAcmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SystemAcmViewHolder(LayoutInflater.from(context).inflate(R.layout.systemacm_layout_item, null));
    }

    @Override
    public void onBindViewHolder(SystemAcmViewHolder holder, int position) {
        String timeString = letters.get(position).getAddTime();
        Spanned fromHtml = Html.fromHtml(letters.get(position).getContent());
        holder.messageTime.setText(timeString);
        holder.messageContent.setText(fromHtml);
    }

    @Override
    public int getItemCount() {
        return letters.size();
    }

    public List<EntityPublic> getLetters() {
        return letters;
    }

    public void setLetters(List<EntityPublic> letters) {
        this.letters = letters;
    }
}
