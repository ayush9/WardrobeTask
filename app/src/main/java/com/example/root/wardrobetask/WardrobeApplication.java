package com.example.root.wardrobetask;

import android.app.Application;

import com.karumi.dexter.Dexter;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WardrobeApplication extends Application
{
    public static String DATABASE_NAME = "wardrobe";

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
