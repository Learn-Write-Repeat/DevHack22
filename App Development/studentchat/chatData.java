package com.biswa1045.studentchat;

public class chatData {

   String msg,name,date;


    public chatData() {
    }

    public chatData(String msg,String name,String date) {
        this.msg = msg;
        this.name = name;
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

