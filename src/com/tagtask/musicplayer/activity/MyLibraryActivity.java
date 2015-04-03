package com.tagtask.musicplayer.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.widget.RemoteViews;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.adapter.FilterPagerAdapter;
import com.tagtask.musicplayer.broadcastreceiver.NotificationRemoteviewReceiver;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.service.MusicPlayService;
import com.tagtask.musicplayer.utils.PagerSlidingTabStrip;

public class MyLibraryActivity extends SherlockFragmentActivity {
	PagerSlidingTabStrip tabs;
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_library);
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new FilterPagerAdapter(getSupportFragmentManager(),
				this));
		tabs.setViewPager(pager);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (MusicPlayService.getInstance() != null) {
			MusicPlayService.getInstance().startForeground(1,
					showNotification(getApplicationContext()));
		}
	}

	private Notification showNotification(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.music_custom_notification);

		Intent intent = new Intent(context,
				NotificationRemoteviewReceiver.class);
		intent.setAction(Constants.ACTION_PAUSE);
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

		Intent previousSongIntent = new Intent(context,
				NotificationRemoteviewReceiver.class);
		previousSongIntent.setAction(Constants.ACTION_PREVIOUS);
		PendingIntent pPreviousSongIntent = PendingIntent.getBroadcast(context,
				0, previousSongIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ibtnPlayPreviousNoti,
				pPreviousSongIntent);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("Music is playing").setOngoing(true)
				.setAutoCancel(false).setContentIntent(pPreviousSongIntent)
				.setContent(remoteViews);
		return builder.build();
	}
}
