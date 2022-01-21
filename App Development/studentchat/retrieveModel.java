package com.biswa1045.studentchat;

public class retrieveModel {
    String username;
    String image;


    public retrieveModel() {
    }

    public retrieveModel(String username,String image) {
this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
