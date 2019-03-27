package com.example.model.myapplication;

import java.io.Serializable;

public class ProfessorCourse implements Serializable {

    private Professor professor;
    private Course course;
    public Professor getProfessor() {
        return professor;
    }
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

}