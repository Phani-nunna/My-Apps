package com.svaad.databaseDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.CatIdDto;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPhotoDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PlaceDto;
import com.svaad.Dto.PointDto;
import com.svaad.database.DatabaseHelper;
import com.svaad.databaseDto.BranchIdDatabaseDto;
import com.svaad.databaseDto.BranchMenuDto;
import com.svaad.responseDto.LogInResponseDto;
import com.svaad.utils.Constants;

public class DatabaseDAO {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public DatabaseDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	// Close the db
	public void close() {
		db.close();
	}

	public void createResHistoryTable(
			CamRestaurantDetailDto camRestaurantDetailDto) {
		ContentValues contentValues = new ContentValues();
		if (camRestaurantDetailDto.getObjectId() != null
				&& camRestaurantDetailDto.getObjectId().length() > 0) {

			contentValues.put(DatabaseHelper.KEY_RES_BRANCHID,
					camRestaurantDetailDto.getObjectId());
		}

		if (camRestaurantDetailDto.getBranchName() != null
				&& camRestaurantDetailDto.getBranchName().length() > 0) {

			contentValues.put(DatabaseHelper.KEY_RES_BRANCHNAME,
					camRestaurantDetailDto.getBranchName());
		}

		if (camRestaurantDetailDto.getLocation() != null) {

			if (camRestaurantDetailDto.getLocation().getName() != null
					&& camRestaurantDetailDto.getLocation().getName().length() > 0) {
				contentValues.put(DatabaseHelper.KEY_RES_RESNAME,
						camRestaurantDetailDto.getLocation().getName());
			}

		}
		db.insert(DatabaseHelper.TABLE_RESHISTORY, null, contentValues);
	}

