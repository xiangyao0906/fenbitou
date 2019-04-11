package com.fenbitou.wantongzaixian;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fenbitou.service.DownLoadVersionService;

import java.text.SimpleDateFormat;

/**
 * Created by bishuang on 2017/7/26.
 * 更新项目的类
 */

public class UpdateEditionActivity extends Activity {

    private DownLoadVersionService.DownloadBinder binder;
    public SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownLoadVersionService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private String updateURL, versionname;
    public static final int RESULT_CANCLE = 11;// 取消升级
    public static final int RESULT_UPDATE = 12;// 去升级
    public static final int RESULT_FORCE_CANCLE = 13;// 强制升级取消
    public static final int RESULT_FORCE_UPDATE = 14;// 强制升级取消
    public static final int RESULT_NO_UPDATE = 15;// 不需要更新
    private LayoutInflater inflater;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getMessageIntent();
        super.onCreate(bundle);
        initComponent();
    }

    public void getMessageIntent() {
        updateURL = getIntent().getStringExtra("url");
        versionname = getIntent().getStringExtra("name");

    }

    public void initComponent() {
        if (updateURL != null && versionname != null) {
            String name;
            try {
                name = getPackageManager().getPackageInfo(getPackageName(),
                        PackageManager.GET_CONFIGURATIONS).versionName;

                if (!versionname.equals(name)
                        && !TextUtils.isEmpty(updateURL)) {
                    showDialog();
                    Intent intent = new Intent(this, DownLoadVersionService.class);
                    intent.putExtra("updateURL", updateURL);
                    startService(intent);
                    bindService(intent, conn, Context.BIND_AUTO_CREATE);
                    return;
                } else {
                    Toast.makeText(UpdateEditionActivity.this, "暂无最新版本",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(UpdateEditionActivity.this, "暂无最新版本",
                    Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_NO_UPDATE);
        this.finish();
    }

    public void showDialog() {
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_show, null);
        WindowManager manager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
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
        TextView sureTextView = (TextView) view.findViewById(R.id.dialogbtnsure);
        TextView textView = (TextView) view.findViewById(R.id.texttitles);
        textView.setText(R.string.set_update);
        sureTextView.setText(R.string.set_yes);
        btnsure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_UPDATE);
                UpdateEditionActivity.this.finish();
                if (binder.getProgress() == 0) {
                    binder.start();
                }
            }
        });
        LinearLayout btncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
        TextView canleTextView = (TextView) view.findViewById(R.id.dialogbtncancle);
        canleTextView.setText(R.string.set_no);
        btncancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(RESULT_CANCELED);
                UpdateEditionActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != binder) {
            unbindService(conn);
        }
    }

}
