package com.fenbitou.community.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.orhanobut.logger.Logger;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.community.GroupDetailActivity;
import com.fenbitou.community.TopicDetailsActivity;
import com.fenbitou.community.adapter.GroupDetailAdapter;
import com.fenbitou.community.adapter.HotGroupListAdapter;
import com.fenbitou.community.utils.Address;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.ItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class CommunityHotTopics extends BaseFragment implements ItemClickListener.OnItemClickListener {

    @BindView(R.id.hot_group_list)
    RecyclerView hotListview;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    ListView lvTarget;
    private List<EntityPublic> hotGroupList;// 热点小组集合
    private HotGroupListAdapter hotGroupListAdapter;
    private List<EntityPublic> hotTopicList;// 热点话题集合
    private int currentPage = 1;
    private GroupDetailAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected int initContentView() {
        return R.layout.comunity_hot_group;
    }

    @Override
    protected void initComponent() {
        hotListview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void initData() {
        hotTopicList = new ArrayList<>();
        hotGroupList = new ArrayList<>();
        hotGroup();
        if (getUserVisibleHint()) {
            if (adapter != null) {
                adapter.clearData();
            }
            currentPage = 1;
            getHotTopicList(currentPage);
        }
        showLoading(null);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        hotListview.addOnItemTouchListener(new ItemClickListener(hotListview, this));
        lvTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int setting = hotTopicList.get(position).getBrowseSettings();
                Bundle bundle = new Bundle();
                bundle.putInt("topicId", hotTopicList.get(position).getId());
                bundle.putInt("isTop", hotTopicList.get(position).getTop());
                bundle.putInt("isEssence", hotTopicList.get(position).getEssence());
                bundle.putInt("isFiery", hotTopicList.get(position).getFiery());
                if (setting == 3) { //后台设置只限本组员浏览
                    bundle.putBoolean("listSetting", true);
                }
                openActivity(TopicDetailsActivity.class, bundle);
            }
        });
    }

    // 热门小组
    private void hotGroup() {
        OkHttpUtils.get()
                .url(Address.HOTGROUP)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                List<EntityPublic> tempHotGroupList = response.getEntity().getHotGroupList();
                                if (tempHotGroupList != null && tempHotGroupList.size() > 0) {
                                    for (int i = 0; i < tempHotGroupList.size(); i++) {
                                        hotGroupList.add(tempHotGroupList.get(i));
                                    }
                                    if (hotGroupListAdapter == null) {
                                        hotGroupListAdapter = new HotGroupListAdapter(hotGroupList, getActivity());
                                        hotListview.setAdapter(hotGroupListAdapter);
                                    } else {
                                        hotGroupListAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        } catch (Exception e) {
                            Logger.i(e.getMessage());
                        }
                    }
                });
    }


    // 获取热点话题列表
    private void getHotTopicList(final int currentPage) {

        OkHttpUtils.get()
                .addParams("page.currentPage", String.valueOf(currentPage))
                .url(Address.HOTTOPICLIST)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                cancelLoading();
                                swipeToLoadLayout.setLoadingMore(false);
                                swipeToLoadLayout.setRefreshing(false);
                                PageEntity pageEntity = response.getEntity().getPage();
                                if (pageEntity.getTotalPageSize() <= currentPage) {
                                    swipeToLoadLayout.setLoadMoreEnabled(false);
                                }
                                List<EntityPublic> tempHotTopicList = response.getEntity().getTopics();
                                if (tempHotTopicList != null && tempHotTopicList.size() > 0) {
                                    hotTopicList.addAll(tempHotTopicList);
                                }
                                if (adapter == null) {
                                    adapter = new GroupDetailAdapter(getContext(), hotTopicList);
                                    lvTarget.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        getHotTopicList(currentPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        adapter.clearData();
        currentPage = 1;
        getHotTopicList(currentPage);

    }

    @Override
    public void onItemClick(View view, int position) {
        int groupId = hotGroupList.get(position).getId();
        Bundle bundle = new Bundle();
        bundle.putInt("GroupId", groupId);
        openActivity(GroupDetailActivity.class, bundle);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        } else if (!isVisibleToUser) {
            onPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("用户不可见fragment");
    }

}
