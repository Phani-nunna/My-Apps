package com.svaad.Dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.svaad.requestDto.EventRequestDto;
import com.svaad.requestDto.LikeRequestDto;
import com.svaad.requestDto.UplaodPhotoRequestDto;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class EventJson {

	public String getPhotoJson(UplaodPhotoRequestDto uplaodPhotoRequestDto) {

		try {
			JSONObject outerObject = getEventJson(uplaodPhotoRequestDto);

			JSONObject innerObject = new JSONObject();

			if (uplaodPhotoRequestDto.getDishPhoto() != null) {

				if (uplaodPhotoRequestDto.getDishPhoto().get__type() != null
						&& uplaodPhotoRequestDto.getDishPhoto().getName() != null
						&& uplaodPhotoRequestDto.getDishPhoto().getUrl() != null) {

					innerObject.put("__type", uplaodPhotoRequestDto
							.getDishPhoto().get__type());
					innerObject.put("name", uplaodPhotoRequestDto
							.getDishPhoto().getName());

					innerObject.put("url", uplaodPhotoRequestDto.getDishPhoto()
							.getUrl());

					outerObject.put("dishPhoto", innerObject);
				}
			}
			return outerObject.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public String getLikeJson(LikeRequestDto likeRequestDto) {

		try {
			JSONObject outerObject = getEventJson(likeRequestDto);
			return outerObject.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}


	private JSONObject getEventJson(EventRequestDto eventRequestDto)
			throws JSONException {
		try {
			JSONObject outerObject = new JSONObject();

			if (eventRequestDto.getBranchDishId() != null) {

				JSONObject branchDishIdObject = getTableObject(eventRequestDto
						.getBranchDishId());
				outerObject.put("branchDishId", branchDishIdObject);
			}

			if (eventRequestDto.getBranchId() != null) {

				JSONObject branchIdObject = getTableObject(eventRequestDto
						.getBranchId());
				outerObject.put("branchId", branchIdObject);
			}

			if (eventRequestDto.getCity() != null) {

				JSONObject cityObject = getTableObject(eventRequestDto
						.getCity());
				outerObject.put("city", cityObject);
			}

			if (eventRequestDto.getDishId() != null) {

				JSONObject dishIdObject = getTableObject(eventRequestDto
						.getDishId());
				outerObject.put("dishId", dishIdObject);
			}

			if (eventRequestDto.getUserId() != null) {

				JSONObject userIdObject = getTableObject(eventRequestDto
						.getUserId());
				outerObject.put("userId", userIdObject);
			}

			if (eventRequestDto.getLocation() != null) {

				JSONObject locationObject = getTableObject(eventRequestDto
						.getLocation());
				outerObject.put("location", locationObject);
			}

			if (eventRequestDto.getPlace() != null) {

				JSONObject placeObject = getTableObject(eventRequestDto
						.getPlace());
				outerObject.put("place", placeObject);
			}

			if (eventRequestDto.getPoint() != null) {

				JSONObject pointObject = getPointObject(eventRequestDto
						.getPoint());
				outerObject.put("point", pointObject);
			}

			if (eventRequestDto.getUserTags() != null) {

				JSONArray searchTagsObject = getSearchTags(eventRequestDto
						.getUserTags());
				outerObject.put("userTags", searchTagsObject);
			}

			if (eventRequestDto.getAcl() != null) {

				JSONObject aclObject = getAcl(eventRequestDto.getAcl());
				outerObject.put("ACL", aclObject);
			}

//			if (eventRequestDto.getBranchName() != null) {
//
//				outerObject.put("branchName", eventRequestDto.getBranchName());
//			}

			if (eventRequestDto.getCommentText() != null) {

				outerObject
						.put("commentText", eventRequestDto.getCommentText());
			}

			if (eventRequestDto.getImgHeight() != 0) {
				outerObject.put("imgHeight", eventRequestDto.getImgHeight());
			}

			if (eventRequestDto.getImgWidth() != 0) {
				outerObject.put("imgWidth", eventRequestDto.getImgWidth());
			}
			
			if(eventRequestDto.getDishTag()!=0)
			{
				outerObject.put("dishTag", eventRequestDto.getDishTag());
			}
			
			if(eventRequestDto.getWishlist()!=null)
			{
				outerObject.put("wishlist", eventRequestDto.getWishlist());
			}

			// if(eventRequestDto.getUserTags()!=null &&
			// eventRequestDto.getUserTags().size()>0)
			// {
			// outerObject.put("userTags", eventRequestDto.getUserTags());
			// }
			return outerObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject getAcl(AclDto acl) throws JSONException {
		JSONObject outerObject = new JSONObject();
		JSONObject userObject = new JSONObject();
		JSONObject starObject = new JSONObject();

		userObject.put("read", acl.getUserId().isRead());
		userObject.put("write", acl.getUserId().isWrite());
		starObject.put("read", acl.getStar().isRead());
		// starObject.put("write", acl.getStar().isRead());

		outerObject.put(
				Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY),
				userObject);
		outerObject.put("*", starObject);
		return outerObject;
	}

	private JSONArray getSearchTags(List<String> searchTags)
			throws JSONException {
		JSONArray jsonArray = new JSONArray();
		if (searchTags != null && searchTags.size() > 0) {
			for (int i = 0; i < searchTags.size(); i++) {
				jsonArray.put(i, searchTags.get(i));
			}
		}
		return jsonArray;
	}

	private JSONObject getTableObject(TableObjectDto tableObjectDto)
			throws JSONException {
		JSONObject outerObject = new JSONObject();
		if (tableObjectDto != null) {

			outerObject.put("__type", tableObjectDto.get__type());
			outerObject.put("className", tableObjectDto.getClassName());
			outerObject.put("objectId", tableObjectDto.getObjectId());
		}
		return outerObject;
	}

	private JSONObject getPointObject(PointDto pointDto) throws JSONException {
		JSONObject outerObject = new JSONObject();
		if (pointDto != null) {

			outerObject.put("__type", pointDto.get__type());
			outerObject.put("latitude", pointDto.getLatitude());
			outerObject.put("longitude", pointDto.getLongitude());
		}
		return outerObject;
	}

}
