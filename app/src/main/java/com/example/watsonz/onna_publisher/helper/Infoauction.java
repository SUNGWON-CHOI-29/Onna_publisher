package com.example.watsonz.onna_publisher.helper;

/**
 * Created by watsonz on 2016-03-10.
 */
public class Infoauction{
    public String id;
    public String uid;
    public String name;
    public String place;
    public String people;
    public String menu;
    public String price;
    public String time;
    public String created_at;

    public Infoauction(){}

    public Infoauction(String id, String uid, String name, String place, String people,
                       String menu, String price, String time){
        this.id         = id;
        this.uid        = uid;
        this.name       = name;
        this.place      = place;
        this.people     = people;
        this.menu       = menu;
        this.price      = price;
        this.time       = time;

    }
}
