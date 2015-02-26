package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.RestaurantListWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RestaurentRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String include;
	private String order;
	private long limit;
	private long skip;
	private String _method;
	private RestaurantListWhereDto where;

	public RestaurantListWhereDto getWhere() {
		return where;
	}

	public void setWhere(RestaurantListWhereDto where) {
		this.where = where;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

}
