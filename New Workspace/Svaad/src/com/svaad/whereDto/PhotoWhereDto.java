package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.Dto.PhotoDto;
import com.svaad.Dto.SvaadPointDto;
import com.svaad.Dto.UserIdDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PhotoWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserIdDto userId;
	private FromRestaurantDto fromRestaurant;
	private PhotoDto dishPhoto;

	public UserIdDto getUserId() {
		return userId;
	}

	public void setUserId(UserIdDto userId) {
		this.userId = userId;
	}

	/**
	 * @return the fromRestaurant
	 */
	public FromRestaurantDto getFromRestaurant() {
		return fromRestaurant;
	}

	/**
	 * @param fromRestaurant
	 *            the fromRestaurant to set
	 */
	public void setFromRestaurant(FromRestaurantDto fromRestaurant) {
		this.fromRestaurant = fromRestaurant;
	}

	public PhotoDto getDishPhoto() {
		return dishPhoto;
	}

	public void setDishPhoto(PhotoDto dishPhoto) {
		this.dishPhoto = dishPhoto;
	}

	

}
