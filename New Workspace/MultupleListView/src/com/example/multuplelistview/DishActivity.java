package com.example.multuplelistview;

import android.R.anim;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class DishActivity extends FragmentActivity {
	ViewPager dishPager;
	DishPagerAdapter dishPagerAdapter;
	private int position;
	private String[] items;
	private Drawable mActionBarBackgroundDrawable;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.dish_pager_screen);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Anime");
		actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setBackgroundDrawable(new ColorDrawable(R.color.actionbarColor));
		getActionBar().setBackgroundDrawable(new ColorDrawable(0xFF3F9FE0));
		//mActionBarBackgroundDrawable = getResources().getDrawable(R.color.actionbarColor);
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		// mActionBarBackgroundDrawable.setAlpha(1);
//	actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
		
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
		}*/
		dishPager = (ViewPager) findViewById(R.id.dish_pager);
		position = getIntent().getIntExtra("Position", 0);
		items = getIntent().getStringArrayExtra("Items");
		System.out.println("celkon dishactivity =" + position);
		dishPagerAdapter = new DishPagerAdapter(getSupportFragmentManager(),
				this, items, position);
		dishPager.setAdapter(dishPagerAdapter);
		dishPager.setCurrentItem(position);
		dishPager.setPageMargin(15);

	}
	 
	private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
}
