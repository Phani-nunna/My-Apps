package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Base64Dto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String base64;
	private String _ContentType;

	public String get_ContentType() {
		return _ContentType;
	}

	public void set_ContentType(String _ContentType) {
		this._ContentType = _ContentType;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
