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
import com.esainc.lib.adapters.ScheduleAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class ScheduleFragment extends SherlockListFragment {
	private static final String LOG_TAG = "Schedule Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	private JSONArray mSchedule;
	private JSONObject mExtra;
	private String mRecord;
	private String mSportID;
	private static boolean mTeams;
	private ProgressDialog pd = null;
	private static ScheduleFragment instance;
	private int mCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		instance = this;
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		mSportID = getArguments().getString("sportID");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		
		mCode = getArguments().getInt("code");
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				if (mCode == 419) {
					mSchedule = new JSONArray();
					mRecord = new String();
				} else {
					mSchedule = new JSONArray(getArguments().getString("data"));
					mRecord = getArguments().getString("record");
				}
				mExtra = new JSONObject(getArguments().getString("extra"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					if (mCode == 419) {
						mSchedule = new JSONArray();
						mRecord = new String();
					} else {
						mSchedule = new JSONArray(savedInstanceState.getString("data"));
						mRecord = getArguments().getString("record");
					}
					mExtra = new JSONObject(getArguments().getString("extra"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mSchedule = new JSONArray();
				mExtra = new JSONObject();
				mRecord = new String();
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
		inflater.inflate(R.menu.scoreboard, menu);
		
		((TeamsActivity) this.getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			Logger.i(LOG_TAG, "Refreshing Schedule");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("type", DataType.Schedule.ordinal());
			dataIntent.putExtra("tabPos", mPos);
			String params[] = new String[] {mSportID};
			dataIntent.putExtra("params", params);
			getActivity().startService(dataIntent);
			((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
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
		TextView txtConference = (TextView) view.findViewById(R.id.txtConference);
		TextView txtScrimmage = (TextView) view.findViewById(R.id.txtScrimmage);
		TextView txtExhibition = (TextView) view.findViewById(R.id.txtExhibition);
		TextView txtRecord = (TextView) view.findViewById(R.id.recordText);
		LinearLayout llRecordHeader = (LinearLayout) view.findViewById(R.id.recordHeader);
		try {
			txtConference.setText(((!mExtra.isNull(getResources().getString(R.string.tag_conference))) ? mExtra.getString(getResources().getString(R.string.tag_conference)) : "*") + " " + getResources().getString(R.string.conferenceTxt));
			txtScrimmage.setText(((!mExtra.isNull(getResources().getString(R.string.tag_scrimmage))) ? mExtra.getString(getResources().getString(R.string.tag_scrimmage)) : "%") + " "  + getResources().getString(R.string.scrimmageTxt));
			txtExhibition.setText(((!mExtra.isNull(getResources().getString(R.string.tag_exhibition))) ? mExtra.getString(getResources().getString(R.string.tag_exhibition)) : "^") + " "  + getResources().getString(R.string.exhibitionTxt));
			if (mRecord.length() > 0) {
				llRecordHeader.setVisibility(View.VISIBLE);
				txtRecord.setText(getResources().getString(R.string.recordTxt) + " " + mRecord);
			} else {
				llRecordHeader.setVisibility(View.GONE);
			}
		} catch (NotFoundException e) {
			Logger.e(LOG_TAG, "Error getting keys", e);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting keys", e);
		}
		setListAdapter(new ScheduleAdapter(getSherlockActivity(), mSchedule, mTeams));
		setAutoRefresh();
		
		TextView empty = (TextView) view.findViewById(android.R.id.empty);
		if (mCode == 419) {
			empty.setText(R.string.error_schedule_hidden);
		} else {
			empty.setText(R.string.noResults);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mSchedule.toString());
		outState.putString("extra", mExtra.toString());
		outState.putString("record", mRecord);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Schedule - " + TeamsActivity.mSportName);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (refreshTimer != null) refreshTimer.cancel();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setTitle(TeamsActivity.mSportName);
		TeamsActivity.stacked = false;
	}
	
	public void setData(String data) {
		if (!data.equals("Schedule has not been released.") && !data.startsWith("Schedule will be released on \n")) {
			try {
				mSchedule = new JSONArray(data);
				((ScheduleAdapter) getListAdapter()).setData(mSchedule);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Setting Data", e);
			}
		} else {
			mSchedule = new JSONArray();
			((ScheduleAdapter) getListAdapter()).setData(mSchedule);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public void setExtra(String extra) {
		try {
			TextView txtConference = (TextView) getActivity().findViewById(R.id.txtConference);
			TextView txtScrimmage = (TextView) getActivity().findViewById(R.id.txtScrimmage);
			TextView txtExhibition = (TextView) getActivity().findViewById(R.id.txtExhibition);
			mExtra = new JSONObject(extra);
			txtConference.setText(mExtra.getString(getResources().getString(R.string.tag_conference)) + " " + getResources().getString(R.string.conferenceTxt));
			txtScrimmage.setText(mExtra.getString(getResources().getString(R.string.tag_scrimmage)) + " "  + getResources().getString(R.string.scrimmageTxt));
			txtExhibition.setText(mExtra.getString(getResources().getString(R.string.tag_exhibition)) + " "  + getResources().getString(R.string.exhibitionTxt));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Extra", e);
		}
	}
	
	public void setRecord(String record) {
		mRecord = record;
		TextView txtRecord = (TextView) getActivity().findViewById(R.id.recordText);
		LinearLayout llRecordHeader = (LinearLayout) getActivity().findViewById(R.id.recordHeader);
		if (mRecord.length() > 0) {
			llRecordHeader.setVisibility(View.VISIBLE);
			txtRecord.setText(getResources().getString(R.string.recordTxt) + " " + mRecord);
		} else {
			llRecordHeader.setVisibility(View.GONE);
		}
	}
	
	public void setCode(int code) {
		mCode = code;
		if (mCode == 419) {
			mSchedule = new JSONArray();
		}
		TextView empty = (TextView) getActivity().findViewById(android.R.id.empty);
		if (mCode == 419) {
			empty.setText(R.string.error_roster_hidden);
		} else {
			empty.setText(R.string.noResults);
		}
	}
	
	public static ScheduleFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Schedule");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
							dataIntent.putExtra("type", DataType.Schedule.ordinal());
							String params[] = new String[] {mSportID};
							dataIntent.putExtra("params", params);
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
