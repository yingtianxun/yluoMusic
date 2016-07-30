package com.yluo.yluomusic.ui.fragment.base;

import com.yluo.yluomusic.utils.StatusBarUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	private static final String TAG = "BaseFragment";
	private View rootView;
	private boolean mIsAdjust = false;
	private Handler handler = new Handler();

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

	protected View createView(LayoutInflater inflater) {

		return inflater.inflate(getLayoutId(), null, false);

	}

	protected abstract int getLayoutId();

	protected abstract void initUI();

	protected abstract void initData();

	protected abstract void initEvent();

	protected abstract void attchWindow(Context context);

	protected abstract void detachWindow();

	protected void adjustViewPaddingTop() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				rootView.setPadding(
						rootView.getPaddingLeft(),
						rootView.getPaddingTop()
								+ StatusBarUtil
										.getStatusBarHeight(getContext()),
						rootView.getPaddingRight(), rootView.getPaddingBottom());

			}
		}, 200);

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		attchWindow(context);
	}

	@Override
	public void onDetach() {
		detachWindow();
		super.onDetach();
	}

	protected View getRootView() {
		return rootView;
	}

	@SuppressWarnings("unchecked")
	protected <T> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}

}
