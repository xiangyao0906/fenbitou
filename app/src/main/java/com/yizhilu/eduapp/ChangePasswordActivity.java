package com.yizhilu.eduapp;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText oldPwd,newPwd,resumeLoad; //旧密码 新密码 重新输入
    private Button preserve; //保存按钮
    private LinearLayout back,correct; //返回 错误
    private TextView correctContent; //错误内容
    private int userId; //用户Id
    private View oldLine,newLine,inputLine; //线


    @Override
    protected int initContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initComponent() {
        oldPwd = (EditText) findViewById(R.id.old_password); //旧密码
        newPwd = (EditText) findViewById(R.id.new_password); //新密码
        resumeLoad = (EditText) findViewById(R.id.password_resumeLoad); //重新输入
        preserve = (Button) findViewById(R.id.password_preserve); //保存
        back =  (LinearLayout) findViewById(R.id.change_password_back); //返回
        correct = (LinearLayout) findViewById(R.id.pwd_correct); //错误提示
        correctContent = (TextView) findViewById(R.id.pwd_CorrectContent); //错误内容
        oldLine=findViewById(R.id.oldpwd_line);
        newLine =findViewById(R.id.newpwd_line);
        inputLine = findViewById(R.id.inputpwd_line);
    }

    @Override
    protected void initData() {
        preserve.setOnClickListener(this); //保存
        back.setOnClickListener(this); //返回
        oldPwd.setOnFocusChangeListener(this); //旧密码
        newPwd.setOnFocusChangeListener(this); //新密码
        resumeLoad.setOnFocusChangeListener(this); //再次输入
    }

    @Override
    protected void addListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_preserve:  //保存
                String oldpwd = oldPwd.getText().toString(); //获取旧密码
                String newpwd = newPwd.getText().toString(); // 获取新密码
                String resumeload = resumeLoad.getText().toString();
                if (TextUtils.isEmpty(oldpwd)) {
                    correctContent.setText("请输入旧密码");
                    correct.setVisibility(View.VISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(newpwd)){
                    correctContent.setText("请输入新密码");
                    correct.setVisibility(View.VISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(resumeload)){
                    correctContent.setText("请再输入一次密码");
                    correct.setVisibility(View.VISIBLE);
                    return;
                }
                if (newpwd.equals(resumeload)) {
                    userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
                    updatePassword(userId, newpwd, oldpwd);
                }else {
                    correctContent.setText("两次新密码不一致");
                    correct.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.change_password_back:
                finish();
                break;
            default:
                break;
        }
    }
    private void updatePassword(int userId,String newpwd,String oldpwd){
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("newpwd", newpwd);
        map.put("oldpwd", oldpwd);
        OkHttpUtils.post().params(map).url(Address.UPDATE_PASSWORD).build().execute(
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            boolean success = object.getBoolean("success");
                            if (success){
                                ChangePasswordActivity.this.finish();
                            }
                            IToast.show(ChangePasswordActivity.this,message);
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.old_password: //旧密码
                if (hasFocus) {
                    correct.setVisibility(View.GONE);
                    correctContent.setText("");
                }else {
                    oldLine.setBackgroundResource(R.color.color_f6);
                }
                break;
            case R.id.new_password: //新密码
                if (hasFocus) {
                    correct.setVisibility(View.GONE);
                    correctContent.setText("");
                }else {
                    newLine.setBackgroundResource(R.color.color_f6);
                }
                break;
            case R.id.password_resumeLoad: //再次输入
                if (hasFocus) {
                    correct.setVisibility(View.GONE);
                    correctContent.setText("");
                }else {
                    inputLine.setBackgroundResource(R.color.color_f6);
                }
                break;

            default:
                break;
        }
    }
}
