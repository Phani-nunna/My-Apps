
package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.VideosRotatorAdapter;
import com.esainc.lib.adapters.YoutubeAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.viewpagerindicator.CirclePageIndicator;
public class YoutubeFragment extends SherlockListFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Youtube Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	private static int mPos;
	private static boolean mTeams;
	private JSONArray mVideos = new JSONArray();
	private JSONArray mVideosTop = new JSONArray();
	private ProgressDialog pd = null;
	private boolean mCrop = false;
	private int mRotatorMax = 5;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		mCrop = Boolean.valueOf(tabInfo[7]).booleanValue();
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mVideos = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mVideos = new JSONArray(savedInstanceState.getString("data"));
					mVideosTop = new JSONArray(savedInstanceState.getString("dataTop"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mVideos = new JSONArray();
				mVideosTop = new JSONArray();
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.loading));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
		}
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.headlines, menu);
		
		if (!mTeams) {
			((MainActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		} else {
			((TeamsActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			Logger.i(LOG_TAG, "Refreshing Playlist");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger;
			if (!mTeams) {
				dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			} else {
				dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
			}
			dataIntent.putExtra("type", DataType.Youtube.ordinal());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("tabPos", mPos);
			getActivity().startService(dataIntent);
			if (!mTeams) {
				((MainActivity) getActivity()).setRefreshActionButtonState(true);
			} else {
				((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
			}
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_youtube, container, false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
		
			if (mVideosTop == null || mVideosTop.length() == 0) {
				try {
					mVideosTop = new JSONArray();
					JSONArray temp = new JSONArray();
					for (int i = 0; i < mVideos.length(); i++) {
						if (i < mRotatorMax) {
							mVideosTop.put(mVideos.get(i));
						} else {
							temp.put(mVideos.get(i));
						}
					}
					mVideos = temp;
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error splitting off top " + mRotatorMax, e);
				}
			}
			mPager = (ViewPager) view.findViewById(R.id.youtube_rotator);
			mPager.setOffscreenPageLimit(((mRotatorMax <= 5) ? mRotatorMax - 1 : 5));
			mPager.setAdapter(new VideosRotatorAdapter(this, getActivity(), mVideosTop, mCrop));
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				if (!mCrop) {
					mPager.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				} else {
					mPager.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				}
			} else {
				// make sure pager isn't too tall - constrain to half display height
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int displayHeight = displaymetrics.heightPixels;
				if (!mCrop) {
					mPager.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				} else {
					mPager.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				}
			}
			mIndicator = (CirclePageIndicator) view.findViewById(R.id.youtube_indicator);
			if(mVideosTop.length()>0) mIndicator.setViewPager(mPager);
		}
		
		setListAdapter(new YoutubeAdapter(getSherlockActivity(), mVideos, mCrop));
		getListView().setOnItemClickListener(this);
		getListView().setEmptyView(getActivity().findViewById(android.R.id.empty));
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mVideos.toString());
		outState.putString("dataTop", mVideosTop.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Videos");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (refreshTimer != null) refreshTimer.cancel();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!mTeams) {
			getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
			String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
			if (!((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
				getSherlockActivity().getSupportActionBar().setTitle(tabInfo[4]);	// Phone Tab Title
			} else {
				getSherlockActivity().getSupportActionBar().setTitle(tabInfo[5]);	// Tablet Tab Title
			}
			tabs.recycle();
		} else {
			getSherlockActivity().getSupportActionBar().setTitle(TeamsActivity.mSportName);
			TeamsActivity.stacked = false;
		}
	}
	
	public void setData(String data) {
		try {
			mVideos = new JSONArray(data);
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
				mVideosTop = new JSONArray();
				try {
					JSONArray temp = new JSONArray();
					for (int i = 0; i < mVideos.length(); i++) {
						if (i < mRotatorMax) {
							mVideosTop.put(mVideos.get(i));
						} else {
							temp.put(mVideos.get(i));
						}
					}
					mVideos = temp;
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error splitting off top " + mRotatorMax, e);
				}
				((VideosRotatorAdapter) mPager.getAdapter()).setData(mVideosTop);				
				mIndicator.setViewPager(mPager);
			}
			
			((YoutubeAdapter) getListAdapter()).setData(mVideos);
			setAutoRefresh();
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	// Automatically refresh data after set amount of time
	private void setAutoRefresh() {
		if (refreshTimer != null) refreshTimer.cancel();
		refreshTimer = new Timer();
		refreshTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Logger.i(LOG_TAG, "Refreshing Playlist");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger;
							if (!mTeams) {
								dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							} else {
								dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
							}
							dataIntent.putExtra("type", DataType.Youtube.ordinal());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("tabPos", mPos);
							getActivity().startService(dataIntent);
						}
					});
				} catch (NullPointerException e) {
					Logger.e(LOG_TAG, "AutoRefresh Failed", e);
				}
			}
		}, refreshTime * 1000);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// Load Video
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + view.getTag().toString())));
	}
}
