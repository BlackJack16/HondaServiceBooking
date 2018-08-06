package com.yoga.hondaservicebooking;

import android.app.Application;

import com.firebase.client.Firebase;


public class Launcher extends Application {
    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

}
