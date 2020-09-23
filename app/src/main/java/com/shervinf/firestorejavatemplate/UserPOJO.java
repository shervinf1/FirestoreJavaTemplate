package com.shervinf.firestorejavatemplate;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserPOJO {

    //This java file is a Plain Old Java Object that is used to upload the user info to the Firestore database when the user has registered.
    //If anything here is changed make sure to change when the UserPOJO is used in the other activities.


    //Declaring member variables.
    private String userID;
    private @ServerTimestamp
    Date timeStamp;
    private String name;

    //Declaring empty constructor
    public UserPOJO() {
    }
    //Declaring main constructor
    public UserPOJO(String userID, Date timeStamp, String name) {
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.name = name;
    }


    //Getters and setters for the member variables
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
