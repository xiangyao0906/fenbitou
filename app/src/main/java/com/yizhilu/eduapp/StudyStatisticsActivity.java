package com.yizhilu.eduapp;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yizhilu.eduapp.R.id.title_text;

/**
 * Created by bishuang on 2017/8/1.
 * 学习统计的类
 */

public class StudyStatisticsActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(title_text)
    TextView titleText;
    @BindView(R.id.login_number)
    TextView loginNumber;
    @BindView(R.id.join_exam)
    TextView joinExam;
    @BindView(R.id.look_video)
    TextView lookVideo;
    @BindView(R.id.publish_note)
    TextView publishNote;
    @BindView(R.id.publish_comment)
    TextView publishComment;
    @BindView(R.id.last_time)
    TextView lastTime;
    @BindView(R.id.regist_time)
    TextView registTime;
    private int userId;

    @Override
    protected int initContentView() {
        return R.layout.act_study_statistics;
    }

    @Override
    protected void initComponent() {
        titleText.setText(R.string.study_statistics);  //设置标题
    }

    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(StudyStatisticsActivity.this, "userId", -1);
        //获取个人简历的方法
        getPersonalResume(userId);
    }

    @Override
    protected void addListener() {

    }

    @OnClick(R.id.back_layout)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: //返回
                finish();
                break;

        }
    }

    /**
     * 获取个人简历的方法
     */
    private void getPersonalResume(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        showLoading(StudyStatisticsActivity.this);
        OkHttpUtils.get().params(map).url(Address.GET_PERSONAL_RESUME).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        cancelLoading();
                        if (response.isSuccess()) {
                            EntityPublic userEntity = response.getEntity().getUser();
                            lastTime.setText(userEntity.getLastLoginTime());
                            registTime.setText(userEntity.getCreatedate());
                            loginNumber.setText(userEntity.getLoginNum() + "");
                            lookVideo.setText(userEntity.getStudyNum() + "");
                            publishNote.setText(userEntity.getNoteNum() + "");
                            publishComment.setText(userEntity.getAssessNum() + "");
                            joinExam.setText(String.valueOf(userEntity.getAssessNum()));
                        } else {
                            IToast.show(StudyStatisticsActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
