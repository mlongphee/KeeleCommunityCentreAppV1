package com.keele_v11;

/**
 * Created by MJMJ2 on 02/05/17.
 */
public class PostEvents {
    public String camp_name;
    public String event_name;
    public String time;
    public String location;
    public String date;
    public long id;

    public PostEvents(String campName,String eventName,String timeName,String location, String date, long id){
        this.camp_name = campName;
        this.event_name = eventName;
        this.time = timeName;
        this.location = location;
        this.date = date;
        this.id = id;
    }

    public PostEvents(){

    }

    public String getCamp_name(){
        return camp_name;
    }

    public void setCamp_name(String campName){
        this.camp_name = campName;
    }

}
