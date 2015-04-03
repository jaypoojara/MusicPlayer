package com.tagtask.musicplayer.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.service.MusicPlayService;

public class NotificationRemoteviewReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent receivedIntent) {
		String action = receivedIntent.getAction();
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.music_custom_notification);
		if (action.equals(Constants.ACTION_PAUSE)) {
			MusicPlayService.getInstance().pauseMusic();
			remoteViews.setImageViewResource(R.id.ibtnPlayNotification,
					R.drawable.play_song);
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().updatePlayButtonIcon(
						R.drawable.play_song, "play");
			}
			updateNotification(remoteViews, Constants.ACTION_PLAY, context);
		}
		if (action.equals(Constants.ACTION_PLAY)) {
			if (!MusicPlayService.getInstance().isMediaPlayerRunning())
				MusicPlayService.getInstance().playMusic();
			remoteViews.setImageViewResource(R.id.ibtnPlayNotification,
					R.drawable.pause_song);
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().updatePlayButtonIcon(
						R.drawable.pause_song, "pause");
			}
			updateNotification(remoteViews, Constants.ACTION_PAUSE, context);
		}
		if (action.equals(Constants.ACTION_SHUTDOWN)) {
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().finish();
			}
			MusicPlayService.getInstance().stopSong();
			MusicPlayService.getInstance().stopSelf();
		}

		if (action.equals(Constants.ACTION_NEXT)) {
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().playNewSong(
						MusicPlayer.getInstance().getPostion() + 1);
			}
		}
		if (action.equals(Constants.ACTION_PREVIOUS)) {
			if (MusicPlayer.getInstance() != null) {
				MusicPlayer.getInstance().playNewSong(
						MusicPlayer.getInstance().getPostion() - 1);
			}
		}

	}

	public void updateNotification(RemoteViews remoteViews, String action,
			Context context) {
		Intent intent = new Intent(context,
				NotificationRemoteviewReceiver.class);
		intent.setAction(action);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ibtnPlayNotification, pIntent);

		Intent shutdownMusicPlayer = new Intent(context,
				NotificationRemoteviewReceiver.class);
		shutdownMusicPlayer.setAction(Constants.ACTION_SHUTDOWN);

		PendingIntent pShutdownIntent = PendingIntent.getBroadcast(context, 0,
				shutdownMusicPlayer, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ivCancel, pShutdownIntent);

		Intent nextSongIntent = new Intent(context,
				NotificationRemoteviewReceiver.class);
		nextSongIntent.setAction(Constants.ACTION_NEXT);
		PendingIntent pNextSongIntent = PendingIntent.getBroadcast(context, 0,
				nextSongIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ibtnPlayNextNotification,
				pNextSongIntent);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("Music is playing").setOngoing(true)
				.setAutoCancel(false).setContentIntent(pIntent)
				.setContent(remoteViews);
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		manager.notify(1, builder.build());
	}
}
