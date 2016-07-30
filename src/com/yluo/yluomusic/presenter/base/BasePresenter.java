package com.yluo.yluomusic.presenter.base;

import android.app.Service;
import android.content.Intent;

import com.yluo.yluomusic.service.PlayMusicService;

public abstract class BasePresenter {
	public abstract void onAttach();
	public abstract void onDetach();
}
