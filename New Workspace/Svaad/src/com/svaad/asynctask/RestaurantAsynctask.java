package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.activity.NearByRestaurantsActivity;
import com.svaad.fragment.SearchRestaurantsFragemnt;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.RestaurentRequestDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class RestaurantAsynctask extends
		AsyncTask<Void, Void, RestaurantListResponseDto> {

	private Context context;
	private Fragment fragment;
	private RestaurentRequestDto restaurentRequestDto;

	public RestaurantAsynctask(Context context, Fragment fragment,
			RestaurentRequestDto restaurentRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.restaurentRequestDto = restaurentRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		 if (context != null) {
//		
//		 ((BaseActivity) context).progressOn();
//		 } 
//		 else if (fragment != null) {
		// ((BaseActivity) fragment.getActivity()).progressOn();
		// }
		
		if (context != null && context instanceof HomeSearchActivity) {

			((SearchRestaurantsFragemnt) fragment).progressOn();
		}
		else if (context != null && context instanceof NearByRestaurantsActivity) {

			((NearByRestaurantsActivity) context).progressOn();
		}


		else if (fragment != null && fragment instanceof SearchRestaurantsFragemnt) {
			((SearchRestaurantsFragemnt) fragment).progressOn();
		}

	}

	@Override
	protected RestaurantListResponseDto doInBackground(Void... params) {

		String jsonString = null;
		try {
			jsonString = Api.toJson(restaurentRequestDto);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String response = Utils.makePostRequest(
				Constants.POPULAR_RESTAURANT_URL, jsonString);
		RestaurantListResponseDto restaurantResponseDto = null;
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

//		 if (context != null) {
//		
//		 ((BaseActivity) context).progressOff();
//		 }
		
		if (context != null && context instanceof HomeSearchActivity) {

			((SearchRestaurantsFragemnt) fragment).progressOff();
		}
		else if (context != null && context instanceof NearByRestaurantsActivity) {

			((NearByRestaurantsActivity) context).progressOff();
		}

		else if (fragment != null && fragment instanceof SearchRestaurantsFragemnt) {
			((SearchRestaurantsFragemnt) fragment).progressOff();
		}
		try {

			if (fragment != null && fragment instanceof SvaadFeedCallback) {
				((SvaadFeedCallback) fragment).setResponse(result);
			} else if (context instanceof SvaadFeedCallback) {
				((SvaadFeedCallback) context).setResponse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
