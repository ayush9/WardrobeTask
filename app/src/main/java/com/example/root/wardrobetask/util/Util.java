package com.example.root.wardrobetask.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by pwadh on 26-11-2016.
 */

public class Util
{
    public static String isPreferShirts = "is_prefer_shirts";
    public static String isPreferPants = "is_prefer_pants";
    public static String isNotificationOn = "is_notification_on";

    public static String SHIRT = "shirt";
    public static String PANT = "pant";

    public static void setGenderPreferenceForShirts(Activity activity, boolean preferShirtsValue)
    {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean(isPreferShirts, preferShirtsValue);
        editor.commit();

    }

    public static boolean getGenderPreferenceForShirts(Activity activity)
    {
        boolean preferShirts = true;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferShirts = defaultSharedPreferences.getBoolean(isPreferShirts, true);
        return preferShirts;
    }

    public static void setGenderPreferenceForPants(Activity activity, boolean preferPantsValue)
    {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean(isPreferPants, preferPantsValue);
        editor.commit();

    }

    public static boolean getGenderPreferenceForPants(Activity activity)
    {
        boolean preferPants = true;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferPants = defaultSharedPreferences.getBoolean(isPreferPants, true);
        return preferPants;
    }


    public static void setPreferenceForNotifcarion(Activity activity, boolean notificationOn)
    {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean(isNotificationOn, notificationOn);
        editor.commit();

    }

    public static boolean getPreferenceForNotification(Activity activity)
    {
        boolean notificationOn = true;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        notificationOn = defaultSharedPreferences.getBoolean(isNotificationOn, true);
        return notificationOn;
    }




}
