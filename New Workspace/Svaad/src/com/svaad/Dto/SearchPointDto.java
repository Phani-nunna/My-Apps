package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SearchPointDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PointDto $nearSphere;
	private int $maxDistanceInKilometers;

	public PointDto get$nearSphere() {
		return $nearSphere;
	}

	public void set$nearSphere(PointDto $nearSphere) {
		this.$nearSphere = $nearSphere;
	}

	public int get$maxDistanceInKilometers() {
		return $maxDistanceInKilometers;
	}

	public void set$maxDistanceInKilometers(int $maxDistanceInKilometers) {
		this.$maxDistanceInKilometers = $maxDistanceInKilometers;
	}

}
