package com.fenbitou.wantongzaixian;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.SystemAcmAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.DividerItemDecoration;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.utils.TimeConstant;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/8/1.
 * 系统消息的类
 */

public class SystemAcmActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private List<EntityPublic> letters;  //公告的集合
    private int userId, currentPage=1;
    private SystemAcmAdapter systemAcmAdapter;

    @Override
    protected int initContentView() {
        return R.layout.act_system_acm;
    }

    @Override
    protected void initComponent() {
        titleText.setText(R.string.study_acm);  //设置标题
    }

    @Override
    protected void initData() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);  //得到用户Id
        letters=new ArrayList<>();

        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        swipeTarget.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        getMessageByUserId(userId, currentPage);

    }

    private void getMessageByUserId(final int userId, final int currentPage) {
        OkHttpUtils.get().url(Address.SYSTEM_ANNOUNCEMENT).addParams("userId", String.valueOf(userId)).addParams("page.currentPage", String.valueOf(currentPage)).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                try {
                    if (response.isSuccess()) {
                        swipeToLoadLayout.setRefreshing(false);
                        swipeToLoadLayout.setLoadingMore(false);
                        PageEntity pageEntity = response.getEntity().getPage();
                        if (pageEntity.getPageSize() <= currentPage) {
                         swipeToLoadLayout.setLoadMoreEnabled(false);
                        }
                        List<EntityPublic> letterList = response.getEntity()
                                .getLetterList();
                        if (letterList != null && letterList.size() > 0) {
                            letters.addAll(letterList);
                        }

                        if(systemAcmAdapter==null){
                            systemAcmAdapter=new SystemAcmAdapter(letters,SystemAcmActivity.this);
                            swipeTarget.setAdapter(systemAcmAdapter);
                        }else {
                            systemAcmAdapter.setLetters(letters);
                            systemAcmAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                }
            }
        });


    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                getMessageByUserId(userId, currentPage);
            }
        }, TimeConstant.REFRSHMAXTIME);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        currentPage = 1;
        letters.clear();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                //联网获取系统公告的方法
                getMessageByUserId(userId, currentPage);
            }
        }, TimeConstant.REFRSHMAXTIME);
    }
}
