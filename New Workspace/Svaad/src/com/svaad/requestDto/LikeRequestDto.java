package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LikeRequestDto extends EventRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean wishlist;

	/**
	 * @return the wishlist
	 */
	public Boolean getWishlist() {
		return wishlist;
	}

	/**
	 * @param wishlist
	 *            the wishlist to set
	 */
	public void setWishlist(Boolean wishlist) {
		this.wishlist = wishlist;
	}
}
