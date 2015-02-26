package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CamDishBranchIdDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String __type;
	private String className;
	private String objectId;
	/**
	 * @return the __type
	 */
	public String get__type() {
		return __type;
	}
	/**
	 * @param __type the __type to set
	 */
	public void set__type(String __type) {
		this.__type = __type;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
