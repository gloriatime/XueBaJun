package com.example.user.signuppage;

/**
 * Created by user on 2019/1/9.
 */

class User {
    private String name;
    private String pwd;
    private String grade;
    private String phone;
    private String college;
    private String major;
    private int point;
    private String head;
    private boolean art,medicine,management,humanity,technology,agriculture,play;
    //User(){}
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getPwd(){
        return pwd;
    }
    public void setPwd(String pwd){
        this.pwd=pwd;
    }
    public String getGrade(){
        return grade;
    }
    public void setGrade(String grade){
        this.grade=grade;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getCollege(){
        return college;
    }
    public void setCollege(String college){
        this.college=college;
    }
    public String getMajor(){
        return major;
    }
    public void setMajor(String college){
        this.major=major;
    }
    public int getPoint(){
        return point;
    }
    public void setPoint(int point){
        this.point=point;
    }
    public String getHead(){
        return head;
    }
    public void setHead(String head){
        this.head=head;
    }
    public boolean isArt(){
        return art;
    }
    public void setArt(boolean art){
        this.art=art;
    }
    public boolean isMedicine(){
        return medicine;
    }
    public void setMedicine(boolean medicine){
        this.medicine=medicine;
    }
    public boolean isManagement(){
        return management;
    }
    public void setManagement(boolean management){
        this.management=management;
    }
    public boolean isHumanity(){
        return humanity;
    }
    public void setHumanity(boolean humanity){
        this.humanity=humanity;
    }
    public boolean isTechnology(){
        return technology;
    }
    public void setTechnology(boolean technology){
        this.technology=technology;
    }
    public boolean isAgriculture(){
        return agriculture;
    }
    public void setAgriculture(boolean agriculture){
        this.agriculture=agriculture;
    }
    public boolean isPlay(){
        return play;
    }
    public void setPlay(boolean play){
        this.play=play;
    }
}
