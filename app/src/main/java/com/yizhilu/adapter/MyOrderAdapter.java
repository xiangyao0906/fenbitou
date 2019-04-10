package com.yizhilu.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yizhilu.eduapp.PaymentConfirmOrderActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.OrderEntity;
import com.yizhilu.utils.IToast;
import com.yizhilu.widget.NoScrollListView;

import java.util.List;

public class MyOrderAdapter extends BaseAdapter {

	private Context context;
	private List<OrderEntity> orderList; // 订单的实体
	private Intent intent; // 意图对象

	public MyOrderAdapter(Context context, List<OrderEntity> orderList) {
		super();
		this.context = context;
		this.orderList = orderList;
		intent = new Intent();
	}

	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order, null);
			holder.orderNumber = (TextView) convertView.findViewById(R.id.orderNumber);
			holder.undetermined_text = (LinearLayout) convertView.findViewById(R.id.undetermined_text);
			holder.paystatus = (TextView) convertView.findViewById(R.id.paystatus);
			holder.goPay = (ImageView) convertView.findViewById(R.id.goPay);
			holder.listView = (NoScrollListView) convertView.findViewById(R.id.listView);
			holder.amountText = (TextView) convertView.findViewById(R.id.amountText);
			holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.orderNumber.setText(orderList.get(position).getOrderNo());
		final String trxStatus = orderList.get(position).getOrderState();
		if ("APPROVE".equals(trxStatus)) { // 待处理
			holder.paystatus.setText(context.getResources().getString(R.string.APPROVE));
			holder.goPay.setVisibility(View.GONE);
			holder.paystatus.setTextColor(context.getResources().getColor(R.color.Red));
		} else if ("INIT".equals(trxStatus)) { // 待支付
			Log.i("xm", trxStatus);
			holder.paystatus.setText(context.getResources().getString(R.string.INIT));
			holder.goPay.setVisibility(View.VISIBLE);
			holder.paystatus.setTextColor(context.getResources().getColor(R.color.Red));
		} else if ("SUCCESS".equals(trxStatus)) { // 已完成
			holder.paystatus.setText(context.getResources().getString(R.string.SUCCESS));
			holder.goPay.setVisibility(View.GONE);
			holder.paystatus.setTextColor(context.getResources().getColor(R.color.order_pay_success));
		} else if ("REFUND".equals(trxStatus)) { // 已退款
			holder.paystatus.setText(context.getResources().getString(R.string.REFUND));
			holder.goPay.setVisibility(View.GONE);
			holder.paystatus.setTextColor(context.getResources().getColor(R.color.Red));
		} else if ("CANCEL".equals(trxStatus)) { // 已取消
			holder.paystatus.setText(context.getResources().getString(R.string.CANCEL));
			holder.goPay.setVisibility(View.GONE);
			holder.paystatus.setTextColor(context.getResources().getColor(R.color.color_7f));
		}
		holder.amountText.setText("¥ " + orderList.get(position).getRealPrice());

		String timeString = orderList.get(position).getCreateTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];

		// holder.timeText.setText(orderList.get(position).getCreateTime()+"下单");
		holder.timeText.setText(month + ":" + time + "下单");
		List<EntityCourse> detailList = orderList.get(position).getOrderDetailsList();
		if (detailList != null && detailList.size() > 0) {
			holder.listView.setAdapter(new OrderCourseAdapter(context, detailList));
		}
		holder.undetermined_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("INIT".equals(trxStatus)) {
					intent.setClass(context, PaymentConfirmOrderActivity.class);
					intent.putExtra("orderId", orderList.get(position).getOrderId());
					context.startActivity(intent);
				}
			}
		});
		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
				final String trxStatus = orderList.get(position).getOrderState();
				int courseId = orderList.get(position).getOrderDetailsList().get(arg2).getDataId();
				String dataType = orderList.get(position).getOrderDetailsList().get(arg2).getDataType();
				if (dataType.equals("COURSE")){
					if ("INIT".equals(trxStatus)) { // 待支付
						intent.setClass(context, PaymentConfirmOrderActivity.class);
						intent.putExtra("orderId", orderList.get(position).getOrderId());
						context.startActivity(intent);
					}else {
						if (orderList.get(position).getOrderDetailsList().get(arg2).getCourseType().equals("COURSE")){
//							intent.setClass(context, CourseDetails96kActivity.class);
							intent.putExtra("courseId", courseId);
							context.startActivity(intent);
						}else{
//							intent.setClass(context, BLLiveDetailsActivity.class);
//							intent.putExtra("courseId", courseId);
//							context.startActivity(intent);
						}
					}
				}else if (dataType.equals("BOOK")){
					if ("INIT".equals(trxStatus)) { // 待支付
						intent.setClass(context, PaymentConfirmOrderActivity.class);
						intent.putExtra("orderId", orderList.get(position).getOrderId());
						context.startActivity(intent);
					}else {
						IToast.show("请前往pc端观看");
					}
				}else {
					IToast.show(context,dataType);
				}

			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView orderNumber, paystatus, amountText, timeText; // 订单号,订单状态,实付款,时间
		private LinearLayout undetermined_text;
		private ImageView goPay; // 去支付的图片
		private NoScrollListView listView;
	}
}
