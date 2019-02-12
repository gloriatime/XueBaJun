package com.example.model.myapplication;

import java.io.Serializable;
import java.util.Date;

public class Comment{
	private int id;
	private User critic;
	private Comment reply;
	private Date date;
	private String content;
	private String type;
	private int belong;

	private Document document;
	private Book book;
	private Course course;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getCritic() {
		return critic;
	}
	public void setCritic(User critic) {
		this.critic = critic;
	}
	public Comment getReply() {
		return reply;
	}
	public void setReply(Comment reply) {
		this.reply = reply;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getBelong() {
		return belong;
	}
	public void setBelong(int belong) {
		this.belong = belong;
	}
}
