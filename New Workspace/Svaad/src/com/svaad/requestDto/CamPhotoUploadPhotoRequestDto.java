package com.svaad.requestDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CamPhotoUploadPhotoRequestDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean nonVeg;
	private String branchId;
	private String name;
	private String branchDishId;
	private String userId;
	private int dishTag;
	private String dishPhoto;
	private String photoUrl;
	private int imgH;
	private int imgW;
	private String commentText;
	private List<String> userTags;

	public String getBranchDishId() {
		return branchDishId;
	}
	public void setBranchDishId(String branchDishId) {
		this.branchDishId = branchDishId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDishPhoto() {
		return dishPhoto;
	}
	public void setDishPhoto(String dishPhoto) {
		this.dishPhoto = dishPhoto;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public int getImgH() {
		return imgH;
	}
	public void setImgH(int imgH) {
		this.imgH = imgH;
	}
	public int getImgW() {
		return imgW;
	}
	public void setImgW(int imgW) {
		this.imgW = imgW;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public List<String> getUserTags() {
		return userTags;
	}
	public void setUserTags(List<String> userTags) {
		this.userTags = userTags;
	}
	/**
	 * @return the dishTag
	 */
	public int getDishTag() {
		return dishTag;
	}
	/**
	 * @param dishTag the dishTag to set
	 */
	public void setDishTag(int dishTag) {
		this.dishTag = dishTag;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getNonVeg() {
		return nonVeg;
	}
	public void setNonVeg(Boolean nonVeg) {
		this.nonVeg = nonVeg;
	}

}
