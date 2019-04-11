package com.fenbitou.wantongzaixian;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.base.DemoApplication;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicListEntity;
import com.fenbitou.entity.PublicListEntityCallback;
import com.fenbitou.fragment.UpdateDialogFragment;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.utils.StringUtil;
import com.fenbitou.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import rx.functions.Action1;

import static com.fenbitou.wantongzaixian.R.id.system_wifi_img;
import static com.fenbitou.wantongzaixian.R.id.title_text;

/**
 * Created by bishuang on 2017/7/26.
 * 系统设置的类
 */

public class SystemSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(title_text)
    TextView titleText;
    @BindView(R.id.system_wifi_text)
    TextView systemWifiText;
    @BindView(system_wifi_img)
    CheckBox systemWifiImg;
    @BindView(R.id.system_eliminate_text1)
    TextView systemEliminateText1;
    @BindView(R.id.system_eliminate_text2)
    TextView systemEliminateText;
    @BindView(R.id.system_eliminate)
    LinearLayout systemEliminate;
    @BindView(R.id.system_Testing_text)
    TextView systemTestingText;
    @BindView(R.id.version_point)
    ImageView versionPoint;
    @BindView(R.id.system_Testing)
    RelativeLayout systemTesting;
    @BindView(R.id.system_about_text)
    TextView systemAboutText;
    @BindView(R.id.system_about_img)
    ImageView systemAboutImg;
    @BindView(R.id.system_about)
    LinearLayout systemAbout;
    @BindView(R.id.system_exit)
    TextView systemExit;
    private boolean isWifi, isVersion; //判断是否是wifi,判断是否点击了检测更新
    private File files;
    private long cacheSize;
    private String formetFileSize;
    private int userId; //用户名
    private PackageInfo packageInfo;  //包的信息
    private String android_url, android_v, packageName;  //apk下载地址,返回版本号,本地版本号
    private PackageManager packageManager;  //包管理对象
    private LayoutInflater inflater;
    private Dialog dialog;
    public static SystemSettingActivity settingActivity;
    @Override
    protected int initContentView() {
        return R.layout.act_system_setting;
    }

    @Override
    protected void initComponent() {
        packageManager = getPackageManager();
        userId = (int) SharedPreferencesUtils.getParam(SystemSettingActivity.this, "userId", 0);
        isWifi= (boolean) SharedPreferencesUtils.getParam(this,"wifi",true);
        systemWifiImg.setChecked(isWifi);
        titleText.setText(R.string.system_set); // 设置标题
        try {
            files = this.getCacheDir();
            cacheSize = StringUtil.getFolderSize(files);
            formetFileSize = StringUtil.FormetFileSize(cacheSize);
            systemEliminateText.setText(formetFileSize);
        } catch (Exception e) {
        }
        if (userId == 0) {
            systemExit.setVisibility(View.GONE);
        } else {
            systemExit.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initData() {
        //检测版本更新的方法
        detectionVersionsUpDate(true);
    }

    @Override
    protected void addListener() {
        systemWifiImg.setOnCheckedChangeListener(this);

        RxView.clicks(systemTesting).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                isVersion = true;
                detectionVersionsUpDate(true);
            }
        });
    }

    @OnClick({R.id.back_layout, system_wifi_img, R.id.system_eliminate, R.id.system_about, R.id.system_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: //返回
                finish();
                break;
            case R.id.system_eliminate:
                if (systemEliminateText.getText().equals("")) {
                    Toast.makeText(this, "无缓存数据", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showDialog();
                }
                break;
            case R.id.system_about: //关于
                openActivity(AboutActivity.class);
                settingActivity = this;
                break;
            case R.id.system_exit:
                showQuitDiaLog();
                break;
        }
    }

    public void showDialog() {
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_show, null);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.show();
        LinearLayout btnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
        TextView textView = (TextView) view.findViewById(R.id.texttitles);
        textView.setText("确定清空 ?");
        btnsure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringUtil.deleteFolderFile(files.getAbsolutePath(), true);
                systemEliminateText.setText("");
                dialog.cancel();
            }
        });
        LinearLayout btncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
        btncancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    /**
     * 方法说明:检测版本更新的方法
     */
    private void detectionVersionsUpDate(final boolean... c) {
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            packageName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.get().url(Address.VERSIONUPDATE).build().execute(new PublicListEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(SystemSettingActivity.this, "加载失败请重试");
            }

            @Override
            public void onResponse(PublicListEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    if (response.isSuccess()) {
                        List<EntityPublic> entity = response.getEntity();
                        for (int i = 0; i < entity.size(); i++) {
                            String keyType = entity.get(i).getkType();
                            if (keyType.equals("android")) {
                                android_url = entity.get(i).getDownloadUrl();
                                android_v = entity.get(i).getVersionNo();
                                int b = ValidateUtil.compareVersion(packageName, android_v);
                                if (!TextUtils.isEmpty(android_v) && b == -1) {
                                    versionPoint.setVisibility(View.VISIBLE);
                                    if (isVersion) {
                                        if (!TextUtils.isEmpty(android_url)) {
//                                            Intent intent = new Intent();
//                                            intent.setClass(SystemSettingActivity.this, UpdateEditionActivity.class);
//                                            intent.putExtra("name", android_v);
//                                            intent.putExtra("url", android_url);
//                                            startActivity(intent);
                                            DemoApplication.downloadContent = entity.get(i).getDepict();
                                            DemoApplication.downloadUrl = android_url;
                                           showUpdateInfo();
                                        }
                                    }
                                } else if (c[0]) {
                                    IToast.show(SystemSettingActivity.this, "目前没有发现新版本");
                                }
                            }
                        }
                    }
                }
            }
        });

    }


    private void showUpdateInfo() {

        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        updateDialogFragment.show(getSupportFragmentManager(), "dialog");


    }

    // 退出时显示的diaLog
    public void showQuitDiaLog() {
        View view = LayoutInflater.from(SystemSettingActivity.this).inflate(
                R.layout.dialog_show, null);
        WindowManager manager = (WindowManager) SystemSettingActivity.this.getSystemService(
                Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(SystemSettingActivity.this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        TextView textView = (TextView) view.findViewById(R.id.texttitles);
        textView.setText("确定退出 ?");
        TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
        LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
        linbtnsure.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("static-access")
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setParam(SystemSettingActivity.this, "userId", -1);
                Intent intent = new Intent();
                intent.setAction("exitApp");
                sendBroadcast(intent);
//                EventBus.getDefault().post("exitApp");
                finish();
                dialog.cancel();
            }
        });
        TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
        LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
        linbtncancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SharedPreferencesUtils.setParam(SystemSettingActivity.this,"wifi",true);

        } else {
            SharedPreferencesUtils.setParam(SystemSettingActivity.this,"wifi",false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingActivity = null;
    }
}
