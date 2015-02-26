package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class LikeEventRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TableObjectDto branchDishId;
	private TableObjectDto branchId;
	private TableObjectDto city;
	private TableObjectDto dishId;
	private TableObjectDto userId;
	private TableObjectDto location;
	private TableObjectDto place;
	private PointDto point;
	private Boolean wishlist;
	private AclDto acl;

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

	public Boolean getWishlist() {
		return wishlist;
	}

	public void setWishlist(Boolean wishlist) {
		this.wishlist = wishlist;
	}

	public AclDto getAcl() {
		return acl;
	}

	public void setAcl(AclDto acl) {
		this.acl = acl;
	}

	@JsonIgnore
	public void setLikeRequestDto(FeedDetailDto dishDetailDto) {

		TableObjectDto branchDishId = new TableObjectDto();
		branchDishId.set__type("Pointer");
		branchDishId.setClassName("BranchDish");
		branchDishId.setObjectId(dishDetailDto.getObjectId());
		setBranchDishId(branchDishId);

		if (dishDetailDto.getBranchDishId().getBranchId() != null) {

			TableObjectDto branchId = new TableObjectDto();
			branchId.set__type("Pointer");
			branchId.setClassName("Branches");
			branchId.setObjectId(dishDetailDto.getBranchDishId().getBranchId()
					.getObjectId());
			setBranchId(branchId);
		}

		if (dishDetailDto.getBranchDishId().getCity() != null) {

			TableObjectDto city = new TableObjectDto();
			city.set__type("Pointer");
			city.setClassName("City");
			city.setObjectId(dishDetailDto.getBranchDishId().getCity()
					.getObjectId());
			setCity(city);
		}

		if (dishDetailDto.getBranchDishId().getDishId() != null) {

			TableObjectDto dishId = new TableObjectDto();
			dishId.set__type("Pointer");
			dishId.setClassName("Dishes");
			dishId.setObjectId(dishDetailDto.getBranchDishId().getDishId()
					.getObjectId());
			setDishId(dishId);
		}

		TableObjectDto userId = new TableObjectDto();
		userId.set__type("Pointer");
		userId.setClassName("_User");
		userId.setObjectId(Utils
				.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY));
		setUserId(userId);

		if (dishDetailDto.getBranchDishId().getLocation() != null) {

			TableObjectDto location = new TableObjectDto();
			location.set__type("Pointer");
			location.setClassName("Location");
			location.setObjectId(dishDetailDto.getBranchDishId().getLocation()
					.getObjectId());
			setLocation(location);
		}

		if (dishDetailDto.getBranchDishId().getPlace() != null) {

			TableObjectDto place = new TableObjectDto();
			place.set__type("Pointer");
			place.setClassName("Place");
			place.setObjectId(dishDetailDto.getBranchDishId().getPlace()
					.getObjectId());
			setPlace(place);
		}

		if (dishDetailDto.getBranchDishId().getPoint() != null) {

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

}
