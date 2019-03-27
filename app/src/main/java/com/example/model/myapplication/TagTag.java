package com.example.model.myapplication;

import java.io.Serializable;

public class TagTag implements Serializable {
	private int id;
	private int tag;
	private int belong;
	private String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getBelong() {
		return belong;
	}
	public void setBelong(int belong) {
		this.belong = belong;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}