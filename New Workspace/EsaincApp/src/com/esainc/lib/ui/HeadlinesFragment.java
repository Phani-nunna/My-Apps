package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.HeadlinesAdapter;
import com.esainc.lib.adapters.HeadlinesRotatorAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.viewpagerindicator.CirclePageIndicator;

public class HeadlinesFragment extends SherlockListFragment implements SearchView.OnQueryTextListener, SearchView.SearchViewChangeMode, OnItemClickListener {
	private static final String LOG_TAG = "Headlines Fragment";
	private JSONArray mHeadlines = new JSONArray();
	private JSONArray mHeadlinesTop = new JSONArray();
	private SearchView mSearchView;
	private MenuItem mSearchItem;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private static int mPos;
	private String mSportID;
	private static boolean mTeams;
	private static HeadlinesFragment instance;
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;;
	private ProgressDialog pd = null;
	private int mRotatorMax = 5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Tracking on google Analytics
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		mSportID = getArguments().getString("sportID");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		refreshTime = Integer.valueOf(tabInfo[2]).intValue();
		tabs.recycle();
		
		if (!TextUtils.isEmpty(getArguments().getString("data"))) {
			try {
				mHeadlines = new JSONArray(getArguments().getString("data"));
			} catch (NotFoundException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error loading data", e);
			}
		} else {
			if (savedInstanceState != null) {
				try {
					mHeadlines = new JSONArray(savedInstanceState.getString("data"));
					mHeadlinesTop = new JSONArray(savedInstanceState.getString("dataTop"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				mHeadlines = new JSONArray();
				mHeadlinesTop = new JSONArray();
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
		
		if (mTeams && getSherlockActivity() instanceof TeamsActivity) {
			((TeamsActivity) getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		} else {
			((MainActivity) getSherlockActivity()).mRefresh = menu.findItem(R.id.action_refresh);
		}
		
		mSearchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) mSearchItem.getActionView();
		//following 2 lines set drawable for search field- impossible in XML due to ABS bug
		View searchPlate = mSearchView.findViewById(R.id.abs__search_plate);
		searchPlate.setBackgroundResource(R.drawable.textfield_searchview_holo_dark);
		SearchManager searchManager = (SearchManager) this.getSherlockActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = searchManager.getSearchableInfo(this.getSherlockActivity().getComponentName());
		mSearchView.setSearchableInfo(info);
		mSearchView.setOnSearchViewChangeMode(this);
		mSearchView.setOnQueryTextListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_search) {
			return true;
		} else if (itemId == R.id.action_refresh) {
			mSearchItem.collapseActionView();
			Logger.i(LOG_TAG, "Refreshing Headlines");
			Intent dataIntent = new Intent(getActivity(), DataLoader.class);
			Messenger dataMessenger;
			if (!mTeams) {
				dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
				dataIntent.putExtra("type", DataType.Headlines.ordinal());
			} else {
				dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
				dataIntent.putExtra("type", DataType.SportHeadlines.ordinal());
				String params[] = new String[] {mSportID};
				dataIntent.putExtra("params", params);
			}
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
		return inflater.inflate(R.layout.activity_headlines, container, false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
			if (mHeadlinesTop == null || mHeadlinesTop.length() == 0){
				try {
					mHeadlinesTop = new JSONArray();
					JSONArray temp = new JSONArray();
					for (int i = 0; i < mHeadlines.length(); i++) {
						if (i < mRotatorMax) {
							mHeadlinesTop.put(mHeadlines.get(i));
						} else {
							temp.put(mHeadlines.get(i));
						}
					}
					mHeadlines = temp;
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error splitting off top " + mRotatorMax, e);
				}
			}
			mPager = (ViewPager) getActivity().findViewById(R.id.rotator);
			mPager.setOffscreenPageLimit(((mRotatorMax <= 5) ? mRotatorMax - 1 : 5));
			mPager.setAdapter(new HeadlinesRotatorAdapter(this, getActivity(), mHeadlinesTop, mPos));

			// make sure pager isn't too tall - constrain to half display height
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mPager.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.NewsImageHeight) / (float) getResources().getInteger(R.integer.NewsImageWidth)) *  (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
			
			mIndicator = (CirclePageIndicator) getActivity().findViewById(R.id.indicator);
			mIndicator.setViewPager(mPager);
		}
		
		setListAdapter(new HeadlinesAdapter(getSherlockActivity(), mHeadlines));
		getListView().setOnItemClickListener(this);
		getListView().setEmptyView(getActivity().findViewById(android.R.id.empty));
		
		if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
			if (mHeadlinesTop.length() > 0 && mHeadlines.length() == 0) {
				view.findViewById(android.R.id.empty).setVisibility(View.GONE);
			} else if (mHeadlinesTop.length() == 0) {
				mPager.setVisibility(View.GONE);
				mIndicator.setVisibility(View.GONE);
			}
		}
		
		setAutoRefresh();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!mTeams) {
			tracker.sendView("Headlines");
		} else {
			tracker.sendView("Headlines - " + TeamsActivity.mSportName);
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
		instance = this;
		if (instance.getActivity() instanceof MainActivity) {
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("data", mHeadlines.toString());
		outState.putString("dataTop", mHeadlinesTop.toString());
		super.onSaveInstanceState(outState);
	}
	
	public void setData(String data) {
		setData(data, false);
	}
	
	public void setData(String data, boolean search) {
		try {
			mHeadlines = new JSONArray(data);
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet() && !search) {
				mHeadlinesTop = new JSONArray();
				try {
					JSONArray temp = new JSONArray();
					for (int i = 0; i < mHeadlines.length(); i++) {
						if (i < mRotatorMax) {
							mHeadlinesTop.put(mHeadlines.get(i));
						} else {
							temp.put(mHeadlines.get(i));
						}
					}
					mHeadlines = temp;
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error splitting off top " + mRotatorMax, e);
				}
				
				((HeadlinesRotatorAdapter) mPager.getAdapter()).setData(mHeadlinesTop);
				mPager.setVisibility(View.VISIBLE);
				mIndicator.setVisibility(View.VISIBLE);
			} else {
				if (getActivity().findViewById(android.R.id.empty) != null) {
					getActivity().findViewById(android.R.id.empty).setVisibility(View.GONE);
				}
			}
			
			if (search && mPager != null) {
				mPager.setVisibility(View.GONE);
				mIndicator.setVisibility(View.GONE);
			}
			
			((HeadlinesAdapter) getListAdapter()).setData(mHeadlines);
			if (((SIDHelpApplication) getActivity().getApplication()).isTablet()) {
				if (mHeadlinesTop.length() > 0 && mHeadlines.length() == 0) {
					getActivity().findViewById(android.R.id.empty).setVisibility(View.GONE);
				} else if (mHeadlinesTop.length() == 0) {
					mPager.setVisibility(View.GONE);
					mIndicator.setVisibility(View.GONE);
				}
			}
			setAutoRefresh();
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
	
	public static HeadlinesFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Headlines");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger;
							if (!mTeams) {
								dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
								dataIntent.putExtra("type", DataType.Headlines.ordinal());
							} else {
								dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
								dataIntent.putExtra("type", DataType.SportHeadlines.ordinal());
								String params[] = new String[] {mSportID};
								dataIntent.putExtra("params", params);
							}
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("tabPos", mPos);
							getActivity().startService(dataIntent);
							if (!mTeams) {
								((MainActivity) getActivity()).setRefreshActionButtonState(true);
							} else {
								((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
							}
						}
					});
				} catch (NullPointerException e) {
					Logger.e(LOG_TAG, "AutoRefresh Failed", e);
				}
			}
		}, refreshTime * 1000);
	}

	@Override
	public void onSearchViewCollapsed() {
		Logger.i(LOG_TAG, "Refreshing Headlines");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger;
		if (!mTeams) {
			dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("type", DataType.Headlines.ordinal());
		} else {
			dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
			dataIntent.putExtra("type", DataType.SportHeadlines.ordinal());
			String params[] = new String[] {String.valueOf(mSportID)};
			dataIntent.putExtra("params", params);
		}
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		getActivity().startService(dataIntent);
		if (!mTeams) {
			((MainActivity) getActivity()).setRefreshActionButtonState(true);
		} else {
			((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		getListView().requestFocus();
		Logger.i(LOG_TAG, "Searching Headlines");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger;
		String criteria = query.trim();
		criteria = criteria.replace(" ", "+");
		if (!mTeams) {
			dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
			dataIntent.putExtra("type", DataType.HeadlinesSearch.ordinal());
			String params[] = new String[] {criteria};
			dataIntent.putExtra("params", params);
		} else {
			dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
			dataIntent.putExtra("type", DataType.SportHeadlinesSearch.ordinal());
			String params[] = new String[] {mSportID, criteria};
			dataIntent.putExtra("params", params);
		}
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		getActivity().startService(dataIntent);
		if (!mTeams) {
			((MainActivity) getActivity()).setRefreshActionButtonState(true);
		} else {
			((TeamsActivity) getActivity()).setRefreshActionButtonState(true);
		}
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// Load article data
		Logger.i(LOG_TAG, "Loading Article");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger = new Messenger(mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		dataIntent.putExtra("type", DataType.HeadlinesArticle.ordinal());
		String params[] = new String[] {view.getTag().toString()};
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
				SherlockFragment articleDetails = new ArticleFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putBoolean("teams", mTeams);
				args.putString("data", bundle.getString("data"));
				articleDetails.setArguments(args);
				ft.replace(android.R.id.content, articleDetails, "Article");
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};
}
