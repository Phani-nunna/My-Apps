package com.svaad.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MyMixpanel {

	private MixpanelAPI mMixpanel;

	public void SvaadMixpanelLogin(Context context, String objectId,
			String firstname, String email, String screen, String action,
			String through) {

		mMixpanel = MixpanelAPI.getInstance(context,
				Constants.MIXPANEL_API_TOKEN);

		mMixpanel.identify(objectId);
		mMixpanel.getPeople().identify(objectId);
		final MixpanelAPI.People people = mMixpanel.getPeople();
		people.setOnce("$first_name", firstname);
		people.setOnce("$email", email);
		people.setOnce(Constants.LOGIN_OBJECTID, objectId);
		people.setOnce(Constants.EMAIL, email);
		people.setOnce(Constants.NAME, firstname);

		JSONObject props = new JSONObject();
		try {
			props.put("Through", through);

			props.put(Constants.SCREEN, screen);
			props.put(Constants.ACTION, action);
			mMixpanel.track("Logging", props);
			people.setOnce("Signup date", Utils.getDateTimeStamp());

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void SvaadMixpanelWishIt(Context context, String userId,
			String username, String dishName, String branchName,
			String branchDishId, String screen,String action) {
		
		mMixpanel = MixpanelAPI.getInstance(context,
				Constants.MIXPANEL_API_TOKEN);

		mMixpanel.identify(userId);
		mMixpanel.getPeople().identify(userId);
		
		JSONObject props = new JSONObject();
		try {
			

			props.put(Constants.SCREEN, screen);
			props.put(Constants.ACTION, action);
			if(userId!=null)
				props.put("User Id", userId);
			if(username!=null)
				props.put("Username", username);
			if(dishName!=null)
				props.put("Dishname", dishName);
			if(branchName!=null)
				props.put("Branchname", branchName);
			if(branchDishId!=null)
				props.put("BranchDishId", branchDishId);
			mMixpanel.track(action, props);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	
	public void SvaadMixpanelAteIt(Context context, String userId,
			String username, String dishName, String branchName,
			String branchDishId, String screen,String action,String picture,String share) {
		
		mMixpanel = MixpanelAPI.getInstance(context,
				Constants.MIXPANEL_API_TOKEN);

		mMixpanel.identify(userId);
		mMixpanel.getPeople().identify(userId);
		
		JSONObject props = new JSONObject();
		try {
			

			props.put(Constants.SCREEN, screen);
			props.put(Constants.ACTION, action);
			if(userId!=null)
				props.put("User Id", userId);
			if(username!=null)
				props.put("Username", username);
			if(dishName!=null)
				props.put("Dishname", dishName);
			if(branchName!=null)
				props.put("Branchname", branchName);
			if(branchDishId!=null)
				props.put("BranchDishId", branchDishId);
			if(picture!=null)
				props.put("Picture", picture);
			if(share!=null)
				props.put("Share", share);
			mMixpanel.track(action, props);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	public void setMixpanelFlush(Context context) {
		mMixpanel = MixpanelAPI.getInstance(context,
				Constants.MIXPANEL_API_TOKEN);
		mMixpanel.flush();
	}

}
