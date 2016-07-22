package com.yluo.yluomusic.ui.fragment.base;

import android.R.integer;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public  abstract class BaseFragment extends Fragment{
	private View rootView;
	@SuppressLint("InflateParams")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = createView(inflater);
		
		initUI();
		
		initEvent();
	
		return rootView;
	}
	protected  View createView(LayoutInflater inflater) {
		
		return inflater.inflate(getLayoutId(), null, false);
			
	}

	protected abstract void initUI() ;
	protected abstract void initEvent();
	protected abstract int getLayoutId();
	
	protected View getRootView() {
		return rootView;
	}
	protected <T> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}

}
