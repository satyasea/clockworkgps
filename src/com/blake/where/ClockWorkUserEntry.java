package com.blake.where;

import java.util.Date;

/**
 * Created by blake on 6/28/14.
 */
public class ClockWorkUserEntry {
    private String username;
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
