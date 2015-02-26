package com.paradigmcreatives.audit;

import android.app.ActivityGroup;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class TopGroupActivity extends ActivityGroup {
	private ViewGroup subActivity;
	private LocalActivityManager localActivityManager;
	private TextView userName;
	private TextView title;
	private ListView options_list;
	TextView logout;
	String[] items = { "Sync All", "Switch User" };
	private static TopGroupActivity mInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mInstance = this;
		userName = (TextView) findViewById(R.id.name);
		title = (TextView) findViewById(R.id.title);
		logout = (TextView) findViewById(R.id.button1);
		options_list = (ListView) findViewById(R.id.options_list);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// options_list.setAdapter(new
				// ArrayAdapter<String>(TopGroupActivity.this,android.R.layout.simple_list_item_1,items));

				logoutDialog();

			}
		});
		// String username = getIntent().getStringExtra("username");
		userName.setText("User : Auditor");
		title.setText("Auditing");
		localActivityManager = getLocalActivityManager();
		Intent intent = new Intent(TopGroupActivity.this,
				AuditorScheduleListActivity.class);
		// Intent intent = new
		// Intent(TopGroupActivity.this,AuditorScheduleListActivity.class);
		launchActivity(intent);

	}

	public void launchActivity(Intent intent) {
		if (subActivity != null)
			subActivity.removeAllViews();
		subActivity = ((LinearLayout) findViewById(R.id.subActivities));
		startActivityInViewGroup(localActivityManager, subActivity, intent);
	}

	public void startActivityInViewGroup(LocalActivityManager localActivityManager, ViewGroup subActivity,Intent intent) {
		if (subActivity == null) {
			return;
		}
		String id = intent.getClass() != null ? intent.getClass().getName()
				: intent.getPackage() + String.valueOf(Math.random()) + 1; // just
																			// non-zero
																			// value

		Window w = localActivityManager.startActivity(id, intent);
		View wd = w != null ? w.getDecorView() : null;
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				LayoutParams.FILL_PARENT);
		if (wd != null) {
			subActivity.addView(wd, lp);
		}
	}

	public static TopGroupActivity getInstance() {
		if (mInstance == null) {
			mInstance = new TopGroupActivity();
		}

		return mInstance;
	}

	private void logoutDialog() {

		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View sync_dialog = inflater.inflate(R.layout.sync_dialog,
				(ViewGroup) findViewById(R.id.sync_dialog_box));
		final Dialog dialog = new Dialog(this);
		TextView tittle = (TextView) sync_dialog.findViewById(R.id.sync_title);
		tittle.setText("Do you want to logout?");
		Button no_button = (Button) sync_dialog
				.findViewById(R.id.no_button);
		Button yes_button = (Button) sync_dialog.findViewById(R.id.yes_button);

		yes_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TopGroupActivity.this,
						AuditLoginActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		no_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(sync_dialog);
		/*
		 * WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		 * 
		 * lp.copyFrom(dialog.getWindow().getAttributes()); lp.width = 200;
		 * lp.height = 200; lp.x=450; lp.y=-150;
		 * dialog.getWindow().setAttributes(lp);
		 */
		dialog.show();
	}
}
