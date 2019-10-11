package com.nanicky.devteam.findcat.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.nanicky.devteam.findcat.Constants;


public class SettingsService {
    private static SettingsService instance;
    private SharedPreferences settingsSharedPreferences;

    public static SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }

    public void init(Context context) {
        this.settingsSharedPreferences = context.getSharedPreferences(Constants.KOTIKI, 0);
    }

    public void resetProgress() {
        SharedPreferences.Editor edit = this.settingsSharedPreferences.edit();
        edit.putString(Constants.SETTINGS_ARRAY_ELEMENTS, Constants.ZEROS);
        edit.apply();
    }

    public boolean getSoundSettingValue() {
        return getSettingValue(Constants.SETTINGS_SOUND);
    }

    public String getLanguageSettingValue() {
        return getSettingValue(Constants.SETTINGS_LANGUAGE, Constants.DEFAULT_SETTING_VALUE);
    }

    public void saveLanguage(String str) {
        saveSettingsValue(Constants.SETTINGS_LANGUAGE, str);
    }

    public boolean getVibrationSettingValue() {
        return getSettingValue(Constants.SETTINGS_VIBRO, false);
    }

    private boolean getSettingValue(String str) {
        return getSettingValue(str, true);
    }

    private boolean getSettingValue(String str, boolean z) {
        return this.settingsSharedPreferences.getBoolean(str, z);
    }

    private String getSettingValue(String str, String str2) {
        return this.settingsSharedPreferences.getString(str, str2);
    }

    public void saveSoundValue(boolean z) {
        saveSettingsValue(Constants.SETTINGS_SOUND, z);
    }

    public void saveVibrationValue(boolean z) {
        saveSettingsValue(Constants.SETTINGS_VIBRO, z);
    }

    private void saveSettingsValue(String str, boolean z) {
        SharedPreferences.Editor edit = this.settingsSharedPreferences.edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    private void saveSettingsValue(String str, String str2) {
        SharedPreferences.Editor edit = this.settingsSharedPreferences.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    private SettingsService() {
    }
}