package com.yizhilu.eduapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.yizhilu.adapter.TeacherDeatailsTeacherListAdapter;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.base.DemoApplication;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.utils.ItemClickListener;
import com.yizhilu.utils.TimeConstant;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author xiangyao
 * @Title: TeacherDeatailsActivity
 * @Package com.yizhilu.myapp
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2017/8/4 11:31
 */


public class TeacherDeatailsActivity extends BaseActivity implements ItemClickListener.OnItemClickListener {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.teacher_image)
    ImageView teacherImage;
    @BindView(R.id.teacher_name)
    TextView teacherName;
    @BindView(R.id.teacher_title)
    TextView teacherTitle;
    @BindView(R.id.open_content)
    ImageView openContent;
    @BindView(R.id.teacher_jianjie)
    TextView teacherJianjie;
    @BindView(R.id.teacher_content)
    TextView teacherContent;
    @BindView(R.id.teacher_content_more)
    TextView teacherContentMore;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.ivSuccess)
    ImageView ivSuccess;
    @BindView(R.id.tvLoadMore)
    TextView tvLoadMore;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private int teacherId;
    private int currentpage = 1;
    private List<EntityCourse> courseList;
    private TeacherDeatailsTeacherListAdapter teacherDeatailsTeacherListAdapter;
    private EntityPublic entityPublic;
    private boolean opten;

    @Override
    protected int initContentView() {
        return R.layout.activity_teacher_deatails;
    }

    @Override
    protected void initComponent() {

        Bundle bundle = getIntent().getExtras();
        teacherId = bundle.getInt("teacherId");
    }

    @Override
    protected void initData() {
        titleText.setText(getResources().getString(R.string.course_details));

        courseList = new ArrayList<>();
        findTeacherInfo(teacherId);
        findCourseByTid(teacherId, currentpage);

    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget,this));
    }


    @OnClick({R.id.back_layout, R.id.open_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.open_content:
                if (opten) {
                    opten = false;
                    openContent.setImageResource(R.drawable.close);
                    teacherContent.setVisibility(View.GONE);
                    teacherContentMore.setVisibility(View.VISIBLE);
                } else {
                    opten = true;
                    openContent.setImageResource(R.drawable.open);
                    teacherContent.setVisibility(View.VISIBLE);
                    teacherContentMore.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * @param tid teacher id
     * @return 返回类型
     * @throws
     * @Title:
     * @Description: 获取老师所属的课程
     */
    public void findCourseByTid(int tid, int page) {
        OkHttpUtils.get().addParams("teacherId", String.valueOf(tid)).addParams("page.currentPage", String.valueOf(page)).url(Address.TEACHER_COURSE).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())){
                    try {
                        if (response.isSuccess()) {
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);

                            List<EntityCourse> tempList = response.getEntity().getCourseList();
                            PageEntity pageEn = response.getEntity()
                                    .getPage();
                            if (pageEn.getTotalPageSize() <= currentpage) {
                                swipeToLoadLayout.setLoadMoreEnabled(false);
                            }
                            for (int i = 0; i < tempList.size(); i++) {
                                courseList.add(tempList.get(i));
                            }
                            if (teacherDeatailsTeacherListAdapter == null) {
                                teacherDeatailsTeacherListAdapter = new TeacherDeatailsTeacherListAdapter(TeacherDeatailsActivity.this, courseList);
                                swipeTarget.setAdapter(teacherDeatailsTeacherListAdapter);
                            } else {
                                teacherDeatailsTeacherListAdapter.setEntitieCourses(courseList);
                                teacherDeatailsTeacherListAdapter.notifyDataSetChanged();
                            }


                            textView2.setText("所有课程" + "(" + courseList.size() + ")");
                        }
                    }catch (Exception e){

                    }
                }
            }
        });

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();

        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentpage++;
                findCourseByTid(teacherId, currentpage);
            }
        }, TimeConstant.REFRSHMAXTIME);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                currentpage = 1;
                courseList.clear();
                findCourseByTid(teacherId, currentpage);

            }
        }, TimeConstant.REFRSHMAXTIME);
    }


    /**
     * @param
     * @return 返回类型
     * @throws
     * @Title: 获取教师的介绍
     * @Description: ${todo}(这里用一句话描述这个方法的作用)
     */


    public void findTeacherInfo(int tid) {

        OkHttpUtils.get().addParams("teacherId", String.valueOf(tid)).url(Address.TEACHER_DETAILS).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                String message = response.getMessage();
                boolean success = response.isSuccess();
                if (success) {
                    entityPublic = response.getEntity().getTeacher();
                    teacherName.setText(entityPublic.getName());
                    if (entityPublic.getIsStar() == 0) {
                        teacherTitle.setText("高级讲师");
                    } else {
                        teacherTitle.setText("首席讲师");
                    }
                    teacherJianjie.setText(entityPublic.getEducation());
                    teacherContent.setText(entityPublic.getCareer());
                    Layout layout = teacherContent.getLayout();
                    if (layout != null) {
                        int lineCount = layout.getLineCount();
                        if (lineCount > 4) {
                            opten = true;
                            teacherContent.setVisibility(View.VISIBLE);
                            teacherContentMore.setText(entityPublic.getCareer());
                        }
                        Glide.with(TeacherDeatailsActivity.this).load(Address.IMAGE_NET + entityPublic.getPicPath()).into(teacherImage);
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        int courseId = courseList.get(position).getId();
        Bundle bundle=new Bundle();
        bundle.putInt("courseId",courseId);

//        DemoApplication.getInstance().getActivityStack().finishActivity(CourseDetails96kActivity.getInstence());
//
//        openActivity(CourseDetails96kActivity.class,bundle);
    }

}