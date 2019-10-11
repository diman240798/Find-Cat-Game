package com.nanicky.devteam.findcat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nanicky.devteam.findcat.services.SettingsService;

import java.util.Locale;

import srsdt1.findacat.R;

public class LanguageActivity extends AppCompatActivity {
    private SettingsService settingsService = SettingsService.getInstance();

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView((int) R.layout.language_activity);
        RadioButton radioButton = (RadioButton) findViewById(R.id.radioEng);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioRu);
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioDe);
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioFr);
        RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioZh);
        RadioButton radioButton6 = (RadioButton) findViewById(R.id.radioJa);
        RadioButton radioButton7 = (RadioButton) findViewById(R.id.radioKo);
        String languageSettingValue = this.settingsService.getLanguageSettingValue();
        if ("ru".equals(languageSettingValue) || "be".equals(languageSettingValue) || "ua".equals(languageSettingValue) || "kk".equals(languageSettingValue)) {
            radioButton2.setChecked(true);
        } else if ("de".equals(languageSettingValue)) {
            radioButton3.setChecked(true);
        } else if ("fr".equals(languageSettingValue)) {
            radioButton4.setChecked(true);
        } else if ("zh".equals(languageSettingValue)) {
            radioButton5.setChecked(true);
        } else if ("ja".equals(languageSettingValue)) {
            radioButton6.setChecked(true);
        } else if ("ko".equals(languageSettingValue)) {
            radioButton7.setChecked(true);
        } else {
            radioButton.setChecked(true);
        }
        ((RadioGroup) findViewById(R.id.langRg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioDe:
                        LanguageActivity.this.setLocale("de");
                        break;
                    case R.id.radioEng:
                        LanguageActivity.this.setLocale("en");
                        break;
                    case R.id.radioFr:
                        LanguageActivity.this.setLocale("fr");
                        break;
                    case R.id.radioJa:
                        LanguageActivity.this.setLocale("ja");
                        break;
                    case R.id.radioKo:
                        LanguageActivity.this.setLocale("ko");
                        break;
                    case R.id.radioRu:
                        LanguageActivity.this.setLocale("ru");
                        break;
                    case R.id.radioZh:
                        LanguageActivity.this.setLocale("zh");
                        break;
                }
                Intent launchIntentForPackage = LanguageActivity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(LanguageActivity.this.getBaseContext().getPackageName());
                if (launchIntentForPackage != null) {
                    launchIntentForPackage.addFlags(67108864);
                    LanguageActivity.this.startActivity(launchIntentForPackage);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void setLocale(String str) {
        this.settingsService.saveLanguage(str);
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}
