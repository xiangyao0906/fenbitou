package com.fenbitou.wantongzaixian;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.MyOrderAdapter;
import com.fenbitou.adapter.OrderSortAdapter;
import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.OrderEntity;
import com.fenbitou.entity.PageEntity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.IToast;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.fenbitou.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.order_null_layout;
import static com.fenbitou.wantongzaixian.R.id.order_status;
import static com.fenbitou.wantongzaixian.R.id.order_status_image;

/**
 * Created by bishuang on 2017/8/18.
 * 我的订单的类
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(order_status)
    TextView orderStatus;
    @BindView(R.id.swipe_target)
    ListView recordListView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(order_null_layout)
    LinearLayout orderNullLayout;
    @BindView(R.id.statucLayout)
    LinearLayout statucLayout;
    @BindView(R.id.selectStatucLayout)
    LinearLayout selectStatucLayout;
    @BindView(order_status_image)
    ImageView statusImage;
    @BindView(R.id.statucListView)
    NoScrollListView statucListView;
    private int currentPage = 0;
    private List<OrderEntity> orderList; // 订单的实体
    private MyOrderAdapter orderAdapter;// 订单的适配器
    private int userId;
    private String trxStatus;
    private boolean isSelect;
    private boolean firstIn = true;
    private String[] statusList = new String[]{"全部订单", "未支付订单", "已支付订单", "已退款订单", "已取消订单"};
    private String[] orderStatusList = new String[]{"", "INIT", "SUCCESS", "REFUND", "CANCEL"};
    private OrderSortAdapter statucAdapter; // 订单状态的适配器

    @Override
    protected int initContentView() {
        return R.layout.act_my_order;
    }

    @Override
    protected void initComponent() {
        showLoading(MyOrderActivity.this);
        orderList = new ArrayList<>(); // 订单的实体
        statucAdapter = new OrderSortAdapter(MyOrderActivity.this, statusList);
        statucListView.setAdapter(statucAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstIn) {
            currentPage = 1;
            orderList.clear();
            getOrderList(currentPage, userId, trxStatus);
        }
    }

    @Override
    protected void initData() {
        currentPage = 1;
        if (orderList != null) {
            orderList.clear();
        }
        userId = (int) SharedPreferencesUtils.getParam(this, "userId", -1);
        // 联网获取我的订单的方法
        getOrderList(currentPage, userId, trxStatus);
    }

    @Override
    protected void addListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        statucListView.setOnItemClickListener(this); // 订单状态的点击事件
    }

    @OnClick({R.id.back_layout, R.id.selectStatucLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                MyOrderActivity.this.finish();
                break;
            case R.id.selectStatucLayout: // 选择订单状态
                if (isSelect) {
                    // 隐藏订单状态的布局
                    hintLayout();
                } else {
                    // 显示订单状态的布局
                    showLayout();
                }
                break;
        }
    }

    /**
     * autour: Bishuang
     * date: 2017/8/18 15:31
     * 方法说明: 联网获取我的订单的方法
     */
    private void getOrderList(final int currentPage, int userId, String trxStatus) {
        Map<String, String> map = new HashMap<>();
        map.put("page.currentPage", String.valueOf(currentPage));
        map.put("queryTrxorder.userId", String.valueOf(userId));
        if (!TextUtils.isEmpty(trxStatus)) {
            map.put("queryTrxorder.trxStatus", trxStatus);
        }
        OkHttpUtils.post().params(map).url(Address.MY_ORDER_LIST).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(MyOrderActivity.this, "加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        boolean success = response.isSuccess();
                        if (success) {
                            firstIn = false;
                            swipeToLoadLayout.setRefreshing(false);
                            swipeToLoadLayout.setLoadingMore(false);
                            PageEntity page = response.getEntity().getPage();
                            if (page.getTotalPageSize() <= currentPage) {
                                swipeToLoadLayout.setLoadMoreEnabled(false);
                            } else {
                                swipeToLoadLayout.setLoadMoreEnabled(true);
                            }
                            if (orderList != null) {
                                orderList.clear();
                            }
                            List<OrderEntity> trxorderList = response.getEntity().getOrderList();
                            if (trxorderList != null && trxorderList.size() > 0) {
                                orderList.addAll(trxorderList);
                            }
                            orderAdapter = new MyOrderAdapter(MyOrderActivity.this, orderList);
                            recordListView.setAdapter(orderAdapter);
                            if (orderList.isEmpty()) {
                                orderNullLayout.setVisibility(View.VISIBLE);
                            } else {
                                orderNullLayout.setVisibility(View.GONE);
                            }
                        } else {
                            IToast.show(MyOrderActivity.this, message);
                        }
                    } catch (Exception e) {
                        Log.i("lala", "onResponse: ");
                    }

                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        trxStatus = orderStatusList[position];
        orderStatus.setText(statusList[position]);

        statucAdapter.setPostion(position);
        statucAdapter.notifyDataSetChanged();

        // 隐藏订单状态的布局
        hintLayout();


        currentPage = 1;
        orderList.clear();
        getOrderList(currentPage, userId, trxStatus);
    }

    /**
     * @author bin 修改人: 时间:2015-10-24 下午4:34:15 方法说明:显示订单状态的布局
     */
    private void showLayout() {
        Animation animationIn = AnimationUtils.loadAnimation(MyOrderActivity.this, R.anim.push_up_in);
        statucLayout.setVisibility(View.VISIBLE);
        statucLayout.setAnimation(animationIn);
        isSelect = true;
        statusImage.setBackgroundResource(R.drawable.btn_withdrawn);
    }

    /**
     * @author bin 修改人: 时间:2015-10-24 下午4:33:29 方法说明:隐藏订单状态的布局
     */
    private void hintLayout() {
        Animation animationOut = AnimationUtils.loadAnimation(MyOrderActivity.this, R.anim.slide_down_out);
        statucLayout.setVisibility(View.GONE);
        statucLayout.setAnimation(animationOut);
        isSelect = false;
        statusImage.setBackgroundResource(R.drawable.btn_open);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        orderList.clear();
        getOrderList(currentPage, userId, trxStatus);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getOrderList(currentPage, userId, trxStatus);
    }

    public void showCancelDialog(final int orderId) {
        new AlertDialog.Builder(this).setTitle("确认取消该订单")
                .setPositiveButton("确定", (dialog, which) -> cancelOrder(orderId))
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // 取消订单
    public void cancelOrder(int orderId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("orderId", String.valueOf(orderId));
        OkHttpUtils.post().params(map).url(Address.CANCEL_ORDER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (response.isSuccess()) {
                    IToast.show(response.getMessage());
                    onRefresh();
                } else {
                    IToast.show(response.getMessage());
                }
            }
        });
    }
}
