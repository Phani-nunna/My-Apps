package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SearchTagsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] $all;

	public String[] get$all() {
		return $all;
	}

	public void set$all(String[] $all) {
		this.$all = $all;
	}

}
