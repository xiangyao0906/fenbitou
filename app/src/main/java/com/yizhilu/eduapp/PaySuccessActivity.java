package com.yizhilu.eduapp;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bishuang on 2017/8/21.
 * 支付成功的嘞
 */

public class PaySuccessActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.promptly_pay)
    TextView promptlyPay;

    @Override
    protected int initContentView() {
        return R.layout.act_pay_success;
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void initData() {
        titleText.setText(R.string.pay_success);
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.promptly_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                PaySuccessActivity.this.finish();
                break;
            case R.id.promptly_pay:
                PaySuccessActivity.this.finish();
                break;
        }
    }
}
