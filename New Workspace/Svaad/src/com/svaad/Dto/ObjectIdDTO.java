package com.svaad.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ObjectIdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> $in;

	/**
	 * @return the $in
	 */
	public List<String> get$in() {
		return $in;
	}

	/**
	 * @param $in
	 *            the $in to set
	 */
	public void set$in(List<String> $in) {
		this.$in = $in;
	}

}
