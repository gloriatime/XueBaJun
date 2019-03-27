package com.example.model.myapplication;

import java.io.Serializable;

public class CollectDocument implements Serializable {
	private User user;
	private Document document;
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
}
