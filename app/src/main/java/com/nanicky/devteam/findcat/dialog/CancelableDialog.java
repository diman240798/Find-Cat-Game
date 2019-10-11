package com.nanicky.devteam.findcat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class CancelableDialog extends Dialog {
    private int mainResourceId;
    private boolean realCancelable = true;

    public CancelableDialog(@NonNull Context context, @StyleRes int i, int i2) {
        super(context, i);
        this.mainResourceId = i2;
    }

    public void setRealCancelable(boolean z) {
        this.realCancelable = z;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            Window window = getWindow();
            if (window != null) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (isPointInsideView(x, y, window.getDecorView()) && !isPointInsideView(x, y, findViewById(this.mainResourceId)) && this.realCancelable) {
                    dismiss();
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean isPointInsideView(float f, float f2, View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        if (f <= ((float) i) || f >= ((float) (i + view.getWidth())) || f2 <= ((float) i2) || f2 >= ((float) (i2 + view.getHeight()))) {
            return false;
        }
        return true;
    }
}