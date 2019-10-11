package com.nanicky.devteam.findcat;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.nanicky.devteam.findcat.services.AchievementsService;
import com.nanicky.devteam.findcat.services.CoinsService;
import com.nanicky.devteam.findcat.services.ProgressService;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.services.SoundService;

public class FindACatApp extends Application {
    private static final String TAG = "FindACatApp";

    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        CoinsService.getInstance().init(this);
        AchievementsService.getInstance().init(this);
        SoundService.getInstance().init(this);
        SettingsService.getInstance().init(this);
        ProgressService.getInstance().init(this);
    }
}
