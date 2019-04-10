package com.yizhilu.eduapp;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/7/26.
 * 意见反馈的类
 */

public class OpinionFeedbackActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.feedback_content)
    EditText feedbackContent;
    @BindView(R.id.feedback_number)
    EditText feedbackNumber;
    @BindView(R.id.feedback_button)
    Button feedbackButton;

    private int userId;
    private String userNumber, userContent; //用户内容 手机号

    @Override
    protected int initContentView() {
        return R.layout.act_opinion_feedback;
    }

    @Override
    protected void initComponent() {
        titleText.setText("意见反馈");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.feedback_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.feedback_button:
                userId = (int) SharedPreferencesUtils.getParam(OpinionFeedbackActivity.this, "userId", 0);  //获取用户Id
                if (userId == 0) {
                    Intent intent = new Intent(OpinionFeedbackActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                userNumber = feedbackNumber.getText().toString();
                userContent = feedbackContent.getText().toString();
                if (TextUtils.isEmpty(userContent)) {
                    IToast.show(OpinionFeedbackActivity.this, "请输入您的反馈内容");
                    return;
                }
                if (TextUtils.isEmpty(userNumber)) {
                    IToast.show(OpinionFeedbackActivity.this, "请输入您的QQ/邮箱/手机号");
                    return;
                }
                if (ValidateUtil.isMobile(userNumber)) {
                    getHelpFeedback(userId, userNumber, userContent);
                } else if (ValidateUtil.isEmail(userNumber)) {
                    getHelpFeedback(userId, userNumber, userContent);
                } else if (ValidateUtil.isNumbers(userNumber) && userNumber.length() >= 5) {
                    getHelpFeedback(userId, userNumber, userContent);
                } else {
                    IToast.show(OpinionFeedbackActivity.this, "请输入正确的QQ/邮箱/手机号格式");
                }
                break;
        }
    }

    /**
     * 方法说明:意见反馈的方法
     */
    private void getHelpFeedback(final int userId, String contact, String userContent) {

        Map<String, String> map = new HashMap<>();
        map.put("userFeedback.userId", userId + "");
        map.put("contact", contact);
        map.put("userFeedback.content", userContent);
        showLoading(OpinionFeedbackActivity.this);
        OkHttpUtils.post().url(Address.HELP_FEEDBACK).params(map).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(OpinionFeedbackActivity.this, "加载失败请重试");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            IToast.show(OpinionFeedbackActivity.this, "反馈成功");
                            finish();
                        } else {
                            IToast.show(OpinionFeedbackActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
