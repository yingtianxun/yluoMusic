package com.yluo.yluomusic.ui.activity;


import com.yluo.yluomusic.R;
import com.yluo.yluomusic.ui.activity.base.BaseActivity;

import android.widget.CheckBox;


public class MainActivity extends BaseActivity {

	
    private CheckBox mCbWifiConnect;
   
	@Override
	protected void initUI() {
		
		setContentView(R.layout.activity_layout);
		
		
		
	}

        
        
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
       
   
	private void handleCbWifiOpenOrClose(boolean isOpen) {
		
		int bkDrawableID = isOpen ? 
				R.drawable.slide_menu_checkbox_open : 
					R.drawable.slide_menu_checkbox_close;
		
		mCbWifiConnect.setBackgroundResource(bkDrawableID);

	}


}
