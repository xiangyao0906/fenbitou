package com.yizhilu.eduapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityPublic;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已绑定的邮箱或手机号
 */
public class PhoneOrEmailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.content_text)
    TextView contentText;
    @BindView(R.id.binding_button)
    Button bindingButton;
    private String type;  //类型
    private EntityPublic entity;

    @Override
    protected int initContentView() {
        return R.layout.activity_phone_or_email;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        if(type.equals("phone")){
            titleText.setText("手机");
            contentText.setText("您还没有绑定手机");
            bindingButton.setText("绑定手机");
        }else{
            titleText.setText("邮箱");
            contentText.setText("您还没有绑定邮箱");
            bindingButton.setText("绑定邮箱");
        }
    }

    private void getIntentMessage() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        entity = (EntityPublic) intent.getSerializableExtra("entity");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }
    @OnClick({R.id.back_layout,R.id.binding_button})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.binding_button:  //去绑定
                Intent intent = new Intent();
                intent.setClass(PhoneOrEmailActivity.this,BindingPhoneEmailActivity.class);
                intent.putExtra("entity",entity);
                intent.putExtra("type",type);
                intent.putExtra("isFirst",true);
                startActivity(intent);
                PhoneOrEmailActivity.this.finish();
                break;
        }
    }
}
