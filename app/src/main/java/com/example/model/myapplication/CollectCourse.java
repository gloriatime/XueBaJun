package com.example.model.myapplication;


import java.io.Serializable;

public class CollectCourse implements Serializable {
	private User user;
	private Course course;
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

}