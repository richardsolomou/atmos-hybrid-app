package com.richardsolomou.atmos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.richardsolomou.atmos.helper.DatabaseHelper;
import com.richardsolomou.atmos.model.Student;


public class AddStudent extends Activity {

	DatabaseHelper db;
	EditText studentID;
	String cardSN;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_student);

		db = new DatabaseHelper(getApplicationContext());
		studentID = (EditText) findViewById(R.id.studentID);

		Bundle extras = getIntent().getExtras();
		if (extras != null) cardSN = extras.getString("cardSN");

		studentID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				boolean handled = false;
				if (i == EditorInfo.IME_ACTION_DONE) {
					addStudent(textView);
					handled = true;
				}
				return handled;
			}
		});
	}

	public void addStudent(View view) {
		Student student = new Student();

		student.setStudentID(studentID.getText().toString());
		student.setCardSN(cardSN);
		student.setCreatedAt(db.getDateTime());
		student.setUpdatedAt(db.getDateTime());

		long student_id = db.createStudent(student);

		if (Long.toString(student_id) != null) {
			Toast.makeText(getApplicationContext(), "Student " + studentID.getText().toString() + " was successfully added to the database.", Toast.LENGTH_SHORT).show();
			Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(objIntent);
		} else {
			Toast.makeText(getApplicationContext(), "Failed to add student.", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancelAdd(View view) {
		Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(objIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

}