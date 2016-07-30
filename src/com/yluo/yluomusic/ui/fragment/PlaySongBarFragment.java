package com.yluo.yluomusic.ui.fragment;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.eventbusmessage.OpenPlayMusicPageMessage;
import com.yluo.yluomusic.presenter.PlayMusicBarPresenter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.CircleImageView;
import com.yluo.yluomusic.ui.widget.ShowSongWordView;
import com.yluo.yluomusic.ui.widget.YluoSeekBar;
import com.yluo.yluomusic.utils.ToastUtil;

public class PlaySongBarFragment extends BaseFragment {

	private static final String TAG = "PlaySongBarFragment";
	
	private PlayMusicBarPresenter barPresenter;

	private CircleImageView cimvRotateIcon; // ����ʱ��ת��ͼƬ��ť

	private ShowSongWordView sVsongword; // ��ʾ��ʵ�

	private YluoSeekBar seekBarSongProgress; // ������

	private TextView tvSongName; // ����

	private TextView tvSingerName; // ������

	private ImageButton iBtnPlaySong; // ���Ű�ť

	private ImageButton iBtnNextOne; // ��һ��

	private ImageButton iBtnShowCurSongList; // ��ʾ��ǰ�����б��

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_play_music_bar;
	}

	@Override
	protected void attchWindow(Context context) {
		if (barPresenter == null) {
			barPresenter = new PlayMusicBarPresenter(this);
		}

		barPresenter.onAttach();
	}

	@Override
	protected void detachWindow() {
		barPresenter.onDetach();
	}

	@Override
	protected void initUI() {
		// ��ת�İ�ť
		cimvRotateIcon = findViewById(R.id.cimv_rotate_icon);
		// ���ø��
		sVsongword = findViewById(R.id.sv_songword);
		// ���Ž�����
		seekBarSongProgress = findViewById(R.id.sb_song_progress);
		// ����
		tvSongName = findViewById(R.id.tv_song_name);
		// ������
		tvSingerName = findViewById(R.id.tv_singer_name);
		// ���Ű�ť
		iBtnPlaySong = findViewById(R.id.ibtn_play_song);
		// ��һ��
		iBtnNextOne = findViewById(R.id.ibtn_next_one);
		// ��ʾ��ǰ�����б�
		iBtnShowCurSongList = findViewById(R.id.ibtn_show_cur_song_list);
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

		iBtnPlaySong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onPlayMusic();
			}
		});

		iBtnNextOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNextSong();
			}
		});
	}

	private void openRotationMusicView() {
		EventBus.getDefault().post(new OpenPlayMusicPageMessage());
	}

	private void onPlayMusic() {
		barPresenter.playMusic();
	}

	private void onNextSong() {
		barPresenter.nextSong();
	}
	
	// ������ʱ��
	public void onUpdateSongDuationTime(int songDurationTime) {
		if(songDurationTime == 0) {
			return;
		}
		seekBarSongProgress.setMax(songDurationTime);
		
		cimvRotateIcon.startRotation();
		
		sVsongword.setSongDuration(songDurationTime);
	}
	
	// ���²��Ž���
	public void onUpdateSongProgress(int curProgress) {
		if(curProgress == -1) {
			return;
		}
		seekBarSongProgress.setProgress(curProgress);
		
		sVsongword.setCurPlayTime(curProgress);
	}
	public void onUpdateSongLrc(List<WordLine> wordLines) {
		if(wordLines == null) {
			return;
		}
		
		Log.d(TAG, "---���:" + wordLines.size());
		
		sVsongword.setSongWords(wordLines);
	}

}
