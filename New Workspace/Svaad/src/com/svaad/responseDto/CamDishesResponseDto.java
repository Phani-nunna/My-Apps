package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.CamDishesDeatailDto;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CamDishesResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CamDishesDeatailDto> results;
	/**
	 * @return the results
	 */
	public List<CamDishesDeatailDto> getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(List<CamDishesDeatailDto> results) {
		this.results = results;
	}
	

}
