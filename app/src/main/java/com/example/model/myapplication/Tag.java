package com.example.model.myapplication;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable {
	private int id;
	private String name;
	private String type;
	private int times;// 被引用次数

	private List<Tag> tagList;// 模糊查询结果列表

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
}