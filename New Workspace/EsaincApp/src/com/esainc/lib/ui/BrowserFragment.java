package com.esainc.lib.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebChromeListener;
import com.esainc.lib.uc.WebViewListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class BrowserFragment extends SherlockFragment {
	private static final String LOG_TAG = "Browser Fragment";
	private Tracker tracker;
	private static int mPos;
	private boolean mTeams;
	private String mSportName;
	private String mLinkName;
	private String mURL;
	private boolean mStats;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		mLinkName = getArguments().getString("linkName");
		mStats = getArguments().getBoolean("stats");
		mURL = getArguments().getString("url");
		if (mURL.endsWith(".pdf")) {
			try {
				mURL = "http://docs.google.com/viewer?embedded=true&url=" + URLEncoder.encode(mURL, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Logger.e(LOG_TAG, "Error encoding PDF URL", e);
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_browser, container, false);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		BrowserActivity.mWebView = (WebView) getActivity().findViewById(R.id.webview);
		BrowserActivity.mWebView.getSettings().setJavaScriptEnabled(true);
		BrowserActivity.mWebView.getSettings().setDefaultFontSize(20);
		BrowserActivity.mWebView.getSettings().setBuiltInZoomControls(true);
		BrowserActivity.mWebView.getSettings().setLoadWithOverviewMode(true);
		BrowserActivity.mWebView.getSettings().setAllowFileAccess(true);
		BrowserActivity.mWebView.getSettings().setUserAgentString(BrowserActivity.mWebView.getSettings().getUserAgentString() + " sidhelpmobileapp");
		BrowserActivity.mWebView.getSettings().setUseWideViewPort(true);
		BrowserActivity.mWebView.getSettings().setSupportMultipleWindows(true);
		BrowserActivity.mWebView.setWebChromeClient(new WebChromeListener(this));
		BrowserActivity.mWebView.setWebViewClient(new WebViewListener(this, mPos, mTeams, BrowserActivity.mWebView, true));
		if (savedInstanceState == null) {
			BrowserActivity.mWebView.loadUrl(mURL);
			BrowserActivity.mWebView.refreshDrawableState();
		} else {
			BrowserActivity.mWebView.restoreState(savedInstanceState);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		BrowserActivity.mWebView.saveState(outState);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!mStats) {
			if (!mTeams) {
				tracker.sendView("Link - " + mLinkName);
			} else {
				tracker.sendView("Team Link - " + mSportName + " - " + mLinkName);
			}
		} else {
			tracker.sendView("Stats");
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getSherlockActivity().getSupportActionBar().setTitle(mLinkName);
		if (mTeams) {
			TeamsActivity.stacked = true;
		}
	}
}
