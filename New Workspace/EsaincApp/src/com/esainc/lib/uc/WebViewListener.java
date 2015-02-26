package com.esainc.lib.uc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.ui.ArticleFragment;
import com.esainc.lib.ui.BrowserActivity;
import com.esainc.lib.ui.CoachesBioFragment;
import com.esainc.lib.ui.MainActivity;
import com.esainc.lib.ui.RosterBioFragment;
import com.esainc.lib.ui.TeamsActivity;
import com.esainc.lib.ui.ThumbnailFragment;
import com.google.analytics.tracking.android.Log;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewListener extends WebViewClient {
	private static final String LOG_TAG = "Web View Listener";
	private static Fragment mFragment;
	private static int mPos;
	private static boolean mTeams;
	private static boolean mBrowser;
	private WebView mWebView;
	
	
	public WebViewListener(Fragment fragment, int pos, boolean team, WebView webView) {
		this(fragment, pos, team, webView, false);
	}
	
	public WebViewListener(Fragment fragment, int pos, boolean team, WebView webView, boolean browser) {
		mFragment = fragment;
		mPos = pos;
		mTeams = team;
		mWebView = webView;
		mBrowser = browser;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.toLowerCase(Locale.US).startsWith("player:")) {
			// Load Player Bio
			Intent dataIntent = new Intent(mFragment.getActivity(), DataLoader.class);
			Messenger dataMessenger = new Messenger(mHandler);
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("tabPos", mPos);
			dataIntent.putExtra("type", DataType.CheckCoach.ordinal());
			String params[] = new String[] {url.toLowerCase(Locale.US).replaceFirst("player:", "")};
			dataIntent.putExtra("params", params);
			mFragment.getActivity().startService(dataIntent);
		} else if (url.toLowerCase(Locale.US).startsWith("youtube:")) {
			// Open youtube video in youtube app
			mFragment.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + url.substring(8))));
		} else if (url.toLowerCase(Locale.US).startsWith("gallery:")) {
			// Open gallery link
			Intent dataIntent = new Intent(mFragment.getActivity(), DataLoader.class);
			Messenger dataMessenger = new Messenger(mHandler);
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("tabPos", mPos);
			dataIntent.putExtra("type", DataType.GalleryPhotos.ordinal());
			String params[] = new String[] {url.toLowerCase(Locale.US).replaceFirst("gallery:", "")};
			dataIntent.putExtra("params", params);
			mFragment.getActivity().startService(dataIntent);
		} else if (url.toLowerCase(Locale.US).startsWith("tel:")) {
			// Open up caller
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse(url));
			mFragment.startActivity(callIntent);
		} else if (url.toLowerCase(Locale.US).startsWith("mailto:")) {
			// Open up email
			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse(url));
			mFragment.startActivity(intent);
		} else if (url.toLowerCase(Locale.US).startsWith("maps:")) {
			url = "http://maps.google.com/" + url.substring(url.indexOf("?"));
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			mFragment.startActivity(mapIntent);
		} else if (url.toLowerCase(Locale.US).startsWith("navigateinapp:")) {
			TypedArray tabs = mFragment.getActivity().getResources().obtainTypedArray(R.array.TabBars);
			int i;
			for (i = 0; i < tabs.length(); i++) {
				String tabInfo[] = mFragment.getActivity().getResources().getStringArray(tabs.getResourceId(i, 0));
				if (tabInfo[0].equalsIgnoreCase(url.substring(url.indexOf(":") + 1))) {
					break;
				}
			}
			tabs.recycle();
			MainActivity.goToTab(i);
		} else if (url.toLowerCase(Locale.US).endsWith(".m3u8")) {
			Intent videoIntent = new Intent(Intent.ACTION_VIEW);
			videoIntent.setDataAndType(Uri.parse(url), "video/*");
			mFragment.startActivity(videoIntent);
		} else if (url.toLowerCase(Locale.US).startsWith("http:") || url.toLowerCase(Locale.getDefault()).startsWith("https:")) {
			if (url.endsWith(".pdf") && !url.startsWith("https://docs.google.com/viewer?embedded=true&url=")) {
				try {
					url = "http://docs.google.com/viewer?embedded=true&url=" + URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					Logger.e(LOG_TAG, "Error encoding PDF URL", e);
				}
			}
			TypedArray tabs;
			if (!mTeams) {
				tabs = mFragment.getActivity().getResources().obtainTypedArray(R.array.TabBars);
			} else {
				tabs = mFragment.getActivity().getResources().obtainTypedArray(R.array.TeamWiseTabBars);
			}
			String tabInfo[] = mFragment.getActivity().getResources().getStringArray(tabs.getResourceId(mPos, 0));
			String htmlLinkAction = tabInfo[3];
			tabs.recycle();
			if (mBrowser) {
				mWebView.loadUrl(url);
			} else if (htmlLinkAction.equalsIgnoreCase("browser")) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				mFragment.startActivity(browserIntent);
			} else if (htmlLinkAction.equalsIgnoreCase("samepage")) {
				mWebView.loadUrl(url);
			} else {
				Intent browserIntent = new Intent(mFragment.getActivity(), BrowserActivity.class);
				browserIntent.putExtra("pos", mPos);
				browserIntent.putExtra("teams", mTeams);
				browserIntent.putExtra("linkName", "");
				browserIntent.putExtra("url", url);
				browserIntent.putExtra("stats", false);
				if (mTeams) {
					browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
					browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
					browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
					browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
				}
				mFragment.startActivity(browserIntent);
			}
		}
		return true;
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		Log.i("Loading page");
		if (mBrowser) {
			((BrowserActivity) mFragment.getActivity()).setRefreshActionButtonState(true);
		}
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.i("Finished Loading");
		if (mBrowser && mFragment.getActivity() != null && mFragment.getActivity() instanceof BrowserActivity) {
			((BrowserActivity) mFragment.getActivity()).setRefreshActionButtonState(false);
			((BrowserActivity) mFragment.getActivity()).setForwardAndBackStates();
		}
	}
	
	@SuppressLint("HandlerLeak")
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			switch (bundle.getInt("code")) {
				case 200:
					if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
						boolean startFragment = true;
						DataType type = DataType.values()[bundle.getInt("type")];
						final FragmentTransaction ft = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
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
							case PlayersBio:
								newFragment = new RosterBioFragment();
								args.putInt("pos", mPos);
								args.putBoolean("teams", mTeams);
								args.putString("data", bundle.getString("data"));
								tag = "Roster Bio";
								break;
							case CoachesBio:
								newFragment = new CoachesBioFragment();
								args.putInt("pos", mPos);
								args.putBoolean("teams", mTeams);
								args.putString("data", bundle.getString("data"));
								tag = "Coach Bio";
								break;
							case CheckCoach:
								startFragment = false;
								JSONObject hdata;
								try {
									hdata = new JSONArray(bundle.getString("data")).getJSONObject(0);
									String rosterID = hdata.getString(mFragment.getResources().getString(R.string.tag_rosterID));
									String coach = hdata.getString(mFragment.getResources().getString(R.string.tag_coach));
									
									Intent dataIntent = new Intent(mFragment.getActivity(), DataLoader.class);
									Messenger dataMessenger = new Messenger(mHandler);
									dataIntent.putExtra("MESSENGER", dataMessenger);
									dataIntent.putExtra("tabPos", mPos);
									if (coach.equals("0")) {
										// Load player bio
										dataIntent.putExtra("type", DataType.PlayersBio.ordinal());
									} else {
										// Load coach bio
										dataIntent.putExtra("type", DataType.CoachesBio.ordinal());
									}
									String params[] = new String[] {rosterID};
									dataIntent.putExtra("params", params);
									mFragment.getActivity().startService(dataIntent);
								} catch (JSONException e) {
									Logger.e(LOG_TAG, "Error checking for coach", e);
								}
								break;
							default:
								break;
						}
						if (startFragment) {
							newFragment.setArguments(args);
							ft.replace(android.R.id.content, newFragment, tag);
							ft.addToBackStack(null);
							ft.commit();
						}
					}
					break;
				case 419:
					AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mFragment.getActivity());
					dlgAlert.setMessage(mFragment.getResources().getString(R.string.error_hidden));
					dlgAlert.setPositiveButton("OK", null);
					dlgAlert.setCancelable(false);
					dlgAlert.create().show();
					break;
				default:
					break;
			}
		}
	};
}
