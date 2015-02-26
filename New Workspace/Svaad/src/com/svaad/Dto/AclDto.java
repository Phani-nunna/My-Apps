package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AclDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadWriteDto userId;
	private ReadWriteDto star;

	public ReadWriteDto getUserId() {
		return userId;
	}

	public void setUserId(ReadWriteDto userId) {
		this.userId = userId;
	}

	public ReadWriteDto getStar() {
		return star;
	}

	public void setStar(ReadWriteDto star) {
		this.star = star;
	}

}
