package com.svaad.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.svaad.BaseActivity;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.requestDto.UnLikeEventRequestDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.MyMixpanel;
import com.svaad.utils.Utils;

public class UnlikeAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Fragment fragment;
	private String branchDishId;
	private String screen;
	
	private FeedDetailDto dishDetailDto;

	private ProgressDialog progressDialog;
	public UnlikeAsyncTask(Context context, Fragment fragment,
			String branchDishId,String screen,FeedDetailDto dishDetailDto) {
		this.context = context;
		this.fragment = fragment;
		this.branchDishId = branchDishId;
		this.screen=screen;
		this.dishDetailDto = dishDetailDto;

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
	protected String doInBackground(Void... params) {
		
		String response = null;
			try 
			{
				String requestJson = Api.toJson(getUnLikeJson());
				if(requestJson!=null)
				{
					 response = Utils.makePostRequest(Constants.UNLiKE_URL,requestJson);
					//return response;
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
				return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
//		progressDialog.dismiss();

		if (context != null) {

			((BaseActivity) context).progressOff();
		}

		if (result != null)
		{
			if(dishDetailDto!=null)
			{
				String dishName=null;
				String branchName=null;
//				String branchDishId=null;
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
					 //branchDishId=branchDishIdDto.getObjectId();
				}
				
				new MyMixpanel().SvaadMixpanelWishIt(context, userId, userName, dishName, branchName, branchDishId, screen, Constants.WISHED);
			}
			
			Toast.makeText(context,	"Removed from wishlist",	Toast.LENGTH_LONG).show();
		} 
		else 
		{
			Toast.makeText(context,	result,	Toast.LENGTH_LONG).show();

		}

	}

	private UnLikeEventRequestDto getUnLikeJson() {
		
		UnLikeEventRequestDto unlike=new UnLikeEventRequestDto();
		if(branchDishId!=null && branchDishId.length()>0)
		{
			unlike.setBranchDishId(branchDishId);
		}
		
		String currentUserId=Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
		if(currentUserId!=null && currentUserId.length()>0)
		{
			unlike.setUserId(currentUserId);
		}
		
		return unlike;
	}
}
