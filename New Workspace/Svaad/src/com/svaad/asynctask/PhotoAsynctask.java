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
import com.svaad.Dto.PhotoDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.SvaadPointDto;
import com.svaad.Dto.UserIdDto;
import com.svaad.Dto.UserIdInQueryDto;
import com.svaad.fragment.Svaad_Fragment_Spinner;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.FeedRequestDto;
import com.svaad.requestDto.PhotoRequestDto;
import com.svaad.requestDto.SvaadNearbyRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.Utils;
import com.svaad.whereDto.FeedWhereDto;
import com.svaad.whereDto.PhotoWhereDto;
import com.svaad.whereDto.SvaadWhereDto;

public class PhotoAsynctask extends AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private Fragment fragment;
	private String userId;

	public PhotoAsynctask(Context context, Fragment fragment, String userId) {
		this.context = context;
		this.fragment = fragment;
		this.userId = userId;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {

			if (fragment != null) {
				((SvaadProgressCallback) fragment).progressOn();
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

			feedJson = Api.toJson(getPhoRequestDto());

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

		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private PhotoRequestDto getPhoRequestDto() {
		PhotoRequestDto photoRequestDto = new PhotoRequestDto();

		PhotoWhereDto photoWhereDto = new PhotoWhereDto();

		UserIdDto userIdDto = new UserIdDto();
		userIdDto.set__type("Pointer");
		userIdDto.setClassName("_User");
		userIdDto.setObjectId(userId);

		FromRestaurantDto fromRestaurantDto = new FromRestaurantDto();
		fromRestaurantDto.set$exists(false);

		PhotoDto photoDto = new PhotoDto();
		photoDto.set$exists(true);

		photoWhereDto.setDishPhoto(photoDto);
		photoWhereDto.setFromRestaurant(fromRestaurantDto);
		photoWhereDto.setUserId(userIdDto);

		photoRequestDto.set_method("GET");
		photoRequestDto
				.setInclude("branchDishId,branchDishId.dishId,branchDishId.location");
		photoRequestDto.setKeys("dishPhoto,dishPhotoThumbnail,branchDishId");
		photoRequestDto.setLimit(1000);
		photoRequestDto.setOrder("-createdAt");
		photoRequestDto.setWhere(photoWhereDto);
		return photoRequestDto;
	}

}
