package com.yizhilu.community;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.MessageCallback;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.EmojiFilter;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AddCommentActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.email_text)
    TextView emailText;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    private int topicId, userId;

    @Override
    protected int initContentView() {
        return R.layout.activity_add_comment;
    }

    @Override
    protected void initComponent() {
        editComment.setFilters(new InputFilter[]{new EmojiFilter()});
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        topicId = bundle.getInt("topicId");
        userId = (int) SharedPreferencesUtils.getParam(AddCommentActivity.this, "userId", -1);
        titleText.setText("写评论");

    }

    @Override
    protected void addListener() {

    }


    @OnClick({R.id.back_layout, R.id.email_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.email_text:
                if (TextUtils.isEmpty(editComment.getText())) {
                    ConstantUtils.showMsg(AddCommentActivity.this, "请输入评论内容！");
                } else {
                    submitComment(topicId, userId, editComment.getText().toString().trim());// 提交话题评论
                }
                break;
        }
    }


    // 提交话题评论
    private void submitComment(int topicId, int userId, String commentContent) {

        OkHttpUtils.get()
                .addParams("topicId", String.valueOf(topicId))
                .addParams("userId", String.valueOf(userId))
                .addParams("commentContent", commentContent)
                .url(Address.CREATECOMMENT)
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                IToast.show(AddCommentActivity.this, response.getMessage());
                                AddCommentActivity.this.finish();
                            } else {
                                if (TextUtils.isEmpty(response.getMessage())) {
                                    IToast.show(AddCommentActivity.this, "评论失败！");
                                } else {
                                    IToast.show(AddCommentActivity.this, response.getMessage());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }

}
