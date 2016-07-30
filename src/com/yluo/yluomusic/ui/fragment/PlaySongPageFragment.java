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
	
	private MusicWordView mwvSongwords; //��ʿؼ�
	
	private YluoSeekBar sbSongProgress; // ������
	
	private ImageButton ibtnPlayMusic;// ���Ű�ť
	
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_playing_song;
	}

	@Override
	protected void initUI() {
		adjustViewPaddingTop();// ����paddingTop
		// ��ʿؼ�
		mwvSongwords = findViewById(R.id.mwv_songwords);
		// ������
		sbSongProgress = findViewById(R.id.sb_song_progress);
		//���Ű�ť
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
