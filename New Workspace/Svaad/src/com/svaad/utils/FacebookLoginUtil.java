package com.svaad.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.svaad.asynctask.LogInAsyncTask;
import com.svaad.requestDto.LogInRequestDto;
import com.svaad.requestDto.LogInRequestDto.AuthData;
import com.svaad.requestDto.LogInRequestDto.AuthData.FBData;

public class FacebookLoginUtil {

	private static Session openActiveSession(Activity activity,
			boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {

		Session.OpenRequest openRequest = new Session.OpenRequest(activity)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(activity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}
		return null;

	}

	public static Session loginToFacebook(final Activity activity,
			final String source,final int mode,final String mixpanelscreen,final String action) {
		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		permissions.add("user_birthday");
		Session session= openActiveSession(activity, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(final Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {

					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {

										String userId = user.getId();
										
										
										
										String userName = user.getUsername();
										
										
										
										String birthday = user.getBirthday();
										String fbName = user.getFirstName()
												+ " " + user.getLastName();
									//	Utils.saveToSharedPreference("FacebookUserName", fbName);
										
										String picture = "https://graph.facebook.com/"
												+ userId + "/picture";
										
									//	Utils.saveToSharedPreference("FacebookPicture", picture);
										
										String email = user
												.getProperty("email")
												.toString();
										String accessToken = session
												.getAccessToken();
										
									//	Utils.saveToSharedPreference("fbaccesstoken", accessToken);
										
										String expirationDate = session
												.getExpirationDate().toString();

										Calendar dateObj = DateUtil
												.getCalFromString(
														"EEE MMM dd HH:mm:ss zzz yyyy",
														expirationDate);

										String date = DateUtil
												.getStringFromCal(
														"yyyy-MM-dd'T'HH:mm:ss.SSS",
														dateObj)
												+ "Z";

										LogInRequestDto loginDto = new LogInRequestDto();
										AuthData authData = new AuthData();
										FBData fb = new FBData();
										fb.setId(userId);
										fb.setAccess_token(accessToken);
										fb.setExpiration_date(date);
										authData.setFacebook(fb);

										loginDto.setAuthData(authData);
										loginDto.setFbId(userId);
										loginDto.setBirthday(birthday);
										loginDto.setEmail(email);
										loginDto.setUsername(email);
										loginDto.setUname(fbName);
										loginDto.setDisplayPicUrl(picture);
										if(source!=null && !source.equalsIgnoreCase("Invite Friends"))
										{
										new LogInAsyncTask(activity, loginDto,
												source,mode,mixpanelscreen,action).execute();
										}
										else
										{
											inviteFriends(activity, session);
										}
										/*
										 * System.out.println(userId);
										 * System.out.println(userName);
										 * System.out.println(birthday);
										 * System.out.println(picture);
										 * System.out.println(email);
										 * System.out.println(accessToken);
										 * System.out.println(expirationDate);
										 */

									}
								}

							});
				}
			}

		}, permissions);
		return session;

	}

//	public static void sendRequestDialog(final Activity activity) {
//
//		Session session = Session.getActiveSession();
//		if(session!=null && session.isOpened())
//			inviteFriends(activity, session);
//		else
//			loginToFacebook(activity, "Invite Friends");
//	}
	
	
	public static void inviteFriends(final Activity activity,Session session)
	{
		Bundle params = new Bundle();
		params.putString("message",
				"Welcome to Svaad");
		WebDialog requestsDialog = new WebDialog.RequestsDialogBuilder(
				activity, session, params)
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						
						
						try {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(activity, "Request sent",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(activity, "Request cancelled",
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).build();

		requestsDialog.show();
	}
	
	
}
