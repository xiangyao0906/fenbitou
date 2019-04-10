package com.yizhilu.eduapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yizhilu.Exam.ExamActivity;
import com.yizhilu.base.BaseActivity;
import com.yizhilu.base.DemoApplication;
import com.yizhilu.community.ConmunityMainActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.fragment.HomeFragment;
import com.yizhilu.fragment.RecordFragment;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.utils.ILog;
import com.yizhilu.utils.IToast;
import com.yizhilu.utils.PhoneUtils;
import com.yizhilu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.zackratos.ultimatebar.UltimateBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, HomeFragment.OnSelectPageListener {

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindViews({R.id.home_button, R.id.course_button, R.id.live_button, R.id.record_button, R.id.download_button})
    List<RadioButton> buttonList;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.user_head_image)
    ImageView userHeadImage;
    @BindView(R.id.my_order_layout)
    LinearLayout myOrderLayout;
    @BindView(R.id.personal_resume_layout)
    LinearLayout personalResumeLayout;
    @BindView(R.id.my_course_layout)
    LinearLayout myCourseLayout;
    @BindView(R.id.my_live_layout)
    LinearLayout myLiveLayout;
    @BindView(R.id.system_msg_layout)
    LinearLayout systemMsgLayout;
    @BindView(R.id.industry_information_layout)
    LinearLayout industryInformationLayout;
    @BindView(R.id.course_teacher_layout)
    LinearLayout courseTeacherLayout;
    @BindView(R.id.my_collection_layout)
    LinearLayout myCollectionLayout;
    @BindView(R.id.discount_coupon_layout)
    LinearLayout discountCouponLayout;
    @BindView(R.id.my_account_layout)
    LinearLayout myAccountLayout;
    @BindView(R.id.opinion_feedback)
    LinearLayout opinionFeedback;
    @BindView(R.id.system_set_linear)
    LinearLayout systemSetLinear;
    @BindView(R.id.drawer_icon)
    ImageView drawerIcon;
    @BindView(R.id.user_name_text)
    TextView userNameText;
    @BindView(R.id.user_signature)
    TextView userSignature;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.my_account_text)
    TextView myAccountText;
    @BindView(R.id.gallery_tv)
    TextView galleryTv;
    @BindView(R.id.community_tv)
    TextView communityTv;

    private int userId; // 用户Id
    private long firstTime;//返回键第一次点击事件
    private static MainActivity mainActivity;
    private String moneyWallet;
    private boolean isSaler;
    private PhoneUtils phoneUtils; // 手机的工具类

    /**
     * @author bin 修改人: 时间:2015-10-29 上午10:33:20 方法说明:获取本类对象的方法
     */
    public static MainActivity getIntence() {
        if (mainActivity == null) {
            mainActivity = new MainActivity();
        }
        return mainActivity;
    }

    /**
     * homeFragment点击更多 接口回调
     *
     * @param position
     */
    @Override
    public void onSelectPage(int position) {
        buttonList.get(position).setChecked(true);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComponent() {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.black));
        initFragment();
    }

    @Override
    protected void initData() {
        phoneUtils = new PhoneUtils(this); // 手机的工具类
        isSaler = (boolean) SharedPreferencesUtils.getParam(this, "isSaler", false);
        ILog.i(DemoApplication.getInstance().getActivityStack().activityList.size() + "");
        addUseRecord();
    }

    @Override
    protected void onResume() {
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        if (userId > -1) {
            getUserMessage();
        } else {
            userHeadImage.setImageResource(R.drawable.head_bg); // 修改用户头像
            drawerIcon.setImageResource(R.drawable.head_bg); // 修改用户头像
            userNameText.setText("未登陆");
        }
        super.onResume();
    }

    /**
     * 获取用户信息
     */
    private void getUserMessage() {
        showLoading(MainActivity.this);
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
                                GlideUtil.loadCircleHeadImage(MainActivity.this,
                                        Address.IMAGE_NET + entityPublic.getAvatar(),
                                        userHeadImage);
                                GlideUtil.loadCircleHeadImage(MainActivity.this,
                                        Address.IMAGE_NET + entityPublic.getAvatar(),
                                        drawerIcon);
                                String showname = entityPublic.getShowname();
                                DemoApplication.nikeName = showname;
                                String email = entityPublic.getEmail();
                                String mobile = entityPublic.getMobile();
                                String userInfo = entityPublic.getUserInfo();
                                if (!TextUtils.isEmpty(userInfo)) {
                                    userSignature.setText(userInfo);
                                }
                                if (!TextUtils.isEmpty(showname)) {
                                    userNameText.setText(showname);
                                } else if (!TextUtils.isEmpty(email)) {
                                    userNameText.setText(email);
                                } else {
                                    userNameText.setText(mobile);
                                }
                                String balance = response.getEntity().getBalance();
                                if (TextUtils.isEmpty(balance)) {
                                    myAccountText.setText("余额 : ￥ 0.00");
                                    moneyWallet = "余额 : ￥ 0.00";
                                } else {
                                    myAccountText.setText("余额 : ￥ " + balance);
                                    moneyWallet = "余额 : ￥ " + balance;
                                }
                            } else {
                                cancelLoading();
                                SharedPreferencesUtils.setParam(MainActivity.this, "userId", -1);
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }

                    }
                });
    }

    @Override
    protected void addListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        try {
            switch (checkedId) {
                case R.id.home_button: //首页
//                    controller.showFragment(0);
                    titleText.setText("首页");
                    setIndexSelected(0);
                    break;
                case R.id.course_button: //发现
//                    controller.showFragment(1);
                    titleText.setText("发现");
                    setIndexSelected(1);
                    break;
                case R.id.live_button: //直播
//                    controller.showFragment(2);
//                    titleText.setText("直播");
//                    setIndexSelected(2);
                    break;
                case R.id.record_button: //记录
//                    controller.showFragment(3);
                    titleText.setText("记录");
                    setIndexSelected(2);
                    break;
                case R.id.download_button: //下载
                    titleText.setText("下载");
//                    setIndexSelected(4);
                    break;
            }
        } catch (Exception e) {
        }
    }

    private Fragment[] mFragments;
    private void initFragment() {
        //首页
        HomeFragment homeFragment = new HomeFragment();
        // 课程列表
        CourseFragment courseFragment = new CourseFragment();
        //学习记录
        RecordFragment recordFragment = new RecordFragment();

        //添加到数组
        mFragments = new Fragment[]{homeFragment, courseFragment, recordFragment};

        //开启事务

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fragment_layout, homeFragment).commit();

        //默认设置为第0个
        setIndexSelected(0);
    }
    private int mIndex;
    private void setIndexSelected(int index) {

        if(mIndex==index){
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft  = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if(!mFragments[index].isAdded()){
            ft.add(R.id.fragment_layout,mFragments[index]).show(mFragments[index]);
        }else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex=index;

    }

    /**
     * 添加 app使用记录
     */
    private void addUseRecord(){
        Map<String, String> map = new HashMap<>();
        map.put("websiteUse.ip", phoneUtils.getIPAddress(this));
        map.put("websiteUse.brand", phoneUtils.getPhoneBrand());
        map.put("websiteUse.modelNumber", phoneUtils.getPhoneModel());
        map.put("websiteUse.size", phoneUtils.getPhoneSize());
//        map.put("websiteLogin.userId", userId + "");
        map.put("websiteUse.type", "android");
        OkHttpUtils.post().params(map).url(Address.ADD_APPLY_RECORD).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    IToast.show(this, "再按一次退出程序");
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    if (userId != 0) {
                        // 添加使用记录的方法
                        // addApplyRecord(userId, 2);
                    }
                    //退出下载服务
                    DemoApplication.getInstance().getActivityStack().AppExit();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    //  修改登陆状态
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getModifyLoginState(String msg) {
//        if (msg.equals("exitApp")) {
//            getSharedPreferences("userId",MODE_PRIVATE).edit().putInt("userId", -1).commit();
//            userId = 0;
//            userHeadImage.setImageResource(R.drawable.head_bg); // 修改用户头像
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.drawer_icon, R.id.my_order_layout, R.id.personal_resume_layout, R.id.my_course_layout, R.id.my_live_layout, R.id.system_msg_layout, R.id.industry_information_layout, R.id.course_teacher_layout, R.id.my_collection_layout, R.id.discount_coupon_layout, R.id.my_account_layout, R.id.opinion_feedback, R.id.system_set_linear,R.id.gallery_tv, R.id.community_tv,R.id.user_head_image})
    public void onViewClicked(View view) {
        if (userId == -1 || userId == 0) {
            openActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.drawer_icon:
                openActivity(PersonalInformationActivity.class);
                break;
            case R.id.my_order_layout:
//                IToast.show(this, "我的订单");
                openActivity(MyOrderActivity.class);
                break;
            case R.id.personal_resume_layout:
                openActivity(StudyStatisticsActivity.class);
                break;
            case R.id.my_course_layout:
                openActivity(MyCourseActivity.class);
                break;
            case R.id.my_live_layout:
//                IToast.show(this, "我的直播");
//                openActivity(MyLiveActivity.class);
                break;
            case R.id.system_msg_layout:
                openActivity(SystemAcmActivity.class);
                break;
            case R.id.industry_information_layout:
                openActivity(InformationActivity.class);
                break;
            case R.id.course_teacher_layout:
                openActivity(TeacherHomeActivity.class);
                break;
            case R.id.my_collection_layout:

                openActivity(MyCollectionActivity.class);
                break;
            case R.id.discount_coupon_layout:
                openActivity(DiscontActivity.class);
                break;
            case R.id.my_account_layout:

                if (isSaler) {
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("moneyWallet", moneyWallet);

                    openActivity(MyWalletActivity.class, bundle);
                } else {
                    openActivity(PersonalAcountActivity.class);
                }


                break;
            case R.id.opinion_feedback:
                openActivity(OpinionFeedbackActivity.class);

                break;
            case R.id.system_set_linear:
                openActivity(SystemSettingActivity.class);
                break;
            case R.id.gallery_tv:
                // TODO: 2017/8/22 0022 跳转考试
                openActivity(ExamActivity.class);
                break;
            case R.id.community_tv:
                // TODO: 2017/8/22 0022 跳转社区
                openActivity(ConmunityMainActivity.class);
                break;
            case R.id.user_head_image:
                drawerlayout.openDrawer(Gravity.LEFT);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerlayout.closeDrawer(Gravity.LEFT);

    }

}
