package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.ErrorDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LogInResponseDto extends ErrorDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String uname;
	private String email;
	private String createdAt;
	private String updatedAt;
	private String objectId;
	private String sessionToken;
	private String displayPicUrl;
	private int followerCount;
	private List<String> followers;
	private List<String> followingUsers;
	private int followingUsersCount;
	private String fbId;
	private String userRole;
	private List<String> wishlistArr;
	private int photoCount;
	private int suggestCount;
	private int wishlistCount;
	
	
	
	
	public String getDisplayPicUrl() {
		return displayPicUrl;
	}

	public void setDisplayPicUrl(String displayPicUrl) {
		this.displayPicUrl = displayPicUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public List<String> getFollowers() {
		return followers;
	}

	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}

	public List<String> getFollowingUsers() {
		return followingUsers;
	}

	public void setFollowingUsers(List<String> followingUsers) {
		this.followingUsers = followingUsers;
	}

	public int getFollowingUsersCount() {
		return followingUsersCount;
	}

	public void setFollowingUsersCount(int followingUsersCount) {
		this.followingUsersCount = followingUsersCount;
	}

	/**
	 * @return the fbId
	 */
	public String getFbId() {
		return fbId;
	}

	/**
	 * @param fbId the fbId to set
	 */
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	/**
	 * @return the photoCount
	 */
	public int getPhotoCount() {
		return photoCount;
	}

	/**
	 * @param photoCount the photoCount to set
	 */
	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	/**
	 * @return the suggestCount
	 */
	public int getSuggestCount() {
		return suggestCount;
	}

	/**
	 * @param suggestCount the suggestCount to set
	 */
	public void setSuggestCount(int suggestCount) {
		this.suggestCount = suggestCount;
	}

	/**
	 * @return the wishlistCount
	 */
	public int getWishlistCount() {
		return wishlistCount;
	}

	/**
	 * @param wishlistCount the wishlistCount to set
	 */
	public void setWishlistCount(int wishlistCount) {
		this.wishlistCount = wishlistCount;
	}

	

}
