package com.svaad.whereDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.SvaadPointDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CamRestaurantListWhereDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean publish;
	private SearchTagsDto nameTags;
	private SvaadPointDto point;

	/**
	 * @return the publish
	 */
	public boolean isPublish() {
		return publish;
	}

	/**
	 * @param publish
	 *            the publish to set
	 */
	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	/**
	 * @return the nameTags
	 */
	public SearchTagsDto getNameTags() {
		return nameTags;
	}

	/**
	 * @param nameTags
	 *            the nameTags to set
	 */
	public void setNameTags(SearchTagsDto nameTags) {
		this.nameTags = nameTags;
	}

	/**
	 * @return the point
	 */
	public SvaadPointDto getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(SvaadPointDto point) {
		this.point = point;
	}

}
