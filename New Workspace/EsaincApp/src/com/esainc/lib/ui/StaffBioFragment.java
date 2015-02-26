package com.esainc.lib.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class StaffBioFragment extends SherlockFragment {
	private static final String LOG_TAG = "Staff Bio Fragment";
	private WebView webDetailView;
	private Tracker tracker;
	private int mPos;
	private JSONArray mData;
	private String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		try {
			mData = new JSONArray(getArguments().getString("data"));
			JSONObject hdata = mData.getJSONObject(0);
			title = hdata.getString(getResources().getString(R.string.tag_firstName)) + " " + hdata.getString(getResources().getString(R.string.tag_lastName));
			getSherlockActivity().getSupportActionBar().setTitle(title);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error loading staff bio data", e);
		}
		
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
		return inflater.inflate(R.layout.activity_staff_detail, container, false);
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "InlinedApi" })
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		try {
			JSONObject hdata = mData.getJSONObject(0);
			
			// Setup HTML for bio
			String data = null;
			StringBuffer dataBuffer = new StringBuffer();
			
			dataBuffer.append("<html>" +
					"<head>" +
						"<style type=\"text/css\">" +
							"* {" +
							"} " +
							"p { " +
								"color:black; " +
							"} " +
							"a { " +
								"color: #" + getResources().getString(R.color.HyperlinksColor).substring(3) + "; " +
								"text-decoration: none; " +
							"} " +
							".contactInfo { " +
								"margin-bottom: 5px; " +
							"} " + 
							".hrDiv { " +
								"padding-top: 3px; " +
								"clear: both; " +
							"} " +
							".video { " +
								"position: relative; " +
								"height: 360px; " +
								"width: 480px" +
							"} " +
							".video a { " +
								"position: absolute; " +
								"display: block; " +
								"background-image: url(https://lh6.ggpht.com/NrQdFAdPSI9-hreB4C7HNhj3yXRiW1jqOOi7eFyakIx_IA-Im0huIeYCs5jTidMT2qA=w70); " +
								"background-repeat: no-repeat; " +
								"background-position: center; " +
								"height: 360px; " +
								"width: 480px; " +
							"}"
						);
			if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
				dataBuffer.append("hr { " +
								"color: #" + getResources().getString(R.color.Default).substring(3) + "; " +
								"background-color: #" + getResources().getString(R.color.Default).substring(3) + "; " +
								"height: 1px; " +
							"} " +
							".name {" +
								"font-size: 1.9em; " +
								"font-weight: bold; " +
								"padding-left: 10px;" +
							"} " +
							".position { " +
								"font-size: 1.0em; " +
								"font-style: italic; " +
								"padding-left: 15px; " +
							"} " +
							".phone { " +
								"font-size: 1.0em; " +
								"font-style: italic; " +
								"float: left; " +
								"padding-left: 15px; " +
							"} " +
							".email { " +
								"float: right; " +
								"font-size: 1.0em; " +
							"} "
						);
			} else {
				dataBuffer.append(".name {" +
								"font-size: 1.2em; " +
								"font-weight: bold; " +
							"} " +
							".position {" +
							"} " +
							".phone { " +
								"float: left; " +
							"} " +
							".email { " +
								"float: right " +
							"} "
						);
			}
			
			dataBuffer.append("</style>" +
						"<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>" +
					"</head>" +
					"<body>" +
						"<div class=\"name\">" + 
							 hdata.getString(getResources().getString(R.string.tag_firstName)) + " " + hdata.getString(getResources().getString(R.string.tag_lastName)) +
						"</div>" +
						"<div class=\"position\">" +
							hdata.getString(getResources().getString(R.string.tag_position)) + 
						"</div>" +
						"<div class=\"contactInfo\">" +
							"<div class=\"phone\">"
				);
			if (((SIDHelpApplication) getActivity().getApplicationContext()).isTablet()) {
				dataBuffer.append(hdata.getString(getResources().getString(R.string.tag_phone)));
			} else {
				dataBuffer.append("<a href=\"tel:" + hdata.getString(getResources().getString(R.string.tag_phone)) + "\">" + hdata.getString(getResources().getString(R.string.tag_phone)) + "</a>");
			}
			dataBuffer.append("</div>" +
							"<div class=\"email\">" +
								"<a href=\"mailto:" + hdata.getString(getResources().getString(R.string.tag_email)) + "\">" + hdata.getString(getResources().getString(R.string.tag_email)) + "</a>" +
							"</div>" +
						"</div>" +
						"<div class=\"hrDiv\">" +
							"<hr />" +
						"</div>");
			
			// Check bio for youtube embed
			Pattern youtubePattern = Pattern.compile("<iframe .*?src=\"http://www.youtube.com/embed/(.*?)\".*?></iframe>");
			Matcher matcher = youtubePattern.matcher(hdata.getString(getResources().getString(R.string.tag_bio)));
			while (matcher.find()) {
				matcher.appendReplacement(dataBuffer, "<div class=\"video\"><a href=\"youtube:$1\"></a><img src=\"http://img.youtube.com/vi/$1/0.jpg\"></div>");
			}
			matcher.appendTail(dataBuffer);
			
			// Close Body and HTML
			dataBuffer.append("</body></html>");
			data = dataBuffer.toString();
			
			webDetailView = (WebView) view.findViewById(R.id.webDetailView);
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
			webDetailView.setWebViewClient(new WebViewListener(this, mPos, false, webDetailView));
			webDetailView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data for display", e);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Staff Bio - " + title);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setTitle(title);
		if (getArguments().getBoolean("teams", false)) {
			TeamsActivity.stacked = true;
		}
	}
}
