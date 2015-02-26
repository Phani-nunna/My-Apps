package com.paradigmcreatives.audit.camera;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.paradigmcreatives.audit.R;
import com.paradigmcreatives.audit.TopGroupActivity;
import com.paradigmcreatives.audit.beans.Banner_Images;
import com.paradigmcreatives.audit.database.AuditDataBase;
import com.paradigmcreatives.audit.database.AuditQuestions;
import com.paradigmcreatives.audit.util.Utility;

public class TakeImage extends Activity {
	private Button takePhoto;
	private Button proceed;
	private ImageView mainBannerImageView;
	private ImageView bannerImage_1;
	private ImageView bannerImage_2;
	private ImageView bannerImage_3;
	private String date_time;
	private Context mContext;
	private AuditDataBase audi_database;
	private ArrayList<Bitmap> stored_images = new ArrayList<Bitmap>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takeimage);
		mContext = this;
		takePhoto = (Button) findViewById(R.id.take_photo);
		proceed = (Button) findViewById(R.id.proceed_button);
		mainBannerImageView = (ImageView) findViewById(R.id.take_imageview);
		bannerImage_1 = (ImageView) findViewById(R.id.image);
		bannerImage_2 = (ImageView) findViewById(R.id.image1);
		bannerImage_3 = (ImageView) findViewById(R.id.image2);
		audi_database = new AuditDataBase(mContext);

		if (bannerImage_1.getTag() == null && bannerImage_2.getTag() == null
				&& bannerImage_3.getTag() == null
				&& mainBannerImageView.getTag() == null) {

			bannerImage_1.setTag("1");

			bannerImage_2.setTag("1");

			bannerImage_3.setTag("1");

			mainBannerImageView.setTag("1");
		}

		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TakeImage.this, CameraView.class);
				startActivityForResult(intent, 120);

			}
		});

		bannerImage_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object bitmap = (Object) bannerImage_1.getTag();
				if (bitmap != null) {
					mainBannerImageView.setImageBitmap((Bitmap) bitmap);
					mainBannerImageView.setTag(bitmap);
				} else {
					mainBannerImageView.setImageBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.takeimage));
				}
			}
		});

		bannerImage_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object bitmap = (Object) bannerImage_2.getTag();
				if (bitmap != null) {
					mainBannerImageView.setImageBitmap((Bitmap) bitmap);
					mainBannerImageView.setTag(bitmap);
				} else {
					mainBannerImageView.setImageBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.takeimage));
				}
			}
		});

		bannerImage_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object bitmap = (Object) bannerImage_3.getTag();
				if (bitmap != null) {
					mainBannerImageView.setImageBitmap((Bitmap) bitmap);
					mainBannerImageView.setTag(bitmap);
				} else {
					mainBannerImageView.setImageBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.takeimage));
				}
			}
		});

		mainBannerImageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (mainBannerImageView.getTag().equals("1")) {
					Toast.makeText(TakeImage.this, "no images to delete",
							Toast.LENGTH_SHORT).show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("Are you sure you want to Delete")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											if (mainBannerImageView.getTag()
													.equals(bannerImage_1
															.getTag())) {
												mainBannerImageView
														.setImageBitmap(BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.takeimage));

												bannerImage_1
														.setVisibility(View.GONE);

												takePhoto
														.setVisibility(View.VISIBLE);
												bannerImage_1.setTag("1");
												if (bannerImage_2.getTag()
														.equals("1")) {
													if (bannerImage_3.getTag()
															.equals("1")) {
														mainBannerImageView
																.setTag("1");
													} else {
														mainBannerImageView
																.setImageBitmap((Bitmap) bannerImage_3
																		.getTag());
														mainBannerImageView
																.setTag(bannerImage_3
																		.getTag());
													}
												} else {
													mainBannerImageView
															.setImageBitmap((Bitmap) bannerImage_2
																	.getTag());
													mainBannerImageView
															.setTag(bannerImage_2
																	.getTag());

												}
											} else if (mainBannerImageView
													.getTag().equals(
															bannerImage_2
																	.getTag())) {
												mainBannerImageView
														.setImageBitmap(BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.takeimage));

												bannerImage_2
														.setVisibility(View.GONE);
												takePhoto
														.setVisibility(View.VISIBLE);
												bannerImage_2.setTag("1");
												if (bannerImage_1.getTag()
														.equals("1")) {
													if (bannerImage_3.getTag()
															.equals("1")) {
														mainBannerImageView
																.setTag("1");
													} else {
														mainBannerImageView
																.setImageBitmap((Bitmap) bannerImage_3
																		.getTag());
														mainBannerImageView
																.setTag(bannerImage_3
																		.getTag());
													}
												} else {
													mainBannerImageView
															.setImageBitmap((Bitmap) bannerImage_1
																	.getTag());
													mainBannerImageView
															.setTag(bannerImage_1
																	.getTag());

												}
											} else if (mainBannerImageView
													.getTag().equals(
															bannerImage_3
																	.getTag())) {
												mainBannerImageView
														.setImageBitmap(BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.takeimage));

												bannerImage_3
														.setVisibility(View.GONE);
												takePhoto
														.setVisibility(View.VISIBLE);
												bannerImage_3.setTag("1");
												mainBannerImageView.setTag("1");
												if (bannerImage_2.getTag()
														.equals("1")) {
													if (bannerImage_1.getTag()
															.equals("1")) {
														mainBannerImageView
																.setTag("1");
													} else {
														mainBannerImageView
																.setImageBitmap((Bitmap) bannerImage_1
																		.getTag());
														mainBannerImageView
																.setTag(bannerImage_1
																		.getTag());
													}
												} else {
													mainBannerImageView
															.setImageBitmap((Bitmap) bannerImage_2
																	.getTag());
													mainBannerImageView
															.setTag(bannerImage_2
																	.getTag());

												}
											}
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
				return true;
			}
		});
		proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!bannerImage_1.getTag().equals("1")) {
					stored_images.add((Bitmap) bannerImage_1.getTag());
				}
				if (!bannerImage_2.getTag().equals("1")) {
					stored_images.add((Bitmap) bannerImage_2.getTag());
				}
				if (!bannerImage_3.getTag().equals("1")) {
					stored_images.add((Bitmap) bannerImage_3.getTag());
				}

				boolean isNetwork = Utility.getInstance().isOnline(
						TakeImage.this);
				if (stored_images.size() > 0) {

					if (!isNetwork) {
						// if Internet is available data send to web services.

					} else {

						if (stored_images.size() > 0) {
							audi_database.openToWrite();
							for (int i = 0; i < stored_images.size(); i++) {
								byte[] imageConvertedData = bitmapToByte(stored_images
										.get(i));
								Banner_Images banner_images = new Banner_Images();
								banner_images.setImage_Path("http://www.audit.com");
								long insertStatus = audi_database.insertIntoBanner_Images(banner_images);
								System.out.println("insertion status"+insertStatus);
							}

						}
						audi_database.close();
						Intent intent_picture = new Intent(TakeImage.this,
								AuditQuestions.class);
						intent_picture.putExtra("process_name", getIntent()
								.getStringExtra("process_name"));
					System.out.println("processname i take image ="+getIntent()
								.getStringExtra("process_name"));
						TopGroupActivity.getInstance().launchActivity(
								intent_picture);

						Toast.makeText(TakeImage.this,
								"Image  stored in Database ",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(TakeImage.this, "Take minimum one Image",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 120) {

				Uri imageUri = data.getData();
				String imagePath = getPath(imageUri);
				File file = new File(imagePath);
				Time dtNow = new Time();
				dtNow.setToNow();
				String time = dtNow.format("%Y%m%d%H%M%S");
				String p = "audit" + time;
				String path = "/sdcard/DCIM/Camera/" + p + ".png";
				File newfile = new File(path);
				boolean check = file.renameTo(newfile);
				byte[] imageData = null;
				if (check) {
					try {
						FileInputStream inputStream = new FileInputStream(
								newfile);
						BufferedInputStream bis = new BufferedInputStream(
								inputStream);
						imageData = new byte[bis.available()];
						bis.read(imageData);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (imageData != null) {
					proceed.setVisibility(View.VISIBLE);
					Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0,
							imageData.length);
					System.out.println("Bitmap height " + bitmap.getHeight());
					System.out.println("Bitmap width " + bitmap.getWidth());
					Bitmap bitmap_with_text = drawTextToBitmap(TakeImage.this,
							bitmap);
					if (bitmap_with_text != null) {
						mainBannerImageView.setTag(bitmap_with_text);
						mainBannerImageView.setImageBitmap(bitmap_with_text);
						if (bannerImage_1.getTag().equals("1")) {
							bannerImage_1.setVisibility(View.VISIBLE);
							bannerImage_1.setTag(bitmap_with_text);
							bannerImage_1.setImageBitmap(bitmap_with_text);
							if (newfile.exists()) {
								newfile.delete();
							}
						} else if (bannerImage_2.getTag().equals("1")) {
							bannerImage_2.setVisibility(View.VISIBLE);
							bannerImage_2.setTag(bitmap_with_text);
							bannerImage_2.setImageBitmap(bitmap_with_text);
							if (newfile.exists()) {
								newfile.delete();
							}
						} else if (bannerImage_3.getTag().equals("1")) {
							bannerImage_3.setVisibility(View.VISIBLE);
							bannerImage_3.setTag(bitmap_with_text);
							bannerImage_3.setImageBitmap(bitmap_with_text);
							takePhoto.setVisibility(View.INVISIBLE);
							if (newfile.exists()) {
								newfile.delete();
							}
						}
					}
				}

			}
		}
	}

	public byte[] bitmapToByte(Bitmap bimap) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bimap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();
		return bitmapdata;
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public Bitmap drawTextToBitmap(Context mContext, Bitmap bitmap) {
		try {
			Resources resources = mContext.getResources();
			float scale = resources.getDisplayMetrics().density;

			Bitmap doodleImage = Bitmap
					.createBitmap(150, 150, Config.ARGB_8888);
			android.graphics.Bitmap.Config bitmapConfig = doodleImage
					.getConfig();
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}

			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);
			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			// paint.setColor(Color.rgb(110,110, 110));
			paint.setColor(color.black);
			// text size in pixels
			paint.setTextSize((int) (12 * scale));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.BLUE);
			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(getCurrDate(), 0, getCurrDate().length(),
					bounds);

			// canvas.drawText(mText, x * scale, y * scale, paint);
			canvas.drawText(getCurrDate(), 100, 210, paint);
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

	}

	public String getCurrDate() {

		Date cal = Calendar.getInstance().getTime();
		date_time = cal.toLocaleString();
		return date_time;

	}

}