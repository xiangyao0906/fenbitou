package com.yizhilu.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.yizhilu.base.DemoApplication;
import com.yizhilu.eduapp.R;
import com.yizhilu.service.DownloadService;
import com.yizhilu.utils.AppUpdateUtils;
import com.yizhilu.utils.ColorUtil;
import com.yizhilu.utils.DrawableUtil;
import com.yizhilu.utils.FileUtil;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;

import java.io.File;

/**
 * Created by Vector
 * on 2017/7/19 0019.
 */

public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public static boolean isShow = false;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private DownloadService.DownloadBinder iBinder;
    private long filesize;
    /**
     * 回调
     */
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            ILog.i("下载app11");
            iBinder = (DownloadService.DownloadBinder) service;
            startDownloadApp(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private LinearLayout mLlClose;
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.drawable.logo;
    private ImageView mTopIv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
//        setStyle(DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);


    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        //是否可以取消,会影响上面那条属性
//        setCancelable(false);
//        //window外可以点击,不拦截窗口外的事件
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (iBinder != null) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lib_update_app_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = (TextView) view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = (TextView) view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = (Button) view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = (NumberProgressBar) view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = (ImageView) view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = (LinearLayout) view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = (ImageView) view.findViewById(R.id.iv_top);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
//        //设置主题色
        initTheme();

        //更新内容
        mContentTextView.setText(DemoApplication.downloadContent);
        //标题
        mTitleTextView.setText("是否升级到新的版本？");

        initEvents();
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {

        final int color = getResources().getColor(R.color.color_main);

        setDialogTheme(color);


    }

    /**
     * 设置
     *
     * @param color 主色
     */
    private void setDialogTheme(int color) {
        mTopIv.setImageResource(R.mipmap.lib_update_app_top_bg);
        mUpdateOkButton.setBackgroundDrawable(DrawableUtil.getDrawable(AppUpdateUtils.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            installApp();
            mUpdateOkButton.setVisibility(View.GONE);
        } else if (i == R.id.iv_close) {
            dismiss();
        }
    }

    private void installApp() {

        String appName = getResources().getString(R.string.apk_name) + ".apk";
        String target = FileUtil.getDirPath("download")
                + File.separator;
        ;
        File app = new File(target, appName);


        if (DownloadService.isRunning) {
            IToast.show(getActivity(), "app正在更新");
            dismiss();
            return;
        }


        if (AppUpdateUtils.appIsDownloaded(app, filesize)) {
            AppUpdateUtils.install(getContext(), app);
            //安装完自杀
            dismiss();
        } else {
            downloadApp();
        }
    }


    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        //使用ApplicationContext延长他的生命周期
        DownloadService.bindService(getActivity().getApplicationContext(), conn);
    }

    /**
     * 回调监听下载
     */
    private void startDownloadApp(DownloadService.DownloadBinder binder) {

        ILog.i("下载app");
        // 开始下载，监听下载进度，可以用对话框显示

        binder.start(new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
                if (!UpdateDialogFragment.this.isRemoving()) {
                    mNumberProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onProgress(float progress, long totalSize) {
                filesize = totalSize;
                if (!UpdateDialogFragment.this.isRemoving()) {
                    mNumberProgressBar.setProgress(Math.round(progress * 100));
                    mNumberProgressBar.setMax(100);
                }
            }

            @Override
            public void setMax(long total) {

            }

            @Override
            public boolean onFinish(File file) {
                if (!UpdateDialogFragment.this.isRemoving()) {
                    dismissAllowingStateLoss();
                }
                return true;
            }

            @Override
            public void onError(String msg) {
                if (!UpdateDialogFragment.this.isRemoving()) {
                    dismissAllowingStateLoss();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }
}
