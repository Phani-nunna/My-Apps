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
import com.svaad.Dto.UploadPhotoDto;
import com.svaad.activity.Ateit2_activity;
import com.svaad.activity.CamResListActivity;
import com.svaad.activity.SearchDish_Activity;
import com.svaad.requestDto.CamPhotoUploadPhotoRequestDto;
import com.svaad.responseDto.CamPhotoResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class CamPhotoUploadFileAsyncTask extends
		AsyncTask<Void, Void, CamPhotoResponseDto> {

	private Context context;
	private String mediaPath, picturename, pictureurl, branchDishid, userId,
			commentText, picture;
	private List<String> userTags;
	private int height, width;
	private ProgressDialog progressDialog;
	private int action;

	public CamPhotoUploadFileAsyncTask(Context context, String mediaPath,
			String branchDishid, String commentText, List<String> userTags,
			String userId, int height, int width, int action) {
		this.context = context;
		this.mediaPath = mediaPath;
		this.userTags = userTags;
		this.height = height;
		this.width = width;
		this.action = action;
		this.branchDishid = branchDishid;
		this.commentText = commentText;
		this.userId = userId;

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
	protected CamPhotoResponseDto doInBackground(Void... params) {
		String response = null;
		CamPhotoUploadPhotoRequestDto camPhotoUploadPhotoRequestDto;
		CamPhotoResponseDto eventResponseDto;
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

					if (uploadPhotoDto != null) {
						picturename = uploadPhotoDto.getName();
						pictureurl = uploadPhotoDto.getUrl();
					}

					camPhotoUploadPhotoRequestDto = getPhotoJson(picturename,
							pictureurl);

					try {
						requestJson = Api.toJson(camPhotoUploadPhotoRequestDto);
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					// requestJson = getPhotoJson(picturename,pictureurl);
					if (requestJson == null) {
						return null;
					}
					// response =
					// Utils.makePostRequest(Constants.SAVE_PHOTO_URL,
					// requestJson);

					// if(resBranchId!=null && dishname!=null&&
					// !dishname.equalsIgnoreCase("")&&!resBranchId.equalsIgnoreCase(""))
					// {
					// response =
					// Utils.makePostRequest(Constants.CAM_DISH_ATE_IT,
					// requestJson);
					// }
					// else if(resPageBranchId!=null && dishname!=null&&
					// !dishname.equalsIgnoreCase("")&&!resPageBranchId.equalsIgnoreCase(""))
					// {
					// response =
					// Utils.makePostRequest(Constants.CAM_DISH_ATE_IT,
					// requestJson);
					// }
					// else
					// {

					response = Utils.makePostRequest(Constants.CAM_ATE_IT,
							requestJson);
					// }
					if (requestJson != null) {
						eventResponseDto = (CamPhotoResponseDto) Api.fromJson(
								response, CamPhotoResponseDto.class);

						return eventResponseDto;
					}

				}

				else {
					camPhotoUploadPhotoRequestDto = getPhotoJson(picturename,
							pictureurl);
					try {
						requestJson = Api.toJson(camPhotoUploadPhotoRequestDto);
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (requestJson == null) {
						return null;
					}
					// response =
					// Utils.makePostRequest(Constants.SAVE_PHOTO_URL,
					// requestJson);
					response = Utils.makePostRequest(Constants.CAM_ATE_IT,
							requestJson);
					if (requestJson != null) {
						eventResponseDto = (CamPhotoResponseDto) Api.fromJson(
								response, CamPhotoResponseDto.class);
						picture = "false";

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

	private CamPhotoUploadPhotoRequestDto getPhotoJson(String picturename,
			String pictureurl) {
		CamPhotoUploadPhotoRequestDto camPhotoUploadPhotoRequestDto = new CamPhotoUploadPhotoRequestDto();

		camPhotoUploadPhotoRequestDto.setBranchDishId(branchDishid);
		if (commentText != null) {
			camPhotoUploadPhotoRequestDto.setCommentText(commentText);
		}
		if (picturename != null)
			camPhotoUploadPhotoRequestDto.setDishPhoto(picturename);

		// if(resBranchId!=null && dishname!=null&&
		// !dishname.equalsIgnoreCase("")&&!resBranchId.equalsIgnoreCase(""))
		// {
		// camPhotoUploadPhotoRequestDto.setBranchId(resBranchId);
		// camPhotoUploadPhotoRequestDto.setName(dishname);
		// }
		// else if(resPageBranchId!=null && dishname!=null&&
		// !dishname.equalsIgnoreCase("")&&!resPageBranchId.equalsIgnoreCase(""))
		// {
		// camPhotoUploadPhotoRequestDto.setBranchId(resPageBranchId);
		// camPhotoUploadPhotoRequestDto.setName(dishname);
		// }

		camPhotoUploadPhotoRequestDto.setDishTag(action);
		camPhotoUploadPhotoRequestDto.setImgH(height);
		camPhotoUploadPhotoRequestDto.setImgW(width);
		if (pictureurl != null)
			camPhotoUploadPhotoRequestDto.setPhotoUrl(pictureurl);
		camPhotoUploadPhotoRequestDto.setUserId(userId);
		camPhotoUploadPhotoRequestDto.setUserTags(userTags);
		return camPhotoUploadPhotoRequestDto;
	}

	private Base64Dto getImageString() throws Exception {
		FileInputStream inputStream = null;
		Base64Dto base64Dto = null;
		ByteArrayOutputStream outputStream = null;

		if (mediaPath != null) {

			try {

				inputStream = new FileInputStream(new File(mediaPath));
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				// Toast.makeText(context, "Image uploading", 200).show();

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
	protected void onPostExecute(CamPhotoResponseDto result) {
		super.onPostExecute(result);

		progressDialog.dismiss();
		if (result != null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.success),
					Toast.LENGTH_LONG).show();

			((Activity) context).finish();

			// ((Activity)SearchDish_Activity.this).finish();

			SearchDish_Activity searchActivity = new SearchDish_Activity();
			if (searchActivity != null) {
				if (searchActivity.mactivity != null) {
					searchActivity.mactivity.finish();
				}
			}

			CamResListActivity camActivity = new CamResListActivity();
			if (camActivity != null) {
				if (camActivity.mactivity != null) {
					camActivity.mactivity.finish();
				}
			}

			Ateit2_activity ateActivity = new Ateit2_activity();
			if (ateActivity != null) {
				if (ateActivity.mactivity != null) {
					ateActivity.mactivity.finish();
				}
			}
		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.request_failed),
					Toast.LENGTH_LONG).show();
		}

	}

}