	public List<CamRestaurantDetailDto> getResHistoryTable() {

		List<CamRestaurantDetailDto> resList = new ArrayList<CamRestaurantDetailDto>();

		// Name of the columns we want to select
		String[] tableColumns = new String[] { DatabaseHelper.KEY_RES_BRANCHID,
				DatabaseHelper.KEY_RES_BRANCHNAME,
				DatabaseHelper.KEY_RES_RESNAME };

		// Query the database
		Cursor cursor = db.query(DatabaseHelper.TABLE_RESHISTORY, tableColumns,
				null, null, null, null, null);
		cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			CamRestaurantDetailDto res = new CamRestaurantDetailDto();
			// Take values from the DB
			res.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.KEY_RES_BRANCHID)));
			res.setBranchName(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.KEY_RES_BRANCHNAME)));

			LocationDto location = new LocationDto();
			location.setName(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.KEY_RES_RESNAME)));

			res.setLocation(location);
			// Add to the DB
			resList.add(res);

			// Move to the next result
			cursor.moveToNext();
		}

		return resList;

	}

	public void createProfileTable(LogInResponseDto loginResponseDto) {
		ContentValues contentValues = new ContentValues();
		if (loginResponseDto.getEmail() != null
				&& loginResponseDto.getEmail().length() > 0) {

			contentValues.put(DatabaseHelper.PROFILE_USEREMAIL,
					loginResponseDto.getEmail());
		}

		if (loginResponseDto.getFbId() != null
				&& loginResponseDto.getFbId().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USERFBID,
					loginResponseDto.getFbId());
		}

		if (loginResponseDto.getUsername() != null
				&& loginResponseDto.getUsername().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USERNAME,
					loginResponseDto.getUsername());
		}

		if (loginResponseDto.getObjectId() != null
				&& loginResponseDto.getObjectId().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USEROBJECTID,
					loginResponseDto.getObjectId());
		}

		if (loginResponseDto.getDisplayPicUrl() != null
				&& loginResponseDto.getDisplayPicUrl().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USERPIC,
					loginResponseDto.getDisplayPicUrl());
		}

		if (loginResponseDto.getUserRole() != null
				&& loginResponseDto.getUserRole().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USERROLE,
					loginResponseDto.getUserRole());
		}

		if (loginResponseDto.getUname() != null
				&& loginResponseDto.getUname().length() > 0) {
			contentValues.put(DatabaseHelper.PROFILE_USERUNAME,
					loginResponseDto.getUname());
		}

		// Insert into DB
		db.insert(DatabaseHelper.TABLE_PROFILE, null, contentValues);

	}

	public List<LogInResponseDto> getProfileTable() {

		List<LogInResponseDto> loginResponseList = new ArrayList<LogInResponseDto>();

		// Name of the columns we want to select
		String[] tableColumns = new String[] { DatabaseHelper.PROFILE_USERNAME,
				DatabaseHelper.PROFILE_USEREMAIL,
				DatabaseHelper.PROFILE_USERPIC,
				DatabaseHelper.PROFILE_USEROBJECTID,
				DatabaseHelper.PROFILE_USERFBID,
				DatabaseHelper.PROFILE_USERUNAME,
				DatabaseHelper.PROFILE_USERROLE };

		// Query the database
		Cursor cursor = db.query(DatabaseHelper.TABLE_PROFILE, tableColumns,
				null, null, null, null, null);
		cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			LogInResponseDto login = new LogInResponseDto();
			// Take values from the DB

			login.setUsername(cursor.getString(0));
			login.setEmail(cursor.getString(1));
			login.setDisplayPicUrl(cursor.getString(2));
			login.setObjectId(cursor.getString(3));
			login.setFbId(cursor.getString(4));
			login.setUname(cursor.getString(5));
			login.setUserRole(cursor.getString(6));

			// Add to the DB
			loginResponseList.add(login);

			// Move to the next result
			cursor.moveToNext();
		}

		return loginResponseList;

	}

	public void insertSocialMenu(List<FeedDetailDto> feedDetailDtoList) {

		ContentValues contentValues = null;
		for (int i = 0; i < feedDetailDtoList.size(); i++) {
			contentValues = new ContentValues();
			if (feedDetailDtoList != null) {
				if (feedDetailDtoList.get(i).getObjectId() != null
						&& feedDetailDtoList.get(i).getObjectId().length() > 0) {
					contentValues.put(DatabaseHelper.SOCIALMENU_OBJECTID,
							feedDetailDtoList.get(i).getObjectId());
				}

				if (feedDetailDtoList.get(i).getCommentText() != null
						&& feedDetailDtoList.get(i).getCommentText().length() > 0) {
					contentValues.put(DatabaseHelper.SOCIALMENU_COMMENTTEXT,
							feedDetailDtoList.get(i).getCommentText());
				}

				if (feedDetailDtoList.get(i).getCreatedAt() != null
						&& feedDetailDtoList.get(i).getCreatedAt().length() > 0) {
					contentValues.put(DatabaseHelper.SOCIALMENU_CREATEDAT,
							feedDetailDtoList.get(i).getCreatedAt());
				}

				FeedUserIdDto userId = feedDetailDtoList.get(i).getUserId();
				if (userId != null) {
					if (userId.getObjectId() != null
							&& userId.getObjectId().length() > 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_USERID,
								userId.getObjectId());
					}
					if (userId.getUname() != null
							&& userId.getUname().length() > 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_UNAME,
								userId.getUname());
					}
					if (userId.getDisplayPicUrl() != null
							&& userId.getDisplayPicUrl().length() > 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_USERPIC,
								userId.getDisplayPicUrl());
					} else {
						contentValues.put(DatabaseHelper.SOCIALMENU_USERPIC,
								Constants.TEMPPIC);
					}
				}

				DishPhotoDto dishPhoto = feedDetailDtoList.get(i)
						.getDishPhotoThumbnail();
				if (dishPhoto != null) {
					if (dishPhoto != null && dishPhoto.getUrl().length() > 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_DISHPHOTO,
								dishPhoto.getUrl());
					} else {
						contentValues.put(DatabaseHelper.SOCIALMENU_DISHPHOTO,
								Constants.TEMPPIC);
					}
				}

				BranchDishIdDto branchDishId = feedDetailDtoList.get(i)
						.getBranchDishId();

				if (branchDishId != null) {
					DishIdDto dishId = branchDishId.getDishId();
					if (dishId != null) {
						if (dishId.getObjectId() != null
								&& dishId.getObjectId().length() > 0) {
							contentValues.put(DatabaseHelper.SOCIALMENU_DISHID,
									dishId.getObjectId());
						}

						DishPicDto dishpic = dishId.getDishPic();

						if (dishpic != null) {

							if (dishpic.getUrl() != null
									&& dishpic.getUrl().length() > 0) {
								contentValues.put(
										DatabaseHelper.SOCIALMENU_DISHPIC,
										dishpic.getUrl());
							} else {
								contentValues.put(
										DatabaseHelper.SOCIALMENU_DISHPIC,
										Constants.TEMPPIC);
							}
						}
						if (dishId.getName() != null
								&& dishId.getName().length() > 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_DISHNAME,
									dishId.getName());
						}
					}

					if (branchDishId.getObjectId() != null
							&& branchDishId.getObjectId().length() > 0) {
						contentValues.put(
								DatabaseHelper.SOCIALMENU_BRANCHDISHID,
								branchDishId.getObjectId());
					}

					BranchIdDto branchId = branchDishId.getBranchId();
					if (branchId != null) {
						if (branchId.getObjectId() != null
								&& branchId.getObjectId().length() > 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_BRANCHID,
									branchId.getObjectId());
						}
					}

					CityDto cityId = branchDishId.getCity();
					if (cityId != null) {
						if (cityId.getObjectId() != null
								&& cityId.getObjectId().length() > 0) {
							contentValues.put(DatabaseHelper.SOCIALMENU_CITYID,
									cityId.getObjectId());
						}
					}

					PointDto pointId = branchDishId.getPoint();
					if (pointId != null) {
						if (pointId.getLatitude() != 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_POINTLATITUDE,
									pointId.getLatitude());
						} else {
							contentValues
									.put(DatabaseHelper.SOCIALMENU_POINTLATITUDE,
											"");
						}
						if (pointId.getLongitude() != 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_POINTLONGITUDE,
									pointId.getLongitude());
						} else {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_POINTLONGITUDE,
									"");
						}

					}

					LocationDto locationId = branchDishId.getLocation();
					if (locationId != null) {
						if (locationId.getObjectId() != null
								&& locationId.getObjectId().length() > 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_LOCATIONID,
									locationId.getObjectId());
						}
					}

					PlaceDto placeId = branchDishId.getPlace();
					if (placeId != null) {
						if (placeId.getObjectId() != null
								&& placeId.getObjectId().length() > 0) {
							contentValues.put(
									DatabaseHelper.SOCIALMENU_PLACEID,
									placeId.getObjectId());
						}
					}

					if (branchDishId.getRegular() != 0) {
						contentValues.put(
								DatabaseHelper.SOCIALMENU_REGULARPRICE,
								branchDishId.getRegular());
					} else {
						contentValues.put(
								DatabaseHelper.SOCIALMENU_REGULARPRICE, "NA");
					}

					// if (branchDishId.getOneTag() != null
					// && branchDishId.getOneTag().length() > 0) {
					// contentValues.put(DatabaseHelper.SOCIALMENU_ONETAG,
					// branchDishId.getOneTag());
					// } else {
					// contentValues
					// .put(DatabaseHelper.SOCIALMENU_ONETAG, "0");
					// }
					//
					// if (branchDishId.getTwoTag() != null
					// && branchDishId.getTwoTag().length() > 0) {
					// contentValues.put(DatabaseHelper.SOCIALMENU_TWOTAG,
					// branchDishId.getTwoTag());
					// } else {
					// contentValues
					// .put(DatabaseHelper.SOCIALMENU_TWOTAG, "0");
					// }
					//
					// if (branchDishId.getThreeTag() != null
					// && branchDishId.getThreeTag().length() > 0) {
					// contentValues.put(DatabaseHelper.SOCIALMENU_THREETAG,
					// branchDishId.getThreeTag());
					// } else {
					// contentValues.put(DatabaseHelper.SOCIALMENU_THREETAG,
					// "0");
					// }
					//
					// if (branchDishId.getFourTag() != null
					// && branchDishId.getFourTag().length() > 0) {
					// contentValues.put(DatabaseHelper.SOCIALMENU_FOURTAG,
					// branchDishId.getFourTag());
					// } else {
					// contentValues.put(DatabaseHelper.SOCIALMENU_FOURTAG,
					// "0");
					// }

					if (branchDishId.getOneTag() != 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_ONETAG,
								branchDishId.getOneTag());
					} else {
						contentValues.put(DatabaseHelper.SOCIALMENU_ONETAG, 0);
					}

					if (branchDishId.getTwoTag() != 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_TWOTAG,
								branchDishId.getTwoTag());
					} else {
						contentValues.put(DatabaseHelper.SOCIALMENU_TWOTAG, 0);
					}

					if (branchDishId.getThreeTag() != 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_THREETAG,
								branchDishId.getThreeTag());
					} else {
						contentValues
								.put(DatabaseHelper.SOCIALMENU_THREETAG, 0);
					}

					if (branchDishId.getFourTag() != 0) {
						contentValues.put(DatabaseHelper.SOCIALMENU_FOURTAG,
								branchDishId.getFourTag());
					} else {
						contentValues.put(DatabaseHelper.SOCIALMENU_FOURTAG, 0);
					}

					// if (branchDishId.getDishTag() != null
					// && branchDishId.getDishTag().length() > 0) {
					// contentValues.put(DatabaseHelper.SOCIALMENU_DISHTAG,
					// branchDishId.getDishTag());
					// } else {
					// contentValues.put(DatabaseHelper.SOCIALMENU_DISHTAG,
					// "0");
					// }

				}
			}

			db.insert(DatabaseHelper.TABLE_SOCIALMENU, null, contentValues);
			close();
		}

	}

	public List<FeedDetailDto> getSocialMenusList() {

		// List<FeedDetailDto> socialMenusLists = new
		// ArrayList<FeedDetailDto>();

		// Name of the columns we want to select
		// String[] tableColumns = new String[] {
		// DatabaseHelper.SOCIALMENU_OBJECTID,DatabaseHelper. };
		// Query the database
		Cursor cursor = db.query(DatabaseHelper.TABLE_SOCIALMENU, null, null,
				null, null, null, null);

		List<FeedDetailDto> socialMenusLists = setCursorValues(cursor);

		return socialMenusLists;

	}

	public List<FeedDetailDto> getBranchIdSocialMenusList(String branchId) {

		String where = DatabaseHelper.SOCIALMENU_BRANCHID + "=?";
		String[] whereArgs = new String[] { branchId + "" };
		// Query the database
		Cursor cursor = db.query(DatabaseHelper.TABLE_SOCIALMENU, null, where,
				whereArgs, null, null, null);
		List<FeedDetailDto> socialMenusLists = setCursorValues(cursor);

		return socialMenusLists;

	}

	public List<FeedDetailDto> setCursorValues(Cursor cursor) {
		List<FeedDetailDto> socialMenusLists = new ArrayList<FeedDetailDto>();
		cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			FeedDetailDto feed = new FeedDetailDto();
			// Take values from the DB
			feed.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_OBJECTID)));

			FeedUserIdDto feedUserId = new FeedUserIdDto();
			feedUserId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_USERID)));
			feedUserId.setUname(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_UNAME)));
			feedUserId.setDisplayPicUrl(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_USERPIC)));
			feed.setUserId(feedUserId);

			DishPhotoDto dishPhoto = new DishPhotoDto();
			dishPhoto.setUrl(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_DISHPHOTO)));
			feed.setDishPhotoThumbnail(dishPhoto);

			DishPicDto dishPic = new DishPicDto();
			dishPic.setUrl(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_DISHPIC)));

			BranchIdDto branchId = new BranchIdDto();
			branchId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_BRANCHID)));

			BranchDishIdDto branchDishId = new BranchDishIdDto();
			branchDishId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_BRANCHDISHID)));
			DishIdDto dishId = new DishIdDto();
			dishId.setDishPic(dishPic);
			dishId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_DISHID)));
			dishId.setName(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_DISHNAME)));

			CityDto citId = new CityDto();
			citId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_CITYID)));

			PointDto point = new PointDto();
			point.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_POINTLATITUDE)));
			point.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_POINTLONGITUDE)));

			LocationDto locationid = new LocationDto();
			locationid.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_LOCATIONID)));

			PlaceDto placId = new PlaceDto();
			placId.setObjectId(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_PLACEID)));

			branchDishId.setBranchId(branchId);
			branchDishId.setDishId(dishId);
			branchDishId.setCity(citId);
			branchDishId.setPoint(point);
			branchDishId.setLocation(locationid);
			branchDishId.setPlace(placId);

			branchDishId.setRegular(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_REGULARPRICE)));
			// branchDishId.setOneTag(cursor.getString(cursor
			// .getColumnIndex(DatabaseHelper.SOCIALMENU_ONETAG)));
			// branchDishId.setTwoTag(cursor.getString(cursor
			// .getColumnIndex(DatabaseHelper.SOCIALMENU_TWOTAG)));
			// branchDishId.setThreeTag(cursor.getString(cursor
			// .getColumnIndex(DatabaseHelper.SOCIALMENU_THREETAG)));
			// branchDishId.setFourTag(cursor.getString(cursor
			// .getColumnIndex(DatabaseHelper.SOCIALMENU_FOURTAG)));

			branchDishId.setOneTag(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_ONETAG)));
			branchDishId.setTwoTag(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_TWOTAG)));
			branchDishId.setThreeTag(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_THREETAG)));
			branchDishId.setFourTag(cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_FOURTAG)));

			// branchDishId.setDishTag(cursor.getString(cursor
			// .getColumnIndex(DatabaseHelper.SOCIALMENU_DISHTAG)));

			feed.setBranchDishId(branchDishId);

			feed.setCommentText(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.SOCIALMENU_COMMENTTEXT)));

			// Add to the DB
			socialMenusLists.add(feed);

			// Move to the next result
			cursor.moveToNext();
		}
		return socialMenusLists;
	}

	// Insert each restaurant menu i BranchMenu table

	public void insertBranchMenu(List<FeedDetailDto> dishDetailsList) {

		ContentValues contentValues = null;
		for (int i = 0; i < dishDetailsList.size(); i++) {
			contentValues = new ContentValues();

			if (dishDetailsList.get(i).getBranchId() != null)

			{
				if (dishDetailsList.get(i).getBranchId().getObjectId() != null
						&& dishDetailsList.get(i).getBranchId().getObjectId()
								.length() > 0) {

					contentValues.put("branchId", dishDetailsList.get(i)
							.getBranchId().getObjectId());
				}
			}

			if (dishDetailsList.get(i).getBranchName() != null
					&& dishDetailsList.get(i).getBranchName().length() > 0) {
				contentValues.put("branchName", dishDetailsList.get(i)
						.getBranchName());
			}
			if (dishDetailsList.get(i).getObjectId() != null
					&& dishDetailsList.get(i).getObjectId().length() > 0) {
				contentValues.put("objectId", dishDetailsList.get(i)
						.getObjectId());
			}
			if (dishDetailsList.get(i).getDishId() != null) {
				if (dishDetailsList.get(i).getDishId().getObjectId() != null
						&& dishDetailsList.get(i).getDishId().getObjectId()
								.length() > 0) {
					contentValues.put("dishId", dishDetailsList.get(i)
							.getDishId().getObjectId());
				}
				if (dishDetailsList.get(i).getDishId().getName() != null
						&& dishDetailsList.get(i).getDishId().getName()
								.length() > 0) {
					contentValues.put("dishName", dishDetailsList.get(i)
							.getDishId().getName());
				}
				if (dishDetailsList.get(i).getDishId().getDesc() != null
						&& dishDetailsList.get(i).getDishId().getDesc()
								.length() > 0) {
					contentValues.put("dishDesc", dishDetailsList.get(i)
							.getDishId().getDesc());
				}
				if (dishDetailsList.get(i).getDishId().getTags() != null
						&& dishDetailsList.get(i).getDishId().getTags()
								.length() > 0) {
					contentValues.put("dishTags", dishDetailsList.get(i)
							.getDishId().getTags());
				}
			}

			DishIdDto disID = dishDetailsList.get(i).getDishId();
			if (disID != null) {
				DishPicDto coverpic = disID.getDishPic();
				if (coverpic != null) {
					String pic = coverpic.getUrl();
					if (pic != null) {

						contentValues.put("dishPic", pic);
					} else {
						contentValues
								.put("dishPic",
										"http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/41445399-cf7d-4667-8bfe-f46dac516737-temp.png");
					}
				} else {
					contentValues
							.put("dishPic",
									"http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/41445399-cf7d-4667-8bfe-f46dac516737-temp.png");
				}
			}
			contentValues.put("nonVeg", dishDetailsList.get(i).isNonVeg());
			contentValues.put("publish", dishDetailsList.get(i).isPublish());

			if (dishDetailsList.get(i).getPrice1() != 0) {
				contentValues.put("price1", dishDetailsList.get(i).getPrice1());
			}
			if (dishDetailsList.get(i).getPrice2() != 0) {
				contentValues.put("price2", dishDetailsList.get(i).getPrice2());
			}
			if (dishDetailsList.get(i).getPrice3() != 0) {
				contentValues.put("price3", dishDetailsList.get(i).getPrice3());
			}
			if (dishDetailsList.get(i).getPrice4() != 0) {
				contentValues.put("price4", dishDetailsList.get(i).getPrice4());
			}
			if (dishDetailsList.get(i).getPrice5() != 0) {
				contentValues.put("price5", dishDetailsList.get(i).getPrice5());
			}
			if (dishDetailsList.get(i).getPrice6() != 0) {
				contentValues.put("price6", dishDetailsList.get(i).getPrice6());
			}

			if (dishDetailsList.get(i).getCatId() != null) {
				if (dishDetailsList.get(i).getCatId().getObjectId() != null
						&& dishDetailsList.get(i).getCatId().getObjectId()
								.length() > 0) {

					contentValues.put("catId", dishDetailsList.get(i)
							.getCatId().getObjectId());
				}
				if (dishDetailsList.get(i).getCatId().getName() != null
						&& dishDetailsList.get(i).getCatId().getName().length() > 0) {
					contentValues.put("catName", dishDetailsList.get(i)
							.getCatId().getName());
				}
			}

			if (dishDetailsList.get(i).getPriceName1() != null
					&& dishDetailsList.get(i).getPriceName1().length() > 0) {
				contentValues.put("priceName1", dishDetailsList.get(i)
						.getPriceName1());
			}

			if (dishDetailsList.get(i).getPriceName2() != null
					&& dishDetailsList.get(i).getPriceName2().length() > 0) {
				contentValues.put("priceName2", dishDetailsList.get(i)
						.getPriceName2());
			}
			if (dishDetailsList.get(i).getPriceName3() != null
					&& dishDetailsList.get(i).getPriceName3().length() > 0) {
				contentValues.put("priceName3", dishDetailsList.get(i)
						.getPriceName3());
			}
			if (dishDetailsList.get(i).getPriceName4() != null
					&& dishDetailsList.get(i).getPriceName4().length() > 0) {
				contentValues.put("priceName4", dishDetailsList.get(i)
						.getPriceName4());
			}
			if (dishDetailsList.get(i).getPriceName5() != null
					&& dishDetailsList.get(i).getPriceName5().length() > 0) {
				contentValues.put("priceName5", dishDetailsList.get(i)
						.getPriceName5());
			}
			if (dishDetailsList.get(i).getPriceName6() != null
					&& dishDetailsList.get(i).getPriceName6().length() > 0) {
				contentValues.put("priceName6", dishDetailsList.get(i)
						.getPriceName6());
			}
			if (dishDetailsList.get(i).getRegular() != 0) {
				contentValues.put("regular", dishDetailsList.get(i)
						.getRegular());
			}
			if (dishDetailsList.get(i).getHomeDeliveryPrice() != null
					&& dishDetailsList.get(i).getHomeDeliveryPrice().length() > 0) {
				contentValues.put("homeDeliveryPrice", dishDetailsList.get(i)
						.getHomeDeliveryPrice());
			}
			if (dishDetailsList.get(i).getHappyHour() != null
					&& dishDetailsList.get(i).getHappyHour().length() > 0) {
				contentValues.put("happyHour", dishDetailsList.get(i)
						.getHappyHour());
			}
			if (dishDetailsList.get(i).getLikes() != 0) {
				contentValues.put("likes", dishDetailsList.get(i).getLikes());
			}
			if (dishDetailsList.get(i).getLocation() != null) {
				if (dishDetailsList.get(i).getLocation().getObjectId() != null
						&& dishDetailsList.get(i).getLocation().getObjectId()
								.length() > 0) {
					contentValues.put("locationID", dishDetailsList.get(i)
							.getLocation().getObjectId());
					contentValues.put("locationName", dishDetailsList.get(i)
							.getLocation().getName());
				}
			}
			if (dishDetailsList.get(i).getCity() != null) {
				if (dishDetailsList.get(i).getCity().getObjectId() != null
						&& dishDetailsList.get(i).getCity().getObjectId()
								.length() > 0) {
					contentValues.put("cityID", dishDetailsList.get(i)
							.getCity().getObjectId());
				}
			}
			if (dishDetailsList.get(i).getPoint() != null) {

				if (dishDetailsList.get(i).getPoint().getLatitude() != 0) {
					contentValues.put("latitude", dishDetailsList.get(i)
							.getPoint().getLatitude());
				}

				if (dishDetailsList.get(i).getPoint().getLongitude() != 0) {
					contentValues.put("longitude", dishDetailsList.get(i)
							.getPoint().getLongitude());
				}
			}
			if (dishDetailsList.get(i).getComments() != 0) {
				contentValues.put("comments", dishDetailsList.get(i)
						.getComments());
			}

			if (dishDetailsList.get(i).getOneTag() != 0) {
				contentValues.put("oneTag", dishDetailsList.get(i).getOneTag());
			} else {
				contentValues.put("oneTag", 0);
			}
			if (dishDetailsList.get(i).getTwoTag() != 0) {
				contentValues.put("twoTag", dishDetailsList.get(i).getTwoTag());
			} else {
				contentValues.put("twoTag", 0);
			}
			if (dishDetailsList.get(i).getThreeTag() != 0) {
				contentValues.put("threeTag", dishDetailsList.get(i)
						.getThreeTag());
			} else {
				contentValues.put("threeTag", 0);
			}
			if (dishDetailsList.get(i).getFourTag() != 0) {
				contentValues.put("fourTag", dishDetailsList.get(i)
						.getFourTag());
			} else {
				contentValues.put("fourTag", 0);
			}
			// Insert into DB
			db.insertWithOnConflict("branchmenu", null, contentValues,
					SQLiteDatabase.CONFLICT_IGNORE);

		}

	}

	public List<BranchMenuDto> getBranchMenu() {

		List<BranchMenuDto> branchMenusList = new ArrayList<BranchMenuDto>();

		// Name of the columns we want to select
		String[] tableColumns = new String[] { "branchId", "branchName",
				"objectId", "dishId", "dishName", "dishDesc", "dishTags",
				"dishPic", "nonVeg", "publish", "price1", "price2", "price3",
				"price4", "price5", "price6", "catId", "catName", "priceName1",
				"priceName2", "priceName3", "priceName4", "priceName5",
				"priceName6", "regular", "homeDeliveryPrice", "happyHour",
				"likes", "locationID", "cityID", "latitude", "longitude",
				"comments" };

		// Query the database
		Cursor cursor = db.query("branchmenu", tableColumns, null, null, null,
				null, null);
		cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			BranchMenuDto branch = new BranchMenuDto();
			// Take values from the DB

			branch.setBranchId(cursor.getString(0));
			branch.setBranchName(cursor.getString(1));
			branch.setObjectId(cursor.getString(2));
			branch.setDishId(cursor.getString(3));
			branch.setDishName(cursor.getString(4));
			branch.setDishDesc(cursor.getString(5));
			branch.setDishTags(cursor.getString(6));
			branch.setDishPic(cursor.getString(7));
			branch.setNonVeg(cursor.getString(8));
			branch.setPublish(cursor.getString(9));
			branch.setPrice1(cursor.getInt(10));
			branch.setPrice2(cursor.getInt(11));
			branch.setPrice3(cursor.getInt(12));
			branch.setPrice4(cursor.getInt(13));
			branch.setPrice5(cursor.getInt(14));
			branch.setPrice6(cursor.getInt(15));
			branch.setCatId(cursor.getString(16));
			branch.setCatName(cursor.getString(17));
			branch.setPriceName1(cursor.getString(18));
			branch.setPriceName2(cursor.getString(19));
			branch.setPriceName3(cursor.getString(20));
			branch.setPriceName4(cursor.getString(21));
			branch.setPriceName5(cursor.getString(22));
			branch.setPriceName6(cursor.getString(23));
			branch.setRegular(cursor.getString(24));
			branch.setHomeDeliveryPrice(cursor.getString(25));
			branch.setHappyHour(cursor.getString(26));
			branch.setLikes(cursor.getInt(27));
			branch.setLocationId(cursor.getString(28));
			branch.setCityID(cursor.getString(29));
			branch.setLatitude(cursor.getString(30));
			branch.setLongitude(cursor.getString(31));
			branch.setComments(cursor.getInt(32));

			// Add to the DB
			branchMenusList.add(branch);

			// Move to the next result
			cursor.moveToNext();
		}

		return branchMenusList;

	}

	public void insertBranchIds(String branchId) {
		String timeStamp = getDateTime();
		ContentValues contentValues = new ContentValues();
		// contentValues.put("sno", );
		contentValues.put("branchId", branchId);
		contentValues.put("time", timeStamp);
		contentValues.put("bookmark", "");

		// Insert into DB
		db.insert(DatabaseHelper.TABLE_BRANCHIDS, null, contentValues);
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public List<BranchIdDatabaseDto> getBanchIds() {
		List<BranchIdDatabaseDto> lists = new ArrayList<BranchIdDatabaseDto>();

		Cursor cursor = db.query(DatabaseHelper.TABLE_BRANCHIDS, null, null,
				null, null, null, null);
		cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			BranchIdDatabaseDto branchid = new BranchIdDatabaseDto();
			// Take values from the DB

			branchid.setBranchId(cursor.getString(0));
			branchid.setTimeStamp(cursor.getString(1));
			branchid.setBookmark(cursor.getString(2));
			// Add to the DB
			lists.add(branchid);
			cursor.moveToNext();
		}
		return lists;

	}

	public List<BranchMenuDto> getgroupByCatName(String branchId) {
		List<BranchMenuDto> branchCatNamesList = new ArrayList<BranchMenuDto>();
		// String selectQuery =
		// "select catId,catName from branchmenu group by catName order by catName";
		String selectQuery = "select catId,catName,branchId from branchmenu where branchId='"
				+ branchId + "' group by catName order by catName";
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		while (!c.isAfterLast()) {
			BranchMenuDto branch = new BranchMenuDto();
			branch.setCatId(c.getString(c.getColumnIndex("catId")));
			branch.setCatName(c.getString(c.getColumnIndex("catName")));
			branchCatNamesList.add(branch);
			c.moveToNext();
		}
		return branchCatNamesList;
	}

	public List<FeedDetailDto> getBranchMenusForOneTag(String branchId) {

		List<FeedDetailDto> branchMenusList = new ArrayList<FeedDetailDto>();

		// // Name of the columns we want to select
		// String[] tableColumns = new String[] { "branchId", "branchName",
		// "objectId", "dishId", "dishName", "dishDesc", "dishTags",
		// "dishPic", "nonVeg", "publish", "price1", "price2", "price3",
		// "price4", "price5", "price6", "catId", "catName", "priceName1",
		// "priceName2", "priceName3", "priceName4", "priceName5",
		// "priceName6", "regular", "homeDeliveryPrice", "happyHour",
		// "likes", "locationID", "cityID", "latitude", "longitude",
		// "comments","oneTag","twoTag","threeTag","fourTag","locationName" };
		//
		// // Query the database
		// Cursor cursor = db.query("branchmenu", tableColumns, null, null,
		// null,
		// branchId, null);

		String selectQuery = "select * from branchmenu where branchId='"
				+ branchId + "' and oneTag>0 order by oneTag desc limit 0,10";

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();

		// Iterate the results
		while (!cursor.isAfterLast()) {
			FeedDetailDto branch = new FeedDetailDto();
			// Take values from the DB
			BranchDishIdDto branchDishIdDto = new BranchDishIdDto();

			BranchIdDto branchid = new BranchIdDto();

			// BranchIdDto branchid=branch.getBranchId();
			branchid.setObjectId(cursor.getString(cursor
					.getColumnIndex("branchId")));
			branchDishIdDto.setBranchId(branchid);

			branchDishIdDto.setBranchName(cursor.getString(cursor
					.getColumnIndex("branchName")));
			branchDishIdDto.setObjectId(cursor.getString(cursor
					.getColumnIndex("objectId")));

			// DishIdDto dishId=branch.getDishId();
			DishIdDto dishId = new DishIdDto();
			dishId.setObjectId(cursor.getString(cursor.getColumnIndex("dishId")));
			dishId.setName(cursor.getString(cursor.getColumnIndex("dishName")));
			dishId.setDesc(cursor.getString(cursor.getColumnIndex("dishDesc")));

			// DishPicDto dishPic=dishId.getDishPic();
			DishPicDto dishPic = new DishPicDto();
			dishPic.setUrl(cursor.getString(cursor.getColumnIndex("dishPic")));
			dishId.setDishPic(dishPic);
			dishId.setTags(cursor.getString(cursor.getColumnIndex("dishTags")));
			branchDishIdDto.setDishId(dishId);

			branchDishIdDto
					.setNonVeg((cursor.getString(cursor
							.getColumnIndex("nonVeg")) != null && cursor
							.getString(cursor.getColumnIndex("nonVeg"))
							.equalsIgnoreCase("true")) ? true : false);

			branchDishIdDto
					.setPublish((cursor.getString(cursor
							.getColumnIndex("publish")) != null && cursor
							.getString(cursor.getColumnIndex("publish"))
							.equalsIgnoreCase("true")) ? true : false);

			branchDishIdDto.setPrice1(cursor.getInt(cursor
					.getColumnIndex("price1")));
			branchDishIdDto.setPrice2(cursor.getInt(cursor
					.getColumnIndex("price2")));
			branchDishIdDto.setPrice3(cursor.getInt(cursor
					.getColumnIndex("price3")));
			branchDishIdDto.setPrice4(cursor.getInt(cursor
					.getColumnIndex("price4")));
			branchDishIdDto.setPrice5(cursor.getInt(cursor
					.getColumnIndex("price5")));
			branchDishIdDto.setPrice6(cursor.getInt(cursor
					.getColumnIndex("price6")));

			branchDishIdDto.setOneTag(cursor.getInt(cursor
					.getColumnIndex("oneTag")));
			branchDishIdDto.setTwoTag(cursor.getInt(cursor
					.getColumnIndex("twoTag")));
			branchDishIdDto.setThreeTag(cursor.getInt(cursor
					.getColumnIndex("threeTag")));
			branchDishIdDto.setFourTag(cursor.getInt(cursor
					.getColumnIndex("fourTag")));

			// CatIdDto catId=branch.getCatId();
			CatIdDto catId = new CatIdDto();
			catId.setObjectId(cursor.getString(cursor.getColumnIndex("catId")));
			catId.setName(cursor.getString(cursor.getColumnIndex("catName")));
			branchDishIdDto.setCatId(catId);

			branchDishIdDto.setPriceName1(cursor.getString(cursor
					.getColumnIndex("priceName1")));
			branchDishIdDto.setPriceName2(cursor.getString(cursor
					.getColumnIndex("priceName2")));
			branchDishIdDto.setPriceName3(cursor.getString(cursor
					.getColumnIndex("priceName3")));
			branchDishIdDto.setPriceName4(cursor.getString(cursor
					.getColumnIndex("priceName4")));
			branchDishIdDto.setPriceName5(cursor.getString(cursor
					.getColumnIndex("priceName5")));
			branchDishIdDto.setPriceName6(cursor.getString(cursor
					.getColumnIndex("priceName6")));
			branchDishIdDto.setRegular(cursor.getInt(cursor
					.getColumnIndex("regular")));
			branchDishIdDto.setHomeDeliveryPrice(cursor.getString(cursor
					.getColumnIndex("homeDeliveryPrice")));
			branchDishIdDto.setHappyHour(cursor.getString(cursor
					.getColumnIndex("happyHour")));
			branchDishIdDto.setLikes(cursor.getInt(cursor
					.getColumnIndex("likes")));

			// LocationDto locationId=branch.getLocation();
			LocationDto locationId = new LocationDto();
			locationId.setObjectId(cursor.getString(cursor
					.getColumnIndex("locationID")));
			locationId.setName(cursor.getString(cursor
					.getColumnIndex("locationName")));
			branchDishIdDto.setLocation(locationId);

			// CityDto citid=branch.getCity();
			CityDto citid = new CityDto();
			citid.setObjectId(cursor.getString(cursor.getColumnIndex("cityID")));
			branchDishIdDto.setCity(citid);
			// PointDto pointDto=branch.getPoint();
			PointDto pointDto = new PointDto();
			pointDto.setLatitude(cursor.getDouble(cursor
					.getColumnIndex("latitude")));
			pointDto.setLongitude(cursor.getDouble(cursor
					.getColumnIndex("longitude")));
			branchDishIdDto.setPoint(pointDto);
			branchDishIdDto.setComments(cursor.getInt(cursor
					.getColumnIndex("comments")));

			branch.setBranchDishId(branchDishIdDto);

			// Add to the DB
			branchMenusList.add(branch);

			// Move to the next result
			cursor.moveToNext();
		}

		return branchMenusList;

	}

	public List<FeedDetailDto> getgroupByCatNames(String branchId) {
		List<FeedDetailDto> branchCatNamesList = new ArrayList<FeedDetailDto>();
		// String selectQuery =
		// "select catId,catName from branchmenu group by catName order by catName";
		String selectQuery = "select catId,catName,branchId from branchmenu where branchId='"
				+ branchId + "' group by catName order by catName";
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		while (!c.isAfterLast()) {
			FeedDetailDto branch = new FeedDetailDto();

			CatIdDto catId = new CatIdDto();

			String caId = c.getString(c.getColumnIndex("catId"));
			if (caId != null && !caId.equals("") && caId.length() > 0) {
				catId.setObjectId(caId);

				String catname = c.getString(c.getColumnIndex("catName"));
				if (catname != null && !catname.equals("")
						&& catname.length() > 0) {
					catId.setName(catname);
				}

				branch.setCatId(catId);
				branchCatNamesList.add(branch);
			}

			c.moveToNext();
		}
		return branchCatNamesList;
	}

	public int checkBranchIdRow(String brancId) {
		Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM "
				+ DatabaseHelper.TABLE_BRANCHIDS + " WHERE "
				+ DatabaseHelper.KEY_BRANCHIDS_BRANCH_ID + "=?",
				new String[] { String.valueOf(brancId) });

		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	public int checkBranchIdInBranchMenu(String brancId) {
		Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM "
				+ DatabaseHelper.TABLE_BRANCH_MENU + " WHERE "
				+ DatabaseHelper.KEY_BRANCH_MENU_BRANCHID + "=?",
				new String[] { String.valueOf(brancId) });

		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	public void deleteBranchMenu(String branchid) {
		// delete from branchmenu where branchId='U8mGCgc1OT';
		String table_name = DatabaseHelper.TABLE_BRANCH_MENU;
		String where = " branchId = '" + branchid + "'";
		String[] whereArgs = null;
		db.delete(table_name, where, whereArgs);
	}

	public void deleteBranchid(String branchid) {
		String table_name = DatabaseHelper.TABLE_BRANCHIDS;
		String where = " branchId = '" + branchid + "'";

		String[] whereArgs = null;
		db.delete(table_name, where, whereArgs);

	}

	public List<FeedDetailDto> getOrderByCatName(String catId) {
		List<FeedDetailDto> catNamesOrderbyLists = new ArrayList<FeedDetailDto>();
		String selectQuery = "select objectId,catId,catName,dishName,regular,dishPic,branchId,dishId,branchName,cityID,locationID,latitude,longitude,oneTag,twoTag,threeTag,fourTag,dishDesc,locationName,latitude,longitude from branchmenu where catId='"
				+ catId + "'";

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		while (!c.isAfterLast()) {
			FeedDetailDto branch = new FeedDetailDto();

			BranchDishIdDto branchDishIdDto = new BranchDishIdDto();
			branchDishIdDto.setObjectId(c.getString(c
					.getColumnIndex("objectId")));

			CatIdDto catIdDto = new CatIdDto();
			catIdDto.setName(c.getString(c.getColumnIndex("catName")));
			branchDishIdDto.setCatId(catIdDto);

			DishIdDto dishIdDto = new DishIdDto();
			dishIdDto.setName(c.getString(c.getColumnIndex("dishName")));
			dishIdDto.setDesc(c.getString(c.getColumnIndex("dishDesc")));
			dishIdDto.setObjectId(c.getString(c.getColumnIndex("dishId")));
			DishPicDto dishPicDto = new DishPicDto();
			dishPicDto.setUrl(c.getString(c.getColumnIndex("dishPic")));
			dishIdDto.setDishPic(dishPicDto);
			dishIdDto.setDishPicThumbnail(dishPicDto);
			branchDishIdDto.setDishId(dishIdDto);

			BranchIdDto branchIdDto = new BranchIdDto();
			branchIdDto.setObjectId(c.getString(c.getColumnIndex("branchId")));
			branchDishIdDto.setBranchId(branchIdDto);

			branchDishIdDto.setRegular(c.getLong(c.getColumnIndex("regular")));

			branchDishIdDto.setBranchName(c.getString(c
					.getColumnIndex("branchName")));

			CityDto citIdDto = new CityDto();
			citIdDto.setObjectId(c.getString(c.getColumnIndex("cityID")));
			branchDishIdDto.setCity(citIdDto);

			PointDto poinddto = new PointDto();
			poinddto.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
			poinddto.setLatitude(c.getDouble(c.getColumnIndex("longitude")));
			branchDishIdDto.setPoint(poinddto);

			LocationDto locationDto = new LocationDto();
			locationDto
					.setObjectId(c.getString(c.getColumnIndex("locationID")));
			locationDto.setName(c.getString(c.getColumnIndex("locationName")));
			branchDishIdDto.setLocation(locationDto);

			PointDto pointDto = new PointDto();
			pointDto.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
			pointDto.setLongitude(c.getDouble(c.getColumnIndex("longitude")));

			// branchDishIdDto.setOneTag(c.getString(c.getColumnIndex("oneTag")));

			// branchDishIdDto.setOneTag(c.getString(c.getColumnIndex("oneTag")));
			// branchDishIdDto.setTwoTag(c.getString(c.getColumnIndex("twoTag")));
			// branchDishIdDto.setThreeTag(c.getString(c.getColumnIndex("threeTag")));
			// branchDishIdDto.setFourTag(c.getString(c.getColumnIndex("fourTag")));

			branchDishIdDto.setOneTag(c.getInt(c.getColumnIndex("oneTag")));
			branchDishIdDto.setTwoTag(c.getInt(c.getColumnIndex("twoTag")));
			branchDishIdDto.setThreeTag(c.getInt(c.getColumnIndex("threeTag")));
			branchDishIdDto.setFourTag(c.getInt(c.getColumnIndex("fourTag")));

			branch.setBranchDishId(branchDishIdDto);

			catNamesOrderbyLists.add(branch);
			c.moveToNext();
		}
		return catNamesOrderbyLists;

	}

}
