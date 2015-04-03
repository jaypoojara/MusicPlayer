package com.tagtask.musicplayer.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.adapter.SongChangeViewPagerAdapter;
import com.tagtask.musicplayer.broadcastreceiver.NotificationRemoteviewReceiver;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.fragment.SongsFragment;
import com.tagtask.musicplayer.service.MusicPlayService;

public class MusicPlayer extends Activity implements OnClickListener,
		OnSeekBarChangeListener, OnPageChangeListener {
	public static MusicPlayer instance;
	public static SeekBar sbSongProgress, sbSetVolume;
	int postion;
	HashMap<String, String> song;
	int duration, currentDuration;
	ImageButton ibtnPlay, ibtnNextSong, ibtnPreviousSong;
	ViewPager vpSongChange;
	AudioManager audioManager;

	public int getPostion() {
		return postion;
	}

	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			int currentPosition = 0;

			while (true) {
				try {
					Thread.sleep(1000);
					currentPosition = MusicPlayService.getInstance()
							.getCurrentPosition();
					if (MusicPlayService.getInstance().state.equals("prepared")) {
						duration = MusicPlayService.getInstance()
								.getMusicDuration();
						sbSongProgress.setMax(duration);
					}
				} catch (InterruptedException e) {
					return;
				} catch (Exception e) {
					return;
				}
				sbSongProgress.setProgress(currentPosition);
			}
		}
	});

	public static MusicPlayer getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player);
		setWidgetReference();
		instance = this;
		initialization();
		bindWidgetEvent();
		thread.start();
	}

	@SuppressWarnings("unchecked")
	private void initialization() {
		setVolumeSeekBar();
		Intent intent = new Intent(getApplicationContext(),
				NotificationRemoteviewReceiver.class);
		intent.setAction(Constants.ACTION_PLAY);
		sendBroadcast(intent);
		song = (HashMap<String, String>) getIntent().getSerializableExtra(
				SongsFragment.SINGLE_SONG);
		postion = getIntent().getExtras().getInt(SongsFragment.POSITION);
		MusicPlayService.setSong(song.get(Constants.DATA), "", "");
		MusicPlayService.getInstance().initMediaPlayer();
		ibtnPlay.setTag("pause");
		duration = MusicPlayService.getInstance().getMusicDuration();
		sbSongProgress.setMax(duration);
		MusicPlayService.getInstance().setPosition(postion);
		vpSongChange
				.setAdapter(new SongChangeViewPagerAdapter(MusicPlayer.this));
		vpSongChange.setCurrentItem(postion);
	}

	private void setVolumeSeekBar() {
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		sbSetVolume.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		sbSetVolume.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
	}

	private void bindWidgetEvent() {
		ibtnPlay.setOnClickListener(this);
		sbSongProgress.setOnSeekBarChangeListener(this);
		ibtnNextSong.setOnClickListener(this);
		ibtnPreviousSong.setOnClickListener(this);
		vpSongChange.setOnPageChangeListener(this);
		sbSetVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);
			}
		});
	}

	private void setWidgetReference() {
		sbSetVolume = (SeekBar) findViewById(R.id.sbSetVolume);
		sbSongProgress = (SeekBar) findViewById(R.id.sbSongProgress);
		ibtnPlay = (ImageButton) findViewById(R.id.bfPlay);
		ibtnNextSong = (ImageButton) findViewById(R.id.bfsNextSong);
		ibtnPreviousSong = (ImageButton) findViewById(R.id.bfsPreviousSong);
		vpSongChange = (ViewPager) findViewById(R.id.vpChangeSong);
	}

	@Override
	public void onClick(View v) {
		if (v == ibtnPlay) {
			Intent intent = new Intent(getApplicationContext(),
					NotificationRemoteviewReceiver.class);
			if (ibtnPlay.getTag().equals("play")) {
				ibtnPlay.setTag("pause");
				intent.setAction(Constants.ACTION_PLAY);
				MusicPlayService.getInstance().playMusic();
				ibtnPlay.setImageDrawable(getResources().getDrawable(
						R.drawable.pause_song));
			} else {
				ibtnPlay.setTag("play");
				intent.setAction(Constants.ACTION_PAUSE);
				MusicPlayService.getInstance().pauseMusic();
				ibtnPlay.setImageDrawable(getResources().getDrawable(
						R.drawable.play_song));
			}
			sendBroadcast(intent);
		}
		if (v == ibtnNextSong) {
			if (++postion < Constants.playList.size())
				playNewSong(postion);
			else {
				Toast.makeText(MusicPlayer.this, "No more songs",
						Toast.LENGTH_SHORT).show();
				postion = Constants.playList.size() - 1;
			}
			vpSongChange.setCurrentItem(postion);
		}
		if (v == ibtnPreviousSong) {
			if (--postion >= 0)
				playNewSong(postion);
			else {
				Toast.makeText(MusicPlayer.this, "No more songs",
						Toast.LENGTH_SHORT).show();
				postion = 0;
			}
			vpSongChange.setCurrentItem(postion);
		}
	}

	public void playNewSong(int postion) {
		if (postion >= 0 && postion < Constants.playList.size()) {
			MusicPlayService.getInstance().stopSong();
			MusicPlayService
					.setSong(Constants.playList.get(postion)
							.get(Constants.DATA), "", "");
			MusicPlayService.getInstance().initMediaPlayer();
			MusicPlayService.getInstance().playMusic();
			vpSongChange.setCurrentItem(postion);
			MusicPlayService.getInstance().setPosition(postion);
			updatePlayButtonIcon(R.drawable.pause_song, "pause");
			Intent intent = new Intent(getApplicationContext(),
					NotificationRemoteviewReceiver.class);
			intent.setAction(Constants.ACTION_PLAY);
			sendBroadcast(intent);
			this.postion = postion;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (ibtnPlay.getTag().equals("play")) {
			MusicPlayService.getInstance().stopSelf();
		}
		thread.interrupt();
	}

	public void updatePlayButtonIcon(int id, String tag) {
		ibtnPlay.setTag(tag);
		ibtnPlay.setImageDrawable(getResources().getDrawable(id));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		MusicPlayService.getInstance().seekMusicTo(seekBar.getProgress());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		playNewSong(arg0);
		postion = arg0;
	}

}
