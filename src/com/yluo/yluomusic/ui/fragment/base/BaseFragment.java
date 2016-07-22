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
<<<<<<< HEAD
		
		
		
=======
>>>>>>> 67aa85a3b89fda1b899aa09ae612054c21dc828e
		rootView = createView(inflater);
		
		initUI();
		
		initEvent();
	
		return rootView;
	}
	protected  View createView(LayoutInflater inflater) {
		
		return inflater.inflate(getLayoutId(), null, false);
			
	}
<<<<<<< HEAD
=======
	
>>>>>>> 67aa85a3b89fda1b899aa09ae612054c21dc828e
	protected abstract void initUI() ;
	protected abstract void initEvent();
	protected abstract int getLayoutId();
	
	protected View getRootView() {
		return rootView;
	}
<<<<<<< HEAD
=======
	protected <T> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}
>>>>>>> 67aa85a3b89fda1b899aa09ae612054c21dc828e
}
