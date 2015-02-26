package com.svaad.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.R;
import com.svaad.Dto.Base64Dto;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.EventJson;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.UploadPhotoDto;
import com.svaad.requestDto.UplaodPhotoRequestDto;
import com.svaad.responseDto.EventResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.MyMixpanel;
import com.svaad.utils.Utils;

public class PhotoUploadFileAsyncTask extends
		AsyncTask<Void, Void, EventResponseDto> {

	private Context context;
	private FeedDetailDto dishDetailDto;
	private String mediaPath;
	private List<String> userTags;
	private int height, width;
	private ProgressDialog progressDialog;
	private int action;
	private String screenName, picture;

	public PhotoUploadFileAsyncTask(Context context,
			FeedDetailDto dishDetailDto, String mediaPath,
			List<String> userTags, int height, int width, int action,
			String screenName) {
		this.context = context;
		this.dishDetailDto = dishDetailDto;
		this.mediaPath = mediaPath;
		this.userTags = userTags;
		this.height = height;
		this.width = width;
		this.action = action;
		this.screenName = screenName;
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
	protected EventResponseDto doInBackground(Void... params) {
		String response = null;
		try {

			Base64Dto base64Dto = getImageString();

			String requestJson = null;
			try {
				requestJson = Api.toJson(base64Dto);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// if (requestJson == null|| requestJson.equalsIgnoreCase("null")) {
			// return null;
			// }

			if (requestJson != null && requestJson.length() > 0
					&& !requestJson.equalsIgnoreCase("null")) {

				response = Utils.makePostRequest(Constants.UPLOAD_IMAGE_URL
						+ System.currentTimeMillis() + ".jpg", requestJson);

			}

			try {
				if (response != null) {
					UploadPhotoDto uploadPhotoDto = (UploadPhotoDto) Api
							.fromJson(response, UploadPhotoDto.class);
					

					requestJson = getPhotoJson(uploadPhotoDto);
					if (requestJson == null) {
						return null;
					}
					// response =
					// Utils.makePostRequest(Constants.SAVE_PHOTO_URL,
					// requestJson);
					response = Utils.makePostRequest(Constants.FEED_URL,
							requestJson);
					if (requestJson != null) {
						EventResponseDto eventResponseDto = (EventResponseDto) Api
								.fromJson(response, EventResponseDto.class);

						if (response != null) {
							setMixpanelData(picture);
						}
						return eventResponseDto;
					}

				}

				else {

					requestJson = getPhotoJson();
					if (requestJson == null) {
						return null;
					}
					// response =
					// Utils.makePostRequest(Constants.SAVE_PHOTO_URL,
					// requestJson);
					response = Utils.makePostRequest(Constants.FEED_URL,
							requestJson);
					if (requestJson != null) {
						EventResponseDto eventResponseDto = (EventResponseDto) Api
								.fromJson(response, EventResponseDto.class);
						picture = "false";

						if (response != null) {
							setMixpanelData(picture);
						}
						return eventResponseDto;
					}
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getPhotoJson(UploadPhotoDto uploadPhotoDto) {
		UplaodPhotoRequestDto uplaodPhotoRequestDto = new UplaodPhotoRequestDto();
		uplaodPhotoRequestDto.setEventRequestDto(dishDetailDto, userTags);

		uplaodPhotoRequestDto.setImgHeight(height);
		uplaodPhotoRequestDto.setImgWidth(width);
		uplaodPhotoRequestDto.setDishTag(action);

		if (dishDetailDto.getCommentText() != null
				&& dishDetailDto.getCommentText().length() > 0) {

			uplaodPhotoRequestDto
					.setCommentText(dishDetailDto.getCommentText());
		}

		if (mediaPath != null && mediaPath.length() > 0) {

			DishPicDto dishPicDto = new DishPicDto();
			dishPicDto.set__type("File");
			dishPicDto.setName(uploadPhotoDto.getName());

			dishPicDto.setUrl(uploadPhotoDto.getUrl());

			uplaodPhotoRequestDto.setDishPhoto(dishPicDto);
		}
		String jsonString = new EventJson().getPhotoJson(uplaodPhotoRequestDto);
		return jsonString;

	}

	private String getPhotoJson() {
		UplaodPhotoRequestDto uplaodPhotoRequestDto = new UplaodPhotoRequestDto();
		uplaodPhotoRequestDto.setEventRequestDto(dishDetailDto, userTags);

		uplaodPhotoRequestDto.setImgHeight(height);
		uplaodPhotoRequestDto.setImgWidth(width);
		uplaodPhotoRequestDto.setDishTag(action);

		if (dishDetailDto.getCommentText() != null
				&& dishDetailDto.getCommentText().length() > 0) {

			uplaodPhotoRequestDto
					.setCommentText(dishDetailDto.getCommentText());
		}

		String jsonString = new EventJson().getPhotoJson(uplaodPhotoRequestDto);
		return jsonString;

	}

	private Base64Dto getImageString() throws Exception {
		FileInputStream inputStream = null;
		Base64Dto base64Dto = null;
		ByteArrayOutputStream outputStream = null;

		if (mediaPath != null) {

			try {

				inputStream = new FileInputStream(new File(mediaPath));
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				//Toast.makeText(context, "Image uploading", 200).show();

				outputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
				byte[] imageByte = outputStream.toByteArray();
				String baseString = Base64.encodeToString(imageByte,
						Base64.DEFAULT);
				base64Dto = new Base64Dto();
				base64Dto.setBase64(baseString);
				base64Dto.set_ContentType("image/jpeg");
				
				picture = "true";
				return base64Dto;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}

					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(EventResponseDto result) {
		super.onPostExecute(result);

		progressDialog.dismiss();
		if (result != null && result.getObjectId() != null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.success),
					Toast.LENGTH_LONG).show();

			((Activity) context).finish();

		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.request_failed),
					Toast.LENGTH_LONG).show();
		}

	}

	private void setMixpanelData(String picture) {
		if (dishDetailDto != null) {
			String dishName = null;
			String branchName = null;
			String branchDishId = null;
			String userId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
			String userName = Utils
					.getFromSharedPreference(Constants.USER_NAME_MIXPANEL);

			BranchDishIdDto branchDishIdDto = dishDetailDto.getBranchDishId();
			if (branchDishIdDto != null) {
				DishIdDto dishIdDto = branchDishIdDto.getDishId();
				if (dishIdDto != null) {
					dishName = dishIdDto.getName();

				}

				branchName = branchDishIdDto.getBranchName();
				branchDishId = branchDishIdDto.getObjectId();
			}

			new MyMixpanel().SvaadMixpanelAteIt(context, userId, userName,
					dishName, branchName, branchDishId, screenName,
					Constants.ATE_IT, picture, "Shared");
		}
	}

}
