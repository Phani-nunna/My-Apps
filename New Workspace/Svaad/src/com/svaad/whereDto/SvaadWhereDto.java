package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.Dto.SvaadPointDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SvaadWhereDto implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private FromRestaurantDto fromRestaurant;
	private SvaadPointDto point;
	/**
	 * @return the fromRestaurant
	 */
	public FromRestaurantDto getFromRestaurant() {
		return fromRestaurant;
	}
	/**
	 * @param fromRestaurant the fromRestaurant to set
	 */
	public void setFromRestaurant(FromRestaurantDto fromRestaurant) {
		this.fromRestaurant = fromRestaurant;
	}
	/**
	 * @return the point
	 */
	public SvaadPointDto getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(SvaadPointDto point) {
		this.point = point;
	}
	
}
