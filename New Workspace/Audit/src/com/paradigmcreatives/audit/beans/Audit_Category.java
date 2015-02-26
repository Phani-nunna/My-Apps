package com.paradigmcreatives.audit.beans;

public class Audit_Category {

	private int Process_ID;
	private int Category_ID;
	private String Category_Name;

	public int getProcess_ID() {
		return Process_ID;
	}

	public void setProcess_ID(int process_ID) {
		Process_ID = process_ID;
	}

	public int getCategory_ID() {
		return Category_ID;
	}

	public void setCategory_ID(int category_ID) {
		Category_ID = category_ID;
	}

	public String getCategory_Name() {
		return Category_Name;
	}

	public void setCategory_Name(String category_Name) {
		Category_Name = category_Name;
	}

}
