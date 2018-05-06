package com.oi.spaghet1;

public class Category {

    private Integer CategoryID;
    private String CatName;
    private Integer SubID;
    private String SubName;

    public Integer getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(Integer CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String CatName) {
        this.CatName = CatName;
    }

    public Integer getSubID() {
        return SubID;
    }

    public void setSubID(Integer SubID) {
        this.SubID = SubID;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String SubName) {
        this.SubName = SubName;
    }


}
