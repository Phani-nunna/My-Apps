package com.svaad.asynctask;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.EventJson;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.requestDto.LikeRequestDto;
import com.svaad.requestDto.NewLikeRequestDto;
import com.svaad.responseDto.CamPhotoResponseDto;
import com.svaad.responseDto.EventResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.MyMixpanel;
import com.svaad.utils.Utils;

public class LikeAsyncTask extends AsyncTask<Void, Void, CamPhotoResponseDto> {

	private Context context;
	private Fragment fragment;
	private FeedDetailDto dishDetailDto;
	private String screen;

	private ProgressDialog progressDialog;
	public LikeAsyncTask(Context context, Fragment fragment,
			FeedDetailDto dishDetailDto,String screen) {
		this.context = context;
		this.fragment = fragment;
		this.dishDetailDto = dishDetailDto;
		this.screen=screen;
		

	}
	
	
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {

			if (context != null) {

				((BaseActivity) context).progressOn();
			} else if (fragment != null) {
				((BaseActivity) fragment.getActivity()).progressOn();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		
		
//		progressDialog = ProgressDialog.show(context, "", "", true);
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflater.inflate(R.layout.progressbar, null, false);
//		progressDialog.setContentView(v);
//		progressDialog.setCancelable(false);
//		progressDialog.setOnCancelListener(new OnCancelListener() {
//			public void onCancel(DialogInterface dialog) {
//				((Activity) context).finish();
//			}
//		});
	}

	@Override
	protected CamPhotoResponseDto doInBackground(Void... params) {
		
		
			try 
			{
				String requestJson = getLikeJson();
				if(requestJson!=null)
				{
					String response = Utils.makePostRequest(Constants.NEW_LIKE_URl,	requestJson);
					if (response != null) 
					{
						CamPhotoResponseDto eventResponseDto = (CamPhotoResponseDto) Api.fromJson(response, CamPhotoResponseDto.class);
						return eventResponseDto;
					}
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
				return null;

	}

	@Override
	protected void onPostExecute(CamPhotoResponseDto result) {
		super.onPostExecute(result);

		if (context != null) {

			((BaseActivity) context).progressOff();
		}
		
//		progressDialog.dismiss();

		if (result != null) {
			
			if(dishDetailDto!=null)
			{
				String dishName=null;
				String branchName=null;
				String branchDishId=null;
				String userId=Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
				String userName=Utils.getFromSharedPreference(Constants.USER_NAME_MIXPANEL);
				
				BranchDishIdDto branchDishIdDto=dishDetailDto.getBranchDishId();
				if(branchDishIdDto!=null)
				{
					DishIdDto dishIdDto=branchDishIdDto.getDishId();
					if(dishIdDto!=null)
					{
						 dishName=dishIdDto.getName();
						
					}
					
					 branchName=branchDishIdDto.getBranchName();
					 branchDishId=branchDishIdDto.getObjectId();
				}
				
				new MyMixpanel().SvaadMixpanelWishIt(context, userId, userName, dishName, branchName, branchDishId, screen, Constants.WISH_IT);
			}
			
			

			Toast.makeText(context,
					context.getResources().getString(R.string.success_wish),
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.request_failed),
					Toast.LENGTH_LONG).show();

		}

	}

//	private String getLikeJson() {
//
//		LikeRequestDto likeRequestDto = new LikeRequestDto();
//		likeRequestDto.setEventRequestDto(dishDetailDto);
//		likeRequestDto.setWishlist(true);
//		String jsonString = new EventJson().getLikeJson(likeRequestDto);
//
//		return jsonString;
//	}
	
	private String getLikeJson() {

		NewLikeRequestDto likeRequestDto = new NewLikeRequestDto();
		if(dishDetailDto!=null)
		{
			BranchDishIdDto branchDishIdDto=dishDetailDto.getBranchDishId();
			if(branchDishIdDto!=null)
			{
				String branchid=branchDishIdDto.getObjectId();
				if(branchid!=null && branchid.length()>0)
				{
					likeRequestDto.setBranchDishId(branchid);
				}
				String userId=Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
				if(userId!=null && userId.length()>0)
				{
					likeRequestDto.setUserId(userId);
				}
			}
			
		}
		String jsonString = null;
		try {
			jsonString = Api.toJson(likeRequestDto);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonString;
	}

}
