package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.AllFoodiesWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllFoodiesRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AllFoodiesWhereDto where;
	private long skip;
	private String order;

	public long getSkip() {
		return skip;
	}
	public void setSkip(long skip) {
		this.skip = skip;
	}
	public long getLimit() {
		return limit;
	}
	public void setLimit(long limit) {
		this.limit = limit;
	}
	public String get_method() {
		return _method;
	}
	public void set_method(String _method) {
		this._method = _method;
	}
	public AllFoodiesWhereDto getWhere() {
		return where;
	}
	public void setWhere(AllFoodiesWhereDto where) {
		this.where = where;
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	private long limit;
	private String _method;

}
