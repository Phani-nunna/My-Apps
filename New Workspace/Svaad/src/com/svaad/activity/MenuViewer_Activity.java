package com.svaad.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.adapter.ViewerMenuAdapter;
import com.svaad.utils.Constants;

public class MenuViewer_Activity extends Fragment {

	ViewPager mViewPager;

	List<String> customMenusList;

	ViewerMenuAdapter adapter;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	String ate, catId;

	int vposition = 0;
	List<FeedDetailDto> feedList;
	List<FeedDetailDto> MostlovedLists;
	FragmentTransaction fragmentTransaction;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.pagerslidingtabstrip_layout,
				container, false);

		// final ActionBar actionBar = getActivity().getActionBar();
		// ((ActionBarActivity) getActivity())
		// .setSupportProgressBarIndeterminateVisibility(false);
		//
		// actionBar.setDisplayHomeAsUpEnabled(true);

		init(view);

		Bundle bundleObject = this.getArguments();
		if (bundleObject != null) {

			// Get ArrayList Bundle
			feedList = (ArrayList<FeedDetailDto>) bundleObject
					.getSerializable(Constants.DATA);
			MostlovedLists = (ArrayList<FeedDetailDto>) bundleObject
					.getSerializable(Constants.DATA_mostloved);

			System.out.println(feedList.size());
			vposition = bundleObject.getInt("val");

		}

		if ((feedList != null && feedList.size() > 0)
				|| (MostlovedLists != null && MostlovedLists.size() > 0)) {
			adapter = new ViewerMenuAdapter(getFragmentManager(), vposition,
					feedList, MostlovedLists);
			pager.setAdapter(adapter);
		}

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pager.setCurrentItem(vposition);
		pager.setOffscreenPageLimit(4);
		tabs.setViewPager(pager);

		// tabs.setBackgroundColor(Color.parseColor("#ffffff"));

		tabs.setShouldExpand(true);
//		tabs.setTextSize(30);
		tabs.setIndicatorHeight(10);
		// tabs.setIndicatorColor(Color.parseColor("#1faaff"));

		tabs.setBackgroundColor(getResources().getColor(R.color.tabbackground));

		tabs.setTextColor(getResources().getColor(R.color.tabtext));

		tabs.setIndicatorColor(getResources().getColor(R.color.tabindicator));
		return view;
	}

	// @Override
	// protected void onCreate(Bundle arg0) {
	// super.onCreate(arg0);
	//
	// setContentView(R.layout.pagerslidingtabstrip_layout);
	// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	//
	// setProgressBarIndeterminateVisibility(false);
	//
	// init();
	//
	// Bundle bundleObject = getIntent().getExtras();
	// if (bundleObject != null) {
	//
	// // Get ArrayList Bundle
	// feedList = (ArrayList<FeedDetailDto>) bundleObject
	// .getSerializable(Constants.DATA);
	// System.out.println(feedList.size());
	// vposition = bundleObject.getInt("val");
	//
	// }
	//
	// adapter = new ViewerMenuAdapter(getSupportFragmentManager(), vposition,
	// feedList);
	//
	// pager.setAdapter(adapter);
	//
	// final int pageMargin = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
	// .getDisplayMetrics());
	// pager.setPageMargin(pageMargin);
	// pager.setCurrentItem(vposition);
	//
	// tabs.setViewPager(pager);
	//
	// tabs.setBackgroundColor(Color.parseColor("#ffffff"));
	//
	// tabs.setShouldExpand(true);
	//
	// tabs.setIndicatorColor(Color.parseColor("#1faaff"));
	//
	// }

	// public void init() {
	//
	// tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	// pager = (ViewPager) findViewById(R.id.pager);
	// }

	public void init(View view) {

		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager = (ViewPager) view.findViewById(R.id.pager);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.viewer_menu, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// getActivity().finish();
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

}
