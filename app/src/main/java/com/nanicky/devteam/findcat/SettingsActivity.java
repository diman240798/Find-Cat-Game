package com.nanicky.devteam.findcat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nanicky.devteam.findcat.dialog.CancelableDialog;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.services.SoundService;
import com.nanicky.devteam.findcat.utils.GraphicUtils;

import es.dmoral.toasty.Toasty;
import srsdt1.findacat.R;

public class SettingsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Boolean sound = true;
    private Boolean vibro = true;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat2;
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView((int) R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.sound = Boolean.valueOf(SettingsService.getInstance().getSoundSettingValue());
        Button button = (Button) findViewById(R.id.soundBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
                Button button = (Button) SettingsActivity.this.findViewById(R.id.soundBtn);
                if (SettingsActivity.this.sound.booleanValue()) {
                    Boolean unused = SettingsActivity.this.sound = false;
                    animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(SettingsActivity.this, R.drawable.ic_volume_on_off);
                    button.setText(SettingsActivity.this.getResources().getString(R.string.sound_button_off));
                } else {
                    SoundService.getInstance().playMeow();
                    animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(SettingsActivity.this, R.drawable.ic_volume_off_on);
                    Boolean unused2 = SettingsActivity.this.sound = true;
                    button.setText(SettingsActivity.this.getResources().getString(R.string.sound_button_on));
                }
                button.setCompoundDrawablesWithIntrinsicBounds(animatedVectorDrawableCompat, null, null, null);
                SettingsActivity.this.playIconAnim(animatedVectorDrawableCompat);
                SettingsService.getInstance().saveSoundValue(SettingsActivity.this.sound.booleanValue());
            }
        });
        if (this.sound.booleanValue()) {
            animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.ic_volume_on_off);
            button.setText(getResources().getString(R.string.sound_button_on));
        } else {
            animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.ic_volume_off_on);
            button.setText(getResources().getString(R.string.sound_button_off));
        }
        button.setCompoundDrawablesWithIntrinsicBounds(animatedVectorDrawableCompat, null, null, null);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.vibro = Boolean.valueOf(SettingsService.getInstance().getVibrationSettingValue());
        Button button2 = (Button) findViewById(R.id.vibrationBtn);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.vibrationClick(vibrator);
            }
        });
        if (this.vibro.booleanValue()) {
            animatedVectorDrawableCompat2 = AnimatedVectorDrawableCompat.create(this, R.drawable.vibr_on_off_anim);
            button2.setText(getResources().getString(R.string.vibro_button_on));
        } else {
            animatedVectorDrawableCompat2 = AnimatedVectorDrawableCompat.create(this, R.drawable.vibr_off_on_anim);
            button2.setText(getResources().getString(R.string.vibro_button_off));
        }
        button2.setCompoundDrawablesWithIntrinsicBounds(animatedVectorDrawableCompat2, null, null, null);
        ((Button) findViewById(R.id.resetBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.resetClickAction();
            }
        });
        ((Button) findViewById(R.id.infoBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.aboutClickAction();
            }
        });
        ((Button) findViewById(R.id.languageBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, LanguageActivity.class));
            }
        });
    }

    /* access modifiers changed from: private */
    public void vibrationClick(Vibrator vibrator) {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
        Button button = (Button) findViewById(R.id.vibrationBtn);
        if (this.vibro.booleanValue()) {
            this.vibro = false;
            button.setText(getResources().getString(R.string.vibro_button_off));
            animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.vibr_on_off_anim);
        } else {
            vibrator.vibrate(500);
            this.vibro = true;
            button.setText(getResources().getString(R.string.vibro_button_on));
            animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.vibr_off_on_anim);
        }
        button.setCompoundDrawablesWithIntrinsicBounds(animatedVectorDrawableCompat, null, null, null);
        playIconAnim(animatedVectorDrawableCompat);
        SettingsService.getInstance().saveVibrationValue(this.vibro.booleanValue());
    }

    /* access modifiers changed from: private */
    public void playIconAnim(AnimatedVectorDrawableCompat animatedVectorDrawableCompat) {
        if (animatedVectorDrawableCompat != null) {
            animatedVectorDrawableCompat.start();
        }
    }

    /* access modifiers changed from: private */
    public void resetClickAction() {
        final CancelableDialog cancelableDialog = new CancelableDialog(this, R.style.Theme_Dialog, R.id.dialog_reset_main);
        Window window = cancelableDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.dimAmount = 0.6f;
            window.setAttributes(attributes);
            window.addFlags(2);
        }
        cancelableDialog.setContentView(R.layout.dialog_reset);
        ((TextView) cancelableDialog.findViewById(R.id.TextView01)).setText(getResources().getString(R.string.reset_sure));
        Button button = (Button) cancelableDialog.findViewById(R.id.ButtonHelp);
        button.setText(getResources().getString(R.string.action_reset));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingsService.getInstance().resetProgress();
                SettingsActivity settingsActivity = SettingsActivity.this;
                Toasty.custom((Context) settingsActivity, (CharSequence) settingsActivity.getString(R.string.nul_success), (int) R.drawable.toast_common_success,
                        Color.parseColor("#C9F9AF"), ContextCompat.getColor(SettingsActivity.this, R.color.colorPrimary), true, true).show();
                cancelableDialog.cancel();
            }
        });
        ((Button) cancelableDialog.findViewById(R.id.ButtonCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancelableDialog.cancel();
            }
        });
        cancelableDialog.setCancelable(true);
        GraphicUtils.animateDialog(cancelableDialog);
        cancelableDialog.show();
    }

    /* access modifiers changed from: private */
    public void aboutClickAction() {
        final CancelableDialog cancelableDialog = new CancelableDialog(this, R.style.Theme_Dialog, R.id.dialog_info_main);
        Window window = cancelableDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.dimAmount = 0.6f;
            window.setAttributes(attributes);
            window.addFlags(2);
        }
        cancelableDialog.setContentView(R.layout.dialog_info);
        ((TextView) cancelableDialog.findViewById(R.id.TextView01)).setText(getResources().getString(R.string.info_message));
        ((Button) cancelableDialog.findViewById(R.id.ButtonCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancelableDialog.cancel();
            }
        });
        cancelableDialog.setCancelable(true);
        GraphicUtils.animateDialog(cancelableDialog);
        cancelableDialog.show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
