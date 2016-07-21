package com.yluo.yluomusic.ui.fragment;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.R.id;
import com.yluo.yluomusic.R.layout;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

public class SlideMenuFragment extends BaseFragment{

	private CheckBox mCbWifiConnect;
	private CheckBox mCbDeskLyric;
	private CheckBox mCbLockLyric;
	
	protected void initEvent() {
//		mCbWifiConnect.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				handleCbWifiOpenOrClose();
//			}
//		});
		mCbWifiConnect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				handleCbWifiOpenOrClose(isChecked);
			}
		});
		
	}
	protected void initUI() {
		mCbWifiConnect = (CheckBox) getRootView().findViewById(R.id.cb_wifi_connect);
		mCbDeskLyric = (CheckBox)getRootView().findViewById(R.id.cb_desk_lyric);
		mCbLockLyric = (CheckBox)getRootView().findViewById(R.id.cb_lock_lyric);
	}
	private void handleCbWifiOpenOrClose(boolean isOpen) {
		
		int bkDrawableID = isOpen ? 
				R.drawable.slide_menu_checkbox_open : 
					R.drawable.slide_menu_checkbox_close;
		
		mCbWifiConnect.setBackgroundResource(bkDrawableID);
		
//		ImageView imageView=new ImageView(null);
//		imageView.setImageResource(R.drawable.slide_menu_checkbox_open);
	}
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_slide_menu;
	}
	
	
	
	
}




















