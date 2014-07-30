package com.blake.where;

import java.util.Date;

/**
 * Created by blake on 6/28/14.
 */
public class ClockWorkUserEntry {


    public ClockWorkUserEntry(int id){
        this.id=id;
    }

    public ClockWorkUserEntry(){

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String username;
    private String phone;
    private String pass;
    private int entryType;
    private Date entryTime;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }
}
