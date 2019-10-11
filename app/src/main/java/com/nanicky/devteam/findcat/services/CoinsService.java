package com.nanicky.devteam.findcat.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.nanicky.devteam.findcat.Constants;

public class CoinsService {
    private static CoinsService instance;
    private SharedPreferences coinsSharedPreferences;

    public static CoinsService getInstance() {
        if (instance == null) {
            synchronized (CoinsService.class) {
                if (instance == null) {
                    instance = new CoinsService();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.coinsSharedPreferences = context.getSharedPreferences(Constants.COINS, 0);
    }

    private CoinsService() {
    }

    public void addCoins(int i) {
        int coins = getCoins();
        SharedPreferences.Editor edit = this.coinsSharedPreferences.edit();
        edit.putInt(Constants.COINS, coins + i);
        edit.apply();
    }

    public int getCoins() {
        return this.coinsSharedPreferences.getInt(Constants.COINS, 0);
    }

    public void removeCoins(int i) {
        int coins = getCoins();
        SharedPreferences.Editor edit = this.coinsSharedPreferences.edit();
        edit.putInt(Constants.COINS, coins - i);
        edit.apply();
    }

    public boolean isOperationAvailable(int i) {
        return getCoins() >= i;
    }
}