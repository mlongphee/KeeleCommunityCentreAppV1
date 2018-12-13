package com.keele_v11;

/**
 * Created by MJMJ2 on 05/05/17.
 */
public class Users {

    public String name;
    public boolean Active;
    public boolean Admin;
    public boolean first_time;
    public String email;

    public Users(){

    }

    public Users(String name, boolean active, boolean admin, boolean first_time, String email){
        this.name = name;
        this.Active = active;
        this.Admin = admin;
        this.first_time = first_time;
        this.email = email;
    }

    public void setFirst_time(boolean first){
        this.first_time = first;
    }
}
