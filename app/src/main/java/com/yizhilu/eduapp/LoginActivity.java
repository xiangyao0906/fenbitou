package com.yizhilu.eduapp;

import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.FinalUtils;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.PhoneUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

import static com.yizhilu.eduapp.R.id.errorMessage_layout;
import static com.yizhilu.eduapp.R.id.error_message;
import static com.yizhilu.eduapp.R.id.goto_register;
import static com.yizhilu.eduapp.R.id.passWord_edit;
import static com.yizhilu.eduapp.R.id.userName_edit;

/**
 * Created by bishuang on 2017/7/14.
 * 登陆的类
 */

public class LoginActivity extends BaseActivity implements PlatformActionListener {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.email_text)
    TextView emailText;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(goto_register)
    LinearLayout gotoRegister;
    @BindView(userName_edit)
    EditText userNameEdit;
    @BindView(R.id.userName_line)
    View userNameLine;
    @BindView(passWord_edit)
    EditText passWordEdit;
    @BindView(R.id.passWord_line)
    View passWordLine;
    @BindView(error_message)
    TextView errorMessage;
    @BindView(errorMessage_layout)
    LinearLayout errorMessageLayout;
    @BindView(R.id.forget_pass)
    TextView forgetPass;
    @BindView(R.id.loginText)
    TextView loginText;
    @BindView(R.id.QQImage)
    ImageView QQImage;
    @BindView(R.id.sinaImage)
    ImageView sinaImage;
    @BindView(R.id.weixinImage)
    ImageView weixinImage;
    private String type; //在网校点考试传过来的
    private String userName, passWord; // 用户名,密码(在注册界面传过来的)
    private boolean isSingle; // 是否是单点登录退出后跳到这个页面的
    private PhoneUtils phoneUtils; // 手机的工具类
    public static LoginActivity loginActivity;
    private String thridType = ""; // 第三方登录的类型

    @Override
    protected int initContentView() {
        getIntentMessage();
        return R.layout.act_login;
    }

    /**
     * @author bin 修改人: 时间:2015-10-20 上午10:40:30 方法说明:获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        type = intent.getStringExtra(FinalUtils.TYPE_STRING);
        userName = intent.getStringExtra("userName");
        passWord = intent.getStringExtra("passWord");
        isSingle = intent.getBooleanExtra("isSingle", false);
    }

    public static LoginActivity getInstence() {
        if (loginActivity == null) {
            loginActivity = new LoginActivity();
        }
        return loginActivity;
    }

    @Override
    protected void initComponent() {

        phoneUtils = new PhoneUtils(LoginActivity.this);
        if (!TextUtils.isEmpty(userName )) {
            userNameEdit.setText(userName);
        }
        if (!TextUtils.isEmpty(passWord)) {
            passWordEdit.setText(passWord);
        }
    }

    @Override
    protected void initData() {
        loginActivity = this;
        titleText.setText(getResources().getString(R.string.login));
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.goto_register, R.id.forget_pass, R.id.loginText, R.id.QQImage, R.id.sinaImage, R.id.weixinImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: //返回
//                if (isSingle) {
//                    DemoApplication.getInstance().getActivityStack().finishAllActivity();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                } else {
                this.finish();

//                }
                break;
            case goto_register: //去注册
                getRegistType();
                break;
            case R.id.forget_pass: // 忘记密码
                openActivity(ForgetPwdActivity.class);
                break;
            case R.id.loginText: //登陆
                errorMessageLayout.setVisibility(View.GONE);
                errorMessage.setText("");
                String userName = userNameEdit.getText().toString(); //用户手机号
                String passWord = passWordEdit.getText().toString(); //用户密码
                if (TextUtils.isEmpty(userName)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入密码");
                    return;
                }
                if (!ValidateUtil.isEmail(userName)
                        && !ValidateUtil.isMobile(userName)) {
                    errorMessageLayout.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入正确的用户名");
                    return;
                }

                // 联网登录的方法
                getLogin(userName, passWord);
                break;
            case R.id.QQImage:
                IToast.show(LoginActivity.this, "QQ登陆");
                if (ShareSDK.getPlatform(QQ.NAME).isClientValid()) {
                    thridType = "QQ";
                    Platform QQfd = ShareSDK.getPlatform(QQ.NAME);
                    QQfd.setPlatformActionListener(this);
                    QQfd.authorize();
                } else {
                    IToast.show(LoginActivity.this, "请安装QQ客户端");
                }
                break;
            case R.id.sinaImage:

                if (ShareSDK.getPlatform(SinaWeibo.NAME).isClientValid()) {
                    thridType = "SINA";
                    Platform Sinafd = ShareSDK.getPlatform(SinaWeibo.NAME);
                    Sinafd.setPlatformActionListener(this);
                    Sinafd.authorize();
                } else {
                    IToast.show(LoginActivity.this, "请安装新浪微博客户端");

                }
                break;
            case R.id.weixinImage:
                if (ShareSDK.getPlatform(Wechat.NAME).isClientValid()) {
                    thridType = "WEIXIN";
                    Platform Wechatfd = ShareSDK.getPlatform(Wechat.NAME);
                    Wechatfd.setPlatformActionListener(this);
                    Wechatfd.authorize();
                } else {

                    IToast.show(LoginActivity.this, "请安装微信客户端");
                }
                break;
        }
    }



    //  登陆的方法
    public void getLogin(String account, String userPassword) {
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("userPassword", userPassword);
        ILog.i(Address.LOGIN + "?" + map + "..........登陆");
        showLoading(LoginActivity.this);
        OkHttpUtils.get().params(map).url(Address.LOGIN).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();
                        if (!TextUtils.isEmpty(response.toString())) {
                            try {
                                String message = response.getMessage();
                                if (response.isSuccess()) {
                                    //登陆成功的方法
                                    LoginScuessMethod(response);
                                } else {
                                    IToast.show(LoginActivity.this, message);
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
        boolean isSale = publicEntity.getEntity().isSaleUser();
        // 添加登陆记录的方法
        addLoginRecord(userId);


        SharedPreferencesUtils.setParam(this, "userId", userId);
        SharedPreferencesUtils.setParam(this, "memTime", memTime);
        SharedPreferencesUtils.setParam(this, "lastLoginTime", lastLoginTime==null?0:lastLoginTime);
        SharedPreferencesUtils.setParam(this, "isSaler", isSale);

        finish();
    }


    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 添加登陆记录的方法
     */
    private void addLoginRecord(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("websiteLogin.ip", phoneUtils.getIPAddress(this));
        map.put("websiteLogin.brand", phoneUtils.getPhoneBrand());
        map.put("websiteLogin.modelNumber", phoneUtils.getPhoneModel());
        map.put("websiteLogin.size", phoneUtils.getPhoneSize());
        map.put("websiteLogin.userId", userId + "");
        map.put("websiteLogin.type", "android");
        OkHttpUtils.get().params(map).url(Address.ADD_LOGIN_RECORD).build().execute(
                new PublicEntityCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {

                    }
                });
    }

    /**
     * 获取注册类型的接口
     */
    private void getRegistType() {
        showLoading(LoginActivity.this);
        OkHttpUtils.get().url(Address.REGIST_TYPE).build().execute(
                new PublicEntityCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();
                        if (!TextUtils.isEmpty(response.toString())) {
                            try {
                                if (response.isSuccess()) {
                                    String keyType = response.getEntity().getKeyType();
                                    if ("mobile".equals(keyType)) {
                                        openActivity(RegistrActivity.class);
                                        LoginActivity.this.finish();
                                    } else if ("mobileAndEmail".equals(keyType)) {
                                        openActivity(RegistrPhoneEmailActivity.class);
                                        LoginActivity.this.finish();
                                    } else {
                                        openActivity(RegistrEmailActivity.class);
                                        LoginActivity.this.finish();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                });

    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        final String userName = platform.getDb().getUserName();// 获取用户名字
        final String userIcon = platform.getDb().getUserIcon(); // 获取用户头像
        final String userId = platform.getDb().getUserId();
        final String userGender = platform.getDb().getUserGender();
        final String unionid = platform.getDb().get("unionid");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (thridType.equals("WEIXIN")) {
                    loginByThird(0, thridType, unionid, userName);
                } else {
                    loginByThird(0, thridType, userId, userName);
                }
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ILog.i("bai");
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }


    /**
     * @return 返回类型
     * @throws
     * @Title:
     * @Description: ${todo}(这里用一句话描述这个方法的作用)
     */
    private void loginByThird(int userId, String appType, String appId, String cusName) {
        OkHttpUtils.get().url(Address.LOGINBYTHIRD)
                .addParams("userId", String.valueOf(userId)).addParams("appType", appType)
                .addParams("appId", appId)
                .addParams("cusName", cusName)
                .build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    LoginScuessMethod(response);
                } else {
                    IToast.show(LoginActivity.this, response.getMessage());
                }
            }
        });
    }

}
