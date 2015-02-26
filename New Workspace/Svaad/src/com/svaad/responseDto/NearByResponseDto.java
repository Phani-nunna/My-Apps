package com.svaad.responseDto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.FeedDetailDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NearByResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<FeedDetailDto> result;
	/**
	 * @return the result
	 */
	public List<FeedDetailDto> getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(List<FeedDetailDto> result) {
		this.result = result;
	}


}
