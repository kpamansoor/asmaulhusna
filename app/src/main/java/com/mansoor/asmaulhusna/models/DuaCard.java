package com.mansoor.asmaulhusna.models;

/**
 * Created by L4208412 on 29/6/2018.
 */

public class DuaCard {

    public DuaCard(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String name;
    private String image;


}
