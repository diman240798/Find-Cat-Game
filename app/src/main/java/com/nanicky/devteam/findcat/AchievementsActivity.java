package com.nanicky.devteam.findcat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nanicky.devteam.findcat.dialog.CancelableDialog;
import com.nanicky.devteam.findcat.services.AchievementsService;
import com.nanicky.devteam.findcat.services.SettingsService;
import com.nanicky.devteam.findcat.utils.GraphicUtils;

import es.dmoral.toasty.Toasty;
import srsdt1.findacat.R;

public class AchievementsActivity extends Activity {
    private static final String TAG = "AchievementsActivity";
    int clickedRow = 0;
    private MediaPlayer mPlayer;
    private String[] pictures;
    private SettingsService settingsService;
    String textToShare = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(R.layout.achievements);
        String[] stringArray = getResources().getStringArray(R.array.achivki_names);
        String[] stringArray2 = getResources().getStringArray(R.array.achivki_opisanie);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_ach_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final boolean[] achievementsFlags = AchievementsService.getInstance().getAchievementsFlags();
        this.pictures = AchievementsService.getInstance().getAchievementsImagesNames();
        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(stringArray, stringArray2, this.pictures, achievementsFlags, this);
        recyclerView.setAdapter(achievementsAdapter);
        recyclerView.addItemDecoration(new AchievementsItemDecoration(dpToPix(2, this)));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View view, int i) {
                view.playSoundEffect(0);
                AchievementsActivity.this.clickedRow = i;
                if (achievementsFlags[i]) {
                    AchievementsActivity.this.textToShare = ((TextView) view.findViewById(R.id.tvText1)).getText().toString().replaceAll("\n", "");
                    AchievementsActivity.this.shareAndroidAchievement();
                    return;
                }
                                AchievementsActivity achievementsActivity = AchievementsActivity.this;
                Toasty.custom((Context) achievementsActivity, (CharSequence) achievementsActivity.getString(R.string.locked),
                        (int) R.drawable.toast_alert, Color.parseColor("#FFFFFF"), ContextCompat.getColor(AchievementsActivity.this, R.color.colorPrimaryDark), true, true).show();
            }
        }));
            }

    /* access modifiers changed from: private */
    public void shareAndroidAchievement() {
        if (Build.VERSION.SDK_INT < 23) {
            shareAction();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        } else {
            shareAction();
        }
    }

    private void shareAction() {
        Uri uri = null;
        try {
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(this.pictures[this.clickedRow] + "sh", "drawable", getPackageName())), null, null));
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении URI на картинку достижения: " + e.getLocalizedMessage());
        }
        if (uri != null) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", getFullShareText(true));
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, getString(R.string.share_achievement)));
        }
    }

    private String getFullShareText(boolean z) {
        String str = String.format(getString(R.string.share_achiv), new Object[]{this.textToShare}) + "\n" + getResources().getString(R.string.hashTag);
        if (!z) {
            return str;
        }
        return str + "\n" + "Add Url Here";
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 100) {
            int length = iArr.length;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                } else if (iArr[i2] == -1) {
                    final CancelableDialog cancelableDialog = new CancelableDialog(this, R.style.Theme_Dialog, R.id.dialog_memory_main);
                    Window window = cancelableDialog.getWindow();
                    if (window != null) {
                        WindowManager.LayoutParams attributes = window.getAttributes();
                        attributes.dimAmount = 0.6f;
                        window.setAttributes(attributes);
                        window.addFlags(2);
                    }
                    cancelableDialog.setContentView(R.layout.dialog_memory);
                    ((TextView) cancelableDialog.findViewById(R.id.TextView01)).setText(getResources().getString(R.string.permission_message));
                    ((Button) cancelableDialog.findViewById(R.id.ButtonCancel)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            cancelableDialog.cancel();
                        }
                    });
                    cancelableDialog.setCancelable(true);
                    GraphicUtils.animateDialog(cancelableDialog);
                    cancelableDialog.show();
                    z = true;
                } else {
                    i2++;
                }
            }
            if (!z) {
                shareAction();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public static int dpToPix(int i, Context context) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from_another_screen", false);
        startActivity(intent);
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
