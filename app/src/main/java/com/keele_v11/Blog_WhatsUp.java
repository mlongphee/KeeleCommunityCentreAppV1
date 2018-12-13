package com.keele_v11;

/**
 * Created by MJMJ2 on 15/02/17.
 */
public class Blog_WhatsUp {

    private String title;
    private String desc;
    private String image;
    private String camp_name;

    TimeElapsed t = new TimeElapsed();
    long millis = System.currentTimeMillis();
    private long time;
    private String timestamp;

    private String newicon;

    public Blog_WhatsUp(){

    }

    public Blog_WhatsUp(String title, String desc, String campName, long time, String timestamp, String image,String newicon){
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.camp_name = campName;
        this.time = time;
        this.timestamp = timestamp;
        this.newicon = newicon;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCamp_name(String name){
        this.camp_name = name;
    }

    public String getCamp_name(){
        return camp_name;
    }

    public void setTime(long time){
        this.time = time;
    }

    public long getTime(){
        return time;
    }

    public void setTimestamp(String time){
        this.timestamp = time;
    }

    public String getTimestamp(){
        return t.convertLongDateToAgoString(getTime(),millis);
    }

    public void setNewicon(String newicon){
        this.newicon = newicon;
    }

    public String getNewicon(){
        return t.checkNew(getTime(), millis, newicon);
    }
}
