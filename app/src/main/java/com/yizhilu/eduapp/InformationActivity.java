package com.yizhilu.eduapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.yizhilu.adapter.ViewPagerAdapter;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.fragment.InformationFragment;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by rss on 2017/9/11.
 * 行业资讯
 */

public class InformationActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabTitle = new ArrayList<>();

    @Override
    protected int initContentView() {
        return R.layout.activity_information;
    }

    @Override
    protected void initComponent() {
        titleText.setText(getResources().getString(R.string.industry_information));
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mViewPagerAdapter);
    }

    @Override
    protected void initData() {
        getTabTitle();
    }

    private void getTabTitle() {
        OkHttpUtils.get().addParams("type", "0").url(Address.INFORMATION_LIST).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        if (response.isSuccess()) {
                            List<EntityPublic> articleTypeList = response.getEntity().getArticleTypeList();
                            if (articleTypeList != null) {

                                for (int i = 0; i < articleTypeList.size() + 1; i++) {
                                    InformationFragment informationFragment = new InformationFragment();
                                    if (i == 0) {
                                        informationFragment.setType(0);
                                        informationFragment.getDateByType();
                                    } else {
                                        informationFragment.setType(articleTypeList.get(i - 1).getId());
                                    }
                                    fragmentList.add(informationFragment);
                                }
                                mViewPagerAdapter.notifyDataSetChanged();
                                tabTitle.add("全部");
                                for (int i = 0; i < articleTypeList.size(); i++) {
                                    tabTitle.add(articleTypeList.get(i).getName());
                                }
                                initMagicIndicator();

                            }
                        }
                    }
                });
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#f9f9f9"));//背景色
        CommonNavigator commonNavigator = new CommonNavigator(InformationActivity.this);
        commonNavigator.setScrollPivotX(0.25f);
        if (tabTitle.size() <= 6) commonNavigator.setAdjustMode(true);//充满屏幕宽度
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabTitle.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(tabTitle.get(index));
                simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.tabText));//未选中颜色
                simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_main));//选中的颜色
                simplePagerTitleView.setTextSize(14);//字体大小
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);//等同标题长度
                indicator.setYOffset(UIUtil.dip2px(context, 2));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));//指示器宽度
                indicator.setColors(context.getResources().getColor(R.color.color_main));//指示器颜色
                //indicator.setStartInterpolator(new AccelerateInterpolator());//开始动画
                //indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));//结束动画
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    protected void addListener() {

    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        this.finish();
    }
}
