package com.svaad.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.asynctask.CamPhotoAddDishUploadFileAsyncTask;
import com.svaad.asynctask.CamPhotoUploadFileAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.TextUtil;
import com.svaad.utils.Utils;

@SuppressLint("NewApi")
public class Ateit2_activity extends BaseActivity {
	ImageView iv, image;
	String path;
	int lovedit, good, itsok, yuck;
	int height, width;
	private android.app.ActionBar actionBar;
	FragmentTransaction fragmentTransaction;
	int mode;
	private EditText commentbox;
	int currentPage;
	private RelativeLayout tvDishNameWrapper, rate, comment;
	private TextView tvDishName, tvLocationName, tvResName, tag, pb;
	private CamRestaurantDetailDto restaurantDetail;
	private CamDishesDeatailDto dishDetail;
	private Button rest_tag, buttonshare, btnEdit;
	private LinearLayout ll, ll2;
	Boolean nonveg;
	private RestaurantDetailsDto resaurantPageDetail;
	 public static Activity mactivity;
	String addDish;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ateit3_layout);
		
		mactivity=this;

		actionBar = getActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);

		LayoutInflater li = LayoutInflater.from(Ateit2_activity.this);
		View customView = li.inflate(R.layout.share_layout, null);
		buttonshare = (Button) customView.findViewById(R.id.buttonshare);
		actionBar.setCustomView(customView);
		actionBar.setDisplayShowCustomEnabled(true);

		init();

		path = getIntent().getExtras().getString("path");
		nonveg = getIntent().getExtras().getBoolean("nonveg");
		addDish = getIntent().getExtras().getString(Constants.ADD_DISH);
		restaurantDetail = (CamRestaurantDetailDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.CAM_RES_DATA) : null);

		resaurantPageDetail = (RestaurantDetailsDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.BRANCH_DETAILS) : null);

		if (resaurantPageDetail != null) {
			rest_tag.setText("Choose a Dish");
		}

		dishDetail = (CamDishesDeatailDto) (this.getIntent().getExtras() != null ? this
				.getIntent().getExtras()
				.getSerializable(Constants.CAM_DISHES_DATA)
				: null);

		if (path != null) {
			Bitmap bitmap = Utils.compressImage(path, this);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			iv.setImageBitmap(bitmap);
		}

		if (restaurantDetail != null && dishDetail != null) {
			tvDishNameWrapper.setVisibility(View.VISIBLE);
			ll.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.VISIBLE);
			btnEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent i = new Intent(Ateit2_activity.this,
							CamResListActivity.class);
					i.putExtra("path", path);
					startActivity(i);
				}
			});
			rate.setVisibility(View.VISIBLE);
			rest_tag.setVisibility(View.GONE);
			// rest_tag.setText("Edit");
			if (restaurantDetail != null) {

				String resname = restaurantDetail.getBranchName();
				LocationDto locationDto = restaurantDetail.getLocation();
				String dishName = dishDetail.getDishName();

				if (dishName != null && dishName.length() > 0)

				{
					tvDishName.setText(dishName);
				}
				if (locationDto != null) {

					String locationname = locationDto.getName();
					if (locationname != null && locationname.length() > 0) {
						tvLocationName.setText("," + locationname);
					} else {
						tvLocationName.setText(null);
					}

				}
				if (resname != null && resname.length() > 0) {

					tvResName.setText(resname);
				} else {
					tvResName.setText(null);
				}
			}

		} else if (resaurantPageDetail != null && dishDetail != null) {
			tvDishNameWrapper.setVisibility(View.VISIBLE);
			ll.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.VISIBLE);
			btnEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent i = new Intent(Ateit2_activity.this,
							CamResListActivity.class);
					i.putExtra("path", path);
					startActivity(i);
				}
			});
			rate.setVisibility(View.VISIBLE);
			rest_tag.setVisibility(View.GONE);
			// rest_tag.setText("Edit");
			if (resaurantPageDetail != null) {

				String resname = resaurantPageDetail.getBranchName();
				LocationDto locationDto = resaurantPageDetail.getLocation();
				String dishName = dishDetail.getDishName();

				if (dishName != null && dishName.length() > 0)

				{
					tvDishName.setText(dishName);
				}
				if (locationDto != null) {

					String locationname = locationDto.getName();
					if (locationname != null && locationname.length() > 0) {
						tvLocationName.setText("," + locationname);
					} else {
						tvLocationName.setText(null);
					}

				}
				if (resname != null && resname.length() > 0) {

					tvResName.setText(resname);
				} else {
					tvResName.setText(null);
				}
			}

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

	private void init() {
		iv = (ImageView) findViewById(R.id.imageView1);

		comment = (RelativeLayout) findViewById(R.id.comment);
		rate = (RelativeLayout) findViewById(R.id.rate);
		commentbox = (EditText) findViewById(R.id.editText1);
		commentbox.setCursorVisible(true);
		image = (ImageView) findViewById(R.id.image);

		ll = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);

		rest_tag = (Button) findViewById(R.id.rest_tag);

		btnEdit = (Button) findViewById(R.id.btnEdit);
		tvDishNameWrapper = (RelativeLayout) findViewById(R.id.tvDishNameWrapper);
		tag = (TextView) findViewById(R.id.tag);
		tvDishName = (TextView) findViewById(R.id.tvDishName);
		tvResName = (TextView) findViewById(R.id.tvResName);
		tvLocationName = (TextView) findViewById(R.id.tvLocationName);
		pb = (TextView) findViewById(R.id.counttext);
	}

	@SuppressLint("NewApi")
	public void restlist(View v) {

		if (resaurantPageDetail != null) {

			Intent i = new Intent(Ateit2_activity.this,
					SearchDish_Activity.class);
			i.putExtra("path", path);
			i.putExtra(Constants.BRANCH_DETAILS, resaurantPageDetail);
			startActivity(i);
		} else {
			Intent i = new Intent(Ateit2_activity.this,
					CamResListActivity.class);
			i.putExtra("path", path);
			startActivity(i);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
//			moveTaskToBack(true);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
		
		itsok=0;
		yuck=0;
		good=0;
		// buttonshare.setBackgroundResource(R.drawable.colorborder);

		buttonshare.setBackgroundColor(Color.parseColor("#47a447"));

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
		
		
		itsok=0;
		yuck=0;
		lovedit=0;
		
		image.setImageResource(R.drawable.line_good);
		tag.setText("#good");
		// buttonshare.setBackgroundResource(R.drawable.colorborder);
		buttonshare.setBackgroundColor(Color.parseColor("#47a447"));
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
		
		
		good=0;
		yuck=0;
		lovedit=0;
		// buttonshare.setBackgroundResource(R.drawable.colorborder);
		buttonshare.setBackgroundColor(Color.parseColor("#47a447"));

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
		
		
		itsok=0;
		good=0;
		lovedit=0;
		// buttonshare.setBackgroundResource(R.drawable.colorborder);
		buttonshare.setBackgroundColor(Color.parseColor("#47a447"));
	}

	public void share(View v) {
		if (comment.getVisibility() == View.VISIBLE) {
			if (addDish != null) {
//				new SvaadDialogs()
//						.showToast(Ateit2_activity.this, nonveg+"Add a dish");
				uploadFileAddDish();
			} else {
//				new SvaadDialogs().showToast(Ateit2_activity.this,
//						"Not Add a dish");
				uploadFile();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Chose an option to share",
					Toast.LENGTH_SHORT).show();
		}
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
		// buttonshare.setBackgroundResource(R.drawable.shapeb);
		buttonshare.setBackgroundColor(Color.parseColor("#555555"));
	}

	private void uploadFile() {

		// String screenName = null;

		String userId = Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

		String resObjectId = null, respageObjectId = null, dishname = null;

		if (restaurantDetail != null) {
			resObjectId = restaurantDetail.getObjectId();
		}

		if (resaurantPageDetail != null) {
			respageObjectId = resaurantPageDetail.getObjectId();
		}

		if (dishDetail != null) {
			dishname = dishDetail.getDishName();
		}
		if (userId != null && userId.length() > 0) {

			String commentText = commentbox.getText().toString();

			// if (commentText != null && commentText.length() > 0) {

			List<String> usertags = TextUtil.getHashTags(commentText);

			// feedDetailDto.setCommentText(commentText);

			if (lovedit != 0) {
				new CamPhotoUploadFileAsyncTask(Ateit2_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, lovedit).execute();

			} else if (good != 0)

			{
				new CamPhotoUploadFileAsyncTask(Ateit2_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, good).execute();
			} else if (itsok != 0) {
				new CamPhotoUploadFileAsyncTask(Ateit2_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, itsok).execute();
			} else {
				new CamPhotoUploadFileAsyncTask(Ateit2_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, yuck).execute();
			}
		} else {
			new SvaadDialogs().showSvaadLoginAlertDialog(Ateit2_activity.this,
					null, Constants.RES_MODE, Constants.Cam_ate,
					Constants.ATE_IT);
		}
	}

	private void uploadFileAddDish() {

		// String screenName = null;

		String userId = Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

		String resObjectId = null, respageObjectId = null, dishname = null;

		if (restaurantDetail != null) {
			resObjectId = restaurantDetail.getObjectId();
		}

		if (resaurantPageDetail != null) {
			respageObjectId = resaurantPageDetail.getObjectId();
		}

		if (dishDetail != null) {
			dishname = dishDetail.getDishName();
		}
		if (userId != null && userId.length() > 0) {

			String commentText = commentbox.getText().toString();

			// if (commentText != null && commentText.length() > 0) {

			List<String> usertags = TextUtil.getHashTags(commentText);

			// feedDetailDto.setCommentText(commentText);

			if (lovedit != 0) {
				new CamPhotoAddDishUploadFileAsyncTask(Ateit2_activity.this,
						path, dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, lovedit, resObjectId,
						respageObjectId, dishname,nonveg).execute();

			} else if (good != 0)

			{
				new CamPhotoAddDishUploadFileAsyncTask(Ateit2_activity.this,
						path, dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, good, resObjectId,
						respageObjectId, dishname,nonveg).execute();
			} else if (itsok != 0) {
				new CamPhotoAddDishUploadFileAsyncTask(Ateit2_activity.this,
						path, dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, itsok, resObjectId,
						respageObjectId, dishname,nonveg).execute();
			} else {
				new CamPhotoAddDishUploadFileAsyncTask(Ateit2_activity.this,
						path, dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, yuck, resObjectId,
						respageObjectId, dishname,nonveg).execute();
			}
		} else {
			new SvaadDialogs().showSvaadLoginAlertDialog(Ateit2_activity.this,
					null, Constants.RES_MODE, Constants.Cam_ate,
					Constants.ATE_IT);
		}
	}

}
