package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.Dto.UserIdDto;
import com.svaad.activity.UserProfileActivity;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.FeedRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;
import com.svaad.whereDto.FeedWhereDto;

public class UserProfileAsynctask extends
		AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private Fragment fragment;
	private String userId;

	public UserProfileAsynctask(Context context, Fragment fragment,
			String userId) {
		this.context = context;
		this.fragment = fragment;
		this.userId = userId;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {

//			if (context != null) {
//
//				((BaseActivity) context).progressOn();
//			} else if (fragment != null) {
//				((BaseActivity) fragment.getActivity()).progressOn();
//			}
//			if (fragment != null) {
//				((SvaadProgressCallback) fragment).progressOn();
//			}
			
			if (fragment != null) {
				((SvaadProgressCallback) fragment).progressOn();
			}
			else if(context!=null && context instanceof UserProfileActivity)
			{
				((SvaadProgressCallback) context).progressOn();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}

	@Override
	protected FeedResponseDto doInBackground(Void... params) {

		String feedJson = null;
		try {

			feedJson = Api.toJson(getFeed());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String feedResponse = null;

		feedResponse = Utils.makePostRequest(Constants.FEED_URL, feedJson);

		FeedResponseDto feedResponseDto = null;
		try {

			if (feedResponse != null) {
				feedResponseDto = (FeedResponseDto) Api.fromJson(feedResponse,
						FeedResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return feedResponseDto;

	}

	@Override
	protected void onPostExecute(FeedResponseDto result) {
		super.onPostExecute(result);

		
		if (fragment != null) {
			((SvaadProgressCallback) fragment).progressOff();
		}
		else if(context!=null && context instanceof UserProfileActivity)
		{
			((SvaadProgressCallback) context).progressOff();
		}
		
//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}

		// if(fragment!=null)
		// {
		// ((FeedFragment)fragment).setContentShown(true);
		// }
		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private FeedRequestDto getFeed() {
		FeedRequestDto feedRequestDto = new FeedRequestDto();
		FeedWhereDto feedWhereDto = new FeedWhereDto();

		FromRestaurantDto fromRestaurantDto = new FromRestaurantDto();
		fromRestaurantDto.set$exists(false);

		feedWhereDto.setFromRestaurant(fromRestaurantDto);

		CityDto cityDto = new CityDto();
		cityDto.set__type("Pointer");
		cityDto.setClassName("City");
		cityDto.setObjectId("0KTrU3LFiO");
		feedWhereDto.setCity(cityDto);

		UserIdDto userIdDto = new UserIdDto();

		userIdDto.set__type("Pointer");
		userIdDto.setClassName("_User");
//		userIdDto.setObjectId("wiUjDiJ9qx");
		userIdDto.setObjectId(userId);

		feedWhereDto.setUserId(userIdDto);

		feedRequestDto
				.setInclude("userId,branchDishId,branchDishId.location,branchDishId.dishId");
		feedRequestDto.setKeys("branchDishId,userId,commentText,dishPhoto,dishTag");
		feedRequestDto.setLimit(1000);
		if (context != null && context instanceof UserProfileActivity)
			 feedRequestDto.setSkip(((UserProfileActivity) context).skip);

		else
			feedRequestDto.setSkip(0);
		feedRequestDto.setOrder("-createdAt");
		feedRequestDto.set_method("GET");
		feedRequestDto.setWhere(feedWhereDto);

		return feedRequestDto;
	}
}
