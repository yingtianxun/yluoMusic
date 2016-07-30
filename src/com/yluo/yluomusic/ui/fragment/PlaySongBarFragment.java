package com.yluo.yluomusic.ui.fragment;


import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.eventbusmessage.OpenPlayMusicPageMessage;
import com.yluo.yluomusic.presenter.PlayMusicBarPresenter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.CircleImageView;

public class PlaySongBarFragment extends BaseFragment{
	
	private static final String TAG = "PlaySongBarFragment";
	private PlayMusicBarPresenter barPresenter;
	
	private CircleImageView cimvRotateIcon; // 播放时旋转的图片按钮
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_play_music_bar;
	}
	
	@Override
	protected void initConfig() {
		barPresenter = new PlayMusicBarPresenter(this);
	}
	@Override
	protected void attchWindow(Context context) {
		barPresenter.onAttach();
	}

	@Override
	protected void detachWindow() {
		barPresenter.onDetach();
	}


	@Override
	protected void initUI() {
		cimvRotateIcon = findViewById(R.id.cimv_rotate_icon);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initEvent() {
		cimvRotateIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openRotationMusicView();
				
			}
		});
	}
	
	
	private void openRotationMusicView() {
		
		EventBus.getDefault().post(new OpenPlayMusicPageMessage());
	}

}





