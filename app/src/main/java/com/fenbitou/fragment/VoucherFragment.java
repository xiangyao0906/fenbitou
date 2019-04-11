package com.fenbitou.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseFragment;
import com.fenbitou.wantongzaixian.R;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author xiangyao
 * @Title: CouponFragment
 * @Package com.yizhilu.fragment
 * @Description: 优惠券
 * @date 2017/8/14 14:05
 */
public class VoucherFragment extends BaseFragment {

    @BindView(R.id.available_text)
    TextView availableText;
    @BindView(R.id.available_view)
    View availableView;
    @BindView(R.id.available_coupon_layout)
    LinearLayout availableCouponLayout;
    @BindView(R.id.used_text)
    TextView usedText;
    @BindView(R.id.used_view)
    View usedView;
    @BindView(R.id.used_coupon_layout)
    LinearLayout usedCouponLayout;
    @BindView(R.id.past_text)
    TextView pastText;
    @BindView(R.id.past_view)
    View pastView;
    @BindView(R.id.past_coupon_layout)
    LinearLayout pastCouponLayout;
    @BindView(R.id.voucher_fra)
    FrameLayout availablePastCouponLayout;
    private android.support.v4.app.FragmentManager fm;
    private AvailableVoucherFragment availableFragment;  //可用代金券
    private UsedVoucherFragment usedVoucherFragment;  //已使用代金券
    private PastVoucherFragment pastVoucherFragment;  //不可用代金券
    private boolean isAvailable,isUsed,isPast;  //是否可用

    @Override
    protected int initContentView() {
        return R.layout.fragment_voucher;
    }

    @Override
    protected void initComponent() {

        availableFragment = new AvailableVoucherFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.voucher_fra,availableFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void initData() {

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
                    availableFragment = new AvailableVoucherFragment();
                    transaction.add(R.id.voucher_fra,availableFragment);
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
                if (usedVoucherFragment == null){
                    usedVoucherFragment = new UsedVoucherFragment();
                    transaction.add(R.id.voucher_fra,usedVoucherFragment);
                }else{
                    //已经创建过了
                    transaction.show(usedVoucherFragment);
                }
                break;
            case R.id.past_coupon_layout: //已过期的
                isAvailable = false;
                isUsed = false;
                isPast = true;
                //设置可用的信息
                setAvailableOrPast();
                if (pastVoucherFragment == null){
                    pastVoucherFragment = new PastVoucherFragment();
                    transaction.add(R.id.voucher_fra,pastVoucherFragment);
                }else{
                    //已经创建过了
                    transaction.show(pastVoucherFragment);
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
        if (usedVoucherFragment != null) {
            transaction.hide(usedVoucherFragment);
        }
        if (pastVoucherFragment != null) {
            transaction.hide(pastVoucherFragment);
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

}
