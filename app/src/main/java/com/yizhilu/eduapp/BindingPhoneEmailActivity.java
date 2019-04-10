package com.yizhilu.eduapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 绑定邮箱或手机号
 */
public class BindingPhoneEmailActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.input_Edit)
    EditText inputEdit;
    @BindView(R.id.code_edit)
    EditText codeEdit;
    @BindView(R.id.getCode_button)
    Button getCodeButton;
    @BindView(R.id.confirm_button)
    Button confirmButton;
    @BindView(R.id.textView)
    TextView textView;
    private String type;  //类型
    private EntityPublic entity;  //实体
    private boolean isCountdown,isClick;  //是否发送验证码
    private int userId;  //用户id
    private boolean isFirst;

    @Override
    protected int initContentView() {
        return R.layout.activity_binding_phone_email;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        if (type.equals("phone")) {
            titleText.setText("更换绑定手机");
            textView.setText("新手机号");
            if(isFirst){
                textView.setText("新手机号");
                inputEdit.setHint("请输入手机号");
                inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputEdit.setEnabled(true);
                confirmButton.setText("绑定");
            }else{
                textView.setText("旧手机号");
                inputEdit.setText(entity.getMobile());
                inputEdit.setEnabled(false);
                confirmButton.setText("下一步");
            }
        } else {
            titleText.setText("更换绑定邮箱");
            textView.setText("新邮箱");
            if(isFirst){
                textView.setText("新邮箱");
                inputEdit.setHint("请输入邮箱");
                inputEdit.setEnabled(true);
                confirmButton.setText("绑定");
            }else{
                textView.setText("旧邮箱");
                inputEdit.setText(entity.getEmail());
                inputEdit.setEnabled(false);
                confirmButton.setText("下一步");
            }
        }

    }
    private void getIntentMessage() {
        userId= (int) SharedPreferencesUtils.getParam(BindingPhoneEmailActivity.this,"userId",-1);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        isFirst = intent.getBooleanExtra("isFirst",false);
        entity = (EntityPublic) intent.getSerializableExtra("entity");
    }
    @Override
    protected void initData() {

    }
    @Override
    protected void addListener() {
        inputEdit.addTextChangedListener(this);  //手机号监听输入
        codeEdit.addTextChangedListener(this);  //验证码
    }

    @OnClick({R.id.back_layout,R.id.confirm_button,R.id.getCode_button})
    public void  onclick(View v){
        switch (v.getId()){
            case R.id.back_layout://返回
                this.finish();
                break;
            case R.id.getCode_button:  //获取验证码
                if(!isCountdown){
                    String mobile = inputEdit.getText().toString();
                    if(TextUtils.isEmpty(mobile)){
                        if(type.equals("phone")){
                            IToast.show(BindingPhoneEmailActivity.this,"请输入手机号");
                        }else{
                            IToast.show(BindingPhoneEmailActivity.this,"请输入邮箱");
                        }
                        return;
                    }
                    if(!type.equals("phone")){ // 不是邮箱
                        if(!ValidateUtil.isEmail(mobile)){
                            IToast.show(BindingPhoneEmailActivity.this,"请输入正确的邮箱");
                            return;
                        }
                    }
                    //发送验证码
                    sendVerificationCode(inputEdit.getText().toString());
                }
                break;
            case R.id.confirm_button://提交
                if(isClick){  //可以点击
                    if(isFirst){  //第一次绑定或更换
                        //绑定的方法
                        bindMethod(inputEdit.getText().toString(),codeEdit.getText().toString());
                    }else{
                        //验证验证码的
                        verificationVerifyCode(inputEdit.getText().toString(), codeEdit.getText().toString());
                    }
                }
                break;
        }
    }

    private void bindMethod(String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("bind",mobile);
        map.put("captcha",code);
        if(type.equals("phone")){
            map.put("bindType","2");
        }else{
            map.put("bindType","1");
        }
        OkHttpUtils.post().params(map).url(Address.BINDSTRING).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                isCountdown = true;
                                startThread();
                                BindingPhoneEmailActivity.this.finish();
                            }else {
                                IToast.show(BindingPhoneEmailActivity.this,response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

    private void verificationVerifyCode(String mobile, final String code) {
        Map<String, String> map = new HashMap<>();
        map.put("bind", mobile);
        map.put("captcha", code);
        OkHttpUtils.post().params(map).url(Address.VERIFICATIONVERIFYCODE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                Intent intent  = new Intent();
                                intent.setClass(BindingPhoneEmailActivity.this,BindingPhoneEmailActivity.class);
                                intent.putExtra("type",type);
                                intent.putExtra("entity",entity);
                                intent.putExtra("isFirst",true);
                                startActivity(intent);
                                IToast.show("解绑成功");
                                BindingPhoneEmailActivity.this.finish();
                            }else {
                                IToast.show(BindingPhoneEmailActivity.this,response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }


    /**
     * @author bin
     * 时间: 2017/1/17 18:06
     * 方法说明:发送验证码
     * string: 手机号或邮箱
     */
    private void sendVerificationCode(String mobile) {
        Map<String, String> map = new HashMap<>();
        if (type.equals("phone")) {
            map.put("bind", mobile);
            map.put("bindType", "2");
        } else {
            map.put("bind", mobile);
            map.put("bindType",  "1");
        }
        OkHttpUtils.post().params(map).url(Address.GETVERIFICATIONCODE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                isCountdown = true;
                                startThread();
                                IToast.show(BindingPhoneEmailActivity.this, "验证码发送成功");
                            }else {
                                IToast.show(BindingPhoneEmailActivity.this,response.getMessage());
                            }

                        } catch (Exception e) {
                        }
                    }
                }
        );
    }
    CountDownTimer timer = null;
    private void startThread() {
        timer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getCodeButton.setText("重新获取" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                getCodeButton.setText("获取验证码");
                isCountdown = false;
            }
        }.start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!TextUtils.isEmpty(inputEdit.getText().toString())&&!TextUtils.isEmpty(codeEdit.getText().toString())){
            confirmButton.setBackgroundResource(R.drawable.rounded_rectangle_blue);
            confirmButton.setTextColor(getResources().getColor(R.color.white));
            isClick = true;
        }else{
            confirmButton.setBackgroundResource(R.drawable.rounded_rectangle_white);
            confirmButton.setTextColor(getResources().getColor(R.color.color_dc));
            isClick = false;
        }
    }
}
