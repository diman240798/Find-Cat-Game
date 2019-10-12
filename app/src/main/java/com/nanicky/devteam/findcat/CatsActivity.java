package com.nanicky.devteam.findcat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanicky.devteam.findcat.dialog.ClosableCornerDialog;
import com.nanicky.devteam.findcat.services.AchievementsService;
import com.nanicky.devteam.findcat.services.CoinsService;
import com.nanicky.devteam.findcat.services.ProgressService;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.services.SoundService;
import com.nanicky.devteam.findcat.utils.AnimationMotionUtils;
import com.nanicky.devteam.findcat.utils.GraphicUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import es.dmoral.toasty.Toasty;
import srsdt1.findacat.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CatsActivity extends Activity {
    private AchievementsService achievementsService;
    /* access modifiers changed from: private */
    private int animationMode = 2;
    private int[][] catsCoordinates;
    /* access modifiers changed from: private */
    public int catsInRow = 0;
    private int clicksCount = 0;
    /* access modifiers changed from: private */
    public CoinsService coinsService;
    /* access modifiers changed from: private */
    public ImageView currentImageView;
    /* access modifiers changed from: private */
    public int currentLevel;
    private boolean fastBack = true;
    /* access modifiers changed from: private */
    public String[] hints;
    private ImageView levelImageViewFirst;
    private ImageView levelImageViewSecond;
    private boolean loadLevelProgress;
    private RelativeLayout main;
    private int missClicks = 0;
    /* access modifiers changed from: private */
    public volatile boolean mustAllocate = true;
    private int[] progressArray;
    private ProgressService progressService;
    private int screenHeight = -1;
    private int screenWidth = -1;
    private SettingsService settingsService;
    private Timer timer;
    /* access modifiers changed from: private */
    public int timerCounter = 0;
    /* access modifiers changed from: private */
    public boolean wasHint = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(R.layout.cats);
        this.main = (RelativeLayout) findViewById(R.id.main);
        this.progressService = ProgressService.getInstance();
        this.settingsService = SettingsService.getInstance();
        this.coinsService = CoinsService.getInstance();
        this.achievementsService = AchievementsService.getInstance();
        this.catsInRow = 0;
        this.progressArray = this.progressService.getProgressArray();
        this.catsCoordinates = this.progressService.getCatsCoordinatesArray();
        this.hints = getResources().getStringArray(R.array.podskazki);
        this.levelImageViewFirst = (ImageView) findViewById(R.id.imageView1);
        this.levelImageViewSecond = (ImageView) findViewById(R.id.imageView2);
        this.currentImageView = this.levelImageViewFirst;
        setVolumeControlStream(3);
        loadLevel(false);
        this.clicksCount = getClicksCount();
    }

    public void onBackPressed() {
        if (this.fastBack) {
                    }
        super.onBackPressed();
    }

    /* access modifiers changed from: private */
    public ImageView getOutView() {
        if (this.currentImageView.equals(this.levelImageViewFirst)) {
            return this.levelImageViewSecond;
        }
        return this.levelImageViewFirst;
    }

    private void switchImageViews() {
        if (this.currentImageView.equals(this.levelImageViewFirst)) {
            this.currentImageView = this.levelImageViewSecond;
        } else {
            this.currentImageView = this.levelImageViewFirst;
        }
    }

    public void showCat(int i, Callback callback) {
        if (this.animationMode != 2) {
            recycleImageView(this.currentImageView);
        }
        if (this.screenWidth == -1 || this.screenHeight == -1) {
            this.screenWidth = GraphicUtils.getScreenWidth(this);
            this.screenHeight = GraphicUtils.getScreenHeight(this);
        }
        Resources resources = getApplicationContext().getResources();
        int identifier = resources.getIdentifier("c" + Integer.toString(i), "drawable", getPackageName());
        if (callback != null) {
            Picasso.with(this).load(identifier).resize(this.screenWidth, this.screenHeight).into(this.currentImageView, callback);
        } else {
            Picasso.with(this).load(identifier).resize(this.screenWidth, this.screenHeight).into(this.currentImageView);
        }
    }

    private void recycleImageView(ImageView imageView) {
        try {
            if (!((BitmapDrawable) imageView.getDrawable()).getBitmap().isRecycled()) {
                ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
            }
        } catch (Exception unused) {
            Log.e("CatsActivity", "Произошла ошибка при попытке recycle изображения в imageView");
                    }
    }

    public void loadLevel(boolean z) {
        if (this.progressService.isGameEnded(this.progressArray)) {
            gameOver();
            return;
        }
        this.loadLevelProgress = false;
        while (!this.loadLevelProgress) {
            this.currentLevel = (int) (Math.random() * 591.0d);
            if (this.progressArray[this.currentLevel] == 0) {
                this.loadLevelProgress = true;
                if (z) {
                    int i = this.animationMode;
                    if (i == 1) {
                        AnimationMotionUtils.changeLevelDivided(this, this.currentImageView, new AnimationMotionUtils.AnimationStageActionListener() {
                            public void betweenAnimAction() {
                                CatsActivity.this.changeCat(true, null);
                            }

                            public void afterAnimAction() {
                                CatsActivity.this.startTimer();
                            }
                        });
                    } else if (i != 2) {
                        changeCat(false, null);
                        startTimer();
                    } else {
                        changeCat(true, new Callback() {
                            public void onError() {
                            }

                            public void onSuccess() {
                                CatsActivity catsActivity = CatsActivity.this;
                                AnimationMotionUtils.changeLevelCombo(catsActivity, catsActivity.getOutView(), CatsActivity.this.currentImageView, new AnimationMotionUtils.AnimationStageActionListener() {
                                    public void betweenAnimAction() {
                                    }

                                    public void afterAnimAction() {
                                        CatsActivity.this.startTimer();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    changeCat(false, null);
                    startTimer();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void changeCat(boolean z, Callback callback) {
        try {
            if (this.animationMode == 2 && z) {
                switchImageViews();
            }
            showCat(this.currentLevel, callback);
        } catch (Exception e) {
            Log.e("CatsActivity", "Произошла ошибка при загрузке уровня", e);
                    }
    }

    private void saveLevelProgress() {
        int[] iArr = this.progressArray;
        iArr[this.currentLevel] = 1;
        this.progressService.saveProgress(iArr);
    }

    private void gameOver() {
        startActivity(new Intent(this, GameOverActivity.class));
        finish();
    }

    public void startTimer() {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            public void run() {
                if (CatsActivity.this.mustAllocate) {
                    int unused = CatsActivity.this.timerCounter = CatsActivity.this.timerCounter + 1;
                }
            }
        }, 1000, 1000);
    }

    public void showTime() {
        String sb;
        try {
            this.timer.cancel();
            String string = getResources().getString(R.string.timer);
            if (this.wasHint) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(" ");
                sb2.append((int) Math.floor((double) (this.timerCounter / 60)));
                sb2.append(":");
                double d = (double) this.timerCounter;
                double floor = Math.floor((double) (this.timerCounter / 60)) * 60.0d;
                Double.isNaN(d);
                sb2.append((int) (d - floor));
                sb = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append(" ");
                sb3.append((int) Math.floor((double) (this.timerCounter / 60)));
                sb3.append(":");
                double d2 = (double) this.timerCounter;
                double floor2 = Math.floor((double) (this.timerCounter / 60)) * 60.0d;
                Double.isNaN(d2);
                sb3.append((int) (d2 - floor2));
                sb3.append(10);
                sb3.append(getResources().getString(R.string.get_points));
                sb = sb3.toString();
            }
            Toasty.custom((Context) this, (CharSequence) sb, (int) R.drawable.toast_success,
                    Color.parseColor("#FFFFFF"), ContextCompat.getColor(this, R.color.colorPrimary), true, true).show();
            if (this.achievementsService.checkAndShowFastSlowAchievements(this, this.timerCounter)) {
                playSound(2);
            }
            this.timer.cancel();
            this.timerCounter = 0;
        } catch (Exception e) {
            Log.e("CatsActivity", "Ошибка при выводе таймера", e);
                    }
    }

    private void showCheaterDialog() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.dimAmount = 0.6f;
        dialog.getWindow().setAttributes(attributes);
        dialog.getWindow().addFlags(2);
        dialog.setContentView(R.layout.dialog_cheat);
        TextView textView = (TextView) dialog.findViewById(R.id.TextView01);
        if (!this.wasHint) {
            textView.setText(getString(R.string.cheat_message, new Object[]{Integer.valueOf(this.coinsService.getCoins())}));
        } else {
            textView.setText(getString(R.string.cheat_message_again));
        }
        Button button = (Button) dialog.findViewById(R.id.ButtonHelp);
        button.setText(getResources().getString(R.string.cheat_help));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CatsActivity.this.wasHint) {
                    CatsActivity catsActivity = CatsActivity.this;
                    catsActivity.showHint(catsActivity.hints[CatsActivity.this.currentLevel]);
                } else if (CatsActivity.this.coinsService.isOperationAvailable(10)) {
                    CatsActivity catsActivity2 = CatsActivity.this;
                    Toasty.custom((Context) catsActivity2, (CharSequence) catsActivity2.getResources().getString(R.string.lost_points), (int) R.drawable.toast_hint, Color.parseColor("#FFFFFF"),
                            ContextCompat.getColor(CatsActivity.this, R.color.colorPrimary), true, true).show();
                    CatsActivity catsActivity3 = CatsActivity.this;
                    catsActivity3.showHint(catsActivity3.hints[CatsActivity.this.currentLevel]);
                    CatsActivity.this.coinsService.removeCoins(10);
                    boolean unused = CatsActivity.this.wasHint = true;
                    int unused2 = CatsActivity.this.catsInRow = 0;
                    HashMap hashMap = new HashMap();
                    hashMap.put("level", Integer.valueOf(CatsActivity.this.currentLevel));
                                    } else {
                    CatsActivity catsActivity4 = CatsActivity.this;
                    Toasty.custom((Context) catsActivity4, (CharSequence) catsActivity4.getString(R.string.not_coins), (int) R.drawable.toast_alert, Color.parseColor("#FFFFFF"),
                            ContextCompat.getColor(CatsActivity.this, R.color.colorPrimary), true, true).show();
                }
                dialog.cancel();
            }
        });
        ((Button) dialog.findViewById(R.id.ButtonCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        GraphicUtils.animateDialog(dialog);
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void showHint(String str) {
        ClosableCornerDialog closableCornerDialog = new ClosableCornerDialog(this);
        closableCornerDialog.setText(str);
        closableCornerDialog.show();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.clicksCount++;
        saveClicksCount(this.clicksCount);
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getAction() == 0) {
            placeMark(x, y);
            int xCoord = (this.currentImageView.getWidth() * this.catsCoordinates[this.currentLevel][0]) / 1000;
            int yCoord = (this.currentImageView.getHeight() * this.catsCoordinates[this.currentLevel][1]) / 1000;
            if (x - 35 >= xCoord || x + 35 <= xCoord || y - 35 >= yCoord || y + 35 <= yCoord) {
                playSound(3);
                this.missClicks++;
                if ((this.missClicks + 1) % 8 == 0) {
                    showCheaterDialog();
                }
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("levelNumber", Integer.valueOf(this.currentLevel));
                                if (this.fastBack) {
                    this.fastBack = false;
                }
                if (!this.progressService.isGameEnded(this.progressArray)) {
                    showTime();
                    if (this.settingsService.getVibrationSettingValue()) {
                        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
                    }
                    if (!this.wasHint) {
                        this.catsInRow++;
                        this.coinsService.addCoins(10);
                    }
                }
                this.missClicks = 0;
                saveLevelProgress();
                loadLevel(true);
                this.wasHint = false;
                playSound(1);
                if (this.achievementsService.checkAndShowRowAchievement(this.catsInRow, this)) {
                    playSound(2);
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private void placeMark(int i, int i2) {
        ImageView imageView = new ImageView(this);
        double width = (double) this.currentImageView.getWidth();
        Double.isNaN(width);
        int i3 = (int) (width * 0.05d);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(i3, i3));
        imageView.setImageResource(R.drawable.point);
        int i4 = i3 / 2;
        imageView.setX((float) (i - i4));
        imageView.setY((float) (i2 - i4));
        this.main.addView(imageView);
        AnimationMotionUtils.removeMarkWithAnimation(imageView, this.main, this);
    }

    private void playSound(int i) {
        if (this.settingsService.getSoundSettingValue()) {
            SoundService.getInstance().playSound(i);
        }
    }

    private void saveClicksCount(int i) {
        SharedPreferences.Editor edit = getSharedPreferences(Constants.CLICKS_KEY, 0).edit();
        edit.putInt(Constants.CLICKS_KEY, i);
        edit.apply();
    }

    private int getClicksCount() {
        return getSharedPreferences(Constants.CLICKS_KEY, 0).getInt(Constants.CLICKS_KEY, 0);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mustAllocate = true;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mustAllocate = false;
    }
}
