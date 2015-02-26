package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RegularDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float $gt;
	private float $lte;

	public float get$gt() {
		return $gt;
	}

	public void set$gt(float $gt) {
		this.$gt = $gt;
	}

	public float get$lte() {
		return $lte;
	}

	public void set$lte(float $lte) {
		this.$lte = $lte;
	}

}
