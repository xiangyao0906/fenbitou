package com.fenbitou.Exam;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bin
 *         时间: 2016/6/4 11:23
 *         类说明:讨论题的选项
 */
public class DiscussActivity extends BaseActivity {
    @BindView(R.id.diacuss_num)
    TextView diacussNum;  //标题
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.start_answer)
    TextView startAnswer;
    @BindView(R.id.oneButton)
    RadioButton oneButton;
    @BindView(R.id.twoButton)
    RadioButton twoButton;
    private String num;  //题数
    private Intent intent;  //意图对象
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_discuss);
//
//        super.onCreate(savedInstanceState);
//    }

    @Override
    protected int initContentView() {
        return R.layout.activity_discuss;
    }

    @Override
    protected void initComponent() {
        //获取传过来的数据
        getIntentMessage();
        intent = new Intent();  //意图对象
//        diacussNum.setText("论述题自测-共(" + num + ")道");
        diacussNum.setText("论述题自测");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    /**
     * @author bin
     * 时间: 2016/6/4 11:30
     * 方法说明:获取传过来的数据
     */
    public void getIntentMessage() {
        Intent intent = getIntent();
        num = intent.getStringExtra("num");
    }

    @OnClick({R.id.cancel, R.id.start_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:  //取消
                DiscussActivity.this.finish();
                break;
            case R.id.start_answer:  //开始做题
                int id = radioGroup.getCheckedRadioButtonId();  //获取选中的Id
                if (id == R.id.oneButton) {
                    num = "1";
                } else if (id == R.id.twoButton) {
                    num = "2";
                } else {
                    ConstantUtils.showMsg(DiscussActivity.this, "请选择题数");
                    return;
                }
                intent.setClass(DiscussActivity.this, BeginExamActivity.class);
                intent.putExtra("examName","论述题自测");
                intent.putExtra("num",num);  //题数
                startActivity(intent);
                DiscussActivity.this.finish();
                break;
        }
    }
}
