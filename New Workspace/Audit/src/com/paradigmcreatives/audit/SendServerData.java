package com.paradigmcreatives.audit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

import com.paradigmcreatives.audit.database.AuditQuestions;

import android.content.Context;
import android.os.AsyncTask;

public class SendServerData extends AsyncTask<JSONObject, Void, Boolean>{
private Context mContex;
	public SendServerData(AuditQuestions data){
		mContex= data;
	}

	@Override
	protected Boolean doInBackground(JSONObject... params) {
		try {
			URL url = new URL("");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "applicatio/json");
			connection.setRequestProperty("accept", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(params[0].toString());
			int reponseCode = connection.getResponseCode();
			if(reponseCode == 200){
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String str ;
				while((str = reader.readLine())!= null){
					sb.append(str);
				}
				return true;
			}else{
				return false;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result){
			System.out.println("working fine");
		}else{
			System.out.println("failed");
		}
	}

}
