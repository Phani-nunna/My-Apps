package com.svaad;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
//		getActionBar().setLogo(R.drawable.home_icon);
		setSupportProgressBarIndeterminateVisibility(false);
	}

	public void progressOn() {
		setSupportProgressBarIndeterminateVisibility(true);
		
		
	}

	public void progressOff() {
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	
	
	public void progressOnTest() {
		setSupportProgressBarIndeterminateVisibility(true);
		
		
	}

	public void progressOffTest() {
		setSupportProgressBarIndeterminateVisibility(false);
	}
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		
//		new SvaadDialogs().showToast(BaseActivity.this, "Base Activity back");
//	}
	

}
