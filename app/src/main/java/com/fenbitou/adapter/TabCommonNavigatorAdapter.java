package com.fenbitou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fenbitou.wantongzaixian.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

/**
 * Created by admin on 2017/5/6.
 * 指示器 适配器   配合viewPage
 */

public class TabCommonNavigatorAdapter extends CommonNavigatorAdapter {


    private Context context;
    private List<String> titleList;
    private ViewPager viewPager;

    public TabCommonNavigatorAdapter(Context context, List<String> titleList, ViewPager viewPager) {
        super();
        this.context = context;
        this.titleList = titleList;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return titleList == null ? 0 : titleList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        //标题
        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
        simplePagerTitleView.setText(titleList.get(index));
        simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));//未选中颜色
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_main));//选中的颜色
        simplePagerTitleView.setTextSize(14);//字体大小
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        //指示器
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);//等同标题长度
        indicator.setYOffset(UIUtil.dip2px(context, 3)); //与底部距离
        indicator.setLineHeight(UIUtil.dip2px(context, 2));//指示器宽度
        indicator.setColors(context.getResources().getColor(R.color.color_main));//指示器颜色
//        indicator.setLineWidth(UIUtil.dip2px(context, 30));//指示器长度
//        indicator.setStartInterpolator(new AccelerateInterpolator());//开始动画
//        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));//结束动画
        return indicator;
    }
}
