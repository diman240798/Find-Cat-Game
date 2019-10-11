package com.nanicky.devteam.findcat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    GestureDetector gestureDetector;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (!(findChildViewUnder == null || this.listener == null || !this.gestureDetector.onTouchEvent(motionEvent))) {
            this.listener.onItemClick(findChildViewUnder, recyclerView.getChildAdapterPosition(findChildViewUnder));
        }
        return false;
    }
}
