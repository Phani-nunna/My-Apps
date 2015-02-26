package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CommentTextDto;
import com.svaad.Dto.CommentsDishIdDto;
import com.svaad.Dto.FromRestaurantDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DishCommentsWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CommentsDishIdDto dishId;
	private BranchIdDto branchId;
	private FromRestaurantDto fromRestaurant;
	private CommentTextDto commentText;

	public CommentsDishIdDto getDishId() {
		return dishId;
	}

	public void setDishId(CommentsDishIdDto dishId) {
		this.dishId = dishId;
	}

	public BranchIdDto getBranchId() {
		return branchId;
	}

	public void setBranchId(BranchIdDto branchId) {
		this.branchId = branchId;
	}

	public FromRestaurantDto getFromRestaurant() {
		return fromRestaurant;
	}

	public void setFromRestaurant(FromRestaurantDto fromRestaurant) {
		this.fromRestaurant = fromRestaurant;
	}

	public CommentTextDto getCommentText() {
		return commentText;
	}

	public void setCommentText(CommentTextDto commentText) {
		this.commentText = commentText;
	}

}
