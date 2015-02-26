package com.svaad.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.svaad.R;
import com.svaad.adapter.HomeTabAdapter;
import com.svaad.utils.Constants;

public class HomeTabsFragment extends Fragment {

	private ActionBarActivity actionBarActivity;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private ActionBar actionBar;
	private HomeTabAdapter adapter;
	View view=null;
	private final String[] TITLES = { "Svaad", "Search", "Me" };
//	private final String[] TITLES = { "Svaad", "Search"};
	private static final int[] ICONS = new int[] {
			R.drawable.home_white, R.drawable.search_white_96,
			R.drawable.me_white_96 };

	FragmentTransaction fragmentTransaction;

	int mode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		actionBar = actionBarActivity.getSupportActionBar();
		view = inflater.inflate(R.layout.pagerslidingtabstrip_layout,
				container, false);

		init(view);

		adapter = new HomeTabAdapter(getFragmentManager(), TITLES, ICONS);

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		pager.setOffscreenPageLimit(5);

		tabs.setViewPager(pager);

		tabs.setBackgroundColor(getResources().getColor(R.color.tabbackground));
		tabs.setIndicatorHeight(10);

		tabs.setTextColor(getResources().getColor(R.color.tabtext));
		tabs.setShouldExpand(true);
		tabs.setIndicatorColor(getResources().getColor(R.color.tabindicator));
		if (this.getArguments() != null) {
			mode = this.getArguments().getInt(Constants.PAGER);

			if (mode == 1) {
				mode = 0;
				pager.setCurrentItem(mode);
			} else {
				pager.setCurrentItem(mode);
			}
		}
		return view;

	}

	public void init(View view) {
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager = (ViewPager) view.findViewById(R.id.pager);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.actionBarActivity = (ActionBarActivity) activity;

	}

}
