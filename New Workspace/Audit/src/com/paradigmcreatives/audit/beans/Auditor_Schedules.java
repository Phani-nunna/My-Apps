package com.paradigmcreatives.audit.beans;


public class Auditor_Schedules {
	private String Process;
	private String State;
	private String District;
	private String Taluka;
	private String Village;
	private String Schedule_Date;
	private int Auditor_Emp_ID;
	private String Status;
	private String Created_date;

	public String getProcess() {
		return Process;
	}

	public void setProcess(String process) {
		Process = process;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getTaluka() {
		return Taluka;
	}

	public void setTaluka(String taluka) {
		Taluka = taluka;
	}

	public String getVillage() {
		return Village;
	}

	public void setVillage(String village) {
		Village = village;
	}

	public int getAuditor_Emp_ID() {
		return Auditor_Emp_ID;
	}

	public void setAuditor_Emp_ID(int auditor_Emp_ID) {
		Auditor_Emp_ID = auditor_Emp_ID;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSchedule_Date() {
		return Schedule_Date;
	}

	public void setSchedule_Date(String schedule_Date) {
		Schedule_Date = schedule_Date;
	}

	public String getCreated_date() {
		return Created_date;
	}

	public void setCreated_date(String created_date) {
		Created_date = created_date;
	}

}
