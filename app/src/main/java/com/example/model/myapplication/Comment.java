package com.example.model.myapplication;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment {
	private int id;
	private User critic;
	private Date date;
	private String type;
	private int belong;
	private String content;

	// 我的评论中需要显示所评论体
	private Book book;
	private Course course;
	private Document document;
	private Professor professor;

	// 从属的回复列表
	private List<Reply> replyList;

	public Comment(User critic, String Content, Date date)
	{
		this.critic = critic;
		this.content = Content;
		this.date = date;
	}

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
	public List<Reply> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
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
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
}