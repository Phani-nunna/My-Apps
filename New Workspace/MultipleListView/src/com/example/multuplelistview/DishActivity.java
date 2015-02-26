package com.example.multuplelistview;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class DishActivity extends FragmentActivity {
	ViewPager dishPager;
	//DishPagerAdapter dishPagerAdapter;
	private int position;
	private String[] items;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.dish_pager_screen);
		dishPager = (ViewPager) findViewById(R.id.dish_pager);
		position = getIntent().getIntExtra("Position", 0);
		items    = getIntent().getStringArrayExtra("Items");
		//dishPagerAdapter = new DishPagerAdapter(this,items,position);

	}
}
