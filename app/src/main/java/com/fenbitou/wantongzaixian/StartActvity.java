package com.fenbitou.wantongzaixian;


import android.content.Intent;
import android.os.Handler;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.utils.SharedPreferencesUtils;


public class StartActvity extends BaseActivity {
    private Handler handler;
    private boolean isFrist;
    private Intent intent;


    @Override
    protected int initContentView() {
        return R.layout.activity_start_actvity;
    }

    @Override
    protected void initComponent() {
        isFrist = (boolean) SharedPreferencesUtils.getParam(this, "isFrist", false);
    }

    @Override
    protected void initData() {
        intent=new Intent();

        handler = new Handler();
        if (isFrist) {  //是第一次进入程序
            handler.postDelayed(runnable, 1000);
        } else {  //不是第一次进入程序
            handler.postDelayed(runnable, 3000);
        }

    }

    @Override
    protected void addListener() {

    }

    //延时跳转的对象
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (!isFrist) {  //是第一次进入程序,跳入到导航页
                intent.setClass(StartActvity.this, GuideActivity.class);
            } else {  //不是第一次进入程序,跳入到主页
                intent.setClass(StartActvity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };
}
