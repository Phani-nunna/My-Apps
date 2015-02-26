package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.activity.Ateit1_activity;
import com.svaad.fragment.Search_Fragment;
import com.svaad.fragment.Search_Res_Fragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.CamRestaurentRequestDto;
import com.svaad.responseDto.CamResSearchResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class CamSearchRestaurantAsynctask extends
		AsyncTask<Void, Void, CamResSearchResponseDto> {

	private Context context;
	private Fragment fragment;
	private CamRestaurentRequestDto restaurentRequestDto;

	public CamSearchRestaurantAsynctask(Context context, Fragment fragment,
			CamRestaurentRequestDto restaurentRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.restaurentRequestDto = restaurentRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (fragment != null && fragment instanceof Search_Res_Fragment) {
			((Search_Res_Fragment) fragment).progressOn();
		}
		else if (fragment != null && fragment instanceof Search_Fragment) {
			((Search_Fragment) fragment).progressOn();
		}

	}

	@Override
	protected CamResSearchResponseDto doInBackground(Void... params) {

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
		CamResSearchResponseDto restaurantResponseDto = null;
		try {
			if (response != null) {
				restaurantResponseDto = (CamResSearchResponseDto) Api.fromJson(
						response, CamResSearchResponseDto.class);
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
	protected void onPostExecute(CamResSearchResponseDto result) {
		super.onPostExecute(result);

		// if (context != null) {
		//
		// ((BaseActivity) context).progressOff();
		// }

		// if (context != null && context instanceof Ateit1_activity) {
		//
		// ((Search_Fragment) fragment).progressOff();
		// }

		if (fragment != null && fragment instanceof Search_Res_Fragment) {
			((Search_Res_Fragment) fragment).progressOff();
		}
		else if (fragment != null && fragment instanceof Search_Fragment) {
			((Search_Fragment) fragment).progressOff();
		}
		try {

			if (fragment != null && fragment instanceof Search_Res_Fragment) {
				((SvaadFeedCallback) fragment).setResponse(result);
			}
			else if (fragment != null && fragment instanceof Search_Fragment) {
				((SvaadFeedCallback) fragment).setResponse(result);
			} 
			else if (context instanceof Ateit1_activity) {
				((SvaadFeedCallback) context).setResponse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
