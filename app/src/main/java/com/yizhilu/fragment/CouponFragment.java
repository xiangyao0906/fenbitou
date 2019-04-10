package com.yizhilu.fragment;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.PublicEntityCallback;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yizhilu.eduapp.R.id.available_text;
import static com.yizhilu.eduapp.R.id.past_text;
import static com.yizhilu.eduapp.R.id.used_text;
import static com.yizhilu.eduapp.R.id.used_view;


/**
 * @author xiangyao
 * @Title: CouponFragment
 * @Package com.yizhilu.fragment
 * @Description: 优惠券
 * @date 2017/8/14 14:05
 */
public class CouponFragment extends BaseFragment {

    @BindView(available_text)
    TextView availableText;
    @BindView(R.id.available_view)
    View availableView;
    @BindView(R.id.available_coupon_layout)
    LinearLayout availableCouponLayout;
    @BindView(used_text)
    TextView usedText;
    @BindView(used_view)
    View usedView;
    @BindView(R.id.used_coupon_layout)
    LinearLayout usedCouponLayout;
    @BindView(past_text)
    TextView pastText;
    @BindView(R.id.past_view)
    View pastView;
    @BindView(R.id.past_coupon_layout)
    LinearLayout pastCouponLayout;
    @BindView(R.id.coupon_rel)
    RelativeLayout availablePastCouponLayout;
    private int userId,page = 1;  //用户Id,页数
    private android.support.v4.app.FragmentManager fm;
    private AvailableCouponFragment availableFragment;  //可用代金券
    private UsedCouponFragment usedFragment;  //已使用代金券
    private PastCouponFragment pastFragment;  //不可用代金券
    private boolean isAvailable,isUsed,isPast;  //是否可用

    @Override
    protected int initContentView() {
        return R.layout.fragment_coupon;
    }

    @Override
    protected void initComponent() {
        availableFragment = new AvailableCouponFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.coupon_rel,availableFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void initData() {
        //联网获取可用优惠券的方法
        getAvailableCouponData(userId,page);
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.available_coupon_layout, R.id.used_coupon_layout, R.id.past_coupon_layout})
    public void onViewClicked(View view) {
        // 开启一个Fragment的事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 隐藏掉所有的fragment
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.available_coupon_layout: //可使用的
                isAvailable = true;
                isUsed = false;
                isPast = false;
                //设置可用的信息
                setAvailableOrPast();
                if (availableFragment == null){
                    availableFragment = new AvailableCouponFragment();
                    transaction.add(R.id.coupon_rel,availableFragment);
                }else{
                //已经创建过了
                transaction.show(availableFragment);
                }
                break;
            case R.id.used_coupon_layout: //已使用的
                isAvailable = false;
                isUsed = true;
                isPast = false;
                //设置可用的信息
                setAvailableOrPast();
                if (usedFragment == null){
                    usedFragment = new UsedCouponFragment();
                    transaction.add(R.id.coupon_rel,usedFragment);
                }else{
                    //已经创建过了
                    transaction.show(usedFragment);
                }
                break;
            case R.id.past_coupon_layout: //已过期的
                isAvailable = false;
                isUsed = false;
                isPast = true;
                //设置可用的信息
                setAvailableOrPast();
                if (pastFragment == null){
                    pastFragment = new PastCouponFragment();
                    transaction.add(R.id.coupon_rel,pastFragment);
                }else{
                    //已经创建过了
                    transaction.show(pastFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     * @param transaction
     * 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (availableFragment != null) {
            transaction.hide(availableFragment);
        }
        if (usedFragment != null) {
            transaction.hide(usedFragment);
        }
        if (pastFragment != null) {
            transaction.hide(pastFragment);
        }
    }
    /**
     * autour: Bishuang
     * date: 2017/8/31 14:59
     * 方法说明: 点击可用和不可用
    */
    private void setAvailableOrPast() {
        if(isAvailable){  //可使用
            availableText.setTextColor(getResources().getColor(R.color.Blue));
            availableView.setBackgroundColor(getResources().getColor(R.color.Blue));
            usedText.setTextColor(getResources().getColor(R.color.color_65));
            usedView.setBackgroundColor(getResources().getColor(R.color.white));
            pastText.setTextColor(getResources().getColor(R.color.color_65));
            pastView.setBackgroundColor(getResources().getColor(R.color.white));
        }else if(isUsed){  //已使用
            availableText.setTextColor(getResources().getColor(R.color.color_65));
            availableView.setBackgroundColor(getResources().getColor(R.color.white));
            usedText.setTextColor(getResources().getColor(R.color.Blue));
            usedView.setBackgroundColor(getResources().getColor(R.color.Blue));
            pastText.setTextColor(getResources().getColor(R.color.color_65));
            pastView.setBackgroundColor(getResources().getColor(R.color.white));
        }else if(isPast){  //已过期
            availableText.setTextColor(getResources().getColor(R.color.color_65));
            availableView.setBackgroundColor(getResources().getColor(R.color.white));
            usedText.setTextColor(getResources().getColor(R.color.color_65));
            usedView.setBackgroundColor(getResources().getColor(R.color.white));
            pastText.setTextColor(getResources().getColor(R.color.Blue));
            pastView.setBackgroundColor(getResources().getColor(R.color.Blue));
        }
    }

    /**
     * @author bin
     * 修改人:
     * 时间:2016-1-14 下午5:51:42
     * 方法说明:联网获取优惠券的方法
     */
    private void getAvailableCouponData(int userId, final int page) {
        Map<String,String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page.currentPage", String.valueOf(page));
        map.put("type", String.valueOf(1));  //1为优惠券
        OkHttpUtils.post().params(map).url(Address.GET_USER_COUPON).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            EntityPublic entityPublic = response.getEntity();
                            availableText.setText("可使用("+entityPublic.getCount_1()+")");  //可使用
                            usedText.setText("已使用("+entityPublic.getCount_2()+")"); //已使用
                            int pastCount = entityPublic.getCount_3()+entityPublic.getCount_4();
                            pastText.setText("已过期("+pastCount+")");  //已过期
                        }
                }
            }
        });

    }
}
