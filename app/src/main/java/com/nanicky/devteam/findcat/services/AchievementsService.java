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

    public boolean checkAndShowFastSlowAchievements(final Context context, int n) {
        final double n2 = n / 60;
        final int n3 = (int)Math.floor(n2);
        String s = "a_fast";
        final String s2 = "a_slow";
        Label_0172: {
            if (n3 == 0) {
                final double n4 = n;
                final double n5 = Math.floor(n2) * 60.0;
                Double.isNaN(n4);
                n = (int)(n4 - n5);
                if (n <= 2) {
                    n = (this.getAchievementStatus(s) ? 1 : 0);
                    if (n == 0) {
                        this.getAchievementDialog(context, "afast", s).show();
                        break Label_0172;
                    }
                }
            }
            n = (int)Math.floor(n2);
            if (n >= 3) {
                n = (this.getAchievementStatus(s2) ? 1 : 0);
                if (n == 0) {
                    this.getAchievementDialog(context, "aslow", s2).show();
                    s = s2;
                    break Label_0172;
                }
            }
            s = null;
        }
        if (s != null) {
            this.saveAchievement(s);
            return true;
        }
        return false;
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
