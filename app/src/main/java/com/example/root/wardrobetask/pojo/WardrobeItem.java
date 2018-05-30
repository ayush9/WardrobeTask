package com.example.root.wardrobetask.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class WardrobeItem extends RealmObject
{
    @PrimaryKey
    String id;
    String imagePath;
    String clothType;

    public String getId() {
        return id;
    }

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }
}
