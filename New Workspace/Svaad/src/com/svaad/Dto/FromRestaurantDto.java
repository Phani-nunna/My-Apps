package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FromRestaurantDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean $exists;

	/**
	 * @return the $exists
	 */
	public Boolean get$exists() {
		return $exists;
	}

	/**
	 * @param $exists the $exists to set
	 */
	public void set$exists(Boolean $exists) {
		this.$exists = $exists;
	}
	

}
