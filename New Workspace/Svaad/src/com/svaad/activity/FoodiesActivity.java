package com.svaad.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.fragment.AllFoodiesFragment;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class FoodiesActivity extends BaseActivity {
	
	String loginUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		final ActionBar actionBar = getActionBar();
		setSupportProgressBarIndeterminateVisibility(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		actionBar.setDisplayUseLogoEnabled(true);

		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Foodies");
		loginUserId=Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.homeLinearLayout,
				new AllFoodiesFragment());
		fragmentTransaction.commit();

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.restaurant_menu, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

//			if(loginUserId!=null && loginUserId.length()>0)
//			{
//				
//				finish();
//			}
//			else
//			{
				
				
//				Intent i=new Intent(FoodiesActivity.this,HomeActivity.class);;
//				startActivity(i);
//				finish();
//			}
//			finish();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
//		if(loginUserId!=null && loginUserId.length()>0)
//		{
//			
//			
//			super.onBackPressed();
//		}
//		else
//		{
//			Intent i=new Intent(FoodiesActivity.this,HomeActivity.class);;
//			startActivity(i);
//			finish();
//		}
	}

}
