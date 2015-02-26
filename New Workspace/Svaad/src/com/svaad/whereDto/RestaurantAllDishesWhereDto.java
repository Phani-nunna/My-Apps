package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.BranchIdDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RestaurantAllDishesWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BranchIdDto branchId;
	private boolean publish;

	public BranchIdDto getBranchId() {
		return branchId;
	}

	public void setBranchId(BranchIdDto branchId) {
		this.branchId = branchId;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

}
