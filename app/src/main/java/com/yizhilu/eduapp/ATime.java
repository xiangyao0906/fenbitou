package com.yizhilu.eduapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class ATime extends Activity {

    TextView tv_date = null;
    TextView tv_time = null;
    int year = 2016;
    int month = 10;
    int day = 8;
    int houre = 15;
    int minute = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickerdialog);
        initView();
    }

    private void initView() {
        tv_date = (TextView) findViewById(R.id.dialog_tv_date);
        tv_time = (TextView) findViewById(R.id.dialog_tv_time);
    }

    // 点击事件,湖区日期
    public void getDate(View v) {

//        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                Log.i("ceshi",year+"-----"+monthOfYear+"-----"+dayOfMonth+"---------日期");
//                ATime.this.year = year;
//                month = monthOfYear;
//                day = dayOfMonth;
//
//            }
//        }, 2016, 10, 8).show();
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
//                tvShowDialog.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                Log.i("ceshi",year+"-----"+month+"-----"+day+"---------日期");
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(ATime.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
        showDate();
    }

    // 点击事件,湖区日期
    public void getTime(View v) {
        showTime();
    }

    // 显示选择日期
    private void showDate() {
        tv_date.setText("你选择的日期是：" + year + "年" + month + "月" + day + "日");
    }

    // 显示选择日期
    private void showTime() {
        tv_time.setText("你选择的时间是：" + houre + "时" + minute + "分");
    }



}