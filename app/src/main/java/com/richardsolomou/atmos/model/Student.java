package com.richardsolomou.atmos.model;

public class Student {

	int id;
	String student_id;
	String card_sn;
	String created_at;
	String updated_at;

	public Student() {
	}

	public Student(String student_id, String card_sn) {
		this.student_id = student_id;
		this.card_sn = card_sn;
	}

	public Student(int id, String student_id, String card_sn) {
		this.id = id;
		this.student_id = student_id;
		this.card_sn = card_sn;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setStudentID(String student_id) {
		this.student_id = student_id;
	}

	public void setCardSN(String card_sn) {
		this.card_sn = card_sn;
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

	public String getCardSN() {
		return this.card_sn;
	}

	public String getCreatedAt() {
		return this.created_at;
	}

	public String getUpdatedAt() {
		return this.updated_at;
	}

}
