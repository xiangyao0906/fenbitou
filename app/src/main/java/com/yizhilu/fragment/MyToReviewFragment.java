package com.yizhilu.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yizhilu.adapter.MyToReviewAdapter;
import com.yizhilu.base.BaseFragment;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.yizhilu.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

import static com.yizhilu.eduapp.R.id.null_text;

/**
 * Created by bishuang on 2017/8/28.
 * 我参加的Fragment
 */

public class MyToReviewFragment extends BaseFragment {
    @BindView(R.id.record_list_view)
    NoScrollListView recordListView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(null_text)
    TextView nullText;
    private List<EntityCourse> myTo;
    private MyToReviewAdapter adapter;
    private int status=3,page=1,userId;

    @Override
    protected int initContentView() {
        return R.layout.fragment_my_to_review;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        myTo=new ArrayList<EntityCourse>();
    }

    @Override
    protected void initData() {
        //我参加的
        getMyTo(status,page,userId);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    /**
     * autour: Bishuang
     * date: 2017/8/29 11:57
     * 方法说明:获取我参加的列表方法
    */

    private void getMyTo(int status, final int page, int userId) {
        Map<String,String> map = new HashMap<>();
        map.put("status", String.valueOf(status));
        map.put("page.currentPage", String.valueOf(page));
        map.put("userId", String.valueOf(userId));
        showLoading(getActivity());
        OkHttpUtils.post().params(map).url(Address.LIVE_USER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        if(response.isSuccess()){
                            cancelLoading();
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            if (response.getEntity().getMyLive() != null){
                                List<EntityCourse> myLive = response.getEntity().getMyLive();
                                PageEntity pageEntity = response.getEntity().getPage();
                                if (pageEntity.getTotalPageSize() <= page) {
                                    swipeToLoadLayout.setLoadMoreEnabled(false);
                                } else {
                                    swipeToLoadLayout.setLoadMoreEnabled(true);
                                }
                                if(myLive!=null&&myLive.size()>0){
                                    for (int i = 0; i < myLive.size(); i++) {
                                        myTo.add(myLive.get(i));
                                    }
                                }
                                adapter=new MyToReviewAdapter(getActivity(),myTo);
                                recordListView.setAdapter(adapter);
                            }else{
                                swipeToLoadLayout.setVisibility(View.GONE);
                            }

                        }else{
                            IToast.show(getActivity(),response.getMessage());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        page = 1;
        myTo.clear();
        //我参加的
        getMyTo(status,page,userId);
    }

    @Override
    public void onLoadMore() {
        page++;
        //我参加的
        getMyTo(status,page,userId);
    }

}
