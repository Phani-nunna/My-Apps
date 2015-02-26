package com.paradigmcreatives.audit.beans;

public class Question_Answers {
	private int question_id;
	private int process_id;
	private String response;
	private String operators_names;
	private String supervisors_names;
	private String technicians_names;
	private String remark;
	private String category_name;
	private String image_path;

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public int getProcess_id() {
		return process_id;
	}

	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getOperators_names() {
		return operators_names;
	}

	public void setOperators_names(String operators_names) {
		this.operators_names = operators_names;
	}

	public String getSupervisors_names() {
		return supervisors_names;
	}

	public void setSupervisors_names(String supervisors_names) {
		this.supervisors_names = supervisors_names;
	}

	public String getTechnicians_names() {
		return technicians_names;
	}

	public void setTechnicians_names(String technicians_names) {
		this.technicians_names = technicians_names;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

}
