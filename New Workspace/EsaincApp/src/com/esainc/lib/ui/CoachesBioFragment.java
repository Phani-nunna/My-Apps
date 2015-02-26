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
import android.text.TextUtils;
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
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class CoachesBioFragment extends SherlockFragment {
	private static final String LOG_TAG = "Coaches Bio Fragment";
	private WebView webDetailView;
	private Tracker tracker;
	private int mPos;
	private JSONArray mData;
	private String title;
	private boolean mTeams;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		try {
			mData = new JSONArray(getArguments().getString("data"));
			JSONObject hdata = mData.getJSONObject(0);
			title = hdata.getString(getResources().getString(R.string.tag_fname)) + " " + hdata.getString(getResources().getString(R.string.tag_lname));
			getSherlockActivity().getSupportActionBar().setTitle(title);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error loading coach bio data", e);
		}
		
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (mTeams) {
			TeamsActivity.stacked = true;
		}
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_team_coaches_detail, container, false);
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "InlinedApi" })
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		try {
			JSONObject hdata = mData.getJSONObject(0);
			
			String photoURL = hdata.getString(getResources().getString(R.string.tag_photo)).replaceAll(" ", "%20");
			
			// Setup HTML for bio
			String data = null;
			StringBuffer dataBuffer = new StringBuffer();
			
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
								"font-size: 1.0em; " +
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
						"<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>" +
					"</head>" +
					"<body>" +
						"<center>");
			
			if (!TextUtils.isEmpty(photoURL)) {
				if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
					int displayWidth = getResources().getInteger(R.integer.RosterImageWidth);
					int displayHeight = getResources().getInteger(R.integer.RosterImageHeight);
					if (displayWidth > 200){
						displayHeight = (int) (200 * ((float) displayHeight/(float) displayWidth));
						displayWidth = 200;
					}
					dataBuffer.append("<div class=\"rosterPhoto\"><img alt=\"\" src=\"" + photoURL + "\" height=\"" + displayHeight + "\" width=\"" + displayWidth + "\"></div>");
				} else {
					dataBuffer.append("<div class=\"rosterPhoto\"><img alt=\"\" src=\"" + photoURL + "\"></div>");
				}
			}
			
			dataBuffer.append("<div class=\"name\">" + hdata.getString(getResources().getString(R.string.tag_fname)) + " " + hdata.getString(getResources().getString(R.string.tag_lname)) + "</div><div class=\"type\">" + hdata.getString(getResources().getString(R.string.tag_coachType)));
			
			if (!TextUtils.isEmpty(hdata.getString(getResources().getString(R.string.tag_yearsCoached)))) {
				dataBuffer.append(hdata.getString(getResources().getString(R.string.tag_yearsCoached)));
			}
			
			dataBuffer.append("</div>" + hdata.getString(getResources().getString(R.string.tag_almaMater)) + "</center>");
			
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
			webDetailView.setWebViewClient(new WebViewListener(this, mPos, mTeams, webDetailView));
			webDetailView.loadData(data, "text/html", "UTF-8");
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data for display", e);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Coaches Bio - " + title);
	}
}
