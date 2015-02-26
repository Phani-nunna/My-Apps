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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.RosterAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class RosterFragment extends SherlockFragment implements OnItemClickListener, OnItemSelectedListener {
	private static final String LOG_TAG = "Roster Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	private JSONArray mPlayers;
	private String mSportID;
	private static boolean mTeams;
	private ProgressDialog pd = null;
	private static RosterFragment instance;
	private ListView mList;
	private GridView mGrid;
	private IcsSpinner mSorter;
	private IcsSpinner mClassFilter;
	private int mCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		if (mCode == 419) {
			mPlayers = new JSONArray();
		} else {
			if (!TextUtils.isEmpty(getArguments().getString("data"))) {
				try {
					mPlayers = new JSONArray(getArguments().getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				if (savedInstanceState != null) {
					try {
						mPlayers = new JSONArray(getArguments().getString("data"));
					} catch (NotFoundException e) {
						Logger.e(LOG_TAG, "Error loading data", e);
					} catch (JSONException e) {
						Logger.e(LOG_TAG, "Error loading data", e);
					}
				} else {
					mPlayers = new JSONArray();
					pd = new ProgressDialog(getActivity());
					pd.setMessage(getResources().getString(R.string.loading));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
			}
		}
		
		setHasOptionsMenu(true);
		instance = this;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.roster, menu);
		
		SpinnerAdapter sorterAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_by_array, R.layout.sort_by);
		mSorter = new IcsSpinner(getActivity(), null, R.attr.actionDropDownStyle);
		mSorter.setAdapter(sorterAdapter);
		mSorter.setOnItemSelectedListener(this);
		menu.findItem(R.id.action_sort).setActionView(mSorter);
		
		if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
			SpinnerAdapter filterAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.class_filter_array, R.layout.sort_by);
			mClassFilter = new IcsSpinner(getActivity(), null, R.attr.actionDropDownStyle);
			mClassFilter.setAdapter(filterAdapter);
			mClassFilter.setOnItemSelectedListener(this);
			menu.findItem(R.id.action_filter).setActionView(mClassFilter);
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_sort) {
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_team_roster, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
			mGrid = (GridView) view.findViewById(R.id.rosterGrid);
			mGrid.setAdapter(new RosterAdapter(getSherlockActivity(), mPlayers));
			mGrid.setOnItemClickListener(this);
			mGrid.setEmptyView(getActivity().findViewById(android.R.id.empty));
		} else {
			mList = (ListView) view.findViewById(R.id.rosterList);
			mList.setAdapter(new RosterAdapter(getSherlockActivity(), mPlayers));
			mList.setOnItemClickListener(this);
			mList.setEmptyView(getActivity().findViewById(android.R.id.empty));
		}
		setAutoRefresh();
		TextView empty = (TextView) view.findViewById(android.R.id.empty);
		if (mCode == 419) {
			empty.setText(R.string.error_roster_hidden);
		} else {
			empty.setText(R.string.noResults);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mPlayers.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Roster - " + TeamsActivity.mSportName);
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
		if (!data.equals("Roster is currently hidden")) {
			try {
				mPlayers = new JSONArray(data);
				if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
					((RosterAdapter) mGrid.getAdapter()).setData(mPlayers);
				} else {
					((RosterAdapter) mList.getAdapter()).setData(mPlayers);
				}
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Setting Data", e);
				mPlayers = new JSONArray();
			}
		} else {
			mPlayers = new JSONArray();
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
				((RosterAdapter) mGrid.getAdapter()).setData(mPlayers);
			} else {
				((RosterAdapter) mList.getAdapter()).setData(mPlayers);
			}
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public void setCode(int code) {
		mCode = code;
		if (mCode == 419) {
			mPlayers = new JSONArray();
		}
		TextView empty = (TextView) getActivity().findViewById(android.R.id.empty);
		if (mCode == 419) {
			empty.setText(R.string.error_roster_hidden);
		} else {
			empty.setText(R.string.noResults);
		}
	}
	
	public static RosterFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Roster");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
							dataIntent.putExtra("type", DataType.Players.ordinal());
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
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// Load Player Bio
		Logger.i(LOG_TAG, "Loading Player Bio");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger = new Messenger(mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		dataIntent.putExtra("type", DataType.PlayersBio.ordinal());
		String params[] = new String[] {String.valueOf(id)};
		dataIntent.putExtra("params", params);
		getActivity().startService(dataIntent);
	}
	
	@SuppressLint("HandlerLeak")
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				final FragmentTransaction ft = getInstance().getFragmentManager().beginTransaction();
				SherlockFragment rosterBio = new RosterBioFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putBoolean("teams", mTeams);
				args.putString("data", bundle.getString("data"));
				rosterBio.setArguments(args);
				ft.replace(android.R.id.content, rosterBio, "Roster Bio");
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};

	@Override
	public void onItemSelected(IcsAdapterView<?> parent, View view, int pos, long id) {
		String sortValue;
		String filterValue;
		
		if (parent == mSorter) {
			// Sort the list
			switch (pos) {
				case 0:		// Number
					sortValue = getResources().getString(R.string.tag_number);
					break;
				case 1:		// First Name
					sortValue = getResources().getString(R.string.tag_fname);
					break;
				case 2:		// Last Name
					sortValue = getResources().getString(R.string.tag_lname);
					break;
				case 3:		// Position
					sortValue = getResources().getString(R.string.tag_pos);
					break;
				default:	// Default to number
					sortValue = getResources().getString(R.string.tag_number);
					break;
			}
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
				((RosterAdapter) mGrid.getAdapter()).sort(sortValue);
			} else {
				((RosterAdapter) mList.getAdapter()).sort(sortValue);
			}
		} else if (parent == mClassFilter) {
			// Filter based upon selected class
			switch (pos) {
				case 0:		// All
					filterValue = getResources().getString(R.string.All);
					break;
				case 1:		// Freshman
					filterValue = getResources().getString(R.string.FR);
					break;
				case 2:		// Sophmores
					filterValue = getResources().getString(R.string.SO);
					break;
				case 3:		// Juniors
					filterValue = getResources().getString(R.string.JR);
					break;
				case 4:		// Seniors
					filterValue = getResources().getString(R.string.SR);
					break;
				default:
					filterValue = getResources().getString(R.string.All);
					break;	// Default to all
			}
			// Get current sorting
			switch (mSorter.getSelectedItemPosition()) {
				case 0:		// Number
					sortValue = getResources().getString(R.string.tag_number);
					break;
				case 1:		// First Name
					sortValue = getResources().getString(R.string.tag_fname);
					break;
				case 2:		// Last Name
					sortValue = getResources().getString(R.string.tag_lname);
					break;
				case 3:		// Position
					sortValue = getResources().getString(R.string.tag_pos);
					break;
				default:	// Default to number
					sortValue = getResources().getString(R.string.tag_number);
					break;
			}
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
				((RosterAdapter) mGrid.getAdapter()).filter(filterValue, sortValue);
			} else {
				((RosterAdapter) mList.getAdapter()).filter(filterValue, sortValue);
			}
		}
	}

	@Override
	public void onNothingSelected(IcsAdapterView<?> parent) {
		// do nothing
	}
}
