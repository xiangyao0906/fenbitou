package com.fenbitou.community;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.Bean.Member;
import com.fenbitou.community.adapter.GroupMemberAdapter;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.DensityUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class GroupMenberActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rv_sticky_example)
    RecyclerView rvStickyExample;
    private List<Member> members;
    private GroupMemberAdapter groupMemberAdapter;
    private int groupId;


    @Override
    protected int initContentView() {
        return R.layout.activity_group_menber;
    }

    @Override
    protected void initComponent() {
        //回调
        GroupListener groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                //根据position获取对应的组名称
                return members.get(position).getTitle();
            }
        };
        //创建StickyDecoration，实现悬浮栏
        StickyDecoration decoration = StickyDecoration.Builder
                .init(groupListener)
                .setGroupBackground(getResources().getColor(R.color.color_main))    //背景色
                .setGroupHeight(DensityUtil.dip2px(this, 35))       //高度
                .setDivideColor(Color.parseColor("#CCCCCC"))        //分割线颜色
                .setDivideHeight(DensityUtil.dip2px(this, 1))       //分割线高度 (默认没有分割线)
                .setGroupTextColor(Color.BLACK)                     //字体颜色
                .setGroupTextSize(DensityUtil.sp2px(this, 15))      //字体大小
                .setTextSideMargin(DensityUtil.dip2px(this, 10))    // 边距   靠左时为左边距  靠右时为右边距
                .isAlignLeft(true)                                //靠右显示  （默认靠左）
                .build();
        rvStickyExample.addItemDecoration(decoration);
        rvStickyExample.setLayoutManager(new LinearLayoutManager(this));
        titleText.setText("小组成员");
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getInt("groupId");
        getMembersInfo(groupId);// 获取成员信息
    }

    @Override
    protected void addListener() {
    }


    // 获取成员信息
    private void getMembersInfo(int groupId) {

        showLoading(GroupMenberActivity.this);
        OkHttpUtils.get()
                .addParams("groupId", String.valueOf(groupId))
                .url(Address.ALLGROUPMEMBER)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();
                        try {
                            if (response.isSuccess()) {
                                members = dealData(response);
                                if (members.size() > 0) {
                                    if (groupMemberAdapter == null) {
                                        groupMemberAdapter = new GroupMemberAdapter(GroupMenberActivity.this, members);
                                    }
                                    rvStickyExample.setAdapter(groupMemberAdapter);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });


    }

    private List<Member> dealData(PublicEntity response) {
        if (members == null) {
            members = new ArrayList<>();
        }

        members.add(new Member("社长", response.getEntity().getGroupLeader().getNickname(), Address.IMAGE + response.getEntity().getGroupLeader().getAvatar()));

        List<EntityPublic> tempMembersList = response.getEntity().getGroupMembers();
        List<EntityPublic> tempLeaderList = response.getEntity().getSmallGroupLeader();

        if (tempLeaderList != null && tempLeaderList.size() > 0) {
            for (int i = 0; i < tempLeaderList.size(); i++) {
                Member member = new Member("副社长", tempLeaderList.get(i).getShowName(), Address.IMAGE + tempLeaderList.get(i).getAvatar());
                members.add(member);
            }

        }
        if (tempMembersList != null && tempMembersList.size() > 0) {
            for (int i = 0; i < tempMembersList.size(); i++) {
                Member member = new Member("成员", tempMembersList.get(i).getShowName(), Address.IMAGE + tempMembersList.get(i).getAvatar());
                members.add(member);
            }
        }
        return members;
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }
}
