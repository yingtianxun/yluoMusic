package com.yluo.yluomusic.ui.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

@SuppressLint("InlinedApi") public abstract class BaseActivity extends FragmentActivity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Ê¹ÓÃ³Á½þÊ½×´Ì¬À¸
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		
		initUI();
	}
	
	protected abstract void initUI();
	
}
