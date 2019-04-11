package com.fenbitou.wantongzaixian;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.MyCourseAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.CourseEntity;
import com.fenbitou.entity.CourseEntityCallback;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.ItemClickListener;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * autour: Bishuang
 * date: 2017/8/3 13:42
 * 类说明: 我的课程的类
 */

public class MyCourseActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.swipe_target)
    RecyclerView recordListView;
    @BindView(R.id.isShow_text)
    TextView isShowText;
    @BindView(R.id.myCourse_isShow)
    LinearLayout myCourseIsShow;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private MyCourseAdapter adapter;
    private List<EntityCourse> datas = new ArrayList<EntityCourse>();
    private int userId;

    @Override
    protected int initContentView() {
        return R.layout.act_my_course;
    }

    @Override
    protected void initComponent() {
        swipeToLoadLayout.setLoadMoreEnabled(false);
        swipeToLoadLayout.setRefreshEnabled(false);


        userId = (int) SharedPreferencesUtils.getParam(MyCourseActivity.this, "userId", 0);
        titleText.setText(getResources().getString(R.string.sliding_course));  //设置标题
    }

    @Override
    protected void initData() {
//      获取我的课程方法
        getCourse(userId);
    }

    @Override
    protected void addListener() {
        recordListView.addOnItemTouchListener(new ItemClickListener(recordListView, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Intent intent = new Intent(MyCourseActivity.this, CourseDetails96kActivity.class);
//                intent.putExtra("courseId", datas.get(position).getCourseId());
//                startActivity(intent);

            }

        }));

    }

    @OnClick(R.id.back_layout)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }

    /**
     * autour: Bishuang
     * date: 2017/8/3 13:57
     * 方法说明: 我的课程
     */
    private void getCourse(int userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        showLoading(MyCourseActivity.this);
        OkHttpUtils.get().params(map).url(Address.MY_BUY_COURSE).build().execute(new CourseEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                recordListView.setVisibility(View.GONE);
                myCourseIsShow.setVisibility(View.VISIBLE);
                isShowText.setText("加载失败啦");
                IToast.show(MyCourseActivity.this, "加载失败");
            }

            @Override
            public void onResponse(CourseEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        boolean success = response.isSuccess();
                        cancelLoading();
                        if (success) {
                            List<EntityCourse> entity = response.getEntity();
                            for (int i = 0; i < entity.size(); i++) {
                                datas.add(entity.get(i));
                            }
                            if (entity.size() != 0) {
                                recordListView.setLayoutManager(new LinearLayoutManager(MyCourseActivity.this));
                                recordListView.setAdapter(adapter = new MyCourseAdapter(MyCourseActivity.this, datas));
                            } else {
                                recordListView.setVisibility(View.GONE);
                                myCourseIsShow.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        recordListView.setVisibility(View.GONE);
                        myCourseIsShow.setVisibility(View.VISIBLE);
                        isShowText.setText("加载失败啦");
                        IToast.show(MyCourseActivity.this, "加载失败");
                    }
                }
            }
        });

    }
}
