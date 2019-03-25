package com.example.model.myapplication;


import java.util.Date;
import java.util.List;

public class Document {
	private int Id;
	private String name = "没名字";
	private String author = "佚名";
	private String up_user;
	private Date up_time = new Date();
	private float score;
	private int number;
	private int comment;
	private int download;
	private String url;


	// 申请查看用户
	private String applicant;

	// 所属评论列表
	private List<Comment> commentList;

	// 搜索功能
	private List<Document> documentList;

	// 对应标签序列
	private List<Tag> tagList;

	// 推荐功能--猜你喜欢
	private List<Document> recommendList;

	public Document(){}

	public Document(String name, int number, int comment){
	    this.name = name;
	    this.comment = comment;
	    this.number = number;
    }

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		this.Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUp_user() {
		return up_user;
	}
	public void setUp_user(String up_user) {
		this.up_user = up_user;
	}
	public Date getUp_time() {
		return up_time;
	}
	public void setUp_time(Date up_time) {
		this.up_time = up_time;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public List<Document> getDocumentList() {
		return documentList;
	}
	public void setDocumentList(List<Document> documentList) {
		this.documentList = documentList;
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
		Document d = (Document)x;
		return this.Id == d.Id;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public List<Document> getRecommendList() {
		return recommendList;
	}
	public void setRecommendList(List<Document> recommendList) {
		this.recommendList = recommendList;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

}
