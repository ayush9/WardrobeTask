package com.example.root.wardrobetask.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class FavoriteCollection extends RealmObject
{
    @PrimaryKey
    String id;
    String shirtImagePath;
    String pantImagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShirtImagePath() {
        return shirtImagePath;
    }

    public void setShirtImagePath(String shirtImagePath) {
        this.shirtImagePath = shirtImagePath;
    }

    public String getPantImagePath() {
        return pantImagePath;
    }

    public void setPantImagePath(String pantImagePath) {
        this.pantImagePath = pantImagePath;
    }
}
