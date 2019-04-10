package com.yizhilu.eduapp;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.utils.Address;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类说明:协议的类
 */
public class AgreementActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.email_text)
    TextView emailText;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected int initContentView() {
        return R.layout.act_agreement;
    }

    @Override
    protected void initComponent() {
        titleText.setText(getResources().getString(R.string.user_agreement));
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(Address.USER_AGREEMENT);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }


    @OnClick(R.id.back_layout)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back_layout: //返回
                this.finish();
                break;
        }
    }
}
