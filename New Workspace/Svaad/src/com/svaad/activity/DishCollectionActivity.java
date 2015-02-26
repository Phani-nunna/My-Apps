package com.svaad.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.adapter.DishCollectionAdapter;

public class DishCollectionActivity extends BaseActivity {

	ActionBar actionbar;

	LinearLayout linearContainer;

	View view;

	private ListView listview;

	private View cv;

	private TextView name1, resname1;

	ImageView user;

	private String imagepath, name, resname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dishcollection_layout);

		init();

		LayoutInflater vi = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cv = vi.inflate(R.layout.dishlist_header, null);

		initHeaderView(cv);

		listview.addHeaderView(cv);

		actionbar = getActionBar();
		actionbar.hide();

		setSupportProgressBarIndeterminateVisibility(false);

		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		actionbar.setDisplayUseLogoEnabled(true);

		try {
			// Get the Bundle Object
			Bundle bundleObject = getIntent().getExtras();

			if (bundleObject != null) {

				name = bundleObject.getString("name");
				resname = bundleObject.getString("resname");
				imagepath = bundleObject.getString("imageurl");

				if (imagepath != null && imagepath.length() > 0) {
					Picasso.with(DishCollectionActivity.this).load(imagepath)
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile).into(user);

				} else {
					Picasso.with(DishCollectionActivity.this)
							.load(R.drawable.default_profile)
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile).into(user);
				}

				if (name != null && name.length() > 0) {
					actionbar.setTitle(name);
					name1.setText(name);
				}

				if (resname != null && resname.length() > 0) {
					actionbar.setSubtitle("for " + resname);
					resname1.setText("for " + resname);
				}
				// Get ArrayList Bundle
				List<FeedDetailDto> feedList = (ArrayList<FeedDetailDto>) bundleObject
						.getSerializable("key");

				if (feedList != null && feedList.size() > 0) {
					Collections.sort(feedList, new FeedComparator());

					listview.setAdapter(new DishCollectionAdapter(
							DishCollectionActivity.this, R.id.lv, feedList));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initHeaderView(View view) {

		name1 = (TextView) cv.findViewById(R.id.tvUserName1);
		resname1 = (TextView) cv.findViewById(R.id.tvResName1);
		user = (ImageView) cv.findViewById(R.id.imageView1);
		ImageButton back = (ImageButton) cv.findViewById(R.id.back2);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

	}

	public void init() {

		listview = (ListView) findViewById(R.id.lv);
	}

	class FeedComparator implements Comparator<FeedDetailDto> {
		@Override
		public int compare(FeedDetailDto a, FeedDetailDto b) {

			if (a.getDishTag() != null && b.getDishTag() != null
					&& a.getDishTag().length() > 0
					&& b.getDishTag().length() > 0) {
				return a.getDishTag().compareTo(b.getDishTag());
			} else {

			}
			return 0;
		}
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
