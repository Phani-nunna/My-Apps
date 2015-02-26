package com.paradigmcreatives.audit;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.paradigmcreatives.audit.database.AuditDatabases;
import com.paradigmcreatives.audit.database.KeyValues;

public class DownloadAsyncTasks extends AsyncTask<Void, Void, Boolean> {
	private ArrayList<KeyValues> keyValues;
	private ArrayList<KeyValues> config_values;
	private AuditDatabases db;
	private Context mContext;
	public DownloadAsyncTasks(Context context) {
		mContext = context;
		
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		keyValues = new ArrayList<KeyValues>();
		config_values = new ArrayList<KeyValues>();
		KeyValues values = new KeyValues();
		db = new AuditDatabases(mContext);
		db.openToWrite();
		values.setQuestion_name("All Operatos wear Uniform? ");
		values.setResponse_type("1");
		values.setResponse_count("2");
		values.setResponse_values("Yes,No");
		keyValues.add(values);
		values = new KeyValues();
		values.setQuestion_name("Are system of required configuration?");
		values.setResponse_type("2");
		values.setResponse_count("2");
		values.setResponse_values("Yes,No");
		keyValues.add(values);
		
		values = new KeyValues();

		values.setQuestion_name("How satisfied are you with the center?");
		values.setResponse_type("1");
		values.setResponse_count("4");
		values.setResponse_values(" Not happy, Unable to comment, Happy,Very happy");
		keyValues.add(values);
		
		values = new KeyValues();

		values.setQuestion_name("Common man able to see the banner from long distance?");
		values.setResponse_type("2");
		values.setResponse_count("2");
		values.setResponse_values("Yes,No");
		keyValues.add(values);
		values = new KeyValues();

		values.setQuestion_name("Facilities avilable in center?");
		values.setResponse_type("2");
		values.setResponse_count("4");
		values.setResponse_values("Water,Aaya,Wash Room,First Aid");
		keyValues.add(values);
		
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012venkateswara786");		
		values.setDate("25/05/2010");
		values.setLocation("hyderabad");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012phani123");		
		values.setDate("22/05/2011");
		values.setLocation("hyderabad");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012kranti286");		
		values.setDate("15/05/2010");
		values.setLocation("vishakapatnam");
		values.setInfo("Scheduled");
		values.setSync("no");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012venky123");		
		values.setDate("22/03/2010");
		values.setLocation("hyderabad");
		values.setInfo("Scheduled");
		values.setSync("no");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012harsha254");		
		values.setDate("15/05/2011");
		values.setLocation("Nellore");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012Rambabu582");		
		values.setDate("15/06/2012");
		values.setLocation("hyderabad");
		values.setInfo("scheduled");
		values.setSync("no");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012rajesh963");		
		values.setDate("25/02/2012");
		values.setLocation("hyderabad");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012venkat843");		
		values.setDate("22/02/2012");
		values.setLocation("khamam");
		values.setInfo("scheduled");
		values.setSync("no");
		config_values.add(values);
		values = new KeyValues();
		values.setSerial_number("AUD191800cUID04012012varun582");		
		values.setDate("18/05/2010");
		values.setLocation("gudivada");
		values.setInfo("scheduled");
		values.setSync("yes");
		config_values.add(values);
		values.setSerial_number("AUD191800cUID04012012varma456");		
		values.setDate("12/07/2012");
		values.setLocation("karimnagar");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values.setSerial_number("AUD191800cUID0401201rajani782");		
		values.setDate("15/10/2011");
		values.setLocation("hyderabad");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		values.setSerial_number("AUD191800cUID04012012harini783");		
		values.setDate("12/05/2011");
		values.setLocation("hyderabad");
		values.setInfo("completed");
		values.setSync("yes");
		config_values.add(values);
		db.questionsInsert(keyValues);
	    db.configurationInsert(config_values);
	
		if(db.retriveQuestionData()!=null && db.retriveConfiguarationData()!= null){
			db.close();
			return true;		

		}else{
			db.close();
			return false;
			
		}

		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		System.out.println("Result " + result);
		if(result){
		if (SplashScreenActivity.getInstance().mHandler != null) {
			SplashScreenActivity.getInstance().mHandler.sendEmptyMessage(1);
		}
	}
	}
}
