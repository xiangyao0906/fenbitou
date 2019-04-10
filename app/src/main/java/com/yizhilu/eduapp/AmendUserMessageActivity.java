package com.yizhilu.eduapp;


import android.content.Intent;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
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


/**
 * 修改用户信息的类
 */
public class AmendUserMessageActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.manLayout)
    LinearLayout manLayout;
    @BindView(R.id.womanLayout)
    LinearLayout womanLayout;
    @BindView(R.id.sexLinearLayout)
    LinearLayout sexLinearLayout;
    @BindView(R.id.messageLayout)
    RelativeLayout messageLayout;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.save_message)
    TextView save_message;
    @BindView(R.id.manImage)
    ImageView manImage;
    @BindView(R.id.womanImage)
    ImageView womanImage;
    @BindView(R.id.chenage_EditText)
    EditText changeEditText;
    private int userId, sex;  //用户Id,性别
    private String title, changeMessage;  //标题,更改的内容
    private String message;

    @Override
    protected int initContentView() {
        return R.layout.activity_amend_usermessage;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        title_text.setText(title);  //设置标题
        if ("选择性别".equals(title)) {
            save_message.setVisibility(View.GONE);
            messageLayout.setVisibility(View.GONE);
            sexLinearLayout.setVisibility(View.VISIBLE);
            if (sex == 0) {
                manImage.setVisibility(View.VISIBLE);
                womanImage.setVisibility(View.GONE);
            } else {
                manImage.setVisibility(View.GONE);
                womanImage.setVisibility(View.VISIBLE);
            }
        }
        changeEditText.setText(message);
    }
    private void getIntentMessage() {
        userId= (int) SharedPreferencesUtils.getParam(AmendUserMessageActivity.this,"userId",-1);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        sex = intent.getIntExtra("sex", 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }
    @OnClick({R.id.back_layout,R.id.save_message,R.id.manLayout,R.id.womanLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:  //返回
                this.finish();
                break;
            case R.id.save_message:  //保存
                changeMessage = changeEditText.getText().toString();
                upDateUserMessage();
                break;
            case R.id.manLayout:
                sex = 0;
                manImage.setVisibility(View.VISIBLE);
                womanImage.setVisibility(View.GONE);
                upDateUserMessage();
                break;
            case R.id.womanLayout:
                sex = 1;
                manImage.setVisibility(View.GONE);
                womanImage.setVisibility(View.VISIBLE);
                upDateUserMessage();
                break;
            default:
                break;
        }
    }

    /**
     * 更新用户信息
     */
    public void upDateUserMessage(){
        Map<String, String> map = new HashMap<>();
        map.put("user.id", String.valueOf(userId));
        if("选择性别".equals(title)){
            map.put("user.gender", String.valueOf(sex));
        }else if("修改昵称".equals(title)){
            map.put("user.nickname", changeMessage);
        }else if("修改姓名".equals(title)){
            map.put("user.realname", changeMessage);
        }else if("个性签名".equals(title)){
            map.put("user.userInfo", changeMessage);
        }
        OkHttpUtils.post().params(map).url(Address.UPDATE_MYMESSAGE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()){
                                IToast.show(AmendUserMessageActivity.this, "修改成功");
                                AmendUserMessageActivity.this.finish();
                            }else {
                                IToast.show(AmendUserMessageActivity.this,response.getMessage());
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

}
