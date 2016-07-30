package com.yluo.yluomusic.presenter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;

public abstract class BasePlayMusicPresent extends BasePresenter{
		
	private static final String TAG = "BasePlayMusicPresent";
	private Fragment fragment;
	
	protected SongManager songManager;
	
	public BasePlayMusicPresent(Fragment fragment) {
		
		this.fragment = fragment;
	}

	@Override
	public void onAttach() {
	Intent intent = new Intent(fragment.getActivity(),PlayMusicService.class);
		
	fragment.getActivity().bindService(
				intent,
				mPlayMusicConnection,
				Service.BIND_AUTO_CREATE);
	}

	@Override
	public void onDetach() {
		fragment.getActivity().unbindService(mPlayMusicConnection);
	}

	protected ServiceConnection mPlayMusicConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "---绑定服务---成功");

			songManager = (SongManager) service ;//SongManager.Stub.asInterface(service);
		}
	};
	
	public abstract void updateSongDuationTime(int songDuration);
	public void playMusic(String songName) {
		try {
			int songDuration = songManager.startPlaySong("无字碑");
			Log.d(TAG, "---时间:" + songDuration);
			updateSongDuationTime(songDuration);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public abstract void updateSongLrc ( ArrayList<WordLine> wordLines);
	
	class SongLrcReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			 ArrayList<WordLine> wordLines = intent.getParcelableArrayListExtra("wordLines");
			 updateSongLrc(wordLines);
			 
			 
		}
	}
	
	public abstract void updateSongProgress(int progress);
	class SongProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateSongProgress(intent.getIntExtra("curProgress", -1));
			
		}
	}
}
