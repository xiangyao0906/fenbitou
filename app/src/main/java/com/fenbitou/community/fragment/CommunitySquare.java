package com.fenbitou.community.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.community.CommunitySearch;
import com.fenbitou.community.GroupDetailActivity;
import com.fenbitou.community.adapter.CommunitySquareAdapter;
import com.fenbitou.community.interfaceUtils.OnTwoItemClickListener;
import com.fenbitou.community.utils.Address;
import com.fenbitou.wantongzaixian.LoginActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.MessageCallback;
import com.fenbitou.entity.MessageEntity;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * @author xiangyao
 * @Title: CommunitySquare  发现页面
 * @Package com.yizhilu.community.fragment
 * @Description: 用来展示兴趣小组的列表
 * @date 2017/8/24 9:18
 */


public class CommunitySquare extends BaseFragment implements OnTwoItemClickListener {

    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.course_search)
    ImageView courseSearch;
    private List<EntityPublic> groupList;// 发现小组集合
    private int currentPage = 1;// 当前页
    private int userId;// 用户Id
    private CommunitySquareAdapter communitySquareAdapter;

    @Override
    protected int initContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        swipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    protected void initData() {
        groupList = new ArrayList<>();
        getFindList(currentPage, userId);
    }

    @Override
    protected void addListener() {
        swipeTarget.getItemAnimator().setChangeDuration(0);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
    }


    // 获取发现列表
    private void getFindList(final int currentPage, final int userId) {
        OkHttpUtils.get()
                .addParams("page.currentPage", String.valueOf(currentPage))
                .addParams("userId", String.valueOf(userId))
                .url(Address.FINDLIST)
                .build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    swipeToLoadLayout.setLoadingMore(false);
                    swipeToLoadLayout.setRefreshing(false);
                    PageEntity pageEntity = response.getEntity().getPage();
                    if (pageEntity.getTotalPageSize() <= currentPage) {
                        swipeToLoadLayout.setLoadMoreEnabled(false);
                    }
                    List<EntityPublic> tempGroupList = response.getEntity().getGroupList();
                    if (tempGroupList != null && tempGroupList.size() > 0) {
                        for (int i = 0; i < tempGroupList.size(); i++) {
                            groupList.add(tempGroupList.get(i));
                        }
                        if (communitySquareAdapter == null) {
                            communitySquareAdapter = new CommunitySquareAdapter(groupList, getActivity(), userId);
                            communitySquareAdapter.setOnItemClickListener(CommunitySquare.this);
                            swipeTarget.setAdapter(communitySquareAdapter);
                        } else {
                            communitySquareAdapter.setGroupList(groupList);
                            communitySquareAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });


    }

    @Override
    public void OnItemClickListener(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("GroupId", groupList.get(position).getId());
        openActivity(GroupDetailActivity.class, bundle);
    }

    @Override
    public void OnItemJoinClickListener(int position) {
        int groupId = groupList.get(position).getId();
        if (userId < 1){
            openActivity(LoginActivity.class);
        }else
            joinGroup(groupId, userId, position);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        groupList.clear();
        currentPage = 1;
        getFindList(currentPage, userId);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        getFindList(currentPage, userId);
    }


    // 加入小组
    private void joinGroup(int groupId, int userId, final int position) {
        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .addParams("userId", String.valueOf(userId))
                .url(Address.JOINGROUP).build()
                .execute(new MessageCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                groupList.get(position).setWhetherTheMembers(1);
                                int num = groupList.get(position).getMemberNum();
                                groupList.get(position).setMemberNum(num+1);
                                communitySquareAdapter.setGroupList(groupList);
                                IToast.show(getActivity(), "加入成功!");
                                communitySquareAdapter.notifyItemChanged(position);
                            } else {
                                IToast.show(getActivity(), "加入失败!");
                            }
                        } catch (Exception e) {
                        }
                    }
                });


    }


    @OnClick(R.id.course_search)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("group", true);
        openActivity(CommunitySearch.class, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
