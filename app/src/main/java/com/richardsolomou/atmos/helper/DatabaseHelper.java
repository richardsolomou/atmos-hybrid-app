package com.richardsolomou.atmos.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.richardsolomou.atmos.model.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 6;

	private static final String DATABASE_NAME = "atmos";

	private static final String TABLE_STUDENTS = "students";

	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATED_AT = "updated_at";

	private static final String KEY_STUDENT_ID = "student_id";
	private static final String KEY_STUDENT_UID = "uid";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE " + TABLE_STUDENTS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_ID + " TEXT,"
				+ KEY_STUDENT_UID + " TEXT," + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT
				+ " DATETIME" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);

		onCreate(database);
	}

	public boolean createStudent(Student student) {
		ContentValues values = new ContentValues();

		values.put(KEY_STUDENT_ID, student.getStudentID());
		values.put(KEY_STUDENT_UID, student.getUID());
		values.put(KEY_CREATED_AT, student.getCreatedAt());
		values.put(KEY_UPDATED_AT, student.getUpdatedAt());

		SQLiteDatabase database = this.getWritableDatabase();

		boolean result = database.insert(TABLE_STUDENTS, null, values) > 0;
		database.close();

		return result;
	}

	public Student getStudent(String student_id, String uid) {
		Student student = null;
		String where = KEY_STUDENT_UID + " = '" + uid + "'";

		if (student_id != null && uid != null) {
			where = KEY_ID + " = " + student_id + " AND " + KEY_STUDENT_UID + " = '" + uid + "'";
		} else if (student_id != null) {
			where = KEY_ID + " = " + student_id;
		}

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + where, null);

		if (cursor.moveToFirst()) {
			student = new Student();
			student.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
			student.setStudentID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)));
			student.setUID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_UID)));
			student.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
			student.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
		}

		cursor.close();
		database.close();

		return student;
	}

	public boolean updateStudent(Student student) {
		ContentValues values = new ContentValues();

		values.put(KEY_STUDENT_ID, student.getStudentID());
		values.put(KEY_STUDENT_UID, student.getUID());
		values.put(KEY_CREATED_AT, student.getCreatedAt());
		values.put(KEY_UPDATED_AT, student.getUpdatedAt());

		SQLiteDatabase database = this.getWritableDatabase();

		boolean result = database.update(TABLE_STUDENTS, values, KEY_ID + " = ?", new String[]{ Integer.toString(student.getID()) }) > 0;
		database.close();

		return result;
	}

	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<Student>();

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);

		if (cursor.moveToFirst()) {
			do {
				Student student = new Student();
				student.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
				student.setStudentID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)));
				student.setUID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_UID)));
				student.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
				student.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));

				students.add(student);
			} while (cursor.moveToNext());
		}

		cursor.close();
		database.close();

		return students;
	}

	public void deleteStudent(long student_id) {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(TABLE_STUDENTS, KEY_ID + " = ?", new String[]{String.valueOf(student_id)});
	}

	public void deleteAllStudents() {
		List<Student> students = getAllStudents();

		for (Student student : students) {
			deleteStudent(student.getID());
		}
	}

	public void resetDB() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_STUDENTS + "'");
	}

	public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}
