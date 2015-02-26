package com.svaad.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.AllFoodiesDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.activity.UserProfileActivity;
import com.svaad.asynctask.FollowAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class AllFoodiesListAdapter extends ArrayAdapter<AllFoodiesDto>
		implements OnClickListener {

	private List<AllFoodiesDto> foodiesDtos;
	private Context context;
	private SharedPreferences sharedPreferences;

	public AllFoodiesListAdapter(Context context, int resource,
			List<AllFoodiesDto> foodiesDtos) {
		super(context, resource, foodiesDtos);
		this.context = context;
		this.foodiesDtos = foodiesDtos;
	}

	public List<AllFoodiesDto> getFoodiesDtos() {
		return foodiesDtos;
	}

	public void setFoodiesDtos(List<AllFoodiesDto> foodiesDtos) {
		this.foodiesDtos = foodiesDtos;
	}

	@Override
	public int getCount() {
		return foodiesDtos.size();
	}

	@Override
	public AllFoodiesDto getItem(int position) {
		return foodiesDtos.get(position);
	}

	public class ViewHolder {
		private ImageView imageAllFoodies;
		private TextView tvAllFoodiesName, tvFollowers;
		private Button btnFollow;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();

		holder.imageAllFoodies = (ImageView) convertView
				.findViewById(R.id.imageAllFoodies);
		holder.tvAllFoodiesName = (TextView) convertView
				.findViewById(R.id.tvAllFoodiesName);

		holder.tvFollowers = (TextView) convertView
				.findViewById(R.id.tvFollowers);
		holder.btnFollow = (Button) convertView.findViewById(R.id.btnFollow);
		holder.btnFollow.setOnClickListener(this);
		return holder;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final AllFoodiesDto allFoodiesDto = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.allfoodies_row, parent,
					false);
			holder = initHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvAllFoodiesName
				.setText((allFoodiesDto.getUname() != null ? allFoodiesDto
						.getUname() : ""));

		holder.tvFollowers.setText(allFoodiesDto.getFollowerCount()
				+ " Followers");

		String url = "";

		if (allFoodiesDto.getDisplayPicUrl() != null
				&& allFoodiesDto.getDisplayPicUrl().length() > 0) {
			url = allFoodiesDto.getDisplayPicUrl()+"?width=100&height=100";

			// if (url != null && url.startsWith("https://"))
			// url = url.replace("https://", "http://");
			if (url != null && url.length() > 0) {
				Picasso.with(context).load(url)
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(holder.imageAllFoodies);
			} else {
				
				Picasso.with(context).load(R.drawable.default_profile)
				.placeholder(R.drawable.default_profile)
				.error(R.drawable.default_profile)

				.into(holder.imageAllFoodies);

			}
		}
		else
		{
			Picasso.with(context).load(R.drawable.default_profile)
			.placeholder(R.drawable.default_profile)
			.error(R.drawable.default_profile)

			.into(holder.imageAllFoodies);
		}

		holder.imageAllFoodies.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AllFoodiesDto allFoodiesDto = (AllFoodiesDto) arg0.getTag();

				String userObjectId = allFoodiesDto.getObjectId();
				long suggestCount = allFoodiesDto.getSuggestCount();
				long wishlistCount = allFoodiesDto.getWishlistCount();
				String uname = allFoodiesDto.getUname();
				String imageUrl = allFoodiesDto.getDisplayPicUrl();
				int foollowerCount = allFoodiesDto.getFollowerCount();
				List<String> followingUsers = allFoodiesDto.getWishlistArr();

				FeedDetailDto feedDetailDto = new FeedDetailDto();
				FeedUserIdDto feedUserIdDto = new FeedUserIdDto();
				if (userObjectId != null && userObjectId.length() > 0) {
					feedUserIdDto.setObjectId(userObjectId);
				}
				feedUserIdDto.setSuggestCount(suggestCount);
				feedUserIdDto.setWishlistCount(wishlistCount);

				if (uname != null && uname.length() > 0) {
					feedUserIdDto.setUname(uname);
				}

				if (imageUrl != null && imageUrl.length() > 0) {
					feedUserIdDto.setDisplayPicUrl(imageUrl);
				}

				feedUserIdDto.setFollowerCount(foollowerCount);
				feedUserIdDto.setWishlistArr(followingUsers);
				feedDetailDto.setUserId(feedUserIdDto);

				Intent userProfileIntent = new Intent(context,
						UserProfileActivity.class);

				userProfileIntent.putExtra(Constants.DATA, feedDetailDto);
				// userProfileIntent.putExtra("me", "me");
				context.startActivity(userProfileIntent);

			}
		});
		holder.imageAllFoodies.setTag(allFoodiesDto);

		List<String> followingUsers = null;
		// final LogInResponseDto userInfo = SvaadApplication.getInstance()
		// .getLoginUserInfo();
		// if (userInfo != null && userInfo.getFollowingUsersCount() > 0) {
		// followingUsers = SvaadApplication.getInstance().getLoginUserInfo()
		// .getFollowingUsers();
		//
		// boolean foollow = followingUsers.contains(allFoodiesDto
		// .getObjectId());
		// if (foollow == true) {
		// holder.btnFollow.setText("Following");
		// holder.btnFollow.setClickable(false);
		// holder.btnFollow
		// .setBackgroundColor(Color.parseColor("#4cae4c"));
		//
		// } else {
		// holder.btnFollow.setText("Follow");
		// holder.btnFollow
		// .setBackgroundColor(Color.parseColor("#357ebd"));
		// }
		//
		// }

		try {
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SvaadApplication.getInstance()
							.getApplicationContext());

			followingUsers = (List<String>) ObjectSerializer
					.deserialize(sharedPreferences.getString(
							Constants.FOLLOWING_USERS,
							ObjectSerializer.serialize(new ArrayList<String>())));

			if (followingUsers != null && followingUsers.size() > 0) {

				boolean follow = followingUsers.contains(allFoodiesDto
						.getObjectId());
				if (follow == true)
				{

					holder.btnFollow.setText("Following");
					holder.btnFollow.setClickable(false);
					holder.btnFollow.setBackgroundColor(Color
							.parseColor("#4cae4c"));
				} 
				else 
				{

					holder.btnFollow.setText("Follow");
					holder.btnFollow.setBackgroundColor(Color
							.parseColor("#357ebd"));

				}
			}
			else
			{
				holder.btnFollow.setText("Follow");
				holder.btnFollow.setBackgroundColor(Color
						.parseColor("#357ebd"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		holder.btnFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * String follower= allFoodiesDto.getObjectId(); String
				 * followingUser=userInfo.getObjectId();
				 */

				if (holder.btnFollow.getText().toString()
						.equalsIgnoreCase("Following")) {
					holder.btnFollow.setClickable(false);
					holder.btnFollow.setBackgroundColor(Color
							.parseColor("#4cae4c"));
				} else {
					String loginUserId = Utils
							.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

					if (loginUserId != null && loginUserId.length() > 0) {
						
						holder.btnFollow.setText("Following");
						holder.btnFollow.setClickable(false);
						holder.btnFollow.setBackgroundColor(Color.parseColor("#4cae4c"));
						new FollowAsyncTask(context, holder.btnFollow,
								loginUserId, allFoodiesDto.getObjectId())
								.execute();
					} else {
						new SvaadDialogs().showSvaadLoginAlertDialog(context,
								null, 100,Constants.FOODIES,Constants.FOLLOW_ACTION);
					}
				}
			}
		});
		// {
		// new
		// FollowAsyncTask(context,holder.btnFollow,userInfo.getObjectId(),allFoodiesDto.getObjectId()).execute();
		// }

		return convertView;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnFollow:

			break;

		case R.id.imageAllFoodies:

			break;

		default:
			break;
		}

	}

}
