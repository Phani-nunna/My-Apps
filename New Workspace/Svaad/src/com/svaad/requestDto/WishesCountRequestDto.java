package com.svaad.requestDto;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.whereDto.WishCountWhereDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WishesCountRequestDto implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WishCountWhereDto where;
	private String include;
	private long limit;
	private long skip;
	private String order;
	private String _method;
	private String keys;
	
	public WishCountWhereDto getWhere() {
		return where;
	}
	public void setWhere(WishCountWhereDto where) {
		this.where = where;
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
	/**
	 * @return the _method
	 */
	public String get_method() {
		return _method;
	}
	/**
	 * @param _method the _method to set
	 */
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
	

}
