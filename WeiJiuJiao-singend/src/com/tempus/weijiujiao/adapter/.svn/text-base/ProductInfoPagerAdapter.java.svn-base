package com.tempus.weijiujiao.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProductInfoPagerAdapter extends FragmentPagerAdapter {
	private String[] titleStrings;
	private List<Fragment> fragmentList;

	public ProductInfoPagerAdapter(List<Fragment> FragmetList,
			String[] titleArray, FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.titleStrings = titleArray;
		this.fragmentList = FragmetList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titleStrings[position];
	}

}
