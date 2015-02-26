package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String __type;
	private String className;
	private String objectId;

	@JsonProperty("__type")
	public String get__type() {
		return __type;
	}

	@JsonProperty("__type")
	public void set__type(String __type) {
		this.__type = __type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
