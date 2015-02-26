package com.example.multuplelistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondTestActivity extends Activity {
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		btn = (Button)findViewById(R.id.btn1);
		btn.setText("Button2");
		btn.setVisibility(View.GONE);
	}

}
