package com.svaad.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.SvaadApplication;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.Dto.InQueryWhereDto;
import com.svaad.Dto.ObjectIdDTO;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.SvaadPointDto;
import com.svaad.Dto.UserIdDto;
import com.svaad.Dto.UserIdInQueryDto;
import com.svaad.fragment.Svaad_Fragment_Spinner;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.FeedRequestDto;
import com.svaad.requestDto.SvaadNearbyRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.Utils;
import com.svaad.whereDto.FeedWhereDto;
import com.svaad.whereDto.SvaadWhereDto;

public class SvaadAsynctask extends AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private Fragment fragment;
	String check, btnText;
	double latitude, longitude;
	boolean scroll;

	public SvaadAsynctask(Context context, Fragment fragment, String check,
			String btnText,boolean scroll) {
		this.context = context;
		this.fragment = fragment;
		this.check = check;
		this.btnText = btnText;
		this.scroll=scroll;

	}

	public SvaadAsynctask(Context context, Fragment fragment, String check,
			String btnText, double latitude, double longitude,boolean scroll) {
		this.context = context;
		this.fragment = fragment;
		this.check = check;
		this.btnText = btnText;
		this.latitude = latitude;
		this.longitude = longitude;
		this.scroll=scroll;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {

			// if (context != null) {
			//
			// ((BaseActivity) context).progressOn();
			// } else if (fragment != null) {
			// ((BaseActivity) fragment.getActivity()).progressOn();
			// }
			if(scroll==false)
			{
			

			if (fragment != null) {
				((SvaadProgressCallback) fragment).progressOn();
			}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		// if(fragment!=null)
		// {
		// ((FeedFragment)fragment).setContentShown(false);
		// }
	}

	@Override
	protected FeedResponseDto doInBackground(Void... params) {

		String feedJson = null;
		try {

			if (check != null && check.length() > 0 && btnText != null
					&& btnText.length() > 0) {
				if (check.equalsIgnoreCase("Near by")
						&& btnText.equalsIgnoreCase("Everyone")) {
					feedJson = Api.toJson(getSvaadNearByDto());
				} else if (check.equalsIgnoreCase("All Locations")
						&& btnText.equalsIgnoreCase("Everyone")) {
					feedJson = Api.toJson(getSvaadEveryoneAlllocations());
				} else if (check.equalsIgnoreCase("Near by")
						&& btnText.equalsIgnoreCase("Feed")) {
					feedJson = Api.toJson(getFeed());

				} else if (check.equalsIgnoreCase("All Locations")
						&& btnText.equalsIgnoreCase("Feed")) {
					feedJson = Api.toJson(getFeedAllLocations());

				}
			}

			// feedJson = Api.toJson(getFeed());

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

		// if (context != null) {
		//
		// ((BaseActivity) context).progressOff();
		// }

		if(scroll==false)
		{
		
		if (fragment != null) {

			((SvaadProgressCallback) fragment).progressOff();
		}
		}

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

	private SvaadNearbyRequestDto getSvaadNearByDto() {
		SvaadNearbyRequestDto svaadNearbyRequestDto = new SvaadNearbyRequestDto();

		SvaadWhereDto svaadWhereDto = new SvaadWhereDto();

		FromRestaurantDto fromRestaurantDto = new FromRestaurantDto();
		fromRestaurantDto.set$exists(false);

		SvaadPointDto svaadPointDto = new SvaadPointDto();
//		svaadPointDto.set$maxDistanceInKilometers(2.5);

		PointDto point = new PointDto();
		point.setLatitude(latitude);
		point.setLongitude(longitude);
		point.set__type("GeoPoint");

		svaadPointDto.set$nearSphere(point);

		svaadWhereDto.setFromRestaurant(fromRestaurantDto);
		svaadWhereDto.setPoint(svaadPointDto);

		svaadNearbyRequestDto
				.setInclude("userId,branchDishId,branchDishId.location,branchDishId.dishId");
		svaadNearbyRequestDto
				.setKeys("branchDishId,userId,commentText,dishPhoto,dishTag");
		svaadNearbyRequestDto.setLimit(100);
//		if (fragment != null && fragment instanceof Svaad_Fragments)
//			svaadNearbyRequestDto.setSkip(((Svaad_Fragments) fragment).skip);
//		else
//			svaadNearbyRequestDto.setSkip(0);
		svaadNearbyRequestDto.setOrder("-createdAt");
		svaadNearbyRequestDto.set_method("GET");
		svaadNearbyRequestDto.setWhere(svaadWhereDto);

		return svaadNearbyRequestDto;
	}

	private FeedRequestDto getSvaadEveryoneAlllocations() {
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

		feedRequestDto
				.setInclude("userId,branchDishId,branchDishId.location,branchDishId.dishId");
		feedRequestDto
				.setKeys("branchDishId,userId,commentText,dishPhoto,dishTag");
		feedRequestDto.setLimit(15);
		if (fragment != null && fragment instanceof Svaad_Fragment_Spinner)
			feedRequestDto.setSkip(((Svaad_Fragment_Spinner) fragment).skip);
		else
			feedRequestDto.setSkip(0);
		feedRequestDto.setOrder("-createdAt");
		feedRequestDto.set_method("GET");
		feedRequestDto.setWhere(feedWhereDto);

		return feedRequestDto;
	}

	private FeedRequestDto getFeedAllLocations() {
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

		UserIdInQueryDto userIdInQueryDto = new UserIdInQueryDto();
		userIdInQueryDto.setClassName("_User");

		InQueryWhereDto inQueryWhereDto = new InQueryWhereDto();

		ObjectIdDTO objectIdDTO = new ObjectIdDTO();

		List<String> followingUsers = null;

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());

		try {
			followingUsers = (List<String>) ObjectSerializer
					.deserialize(sharedPreferences.getString(
							Constants.FOLLOWING_USERS,
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (followingUsers != null && followingUsers.size() > 0) {
			objectIdDTO.set$in(followingUsers);
		}

		inQueryWhereDto.setObjectId(objectIdDTO);

		userIdInQueryDto.setWhere(inQueryWhereDto);

		userIdDto.set$inQuery(userIdInQueryDto);

		feedWhereDto.setUserId(userIdDto);

		feedRequestDto
				.setInclude("userId,branchDishId,branchDishId.location,branchDishId.dishId");
		feedRequestDto
				.setKeys("branchDishId,userId,commentText,dishPhoto,dishTag");
		feedRequestDto.setLimit(15);
		if (fragment != null && fragment instanceof Svaad_Fragment_Spinner)
			feedRequestDto.setSkip(((Svaad_Fragment_Spinner) fragment).skip);
		else
			feedRequestDto.setSkip(0);
		feedRequestDto.setOrder("-createdAt");
		feedRequestDto.set_method("GET");
		feedRequestDto.setWhere(feedWhereDto);

		return feedRequestDto;
	}

	private FeedRequestDto getFeed() {
		FeedRequestDto feedRequestDto = new FeedRequestDto();
		FeedWhereDto feedWhereDto = new FeedWhereDto();

		FromRestaurantDto fromRestaurantDto = new FromRestaurantDto();
		fromRestaurantDto.set$exists(false);

		feedWhereDto.setFromRestaurant(fromRestaurantDto);

		SvaadPointDto svaadPointDto = new SvaadPointDto();
//		svaadPointDto.set$maxDistanceInKilometers(2.5);

		PointDto point = new PointDto();
		point.setLatitude(latitude);
		point.setLongitude(longitude);
		point.set__type("GeoPoint");

		svaadPointDto.set$nearSphere(point);

		feedWhereDto.setPoint(svaadPointDto);

		CityDto cityDto = new CityDto();
		cityDto.set__type("Pointer");
		cityDto.setClassName("City");
		cityDto.setObjectId("0KTrU3LFiO");
		feedWhereDto.setCity(cityDto);

		UserIdDto userIdDto = new UserIdDto();

		UserIdInQueryDto userIdInQueryDto = new UserIdInQueryDto();
		userIdInQueryDto.setClassName("_User");

		InQueryWhereDto inQueryWhereDto = new InQueryWhereDto();

		ObjectIdDTO objectIdDTO = new ObjectIdDTO();

		List<String> followingUsers = null;

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());

		try {
			followingUsers = (List<String>) ObjectSerializer
					.deserialize(sharedPreferences.getString(
							Constants.FOLLOWING_USERS,
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (followingUsers != null && followingUsers.size() > 0) {
			objectIdDTO.set$in(followingUsers);
		}

		inQueryWhereDto.setObjectId(objectIdDTO);

		userIdInQueryDto.setWhere(inQueryWhereDto);

		userIdDto.set$inQuery(userIdInQueryDto);

		feedWhereDto.setUserId(userIdDto);

		feedRequestDto
				.setInclude("userId,branchDishId,branchDishId.location,branchDishId.dishId");
		feedRequestDto
				.setKeys("branchDishId,userId,commentText,dishPhoto,dishTag");
		feedRequestDto.setLimit(100);
	
//		if (fragment != null && fragment instanceof Svaad_Fragments)
//			feedRequestDto.setSkip(((Svaad_Fragments) fragment).skip);
//		else
//			feedRequestDto.setSkip(0);
		feedRequestDto.setOrder("-createdAt");
		feedRequestDto.set_method("GET");
		feedRequestDto.setWhere(feedWhereDto);

		return feedRequestDto;
	}
}
