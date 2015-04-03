package com.tagtask.musicplayer.adapter;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.fragment.AlbumFragment;
import com.tagtask.musicplayer.fragment.ArtistFragment;
import com.tagtask.musicplayer.fragment.GenreFagment;
import com.tagtask.musicplayer.fragment.SongsFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FilterPagerAdapter extends FragmentPagerAdapter {
	Context mContext;

	public FilterPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return SongsFragment.newInstance();
		case 1:
			return AlbumFragment.newInstance();
		case 2:
			return GenreFagment.newInstance();
		case 3:
			return ArtistFragment.newInstance();
		default:
			break;
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getStringArray(R.array.filter_song)[position];
	}

	@Override
	public int getCount() {
		return 4;
	}

}
