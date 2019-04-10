package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.Exam.adapter.PracticeConditionAdapter;
import com.yizhilu.Exam.adapter.PracticeReportListAdapter;
import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.ListEntity;
import com.yizhilu.Exam.entity.ListEntityCallback;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollExpandableListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

//练习报告
public class PracticeReportActivityTwo extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;  //返回图片
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;  //返回布局
    @BindView(R.id.title)
    TextView title;  //标题
    @BindView(R.id.reportType)
    TextView reportType;  //练习类型
    @BindView(R.id.submitTime)
    TextView submitTime;  //交卷时间
    @BindView(R.id.right_number)
    TextView rightNumber;  //正确题数
    @BindView(R.id.zongNumber)
    TextView zongNumber;  //总题数
    @BindView(R.id.answer_time)
    TextView answerTime;  //答题用时
    @BindView(R.id.average_time)
    TextView averageTime;  //平均用时
    @BindView(R.id.correct_rate)
    TextView correctRate;  //正确率
    @BindView(R.id.reportListView)
    NoScrollExpandableListView reportListView;  //知识点练习情况的列表
    @BindView(R.id.lookParser)
    TextView lookParser;  //查看解析
    private int paperId;  //做完题的试卷或练习的Id
    private int userId, subjectId;//用户id,专业id
    private ProgressDialog progressDialog;  //加载数据显示
    private int paperType;  //试卷类型
    private Intent intent;  //意图对象
    private PublicEntity paperRecord;  //报告记录的实体
    private PracticeReportListAdapter adapter;
    private List<PublicEntity> listEntity;
    private GridView gridView;
    private TextView questionsNum;
    private List<Integer> listNum;


    @Override
    protected int initContentView() {
        return R.layout.activity_practice_report;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(PracticeReportActivityTwo.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.practice_report);  //设置标题
        listEntity = new ArrayList<PublicEntity>();
        gridView = (GridView) findViewById(R.id.gridView);
        questionsNum = (TextView) findViewById(R.id.questions_num);
        listNum = new ArrayList<Integer>();
    }

    @Override
    protected void initData() {
        //获取传过来的信息
        getIntentMessage();
        //联网获取报告的方法
        getHttpReportData(paperId);
        //联网获取每个考点的练习情况
        getHttpPracticeCondition(userId, subjectId, paperId);
    }

    @Override
    protected void addListener() {

    }


    /**
     * 获取传过来的参数
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        paperId = intent.getIntExtra("paperId", 0);
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(this,"subjectId",0);
    }


    /**
     * @author bin
     * 时间: 2016/6/12 9:21
     * 方法说明:联网获取练习报告的方法
     */
    private void getHttpReportData(int paperId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(paperId));
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subjectId));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.PRACTICEREPORT_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }
            @Override
            public void onResponse(PublicEntity response, int id) {
                String message = response.getMessage();
                if (response.isSuccess()) {
                    cancelLoading();
                    paperRecord = response.getEntity().getPaperRecord();
                    paperType = paperRecord.getType();
                    reportType.setText(paperRecord.getPaperName());  //试卷类型
                    submitTime.setText(paperRecord.getAddTime() + "");  //交卷时间
                    rightNumber.setText(paperRecord.getCorrectNum() + "");  //答对题数
                    zongNumber.setText("/" + paperRecord.getQstCount() + "道");  //总题数
                    correctRate.setText((int) (paperRecord.getAccuracy() * 100) + "");  //正确率
                    answerTime.setText(paperRecord.getTestTime() + "");  //答题用时
                    double aa = (double) paperRecord.getTestTime() / paperRecord.getQstCount();
                    BigDecimal b = new BigDecimal(aa);
                    double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    averageTime.setText(f1 + "");  //平均用时
                    List<PublicEntity> list = response.getEntity().getQueryPaperReport();
                    for (int i = 0; i < list.size(); i++) {
                        listEntity.add(list.get(i));
                        if (list.get(i).getQuestionStatus() == 0) {
                            listNum.add(list.get(i).getQuestionStatus());
                        }
                    }
                    questionsNum.setText("共" + listEntity.size() + "道 你答对了" + listNum.size() + "道");
                    adapter = new PracticeReportListAdapter(PracticeReportActivityTwo.this, listEntity);
                    gridView.setAdapter(adapter);
                } else {
                    ConstantUtils.showMsg(PracticeReportActivityTwo.this, message);
                }
            }
        });
    }

    /**
     * @author bin
     * 时间: 2016/6/12 11:01
     * 方法说明:联网获取每个考点的练习情况
     */
    private void getHttpPracticeCondition(int userId, int subId, int paperRecordId) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("paperRecordId", String.valueOf(paperRecordId));

        OkHttpUtils.post().params(map).url(ExamAddress.PRACTICECONDITION_URL).build().execute(new ListEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(ListEntity response, int id) {
                PracticeConditionAdapter adapter = new PracticeConditionAdapter(PracticeReportActivityTwo.this, response);
                reportListView.setAdapter(adapter);
            }
        });

    }

    @OnClick({R.id.left_layout, R.id.lookParser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                PracticeReportActivityTwo.this.finish();
                break;
            case R.id.lookParser:
                intent.setClass(PracticeReportActivityTwo.this, LookParserActivity.class);
                intent.putExtra("id", paperRecord.getId());
                startActivity(intent);
                PracticeReportActivityTwo.this.finish();
                break;
        }
    }
}
