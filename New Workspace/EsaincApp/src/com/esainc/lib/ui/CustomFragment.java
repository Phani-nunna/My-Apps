package com.esainc.lib.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class CustomFragment extends SherlockFragment {
	private static final String LOG_TAG = "Article Fragment";
	private WebView wvBroadcast;
	private String mData;
	private int mPos;
	private Tracker tracker;
	private Timer refreshTimer;
	private int refreshTime = 0;
	private String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();

		mPos = getArguments().getInt("pos");
		mData = getArguments().getString("data");
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
		if (!((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
			title = tabInfo[4];	// Phone Tab Title
		} else {
			title = tabInfo[5];	// Tablet Tab Title
		}
		tabs.recycle();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
		View view = inflater.inflate(R.layout.activity_custompage, container, false);
		return view;
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "InlinedApi" })
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		wvBroadcast = (WebView) view.findViewById(R.id.webviewBroadcast);
		CookieSyncManager.createInstance(wvBroadcast.getContext()).sync();
		CookieManager.getInstance().acceptCookie();
		
		wvBroadcast.getSettings().setJavaScriptEnabled(true);
		if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
			wvBroadcast.getSettings().setDefaultFontSize(20);
		} else {
			wvBroadcast.getSettings().setDefaultFontSize(15);
		}
		wvBroadcast.getSettings().setAllowFileAccess(true);
		wvBroadcast.getSettings().setUserAgentString(wvBroadcast.getSettings().getUserAgentString() + " sidhelpmobileapp");
		wvBroadcast.getSettings().setUseWideViewPort(true);
		wvBroadcast.getSettings().setLoadWithOverviewMode(true);
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB && VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
			wvBroadcast.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		} 
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			wvBroadcast.setBackgroundColor(Color.argb(1, 0, 0, 0));
		} else {
			wvBroadcast.setBackgroundColor(Color.TRANSPARENT);
		}
		wvBroadcast.setWebChromeClient(new WebChromeClient());
		wvBroadcast.setWebViewClient(new WebViewListener(this, mPos, false, wvBroadcast));
		
		if (!TextUtils.isEmpty(mData) && mData.startsWith("http://")) {
			wvBroadcast.loadUrl(mData);
		} else {
			wvBroadcast.loadDataWithBaseURL(null, mData, "text/html", "UTF-8", null);
		}
		wvBroadcast.refreshDrawableState();
		setAutoRefresh();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView(title);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (refreshTimer != null) refreshTimer.cancel();
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
		mData = data;
		if (!TextUtils.isEmpty(mData) && mData.startsWith("http://")) {
			wvBroadcast.loadUrl(mData);
		} else {
			wvBroadcast.loadDataWithBaseURL(null, mData, "text/html", "UTF-8", null);
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
							Messenger dataMessenger = new Messenger(((MainActivity) getActivity()).getHandler());
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("type", DataType.Custom.ordinal());
							dataIntent.putExtra("tabPos", mPos);
							getActivity().startService(dataIntent);
							((MainActivity) getActivity()).setRefreshActionButtonState(true);
						}
					});
				} catch (NullPointerException e) {
					Logger.e(LOG_TAG, "AutoRefresh Failed", e);
				}
			}
		}, refreshTime * 1000);
	}
}
