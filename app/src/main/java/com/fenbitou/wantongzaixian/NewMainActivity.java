package com.fenbitou.wantongzaixian;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.base.DemoApplication;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.fragment.CommunityFragment;
import com.fenbitou.fragment.HomeFragment;
import com.fenbitou.fragment.LiveHomePageFragment;
import com.fenbitou.fragment.MineFragment;
import com.fenbitou.fragment.NewHomeFragment;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.PhoneUtils;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;

import org.zackratos.ultimatebar.UltimateBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;

public class NewMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, HomeFragment.OnSelectPageListener {

    @BindView(R.id.home_user_advanter)
    LinearLayout homeUserAdvanter;
    @BindView(R.id.home_online_school)
    TextView homeOnlineSchool;
    @BindView(R.id.home_exam)
    TextView homeExam;
    @BindView(R.id.home_community)
    TextView homeCommunity;
    @BindView(R.id.fragment_layout)
    LinearLayout fragmentLayout;
    @BindView(R.id.userHeadImage)
    ImageView userHeadImage;

    @BindViews({R.id.home_button, R.id.course_button, R.id.live_button, R.id.community_button, R.id.mine_button})
    List<RadioButton> buttonList;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private int userId; // 用户Id
    private long firstTime;//返回键第一次点击事件
    private static NewMainActivity newMainActivity;
    private String moneyWallet;
    private boolean isSaler;
    private PhoneUtils phoneUtils; // 手机的工具类


    /**
     * @author bin 修改人: 时间:2015-10-29 上午10:33:20 方法说明:获取本类对象的方法
     */
    public static NewMainActivity getIntence() {
        if (newMainActivity == null) {
            newMainActivity = new NewMainActivity();
        }
        return newMainActivity;
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
        return R.layout.activity_new_main;
    }

    @Override
    protected void initComponent() {


        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        if (userId > -1) {
            getUserMessage();
        } else {
            userHeadImage.setImageResource(R.drawable.head_bg); // 修改用户头像
        }
    }

    @Override
    protected void initData() {
        phoneUtils = new PhoneUtils(this); // 手机的工具类
        isSaler = (boolean) SharedPreferencesUtils.getParam(this, "isSaler", false);
        ILog.i(DemoApplication.getInstance().getActivityStack().activityList.size() + "");
        addUseRecord();
    }

    @Override
    protected void addListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home_button:
                setIndexSelected(0);
                break;
            case R.id.course_button:
                setIndexSelected(1);
                break;
            case R.id.live_button:
                setIndexSelected(2);
                break;
            case R.id.community_button:
                setIndexSelected(3);
                break;
            case R.id.mine_button:
                setIndexSelected(4);
                break;
            default:
                break;
        }
    }


    private Fragment[] mFragments;

    private void initFragment() {
        //首页
        NewHomeFragment homeFragment = new NewHomeFragment();
        // 课程列表
        CourseFragment courseFragment = new CourseFragment();
        //学习记录
        LiveHomePageFragment liveHomePageFragment = new LiveHomePageFragment();

        CommunityFragment communityFragment=new CommunityFragment();

        MineFragment mineFragment=new MineFragment();

        //添加到数组
        mFragments = new Fragment[]{homeFragment, courseFragment, liveHomePageFragment,communityFragment,mineFragment};

        //开启事务

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fragment_layout, homeFragment).commit();

        //默认设置为第0个
        setIndexSelected(0);
    }

    private int mIndex;

    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fragment_layout, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commitAllowingStateLoss();
        //再次赋值
        mIndex = index;

    }

    /**
     * 添加 app使用记录
     */
    private void addUseRecord() {
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


    @OnClick({R.id.home_user_advanter, R.id.home_online_school, R.id.home_exam, R.id.home_community})
    public void onViewClicked(View view) {
        if (userId == -1 || userId == 0) {
            openActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.home_user_advanter:
                IToast.show("首页头像");
                break;
            case R.id.home_online_school:
                IToast.show("首页网校");
                break;
            case R.id.home_exam:
                IToast.show("首页考试");
                break;
            case R.id.home_community:
                IToast.show("首页社区");
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserMessage() {
        showLoading(NewMainActivity.this);
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
                                GlideUtil.loadCircleHeadImage(NewMainActivity.this,
                                        Address.IMAGE_NET + entityPublic.getAvatar(),
                                        userHeadImage);

                                String userInfo = entityPublic.getUserInfo();

                            } else {
                                cancelLoading();
                                SharedPreferencesUtils.setParam(NewMainActivity.this, "userId", -1);
                            }
                        } catch (Exception e) {
                            ILog.i(e.getMessage());
                        }

                    }
                });
    }
}
