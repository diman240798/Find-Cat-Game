package com.nanicky.devteam.findcat;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nanicky.devteam.findcat.services.AchievementsService;
import com.nanicky.devteam.findcat.services.ProgressService;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.services.SoundService;

import es.dmoral.toasty.Toasty;
import srsdt1.findacat.R;

import java.util.Timer;
import java.util.TimerTask;

public class GameOverActivity extends AppCompatActivity {
    public static final int SHARE_PERMISSION_CODE = 100;
    private static final String TAG = "GameOverActivity";
    private AchievementsService achievementsService = AchievementsService.getInstance();
    /* access modifiers changed from: private */
    public volatile Timer catClickTimer;
    private ProgressService progressService = ProgressService.getInstance();
    /* access modifiers changed from: private */
    public SettingsService settingsService = SettingsService.getInstance();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView((int) R.layout.activity_game_over);
        ((Button) findViewById(R.id.ButtonActionReset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GameOverActivity.this.settingsService.resetProgress();
                GameOverActivity.this.startActivity(new Intent(GameOverActivity.this, CatsActivity.class));
                GameOverActivity.this.finish();
            }
        });
        ((Button) findViewById(R.id.ButtonRate)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
                    GameOverActivity.this.shareGame();
                } else if (ContextCompat.checkSelfPermission(GameOverActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    ActivityCompat.requestPermissions(GameOverActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
                } else {
                    GameOverActivity.this.shareGame();
                }
            }
        });
        findViewById(R.id.catImage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (GameOverActivity.this.catClickTimer == null && GameOverActivity.this.settingsService.getSoundSettingValue()) {
                    SoundService.getInstance().playSound(Math.random() < 0.5d ? 2 : 1);
                    Timer unused = GameOverActivity.this.catClickTimer = new Timer(false);
                    GameOverActivity.this.catClickTimer.schedule(new TimerTask() {
                        public void run() {
                            if (GameOverActivity.this.catClickTimer != null) {
                                GameOverActivity.this.catClickTimer.cancel();
                            }
                            Timer unused = GameOverActivity.this.catClickTimer = null;
                        }
                    }, 2000);
                }
            }
        });
        if (this.progressService.isGameEnded() && this.achievementsService.checkAndShowEndAchievement(this) && this.settingsService.getSoundSettingValue()) {
            SoundService.getInstance().playSound(2);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 100) {
            return;
        }
        if (iArr[0] == 0) {
            shareGame();
            return;
        }
        Toasty.custom((Context) this, (CharSequence) getString(R.string.tw_error), (int) R.drawable.toast_alert, Color.parseColor("#FFFFFF"),
                ContextCompat.getColor(this, R.color.colorPrimaryDark), true, true).show();
    }

    /* access modifiers changed from: private */
    public void shareGame() {
        Uri uri = null;
        try {
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("share_image", "drawable", getPackageName())), null, null));
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении URI на картинку достижения: " + e.getLocalizedMessage());
        }
        if (uri != null) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", getString(R.string.tw_post));
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, getString(R.string.share_game)));
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
