package com.paradigmcreatives.audit;

import android.app.Activity;
import android.os.Bundle;

public class NoInternet  extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internet_details);		
	}

	@Override
	public void onBackPressed() {		
		super.onBackPressed();
		finish();
	}

	
	
}
