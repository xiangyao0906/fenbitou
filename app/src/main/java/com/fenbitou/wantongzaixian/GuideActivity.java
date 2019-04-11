package com.fenbitou.wantongzaixian;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.entity.PublicEntity;
import com.fenbitou.entity.PublicEntityCallback;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.PhoneUtils;
import com.fenbitou.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by bishuang on 2017/7/26.
 * 引导页的类
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    //    @BindView(R.id.btnGo)
//    LinearLayout btnGo;
    private LinearLayout btnGo;

    private List<View> viewList; // 存放引导页的布局
    private ImageView imageOne, imageTwo; // 引导页的第一张和第二张图片
    private View viewThree; // 引导页第三张的布局
    private PhoneUtils phoneUtils; // 手机的工具类
    private String from = "";
    @Override
    protected int initContentView() {
        return R.layout.act_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initComponent() {
        phoneUtils = new PhoneUtils(GuideActivity.this); // 手机的工具类
        viewList = new ArrayList<View>(); // 存放引导页的布局
        imageOne = new ImageView(GuideActivity.this);
        imageOne.setBackgroundResource(R.drawable.viewone);
        imageTwo = new ImageView(GuideActivity.this);
        imageTwo.setBackgroundResource(R.drawable.viewtwo);
        viewThree = LayoutInflater.from(GuideActivity.this).inflate(R.layout.act_guide_viewthree, null);
        btnGo = (LinearLayout) viewThree.findViewById(R.id.btnGo);

        viewList.add(imageOne);
        viewList.add(imageTwo);
        viewList.add(viewThree);
        from = getIntent().getStringExtra("from");
    }

    @Override
    protected void initData() {
        // 绑定适配器
        viewPager.setAdapter(new ViewPagerAdapter());
        // 添加安装记录的方法
        addInatallRecord();
    }

    @Override
    protected void addListener() {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtils.setParam(GuideActivity.this, "isFrist", true);
                if ("AboutActivity".equals(from)) {
                    finish();
                } else {
                    openActivity(MainActivity.class);
                    finish();
                }
            }
        });
    }

    /**
     * 类说明:引导页滑动的适配器
     */
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

    }

    /**
     * 方法说明:添加安装记录的方法
     */
    private void addInatallRecord() {
        Map<String, String> map = new HashMap<>();
        map.put("websiteInstall.ip", phoneUtils.getIPAddress(this));
        map.put("websiteInstall.brand", phoneUtils.getPhoneBrand());
        map.put("websiteInstall.modelNumber", phoneUtils.getPhoneModel());
        map.put("websiteInstall.size", phoneUtils.getPhoneSize());
        map.put("websiteLogin.type", "android");
        OkHttpUtils.post().params(map).url(Address.ADD_INSTALL_RECORD).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {

            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
