package com.fenbitou.wantongzaixian;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.EntityPublicCallback;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.ConstantUtils;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.ILog;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.alipay_select;
import static com.fenbitou.wantongzaixian.R.id.coupon_text;
import static com.fenbitou.wantongzaixian.R.id.reality_price;
import static com.fenbitou.wantongzaixian.R.id.wxpay_select;
import static com.fenbitou.wantongzaixian.R.id.zong_price;

/**
 * Created by bishuang on 2017/8/22.
 * 课程详情购买-- 确认订单的类
 */

public class ConfirmOrderActivity extends BaseActivity {

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
    @BindView(alipay_select)
    ImageView alipaySelect;
    @BindView(R.id.alipay_layout)
    LinearLayout alipayLayout;
    @BindView(wxpay_select)
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
    @BindView(coupon_text)
    TextView couponText;
    @BindView(R.id.coupon_layout)
    LinearLayout couponLayout;
    @BindView(R.id.coupon_zong_layout)
    RelativeLayout couponZongLayout;
    @BindView(reality_price)
    TextView realityPrice;
    @BindView(R.id.submitOrder)
    TextView submitOrder;
    private EntityPublic publicEntity; // 课程的实体
    private int userId, courseId; // 用户Id,课程Id
    private EntityPublic entityPublic; // 优惠券的实体
    private boolean isCoupon; // 是否点击了优惠券布局
    private String couponCode = ""; // 优惠券编码
    private String payType = "ALIPAY"; // 支付类型(默认值支付宝)

    @Override
    protected int initContentView() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void initComponent() {
        getIntentMessage();
        courseName.setText(publicEntity.getCourse().getName());
        titleText.setText(getResources().getString(R.string.confirm_order)); // 设置标题
        playNum.setText("播放量 : " + publicEntity.getCourseNum());
        zongPrice.setText("￥ " + publicEntity.getCourse().getCurrentprice());
        realityPrice.setText("￥ " + publicEntity.getCourse().getCurrentprice());
        GlideUtil.loadImage(ConfirmOrderActivity.this,Address.IMAGE_NET+publicEntity.getCourse().getMobileLogo(),courseImage);
    }

    /**
     * autour: Bishuang
     * date: 2017/8/22 16:54
     * 方法说明: 获取传过来的数据
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        publicEntity = (EntityPublic) intent.getSerializableExtra("publicEntity");
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
    }

    @Override
    protected void initData() {
        // 获取可用优惠券的方法
        getApplyCoupon(userId, "COURSE", publicEntity.getCourse().getCurrentprice());
    }

    @Override
    protected void addListener() {

    }

    @OnClick({R.id.back_layout, R.id.submitOrder,R.id.alipay_layout,R.id.wxpay_layout,R.id.coupon_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回的布局
                this.finish();
                break;
            case R.id.submitOrder: // 提交订单
                courseId = publicEntity.getCourse().getId();
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
                // 获取可用优惠券的方法
                getApplyCoupon(userId, "COURSE", publicEntity.getCourse().getCurrentprice());
                break;
            default:
                break;
        }
    }

    /**
     * autour: Bishuang
     * date: 2017/8/22 17:43
     * 方法说明:
    */
    private void getApplyCoupon(int userId, String type, float price) {
        Map<String,String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("type", type);
        map.put("orderAmount", String.valueOf(price));
        showLoading(ConfirmOrderActivity.this);
        OkHttpUtils.post().params(map).url(Address.GET_COUPON_LIST).build().execute(new EntityPublicCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(EntityPublic response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        entityPublic = response;
                        String message = entityPublic.getMessage();
                        if (entityPublic.isSuccess()) {
                            cancelLoading();
                            List<EntityPublic> couponList = entityPublic.getEntity();
                            if (couponList.isEmpty()) {
                                if (isCoupon) {
                                    IToast.show(ConfirmOrderActivity.this, "无可用购课券");
                                } else {
                                    couponText.setText("无可用购课券");
                                }
                            } else {
                                if (isCoupon) {
                                    Intent intent = new Intent();
                                    intent.setClass(ConfirmOrderActivity.this, AvailableCouponActivity.class);
                                    intent.putExtra("entity", entityPublic);
                                    startActivityForResult(intent, 1);
                                }
                            }
                        }else{
                            IToast.show(ConfirmOrderActivity.this,message);
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
                EntityPublic entity = entityPublic.getEntity().get(position);
                int type = entity.getType();
                String amount = entity.getAmount();
                couponCode = entity.getCouponCode();
                String retainOne = ConstantUtils.getRetainOneDecimal(1, amount);
                float currentprice = publicEntity.getCourse().getCurrentprice();
                float amountFloat = Float.parseFloat(amount);
                if (type == 1) {
                    couponText.setText("折扣券(" + retainOne + "折)");
//					reality_price.setText("￥ " + currentprice * (amountFloat / 10));
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

    /**
     * @author 刘常启 修改人: 时间:2015-10-13 上午9:47:33 方法说明:连接网络获取创建订单数据
     */
    private void getCreateOrder(int userId, int courseId, String payType, String couponCode) {
        Map<String,String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("courseId", String.valueOf(courseId));
        map.put("payType", payType);
        map.put("couponCode", couponCode);
        map.put("orderForm", "Android");
        showLoading(ConfirmOrderActivity.this);
        ILog.i(Address.CREATE_ORDER+"?"+map+"---------课程详情-------连接网络获取创建订单数据");
        OkHttpUtils.post().params(map).url(Address.CREATE_ORDER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(ConfirmOrderActivity.this,"加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            Intent intent = new Intent(ConfirmOrderActivity.this, PayActivity.class);
                            intent.putExtra("publicEntity", response);
                            intent.putExtra("currentPrice", publicEntity.getCourse().getCurrentprice());
                            startActivity(intent);
                             ConfirmOrderActivity.this.finish();
                        } else {
                            ConstantUtils.showMsg(ConfirmOrderActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.setParam(ConfirmOrderActivity.this,"isbackPosition",false);
    }
}
