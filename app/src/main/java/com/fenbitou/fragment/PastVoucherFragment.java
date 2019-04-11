package com.fenbitou.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fenbitou.adapter.CouponAdapter;
import com.fenbitou.base.BaseFragment;
import com.fenbitou.entity.CouponEntity;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.wantongzaixian.R;
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
import okhttp3.Call;

import static com.fenbitou.wantongzaixian.R.id.null_layout;

/**
 * Created by bishuang on 2017/8/31.
 * 代金券-已过期
 */

public class PastVoucherFragment extends BaseFragment {
    @BindView(R.id.record_list_view)
    NoScrollListView recordListView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(null_layout)
    LinearLayout nullLayout;
    private List<CouponEntity> allCouponList;  //优惠券的总集合
    private int userId, page = 1;

    @Override
    protected int initContentView() {
        return R.layout.frg_past_voucher;
    }

    @Override
    protected void initComponent() {
        userId = (int) SharedPreferencesUtils.getParam(getActivity(), "userId", -1);
        allCouponList = new ArrayList<CouponEntity>();  //优惠券总集合
    }

    @Override
    protected void initData() {
// 联网获取不可用代金券的方法
        getAvailableCouponData(userId, page);
    }

    @Override
    protected void addListener() {

    }

    /**
     * @author bin 修改人: 时间:2016-4-1 上午9:20:07 方法说明:设置过期代金券的方法
     */
    private void setVoucherData(final int page, PublicEntity publicEntity) {
            if (publicEntity.isSuccess()) {
                cancelLoading();
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                EntityPublic entityPublic = publicEntity.getEntity();
                if (page >= entityPublic.getPage().getTotalPageSize()) {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
                }
                List<CouponEntity> couponList = entityPublic.getCouponList();
                if (couponList == null || couponList.size() <= 0) {
                    nullLayout.setVisibility(View.VISIBLE);
                    swipeToLoadLayout.setVisibility(View.GONE);
                } else {
                    nullLayout.setVisibility(View.GONE);
                    swipeToLoadLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < couponList.size(); i++) {
                        CouponEntity couponEntity = couponList.get(i);
                        if (couponEntity.getStatus() == 2
                                || couponEntity.getStatus() == 3
                                || couponEntity.getStatus() == 4) {
                            allCouponList.add(couponEntity);
                        }
                    }
                }
                if (allCouponList.isEmpty()) {
                    nullLayout.setVisibility(View.VISIBLE);
                    swipeToLoadLayout.setVisibility(View.GONE);
                }
                recordListView.setAdapter(new CouponAdapter(getActivity(),
                        allCouponList));
            }
    }

    /**
     * @author bin 修改人: 时间:2016-1-14 下午5:51:42 方法说明:联网获取不可用代金券的方法
     */
    private void getAvailableCouponData(int userId, final int page) {
        Map<String,String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("page.currentPage", String.valueOf(page));
        map.put("type", String.valueOf(2)); // 2代金券
        showLoading(getActivity());
        OkHttpUtils.post().params(map).url(Address.GET_USER_COUPON).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                IToast.show(getActivity(),"加载失败");
            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                        String message = response.getMessage();
                        // 设置过期代金券的方法
                        setVoucherData(page, response);
                }
            }
        });


    }

    @Override
    public void onRefresh() {
        page = 1;
        allCouponList.clear();
        // 联网获取不可用代金券的方法
        getAvailableCouponData(userId, page);
    }

    @Override
    public void onLoadMore() {
        page++;
        // 联网获取不可用代金券的方法
        getAvailableCouponData(userId, page);
    }

}
