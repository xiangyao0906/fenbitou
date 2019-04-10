package com.yizhilu.eduapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyWalletActivity extends BaseActivity {


    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.wallet_gerenmoney)
    TextView walletGerenmoney;
    @BindView(R.id.personAccountLayout)
    RelativeLayout personAccountLayout;
    @BindView(R.id.currentScore)
    TextView currentScore;
    @BindView(R.id.awardLayout)
    RelativeLayout awardLayout;
    @BindView(R.id.windupScore)
    TextView windupScore;
    @BindView(R.id.recordLayout)
    RelativeLayout recordLayout;
    @BindView(R.id.wallet_settlement)
    RelativeLayout walletSettlement;
    private int userId;

    @Override
    protected int initContentView() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initComponent() {
        userId= (int) SharedPreferencesUtils.getParam(MyWalletActivity.this,"userId",-1);
        titleText.setText(getResources().getString(R.string.mywallet));

    }

    @Override
    protected void initData() {
        findmypocket(userId);
        Bundle bundle=getIntent().getExtras();
        walletGerenmoney.setText(bundle.getCharSequence("moneyWallet"));

    }

    @Override
    protected void addListener() {

    }


    @OnClick({R.id.back_layout, R.id.personAccountLayout, R.id.awardLayout, R.id.recordLayout, R.id.wallet_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.personAccountLayout:
                openActivity(PersonalAcountActivity.class);
                break;
            case R.id.awardLayout:
                break;
            case R.id.recordLayout:
                break;
            case R.id.wallet_settlement:
                break;
        }
    }

    public void findmypocket(int userId){

        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).url(Address.MYPOCKE).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(response.isSuccess()){
                    windupScore.setText(response.getEntity().getWindupScore());
                    currentScore.setText(response.getEntity().getCurrentScore());
                }

            }
        });

    }

}
