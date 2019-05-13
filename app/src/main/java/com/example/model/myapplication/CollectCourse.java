package com.example.model.myapplication;


import java.io.Serializable;
import java.sql.Timestamp;

public class CollectCourse implements Serializable {
	private User user;
	private Course course;
	private Timestamp time;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}