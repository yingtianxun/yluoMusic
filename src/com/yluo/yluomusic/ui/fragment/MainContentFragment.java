package com.yluo.yluomusic.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.adapter.viewpageradapter.MainContentFragmentAdapter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;

public class MainContentFragment extends BaseFragment{
	private List<Fragment> fragmentslist;
	private ViewPager maincontentPager;
	private ViewPagerListenFragment viewPagerListenFragment;
	private ViewPagerLookFragment viewPagerLookFragment;
	private ViewPagerSingFragment viewPagerSingFragment;
	@Override
	protected void initUI() {
        fragmentslist = new ArrayList<Fragment>(3);
        maincontentPager = (ViewPager) findViewById(R.id.vp_maincontent);
	     viewPagerListenFragment = new ViewPagerListenFragment();
	     viewPagerLookFragment = new ViewPagerLookFragment();
	     viewPagerSingFragment = new ViewPagerSingFragment();
       
	     fragmentslist.add(viewPagerListenFragment);
	     fragmentslist.add(viewPagerLookFragment);
	     fragmentslist.add(viewPagerSingFragment);
	     
	     FragmentManager fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
	     MainContentFragmentAdapter adapter=new MainContentFragmentAdapter(fm, fragmentslist);
	     
	     maincontentPager.setAdapter(adapter);
	     maincontentPager.setCurrentItem(0);
	     maincontentPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		
		return R.layout.fragment_main_content;
	}

}
