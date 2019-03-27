package com.example.model.myapplication;

public class Reply {
    private int id;
    private User critic;
    private User at;
    private int belong;
    private String content;

    public Reply(User critic, User at, String Content){
        this.critic = critic;
        this.at = at;
        this.content = Content;
    }

    public Reply(User critic, String Content){
        this.critic = critic;
        this.content = Content;
    }
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
        return content;
    }
    public void setContent(String content) {
        this.content = content;
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
