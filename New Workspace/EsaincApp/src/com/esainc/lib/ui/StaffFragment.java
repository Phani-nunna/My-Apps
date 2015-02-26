package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.StaffAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class StaffFragment extends SherlockListFragment {
	private static final String LOG_TAG = "Staff Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	private JSONArray mStaff;
	private static boolean mFromLinks = false;
	private static StaffFragment instance;
	private ProgressDialog pd = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mFromLinks = getArguments().getBoolean("fromLinks", false);
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mStaff = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mStaff = new JSONArray(savedInstanceState.getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mStaff = new JSONArray();
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
		int itemId = item.getItemId();
		if (itemId == android.R.id.home && mFromLinks) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_staff, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setListAdapter(new StaffAdapter(getSherlockActivity(), mStaff));
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mStaff.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Staff");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (refreshTimer != null) refreshTimer.cancel();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (getArguments().getBoolean("fromLinks", false)) {
			getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			if (getArguments().getBoolean("teams", false)) {
				TeamsActivity.stacked = true;
			}
			getSherlockActivity().getSupportActionBar().setTitle(getArguments().getString("linkName", "Staff"));
		} else {
			if (!getArguments().getBoolean("teams", false)) {
				getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
				String tabInfo[] = getResources().getStringArray(tabs.getResourceId(getArguments().getInt("pos"), 0));
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
	}
	
	public void setData(String data) {
		try {
			mStaff = new JSONArray(data);
			((StaffAdapter) getListAdapter()).setData(mStaff);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public static StaffFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Staff");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.Staff.ordinal());
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

	@SuppressLint("HandlerLeak")
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				final FragmentTransaction ft = getInstance().getFragmentManager().beginTransaction();
				SherlockFragment staffBio = new StaffBioFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putString("data", bundle.getString("data"));
				staffBio.setArguments(args);
				ft.replace(android.R.id.content, staffBio, "Staff Bio");
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};
}
