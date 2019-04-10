package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.Exam.adapter.WrongRecordAdapter;
import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.Exam.entity.StringEntity;
import com.yizhilu.Exam.entity.StringEntityCallback;
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
 * 错题记录
 */
public class WrongRecordActivity extends BaseActivity implements RecordInterface {


    @BindView(R.id.side_menu)
    ImageView sideMenu;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.total_number)
    TextView totalNumber;
    @BindView(R.id.wrongRecordList)
    NoScrollListView wrongRecordList;
    @BindView(R.id.assessment_layout)
    LinearLayout assessmentLayout;
    @BindView(R.id.record_layout)
    LinearLayout recordLayout;
    @BindView(R.id.error_image)
    ImageView errorImage;
    @BindView(R.id.error_text)
    TextView errorText;
    @BindView(R.id.collection_layout)
    LinearLayout collectionLayout;
    @BindView(R.id.refreshScrollView)
    SwipeToLoadLayout refreshScrollView;  //刷新的布局
    @BindView(R.id.null_text)
    TextView nullText;  //无内容显示文字
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;  //无内容显示布局
    private ProgressDialog progressDialog;  //加载数据显示
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private List<PublicEntity> questionList; // 错误习题的集合
    private WrongRecordAdapter adapter;//错题适配器
    private Intent intent;  //意图对象

    @Override
    protected int initContentView() {
        return R.layout.activity_wrong_record;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
        questionList = new ArrayList<PublicEntity>();  //错误习题的集合
        progressDialog = new ProgressDialog(WrongRecordActivity.this); //加载数据显示的dialog
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(this,"subjectId",0);
        sideMenu.setBackgroundResource(R.mipmap.return_button);
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        title.setText(R.string.wrong_record);  //设置标题
        errorImage.setBackgroundResource(R.mipmap.error_no);
        errorText.setTextColor(getResources().getColor(R.color.color_main));
    }

    @Override
    protected void initData() {
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }

    @Override
    protected void addListener() {
        refreshScrollView.setOnRefreshListener(this);  //刷新
        refreshScrollView.setOnLoadMoreListener(this);  //加载
    }

    @OnClick({R.id.left_layout, R.id.assessment_layout, R.id.record_layout, R.id.collection_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.assessment_layout:
                openActivity(AbilityAssessActivity.class);
                finish();
                break;
            case R.id.record_layout:
                openActivity(StudyRecordActivity.class);
                finish();
                break;
            case R.id.collection_layout:
                openActivity(CollectRecordActivity.class);
                finish();
                break;
        }
    }


    /**
     * 联网获取错误习题的方法
     */
    private void getErrorExerciseData(int userId, int subId, final int pageSize) {
        Map<String,String> map  = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("page.currentPage", String.valueOf(pageSize));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.ERROREXERCISE_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                refreshScrollView.setRefreshing(false);
                refreshScrollView.setLoadingMore(false);
                cancelLoading();
                try {
                    PublicEntity publicEntity = response;
                    String message = publicEntity.getMessage();
                    if (publicEntity.isSuccess()) {
                        totalNumber.setText(publicEntity.getEntity().getPage().getTotalResultSize() + "");
                        if (pageSize == 1) {
                            questionList.clear();
                        }
                        if (pageSize == publicEntity.getEntity().getPage().getTotalPageSize()) {
//                            refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        List<PublicEntity> paperRecordList = publicEntity.getEntity().getQueryQuestionList();
                        if (paperRecordList != null && paperRecordList.size() > 0) {
                            for (int i = 0; i < paperRecordList.size(); i++) {
                                questionList.add(paperRecordList.get(i));
                            }
                        }
                        if(questionList.size()<=0){
                            refreshScrollView.setVisibility(View.GONE);
                            nullLayout.setVisibility(View.VISIBLE);
                            nullText.setText("无错误试题");
                        }else{
                            refreshScrollView.setVisibility(View.VISIBLE);
                            nullLayout.setVisibility(View.GONE);
                        }
                        adapter = new WrongRecordAdapter(WrongRecordActivity.this, questionList);
                        adapter.setRecordInterface(WrongRecordActivity.this);
                        wrongRecordList.setAdapter(adapter);
                    } else {
                        ConstantUtils.showMsg(WrongRecordActivity.this, message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    //
    @Override
    public void myClick(int position, String name) {
        if ("删除错题".equals(name)) {
            int errorQuestionId = questionList.get(position).getErrorQuestionId();
            //移除错题的方法
            removeErrorTi(errorQuestionId);
        } else if ("查看解析".equals(name)) {
            int id = questionList.get(position).getId();
            // TODO: 2017/8/31 0031 没写
            intent.setClass(WrongRecordActivity.this, SingleLookParserActivity.class);
            intent.putExtra("id", id);  //试题的Id
            startActivity(intent);
        }
    }


    /**
     * 移除错题的方法
     */
    private void removeErrorTi(int id) {
        Map<String,String> map  = new HashMap<>();
        map.put("ids", String.valueOf(id));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.REMOVEERRORTI_URL).build().execute(new StringEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(StringEntity response, int id) {
                cancelLoading();
                try {
                    StringEntity stringEntity = response;
                    if (stringEntity.isSuccess()) {
                        page = 1;
                        questionList.clear();
                        // 联网获取错误习题的方法
                        getErrorExerciseData(userId, subjectId, page);
                    } else {
                        ConstantUtils.showMsg(WrongRecordActivity.this, stringEntity.getMessage());
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        questionList.clear();
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }


    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:上拉加载
     */
    @Override
    public void onLoadMore() {
        page++;
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }

}
