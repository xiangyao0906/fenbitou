package com.yizhilu.eduapp;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.base.BaseActivity;
import com.yizhilu.fragment.CouponFragment;
import com.yizhilu.fragment.VoucherFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class DiscontActivity extends BaseActivity {


    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.voucher_layout)
    RelativeLayout voucherLayout;
    @BindView(R.id.coupon_layout)
    RelativeLayout couponLayout;
    @BindView(R.id.voucher_text)
    TextView voucherText;
    @BindView(R.id.coupon_text)
    TextView couponText;
    @BindView(R.id.title_coupon_layout)
    LinearLayout titleCouponLayout;
    @BindView(R.id.lin_pager)
    FrameLayout lin_pager;
    private boolean isCoupon = true, isVoucher = false; // 点击的是优惠券
    private CouponFragment couponFragment; // 优惠券的类
    private VoucherFragment voucherFragment; // 代金券的类

    @Override
    protected int initContentView() {
        return R.layout.activity_discont;
    }

    @Override
    protected void initComponent() {
        couponFragment = new CouponFragment();//优惠券的类
        voucherFragment = new VoucherFragment(); //代金券的类
        getSupportFragmentManager().beginTransaction().add(R.id.lin_pager,voucherFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.lin_pager,couponFragment).commit();
        getSupportFragmentManager().beginTransaction().show(couponFragment).hide(voucherFragment).commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }


    @OnClick({R.id.back_layout, R.id.voucher_layout, R.id.coupon_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.voucher_layout:
                isVoucher = true;
                isCoupon = false;
                // 设置图片和背景颜色
                setCouponOrVoucher();
                getSupportFragmentManager().beginTransaction().show(couponFragment).hide(voucherFragment).commit();
                break;
            case R.id.coupon_layout:
                isVoucher = false;
                isCoupon = true;
                setCouponOrVoucher();
                getSupportFragmentManager().beginTransaction().show(voucherFragment).hide(couponFragment).commit();

                break;
        }
    }

    /**
     * @author xiangyao 修改人: 时间:2017年8月14日14:23:12 方法说明:设置优惠券和代金券
     */
    private void setCouponOrVoucher() {
        if (isVoucher) { // 代金券
            voucherLayout.setBackgroundResource(R.drawable.left_yes);
            couponLayout.setBackgroundResource(R.drawable.right_no);
            voucherText.setTextColor(getResources().getColor(R.color.color_main));
            couponText.setTextColor(getResources().getColor(R.color.white));
        } else if (isCoupon) { // 优惠券
            couponLayout.setBackgroundResource(R.drawable.right_yes);
            voucherLayout.setBackgroundResource(R.drawable.left_no);
            couponText.setTextColor(getResources().getColor(R.color.color_main));
            voucherText.setTextColor(getResources().getColor(R.color.white));
        }
    }


}
