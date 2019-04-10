package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.Exam.adapter.StudyRecordAdapter;
import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author bin
 *         时间: 2016/5/26 13:59
 *         方法说明:学习记录的页面
 */
public class StudyRecordActivity extends BaseActivity implements RecordInterface {

    @BindView(R.id.side_menu)
    ImageView sideMenu;  //返回图片
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;  //返回布局
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.studyRecordList)  //学习记录的列表
    NoScrollListView studyRecordList;
    @BindView(R.id.assessment_layout)
    LinearLayout assessmentLayout;  //能力评估布局
    @BindView(R.id.record_image)
    ImageView recordImage;  //考试记录图片
    @BindView(R.id.record_text)
    TextView recordText;  //考试记录文字
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;  //错题记录布局
    @BindView(R.id.collection_layout)
    LinearLayout collectionLayout;  //收藏试题布局
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;  //下拉刷新和加载
    @BindView(R.id.null_text)
    TextView nullText;  //无内容显示文字
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;  //无内容显示布局
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private ProgressDialog progressDialog;  //加载数据显示
    private List<PublicEntity> recordList;  //学习记录的集合
    private StudyRecordAdapter recordAdapter;  //学习记录的适配器
    private Intent intent;  //意图对象


    @Override
    protected int initContentView() {
        return R.layout.activity_study_record;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
        recordList = new ArrayList<PublicEntity>();  //学习记录的集合
        progressDialog = new ProgressDialog(StudyRecordActivity.this); //加载数据显示的dialog
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(StudyRecordActivity.this,"subjectId",0);

        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.exam_record);  //设置标题
        recordImage.setBackgroundResource(R.mipmap.recorded);
        recordText.setTextColor(getResources().getColor(R.color.color_main));
    }

    @Override
    protected void initData() {
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }


    @OnClick({R.id.left_layout, R.id.assessment_layout, R.id.error_layout, R.id.collection_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.assessment_layout:
                openActivity(AbilityAssessActivity.class);
                finish();
                break;
            case R.id.error_layout:
                openActivity(WrongRecordActivity.class);
                finish();
                break;
            case R.id.collection_layout:
                openActivity(CollectRecordActivity.class);
                finish();
                break;
        }
    }

    /**
     * 联网获取练习历史的方法
     */
    private void getExamRecord(int userId, int subId, final int page) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("page.currentPage", String.valueOf(page));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.PRACTICEHISTORY_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                try {
                    PublicEntity publicEntity = response;
                    String message = publicEntity.getMessage();
                    if (publicEntity.isSuccess()) {
                        if (page == 1) {
                            recordList.clear();
                        }
                        PublicEntity entity = publicEntity.getEntity();
                        if (entity == null) {
                            swipeToLoadLayout.setVisibility(View.GONE);
                            nullLayout.setVisibility(View.VISIBLE);
                            nullText.setText("无考试记录");
                            return;
                        }
//                        if (entity.getPage().getTotalPageSize() == page) {
////                            swipeToLoadLayout.setRefreshing(true);
////                            swipeToLoadLayout.setLoadingMore(false);
//                            swipeToLoadLayout.setOnLoadMoreListener(null);
//                        }
                        List<PublicEntity> paperRecordList = entity
                                .getQueryPaperRecordList();
                        if (paperRecordList != null
                                && paperRecordList.size() > 0) {
                            for (int i = 0; i < paperRecordList
                                    .size(); i++) {
                                recordList.add(publicEntity
                                        .getEntity()
                                        .getQueryPaperRecordList()
                                        .get(i));
                            }
                            if (recordList != null && recordList.size() > 0) {
                                Log.i("yeye", "5888888" + recordList.toString());
                                recordAdapter = new StudyRecordAdapter(StudyRecordActivity.this,
                                        recordList);
                                recordAdapter.setRecordInterface(StudyRecordActivity.this);
                                studyRecordList.setAdapter(recordAdapter);

                            }
                        }
                        if (recordList.size() <= 0) {
                            swipeToLoadLayout.setVisibility(View.GONE);
                            nullLayout.setVisibility(View.VISIBLE);
                            nullText.setText("无考试记录");
                        } else {
                            nullLayout.setVisibility(View.GONE);
                            swipeToLoadLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ConstantUtils.showMsg(StudyRecordActivity.this,
                                message);
                    }
                } catch (Exception e) {
                    swipeToLoadLayout.setVisibility(View.GONE);
                    nullLayout.setVisibility(View.VISIBLE);
                    nullText.setText("无考试记录");
                }
            }
        });

    }

    @Override
    public void myClick(int position, String name) {
        if ("继续练习".equals(name)) {
            int type = recordList.get(position).getType();
            if (type != 2) {
                intent.setClass(StudyRecordActivity.this, BeginExamActivity.class);
                intent.putExtra("examName", "继续练习");
                intent.putExtra("continueId", recordList.get(position).getId());
                startActivity(intent);
            } else {
                intent.setClass(StudyRecordActivity.this, BeginExamPaperActivity.class);
                intent.putExtra("examName", "继续练习");
                intent.putExtra("continueId", recordList.get(position).getId());
                startActivity(intent);
            }
        } else if ("查看解析".equals(name)) {
            int type = recordList.get(position).getType();
            int id = recordList.get(position).getId();
            if (type != 2) {  //练习
                intent.setClass(StudyRecordActivity.this, LookParserActivity.class);
                intent.putExtra("id", id);  //试题的Id
                startActivity(intent);
            } else {  //考试
                intent.setClass(StudyRecordActivity.this, LookParserActivity.class);
                intent.putExtra("id", id);  //试题的Id
                startActivity(intent);
            }
        } else if ("查看报告".equals(name)) {
//            Intent intent = new Intent(StudyRecordActivity.this, PracticeReportActivity.class);
//            intent.putExtra("paperId", recordList.get(position).getId());
            intent.setClass(StudyRecordActivity.this, PracticeReportActivityTwo.class);
            intent.putExtra("paperId", recordList.get(position).getId());
            startActivity(intent);
        }
    }


    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        recordList.clear();
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }


    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:上拉加载
     */
    @Override
    public void onLoadMore() {
        page++;
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }


}
