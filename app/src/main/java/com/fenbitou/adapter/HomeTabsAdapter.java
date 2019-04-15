package com.fenbitou.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenbitou.entity.HomeTabEntity;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

public class HomeTabsAdapter extends BaseQuickAdapter<HomeTabEntity, BaseViewHolder> {
    public HomeTabsAdapter( @Nullable List<HomeTabEntity> data) {
        super(R.layout.home_tab_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeTabEntity item) {

        helper.setImageResource(R.id.tab_icon,item.getTabIcon());

        helper.setText(R.id.tab_text,item.getTabText());


    }
}
