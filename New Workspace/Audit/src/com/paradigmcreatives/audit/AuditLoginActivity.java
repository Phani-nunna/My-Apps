package com.paradigmcreatives.audit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paradigmcreatives.audit.database.AuditDataBase;
import com.paradigmcreatives.audit.util.Constants;
import com.paradigmcreatives.broadcast.PageExpireReceiver;

public class AuditLoginActivity extends Activity implements OnClickListener {
	private Button submitButton;
	private EditText input_pwd;
	private EditText input_userName;
	private AuditDataBase mDataBase;
	public static int alertTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audit_login);
		mDataBase = new AuditDataBase(AuditLoginActivity.this);
		SplashScreenActivity.getInstance().getProgressDialog().dismiss();
		submitButton = (Button) findViewById(R.id.submit_btn);
		input_pwd = (EditText) findViewById(R.id.input_pwd);
		input_userName = (EditText) findViewById(R.id.input_userName);
		input_userName.setText(" ");
		submitButton.setOnClickListener(this);

	}

	public void onClick(View v) {

		if (v.getId() == R.id.submit_btn) {

			String userName = input_userName.getText().toString().trim();
			String pwd = input_pwd.getText().toString().trim();
			if (userName.length() <= 0) {
				if (pwd.length() <= 0) {
					Toast.makeText(AuditLoginActivity.this,
							"Enter your username and password",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AuditLoginActivity.this,
							"Enter your username", Toast.LENGTH_SHORT).show();
				}
			} else {
				if (pwd.length() <= 0) {
					Toast.makeText(AuditLoginActivity.this,
							"Enter your password", Toast.LENGTH_SHORT).show();
				} else {
					boolean check;
					mDataBase.openToRead();
					check = mDataBase.getLoginAvailability(userName, pwd);

					if (check) {
						alertTime = 60;
						mDataBase.close();
						Intent expire_intent = new Intent(this,
								PageExpireReceiver.class);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(this.getApplicationContext(),
										234324243, expire_intent, 0);
						AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
						alarmManager
								.set(AlarmManager.RTC_WAKEUP,
										System.currentTimeMillis()
												+ (alertTime * 1000),
										pendingIntent);
						Toast.makeText(this,
								"Alarm set in " + alertTime + " seconds",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(AuditLoginActivity.this,
								TopGroupActivity.class);
						startActivityForResult(intent, Constants.LOGIN_SUCCESS);
					} else {
						mDataBase.close();
						Toast.makeText(this, "InValid Credentials",
								Toast.LENGTH_LONG).show();
					}
				}
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.LOGIN_SUCCESS) {
			input_userName.setText("");
			input_pwd.setText("");
			input_userName.requestFocus();
		}
	}
}