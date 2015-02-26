package com.paradigmcreatives.audit.beans;

public class Villages {

	private int village_id;
	private int state_id;
	private int district_id;
	private int taluka_id;
	private String village_name;

	public int getVillage_id() {
		return village_id;
	}

	public void setVillage_id(int village_id) {
		this.village_id = village_id;
	}

	public int getState_id() {
		return state_id;
	}

	public void setState_id(int state_id) {
		this.state_id = state_id;
	}

	public int getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}

	public int getTaluka_id() {
		return taluka_id;
	}

	public void setTaluka_id(int taluka_id) {
		this.taluka_id = taluka_id;
	}

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}

}
