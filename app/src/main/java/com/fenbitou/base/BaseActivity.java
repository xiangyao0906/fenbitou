package com.fenbitou.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.fenbitou.fragment.LoadingFragmentDialog;
import com.fenbitou.wantongzaixian.LoginActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;


/**
 * Created by ming on 2016/12/14 12:01.
 * Explain:
 */

public abstract class BaseActivity extends AutoLayoutActivity implements AdapterView.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private LoadingFragmentDialog loadingDialog = null;
    private int userId;
    private Dialog quitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DemoApplication.getInstance().getActivityStack().addActivity(this);

        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        loadingDialog = new LoadingFragmentDialog();

        ButterKnife.bind(this);

        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarEnable(false).init();
        initComponent();

        initData();

        addListener();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);

        DemoApplication.setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        DemoApplication.getInstance().getActivityStack().removeActivity(this);
    }

    /**
     * 初始化布局资源文件
     */
    protected abstract int initContentView();

    /**
     * 初始化组件
     */
    protected abstract void initComponent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 添加监听
     */
    protected abstract void addListener();

    public void showLoading(Activity context) {
        if(loadingDialog==null){
            loadingDialog=new LoadingFragmentDialog();
        }
        loadingDialog.show(context.getFragmentManager(), "请稍后");

    }
    public void cancelLoading() {
        loadingDialog.dismiss();

    }

    public void openActivity(Class<?> targetActivityClass) {
        Intent intent = new Intent(this, targetActivityClass);
        startActivity(intent);
    }

    public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openActivityAndCloseThis(Class<?> targetActivityClass) {
        openActivity(targetActivityClass);
        this.finish();
    }

    public void openActivityAndCloseThis(Class<?> targetActivityClass, Bundle bundle) {
        openActivity(targetActivityClass, bundle);
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);

    }

    @Override
    public void onLoadMore() {

    }


    // 限制登錄顯示的diaLog
    public void showQuitDiaLog() {
        if (quitDialog != null) {
            quitDialog.show();
        } else {
            View view = getLayoutInflater().inflate(R.layout.dialog_show, null);
            WindowManager manager = (WindowManager) getSystemService(
                    Context.WINDOW_SERVICE);
            int width = manager.getDefaultDisplay().getWidth();
            int scree = (width / 3) * 2;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = scree;
            view.setLayoutParams(layoutParams);
            quitDialog = new Dialog(this, R.style.custom_dialog);
            quitDialog.setContentView(view);
            quitDialog.setCancelable(false);
            quitDialog.show();
            TextView titles = (TextView) view.findViewById(R.id.texttitles);
            titles.setText("您的账号已在其他设备登陆。");
            TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
            btnsure.setText("退出");

            LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
            linbtnsure.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("static-access")
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtils.setParam(BaseActivity.this, "userId", -1);
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.putExtra("isSingle", true);
                    startActivity(intent);
                    quitDialog.dismiss();
                }
            });
            TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
            btncancle.setText("重新登录");
            btncancle.setTextColor(getResources().getColor(R.color.Blue));
            LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
            linbtncancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SharedPreferencesUtils.setParam(BaseActivity.this, "userId", -1);
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.putExtra("isSingle", true);
                    startActivity(intent);
                    quitDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
    }
}
