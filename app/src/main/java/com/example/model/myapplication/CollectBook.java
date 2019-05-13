package com.example.model.myapplication;

import java.io.Serializable;
import java.sql.Timestamp;

public class CollectBook implements Serializable {
	private User user;
	private Book book;
	private Timestamp time;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
