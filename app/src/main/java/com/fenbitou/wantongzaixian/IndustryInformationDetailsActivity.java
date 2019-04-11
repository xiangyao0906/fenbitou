package com.fenbitou.wantongzaixian;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ShareDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class IndustryInformationDetailsActivity extends BaseActivity {


    @BindView(R.id.details_back)
    ImageView detailsBack;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.contener)
    LinearLayout contener;
    @BindView(R.id.information_share)
    LinearLayout informationShare;
    private AgentWeb agentWeb;
    private int informationId;  //咨询的Id
    private EntityCourse entityCourse;  //资讯的实体
    private String title;
    private ShareDialog shareDialog;

    @Override
    protected int initContentView() {
        return R.layout.activity_industry_information_details;
    }

    @Override
    protected void initComponent() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("informationTitle");
        entityCourse = (EntityCourse) bundle.getSerializable("entity");
        informationId = entityCourse.getId();  //资讯的Id

    }

    @Override
    protected void initData() {
        agentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(contener, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()//
                .ready()
                .go(Address.INFORMATION_DETAILS + informationId);
    }

    @Override
    protected void addListener() {

    }



    @OnClick({R.id.details_back, R.id.information_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.details_back:
                finish();
                break;
            case R.id.information_share:
                if(entityCourse == null){
                    return;
                }
                if(shareDialog == null){
                    shareDialog = new ShareDialog(this, R.style.custom_dialog);
                    shareDialog.setCancelable(false);
                    shareDialog.show();
                    shareDialog.shareInfo(entityCourse,false,true,false);
                }else{
                    shareDialog.show();
                }
                break;
        }
    }
}
