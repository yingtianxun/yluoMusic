package com.yluo.yluomusic.adapter.base;

import java.util.List;

import android.R.integer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BaseViewPagerAdapter extends FragmentPagerAdapter{
	
	private List<Fragment>  fragments;
	
	public BaseViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		
		return this.fragments.get(arg0  ) ;
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	protected  List<Fragment> getFragments() {
		return this.fragments;
	}
	
}
