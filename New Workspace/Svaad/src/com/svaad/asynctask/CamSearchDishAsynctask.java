package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.activity.Ateit1_activity;
import com.svaad.activity.SearchDish_Activity;
import com.svaad.fragment.Search_Fragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.CamDishRequestDto;
import com.svaad.requestDto.CamRestaurentRequestDto;
import com.svaad.responseDto.CamDishesResponseDto;
import com.svaad.responseDto.CamResSearchResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class CamSearchDishAsynctask extends
		AsyncTask<Void, Void, CamDishesResponseDto> {

	private Context context;
	private Fragment fragment;
	private CamDishRequestDto restaurentRequestDto;

	public CamSearchDishAsynctask(Context context, Fragment fragment,
			CamDishRequestDto restaurentRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.restaurentRequestDto = restaurentRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (context != null && context instanceof SearchDish_Activity) {
			((SearchDish_Activity) context).progressOn();
		}

	}

	@Override
	protected CamDishesResponseDto doInBackground(Void... params) {

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

		String response = Utils.makePostRequest(Constants.BRANCH_MENU_URL,
				jsonString);
		CamDishesResponseDto restaurantResponseDto = null;
		try {
			if (response != null) {
				restaurantResponseDto = (CamDishesResponseDto) Api.fromJson(
						response, CamDishesResponseDto.class);
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
	protected void onPostExecute(CamDishesResponseDto result) {
		super.onPostExecute(result);

		if (context != null && context instanceof SearchDish_Activity) {
			((SearchDish_Activity) context).progressOff();
		}
		try {

			if (context != null && context instanceof SearchDish_Activity) {
				((SvaadFeedCallback) context).setResponse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
