package com.svaad.activity;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.svaad.R;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.asynctask.PhotoUploadFileAsyncTask;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.KitkatMediaPath;
import com.svaad.utils.MediaHandeler;
import com.svaad.utils.TextUtil;
import com.svaad.utils.Utils;

public class AteIt_activity extends Activity {

	Intent a;
	TextView pb, dishname, howisdish;
	int lovedit, good, itsok, yuck;
	RelativeLayout rate, comment, ateit1, ateit2, ateith;
	private String mediaPath;
	FeedDetailDto feedDetailDto;
	ImageView image, uploadpic;
	EditText commentbox;
	TextView tag;
	ActionBar actionbar;
	int height, width;
	Button buttonshare;
	BranchDishIdDto branchDishId;
	DishIdDto dishIdDto;
	public static Bitmap bitmap;
	public String dish_name;
	private String screen;

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ateit_layout);
		// getActionBar().hide();
		initUI();
		rate = (RelativeLayout) findViewById(R.id.rate);
		comment = (RelativeLayout) findViewById(R.id.comment);
		feedDetailDto = (FeedDetailDto) getIntent().getExtras()
				.getSerializable(Constants.DATA);

		if (this.getIntent().getExtras() != null) {
			screen = this.getIntent().getExtras().getString(Constants.SVAAD);
		}

		a = new Intent(this, AteIt_Actions_Activity.class);
		a.putExtra(Constants.DATA, feedDetailDto);
		a.putExtra(Constants.SVAAD, screen);
		a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (feedDetailDto != null)
			branchDishId = feedDetailDto.getBranchDishId();

		if (branchDishId != null) {
			dishIdDto = branchDishId.getDishId();
		}

		if (dishIdDto != null) {
			dish_name = dishIdDto.getName();
			dishname.setText(dish_name);
			howisdish.setText("How is " + dish_name + " ?");
		} else {
			dishname.setText("dish");
			howisdish.setText("How is this dish ?");
		}

		InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(140);
		commentbox.setFilters(filter);
		commentbox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (commentbox.getText().length() > 140)
					Toast.makeText(getApplicationContext(), "limit up",
							Toast.LENGTH_SHORT).show();
				else
					pb.setText((140 - commentbox.getText().length()) + "");
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}
		});

	}

	private void initUI() {
		image = (ImageView) findViewById(R.id.image);
		tag = (TextView) findViewById(R.id.tag);
		ateit1 = (RelativeLayout) findViewById(R.id.rl1);
		ateit2 = (RelativeLayout) findViewById(R.id.mll);
		ateith = (RelativeLayout) findViewById(R.id.hll);
		dishname = (TextView) findViewById(R.id.dishname);
		howisdish = (TextView) findViewById(R.id.howisdish);
		commentbox = (EditText) findViewById(R.id.editText1);
		commentbox.setCursorVisible(true);
		buttonshare = (Button) findViewById(R.id.buttonshare);
		pb = (TextView) findViewById(R.id.counttext);
		uploadpic = (ImageView) findViewById(R.id.uploadpic);
	}

	public void awesome(View v) {
		TranslateAnimation t = new TranslateAnimation(rate.getScaleX(),
				rate.getScaleX(), rate.getScaleY(), rate.getScaleY() - 20);
		t.setDuration(150);
		rate.startAnimation(t);
		rate.setVisibility(View.INVISIBLE);
		comment.setVisibility(View.VISIBLE);
		TranslateAnimation t1 = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY() + 600,
				comment.getScaleY());
		t1.setDuration(300);
		comment.startAnimation(t1);
		image.setImageResource(R.drawable.line_love);
		tag.setText("#lovedit");
		lovedit = 1;
		buttonshare.setBackgroundResource(R.drawable.colorborder);

	}

	public void good(View v) {
		TranslateAnimation t = new TranslateAnimation(rate.getScaleX(),
				rate.getScaleX(), rate.getScaleY(), rate.getScaleY() - 20);
		t.setDuration(150);
		rate.startAnimation(t);
		rate.setVisibility(View.INVISIBLE);
		comment.setVisibility(View.VISIBLE);
		TranslateAnimation t1 = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY() + 600,
				comment.getScaleY());
		t1.setDuration(300);
		comment.startAnimation(t1);
		good = 2;
		image.setImageResource(R.drawable.line_good);
		tag.setText("#good");
		buttonshare.setBackgroundResource(R.drawable.colorborder);
	}

	public void itsok(View v) {
		TranslateAnimation t = new TranslateAnimation(rate.getScaleX(),
				rate.getScaleX(), rate.getScaleY(), rate.getScaleY() - 20);
		t.setDuration(150);
		rate.startAnimation(t);
		rate.setVisibility(View.INVISIBLE);
		comment.setVisibility(View.VISIBLE);
		TranslateAnimation t1 = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY() + 600,
				comment.getScaleY());
		t1.setDuration(300);
		comment.startAnimation(t1);
		image.setImageResource(R.drawable.line_meh);
		tag.setText("#itsok");
		itsok = 3;
		buttonshare.setBackgroundResource(R.drawable.colorborder);

	}

	public void yuck(View v) {
		TranslateAnimation t = new TranslateAnimation(rate.getScaleX(),
				rate.getScaleX(), rate.getScaleY(), rate.getScaleY() - 20);
		t.setDuration(150);
		rate.startAnimation(t);
		rate.setVisibility(View.INVISIBLE);
		comment.setVisibility(View.VISIBLE);
		TranslateAnimation t1 = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY() + 600,
				comment.getScaleY());
		t1.setDuration(300);
		comment.startAnimation(t1);
		image.setImageResource(R.drawable.line_yuck);
		// tag.setText("#yuck");
		tag.setText("#nevermind");
		yuck = 4;
		buttonshare.setBackgroundResource(R.drawable.colorborder);
	}

	public void back(View v) {
		TranslateAnimation t1 = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY(),
				comment.getScaleY() + 600);
		t1.setDuration(150);
		comment.startAnimation(t1);
		comment.setVisibility(View.GONE);
		rate.setVisibility(View.VISIBLE);
		TranslateAnimation t = new TranslateAnimation(comment.getScaleX(),
				comment.getScaleX(), comment.getScaleY() - 20,
				comment.getScaleY());
		t.setDuration(300);
		rate.startAnimation(t);
		buttonshare.setBackgroundResource(R.drawable.shapeb);
	}

	public void back2(View v) {
		ateit1.setVisibility(View.VISIBLE);
		TranslateAnimation t = new TranslateAnimation(ateit2.getScaleX(),
				ateit2.getScaleX(), ateit2.getScaleY(),
				ateit2.getScaleY() + 600);
		t.setDuration(300);
		ateit2.startAnimation(t);
		ateith.setVisibility(View.VISIBLE);
		TranslateAnimation th = new TranslateAnimation(ateith.getScaleX(),
				ateith.getScaleX(), ateith.getScaleY(),
				ateith.getScaleY() - 300);
		th.setDuration(300);
		ateith.startAnimation(th);
		ateit2.setVisibility(View.INVISIBLE);
		ateith.setVisibility(View.INVISIBLE);

	}

	public void skip(View v) {
		ateit1.setVisibility(View.INVISIBLE);
		ateit2.setVisibility(View.VISIBLE);
		ateith.setVisibility(View.VISIBLE);
		TranslateAnimation t = new TranslateAnimation(ateit2.getScaleX(),
				ateit2.getScaleX(), ateit2.getScaleY() + 600,
				ateit2.getScaleY());
		t.setDuration(300);
		ateit2.startAnimation(t);
		ateith.setVisibility(View.VISIBLE);
		TranslateAnimation th = new TranslateAnimation(ateith.getScaleX(),
				ateith.getScaleX(), ateith.getScaleY() - 300,
				ateith.getScaleY());
		th.setDuration(300);
		ateith.startAnimation(th);
	}

	public void share(View v) {
		if (rate.getVisibility() == View.VISIBLE) {
			Toast.makeText(getApplicationContext(), "Chose an option to share",
					Toast.LENGTH_SHORT).show();
		} else {
			uploadFile();
		}
	}

	public void selectapic(View v) {
		mediaPath = Utils.getMediaPath();
		MediaHandeler.launchImageGallery(AteIt_activity.this, mediaPath);
		// if(bitmap!=null){
		// uploadpic.setImageBitmap(bitmap);
		// }
		// else
		// uploadpic.setImageResource(R.drawable.temp);

	}

	public void takeapic(View v) {
		mediaPath = Utils.getMediaPath();
		MediaHandeler.captureImage(AteIt_activity.this, mediaPath);
		// if(bitmap!=null){
		// uploadpic.setImageBitmap(bitmap);
		// }
		// else
		// uploadpic.setImageResource(R.drawable.temp);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		super.onActivityResult(requestCode, resultCode, intent);

		String mediaPath = AteIt_activity.this.getMediaPath();
		reinitializeValues();

		try {
			String dishDetailJson = Api.toJson(feedDetailDto);
			Utils.saveToSharedPreference(Constants.DISH_DETAIL_JSON,
					dishDetailJson);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (feedDetailDto == null || mediaPath == null
				|| resultCode == Constants.DISH_DETAIL_RESULT) {
			return;
		}
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case Constants.CAPTURE_IMAGE:
				bitmap = Utils.compressImage(mediaPath, this);
				uploadpic.setImageBitmap(bitmap);
				height = bitmap.getHeight();
				width = bitmap.getWidth();
				a.putExtra(Constants.HEIGHT, height);
				a.putExtra(Constants.WIDTH, width);
				a.putExtra(Constants.IMAGE_PATH, mediaPath);
				// uploadFile();
				skip(null);
				break;

			case Constants.GALLERY_IMAGE:
				if (mediaPath != null && intent != null
						&& intent.getData() != null) {

					Uri originalUri = intent.getData();
					Uri uri = intent.getData();

					String id = "";
					if (uri.getLastPathSegment().contains(":"))
						id = uri.getLastPathSegment().split(":")[1];
					else
						id = uri.getLastPathSegment();

					// Uri uri = getUri();
					//
					// Uri uri = intent.getData();
					if (uri != null && "content".equals(uri.getScheme())) {
						Cursor cursor = getContentResolver()
								.query(uri,
										new String[] {
												MediaStore.Images.Media.DATA,
												MediaStore.Images.ImageColumns.MIME_TYPE },
										MediaStore.Images.Media._ID + "=" + id,
										null, null);

						if (cursor != null && cursor.moveToFirst()) {

							if (cursor.getString(0) != null) {

								String l = cursor
										.getString(cursor
												.getColumnIndex(MediaStore.Images.Media.DATA));
								Utils.copyFile(this, l, mediaPath);

								Bitmap bitmap2 = Utils.compressImage(mediaPath,
										this);
								// uploadFile();
								uploadpic.setImageBitmap(bitmap2);
								height = bitmap2.getHeight();
								width = bitmap2.getWidth();

								a.putExtra(Constants.HEIGHT, height);
								a.putExtra(Constants.WIDTH, width);
								a.putExtra(Constants.IMAGE_PATH, mediaPath);
							}

							else {

								// ParcelFileDescriptor parcelFileDescriptor;
								// try {
								// parcelFileDescriptor = getContentResolver()
								// .openFileDescriptor(uri, "r");
								// FileDescriptor fileDescriptor =
								// parcelFileDescriptor
								// .getFileDescriptor();
								// Bitmap image = BitmapFactory
								// .decodeFileDescriptor(fileDescriptor);
								// parcelFileDescriptor.close();
								//
								// FileOutputStream out=null;
								// Bitmap bitmap2 =null;
								// try {
								// out = new FileOutputStream(mediaPath);
								// image.compress(Bitmap.CompressFormat.PNG, 90,
								// out);
								// bitmap2 = Utils.compressImage(mediaPath,
								// this);
								// } catch (Exception e) {
								// e.printStackTrace();
								// } finally {
								// try{
								// out.close();
								// } catch(Throwable ignore) {}
								// }
								//
								// uploadpic.setImageBitmap(bitmap2);
								//
								// height = bitmap2.getHeight();
								// width = bitmap2.getWidth();
								//
								// a.putExtra(Constants.HEIGHT, height);
								// a.putExtra(Constants.WIDTH, width);
								// a.putExtra(Constants.IMAGE_PATH, mediaPath);
								//
								// } catch (FileNotFoundException e) {
								// e.printStackTrace();
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }

								Uri origUri = intent.getData();
								final int takeFlags = intent.getFlags()
										& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
								// Check for the freshest data.

								// String kitkatPath=origUri.toString();
								getContentResolver()
										.takePersistableUriPermission(origUri,
												takeFlags);
								String kitkathpath = KitkatMediaPath.getPath(
										AteIt_activity.this, origUri);
								Utils.copyFile(this, kitkathpath, mediaPath);
								Bitmap bitmap2 = Utils.compressImage(mediaPath,
										this);
								;
								uploadpic.setImageBitmap(bitmap2);

								height = bitmap2.getHeight();
								width = bitmap2.getWidth();

								a.putExtra(Constants.HEIGHT, height);
								a.putExtra(Constants.WIDTH, width);
								a.putExtra(Constants.IMAGE_PATH, mediaPath);

							}

						}
						cursor.close();
					} else if (mediaPath != null && uri != null
							&& "file".equals(uri.getScheme())) {
						Utils.copyFile(this, uri.getPath(), mediaPath);
						Bitmap bitmap3 = Utils.compressImage(mediaPath, this);
						height = bitmap3.getHeight();
						width = bitmap3.getWidth();
						// uploadFile();

						a.putExtra(Constants.HEIGHT, height);
						a.putExtra(Constants.WIDTH, width);
						a.putExtra(Constants.IMAGE_PATH, mediaPath);

						uploadpic.setImageBitmap(bitmap3);
					} else {
						Toast.makeText(
								this,
								"The item you picked is not an image. Please pick an image.",
								Toast.LENGTH_LONG).show();
						return;
					}

				}
				skip(null);
				break;

			default:
				break;
			}
		}
	}

	public String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}

	private void reinitializeValues() {
		if (feedDetailDto == null) {
			try {
				String stringJson = Utils
						.getFromSharedPreference(Constants.DISH_DETAIL_JSON);
				if (stringJson == null) {
					return;
				}
				feedDetailDto = (FeedDetailDto) Api.fromJson(Utils
						.getFromSharedPreference(Constants.DISH_DETAIL_JSON),
						FeedDetailDto.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (mediaPath == null) {
			mediaPath = Utils.getFromSharedPreference(Constants.IMAGE_PATH);
		}
	}

	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	private void uploadFile() {

		String screenName = null;
		if (screen != null) {
			screenName = screen;
		}

		String commentText = commentbox.getText().toString();

		if (feedDetailDto != null) {

			// if (commentText != null && commentText.length() > 0) {

			List<String> usertags = TextUtil.getHashTags(commentText);

			feedDetailDto.setCommentText(commentText);

			if (lovedit != 0) {
				new PhotoUploadFileAsyncTask(this, feedDetailDto, mediaPath,
						usertags, height, width, lovedit, screenName).execute();

			} else if (good != 0)

			{
				new PhotoUploadFileAsyncTask(this, feedDetailDto, mediaPath,
						usertags, height, width, good, screenName).execute();
			} else if (itsok != 0) {
				new PhotoUploadFileAsyncTask(this, feedDetailDto, mediaPath,
						usertags, height, width, itsok, screenName).execute();
			} else {
				new PhotoUploadFileAsyncTask(this, feedDetailDto, mediaPath,
						usertags, height, width, yuck, screenName).execute();
			}

		}
	}

}
