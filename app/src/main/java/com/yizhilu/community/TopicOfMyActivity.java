package com.yizhilu.community;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.community.adapter.TopicOfMyAdAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.ItemClickListener;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author xiangyao
 */
public class TopicOfMyActivity extends BaseActivity implements ItemClickListener.OnItemClickListener {

    int userId = -1;

    int currentPage = 1;

    TopicOfMyAdAdapter topicOfMyAdapter = null;

    ArrayList<EntityPublic> myTopicList = null;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.activity_topic_of_my;
    }

    /**
     * 初始化组件
     */
    @Override
    protected void initComponent() {

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        myTopicList = new ArrayList<EntityPublic>();
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        getTopicOfMy(userId, currentPage);
        titleText.setText("我的话题");
    }

    /**
     * 添加监听
     */
    @Override
    protected void addListener() {
        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget, this));
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }

    /**
     * 点击时回调
     *
     * @param view     点击的View
     * @param position 点击的位置
     */
    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("topicId", myTopicList.get(position).getId());
        bundle.putInt("isTop", myTopicList.get(position).getTop());
        bundle.putInt("isEssence", myTopicList.get(position).getEssence());
        bundle.putSerializable("isFiery", myTopicList.get(position));
        openActivity(TopicDetailsActivity.class, bundle);
    }

    /*
 * 获取我的话题
 * **/
    public void getTopicOfMy(int userId, final int currentPage) {


        showLoading(this);

        OkHttpUtils.get()
                .addParams("userId", String.valueOf(userId))
                .addParams("currentPage", String.valueOf(currentPage))
                .url(Address.MYTOPICLIST)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        cancelLoading();
                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();

                        swipeToLoadLayout.setRefreshing(false);
                        swipeToLoadLayout.setLoadingMore(false);

                        if (response.isSuccess()) {
                            PageEntity pageEntity = response.getEntity().getPage();
                            if (pageEntity.getTotalPageSize() <= currentPage) {
                                swipeToLoadLayout.setLoadMoreEnabled(false);
                            }
                            List<EntityPublic> tempMyTopicList = response.getEntity().getTopicList();
                            if (tempMyTopicList != null && tempMyTopicList.size() > 0) {

                                for (int i = 0; i < tempMyTopicList.size(); i++) {
                                    myTopicList.add(tempMyTopicList.get(i));
                                }
                            }
                            if (topicOfMyAdapter == null) {
                                topicOfMyAdapter = new TopicOfMyAdAdapter(TopicOfMyActivity.this,
                                        myTopicList);
                                swipeTarget.setAdapter(topicOfMyAdapter);

                            } else {
                                topicOfMyAdapter.notifyDataSetChanged();
                            }

                        }

                    }

                });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        myTopicList.clear();
        currentPage = 1;
        getTopicOfMy(userId, currentPage);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        getTopicOfMy(userId, currentPage);
    }
}
