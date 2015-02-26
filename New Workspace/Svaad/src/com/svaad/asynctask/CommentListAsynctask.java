package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.BaseActivity;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.DishCommentsRequestDto;
import com.svaad.responseDto.DishCommentsResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class CommentListAsynctask extends
		AsyncTask<Void, Void, DishCommentsResponseDto> {

	private Context context;
	private Fragment fragment;
	
	private DishCommentsRequestDto dishCommentsRequestDto;

	public CommentListAsynctask(Context context, Fragment fragment,DishCommentsRequestDto dishCommentsRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.dishCommentsRequestDto=dishCommentsRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (context != null) {

			((BaseActivity) context).progressOn();
		} else if (fragment != null) {
			((BaseActivity) fragment.getActivity()).progressOn();
		}

	}

	@Override
	protected DishCommentsResponseDto doInBackground(Void... params) {
		String jsonString = null;
		try {
			jsonString = Api.toJson(dishCommentsRequestDto);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		String response = Utils.makePostRequest(Constants.DISH_COMMENTS_URL,
//				jsonString);
		String response = Utils.makePostRequest(Constants.FEED_URL,
				jsonString);
		DishCommentsResponseDto dishCommentsResponseDto = null;
		try {
			if (response != null) {
				dishCommentsResponseDto = (DishCommentsResponseDto) Api
						.fromJson(response, DishCommentsResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dishCommentsResponseDto;
	}

	@Override
	protected void onPostExecute(DishCommentsResponseDto result) {
		super.onPostExecute(result);

		if (context != null) {

			((BaseActivity) context).progressOff();
		}

		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}
	}

}
