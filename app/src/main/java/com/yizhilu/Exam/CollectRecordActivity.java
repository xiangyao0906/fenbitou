package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.yizhilu.Exam.adapter.CollectRecordAdapter;
import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author bin
 *         时间: 2016/6/2 11:50
 *         类说明:收藏试题的类
 */
public class CollectRecordActivity extends BaseActivity implements RecordInterface {
    @BindView(R.id.side_menu)
    ImageView sideMenu;  //返回图片
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;  //返回布局
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.assessment_layout)
    LinearLayout assessmentLayout;  //能力评估布局
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;  //错题记录布局
    @BindView(R.id.record_layout)
    LinearLayout recordLayout;  //学习记录布局
    @BindView(R.id.collection_image)
    ImageView collectionImage;  //收藏图片
    @BindView(R.id.collection_text)
    TextView collectionText;  //收藏文字
    @BindView(R.id.total_number)
    TextView totalNumber;  //收藏题数
    @BindView(R.id.collectRecordList)
    NoScrollListView collectRecordList;  //收藏试题的列表
    @BindView(R.id.refreshScrollView)
    SwipeToLoadLayout refreshScrollView;  //刷新
    @BindView(R.id.null_text)
    TextView nullText;  //空布局显示的文字
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;  //无内容显示
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private ProgressDialog progressDialog;  //加载数据显示
    private Intent intent;  //意图对象
    private List<PublicEntity> collectList;  //收藏的集合
    private CollectRecordAdapter collectRecordAdapter;  //收藏的适配器


    @Override
    protected int initContentView() {
        return R.layout.activity_collect_record;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(CollectRecordActivity.this); //加载数据显示的dialog
        collectList = new ArrayList<PublicEntity>();  //收藏的集合
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(CollectRecordActivity.this,"subjectId",0);
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.collection);  //设置标题
        collectionImage.setBackgroundResource(R.mipmap.collected);
        collectionText.setTextColor(getResources().getColor(R.color.color_main));
    }

    @Override
    protected void initData() {
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }

    @Override
    protected void addListener() {
        refreshScrollView.setOnRefreshListener(this);  //刷新
        refreshScrollView.setOnLoadMoreListener(this);  //加载
    }



    @OnClick({R.id.left_layout, R.id.assessment_layout, R.id.record_layout, R.id.error_layout})
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
            case R.id.error_layout:
                openActivity(WrongRecordActivity.class);
                finish();
                break;
        }
    }



    /**
     * 联网获取收藏习题的方法
     */
    private void getCollectExerciseData(int userId, int subId, final int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("page.currentPage", String.valueOf(pageSize));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.COLLECTEXERCISE_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(String response, int id) {
                cancelLoading();
                refreshScrollView.setRefreshing(false);
                refreshScrollView.setLoadingMore(false);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        PublicEntity publicEntity = new Gson().fromJson(response, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            totalNumber.setText(publicEntity.getEntity().getPage().getTotalResultSize() + "");
                            if (pageSize == 1) {
                                collectList.clear();
                            }
                            if (pageSize == publicEntity.getEntity()
                                    .getPage().getTotalPageSize()) {
                            }
                            if (response.contains("queryQuestionList")) {
                                for (int i = 0; i < publicEntity
                                        .getEntity().getQueryQuestionList()
                                        .size(); i++) {
                                    collectList.add(publicEntity
                                            .getEntity()
                                            .getQueryQuestionList().get(i));
                                }
                            }
                            if (collectList.size() <= 0) {
                                refreshScrollView.setVisibility(View.GONE);
                                nullLayout.setVisibility(View.VISIBLE);
                                nullText.setText("无收藏试题");
                            } else {
                                refreshScrollView.setVisibility(View.VISIBLE);
                                nullLayout.setVisibility(View.GONE);
                            }
                            collectRecordAdapter = new CollectRecordAdapter(
                                    CollectRecordActivity.this, collectList);
                            collectRecordAdapter.setRecordInterface(CollectRecordActivity.this);  //绑定点击事件
                            collectRecordList.setAdapter(collectRecordAdapter);
                        } else {
                            ConstantUtils.showMsg(CollectRecordActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    @Override
    public void myClick(int position, String name) {
        int id = collectList.get(position).getId();
        if ("查看解析".equals(name)) {
            intent.setClass(CollectRecordActivity.this, SingleLookParserActivity.class);
            intent.putExtra("id", id);  //试题的Id
            startActivity(intent);
        } else if ("取消收藏".equals(name)) {
            //取消收藏的方法
            cancelCollectTi(userId, subjectId, id);
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/7 10:33
     * 方法说明:取消收藏的方法
     */
    private void cancelCollectTi(final int userId, int subId, int id) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("qstId", String.valueOf(id));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.CANCELCOLLECT_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                try {
                    PublicEntity publicEntity = response;
                    String message = publicEntity.getMessage();
                    if (publicEntity.isSuccess()) {
                        page = 1;
                        collectList.clear();
//                            refreshScrollView.setMode(Mode.BOTH);
                        //联网获取收藏习题的方法
                        getCollectExerciseData(userId, subjectId, page);
                    } else {
                        ConstantUtils.showMsg(CollectRecordActivity.this, message);
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
        collectList.clear();
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }


    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:上拉加载
     */
    @Override
    public void onLoadMore() {
        page++;
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }


}
