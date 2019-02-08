package com.example.model.myapplication;

import java.sql.Date;

public class Document {
	private int Id;
	private String name;
	private String author;
	private String up_user;
	private Date up_time;
	private float score;
	private int number;
	private int comment;
	private int download;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUp_user() {
		return up_user;
	}
	public void setUp_user(String up_user) {
		this.up_user = up_user;
	}
	public Date getUp_time() {
		return up_time;
	}
	public void setUp_time(Date up_time) {
		this.up_time = up_time;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}
	

}
