package com.fenbitou.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.MyIsLiveAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.null_text;

/**
 * Created by bishuang on 2017/8/28.
 * 正在直播的Fragment
 */

public class MyIsLiveFragment extends BaseFragment {

    @BindView(R.id.record_list_view)
    NoScrollListView recordListView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(null_text)
    TextView nullText;
    private List<EntityCourse> listLive;
    private MyIsLiveAdapter adapter;
    private int status=2,page=1,userId;


    @Override
    protected int initContentView() {
        return R.layout.fragment_my_is_live;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        listLive=new ArrayList<EntityCourse>();


    }

    @Override
    protected void initData() {
        //正在直播数据
        getMyIsLive(status,page,userId);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    private void getMyIsLive(int status, final int page, int userId) {
        Map<String,String> map = new HashMap<>();
        map.put("status", String.valueOf(status));
        map.put("page.currentPage", String.valueOf(page));
        map.put("userId", String.valueOf(userId));
        showLoading(getActivity());

        OkHttpUtils.post().params(map).url(Address.LIVE_USER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(getActivity(),"加载失败");
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
                                        listLive.add(myLive.get(i));
                                    }
                                }
                                adapter=new MyIsLiveAdapter(getActivity(),listLive);
                                recordListView.setAdapter(adapter);
                            }else{
                                swipeToLoadLayout.setVisibility(View.GONE);
                            }


                        }else{
                            IToast.show(getActivity(),response.getMessage());
                        }
                    } catch (Exception e) {
                        IToast.show(getActivity(),"加载失败");
                    }
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        page = 1;
        listLive.clear();
        //直播数据
        getMyIsLive(status,page,userId);
    }

    @Override
    public void onLoadMore() {
        page++;
        //直播数据
        getMyIsLive(status,page,userId);
    }

}
