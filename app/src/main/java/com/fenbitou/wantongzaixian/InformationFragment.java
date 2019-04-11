package com.fenbitou.wantongzaixian;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.NewsDymaticAdapter;
import com.fenbitou.base.BaseVisibleFragment;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by rss on 2017/9/11.
 * 行业资讯
 */

public class InformationFragment extends BaseVisibleFragment {


    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.show_background)
    LinearLayout showBackground;

    private List<EntityCourse> informationList = new ArrayList<>(); // 文章的实体
    private NewsDymaticAdapter newsDymaticAdapter;

    private int type,currentPage = 1;

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_information;
    }

    @Override
    protected void initComponent() {
        swipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData() {
        getDateByType();
    }

    public void getDateByType() {
        OkHttpUtils.get().addParams("type", String.valueOf(type))
                .addParams("page.currentPage", String.valueOf(currentPage))
                .url(Address.INFORMATION_LIST).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }


                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            if (response.isSuccess()){
                                List<EntityCourse> articleList = response.getEntity().getArticleList();

                                if (currentPage == response.getEntity().getPage().getTotalPageSize()) {
                                    swipeToLoadLayout.setLoadMoreEnabled(false);
                                }

                                if (articleList != null && articleList.size() > 0) {
                                    showBackground.setVisibility(View.GONE);
                                    swipeToLoadLayout.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < articleList.size(); i++) {
                                        informationList.add(articleList.get(i));
                                    }
                                } else {
                                    showBackground.setVisibility(View.VISIBLE);
                                    swipeToLoadLayout.setVisibility(View.GONE);
                                }
                                if (newsDymaticAdapter == null) {
                                    newsDymaticAdapter = new NewsDymaticAdapter(getActivity(), informationList);
                                    swipeTarget.setAdapter(newsDymaticAdapter);
                                    mHasLoadedOnce = true;
                                } else {
                                    newsDymaticAdapter.setInformationList(informationList);
                                    newsDymaticAdapter.notifyDataSetChanged();
                                    mHasLoadedOnce = true;
                                }
                            }
                        }catch (Exception e){

                        }
                    }
                });
    }


    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EntityCourse entityCourse = informationList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", entityCourse);
                openActivity(IndustryInformationDetailsActivity.class, bundle);
            }
        }));
    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        informationList.clear();
        getDateByType();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getDateByType();
    }

}
