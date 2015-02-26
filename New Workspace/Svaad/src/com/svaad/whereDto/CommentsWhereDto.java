package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CommentsWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long $gt;

	public long get$gt() {
		return $gt;
	}

	public void set$gt(long $gt) {
		this.$gt = $gt;
	}

}
