package com.svaad.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FeedUserIdDto extends TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private long followerCount;
	private long suggestCount;
	private long wishlistCount;
	private String uname;
	private String username;
	private String createdAt;
	private String updatedAt;
	private String displayPicUrl;
	private List<String> wishlistArr;
	private long photoCount;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(long followerCount) {
		this.followerCount = followerCount;
	}

	public long getSuggestCount() {
		return suggestCount;
	}

	public void setSuggestCount(long suggestCount) {
		this.suggestCount = suggestCount;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	/**
	 * @return the displayPicUrl
	 */
	public String getDisplayPicUrl() {
		return displayPicUrl;
	}

	/**
	 * @param displayPicUrl the displayPicUrl to set
	 */
	public void setDisplayPicUrl(String displayPicUrl) {
		this.displayPicUrl = displayPicUrl;
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

	public long getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(long photoCount) {
		this.photoCount = photoCount;
	}

}
