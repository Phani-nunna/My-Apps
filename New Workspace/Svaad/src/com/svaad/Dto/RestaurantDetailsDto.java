package com.svaad.Dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RestaurantDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BranchTypeDto branchType;
	private CityDto city;
	private CountryDto country;
	private LocationDto location;
	private SpotIdDto spotId;
	private StateDto state;
	private boolean ac;
	private boolean bar;
	private boolean breakfast;
	private boolean catering;
	private String branchName;
	private String building;
	private String contactName;
	private CoverPicDto coverPic;
	private CoverPicDto coverPicSmall;
	private boolean creditCard;
	private boolean dinein;
	private String email;
	private boolean events;
	private boolean extraCharges;
	private String extraChargesRate;
	private String fax;
	private boolean homeDelivery;
	private String homeDeliveryLink;
	private String landmark;
	private boolean outdoorDinein;
	private boolean outsideSeating;
	private String overAllRating;
	private boolean parking;
	private String parkingFee;
	private String phone1;
	private String phone2;
	private String phone3;
	private String phone4;
	private String pincode;
	private String pintBeer;
	private PointDto point;
	private boolean publish;
	private boolean pureNonVeg;
	private boolean pureVeg;
	private boolean reservation;
	private boolean reservationMust;
	private String reservationMustLink;
	private String[] searchTags;
	private boolean serviceCharge;
	private boolean smokingZone;
	private boolean stag;
	private boolean takeaway;
	private boolean vat;
	private boolean vegan;
	private boolean walletParking;
	private boolean wifi;
	private String serviceChargeRate;
	private String street;
	private String vatRate;
	private String walletParkingFee;
	private String createdAt;
	private String updatedAt;
	private String objectId;
	private String timings;
	private CoverPicDto photo;
	private String name;
	

	public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto country) {
		this.country = country;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public SpotIdDto getSpotId() {
		return spotId;
	}

	public void setSpotId(SpotIdDto spotId) {
		this.spotId = spotId;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public boolean isAc() {
		return ac;
	}

	public void setAc(boolean ac) {
		this.ac = ac;
	}

	public boolean isBar() {
		return bar;
	}

	public void setBar(boolean bar) {
		this.bar = bar;
	}

	public boolean isBreakfast() {
		return breakfast;
	}

	public void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}

	public boolean isCatering() {
		return catering;
	}

	public void setCatering(boolean catering) {
		this.catering = catering;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public CoverPicDto getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(CoverPicDto coverPic) {
		this.coverPic = coverPic;
	}

	public CoverPicDto getCoverPicSmall() {
		return coverPicSmall;
	}

	public void setCoverPicSmall(CoverPicDto coverPicSmall) {
		this.coverPicSmall = coverPicSmall;
	}

	public boolean isCreditCard() {
		return creditCard;
	}

	public void setCreditCard(boolean creditCard) {
		this.creditCard = creditCard;
	}

	public boolean isDinein() {
		return dinein;
	}

	public void setDinein(boolean dinein) {
		this.dinein = dinein;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEvents() {
		return events;
	}

	public void setEvents(boolean events) {
		this.events = events;
	}

	public boolean isExtraCharges() {
		return extraCharges;
	}

	public void setExtraCharges(boolean extraCharges) {
		this.extraCharges = extraCharges;
	}

	public String getExtraChargesRate() {
		return extraChargesRate;
	}

	public void setExtraChargesRate(String extraChargesRate) {
		this.extraChargesRate = extraChargesRate;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public boolean isHomeDelivery() {
		return homeDelivery;
	}

	public void setHomeDelivery(boolean homeDelivery) {
		this.homeDelivery = homeDelivery;
	}

	public String getHomeDeliveryLink() {
		return homeDeliveryLink;
	}

	public void setHomeDeliveryLink(String homeDeliveryLink) {
		this.homeDeliveryLink = homeDeliveryLink;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public boolean isOutdoorDinein() {
		return outdoorDinein;
	}

	public void setOutdoorDinein(boolean outdoorDinein) {
		this.outdoorDinein = outdoorDinein;
	}

	public boolean isOutsideSeating() {
		return outsideSeating;
	}

	public void setOutsideSeating(boolean outsideSeating) {
		this.outsideSeating = outsideSeating;
	}

	public String getOverAllRating() {
		return overAllRating;
	}

	public void setOverAllRating(String overAllRating) {
		this.overAllRating = overAllRating;
	}

	public boolean isParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}

	public String getParkingFee() {
		return parkingFee;
	}

	public void setParkingFee(String parkingFee) {
		this.parkingFee = parkingFee;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getPhone4() {
		return phone4;
	}

	public void setPhone4(String phone4) {
		this.phone4 = phone4;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPintBeer() {
		return pintBeer;
	}

	public void setPintBeer(String pintBeer) {
		this.pintBeer = pintBeer;
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

	public boolean isPureNonVeg() {
		return pureNonVeg;
	}

	public void setPureNonVeg(boolean pureNonVeg) {
		this.pureNonVeg = pureNonVeg;
	}

	public boolean isPureVeg() {
		return pureVeg;
	}

	public void setPureVeg(boolean pureVeg) {
		this.pureVeg = pureVeg;
	}

	public boolean isReservation() {
		return reservation;
	}

	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}

	public boolean isReservationMust() {
		return reservationMust;
	}

	public void setReservationMust(boolean reservationMust) {
		this.reservationMust = reservationMust;
	}

	public String getReservationMustLink() {
		return reservationMustLink;
	}

	public void setReservationMustLink(String reservationMustLink) {
		this.reservationMustLink = reservationMustLink;
	}

	public String[] getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(String[] searchTags) {
		this.searchTags = searchTags;
	}

	public boolean isServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(boolean serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public boolean isSmokingZone() {
		return smokingZone;
	}

	public void setSmokingZone(boolean smokingZone) {
		this.smokingZone = smokingZone;
	}

	public boolean isStag() {
		return stag;
	}

	public void setStag(boolean stag) {
		this.stag = stag;
	}

	public boolean isTakeaway() {
		return takeaway;
	}

	public void setTakeaway(boolean takeaway) {
		this.takeaway = takeaway;
	}

	public boolean isVat() {
		return vat;
	}

	public void setVat(boolean vat) {
		this.vat = vat;
	}

	public boolean isVegan() {
		return vegan;
	}

	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}

	public boolean isWalletParking() {
		return walletParking;
	}

	public void setWalletParking(boolean walletParking) {
		this.walletParking = walletParking;
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public String getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(String serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getWalletParkingFee() {
		return walletParkingFee;
	}

	public void setWalletParkingFee(String walletParkingFee) {
		this.walletParkingFee = walletParkingFee;
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

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public BranchTypeDto getBranchType() {
		return branchType;
	}

	public void setBranchType(BranchTypeDto branchType) {
		this.branchType = branchType;
	}

	public CoverPicDto getPhoto() {
		return photo;
	}

	public void setPhoto(CoverPicDto photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the timings
	 */
	public String getTimings() {
		return timings;
	}

	/**
	 * @param timings the timings to set
	 */
	public void setTimings(String timings) {
		this.timings = timings;
	}

}
