package com.richardsolomou.atmos.model;

public class Student {

	int id;
	String name;
	String id_number;
	String card_sn;
	String created_at;
	String updated_at;

	public Student() {
	}

	public Student(String name, String id_number, String card_sn) {
		this.name = name;
		this.id_number = id_number;
		this.card_sn = card_sn;
	}

	public Student(int id, String name, String id_number, String card_sn) {
		this.id = id;
		this.name = name;
		this.id_number = id_number;
		this.card_sn = card_sn;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIDNumber(String id_number) {
		this.id_number = id_number;
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

	public String getName() {
		return this.name;
	}

	public String getIDNumber() {
		return this.id_number;
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
