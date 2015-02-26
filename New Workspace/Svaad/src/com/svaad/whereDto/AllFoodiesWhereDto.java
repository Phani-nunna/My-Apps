package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.FoodiesUserRoleDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class AllFoodiesWhereDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private FoodiesUserRoleDto userRole;

	public FoodiesUserRoleDto getUserRole() {
		return userRole;
	}

	public void setUserRole(FoodiesUserRoleDto userRole) {
		this.userRole = userRole;
	}
	
	

}
