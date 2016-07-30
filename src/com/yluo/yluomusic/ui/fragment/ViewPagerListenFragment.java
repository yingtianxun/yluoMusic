package com.yluo.yluomusic.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.utils.DpTranToPx;

public class ViewPagerListenFragment extends BaseFragment {

	private LinearLayout llILove;  // ��ϲ��

	private LinearLayout llSongList; //�赥

	private LinearLayout llDownload; // ����

	private LinearLayout llRecent; // ���
	
	private LinearLayout llMusicLibrary;  // �ֿ�
	
	private LinearLayout llRadio;  // ��̨
	
	private LinearLayout llCoolGroup;  // ��Ⱥ

	@Override
	protected int getLayoutId() {
		return R.layout.vp_pager_listen;
	}

	@SuppressLint("NewApi")
	@Override
	protected void initUI() {
		// ��ϲ��
		llILove = findViewById(R.id.ll_i_love);

		// �赥
		llSongList = findViewById(R.id.ll_song_list);

		// ����
		llDownload = findViewById(R.id.ll_download);

		// ���
		llRecent = findViewById(R.id.ll_recent);
		
		// �ֿ�
		llMusicLibrary = findViewById(R.id.ll_music_library);
		
		// ��̨
		llRadio = findViewById(R.id.ll_radio);
		
		// ��Ⱥ
		llCoolGroup = findViewById(R.id.ll_coolgroup);
	}

	private void setTextViewDrawable(TextView txView, int resourceId,int size) {
		Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId);

		drawable.setBounds(0, 0, DpTranToPx.dp2px(getContext(), 50),
				DpTranToPx.dp2px(getContext(), 50));

		txView.setCompoundDrawables(null, drawable, null, null);// ֻ�����
	}

	@Override
	protected void initEvent() {
		llILove.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		llSongList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		llDownload.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});

		llRecent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		llMusicLibrary.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		llRadio.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});
		
		llCoolGroup.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});
	}

	@Override
	protected void initData() {

	}



	@Override
	protected void attchWindow(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void detachWindow() {
		// TODO Auto-generated method stub
		
	}


}
