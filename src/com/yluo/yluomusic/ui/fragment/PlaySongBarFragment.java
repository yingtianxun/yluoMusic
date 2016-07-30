package com.yluo.yluomusic.ui.fragment;


import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.service.PlayMusiceService;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;

public class PlaySongBarFragment extends BaseFragment{
	
	private static final String TAG = "PlaySongBarFragment";
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_play_music_bar;
	}
	
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		// 一开始就绑定服务
		Intent intent = new Intent(getActivity(),PlayMusiceService.class);
		
		getActivity().bindService(
				intent,
				mPlayMusicConnection,
				Service.BIND_AUTO_CREATE);
		
	}

	@Override
	public void onDetach() {
		// 注销服务
		getActivity().unbindService(mPlayMusicConnection);
		super.onDetach();
	}


	@Override
	protected void initUI() {
		
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initEvent() {
		
	}
	
	private ServiceConnection mPlayMusicConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "---绑定服务---成功");
			
		}
	};

}





