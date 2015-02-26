package com.svaad.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPhotoDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.Dto.LocationDto;
import com.svaad.activity.AteIt_activity;
import com.svaad.activity.DishProfileActivity;
import com.svaad.activity.UserProfileActivity;
import com.svaad.asynctask.LikeAsyncTask;
import com.svaad.asynctask.UnlikeAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class FeedAdapter extends ArrayAdapter<FeedDetailDto> {

	Context context;
	GridView grid;
	LayoutInflater inflater;

	List<FeedDetailDto> feedList;

	FeedDetailDto feed;

	List<String> likesList = new ArrayList<String>();
	List<String> wishlists = null;

	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());

	public FeedAdapter(Context context, int gridviewId, GridView grid,
			List<FeedDetailDto> feedList) {

		super(context, gridviewId, feedList);
		this.context = context;
		this.grid = grid;
		this.feedList = feedList;
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public FeedDetailDto getItem(int arg0) {
		return feedList.get(arg0);
	}

	public List<FeedDetailDto> getFeedDtos() {
		return feedList;
	}

	public void setFeedDtos(List<FeedDetailDto> feedList) {
		this.feedList = feedList;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup group) {
		final ViewHolder holder;

		feed = getItem(arg0);

		if (view == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.feedthumbnail, group, false);

			holder = new ViewHolder();
			
			
			holder.tvResNamew = (TextView) view.findViewById(R.id.tvResNamew);
			holder.tvLocationNamew = (TextView) view
					.findViewById(R.id.tvLocationNamew);

			holder.tvDishNamew = (TextView) view.findViewById(R.id.tvDishNamew);
			
			holder.withoutImageRl = (RelativeLayout) view
					.findViewById(R.id.withoutImageRl);
			holder.tvDishNameWrapper = (RelativeLayout) view
					.findViewById(R.id.tvDishNameWrapper);


			holder.dishImage = (ImageView) view.findViewById(R.id.imageView);
			holder.userImage = (ImageView) view.findViewById(R.id.userImage);

			holder.tvCommenttext = (TextView) view
					.findViewById(R.id.tvCommenttext);
			holder.tvDishTag = (TextView) view.findViewById(R.id.tvDishTag);

			holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
			holder.tvResName = (TextView) view.findViewById(R.id.tvResName);
			holder.tvLocationName = (TextView) view
					.findViewById(R.id.tvLocationName);

			holder.tvDishName = (TextView) view.findViewById(R.id.tvDishName);

			holder.btnAddToList = (Button) view.findViewById(R.id.btnAddTo);

			holder.btnAteIt = (Button) view.findViewById(R.id.btnAteIt);

			holder.userRl = (RelativeLayout) view.findViewById(R.id.userRl);
			
			
			holder.tvAteitCount=(TextView)view.findViewById(R.id.tvAteitCount);


			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String comment = feed.getCommentText();

		String dishPhotoImage = null;
		DishPhotoDto dishPhoto = feed.getDishPhoto();
		final BranchDishIdDto branchDishId = feed.getBranchDishId();
		FeedUserIdDto userId = feed.getUserId();
		DishIdDto dishIdDto = null;
		LocationDto locationDto = null;
		
		if (branchDishId != null) {

			long ateitCount = branchDishId.getComments();

			if (ateitCount != 0) {
				holder.tvAteitCount.setVisibility(View.VISIBLE);
				holder.tvAteitCount.setText(ateitCount + " people Ate it");
			} else {
				holder.tvAteitCount.setVisibility(View.GONE);

			}
		}

		if (comment != null && comment.length() > 0) {
			holder.tvCommenttext.setVisibility(View.VISIBLE);
			holder.tvCommenttext.setText(comment.toString().trim());
		} else {
			holder.tvCommenttext.setText(null);
			holder.tvCommenttext.setVisibility(View.GONE);
		}

		if (feed.getDishTag() != null && feed.getDishTag().length() > 0) {
			holder.tvDishTag.setVisibility(View.VISIBLE);

			String dishTags = feed.getDishTag();

			if (dishTags != null && dishTags.length() > 0) {
				Utils.setColorsToDishTags(dishTags, holder.tvDishTag);
			}
			// if(feed.getDishTag().equalsIgnoreCase("1"))
			// {
			// tvDishTag.setText("Loved it");
			// }
			// else if(feed.getDishTag().equalsIgnoreCase("2"))
			// {
			// tvDishTag.setText("Good");
			// }
			// else if(feed.getDishTag().equalsIgnoreCase("3"))
			// {
			// tvDishTag.setText("Its ok");
			// }
			// else if(feed.getDishTag().equalsIgnoreCase("4"))
			// {
			// tvDishTag.setText("Yuck");
			// }
		} else {
			holder.tvDishTag.setVisibility(View.GONE);
		}

		if (userId != null) {
			String uname = userId.getUname();

			

			if (uname != null && uname.length() > 0) {
				holder.tvUserName.setText(uname);
			}

			else {
				holder.tvUserName.setText(null);
			}
			if (userId.getDisplayPicUrl() != null
					&& userId.getDisplayPicUrl().length() > 0) {
				Picasso.with(context).load(userId.getDisplayPicUrl())
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(holder.userImage);

			} else {
				Picasso.with(context).load(R.drawable.default_profile)
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(holder.userImage);

			}

		} else {
			holder.tvUserName.setText(null);
			Picasso.with(context).load(R.drawable.default_profile)
					.placeholder(R.drawable.default_profile)
					.error(R.drawable.default_profile)

					.into(holder.userImage);

		}

		try {
			wishlists = (List<String>) ObjectSerializer
					.deserialize(sharedPreferences.getString(
							Constants.WISHLIST_ARRAY,
							ObjectSerializer.serialize(new ArrayList<String>())));

			if (wishlists != null && wishlists.size() > 0) {

				if (branchDishId != null) {

					String branchDishObjectId = branchDishId.getObjectId();
					if (branchDishObjectId != null
							&& branchDishObjectId.length() > 0) {
						if (wishlists.contains(branchDishObjectId)) {
							holder.btnAddToList.setText("Wished");
							holder.btnAddToList
									.setCompoundDrawablesWithIntrinsicBounds(
											R.drawable.hearto, 0, 0, 0);
						} else {
							holder.btnAddToList.setText("Wish it");
							holder.btnAddToList
									.setCompoundDrawablesWithIntrinsicBounds(
											R.drawable.heart, 0, 0, 0);
						}

					}

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (branchDishId != null) {

			dishIdDto = branchDishId.getDishId();

			locationDto = branchDishId.getLocation();

			String branchName = branchDishId.getBranchName();

			if (locationDto != null) {
				String locationName = locationDto.getName();
				if (locationName != null && locationName.length() > 0) {
					holder.tvLocationName.setText(locationName);
				}

				else {
					holder.tvLocationName.setText(null);
				}
			}

			else {
				holder.tvLocationName.setText(null);
			}

			if (dishIdDto != null) {
				String dishname = dishIdDto.getName();
				if (dishname != null && dishname.length() > 0) {
					holder.tvDishName.setText(dishname);
				} else {
					holder.tvDishName.setText(null);
				}
			} else {
				holder.tvDishName.setText(null);
			}

			if (branchName != null && branchName.length() > 0) {
				holder.tvResName.setText("@ " + branchName + " , ");

			}

			else {
				holder.tvResName.setText(null);
			}
		} else {
			holder.tvResName.setText(null);
			holder.tvDishName.setText(null);
			holder.tvLocationName.setText(null);
		}

		if (dishPhoto != null) {
			dishPhotoImage = dishPhoto.getUrl();
			if (dishPhotoImage != null && dishPhotoImage.length() > 0) {
				Picasso.with(context).load(dishPhotoImage)
						.placeholder(R.drawable.temp).error(R.drawable.temp)

						.into(holder.dishImage);
				
				holder.withoutImageRl.setVisibility(View.GONE);
				holder.tvDishNameWrapper.setVisibility(View.VISIBLE);
				holder.dishImage.setVisibility(View.VISIBLE);
			}

			else {

//				Picasso.with(context).load(R.drawable.temp)
//						.placeholder(R.drawable.temp).error(R.drawable.temp)
//
//						.into(holder.dishImage);
				
				
				holder.withoutImageRl.setVisibility(View.VISIBLE);
				holder.tvDishNameWrapper.setVisibility(View.GONE);
				holder.dishImage.setVisibility(View.GONE);

				String branchName = branchDishId.getBranchName();

				if (locationDto != null) {
					String locationName = locationDto.getName();
					if (locationName != null && locationName.length() > 0) {
						holder.tvLocationNamew.setText(locationName);
					}

					else {
						holder.tvLocationNamew.setText(null);
					}
				}

				else {
					holder.tvLocationNamew.setText(null);
				}

				if (dishIdDto != null) {
					String dishname = dishIdDto.getName();
					if (dishname != null && dishname.length() > 0) {
						holder.tvDishNamew.setText(dishname);
					} else {
						holder.tvDishNamew.setText(null);
					}
				}

				if (branchName != null && branchName.length() > 0) {
					holder.tvResNamew.setText("@ " + branchName + " , ");

				}

				else {
					holder.tvResNamew.setText(null);
				}

			}
		}

		if (dishPhoto == null) {

			if (branchDishId != null) {

				dishIdDto = branchDishId.getDishId();

				if (dishIdDto != null) {

					DishPicDto dishPicDto = dishIdDto.getDishPic();
					if (dishPicDto != null) {
						String dishPicUrl = dishPicDto.getUrl();
						if (dishPicUrl != null && dishPicUrl.length() > 0) {

							Picasso.with(context).load(dishPicUrl)
									.placeholder(R.drawable.temp)
									.error(R.drawable.temp)

									.into(holder.dishImage);
							
							holder.withoutImageRl.setVisibility(View.GONE);
							holder.tvDishNameWrapper
									.setVisibility(View.VISIBLE);
							holder.dishImage.setVisibility(View.VISIBLE);
						}

					} else {

//						Picasso.with(context).load(R.drawable.temp)
//								.placeholder(R.drawable.temp)
//								.error(R.drawable.temp)
//
//								.into(holder.dishImage);
						
						
						holder.withoutImageRl.setVisibility(View.VISIBLE);
						holder.tvDishNameWrapper.setVisibility(View.GONE);
						holder.dishImage.setVisibility(View.GONE);

						String branchName = branchDishId.getBranchName();

						if (locationDto != null) {
							String locationName = locationDto.getName();
							if (locationName != null
									&& locationName.length() > 0) {
								holder.tvLocationNamew.setText(locationName);
							}

							else {
								holder.tvLocationNamew.setText(null);
							}
						}

						else {
							holder.tvLocationNamew.setText(null);
						}

						if (dishIdDto != null) {
							String dishname = dishIdDto.getName();
							if (dishname != null && dishname.length() > 0) {
								holder.tvDishNamew.setText(dishname);
							} else {
								holder.tvDishNamew.setText(null);
							}
						}

						if (branchName != null && branchName.length() > 0) {
							holder.tvResNamew
									.setText("@ " + branchName + " , ");

						}

						else {
							holder.tvResNamew.setText(null);
						}
						
					}
				}
			}

		}
		holder.dishImage.setTag(feed);
		holder.userImage.setTag(feed);
		holder.btnAteIt.setTag(feed);
		holder.btnAddToList.setTag(feed);
		holder.tvCommenttext.setTag(feed);
		holder.tvAteitCount.setTag(feed);
		
		holder.withoutImageRl.setTag(feed);
		holder.withoutImageRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});
		
		holder.tvAteitCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.tvCommenttext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.dishImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.btnAteIt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
						&& Utils.getFromSharedPreference(
								Constants.USER_OBJECT_ID_KEY).length() > 0) {
					FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();
					Intent ateItIntent = new Intent(context,
							AteIt_activity.class);
					ateItIntent.putExtra(Constants.DATA, feedDetailDto);
					ateItIntent.putExtra(Constants.SVAAD, Constants.ATE_IT);
					context.startActivity(ateItIntent);
				} else {
					new SvaadDialogs().showSvaadLoginAlertDialog(context, null,
							Constants.RES_MODE,Constants.ME,Constants.ATE_IT);
				}
			}
		});
		holder.userImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();
				Intent userProfileIntent = new Intent(context,
						UserProfileActivity.class);

				userProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(userProfileIntent);

			}
		});

		holder.btnAddToList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();
				String text = holder.btnAddToList.getText().toString();

				if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
						&& Utils.getFromSharedPreference(
								Constants.USER_OBJECT_ID_KEY).length() > 0) {

					if (text != null && text.equalsIgnoreCase("Wish it")) {

						if (feedDetailDto != null) {
							BranchDishIdDto branchDishId = feedDetailDto
									.getBranchDishId();
							if (branchDishId != null) {
								String objectid = branchDishId.getObjectId();

								if (objectid != null && objectid.length() > 0) {

									try {
										wishlists = (List<String>) ObjectSerializer.deserialize(sharedPreferences
												.getString(
														Constants.WISHLIST_ARRAY,
														ObjectSerializer
																.serialize(new ArrayList<String>())));

										if (wishlists != null
												&& wishlists.size() > 0) {
											wishlists.add(objectid);

											try {

												Utils.saveToSharedPreferenceList(
														Constants.WISHLIST_ARRAY,
														wishlists);
												holder.btnAddToList
														.setText("Wished");
												holder.btnAddToList
														.setCompoundDrawablesWithIntrinsicBounds(
																R.drawable.hearto,
																0, 0, 0);

												// MeFragment m=new
												// MeFragment();
												// m.wishedTextView.setText("" +
												// wishlists.size()+ "");

												new LikeAsyncTask(context,
														null, feedDetailDto,Constants.ME)
														.execute();
											} catch (Exception e) {
												e.printStackTrace();
											}

										}

										else {
											likesList.add(objectid);

											if (likesList != null
													&& likesList.size() > 0) {

												try {
													holder.btnAddToList
															.setText("Wished");
													holder.btnAddToList
															.setCompoundDrawablesWithIntrinsicBounds(
																	R.drawable.hearto,
																	0, 0, 0);
													Utils.saveToSharedPreferenceList(
															Constants.WISHLIST_ARRAY,
															likesList);

													new LikeAsyncTask(context,
															null, feedDetailDto,Constants.ME)
															.execute();
												} catch (Exception e) {
													e.printStackTrace();
												}

											}

										}
									} catch (IOException e1) {
										e1.printStackTrace();
									}

								}
							}
						}
					} else {
						try {
							wishlists = (List<String>) ObjectSerializer.deserialize(sharedPreferences
									.getString(
											Constants.WISHLIST_ARRAY,
											ObjectSerializer
													.serialize(new ArrayList<String>())));
							if (wishlists != null && wishlists.size() > 0) {

								if (branchDishId != null) {

									String branchDishObjectId = branchDishId
											.getObjectId();
									if (branchDishObjectId != null
											&& branchDishObjectId.length() > 0) {
										for (int i = 0; i < wishlists.size(); i++) {
											String id = wishlists.get(i);
											if (id != null && id.length() > 0) {
												if (id.equals(branchDishObjectId)) {
													wishlists.remove(i);
													holder.btnAddToList
															.setText("Wish it");
													holder.btnAddToList
															.setCompoundDrawablesWithIntrinsicBounds(
																	R.drawable.heart,
																	0, 0, 0);
													Utils.saveToSharedPreferenceList(
															Constants.WISHLIST_ARRAY,
															wishlists);

													// MeFragment m=new
													// MeFragment();
													// m.wishedTextView.setText(""
													// + wishlists.size()+ "");

													new UnlikeAsyncTask(
															context, null,
															branchDishObjectId,Constants.ME,feedDetailDto)
															.execute();

												} else {
													holder.btnAddToList
															.setText("Wished");
													holder.btnAddToList
															.setCompoundDrawablesWithIntrinsicBounds(
																	R.drawable.hearto,
																	0, 0, 0);
												}
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				else {
					new SvaadDialogs().showSvaadLoginAlertDialog(context, null,
							Constants.RES_MODE,Constants.ME,Constants.WISH_IT);
				}

			}
		});

		return view;
	}

	public class ViewHolder {
		private ImageView dishImage, userImage;
		private TextView tvCommenttext, tvDishName, tvUserName, tvResName,
				tvLocationName, tvDishTag,tvAteitCount, tvDishNamew,
				tvResNamew, tvLocationNamew;
		private Button btnAddToList, btnAteIt;

		private RelativeLayout userRl, withoutImageRl, tvDishNameWrapper;;
	}

}
