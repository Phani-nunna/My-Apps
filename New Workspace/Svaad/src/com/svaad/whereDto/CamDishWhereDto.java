package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.CamDishBranchIdDto;
import com.svaad.Dto.CamDishNameDto;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class CamDishWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CamDishBranchIdDto branchId;
	private CamDishNameDto dishName;

	/**
	 * @return the branchId
	 */
	public CamDishBranchIdDto getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(CamDishBranchIdDto branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the dishName
	 */
	public CamDishNameDto getDishName() {
		return dishName;
	}

	/**
	 * @param dishName the dishName to set
	 */
	public void setDishName(CamDishNameDto dishName) {
		this.dishName = dishName;
	}
}
