package com.xmart.projects.storline.models;

import java.util.Date;
import java.util.List;

/**
 * Created by smart on 06/09/2018.
 */

public class Sales {

    public String key;
    public User user;
    public List<Product> products;
    public Date date;
    public int items;
    public double total;

    public Sales() {

    }


    public Sales(User user, List<Product> products, Date date, int items, double total) {
        this.user = user;
        this.products = products;
        this.date = date;
        this.items = items;
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
