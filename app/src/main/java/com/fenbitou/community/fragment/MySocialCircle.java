package com.fenbitou.community.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.orhanobut.logger.Logger;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.community.MyGroupActivity;
import com.fenbitou.community.TopicDetailsActivity;
import com.fenbitou.community.TopicOfMyActivity;
import com.fenbitou.community.adapter.GroupDetailsMemberAdapter;
import com.fenbitou.community.adapter.HotopicAdapter;
import com.fenbitou.community.interfaceUtils.OnSingleItemClicListener;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author xiangyao
 */
public class MySocialCircle extends BaseFragment implements OnSingleItemClicListener {

    @BindView(R.id.user_avatar_img)
    ImageView userAvatarImg;
    @BindView(R.id.group_image_list)
    RecyclerView groupImageList;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.join_group_tv)
    TextView joinGroupTv;
    @BindView(R.id.all_topic_tv)
    TextView allTopicTv;
    @BindView(R.id.my_group_more)
    ImageView myGroupMore;
    @BindView(R.id.nodata)
    LinearLayout nodata;
    private List<EntityPublic> myTopicList;// 我的话题集合
    private int currentPage = 1, userId;
    private List<EntityPublic> intoGroupImageList;// 加入的小组的图片列表
    private GroupDetailsMemberAdapter groupDetailsMemberAdapter;
    private HotopicAdapter hotopicAdapter;

    @Override
    protected int initContentView() {
        return R.layout.fragment_group_my;
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void initData() {
        myTopicList = new ArrayList<>();
        intoGroupImageList = new ArrayList<>();
        swipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupImageList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);

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
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {


            myTopicList.clear();
            intoGroupImageList.clear();
            getData(userId, currentPage);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("用户不可见fragment");
    }

    /**
     * @return null
     * @throws
     * @Title:
     * @Description: 获取社区的首页数据(我的小组/话题列表)
     */
    private void getData(int userId, final int currentPage) {
        showLoading(getActivity());

        OkHttpUtils.get()
                .addParams("userId", String.valueOf(userId))
                .addParams("page.currentPage", String.valueOf(currentPage))
                .url(Address.SNSMY)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if (response.isSuccess()) {
                            cancelLoading();
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            PageEntity pageEntity = response.getEntity().getPage();
                            if (pageEntity.getTotalPageSize() <= currentPage) {
                                swipeToLoadLayout.setLoadMoreEnabled(false);
                            }
                            List<EntityPublic> tempMyTopicList = response.getEntity().getTopicList();
                            List<EntityPublic> tempIntoGroupImageList = response.getEntity().getGroupMembers();
                            if (tempMyTopicList != null && tempMyTopicList.size() > 0) {
                                for (int i = 0; i < tempMyTopicList.size(); i++) {
                                    myTopicList.add(tempMyTopicList.get(i));
                                }
                                swipeToLoadLayout.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                            } else {
                                swipeToLoadLayout.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
                            }
                            GlideUtil.loadCircleImage(getActivity(), Address.IMAGE + response.getEntity().getUserExpandDto().getAvatar(), userAvatarImg);
                            joinGroupTv.setText(String.valueOf(response.getEntity().getGroupNo()));
                            allTopicTv.setText(String.valueOf(response.getEntity().getTopicNo()));


                            if (tempIntoGroupImageList != null && tempIntoGroupImageList.size() > 0) {
                                for (int i = 0; i < tempIntoGroupImageList.size(); i++) {
                                    intoGroupImageList.add(tempIntoGroupImageList.get(i));
                                }
                                if (groupDetailsMemberAdapter == null) {
                                    groupDetailsMemberAdapter = new GroupDetailsMemberAdapter(getActivity(), intoGroupImageList, GroupDetailsMemberAdapter.HEAD_URL.IMAGEURL);
                                    groupImageList.setAdapter(groupDetailsMemberAdapter);
                                } else {
                                    groupDetailsMemberAdapter.setGroupMembers(intoGroupImageList);
                                    groupDetailsMemberAdapter.notifyDataSetChanged();
                                }


                                if (hotopicAdapter == null) {
                                    hotopicAdapter = new HotopicAdapter(getActivity(), myTopicList);

                                    /*
                                    * NineGridTestLayout 和listview 的点击事件冲突
                                    *
                                    * 故自定义接口
                                    * **/

                                    hotopicAdapter.setOnItemClickListener(MySocialCircle.this);
                                    swipeTarget.setAdapter(hotopicAdapter);
                                } else {
                                    hotopicAdapter.setMyTopicList(myTopicList);
                                    hotopicAdapter.notifyDataSetChanged();
                                }


                            }
                        }
                    }
                });


    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        getData(userId, currentPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        currentPage = 1;
        myTopicList.clear();
        intoGroupImageList.clear();
        getData(userId, currentPage);
    }

    @Override
    public void OnItemClickListener(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("topicId", myTopicList.get(position).getId());
        bundle.putInt("isTop", myTopicList.get(position).getTop());
        bundle.putInt("isEssence", myTopicList.get(position).getEssence());
        bundle.putInt("isFiery", myTopicList.get(position).getFiery());
        openActivity(TopicDetailsActivity.class, bundle);
    }


    @OnClick({R.id.my_group_more, R.id.nodata, R.id.new_topic_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_group_more:
                openActivity(MyGroupActivity.class);
                break;
            case R.id.new_topic_layout:
                openActivity(TopicOfMyActivity.class);
                break;
        }
    }


}
