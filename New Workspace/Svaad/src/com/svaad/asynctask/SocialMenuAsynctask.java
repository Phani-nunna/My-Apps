package com.svaad.asynctask;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.activity.RestaurantProfilesActivity;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.DishCommentsRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;
import com.svaad.whereDto.DishCommentsWhereDto;

public class SocialMenuAsynctask extends AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private Fragment fragment;
	private String branchId;
	
	DatabaseDAO da;

	public SocialMenuAsynctask(Context context, Fragment fragment,String branchId) {
		this.context = context;
		this.fragment = fragment;
		this.branchId=branchId;

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
			
			if(context!=null && context instanceof RestaurantProfilesActivity)
			{
				((RestaurantProfilesActivity)context).progressOn();
			}
//			else if(context!=null && context instanceof NewRestaurantProfileActivity)
//			{
//				((NewRestaurantProfileActivity)context).progressOn();
//			}
//			else if(fragment!=null && fragment instanceof New_Restaurant_Fragment)
//			{
//				((New_Restaurant_Fragment)fragment).progressOn();
//			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	protected FeedResponseDto doInBackground(Void... params) {

		String feedJson = null;
		try {

			feedJson = Api.toJson(getSocialMenu());

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String feedResponse = null;

		feedResponse = Utils.makePostRequest(Constants.FEED_URL, feedJson);

		FeedResponseDto feedResponseDto = null;
		try {

			if (feedResponse != null) {
				feedResponseDto = (FeedResponseDto) Api.fromJson(feedResponse,
						FeedResponseDto.class);
				
//				List<FeedDetailDto> socialMenusList=((FeedResponseDto) feedResponseDto).getResults();
//				try
//				{
//					da=new DatabaseDAO(context);
//					da.insertSocialMenu(socialMenusList);
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//				}
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
	protected void onPostExecute(FeedResponseDto result) {
		super.onPostExecute(result);

//		if (context != null) {
//
//			((BaseActivity) context).progressOff();
//		}
		if(context!=null && context instanceof RestaurantProfilesActivity)
		{
			((RestaurantProfilesActivity)context).progressOff();
		}
//		else if(context!=null && context instanceof NewRestaurantProfileActivity)
//		{
//			((NewRestaurantProfileActivity)context).progressOff();
//		}
//		else if(fragment!=null && fragment instanceof New_Restaurant_Fragment)
//		{
//			((New_Restaurant_Fragment)fragment).progressOff();
//		}
		if(result==null)
		{
			new SvaadDialogs().showToast(context, "No results");
		}

		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private DishCommentsRequestDto getSocialMenu() {
		
		DishCommentsRequestDto restaurantRequestDto=new DishCommentsRequestDto();
		
		DishCommentsWhereDto whereDto=new DishCommentsWhereDto();
		BranchIdDto branchIdDto=new BranchIdDto();
		branchIdDto.set__type("Pointer");
		branchIdDto.setClassName("Branches");
		if(branchId!=null && branchId.length()>0)
		{
			branchIdDto.setObjectId(branchId);
		}
		whereDto.setBranchId(branchIdDto);
		
		FromRestaurantDto fromResDto=new FromRestaurantDto();		
		fromResDto.set$exists(false);
		whereDto.setFromRestaurant(fromResDto);
		
//		CommentTextDto commentTextDto=new CommentTextDto();
//		commentTextDto.set$exists(true);
//		commentTextDto.set$ne("");
//		whereDto.setCommentText(commentTextDto);
		
		restaurantRequestDto.setWhere(whereDto);
		restaurantRequestDto.setInclude("userId,branchDishId.dishId,branchDishId.location");
		restaurantRequestDto.setKeys("userId,branchDishId,commentText,dishPhoto,dishTag");
		restaurantRequestDto.setLimit(1000);
		restaurantRequestDto.setSkip(0);
		restaurantRequestDto.setOrder("-createdAt");
		restaurantRequestDto.set_method("GET");	
		
		return restaurantRequestDto;
	}
}
