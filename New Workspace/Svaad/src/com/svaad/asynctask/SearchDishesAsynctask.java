package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.activity.SearchOverviewActivity;
import com.svaad.fragment.SearchDishesGridFragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Utils;
import com.svaad.whereDto.DishWhereDto;

public class SearchDishesAsynctask extends
		AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private String url;
	private DishWhereDto dishWhereRequestDto;
	private Fragment fragment;
	private boolean scroll;

	// private ProgressDialog progressDialog;

	public SearchDishesAsynctask(Context context,
			Fragment fragment, String url, DishWhereDto dishWhereRequestDto,boolean scroll) {
		this.context = context;
		this.url = url;
		this.fragment = fragment;
		this.dishWhereRequestDto = dishWhereRequestDto;
		this.scroll=scroll;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	

//		if (context != null && context instanceof BaseActivity) {
//
//			((BaseActivity) context).progressOn();
//		}
//		else if(context != null && context instanceof BaseActivity)
//		{
//			((BaseActivity) context).progressOn();
//		}
		
		if(scroll==false)
		{
		
		
		if (context != null && context instanceof HomeSearchActivity) {

//			((SearchDishesFragemnt) fragment).progressOn();
			
			((SearchDishesGridFragment) fragment).progressOn();
		}
		else if(context != null && context instanceof SearchOverviewActivity)
		{
			((SearchOverviewActivity) context).progressOn();
		}
		
//		else if (fragment != null && fragment instanceof SearchDishesFragemnt) {
//
//			((SearchDishesFragemnt) fragment).progressOn();
//		}
		else if (fragment != null && fragment instanceof SearchDishesGridFragment) {

			((SearchDishesGridFragment) fragment).progressOn();
		}
		
		}

	}

	@Override
	protected FeedResponseDto doInBackground(Void... params) {
		String jsonString = null;
		try {
			jsonString = Api.toJson(dishWhereRequestDto);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String response = Utils.makePostRequest(url, jsonString);
		FeedResponseDto dishResponseDto = null;
		try {
			if (response != null) {
				dishResponseDto = (FeedResponseDto) Api
						.fromJson(response,
								FeedResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dishResponseDto;
	}

	@Override
	protected void onPostExecute(FeedResponseDto result) {
		super.onPostExecute(result);
		try {

//			if (context != null && context instanceof BaseActivity) {
//
//				((BaseActivity) context).progressOff();
//			}
//			else if(context != null && context instanceof BaseActivity)
//			{
//				((BaseActivity) context).progressOff();
//			}

			
			if(scroll==false)
			{
			
			if (context != null && context instanceof HomeSearchActivity) {

//				((SearchDishesFragemnt) fragment).progressOff();
				((SearchDishesGridFragment) fragment).progressOff();
			}
			else if(context != null && context instanceof SearchOverviewActivity)
			{
				((SearchOverviewActivity) context).progressOff();
			}
//			else if (fragment != null && fragment instanceof SearchDishesFragemnt) {
//
//				((SearchDishesFragemnt) fragment).progressOff();
//			}
			else if (fragment != null && fragment instanceof SearchDishesGridFragment) {

				((SearchDishesGridFragment) fragment).progressOff();
			}
			}
		
			// if(result==null)
			// {
			// return;
			// }

			if (fragment != null && fragment instanceof SvaadFeedCallback) {
				((SvaadFeedCallback) fragment).setResponse(result);
			} else if (context != null
					&& context instanceof SvaadFeedCallback) {
				((SvaadFeedCallback) context).setResponse(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
