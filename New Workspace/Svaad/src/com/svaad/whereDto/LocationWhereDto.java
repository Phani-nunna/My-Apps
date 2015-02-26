package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LocationWhereDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean hasRestaurant;

	public boolean isHasRestaurant() {
		return hasRestaurant;
	}

	public void setHasRestaurant(boolean hasRestaurant) {
		this.hasRestaurant = hasRestaurant;
	}

}
