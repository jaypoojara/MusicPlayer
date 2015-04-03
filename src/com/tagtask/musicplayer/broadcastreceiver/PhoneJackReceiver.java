package com.tagtask.musicplayer.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.service.MusicPlayService;

public class PhoneJackReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			int state = intent.getIntExtra("state", -1);
			switch (state) {
			case 0:
				if (MusicPlayService.getInstance() != null) {
					MusicPlayService.getInstance().pauseMusic();
				}
				if (MusicPlayer.getInstance() != null) {
					MusicPlayer.getInstance().updatePlayButtonIcon(
							R.drawable.play_song, "pause");
				}
				break;
			case 1:
				if (MusicPlayService.getInstance() != null) {
					if (!MusicPlayService.getInstance().isMediaPlayerRunning())
						MusicPlayService.getInstance().playMusic();
				}
				if (MusicPlayer.getInstance() != null) {
					MusicPlayer.getInstance().updatePlayButtonIcon(
							R.drawable.pause_song, "play");
				}
				break;
			default:
			}

		}
	}
}
