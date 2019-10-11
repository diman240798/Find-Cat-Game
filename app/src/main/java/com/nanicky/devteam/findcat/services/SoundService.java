package com.nanicky.devteam.findcat.services;

import android.content.Context;
import android.media.SoundPool;
import android.os.Build;

import srsdt1.findacat.R;


public class SoundService {
    public static final int ACHIEVEMENT = 2;
    public static final int MEOW = 1;
    public static final int TICK = 3;
    public static SoundService instance = new SoundService();
    private int achievementSound;
    private int soundIdMeow;
    private int soundIdTick;
    private SoundPool soundPool;

    public static SoundService getInstance() {
        return instance;
    }

    public void init(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.soundPool = new SoundPool.Builder().setMaxStreams(3).build();
        } else {
            this.soundPool = new SoundPool(3, 3, 0);
        }
        this.achievementSound = this.soundPool.load(context, R.raw.purr, 1);
        this.soundIdMeow = this.soundPool.load(context, R.raw.sound, 1);
        this.soundIdTick = this.soundPool.load(context, R.raw.tick_sound, 1);
    }

    public void playSound(int i) {
        if (i == 1) {
            playMeow();
        } else if (i == 2) {
            playAchievement();
        } else if (i == 3) {
            playTick();
        }
    }

    public void playMeow() {
        this.soundPool.play(this.soundIdMeow, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playAchievement() {
        this.soundPool.play(this.achievementSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    private void playTick() {
        this.soundPool.play(this.soundIdTick, 1.0f, 1.0f, 0, 0, 1.0f);
    }
}