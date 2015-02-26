package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.HomeDetailDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HomeResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<HomeDetailDto> results;

	public List<HomeDetailDto> getResults() {
		return results;
	}

	public void setResults(List<HomeDetailDto> results) {
		this.results = results;
	}

}
