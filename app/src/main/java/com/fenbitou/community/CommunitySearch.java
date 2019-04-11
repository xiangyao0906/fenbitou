package com.fenbitou.community;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.adapter.HotSearAdapter;
import com.fenbitou.community.adapter.SearchResultsGroupAdapter;
import com.fenbitou.community.utils.Address;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.ItemClickListener;
import com.fenbitou.utils.KeyboardTools;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * @author xiangyao
 * @Title: CommunitySearch
 * @Package com.yizhilu.community
 * @Description: 社区搜索的页面
 * @date 2017/8/24 9:40
 */


public class CommunitySearch extends BaseActivity implements ItemClickListener.OnItemClickListener, TextWatcher, View.OnKeyListener {
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.lv_kecheng)
    NoScrollListView lvKecheng;
    @BindView(R.id.show_course_linear)
    LinearLayout showCourseLinear;
    @BindView(R.id.lv_wenzhang)
    NoScrollListView lvWenzhang;
    @BindView(R.id.show_article_linear)
    LinearLayout showArticleLinear;
    @BindView(R.id.layout_search_ing)
    LinearLayout layoutSearchIng;
    @BindView(R.id.lv_search)
    RecyclerView lvSearch;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    private String content;
    private List<EntityCourse> hotseach;
    private List<EntityCourse> courseSearch;
    private List<EntityCourse> courseSearchList;
    private List<EntityCourse> articleSearch;
    private List<EntityCourse> articleSearchList;
    private List<EntityPublic> groupList;
    private List<EntityPublic> topicList;
    private boolean groupSearsh;
    private HotSearAdapter hotSearAdapter;


    @Override
    protected int initContentView() {
        return R.layout.activity_community_search;
    }

    @Override
    protected void initComponent() {

        lvSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        groupSearsh = bundle.getBoolean("group", false);
        hotseach = new ArrayList();
        courseSearchList = new ArrayList();
        articleSearchList = new ArrayList();
        groupList = new ArrayList();
        topicList = new ArrayList();

        if (groupSearsh) {
            edSearch.setHint("小组、话题");
        }
    }


    @Override
    protected void addListener() {
        lvSearch.addOnItemTouchListener(new ItemClickListener(lvSearch, this));
        edSearch.addTextChangedListener(this);
        edSearch.setOnKeyListener(this);
        lvKecheng.setOnItemClickListener(this);
        lvWenzhang.setOnItemClickListener(this);
    }


    @OnClick({R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_sure:
                google();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoading();
    }

    @Override
    public void onItemClick(View view, int position) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("courseId", hotseach.get(position).getCourseId());
//        openActivity(CourseDetails96kActivity.class, bundle);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCancel.setVisibility(View.GONE);
        tvSure.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(edSearch.getText().toString())) {
            tvCancel.setVisibility(View.VISIBLE);
            tvSure.setVisibility(View.GONE);
            layoutSearchIng.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //Android一次按下操作定义了两个事件，ACTION_DOWN和ACTION_UP，即按下和松手两个动作。
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 先隐藏键盘
            KeyboardTools.closeKeybord(edSearch, CommunitySearch.this);

            google();
            return false;
        }
        return false;
    }

    // 社区搜索
    private void getSearchGroupResults(String content) {
        OkHttpUtils.get()
                .addParams("searchKey", content)
                .url(Address.SEARCH_GROUP_TOPI)
                .build()
                .execute(new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                List<EntityPublic> tempTopic = response.getEntity().getTopicList();
                                List<EntityPublic> tempGroup = response.getEntity().getGroupList();
                                if (tempTopic != null && tempTopic.size() > 0) {
                                    topicList.addAll(tempTopic);
                                    lvWenzhang.setAdapter(
                                            new SearchResultsGroupAdapter(CommunitySearch.this, topicList, false));
                                }
                                if (tempGroup != null && tempGroup.size() > 0) {
                                    groupList.addAll(tempGroup);
                                    lvKecheng.setAdapter(
                                            new SearchResultsGroupAdapter(CommunitySearch.this, groupList, true));
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }




    //搜索框的搜索功能
    private void google() {

        KeyboardTools.closeKeybord(edSearch, CommunitySearch.this);
        courseSearchList.clear();
        articleSearchList.clear();
        topicList.clear();
        groupList.clear();
        content = edSearch.getText().toString();
        getSearchGroupResults(content);
        layoutSearch.setVisibility(View.GONE);
        layoutSearchIng.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        super.onItemClick(adapterView, view, position, l);
        Bundle bundle;
        switch (adapterView.getId()) {
            case R.id.lv_kecheng:
                    bundle=new Bundle();
                    bundle.putInt("GroupId", groupList.get(position).getId());
                    bundle.putInt("position", position);
                    openActivity(GroupDetailActivity.class,bundle);
                break;
            case R.id.lv_wenzhang:
                    bundle=new Bundle();
                    bundle.putInt("topicId", topicList.get(position).getId());
                    bundle.putInt("isTop", topicList.get(position).getTop());
                    bundle.putInt("isEssence", topicList.get(position).getEssence());
                    bundle.putInt("isFiery", topicList.get(position).getFiery());
                    openActivity(TopicDetailsActivity.class,bundle);
                break;
        }
    }
}
