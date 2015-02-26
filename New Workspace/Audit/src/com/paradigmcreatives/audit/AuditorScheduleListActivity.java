package com.paradigmcreatives.audit;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.paradigmcreatives.audit.beans.Auditor_Schedules;
import com.paradigmcreatives.audit.camera.TakeImage;
import com.paradigmcreatives.audit.database.AuditDataBase;
import com.paradigmcreatives.audit.util.Utility;

public class AuditorScheduleListActivity extends Activity {
	private boolean showSyncOpt;
	private ListView audit_config_list;
	private AuditorAdapter adapter;
	private AuditDataBase mDatabase;
	private ArrayList<Auditor_Schedules> schedules;
	private boolean isNetwork;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audit_list);
		if (SplashScreenActivity.getInstance().getProgressDialog() != null) {
			SplashScreenActivity.getInstance().getProgressDialog().dismiss();
		}
		audit_config_list = (ListView) findViewById(R.id.list);
		mDatabase = new AuditDataBase(AuditorScheduleListActivity.this);
		mDatabase.openToRead(); // Database opened
		schedules = new ArrayList<Auditor_Schedules>();
		schedules = mDatabase.getAuditorSchedules();
		if (schedules != null && schedules.size() > 0) {
			adapter = new AuditorAdapter(this, schedules);
			audit_config_list.setAdapter(adapter);
		}
		mDatabase.close();
		audit_config_list.setCacheColorHint(Color.TRANSPARENT); // set
																// Transparent
																// color to
																// List
		audit_config_list.setFadingEdgeLength(1);

		/**
		 * These is onItemClickListener to audit_config_list When user clicks on
		 * Schedule items ,then only it navigates to other screen
		 */
		audit_config_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
					long arg3) {
				System.out.println("schedules length"+schedules.size());
				System.out.println("status value"+schedules.get(positon).getStatus());
				System.out.println("status value"+schedules.get(positon).getState());
				System.out.println("status value"+schedules.get(positon).getAuditor_Emp_ID());
				if (!schedules.get(positon).getStatus().equals("completed")) {
					String processName = schedules.get(positon).getProcess();		
					System.out.println("proces name in listact="+processName);
					Intent intent = new Intent(
							AuditorScheduleListActivity.this, TakeImage.class);
					intent.putExtra("process_name", processName);
					TopGroupActivity.getInstance().launchActivity(intent);
				}

			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!showSyncOpt) {
			int size = menu.size();
			if (size == 0)
				menu.add("Sync"); // create Sync option menu
		
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		syncDialog(); // call sync dialog
		return true;
	}

	/***
	 * These dialog is used to alert a user to sync data to web.
	 */
	private void syncDialog () {

		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View sync_dialog = inflater.inflate(R.layout.sync_dialog, (ViewGroup) findViewById(R.id.sync_dialog_box));
		final Dialog dialog = new Dialog(this);
		final Button no_button = (Button) sync_dialog.findViewById(R.id.no_button);
		Button yes_button = (Button) sync_dialog.findViewById(R.id.yes_button);

		yes_button.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {

				isNetwork = Utility.getInstance().isOnline(AuditorScheduleListActivity.this);
				if (!isNetwork) { // checked Internet connection

					// send data to web.
					Toast.makeText(AuditorScheduleListActivity.this, "synced...", Toast.LENGTH_LONG).show();
					showSyncOpt = true;
				}
				else {
					Toast.makeText(AuditorScheduleListActivity.this, "No Network Connection ,Please try again later", Toast.LENGTH_SHORT).show();
				}

				dialog.dismiss();
			}
		});
		no_button.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {
				dialog.dismiss();
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(sync_dialog);
		dialog.show();
	}

}
