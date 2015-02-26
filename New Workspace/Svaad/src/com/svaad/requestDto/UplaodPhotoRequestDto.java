package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.svaad.Dto.DishPicDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UplaodPhotoRequestDto extends EventRequestDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DishPicDto dishPhoto;
	
	private long imgHeight;
	private long imgWidth;
	private long dishTag;

	public DishPicDto getDishPhoto() {
		return dishPhoto;
	}

	public void setDishPhoto(DishPicDto dishPhoto) {
		this.dishPhoto = dishPhoto;
	}

	/**
	 * @return the imgHeight
	 */
	public long getImgHeight() {
		return imgHeight;
	}

	/**
	 * @param imgHeight the imgHeight to set
	 */
	public void setImgHeight(long imgHeight) {
		this.imgHeight = imgHeight;
	}

	/**
	 * @return the imgWidth
	 */
	public long getImgWidth() {
		return imgWidth;
	}

	/**
	 * @param imgWidth the imgWidth to set
	 */
	public void setImgWidth(long imgWidth) {
		this.imgWidth = imgWidth;
	}

	/**
	 * @return the dishTag
	 */
	public long getDishTag() {
		return dishTag;
	}

	/**
	 * @param dishTag the dishTag to set
	 */
	public void setDishTag(long dishTag) {
		this.dishTag = dishTag;
	}

}
