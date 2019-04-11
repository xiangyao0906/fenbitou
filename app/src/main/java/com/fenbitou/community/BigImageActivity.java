package com.fenbitou.community;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseActivity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;
import com.fenbitou.utils.ZoomOutPageTransformer;
import com.fenbitou.widget.PhotoView;
import com.fenbitou.widget.PhotoViewPager;

import org.zackratos.ultimatebar.UltimateBar;

import butterknife.BindView;
import butterknife.OnClick;

public class BigImageActivity extends BaseActivity {

    @BindView(R.id.photo_viewPager)
    PhotoViewPager photoViewPager;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;

    private String[] picUrls;// 图片地址数组
    private int index;// 当前下标
    private int size;// 总size
    private static PhotoView photoView;


    @Override
    protected int initContentView() {
        return R.layout.activity_big_image;
    }

    @Override
    protected void initComponent() {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.tabText));
        index = getIntent().getIntExtra("index", 0);
        picUrls = getIntent().getStringArrayExtra("picUrls");
        size = picUrls.length;
        title.setText(index + 1 + "/" + size);
        photoViewPager.setAdapter(new CheckImageAdapter(picUrls, BigImageActivity.this));
        photoViewPager.setCurrentItem(index);
        photoViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        photoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                title.setText(position + 1 + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {

    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }

    static class CheckImageAdapter extends PagerAdapter {

        private String[] picUrls;
        private int size;
        private Context context;

        public CheckImageAdapter(String[] picUrls, Context context) {
            this.picUrls = picUrls;
            this.size = picUrls.length;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            photoView = new PhotoView(container.getContext());
            GlideUtil.loadImage(context, picUrls[position], photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
