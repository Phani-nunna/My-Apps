package com.paradigmcreatives.audit.beans;

public class Audit_Questions {

	private int Process_Id;
	private int Category_Id;
	private int Question_ID;
	private String Question;
	private int No_of_Controls;
	private int Control_ID;

	public int getProcess_Id() {
		return Process_Id;
	}

	public void setProcess_Id(int process_Id) {
		Process_Id = process_Id;
	}

	public int getCategory_Id() {
		return Category_Id;
	}

	public void setCategory_Id(int category_Id) {
		Category_Id = category_Id;
	}

	public int getQuestion_ID() {
		return Question_ID;
	}

	public void setQuestion_ID(int question_ID) {
		Question_ID = question_ID;
	}

	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String question) {
		Question = question;
	}

	public int getNo_of_Controls() {
		return No_of_Controls;
	}

	public void setNo_of_Controls(int no_of_Controls) {
		No_of_Controls = no_of_Controls;
	}

	public int getControl_ID() {
		return Control_ID;
	}

	public void setControl_ID(int control_ID) {
		Control_ID = control_ID;
	}

}
