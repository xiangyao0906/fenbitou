package com.yizhilu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.sliderlibrary.SliderLayout;
import com.example.sliderlibrary.SliderTypes.BaseSliderView;
import com.example.sliderlibrary.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yizhilu.adapter.HomeGridAdapter;
import com.yizhilu.base.BaseFragment;
import com.yizhilu.base.DemoApplication;
import com.yizhilu.eduapp.IndustryInformationDetailsActivity;
import com.yizhilu.eduapp.R;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.CourseEntityCallback;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.entity.PublicListEntity;
import com.yizhilu.entity.PublicListEntityCallback;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.ValidateUtil;
import com.yizhilu.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2017/6/29.
 * 首页
 */

public class HomeFragment extends BaseFragment implements MarqueeView.OnItemClickListener {

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.home_banner_layout)
    SliderLayout homeBannerLayout;
    @BindView(R.id.recommend_grid_view)
    NoScrollGridView recommendGridView;
    @BindView(R.id.hot_grid_view)
    NoScrollGridView hotGridView;
    @BindView(R.id.small_recommend_more)
    ImageView smallRecommendMore;
    @BindView(R.id.hot_recommend_more)
    ImageView hotRecommendMore;
    @BindView(R.id.announcementText)
    MarqueeView announcementTextV;

    //公告的的集合  title
    private List<String> infos;
    private List<EntityCourse> announcementList;

    private List<EntityPublic> bannerList, recommendList, hotList;

    private HomeGridAdapter recommendAdapter, hotApater;

    private OnSelectPageListener onSelectPageListener;  //接口的对象

    @Override
    public void onItemClick(int position, TextView textView) {
        Intent announcement = new Intent();
        announcement.setClass(getActivity(), IndustryInformationDetailsActivity.class);
        announcement.putExtra("entity", announcementList.get(position));
        announcement.putExtra("title", "公告详情");
        startActivity(announcement);
    }


    public interface OnSelectPageListener {
        void onSelectPage(int position);
    }

    public void setOnSelectPageListener(OnSelectPageListener onSelectPageListener) {
        this.onSelectPageListener = onSelectPageListener;
    }


    private int announcementIndex = 0;

    private Handler mHandler = new Handler();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onSelectPageListener = (OnSelectPageListener) context;
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initComponent() {
        bannerList = new ArrayList<>();
        announcementList = new ArrayList<>();
        recommendList = new ArrayList<>();
        hotList = new ArrayList<>();
        packageManager = getActivity().getPackageManager();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) homeBannerLayout.getLayoutParams();
        layoutParams.height = (int) DensityUtil.getBannerHeight(getActivity());
        homeBannerLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        infos = new ArrayList<>();
        // 获取广告图的方法
        getBanner();
//        // 获取公告的方法
        getAnnouncement();
//        // 获取小编推荐和热门推荐的课程
        getRecommendCourse();
        // 检测升级
        detectionVersionsUpDate();
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
                                initBannerLayout();
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

    /**
     * 初始化轮播图
     */
    private void initBannerLayout() {
        for (int i = 0; i < bannerList.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.image(Address.IMAGE_NET + bannerList.get(i).getImagesUrl())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView slider) {
//                            Intent intent = new Intent();
//                            intent.setClass(getActivity(), CourseDetails96kActivity.class);
//                            intent.putExtra("courseId", bannerList.get(homeBannerLayout.getCurrentPosition()).getCourseId());
//                            startActivity(intent);
                        }
                    });
            homeBannerLayout.addSlider(textSliderView);
        }
        homeBannerLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        homeBannerLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        homeBannerLayout.setDuration(3000);
    }

    /**
     * 获取公告
     */
    private void getAnnouncement() {
        OkHttpUtils.get().url(Address.ANNOUNCEMENT).build().execute(
                new CourseEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(CourseEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                announcementList.addAll(response.getEntity());
                                infos.clear();
                                if (announcementList != null && announcementList.size() > 0) {
                                    for (int i = 0; i < announcementList.size(); i++) {
                                        infos.add(announcementList.get(i).getTitle());
                                    }
                                    announcementTextV.startWithList(infos);
                                }
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

    /**
     * 方法说明:检测版本更新的方法
     */
    private PackageManager packageManager;
    String packageName = "";

    private void detectionVersionsUpDate() {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            packageName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.get().url(Address.VERSIONUPDATE).build().execute(new PublicListEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(PublicListEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    if (response.isSuccess()) {
                        List<EntityPublic> entity = response.getEntity();
                        for (int i = 0; i < entity.size(); i++) {
                            String keyType = entity.get(i).getkType();
                            if (keyType.equals("android")) {
                                String android_url = entity.get(i).getDownloadUrl();
                                String android_v = entity.get(i).getVersionNo();
                                int b = ValidateUtil.compareVersion(packageName, android_v);
                                if (!TextUtils.isEmpty(android_v) && b == -1) {
                                    if (!TextUtils.isEmpty(android_url)) {
                                        DemoApplication.downloadContent = entity.get(i).getDepict();
                                        DemoApplication.downloadUrl = android_url;
                                        showUpdateInfo();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void showUpdateInfo() {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        updateDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    /**
     * 获取首页课程
     */
    private void getRecommendCourse() {
        OkHttpUtils.get().url(Address.RECOMMEND_COURSE).build().execute(
                new StringCallback() {
                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ILog.i(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean success = object.getBoolean("success");
                            if (success) {
                                cancelLoading();
                                JSONObject entityObject = object.getJSONObject("entity");
                                if (entityObject.toString().contains("\"1\"")) {
                                    JSONArray jsonArray = entityObject.getJSONArray("1");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            EntityPublic entity = new Gson().fromJson(jsonObject.toString(), EntityPublic.class);
                                            recommendList.add(entity);
                                        }
                                    }
                                    recommendAdapter = new HomeGridAdapter(getActivity(), recommendList);
                                    recommendGridView.setAdapter(recommendAdapter);
                                }
                                if (entityObject.toString().contains("\"2\"")) {
                                    JSONArray jsonArray = entityObject.getJSONArray("2");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            EntityPublic entity = new Gson().fromJson(jsonObject.toString(), EntityPublic.class);
                                            hotList.add(entity);
                                        }
                                    }
                                    hotApater = new HomeGridAdapter(getActivity(), hotList);
                                    hotGridView.setAdapter(hotApater);
                                }

                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());

                        }
                    }
                }
        );
    }


    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        recommendGridView.setOnItemClickListener(this);
        hotGridView.setOnItemClickListener(this);
        announcementTextV.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        switch (adapterView.getId()) {
            case R.id.recommend_grid_view: // 小编推荐
//                intent.setClass(getActivity(), CourseDetails96kActivity.class);
//                intent.putExtra("courseId", recommendList.get(i).getCourseId());
//                startActivity(intent);
                break;
            case R.id.hot_grid_view:  //热门推荐
//                intent.setClass(getActivity(), CourseDetails96kActivity.class);
//                intent.putExtra("courseId", hotList.get(i).getCourseId());
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.small_recommend_more, R.id.hot_recommend_more})
    public void onClick() {
        onSelectPageListener.onSelectPage(1);
    }


    @Override
    public void onRefresh() {
        recommendList.clear();
        hotList.clear();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerList.isEmpty()) getBanner();
                if (announcementList.isEmpty()) getAnnouncement();
                getRecommendCourse();
            }
        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bannerList.isEmpty()) homeBannerLayout.startAutoCycle();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!bannerList.isEmpty()) homeBannerLayout.stopAutoCycle();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            announcementTextV.stopFlipping();
        } else {
            announcementTextV.startFlipping();
        }
    }

}
