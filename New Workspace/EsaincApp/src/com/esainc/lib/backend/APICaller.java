package com.esainc.lib.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class APICaller {
	
	public APICaller() {
	}
	
	public String loadDataFromUrl(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream is = null;
		String data = null;
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line;
			if ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			data = sb.toString();
		} catch (UnsupportedEncodingException e) {
			//Logger.e("Parsing Error", "Error getting data", e);
			data = "Error:loading: Failed to load data";
		} catch (ClientProtocolException e) {
			//Logger.e("Parsing Error", "Error getting data", e);
			data = "Error:loading: Failed to load data";
		} catch (IOException e) {
			//Logger.e("Parsing Error", "Error getting data", e);
			data = "Error:loading: Failed to load data";
		}
		
		return data;
	}
}
