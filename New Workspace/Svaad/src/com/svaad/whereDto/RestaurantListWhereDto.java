package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.SearchPointDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.TableObjectDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RestaurantListWhereDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean publish;
	private Boolean pureVeg;

	private Long priceCategory;
	private SearchPointDto point;
	private SearchTagsDto searchTags;
	private TableObjectDto location;
	private TableObjectDto place;

	public TableObjectDto getPlace() {
		return place;
	}

	public void setPlace(TableObjectDto place) {
		this.place = place;
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

	public SearchTagsDto getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(SearchTagsDto searchTags) {
		this.searchTags = searchTags;
	}

	public Long getPriceCategory() {
		return priceCategory;
	}

	public void setPriceCategory(Long priceCategory) {
		this.priceCategory = priceCategory;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	/**
	 * @return the pureVeg
	 */
	public Boolean getPureVeg() {
		return pureVeg;
	}

	/**
	 * @param pureVeg the pureVeg to set
	 */
	public void setPureVeg(Boolean pureVeg) {
		this.pureVeg = pureVeg;
	}

	

}
