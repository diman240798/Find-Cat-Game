package com.nanicky.devteam.findcat;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AchievementsItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public AchievementsItemDecoration(int i) {
        this.space = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i = this.space;
        rect.left = i;
        rect.right = i;
        rect.bottom = i;
        if (recyclerView.getChildAdapterPosition(view) == 0) {
            rect.top = this.space;
        }
    }
}
