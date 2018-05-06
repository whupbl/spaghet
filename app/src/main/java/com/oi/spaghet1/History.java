package com.oi.spaghet1;

public class History {

    private Integer ID;
    private Integer ClientID;
    private Integer ClientToCook;
    private String DishName;
    private String Description;
    private String CookName;
    private String TimeStart;
    private String PhoneNumber;


    public Integer getClientID() {
        return ClientID;
    }

    public void setClientID(Integer clientID) {
        ClientID = clientID;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getClientToCook() {
        return ClientToCook;
    }

    public void setClientToCook(Integer clientToCook) {
        ClientToCook = clientToCook;
    }


    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getCookName() {
        return CookName;
    }

    public void setCookName(String cookName) {
        CookName = cookName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}