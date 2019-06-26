package com.example.watsonz.onna_publisher.helper;

/**
 * Created by watsonz on 2016-06-29.
 */
public class InfoCupon {
    public String id;
    public String aid;
    public String uid;
    public String store;
    public String price;
    public String time;
    public String mainA;
    public String mainB;
    public String mainC;
    public String sideA;
    public String sideB;
    public String sideC;
    public String drinkA;
    public String drinkB;
    public String drinkC;

    public InfoCupon(){}

    public InfoCupon(String id, String aid, String uid, String store, String price, String time,
                     String mainA, String mainB, String mainC,
                     String sideA, String sideB, String sideC,
                     String drinkA, String drinkB, String drinkC){
        this.id         = id;
        this.aid        = aid;
        this.uid        = uid;
        this.store      = store;
        this.price      =  price;
        this.time       = time;
        this.mainA       = mainA;
        this.mainB       = mainB;
        this.mainC       = mainC;
        this.sideA       = sideA;
        this.sideB       = sideB;
        this.sideC       = sideC;
        this.drinkA       = drinkA;
        this.drinkB       = drinkB;
        this.drinkC       = drinkC;
    }
}
