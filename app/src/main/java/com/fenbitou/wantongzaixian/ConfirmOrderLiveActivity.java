package com.fenbitou.wantongzaixian;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.EntityPublicCallback;
import com.fenbitou.entity.LiveEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.DensityUtil;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.alipay_layout;
import static com.fenbitou.wantongzaixian.R.id.reality_price;
import static com.fenbitou.wantongzaixian.R.id.wxpay_layout;
import static com.fenbitou.wantongzaixian.R.id.zong_price;

/**
 * Created by bishuang on 2017/9/8.
 * 直播确认订单的类
 */

public class ConfirmOrderLiveActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.courseImage)
    ImageView courseImage;
    @BindView(R.id.courseName)
    TextView courseName;
    @BindView(R.id.playNum)
    TextView playNum;
    @BindView(R.id.alipay_select)
    ImageView alipaySelect;
    @BindView(R.id.alipay_layout)
    LinearLayout alipayLayout;
    @BindView(R.id.view_pay)
    View viewPay;
    @BindView(R.id.wxpay_select)
    ImageView wxpaySelect;
    @BindView(R.id.wxpay_layout)
    LinearLayout wxpayLayout;
    @BindView(R.id.Payment)
    TextView Payment;
    @BindView(zong_price)
    TextView zongPrice;
    @BindView(R.id.total)
    LinearLayout total;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.coupon_cancel)
    LinearLayout couponCancel;
    @BindView(R.id.coupon_text)
    TextView couponText;
    @BindView(R.id.coupon_layout)
    LinearLayout couponLayout;
    @BindView(R.id.coupon_zong_layout)
    RelativeLayout couponZongLayout;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(reality_price)
    TextView realityPrice;
    @BindView(R.id.submitOrder)
    TextView submitOrder;
    private LiveEntity publicEntity;
    private int userId, courseId; // 用户Id,课程Id
    private EntityPublic entityPublic; // 优惠券的实体
    private boolean isCoupon; // 是否点击了优惠券布局
    private boolean alipayBoolean = false;
    private String payType = "ALIPAY"; // 支付类型(默认值支付宝)
    private String couponCode = ""; // 优惠券编码

    @Override
    protected int initContentView() {
        return R.layout.activity_confirm_order;
    }

    /**
     * 方法说明:获取传过来的数据
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        publicEntity = (LiveEntity) intent.getSerializableExtra("publicEntity");
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        titleText.setText(getResources().getString(R.string.confirm_order)); // 设置标题
        courseName.setText(publicEntity.getEntity().getName());
        playNum.setVisibility(View.GONE);
        zongPrice.setText("￥ " + publicEntity.getEntity().getCurrentPrice());
        realityPrice.setText("￥ " + publicEntity.getEntity().getCurrentPrice());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) courseImage.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(this);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        courseImage.setLayoutParams(params);
        Glide.with(this).load(Address.IMAGE_NET + publicEntity.getEntity().getLogo()).into(courseImage);
    }

    @Override
    protected void initData() {
        String currentPrice = publicEntity.getEntity().getCurrentPrice();
        float parseFloat = Float.parseFloat(currentPrice);
        // 获取可用优惠券的方法
        getApplyCoupon(userId, "COURSE", parseFloat);
        // 是否可以使用微信或支付宝支付
        getShare();
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.coupon_layout, alipay_layout, wxpay_layout, R.id.submitOrder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回的布局
                this.finish();
                break;
            case R.id.submitOrder: // 提交订单
                courseId = publicEntity.getEntity().getId();
                // 创建订单的方法
                getCreateOrder(userId, courseId, payType, couponCode);
                break;
            case R.id.alipay_layout: // 支付宝
                payType = "ALIPAY";
                alipaySelect.setBackgroundResource(R.drawable.pay_selected);
                wxpaySelect.setBackgroundResource(R.drawable.pay_not_selected);
                break;
            case R.id.wxpay_layout: // 微信支付
                payType = "WEIXIN";
                wxpaySelect.setBackgroundResource(R.drawable.pay_selected);
                alipaySelect.setBackgroundResource(R.drawable.pay_not_selected);
                break;
            case R.id.coupon_layout: // 优惠券布局
                isCoupon = true;
                String currentPrice = publicEntity.getEntity().getCurrentPrice();
                float currentprice = Float.parseFloat(currentPrice);
                // 获取可用优惠券的方法
                getApplyCoupon(userId, "COURSE", currentprice);
                break;
        }
    }


    /**
     * @author 刘常启 修改人: 时间:2015-10-13 上午9:47:33 方法说明:连接网络获取创建订单数据
     */
    private void getCreateOrder(int userId, int courseId, String payType, String couponCode) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("courseId", String.valueOf(courseId));
        map.put("payType", payType);
        map.put("couponCode", couponCode);
        map.put("orderForm", "Android");
        showLoading(ConfirmOrderLiveActivity.this);
        OkHttpUtils.post().params(map).url(Address.CREATE_ORDER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(ConfirmOrderLiveActivity.this, "加载失败");
                cancelLoading();
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                cancelLoading();
                if (!TextUtils.isEmpty(response.toString())) {
                    String message = response.getMessage();
                    if (response.isSuccess()) {
                        Intent intent = new Intent(ConfirmOrderLiveActivity.this, PayActivity.class);
                        intent.putExtra("publicEntity", response);
                        String currentPrice = publicEntity.getEntity().getCurrentPrice();
                        float currentprice = Float.parseFloat(currentPrice);
                        intent.putExtra("currentPrice", currentprice);
                        startActivity(intent);
                    } else {
                        IToast.show(ConfirmOrderLiveActivity.this, message);
                    }
                }
            }
        });

    }

    /**
     * 方法说明:获取可用优惠券的方法
     */
    private void getApplyCoupon(int userId, String type, float price) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("type", type);
        map.put("orderAmount", String.valueOf(price));
        showLoading(ConfirmOrderLiveActivity.this);
        OkHttpUtils.post().params(map).url(Address.GET_COUPON_LIST).build().execute(new EntityPublicCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
                IToast.show(ConfirmOrderLiveActivity.this, "加载失败");
            }

            @Override
            public void onResponse(EntityPublic response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    cancelLoading();
                    entityPublic = response;
                    String message = entityPublic.getMessage();
                    if (entityPublic.isSuccess()) {
                        List<EntityPublic> couponList = entityPublic.getEntity();
                        if (couponList.isEmpty()) {
                            if (isCoupon) {
                                IToast.show(ConfirmOrderLiveActivity.this, "无可用购课券");
                            } else {
                                couponText.setText("无可用购课券");
                            }
                        } else {
                            if (isCoupon) {
                                Intent intent = new Intent();
                                intent.setClass(ConfirmOrderLiveActivity.this, AvailableCouponActivity.class);
                                intent.putExtra("entity", entityPublic);
                                startActivityForResult(intent, 1);
                            }
                        }
                    } else {
                        IToast.show(ConfirmOrderLiveActivity.this, message);
                    }
                }
            }
        });
    }

    // 是否能分享
    private void getShare() {

        OkHttpUtils.get().url(Address.WEBSITE_VERIFY_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancelLoading();
                IToast.show(ConfirmOrderLiveActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        boolean success = response.isSuccess();
                        if (success) {
                            EntityPublic entity = response.getEntity();
                            String verifyAlipay = entity.getVerifyAlipay();
                            String verifywx = entity.getVerifywx();
                            if (verifyAlipay.equals("ON")) {
                                alipayLayout.setVisibility(View.VISIBLE);
                            } else {
                                alipayBoolean = true;
                                viewPay.setVisibility(View.GONE);
                                alipayLayout.setVisibility(View.GONE);
                            }
                            if (verifywx.equals("ON")) {
                                if (alipayBoolean) {
                                    payType = "WEIXIN";
                                    wxpaySelect.setBackgroundResource(R.drawable.pay_selected);
                                    alipaySelect.setBackgroundResource(R.drawable.pay_not_selected);
                                }
                                wxpayLayout.setVisibility(View.VISIBLE);
                            } else {
                                wxpayLayout.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                // coupon_cancel.setVisibility(View.VISIBLE);
                EntityPublic entity = entityPublic.getEntity().get(position);
                int type = entity.getType();
                String amount = entity.getAmount();
                couponCode = entity.getCouponCode();
                String retainOne = ConstantUtils.getRetainOneDecimal(1, amount);
                String currentPrice = publicEntity.getEntity().getCurrentPrice();
                float currentprice = Float.parseFloat(currentPrice);
                float amountFloat = Float.parseFloat(amount);
                if (type == 1) {
                    couponText.setText("折扣券(" + retainOne + "折)");
                    realityPrice.setText("￥ " + currentprice * (amountFloat / 10));
                } else if (type == 2) {
                    couponText.setText("立减(" + retainOne + "元)");
                    realityPrice.setText("￥ " + (currentprice - amountFloat));
                }
            } else {
                realityPrice.setText("￥ " + publicEntity.getEntity().getCurrentPrice());
                couponText.setText("未选择");
                couponCode = "";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
