package com.svaad.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.adapter.UserProfileTabsMenuAdapter;
import com.svaad.fragment.WishesCountFragment;
import com.svaad.utils.Constants;

public class UserProfileTabs_Activity extends BaseActivity {

	ViewPager mViewPager;

	List<String> customMenusList;

	UserProfileTabsMenuAdapter adapter;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	String ate,catId;

	int position=0;
	
	String titles[]={"Wishlist"};
	
	FragmentTransaction fragmentTransaction;

	private FeedDetailDto feedDetailDto;

	private WishesCountFragment wishesCountFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

//		setContentView(R.layout.pagerslidingtabstrip_layout);
		
		setContentView(R.layout.pull_activity_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setProgressBarIndeterminateVisibility(false);
		getSupportActionBar().setTitle("Wishlist");

//		init();
		
		
		if(this.getIntent().getExtras()!=null)
		{
			position=this.getIntent().getExtras().getInt(Constants.PAGER);
			feedDetailDto = (FeedDetailDto) getIntent().getExtras()
					.getSerializable(Constants.DATA);
			
		}
		
		
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		wishesCountFragment = new WishesCountFragment();
		Bundle args = new Bundle();
		args.putSerializable(Constants.DATA, (Serializable) feedDetailDto);
		
		wishesCountFragment.setArguments(args);;
		fragmentTransaction.replace(R.id.pullLinearLayout, wishesCountFragment);
		fragmentTransaction.commit();
		
//		adapter = new UserProfileTabsMenuAdapter(getSupportFragmentManager(),titles,feedDetailDto);
//
//		pager.setAdapter(adapter);
//
//		final int pageMargin = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
//						.getDisplayMetrics());
//		pager.setPageMargin(pageMargin);
//		
//		pager.setCurrentItem(position);
//		
//
//		tabs.setViewPager(pager);
//
////		tabs.setBackgroundColor(Color.parseColor("#ffffff"));
//
//		tabs.setShouldExpand(true);
////		tabs.setTextSize(30);
//		tabs.setIndicatorHeight(10);
//
////		tabs.setIndicatorColor(Color.parseColor("#1faaff"));
//		tabs.setBackgroundColor(getResources().getColor(R.color.tabbackground));
//
//		tabs.setTextColor(getResources().getColor(R.color.tabtext));
//
//		tabs.setIndicatorColor(getResources().getColor(R.color.tabindicator));

	}

//	public void init() {
//
//		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//		pager = (ViewPager) findViewById(R.id.pager);
//	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.viewer_menu, menu);
//
//		return super.onCreateOptionsMenu(menu);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}
}
