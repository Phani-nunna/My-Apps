package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.DishProfileResWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DishProfileResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _method;
	private String include;
	private DishProfileResWhereDto where;
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
	public DishProfileResWhereDto getWhere() {
		return where;
	}
	public void setWhere(DishProfileResWhereDto where) {
		this.where = where;
	}
	

}
