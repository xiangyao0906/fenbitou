package com.fenbitou.bl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PolyvPlayerFragmentAdapter extends FragmentPagerAdapter {
	List<Fragment> fragmentList = new ArrayList<Fragment>();

	public PolyvPlayerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public PolyvPlayerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
