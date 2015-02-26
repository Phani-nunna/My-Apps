package com.svaad.asynctask;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.HomeRequestDto;
import com.svaad.responseDto.HomeResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class HomeAsynctask extends AsyncTask<Void, Void, HomeResponseDto> {

	private Context context;
	private Fragment fragment;
	private ProgressDialog progressDialog;

	public HomeAsynctask(Context context, Fragment fragment) {
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
			
			if (fragment != null) {
				((SvaadProgressCallback) fragment).progressOn();
			}

			// progressDialog = ProgressDialog.show(context, "", "", true);
			// LayoutInflater inflater = (LayoutInflater) context
			// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// View v = inflater.inflate(R.layout.progressbar, null, false);
			// progressDialog.setContentView(v);
			// progressDialog.setCancelable(false);
			// progressDialog.setOnCancelListener(new OnCancelListener() {
			// public void onCancel(DialogInterface dialog) {
			// ((Activity) context).finish();
			// }
			// });
			//
		} catch (Exception e) {
			e.printStackTrace();

		}
		// if(fragment!=null)
		// {
		// ((FeedFragment)fragment).setContentShown(false);
		// }
	}

	@Override
	protected HomeResponseDto doInBackground(Void... params) {

		String feedJson = null;
		try {

			feedJson = Api.toJson(getHome());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String feedResponse = null;

		feedResponse = Utils.makePostRequest(Constants.HOME_URL, feedJson);

		HomeResponseDto feedResponseDto = null;
		try {

			if (feedResponse != null) {
				feedResponseDto = (HomeResponseDto) Api.fromJson(feedResponse,
						HomeResponseDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return feedResponseDto;

	}

	@Override
	protected void onPostExecute(HomeResponseDto result) {
		super.onPostExecute(result);

//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}
		
		if (fragment != null) {
			((SvaadProgressCallback) fragment).progressOff();
		}

		// progressDialog.dismiss();
		// if(fragment!=null)
		// {
		// ((FeedFragment)fragment).setContentShown(true);
		// }
		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private HomeRequestDto getHome() {
		HomeRequestDto homeRequestDto = new HomeRequestDto();
		homeRequestDto.setOrder("-updatedAt");
		homeRequestDto.set_method("GET");
		homeRequestDto.setLimit(1000);

		return homeRequestDto;
	}
}
