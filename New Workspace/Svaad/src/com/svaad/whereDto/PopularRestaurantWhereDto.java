package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class PopularRestaurantWhereDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean isPopular;
	private Boolean publish;
	/**
	 * @return the isPopular
	 */
	public Boolean getIsPopular() {
		return isPopular;
	}
	/**
	 * @param isPopular the isPopular to set
	 */
	public void setIsPopular(Boolean isPopular) {
		this.isPopular = isPopular;
	}
	/**
	 * @return the publish
	 */
	public Boolean getPublish() {
		return publish;
	}
	/**
	 * @param publish the publish to set
	 */
	public void setPublish(Boolean publish) {
		this.publish = publish;
	}
	
	
	
	
}
