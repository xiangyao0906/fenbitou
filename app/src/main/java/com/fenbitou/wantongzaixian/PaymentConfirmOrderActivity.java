package com.fenbitou.wantongzaixian;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.adapter.CourseOrderAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.EntityPublicCallback;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.PhoneUtils;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/8/18.
 * 确认订单的类
 */

public class PaymentConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.payment_course_listView)
    NoScrollListView paymentCourseListView;
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
    @BindView(R.id.zong_price)
    TextView zongPrice;
    @BindView(R.id.total)
    LinearLayout total;
    @BindView(R.id.coupon_text)
    TextView couponText;
    @BindView(R.id.coupon_layout)
    LinearLayout couponLayout;
    @BindView(R.id.reality_price)
    TextView realityPrice;
    @BindView(R.id.submitOrder)
    TextView submitOrder;
    private int userId, orderId; // 用户Id,订单Id
    private boolean alipayBoolean = false;
    private String payType = "ALIPAY"; // 支付类型(默认值支付宝)
    private EntityPublic entityPublic, trxorder; // 优惠券的实体,订单实体
    private String couponCode = ""; // 优惠券编码
    private static PaymentConfirmOrderActivity orderActivity;

    private EntityPublic publicEntity; // 课程的实体

    @Override
    protected int initContentView() {
        getIntentMessage();
        return R.layout.act_payment_confirm_order;
    }

    /**
     * autour: Bishuang
     * date: 2017/8/18 17:00
     * 方法说明: 获取传过来的数据
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0); // 获取订单Id
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
    }


    public static Activity getInstence() {
        if (orderActivity == null) {
            orderActivity = new PaymentConfirmOrderActivity();
        }
        return orderActivity;
    }


    @Override
    protected void initComponent() {
        orderActivity = this;
        titleText.setText(getResources().getString(R.string.confirm_order)); // 设置标题
    }

    @Override
    protected void initData() {
        // 是否可以使用微信或支付宝支付
        getShare();
        // 获取订单的数据
        getOrderData(orderId);
    }

    @Override
    protected void addListener() {

    }

    // 是否能分享
    private void getShare() {
        showLoading(PaymentConfirmOrderActivity.this);
        OkHttpUtils.get().url(Address.WEBSITE_VERIFY_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(PaymentConfirmOrderActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
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

    /**
     * autour: Bishuang
     * date: 2017/8/18 17:08
     * 方法说明: 获取订单的数据
     */
    private void getOrderData(int orderId) {

        Map<String, String> map = new HashMap<>();
        map.put("orderId", String.valueOf(orderId));
        showLoading(PaymentConfirmOrderActivity.this);
        ILog.i(Address.ORDER_NO_PAYMENT + "?" + map + "---------------获取订单数据");
        OkHttpUtils.post().params(map).url(Address.ORDER_NO_PAYMENT).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(PaymentConfirmOrderActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        if (response.isSuccess()) {
                            publicEntity = response.getEntity().getDetailList().get(0);
                            trxorder = response.getEntity().getTrxorder();
                            paymentCourseListView.setAdapter(new CourseOrderAdapter(PaymentConfirmOrderActivity.this, response));
//                            if (response.toString().contains("couponCodeDTO")) {
                            if (response.getEntity().getCouponCodeDTO() != null ) {
                                EntityPublic codeDTO = response.getEntity().getCouponCodeDTO();
                                couponCode = codeDTO.getCouponCode(); // 优惠券编码
                                String amount = codeDTO.getAmount();
                                int type = codeDTO.getType();
                                String retainOne = PhoneUtils.getRetainOneDecimal(1, amount);
                                if (type == 1) {
                                    couponText.setText("折扣券(" + retainOne + "折)");
                                } else if (type == 2) {
                                    couponText.setText("立减(" + retainOne + "元)");
                                } else if (type == 3) {

                                }
                            } else {
                                couponText.setText("请选择");
                            }
                            zongPrice.setText("￥ " + trxorder.getSumPrice()); // 订单总金额
                            realityPrice.setText("￥ " + trxorder.getRealPrice()); // 实付订单金额
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    @OnClick({R.id.back_layout, R.id.submitOrder, R.id.alipay_layout
            , R.id.wxpay_layout, R.id.coupon_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回的布局
                PaymentConfirmOrderActivity.this.finish();
                break;
            case R.id.submitOrder: // 确认订单订单
                // 修改订单数据（用于重新支付前）接口
                getAgainPayVerificationOrder(orderId, trxorder.getPayType(),couponCode);
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
            case R.id.coupon_layout: // 优惠券的布局
                if (!TextUtils.isEmpty(trxorder.getSumPrice())) {
                    getApplyCoupon(userId, "COURSE", trxorder.getSumPrice());
                }
                break;
        }
    }

    /**
     * @author bin 修改人: 时间:2016-1-25 下午4:39:44 方法说明:获取可用优惠券的方法
     */
    private void getApplyCoupon(int userId, String type, String price) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("type", type);
        map.put("orderAmount", price);
        showLoading(PaymentConfirmOrderActivity.this);
        ILog.i(Address.GET_COUPON_LIST + "?" + map + "--------------获取可用优惠券的方法");
        OkHttpUtils.post().params(map).url(Address.GET_COUPON_LIST).build().execute(new EntityPublicCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("qqqqqq", "onError: " + e);
                IToast.show(PaymentConfirmOrderActivity.this, "加载失败");
            }

            @Override
            public void onResponse(EntityPublic response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        entityPublic = response;
                        String message = entityPublic.getMessage();
                        if (entityPublic.isSuccess()) {
                            List<EntityPublic> couponList = entityPublic.getEntity();
                            if (couponList.isEmpty()) {
                                IToast.show(PaymentConfirmOrderActivity.this, "无可用购课券");
                            } else {
                                Intent intent = new Intent();
                                intent.setClass(PaymentConfirmOrderActivity.this, AvailableCouponActivity.class);
                                intent.putExtra("entity", entityPublic);
                                startActivityForResult(intent, 1);
                            }
                        }else{
                            IToast.show(PaymentConfirmOrderActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * autour: Bishuang
     * date: 2017/8/18 17:36
     * 方法说明:重新支付检验订单的接口
     */
    private void getAgainPayVerificationOrder(int orderId, String type,String couponCode) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", String.valueOf(orderId));
        map.put("payType", type);
        map.put("couponCode", couponCode);
        showLoading(PaymentConfirmOrderActivity.this);
        ILog.i(Address.AGAINPAYVERIFICATIONORDER + "?" + map + "--------------重新支付检验订单的接口");
        OkHttpUtils.post().params(map).url(Address.AGAINPAYVERIFICATIONORDER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(PaymentConfirmOrderActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
//                    try {
                    cancelLoading();
                    String message = response.getMessage();
                    if (response.isSuccess()) {
                        Intent intent = new Intent(PaymentConfirmOrderActivity.this, PayActivity.class);
                        intent.putExtra("isPayment", true); // 待支付
                        intent.putExtra("publicEntity", response);
                        intent.putExtra("payType", payType);
                        startActivity(intent);
                        finish();
                    } else {
                        IToast.show(PaymentConfirmOrderActivity.this, message);
                    }
//                    } catch (Exception e) {
//                    }
                }
            }
        });
    }


    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                EntityPublic entity = entityPublic.getEntity().get(position);
                int type = entity.getType();
                String amount = entity.getAmount();
                couponCode = entity.getCouponCode();
                String retainOne = ConstantUtils.getRetainOneDecimal(1, amount);
                float currentprice = publicEntity.getCourse().getCurrentprice();
                float amountFloat = Float.parseFloat(amount);
                if (type == 1) {
                    couponText.setText("折扣券(" + retainOne + "折)");
                    double payPrice = mul(currentprice,amountFloat / 10);
                    DecimalFormat myformat=new DecimalFormat("0.00");
                    String str = myformat.format(payPrice);
                    realityPrice.setText("￥ " + str);
                } else if (type == 2) {
                    couponText.setText("立减(" + retainOne + "元)");
                    realityPrice.setText("￥ " + (currentprice - amountFloat));
                }
            } else {
                realityPrice.setText("￥ " + publicEntity.getCourse().getCurrentprice());
                couponText.setText("未选择");
                couponCode = "";
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.setParam(PaymentConfirmOrderActivity.this, "isbackPosition", false);
    }
}
