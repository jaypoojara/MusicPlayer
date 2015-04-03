package com.tagtask.musicplayer.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.service.MusicPlayService;

public class PhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			if (MusicPlayService.getInstance() != null) {
				MusicPlayService.getInstance().pauseMusic();
			}
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().updatePlayButtonIcon(
						R.drawable.play_song, "pause");
			}
		}

		if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			if (MusicPlayService.getInstance() != null) {
				MusicPlayService.getInstance().pauseMusic();
			}
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().updatePlayButtonIcon(
						R.drawable.play_song, "pause");
			}
		}

		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			if (MusicPlayService.getInstance() != null) {
				MusicPlayService.getInstance().playMusic();
			}
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().updatePlayButtonIcon(
						R.drawable.pause_song, "play");
			}
		}
	}

}
