package com.fenbitou.wantongzaixian;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.HorizontalListViewAdapter;
import com.fenbitou.adapter.NewsDymaticAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class IndustryinformationActivity extends BaseActivity implements ItemClickListener.OnItemClickListener {
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.tabs)
    RecyclerView tabs;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.show_background)
    LinearLayout showBackground;
    @BindView(R.id.title_text)
    TextView titleText;
    private List<EntityPublic> list;
    private HorizontalListViewAdapter horizontalListViewAdapter;
    private List<EntityCourse> informationList; // 文章的实体
    private NewsDymaticAdapter newsDymaticAdapter;
    private int currentPage = 1, type = 0;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_industryinformation;
    }

    @Override
    protected void initComponent() {
        titleText.setText(getResources().getString(R.string.industry_information));
    }

    @Override
    protected void initData() {
        informationList = new ArrayList<>();
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        EntityPublic entity = new EntityPublic();
        entity.setName("全部");
        entity.setId(0);
        list.add(entity);

        findAllType(currentPage, type);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        tabs.addOnItemTouchListener(new ItemClickListener(tabs, this));
        swipeTarget.addOnItemTouchListener(new ItemClickListener(swipeTarget, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EntityCourse entityCourse = informationList.get(position);
                if (title == null || title.equals("")) {
                    title = "全部资讯";
                }
                Bundle bundle = new Bundle();
                bundle.getCharSequence("informationTitle", title);
                bundle.putSerializable("entity", entityCourse);
                openActivity(IndustryInformationDetailsActivity.class, bundle);
            }

        }));

    }


    public void findAllType(final int currentPage, final int type1) {
        OkHttpUtils.get().addParams("page.currentPage", String.valueOf(currentPage)).addParams("type", String.valueOf(type1)).url(Address.INFORMATION_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    List<EntityPublic> articleTypeList = response.getEntity().getArticleTypeList();
                    if (articleTypeList != null && articleTypeList.size() > 0) {
                        for (int i = 0; i < articleTypeList.size(); i++) {
                            list.add(articleTypeList.get(i));
                        }
                        if (horizontalListViewAdapter == null) {
                            horizontalListViewAdapter = new HorizontalListViewAdapter(list, IndustryinformationActivity.this);
                            tabs.setAdapter(horizontalListViewAdapter);
                        } else {
                            horizontalListViewAdapter.notifyDataSetChanged();
                        }
                    }


                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                findInfomationByType(type1, currentPage);
            }
        });

    }


    @Override
    public void onItemClick(View view, final int position) {

        horizontalListViewAdapter.setPosition(position);
        horizontalListViewAdapter.notifyDataSetChanged();

        swipeToLoadLayout.setOnRefreshListener(this);

        // 更新数据
        title = list.get(position).getTitle();
        informationList.clear();
        currentPage = 1;
        type = list.get(position).getId();
        newsDymaticAdapter.notifyDataSetChanged();
        showLoading(IndustryinformationActivity.this);

        findInfomationByType(type, currentPage);


    }


    public void findInfomationByType(int type, final int currentPage) {


        OkHttpUtils.get().addParams("type", String.valueOf(type)).addParams("page.currentPage", String.valueOf(currentPage)).url(Address.INFORMATION_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }


            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
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
                    newsDymaticAdapter = new NewsDymaticAdapter(IndustryinformationActivity.this, informationList);
                    swipeTarget.setAdapter(newsDymaticAdapter);
                } else {
                    newsDymaticAdapter.setInformationList(informationList);
                    newsDymaticAdapter.notifyDataSetChanged();
                }

            }
        });


    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        currentPage = 1;
        informationList.clear();


        findInfomationByType(type, currentPage);

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        findInfomationByType(type, currentPage);
    }
}
