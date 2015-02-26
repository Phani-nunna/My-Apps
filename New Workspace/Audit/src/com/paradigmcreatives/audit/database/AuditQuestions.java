package com.paradigmcreatives.audit.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.paradigmcreatives.audit.AuditLoginActivity;
import com.paradigmcreatives.audit.R;
import com.paradigmcreatives.audit.SendServerData;
import com.paradigmcreatives.audit.beans.Audit_Questions;
import com.paradigmcreatives.audit.beans.Banner_Images;
import com.paradigmcreatives.audit.beans.Question_Answers;
import com.paradigmcreatives.audit.camera.CameraView;
import com.paradigmcreatives.audit.util.Utility;

public class AuditQuestions extends Activity implements OnClickListener {

	private EditText remark_editText, find_question, editText;
	private Button previous_btn, next_btn, cam_btn;
	private TextView question, questionCount, categoryName;
	private Bitmap bitmap;
	private ImageView priviewImageView;
	private boolean isChecked = false;
	private RadioButton[] rb;
	private CheckBox[] cb;
	private String response = "";
	private String remark = "";
	private String sup_name = "";
	private String optr_name = "";
	private String tech_name = "";
	private String question_id = "one";
	private String category_name = "";
	private byte[] byteImage1 = null;
	private ArrayList<Integer> checkCount = new ArrayList<Integer>();
	private Cursor cursor;
	private AuditDataBase mySQLiteAdapter;
	private MultiAutoCompleteTextView operaternames, supervisornames,
			technicalnames;
	public int isDeleted;
	private int type_of_response;
	private int count_of_response;
	private String[] radioOptions;
	private String[] checkOptions;
	private LinearLayout lp;
	private RadioGroup rg;
	private int mYear;
	private int mMonth;
	private int mDay;
	TextView tv, tv1;
	private int myHour;
	private int myMinute;
	private ArrayList<Audit_Questions> quArrayList;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private int currentQuestion;
	// this strings belongs to MultiAutoCompleteTextView
	String operator_string[] = { "Ramarao", "Vamsi", "hari", "bhaskar",
			"rajesh", "nagesh", "narendra", "phani" };

	String supervisor_string[] = { "Ramarao", "Vamsi", "hari", "bhaskar",
			"rajesh", "nagesh", "narendra", "phani" };

	String technical_string[] = { "Ramarao", "Vamsi", "hari", "bhaskar",
			"rajesh", "nagesh", "narendra", "phani" };

