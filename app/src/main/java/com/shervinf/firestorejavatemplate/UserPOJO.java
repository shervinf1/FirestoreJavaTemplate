package com.shervinf.firestorejavatemplate;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserPOJO {
    private String userID;
    private @ServerTimestamp
    Date timeStamp;
    private String name;

    public UserPOJO() {
    }

    public UserPOJO(String userID, Date timeStamp, String name) {
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public UserPOJO(String userID, Date timeStamp) {
        this.userID = userID;
        this.timeStamp = timeStamp;

    }


    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
