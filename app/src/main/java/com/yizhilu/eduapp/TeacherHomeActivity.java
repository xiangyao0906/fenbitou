package com.yizhilu.eduapp;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.adapter.TeacherHomeAdapter;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.entity.TeacherEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.ItemClickListener;
import com.yizhilu.utils.TimeConstant;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;


/**
 * @author xiangyao
 * @Title: TeacherHomeActivity
 * @Package com.yizhilu.myapp
 * @Description: 教师列表
 * @date 2017/8/3 16:29
 */

public class TeacherHomeActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private TeacherHomeAdapter teacherHomeAdapter;

    private List<TeacherEntity> teacherList;
    private int currentPage = 1;

    @Override
    protected int initContentView() {
        return R.layout.activity_teacher_home;
    }

    @Override
    protected void initComponent() {
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        teacherList = new ArrayList<>();
        titleText.setText(getResources().getString(R.string.course_teachers));
        findTeacher(1);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("teacherId", teacherList.get(position).getId());
                openActivity(TeacherDeatailsActivity.class, bundle);

            }

        }));
    }


    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }

    public void findTeacher(int page) {

        OkHttpUtils.get().addParams("page.currentPage", String.valueOf(page)).url(Address.TEACHER_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ILog.i("Error");
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                showLoading(TeacherHomeActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                String message = response.getMessage();
                if (response.isSuccess()) {
                    swipeToLoadLayout.setRefreshing(false);
                    swipeToLoadLayout.setLoadingMore(false);
                    PageEntity page = response.getEntity()
                            .getPage();
                    if (page.getTotalPageSize() <= currentPage) {
                        swipeToLoadLayout.setLoadMoreEnabled(false);
                    }
                    List<TeacherEntity> list = response.getEntity().getTeacherList();
                    for (int i = 0; i < list.size(); i++) {
                        teacherList.add(list.get(i));
                    }
                    if (teacherHomeAdapter == null) {
                        teacherHomeAdapter = new TeacherHomeAdapter(teacherList, TeacherHomeActivity.this);
                        swipeTarget.setAdapter(teacherHomeAdapter);
                    } else {
                        teacherHomeAdapter.notifyDataSetChanged();
                    }

                }
            }
        });

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                teacherList.clear();
                findTeacher(currentPage);
            }
        }, TimeConstant.REFRSHMAXTIME);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findTeacher(currentPage);
            }
        }, TimeConstant.REFRSHMAXTIME);
    }
}