	/**
	 * Called when the activity is first created.
	 * 
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audit_questions);
		currentQuestion = 0;
		// MultiAutoCompleteTextView datas
		operaternames = (MultiAutoCompleteTextView) findViewById(R.id.auto_operator);
		supervisornames = (MultiAutoCompleteTextView) findViewById(R.id.auto_supervisor);
		technicalnames = (MultiAutoCompleteTextView) findViewById(R.id.auto_technical);

		operaternames.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, operator_string));
		operaternames
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		operaternames.setDropDownHeight(400);
		operaternames.setText(" ");
		supervisornames.setText(" ");
		technicalnames.setText(" ");
		supervisornames
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line,
						supervisor_string));
		supervisornames
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		supervisornames.setDropDownHeight(500);

		technicalnames.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, technical_string));
		technicalnames
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		technicalnames.setDropDownHeight(500);

		// buttons
		previous_btn = (Button) findViewById(R.id.previous_btn);
		next_btn = (Button) findViewById(R.id.next_btn);
		cam_btn = (Button) findViewById(R.id.cam_btn);
		next_btn.setOnClickListener(this);
		previous_btn.setOnClickListener(this);
		cam_btn.setOnClickListener(this);

		// textviews
		// operatorName = (TextView) findViewById(R.id.text_operator);
		// supervisorName = (TextView) findViewById(R.id.text_supervisor);
		// technicalName = (TextView) findViewById(R.id.text_technical);
		question = (TextView) findViewById(R.id.question_textView);
		// questionNo = (TextView)findViewById(R.id.question_no);
		questionCount = (TextView) findViewById(R.id.count);
		categoryName = (TextView) findViewById(R.id.category_name);
		// edittexts
		remark_editText = (EditText) findViewById(R.id.remark_editText);
		find_question = (EditText) findViewById(R.id.find_question);

		// image
		priviewImageView = (ImageView) findViewById(R.id.preview_imageView);

		lp = (LinearLayout) findViewById(R.id.audit_controls_layout);
		question.requestFocus();
		remark_editText.setText(" ");
		mySQLiteAdapter = new AuditDataBase(this);
		mySQLiteAdapter.openToRead();
		String processName = getIntent().getStringExtra("process_name");
		System.out.println("processname =" + processName);
		if (processName != null) {
			quArrayList = mySQLiteAdapter.getAllQuestions(processName);
			System.out.println("listSize =" + quArrayList.size());
		}
		if (quArrayList != null) {
			insertingData(quArrayList.get(currentQuestion), quArrayList.size());
		}

	}

	private void insertingData(Audit_Questions current_question, int size) {
		category_name = mySQLiteAdapter.getCategoryName(current_question
				.getCategory_Id());
		categoryName.setText(category_name);
		String qsno = "" + (currentQuestion + 1) + "."
				+ current_question.getQuestion();
		question.setText(qsno);
		String topcount = "" + (currentQuestion + 1) + "/" + size;
		questionCount.setText(topcount);
		// String type_response =
		// mySQLiteAdapter.getResponseType(current_question.getControl_ID());
		type_of_response = current_question.getControl_ID();
		count_of_response = current_question.getNo_of_Controls();

		switch (type_of_response) {

		case 1:

			lp.removeAllViews();
			ArrayList<String> responseValuesOfQuestion = mySQLiteAdapter
					.getResponseVales(current_question.getQuestion_ID());
			radioOptions = responseValuesOfQuestion
					.toArray(new String[responseValuesOfQuestion.size()]);
			rb = new RadioButton[radioOptions.length];
			rg = new RadioGroup(this); // create the RadioGroup
			rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.HORIZONTAL
			for (int i = 0; i < count_of_response; i++) {
				rb[i] = new RadioButton(this);
				rg.addView(rb[i]);
				rb[i].setText(radioOptions[i]);
				rb[i].setTextColor(Color.BLACK);
			}
			lp.addView(rg);
			break;
		case 2:
			lp.removeAllViews();
			ArrayList<String> responseValuesOfQuestion1 = mySQLiteAdapter
					.getResponseVales(current_question.getQuestion_ID());
			checkOptions = responseValuesOfQuestion1
					.toArray(new String[responseValuesOfQuestion1.size()]);
			cb = new CheckBox[checkOptions.length];
			for (int i = 0; i < count_of_response; i++) {
				cb[i] = new CheckBox(this);
				cb[i].setText(checkOptions[i]);
				cb[i].setTextColor(Color.BLACK);
				lp.addView(cb[i]);

			}
			break;
		case 3:
			lp.requestFocus();
			lp.removeAllViews();
			ArrayList<String> responseValuesOfQuestion2 = mySQLiteAdapter
					.getResponseVales(current_question.getQuestion_ID());
			editText = new EditText(this);
			editText.setHint(responseValuesOfQuestion2.get(0));
			lp.addView(editText);
			/*
			 * Button but = new Button(this); LinearLayout.LayoutParams lps =
			 * new LinearLayout.LayoutParams(96, 96);
			 * but.setBackgroundResource(R.drawable.timedate); but.setId(22);
			 * but.setOnClickListener(this); lp.addView(but, lps);
			 */
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.cam_btn:
			Intent intent = new Intent(this, CameraView.class);
			startActivityForResult(intent, 1);
			break;

		case 22:
			isChecked = true;
			Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(DATE_DIALOG_ID);
			break;

		case 44:
			final Calendar ca = Calendar.getInstance();
			myHour = ca.get(Calendar.HOUR_OF_DAY);
			myMinute = ca.get(Calendar.MINUTE);
			showDialog(TIME_DIALOG_ID);
			break;

		case R.id.next_btn:

