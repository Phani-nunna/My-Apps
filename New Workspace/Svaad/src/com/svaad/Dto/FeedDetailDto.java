package com.svaad.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FeedDetailDto extends TableObjectDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Feed Response
	private BranchDishIdDto branchDishId;
	private FeedUserIdDto userId;
	private String commentText;
	private DishPhotoDto dishPhoto;
	private DishPhotoDto dishPhotoThumbnail;
	private String createdAt;
	private String updatedAt;
	private String dishTag;
	
	
	//Restaurant view full menu response
	private BranchIdDto branchId;
	private CityDto city;
	private DishIdDto dishId;
	private PlaceDto place;
	private LocationDto location;
	private String happyHour;
	private String homeDeliveryPrice;
	private long likes;
	private boolean nonVeg;
	private PointDto point;
	private boolean publish;
	private boolean recommended;
	private long regular;
	//private String[] searchTags;
	private List<String> searchTags;
	private long suggestCount;
	private String objectId;
	private String branchName;

	private CatIdDto catId;
	private String[] commentedBy;
	private long comments;
	private List<String> likedBy;
	private long shareCount;
	private int position;

	private int price1;
	private int price2;
	private int price3;
	private int price4;
	private int price5;
	private int price6;
	private String priceName1;
	private String priceName2;
	private String priceName3;
	private String priceName4;
	private String priceName5;
	private String priceName6;
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

	private int oneTag;
	private int twoTag;
	private int threeTag;
	private int fourTag;
	
	private String seeMore;
	
	
	public BranchIdDto getBranchId() {
		return branchId;
	}

	public void setBranchId(BranchIdDto branchId) {
		this.branchId = branchId;
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

	public PlaceDto getPlace() {
		return place;
	}

	public void setPlace(PlaceDto place) {
		this.place = place;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
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

	public boolean isNonVeg() {
		return nonVeg;
	}

	public void setNonVeg(boolean nonVeg) {
		this.nonVeg = nonVeg;
	}

	public PointDto getPoint() {
		return point;
	}

	public void setPoint(PointDto point) {
		this.point = point;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	public long getRegular() {
		return regular;
	}

	public void setRegular(long regular) {
		this.regular = regular;
	}



	public long getSuggestCount() {
		return suggestCount;
	}

	public void setSuggestCount(long suggestCount) {
		this.suggestCount = suggestCount;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public CatIdDto getCatId() {
		return catId;
	}

	public void setCatId(CatIdDto catId) {
		this.catId = catId;
	}

	public String[] getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(String[] commentedBy) {
		this.commentedBy = commentedBy;
	}

	public long getComments() {
		return comments;
	}

	public void setComments(long comments) {
		this.comments = comments;
	}

	public List<String> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<String> likedBy) {
		this.likedBy = likedBy;
	}

	public long getShareCount() {
		return shareCount;
	}

	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPrice1() {
		return price1;
	}

	public void setPrice1(int price1) {
		this.price1 = price1;
	}

	public int getPrice2() {
		return price2;
	}

	public void setPrice2(int price2) {
		this.price2 = price2;
	}

	public int getPrice3() {
		return price3;
	}

	public void setPrice3(int price3) {
		this.price3 = price3;
	}

	public int getPrice4() {
		return price4;
	}

	public void setPrice4(int price4) {
		this.price4 = price4;
	}

	public int getPrice5() {
		return price5;
	}

	public void setPrice5(int price5) {
		this.price5 = price5;
	}

	public int getPrice6() {
		return price6;
	}

	public void setPrice6(int price6) {
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


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public BranchDishIdDto getBranchDishId() {
		return branchDishId;
	}

	public void setBranchDishId(BranchDishIdDto branchDishId) {
		this.branchDishId = branchDishId;
	}

	public FeedUserIdDto getUserId() {
		return userId;
	}

	public void setUserId(FeedUserIdDto userId) {
		this.userId = userId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public DishPhotoDto getDishPhoto() {
		return dishPhoto;
	}

	public void setDishPhoto(DishPhotoDto dishPhoto) {
		this.dishPhoto = dishPhoto;
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

	@Override
	public String toString() {
		return userId.getUname()+" "+createdAt;
		
	}
	public String getDishTag() {
		return dishTag;
	}

	public void setDishTag(String dishTag) {
		this.dishTag = dishTag;
	}

	/**
	 * @return the searchTags
	 */
	public List<String> getSearchTags() {
		return searchTags;
	}

	/**
	 * @param searchTags the searchTags to set
	 */
	public void setSearchTags(List<String> searchTags) {
		this.searchTags = searchTags;
	}

//	/**
//	 * @return the oneTag
//	 */
//	public String getOneTag() {
//		return oneTag;
//	}
//
//	/**
//	 * @param oneTag the oneTag to set
//	 */
//	public void setOneTag(String oneTag) {
//		this.oneTag = oneTag;
//	}
//
//	/**
//	 * @return the twoTag
//	 */
//	public String getTwoTag() {
//		return twoTag;
//	}
//
//	/**
//	 * @param twoTag the twoTag to set
//	 */
//	public void setTwoTag(String twoTag) {
//		this.twoTag = twoTag;
//	}
//
//	/**
//	 * @return the threeTag
//	 */
//	public String getThreeTag() {
//		return threeTag;
//	}
//
//	/**
//	 * @param threeTag the threeTag to set
//	 */
//	public void setThreeTag(String threeTag) {
//		this.threeTag = threeTag;
//	}
//
//	/**
//	 * @return the fourTag
//	 */
//	public String getFourTag() {
//		return fourTag;
//	}
//
//	/**
//	 * @param fourTag the fourTag to set
//	 */
//	public void setFourTag(String fourTag) {
//		this.fourTag = fourTag;
//	}

	/**
	 * @return the seeMore
	 */
	public String getSeeMore() {
		return seeMore;
	}

	/**
	 * @param seeMore the seeMore to set
	 */
	public void setSeeMore(String seeMore) {
		this.seeMore = seeMore;
	}

	/**
	 * @return the dishPhotoThumbnail
	 */
	public DishPhotoDto getDishPhotoThumbnail() {
		return dishPhotoThumbnail;
	}

	/**
	 * @param dishPhotoThumbnail the dishPhotoThumbnail to set
	 */
	public void setDishPhotoThumbnail(DishPhotoDto dishPhotoThumbnail) {
		this.dishPhotoThumbnail = dishPhotoThumbnail;
	}

}
