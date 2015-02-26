package com.esainc.lib.ui;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;

@SuppressLint("UseSparseArrays")
public class SplashScreen extends Activity {
	private static final String LOG_TAG = "Splash Screen";
	private HashMap<Integer, String> tabData = new HashMap<Integer, String>();
	private HashMap<Integer, String> tabExtra = new HashMap<Integer, String>();
	private int tabCount;
	public static SplashScreen instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		if (!((SIDHelpApplication) getApplicationContext()).isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setContentView(R.layout.activity_splashscreen);
		
		// Check connectivity
		boolean haveConnectedWifi = false;
		boolean haveConenctedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
				haveConnectedWifi = true;
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
				haveConenctedMobile = true;
			}
		}
		if (!haveConnectedWifi && !haveConenctedMobile) {
			// Display message showing error
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SplashScreen.instance);
			dlgAlert.setMessage(getResources().getString(R.string.error_no_connection));
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			dlgAlert.setCancelable(false);
			dlgAlert.create().show();
			return;
		}
		
		Intent dataIntent = new Intent(this, DataLoader.class);
		Messenger dataMessenger = new Messenger(mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", 0);
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		String tabInfo[] = getResources().getStringArray(tabs.getResourceId(0, 0));
		tabCount = tabs.length();
		if (tabInfo[0].equalsIgnoreCase("customizedNewsPage")) {
			Logger.i(LOG_TAG, "Loading Headlines");
			dataIntent.putExtra("type", DataType.Headlines.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("schedule")) {
			Logger.i(LOG_TAG, "Loading Scoreboard Recent");
			dataIntent.putExtra("type", DataType.ScoreboardRecent.ordinal());
			startService(dataIntent);
			
			// Load Today as well
			Logger.i(LOG_TAG, "Loading Scoreboard Today");
			dataIntent = new Intent(this, DataLoader.class);
			dataMessenger = new Messenger(mHandler);
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("tabPos", tabCount++);
			dataIntent.putExtra("type", DataType.ScoreboardToday.ordinal());
			startService(dataIntent);
			
			// Load Upcoming as well
			Logger.i(LOG_TAG, "Loading Scoreboard Upcoming");
			dataIntent = new Intent(this, DataLoader.class);
			dataMessenger = new Messenger(mHandler);
			dataIntent.putExtra("MESSENGER", dataMessenger);
			dataIntent.putExtra("tabPos", tabCount++);
			dataIntent.putExtra("type", DataType.ScoreboardUpcoming.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("teams")) {
			Logger.i(LOG_TAG, "Loading Teams");
			dataIntent.putExtra("type", DataType.Teams.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("photos")) {
			Logger.i(LOG_TAG, "Loading Photos");
			dataIntent.putExtra("type", DataType.Galleries.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("staff")) {
			Logger.i(LOG_TAG, "Loading Staff");
			dataIntent.putExtra("type", DataType.Staff.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("youtubePlaylist")) {
			Logger.i(LOG_TAG, "Loading Youtube Playlist");
			dataIntent.putExtra("type", DataType.Youtube.ordinal());
		} else if (tabInfo[0].startsWith("CustomTab")) {
			Logger.i(LOG_TAG, "Loading Custom Tab");
			dataIntent.putExtra("type", DataType.Custom.ordinal());
		} else if (tabInfo[0].equalsIgnoreCase("links")) {
			Logger.i(LOG_TAG, "Loading Links");
			dataIntent.putExtra("type", DataType.Links.ordinal());
		}else if (tabInfo[0].equalsIgnoreCase("mediaschedule")) {
			Logger.i(LOG_TAG, "Loading MediaSchedule");
			dataIntent.putExtra("type", DataType.MediaSchedule.ordinal());
		}
		tabs.recycle();
		startService(dataIntent);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				
				if (bundle.getString("data").startsWith("Error:")) {
					final String msg = bundle.getString("data");
					
					if (msg.startsWith("Error:")) {
						// Loading error
						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SplashScreen.instance);
						dlgAlert.setMessage(getResources().getString(R.string.error_site_unreachable));
						dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});
						dlgAlert.setCancelable(false);
						dlgAlert.create().show();
					} else {
						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SplashScreen.instance);
						switch (bundle.getInt("code")) {
							case 410:	// App is no longer supported
								dlgAlert.setMessage(getResources().getString(R.string.error_unsupported));
								dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}
								});
								dlgAlert.setCancelable(false);
								dlgAlert.create().show();
								break;
							case 426:	// Newer version error
								dlgAlert.setMessage(getResources().getString(R.string.error_new_version));
								dlgAlert.setPositiveButton("Download", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										String link = msg.substring(msg.indexOf("|") + 1);
										link = link.replace("\\/", "/");
										Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
										startActivity(browserIntent);
										finish();
									}
								});
								dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}
								});
								dlgAlert.setCancelable(false);
								dlgAlert.create().show();
								break;
							default:
								break;
						}
					}
				} else {
					// Handle actual data
					tabData.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("data"));
					if (bundle.getString("extra") != null) tabExtra.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("extra"));
					Intent intent = new Intent(SplashScreen.this, MainActivity.class);
					intent.putExtra("tabData", tabData);
					intent.putExtra("tabExtra", tabExtra);
					startActivity(intent);
					finish();
				}
			}
		}
	};
}
