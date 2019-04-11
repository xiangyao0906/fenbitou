package com.fenbitou.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.adapter.TeacherListAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.entity.TeacherEntity;
import com.fenbitou.wantongzaixian.ConfirmOrderActivity;
import com.fenbitou.wantongzaixian.LoginActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.wantongzaixian.TeacherDeatailsActivity;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2017/7/3.
 * 课程介绍
 */

public class CourseIntroduceFragment extends BaseFragment {
    @BindView(R.id.course_name)
    TextView courseName;
    @BindView(R.id.course_price)
    TextView course_price;
    @BindView(R.id.course_price_two)
    TextView course_price_two;
    @BindView(R.id.buy_btn)
    Button buyBtn;
    @BindView(R.id.free_image)
    ImageView freeImage;
    @BindView(R.id.pull_down_image)
    ImageView pullDownImage;
    @BindView(R.id.web_view_layout)
    LinearLayout webViewLayout;
    @BindView(R.id.course_webView)
    WebView courseWebView;
    @BindView(R.id.teacher_list_view)
    NoScrollListView teacherListView;

    private List<TeacherEntity> teacherList;
    private TeacherListAdapter teacherListAdapter;

    private int userId, courseId;
    private EntityPublic entityPublic;

//    private CourseDetails96kActivity courseDetailsActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        courseDetailsActivity = (CourseDetails96kActivity) context;
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_course_introduce;
    }

    @Override
    protected void initComponent() {
        course_price_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 添加中间横线
        teacherList = new ArrayList<>();
        teacherListAdapter = new TeacherListAdapter(getActivity(), teacherList);
        teacherListView.setAdapter(teacherListAdapter);
        courseWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        courseWebView.getSettings().setLoadWithOverviewMode(true);
        courseWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initData() {
        try{
            entityPublic = (EntityPublic) getArguments().getSerializable("entity");
            courseId = entityPublic.getCourse().getId();
        }catch (Exception e){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        if (courseId != 0){
            getCourseDetails();
        }
    }

    /**
     * 课程详情
     */
    private void getCourseDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", String.valueOf(courseId));
        map.put("userId", String.valueOf(userId));
        OkHttpUtils.get().params(map).url(Address.COURSE_DETAILS).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                entityPublic = response.getEntity();
                                if (entityPublic == null) return;
                                courseId = entityPublic.getCourse().getId();
                                courseName.setText(entityPublic.getCourse().getName());
                                if (entityPublic.getCourse().getIsPay() == 0) { // 免费
                                    buyBtn.setVisibility(View.GONE);
                                    freeImage.setVisibility(View.VISIBLE);
                                } else if (entityPublic.getCourse().getIsPay() == 1) { // 付费
                                    buyBtn.setVisibility(View.VISIBLE);
                                    if (entityPublic.isok()) { // 已经购买
                                        buyBtn.setBackgroundResource(R.drawable.course_witch_bg);
                                        buyBtn.setText(getResources().getString(R.string.liji_look));
                                    }
                                }
                                course_price.setText(entityPublic.getCourse().getCurrentprice() + "");
                                course_price_two.setText(entityPublic.getCourse().getSourceprice() + "");
                                courseWebView.loadUrl(Address.COURSE_CONTENT + courseId + ".json");
                                if (entityPublic.getCourse().getTeacherList() != null) {
                                    teacherList.addAll(entityPublic.getCourse().getTeacherList());
                                    teacherListAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

    @Override
    protected void addListener() {
        teacherListView.setOnItemClickListener(this);
    }

    @OnClick(R.id.buy_btn)
    public void buyCourse() {
        String purchase = buyBtn.getText().toString();
        if (getResources().getString(R.string.liji_look).equals(purchase)) {
//            courseDetailsActivity.isPlayImmediately = true;
//            courseDetailsActivity.verificationPlayVideo(0);
        } else {
            if (userId == -1) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                intent.putExtra("publicEntity", entityPublic);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.pull_down_image)
    public void showOrHideWebView() {
        if (webViewLayout.getVisibility() == View.VISIBLE) {
            webViewLayout.setVisibility(View.GONE);
            pullDownImage.setBackgroundResource(R.drawable.close);
        } else {
            webViewLayout.setVisibility(View.VISIBLE);
            pullDownImage.setBackgroundResource(R.drawable.open);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int teacherId = teacherList.get(i).getId();
        Bundle bundle = new Bundle();
        bundle.putInt("teacherId", teacherId);
        openActivity(TeacherDeatailsActivity.class, bundle);
    }
}
