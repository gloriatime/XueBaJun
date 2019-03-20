package com.example.model.myapplication;

public class Reply {
    private int id;
    private User critic;
    private User at;
    private int belong;
    private String Content;

    // »¹ÓÐÒ»¸öteacher¡£
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getCritic() {
        return critic;
    }
    public void setCritic(User critic) {
        this.critic = critic;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public int getBelong() {
        return belong;
    }
    public void setBelong(int belong) {
        this.belong = belong;
    }
    public User getAt() {
        return at;
    }
    public void setAt(User at) {
        this.at = at;
    }
}
