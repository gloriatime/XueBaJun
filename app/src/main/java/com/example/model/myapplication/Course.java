package com.example.model.myapplication;

import com.example.model.myapplication.Book;
import com.example.model.myapplication.Teacher;

import java.util.List;

public class Course {
	private int id;
	private String name;
	private String term;
	private String intro;
	private String wcollege;
	private String prefermajor;
	private String Ctime;
	private String examiningmode;
	private Book textbbook;
	private float score;
	private int number;
	private int comment;
	private List<Teacher> teacherList;
	public Course() { }
	public int getId() { return id; }
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getWcollege() { return wcollege; }
	public void setWcollege() {this.wcollege = wcollege;}
	public String getprefermajor(){ return prefermajor;}
	public void setPrefermajor(){ this.prefermajor = prefermajor;}
	public String getTerm() { return term; }
	public void setTerm(String term) { this.term = term; }
	public void setCtime() { this.Ctime = Ctime;}

	public String getCtime() {
		return Ctime;
	}
	public void setExaminingmode()
	{
		this.examiningmode = examiningmode;
	}
	public String getExaminingmode()
	{
		return examiningmode;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getScore() {
		String sco = String.valueOf(score);
		return sco;
	}
	public void setScore(float score) {
		this.score = score;
	}


	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public void changeScore(String str)
	{
		float sc = Float.parseFloat(str);
	}
	public List<Teacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<Teacher> teacherList) {
		this.teacherList = teacherList;
	}

	public Book getTextbbook() {
		return textbbook;
	}

	public void setTextbbook(Book textbbook) {
		this.textbbook = textbbook;
	}
}
