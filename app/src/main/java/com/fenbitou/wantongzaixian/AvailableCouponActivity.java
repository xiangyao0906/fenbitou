package com.fenbitou.wantongzaixian;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.adapter.AvailableCouponAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.widget.NoScrollListView;

public class AvailableCouponActivity extends BaseActivity implements View.OnClickListener {
    private NoScrollListView listView;  //可用優惠券的的列表
    private EntityPublic entityPublic;  //优惠券的实体
    private LinearLayout back_layout;  //返回的布局
    private TextView title_text;  //标题
    private LinearLayout top_layout;  //标题的布局
    private int posotionSelect; // 当前选中position
    private AvailableCouponAdapter adapter;
    private TextView collection_image_edit; // 取消选中
    private updateIsShow isShow; //接收显示取消选中的广播
    private boolean isSelect;
    private int position = -1;

    @Override
    protected int initContentView() {
        return R.layout.copy_of_fragment_available_coupon;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
        title_text = (TextView) findViewById(R.id.collection_text);  //标题
        listView = (NoScrollListView) findViewById(R.id.listView);  //可用優惠券的列表
        collection_image_edit = (TextView) findViewById(R.id.collection_image_edit); // 取消选中
        title_text.setText(getResources().getString(R.string.discount_coupon));  //设置标题
        adapter = new AvailableCouponAdapter(AvailableCouponActivity.this, entityPublic.getEntity());
        listView.setAdapter(adapter);
        getSharedPreferences("selectPosition", MODE_PRIVATE).edit().putInt("selectPosition", -1).commit();
        boolean isposition = getSharedPreferences("isbackPosition", MODE_PRIVATE).getBoolean("isbackPosition", false);
        if (isposition) {
            int position = getSharedPreferences("backPosition", MODE_PRIVATE).getInt("backPosition", 0);
            adapter.changeSelected(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {
        back_layout.setOnClickListener(this);  //返回
        listView.setOnItemClickListener(this);  //优惠券的点击事件
        collection_image_edit.setOnClickListener(this); // 取消选中
    }

    private void getIntentMessage() {
        Intent intent = getIntent();
        entityPublic = (EntityPublic) intent.getSerializableExtra("entity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShow == null) {
            isShow = new updateIsShow();
            IntentFilter filter = new IntentFilter();
            filter.addAction("inform");
            registerReceiver(isShow, filter);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        isSelect = true;
        posotionSelect = position;
        adapter.changeSelected(posotionSelect);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:  //返回
                position = getSharedPreferences("selectPosition", MODE_PRIVATE).getInt("selectPosition", -1);
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(1, intent);
                this.finish();
                break;
            case R.id.collection_image_edit://点击取消选中
                getSharedPreferences("selectPosition", MODE_PRIVATE).edit().putInt("selectPosition", -1).commit();
                adapter = new AvailableCouponAdapter(AvailableCouponActivity.this, entityPublic.getEntity());
                listView.setAdapter(adapter);
                isSelect = false;
                break;
            default:
                break;
        }
    }

    //	接收显示通知的广播
    class updateIsShow extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String action = arg1.getAction();
            if (action.equals("inform")) {
                collection_image_edit.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShow != null) {
            AvailableCouponActivity.this.unregisterReceiver(isShow);
        }
        if (isSelect) {
            getSharedPreferences("backPosition", MODE_PRIVATE)
                    .edit().putInt("backPosition", posotionSelect).commit();
            getSharedPreferences("isbackPosition", MODE_PRIVATE)
                    .edit().putBoolean("isbackPosition", true).commit();
        } else {
            if (position == -1) {
                getSharedPreferences("isbackPosition", MODE_PRIVATE)
                        .edit().putBoolean("isbackPosition", false).commit();
            }
        }
    }


}
