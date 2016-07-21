package com.yluo.yluomusic;

import com.yluo.yluomusic.ui.fragment.SlideMenuFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class MainActivity extends FragmentActivity {

	
    private CheckBox mCbWifiConnect;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_layout);
        
	      android.support.v4.app.FragmentManager fManager =  this.getSupportFragmentManager();
	      FragmentTransaction transaction = fManager.beginTransaction();
	      SlideMenuFragment slideMenuFragment = new SlideMenuFragment();
//	      transaction.add(slideMenuFragment, "111");
//	      transaction.replace(R.id.fl_test, slideMenuFragment);
	      transaction.add(R.id.fl_test, slideMenuFragment);
	      transaction.commit();
        
        
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
