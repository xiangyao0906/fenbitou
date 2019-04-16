package com.fenbitou.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenbitou.adapter.HomeCourseAdapter;
import com.fenbitou.adapter.HomeTabsAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.HomeTabEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.BannerImageLoader;
import com.fenbitou.utils.GallerySnapHelper;
import com.fenbitou.utils.GridSpacingItemDecoration;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SpacesItemDecoration;
import com.fenbitou.wantongzaixian.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewHomeFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.youthBanner)
    Banner youthBanner;
    @BindView(R.id.tabs)
    RecyclerView tabs;
    @BindView(R.id.home_course_recycleview)
            RecyclerView homeCourseRecycleview;
    HomeTabsAdapter homeTabsAdapter;
    private List<EntityPublic> bannerList;

    private List<String> imageUrls;

    private List<HomeTabEntity> homeTabEntities;


    private int res[] = new int[]{R.drawable.shouye_fenlei1, R.drawable.shouye_fenlei2, R.drawable.shouye_fenlei3
            , R.drawable.shouye_fenlei4, R.drawable.shouye_fenlei5, R.drawable.shouye_fenlei6, R.drawable.shouye_fenlei7, R.drawable.shouye_fenlei8};

    private String tabTitle[] = new String[]{"考研数学", "考研英语", "考研政治", "管理类联考", "心理学", "西医综合", "经济学", "更多课程"};

    public NewHomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected int initContentView() {
        return R.layout.fragment_new_home;
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void initData() {
        homeTabEntities = new ArrayList<>();
        imageUrls = new ArrayList<>();
        bannerList = new ArrayList<>();
        // 获取广告图的方法
        getBanner();

        initTabs();
    }

    private void initTabs() {

        for (int i = 0; i < res.length; i++) {
            HomeTabEntity homeTabEntity = new HomeTabEntity(res[i], tabTitle[i]);
            homeTabEntities.add(homeTabEntity);

        }


        homeTabsAdapter = new HomeTabsAdapter(homeTabEntities);

        tabs.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        GallerySnapHelper mGallerySnapHelper = new GallerySnapHelper();
        mGallerySnapHelper.attachToRecyclerView(tabs);
        tabs.setAdapter(homeTabsAdapter);

        homeCourseRecycleview.setLayoutManager(new GridLayoutManager(getActivity(),2));

        HomeCourseAdapter homeCourseAdapter=new HomeCourseAdapter(getActivity());

        homeCourseRecycleview.addItemDecoration(new GridSpacingItemDecoration(2,20,false));

        homeCourseRecycleview.setAdapter(homeCourseAdapter);

    }

    @Override
    protected void addListener() {

        homeTabsAdapter.setOnItemChildClickListener(this);

    }

    /**
     * 获取轮播图
     */
    private void getBanner() {
        OkHttpUtils.get().url(Address.BANNER).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                bannerList.addAll(response.getEntity().getIndexCenterBanner());
                                initBanner();
                            } else {
                                IToast.show(getActivity(), response.getMessage());
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }
                    }
                }
        );

    }

    private void initBanner() {

        for (int i = 0; i < bannerList.size(); i++) {

            imageUrls.add(bannerList.get(i).getImagesUrl());


        }

        youthBanner.setImages(imageUrls).setImageLoader(new BannerImageLoader()).setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.RIGHT).setDelayTime(4000).start();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        IToast.show("当前位置是"+position);
    }
}
