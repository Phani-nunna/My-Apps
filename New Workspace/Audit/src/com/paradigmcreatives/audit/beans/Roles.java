package com.paradigmcreatives.audit.beans;


public class Roles {

	private int Role_ID;
	private String Role_Name;
	private String Description;
	private String Created_Date;
	private String Updated_Date;
	private String Status;

	public int getRole_ID() {
		return Role_ID;
	}

	public void setRole_ID(int role_ID) {
		Role_ID = role_ID;
	}

	public String getRole_Name() {
		return Role_Name;
	}

	public void setRole_Name(String role_Name) {
		Role_Name = role_Name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getCreated_Date() {
		return Created_Date;
	}

	public void setCreated_Date(String created_Date) {
		Created_Date = created_Date;
	}

	public String getUpdated_Date() {
		return Updated_Date;
	}

	public void setUpdated_Date(String updated_Date) {
		Updated_Date = updated_Date;
	}

}
