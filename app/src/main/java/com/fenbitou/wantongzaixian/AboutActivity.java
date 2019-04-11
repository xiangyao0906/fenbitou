package com.fenbitou.wantongzaixian;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.ShareDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/7/26.
 * 关于我们的类
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    ImageView rightBtn;
    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.go_welcome)
    TextView goWelcome;
    private String name;
    private ShareDialog shareDialog;

    @Override
    protected int initContentView() {
        return R.layout.act_about;
    }

    @Override
    protected void initComponent() {
        titleText.setText(R.string.about_us);
        try {
            name = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        aboutVersion.setText("V "+name); //获取当前版本号
        //是否能分享
        getShare();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.right_btn,R.id.go_welcome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.right_btn: //分享
                if(shareDialog == null){
                    shareDialog = new ShareDialog(this, R.style.custom_dialog);
                    shareDialog.setCancelable(false);
                    shareDialog.show();
                    shareDialog.shareInfo(null, false, false, true);
                }else{
                    shareDialog.show();
                }
                break;
            case R.id.go_welcome: //欢迎页
//                DemoApplication.getInstance().getActivityStack().exit();
//                openActivity(GuideActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from","AboutActivity");
                openActivity(GuideActivity.class,bundle);
                SystemSettingActivity.settingActivity.finish();
                finish();
                break;
        }
    }

    // 是否能分享   
    private void getShare() {
        showLoading(AboutActivity.this);
        OkHttpUtils.get().url(Address.WEBSITE_VERIFY_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(AboutActivity.this,"加载失败请重试");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        boolean success = response.isSuccess();
                        EntityPublic entity = response.getEntity();
                        String verifyTranspond = entity.getVerifyTranspond();
                        if (success) {
                            Log.i("lala", verifyTranspond);
                            if (verifyTranspond.equals("ON")) {
                                rightBtn.setVisibility(View.VISIBLE);
                            } else {
                                rightBtn.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }
}
