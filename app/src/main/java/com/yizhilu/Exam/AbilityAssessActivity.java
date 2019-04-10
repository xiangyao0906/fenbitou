package com.yizhilu.Exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.Exam.constants.ExamAddress;
import com.yizhilu.Exam.entity.PublicEntity;
import com.yizhilu.Exam.entity.PublicEntityCallback;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author bin
 *         时间: 2016/5/26 13:41
 *         类说明:能力评估的类
 */
public class AbilityAssessActivity extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;//返回箭头
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;//返回
    @BindView(R.id.title)
    TextView title;//标题
    @BindView(R.id.set_image)
    ImageView setImage;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.left_text)
    TextView leftText;
    @BindView(R.id.right_layout_tv)
    TextView rightLayoutTv;
    @BindView(R.id.ranking)
    TextView ranking;//排名
    @BindView(R.id.total_number)
    TextView totalNumber;//总题数
    @BindView(R.id.right_number)
    TextView rightNumber;//答对题数
    @BindView(R.id.wrong_number)
    TextView wrongNumber;//答错题数
    @BindView(R.id.average_score_number)
    TextView averageScoreNumber;//平均分
    @BindView(R.id.assessment_image)
    ImageView assessmentImage;//能力评估图片
    @BindView(R.id.record_layout)
    LinearLayout recordLayout;//考试记录
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;//错题记录
    @BindView(R.id.collection_layout)
    LinearLayout collectionLayout;//收藏记录
    @BindView(R.id.assessment_text)
    TextView assessmentText;//能力评估字
    private int userId, subId;  //用户Id,专业Id
    private ProgressDialog progressDialog;
    private Intent intent;  //意图对象


    @Override
    protected int initContentView() {
        return R.layout.activity_ability_assess;
    }

    @Override
    protected void initComponent() {
        intent = new Intent();
        assessmentImage.setBackgroundResource(R.mipmap.assessment_no);
        assessmentText.setTextColor(getResources().getColor(R.color.color_main));
        subId= (int) SharedPreferencesUtils.getParam(this,"subjectId",0);
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        progressDialog = new ProgressDialog(AbilityAssessActivity.this);
        title.setText(getResources().getText(R.string.capability_assessment));
        sideMenu.setBackgroundResource(R.mipmap.return_button);
        leftLayout.setVisibility(View.VISIBLE);
        leftText.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        // 获取能力评估的方法
        getAbilityAssessData(userId, subId);
    }

    @Override
    protected void addListener() {

    }


    /**
     * 获取能力评估的方法
     */
    private void getAbilityAssessData(int userId, int subId) {
        Map<String, String> map = new HashMap<>();
        map.put("cusId", String.valueOf(userId));
        map.put("subId", String.valueOf(subId));
        showLoading(this);
        OkHttpUtils.post().params(map).url(ExamAddress.ABILITYASSESS_URL).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                try {
                    String message = response.getMessage();
                    if (response.isSuccess()) {
                        PublicEntity entity = response.getEntity();
                        ranking.setText(entity.getAverageScoreRanking() + ""); //排名
                        totalNumber.setText(entity.getQstNum() + ""); // 做题总数
                        rightNumber.setText(entity.getRightQstNum() + "");  //做对题数
                        wrongNumber.setText(entity.getErrorQstNum() + ""); // 答题量
                        averageScoreNumber.setText(entity.getAverageScore() + "");  //平均分
                    } else {
                        ConstantUtils.showMsg(
                                AbilityAssessActivity.this, message);
                    }

                } catch (Exception e) {
                }
            }
        });
    }


    @OnClick({R.id.left_layout, R.id.record_layout, R.id.error_layout, R.id.collection_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
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
            case R.id.collection_layout:
                openActivity(CollectRecordActivity.class);
                finish();
                break;
        }
    }
}
