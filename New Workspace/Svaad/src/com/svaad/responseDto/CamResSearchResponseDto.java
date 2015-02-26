package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.CamRestaurantDetailDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class CamResSearchResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<CamRestaurantDetailDto> results;

	public List<CamRestaurantDetailDto> getResults() {
		return results;
	}

	public void setResults(List<CamRestaurantDetailDto> results) {
		this.results = results;
	}

	
}
