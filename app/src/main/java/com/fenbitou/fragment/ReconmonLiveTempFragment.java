package com.fenbitou.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenbitou.adapter.HomeCourseAdapter;
import com.fenbitou.adapter.HomeReconnedLiveAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.HomeLiveItemEntity;
import com.fenbitou.utils.GridSpacingItemDecoration;
import com.fenbitou.utils.SpacesItemDecoration;
import com.fenbitou.wantongzaixian.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReconmonLiveTempFragment extends BaseFragment {

    @BindView(R.id.recommend_live_item)
    RecyclerView recommendLiveItem;

    public static ReconmonLiveTempFragment newInstance(Bundle args) {

        ReconmonLiveTempFragment fragment = new ReconmonLiveTempFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ReconmonLiveTempFragment() {


    }


    @Override
    protected int initContentView() {
        return R.layout.fragment_reconmon_live_temp;
    }

    @Override
    protected void initComponent() {



    }

    @Override
    protected void initData() {
        List<HomeLiveItemEntity>homeLiveItemEntities=new ArrayList<>();
        homeLiveItemEntities.add(new HomeLiveItemEntity());
        homeLiveItemEntities.add(new HomeLiveItemEntity());

        HomeReconnedLiveAdapter homeReconnedLiveAdapter=new HomeReconnedLiveAdapter(homeLiveItemEntities);

        recommendLiveItem.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recommendLiveItem.setNestedScrollingEnabled(false);

        recommendLiveItem.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));


        recommendLiveItem.setNestedScrollingEnabled(false);
        recommendLiveItem.setAdapter(homeReconnedLiveAdapter);



    }

    @Override
    protected void addListener() {

    }

}
