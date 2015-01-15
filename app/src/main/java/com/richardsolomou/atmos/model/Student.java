package com.richardsolomou.atmos.model;

public class Student {

	int id;
	String student_id;
	String uid;
	String created_at;
	String updated_at;

	public Student() {
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setStudentID(String student_id) {
		this.student_id = student_id;
	}

	public void setUID(String uid) {
		this.uid = uid;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	public void setUpdatedAt(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getID() {
		return this.id;
	}

	public String getStudentID() {
		return this.student_id;
	}

	public String getUID() {
		return this.uid;
	}

	public String getCreatedAt() {
		return this.created_at;
	}

	public String getUpdatedAt() {
		return this.updated_at;
	}

}