package com.tagtask.musicplayer.service;

import java.io.IOException;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.fragment.SongsFragment;

public class MusicPlayService extends Service implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
	MediaPlayer mMediaPlayer;
	HashMap<String, String> song;
	int position;
	boolean isNewRequest;
	public String state;
	private static MusicPlayService mInstance;
	static String songUrl, songTitle, albumArtUrl;
	private int mBufferPosition;

	public void setIsNewRequest(boolean newRequest) {
		isNewRequest = true;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public static MusicPlayService getInstance() {
		return mInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			String action = intent.getAction();
			song = (HashMap<String, String>) intent
					.getSerializableExtra(SongsFragment.SINGLE_SONG);
			songUrl = song.get(Constants.DATA);
			Log.d("action", action);
			if (action.equals(Constants.ACTION_PLAY))
				processPlayRequest();
			if (action.equals(Constants.ACTION_PAUSE))
				pauseMusic();

		} catch (NullPointerException e) {
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	private void processPlayRequest() {
		mMediaPlayer = new MediaPlayer(); // initialize it here
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		initMediaPlayer();
	}

	private void setBufferPosition(int progress) {
		mBufferPosition = progress;
	}

	public void initMediaPlayer() {
		try {
			mMediaPlayer.setDataSource(songUrl);
			mMediaPlayer.prepareAsync();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			e.printStackTrace();
		}

	}

	public static void setSong(String url, String title, String albumArtUrl) {
		songUrl = url;
		songTitle = title;
		MusicPlayService.albumArtUrl = albumArtUrl;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		setBufferPosition(percent * getMusicDuration() / 100);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	public void pauseMusic() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
		}
	}

	public MediaPlayer getmMediaPlayer() {
		return mMediaPlayer;
	}

	public HashMap<String, String> getSong() {
		return song;
	}

	public static String getSongUrl() {
		return songUrl;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public static String getAlbumArtUrl() {
		return albumArtUrl;
	}

	public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
		this.mMediaPlayer = mMediaPlayer;
	}

	public void setSong(HashMap<String, String> song) {
		this.song = song;
	}

	public static void setSongUrl(String songUrl) {
		MusicPlayService.songUrl = songUrl;
	}

	public static void setSongTitle(String songTitle) {
		MusicPlayService.songTitle = songTitle;
	}

	public static void setAlbumArtUrl(String albumArtUrl) {
		MusicPlayService.albumArtUrl = albumArtUrl;
	}

	public int getMusicDuration() {
		return mMediaPlayer.getDuration();
	}

	public int getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}

	public int getBufferPercentage() {
		return mBufferPosition;
	}

	public void seekMusicTo(int pos) {
		mMediaPlayer.seekTo(pos);

	}

	public void playMusic() {
		mMediaPlayer.start();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		state = "prepared";
	}

	public boolean isMediaPlayerRunning() {
		return mMediaPlayer.isPlaying() ? true : false;
	}

	public void stopSong() {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
	}

}
