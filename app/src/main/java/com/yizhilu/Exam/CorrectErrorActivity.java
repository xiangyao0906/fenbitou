package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author bin
 *         时间: 2016/6/7 13:41
 *         方法说明:纠错的类
 */
public class CorrectErrorActivity extends BaseActivity {

    @BindView(R.id.side_menu)
    ImageView sideMenu;  //返回按钮
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;  //返回布局
    @BindView(R.id.title)
    TextView title;   //标题
    @BindView(R.id.right_layout_tv)
    TextView rightLayoutTv;  //提交
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;  //提交的布局
    @BindView(R.id.correct_eidt)
    EditText correctEidt;  //编辑框
    private int qstId;  //试题的Id
    private ProgressDialog progressDialog;  //加载数据显示

    @Override
    protected int initContentView() {
        return R.layout.activity_correct_error;
    }

    @Override
    protected void initComponent() {
        progressDialog = new ProgressDialog(CorrectErrorActivity.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.correct_error);  //设置标题
        rightLayout.setVisibility(View.VISIBLE);  //提交布局
        rightLayoutTv.setVisibility(View.VISIBLE);
        rightLayoutTv.setText(R.string.submit);  //提交
    }

    @Override
    protected void initData() {
        //获取传过来的信息
        getIntentMessage();
    }

    @Override
    protected void addListener() {

    }

    /**
     * @author bin
     * 时间: 2016/6/7 14:16
     * 方法说明:获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        qstId = intent.getIntExtra("questionId", 0);
    }

    @OnClick({R.id.left_layout, R.id.right_layout})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:  //返回
                CorrectErrorActivity.this.finish();
                break;
            case R.id.right_layout:  //提交
                String correct = correctEidt.getText().toString();
                if(TextUtils.isEmpty(correct)){
                    ConstantUtils.showMsg(CorrectErrorActivity.this,"请输入纠错内容");
                }else{
                    //提交纠错的方法
                    submitCorrection(correct);
                }
                break;
        }
    }


    /**
     * @author bin
     * 时间: 2016/6/7 14:18
     * 方法说明:提交纠错的方法
     */
    private void submitCorrection(String string) {
        Map<String ,String > map = new HashMap<>();
        map.put("paperId", String.valueOf(0));
        map.put("qstId", String.valueOf(qstId));
        map.put("content", String.valueOf(string));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.SUBMITCORRECTION_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    cancelLoading();
                    try {
                        JSONObject object = new JSONObject(response);
                        String message = object.getString("message");
                        if (object.getBoolean("success")) {
                            correctEidt.setText("");
                            ConstantUtils.showMsg(CorrectErrorActivity.this, "纠错提交成功");
                            CorrectErrorActivity.this.finish();
                        } else {
                            ConstantUtils.showMsg(CorrectErrorActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

}
