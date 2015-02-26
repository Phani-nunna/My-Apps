package com.svaad.asynctask;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.R;
import com.svaad.Dto.DishProfileResDto;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class DishPRofileResAsynctask extends
		AsyncTask<Void, Void, RestaurantListResponseDto> {

	private Context context;
	private Fragment fragment;
	private DishProfileResDto restaurentRequestDto;
	private Dialog progressDialog;

	public DishPRofileResAsynctask(Context context, Fragment fragment,
			DishProfileResDto restaurentRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.restaurentRequestDto = restaurentRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();


		
		progressDialog = ProgressDialog.show(context, "", "", true);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.progressbar, null, false);
		progressDialog.setContentView(v);
		progressDialog.setCancelable(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				((Activity) context).finish();
			}
		});


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
		progressDialog.dismiss();

		
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
