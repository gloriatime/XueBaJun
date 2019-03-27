package com.example.model.myapplication;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
	private int id;
	private String name;
	private String author;
	private String press;
	private String intro;
	private float score;
	private int number;
	private int comment;
	private int download;
	private String cover;
	private Course course;

	// 申请查看用户
	private String applicant;

	// 所属评论列表
	private List<Comment> commentList;

	// 对应标签序列
	private List<Tag> tagList;

	// 搜索功能
	private List<Book> bookList;

	// 推荐功能--猜你喜欢
	private List<Book> recommendList;

	// 推荐功能--20热门
	private List<Book> topTwentyList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
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

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}

	// 重写相同判别法，过滤推荐列表中的相同元素
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object x) {
		if (this.getClass() != x.getClass())
			return false;
		Book b = (Book) x;
		return this.id == b.getId();
	}

	public List<Book> getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(List<Book> recommendList) {
		this.recommendList = recommendList;
	}

	public List<Book>  getTopTwentyList() {
		return topTwentyList;
	}

	public void setTopTwentyList(List<Book> topTwentyList) {
		this.topTwentyList = topTwentyList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public List<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
}