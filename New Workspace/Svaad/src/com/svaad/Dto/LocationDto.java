package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LocationDto extends TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CityDto city;
	private Boolean hasRestaurant;
	private String name;
	private String createdAt;
	private String updatedAt;

	public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}

	public Boolean getHasRestaurant() {
		return hasRestaurant;
	}

	public void setHasRestaurant(Boolean hasRestaurant) {
		this.hasRestaurant = hasRestaurant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
