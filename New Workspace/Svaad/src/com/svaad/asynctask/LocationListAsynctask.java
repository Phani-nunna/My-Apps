package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.activity.LocationsActivity;
import com.svaad.fragment.NewLocationFragemnt;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.LocationRequestDto;
import com.svaad.responseDto.LocationResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;
import com.svaad.whereDto.LocationWhereDto;

public class LocationListAsynctask extends
		AsyncTask<Void, Void, LocationResponseDto> {

	private Context context;
	private Fragment fragment;

	public LocationListAsynctask(Context context, Fragment fragment) {
		this.context = context;
		this.fragment = fragment;

	}

	private LocationRequestDto getlocationRequestDto() {
		LocationRequestDto locationRequestDto = new LocationRequestDto();
		LocationWhereDto locationWhereDto = new LocationWhereDto();
		locationWhereDto.setHasRestaurant(true);
		locationRequestDto.setWhere(locationWhereDto);
		locationRequestDto.set_method("GET");
		locationRequestDto.setKeys("name");
		locationRequestDto.setLimit(1000);
		locationRequestDto.setOrder("name");
		return locationRequestDto;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		try {

			// if (context != null) {
			//
			// ((BaseActivity) context).progressOn();
			// } else if (fragment != null) {
			// ((BaseActivity) fragment.getActivity()).progressOn();
			// }

			if (context != null && context instanceof LocationsActivity) {
				((SvaadProgressCallback) context).progressOn();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	protected LocationResponseDto doInBackground(Void... params) {
		String jsonString = null;
		try {
			jsonString = Api.toJson(getlocationRequestDto());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String response = Utils.makePostRequest(Constants.ALL_LOCATIONS_URL,
				jsonString);
		LocationResponseDto locationResponseDto = null;
		try {
			if (response != null) {
				locationResponseDto = (LocationResponseDto) Api.fromJson(
						response, LocationResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return locationResponseDto;
	}

	@Override
	protected void onPostExecute(LocationResponseDto result) {
		super.onPostExecute(result);

		// if (context != null) {
		//
		// ((BaseActivity) context).progressOff();
		// }
		if (result == null) {
			// Toast.makeText(context, "No location found", Toast.LENGTH_LONG)
			// .show();
			return;
		}


		if (context != null && context instanceof LocationsActivity) {
			((SvaadProgressCallback) context).progressOff();
		}

		 if (result != null) {
			// SvaadApplication.getInstance().setLocationDtos(result.getResults());
			if (fragment != null && fragment instanceof NewLocationFragemnt) {
				((SvaadFeedCallback) fragment).setResponse(result);
			}
			else if(context != null && context instanceof LocationsActivity)
			{
				((SvaadFeedCallback) context).setResponse(result);
			}
		}

		

	}
}
