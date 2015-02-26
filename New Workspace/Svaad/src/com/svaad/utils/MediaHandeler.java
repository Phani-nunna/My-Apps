package com.svaad.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.svaad.BuildConfig;
import com.svaad.Dto.RestaurantDetailsDto;

public class MediaHandeler {

	public static final String TAG = "MediaHandeler";

	public static void launchImageGallery(Context context, String mediaPath) {

		// Intent i = new Intent(Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Utils.saveToSharedPreference(Constants.IMAGE_PATH, mediaPath);

		File imageFile = new File(mediaPath);
		File imageDir = new File(imageFile.getParent());
		if (!imageDir.exists()) {
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "Creating directory: " + imageFile.getParent());
			}
			imageDir.mkdirs();
		}
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("image/*");
		// ((Activity) context).startActivityForResult(
		// Intent.createChooser(intent, "Pick signature image from"),
		// Constants.GALLERY_IMAGE);

		if (Build.VERSION.SDK_INT < 19) {
			Intent in = new Intent();
			// in.setType("image/jpeg");
			in.setType("image/*");
			in.setAction(Intent.ACTION_GET_CONTENT);
			// ((Activity) context).startActivityForResult(
			// Intent.createChooser(in, "Pick signature image from"),
			// Constants.GALLERY_IMAGE);
			((Activity) context).startActivityForResult(in,
					Constants.GALLERY_IMAGE);
		} else {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			// intent.setType("image/jpeg");
			intent.setType("image/*");
			((Activity) context).startActivityForResult(intent,
					Constants.GALLERY_IMAGE);
		}

	}

	public static void captureImage(Context context, String mediaPath) {
		if (!Utils.isSDCardValid(context, true)) {
			return;
		}
		Utils.saveToSharedPreference(Constants.IMAGE_PATH, mediaPath);

		// Lenovo P700i doesn't return the intent data in the expected format
		// if we don't specify the path explicitly
		// Explicitly specifying the path worked on HTC Desire Z also
		File imageFile = new File(mediaPath);
		Uri outputFileUri = Uri.fromFile(imageFile);
		File imageDir = new File(imageFile.getParent());
		if (!imageDir.exists()) {
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "Creating directory: " + imageFile.getParent());
			}
			imageDir.mkdirs();
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "Capturing image as: " + mediaPath);
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Note: You need to pass the URI, not the path!
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		((Activity) context).startActivityForResult(intent,
				Constants.CAPTURE_IMAGE);
	}

	public static void captureImg(Context context, String mediaPath) {
		// if (!Utils.isSDCardValid(context, true)) {
		// return;
		// }
		// Utils.saveToSharedPreference(Constants.IMAGE_PATH, mediaPath);
		//
		// // Lenovo P700i doesn't return the intent data in the expected format
		// // if we don't specify the path explicitly
		// // Explicitly specifying the path worked on HTC Desire Z also
		// File imageFile = new File(mediaPath);
		// Uri outputFileUri = Uri.fromFile(imageFile);
		// File imageDir = new File(imageFile.getParent());
		// if (!imageDir.exists()) {
		// if (BuildConfig.DEBUG) {
		// Log.i(TAG, "Creating directory: " + imageFile.getParent());
		// }
		// imageDir.mkdirs();
		// }
		//
		// if (BuildConfig.DEBUG) {
		// Log.d(TAG, "Capturing image as: " + mediaPath);
		// }
		//
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // Note: You need to pass the URI, not the path!
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		//
		// ((Activity) context).startActivityForResult(intent,
		// Constants.CAPTURE_IMAGE);

		String folder = "Svaad";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
		File myDirectory = new File(Environment.getExternalStorageDirectory()
				+ "/" + folder + "/");
		if (!myDirectory.exists())
			myDirectory.mkdirs();
		File file = new File(myDirectory, fileName);
		Uri imageUri = Uri.fromFile(file);
		String currentPhotoPath = file.getAbsolutePath();
		Utils.saveToSharedPreference("cam_path", currentPhotoPath);
		Intent intent = new Intent(	android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		((Activity) context).startActivityForResult(intent,
				Constants.CAPTURE_IMAGE);
	}
	


}
