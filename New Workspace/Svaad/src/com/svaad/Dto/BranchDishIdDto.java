package com.svaad.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BranchDishIdDto extends TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BranchIdDto branchId;
	private CatIdDto catId;
	private CityDto city;
	private DishIdDto dishId;
//	private DishIdDto dishPicThumbnail;
	
	private LocationDto location;
	private String branchName;
	private List<String> commentedBy;
	private long comments;
	private String happyHour;
	private String homeDeliveryPrice;
	private long likes;
	private Boolean nonVeg;
	private PointDto point;
	private long price1;
	private long price2;
	private long price3;
	private long price4;
	private long price5;
	private long price6;
	private String priceName1;
	private String priceName2;
	private String priceName3;
	private String priceName4;
	private String priceName5;
	private String priceName6;
	private Boolean publish;
	private long regular;
	private List<String> searchTags;
	private long suggestCount;
	private String createdAt;
	private String updatedAt;
	private PlaceDto place;
	
//	private String oneTag;
//	private String twoTag;
//	private String threeTag;
//	private String fourTag;

	
	private int oneTag;
	private int twoTag;
	private int threeTag;
	private int fourTag;

	public int getOneTag() {
		return oneTag;
	}

	public void setOneTag(int oneTag) {
		this.oneTag = oneTag;
	}

	public int getTwoTag() {
		return twoTag;
	}

	public void setTwoTag(int twoTag) {
		this.twoTag = twoTag;
	}

	public int getThreeTag() {
		return threeTag;
	}

	public void setThreeTag(int threeTag) {
		this.threeTag = threeTag;
	}

	public int getFourTag() {
		return fourTag;
	}

	public void setFourTag(int fourTag) {
		this.fourTag = fourTag;
	}


	

	public BranchIdDto getBranchId() {
		return branchId;
	}

	public void setBranchId(BranchIdDto branchId) {
		this.branchId = branchId;
	}

	public CatIdDto getCatId() {
		return catId;
	}

	public void setCatId(CatIdDto catId) {
		this.catId = catId;
	}

	public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}

	public DishIdDto getDishId() {
		return dishId;
	}

	public void setDishId(DishIdDto dishId) {
		this.dishId = dishId;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public List<String> getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(List<String> commentedBy) {
		this.commentedBy = commentedBy;
	}

	public long getComments() {
		return comments;
	}

	public void setComments(long comments) {
		this.comments = comments;
	}

	public String getHappyHour() {
		return happyHour;
	}

	public void setHappyHour(String happyHour) {
		this.happyHour = happyHour;
	}

	public String getHomeDeliveryPrice() {
		return homeDeliveryPrice;
	}

	public void setHomeDeliveryPrice(String homeDeliveryPrice) {
		this.homeDeliveryPrice = homeDeliveryPrice;
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public Boolean getNonVeg() {
		return nonVeg;
	}

	public void setNonVeg(Boolean nonVeg) {
		this.nonVeg = nonVeg;
	}

	public PointDto getPoint() {
		return point;
	}

	public void setPoint(PointDto point) {
		this.point = point;
	}

	public long getPrice1() {
		return price1;
	}

	public void setPrice1(long price1) {
		this.price1 = price1;
	}

	public long getPrice2() {
		return price2;
	}

	public void setPrice2(long price2) {
		this.price2 = price2;
	}

	public long getPrice3() {
		return price3;
	}

	public void setPrice3(long price3) {
		this.price3 = price3;
	}

	public long getPrice4() {
		return price4;
	}

	public void setPrice4(long price4) {
		this.price4 = price4;
	}

	public long getPrice5() {
		return price5;
	}

	public void setPrice5(long price5) {
		this.price5 = price5;
	}

	public long getPrice6() {
		return price6;
	}

	public void setPrice6(long price6) {
		this.price6 = price6;
	}

	public String getPriceName1() {
		return priceName1;
	}

	public void setPriceName1(String priceName1) {
		this.priceName1 = priceName1;
	}

	public String getPriceName2() {
		return priceName2;
	}

	public void setPriceName2(String priceName2) {
		this.priceName2 = priceName2;
	}

	public String getPriceName3() {
		return priceName3;
	}

	public void setPriceName3(String priceName3) {
		this.priceName3 = priceName3;
	}

	public String getPriceName4() {
		return priceName4;
	}

	public void setPriceName4(String priceName4) {
		this.priceName4 = priceName4;
	}

	public String getPriceName5() {
		return priceName5;
	}

	public void setPriceName5(String priceName5) {
		this.priceName5 = priceName5;
	}

	public String getPriceName6() {
		return priceName6;
	}

	public void setPriceName6(String priceName6) {
		this.priceName6 = priceName6;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public long getRegular() {
		return regular;
	}

	public void setRegular(long regular) {
		this.regular = regular;
	}

	public List<String> getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(List<String> searchTags) {
		this.searchTags = searchTags;
	}

	public long getSuggestCount() {
		return suggestCount;
	}

	public void setSuggestCount(long suggestCount) {
		this.suggestCount = suggestCount;
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
	 * @return the place
	 */
	public PlaceDto getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(PlaceDto place) {
		this.place = place;
	}
//	public String getOneTag() {
//		return oneTag;
//	}
//
//	public void setOneTag(String oneTag) {
//		this.oneTag = oneTag;
//	}
//
//	public String getTwoTag() {
//		return twoTag;
//	}
//
//	public void setTwoTag(String twoTag) {
//		this.twoTag = twoTag;
//	}
//
//	public String getThreeTag() {
//		return threeTag;
//	}
//
//	public void setThreeTag(String threeTag) {
//		this.threeTag = threeTag;
//	}
//
//	public String getFourTag() {
//		return fourTag;
//	}
//
//	public void setFourTag(String fourTag) {
//		this.fourTag = fourTag;
//	}

//	/**
//	 * @return the dishPicThumbnail
//	 */
//	public DishIdDto getDishPicThumbnail() {
//		return dishPicThumbnail;
//	}
//
//	/**
//	 * @param dishPicThumbnail the dishPicThumbnail to set
//	 */
//	public void setDishPicThumbnail(DishIdDto dishPicThumbnail) {
//		this.dishPicThumbnail = dishPicThumbnail;
//	}

	
}
