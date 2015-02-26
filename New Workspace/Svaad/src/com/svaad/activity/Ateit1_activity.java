package com.svaad.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.LocationDto;
import com.svaad.adapter.CamTabAdapter;
import com.svaad.asynctask.CamPhotoUploadFileAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.TextUtil;
import com.svaad.utils.Utils;

@SuppressLint("NewApi")
public class Ateit1_activity extends BaseActivity implements OnClickListener {
	ImageView iv, image;
	String path;
	RelativeLayout listRL;
	int lovedit, good, itsok, yuck;
	private ActionBarActivity actionBarActivity;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	int height, width;
	private android.app.ActionBar actionBar;
	private CamTabAdapter adapter;
	private final String[] TITLES = { "Search", "History" };
	FragmentTransaction fragmentTransaction;
	ImageButton btnBack;
	private String queryText;
	int mode;
	private EditText editext, commentbox;
	int currentPage;
	private RelativeLayout tvDishNameWrapper, rate, comment;
	private TextView tvDishName, tvLocationName, tvResName, tag, pb;
	private CamRestaurantDetailDto restaurantDetail;
	private CamDishesDeatailDto dishDetail;
	private Button rest_tag, buttonshare;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ateit2_layout);

		actionBar = getActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);

		LayoutInflater li = LayoutInflater.from(Ateit1_activity.this);
		View customView = li.inflate(R.layout.share_layout, null);
		buttonshare = (Button) customView.findViewById(R.id.buttonshare);
		actionBar.setCustomView(customView);
		actionBar.setDisplayShowCustomEnabled(true);

		iv = (ImageView) findViewById(R.id.imageView1);
		listRL = (RelativeLayout) findViewById(R.id.listRL);
		comment = (RelativeLayout) findViewById(R.id.comment);
		rate = (RelativeLayout) findViewById(R.id.rate);
		editext = (EditText) findViewById(R.id.editTextRes);
		// editTextRes=(EditText)findViewById(R.id.editTextRes);
		commentbox = (EditText) findViewById(R.id.editText1);
		commentbox.setCursorVisible(true);
		image = (ImageView) findViewById(R.id.image);

		rest_tag = (Button) findViewById(R.id.rest_tag);
		tvDishNameWrapper = (RelativeLayout) findViewById(R.id.tvDishNameWrapper);
		tag = (TextView) findViewById(R.id.tag);
		tvDishName = (TextView) findViewById(R.id.tvDishName);
		tvResName = (TextView) findViewById(R.id.tvResName);
		tvLocationName = (TextView) findViewById(R.id.tvLocationName);
		pb = (TextView) findViewById(R.id.counttext);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		path = getIntent().getExtras().getString("path");
		restaurantDetail = (CamRestaurantDetailDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.CAM_RES_DATA) : null);

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
			rate.setVisibility(View.VISIBLE);
			rest_tag.setText("Edit");

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
					tvLocationName.setText(locationname);
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

		editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					in.hideSoftInputFromWindow(
							editext.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

					queryText = editext.getText().toString();
					if (queryText != null && queryText.length() > 0
							&& !queryText.equals("")) {

						// Intent i = new Intent(getActivity(),
						// SearchOverviewActivity.class);
						// i.putExtra(Constants.QUERYTEXT, queryText);
						// startActivity(i);

						setpager(queryText);
						return true;
					}
				}

				return false;
			}
		});

		// path = getIntent().getStringExtra("path");
		// if (path != null && path.length() > 0) {
		// FileInputStream fs = null;
		// Bitmap bm;
		// BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		// try {
		// fs = new FileInputStream(new File(path));
		// if (fs != null) {
		// bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
		// bfOptions);
		//
		// iv.setImageBitmap(bm);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (fs != null) {
		// try {
		// fs.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// // try{
		// // Bitmap bm = Utils.compressImage(path,
		// // this);
		// //
		// // iv.setImageBitmap(bm);
		// // }
		// // catch(Exception e)
		// // {
		// // e.printStackTrace();
		// // }
		//
		// } else
		// Toast.makeText(getApplicationContext(), "No Dish Image Selected",
		// Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("NewApi")
	public void restlist(View v) {
		TranslateAnimation ta = new TranslateAnimation(listRL.getScaleX(),
				listRL.getScaleX(), listRL.getScaleY() + 1000,
				listRL.getScaleY());
		ta.setDuration(500);

		listRL.startAnimation(ta);
		listRL.setVisibility(View.VISIBLE);
		setpager(queryText);
	}

	@Override
	public void onBackPressed() {
		if (listRL.getVisibility() == View.VISIBLE) {
			TranslateAnimation ta = new TranslateAnimation(listRL.getScaleX(),
					listRL.getScaleX(), listRL.getScaleY(),
					listRL.getScaleY() + 1000);
			ta.setDuration(500);
			listRL.startAnimation(ta);
			actionBar.show();
			listRL.setVisibility(View.INVISIBLE);
		} else {

			super.onBackPressed();
		}

	}

	@SuppressLint("NewApi")
	public void dishlist(View v) {
		TranslateAnimation ta = new TranslateAnimation(listRL.getScaleX(),
				listRL.getScaleX(), listRL.getScaleY() + 1000,
				listRL.getScaleY());
		ta.setDuration(500);
		listRL.startAnimation(ta);
		listRL.setVisibility(View.VISIBLE);
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

	public void setpager(String searchText) {

		if (searchText != null) {

			actionBar.hide();

			adapter = new CamTabAdapter(getSupportFragmentManager(), TITLES,
					searchText, path);

			pager.setAdapter(adapter);

			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			pager.setPageMargin(pageMargin);

			pager.setOffscreenPageLimit(5);

			tabs.setViewPager(pager);

			tabs.setBackgroundColor(getResources().getColor(
					R.color.tabbackground));
			tabs.setIndicatorHeight(10);

			tabs.setTextColor(getResources().getColor(R.color.tabtext));
			tabs.setShouldExpand(true);
			tabs.setIndicatorColor(getResources()
					.getColor(R.color.tabindicator));

		} else {
			actionBar.hide();

			adapter = new CamTabAdapter(getSupportFragmentManager(), TITLES,
					searchText, path);

			pager.setAdapter(adapter);

			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			pager.setPageMargin(pageMargin);

			pager.setOffscreenPageLimit(5);

			tabs.setViewPager(pager);

			tabs.setBackgroundColor(getResources().getColor(
					R.color.tabbackground));
			tabs.setIndicatorHeight(10);

			tabs.setTextColor(getResources().getColor(R.color.tabtext));
			tabs.setShouldExpand(true);
			tabs.setIndicatorColor(getResources()
					.getColor(R.color.tabindicator));
		}
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

	public void share(View v) {
		if (comment.getVisibility() == View.VISIBLE) {
			uploadFile();
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
		buttonshare.setBackgroundResource(R.drawable.shapeb);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnBack:

			if (listRL.getVisibility() == View.VISIBLE) {
				TranslateAnimation ta = new TranslateAnimation(
						listRL.getScaleX(), listRL.getScaleX(),
						listRL.getScaleY(), listRL.getScaleY() + 1000);
				ta.setDuration(500);
				listRL.startAnimation(ta);
				actionBar.show();
				listRL.setVisibility(View.INVISIBLE);
			} else {

				super.onBackPressed();
			}

			break;

		default:
			break;
		}
	}

	private void uploadFile() {

		// String screenName = null;

		String userId = Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
		if (userId != null && userId.length() > 0) {
			
			String branchid = null,dishname = null,branchpageId=null;

			String commentText = commentbox.getText().toString();

			// if (commentText != null && commentText.length() > 0) {

			List<String> usertags = TextUtil.getHashTags(commentText);

			// feedDetailDto.setCommentText(commentText);

			if (lovedit != 0) {
				new CamPhotoUploadFileAsyncTask(Ateit1_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, lovedit).execute();

			} else if (good != 0)

			{
				new CamPhotoUploadFileAsyncTask(Ateit1_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, good).execute();
			} else if (itsok != 0) {
				new CamPhotoUploadFileAsyncTask(Ateit1_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, itsok).execute();
			} else {
				new CamPhotoUploadFileAsyncTask(Ateit1_activity.this, path,
						dishDetail.getObjectId(), commentText, usertags,
						userId, height, width, yuck).execute();
			}
		}
		else
		{
		new SvaadDialogs().showSvaadLoginAlertDialog(
				Ateit1_activity.this, null, Constants.RES_MODE,
				Constants.Cam_ate, Constants.ATE_IT);
		}
	}

}
