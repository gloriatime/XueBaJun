package com.example.model.myapplication;

import java.io.Serializable;
import java.sql.Timestamp;

public class CollectDocument implements Serializable {
	private User user;
	private Document document;
	private Timestamp time;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
