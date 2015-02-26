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
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;

public class TeamsActivity extends SherlockFragmentActivity {
	private static final String LOG_TAG = "Teams Activity";
	public static HashMap<Integer, String> tabData;
	public static HashMap<Integer, String> tabExtra;
	public static HashMap<Integer, Integer> tabCode;
	public static ActionBar mActionBar;
	public MenuItem mRefresh;
	public static int mCurrentPos;
	public static String mCurrentTag;
	private String mSportID;
	public static String mSportName = "Sport Name";
	public static int tabCount;
	private String mRecord;
	public static boolean stacked = false;
	public static boolean showLinks = true;
	public static boolean showCoach = true;
	public static boolean showRoster = true;
	public static boolean showSchedule = true;
	public static boolean configChanged = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState != null) {
			HashMap<String, Object> savedData = (HashMap<String, Object>) savedInstanceState.getSerializable("savedData");
			if (savedData != null && savedData.get("sportID").equals(getIntent().getExtras().getString("sportID"))) {
				tabData = (HashMap<Integer, String>) savedData.get("tabData");
				tabExtra = (HashMap<Integer, String>) savedData.get("tabExtra");
				tabCode = (HashMap<Integer, Integer>) savedData.get("tabCode");
				mSportID = ((String) savedData.get("sportID"));
				mSportName = ((String) savedData.get("sportName"));
				showLinks = ((Boolean) savedData.get("showLinks"));
				showSchedule = ((Boolean) savedData.get("showSchedule"));
				showRoster = ((Boolean) savedData.get("showRoster"));
				showCoach = ((Boolean) savedData.get("showCoach"));
				mRecord = ((String) savedData.get("record"));
				if (!configChanged) {
					mCurrentPos = ((Integer) savedData.get("pos")).intValue();
					mCurrentTag = ((String) savedData.get("tag"));
					configChanged = true;
				}
			}
		} else {
			tabData = (HashMap<Integer, String>) getIntent().getExtras().getSerializable("tabData");
			tabExtra = (HashMap<Integer, String>) getIntent().getExtras().getSerializable("tabExtra");
			tabCode = (HashMap<Integer, Integer>) getIntent().getExtras().getSerializable("tabCode");
			mRecord = getIntent().getExtras().getString("record");
			mSportID = getIntent().getExtras().getString("sportID");
			mSportName = getIntent().getExtras().getString("sportName");
			mCurrentPos = 0;
			mCurrentTag = null;
			showLinks = getIntent().getExtras().getString("showLinks").equals("1");
			showSchedule = getIntent().getExtras().getString("showSchedule").equals("1");
			showRoster = getIntent().getExtras().getString("showRoster").equals("1");
			showCoach = getIntent().getExtras().getString("showCoach").equals("1");
			TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
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
				String params[] = new String[0];
				if (tabInfo[0].equalsIgnoreCase("teamwise_news")) {
					Logger.i(LOG_TAG, "Loading Sport Headlines");
					dataIntent.putExtra("type", DataType.SportHeadlines.ordinal());
					params = new String[] {mSportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_schedule")) {
					Logger.i(LOG_TAG, "Loading Sport Schedule");
					dataIntent.putExtra("type", DataType.Schedule.ordinal());
					params = new String[] {mSportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_roster")) {
					Logger.i(LOG_TAG, "Loading Sport Roster");
					dataIntent.putExtra("type", DataType.Players.ordinal());
					params = new String[] {mSportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_coaches")) {
					if (((SIDHelpApplication) getApplicationContext()).isTablet()) {
						Logger.i(LOG_TAG, "Loading Sport Coaches Bios");
						dataIntent.putExtra("type", DataType.CoachesBioSport.ordinal());
					} else {
						Logger.i(LOG_TAG, "Loading Sport Coaches");
						dataIntent.putExtra("type", DataType.Coaches.ordinal());
					}
					params = new String[] {mSportID};
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_links")) {
					Logger.i(LOG_TAG, "Loading Sport Links");
					dataIntent.putExtra("type", DataType.SportLinks.ordinal());
					params = new String[] {mSportID};
				}
				dataIntent.putExtra("params", params);
				startService(dataIntent);
			}
			tabs.recycle();
		}
		
		mActionBar = getSupportActionBar();
		mActionBar.removeAllTabs();
		if (!((SIDHelpApplication) getApplicationContext()).isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
		Tab tab;
		for (int i = 0; i < tabs.length(); i++) {
			String tabInfo[] = getResources().getStringArray(tabs.getResourceId(i, 0));
			tab = mActionBar.newTab().setText(tabInfo[1]);
			
			if (tabInfo[0].equalsIgnoreCase("teamwise_news")) {
				tab.setTabListener(new TeamsTabListener<HeadlinesFragment>(TeamsActivity.this, i, HeadlinesFragment.class, "Sport Headlines"));
				mActionBar.addTab(tab);
			} else if (tabInfo[0].equalsIgnoreCase("teamwise_schedule") && showSchedule) {
				tab.setTabListener(new TeamsTabListener<ScheduleFragment>(TeamsActivity.this, i, ScheduleFragment.class, "Sport Schedule"));
				mActionBar.addTab(tab);
			} else if (tabInfo[0].equalsIgnoreCase("teamwise_roster") && showRoster) {
				tab.setTabListener(new TeamsTabListener<RosterFragment>(TeamsActivity.this, i, RosterFragment.class, "Sport Players"));
				mActionBar.addTab(tab);
			} else if (tabInfo[0].equalsIgnoreCase("teamwise_coaches") && showCoach) {
				tab.setTabListener(new TeamsTabListener<CoachesFragment>(TeamsActivity.this, i, CoachesFragment.class, "Sport Coaches"));
				mActionBar.addTab(tab);
			} else if (tabInfo[0].equalsIgnoreCase("teamwise_links") && showLinks) {
				tab.setTabListener(new TeamsTabListener<LinksFragment>(TeamsActivity.this, i, LinksFragment.class, "Sport Links"));
				mActionBar.addTab(tab);
			} else {
			//mActionBar.addTab(tab);
			}
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
				if (bundle.getString("record") != null) mRecord = bundle.getString("record");
				if (bundle.getString("extra") != null) tabExtra.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getString("extra"));
				tabCode.put(Integer.valueOf(bundle.getInt("tabPos")), bundle.getInt("code"));
				DataType type = DataType.values()[bundle.getInt("type")];
				if (type == DataType.SportLinks) {
					// Check for special links
					try {
						JSONArray mLinks = new JSONArray(bundle.getString("data"));
						for (int i = 0; i < mLinks.length(); i++) {
							JSONObject hdata = mLinks.getJSONObject(i);
							String linkURL = hdata.getString(getResources().getString(R.string.tag_LinkUrl));
							if (linkURL.equalsIgnoreCase("staff list")) {
								Logger.i(LOG_TAG, "Loading Staff");
								Intent dataIntent  = new Intent(TeamsActivity.this, DataLoader.class);
								Messenger dataMessenger = new Messenger(mHandler);
								dataIntent.putExtra("MESSENGER", dataMessenger);
								dataIntent.putExtra("tabPos", tabCount + 2);
								dataIntent.putExtra("type", DataType.Staff.ordinal());
								startService(dataIntent);
							} else if (linkURL.equalsIgnoreCase("photo gallery")) {
								Logger.i(LOG_TAG, "Loading Photos");
								Intent dataIntent  = new Intent(TeamsActivity.this, DataLoader.class);
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
				if (mCurrentPos == bundle.getInt("tabPos")) {
					switch (type) {
						case SportHeadlines:
							((HeadlinesFragment) getSupportFragmentManager().findFragmentByTag("Sport Headlines")).setData(bundle.getString("data"));
							break;
						case SportHeadlinesSearch:
							((HeadlinesFragment) getSupportFragmentManager().findFragmentByTag("Sport Headlines")).setData(bundle.getString("data"), true);
							break;
						case Schedule:
							((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("Sport Schedule")).setData(bundle.getString("data"));
							((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("Sport Schedule")).setExtra(bundle.getString("extra"));
							((ScheduleFragment) getSupportFragmentManager().findFragmentByTag("Sport Schedule")).setRecord(bundle.getString("record"));
							break;
						case Roster:
							((RosterFragment) getSupportFragmentManager().findFragmentByTag("Sport Players")).setData(bundle.getString("data"));
							((RosterFragment) getSupportFragmentManager().findFragmentByTag("Sport Players")).setCode(bundle.getInt("code"));
							break;
						case Players:
							((RosterFragment) getSupportFragmentManager().findFragmentByTag("Sport Players")).setData(bundle.getString("data"));
							((RosterFragment) getSupportFragmentManager().findFragmentByTag("Sport Players")).setCode(bundle.getInt("code"));
							break;
						case Coaches:
						case CoachesBioSport:
							((CoachesFragment) getSupportFragmentManager().findFragmentByTag("Sport Coaches")).setData(bundle.getString("data"));
							((CoachesFragment) getSupportFragmentManager().findFragmentByTag("Sport Coaches")).setCode(bundle.getInt("code"));
							break;
						case SportLinks:
							((LinksFragment) getSupportFragmentManager().findFragmentByTag("Sport Links")).setData(bundle.getString("data"));
							break;
						case Custom:
							((CustomFragment) getSupportFragmentManager().findFragmentByTag("Sport Custom")).setData(bundle.getString("data"));
							break;
						case Staff:
							((StaffFragment) getSupportFragmentManager().findFragmentByTag("Staff")).setData(bundle.getString("data"));
							break;
						case Galleries:
							((GalleryFragment) getSupportFragmentManager().findFragmentByTag("Photos")).setData(bundle.getString("data"));
							break;
						case GalleryPhotos:
							((ThumbnailFragment) getSupportFragmentManager().findFragmentByTag("Thumbnails")).setData(bundle.getString("data"));
							break;
						default:
							break;
					}
					setRefreshActionButtonState(false);
				}
			}
		}
	};
	
	public Handler getHandler() {
		return mHandler;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.teams, menu);
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			if (!stacked) {
				NavUtils.navigateUpFromSameTask(this);
				return true;
			}
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		HashMap<String, Object> savedData = new HashMap<String, Object>();
		savedData.put("tabData", tabData);
		savedData.put("tabExtra", tabExtra);
		savedData.put("tabCode", tabCode);
		savedData.put("sportID", mSportID);
		savedData.put("sportName", mSportName);
		savedData.put("pos", Integer.valueOf(mCurrentPos));
		savedData.put("tag", mCurrentTag);
		savedData.put("showLinks", showLinks);
		savedData.put("showRoster", showRoster);
		savedData.put("showSchedule", showSchedule);
		savedData.put("showCoach", showCoach);
		savedData.put("record", mRecord);
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
	
	private class TeamsTabListener<T extends Fragment> implements TabListener {
		private Fragment mFragment;
		private final SherlockFragmentActivity mActivity;
		private final int mPos;
		private final Class<T> mClass;
		private final String mTag;
		
		/** Constructor used each time a new tab is created. */
		public TeamsTabListener(SherlockFragmentActivity activity, int pos, Class<T> clz, String tag) {
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
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				Bundle args = new Bundle();
				if (mClass.isAssignableFrom(ScheduleFragment.class)) {
					args.putString("record", mRecord);
				}
				args.putString("data", tabData.get(mPos));
				if (tabExtra.get(mPos) != null) args.putString("extra", tabExtra.get(mPos));
				args.putInt("pos", mPos);
				args.putBoolean("teams", true);
				args.putString("sportID", mSportID);
				args.putString("record", mRecord);
				if (tabCode.get(mPos) != null) {
					args.putInt("code", tabCode.get(mPos));
				} else {
					args.putInt("code", 200);
				}
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
			mActionBar.setTitle(mSportName);
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
