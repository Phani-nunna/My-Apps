package com.esainc.lib.ui;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class SoundFragment extends SherlockFragment {
	private static final String LOG_TAG = "Teams Fragment";
	private Tracker tracker;
	private static int mPos;
	private LinearLayout llSound;
	private MediaPlayer player;
	private AssetFileDescriptor assetFile;
	private String soundFile;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPos = getArguments().getInt("pos");
		return inflater.inflate(R.layout.activity_sound, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		String bgImg = tabInfo[6];
		soundFile = tabInfo[7];
		tabs.recycle();

		llSound = (LinearLayout) view.findViewById(R.id.llSound);
		llSound.setBackgroundResource(getResources().getIdentifier(bgImg, "drawable", getActivity().getPackageName()));
		llSound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (player.isPlaying()) {
					player.stop();
				}
				callMediaPlayer();
				player.start();
			}
		});
		
		callMediaPlayer();
		player.start();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("University sound page");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		player.stop();
	}
	
//	@Override
//	public void onConfigurationChanged(Configuration newCOnfig) {
//		super.onConfigurationChanged(newConfig);
//		setBackground();
//	}
	
	private void callMediaPlayer() {
		// Read the music file from the asset folder
		try {
			assetFile = getActivity().getAssets().openFd(soundFile);
			// Create new media player
			player = new MediaPlayer();
			// Set the player music source
			player.setDataSource(assetFile.getFileDescriptor(), assetFile.getStartOffset(), assetFile.getLength());
			player.prepare();
		} catch (IOException e) {
			Logger.e(LOG_TAG, "Error getting sound", e);
		}
	}
}
