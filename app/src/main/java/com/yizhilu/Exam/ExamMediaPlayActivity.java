package com.yizhilu.Exam;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class ExamMediaPlayActivity extends BaseActivity {

    /**
     * 传递过来的播放地址
     */
    private String videoUrl;
    /**
     * 用户id
     */
    private int userId;
    private String resultUrl;

    @Override
    protected int initContentView() {
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_exam_media_play;
    }

    @Override
    protected void initComponent() {
        Intent intent = getIntent();
        //获取试卷的名称
        videoUrl = intent.getStringExtra("videoId");

    }

    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        if (!TextUtils.isEmpty(videoUrl)){
            parseUrl(videoUrl);
        }

    }

    private void parseUrl(String videoUrl) {
    }


    @Override
    protected void addListener() {

    }
}
