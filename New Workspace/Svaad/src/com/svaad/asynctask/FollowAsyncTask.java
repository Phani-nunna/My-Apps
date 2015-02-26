package com.svaad.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.BaseActivity;
import com.svaad.SvaadApplication;
import com.svaad.requestDto.FollowRequestDto;
import com.svaad.responseDto.FollowResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class FollowAsyncTask extends AsyncTask<Void, Void, FollowResponseDto> {

	Context context;
	View view;
	String loginuserObjetctId;
	String foodiesObjectId;
	private SharedPreferences sharedPreferences;
	private List<String> followingUsers;;

	// ProgressBar pbar;

	public FollowAsyncTask(Context context, View view,
			String loginuserObjetctId, String foodiesObjectId) {

		this.context = context;
		this.view = view;
		this.foodiesObjectId = foodiesObjectId;
		this.loginuserObjetctId = loginuserObjetctId;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		try {

			if (context != null) {

				((BaseActivity) context).progressOn();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected FollowResponseDto doInBackground(Void... params) {
		String followJson = null;
		try {

			followJson = Api.toJson(getFollow());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String followResponse = null;

		followResponse = Utils
				.makePostRequest(Constants.USER_FOLLW, followJson);

		FollowResponseDto followResponseDto = null;
		try {

			if (followResponse != null) {
				followResponseDto = (FollowResponseDto) Api.fromJson(
						followResponse, FollowResponseDto.class);
//				LogInResponseDto loginRes = SvaadApplication.getInstance()
//						.getLoginUserInfo();
//				loginRes.setFollowingUsersCount(followResponseDto.getResult()
//						.size());
//				loginRes.setFollowingUsers(followResponseDto.getResult());
//				String loginResP = Api.toJson(loginRes);
//				Utils.saveToSharedPreference(Constants.USER_OBJECT_INFO,
//						loginResP);
//				SvaadApplication.getInstance().setLoginUserInfo(loginRes);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return followResponseDto;
	}

	private FollowRequestDto getFollow() {
		FollowRequestDto requestDto = new FollowRequestDto();
		requestDto.setFollower(loginuserObjetctId);
		
		requestDto.setFollowingUser(foodiesObjectId);
		return requestDto;
	}

	@Override
	protected void onPostExecute(FollowResponseDto result) {
		super.onPostExecute(result);
		
		if (context != null) {

			((BaseActivity) context).progressOff();
		}

		
		if (result != null) {
			
			
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SvaadApplication.getInstance()
							.getApplicationContext());

			
			try {
				followingUsers = (List<String>) ObjectSerializer
						.deserialize(sharedPreferences.getString(
								Constants.FOLLOWING_USERS, ObjectSerializer
										.serialize(new ArrayList<String>())));
				
				if(followingUsers!=null && followingUsers.size()>0)
				{
					followingUsers.add(foodiesObjectId);
				}
				else
				{
					followingUsers=new ArrayList<String>();
					followingUsers.add(foodiesObjectId);
				}
				
				Utils.saveToSharedPreferenceList(Constants.FOLLOWING_USERS, followingUsers);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			new SvaadDialogs().showToast(context, "Successfully followed");

			
//			Button btnFolow = (Button) view.findViewById(R.id.btnFollow);
//			btnFolow.setText("Following");
//			btnFolow.setClickable(false);
//			btnFolow.setBackgroundColor(Color.parseColor("#4cae4c"));

		}

	}
}
