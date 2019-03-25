package com.example.model.myapplication;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
	private String name = "我好菜啊";
	private String pwd = "123456";
	private String grade;
	private String phone;
	private String college;
	private int point;
	private String head;
	private boolean art,medicine,management,humanity,technology,agriculture,play;

	// 每一个用户都拥有的列表

	// “我的关注”功能
	private List<Concern> myconcernlist;// 我关注的人
	List<Concern>concern_me_list;// 关注我的人

	// “我的收藏”功能
	private List<CollectDocument> collected_documents;// 我的收藏--资料
	private List<CollectBook> collected_books;// 我的收藏--书籍
	private List<CollectCourse> collected_courses;// 我的收藏--课程

	// "关于我的"功能
	private List<Document> myDocument;
	private List<Comment> myComment;
	private List<News> myNews;

	public User(){}
	public User(User user){
		this.name = user.getName();
		this.phone = user.getPhone();
		this.college =user.getCollege();
		this.grade = user.getGrade();
		this.point = user.getPoint();
		this.pwd = user.getPwd();
		this.agriculture = user.isAgriculture();
		this.art = user.isArt();
		this.medicine = user.isMedicine();
		this.management = user.isManagement();
		this.humanity = user.isHumanity();
		this.technology=user.isTechnology();
		this.play = user.isPlay();
		this.head = user.getHead();
		this.myconcernlist = user.getMyconcernlist();
		this.concern_me_list=user.getConcern_me_list();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public boolean isArt() {
		return art;
	}
	public void setArt(boolean art) {
		this.art = art;
	}
	public boolean isMedicine() {
		return medicine;
	}
	public void setMedicine(boolean medicine) {
		this.medicine = medicine;
	}
	public boolean isManagement() {
		return management;
	}
	public void setManagement(boolean management) {
		this.management = management;
	}
	public boolean isHumanity() {
		return humanity;
	}
	public void setHumanity(boolean humanity) {
		this.humanity = humanity;
	}
	public boolean isTechnology() {
		return technology;
	}
	public void setTechnology(boolean technology) {
		this.technology = technology;
	}
	public boolean isAgriculture() {
		return agriculture;
	}
	public void setAgriculture(boolean agriculture) {
		this.agriculture = agriculture;
	}
	public boolean isPlay() {
		return play;
	}
	public void setPlay(boolean play) {
		this.play = play;
	}

	public List<Concern> getConcern_me_list() {
		return concern_me_list;
	}
	public void setConcern_me_list(List<Concern> concern_me_list) {
		this.concern_me_list = concern_me_list;
	}
	public List<Concern> getMyconcernlist() {
		return myconcernlist;
	}
	public void setMyconcernlist(List<Concern> myconcernlist) {
		this.myconcernlist = myconcernlist;
	}
	public List<CollectDocument> getCollected_documents() {
		return collected_documents;
	}
	public void setCollected_documents(List<CollectDocument> collected_documents) {
		this.collected_documents = collected_documents;
	}
	public List<CollectBook> getCollected_books() {
		return collected_books;
	}
	public void setCollected_books(List<CollectBook> collected_books) {
		this.collected_books = collected_books;
	}
	public List<CollectCourse> getCollected_courses() {
		return collected_courses;
	}
	public void setCollected_courses(List<CollectCourse> collected_courses) {
		this.collected_courses = collected_courses;
	}
	public List<Document> getMyDocument() {
		return myDocument;
	}
	public void setMyDocument(List<Document> myDocument) {
		this.myDocument = myDocument;
	}
	public List<Comment> getMyComment() {
		return myComment;
	}
	public void setMyComment(List<Comment> myComment) {
		this.myComment = myComment;
	}
	public List<News> getMyNews() {
		return myNews;
	}
	public void setMyNews(List<News> myNews) {
		this.myNews = myNews;
	}

}