package com.svaad.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.SvaadApplication;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.Dto.ObjectIdDTO;
import com.svaad.fragment.WishesCountFragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.WishesCountRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.responseDto.LogInResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.Utils;
import com.svaad.whereDto.WishCountWhereDto;

public class WishCountAsynctask extends AsyncTask<Void, Void, FeedResponseDto> {

	private Context context;
	private Fragment fragment;
	private LogInResponseDto logInResponseDto;
	private FeedDetailDto feedDetailDto;
	private List<String> wishlists;

	public WishCountAsynctask(Context context, Fragment fragment,
			FeedDetailDto feedDetailDto) {
		this.context = context;
		this.fragment = fragment;
		this.feedDetailDto = feedDetailDto;

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
			
			 if(fragment!=null)
			 {
			 ((WishesCountFragment)fragment).progressOn();
			 }

		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}

	@Override
	protected FeedResponseDto doInBackground(Void... params) {

		String feedJson = null;
		try {

			if (feedDetailDto != null) {
				feedJson = Api.toJson(getFeedUser(feedDetailDto));
			} else {

				feedJson = Api.toJson(getFeed());
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String feedResponse = null;

		feedResponse = Utils.makePostRequest(Constants.BRANCH_MENU_URL,
				feedJson);

		FeedResponseDto feedResponseDto = null;
		try {

			if (feedResponse != null) {
				feedResponseDto = (FeedResponseDto) Api.fromJson(feedResponse,
						FeedResponseDto.class);
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

		 if(fragment!=null)
		 {
		 ((WishesCountFragment)fragment).progressOff();
		 }
		if (fragment != null && fragment instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) fragment).setResponse(result);
		} else if (context instanceof SvaadFeedCallback) {
			((SvaadFeedCallback) context).setResponse(result);
		}

	}

	private WishesCountRequestDto getFeed() {
		WishesCountRequestDto wishRequestDto = new WishesCountRequestDto();
		WishCountWhereDto wishCountWhereDto = new WishCountWhereDto();

		ObjectIdDTO objectIdDTO = new ObjectIdDTO();

		// String loginresponse = Utils
		// .getFromSharedPreference(Constants.SVAADLOGIN_RESPONSE);
		//
		// try {
		// if (loginresponse != null) {
		// logInResponseDto = (LogInResponseDto) Api.fromJson(
		// loginresponse, LogInResponseDto.class);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// List<String> followingUsers = logInResponseDto.getWishlistArr();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());

		try {
			wishlists = (List<String>) ObjectSerializer
					.deserialize(sharedPreferences.getString(
							Constants.WISHLIST_ARRAY,
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> whishLists = wishlists;

		if (whishLists != null && whishLists.size() > 0) {
			objectIdDTO.set$in(whishLists);
		}

		wishCountWhereDto.setObjectId(objectIdDTO);

		wishRequestDto.setWhere(wishCountWhereDto);
		wishRequestDto.setInclude("dishId,location");
		wishRequestDto.setLimit(1000);
		wishRequestDto.setSkip(((WishesCountFragment) fragment).skip);
		wishRequestDto.setOrder("-createdAt");
		wishRequestDto.set_method("GET");

		return wishRequestDto;
	}

	private WishesCountRequestDto getFeedUser(FeedDetailDto feedDetailDto) 
	{
		WishesCountRequestDto wishRequestDto = new WishesCountRequestDto();
		WishCountWhereDto wishCountWhereDto = new WishCountWhereDto();

		ObjectIdDTO objectIdDTO = new ObjectIdDTO();

		FeedUserIdDto feedUserId = feedDetailDto.getUserId();
		if (feedUserId != null) {
			List<String> followingUsers = feedUserId.getWishlistArr();

			if (followingUsers != null && followingUsers.size() > 0) {
				objectIdDTO.set$in(followingUsers);
			}

			wishCountWhereDto.setObjectId(objectIdDTO);

			wishRequestDto.setWhere(wishCountWhereDto);
			wishRequestDto.setInclude("dishId,location");
			// wishRequestDto.setKeys("dishTag");
			wishRequestDto.setLimit(1000);
			wishRequestDto.setSkip(((WishesCountFragment) fragment).skip);
			wishRequestDto.setOrder("-createdAt");
			wishRequestDto.set_method("GET");

		}

		return wishRequestDto;

	}

}
