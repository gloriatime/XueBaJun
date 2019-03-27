package com.example.model.myapplication;

import java.io.Serializable;
import java.util.List;


public class UserTag implements Serializable {
	private String user;
	private int tag;
	private int times;

	// 兴趣推荐 -此处结构只作为返回前端的载体，具体实现则位于RecommendService
	private List<Document> recommendDocumentList;
	private List<Course> recommendCourseList;
	private List<Book> recommendBookList;

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<Document> getRecommendDocumentList() {
		return recommendDocumentList;
	}
	public void setRecommendDocumentList(List<Document> recommendDocumentList) {
		this.recommendDocumentList = recommendDocumentList;
	}
	public List<Course> getRecommendCourseList() {
		return recommendCourseList;
	}
	public void setRecommendCourseList(List<Course> recommendCourseList) {
		this.recommendCourseList = recommendCourseList;
	}
	public List<Book> getRecommendBookList() {
		return recommendBookList;
	}
	public void setRecommendBookList(List<Book> recommendBookList) {
		this.recommendBookList = recommendBookList;
	}
}