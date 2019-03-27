package com.example.model.myapplication;

import java.util.List;
public class Professor {
    private int id;
    private String name;
    private String intro;
    private String field;
    private int comment;
    private float score;
    private int number;
    private String pic;

    // 教师教授课程关系列表
    private List<ProfessorCourse> professorCourseList;

    // 所属评论列表
    private List<Comment> commentList;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public int getComment() {
        return comment;
    }
    public void setComment(int comment) {
        this.comment = comment;
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
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public List<ProfessorCourse> getProfessorCourseList() {
        return professorCourseList;
    }
    public void setProfessorCourseList(List<ProfessorCourse> professorCourseList) {
        this.professorCourseList = professorCourseList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Comment> getCommentList() {
        return commentList;
    }
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
