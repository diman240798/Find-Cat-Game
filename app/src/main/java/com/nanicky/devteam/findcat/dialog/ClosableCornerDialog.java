package com.nanicky.devteam.findcat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nanicky.devteam.findcat.utils.GraphicUtils;

import srsdt1.findacat.R;

public class ClosableCornerDialog extends Dialog {
    public ClosableCornerDialog(@NonNull Context context) {
        super(context, R.style.Theme_Dialog);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.dimAmount = 0.6f;
        getWindow().setAttributes(attributes);
        getWindow().addFlags(2);
        setContentView(R.layout.corner_closable_dialog);
        findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClosableCornerDialog.this.cancel();
            }
        });
        GraphicUtils.animateDialog(this);
    }

    public void setText(int i) {
        setText(getContext().getString(i));
    }

    public void setText(String str) {
        ((TextView) findViewById(R.id.main_text)).setText(str);
    }
}