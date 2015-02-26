package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserIdDto extends TableObjectDto implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserIdInQueryDto $inQuery;

	/**
	 * @return the $inQuery
	 */
	public UserIdInQueryDto get$inQuery() {
		return $inQuery;
	}

	/**
	 * @param $inQuery the $inQuery to set
	 */
	public void set$inQuery(UserIdInQueryDto $inQuery) {
		this.$inQuery = $inQuery;
	}
}
