package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yizhilu.Exam.adapter.StageCommentAdapter;
import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
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
 *         时间: 2016/5/31 14:25
 *         类说明:阶段测评的类
 */
public class StageCommentActivity extends BaseActivity implements RecordInterface {

    @BindView(R.id.left_layout)
    LinearLayout leftLayout; //返回的布局
    @BindView(R.id.title)
    TextView title; //标题
    @BindView(R.id.comment_listView)
    ListView commentListView; //测试列表
    @BindView(R.id.side_menu)
    ImageView sideMenu;
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;

    private int userId, subjectId, examType, page = 1, pageSize = 10; //用户Id,专业id,试卷类型,页数,每页显示的条目数
    private ProgressDialog progressDialog;  //加载数据显示的dialog
    private String examName;  //试卷类型名称
    private List<PublicEntity> paperList;
    private StageCommentAdapter stageCommentAdapter;  //阶段测试的适配器
    private Intent intent;  //意图对象

    @Override
    protected int initContentView() {
        return R.layout.activity_stage_comment;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        intent = new Intent();  //意图对象
        paperList = new ArrayList<PublicEntity>();
//        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", -1);  //获取用户id
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        subjectId= (int) SharedPreferencesUtils.getParam(StageCommentActivity.this,"subjectId",0);
        progressDialog = new ProgressDialog(StageCommentActivity.this); //加载数据显示的dialog
        leftLayout.setVisibility(View.VISIBLE); //返回的按钮
        if ("阶段测试".equals(examName)) {
            title.setText(R.string.comment_title);  //设置标题
        } else if ("真题练习".equals(examName)) {
            title.setText(R.string.zhenti_practice);  //设置标题
        }
        sideMenu.setBackgroundResource(R.mipmap.return_button);

    }

    @Override
    protected void initData() {
        //联网获取阶段测试的数据
        getPhaseTestData(userId, subjectId, examType, page, pageSize, examName);
    }

    @Override
    protected void addListener() {

    }

    /**
     * 获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        examType = intent.getIntExtra("examType", 0);  //2：阶段测试  3：真题练习
        examName = intent.getStringExtra("examName");  //获取试卷的名称
    }

    /**
     * 联网获取阶段测试和真题练习的数据的数据
     */
    private void getPhaseTestData(int userId, int subId, final int type, final int page, int pageSize, String title) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        map.put("type", String.valueOf(type));
        map.put("page.currentPage", String.valueOf(page));
        map.put("page.pageSize", String.valueOf(pageSize));
        map.put("title", title);
        showLoading(this);
        Log.i("ceshi",ExamAddress.PHASETESTLIST_URL+"?"+map+"--------------列表");
        OkHttpUtils.post().params(map).url(ExamAddress.PHASETESTLIST_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                String message = response.getMessage();
                if (response.isSuccess()) {
                    cancelLoading();
                    List<PublicEntity> list = response.getEntity().getPaperList();
                    if (list == null || list.size() <= 0) {
                        nullLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (response.getEntity().getPaperList() != null && response.getEntity().getPaperList().size() > 0) {
                        for (int i = 0; i < response.getEntity().getPaperList().size(); i++) {
                            paperList.add(list.get(i));
                        }
                    }
                    stageCommentAdapter = new StageCommentAdapter(StageCommentActivity.this, paperList, type);
                    stageCommentAdapter.setRecordInterface(StageCommentActivity.this);
                    commentListView.setAdapter(stageCommentAdapter);
//                    refreshScrollView.onRefreshComplete();
                }
            }
        });
    }

    /**
     * @author bin
     * 时间: 2016/6/8 10:13
     * 方法说明:item中进入考试的点击事件
     */
    @Override
    public void myClick(int position, String name) {
        Log.i("zqzq","myClick" + position);
        if ("进入考试".equals(name)) {
            int testNumber = paperList.get(position).getTestNumber();
            int status = paperList.get(position).getStatus();
            int paperlist = paperList.get(position).getCanTest();
            if (paperlist == 0) {
                ConstantUtils.showMsg(StageCommentActivity.this, "此试卷只允许做一次,您已经做过一次了!");
                return;
            }
            int id = paperList.get(position).getId();
            intent.setClass(StageCommentActivity.this, BeginExamPaperActivity.class);
            intent.putExtra("examName", "阶段测试和真题练习");  //试卷类型
            intent.putExtra("paperId", id);  //试卷的Id
            startActivity(intent);
        }
    }


    @OnClick(R.id.left_layout)
    public void onViewClicked() {
        finish();
    }
}
