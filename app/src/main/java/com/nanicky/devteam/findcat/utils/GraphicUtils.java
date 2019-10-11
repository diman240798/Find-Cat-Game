package com.nanicky.devteam.findcat.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

import srsdt1.findacat.R;


public class GraphicUtils {
    private static final String TAG = "GraphicUtils";

    public static void animateDialog(Dialog dialog) {
        if (dialog != null) {
            try {
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            } catch (Exception e) {
                Log.e(TAG, "При анимировании диалога произошла ошибка", e);
            }
        }
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3, Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inPurgeable = true;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        options.inDensity = displayMetrics.densityDpi;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }
}