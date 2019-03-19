package com.example.model.myapplication;

import java.util.List;

public class Teacher {
    private int id;
    private String Tname;
    private String BelongSchool;
    private String BelongCollege;
    private String intrpeople;
    private String cover;
    private String research;
    private List<Course> courseList;
    public Teacher(){ }
    public int getId() { return id;}
    public void setId() { this.id = id;}
    public String getTname() { return Tname;}
    public void setTname() { this.Tname = Tname;}
    public String getBelongSchool() { return BelongSchool;}
    public void setBelongSchool() { this.BelongSchool = BelongSchool;}
    public String getBelongCollege() { return BelongCollege;}
    public void setBelongCollege() { this.BelongCollege = BelongCollege;}
    public String getintrpeople() { return intrpeople;}
    public void setintrpeople() { this.intrpeople = intrpeople;}
    public String getCover() { return cover;}
    public void setCover() { this.cover = cover;}
    public String getResearch() { return research;}
    public void setResearch() { this.research = research;}

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
