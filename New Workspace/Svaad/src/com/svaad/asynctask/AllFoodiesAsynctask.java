package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.Dto.FoodiesUserRoleDto;
import com.svaad.fragment.AllFoodiesFragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.AllFoodiesRequestDto;
import com.svaad.responseDto.AllFoodiesResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;
import com.svaad.whereDto.AllFoodiesWhereDto;

public class AllFoodiesAsynctask extends
		AsyncTask<Void, Void, AllFoodiesResponseDto> {

	private Context context;
	private Fragment fragment;


	public AllFoodiesAsynctask(Context context, Fragment fragment) {
		this.context = context;
		this.fragment = fragment;

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
			
			if(fragment!=null && fragment instanceof AllFoodiesFragment)
			{
				((AllFoodiesFragment)fragment).progressOn();
			}



		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	protected AllFoodiesResponseDto doInBackground(Void... params) {

		String allFoodiesJson = null;
		try {

			allFoodiesJson = Api.toJson(getFoodies());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String foodiesResponse = null;

		foodiesResponse = Utils.makePostRequest(Constants.ALL_FOODIES_URL,
				allFoodiesJson);

		AllFoodiesResponseDto allFoodiesResponseDto = null;
		try {

			if (foodiesResponse != null) {
				allFoodiesResponseDto = (AllFoodiesResponseDto) Api.fromJson(
						foodiesResponse, AllFoodiesResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allFoodiesResponseDto;

	}

	@Override
	protected void onPostExecute(AllFoodiesResponseDto result) {
		super.onPostExecute(result);

//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}
		
		
		if(fragment!=null && fragment instanceof AllFoodiesFragment)
		{
			((AllFoodiesFragment)fragment).progressOff();
		}
		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private AllFoodiesRequestDto getFoodies() {
		AllFoodiesRequestDto allFoodiesRequestDto = new AllFoodiesRequestDto();
		AllFoodiesWhereDto allFoodiesWhereDto = new AllFoodiesWhereDto();

		FoodiesUserRoleDto foodiesUserRole = new FoodiesUserRoleDto();
		foodiesUserRole.set$exists(false);

		allFoodiesWhereDto.setUserRole(foodiesUserRole);
		allFoodiesRequestDto.setLimit(50);
		allFoodiesRequestDto.setSkip(((AllFoodiesFragment) fragment).skip);
		allFoodiesRequestDto.set_method("GET");
		allFoodiesRequestDto.setOrder("-suggestCount");
		allFoodiesRequestDto.setWhere(allFoodiesWhereDto);

		return allFoodiesRequestDto;
	}

}
