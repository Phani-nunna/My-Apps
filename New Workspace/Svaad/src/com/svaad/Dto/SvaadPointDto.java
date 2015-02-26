package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SvaadPointDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PointDto $nearSphere;
//	private double $maxDistanceInKilometers;
	/**
	 * @return the $nearSphere
	 */
	public PointDto get$nearSphere() {
		return $nearSphere;
	}
	/**
	 * @param $nearSphere the $nearSphere to set
	 */
	public void set$nearSphere(PointDto $nearSphere) {
		this.$nearSphere = $nearSphere;
	}
	/**
	 * @return the $maxDistanceInKilometers
	 */
//	public double get$maxDistanceInKilometers() {
//		return $maxDistanceInKilometers;
//	}
//	/**
//	 * @param $maxDistanceInKilometers the $maxDistanceInKilometers to set
//	 */
//	public void set$maxDistanceInKilometers(double $maxDistanceInKilometers) {
//		this.$maxDistanceInKilometers = $maxDistanceInKilometers;
//	}
	

}
