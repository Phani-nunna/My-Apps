package com.svaad.requestDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.AclDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.ReadWriteDto;
import com.svaad.Dto.TableObjectDto;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EventRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TableObjectDto branchDishId;
	private TableObjectDto branchId;
	// private String branchName;
	private TableObjectDto city;
	private TableObjectDto dishId;
	private TableObjectDto userId;
	private TableObjectDto location;
	private TableObjectDto place;
	private PointDto point;
	private List<String> userTags;
	private AclDto acl;
	private String commentText;
	private long imgHeight;
	private long imgWidth;
	private long dishTag;
	private Boolean wishlist;

	public TableObjectDto getBranchDishId() {
		return branchDishId;
	}

	public void setBranchDishId(TableObjectDto branchDishId) {
		this.branchDishId = branchDishId;
	}

	public TableObjectDto getBranchId() {
		return branchId;
	}

	public void setBranchId(TableObjectDto branchId) {
		this.branchId = branchId;
	}

	// public String getBranchName() {
	// return branchName;
	// }
	//
	// public void setBranchName(String branchName) {
	// this.branchName = branchName;
	// }

	public TableObjectDto getCity() {
		return city;
	}

	public void setCity(TableObjectDto city) {
		this.city = city;
	}

	public TableObjectDto getDishId() {
		return dishId;
	}

	public void setDishId(TableObjectDto dishId) {
		this.dishId = dishId;
	}

	public TableObjectDto getUserId() {
		return userId;
	}

	public void setUserId(TableObjectDto userId) {
		this.userId = userId;
	}

	public TableObjectDto getLocation() {
		return location;
	}

	public void setLocation(TableObjectDto location) {
		this.location = location;
	}

	public TableObjectDto getPlace() {
		return place;
	}

	public void setPlace(TableObjectDto place) {
		this.place = place;
	}

	public PointDto getPoint() {
		return point;
	}

	public void setPoint(PointDto point) {
		this.point = point;
	}

	@JsonProperty("ACL")
	public AclDto getAcl() {
		return acl;
	}

	@JsonProperty("ACL")
	public void setAcl(AclDto acl) {
		this.acl = acl;
	}

	@JsonIgnore
	public void setEventRequestDto(FeedDetailDto dishDetailDto,
			List<String> userTags) {

		// if (dishDetailDto.getBranchDishId().getBranchName() != null
		// && dishDetailDto.getBranchDishId().getBranchName().length() > 0) {
		//
		// setBranchName(dishDetailDto.getBranchDishId().getBranchName());
		// }

		if (dishDetailDto.getCommentText() != null
				&& dishDetailDto.getCommentText().length() > 0) {

			setCommentText(dishDetailDto.getCommentText());
		}

		TableObjectDto branchDishId = new TableObjectDto();
		branchDishId.set__type("Pointer");
		branchDishId.setClassName("BranchDish");
		if (dishDetailDto.getBranchDishId().getObjectId() != null
				&& dishDetailDto.getBranchDishId().getObjectId().length() > 0) {
			branchDishId.setObjectId(dishDetailDto.getBranchDishId()
					.getObjectId());
			setBranchDishId(branchDishId);
		}

		if (dishDetailDto.getBranchDishId().getBranchId() != null) {

			TableObjectDto branchId = new TableObjectDto();
			branchId.set__type("Pointer");
			branchId.setClassName("Branches");
			branchId.setObjectId(dishDetailDto.getBranchDishId().getBranchId()
					.getObjectId());
			setBranchId(branchId);
		}

		if (dishDetailDto.getBranchDishId().getCity() != null) {

			// private TableObjectDto city;
			TableObjectDto city = new TableObjectDto();
			city.set__type("Pointer");
			city.setClassName("City");
			city.setObjectId(dishDetailDto.getBranchDishId().getCity()
					.getObjectId());
			setCity(city);
		}

		if (dishDetailDto.getBranchDishId().getDishId() != null) {

			// private TableObjectDto dishId;
			TableObjectDto dishId = new TableObjectDto();
			dishId.set__type("Pointer");
			dishId.setClassName("Dishes");
			dishId.setObjectId(dishDetailDto.getBranchDishId().getDishId()
					.getObjectId());
			setDishId(dishId);
		}

		// private TableObjectDto userId;
		TableObjectDto userId = new TableObjectDto();
		userId.set__type("Pointer");
		userId.setClassName("_User");
		userId.setObjectId(Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY));
		setUserId(userId);

		if (dishDetailDto.getBranchDishId().getLocation() != null) {

			// private TableObjectDto location;
			TableObjectDto location = new TableObjectDto();
			location.set__type("Pointer");
			location.setClassName("Location");
			location.setObjectId(dishDetailDto.getBranchDishId().getLocation()
					.getObjectId());
			setLocation(location);
		}

		if (dishDetailDto.getBranchDishId().getPlace() != null) {

			// private TableObjectDto place;
			TableObjectDto place = new TableObjectDto();
			place.set__type("Pointer");
			place.setClassName("Place");
			place.setObjectId(dishDetailDto.getBranchDishId().getPlace()
					.getObjectId());
			setPlace(place);
		}

		if (dishDetailDto.getBranchDishId().getPoint() != null) {

			// private TableObjectDto point;
			PointDto point = new PointDto();
			point.set__type("GeoPoint");
			point.setLatitude(dishDetailDto.getBranchDishId().getPoint()
					.getLatitude());
			point.setLongitude(dishDetailDto.getBranchDishId().getPoint()
					.getLongitude());
			setPoint(point);
		}

		// if (dishDetailDto.getBranchDishId().getSearchTags() != null) {
		//
		// List<String> searchTags = dishDetailDto.getBranchDishId()
		// .getSearchTags();
		// setUserTags(searchTags);
		// }

		if (userTags != null && userTags.size() > 0) {
			setUserTags(userTags);
		}

		AclDto aclDto = new AclDto();
		ReadWriteDto userIdReadWriteDto = new ReadWriteDto();
		userIdReadWriteDto.setRead(true);
		userIdReadWriteDto.setWrite(true);
		ReadWriteDto starReadWriteDto = new ReadWriteDto();
		starReadWriteDto.setRead(true);
		starReadWriteDto.setWrite(true);
		aclDto.setStar(starReadWriteDto);
		aclDto.setUserId(userIdReadWriteDto);
		setAcl(aclDto);

	}

	public void setEventRequestDto(FeedDetailDto dishDetailDto) {

		// if (dishDetailDto.getBranchDishId().getBranchName() != null
		// && dishDetailDto.getBranchDishId().getBranchName().length() > 0) {
		//
		// setBranchName(dishDetailDto.getBranchDishId().getBranchName());
		// }

		if (dishDetailDto.getCommentText() != null
				&& dishDetailDto.getCommentText().length() > 0) {

			setCommentText(dishDetailDto.getCommentText());
		}

		TableObjectDto branchDishId = new TableObjectDto();
		branchDishId.set__type("Pointer");
		branchDishId.setClassName("BranchDish");

		if (dishDetailDto.getBranchDishId().getObjectId() != null) {

			branchDishId.setObjectId(dishDetailDto.getBranchDishId()
					.getObjectId());
			setBranchDishId(branchDishId);
		}

		if (dishDetailDto.getBranchDishId().getBranchId() != null) {

			TableObjectDto branchId = new TableObjectDto();
			branchId.set__type("Pointer");
			branchId.setClassName("Branches");
			branchId.setObjectId(dishDetailDto.getBranchDishId().getBranchId()
					.getObjectId());
			setBranchId(branchId);
		}

		if (dishDetailDto.getBranchDishId().getCity() != null) {

			// private TableObjectDto city;
			TableObjectDto city = new TableObjectDto();
			city.set__type("Pointer");
			city.setClassName("City");
			city.setObjectId(dishDetailDto.getBranchDishId().getCity()
					.getObjectId());
			setCity(city);
		}

		if (dishDetailDto.getBranchDishId().getDishId() != null) {

			// private TableObjectDto dishId;
			TableObjectDto dishId = new TableObjectDto();
			dishId.set__type("Pointer");
			dishId.setClassName("Dishes");
			dishId.setObjectId(dishDetailDto.getBranchDishId().getDishId()
					.getObjectId());
			setDishId(dishId);
		}

		// private TableObjectDto userId;
		TableObjectDto userId = new TableObjectDto();
		userId.set__type("Pointer");
		userId.setClassName("_User");
		userId.setObjectId(Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY));
		setUserId(userId);

		if (dishDetailDto.getBranchDishId().getLocation() != null) {

			// private TableObjectDto location;
			TableObjectDto location = new TableObjectDto();
			location.set__type("Pointer");
			location.setClassName("Location");
			location.setObjectId(dishDetailDto.getBranchDishId().getLocation()
					.getObjectId());
			setLocation(location);
		}

		if (dishDetailDto.getBranchDishId().getPlace() != null) {

			// private TableObjectDto place;
			TableObjectDto place = new TableObjectDto();
			place.set__type("Pointer");
			place.setClassName("Place");
			place.setObjectId(dishDetailDto.getBranchDishId().getPlace()
					.getObjectId());
			setPlace(place);
		}

		if (dishDetailDto.getBranchDishId().getPoint() != null) {

			// private TableObjectDto point;
			PointDto point = new PointDto();
			point.set__type("GeoPoint");
			point.setLatitude(dishDetailDto.getBranchDishId().getPoint()
					.getLatitude());
			point.setLongitude(dishDetailDto.getBranchDishId().getPoint()
					.getLongitude());
			setPoint(point);
		}

		AclDto aclDto = new AclDto();
		ReadWriteDto userIdReadWriteDto = new ReadWriteDto();
		userIdReadWriteDto.setRead(true);
		userIdReadWriteDto.setWrite(true);
		ReadWriteDto starReadWriteDto = new ReadWriteDto();
		starReadWriteDto.setRead(true);
		starReadWriteDto.setWrite(true);
		aclDto.setStar(starReadWriteDto);
		aclDto.setUserId(userIdReadWriteDto);
		setAcl(aclDto);

	}

	/**
	 * @return the userTags
	 */
	public List<String> getUserTags() {
		return userTags;
	}

	/**
	 * @param userTags
	 *            the userTags to set
	 */
	public void setUserTags(List<String> userTags) {
		this.userTags = userTags;
	}

	/**
	 * @return the commentText
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * @param commentText
	 *            the commentText to set
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * @return the imgHeight
	 */
	public long getImgHeight() {
		return imgHeight;
	}

	/**
	 * @param imgHeight
	 *            the imgHeight to set
	 */
	public void setImgHeight(long imgHeight) {
		this.imgHeight = imgHeight;
	}

	/**
	 * @return the imgWidth
	 */
	public long getImgWidth() {
		return imgWidth;
	}

	/**
	 * @param imgWidth
	 *            the imgWidth to set
	 */
	public void setImgWidth(long imgWidth) {
		this.imgWidth = imgWidth;
	}

	/**
	 * @return the dishTag
	 */
	public long getDishTag() {
		return dishTag;
	}

	/**
	 * @param dishTag
	 *            the dishTag to set
	 */
	public void setDishTag(long dishTag) {
		this.dishTag = dishTag;
	}

	/**
	 * @return the wishlist
	 */
	public Boolean getWishlist() {
		return wishlist;
	}

	/**
	 * @param wishlist
	 *            the wishlist to set
	 */
	public void setWishlist(Boolean wishlist) {
		this.wishlist = wishlist;
	}

}
