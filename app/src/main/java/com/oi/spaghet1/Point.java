package com.oi.spaghet1;

public class Point {
    private double lng;
    private double lat;
    private String name;
    private String description;
    private int timeToGetReady;
    private String cookName;
    private String phone;
    private int price;
    private double rate;

    public Point(double lat_, double lng_, String name_, String description_, int timeToGetReady_, String cookName_,
                 String phone_, int price_, double rate_){
        lat = lat_;
        lng = lng_;
        name = name_;
        description = description_;
        timeToGetReady = timeToGetReady_;
        cookName = cookName_;
        phone = phone_;
        price = price_;
        rate = rate_;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeToGetReady() {
        return timeToGetReady;
    }

    public void setTimeToGetReady(int timeToGetReady) {
        this.timeToGetReady = timeToGetReady;
    }

    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
