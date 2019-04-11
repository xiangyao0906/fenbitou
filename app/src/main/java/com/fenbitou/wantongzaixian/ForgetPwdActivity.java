package com.fenbitou.wantongzaixian;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.base.DemoApplication;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.entity.PublicStringEntity;
import com.fenbitou.entity.PublicStringEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.confirm_passWord_edit;
import static com.fenbitou.wantongzaixian.R.id.get_obtain_code;

/**
 * Created by bishuang on 2017/7/28.
 * 忘记密码的类
 */

public class ForgetPwdActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.goto_register)
    LinearLayout gotoRegister;
    @BindView(R.id.userName_edit)
    EditText userNameEdit;
    @BindView(R.id.verification_code_edit)
    EditText verificationCodeEdit;
    @BindView(get_obtain_code)
    TextView getObtainCode;
    @BindView(R.id.passWord_edit)
    EditText passWordEdit;
    @BindView(confirm_passWord_edit)
    EditText confirmPassWordEdit;
    @BindView(R.id.confirm_passWord_line)
    View confirmPassWordLine;
    @BindView(R.id.config_modify)
    TextView configModify;
    private boolean isCountdown; // 是否是发送验证码

    @Override
    protected int initContentView() {
        return R.layout.act_forger_pwd;
    }

    @Override
    protected void initComponent() {
        titleText.setText(R.string.pass_retrieve); // 设置标题
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.goto_register, get_obtain_code, R.id.config_modify})
    public void onViewClicked(View view) {
        String mobile = userNameEdit.getText().toString();
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.goto_register://去登陆
                finish();
                break;
            case get_obtain_code: //获取验证码
                if (!isCountdown) {
                    if (TextUtils.isEmpty(mobile)) {
                        IToast.show(ForgetPwdActivity.this,"请输入手机号/邮箱");
                        return;
                    }
                    if (!ValidateUtil.isMobile(mobile)&&!ValidateUtil.isEmail(mobile)) {
                        IToast.show(ForgetPwdActivity.this,"请输入正确的手机号/邮箱");
                        return;
                    }else if(ValidateUtil.isMobile(mobile)){
                        //手机找回密码,获取sgin值
                        getSginData(mobile);
                    }else if(ValidateUtil.isEmail(mobile)){
                        //邮箱找回密码
                        getEmailsCode(mobile);
                    }
                }
                break;
            case R.id.config_modify: //确认修改
                String code = verificationCodeEdit.getText().toString();
                String passWord = passWordEdit.getText().toString();
                String confirmPass = confirmPassWordEdit.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    IToast.show(ForgetPwdActivity.this,"请输入手机号/邮箱");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    IToast.show(ForgetPwdActivity.this,"请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    IToast.show(ForgetPwdActivity.this,"请输入密码");
                    return;
                }
                if(TextUtils.isEmpty(confirmPass)){
                    IToast.show(ForgetPwdActivity.this,"请输入确认密码");
                    return;
                }
                if (!ValidateUtil.isMobile(mobile)&&!ValidateUtil.isEmail(mobile)) {
                    IToast.show(ForgetPwdActivity.this,"请输入正确的手机号/邮箱");
                    return;
                }
                if (!(passWord.length() >= 6 && passWord.length() <= 18)) {
                    IToast.show(ForgetPwdActivity.this,"密码长度为6-18位");
                    return;
                }
                if (!(passWord.length() >= 6 && passWord.length() <= 18)||!ValidateUtil.isNumberOrLetter(passWord)) {
                    IToast.show(ForgetPwdActivity.this,"请输入正确的密码格式");
                    return;
                }
                if (!confirmPass.equals(passWord)) {
                    IToast.show(ForgetPwdActivity.this,"两次密码不对应");
                    return;
                }
                Log.i("ceshi",ValidateUtil.isMobile(mobile)+"---------"+ValidateUtil.isEmail(mobile));
                if(ValidateUtil.isMobile(mobile)){
                    //手机号找回密码的方法
                    getPassWord(mobile,code, passWord,confirmPass,true);
                }else if(ValidateUtil.isEmail(mobile)){
                    //邮箱找回密码的方法
                    getPassWord(mobile,code, passWord,confirmPass,false);
                }
                break;
        }
    }


    /**
     * @author bin 修改人: 时间:2015-12-4 上午11:58:37 方法说明:找回密码的方法
     */
    private void getPassWord(final String mobile, String code, final String userPassword,String confirmPass,boolean isMobile) {

        Map<String,String> map = new HashMap<>();
        if(isMobile){
            map.put("retrieveType", "mobile");
        }else{
            map.put("retrieveType", "email");
        }
        map.put("mobileOrEmail", mobile);
        map.put("code", code);
        map.put("newPwd", userPassword);
        map.put("confirmPwd", confirmPass);
        Log.i("ceshi",Address.GET_PASSWORD+"?"+map+"---------------修改密码");
        OkHttpUtils.post().url(Address.GET_PASSWORD).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            DemoApplication.getInstance().getActivityStack().finishActivity(LoginActivity.getInstence());
                            Intent intent = new Intent();
                            intent.setClass(ForgetPwdActivity.this, LoginActivity.class);
                            intent.putExtra("userName", mobile);
                            intent.putExtra("passWord", userPassword);
                            startActivity(intent);
                            finish();
                        } else {
                            IToast.show(ForgetPwdActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 方法说明:获取邮箱的验证码
     */
    private void getEmailsCode(String mobile) {
        Map<String,String> map = new HashMap<>();
        map.put("sendType", "retrieve");
        map.put("email", mobile);
        showLoading(ForgetPwdActivity.this);
        ILog.i(Address.GET_EMAIL_CODE+"?"+map+"----------验证码");
        OkHttpUtils.post().url(Address.GET_EMAIL_CODE).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            IToast.show(ForgetPwdActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            IToast.show(ForgetPwdActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 方法说明:获取sgin值的方法
     */
    private void getSginData(final String mobile) {
        Map<String,String> map = new HashMap<>();
        map.put("mobileType", "Android");
        map.put("mobile", mobile);
        showLoading(ForgetPwdActivity.this);
        OkHttpUtils.post().url(Address.GET_SGIN).params(map).build().execute(new PublicStringEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicStringEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            String sgin = response.getEntity();
                            // 联网获取验证码的方法
                            getVerificationCode(mobile,sgin);
                        }else{
                            IToast.show(ForgetPwdActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 方法说明:获取验证码的接口
     */
    private void getVerificationCode(String mobile,String sgin) {
       Map<String,String> map = new HashMap<>();
        map.put("sendType", "retrieve");
        map.put("mobile", mobile);
        map.put("mobileType", "Android");
        map.put("sgin", sgin);
        showLoading(ForgetPwdActivity.this);

        OkHttpUtils.post().url(Address.GET_PHONE_CODE).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            IToast.show(ForgetPwdActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            IToast.show(ForgetPwdActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }


    /**
     * 方法说明:开启倒计时的线程
     */
    private void startTheard() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(
                    long millisUntilFinished) {
                getObtainCode.setText("重新获取"+ millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                getObtainCode.setText("获取验证码");
                isCountdown = false;
            }
        }.start();
    }

}
