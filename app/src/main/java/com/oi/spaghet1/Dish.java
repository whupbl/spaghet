package com.oi.spaghet1;

public class Dish {

    private int ID;
    private String Title;
    private Integer Price;
    private String Description;
    private String Category;
    private String Subcategory;
    private Integer CookingTime;
    private String CookName;
    private Double Rating;
    private Double Lat;
    private Double Lng;
    private String Phone;
//hi
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getSubcategory() {
        return Subcategory;
    }

    public void setSubcategory(String Subcategory) {
        this.Subcategory = Subcategory;
    }

    public Integer getCookingTime() {
        return CookingTime;
    }

    public void setCookingTime(Integer CookingTime) {
        this.CookingTime = CookingTime;
    }

    public String getCookName() {
        return CookName;
    }

    public void setCookName(String CookName) {
        this.CookName = CookName;
    }

    public Double getRating() {
        return Rating;
    }

    public void setRating(Double Rating) {
        this.Rating = Rating;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double Lat) {
        this.Lat = Lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double Lng) {
        this.Lng = Lng;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}