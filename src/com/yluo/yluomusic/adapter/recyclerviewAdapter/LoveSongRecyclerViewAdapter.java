package com.yluo.yluomusic.adapter.recyclerviewAdapter;

import java.util.HashMap;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.adapter.gridviewadapter.LoveSongGridViewAdapter;
import com.yluo.yluomusic.bean.LoveSongBean;
import com.yluo.yluomusic.ui.activity.MainActivity;
import com.yluo.yluomusic.ui.widget.StrecthLayout2;

import android.R.integer;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoveSongRecyclerViewAdapter extends RecyclerView.Adapter<LoveSongRecyclerViewAdapter.MyViewHolder> {

	private static final String TAG = "LoveSongRecyclerViewAdapter";
	private Context mContext;
	private static int gridViewHeigh = 0;
	private GridView lastGridView = null;
	private List<LoveSongBean> mLoveSongBeans;
	private StrecthLayout2 mLastOpenStrecthLayout2 = null;
	private int mLastOpenPostion = -1;
	private boolean beforeClickFlag = true;
	private HashMap<Integer, Boolean> mMap= new HashMap<Integer, Boolean>();
	public LoveSongRecyclerViewAdapter(Context mContext,
			List<LoveSongBean> loveSongBeans) {
		super();
		this.mContext = mContext;
		mLoveSongBeans = loveSongBeans;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mLoveSongBeans.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

		LoveSongBean loveSongBean = mLoveSongBeans.get(position);

		Log.d(TAG, loveSongBean.toString());
		Log.d(TAG, "holder:" + holder);
		holder.mPosition = position;
		holder.singerTextView.setText(loveSongBean.getSingerName());
		if (loveSongBean.hasMv()) {
			holder.ivMv.setVisibility(View.VISIBLE);
		} else {
			holder.ivMv.setVisibility(View.INVISIBLE);
		}
//		if (position > lastMaxPostion) {
//			
//		}
		
//		LoveSongGridViewAdapter adapter = (LoveSongGridViewAdapter) holder.gvSongHandle.getAdapter();
		
		LayoutParams layoutParams = holder.gvSongHandle.getLayoutParams();
		if(gridViewHeigh == 0) {
			gridViewHeigh = layoutParams.height;
		}
		
		layoutParams.height = 0;
		holder.gvSongHandle.setLayoutParams(layoutParams);
		Boolean isOpenObj = mMap.get(position);
		
		boolean isOpen = isOpenObj == null || !isOpenObj ? false : true;
		
		if (isOpen) {
//			View view = null;
//			if (holder.gvSongHandle.getChildCount() != 0) {
//				view = holder.gvSongHandle.getChildAt(0);
//			} else {
//				view = holder.gvSongHandle.getAdapter().getView(0, null, null);
//			}
//
//			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
//					(1 << 30) - 1, MeasureSpec.AT_MOST);
//			int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
//					(1 << 30) - 1, MeasureSpec.AT_MOST);
//			view.measure(widthMeasureSpec, heightMeasureSpec);
			
			holder.strecthLayout.open();
			
//			LayoutParams layoutParams = holder.gvSongHandle.getLayoutParams();
//			layoutParams.height = view.getMeasuredHeight() * 2;
//			holder.gvSongHandle.setLayoutParams(layoutParams);
		}
		else{
			Log.d(TAG, "shrink");
			holder.strecthLayout.shrink();
//			View view = null;
//			if (holder.gvSongHandle.getChildCount() != 0) {
//				view = holder.gvSongHandle.getChildAt(0);
//			} else {
//				view = holder.gvSongHandle.getAdapter().getView(0, null, null);
//			}
//
//			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
//					(1 << 30) - 1, MeasureSpec.AT_MOST);
//			int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
//					(1 << 30) - 1, MeasureSpec.AT_MOST);
//			view.measure(widthMeasureSpec, heightMeasureSpec);

//			LayoutParams layoutParams = holder.strecthLayout.getLayoutParams();
//			layoutParams.height = holder.strecthLayout.getHeaderHeight();
//			holder.strecthLayout.setLayoutParams(layoutParams);
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.recycle_view_item, parent, false);

		MyViewHolder holder = new MyViewHolder(view);

		LoveSongGridViewAdapter loveSongGridViewAdapter = new LoveSongGridViewAdapter(
				mContext);

		holder.gvSongHandle.setAdapter(loveSongGridViewAdapter);

		
		return holder;
	}

	class MyViewHolder extends ViewHolder {
		public StrecthLayout2 strecthLayout;
		public LinearLayout loveLinearLayout;
		public ImageView loveAddImageView;
		public ImageView loveExpandImageView;
		public TextView songNameTextView;
		public TextView singerTextView;
		public ImageView ivMv;
		public ImageView ivChosen;
		public GridView gvSongHandle;
		public int mPosition = -1;
		private boolean bFlag = true;

		public MyViewHolder(View view) {
			super(view);
			strecthLayout = (StrecthLayout2) view;
			loveLinearLayout = (LinearLayout) view
					.findViewById(R.id.ll_love_play_stop);
			loveAddImageView = (ImageView) view.findViewById(R.id.iv_love_add);
			loveExpandImageView = (ImageView) view
					.findViewById(R.id.iv_love_expand);
			songNameTextView = (TextView) view
					.findViewById(R.id.tv_love_song_name);
			singerTextView = (TextView) view.findViewById(R.id.tv_love_singer);
			ivMv = (ImageView) view.findViewById(R.id.iv_love_mv);
			ivChosen = (ImageView) view.findViewById(R.id.iv_love_chosen);

			gvSongHandle = (GridView) view.findViewById(R.id.gv_song_handle);
			// gvSongHandle.measure(widthMeasureSpec, heightMeasureSpec)

			loveExpandImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


//					View view = null;
//					if (gvSongHandle.getChildCount() != 0) {
//						view = gvSongHandle.getChildAt(0);
//					} else {
//						view = gvSongHandle.getAdapter().getView(0, null, null);
//					}
//
//					int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
//							(1 << 30) - 1, MeasureSpec.AT_MOST);
//					int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
//							(1 << 30) - 1, MeasureSpec.AT_MOST);
//					view.measure(widthMeasureSpec, heightMeasureSpec);
//
					LayoutParams layoutParams = gvSongHandle.getLayoutParams();
					layoutParams.height = gridViewHeigh;
					gvSongHandle.setLayoutParams(layoutParams);
//					Log.d(TAG, "gridViewHeigh=" + gridViewHeigh + "bflag"
//							+ bFlag);
					
					
					if (mLastOpenStrecthLayout2 != null) {
						// 关闭上一个item
						if (mLastOpenStrecthLayout2 == strecthLayout) {
							mLastOpenStrecthLayout2 = null;
							strecthLayout.strecth(gridViewHeigh);
							Log.d(TAG, "关闭自己");
							
							mMap.put(mPosition, false);
						} else {
							mLastOpenStrecthLayout2.strecth(gridViewHeigh);
							mLastOpenStrecthLayout2 = strecthLayout;
							strecthLayout.strecth(gridViewHeigh);
							Log.d(TAG, "先关闭上一个，再打开自己");
							mMap.put(mLastOpenPostion, false);
							
							mMap.put(mPosition, true);
							mLastOpenPostion = mPosition;
						}
					} else {
						// 打开我自己
						mLastOpenStrecthLayout2 = strecthLayout;
						strecthLayout.strecth(gridViewHeigh);
						Log.d(TAG, "之前都已关闭，打开自己");
						mLastOpenPostion = mPosition;
						mMap.put(mPosition, true);
					}
				}
			});
		}

	}
}
