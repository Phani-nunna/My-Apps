package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.BaseActivity;
import com.svaad.activity.PullToviewMenu_Activity;
import com.svaad.fragment.PullToviewMenu_Fragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.RestaurantAllDishesRequestDto;
import com.svaad.responseDto.RestaurantAllDishesResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class RestaurantAllDishesAsynctask extends
		AsyncTask<Void, Void, RestaurantAllDishesResponseDto> {

	private Context context;

	private Fragment fragment;
	private RestaurantAllDishesRequestDto dishesRequestDto;
//	private ProgressDialog progressDialog;

	public RestaurantAllDishesAsynctask(Context context, Fragment fragment,
			RestaurantAllDishesRequestDto dishesRequestDto) {
		this.context = context;
		this.fragment = fragment;
		this.dishesRequestDto = dishesRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
//		progressDialog = ProgressDialog.show(context, "", "", true);
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflater.inflate(R.layout.progressbar, null, false);
//		progressDialog.setContentView(v);
//		progressDialog.setCancelable(true);
//		progressDialog.setCanceledOnTouchOutside(true);
		
		
		try {

			if (context != null) {

				((BaseActivity) context).progressOn();
			} else if (fragment != null) {
				((BaseActivity) fragment.getActivity()).progressOn();
			}
			
//			if(context!=null && context instanceof PullToviewMenu_Activity)
//			{
//				((PullToviewMenu_Activity) context).progressOn();
//			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	protected RestaurantAllDishesResponseDto doInBackground(Void... params) {
		String jsonString = null;
		try {
			jsonString = Api.toJson(dishesRequestDto);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String response = Utils.makePostRequest(Constants.BRANCH_MENU_URL,
				jsonString);
		RestaurantAllDishesResponseDto restaurantResponseDto = null;
		try {
			if (response != null) {
				restaurantResponseDto = (RestaurantAllDishesResponseDto) Api
						.fromJson(response,
								RestaurantAllDishesResponseDto.class);
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
	protected void onPostExecute(RestaurantAllDishesResponseDto result) {
		super.onPostExecute(result);
		
		try{
		if (context != null) {

			((BaseActivity) context).progressOff();
		}
			
//			if(context!=null && context instanceof PullToviewMenu_Activity)
//			{
//				((PullToviewMenu_Activity) context).progressOff();
//			}
		if (result != null) {
//			Toast.makeText(context, "Success Restaurant dishes",
//					Toast.LENGTH_LONG).show();

			if (fragment != null && fragment instanceof PullToviewMenu_Fragment) {
				((SvaadFeedCallback) fragment).setResponse(result);
			}
			else if(context!=null && context instanceof PullToviewMenu_Activity)
			{
				((SvaadFeedCallback) context).setResponse(result);
			}
		} else {
			Toast.makeText(context, "No data", Toast.LENGTH_LONG).show();
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
