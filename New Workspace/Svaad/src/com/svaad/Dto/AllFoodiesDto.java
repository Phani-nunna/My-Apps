package com.svaad.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class AllFoodiesDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String birthday;
	
	private String displayPicUrl;
	private String email;
	private String fbId;
	private String uname;
	private String username;
	private String createdAt;
	private String updatedAt;
	private String objectId;
	private List<String> followers;
	private int followerCount;
	private long suggestCount;
	private long wishlistCount;
	private List<String> wishlistArr;
	
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDisplayPicUrl() {
		return displayPicUrl;
	}
	public void setDisplayPicUrl(String displayPicUrl) {
		this.displayPicUrl = displayPicUrl;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String userName) {
		this.username = userName;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public List<String> getFollowers() {
		return followers;
	}
	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
	/**
	 * @return the suggestCount
	 */
	public long getSuggestCount() {
		return suggestCount;
	}
	/**
	 * @param suggestCount the suggestCount to set
	 */
	public void setSuggestCount(long suggestCount) {
		this.suggestCount = suggestCount;
	}
	/**
	 * @return the wishlistCount
	 */
	public long getWishlistCount() {
		return wishlistCount;
	}
	/**
	 * @param wishlistCount the wishlistCount to set
	 */
	public void setWishlistCount(long wishlistCount) {
		this.wishlistCount = wishlistCount;
	}
	/**
	 * @return the wishlistArr
	 */
	public List<String> getWishlistArr() {
		return wishlistArr;
	}
	/**
	 * @param wishlistArr the wishlistArr to set
	 */
	public void setWishlistArr(List<String> wishlistArr) {
		this.wishlistArr = wishlistArr;
	}

	

}
