package com.yluo.yluomusic.aidl;

import  com.yluo.yluomusic.aidl.WordLine;

interface SongManager {
	 void startPlaySong(String songName);
	
	 void stopPlaySong();
	
	 void changePlaySongProgress(int progress);
	
	 int getSongDuration(String songName);
	 
	 List<WordLine> getSongWordLine(String songName);
	 
	 boolean isPlayingMusic();
	 
	 void pauseMusic();
}
