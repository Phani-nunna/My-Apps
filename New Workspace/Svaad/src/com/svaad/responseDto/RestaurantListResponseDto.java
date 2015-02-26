package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.RestaurantDetailsDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RestaurantListResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RestaurantDetailsDto> results;
	private long count;

	public List<RestaurantDetailsDto> getResults() {
		return results;
	}

	public void setResults(List<RestaurantDetailsDto> results) {
		this.results = results;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
