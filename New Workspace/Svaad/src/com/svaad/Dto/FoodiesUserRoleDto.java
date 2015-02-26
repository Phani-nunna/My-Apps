package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class FoodiesUserRoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	
	private boolean $exists;
	//private int type;
	public boolean is$exists() {
		return $exists;
	}
	public void set$exists(boolean $exists) {
		this.$exists = $exists;
	}
	
	

}
