package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DishWhereDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserIdWhereDto where;
	private String keys;
	private String _method;
	private String include;
	private long limit;
	private long skip;
	private String order;

	public UserIdWhereDto getWhere() {
		return where;
	}

	public void setWhere(UserIdWhereDto where) {
		this.where = where;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String get_method() {
		return _method;
	}

	public void set_method(String _method) {
		this._method = _method;
	}

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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
