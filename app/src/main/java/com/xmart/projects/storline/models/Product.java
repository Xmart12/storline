package com.xmart.projects.storline.models;

public class Product {

    String key;
    String description;
    String longDescription;
    String category;
    float price;
    int stock;
    boolean show;
    String imgUrl;
    String imgName;

    //Construct
    public Product(){}

    //Construct Model
    public Product(String description, String longDescription, String category,
                   float price, String imgUrl, String imgName, int stock, boolean show) {
        this.description = description;
        this.longDescription = longDescription;
        this.category = category;
        this.price = price;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.stock = stock;
        this.show = show;
    }

    //Key

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //Description

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Long Description

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    //Category

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //Price

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    //Stock

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    //Show Flag

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    //Img URL

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    //Img Name

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

}

