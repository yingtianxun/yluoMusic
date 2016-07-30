package com.yluo.yluomusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PlayMusiceService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}
	
	
	
	public static class MusicBinder extends Binder {
		
	}
	

}
