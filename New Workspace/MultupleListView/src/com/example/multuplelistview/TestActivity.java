package com.example.multuplelistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.SubscriptSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity{
	
	Button btn;
	
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		btn = (Button)findViewById(R.id.btn1);
		String str ="32";
		SpannableStringBuilder cs = new SpannableStringBuilder("$"+str);
		cs.setSpan(new SubscriptSpan(), 1, str.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	  //  SubscriptSpan.
		btn.setText(cs);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
             Intent intent = new Intent(TestActivity.this,SecondTestActivity.class);
             startActivity(intent);
         
             overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		});
	}

}
