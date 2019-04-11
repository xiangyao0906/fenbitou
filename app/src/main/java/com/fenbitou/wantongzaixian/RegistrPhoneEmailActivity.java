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
import com.fenbitou.entity.PublicStringEntity;
import com.fenbitou.entity.PublicStringEntityCallback;
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

import static com.fenbitou.wantongzaixian.R.id.check_box;
import static com.fenbitou.wantongzaixian.R.id.confirm_passWord_edit;
import static com.fenbitou.wantongzaixian.R.id.errorMessage_layout;
import static com.fenbitou.wantongzaixian.R.id.error_message;
import static com.fenbitou.wantongzaixian.R.id.passWord_edit;
import static com.fenbitou.wantongzaixian.R.id.title_text;

/**
 * Created by bishuang on 2017/7/14.
 * 手机邮箱注册的类
 */

public class RegistrPhoneEmailActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(title_text)
    TextView titleText;
    @BindView(R.id.goto_login)
    LinearLayout gotoLogin;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.moblieCode_edit)
    EditText moblieCodeEdit;
    @BindView(R.id.get_obtain_phonecode)
    TextView getObtainPhonecode;
    @BindView(R.id.moblieCodeLayout)
    LinearLayout moblieCodeLayout;
    @BindView(R.id.phone_code_line)
    View phoneCodeLine;
    @BindView(R.id.email_edit)
    EditText emailEdit;
    @BindView(R.id.emailCode_edit)
    EditText emailCodeEdit;
    @BindView(R.id.get_obtain_emailcode)
    TextView getObtainEmailcode;
    @BindView(R.id.emailCodeLayout)
    LinearLayout emailCodeLayout;
    @BindView(R.id.email_code_line)
    View emailCodeLine;
    @BindView(passWord_edit)
    EditText passWordEdit;
    @BindView(confirm_passWord_edit)
    EditText confirmPassWordEdit;
    @BindView(error_message)
    TextView errorMessage;
    @BindView(errorMessage_layout)
    LinearLayout errorMessageLayout;
    @BindView(check_box)
    CheckBox checkBox;
    @BindView(R.id.agreementText)
    TextView agreementText;
    @BindView(R.id.registerText)
    TextView registerText;
    private boolean isCountdown,isMobileCode,isBinDing,isEmailCode; // 是否是发送验证码,手机验证码是否开放,是否是从绑定页面跳过来的,邮箱的验证码是否开放
    private String cusName,appId,appType,photo;  //第三方的名称,id,类型,头像
    private String typeCode; //验证码类型
    private PhoneUtils phoneUtils; // 手机的工具类

    @Override
    protected int initContentView() {
        getIntentMessage();
        return R.layout.act_registr_phone_email;
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
    protected void initComponent() {
        phoneUtils = new PhoneUtils(RegistrPhoneEmailActivity.this);
        titleText.setText(getResources().getString(R.string.register)); // 设置标题
    }

    @Override
    protected void initData() {
        //获取邮箱或手机号接受验证码开关的接口
        getCodeSwitchData();

    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.goto_login, R.id.agreementText, R.id.registerText,R.id.get_obtain_phonecode})
    public void onViewClicked(View view) {
        String mobile = phoneEdit.getText().toString(); //获取手机号
        String email = emailEdit.getText().toString(); //获取邮箱
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.goto_login: //去登陆
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.get_obtain_phonecode:  //获取验证码
                if (!isCountdown) {
                    if (TextUtils.isEmpty(mobile)) {
                        IToast.show(RegistrPhoneEmailActivity.this,"请输入手机号");
                        return;
                    }
                    if (!ValidateUtil.isMobile(mobile)) {
                        IToast.show(RegistrPhoneEmailActivity.this,"请输入正确的手机号");
                        return;
                    }
                    typeCode = "phone"; //验证码类型
                    //手机注册,获取sgin值
                    getSginData(mobile);
                }
                break;
            case R.id.get_obtain_emailcode:
                if (!isCountdown) {
                    if (TextUtils.isEmpty(email)) {
                        IToast.show(RegistrPhoneEmailActivity.this,"请输入邮箱");
                        return;
                    }
                    if (!ValidateUtil.isEmail(email)) {
                        IToast.show(RegistrPhoneEmailActivity.this,"请输入正确的邮箱");
                        return;
                    }
                    typeCode = "email"; //验证码类型
                    //邮箱获取验证码
                    getEmailsCode(email);
                }
                break;
            case R.id.agreementText:
                openActivity(AgreementActivity.class);
                break;
            case R.id.registerText:
                errorMessageLayout.setVisibility(View.GONE);
                errorMessage.setText("");
                String codePhone = moblieCodeEdit.getText().toString();
                String codeEmail = emailCodeEdit.getText().toString();
                String passWord = passWordEdit.getText().toString();
                String confirm_passWord = confirmPassWordEdit.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入邮箱");
                    return;
                }
                if(isMobileCode){  //如果验证码开放的话——手机
                    if (TextUtils.isEmpty(codePhone)) {
                        errorMessageLayout.setVisibility(View.VISIBLE);
                        errorMessage.setText("请输入验证码");
                        return;
                    }
                }
                if(isEmailCode){  //如果验证码开放的话——邮箱
                    if (TextUtils.isEmpty(codeEmail)) {
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
                if (!ValidateUtil.isMobile(mobile)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入正确的手机号");
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
                if(isBinDing){  //在绑定页面跳过来的-------------------
                    //绑定第三方注册的方法
                    getRegisterBinding(mobile, codePhone, passWord, confirm_passWord);
                }else{
                    //联网注册的方法
                    if(isMobileCode && isEmailCode){ //手机验证码为ON 邮箱验证码为ON
                        getRegisterNN(mobile, email, passWord, confirm_passWord, codePhone, codeEmail);
                    }else if (!isMobileCode && !isEmailCode) {//手机验证码为OFF 邮箱验证码为OFF
                        getRegisterFF(mobile, email, passWord, confirm_passWord);
                    }
                }
                break;
        }
    }

    /**
     * 方法说明:注册的方法_手机验证码为OFF 邮箱验证码为OFF
     */
    private void getRegisterFF(final String mobile,String email,final String userPassword,String confirmPwd){
        Map<String,String> map = new HashMap<>();
        map.put("regType", "mobile_email");
        map.put("mobile", mobile);
        map.put("email", email);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        showLoading(RegistrPhoneEmailActivity.this);
        ILog.i(Address.REGISTER+"?"+map+"------------------注册NONONON");
        OkHttpUtils.post().url(Address.REGISTER).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        cancelLoading();
                        if(response.isSuccess()){
                            Intent intent = new Intent();
                            intent.setClass(RegistrPhoneEmailActivity.this,LoginActivity.class);
                            intent.putExtra("userName", mobile);
                            intent.putExtra("passWord", userPassword);
                            startActivity(intent);
                            RegistrPhoneEmailActivity.this.finish();
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }


    /**
     * 方法说明:注册的方法_手机验证码为ON 邮箱验证码为ON
     */
    private void getRegisterNN(final String mobile,String email,final String userPassword,String confirmPwd,String codePhone,String codeEmail){
        Map<String,String> map = new HashMap<>();
        map.put("regType", "mobile_email");
        map.put("mobile", mobile);
        map.put("email", email);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        map.put("mobileCheckCode", codePhone); //手机验证码
        map.put("emailCheckCode", codeEmail); //邮箱验证码
        showLoading(RegistrPhoneEmailActivity.this);
        ILog.i(Address.REGISTER+"?"+map+"------------------注册NONONON");
        OkHttpUtils.post().url(Address.REGISTER).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        cancelLoading();
                        if(response.isSuccess()){
                            Intent intent = new Intent();
                            intent.setClass(RegistrPhoneEmailActivity.this,LoginActivity.class);
                            intent.putExtra("userName", mobile);
                            intent.putExtra("passWord", userPassword);
                            startActivity(intent);
                            RegistrPhoneEmailActivity.this.finish();
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this, message);
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
    private void getRegisterBinding(final String mobile,String code,final String userPassword,String confirmPwd) {

        Map<String,String> map = new HashMap<>();
        map.put("regType", "mobile");
        map.put("mobile", mobile);
        map.put("checkCode", code);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        map.put("cusName", cusName);
        map.put("appId", appId);
        map.put("appType", appType);
        map.put("photo", photo);
        showLoading(RegistrPhoneEmailActivity.this);
        OkHttpUtils.post().url(Address.REGISTERBINDING).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            cancelLoading();
                            IToast.show(RegistrPhoneEmailActivity.this,message);
                            //绑定成功的方法(绑定成功就相当于登录)
                            LoginScuessMethod(response);
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this,message);
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
     * 方法说明:获取验证码的接口
     */
    private void getEmailsCode(String email) {
        showLoading(RegistrPhoneEmailActivity.this);
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
                            cancelLoading();
                            IToast.show(RegistrPhoneEmailActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this,message);
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
        Map<String, String> map = new HashMap<>();
        map.put("mobileType", "Android");
        map.put("mobile", mobile);
        showLoading(RegistrPhoneEmailActivity.this);
        OkHttpUtils.post().params(map).url(Address.GET_SGIN).build().execute(new PublicStringEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicStringEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            cancelLoading();
                            String sgin = response.getEntity();
                            // 联网获取验证码的方法
                            getVerificationCode(mobile,sgin);
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this,message);
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
        showLoading(RegistrPhoneEmailActivity.this);
        Map<String,String> map = new HashMap<>();
        map.put("sendType", "register");
        map.put("mobile", mobile);
        map.put("mobileType", "Android");
        map.put("sgin", sgin);
        OkHttpUtils.post().params(map).url(Address.GET_PHONE_CODE).build().execute(new PublicEntityCallback() {
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

                            IToast.show(RegistrPhoneEmailActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * @author bin
     * 修改人:
     * 时间:2016-1-12 下午4:14:39
     * 方法说明:开启倒计时的线程
     */
    private void startTheard() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(
                    long millisUntilFinished) {
                if (typeCode.equals("phone")) {
                    getObtainPhonecode.setText("重新获取" + millisUntilFinished/ 1000 + "秒");
                }else {
                    getObtainEmailcode.setText("重新获取"+ millisUntilFinished/ 1000 + "秒");
                }
            }

            @Override
            public void onFinish() {
                if (typeCode.equals("phone")) {
                    getObtainPhonecode.setText("获取验证码");
                }else{
                    getObtainEmailcode.setText("获取验证码");
                }
                isCountdown = false;
            }
        }.start();
    }


    /**
     * 方法说明:获取邮箱或手机号接受验证码开关的接口
     */
    private void getCodeSwitchData() {
        showLoading(RegistrPhoneEmailActivity.this);
        OkHttpUtils.get().url(Address.GETCODESWITCH).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            cancelLoading();
                            EntityPublic entityPublic = response.getEntity();
                            String mobileCode = entityPublic.getVerifyRegMobileCode();
                            if("ON".equals(mobileCode)){
                                isMobileCode = true;
                                moblieCodeLayout.setVisibility(View.VISIBLE);
                                phoneCodeLine.setVisibility(View.VISIBLE);
                            }else{
                                //验证码没开放
                                isMobileCode = false;
                                moblieCodeLayout.setVisibility(View.GONE);
                                phoneCodeLine.setVisibility(View.GONE);
                            }
                            String emailCode = entityPublic.getVerifyRegEmailCode();
                            if ("ON".equals(emailCode)) { //判断邮箱验证码布局是否显示
                                isEmailCode = true;
                                emailCodeLayout.setVisibility(View.VISIBLE);
                                emailCodeLine.setVisibility(View.VISIBLE);
                            }else{
                                //验证码没开放
                                isEmailCode = false;
                                emailCodeLayout.setVisibility(View.GONE);
                                emailCodeLine.setVisibility(View.GONE);
                            }
                        }else{
                            IToast.show(RegistrPhoneEmailActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
