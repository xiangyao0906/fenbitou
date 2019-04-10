package com.yizhilu.Exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.eduapp.R;

import java.util.List;

/**
 * @author ming
 *
 */
public class PracticeReportListAdapter extends BaseAdapter{

	private Context context;
	private List<PublicEntity> listEntity;

	public PracticeReportListAdapter(Context context,
									 List<PublicEntity> listEntity) {
		super();
		this.context = context;
		this.listEntity = listEntity;
	}

	@Override
	public int getCount() {
		return listEntity.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vhHolder = null;
		if (arg1 == null) {
			vhHolder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_practice_report_list, null);
			vhHolder.lin = (LinearLayout) arg1.findViewById(R.id.reportList_lin);
			vhHolder.tv = (TextView) arg1.findViewById(R.id.reportList_text);
			arg1.setTag(vhHolder);
		}else{
			vhHolder = (ViewHolder) arg1.getTag();
		}
		if (listEntity.get(arg0).getQstType() == 6) {
			vhHolder.lin.setBackgroundResource(R.drawable.practice_list_gray);
			vhHolder.tv.setText(arg0+1+"");
			vhHolder.tv.setTextColor(context.getResources().getColor(R.color.color_99));
		}else{
			if (listEntity.get(arg0).getQuestionStatus() == 0) {
				vhHolder.lin.setBackgroundResource(R.drawable.practice_list_greed);
				vhHolder.tv.setText(arg0+1+"");
				vhHolder.tv.setTextColor(context.getResources().getColor(R.color.color_8c));
			}else{
				vhHolder.lin.setBackgroundResource(R.drawable.practice_list_red);
				vhHolder.tv.setText(arg0+1+"");
				vhHolder.tv.setTextColor(context.getResources().getColor(R.color.color_74));
			}
		}

		return arg1;
	}

	class ViewHolder{
		LinearLayout lin;
		TextView tv;
	}

}
