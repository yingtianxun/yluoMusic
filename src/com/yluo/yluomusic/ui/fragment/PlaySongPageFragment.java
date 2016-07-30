package com.yluo.yluomusic.ui.fragment;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.presenter.PlayMusicBarPresenter;
import com.yluo.yluomusic.presenter.PlayMusicPagePresenter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.MusicWordView;
import com.yluo.yluomusic.ui.widget.YluoSeekBar;

public class PlaySongPageFragment extends BaseFragment{
	
	private static final String TAG = "PlaySongPageFragment";
	
	private PlayMusicPagePresenter pagePresenter;
	
	private MusicWordView mwvSongwords; //歌词控件
	
	private YluoSeekBar sbSongProgress; // 进度条
	
	private ImageButton ibtnPlayMusic;// 播放按钮
	
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_playing_song;
	}

	@Override
	protected void initUI() {
		adjustViewPaddingTop();// 调整paddingTop
		// 歌词控件
		mwvSongwords = findViewById(R.id.mwv_songwords);
		// 进度条
		sbSongProgress = findViewById(R.id.sb_song_progress);
		//播放按钮
		ibtnPlayMusic = findViewById(R.id.ibtn_play_music);
		
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initEvent() {
		ibtnPlayMusic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onPlayMusic();
				
			}
		});
	}

	@Override
	protected void attchWindow(Context context) {
		if (pagePresenter == null) {
			pagePresenter = new PlayMusicPagePresenter(this);
		}

		pagePresenter.onAttach();
	}

	@Override
	protected void detachWindow() {
		pagePresenter.onDetach();
	}
	
	private void onPlayMusic() {
		
	}
	
	
	
	
}
