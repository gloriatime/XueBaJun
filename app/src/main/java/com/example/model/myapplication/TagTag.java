package com.example.model.myapplication;

public class TagTag {
	private int Id;
	private int tag;
	private int belong;
	private String type;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
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