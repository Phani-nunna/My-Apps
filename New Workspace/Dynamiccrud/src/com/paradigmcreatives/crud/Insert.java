package com.paradigmcreatives.crud;


import com.paradigmcreatives.crud.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Insert extends Activity {

	EditText inputContent1;
	EditText inputContent2;
	Button buttonAdd;
	Button buttonDelete;
	ListView listContent;
	SimpleCursorAdapter cursorAdapter;
	Cursor cursor;
	private Database mySQLiteAdapter;	
	public int isDeleted;

	/** 
	 * Called when the activity is first created. 
	 * 
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		inputContent1 = (EditText)findViewById(R.id.content11);

		inputContent2 = (EditText)findViewById(R.id.content22);

		buttonAdd = (Button)findViewById(R.id.add1);

		buttonDelete = (Button)findViewById(R.id.delete);

		listContent = (ListView)findViewById(R.id.contentlist1);

		mySQLiteAdapter = new Database(this);

		mySQLiteAdapter.openToWrite();

		cursor = mySQLiteAdapter.queueAll();

		String[] from = new String[]{Database.KEY_ID, Database.KEY_CONTENT1, Database.KEY_CONTENT2};

		int[] to = new int[]{R.id.id1, R.id.text11, R.id.text22};

		cursorAdapter =	new SimpleCursorAdapter(this, R.layout.rows, cursor, from, to);

		listContent.setAdapter(cursorAdapter);

		buttonAdd.setOnClickListener(buttonAddOnClickListener);

		buttonDelete.setOnClickListener(buttonDeleteOnClickListener);

	}

	Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {

			String data1 = inputContent1.getText().toString();
			String data2 = inputContent2.getText().toString();
			long isInserted = mySQLiteAdapter.insert(data1, data2);

			if(isInserted > 0)

				Toast.makeText(getApplicationContext(), "Inserted",Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(getApplicationContext(), "Fail to Inserted",Toast.LENGTH_SHORT ).show();

			updateList();

			dialog();
		}

	};

	Button.OnClickListener buttonDeleteOnClickListener = new Button.OnClickListener(){


		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int isDeleteAll =  mySQLiteAdapter.deleteAll();

			if(isDeleteAll > 0) {
				Toast.makeText(getApplicationContext(), " Deleted", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(getApplicationContext(), "Fail to Deleted", Toast.LENGTH_SHORT).show();

			}


			updateList();
		    dialog();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mySQLiteAdapter.close();
	}

	private void updateList(){
		cursor.requery();

	}

	/** Dialogue box
	 *  update
	 *  delete
	 */
	public void  dialog() {


		listContent.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


				Toast.makeText(getApplicationContext(), "No. of rows: " + cursor.getCount(), Toast.LENGTH_SHORT).show();

				Toast.makeText(getApplicationContext(), "Position: " + cursor.getPosition(), Toast.LENGTH_SHORT).show();

				final int item_id = cursor.getInt(cursor.getColumnIndex(mySQLiteAdapter.KEY_ID));

				String item_content1 = cursor.getString(cursor.getColumnIndex(mySQLiteAdapter.KEY_CONTENT1));

				String item_content2 = cursor.getString(cursor.getColumnIndex(mySQLiteAdapter.KEY_CONTENT2));


				AlertDialog.Builder myDialog = new AlertDialog.Builder(Insert.this);

				myDialog.setTitle("Delete/Edit?");

				//...................TEXT VIEW.........................

				TextView dialogTxt_id = new TextView(Insert.this);

				LayoutParams dialogTxt_idLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				dialogTxt_id.setLayoutParams(dialogTxt_idLayoutParams);

				dialogTxt_id.setText("#" + String.valueOf(item_id));
				//............................EDITTEXT 1..................................................................
				final EditText dialogC1_id = new EditText(Insert.this);

				LayoutParams dialogC1_idLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

				dialogC1_id.setLayoutParams(dialogC1_idLayoutParams);

				dialogC1_id.setText(item_content1);

				//............................EDIT TEXT2...........................................................................

				final EditText dialogC2_id = new EditText(Insert.this);

				LayoutParams dialogC2_idLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

				dialogC2_id.setLayoutParams(dialogC2_idLayoutParams);

				dialogC2_id.setText(item_content2);

				//..................LINEAR LAYOUT.................................................................................

				LinearLayout layout = new LinearLayout(Insert.this);

				layout.setOrientation(LinearLayout.VERTICAL);

				layout.addView(dialogTxt_id);

				layout.addView(dialogC1_id);

				layout.addView(dialogC2_id);

				myDialog.setView(layout);

				//............................DIALOGUE BOX...................................................................

				myDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {

						mySQLiteAdapter.delete(item_id);

						updateList();
					}
				});
				//............................UPDATE BUTTON.................................................

				myDialog.setNeutralButton("Update", new DialogInterface.OnClickListener() {

					// do something when the button is clicked

					public void onClick(DialogInterface arg0, int arg1) {

						String value1 = dialogC1_id.getText().toString();

						String value2 = dialogC2_id.getText().toString();

						mySQLiteAdapter.update_byID(item_id, value1, value2);

						updateList();
					}
				});
				//...........................CANCEL BUTTON.......................................................................................

				myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {


					}
				});

				//................CALLS DIALOGE BOX.........................
				myDialog.show();



			}
		});	

	}
}




