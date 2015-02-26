package com.svaad.asynctask;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.activity.HomeActivity;
import com.svaad.requestDto.LogInRequestDto;
import com.svaad.responseDto.LogInResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.MyMixpanel;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class LogInAsyncTask extends AsyncTask<Void, Void, LogInResponseDto> {

	private Context context;
	private LogInRequestDto logInRequestDto;
	private String screenType;
	private ProgressDialog progressDialog;
	private int mode;
	private String mixpanelScreen,action;

	public LogInAsyncTask(Context context, LogInRequestDto logInRequestDto,
			String screenType,int mode,String mixpanelScreen,String action) {
		this.context = context;
		this.logInRequestDto = logInRequestDto;
		this.screenType = screenType;
		this.mode=mode;
		this.mixpanelScreen=mixpanelScreen;
		this.action=action;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		if (context != null) {
//
//			((BaseActivity) context).progressOn();
//		}
		
		progressDialog = ProgressDialog.show(context, "", "", true);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.progressbar, null, false);
		progressDialog.setContentView(v);
		progressDialog.setCancelable(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				((Activity) context).finish();
			}
		});

	}

	@Override
	protected LogInResponseDto doInBackground(Void... params) {

		String jsonString = null;
		try {
			jsonString = Api.toJson(logInRequestDto);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String url = null;
		String response = null;
		if (screenType.equalsIgnoreCase(Constants.SIGN_UP_SCREEN))
				
		{
//			url = Constants.SIGN_UP_URL;
//			response = Utils.makePostRequest(url, jsonString);
//			response = Utils.makeGetRequest(Constants.LOG_IN_URL + "?username="
//					+ logInRequestDto.getUsername() + "&password="
//					+ logInRequestDto.getPassword());

			url = Constants.SIGN_UP_URL;
			response = Utils.makePostRequest(url, jsonString);
			
			if(response!=null && response.length()>0)
			{
				url = Constants.LOG_IN_URL;
				response = Utils.makeGetRequest(url + "?username="
						+ logInRequestDto.getUsername() + "&password="
						+ logInRequestDto.getPassword());

				Utils.saveToSharedPreference(Constants.SVAADLOGIN_RESPONSE, response);
			}


			
		

		} 
		
		else if(screenType.equalsIgnoreCase(Constants.LOG_IN_FB_SCREEN)
				|| screenType.equalsIgnoreCase(Constants.SIGN_UP_FB_SCREEN))
		{
			url = Constants.SIGN_UP_URL;
			response = Utils.makePostRequest(url, jsonString);
			Utils.saveToSharedPreference(Constants.SVAADLOGIN_RESPONSE, response);
		}
			
		else if (screenType.equalsIgnoreCase(Constants.LOG_IN_SCREEN)) 
		{
			url = Constants.LOG_IN_URL;
			response = Utils.makeGetRequest(url + "?username="
					+ logInRequestDto.getUsername() + "&password="
					+ logInRequestDto.getPassword());

			Utils.saveToSharedPreference(Constants.SVAADLOGIN_RESPONSE,
					response);

		}

		LogInResponseDto logInResponseDto = null;
		try {
			if (response != null) {
				logInResponseDto = (LogInResponseDto) Api.fromJson(response,
						LogInResponseDto.class);
				if (logInResponseDto != null
						&& logInResponseDto.getObjectId() != null) {
					SvaadApplication.getInstance().setLoginUserInfo(
							logInResponseDto);

					List<String> followinusers = logInResponseDto
							.getFollowingUsers();
//					List<String> followersArray = logInResponseDto
//							.getFollowers();

					List<String> wishlistarray = logInResponseDto
							.getWishlistArr();

					if (wishlistarray != null && wishlistarray.size() > 0) {
						Utils.saveToSharedPreferenceList(Constants.WISHLIST_ARRAY,
								wishlistarray);
					}

					if (followinusers != null && followinusers.size() > 0) {

						Utils.saveToSharedPreferenceList(Constants.FOLLOWING_USERS,
								followinusers);
					}
					
					if(logInResponseDto.getUname()!=null && logInResponseDto.getUname().length()>0)
					{

					Utils.saveToSharedPreference(Constants.USER_NAME_MIXPANEL, logInResponseDto.getUname());
					}
//					if (followersArray != null && followersArray.size() > 0) {
//
//						Utils.saveToSharedPreference(Constants.FOLLOWERS_ARRAY,
//								followersArray);
//					}
					Utils.saveToSharedPreference(Constants.USER_OBJECT_ID_KEY,
							logInResponseDto.getObjectId());

			
					
					
				
				
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logInResponseDto;

	}

	@Override
	protected void onPostExecute(LogInResponseDto result) {
		super.onPostExecute(result);

//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}
		progressDialog.dismiss();

		
		if(result==null&&screenType.equalsIgnoreCase(Constants.LOG_IN_SCREEN))
		{
			new SvaadDialogs().showToast(context, "Invalid login parameters");
		}
		
		if (result != null && result.getObjectId() != null&& mode==Constants.RES_MODE) {
		new SvaadDialogs().showToast(context, "Login successful");
		
		
	
		
		try {
			
			if(screenType.equalsIgnoreCase(Constants.LOG_IN_SCREEN)
					||screenType.equalsIgnoreCase(Constants.SIGN_UP_SCREEN))
					
			{
			new MyMixpanel().SvaadMixpanelLogin(context,
					result.getObjectId(), result.getUname(),
					result.getUsername(), mixpanelScreen,action,"Svaad");
			}
			else if( screenType.equalsIgnoreCase(Constants.SIGN_UP_FB_SCREEN)
					|| screenType.equalsIgnoreCase(Constants.LOG_IN_FB_SCREEN))
			{
				new MyMixpanel().SvaadMixpanelLogin(context,
						result.getObjectId(), result.getUname(),
						result.getUsername(), mixpanelScreen,action,"Facebook");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

//		if(mode==Constants.PAGER_MODE_ME)
//		{
//			Intent intent = new Intent(context, MeActivity.class);
//			intent.putExtra(Constants.PAGER,mode);
//			context.startActivity(intent);
//			((Activity) context).finish();
//		}

		if (result != null && result.getObjectId() != null&& mode==Constants.PAGER_MODE_FEED||mode==Constants.PAGER_MODE_ME)
//		if (result != null && result.getObjectId() != null&& mode==Constants.PAGER_MODE_FEED)
		{
			if (screenType.equalsIgnoreCase(Constants.LOG_IN_SCREEN)
					|| screenType.equalsIgnoreCase(Constants.SIGN_UP_SCREEN)
					|| screenType.equalsIgnoreCase(Constants.SIGN_UP_FB_SCREEN)
					|| screenType.equalsIgnoreCase(Constants.LOG_IN_FB_SCREEN)) {
				
				
				
				Utils.saveToSharedPreference(Constants.USER_NAME_MIXPANEL, result.getUname());
				
								try {
									
									if(screenType.equalsIgnoreCase(Constants.LOG_IN_SCREEN)||screenType.equalsIgnoreCase(Constants.SIGN_UP_SCREEN))
									{
									new MyMixpanel().SvaadMixpanelLogin(context,
											result.getObjectId(), result.getUname(),
											result.getUsername(), mixpanelScreen,action,"Svaad");
									}
									else if( screenType.equalsIgnoreCase(Constants.SIGN_UP_FB_SCREEN)
											|| screenType.equalsIgnoreCase(Constants.LOG_IN_FB_SCREEN)) 
									{
										new MyMixpanel().SvaadMixpanelLogin(context,
												result.getObjectId(), result.getUname(),
												result.getUsername(), mixpanelScreen,action,"Facebook");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
				
				Intent intent = new Intent(context, HomeActivity.class);
//				intent.putExtra(Constants.PAGER, Constants.PAGER_MODE_FEED);
				intent.putExtra(Constants.PAGER,mode);
				context.startActivity(intent);
				((Activity) context).finish();
			} else {
				// new MyDialogs().showLoginDailog(context,
				// context.getResources()
				// .getString(R.string.signin_success));
				// ((Activity) context).finish();
			}
		} 
//		else if (result != null) {
//			String fail = context.getResources().getString(
//					R.string.request_failed);
//			Toast.makeText(
//					context,
//					result.getError() != null ? fail + ": " + result.getError()
//							: fail, Toast.LENGTH_LONG).show();
//		}

	}
}
