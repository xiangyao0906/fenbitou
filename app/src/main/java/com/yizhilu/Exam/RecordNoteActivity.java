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
import com.yizhilu.Exam.utils.StaticUtils;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
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
 *         时间: 2016/6/25 14:47
 *         类说明:记笔记的类
 */
public class RecordNoteActivity extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_layout_tv)
    TextView rightLayoutTv;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.node_content)
    EditText nodeContent;
    private ProgressDialog progressDialog;  //加载数据显示
    private int userId, subId, qstId, zongPosition; //用户,专业,试题的Id,当前试题下标
    private Intent intent;  //意图对象
    private String noteContent;  //笔记内容

    @Override
    protected int initContentView() {
        return R.layout.activity_record_note;
    }

    @Override
    protected void initComponent() {
        //获取传过来的信息
        getIntentMessage();
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(RecordNoteActivity.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.add_note);  //设置标题
        rightLayout.setVisibility(View.VISIBLE);  //提交布局
        rightLayoutTv.setVisibility(View.VISIBLE);
        rightLayoutTv.setText(R.string.submit);  //提交
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    /**
     * @author bin
     * 时间: 2016/6/27 11:56
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subId= (int) SharedPreferencesUtils.getParam(RecordNoteActivity.this,"subjectId",0);
        Intent intent = getIntent();
        qstId = intent.getIntExtra("qstId", 0);  //获取试题的id
        zongPosition = intent.getIntExtra("zongPosition", -1);  //试题的下标
        noteContent = intent.getStringExtra("noteContent");  //笔记内容

        nodeContent.setText(noteContent);  //设置笔记内容
    }



    @OnClick({R.id.left_layout, R.id.right_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                intent.putExtra("note",noteContent);
                if(zongPosition<0){   //在错题记录或习题收藏过来的
                    setResult(0,intent);
                }else{
                    StaticUtils.getNoteList().set(zongPosition-1,noteContent);
                    setResult(1,intent);
                }
                RecordNoteActivity.this.finish();
                break;
            case R.id.right_layout:
                String correct = nodeContent.getText().toString();
                if (TextUtils.isEmpty(correct)) {
                    ConstantUtils.showMsg(RecordNoteActivity.this, "请输入笔记内容");
                } else {
                    //添加笔记的方法
                    addNote(userId, subId, qstId, correct);
                }
                break;
        }
    }


    /**
     * @author bin
     * 时间: 2016/6/7 14:18
     * 方法说明:添加笔记的方法
     */
    private void addNote(int cusId, int subId, int qstId, final String noteContent) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(cusId));
        map.put("subId", String.valueOf(subId));
        map.put("qstId", String.valueOf(qstId));
        map.put("noteContent", noteContent);
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.ADDNOTE_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                cancelLoading();
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String message = object.getString("message");
                        if (object.getBoolean("success")) {
                            nodeContent.setText("");
                            ConstantUtils.showMsg(RecordNoteActivity.this, "笔记添加成功");
                            intent.putExtra("note", noteContent);
                            if (zongPosition < 0) {   //在错题记录过来的
                                setResult(0, intent);
                            } else {
                                StaticUtils.getNoteList().set(zongPosition - 1, noteContent);
                                setResult(1, intent);
                            }
                            RecordNoteActivity.this.finish();
                        } else {
                            ConstantUtils.showMsg(RecordNoteActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        intent.putExtra("note",noteContent);
        if(zongPosition<0){   //在错题记录或习题收藏过来的
            setResult(0,intent);
        }else{
            StaticUtils.getNoteList().set(zongPosition-1,noteContent);
            setResult(1,intent);
        }
        finish();
    }
}
