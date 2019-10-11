package com.nanicky.devteam.findcat.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }
}