package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DishIdDto extends TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpotIdDto spotId;
	private String desc;
	private DishPicDto dishPic;
	private DishPicDto dishPicThumbnail;
	private long imgHeight;
	private long imgWidth;
	private String name;
	private Boolean nonVeg;
	private String tags;
	private String createdAt;
	private String updatedAt;
	

	public SpotIdDto getSpotId() {
		return spotId;
	}

	public void setSpotId(SpotIdDto spotId) {
		this.spotId = spotId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public DishPicDto getDishPic() {
		return dishPic;
	}

	public void setDishPic(DishPicDto dishPic) {
		this.dishPic = dishPic;
	}

	public long getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(long imgHeight) {
		this.imgHeight = imgHeight;
	}

	public long getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(long imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getNonVeg() {
		return nonVeg;
	}

	public void setNonVeg(Boolean nonVeg) {
		this.nonVeg = nonVeg;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the dishPicThumbnail
	 */
	public DishPicDto getDishPicThumbnail() {
		return dishPicThumbnail;
	}

	/**
	 * @param dishPicThumbnail the dishPicThumbnail to set
	 */
	public void setDishPicThumbnail(DishPicDto dishPicThumbnail) {
		this.dishPicThumbnail = dishPicThumbnail;
	}

}