			switch (type_of_response) {

			case 1:
				for (int i = 0; i < count_of_response; i++) {
					if (rb[i].isChecked()) {
						response = radioOptions[i];
						isChecked = true;
					}
				}
				break;
			case 2:
				for (int i = 0; i < count_of_response; i++) {
					if (cb[i].isChecked()) {
						isChecked = true;
						response = checkOptions[i];
						checkCount.add(i);
					}
				}
				break;
			case 3:
				response = editText.getText().toString().trim();
				if (response.length() > 0) {
					isChecked = true;
				}
				break;
			default:

			}
			remark = remark_editText.getText().toString().trim();
			String optrName = operaternames.getText().toString();
			if (optrName.length() > 0) {
				try {

					optr_name = optrName
							.substring(0, optrName.lastIndexOf(","));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String supName = supervisornames.getText().toString();
			if (supName.length() > 0) {
				try {

					sup_name = supName.substring(0, supName.lastIndexOf(","));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String techName = technicalnames.getText().toString();
			if (techName.length() > 0) {
				try {

					tech_name = techName
							.substring(0, techName.lastIndexOf(","));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (isChecked) {
				System.out.println("optrame " + optr_name);
				long isInserted = -1;
				mySQLiteAdapter.openToWrite();
				Question_Answers qAnswers = new Question_Answers();
				qAnswers.setResponse(response);
				qAnswers.setImage_path("http:");
				qAnswers.setOperators_names(optr_name);
				qAnswers.setSupervisors_names(sup_name);
				qAnswers.setTechnicians_names(tech_name);
				qAnswers.setQuestion_id(quArrayList.get(currentQuestion)
						.getQuestion_ID());
				qAnswers.setCategory_name(category_name);
				qAnswers.setProcess_id(quArrayList.get(currentQuestion)
						.getProcess_Id());
				qAnswers.setRemark(remark);
				isInserted = mySQLiteAdapter
						.insertIntoQuestionAnswers(qAnswers);
				lp.requestFocus();
				if (isInserted != -1) {
					operaternames.setText("");
					supervisornames.setText("");
					technicalnames.setText("");
					remark_editText.setText("");
					optr_name = "";
					sup_name = "";
					tech_name = "";
					isChecked = false;
					lp.requestFocus();
					priviewImageView.setImageBitmap(null);
					mySQLiteAdapter.openToRead();
					if (currentQuestion == quArrayList.size() - 1) {

						boolean isNetwork = Utility.getInstance()
								.isOnline(this);
						if (isNetwork) {
							JSONObject finalObject = getFinalJsonObject();
							sendData(finalObject);
							Intent login = new Intent(AuditQuestions.this,
									AuditLoginActivity.class);
							startActivity(login);
						}

						Toast.makeText(AuditQuestions.this,
								"SubmittedSuccesfully", Toast.LENGTH_SHORT)
								.show();
						Intent login = new Intent(AuditQuestions.this,
								AuditLoginActivity.class);
						startActivity(login);
					}
					currentQuestion++;
					if (currentQuestion < quArrayList.size())
						insertingData(quArrayList.get(currentQuestion),
								quArrayList.size());
					mySQLiteAdapter.close();
					if (currentQuestion == quArrayList.size() - 1) {
						next_btn.setBackgroundResource(R.drawable.submit);
					}

				} else {
					Toast.makeText(AuditQuestions.this,
							"failed to insert in Database", Toast.LENGTH_SHORT)
							.show();

				}

			} else {
				Toast.makeText(AuditQuestions.this, "fill All feilds",
						Toast.LENGTH_SHORT).show();

			}

		}

	}

	private JSONObject getFinalJsonObject() {
		ArrayList<Question_Answers> questionAnswers = mySQLiteAdapter
				.getQuestionAnswers();

		JSONObject jsonObject = new JSONObject();
		JSONArray questionAnswersArray = new JSONArray();
		try {
			if (questionAnswers != null) {
				if (questionAnswers.size() > 0) {
					for (int i = 0; i < questionAnswers.size(); i++) {
						JSONObject innerObject = new JSONObject();
						innerObject.put("Question_Id", questionAnswers.get(i)
								.getQuestion_id());
						innerObject.put("Category_Name", questionAnswers.get(i)
								.getCategory_name());
						innerObject.put("process_id", questionAnswers.get(i)
								.getProcess_id());
						innerObject.put("response", questionAnswers.get(i)
								.getResponse());
						String operatorNames = questionAnswers.get(i)
								.getOperators_names();

						System.out.println("operator name" + operatorNames);

						if (operatorNames != null && operatorNames.length() > 0) {
							operatorNames = operatorNames.trim();
							String[] arrayOfOperatorNames = operatorNames
									.split(",");
							if (arrayOfOperatorNames.length > 0) {
								JSONArray operatorArray = new JSONArray();
								for (int j = 0; j < arrayOfOperatorNames.length; j++) {
									operatorArray.put(arrayOfOperatorNames[j]);
								}
								innerObject
										.put("operator_names", operatorArray);
							}
						}
						String supervisorNames = questionAnswers.get(i)
								.getSupervisors_names();
						System.out
								.println("supervisory name" + supervisorNames);
						if (supervisorNames != null
								&& supervisorNames.length() > 0) {
							supervisorNames = supervisorNames.trim();
							String[] arrayOfSupervisorNames = supervisorNames
									.split(",");
							if (arrayOfSupervisorNames.length > 0) {
								JSONArray supervisoryArray = new JSONArray();
								for (int j = 0; j < arrayOfSupervisorNames.length; j++) {
									supervisoryArray
											.put(arrayOfSupervisorNames[j]);
								}
								innerObject.put("supervisor_names",
										supervisoryArray);
							}
						}

						String technicianNames = questionAnswers.get(i)
								.getTechnicians_names();
						System.out.println("technician name" + technicianNames);
						if (technicianNames != null
								&& technicianNames.length() > 0) {
							technicianNames = technicianNames.trim();
							String[] arrayOftechnicianNames = technicianNames
									.split(",");
							if (arrayOftechnicianNames.length > 0) {
								JSONArray technicianArray = new JSONArray();
								for (int j = 0; j < arrayOftechnicianNames.length; j++) {
									technicianArray
											.put(arrayOftechnicianNames[j]);
								}
								innerObject.put("technician_names",
										technicianArray);
							}
						}

						String remark = questionAnswers.get(i).getRemark();
						if (remark != null && remark.length() > 0) {
							remark = remark.trim();
							innerObject.put("remark", remark);
						}
						String imagePath = questionAnswers.get(i)
								.getImage_path();
						if (imagePath != null && imagePath.length() > 0) {
							innerObject.put("image_path", imagePath);
						}
						questionAnswersArray.put(innerObject);
					}
					jsonObject.put("Question_Answers", questionAnswersArray);
					
				}
			}
			
			
			ArrayList<Banner_Images>imageBanner_Images = mySQLiteAdapter.getBannerImages();
			JSONArray banner_images = new JSONArray();
			if(imageBanner_Images != null&& imageBanner_Images.size()>0){
				for(int i =0;i<imageBanner_Images.size();i++){
					banner_images.put(imageBanner_Images.get(i).getImage_Path());
					
				}
				
				jsonObject.put("Banner_images", banner_images);
			}
			System.out.println("final json" + jsonObject.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Uri imageUri = data.getData();

				try {
					File f = new File(getPath(imageUri));
					FileInputStream inputStream = new FileInputStream(f);
					BufferedInputStream buffer = new BufferedInputStream(
							inputStream);
					byteImage1 = new byte[buffer.available()];
					buffer.read(byteImage1);
					bitmap = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), imageUri);
					priviewImageView.setImageBitmap(bitmap);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mySQLiteAdapter.close();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(AuditQuestions.this, mTimeSetListener,
					myHour, myMinute, true);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(AuditQuestions.this, mDateSetListener,
					mYear, mMonth, mDay);
		}
		return null;
	}

	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			System.out.println("date "
					+ (new StringBuilder()

					.append(mMonth + 1).append("-").append(mDay).append("-")
							.append(mYear).append(" ")));

		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			myHour = hourOfDay;
			myMinute = minuteOfHour;

			System.out.println("time "
					+ (new StringBuilder().append(myHour).append(":")
							.append(myMinute).append(" ")));
			response = "" + myHour + ":" + myMinute;
		}

	};

	public void sendData(JSONObject jsonObject){
		SendServerData data = new SendServerData(AuditQuestions.this);
		JSONObject[] obj =  new JSONObject[] {jsonObject};
		data.execute(obj);
	}
}
