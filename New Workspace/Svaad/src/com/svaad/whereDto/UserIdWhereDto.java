package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.CommentedByDto;
import com.svaad.Dto.RegularDto;
import com.svaad.Dto.SearchPointDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.TableObjectDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserIdWhereDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TableObjectDto userId;
	private TableObjectDto branchId;
	private CommentsWhereDto comments;
	private SearchTagsDto searchTags;
	private TableObjectDto catId;
	private RegularDto regular;
	private SearchPointDto point;
	private Boolean publish;
	private TableObjectDto location;
	private CommentedByDto commentedBy;
	private CommentedByDto likedBy;
	private TableObjectDto branchDishId;
	private TableObjectDto place;
	
	private Boolean nonVeg;

	public TableObjectDto getPlace() {
		return place;
	}

	public void setPlace(TableObjectDto place) {
		this.place = place;
	}

	public TableObjectDto getBranchDishId() {
		return branchDishId;
	}

	public void setBranchDishId(TableObjectDto branchDishId) {
		this.branchDishId = branchDishId;
	}

	public CommentedByDto getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(CommentedByDto likedBy) {
		this.likedBy = likedBy;
	}

	public CommentedByDto getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(CommentedByDto commentedBy) {
		this.commentedBy = commentedBy;
	}

	public TableObjectDto getLocation() {
		return location;
	}

	public void setLocation(TableObjectDto location) {
		this.location = location;
	}

	public SearchPointDto getPoint() {
		return point;
	}

	public void setPoint(SearchPointDto point) {
		this.point = point;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public RegularDto getRegular() {
		return regular;
	}

	public void setRegular(RegularDto regular) {
		this.regular = regular;
	}

	public TableObjectDto getCatId() {
		return catId;
	}

	public void setCatId(TableObjectDto catId) {
		this.catId = catId;
	}

	public SearchTagsDto getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(SearchTagsDto searchTags) {
		this.searchTags = searchTags;
	}

	public CommentsWhereDto getComments() {
		return comments;
	}

	public void setComments(CommentsWhereDto comments) {
		this.comments = comments;
	}

	public TableObjectDto getBranchId() {
		return branchId;
	}

	public void setBranchId(TableObjectDto branchId) {
		this.branchId = branchId;
	}

	public TableObjectDto getUserId() {
		return userId;
	}

	public void setUserId(TableObjectDto userId) {
		this.userId = userId;
	}

	public Boolean getNonVeg() {
		return nonVeg;
	}

	public void setNonVeg(Boolean nonVeg) {
		this.nonVeg = nonVeg;
	}

}
