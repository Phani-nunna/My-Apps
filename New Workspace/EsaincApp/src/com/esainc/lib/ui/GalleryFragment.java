package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.GalleryAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class GalleryFragment extends SherlockListFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Gallery Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	private static int mPos;
	private JSONArray mGalleries;
	private static boolean mFromLinks = false;
	private static GalleryFragment instance;
	private ProgressDialog pd = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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
				mGalleries = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mGalleries = new JSONArray(savedInstanceState.getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mGalleries = new JSONArray();
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.loading));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.gallery, menu);
		
		if (getArguments().getBoolean("teams", false)) {
			((TeamsActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		} else {
			((MainActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			Logger.i(LOG_TAG, "Refreshing Galleries");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger;
			if (getArguments().getBoolean("teams", false)) {
				dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
			} else {
				dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			}
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.Galleries.ordinal());
			dataIntent.putExtra("tabPos", mPos);
			getActivity().startService(dataIntent);
			if (getArguments().getBoolean("teams", false)) {
				((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
			} else {
				((MainActivity) getActivity()).setRefreshActionButtonState(true);
			}
			return true;
		} else if (itemId == android.R.id.home && mFromLinks) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_photos, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setListAdapter(new GalleryAdapter(getSherlockActivity(), mGalleries));
		getListView().setOnItemClickListener(this);
		setAutoRefresh();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mGalleries.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Photos");
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
			getSherlockActivity().getSupportActionBar().setTitle(getArguments().getString("linkName", "Photos"));
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
			mGalleries = new JSONArray(data);
			((GalleryAdapter) getListAdapter()).setData(mGalleries);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public static GalleryFragment getInstance() {
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
							
						}
					});
				} catch (NullPointerException e) {
					Logger.e(LOG_TAG, "Autorefresh Failed", e);
				}
			}
		}, refreshTime * 1000);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// Load gallery data
		Logger.i(LOG_TAG, "Loading Gallery");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger = new Messenger(mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		dataIntent.putExtra("type", DataType.GalleryPhotos.ordinal());
		String params[] = new String[] {view.getTag().toString()};
		dataIntent.putExtra("params", params);
		getActivity().startService(dataIntent);
	}
	
	@SuppressLint("HandlerLeak")
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getString("data") != null) {
				final FragmentTransaction ft = getInstance().getFragmentManager().beginTransaction();
				SherlockFragment galleryPhotos = new ThumbnailFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putString("data", bundle.getString("data"));
				galleryPhotos.setArguments(args);
				ft.replace(android.R.id.content, galleryPhotos, "Thumbnails");
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};
}
