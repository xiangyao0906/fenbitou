package com.fenbitou.wantongzaixian;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.EntityPublicCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

/**
 * 解除绑定第三方
 */
public class RemoveBindingActivity extends BaseActivity implements PlatformActionListener {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.QQImage)
    ImageView QQImage;
    @BindView(R.id.QQText)
    TextView QQText;
    @BindView(R.id.QQBinding)
    TextView QQBinding;
    @BindView(R.id.QQBinding_linear)
    LinearLayout QQBindingLinear;
    @BindView(R.id.sinaImage)
    ImageView sinaImage;
    @BindView(R.id.sinaText)
    TextView sinaText;
    @BindView(R.id.sinaBinding)
    TextView sinaBinding;
    @BindView(R.id.sinaBinding_linear)
    LinearLayout sinaBindingLinear;
    @BindView(R.id.weixinImage)
    ImageView weixinImage;
    @BindView(R.id.weixinText)
    TextView weixinText;
    @BindView(R.id.weixinBinding)
    TextView weixinBinding;
    @BindView(R.id.weixinText_linear)
    LinearLayout weixinTextLinear;
    @BindView(R.id.qq_relat)
    RelativeLayout qqRelat;
    @BindView(R.id.sina_relat)
    RelativeLayout sinaRelat;
    @BindView(R.id.weixin_relat)
    RelativeLayout weixinRelat;
    private int userId;
    private EntityPublic entity;
    private Dialog dialog;
    private String thridType;
    private String binding;
    private Platform platform;

    @Override
    protected int initContentView() {
        return R.layout.activity_remove_binding;
    }

    @Override
    protected void initComponent() {
        titleText.setText("第三方账户绑定");
    }

    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(RemoveBindingActivity.this, "userId", -1);
        searchBingding(userId);
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;

        }
    }


    public void searchBingding(int userId) {
        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).url(Address.QUERYUSERBUNDLING).build().execute(new EntityPublicCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(EntityPublic response, int id) {
                if (response.isSuccess()) {
                    List<EntityPublic> entityList = response.getEntity();
                    if (!entityList.isEmpty()) {
                        setThridMessage();
                        for (int i = 0; i < entityList.size(); i++) {
                            entity = entityList.get(i);
                            String profiletype = entity.getProfiletype();
                            if ("QQ".equals(profiletype)) {
                                QQText.setText(entity.getName());
                                QQImage.setBackgroundResource(R.drawable.qqimageyes);
                                QQBinding.setText(R.string.relieve_binding);
                                QQBinding.setTextColor(getResources().getColor(R.color.color_80));
                            } else if ("WEIXIN".equals(profiletype)) {
                                weixinText.setText(entity.getName());
                                weixinImage.setBackgroundResource(R.drawable.weixinimageyes);
                                weixinBinding.setText(R.string.relieve_binding);
                                weixinBinding.setTextColor(getResources().getColor(R.color.color_80));
                            } else if ("SINA".equals(profiletype)) {
                                sinaText.setText(entity.getName());
                                sinaImage.setBackgroundResource(R.drawable.sinaimageyes);
                                sinaBinding.setText(R.string.relieve_binding);
                                sinaBinding.setTextColor(getResources().getColor(R.color.color_80));
                            }
                        }
                    } else {
                        //设置第三方信息
                        setThridMessage();
                    }
                }
            }
        });
    }

    /**
     * @return 返回类型
     * @throws
     * @Title:
     * @Description: ${todo}(这里用一句话描述这个方法的作用)
     */
    public void setThridMessage() {
        QQText.setText("");
        QQBinding.setText(R.string.immediately_binding);
        QQBinding.setTextColor(getResources().getColor(R.color.color_83));
        QQImage.setBackgroundResource(R.drawable.qqimagenot);
        weixinText.setText("");
        weixinBinding.setText(R.string.immediately_binding);
        weixinBinding.setTextColor(getResources().getColor(R.color.color_83));
        weixinImage.setBackgroundResource(R.drawable.weixinimagenot);
        sinaText.setText("");
        sinaBinding.setText(R.string.immediately_binding);
        sinaBinding.setTextColor(getResources().getColor(R.color.color_83));
        sinaImage.setBackgroundResource(R.drawable.sinaimagenot);
    }


    /**
     * @return 返回类型
     * @throws
     * @Title:
     * @Description: ${todo}(这里用一句话描述这个方法的作用)
     */
    private void unBundling(final int userId, String appType) {


        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).addParams("appType", appType).url(Address.UNBINDINGAPP).build().execute(new EntityPublicCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(EntityPublic response, int id) {
                if (response.isSuccess()) {
                    IToast.show(RemoveBindingActivity.this, response.getMessage());
                    searchBingding(userId);
                } else {
                    IToast.show(RemoveBindingActivity.this, response.getMessage());
                }
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.qq_relat, R.id.sina_relat, R.id.weixin_relat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qq_relat:
                thridType = "QQ";
                String bind = QQBinding.getText().toString();
                if (bind.equals(getResources().getString(R.string.relieve_binding))) {
                    //去解除绑定
                    unBingdingDialog();
                } else {
                    if (ShareSDK.getPlatform(QQ.NAME).isClientValid()) {
                        platform = ShareSDK.getPlatform(QQ.NAME);
                        platform.setPlatformActionListener(this);
                        platform.authorize();
                    } else {
                        IToast.show(RemoveBindingActivity.this, "请安装QQ客户端");
                    }
                }
                break;
            case R.id.sina_relat:
                break;
            case R.id.weixin_relat:
                thridType = "WEIXIN";
                binding = weixinBinding.getText().toString();
                if (binding.equals(getResources().getString(R.string.relieve_binding))) {
                    //去解除绑定
                    unBingdingDialog();
                } else {
                    //没有绑定,去绑定
                    if (ShareSDK.getPlatform(Wechat.NAME).isClientValid()) {
                        platform = ShareSDK.getPlatform(Wechat.NAME);
                        if (platform.isAuthValid()) {
                            platform.removeAccount(true);
                        }
                        platform.setPlatformActionListener(this);
                        platform.showUser(null);
                    } else {
                        IToast.show(RemoveBindingActivity.this, "请安装微信客户端");
                    }
                }
                break;
        }
    }

    public void unBingdingDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_show, null);
        WindowManager manager = (WindowManager) getSystemService(
                Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        TextView titles = (TextView) view.findViewById(R.id.texttitles);
        if ("QQ".equals(thridType)) {
            titles.setText("解除绑定后将不在使用QQ登录\n宏脉软件是否继续？");
        } else if ("WEIXIN".equals(thridType)) {
            titles.setText("解除绑定后将不在使用微信登录\n宏脉软件是否继续？");
        } else if ("SINA".equals(thridType)) {
            titles.setText("解除绑定后将不在使用新浪微博登录宏脉校软件是否继续？");
        }
        TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
        btnsure.setText("是");

        LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
        linbtnsure.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("static-access")
            @Override
            public void onClick(View v) {
                //新的解绑规则
                unBundling(userId, thridType);
                dialog.dismiss();
            }
        });
        TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
        btncancle.setText("否");
        LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
        linbtncancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        // 获取资料
        final String userName = platform.getDb().getUserName();// 获取用户名字
        final String userIcon = platform.getDb().getUserIcon(); // 获取用户头像
        final String appId = platform.getDb().getUserId();
        String userGender = platform.getDb().getUserGender();
        final String unionid = platform.getDb().get("unionid");
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // 验证是否绑定第三方登录的方法
                if (thridType.equals("WEIXIN")) {
                    addBundling(userId, userName, unionid, thridType, userIcon);
                } else {
                    addBundling(userId, userName, appId, thridType, userIcon);
                }
            }

        });
//        Log.i("qqqqq", "onComplete: " + i);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
//        Log.i("qqqqqqq", "onError: " + throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
//        Log.i("qqqqq", "onCancel: " + i);
    }

    /**
     * @return 返回类型
     * @throws
     * @Title:
     * @Description: ${todo}(这里用一句话描述这个方法的作用)
     */
    private void addBundling(final int userId, String userName, String appId,
                             String thridType, String userIcon) {

        OkHttpUtils.get().
                addParams("userId", String.valueOf(userId))
                .addParams("cusName", userName)
                .addParams("appId", appId)
                .addParams("appType", thridType)
                .addParams("photo", userIcon)
                .url(Address.ADDBUNDLING)
                .build()
                .execute(new EntityPublicCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(EntityPublic response, int id) {
                        if (response.isSuccess()) {
                            IToast.show(RemoveBindingActivity.this, response.getMessage());
                            //获取与第三方绑定的方法
                            searchBingding(userId);
                        } else {
                            IToast.show(RemoveBindingActivity.this, response.getMessage());
                        }
                    }
                });

    }
}
