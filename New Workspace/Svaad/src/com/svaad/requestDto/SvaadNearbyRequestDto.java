package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.SvaadWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SvaadNearbyRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SvaadWhereDto where;
	private String include;
	private String keys;
	private long limit;
//	private long skip;
	private String order;
	private String _method;

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

//	public long getSkip() {
//		return skip;
//	}
//
//	public void setSkip(long skip) {
//		this.skip = skip;
//	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String get_method() {
		return _method;
	}

	public void set_method(String _method) {
		this._method = _method;
	}

	/**
	 * @return the where
	 */
	public SvaadWhereDto getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(SvaadWhereDto where) {
		this.where = where;
	}


}
