package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.CamRestaurantListWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CamRestaurentRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String include;
	private String keys;
	private long limit;
	private long skip;
	private String _method;
	private CamRestaurantListWhereDto where;

	

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	
	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getSkip() {
		return skip;
	}

	public void setSkip(long skip) {
		this.skip = skip;
	}

	@JsonProperty("_method")
	public String get_method() {
		return _method;
	}

	@JsonProperty("_method")
	public void set_method(String _method) {
		this._method = _method;
	}

	/**
	 * @return the keys
	 */
	public String getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(String keys) {
		this.keys = keys;
	}

	/**
	 * @return the where
	 */
	public CamRestaurantListWhereDto getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(CamRestaurantListWhereDto where) {
		this.where = where;
	}

	
}
