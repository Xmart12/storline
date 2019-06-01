package com.xmart.projects.storline.models;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by smart on 05/09/2018.
 */

public class User {

    String key;
    String email;
    String name;
    List<String> roles;
    String url_img;
    String imgName;

    //Construct
    public User(){}

    //Construct
    public User(String email, String name, List<String> roles, String url_img) {
        this.email = email;
        this.name = name;
        this.roles = roles;
        this.url_img = url_img;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
