package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.CoachesAdapter;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class CoachesFragment extends SherlockListFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Coaches Fragment";
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	public static int mPos;
	private JSONArray mCoaches;
	private String mSportID;
	private static boolean mTeams;
	private ProgressDialog pd = null;
	private static CoachesFragment instance;
	private WebView webDetailView;
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
			mCoaches = new JSONArray();
		} else {
			if (!TextUtils.isEmpty(getArguments().getString("data"))) {
				try {
					mCoaches = new JSONArray(getArguments().getString("data"));
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error loading data", e);
				}
			} else {
				if (savedInstanceState != null) {
					try {
						mCoaches = new JSONArray(savedInstanceState.getString("data"));
					} catch (NotFoundException e) {
						Logger.e(LOG_TAG, "Error loading data", e);
					} catch (JSONException e) {
						Logger.e(LOG_TAG, "Error loading data", e);
					}
				} else {
					mCoaches = new JSONArray();
					pd = new ProgressDialog(getActivity());
					pd.setMessage(getResources().getString(R.string.loading));
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					Log.d(LOG_TAG, "Establishing Loading Dialog in onCreate");
					pd.show();
				}
			}
		}
		
		setHasOptionsMenu(true);
		instance = this;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
		return inflater.inflate(R.layout.activity_team_coaches, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
			webDetailView = (WebView) view.findViewById(R.id.webViewCoaches);
			setWebViewForTablet();
		} else {
			setListAdapter(new CoachesAdapter(getSherlockActivity(), mCoaches));
			getListView().setOnItemClickListener(this);
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
		outState.putString("data", mCoaches.toString());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Coaches - " + TeamsActivity.mSportName);
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
				mCoaches = new JSONArray(data);
				Log.d(LOG_TAG, "Setting Coaches Array");
				((CoachesAdapter) getListAdapter()).setData(mCoaches);
				if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
					setWebViewForTablet();
				}
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Setting Data", e);
			}
		} else {
			mCoaches = new JSONArray();
			((CoachesAdapter) getListAdapter()).setData(mCoaches);
			if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
				setWebViewForTablet();
			}
		}
		if (pd != null) {
			Log.d(LOG_TAG, "Removing Loading Dialog in setData");
			pd.dismiss();
			pd = null;	
		}
		if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
			setWebViewForTablet();
		} 
	}
	
	public void setCode(int code) {
		mCode = code;
		if (mCode == 419) {
			mCoaches = new JSONArray();
		}
		TextView empty = (TextView) getActivity().findViewById(android.R.id.empty);
		if (mCode == 419) {
			empty.setText(R.string.error_roster_hidden);
		} else {
			empty.setText(R.string.noResults);
		}
	}
	
	public static CoachesFragment getInstance() {
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
							Logger.i(LOG_TAG, "Refreshing Coaches");
							Intent dataIntent = new Intent(getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(((TeamsActivity) getActivity()).getHandler());
							dataIntent.putExtra("type", DataType.Coaches.ordinal());
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
		// Load Coaches Bio
		Logger.i(LOG_TAG, "Loading Coach Bio");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger = new Messenger(mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		dataIntent.putExtra("type", DataType.CoachesBio.ordinal());
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
				SherlockFragment coachBio = new CoachesBioFragment();
				Bundle args = new Bundle();
				args.putInt("pos", mPos);
				args.putBoolean("teams", mTeams);
				args.putString("data", bundle.getString("data"));
				coachBio.setArguments(args);
				ft.replace(android.R.id.content, coachBio, "Coach Bio");
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	};
	
	@SuppressLint({ "InlinedApi", "SetJavaScriptEnabled", "NewApi" })
	public void setWebViewForTablet() {
		

		try {
			StringBuffer dataBuffer = new StringBuffer();
			Log.d(LOG_TAG, "Starting HTML dataBuffer");
			dataBuffer.append("<html>" +
					"<head>" +
						"<style type='text/css'>" +
							"* {" +
							"} " +
							"p { " +
								"color: black; " +
							"} " +
							"a { " +
								"color: #" + getResources().getString(R.color.HyperlinksColor).substring(3) + "; " +
								"text-decoration: none; " +
							"} " +
							".rosterPhoto { " +
								"align: center; " +
							"}" +
							".rosterPhoto img { " +
								"max-width: 200px; " +
								"border: 2px solid  #" + getResources().getString(R.color.Border).substring(3) + "; " +
								"box-shadow: 1px 1px; " +
							"} " +
							".name { " +
								"font-size: 1.5em; " +
								"font-weight: bold; " +
								"margin-bottom: 5px; " +
							"} .type { " +
								"font-size: 0.9em; " +
								"font-style: italic; " +
							"}" +
							".video { " +
								"position:relative; " +
								"height: 360px; " +
								"width: 480px" +
							"} " +
							".video a { " +
								"position:absolute; display: " +
								"block; background-image: url(https://lh6.ggpht.com/NrQdFAdPSI9-hreB4C7HNhj3yXRiW1jqOOi7eFyakIx_IA-Im0huIeYCs5jTidMT2qA=w70); " +
								"background-repeat: no-repeat; " +
								"background-position: center; " +
								"height: 360px; " +
								"width: 480px; " +
							"}" +
						"</style>" +
						"<meta name='viewport' content='initial-scale=1.0; maximum-scale=1.0;'>" +
					"</head>" +
					"<body>");
			for (int i=0; i < mCoaches.length(); i++) {
				JSONObject hdata = mCoaches.getJSONObject(i);
				
				String photoURL = hdata.getString(getResources().getString(R.string.tag_photo)).replaceAll(" ", "%20");

				if (i > 0) {
					dataBuffer.append("<div style=\"clear:both;\"><hr>&nbsp;<br></div>");
					//horizontal line before all bios after the first
				}
				
				if (!TextUtils.isEmpty(photoURL)) {
					if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
						int displayWidth = getResources().getInteger(R.integer.RosterImageWidth);
						int displayHeight = getResources().getInteger(R.integer.RosterImageHeight);
						dataBuffer.append("<div style=\"margin-bottom:10px;margin-right:20px;float:left;\"><img alt=\"\" src=\""+ photoURL +"\" height=\""+ displayHeight +"\" width =\""+ displayWidth +"\" style=\"max-width:300px;border:3px solid " + getResources().getString(R.color.Border).substring(3) + ";box-shadow: 3px 3px 4px #000;\"></div>");
					} else {
						dataBuffer.append("<div style=\"margin-bottom:10px;margin-right:20px;float:left;\"><img alt=\"\" src=\""+ photoURL +"\" style=\"max-width:300px;border:3px solid " + getResources().getString(R.color.Border).substring(3) + ";box-shadow: 3px 3px 4px #000;\"></div>");
					}
				}
				
				dataBuffer.append("<div class=\"name\">" + hdata.getString(getResources().getString(R.string.tag_fname)) + " " + hdata.getString(getResources().getString(R.string.tag_lname)) + "</div><div class=\"type\">" + hdata.getString(getResources().getString(R.string.tag_coachType)));
				
				if (!TextUtils.isEmpty(hdata.getString(getResources().getString(R.string.tag_yearsCoached)))) {
					dataBuffer.append(hdata.getString(getResources().getString(R.string.tag_yearsCoached)));
				}
				
				dataBuffer.append("</div>" + hdata.getString(getResources().getString(R.string.tag_almaMater)));
				
				// Check bio for youtube embed
				Pattern youtubePattern = Pattern.compile("<iframe .*?src=\"http://www.youtube.com/embed/(.*?)(\\??(?<=\\?)(.*?))?\".*?>");
				Matcher matcher = youtubePattern.matcher(hdata.getString(getResources().getString(R.string.tag_bio)));
				while (matcher.find()) {
					matcher.appendReplacement(dataBuffer, "<div class=\"video\"><a href=\"youtube:$1\"></a><img src=\"http://img.youtube.com/vi/$1/0.jpg\"></div>");
				}
				matcher.appendTail(dataBuffer);
			}
			// Close Body and HTML
			dataBuffer.append("</body></html>");
			// Setup HTML for bio
			String data = null;
			data = dataBuffer.toString();
			
			
			webDetailView.getSettings().setJavaScriptEnabled(true);
			webDetailView.getSettings().setLoadWithOverviewMode(true);
			webDetailView.getSettings().setUserAgentString(webDetailView.getSettings().getUserAgentString() + " sidhelpmobileapp");
			webDetailView.getSettings().setUseWideViewPort(true);
			if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB && VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
				webDetailView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			} 
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				webDetailView.setBackgroundColor(Color.argb(1, 0, 0, 0));
			} else {
				webDetailView.setBackgroundColor(Color.TRANSPARENT);
			}
			webDetailView.setWebChromeClient(new WebChromeClient());
			webDetailView.setWebViewClient(new WebViewListener(this, mPos, true, webDetailView));
			webDetailView.loadData(data, "text/html", "UTF-8");
			webDetailView.setVisibility(View.VISIBLE);
			Log.d(LOG_TAG, "WebView Loaded");
			webDetailView.refreshDrawableState();
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Error getting data for display", e);
		}

		setListAdapter(new CoachesAdapter(getSherlockActivity(), mCoaches));
		getListView().setVisibility(View.GONE);
		TextView empty = (TextView) getListView().getEmptyView();
		empty.setText("");
	}
}
