package com.vinuthana.vinvidya.utils;

/**
 * Created by Belal on 9/5/2017.
 */


//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private int id;
    private String username, password,gender;

    public User(int id, String username, String password) {
        //String gender
        this.id = id;
        this.username = username;
        this.password = password;
        //this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /*public String getGender() {
        return gender;
    }*/
}
