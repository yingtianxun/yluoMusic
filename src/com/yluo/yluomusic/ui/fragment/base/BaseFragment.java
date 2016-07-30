package com.yluo.yluomusic.ui.fragment.base;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public  abstract class BaseFragment extends Fragment{
	private static final String TAG = "BaseFragment";
	private View rootView;
	@SuppressLint("InflateParams")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = createView(inflater);
		
		
		
		initUI();
		initData();
		initEvent();
	
		return rootView;
	}
	protected  View createView(LayoutInflater inflater) {
		
		return inflater.inflate(getLayoutId(), null, false);
			
	}
	protected abstract int getLayoutId();
//	protected abstract void initConfig(); // ≥ı ºªØ≈‰÷√
	protected abstract void initUI() ;
	protected abstract void initData() ;
	protected abstract void initEvent();
	protected abstract void attchWindow(Context context);
	protected abstract void detachWindow();
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		attchWindow( context);
	}
	@Override
	public void onDetach() {
		detachWindow();
		super.onDetach();
	}
	protected View getRootView() {
		return rootView;
	}
	protected <T> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}

}
