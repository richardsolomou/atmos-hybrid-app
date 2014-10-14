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

	private static final int DATABASE_VERSION = 5;

	private static final String DATABASE_NAME = "atmos";

	private static final String TABLE_STUDENTS = "students";

	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATED_AT = "updated_at";

	private static final String KEY_STUDENT_ID = "student_id";
	private static final String KEY_STUDENT_CARD_SN = "card_sn";

	private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_ID + " TEXT,"
			+ KEY_STUDENT_CARD_SN + " TEXT," + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT
			+ " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_STUDENTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);

		onCreate(database);
	}

	public long createStudent(Student student) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_STUDENT_ID, student.getStudentID());
		values.put(KEY_STUDENT_CARD_SN, student.getCardSN());
		values.put(KEY_CREATED_AT, student.getCreatedAt());
		values.put(KEY_UPDATED_AT, student.getUpdatedAt());

		long student_id = database.insert(TABLE_STUDENTS, null, values);

		closeDB();

		return student_id;
	}

	public Student getStudent(String student_id, String card_sn) {
		Student student = null;
		SQLiteDatabase database = this.getReadableDatabase();
		String where = KEY_STUDENT_CARD_SN + " = '" + card_sn + "'";

		if (student_id != null && card_sn != null) {
			where = KEY_ID + " = " + student_id + " AND " + KEY_STUDENT_CARD_SN + " = '" + card_sn + "'";
		} else if (student_id != null) {
			where = KEY_ID + " = " + student_id;
		}

		String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " WHERE " + where;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			student = new Student();
			student.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
			student.setStudentID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)));
			student.setCardSN(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_CARD_SN)));
			student.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
			student.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
		}

		cursor.close();
		closeDB();

		return student;
	}

	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<Student>();
		String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Student student = new Student();
				student.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
				student.setStudentID(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)));
				student.setCardSN(cursor.getString(cursor.getColumnIndex(KEY_STUDENT_CARD_SN)));
				student.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
				student.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));

				students.add(student);
			} while (cursor.moveToNext());
		}

		cursor.close();
		closeDB();

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

	public void closeDB() {
		SQLiteDatabase database = this.getReadableDatabase();
		if (database != null && database.isOpen()) database.close();
	}

	public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}
