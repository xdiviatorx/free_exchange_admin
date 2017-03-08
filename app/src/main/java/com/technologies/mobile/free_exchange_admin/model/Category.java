package com.technologies.mobile.free_exchange_admin.model;

/**
 * Created by diviator on 13.01.2017.
 */

public class Category {

    private String name = "";
    private int id = -1;

    Category(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
