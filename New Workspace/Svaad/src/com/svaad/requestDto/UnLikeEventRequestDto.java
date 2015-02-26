package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UnLikeEventRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String branchDishId;
	private String userId;
	/**
	 * @return the branchDishId
	 */
	public String getBranchDishId() {
		return branchDishId;
	}
	/**
	 * @param branchDishId the branchDishId to set
	 */
	public void setBranchDishId(String branchDishId) {
		this.branchDishId = branchDishId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
