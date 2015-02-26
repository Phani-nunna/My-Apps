package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.ScoreboardAdapter;
import com.esainc.lib.adapters.ScoreboardAdapter.Scoreboards;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class ScoreboardFragment extends SherlockListFragment {
	private static final String LOG_TAG = "Scoreboard Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	public static int mRecentPos;
	public static int mTodayPos;
	public static int mUpcomingPos;
	private JSONArray mRecent = new JSONArray();
	private JSONArray mToday = new JSONArray();
	private JSONArray mUpcoming = new JSONArray();
	private JSONObject mExtra = new JSONObject();
	private static ScoreboardFragment instance;
	private Button btnRecent;
	private Button btnToday;
	private Button btnUpcoming;
	private ProgressDialog pd = null;
	private static boolean mTeams;
	private boolean recentLoaded;
	private boolean todayLoaded;
	private boolean upcomingLoaded;
	private boolean extraLoaded;
	private Scoreboards mCurrentScoreboard = Scoreboards.Recent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		instance = this;
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		mRecentPos = getArguments().getInt("recentPos");
		mTodayPos = getArguments().getInt("todayPos");
		mUpcomingPos = getArguments().getInt("upcomingPos");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		if (!TextUtils.isEmpty(getArguments().getString("recent"))) {
			try {
				mRecent = new JSONArray(getArguments().getString("recent"));
				recentLoaded = true;
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mRecent = new JSONArray(savedInstanceState.getString("recent"));
					recentLoaded = true;
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mRecent = new JSONArray();
				recentLoaded = false;
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.loading));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
		}
		if (!TextUtils.isEmpty(getArguments().getString("today"))) {
			try {
				mToday = new JSONArray(getArguments().getString("today"));
				todayLoaded = true;
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mToday = new JSONArray(savedInstanceState.getString("today"));
					todayLoaded = true;
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mToday = new JSONArray();
				todayLoaded = false;
			}
		}
		if (!TextUtils.isEmpty(getArguments().getString("upcoming"))) {
			try {
				mUpcoming = new JSONArray(getArguments().getString("upcoming"));
				upcomingLoaded = true;
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mUpcoming = new JSONArray(savedInstanceState.getString("upcoming"));
					upcomingLoaded = true;
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mUpcoming = new JSONArray();
				upcomingLoaded = false;
			}
		}
		if (!TextUtils.isEmpty(getArguments().getString("extra"))) {
			try {
				mExtra = new JSONObject(getArguments().getString("extra"));
				extraLoaded = true;
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mExtra = new JSONObject(savedInstanceState.getString("extra"));
					extraLoaded = true;
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mExtra = new JSONObject();
				extraLoaded = false;
			}
		}
		if (savedInstanceState != null) {
			mCurrentScoreboard = Scoreboards.values()[savedInstanceState.getInt("current")];
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.scoreboard, menu);
		
		((MainActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			Logger.i(LOG_TAG, "Refreshing Scoreboard Recent");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.ScoreboardRecent.ordinal());
			dataIntent.putExtra("tabPos", mRecentPos);
			getActivity().startService(dataIntent);
			
			Logger.i(LOG_TAG, "Refreshing Scoreboard Today");
			dataIntent = new Intent(getActivity(), DataLoader.class);
			dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.ScoreboardToday.ordinal());
			dataIntent.putExtra("tabPos", mTodayPos);
			getActivity().startService(dataIntent);
			
			Logger.i(LOG_TAG, "Refreshing Scoreboard Upcoming");
			dataIntent = new Intent(getActivity(), DataLoader.class);
			dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.ScoreboardUpcoming.ordinal());
			dataIntent.putExtra("tabPos", mUpcomingPos);
			getActivity().startService(dataIntent);
			
			((MainActivity) getActivity()).setRefreshActionButtonState(true);
			((MainActivity) getActivity()).recentRefreshed = false;
			((MainActivity) getActivity()).todayRefreshed = false;
			((MainActivity) getActivity()).upcomingRefreshed = false;
			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_scoreboard, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setSymbolDisplay();
		
		setListAdapter(new ScoreboardAdapter(getSherlockActivity(), mRecent, mToday, mUpcoming, mCurrentScoreboard, mTeams));
		btnRecent = (Button) view.findViewById(R.id.btnRecent);
		btnRecent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setScoreboard(Scoreboards.Recent);
			}
		});
		btnToday = (Button) view.findViewById(R.id.btnToday);
		btnToday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setScoreboard(Scoreboards.Today);
			}
		});
		btnUpcoming = (Button) view.findViewById(R.id.btnUpcoming);
		btnUpcoming.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setScoreboard(Scoreboards.Upcoming);
			}
		});
		setScoreboard(mCurrentScoreboard);
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("recent", mRecent.toString());
		outState.putString("today", mToday.toString());
		outState.putString("upcoming", mUpcoming.toString());
		outState.putString("extra", mExtra.toString());
		outState.putInt("current", mCurrentScoreboard.ordinal()); 
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Scoreboard");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (refreshTimer != null) refreshTimer.cancel();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		if (!((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
			getSherlockActivity().getSupportActionBar().setTitle(tabInfo[4]);	// Phone Tab Title
		} else {
			getSherlockActivity().getSupportActionBar().setTitle(tabInfo[5]);	// Tablet Tab Title
		}
		tabs.recycle();
	}
	
	public void setData(String data, Scoreboards scoreboard) {
		try {
			switch (scoreboard) {
				case Recent:
					mRecent = new JSONArray(data);
					recentLoaded = true;
					((ScoreboardAdapter) getListAdapter()).setData(mRecent, scoreboard);
					break;
				case Today:
					mToday = new JSONArray(data);
					todayLoaded = true;
					((ScoreboardAdapter) getListAdapter()).setData(mToday, scoreboard);
					break;
				case Upcoming:
					mUpcoming = new JSONArray(data);
					upcomingLoaded = true;
					((ScoreboardAdapter) getListAdapter()).setData(mUpcoming, scoreboard);
					break;
			}
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public void setExtra(String data) {
		try {
			mExtra = new JSONObject(data);
			extraLoaded = true;
			setSymbolDisplay();
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Extra", e);
		}
	}
	
	private void setSymbolDisplay() {
		if (extraLoaded) {
			TextView txtConference = (TextView) getActivity().findViewById(R.id.txtConference);
			TextView txtScrimmage = (TextView) getActivity().findViewById(R.id.txtScrimmage);
			TextView txtExhibition = (TextView) getActivity().findViewById(R.id.txtExhibition);
			
			try {
				txtConference.setText(mExtra.getString(getResources().getString(R.string.tag_conference)) + " " + getResources().getString(R.string.conferenceTxt));
				txtScrimmage.setText(mExtra.getString(getResources().getString(R.string.tag_scrimmage)) + " "  + getResources().getString(R.string.scrimmageTxt));
				txtExhibition.setText(mExtra.getString(getResources().getString(R.string.tag_exhibition)) + " "  + getResources().getString(R.string.exhibitionTxt));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error getting keys", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error getting keys", e);
			}
		}
	}
	
	public static ScoreboardFragment getInstance() {
		return instance;
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
							Logger.i(LOG_TAG, "Refreshing Scoreboard Recent");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.ScoreboardRecent.ordinal());
							dataIntent.putExtra("tabPos", mRecentPos);
							getActivity().startService(dataIntent);
							
							Logger.i(LOG_TAG, "Refreshing Scoreboard Recent");
							dataIntent = new Intent(getActivity(), DataLoader.class);
							dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.ScoreboardToday.ordinal());
							dataIntent.putExtra("tabPos", mTodayPos);
							getActivity().startService(dataIntent);
							
							Logger.i(LOG_TAG, "Refreshing Scoreboard Recent");
							dataIntent = new Intent(getActivity(), DataLoader.class);
							dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.ScoreboardUpcoming.ordinal());
							dataIntent.putExtra("tabPos", mUpcomingPos);
							getActivity().startService(dataIntent);
						}
					});
				} catch (NullPointerException e) {
					Logger.e(LOG_TAG, "AutoRefresh Failed", e);
				}
			}
		}, refreshTime * 1000);
	}
	
	private void setScoreboard(Scoreboards scoreboard) {
		mCurrentScoreboard = scoreboard;
		((ScoreboardAdapter) getListAdapter()).switchScoreboard(scoreboard);
		switch (scoreboard) {
			case Recent:
				btnRecent.setBackgroundResource(R.drawable.sb_tab_active);
				btnRecent.setTextColor(getResources().getColor(R.color.scoreboardButtonsActiveText));
				btnToday.setBackgroundResource(0);
				btnToday.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				btnUpcoming.setBackgroundResource(0);
				btnUpcoming.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				
				if (!recentLoaded && pd == null) {
					pd = new ProgressDialog(getActivity());
					pd.setMessage(getResources().getString(R.string.loading));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
				break;
			case Today:
				btnRecent.setBackgroundResource(0);
				btnRecent.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				btnToday.setBackgroundResource(R.drawable.sb_tab_active);
				btnToday.setTextColor(getResources().getColor(R.color.scoreboardButtonsActiveText));
				btnUpcoming.setBackgroundResource(0);
				btnUpcoming.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				
				if (!todayLoaded && pd == null) {
					pd = new ProgressDialog(getActivity());
					pd.setMessage(getResources().getString(R.string.loading));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
				break;
			case Upcoming:
				btnRecent.setBackgroundResource(0);
				btnRecent.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				btnToday.setBackgroundResource(0);
				btnToday.setTextColor(getResources().getColor(R.color.scoreboardButtonsText));
				btnUpcoming.setBackgroundResource(R.drawable.sb_tab_active);
				btnUpcoming.setTextColor(getResources().getColor(R.color.scoreboardButtonsActiveText));
				
				if (!upcomingLoaded && pd == null) {
					pd = new ProgressDialog(getActivity());
					pd.setMessage(getResources().getString(R.string.loading));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
				break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				DataType type = DataType.values()[bundle.getInt("type")];
				final FragmentTransaction ft = getInstance().getFragmentManager().beginTransaction();
				SherlockFragment newFragment = new SherlockFragment();
				Bundle args = new Bundle();
				String tag = "";
				switch (type) {
					case HeadlinesArticle:
						newFragment = new ArticleFragment();
						args.putInt("pos", mPos);
						args.putBoolean("teams", mTeams);
						args.putString("data", bundle.getString("data"));
						tag = "Article";
						break;
					case GalleryPhotos:
						newFragment = new ThumbnailFragment();
						args.putInt("pos", mPos);
						args.putBoolean("teams", mTeams);
						args.putString("data", bundle.getString("data"));
						tag = "Thumbnails";
						break;
					default:
						break;
				}
				newFragment.setArguments(args);
				ft.replace(android.R.id.content, newFragment, tag);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};
}
