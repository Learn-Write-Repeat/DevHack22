package com.biswa1045.studentchat;

public class retrieveModelDoc {
    String name;
    String userid;
    String link;
   String owner_name;

    public retrieveModelDoc() {
    }

    public retrieveModelDoc(String name, String userid,String link,String owner_name) {
this.name = name;
        this.userid = userid;
        this.link = link;
        this.owner_name=owner_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
