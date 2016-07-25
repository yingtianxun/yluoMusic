package com.yluo.yluomusic.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.R.drawable;
import com.yluo.yluomusic.R.id;
import com.yluo.yluomusic.R.layout;
import com.yluo.yluomusic.adapter.viewpageradapter.MainContentFragmentAdapter;
import com.yluo.yluomusic.ui.fragment.MainContentFragment;
import com.yluo.yluomusic.ui.fragment.SlideMenuFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerListenFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerLookFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerSingFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;


public class MainActivity extends FragmentActivity {

	
    private CheckBox mCbWifiConnect;
   

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_layout);
        
//        setContentView(R.layout.content);
        // configure the SlidingMenu
//        SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
////        menu.setShadowWidthRes(R.dimen.shadow_width);
////        menu.setShadowDrawable(R.drawable.shadow);
////        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.fragment_slide_menu);
        
        //setContentView(R.layout.main_content_bottom_play);

        setContentView(R.layout.recycle_view_love);

//	      android.support.v4.app.FragmentManager fManager =  this.getSupportFragmentManager();
//	      FragmentTransaction transaction = fManager.beginTransaction();
//	      MainContentFragment contentFragment = new MainContentFragment();
////	      transaction.add(contentFragment, "111");
////	      transaction.replace(R.id.fl_test, slideMenuFragment);
//	      transaction.add(R.id.fl_test, contentFragment);
//	      transaction.commit();

        //setContentView(R.layout.vp_pager_listen);
//        setContentView(R.layout.fragment_slide_menu);
//        mCbWifiConnect = (CheckBox)findViewById(R.id.cb_wifi_connect);
//        
//	mCbWifiConnect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				handleCbWifiOpenOrClose(isChecked);
//			}
//		});
//        android.support.v4.app.FragmentManager fManager =  this.getSupportFragmentManager();
//        FragmentTransaction transaction = fManager.beginTransaction();
//        SlideMenuFragment slideMenuFragment = new SlideMenuFragment();
//        transaction.add(slideMenuFragment, "111");
//        transaction.commit();
        
        
    }
	private void handleCbWifiOpenOrClose(boolean isOpen) {
		
		int bkDrawableID = isOpen ? 
				R.drawable.slide_menu_checkbox_open : 
					R.drawable.slide_menu_checkbox_close;
		
		mCbWifiConnect.setBackgroundResource(bkDrawableID);
		
//		ImageView imageView=new ImageView(null);
//		imageView.setImageResource(R.drawable.slide_menu_checkbox_open);
	}

}
