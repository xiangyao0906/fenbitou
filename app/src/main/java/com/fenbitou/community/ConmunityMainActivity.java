package com.fenbitou.community;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.Exam.ExamActivity;
import com.fenbitou.adapter.ViewPagerAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.community.fragment.CommunityHotTopics;
import com.fenbitou.community.fragment.CommunitySquare;
import com.fenbitou.community.fragment.MySocialCircle;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ConmunityMainActivity extends BaseActivity {
    private static final String[] CHANNELS = new String[]{"热点", "发现", "我的"};
    private int imgs[] = {R.drawable.btn_hot, R.drawable.btn_find, R.drawable.btn_my};
    private int imgs_selected[] = {R.drawable.btn_hoted, R.drawable.btn_finded, R.drawable.btn_myed};
    private List<Fragment> fragmentList;
    private List<String> mDataList = Arrays.asList(CHANNELS);
    @BindView(R.id.main_img)
    ImageView mainImg;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private int userId;
    private CommunityHotTopics communityHotTopics;//热点
    private CommunitySquare communitySquare;//发现
    private MySocialCircle mySocialCircle;//我的
    ProgressDialog progressDialog;


    @Override
    protected int initContentView() {
        return R.layout.activity_conmunity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        getUserMessage(userId);
    }

    @Override
    public void initComponent() {
        initFragment();
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        initMagicIndicator1();
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        communityHotTopics = new CommunityHotTopics();
        communitySquare = new CommunitySquare();
        mySocialCircle = new MySocialCircle();
        fragmentList.add(communityHotTopics);
        fragmentList.add(communitySquare);
        fragmentList.add(mySocialCircle);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }

    /**
     * 获取用户信息
     */
    private void getUserMessage(int userId) {
        showLoading();
        OkHttpUtils.get().addParams("userId", String.valueOf(userId)).url(Address.MY_MESSAGE).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        try {
                            if (response.isSuccess()) {
                                cancelLoading();
                                EntityPublic entityPublic = response.getEntity().getUserExpandDto();
                                GlideUtil.loadCircleHeadImage(ConmunityMainActivity.this,
                                        Address.IMAGE_NET + entityPublic.getAvatar(),
                                        mainImg);

                            } else {
                                IToast.show(ConmunityMainActivity.this, response.getMessage());
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }

                    }
                });
    }


    @OnClick({R.id.gotoDemo1, R.id.textView3, R.id.main_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.gotoDemo1:
                finish();
//                openActivityAndCloseThis(MainActivity.class);
                break;
            case R.id.textView3:
                openActivityAndCloseThis(ExamActivity.class);
                break;
            case R.id.main_img:
                mViewPager.setCurrentItem(2);
                break;
        }

    }


    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.simple_pager_title_layout, null);
                final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.title_img);
                final TextView titleText = (TextView) customLayout.findViewById(R.id.title_text);

                titleText.setText(mDataList.get(index));
                commonPagerTitleView.setContentView(customLayout);
                titleText.setTextSize(14);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleImg.setBackgroundResource(imgs_selected[index]);
                        titleText.setTextColor(getResources().getColor(R.color.text_blue3F8));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleImg.setBackgroundResource(imgs[index]);
                        titleText.setTextColor(getResources().getColor(R.color.text_gray595));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    public void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void cancelLoading() {
        progressDialog.dismiss();
        progressDialog = null;
    }
}
