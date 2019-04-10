package com.yizhilu.adapter.viewholder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.widget.NoScrollListView;

/**
 * Created by admin on 2017/6/30.
 * 公共  订单  viewHolder
 */

public class OrderViewHolder {
      public TextView orderNumber, orderstatus, amountText, orderTime; // 订单号,订单状态,实付款,时间
      public LinearLayout undetermined_text;
      public ImageView goPay; // 去支付的图片
      public NoScrollListView listView;
}
