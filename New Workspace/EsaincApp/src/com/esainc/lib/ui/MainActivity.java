package com.esainc.lib.ui;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.adapters.ScoreboardAdapter.Scoreboards;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;

public class MainActivity extends SherlockFragmentActivity {
	private static final String LOG_TAG = "Main Activity";
	public static HashMap<Integer, String> tabData;
	public static HashMap<Integer, String> tabExtra;
	public static ActionBar mActionBar;
	public MenuItem mRefresh;
	public static int mCurrentPos;
	public static String mCurrentTag;
	public static int tabCount;
	private int scoreboardTabPos;
	public boolean recentRefreshed = false;
	public boolean todayRefreshed = false;
	public boolean upcomingRefreshed = false;
	public static boolean configChanged = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			//HashMap<String, Object> savedData = (HashMap<String, Object>) getLastCustomNonConfigurationInstance();

			HashMap<String, Object> savedData = (HashMap<String, Object>) savedInstanceState.getSerializable("savedData");
			if (savedData != null) {
				tabData = (HashMap<Integer, String>) savedData.get("tabData");
				tabExtra = (HashMap<Integer, String>) savedData.get("tabExtra");
				tabCount = ((Integer) savedData.get("tabCount")).intValue();
				if (!configChanged) {
					mCurrentPos = ((Integer) savedData.get("pos")).intValue();
					mCurrentTag = ((String) savedData.get("tag"));
					configChanged = true;
				}
			}
		} else {
			tabData = (HashMap<Integer, String>) getIntent().getExtras().getSerializable("tabData");
			tabExtra = (HashMap<Integer, String>) getIntent().getExtras().getSerializable("tabExtra");
			mCurrentPos = 0;
			mCurrentTag = null;
			
			TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
			String tabInfo[];
			Intent dataIntent;
			Messenger dataMessenger;
			
			tabCount = tabs.length();
			for (int i = 1; i < tabCount; i++) {
				dataIntent = new Intent(this, DataLoader.class);
				dataMessenger = new Messenger(mHandler);
				dataIntent.putExtra("MESSENGER", dataMessenger);
				dataIntent.putExtra("tabPos", i);
				tabInfo = getResources().getStringArray(tabs.getResourceId(i, 0));
				if (tabInfo[0].equalsIgnoreCase("customizedNewsPage")) {
					Logger.i(LOG_TAG, "Loading Headlines");
					dataIntent.putExtra("type", DataType.Headlines.ordinal());
				} else if (tabInfo[0].equalsIgnoreCase("schedule")) {
					scoreboardTabPos = i;
					Logger.i(LOG_TAG, "Loading Scoreboard Recent");
					dataIntent.putExtra("type", DataType.ScoreboardRecent.ordinal());
					startService(dataIntent);
					
					// Load Today as well
					Logger.i(LOG_TAG, "Loading Scoreboard Today");
					dataIntent = new Intent(this, DataLoader.class);
					dataMessenger = new Messenger(mHandler);
					dataIntent.putExtra("MESSENGER", dataMessenger);
					dataIntent.putExtra("tabPos", tabCount);
					dataIntent.putExtra("type", DataType.ScoreboardToday.ordinal());
					startService(dataIntent);
					
					// Load Upcoming as well
					Logger.i(LOG_TAG, "Loading Scoreboard Upcoming");
					dataIntent = new Intent(this, DataLoader.class);
					dataMessenger = new Messenger(mHandler);
					dataIntent.putExtra("MESSENGER", dataMessenger);
					dataIntent.putExtra("tabPos", tabCount + 1);
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
					dataIntent.putExtra("customTag", tabInfo[0]);
				} else if (tabInfo[0].equalsIgnoreCase("links")) {
					Logger.i(LOG_TAG, "Loading Links");
					dataIntent.putExtra("type", DataType.Links.ordinal());
				}else if(tabInfo[0].equalsIgnoreCase("mediaschedule")){
					Logger.i(LOG_TAG, "Loading Media Schedule");
					dataIntent.putExtra("type", DataType.MediaSchedule.ordinal());
				}
				startService(dataIntent);
			}
			tabs.recycle();
			
		}
		
		mActionBar = getSupportActionBar();
		if (!((SIDHelpApplication) getApplicationContext()).isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
		Tab tab;
		for (int i = 0; i < tabs.length(); i++) {
			String tabInfo[] = getResources().getStringArray(tabs.getResourceId(i, 0));
			tab = mActionBar.newTab().setText(tabInfo[1]);
			
			if (tabInfo[0].equalsIgnoreCase("customizedNewsPage")) {
				tab.setTabListener(new MainTabListener<HeadlinesFragment>(MainActivity.this, i, HeadlinesFragment.class, "Headlines"));
			} else if (tabInfo[0].equalsIgnoreCase("schedule")) {
				tab.setTabListener(new MainTabListener<ScoreboardFragment>(MainActivity.this, i, ScoreboardFragment.class, "Scoreboard"));
			} else if (tabInfo[0].equalsIgnoreCase("teams")) {
				tab.setTabListener(new MainTabListener<TeamsFragment>(MainActivity.this, i, TeamsFragment.class, "Teams"));
			} else if (tabInfo[0].equalsIgnoreCase("photos")) {
				tab.setTabListener(new MainTabListener<GalleryFragment>(MainActivity.this, i, GalleryFragment.class, "Photos"));
			} else if (tabInfo[0].equalsIgnoreCase("staff")) {
				tab.setTabListener(new MainTabListener<StaffFragment>(MainActivity.this, i, StaffFragment.class, "Staff"));
			} else if (tabInfo[0].equalsIgnoreCase("youtubePlaylist")) {
				tab.setTabListener(new MainTabListener<YoutubeFragment>(MainActivity.this, i, YoutubeFragment.class, "Youtube"));
			} else if (tabInfo[0].startsWith("CustomTab")) {
				tab.setTabListener(new MainTabListener<CustomFragment>(MainActivity.this, i, CustomFragment.class, tabInfo[0]));
			} else if (tabInfo[0].equals("UniversitySound")) {
				tab.setTabListener(new MainTabListener<SoundFragment>(MainActivity.this, i, SoundFragment.class, "Sound"));
			} else if (tabInfo[0].equalsIgnoreCase("links")) {
				tab.setTabListener(new MainTabListener<LinksFragment>(MainActivity.this, i, LinksFragment.class, "Links"));
			}else if (tabInfo[0].equalsIgnoreCase("mediaschedule")) {
				tab.setTabListener(new MainTabListener<MediaScheduleFragment>(MainActivity.this, i, MediaScheduleFragment.class, "MediaSchedule"));
			}
			mActionBar.addTab(tab);
		}
		tabs.recycle();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Set navbar for ICS+ devices
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			Bundle bundle = message.getData();
			if (bundle.getInt("result") == Activity.RESULT_OK && bundle.getString("data") != null) {
				tabData.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("data"));
				if (bundle.getString("extra") != null) tabExtra.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("extra"));
				DataType type = DataType.values()[bundle.getInt("type")];
				if (type == DataType.Links) {
					// Check for special links
					try {
						JSONArray mLinks = new JSONArray(bundle.getString("data"));
						for (int i = 0; i < mLinks.length(); i++) {
							JSONObject hdata = mLinks.getJSONObject(i);
							String linkURL = hdata.getString(getResources().getString(R.string.tag_LinkUrl));
							if (linkURL.equalsIgnoreCase("staff list")) {
								Logger.i(LOG_TAG, "Loading Staff");
								Intent dataIntent  = new Intent(MainActivity.this, DataLoader.class);
								Messenger dataMessenger = new Messenger(mHandler);
								dataIntent.putExtra("MESSENGER", dataMessenger);
								dataIntent.putExtra("tabPos", tabCount + 2);
								dataIntent.putExtra("type", DataType.Staff.ordinal());
								startService(dataIntent);
							} else if (linkURL.equalsIgnoreCase("photo gallery")) {
								Logger.i(LOG_TAG, "Loading Photos");
								Intent dataIntent  = new Intent(MainActivity.this, DataLoader.class);
								Messenger dataMessenger = new Messenger(mHandler);
								dataIntent.putExtra("MESSENGER", dataMessenger);
								dataIntent.putExtra("tabPos", tabCount + 3);
								dataIntent.putExtra("type", DataType.Galleries.ordinal());
								startService(dataIntent);
							}
						}
					} catch (JSONException e) {
						Logger.e(LOG_TAG, "Error checking for custom links", e);
					}
				}
				if (mCurrentPos == bundle.getInt("tabPos") || (mCurrentPos == scoreboardTabPos && bundle.getInt("tabPos") >= tabCount && bundle.getInt("tabPos") <= tabCount + 1)) {
					switch (type) {
						case Headlines:
							((HeadlinesFragment) getSupportFragmentManager().findFragmentByTag("Headlines")).setData(bundle.getString("data"));
							break;
						case HeadlinesSearch:
							((HeadlinesFragment) getSupportFragmentManager().findFragmentByTag("Headlines")).setData(bundle.getString("data"), true);
							break;
						case ScoreboardRecent:
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setData(bundle.getString("data"), Scoreboards.Recent);
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setExtra(bundle.getString("extra"));
							recentRefreshed = true;
							break;
						case ScoreboardToday:
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setData(bundle.getString("data"), Scoreboards.Today);
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setExtra(bundle.getString("extra"));
							todayRefreshed = true;
							break;
						case ScoreboardUpcoming:
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setData(bundle.getString("data"), Scoreboards.Upcoming);
							((ScoreboardFragment) getSupportFragmentManager().findFragmentByTag("Scoreboard")).setExtra(bundle.getString("extra"));
							upcomingRefreshed = true;
							break;
						case Teams:
							((TeamsFragment) getSupportFragmentManager().findFragmentByTag("Teams")).setData(bundle.getString("data"));
							break;
						case Staff:
							((StaffFragment) getSupportFragmentManager().findFragmentByTag("Staff")).setData(bundle.getString("data"));
							break;
						case Links:
							((LinksFragment) getSupportFragmentManager().findFragmentByTag("Links")).setData(bundle.getString("data"));
							break;
						case MediaSchedule:
							((MediaScheduleFragment) getSupportFragmentManager().findFragmentByTag("MediaSchedule")).setData(bundle.getString("data"));
							((MediaScheduleFragment) getSupportFragmentManager().findFragmentByTag("MediaSchedule")).setExtra(bundle.getString("extra"));

							break;
						case Galleries:
							((GalleryFragment) getSupportFragmentManager().findFragmentByTag("Photos")).setData(bundle.getString("data"));
							break;
						case GalleryPhotos:
							((ThumbnailFragment) getSupportFragmentManager().findFragmentByTag("Thumbnails")).setData(bundle.getString("data"));
							break;
						case Youtube:
							((YoutubeFragment) getSupportFragmentManager().findFragmentByTag("Youtube")).setData(bundle.getString("data"));
							break;
						case Custom:
							((CustomFragment) getSupportFragmentManager().findFragmentByTag(bundle.getString("customTag"))).setData(bundle.getString("data"));
							break;
						default:
							break;
					}
					if (mCurrentPos != scoreboardTabPos || (recentRefreshed && todayRefreshed && upcomingRefreshed)) {
						setRefreshActionButtonState(false);
					}
				}
			}
		}
	};
	
	public Handler getHandler() {
		return mHandler;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// int itemId = item.getItemId();
		// if (itemId == R.id.refresh) {
		// return true;
		// }
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		HashMap<String, Object> savedData = new HashMap<String, Object>();
		savedData.put("tabData", tabData);
		savedData.put("tabExtra", tabExtra);
		savedData.put("pos", Integer.valueOf(mCurrentPos));
		savedData.put("tag", mCurrentTag);
		savedData.put("tabCount", tabCount);
		outState.putSerializable("savedData", savedData);

	}
	
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		mActionBar.selectTab(mActionBar.getTabAt(mCurrentPos));
		configChanged = false;
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (mRefresh != null) {
			if (refreshing) {
				mRefresh.setActionView(R.layout.actionbar_indeterminate_progress);
			} else {
				mRefresh.setActionView(null);
			}
		}
	}
	
	public static void goToTab(int pos) {
		mActionBar.selectTab(mActionBar.getTabAt(pos));
	}
	
	public static void closeBrowser(BrowserActivity browser, int pos) {
		closeBrowser(browser, pos, true);
	}
	
	public static void closeBrowser(BrowserActivity browser, int pos, boolean config) {
		browser.finish();
		mCurrentPos = pos;
		configChanged = config;
	}
	
	private class MainTabListener<T extends Fragment> implements TabListener {
		private Fragment mFragment;
		private final SherlockFragmentActivity mActivity;
		private final int mPos;
		private final Class<T> mClass;
		private final String mTag;
		
		/** Constructor used each time a new tab is created. */
		public MainTabListener(SherlockFragmentActivity activity, int pos, Class<T> clz, String tag) {
			mActivity = activity;
			mPos = pos;
			mClass = clz;
			mTag = tag;
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ignoredFt) {
			FragmentManager fragMgr = ((FragmentActivity) mActivity).getSupportFragmentManager();
			FragmentTransaction ft = fragMgr.beginTransaction();
			
			if (mCurrentTag != null && mCurrentTag.equalsIgnoreCase(mTag) && fragMgr.findFragmentByTag(mCurrentTag) != null) {
				mFragment = fragMgr.findFragmentByTag(mCurrentTag);
			}

			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				
				Bundle args = new Bundle();
				if (mClass.isAssignableFrom(ScoreboardFragment.class)) {
					args.putString("recent", tabData.get(mPos));
					args.putString("today", tabData.get(tabCount));
					args.putString("upcoming", tabData.get(tabCount + 1));
					args.putInt("recentPos", mPos);
					args.putInt("todayPos", tabData.size() - 2);
					args.putInt("upcomingPos", tabData.size() - 1);
				} else {
					args.putString("data", tabData.get(mPos));
				}
				if (tabExtra.get(mPos) != null) args.putString("extra", tabExtra.get(mPos));
				args.putInt("pos", mPos);
				args.putBoolean("teams", false);
				mFragment.setArguments(args);
				
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
			if (!configChanged) {
				mCurrentPos = mPos;
				mCurrentTag = mTag;
				mRefresh = null;
			}
			
			ft.commitAllowingStateLoss();
			TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
			String tabInfo[] = getResources().getStringArray(tabs.getResourceId(mPos, 0));
			if (!((SIDHelpApplication) getApplicationContext()).isTablet()) {
				mActionBar.setTitle(tabInfo[4]);	// Phone Tab Title
			} else {
				mActionBar.setTitle(tabInfo[5]);	// Tablet Tab Title
			}
			tabs.recycle();
		}
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ignoredFt) {
			FragmentManager fragMgr = ((FragmentActivity) mActivity).getSupportFragmentManager();
			FragmentTransaction ft = fragMgr.beginTransaction();
			if (fragMgr.getBackStackEntryCount() > 0) fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			
			// Check if the fragment is already initialized
			if (mFragment == null) {
				mFragment = SherlockFragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.detach(mFragment);
			}
			
			ft.commitAllowingStateLoss();
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ignoredFt) {
			if (!configChanged) {
				onTabUnselected(tab, ignoredFt);
				onTabSelected(tab, ignoredFt);
			}
		}
	}
}
