package com.example.model.myapplication;

import java.util.List;

public class Course {
	private int Id;
	private String name;
	private String term;
	private String intro;
	private float score;
	private int number;
	private int comment;
	private Book book;

	// 申请查看用户
	private String applicant;

	// 所属评论列表
	private List<Comment> commentList;

	// 对应标签序列
	private List<Tag> tagList;

	// 对应教师关系列表
	private List<ProfessorCourse> professorCourseList;

	// 搜索功能
	private List<Course> courseList;

	// 推荐功能--猜你喜欢
	private  List<Course> recommendList;

	// 20热门
	private List<Course> topTwentyList;

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public float getScore() {
		return score;
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
	public List<Course> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	// 重写相同判别法，过滤推荐列表中的相同元素
	@Override
	public int hashCode() {
		return Id;
	}
	@Override
	public boolean equals(Object x){
		if(this.getClass() != x.getClass())
			return false;
		Course c = (Course)x;
		return this.Id == c.Id;
	}
	public List<Course> getRecommendList() {
		return recommendList;
	}
	public void setRecommendList(List<Course> recommendList) {
		this.recommendList = recommendList;
	}
	public List<Course> getTopTwentyList() {
		return topTwentyList;
	}
	public void setTopTwentyList(List<Course> topTwentyList) {
		this.topTwentyList = topTwentyList;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public List<ProfessorCourse> getProfessorCourseList() {
		return professorCourseList;
	}
	public void setProfessorCourseList(List<ProfessorCourse> professorCourseList) {
		this.professorCourseList = professorCourseList;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

}