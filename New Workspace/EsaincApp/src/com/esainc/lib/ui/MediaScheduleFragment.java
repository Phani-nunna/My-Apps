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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.MediaScheduleAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class MediaScheduleFragment extends SherlockListFragment {
	private static final String LOG_TAG = "MediaScheduleFragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	private JSONArray mSchedule;
	private JSONObject mExtra;	
	private ProgressDialog pd = null;
	private static MediaScheduleFragment instance;
	private boolean extraLoaded;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		instance = this;
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		
		TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mSchedule = new JSONArray(getArguments().getString("data"));
				
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mSchedule = new JSONArray(savedInstanceState.getString("data"));
					
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mSchedule = new JSONArray();
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.loading));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
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
			Logger.i(LOG_TAG, "Refreshing MediaSchedule");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.MediaSchedule.ordinal());
			dataIntent.putExtra("tabPos", mPos);
			getActivity().startService(dataIntent);
			((MainActivity) getActivity()).setRefreshActionButtonState(true);
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_schedule, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LinearLayout llRecordHeader = (LinearLayout) view.findViewById(R.id.recordHeader);
        llRecordHeader.setVisibility(View.GONE);
        setSymbolDisplay();
		setListAdapter(new MediaScheduleAdapter(getSherlockActivity(), mSchedule, false));
		setAutoRefresh();
         
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mSchedule.toString());
		outState.putString("extra", mExtra.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Media Schedule");
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
			mSchedule = new JSONArray(data);
			((MediaScheduleAdapter) getListAdapter()).setData(mSchedule);
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
		Logger.e(LOG_TAG, "Setting Extra error", e);
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
	public static MediaScheduleFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Media Schedule");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("type", DataType.MediaSchedule.ordinal());
							
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
						args.putString("data", bundle.getString("data"));
						tag = "Article";
						break;
					case GalleryPhotos:
						newFragment = new ThumbnailFragment();
						args.putInt("pos", mPos);
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
