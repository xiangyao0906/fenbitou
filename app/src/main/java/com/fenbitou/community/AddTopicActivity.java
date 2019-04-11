package com.fenbitou.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.MessageCallback;
import com.fenbitou.entity.MessageEntity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.EmojiFilter;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AddTopicActivity extends BaseActivity {


    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.email_text)
    TextView send;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    private int groupId, userId;

    @Override
    protected int initContentView() {
        return R.layout.activity_add_topic;
    }

    @Override
    protected void initComponent() {
        editContent.setFilters(new InputFilter[]{new EmojiFilter()});
        editTitle.setFilters(new InputFilter[]{new EmojiFilter()});
        rightLayout.setVisibility(View.VISIBLE);
        titleText.setText("发表话题");// 设置标题
        send.setText("发表");
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getInt("groupId");
        userId = (int) SharedPreferencesUtils.getParam(AddTopicActivity.this, "userId", -1);

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
            case R.id.email_text://发表

                if (TextUtils.isEmpty(editTitle.getText())) {
                    ConstantUtils.showMsg(AddTopicActivity.this, "标题不能为空");
                } else if (editTitle.getText().length() < 4) {
                    ConstantUtils.showMsg(AddTopicActivity.this, "标题不能少于4个字符");
                } else if (TextUtils.isEmpty(editContent.getText())) {
                    ConstantUtils.showMsg(AddTopicActivity.this, "内容不能为空");
                } else if (editContent.getText().length() < 10) {
                    ConstantUtils.showMsg(AddTopicActivity.this, "内容不能少于10个字符");
                } else {
                    submitTopic(groupId, userId, editTitle.getText().toString().trim(),
                            editContent.getText().toString().trim());// 提交话题
                }


                break;
        }
    }

    private void submitTopic(int groupId, int userId, String title, String content) {

        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .addParams("userId", String.valueOf(userId))
                .addParams("title", title)
                .addParams("content", content)
                .url(Address.ADDTOPIC)
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                Intent intent = new Intent();
                                intent.setAction("Change");
                                intent.putExtra("topic", true);
                                sendBroadcast(intent);
                                ConstantUtils.showMsg(AddTopicActivity.this, "发表成功！");
                                AddTopicActivity.this.finish();
                            } else {
                                if (TextUtils.isEmpty(response.getMessage())) {
                                    ConstantUtils.showMsg(AddTopicActivity.this, "发表失败！");
                                } else {
                                    ConstantUtils.showMsg(AddTopicActivity.this, response.getMessage());
                                }
                            }
                        } catch (Exception e) {
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
}
