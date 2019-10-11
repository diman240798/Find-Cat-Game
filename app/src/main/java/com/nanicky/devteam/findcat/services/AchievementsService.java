package com.nanicky.devteam.findcat.services;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanicky.devteam.findcat.Constants;
import com.nanicky.devteam.findcat.utils.GraphicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import srsdt1.findacat.R;

public class AchievementsService {
    private static AchievementsService instance;
    private List<String> achievementsImagesNames = new ArrayList();
    private List<String> achievementsKeys = new ArrayList();
    private SharedPreferences achievementsSharedPreferences;
    private List<Integer> achievementsWeights = new ArrayList();
    private Map<String, String> keysNamesMap = new HashMap();

    public static AchievementsService getInstance() {
        if (instance == null) {
            instance = new AchievementsService();
        }
        return instance;
    }

    public void init(Context context) {
        this.achievementsSharedPreferences = context.getSharedPreferences(Constants.ACHIEVEMENTS_FILE, 0);
        this.achievementsKeys.clear();
        this.achievementsKeys.add("a_5");
        this.achievementsKeys.add("a_15");
        this.achievementsKeys.add("a_30");
        this.achievementsKeys.add("a_50");
        this.achievementsKeys.add("a_100");
        this.achievementsKeys.add("a_200");
        this.achievementsKeys.add("a_300");
        this.achievementsKeys.add("a_400");
        this.achievementsKeys.add("a_500");
        this.achievementsKeys.add("a_fast");
        this.achievementsKeys.add("a_slow");
        this.achievementsKeys.add("a_end");
        this.achievementsImagesNames.clear();
        this.achievementsImagesNames.add("a5");
        this.achievementsImagesNames.add("a15");
        this.achievementsImagesNames.add("a30");
        this.achievementsImagesNames.add("a50");
        this.achievementsImagesNames.add("a100");
        this.achievementsImagesNames.add("a200");
        this.achievementsImagesNames.add("a300");
        this.achievementsImagesNames.add("a400");
        this.achievementsImagesNames.add("a500");
        this.achievementsImagesNames.add("afast");
        this.achievementsImagesNames.add("aslow");
        this.achievementsImagesNames.add("aend");
        this.achievementsWeights.clear();
        this.achievementsWeights.add(5);
        this.achievementsWeights.add(15);
        this.achievementsWeights.add(30);
        this.achievementsWeights.add(50);
        this.achievementsWeights.add(100);
        this.achievementsWeights.add(Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
        this.achievementsWeights.add(300);
        this.achievementsWeights.add(400);
        this.achievementsWeights.add(500);
        String[] stringArray = context.getResources().getStringArray(R.array.achivki_names);
        for (int i = 0; i < this.achievementsKeys.size(); i++) {
            this.keysNamesMap.put(this.achievementsKeys.get(i), stringArray[i]);
        }
    }

    public boolean[] getAchievementsFlags() {
        boolean[] zArr = new boolean[this.achievementsKeys.size()];
        for (int i = 0; i < zArr.length; i++) {
            zArr[i] = this.achievementsSharedPreferences.getBoolean(this.achievementsKeys.get(i), false);
        }
        return zArr;
    }

    public String[] getAchievementsImagesNames() {
        return (String[]) this.achievementsImagesNames.toArray(new String[this.achievementsImagesNames.size()]);
    }

    private void saveAchievement(String str) {
        SharedPreferences.Editor edit = this.achievementsSharedPreferences.edit();
        edit.putBoolean(str, true);
        edit.apply();
    }

    private boolean getAchievementStatus(String str) {
        return this.achievementsSharedPreferences.getBoolean(str, false);
    }

    public boolean checkAndShowEndAchievement(Context context) {
        if (getAchievementStatus("a_end")) {
            return false;
        }
        getAchievementDialog(context, "aend", "a_end").show();
        saveAchievement("a_end");
        return true;
    }

    public void showTestAchievement(Context context) {
        getAchievementDialog(context, "aend", "a_end").show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkAndShowFastSlowAchievements(Context r12, int r13) {
        /*
            r11 = this;
            int r0 = r13 / 60
            double r0 = (double) r0
            double r2 = java.lang.Math.floor(r0)
            int r2 = (int) r2
            java.lang.String r3 = "a_fast"
            java.lang.String r4 = "a_slow"
            if (r2 != 0) goto L_0x002f
            double r5 = (double) r13
            double r7 = java.lang.Math.floor(r0)
            r9 = 4633641066610819072(0x404e000000000000, double:60.0)
            double r7 = r7 * r9
            java.lang.Double.isNaN(r5)
            double r5 = r5 - r7
            int r13 = (int) r5
            r2 = 2
            if (r13 > r2) goto L_0x002f
            boolean r13 = r11.getAchievementStatus(r3)
            if (r13 != 0) goto L_0x002f
            java.lang.String r13 = "afast"
            android.app.Dialog r12 = r11.getAchievementDialog(r12, r13, r3)
            r12.show()
            goto L_0x0049
        L_0x002f:
            double r0 = java.lang.Math.floor(r0)
            int r13 = (int) r0
            r0 = 3
            if (r13 < r0) goto L_0x0048
            boolean r13 = r11.getAchievementStatus(r4)
            if (r13 != 0) goto L_0x0048
            java.lang.String r13 = "aslow"
            android.app.Dialog r12 = r11.getAchievementDialog(r12, r13, r4)
            r12.show()
            r3 = r4
            goto L_0x0049
        L_0x0048:
            r3 = 0
        L_0x0049:
            if (r3 == 0) goto L_0x0050
            r11.saveAchievement(r3)
            r12 = 1
            return r12
        L_0x0050:
            r12 = 0
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: srsdt1.findacat.services.AchievementsService.checkAndShowFastSlowAchievements(android.content.Context, int):boolean");
    }

    public boolean checkAndShowRowAchievement(int i, Context context) {
        boolean[] achievementsFlags = getAchievementsFlags();
        int i2 = 0;
        while (i2 < this.achievementsWeights.size()) {
            if (i != this.achievementsWeights.get(i2).intValue() || achievementsFlags[i2]) {
                i2++;
            } else {
                String str = this.achievementsKeys.get(i2);
                saveAchievement(str);
                getAchievementDialog(context, getAchievementsImagesNames()[i2], str).show();
                return true;
            }
        }
        return false;
    }

    private Dialog getAchievementDialog(Context context, String str, String str2) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.achievments);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        } catch (Exception e) {
            Log.e("AchievementsServ", "Произошла ошибка при установке фона в диалоге достижения", e);
        }
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.achNameTv)).setText(this.keysNamesMap.get(str2));
        ((Button) dialog.findViewById(R.id.Button01)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ((ImageView) dialog.findViewById(R.id.ImageView01)).setImageResource(context.getResources().getIdentifier(str, "drawable", context.getPackageName()));
        GraphicUtils.animateDialog(dialog);
        return dialog;
    }
}
