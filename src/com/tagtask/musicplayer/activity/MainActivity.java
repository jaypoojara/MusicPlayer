package com.tagtask.musicplayer.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.adapter.AllSongsListAdapter;
import com.tagtask.musicplayer.broadcastreceiver.PhoneJackReceiver;

@SuppressLint("NewApi")
public class MainActivity extends SherlockActivity implements
		OnItemClickListener {
	ListView lvAllSongs, lvOptions;
	ArrayList<HashMap<String, String>> allSongsData;
	AllSongsListAdapter adapter;
	LinearLayout llMyLibrary, llMyPlaylist;
	SlidingMenu menu;
	public static MainActivity instance;
	BroadcastReceiver phoneJackReceiver;

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setWidgetReference();
		initialization();
		bindWidgetEvent();
		phoneJackReceiver = new PhoneJackReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(phoneJackReceiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void bindWidgetEvent() {
		lvOptions.setOnItemClickListener(this);
		llMyLibrary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MyLibraryActivity.class));
			}
		});
	}

	private void setWidgetReference() {
		lvOptions = new ListView(MainActivity.this);
		lvOptions.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				getResources().getStringArray(R.array.options)));
		llMyLibrary = (LinearLayout) findViewById(R.id.llMyLibrary);
	}

	private void initialization() {
		getSupportActionBar().setHomeButtonEnabled(true);
		setUpSlidingMenu();

	}

	private void setUpSlidingMenu() {
		menu = new SlidingMenu(MainActivity.this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setMenu(lvOptions);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			menu.toggle();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String action = (String) parent.getItemAtPosition(position);
		if (action.equalsIgnoreCase("my library"))
			startActivity(new Intent(MainActivity.this, MyLibraryActivity.class));
	}
}
