package com.fenbitou.wantongzaixian;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.PhoneUtils;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.email_line;
import static com.fenbitou.wantongzaixian.R.id.title_text;

/**
 * Created by bishuang on 2017/7/14.
 * 邮箱注册的类
 */

public class RegistrEmailActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(title_text)
    TextView titleText;
    @BindView(R.id.goto_login)
    LinearLayout gotoLogin;
    @BindView(R.id.userName_edit)
    EditText userNameEdit;
    @BindView(R.id.email_edit)
    EditText emailEdit;
    @BindView(R.id.get_obtain_code)
    TextView getObtainCode;
    @BindView(R.id.moblieCodeLayout)
    LinearLayout moblieCodeLayout;
    @BindView(email_line)
    View emailLine;
    @BindView(R.id.passWord_edit)
    EditText passWordEdit;
    @BindView(R.id.confirm_passWord_edit)
    EditText confirmPassWordEdit;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.errorMessage_layout)
    LinearLayout errorMessageLayout;
    @BindView(R.id.check_box)
    CheckBox checkBox;
    @BindView(R.id.agreementText)
    TextView agreementText;
    @BindView(R.id.registerText)
    TextView registerText;
    private String cusName, appId, appType, photo;  //第三方的名称,id,类型,头像
    private boolean isBinDing,isEmailCode,isCountdown; //是否是从绑定页面跳过来的 邮箱验证码是否开放 是否是发送验证码
    private PhoneUtils phoneUtils; // 手机的工具类

    @Override
    protected int initContentView() {
        getIntentMessage();
        return R.layout.act_registr_email;
    }

    @Override
    protected void initComponent() {
        phoneUtils = new PhoneUtils(RegistrEmailActivity.this);
        titleText.setText(getResources().getString(R.string.register)); // 设置标题
    }

    /**
     * 方法说明:获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        isBinDing = intent.getBooleanExtra("isBinDing", false);
        cusName = intent.getStringExtra("cusName");
        appId = intent.getStringExtra("appId");
        appType = intent.getStringExtra("appType");
        photo = intent.getStringExtra("photo");
    }

    @Override
    protected void initData() {
        getCodeSwitchData();
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout,R.id.goto_login, R.id.get_obtain_code, R.id.registerText,R.id.agreementText})
    public void onViewClicked(View view) {
        String email = userNameEdit.getText().toString();
        switch (view.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.goto_login: //去登陆
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.get_obtain_code: //验证码
                if (!isCountdown) {
                if (TextUtils.isEmpty(email)) {
                    IToast.show(RegistrEmailActivity.this,"请输入邮箱");
                    return;
                }
                if (!ValidateUtil.isEmail(email)) {
                    IToast.show(RegistrEmailActivity.this,"请输入正确的邮箱");
                    return;
                }
                    getVerificationCode(email);
            }
                break;
            case R.id.registerText:
                errorMessageLayout.setVisibility(View.GONE);
                errorMessage.setText("");
                String code = emailEdit.getText().toString();
                String passWord = passWordEdit.getText().toString();
                String confirm_passWord = confirmPassWordEdit.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入邮箱");
                    return;
                }
                if(isEmailCode){  //如果验证码开放的话
                    if (TextUtils.isEmpty(code)) {
                        errorMessageLayout.setVisibility(View.VISIBLE);
                        errorMessage.setText("请输入验证码");
                        return;
                    }
                }
                if (TextUtils.isEmpty(passWord)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(confirm_passWord)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请确认密码");
                    return;
                }
                if (!ValidateUtil.isEmail(email)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入正确的邮箱");
                    return;
                }
                if (!(passWord.length() >= 6 && passWord.length() <= 18)||!ValidateUtil.isNumberOrLetter(passWord)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("输入密码格式不正确");
                    return;
                }
                if (!confirm_passWord.equals(passWord)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("两次密码不对应");
                    return;
                }
                boolean checked = checkBox.isChecked();
                if(!checked){
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请阅读并遵守协议方可注册");
                    return;
                }
                if(isBinDing){  //在绑定页面跳过来的
                    //绑定第三方注册的方法
                    getRegisterBinding(email, code, passWord, confirm_passWord);
                }else{
                    //联网注册的方法
                    getRegisterNF(email,code,passWord,confirm_passWord);
                }
                break;
            case R.id.agreementText: // 协议
                openActivity(AgreementActivity.class);
                break;
        }
    }

    /**
     * 方法说明:注册的方法_邮箱验证码为ON 邮箱验证码为OF
     */
    private void getRegisterNF(final String email,String code,final String userPassword,String confirmPwd){
        Map<String,String> map = new HashMap<>();
        map.put("regType", "email");
        map.put("email", email);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        map.put("emailCheckCode", code); //邮箱验证码
        showLoading(RegistrEmailActivity.this);
        ILog.i(Address.REGISTER+map+"------------邮箱");
        OkHttpUtils.post().params(map).url(Address.REGISTER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            Intent intent = new Intent();
                            intent.setClass(RegistrEmailActivity.this,LoginActivity.class);
                            intent.putExtra("userName", email);
                            intent.putExtra("passWord", userPassword);
                            startActivity(intent);
                            RegistrEmailActivity.this.finish();
                        }else{
                            IToast.show(RegistrEmailActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * 方法说明:绑定第三方注册的方法
     */
    private void getRegisterBinding(final String email,String code,final String userPassword,String confirmPwd) {

        Map<String,String> map = new HashMap<>();
        map.put("regType", "email");
        map.put("email", email);
        map.put("checkCode", code);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        map.put("cusName", cusName);
        map.put("appId", appId);
        map.put("appType", appType);
        map.put("photo", photo);
        showLoading(RegistrEmailActivity.this);
        OkHttpUtils.post().params(map).url(Address.REGISTERBINDING).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            IToast.show(RegistrEmailActivity.this,message);
                            //绑定成功的方法(绑定成功就相当于登录)
                            LoginScuessMethod(response);
                        }else{
                            IToast.show(RegistrEmailActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 方法说明:登陆成功的方法
     */
    private void LoginScuessMethod(PublicEntity publicEntity) {
        int userId = publicEntity.getEntity().getUser().getId();
        //判断用户是否存在其他地方登录标记
        String memTime = publicEntity.getEntity().getMemTime();
        String lastLoginTime = publicEntity.getEntity().getLastLoginTime();
        // 添加登陆记录的方法
        addLoginRecord(userId);
        SharedPreferencesUtils.setParam(this,"userId",userId);
        SharedPreferencesUtils.setParam(this,"memTime",memTime);
        SharedPreferencesUtils.setParam(this,"lastLoginTime",lastLoginTime);

        finish();
    }

    /**
     * 添加登陆记录的方法
     */
    private void addLoginRecord(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("websiteLogin.ip", phoneUtils.GetHostIp());
        map.put("websiteLogin.brand", phoneUtils.getPhoneBrand());
        map.put("websiteLogin.modelNumber", phoneUtils.getPhoneModel());
        map.put("websiteLogin.size", phoneUtils.getPhoneSize());
        map.put("websiteLogin.userId", userId+"");
        map.put("websiteLogin.type", "android");
        OkHttpUtils.post().params(map).url(Address.LOGIN).build().execute(
                new PublicEntityCallback(){

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {

                    }
                });
    }

    /**
     * 方法说明:获取邮箱或手机号接受验证码开关的接口
     */
    private void getCodeSwitchData() {
        showLoading(RegistrEmailActivity.this);
        OkHttpUtils.get().url(Address.GETCODESWITCH).build().execute(
                new PublicEntityCallback() {
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
                                    EntityPublic entityPublic = response.getEntity();
                                    String emailCode = entityPublic.getVerifyRegEmailCode();
                                    if ("ON".equals(emailCode)) {
                                        isEmailCode = true;
                                        moblieCodeLayout.setVisibility(View.VISIBLE);
                                        emailLine.setVisibility(View.VISIBLE);
                                    } else {
                                        //验证码没开放
                                        isEmailCode = false;
                                        moblieCodeLayout.setVisibility(View.GONE);
                                        emailLine.setVisibility(View.GONE);
                                    }
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
    private void getVerificationCode(String email) {
        showLoading(RegistrEmailActivity.this);
        Map<String,String> map = new HashMap<>();
        map.put("sendType", "register");
        map.put("email", email);
        OkHttpUtils.post().params(map).url(Address.GET_EMAIL_CODE).build().execute(new PublicEntityCallback() {
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
                            IToast.show(RegistrEmailActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            IToast.show(RegistrEmailActivity.this,message);
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
            public void onTick(long millisUntilFinished) {
                getObtainCode.setText("重新获取" + millisUntilFinished/ 1000 + "秒");
            }

            @Override
            public void onFinish() {
                getObtainCode.setText("获取验证码");
                isCountdown = false;
            }
        }.start();
    }
}
