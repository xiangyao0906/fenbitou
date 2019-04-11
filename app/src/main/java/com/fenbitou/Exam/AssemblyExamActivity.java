package com.fenbitou.Exam;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bin
 *         时间: 2016/6/4 11:38
 *         类说明:组卷模考的选择的类
 */
public class AssemblyExamActivity extends BaseActivity {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;  //难度的组
    @BindView(R.id.cancel)
    TextView cancel;  //取消
    @BindView(R.id.start_answer)
    TextView startAnswer;  //开始做题
    @BindView(R.id.cb1)
    CheckBox cb1;  //知识点练习
    @BindView(R.id.cb2)
    CheckBox cb2;  //阶段测试
    @BindView(R.id.cb3)
    CheckBox cb3;  //真题练习
    private int level;  //难易程度
    private StringBuffer sourceBuffer;  //试卷的来源
    private Intent intent;  //意图对象
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView();
//        ButterKnife.inject(this);
//        super.onCreate(savedInstanceState);
//    }

    @Override
    protected int initContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_assembly_exam;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.cancel, R.id.start_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:  //取消
                AssemblyExamActivity.this.finish();
                break;
            case R.id.start_answer:  //开始做题
                sourceBuffer = new StringBuffer();  //试卷的来源
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.rb1) {
                    level = 1;
                } else if (id == R.id.rb2) {
                    level = 2;
                } else if (id == R.id.rb3) {
                    level = 3;
                } else {
                    ConstantUtils.showMsg(AssemblyExamActivity.this, "请选择难度");
                    return;
                }
                if (cb1.isChecked()) {
                    sourceBuffer.append("'theSpecialTest',"); // 知识点练习
                }
                if (cb2.isChecked()) {
                    sourceBuffer.append("'examSprint',"); // 阶段测试
                }
                if (cb3.isChecked()) {
                    sourceBuffer.append("'zhenTi',"); // 真题练习
                }
                if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()) {
                    ConstantUtils.showMsg(AssemblyExamActivity.this, "请选择试卷来源");
                    return;
                }
                intent.setClass(AssemblyExamActivity.this, BeginExamActivity.class);
                intent.putExtra("examName", "组卷模考");
                intent.putExtra("level", level);  //难易程度
                intent.putExtra("source", sourceBuffer.toString());  //来源
                startActivity(intent);
                AssemblyExamActivity.this.finish();
                break;
        }
    }

}
