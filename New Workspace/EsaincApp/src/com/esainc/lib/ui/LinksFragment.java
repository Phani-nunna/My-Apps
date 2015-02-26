package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.FragmentTransaction;
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
import com.esainc.lib.adapters.LinksAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class LinksFragment extends SherlockListFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Links Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	private static int mPos;
	private JSONArray mLinks;
	private String mSportID;
	private static boolean mTeams;
	private ProgressDialog pd = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		mSportID = getArguments().getString("sportID");
		
		TypedArray tabs;
		if (!mTeams) {
			tabs = getResources().obtainTypedArray(R.array.TabBars);
		} else {
			tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
		}
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mLinks = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mLinks = new JSONArray(savedInstanceState.getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mLinks = new JSONArray();
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
		return inflater.inflate(R.layout.activity_additionallinks, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setListAdapter(new LinksAdapter(getSherlockActivity(), mLinks));
		getListView().setOnItemClickListener(this);
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mLinks.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!mTeams) {
			tracker.sendView("Links");
		} else {
			tracker.sendView("Links - " + TeamsActivity.mSportName);
		}
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
			mLinks = new JSONArray(data);
			((LinksAdapter) getListAdapter()).setData(mLinks);
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
							Logger.i(LOG_TAG, "Refreshing Headlines");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger;
							if (!mTeams) {
								dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
								dataIntent.putExtra("type", DataType.Links.ordinal());
							} else {
								dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
								dataIntent.putExtra("type", DataType.SportLinks.ordinal());
								String params[] = new String[] {mSportID};
								dataIntent.putExtra("params", params);
							}
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
		// Load link
		try {
			JSONObject hdata = mLinks.getJSONObject(pos);
			String url = hdata.getString(getResources().getString(R.string.tag_LinkUrl));
			
			if (url.equalsIgnoreCase("staff list")) {
				final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				StaffFragment staffFragment = new StaffFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putBoolean("teams", mTeams);
				if (getArguments().getBoolean("teams", false)) {
					args.putString("data", TeamsActivity.tabData.get(TeamsActivity.tabCount + 2));
					if (TeamsActivity.tabExtra.get(TeamsActivity.tabCount + 2) != null) args.putString("extra", TeamsActivity.tabExtra.get(TeamsActivity.tabCount + 2));
				} else {
					args.putString("data", MainActivity.tabData.get(MainActivity.tabCount + 2));
					if (MainActivity.tabExtra.get(MainActivity.tabCount + 2) != null) args.putString("extra", MainActivity.tabExtra.get(MainActivity.tabCount + 2));
				}
				args.putBoolean("fromLinks", true);
				args.putString("linkName", hdata.getString(getResources().getString(R.string.tag_LinkName)));
				staffFragment.setArguments(args);
				ft.replace(android.R.id.content, staffFragment, "Staff");
				ft.addToBackStack(null);
				ft.commit();
			} else if (url.equalsIgnoreCase("photo gallery")) {
				final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				GalleryFragment galleryFragment = new GalleryFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putBoolean("teams", mTeams);
				if (getArguments().getBoolean("teams", false)) {
					args.putString("data", TeamsActivity.tabData.get(TeamsActivity.tabCount + 3));
					if (TeamsActivity.tabExtra.get(TeamsActivity.tabCount + 3) != null) args.putString("extra", TeamsActivity.tabExtra.get(TeamsActivity.tabCount + 3));
				} else {
					args.putString("data", MainActivity.tabData.get(MainActivity.tabCount + 3));
					if (MainActivity.tabExtra.get(MainActivity.tabCount + 3) != null) args.putString("extra", MainActivity.tabExtra.get(MainActivity.tabCount + 3));
				}
				args.putBoolean("fromLinks", true);
				args.putString("linkName", hdata.getString(getResources().getString(R.string.tag_LinkName)));
				galleryFragment.setArguments(args);
				ft.replace(android.R.id.content, galleryFragment, "Photos");
				ft.addToBackStack(null);
				ft.commit();
			} else {
				Intent browserIntent = new Intent(getActivity(), BrowserActivity.class);
				browserIntent.putExtra("pos", mPos);
				browserIntent.putExtra("teams", mTeams);
				browserIntent.putExtra("linkName", hdata.getString(getResources().getString(R.string.tag_LinkName)));
				browserIntent.putExtra("url", hdata.getString(getResources().getString(R.string.tag_LinkUrl)));
				browserIntent.putExtra("stats", false);
				if (mTeams) {
					browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
					browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
					browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
					browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
				}
				startActivity(browserIntent);
			}
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error loading link", e);
		}
	}
}
