package com.fenbitou.community;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.adapter.GroupDetailsMemberAdapter;
import com.fenbitou.community.adapter.GroupDetailsNewAdapter;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.MessageCallback;
import com.fenbitou.entity.MessageEntity;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class GroupDetailActivity extends BaseActivity {

    @BindView(R.id.group_detail_name)
    TextView groupDetailName;
    @BindView(R.id.group_detail_members)
    TextView groupDetailMembers;
    @BindView(R.id.group_detail_topic)
    TextView groupDetailTopic;
    @BindView(R.id.group_detail_introduction)
    TextView groupDetailIntroduction;
    @BindView(R.id.group_detail_avatar)
    ImageView groupDetailAvatar;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.group_detail_image_list)
    RecyclerView groupMemberview;
    @BindView(R.id.join_group)
    TextView joinGroup;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.swipe_target)
    ListView topicListview;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.add_topic)
    ImageView addTopic;
    @BindView(R.id.more_members)
    ImageView moreMembers;
    private List<EntityPublic> groupMembersList;// 小组成员头像集合
    private int groupId, userId;
    private GroupDetailsMemberAdapter groupDetailsMemberAdapter;
    private int currentPage = 1;
    private GroupDetailsNewAdapter groupDetailsAdapter;
    private List<EntityPublic> topiclist;
    private Dialog dialog;


    @Override
    protected int initContentView() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void initComponent() {
        groupMemberview.setLayoutManager(new LinearLayoutManager(GroupDetailActivity.this, RecyclerView.HORIZONTAL, false));
        titleText.setText("小组详情");
    }

    @Override
    protected void initData() {
        groupMembersList = new ArrayList<>();
        topiclist = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getInt("GroupId", 0);
        userId = (int) SharedPreferencesUtils.getParam(GroupDetailActivity.this, "userId", -1);
        getGroupDetail(groupId, userId);
        getGroupDetailTopicList(groupId, currentPage);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        topicListview.setOnItemClickListener((adapterView, view, position, l) -> {
            int setting = topiclist.get(position).getBrowseSettings();
            Bundle bundle = new Bundle();
            bundle.putInt("topicId", topiclist.get(position).getId());
            bundle.putInt("isTop", topiclist.get(position).getTop());
            bundle.putInt("isEssence", topiclist.get(position).getEssence());
            bundle.putInt("isFiery", topiclist.get(position).getFiery());
            if (setting == 3) { //后台设置只限本组员浏览
                bundle.putBoolean("listSetting", true);
            }
            openActivity(TopicDetailsActivity.class, bundle);
        });
    }

    // 获取小组详情
    private void getGroupDetail(int groupId, final int userId) {
        showLoading(GroupDetailActivity.this);

        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .addParams("userId", String.valueOf(userId))
                .url(Address.GROUPINFO)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if (response.isSuccess()) {
                            cancelLoading();
                            if (response.getEntity().getGroup().getCusId() == userId) {
                                joinGroup.setVisibility(View.GONE);
                            } else {
                                joinGroup.setVisibility(View.VISIBLE);
                                if (response.getEntity().getJobType() != 0) {
                                    joinGroup.setText("－退出");
                                } else {
                                    joinGroup.setText("＋加入");
                                }
                            }
                            GlideUtil.loadCircleImage(GroupDetailActivity.this, Address.IMAGE + response.getEntity().getGroup().getImageUrl(), groupDetailAvatar);
                            groupDetailName.setText(response.getEntity().getGroup().getShowName());
                            groupDetailMembers.setText("成员 " + response.getEntity().getGroup().getMemberNum());
                            groupDetailTopic.setText("话题 " + response.getEntity().getGroup().getTopicCounts());
                            List<EntityPublic> tempGroupMembersList = response.getEntity().getGroupMembers();
                            groupDetailIntroduction.setText(response.getEntity().getGroup().getIntroduction());
                            if (tempGroupMembersList != null && tempGroupMembersList.size() > 0) {
                                for (int i = 0; i < tempGroupMembersList.size(); i++) {
                                    groupMembersList.add(tempGroupMembersList.get(i));
                                }
                            }
                            if (groupDetailsMemberAdapter == null) {
                                groupDetailsMemberAdapter = new GroupDetailsMemberAdapter(GroupDetailActivity.this, groupMembersList, GroupDetailsMemberAdapter.HEAD_URL.AVATAR);
                                groupMemberview.setAdapter(groupDetailsMemberAdapter);
                            } else {
                                groupDetailsMemberAdapter.setGroupMembers(groupMembersList);
                                groupDetailsMemberAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoading();
    }


    // 加入小组
    private void joinGroup(int groupId, int userId) {


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
                        if (response.isSuccess()) {
                            joinGroup.setText("－退出");

                            IToast.show(GroupDetailActivity.this, "加入成功!");
                        } else {
                            IToast.show(GroupDetailActivity.this, "加入失败!");
                        }
                    }
                });

    }


    // 获取小组详情的话题列表
    private void getGroupDetailTopicList(int groupId, final int currentPage) {

        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .addParams("page.currentPage", String.valueOf(currentPage))
                .url(Address.GROUPTOPICLIST)
                .build()
                .execute(new PublicEntityCallback() {
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
                            List<EntityPublic> tempGroupTopicList = response.getEntity().getAllTopicList();
                            if (tempGroupTopicList != null){
                                topiclist.addAll(tempGroupTopicList);
                            }

                            if (groupDetailsAdapter == null) {
                                groupDetailsAdapter = new GroupDetailsNewAdapter(GroupDetailActivity.this, topiclist);
                                topicListview.setAdapter(groupDetailsAdapter);
                            } else {
                                groupDetailsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


    }


    @OnClick({R.id.back_layout, R.id.join_group, R.id.add_topic, R.id.more_members})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.join_group:
                if (userId == 0) {
                    ConstantUtils.showMsg(GroupDetailActivity.this, "您还没有登录");
                    return;
                }
                if (joinGroup.getText().equals("＋加入")) {
                    joinGroup(groupId, userId);// 加入小组
                } else {
                    exitDiaLog();
                }
                break;
            case R.id.add_topic:
                checkAddTopic(groupId, userId);
                break;
            case R.id.more_members:
                Bundle bundle = new Bundle();
                bundle.putInt("groupId", groupId);
                openActivity(GroupMenberActivity.class, bundle);
                break;
        }
    }


    // 退出小组
    private void exitGroup(int groupId, int userId) {
        OkHttpUtils.get()
                .url(Address.EXITGROUP)
                .addParams("groupId", String.valueOf(groupId))
                .addParams("userId", String.valueOf(userId))
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                joinGroup.setText("＋加入");
                                ConstantUtils.showMsg(GroupDetailActivity.this, "退出成功!");
                            } else {
                                ConstantUtils.showMsg(GroupDetailActivity.this, "退出失败！");
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        topiclist.clear();
        currentPage = 1;
        getGroupDetailTopicList(groupId, currentPage);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        getGroupDetailTopicList(groupId, currentPage);
    }

    // 发表话题前验证
    private void checkAddTopic(final int groupId, int userId) {
        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .addParams("userId", String.valueOf(userId))
                .url(Address.CHECKIFCANADDTOPIC)
                .build()
                .execute(new MessageCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MessageEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("groupId", groupId);
                                openActivity(AddTopicActivity.class, bundle);
                            } else {
                                ConstantUtils.showMsg(GroupDetailActivity.this, "你还不是该小组的成员，快加入他们发表话题吧！");
                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void exitDiaLog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_show, null);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = manager.getDefaultDisplay().getWidth();
        int scree = (width / 3) * 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = scree;
        view.setLayoutParams(layoutParams);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        TextView textView = (TextView) view.findViewById(R.id.texttitles);
        textView.setText("真的要退出小组吗 ?");
        TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
        LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
        linbtnsure.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("static-access")
            @Override
            public void onClick(View v) {
                exitGroup(groupId, userId);// 退出小组
                dialog.cancel();
            }
        });
        TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
        LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
        linbtncancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}
