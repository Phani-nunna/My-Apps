package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.PopularReataurantRequestDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class PopularRestaurantAsynctask extends
		AsyncTask<Void, Void, RestaurantListResponseDto> {

	private Context context;
	private Fragment fragment;
	private PopularReataurantRequestDto restaurentRequestDto;

	public PopularRestaurantAsynctask(Context context, Fragment fragment,
			PopularReataurantRequestDto restaurentRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.restaurentRequestDto = restaurentRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		if (context != null) {
//
//			((BaseActivity) context).progressOn();
//		} else if (fragment != null) {
//			((BaseActivity) fragment.getActivity()).progressOn();
//		}
		
		
		if(context!=null)
		{
			((SvaadProgressCallback)context).progressOn();
		}

	}

	@Override
	protected RestaurantListResponseDto doInBackground(Void... params) {

		String jsonString = null;
		RestaurantListResponseDto restaurantResponseDto = null;
		String response = null;

		try {

			jsonString = Api.toJson(restaurentRequestDto);
			if (jsonString != null) {
				response = Utils.makePostRequest(
						Constants.POPULAR_RESTAURANT_URL, jsonString);
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if (response != null) {
				restaurantResponseDto = (RestaurantListResponseDto) Api
						.fromJson(response, RestaurantListResponseDto.class);
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return restaurantResponseDto;

	}

	@Override
	protected void onPostExecute(RestaurantListResponseDto result) {
		super.onPostExecute(result);

//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}
		

		if (context != null) {

			((SvaadProgressCallback) context).progressOff();
		}

		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

}
