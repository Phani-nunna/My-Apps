package com.esainc.lib.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;

public class BrowserActivity extends SherlockFragmentActivity {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Browser Activity";
	private ShareActionProvider mShareActionProvider;
	private Intent shareIntent;
	private MenuItem mPrev;
	private MenuItem mNext;
	private MenuItem mRefresh;
	private MenuItem mShare;
	public static WebView mWebView;
	private String mURL;
	private boolean mTeams;
	private int mCurrentPos;
	public boolean showLinks = true;
	public boolean showCoach = true;
	public boolean showRoster = true;
	public boolean showSchedule = true;
	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!((SIDHelpApplication) getApplicationContext()).isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		mURL = getIntent().getExtras().getString("url");
		mTeams =  getIntent().getExtras().getBoolean("teams");
		mCurrentPos = getIntent().getExtras().getInt("pos");
		
		FragmentManager fragMgr = getSupportFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();
		Fragment frag;
		if (fragMgr.findFragmentByTag("Browser") != null) {
			frag = fragMgr.findFragmentByTag("Browser");
			ft.attach(frag);
		} else {
			frag = Fragment.instantiate(this, BrowserFragment.class.getName());
			Bundle args = new Bundle();
			args.putInt("pos", mCurrentPos);
			args.putBoolean("teams", mTeams);
			args.putString("linkName", getIntent().getExtras().getString("linkName"));
			args.putString("url", mURL);
			args.putBoolean("stats", getIntent().getExtras().getBoolean("stats"));
			args.putString("sportName", getIntent().getExtras().getString("sportName"));
			frag.setArguments(args);
			ft.add(android.R.id.content, frag, "Browser");
		}
		ft.commit();
		
		if (!mTeams) {
			TypedArray tabs = getResources().obtainTypedArray(R.array.TabBars);
			Tab tab;
			for (int i = 0; i < tabs.length(); i++) {
				String tabInfo[] = getResources().getStringArray(tabs.getResourceId(i, 0));
				tab = mActionBar.newTab().setText(tabInfo[1]);
				tab.setTabListener(new BrowserTabListener<MainActivity>(i));
				mActionBar.addTab(tab);
			}
			tabs.recycle();
		} else {
			showLinks = getIntent().getExtras().getBoolean("showLinks");
			showSchedule = getIntent().getExtras().getBoolean("showSchedule");
			showRoster = getIntent().getExtras().getBoolean("showRoster");
			showCoach = getIntent().getExtras().getBoolean("showCoach");
			TypedArray tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
			Tab tab;
			for (int i = 0; i < tabs.length(); i++) {
				String tabInfo[] = getResources().getStringArray(tabs.getResourceId(i, 0));
				tab = mActionBar.newTab().setText(tabInfo[1]);
				
				if (tabInfo[0].equalsIgnoreCase("teamwise_news")) {
					tab.setTabListener(new BrowserTabListener<TeamsActivity>(i));
					mActionBar.addTab(tab);
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_schedule") && showSchedule) {
					tab.setTabListener(new BrowserTabListener<TeamsActivity>(i));
					mActionBar.addTab(tab);
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_roster") && showRoster) {
					tab.setTabListener(new BrowserTabListener<TeamsActivity>(i));
					mActionBar.addTab(tab);
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_coaches") && showCoach) {
					tab.setTabListener(new BrowserTabListener<TeamsActivity>(i));
					mActionBar.addTab(tab);
				} else if (tabInfo[0].equalsIgnoreCase("teamwise_links") && showLinks) {
					tab.setTabListener(new BrowserTabListener<TeamsActivity>(i));
					mActionBar.addTab(tab);
				} else {
				}
			}
			tabs.recycle();
		}
		getSupportActionBar().selectTab(getSupportActionBar().getTabAt(mCurrentPos));
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		if (!TextUtils.isEmpty(getIntent().getExtras().getString("linkName"))) {
			getSupportActionBar().setTitle(getIntent().getExtras().getString("linkName"));
		} else {
			getSupportActionBar().setTitle("");
		}
		
		shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mURL);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.browser, menu);
		
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
		mShareActionProvider.setShareIntent(shareIntent);
		mRefresh = menu.findItem(R.id.action_refresh);
		mPrev = menu.findItem(R.id.action_prev);
		mNext = menu.findItem(R.id.action_next);
		mShare = menu.findItem(R.id.action_share);

		setRefreshActionButtonState(true);
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (itemId == R.id.action_share) {
			startActivity(Intent.createChooser(shareIntent, "Share via"));
			return true;
		} else if (itemId == R.id.action_refresh) {
			mWebView.reload();
			return true;
		} else if (itemId == R.id.action_browser) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
			startActivity(browserIntent);
			return true;
		} else if (itemId == R.id.action_prev) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}
			setForwardAndBackStates();
			return true;
		} else if (itemId == R.id.action_next) {
			if (mWebView.canGoForward()) {
				mWebView.goForward();
			}
			setForwardAndBackStates();
			return true;
		}
		return (super.onOptionsItemSelected(item));
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
	
	public void setForwardAndBackStates() {
		if (mNext != null) {
			if (mWebView.canGoForward()) {
				mNext.setIcon(R.drawable.ic_action_forward);
			} else {
				mNext.setIcon(R.drawable.ic_action_forward_disabled);
			}
		}
		
		if (mPrev != null) {
			if (mWebView.canGoBack()) {
				mPrev.setIcon(R.drawable.ic_action_back);
			} else {
				mPrev.setIcon(R.drawable.ic_action_back_disabled);
			}
		}
	}
	
	public void onSaveInstanceState(Bundle outState) {
		mWebView.saveState(outState);
		super.onSaveInstanceState(outState);
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
		
		if (mShare != null && mShare.isActionViewExpanded()) {
			mShare.collapseActionView();
		}
	}
	
	private class BrowserTabListener<T extends SherlockFragmentActivity> implements TabListener {
		private final int mPos;
		
		/** Constructor used each time a new tab is created. */
		public BrowserTabListener(int pos) {
			mPos = pos;
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ignoredFt) {
			if (mPos != mCurrentPos) {
				if (!mTeams) {
					MainActivity.closeBrowser(BrowserActivity.this, mPos);
				} else {
					TeamsActivity.closeBrowser(BrowserActivity.this, mPos);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ignoredFt) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ignoredFt) {
			if (!mTeams) {
				MainActivity.closeBrowser(BrowserActivity.this, mPos, false);
			} else {
				TeamsActivity.closeBrowser(BrowserActivity.this, mPos, false);
			}
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if (!mTeams) {
			MainActivity.closeBrowser(BrowserActivity.this, mCurrentPos);
		} else {
			TeamsActivity.closeBrowser(BrowserActivity.this, mCurrentPos);
		}
	}
}
