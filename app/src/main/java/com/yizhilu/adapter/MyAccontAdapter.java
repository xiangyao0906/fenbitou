package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yizhilu.adapter.viewholder.MyAccontViewHolder;
import com.yizhilu.entity.EntityAccList;
import com.yizhilu.eduapp.R;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/16.
 */

public class MyAccontAdapter extends RecyclerView.Adapter<MyAccontViewHolder> {
    private Context context;
    private List<EntityAccList> accouList;

    public MyAccontAdapter(Context context, List<EntityAccList> accouList) {
        this.context = context;
        this.accouList = accouList;
    }

    @Override
    public MyAccontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAccontViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myaccount,null));
    }

    @Override
    public void onBindViewHolder(MyAccontViewHolder holder, int position) {


        String timeString = accouList.get(position).getCreateTime();
        String month = timeString.split(":")[0];
        String time = timeString.split(":")[1].split(":")[0];

        holder.tv_account_time.setText(month+":"+time);
        String historyType = accouList.get(position).getActHistoryType();
        if("SALES".equals(historyType)){
            holder.tv_account_xiaofei.setText("消费");
            holder.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_dd3214));
            holder.tv_account_money.setText("- "+accouList.get(position).getTrxAmount()+ "");
        }else if("REFUND".equals(historyType)){
            holder.tv_account_xiaofei.setText("退款");
            holder.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
            holder.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
        }else if("CASHLOAD".equals(historyType)){
            holder.tv_account_xiaofei.setText("现金充值");
            holder.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
            holder.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
        }else if("VMLOAD".equals(historyType)){
            holder.tv_account_xiaofei.setText("充值卡充值");
            holder.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
            holder.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
        }
        
        
        

    }

    @Override
    public int getItemCount() {
        return accouList.size();
    }

    public List<EntityAccList> getAccouList() {
        return accouList;
    }

    public void setAccouList(List<EntityAccList> accouList) {
        this.accouList = accouList;
    }
}
