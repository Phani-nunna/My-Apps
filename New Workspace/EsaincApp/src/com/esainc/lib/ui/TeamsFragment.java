package com.esainc.lib.ui;

import java.util.HashMap;
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
import android.text.TextUtils;
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
import com.esainc.lib.adapters.TeamsAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class TeamsFragment extends SherlockListFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Teams Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	private static int mPos;
	private JSONArray mTeams;
	private static TeamsFragment instance;
	private String sorter;
	private ProgressDialog pd = null;
	public static String showLinks = "1";
	public static String showCoach = "1";
	public static String showSchedule = "1";
	public static String showRoster = "1";
	private String sportID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		sorter = tabInfo[6];
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mTeams = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mTeams = new JSONArray(savedInstanceState.getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mTeams = new JSONArray();
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
		menu.clear();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_teams, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setListAdapter(new TeamsAdapter(getSherlockActivity(), mTeams, sorter));
		getListView().setOnItemClickListener(this);
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mTeams.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Teams");
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
	
	public void setData(String data) {
		try {
			mTeams = new JSONArray(data);
			((TeamsAdapter) getListAdapter()).setData(mTeams);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public static TeamsFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Teams");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.Teams.ordinal());
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
		// Load Teams data
		//Check clicked position is header or not
		if (!view.getTag().toString().equals(getResources().getString(R.string.header))) {
			
			Logger.i(LOG_TAG, "Loading Sport Info");
			Intent infoIntent;
			Messenger infoMessenger;
			infoIntent = new Intent(TeamsFragment.getInstance().getActivity(), DataLoader.class);
			infoMessenger = new Messenger(infoHandler);
			infoIntent.putExtra("MESSENGER", infoMessenger);
			infoIntent.putExtra("type", DataType.SportInfo.ordinal());
			String params[] = new String[0];
			params = new String[] {view.getTag().toString()};
			sportID = view.getTag().toString();
			infoIntent.putExtra("params", params);
			TeamsFragment.getInstance().getActivity().startService(infoIntent);
			
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.loading));
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	}
	
	public String getSportName(String sportID) {
		String sportName = "";
		for (int i = 0; i < mTeams.length(); i++) {
			try {
				if (mTeams.getJSONObject(i).getString(getActivity().getResources().getString(R.string.tag_sportID)).equals(sportID)) {
					sportName = mTeams.getJSONObject(i).getString(getActivity().getResources().getString(R.string.tag_sportName));
					break;
				}
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error get sport name", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error get sport name", e);
			}
		}
		return sportName;
	}
	
	@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				HashMap<Integer, String> tabData = new HashMap<Integer, String>();
				tabData.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("data"));
				HashMap<Integer, String> tabExtra = new HashMap<Integer, String>();
				if (bundle.getString("extra") != null) tabExtra.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("extra"));
				HashMap<Integer, Integer> tabCode = new HashMap<Integer, Integer>();
				tabCode.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getInt("code"));
				Intent intent = new Intent(getInstance().getActivity(), TeamsActivity.class);
				intent.putExtra("tabData", tabData);
				intent.putExtra("tabExtra", tabExtra);
				intent.putExtra("tabCode", tabCode);
				intent.putExtra("sportID", bundle.getString("sportID"));
				intent.putExtra("sportName", getInstance().getSportName(bundle.getString("sportID")));
				intent.putExtra("showLinks", showLinks);
				intent.putExtra("showRoster", showRoster);
				intent.putExtra("showSchedule", showSchedule);
				intent.putExtra("showCoach", showCoach);
				getInstance().pd.dismiss();
				getInstance().pd = null;
				getInstance().getActivity().startActivity(intent);
			}
		}
	};
	
	@SuppressLint("HandlerLeak")
	private Handler infoHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				JSONArray sportInfo;
				Intent dataIntent = new Intent(TeamsFragment.getInstance().getActivity(), DataLoader.class);
				Messenger dataMessenger = new Messenger(TeamsFragment.mHandler);
				dataIntent.putExtra("MESSENGER", dataMessenger);
				dataIntent.putExtra("tabPos", 0);
				try {
					sportInfo = new JSONArray(bundle.getString("data"));
					JSONObject sData = sportInfo.getJSONObject(0);
					showRoster = sData.getString("showRoster");
					showSchedule = sData.getString("showSchedule");
					showCoach = sData.getString("showCoach");
					showLinks = sData.getString("showLinks");

				} catch (JSONException e) {

					e.printStackTrace();
				}
				Logger.i(LOG_TAG, "Loading Teams Data");
				
				TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
				String tabInfo[] = getResources().getStringArray(tabs.getResourceId(0, 0));

				String[] params = null;
				if (tabInfo[0].equalsIgnoreCase("teamwise_news")) {
					dataIntent.putExtra("type", DataType.SportHeadlines.ordinal());
					params = new String[] {sportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_schedule")) {
					dataIntent.putExtra("type", DataType.Schedule.ordinal());
					params = new String[] {sportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_roster")) {
					dataIntent.putExtra("type", DataType.Players.ordinal());
					params = new String[] {sportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_coaches")) {
					dataIntent.putExtra("type", DataType.Coaches.ordinal());
					params = new String[] {sportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_links")) {
					dataIntent.putExtra("type", DataType.SportLinks.ordinal());
					params = new String[] {sportID};
				}
				tabs.recycle();
				dataIntent.putExtra("params", params);
				TeamsFragment.getInstance().getActivity().startService(dataIntent);
				
			}
		}
	};
}
