package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.AllFoodiesDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class AllFoodiesResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AllFoodiesDto> results;

	public List<AllFoodiesDto> getResults() {
		return results;
	}

	public void setResults(List<AllFoodiesDto> results) {
		this.results = results;
	}

	
}
