package com.fenbitou.wantongzaixian;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.MyAccontAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityAccList;
import com.fenbitou.entity.EntityUserAccount;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by xiangyao on 2017/8/16.
 */

public class PersonalAcountActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.tv_accountcash)
    TextView tvAccountcash;
    @BindView(R.id.show_img)
    LinearLayout showImg;
    private List<EntityAccList> accounList;// 账户List
    private List<EntityAccList> myAccLists;
    private MyAccontAdapter myAccontAdapter;
    private int userId, currentPage=1;


    @Override
    protected int initContentView() {
        return R.layout.activity_personal_acount;
    }

    @Override
    protected void initComponent() {
        userId= (int) SharedPreferencesUtils.getParam(PersonalAcountActivity.this,"userId",-1);
        titleText.setText(getResources().getString(R.string.personal_account));
        swipeTarget.setLayoutManager(new LinearLayoutManager(PersonalAcountActivity.this));

    }

    @Override
    protected void initData() {
        myAccLists = new ArrayList<>();
        getAccontMessage(userId,currentPage);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        this.finish();
    }


    /**
     * 方法说明:获取账户信息
     */
    private void getAccontMessage(final int userId, final int currentPage) {
        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).addParams("page.currentPage", String.valueOf(currentPage)).url(Address.USER_MESSAGE).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    swipeToLoadLayout.setRefreshing(false);
                    swipeToLoadLayout.setLoadingMore(false);
                    PageEntity page = response.getEntity()
                            .getPage();
                    if (page.getTotalPageSize() <= currentPage) {
                        swipeToLoadLayout.setLoadMoreEnabled(false);
                    }
                    EntityUserAccount cashAccount = response
                            .getEntity().getUserAccount();
                    tvAccountcash.setText(cashAccount.getBalance() + "");
                    accounList = response.getEntity()
                            .getAccList();
                    if (accounList != null && accounList.size() > 0) {
                        for (int i = 0; i < accounList.size(); i++) {
                            myAccLists.add(accounList.get(i));
                        }

                        if (myAccontAdapter == null) {
                            myAccontAdapter = new MyAccontAdapter(PersonalAcountActivity.this, myAccLists);
                            swipeTarget.setAdapter(myAccontAdapter);
                        } else {
                            myAccontAdapter.setAccouList(myAccLists);
                            myAccontAdapter.notifyDataSetChanged();
                        }

                    } else {
                        swipeToLoadLayout.setVisibility(View.GONE);
                        showImg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        currentPage++;
        //获取账户信息
        getAccontMessage(userId, currentPage);

    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        currentPage = 1;
        myAccLists.clear();
        //获取账户信息
        getAccontMessage(userId, currentPage);
    }
}
