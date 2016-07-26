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
import com.yluo.yluomusic.ui.widget.HideSlideMenuLayout;
import com.yluo.yluomusic.ui.widget.SacleSlideMenuLayout;
import com.yluo.yluomusic.ui.widget.ShowSongWordView;

public class MainContentFragment extends BaseFragment {
	private List<Fragment> fragmentslist;
	private ViewPager maincontentPager;

	private SacleSlideMenuLayout slMainContent;
	
	private ShowSongWordView sv_songword;
	
	private HideSlideMenuLayout hlSubMenu;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main_content;
	}

	@Override
	protected void initUI() {
		maincontentPager =  findViewById(R.id.vp_maincontent);
		
		slMainContent = findViewById(R.id.sl_main_content);
		
		// 设置做菜单
		slMainContent.setLeftMenuView(R.layout.fragment_slide_menu);
		
		// 设置歌词
		sv_songword =  findViewById(R.id.sv_songword);
        
		// 设置歌词文件
        sv_songword.setSongWords(R.raw.wuzibei);
        
        // 设置子菜单
        hlSubMenu = findViewById(R.id.hl_sub_menu);
        
        hlSubMenu.setRightMenuView(R.layout.test);
		
	}

	@Override
	protected void initData() {
		fragmentslist = new ArrayList<Fragment>(3);
	

		fragmentslist.add(new ViewPagerListenFragment());
		fragmentslist.add(new ViewPagerLookFragment());
		fragmentslist.add(new ViewPagerSingFragment());
		
		FragmentManager fm = ((FragmentActivity) getContext())
				.getSupportFragmentManager();
		MainContentFragmentAdapter adapter = new MainContentFragmentAdapter(fm,
				fragmentslist);

		maincontentPager.setAdapter(adapter);
		maincontentPager.setCurrentItem(0);

	}

	@Override
	protected void initEvent() {
		maincontentPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

}
