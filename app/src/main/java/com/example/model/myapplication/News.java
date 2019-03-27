package com.example.model.myapplication;

import java.io.Serializable;

public class News implements Serializable {
	private int id;
	private String content;
	private User belong;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getBelong() {
		return belong;
	}
	public void setBelong(User belong) {
		this.belong = belong;
	}
}