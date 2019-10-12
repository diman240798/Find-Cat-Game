package com.nanicky.devteam.findcat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanicky.devteam.findcat.dialog.CancelableDialog;
import com.nanicky.devteam.findcat.services.CoinsService;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.utils.GraphicUtils;

import es.dmoral.toasty.Toasty;
import srsdt1.findacat.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int SHARE_PERMISSION_CODE = 100;
    private static final String TAG = "MainActivity";
    /* access modifiers changed from: private */
    private SettingsService settingsService = SettingsService.getInstance();
    /* access modifiers changed from: private */
    public boolean startClicked = false;
    /* access modifiers changed from: private */

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView((int) R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(3);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.FIRST_LAUNCH_FILE, 0);
        if (!sharedPreferences.getBoolean(Constants.FIRST_LAUNCH_KEY, false)) {
            CoinsService.getInstance().addCoins(100);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(Constants.FIRST_LAUNCH_KEY, true);
            edit.apply();
        }
        ((Button) findViewById(R.id.settingsBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        Button button = (Button) findViewById(R.id.startGameBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!MainActivity.this.startClicked) {
                    boolean unused = MainActivity.this.startClicked = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            MainActivity.this.startGame();

                        }
                    }, 400);
                }
            }
        });
        final AnimatedVectorDrawableCompat create = AnimatedVectorDrawableCompat.create(this, R.drawable.play_animated);
        if (create != null) {
            button.setCompoundDrawablesWithIntrinsicBounds(create, null, null, null);
            create.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                public void onAnimationEnd(Drawable drawable) {
                    MainActivity.this.restartPlayAnim(create, PathInterpolatorCompat.MAX_NUM_POINTS);
                }
            });
            restartPlayAnim(create, 1000);
        }
        ImageView imageView = (ImageView) findViewById(R.id.other_games_btn);
        final AnimatedVectorDrawableCompat create2 = AnimatedVectorDrawableCompat.create(this, R.drawable.cat_icon_anim);
        if (create2 != null) {
            imageView.setImageDrawable(create2);
            create2.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                public void onAnimationEnd(Drawable drawable) {
                    MainActivity.this.restartPlayAnim(create2, 1000);
                }
            });
            restartPlayAnim(create2, 500);
        }
        View.OnClickListener r0 = new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.otherGames();
            }
        };
        findViewById(R.id.other_games).setOnClickListener(r0);
        imageView.setOnClickListener(r0);
        findViewById(R.id.shareBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
                    MainActivity.this.shareGame();
                } else if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
                } else {
                    MainActivity.this.shareGame();
                }
            }
        });
        if (Constants.DEFAULT_SETTING_VALUE.equals(this.settingsService.getLanguageSettingValue())) {
            this.settingsService.saveLanguage(Locale.getDefault().getLanguage().toLowerCase());
        }
    }


    /* access modifiers changed from: private */
    public void restartPlayAnim(final AnimatedVectorDrawableCompat animatedVectorDrawableCompat, int i) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                animatedVectorDrawableCompat.start();
            }
        }, (long) i);
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
        finishAffinity();
    }

    public void otherGames() {
        final CancelableDialog cancelableDialog = new CancelableDialog(this, R.style.Theme_Dialog, R.id.dialog_other_games_main);
        Window window = cancelableDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.dimAmount = 0.6f;
            window.setAttributes(attributes);
            window.addFlags(2);
        }
        cancelableDialog.setContentView(R.layout.dialog_other_games);
        ((TextView) cancelableDialog.findViewById(R.id.TextView01)).setText(getResources().getString(R.string.other_message));
        Button button = (Button) cancelableDialog.findViewById(R.id.enjoyButton);
        View.OnClickListener closeDialogListener = view -> cancelableDialog.cancel();

        button.setOnClickListener(closeDialogListener);
        ((Button) cancelableDialog.findViewById(R.id.ButtonCancel)).setOnClickListener(closeDialogListener);

        cancelableDialog.setCancelable(true);
        GraphicUtils.animateDialog(cancelableDialog);
        cancelableDialog.show();
    }

    public void openAchievementsScreen(View view) {
        startActivity(new Intent(this, AchievementsActivity.class));
    }

    public void startGame() {
        startActivity(new Intent(this, CatsActivity.class));
    }
}